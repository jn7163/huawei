package com.imgraduate.top_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.imgraduate.activity.R;
import com.imgraduate.activity.YelloViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang on 2017/4/16.
 */

public class yellow_fragment extends Fragment {
    private View view = null;
    private GridView gridView;
    private Integer[] imgs = {
            R.drawable.huawei, R.drawable.ali, R.drawable.baidu,
            R.drawable.tengxun, R.drawable.jingdong, R.drawable.an360,
            R.drawable.zhilian, R.drawable.job51, R.drawable.zhonghua
    };
    private ArrayList<Map<String, Integer>> list;
    private String[] url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_yellow_fragment, null);
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        list = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++){
            Map<String, Integer> map = new HashMap<>();
            map.put("img", imgs[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.my_gridview_item,
                new String[]{"img"}, new int[]{R.id.image});
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "pic" + position, Toast.LENGTH_SHORT).show();
                url = new String[]{"http://www.huawei.com/cn/",
                        "https://job.alibaba.com/zhaopin/index.htm",
                        "http://talent.baidu.com/external/baidu/index.html",
                        "http://hr.tencent.com/",
                        "https://zhaopin.jd.com/JD/web/index",
                        "http://hr.360.cn/",
                        "http://ts.zhaopin.com/jump/index.html?sid=121113803&site=pzzhubiaoti1",
                        "http://www.51job.com/?from=baidupz",
                        "http://www.chinahr.com"};
                Intent intent = new Intent(getActivity(), YelloViewActivity.class);
                intent.putExtra("url", url[position]);
                startActivity(intent);
            }
        });

        return view;
    }

    class MyGridViewAdapter extends BaseAdapter{

        private Context context;

        public MyGridViewAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(175, 175));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8,8,8,8);
            }
            else{
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imgs[position]);
            return imageView;
        }
    }
}
