package com.imgraduate.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.imgraduate.activity.MainActivity;
import com.imgraduate.activity.R;

import java.io.File;


/**
 * Created by zhang on 2017/4/24.
 */

public class AfterLoginFragment extends Fragment {
    private View view = null;
    private TextView tv_username;
    private TextView tv_province;
    private TextView tv_school;
    private TextView tv_sex;
    private TextView tv_type;
    private Button btn_logout;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.after_login_layout, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null){
            parent.removeView(view);
        }
        findView();
        initView();

        return view;
    }

    public void findView(){
        tv_province = (TextView) view.findViewById(R.id.province);
        tv_school = (TextView) view.findViewById(R.id.school);
        tv_sex = (TextView) view.findViewById(R.id.sex);
        tv_username = (TextView) view.findViewById(R.id.username);
        tv_type = (TextView) view.findViewById(R.id.type);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
    }

    public void initView(){
        tv_province.setText(OwnerFragment.identity.getProvince());
        tv_username.setText(OwnerFragment.identity.getUsername());
        tv_sex.setText(OwnerFragment.identity.getSex());
        tv_school.setText(OwnerFragment.identity.getSchool());
        tv_type.setText(OwnerFragment.identity.getType());

        Drawable drawable = (OwnerFragment.identity.getSex().equals("男"))?
                getResources().getDrawable(R.drawable.male) : getResources().getDrawable(R.drawable.female);
        drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_username.setCompoundDrawables(null, null, null, drawable);
//        tv_username.setCompoundDrawablePadding(10dp);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("你确定要退出登录吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OwnerFragment.identity = null;
                File file = new File(getActivity().getFilesDir().getAbsolutePath(), "UserInfo");
                if (file.isFile() && file.exists()){
                    file.delete();
                }
                ((MainActivity)getActivity()).updateNewsFragment();
                ((MainActivity)getActivity()).showOwnerFragment();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }
}
