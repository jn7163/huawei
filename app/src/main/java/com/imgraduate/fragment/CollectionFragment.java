package com.imgraduate.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imgraduate.activity.R;
import com.imgraduate.activity.WebviewActivity;
import com.imgraduate.adapter.RecyclerViewAdapter;
import com.imgraduate.db.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionFragment extends Fragment {

	private View view = null;
	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	private ArrayList<Map<String, String>> list;
	private MyDatabaseHelper dbHelper;

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null){
			view = inflater.inflate(R.layout.storefragment, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null){
			parent.removeView(view);
		}

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new RecyclerViewAdapter();
		list = adapter.getList();
		adapter.setFootViewText("已经到底啦");

		if (OwnerFragment.identity == null){
			adapter.setFootViewText("请先登录");
			recyclerView.setAdapter(adapter);
            return view;
        }
		dbHelper = new MyDatabaseHelper(getActivity(),getResources().getString(R.string.database_filename),null,1);
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from collection where username=?",
				new String[]{OwnerFragment.identity.getUsername()});
		cursor.moveToLast();
		for (int i = 0; i < cursor.getCount(); i++){
			Map<String, String> map = new HashMap<>();
			map.put("url", cursor.getString(cursor.getColumnIndex("url")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("detail", cursor.getString(cursor.getColumnIndex("detail")));
			list.add(map);
            cursor.moveToPrevious();
		}

		adapter.setOnItemClickListener(new RecyclerViewAdapter.onRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				Intent intent = new Intent(getActivity(), WebviewActivity.class);
				intent.putExtra("name", list.get(position).get("name"));
				intent.putExtra("detail", list.get(position).get("url"));
				intent.putExtra("url", list.get(position).get("url"));
				startActivity(intent);

			}
		});
		recyclerView.setAdapter(adapter);

		return view;
	}



}
