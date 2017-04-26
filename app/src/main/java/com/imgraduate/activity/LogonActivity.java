package com.imgraduate.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.imgraduate.db.MyDatabaseHelper;
import com.imgraduate.entity.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.imgraduate.fragment.OwnerFragment;

public class LogonActivity extends Activity{
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private Button logon;
	private EditText editText3;
	private EditText editText4;
	private EditText tv_confirmPw;
	private RadioButton rb_boy;
	private RadioButton rb_girl;
	private RadioButton rb_enterprise;
	private RadioButton rb_normal;
	private ArrayList<String> school_list;
	private ArrayList<String> province_list;
	private Map<String, ArrayList<String>> map;
	private Spinner spinner_province;
	private Spinner spinner_school;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logon_layout);

		findView();
		dbHelper = new MyDatabaseHelper(LogonActivity.this,getResources().getString(R.string.database_filename),null,1);
		database = dbHelper.getReadableDatabase();
		String sql = "select * from school";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		school_list = new ArrayList<>();
		province_list = new ArrayList<>();
		map = new HashMap<>();
		for (int i = 0; i < cursor.getCount() - 1; i++){
			String school = cursor.getString(cursor.getColumnIndex("school"));
			String province = cursor.getString(cursor.getColumnIndex("province"));
			if (!province_list.contains(province)){
				if (province_list.size() != 0){
					map.put(province_list.get(province_list.size()-1), school_list);
				}
				school_list = new ArrayList<>();
				province_list.add(province);
			}
			school_list.add(school);
			cursor.moveToNext();
		}
		map.put(province_list.get(province_list.size()-1), school_list);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, province_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_province.setAdapter(adapter);
		spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(LogonActivity.this, spinner_province.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(LogonActivity.this,
						android.R.layout.simple_spinner_item, map.get(province_list.get(position)));
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_school.setAdapter(adapter1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		logon = (Button) findViewById(R.id.logon);
		logon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!checkData()){
					Toast.makeText(LogonActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!editText4.getText().toString().equals(tv_confirmPw.getText().toString())){
					Toast.makeText(LogonActivity.this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
					return;
				}
				if (editText4.getText().toString().length() < 6){
					Toast.makeText(LogonActivity.this, "密码长度需大于6个字符", Toast.LENGTH_SHORT).show();
					return;
				}

				String sex = rb_boy.isChecked()? "男" : "女";
				String type = rb_enterprise.isChecked()? "企业" : "普通";
				String username = editText3.getText().toString();
				String password = editText4.getText().toString();
				String province = spinner_province.getSelectedItem().toString();
				String school = spinner_school.getSelectedItem().toString();

				if(register(username, password, sex, type, province, school)){
					Toast.makeText(LogonActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
					OwnerFragment.identity = new Identity();
					OwnerFragment.identity.setPassword(password);
					OwnerFragment.identity.setUsername(username);
					OwnerFragment.identity.setProvince(province);
					OwnerFragment.identity.setSchool(school);
					OwnerFragment.identity.setSex(sex);
					OwnerFragment.identity.setType(type);
//					FragmentManager manager = new OwnerFragment().getFragmentManager();
//					AfterLoginFragment afterLoginFragment = new AfterLoginFragment();
//					FragmentTransaction transaction = manager.beginTransaction();
//					transaction.replace(R.id.contentLayout, afterLoginFragment);
//					transaction.commit();
					finish();
				}
			}
		});

	}

	//向数据库插入数据
	public boolean register(String username,String password, String sex, String type, String province, String school){
		if (!CheckIsDataAlreadyInDBorNot(username)){
			Toast.makeText(LogonActivity.this, "此用户名已被注册", Toast.LENGTH_SHORT).show();
			return false;
		}
		database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username",username);
		values.put("password",password);
		values.put("sex", sex);
		values.put("type", type);
		values.put("province", province);
		values.put("school", school);
		database.insert("userdata",null,values);
		database.close();
		return true;
	}
	public boolean CheckIsDataAlreadyInDBorNot(String value){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		String Query = "Select * from userdata where username =?";
		Cursor cursor = db.rawQuery(Query,new String[] { value });
		if (cursor.getCount()>0){
			cursor.close();
			db.close();
			return false;
		}
		cursor.close();
		db.close();
		return true;
	}
	private void findView(){
		spinner_province = (Spinner) findViewById(R.id.spinner_province);
		spinner_school = (Spinner) findViewById(R.id.spinner_school);
		editText3 = (EditText)findViewById(R.id.username);
		editText4 = (EditText)findViewById(R.id.password);
		rb_boy = (RadioButton) findViewById(R.id.rb_boy);
		rb_girl = (RadioButton) findViewById(R.id.rb_girl);
		rb_enterprise = (RadioButton) findViewById(R.id.rb_enterprise);
		rb_normal = (RadioButton) findViewById(R.id.rb_normal);
		tv_confirmPw = (EditText) findViewById(R.id.textConfirmPw);
	}

	private boolean checkData(){
		if ((editText3.getText().equals("")) || (editText4.getText().equals(""))
				||((!rb_boy.isChecked()) && (!rb_girl.isChecked()))
				||((!rb_enterprise.isChecked()) && (!rb_normal.isChecked()))){
			return false;
		}
		return true;
	}

}
