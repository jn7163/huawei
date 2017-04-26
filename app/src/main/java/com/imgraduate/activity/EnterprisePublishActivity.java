package com.imgraduate.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.top_fragment.enterprise_fragment;

import java.util.HashMap;
import java.util.Map;

public class EnterprisePublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_publish);
        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ((TextView)findViewById(R.id.tv_title)).getText().toString();
                String content = ((TextView)findViewById(R.id.tv_content)).getText().toString();
                String url = ((TextView)findViewById(R.id.tv_url)).getText().toString();

                MyDatabaseHelper helper = new MyDatabaseHelper(EnterprisePublishActivity.this, getResources().getString(R.string.database_filename), null, 1);
                SQLiteDatabase database = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("detail", content);
                values.put("url", url);
                database.insert("enterprise", null, values);
                database.close();
                Toast.makeText(EnterprisePublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", title);
                map.put("detail", content);
                map.put("url", url);
                enterprise_fragment.list.add(0, map);
                enterprise_fragment.adapter.notifyDataSetChanged();
                finish();
            }
        });
    }
}
