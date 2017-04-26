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


public class RecyclerViewAdapterWithHeader extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 0;
    private final int HEAD_TYPE = 1;
    private ArrayList<Map<String, String>> list;
    private onItemClickListener itemClickListener = null;
    public RecyclerViewAdapterWithHeader(){
        super();
        list = new ArrayList<>();
    }

    class MyViewHolderWithHeader extends RecyclerView.ViewHolder {

        private TextView tv_content;
        private TextView tv_datetime;
        private TextView tv_username;

        public MyViewHolderWithHeader(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_datetime = (TextView) itemView.findViewById(R.id.tv_datetime);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
        }

        public TextView getTv_content() {
            return tv_content;
        }

        public TextView getTv_datetime() {
            return tv_datetime;
        }

        public TextView getTv_username() {
            return tv_username;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE){
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_header, null);
            return new MyViewHolderWithHeader(mView);
        }
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_normal, null);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null){
                    itemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
        return new MyViewHolderWithHeader(mView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolderWithHeader viewHolder = (MyViewHolderWithHeader) holder;
        viewHolder.getTv_content().setText(list.get(position).get("content"));
        viewHolder.getTv_datetime().setText(list.get(position).get("datetime"));
        viewHolder.getTv_username().setText(Html.fromHtml(list.get(position).get("username")));
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println(getItemCount());
        if (position == 0){
            return HEAD_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Map<String, String>> getList() {
        return list;
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.itemClickListener = listener;
    }
    public  interface onItemClickListener {
        void onItemClick(View v, int position);
    }
}
