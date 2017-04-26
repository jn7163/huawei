package com.imgraduate.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imgraduate.adapter.RecyclerViewAdapterWithHeader;
import com.imgraduate.db.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class comment_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterWithHeader adapter;
    private ArrayList<Map<String, String>> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterWithHeader();
        list = adapter.getList();
        Map<String, String> map = new HashMap<>();
        map.put("content", getIntent().getStringExtra("name"));
        map.put("datetime", "");
        map.put("username", "");
        list.add(map);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(comment_Activity.this, getResources().getString(R.string.database_filename),null,1);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from ask where name=?",
                new String[]{getIntent().getStringExtra("name")});
        cursor.moveToLast();
        for (int i = 0; i < cursor.getCount(); i++){
            map = new HashMap<>();
            map.put("id", cursor.getString(cursor.getColumnIndex("id")));
            map.put("content", cursor.getString(cursor.getColumnIndex("content")));
            map.put("username", cursor.getString(cursor.getColumnIndex("username")));
            map.put("datetime", cursor.getString(cursor.getColumnIndex("datetime")));
            list.add(map);
            cursor.moveToPrevious();
        }
        cursor.close();
        database.close();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapterWithHeader.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(comment_Activity.this, answer_activity.class);
                intent.putExtra("id", list.get(position).get("id"));
                intent.putExtra("content", list.get(position).get("content"));
                intent.putExtra("username", list.get(position).get("username"));
                intent.putExtra("datetime", list.get(position).get("datetime"));
                startActivity(intent);
            }
        });
    }

}
