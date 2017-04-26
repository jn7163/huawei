package com.imgraduate.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.imgraduate.adapter.RecyclerViewAdapterWithHeader;
import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.fragment.OwnerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class answer_activity extends AppCompatActivity {

    private ArrayList<Map<String, String>> list;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterWithHeader adapter;
    private String id;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_activity);

        initView();

        findViewById(R.id.btn_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OwnerFragment.identity == null){
                    Toast.makeText(answer_activity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("content", ((TextView)findViewById(R.id.editText)).getText().toString());
                values.put("id", id);
                values.put("username", OwnerFragment.identity.getUsername());
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                database.insert("answer", null, values);
                database.close();
                ((TextView)findViewById(R.id.editText)).setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(answer_activity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                initView();
            }

        });
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterWithHeader();
        list = adapter.getList();

        Map<String, String> map = new HashMap<>();
        map.put("content",  getIntent().getStringExtra("content"));
        map.put("username", getIntent().getStringExtra("username"));
        map.put("datetime", getIntent().getStringExtra("datetime"));
        list.add(map);

        id = getIntent().getStringExtra("id");
        databaseHelper = new MyDatabaseHelper(this, getResources().getString(R.string.database_filename), null, 1);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from answer where id=?", new String[]{id});
        cursor.moveToLast();
        for (int i = 0; i < cursor.getCount(); i++){
            map = new HashMap<>();
            map.put("content",  cursor.getString(cursor.getColumnIndex("content")));
            map.put("username", cursor.getString(cursor.getColumnIndex("username")));
            map.put("datetime", cursor.getString(cursor.getColumnIndex("datetime")));
            list.add(map);
            cursor.moveToPrevious();
        }
        cursor.close();
        database.close();
        recyclerView.setAdapter(adapter);
    }
}
