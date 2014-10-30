package com.sbxzs.view;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.sbxzs.util.AccessTokenKeeper;
import com.sbxzs.util.ImgToText;
import com.sbxzs.util.JsonSex;
import com.sbxzs.util.NetworkState;
import com.touch.sbxzs.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @Description 发布微博界面
 * 
 */
public class PlayActivity extends Activity implements OnClickListener, TextWatcher, RequestListener{

	public EditText editText;
	public Button sendBtn,expressionBtn;
	public TextView textView;
	public LinearLayout total;
	public GridView gridview;
	public StatusesAPI api;
	public ProgressDialog progressDialog = null;
	public static Oauth2AccessToken myaccessToken;
	private static Boolean isExit = false; 
	private static Boolean hasTask = false;
	public static long uid=0;
	public static String sexMessage="『未知』";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_play);
		initView();		
	}

	public void initView(){
		editText=(EditText)findViewById(R.id.eidtText);
		editText.addTextChangedListener(this);
		
		textView=(TextView)findViewById(R.id.text_limit);
		
		sendBtn=(Button)findViewById(R.id.send);
		sendBtn.setOnClickListener(this);
		
		total=(LinearLayout)findViewById(R.id.ll_text_limit_unit);
		total.setOnClickListener(this);
		
		expressionBtn=(Button)findViewById(R.id.expression);
		expressionBtn.setOnClickListener(this);
		
		gridview=(GridView)findViewById(R.id.gridview);
		
		//内置华广说吧的AccessToken
		PlayActivity.myaccessToken=new Oauth2AccessToken("2.00kcGKeCJbesgBb68d6813ef0dwBxG", "157679999");	
		
        //关闭第一个WelcomeAvtivity
    	WelcomeActivity.instance.finish();
		AccountAPI account=new AccountAPI(WelcomeActivity.accessToken);
		    account.getUid(new RequestListener (){
				@Override
				public void onComplete(String arg0) {
					// TODO Auto-generated method stub
				    System.out.println("uid--->"+arg0);
                    String s=arg0.substring(7, arg0.length()-1);
                    
                    uid= Long.parseLong(s);                  
                    
                    UsersAPI userApi=new UsersAPI(WelcomeActivity.accessToken);
                    userApi.show(uid, new RequestListener(){

						@Override
						public void onComplete(String arg0) {
							// TODO Auto-generated method stub
							sexMessage=JsonSex.getSex(arg0);
						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub
							System.out.println("WeiboException"+arg0.getMessage());
						}

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub
							System.out.println("IOException"+arg0.getMessage());
						}
                    	
                    });                                       
				}

				@Override
				public void onError(WeiboException arg0) {
					// TODO Auto-generated method stub
					 System.out.println("WeiboException"+arg0.getMessage());
				}

				@Override
				public void onIOException(IOException arg0) {
					// TODO Auto-generated method stub
					 System.out.println("IOException");
				}
		    	
		    });
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String textNum=editText.getText().toString().trim();
		int length=textNum.length();
		String str="";
		if(length<=140){
			length=140-length;
			str=String.valueOf(length);
			textView.setTextColor(Color.GRAY);
             if (!sendBtn.isEnabled())
            	 sendBtn.setEnabled(true);
		}
		else{
			length=length-140;
			str="-"+String.valueOf(length);
			textView.setTextColor(Color.RED);
             if (sendBtn.isEnabled())
            	 sendBtn.setEnabled(false);
		}
		textView.setText(str);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.send:
			readTime();
			break;
		case R.id.ll_text_limit_unit:
			limit();
			break;
		case R.id.expression:
			showExpression();
			break;
			default:
				break;
		}
	}
	
	/**
	 * 点击字数提示框的方法
	 */
	public void limit()
	{
		String text=editText.getText().toString().trim();
		if(text.length()>0){
		 Dialog dialog = new AlertDialog.Builder(this)
		 .setTitle(R.string.attention)
         .setMessage(R.string.delete_all)
         .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 editText.setText("");
             }
         }).setNegativeButton(R.string.cancel, null).create();
		 
		 dialog.show();
		}
	}
	
	
	/**
	 * 显示发送的进度条
	 */
	public void showProgressDialog()
	{
		if (progressDialog == null)
		{
			progressDialog = ProgressDialog.show(this, "发布微博", "Sending...");
		
		}
	}
	
	/**
	 * 销毁进度条
	 */
	public void dismissProgressDialog()
	{
		
		if (progressDialog != null)
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * 判断剩余发布时间
	 */
	public void readTime(){		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式			
		String oldstr=AccessTokenKeeper.getDateTime(PlayActivity.this);;
		String newstr= df.format(new Date());
		
		System.out.println("oldstr is---->"+oldstr);
		System.out.println("newstr is---->"+newstr);
		if(oldstr==null||oldstr.equals("")){
		     send();
		}
		else{
			  long time=0;
		    try {
			    Date oldtime=df.parse(oldstr);
			    Date newtime=df.parse(newstr);
			    time=(newtime.getTime()-oldtime.getTime())/1000;
			
			    System.out.println("mm is---->"+time);
		       } catch (ParseException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		       }
		    
		    if(time>600){
		    	send();
		    }
		    else{ 	
		    	Toast.makeText(this, (600-time)+"秒后可再次发布", Toast.LENGTH_SHORT).show();
		    }
		}
	}
	
	
	/**
	 * 发送微博执行的方法
	 */
	public void send()
	{		
		String text=editText.getText().toString().trim();
		String sumLength=text+sexMessage;
		 if (text.length() == 0)
		 {
			 Toast.makeText(this, "发表的内容不能为空", Toast.LENGTH_SHORT).show();
			 return ;
		 }
         if(sumLength.length()>140){			 
			 String finalySend=text.substring(0, text.length()-sexMessage.length());
			        finalySend=finalySend+sexMessage;
			 api=new StatusesAPI(PlayActivity.myaccessToken);
			 api.update(finalySend,"00.00", "00.00", this);
			 showProgressDialog();
			 
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				AccessTokenKeeper.saveDateTime(PlayActivity.this, df.format(new Date()));// 保存当前系统时间
		 }
		 else
		 {
			 api=new StatusesAPI(PlayActivity.myaccessToken);
			 api.update(text+sexMessage,"00.00", "00.00", this);
			 showProgressDialog();
			 
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				AccessTokenKeeper.saveDateTime(PlayActivity.this, df.format(new Date()));// 保存当前系统时间
		 }                
	}
	

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {            
                Toast.makeText(PlayActivity.this,"发送成功", Toast.LENGTH_SHORT).show();             
                dismissProgressDialog();       
            }
        });
	}

	@Override
	public void onError(WeiboException arg0) {
		// TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {            
                Toast.makeText(PlayActivity.this,"WeiboException :发送失败", Toast.LENGTH_LONG).show();             
                dismissProgressDialog();
            }
        });
        System.out.println("exception--->"+arg0.getMessage());
	}

	@Override
	public void onIOException(IOException arg0) {
		// TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {            
                Toast.makeText(PlayActivity.this,"IOException :发送失败", Toast.LENGTH_LONG).show();             
                dismissProgressDialog();       
            }
        });
	}

    /**
     * 显示表情框
     */
    public void showExpression(){
    	if(gridview.getVisibility()==View.GONE){
    		System.out.println("---->gridview");
    		gridview.setVisibility(View.VISIBLE);
    		 ArrayList<HashMap<String,Object>> imagelist = new ArrayList<HashMap<String,Object>>();
    		
    		 for(int i=1;i<=77;i++){
    			 HashMap<String, Object> map = new HashMap<String, Object>();  
    		    try {
				     Field f = (Field)R.drawable.class.getDeclaredField("img"+i);  
				     int j = f.getInt(R.drawable.class);
				     map.put("ItemImage", j);//添加图像资源的ID
				     imagelist.add(map);
			    } catch (IllegalArgumentException e) {
				     // TODO Auto-generated catch block
				     e.printStackTrace();
			    } catch (NoSuchFieldException e) {
				     // TODO Auto-generated catch block
				     e.printStackTrace();
			    } catch (IllegalAccessException e) {
				     // TODO Auto-generated catch block
				     e.printStackTrace();
			    } 
    		 }
    		 
    		//生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
    		 SimpleAdapter simpleAdapter = new SimpleAdapter(this, imagelist, R.layout.gridview_item, new String[] {"ItemImage"}, new int[]{R.id.gridview_item});
    		 gridview.setAdapter(simpleAdapter);
    		 gridview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					System.out.println("点击的位置是-------->"+arg2);
					ImgToText toText=new ImgToText(arg2);
				     int cursor =editText.getSelectionStart();  
				     editText.getText().insert(cursor,toText.getImgText()+""); 
				     gridview.setVisibility(View.GONE);
				}
    			 
    		 });
    	}
    	else{
    		gridview.setVisibility(View.GONE);
    	}
    	
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//更改账号菜单项
		MenuItem changeItem=menu.add(Menu.NONE, 0, 0, R.string.change);
		changeItem.setIcon(R.drawable.change);
		
		//关于菜单项
		MenuItem aboutItem=menu.add(Menu.NONE, 1, 1, R.string.about);
		aboutItem.setIcon(R.drawable.about);
		return super.onCreateOptionsMenu(menu);
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 0:
			//点击了更改帐号菜单项
			clearExit();
			break;
		case 1:
			//点击了关于菜单项
			about();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 清除用户数据并退出应用程序
	 */
	public void clearExit(){
		new AlertDialog.Builder(PlayActivity.this)  
		.setTitle(R.string.changetips) //设置标题
		.setMessage(R.string.changemessage) //设置显示信息
		.setIcon(R.drawable.question) //设置标题栏显示的图标
		//添加确定按钮及事件
		.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				   AccessTokenKeeper.clear(PlayActivity.this);
		           finish();    
                   System.exit(0); 					
			}
		})
		//添加取消按钮及事件 
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
	}
	
	/**
	 * 显示"关于"界面
	 */
	public void about(){
		Dialog builder = new Dialog(PlayActivity.this);
		       builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		       builder.setContentView(R.layout.dialog_about);
		       builder.setTitle(R.string.about);
		       builder.show();
	}
	
	Timer tExit = new Timer();
	TimerTask task = new TimerTask(){             
        @Override    
        public void run() {    
           isExit = false;    
           hasTask = true;    
	    }    
    }; 
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 if(isExit == false ) { 
				  isExit = true;
				  Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				  
				  if(!hasTask){
					   tExit.schedule(task, 2000);    
				  }
			 }
			 else{
		           finish();    
                   System.exit(0); 
			 }
		 }		
		return false;
	}
}
