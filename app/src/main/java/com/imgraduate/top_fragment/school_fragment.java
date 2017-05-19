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
import com.imgraduate.fragment.OwnerFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
    private AsyncTask asyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_school_fragment, container, false);
            if (OwnerFragment.identity != null){
                initListView();
                loadedPages = 1;
                if (OwnerFragment.identity.getSchool().equals("西安电子科技大学")){
                    new myAsyncTaskXidian().execute(String.valueOf(loadedPages));
                }
                else if (OwnerFragment.identity.getSchool().equals("西北工业大学")){
                    new myAsyncTask().execute(String.valueOf(loadedPages));
                }
                else{
                    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new RecyclerViewAdapter();
                    adapter.setFootViewText("当前学校暂无数据！");
                    recyclerView.setAdapter(adapter);
                }
            }
            else{
                initUnLoginListView();
            }
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initUnLoginListView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        adapter.setFootViewText("请先登录");
        recyclerView.setAdapter(adapter);
    }

    private void initListView() {
        finishFlag = true;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        list = adapter.getList();
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                footerView.setVisibility(View.VISIBLE);
//            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    lastPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    if ((lastPosition >= recyclerView.getLayoutManager().getItemCount() - 5) && (finishFlag)) {
                        loadedPages++;
                        if (OwnerFragment.identity.getSchool().equals("西安电子科技大学")){
                            new myAsyncTaskXidian().execute(String.valueOf(loadedPages));
                        }
                        else if (OwnerFragment.identity.getSchool().equals("西北工业大学")){
                            new myAsyncTask().execute(String.valueOf(loadedPages));
                        }

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
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                finishFlag = false;
                Document document = Jsoup.parse(builder.toString());
                Elements divs = document.select("div");
                for (Element div : divs) {
                    if (div.attr("class").equals("col-md-12 ")) {
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
            if (values[0] == 0) {
                adapter.setFootViewText("网络连接超时");
            } else if (values[0] == 1) {
                adapter.setFootViewText("正在加载中");
            }
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            adapter.notifyDataSetChanged();
            finishFlag = true;
            if (!flag) {
                adapter.setFootViewText("加载失败");
            } else {
                adapter.setFootViewText("正在加载中");
            }
        }
    }

    private class myAsyncTaskXidian extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            try {
                publishProgress(1);
                URL url = new URL("http://job.xidian.edu.cn/html/zpxx/list_27_" + params[0] + ".html");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                finishFlag = false;
                Document document = Jsoup.parse(builder.toString());
                Elements divs = document.select("div");
                for (Element div : divs) {
                    if (div.attr("class").equals("arcList")) {
                        Elements aTags = div.select("a");
                        for (Element aTag : aTags) {
                            flag = true;
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("url", "http://job.xidian.edu.cn" + aTag.attr("href"));
                            map.put("name", aTag.html());
                            map.put("detail", "");
                            list.add(map);
                        }
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
            if (values[0] == 0) {
                adapter.setFootViewText("网络连接超时");
            } else if (values[0] == 1) {
                adapter.setFootViewText("正在加载中");
            }
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            adapter.notifyDataSetChanged();
            finishFlag = true;
            if (!flag) {
                adapter.setFootViewText("加载失败");
            } else {
                adapter.setFootViewText("正在加载中");
            }
        }
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }


        public static byte[] getUTF8BytesFromGBKString (String gbkStr){
            int n = gbkStr.length();
            byte[] utfBytes = new byte[3 * n];
            int k = 0;
            for (int i = 0; i < n; i++) {
                int m = gbkStr.charAt(i);
                if (m < 128 && m >= 0) {
                    utfBytes[k++] = (byte) m;
                    continue;
                }
                utfBytes[k++] = (byte) (0xe0 | (m >> 12));
                utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
                utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
            }
            if (k < utfBytes.length) {
                byte[] tmp = new byte[k];
                System.arraycopy(utfBytes, 0, tmp, 0, k);
                return tmp;
            }
            return utfBytes;
        }
}
