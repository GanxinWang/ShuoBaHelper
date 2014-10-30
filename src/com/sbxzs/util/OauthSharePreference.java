package com.sbxzs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OauthSharePreference {
	private final static String SHAREPREFERENCE_NAME = "user_info";//�����ļ���	
	
	/**�÷���������Ȩ�󷵻ص���������
	 * 
	 * @param context
	 * @param token
	 * @param expires
	 * @return
	 */
	public static boolean putValues(Context context,String token,String expires){
		
		//��ȡSharedPrefererces
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		//��SharedPrefererces�ı༭״̬
		Editor editor = sharedPreferences.edit();
		//����access_token
		editor.putString("access_token", token);
		//����expires_in
		editor.putString("expires_in", expires);
		
		//commit()�����ύ����
		return editor.commit();
	}
	
	/**�÷����õ�access_token��ֵ
	 * 
	 * @param context
	 * @return
	 */
	public static String getToken(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString("access_token", "");
	}
	
	/**�÷����õ�expires_in��ֵ
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
