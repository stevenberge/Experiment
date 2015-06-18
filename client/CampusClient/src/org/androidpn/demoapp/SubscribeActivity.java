package org.androidpn.demoapp;

import org.androidpn.client.Constants;
import org.androidpn.util.ActivityUtil;
import org.androidpn.util.GetPostUtil;
import org.androidpn.util.IsNetworkConn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View.OnClickListener;

public class SubscribeActivity extends Activity {
	private SharedPreferences originSharedPrefs;
	private CheckBox cb_all;
	private CheckBox cb_news;
	private CheckBox cb_hotVideo;
	private CheckBox cb_goodLife;
	private CheckBox cb_achievment;
	private Button btn_subsub;
	private ProgressDialog dialogProgress;
	private Thread submitThread;
	private Runnable submitRunnable;
	private Handler handler;
	
	private boolean isSubAll=true;
	private String SubAll="all";

	private String SubGoodLife="good-life"; 
	public String IdGoodLife = "20";
	private String SubAchievment="achievment"; 
	public String IdAchievment = "22";
	private String SubNews="news"; 
	public String IdNews = "30";
	private String SubHotVideo="hot-video"; 
	public String IdHotVideo = "21";
	private String userID="";
	private StringBuilder subs = new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe);
		//添加到activitylist里，方便最后统一退出
        ActivityUtil.getInstance().addActivity(this);
        
		Log.i("xiaobingo", "进入订阅界面");
		Bundle bundle = this.getIntent().getExtras();
		userID = bundle.getString("userID");
		
		cb_all = (CheckBox)findViewById(R.id.subAll);
		cb_news = (CheckBox)findViewById(R.id.subNews);
		cb_hotVideo = (CheckBox)findViewById(R.id.subHotVideo);
		cb_goodLife = (CheckBox)findViewById(R.id.subGoodLife);
		cb_achievment = (CheckBox)findViewById(R.id.subAchievment);
		btn_subsub = (Button)findViewById(R.id.btn_subsub);
		dialogProgress = new ProgressDialog(SubscribeActivity.this);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.arg1==0) { //取消
					submitThread.interrupt();
        			Toast.makeText(SubscribeActivity.this, "用户 "+userID+" 中止提交", Toast.LENGTH_SHORT).show();
				}else if (msg.arg1==1) { //成功
					dialogProgress.dismiss();
					Toast.makeText(SubscribeActivity.this, "用户 "+userID+" 提交成功", Toast.LENGTH_LONG).show();
				}else if (msg.arg1==2) { //失败
					dialogProgress.dismiss();
        			Toast.makeText(SubscribeActivity.this, "用户 "+userID+" 提交失败", Toast.LENGTH_LONG).show();
				}
			}			
		};
		
		submitRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int code=0;
				 String androidpnURL = getString(R.string.androidpnserver);
	             // 用POST方式发送
	        		/*--拼接POST字符串--*/
	        		StringBuilder parameter = new StringBuilder();
	        		parameter.append("action=setSubs"); // 
	        		parameter.append("&username=");
	        		parameter.append(userID);
	        		parameter.append("&subs=");
	        		parameter.append(subs);
	        		/*--End--*/
	        		
	        		String resp = GetPostUtil.send("POST", androidpnURL + "subscriptions.do", parameter);
	        		Log.i("xiaobingo", "订阅响应："+resp);
	        		Message msg = handler.obtainMessage();	 
	        		if (resp.contains("succeed")) { //订阅成功
						code = 1;
						msg.arg1 = 1;
						msg.sendToTarget();
					}else { //订阅失败
						msg.arg1 = 2;
						msg.sendToTarget();
					}
	        		 SubscribeActivity.this.finish();
			}
		};
		
		//订阅所有
		cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isSubAll=isChecked;
				if (!isSubAll) {
					SubAll = null;
					cb_news.setChecked(false);
					cb_hotVideo.setChecked(false);
					cb_goodLife.setChecked(false);
					cb_achievment.setChecked(false);
				}
				else {
					SubAll="all";
					SubGoodLife="good-life";
					SubAchievment="achievment";
					SubNews="news";
					SubHotVideo="hot-video";
					cb_news.setChecked(true);
					cb_hotVideo.setChecked(true);
					cb_goodLife.setChecked(true);
					cb_achievment.setChecked(true);
				}
			}			
		});
		
		//订阅信息学院视频
		cb_goodLife.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					SubGoodLife= null;
					cb_all.setChecked(false);
				}
				else {
					SubGoodLife = "good-life";
				}
			}			
		});
		
		//订阅汇丰商学院视频
		cb_achievment.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					SubAchievment= null;
					cb_all.setChecked(false);
				}
				else {
					SubAchievment = "achievment";
				}
			}			
		});
		
	
		//订阅南燕要闻
		cb_news.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					SubNews= null;
					cb_all.setChecked(false);
				}
				else {
					SubNews = "news";
				}
			}			
		});
		
		//订阅通知公告
		cb_hotVideo.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					SubHotVideo= null;
					cb_all.setChecked(false);
				}
				else {
					SubHotVideo = "hot-video";
				}
			}			
		});
		
		//提交订阅或者取消订阅
		btn_subsub.setOnClickListener(new OnClickListener() { 			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//判断联网
        		IsNetworkConn isConn = new IsNetworkConn(SubscribeActivity.this);
        		if (!isConn.isConnected) {
        			Toast.makeText(SubscribeActivity.this, "未联网，请先联网~", Toast.LENGTH_LONG).show();
        			return;
				}
				subs = new StringBuilder();
				if(SubAll != null || SubNews != null){
					subs.append(IdNews+";");
				}
				if(SubAll != null || SubHotVideo != null){
					subs.append(IdHotVideo+";");
				}
				if(SubAll != null || SubGoodLife != null){
					subs.append(IdGoodLife+";");
				}
				if(SubAll != null || SubAchievment != null){
					subs.append(IdAchievment+";");
				}
					 
				Log.i("xiaobingo", "订阅的有："+subs.toString());
				submitThread = new Thread(submitRunnable);
				submitThread.start();
				//订阅改变了，修改本地订阅记录
				Editor editor = originSharedPrefs.edit();
				editor.putString(Constants.USER_SUBSCRIPTION, subs.toString());
				editor.commit(); //保存订阅
			}
		});
	}
	
	//在onresume中设置每一次恢复订阅activity时，都要判断加载用户已订阅的内容
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//先重置下
		cb_news.setChecked(false);
		cb_hotVideo.setChecked(false);
		cb_goodLife.setChecked(false);
		cb_achievment.setChecked(false);
 
		originSharedPrefs = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (originSharedPrefs.contains(Constants.USER_SUBSCRIPTION)) {
			String thisSubscriptions = originSharedPrefs.getString(Constants.USER_SUBSCRIPTION, null); //读取保存的用户订阅记录
			//String[] thisSubscription = thisSubscriptions.split("&&");
			if (thisSubscriptions.contains("all")) {
				cb_all.setChecked(true);
			}
			if (thisSubscriptions.contains("good-life")) {
				cb_goodLife.setChecked(true);
			}
			if (thisSubscriptions.contains("achievment")) {
				cb_achievment.setChecked(true);
			}
		 
			if (thisSubscriptions.contains("news")) {
				cb_news.setChecked(true);
			}
			if (thisSubscriptions.contains("hot-video")) {
				cb_hotVideo.setChecked(true);
			}
		}

	}
	
}
