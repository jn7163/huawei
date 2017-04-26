package com.imgraduate.top_fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imgraduate.activity.EnterprisePublishActivity;
import com.imgraduate.activity.R;
import com.imgraduate.activity.WebviewActivity;
import com.imgraduate.adapter.RecyclerViewAdapter;
import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.fragment.OwnerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class enterprise_fragment extends Fragment {
    private View view = null;
    private RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    public static ArrayList<Map<String, String>> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enterprise_fragment, null);
        initView();
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        adapter.setFootViewText("已经到底啦！");
        list = adapter.getList();
        getData();
        adapter.setOnItemClickListener(new RecyclerViewAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra("url", list.get(position).get("url"));
                intent.putExtra("name", list.get(position).get("name"));
                intent.putExtra("detail", list.get(position).get("detail"));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        MyDatabaseHelper helper = new MyDatabaseHelper(getActivity(), getResources().getString(R.string.database_filename), null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from enterprise", null);
        cursor.moveToLast();
        for (int i = 0; i < cursor.getCount(); i++){
            Map<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(cursor.getColumnIndex("title")));
            map.put("detail", cursor.getString(cursor.getColumnIndex("detail")));
            map.put("url", cursor.getString(cursor.getColumnIndex("url")));
            list.add(map);
            cursor.moveToPrevious();
        }
        database.close();
    }
}
