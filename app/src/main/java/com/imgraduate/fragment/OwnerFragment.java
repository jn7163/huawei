package com.imgraduate.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imgraduate.activity.MainActivity;
import com.imgraduate.activity.LogonActivity;
import com.imgraduate.activity.R;
import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.entity.Identity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class OwnerFragment extends Fragment {

    private View view = null;
    private MyDatabaseHelper dbHelper;
    private EditText username;
    private EditText userpassword;
    private Button register;
    private Button login;

    private TextView tv_username;
    private TextView tv_province;
    private TextView tv_school;
    private TextView tv_sex;
    private TextView tv_type;

    public static Identity identity = null;
    private FragmentManager manager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment, null);
        register = (Button) view.findViewById(R.id.register);
        login = (Button) view.findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),LogonActivity.class), 0);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new MyDatabaseHelper(getActivity(), getResources().getString(R.string.database_filename), null, 1);
                username = (EditText)view.findViewById(R.id.username);
                userpassword = (EditText)view.findViewById(R.id.password);
                String userName=username.getText().toString();
                String passWord=userpassword.getText().toString();
                if(loginFun(userName,passWord)){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).showAfterLoginFragment();
                    try{
                        File file = new File(getActivity().getFilesDir().getPath(),"UserInfo");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write((userName + "##" + passWord).getBytes());
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    public boolean loginFun(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select* from userdata where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[] {username,password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            identity = new Identity();
            identity.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            identity.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            identity.setProvince(cursor.getString(cursor.getColumnIndex("province")));
            identity.setSchool(cursor.getString(cursor.getColumnIndex("school")));
            identity.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            identity.setType(cursor.getString(cursor.getColumnIndex("type")));

            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            ((MainActivity)getActivity()).showAfterLoginFragment();
        }
    }
}
