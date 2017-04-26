package com.imgraduate.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.fragment.OwnerFragment;

public class QueryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        setTitle(title);

        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ((EditText)findViewById(R.id.tv_content)).getText().toString();
                String username = OwnerFragment.identity.getUsername();
                String name = getIntent().getStringExtra("name");
                String id = java.util.UUID.randomUUID().toString();
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(QueryActivity.this,getResources().getString(R.string.database_filename),null,1);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("content", content);
                values.put("id", id);
                values.put("name", name);
                values.put("username", username);
                database.insert("ask", null, values);
                database.close();
                Toast.makeText(QueryActivity.this, "提问成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
