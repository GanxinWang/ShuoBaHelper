package com.sbxzs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OauthSharePreference {
	private final static String SHAREPREFERENCE_NAME = "user_info";//共享文件名	
	
	/**该方法保存授权后返回的两个数据
	 * 
	 * @param context
	 * @param token
	 * @param expires
	 * @return
	 */
	public static boolean putValues(Context context,String token,String expires){
		
		//获取SharedPrefererces
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		//打开SharedPrefererces的编辑状态
		Editor editor = sharedPreferences.edit();
		//保存access_token
		editor.putString("access_token", token);
		//保存expires_in
		editor.putString("expires_in", expires);
		
		//commit()用于提交数据
		return editor.commit();
	}
	
	/**该方法得到access_token的值
	 * 
	 * @param context
	 * @return
	 */
	public static String getToken(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString("access_token", "");
	}
	
	/**该方法得到expires_in的值
	 * 
	 * @param context
	 * @return
	 */
	public static String getExpires(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString("expires_in", "");
	}
}
