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
 * @Description ��������
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
	 * ��ʾ��ӭ����
	 */
	public void initView(){
		instance=this;
		
		//�õ�iamgeView�ؼ�
		imageView=(ImageView)this.findViewById(R.id.imageView);		
		animation=new AlphaAnimation(0.2f,1.0f); //ʵ����animation����
		animation.setDuration(1500);             //���ö�������ʱ��
		imageView.startAnimation(animation);     //������iamgeView�ؼ�������
		
		//����AnimationListener
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				//�����̣߳�ʵ��Runnable��run()����
				new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(3500);//��ӭ������3.5��
							Message msg=new Message();//����Message����
							logHandler.sendMessage(msg); //����Ϣ�ŵ���Ϣ������
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}).start();  //�����߳�
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

	//ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н���
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
			//����AlertDialog.Builder����
			new AlertDialog.Builder(WelcomeActivity.this)  
			.setTitle(R.string.authtips) //���ñ���
			.setMessage(R.string.authmessage) //������ʾ��Ϣ
			.setIcon(R.drawable.logo) //���ñ�������ʾ��ͼ��
			//���ȷ����ť���¼�
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
		//����"����"�������ó�����ȫ�˳�Ӧ��
		if(keyCode==4){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}

}
