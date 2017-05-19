package com.imgraduate.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.entity.Identity;
import com.imgraduate.fragment.AfterLoginFragment;
import com.imgraduate.fragment.CollectionFragment;
import com.imgraduate.fragment.NewsFragment;
import com.imgraduate.fragment.OwnerFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.imgraduate.fragment.OwnerFragment.identity;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationBar navigationBar;
    private NewsFragment newsFragment = null;
    private CollectionFragment collectionFragment = null;
    private OwnerFragment ownerFragment = null;
    private AfterLoginFragment afterLoginFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoLogin(this);
        navigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        navigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        navigationBar.addItem(new BottomNavigationItem(R.drawable.foot_news_light, "招聘信息").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.foot_store_light, "收藏").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.foot_me_light, "我的").setActiveColorResource(R.color.cadetblue))
                .setFirstSelectedPosition(0).initialise();

        setDefaultFragment();

        navigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                hideFragments(transaction);
                switch (position){
                    case 0:
                        if (newsFragment == null){
                            newsFragment = new NewsFragment();
                            transaction.add(R.id.mContent, newsFragment);
                        }
                        else{
                            transaction.show(newsFragment);
                        }
                        break;
                    case 1:
                        if (collectionFragment != null){
                            transaction.remove(collectionFragment);
                        }
                        collectionFragment = new CollectionFragment();
                        transaction.add(R.id.mContent, collectionFragment);
                        break;
                    case 2:
                        if (identity == null){
                            if (ownerFragment == null){
                                ownerFragment = new OwnerFragment();
                                transaction.add(R.id.mContent, ownerFragment);
                            }
                            else{
                                transaction.show(ownerFragment);
                            }
                        }
                        else{
                            if (afterLoginFragment == null){
                                afterLoginFragment = new AfterLoginFragment();
                                transaction.add(R.id.mContent, afterLoginFragment);
                            }
                            else{
                                transaction.show(afterLoginFragment);
                            }
                        }
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void autoLogin(Context context){
        if (identity != null){
            return;
        }
        try {
            File file = new File(context.getFilesDir().getAbsolutePath(), "UserInfo");
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String content = reader.readLine();
            String[] contents = content.split("##");
            MyDatabaseHelper helper = new MyDatabaseHelper(MainActivity.this, getResources().getString(R.string.database_filename), null, 1);
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from userdata where username=? and password=?", contents);
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                identity = new Identity();
                identity.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                identity.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                identity.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                identity.setSchool(cursor.getString(cursor.getColumnIndex("school")));
                identity.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                identity.setType(cursor.getString(cursor.getColumnIndex("type")));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        newsFragment = new NewsFragment();
        transaction.add(R.id.mContent, newsFragment);
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (collectionFragment != null) {
            transaction.hide(collectionFragment);
        }
        if (ownerFragment != null) {
            transaction.hide(ownerFragment);
        }
        if (afterLoginFragment != null){
            transaction.hide(afterLoginFragment);
        }
    }
    public void showAfterLoginFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (afterLoginFragment != null){
            transaction.remove(afterLoginFragment);
        }
        afterLoginFragment = new AfterLoginFragment();
        transaction.add(R.id.mContent, afterLoginFragment);
        transaction.commit();
    }

    public void showOwnerFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (ownerFragment == null){
            ownerFragment = new OwnerFragment();
            transaction.add(R.id.mContent, ownerFragment);
        }
        else{
            transaction.show(ownerFragment);
        }
        transaction.commit();
    }

    public void updateNewsFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (newsFragment != null){
            transaction.remove(newsFragment);
        }
        newsFragment = new NewsFragment();
        transaction.add(R.id.mContent, newsFragment);

        transaction.commit();
    }
}
