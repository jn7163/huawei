package com.imgraduate.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.fragment.OwnerFragment;

/**
 * Created by zhang on 2017/4/17.
 */

public class WebviewActivity extends Activity {
    private boolean isCollected = false;
    private MyDatabaseHelper dbHelper;
    private String url;
    private String name;
    private String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        detail = intent.getStringExtra("detail");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        Log.e("UserAgent:", webView.getSettings().getUserAgentString());
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        webView.loadUrl(url);

        if (OwnerFragment.identity != null){
            dbHelper = new MyDatabaseHelper(this, getResources().getString(R.string.database_filename), null, 1);
            final SQLiteDatabase database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from collection where name=? and username=?",
                    new String[]{name, OwnerFragment.identity.getUsername()});
            if (cursor.getCount() != 0){
                findViewById(R.id.btnStore).setBackgroundResource(R.drawable.love_red);
                isCollected = true;
            }
            else{
                findViewById(R.id.btnStore).setBackgroundResource(R.drawable.love_light);
                isCollected = false;
            }
            cursor.close();
            database.close();
        }

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OwnerFragment.identity == null){
                    Toast.makeText(WebviewActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 = new Intent(WebviewActivity.this, QueryActivity.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });

        findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(WebviewActivity.this, comment_Activity.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });


        findViewById(R.id.btnStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OwnerFragment.identity == null){
                    Toast.makeText(WebviewActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isCollected){
                    SQLiteDatabase database1 = dbHelper.getWritableDatabase();
                    database1.delete("collection", "name=? and username=?",
                            new String[]{name, OwnerFragment.identity.getUsername()});
                    isCollected = false;
                    findViewById(R.id.btnStore).setBackgroundResource(R.drawable.love_light);
                    Toast.makeText(WebviewActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }
                else {
                    SQLiteDatabase database1 = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("url", url);
                    values.put("username", OwnerFragment.identity.getUsername());
                    values.put("detail", detail);
                    database1.insert("collection", null, values);
                    isCollected = true;
                    findViewById(R.id.btnStore).setBackgroundResource(R.drawable.love_red);
                    Toast.makeText(WebviewActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
