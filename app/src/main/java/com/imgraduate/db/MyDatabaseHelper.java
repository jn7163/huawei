package com.imgraduate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	public static final String CREATE_BOOK ="create table userData(" 
			+"id integer primary key autoincrement,"
            +"nametext text not null,"
            +"password text not null)";
	
	private Context mContext;

	//用户数据库文件的版本
	private static final int DB_VERSION    = 1;
	//数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名
	private static String DB_PATH        ="/data/data/com.example.huawei/databases/";
	private String dbpath;

	//下面两个静态变量分别是目标文件的名称和在assets文件夹下的文件名
	private static String DB_NAME         = "huawei.db";
	private static String ASSETS_NAME     = "huawei.db";


	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

//		db.execSQL(CREATE_BOOK);
		//db.execSQL("insert into userDate(nametext,password) values('14','14')");
		//�����ɹ�
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
//		db.execSQL("drop table if exists logininf");
//        onCreate(db);
	}

	public void createDataBase() throws IOException {
		dbpath = mContext.getFilesDir().getPath().split("files")[0] + "databases/";
		boolean dbExist = checkDataBase();
		if(dbExist)
		{
			//数据库已存在，不做任何操作
		}
		else
		{
			//创建数据库
			try {
				File dir = new File(dbpath);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File dbf = new File(dbpath + DB_NAME);
				if(dbf.exists()){
					dbf.delete();
				}
				SQLiteDatabase.openOrCreateDatabase(dbf, null);
				// 复制asseets中的数据库文件到DB_PATH下
				copyDataBase();
			} catch (IOException e) {
				throw new Error("数据库创建失败");
			}
		}
	}

	//检查数据库是否有效
	private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;
		String myPath = dbpath + DB_NAME;
		try{
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
			//database does't exist yet.
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * 复制assets文件中的数据库到指定路径
	 * 使用输入输出流进行复制
	 **/
	private void copyDataBase() throws IOException{

		InputStream myInput = mContext.getAssets().open(ASSETS_NAME);
		String outFileName = dbpath + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

}
