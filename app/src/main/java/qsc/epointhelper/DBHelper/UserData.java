package qsc.epointhelper.DBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserData {

	private Activity activity;
	public UserData(Activity activity)
	{
		this.activity = activity;
	}
	
	/**
	 * 读取数据
	 * @param Key
	 * @return
	 */
	public String getData(String Key) {
		// 获取SharedPreferences对象
		Context ctx = activity;
		SharedPreferences sp = ctx.getSharedPreferences("UserData",
				Context.MODE_MULTI_PROCESS);
		// 读取数据
		return sp.getString(Key, "none");
	}

	/**
	 * 保存数据
	 * @param Key
	 * @param Value
	 */
	public void SaveData(String Key, String Value) {
		// 获取SharedPreferences对象
		Context ctx = activity;
		SharedPreferences sp = ctx.getSharedPreferences("UserData",
				Context.MODE_PRIVATE);
		// 存入数据
		Editor editor = sp.edit();
		editor.putString(Key, Value);
		editor.commit();
	}

}
