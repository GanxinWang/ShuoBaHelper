package com.sbxzs.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sbxzs.view.PlayActivity;
import com.sbxzs.view.WelcomeActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class WeiboWebview {
	public static final String CONSUMER_KEY = "1549764719";
	public static final String REDIRECT_URL = "http://weibo.com/226782998";
	public Weibo  myWeibo;
	public Context context;

	public void show(Context context){
		myWeibo=Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		myWeibo.authorize(context, new AuthDialogListener());
		this.context=context;
	}
	
	class AuthDialogListener implements WeiboAuthListener{

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Toast.makeText(context, "Auth cancel",Toast.LENGTH_LONG)
			.show();
		}

		@Override
		public void onComplete(Bundle e) {
			// TODO Auto-generated method stub
			String token = e.getString("access_token");
			String expires_in = e.getString("expires_in");
			WelcomeActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			AccessTokenKeeper.keepAccessToken(context, WelcomeActivity.accessToken);
			
			System.out.println("---ok!!!----"+token+"----"+expires_in+"----");
			
			Intent intent=new Intent();
			intent.setClass(context, PlayActivity.class);
			context.startActivity(intent);
		}

		@Override
		public void onError(WeiboDialogError e) {
			// TODO Auto-generated method stub
			Toast.makeText(context,"ß×?ÍøÂç³ö´íÁË,Çë¼ì²éÍøÂçÅäÖÃ!", Toast.LENGTH_LONG)
			.show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			Toast.makeText(context,"Auth exception : "+ e.getMessage(), Toast.LENGTH_LONG)
			.show();
		}
		
	}
}
