package com.sbxzs.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 
 * @Description 网络状态判断工具类
 * 
 */
public class NetworkState {
	
	/**
	 * 判断网络状态并返回Toast信息
	 * @param context 上下文
	 */
	public static void netWorkMessage(Context context){
	    boolean flag=isNetworkAvailable(context);
	    if(flag==true){
		   Toast.makeText(context,"已连接", Toast.LENGTH_SHORT)
		   .show();
	    }
	    else{
		   Toast.makeText(context,"连接失败", Toast.LENGTH_SHORT)
		   .show();
	    }
	}
	/**
	 * 判断网络状态
	 * @param context  上下文
	 * @return  true表示有网络,false表示无网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		//获得网络状态管理器
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
