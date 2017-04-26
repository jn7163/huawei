package com.imgraduate.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imgraduate.activity.R;

import java.util.ArrayList;
import java.util.Map;


public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1;
    private String footViewText = "正在加载中...";
    private ArrayList<Map<String, String>> list;
    private onRecyclerViewItemClickListener itemClickListener = null;
    public RecyclerViewAdapter(){
        super();
        list = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView subTextView;
        private TextView footView;
        private View root;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_TYPE){
                subTextView = (TextView) itemView.findViewById(R.id.textView2);
                textView = (TextView) itemView.findViewById(R.id.textView1);
            } else if (viewType == FOOT_TYPE){
                footView = (TextView) itemView.findViewById(R.id.footView);
            }
        }

        public TextView getSubTextView() {
            return subTextView;
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getFootView() {
            return footView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_TYPE){
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_bottom_layout, parent, false), FOOT_TYPE);
        }
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item_2, parent, false);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null){
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return new MyViewHolder(mView, NORMAL_TYPE);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == NORMAL_TYPE){
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.getTextView().setText(list.get(position).get("name"));
            viewHolder.getSubTextView().setText(Html.fromHtml(list.get(position).get("detail")));
            viewHolder.itemView.setTag(position);
        }else if (getItemViewType(position) == FOOT_TYPE){
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.getFootView().setText(footViewText);
        }

    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener){
        this.itemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(getItemCount());
        if (position == getItemCount() - 1){
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public ArrayList<Map<String, String>> getList() {
        return list;
    }

    public void setFootViewText(String footViewText) {
        this.footViewText = footViewText;
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View v, int position);
    }

}
