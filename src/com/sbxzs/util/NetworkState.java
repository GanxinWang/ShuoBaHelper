package com.sbxzs.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 
 * @Description ����״̬�жϹ�����
 * 
 */
public class NetworkState {
	
	/**
	 * �ж�����״̬������Toast��Ϣ
	 * @param context ������
	 */
	public static void netWorkMessage(Context context){
	    boolean flag=isNetworkAvailable(context);
	    if(flag==true){
		   Toast.makeText(context,"������", Toast.LENGTH_SHORT)
		   .show();
	    }
	    else{
		   Toast.makeText(context,"����ʧ��", Toast.LENGTH_SHORT)
		   .show();
	    }
	}
	/**
	 * �ж�����״̬
	 * @param context  ������
	 * @return  true��ʾ������,false��ʾ������
	 */
	public static boolean isNetworkAvailable(Context context) {
		//�������״̬������
		ConnectivityManager connectivityManager=(ConnectivityManager )context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager==null){
			return false;
		}
		else{
			NetworkInfo [] info=connectivityManager.getAllNetworkInfo();					
			if(info !=null){
				for(NetworkInfo network : info){
					if(network.getState()==NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
}
