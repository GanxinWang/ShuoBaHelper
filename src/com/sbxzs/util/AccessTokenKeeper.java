package com.sbxzs.util;

import java.io.File;

import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class AccessTokenKeeper {
	private static final String PREFERENCES_NAME = "User_Info";
	/**
	 * 保存accesstoken到SharedPreferences
	 * @param context Activity 上下文环�?	 * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getToken());
		editor.putLong("expiresTime", token.getExpiresTime());
		editor.commit();
	}
	/**
	 * 清空sharepreference
	 * @param context
	 */
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
        /** 删除所有的数据 **/  
        File file1= new File("/data/data/com.touch.sbxzs/shared_prefs", PREFERENCES_NAME+".xml");
        File file2=new File("/data/data/com.touch.sbxzs/databases","webview.db");
        File file3=new File("/data/data/com.touch.sbxzs/databases","webview.db-journal");
        File file4=new File("/data/data/com.touch.sbxzs/databases","webviewCookiesChromium.db");
        File file5=new File("/data/data/com.touch.sbxzs/databases","webviewCookiesChromiumPrivate.db");
        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
        System.out.println("---->删除成功！！！");
	}	

	/**
	 * 从SharedPreferences读取accessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString("token", ""));
		token.setExpiresTime(pref.getLong("expiresTime", 0));
		return token;
	}
	
    /**
     * 保存系统日期和时间
     * @param context
     * @param s
     */
	public static void saveDateTime(Context context,String s){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("datetime", s);
		editor.commit();
	}
	
    /**
     *  取出系统日期和时间
     * @param context
     * @return
     */
	public static String getDateTime(Context context){
		String s="";
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		s=pref.getString("datetime", "");
		return s;
	}
}
