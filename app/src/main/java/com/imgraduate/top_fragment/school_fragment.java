package com.imgraduate.top_fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.imgraduate.activity.R;
import com.imgraduate.activity.WebviewActivity;
import com.imgraduate.adapter.RecyclerViewAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class school_fragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Map<String, String>> list;
    private RecyclerViewAdapter adapter;
    private View view;
    private boolean finishFlag;
    private int loadedPages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_school_fragment, container, false);
            initListView();
            loadedPages = 1;
            new myAsyncTask().execute(String.valueOf(loadedPages));
        }

        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initListView(){
        finishFlag = true;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        list = adapter.getList();
        adapter.setOnItemClickListener(new RecyclerViewAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),WebviewActivity.class);
                intent.putExtra("url", list.get(position).get("url"));
                intent.putExtra("name", list.get(position).get("name"));
                intent.putExtra("detail", list.get(position).get("detail"));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                footerView.setVisibility(View.VISIBLE);
//            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    lastPosition = ((LinearLayoutManager)manager).findLastVisibleItemPosition();
                    if ((lastPosition >= recyclerView.getLayoutManager().getItemCount() - 5) && (finishFlag)){
                        loadedPages++;
                        new myAsyncTask().execute(String.valueOf(loadedPages));

                    }
                }
            }
        });
    }

    private class myAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            try {
                publishProgress(1);
                URL url = new URL(getString(R.string.nwpuurl) + "?page=" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }
                finishFlag = false;
                Document document = Jsoup.parse(builder.toString());
                Elements divs = document.select("div");
                for (Element div : divs){
                    if (div.attr("class").equals("col-md-12 ")){
                        flag = true;
                        System.out.println(div.select("a").html());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("url", getString(R.string.baseURL) + div.select("a").attr("href"));
                        map.put("name", div.select("a").html());
                        map.put("detail", div.select("p").html());
                        list.add(map);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                publishProgress(0);
                e.printStackTrace();
            }
            return flag;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 0){
                adapter.setFootViewText("网络连接超时");
            }
            else if (values[0] == 1){
                adapter.setFootViewText("正在加载中");
            }
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            adapter.notifyDataSetChanged();
            finishFlag = true;
            if (!flag){
                adapter.setFootViewText("加载失败");
            }
            else{
                adapter.setFootViewText("正在加载中");
            }
        }
    }
}
