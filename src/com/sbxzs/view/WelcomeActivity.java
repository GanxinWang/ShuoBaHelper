package com.sbxzs.view;

import com.sbxzs.util.AccessTokenKeeper;
import com.sbxzs.util.WeiboWebview;
import com.touch.sbxzs.R;
import com.weibo.sdk.android.Oauth2AccessToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
/**
 * 
 * @Description 启动界面
 * 
 */
public class WelcomeActivity extends Activity {

	public ImageView imageView;
	public Animation animation=null;
	public static Oauth2AccessToken accessToken;
	public static Activity instance=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);				
		initView();
	}
	
	/**
	 * 显示欢迎界面
	 */
	public void initView(){
		instance=this;
		
		//得到iamgeView控件
		imageView=(ImageView)this.findViewById(R.id.imageView);		
		animation=new AlphaAnimation(0.2f,1.0f); //实例化animation对象
		animation.setDuration(1500);             //设置动画持续时间
		imageView.startAnimation(animation);     //关联到iamgeView控件并启动
		
		//设置AnimationListener
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				//创建线程，实现Runnable的run()方法
				new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(3500);//欢迎界面暂3.5秒
							Message msg=new Message();//创建Message对象
							logHandler.sendMessage(msg); //将消息放到消息队列中
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}).start();  //启动线程
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
	}

	//执行接收到的消息。执行的顺序是按照队列进行
	Handler logHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			initData();
		}
	};
	
	public void initData(){
		//String access_token=OauthSharePreference.getToken(this);
		WelcomeActivity.accessToken =AccessTokenKeeper.readAccessToken(this);
		if(WelcomeActivity.accessToken.isSessionValid()){
			System.out.println("------->first");
			Intent intent=new Intent();
			intent.setClass(WelcomeActivity.this, PlayActivity.class);
			startActivity(intent);
		}
		else{
			System.out.println("------->second");
			//创建AlertDialog.Builder对象
			new AlertDialog.Builder(WelcomeActivity.this)  
			.setTitle(R.string.authtips) //设置标题
			.setMessage(R.string.authmessage) //设置显示信息
			.setIcon(R.drawable.logo) //设置标题栏显示的图标
			//添加确定按钮及事件
			.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					System.out.println("------>in");
					WeiboWebview webview=new WeiboWebview();
					webview.show(WelcomeActivity.this);										
				}
			}).show();
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub		
		//按下"返回"按键，让程序完全退出应用
		if(keyCode==4){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}

}
