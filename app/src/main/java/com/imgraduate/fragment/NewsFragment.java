package com.imgraduate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

//import com.example.activity.Find_tab_Adapter;
import com.imgraduate.activity.*;
import com.imgraduate.activity.R;
import com.imgraduate.top_fragment.province_fragment;
import com.imgraduate.top_fragment.enterprise_fragment;
import com.imgraduate.top_fragment.school_fragment;
import com.imgraduate.top_fragment.yellow_fragment;

import java.util.ArrayList;

/**
 * Created by zhang on 2017/4/16.
 */

public class NewsFragment extends Fragment{

    private ViewPager myviewpager;
    private ArrayList<Fragment> fragments;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.newsfragment, null);
            initView();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }
    public void initView(){
        myviewpager = (ViewPager)view.findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new school_fragment());
        fragments.add(new province_fragment());
        fragments.add(new enterprise_fragment());
        fragments.add(new yellow_fragment());

        ArrayList<String> list_title = new ArrayList<>();
        list_title.add("本校");
        list_title.add("本省");
        list_title.add("企业");
        list_title.add("黄页");

        Find_tab_Adapter adapter = new Find_tab_Adapter(getChildFragmentManager(), fragments, list_title);
        myviewpager.setAdapter(adapter);
        myviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    showFloatingActionButton();
                }
                else {
                    hideFloatingActionButton();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(myviewpager);

        view.findViewById(R.id.btn_startPublishActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((OwnerFragment.identity == null) || (OwnerFragment.identity.getType().equals("普通"))){
                    Toast.makeText(getActivity(), "您不是企业用户或未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getActivity(), EnterprisePublishActivity.class));
            }
        });
    }

    private void showFloatingActionButton(){
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(200);
        view.findViewById(R.id.btn_startPublishActivity).startAnimation(mShowAction);
        view.findViewById(R.id.btn_startPublishActivity).setVisibility(View.VISIBLE);
    }

    private void hideFloatingActionButton(){
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.5f);
        mHiddenAction.setDuration(200);
        view.findViewById(R.id.btn_startPublishActivity).startAnimation(mHiddenAction);
        view.findViewById(R.id.btn_startPublishActivity).setVisibility(View.GONE);
    }
}