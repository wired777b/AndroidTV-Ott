package com.hiveview.domybox.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.service.HiveViewService;
import com.hiveview.domybox.service.entity.SYSUpDataEntity;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.utils.AnimationUtil;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.MyFocusTextView;
/**
 * 账户设置页面的activity
 * @author jz
 *
 */
public class MySetInfoActivity extends BaseActivity {
	
	protected static final int NEW_VERSION = 3382;
	private RelativeLayout rl_set;
	private RelativeLayout set_user;
	private RelativeLayout set_update;
	private RelativeLayout set_aboutBox;
	private RelativeLayout set_net;
	private RelativeLayout set_weather;
	private SharedPreferences sp;
	private MyFocusTextView user_weather;
	private SYSUpDataEntity updateEntity;
	protected boolean isNewVersion = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)  {
			switch (msg.what) {
			case NEW_VERSION:
				isNewVersion  = true;
				newVersion();
				break;
			default:
				break;
			}
		}

		
	};
	//TextView
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_set_main);
		init();
	}
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		//检测服务器最新版本信息
		getVersionInfo();
	}
	/**
	 * 当有新版本的时候
	 * 更新textview的信息并添加点击事件
	 */
	private void newVersion() {
		TextView user_update1 = (TextView) set_update.findViewById(R.id.user_update1);
		user_update1.setText("新版本" + updateEntity.getVersion());
	};
	
	/**
	 * 焦点改变动画
	 */
	private void init() {
		sp = getSharedPreferences("weatherSet", Context.MODE_PRIVATE);
		initTypeface();
		rl_set = (RelativeLayout) findViewById(R.id.rl_set);
		set_user = (RelativeLayout) findViewById(R.id.set_user);
		set_update = (RelativeLayout) findViewById(R.id.set_update);
		set_aboutBox = (RelativeLayout) findViewById(R.id.set_aboutBox);
		set_net = (RelativeLayout) findViewById(R.id.set_net);
		set_weather = (RelativeLayout) findViewById(R.id.set_weather);
		user_weather = (MyFocusTextView) set_weather.findViewById(R.id.user_weather);
		user_weather.setStart(false);
		user_weather.setText(getWeatherInfo());
		getVersionInfo();
		set_user.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
				}else {
					AnimationUtil.getLittleAnimation(v);
				}
				rl_set.invalidate();
			}
		});
		
		set_update.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
				}else {
					AnimationUtil.getLittleAnimation(v);
				}
				rl_set.invalidate();
			}
		});
		
		set_aboutBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
				}else {
					AnimationUtil.getLittleAnimation(v);
				}
				rl_set.invalidate();
			}
		});
		
		set_weather.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					user_weather.setStart(true);
					user_weather.setText(getWeatherInfo());
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
				}else {
					user_weather.setStart(false);
					AnimationUtil.getLittleAnimation(v);
				}
				rl_set.invalidate();
			}
		});
		
		set_net.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
				}else {
					AnimationUtil.getLittleAnimation(v);
				}
				rl_set.invalidate();
			}
		});
		setNextFocus();
		set_user.requestFocus();
		setOnClickEvent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String info = getWeatherInfo();
		Log.i("msg","info = " + info);
		user_weather.setText(info);
	}
	
	/**
	 * 设置点击事件
	 */
	private void setOnClickEvent() {
		
		set_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtil.showMessageView(MySetInfoActivity.this, "  用户体系还未开放，敬请期待！ ", 3);
			}
		});
		
		set_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isNewVersion){
					
					final Dialog dialog = new Dialog(MySetInfoActivity.this, R.style.dialog); 
					
					View view = View.inflate(MySetInfoActivity.this, R.layout.dialog_layout_updata, null);
					TextView tvTitleName = (TextView) view.findViewById(R.id.tv_app_dialog_title_name);
					TextView tvContent = (TextView) view.findViewById(R.id.tv_app_dialog_content);

					TextView tv_dialog_cancel 	= (TextView) view.findViewById(R.id.tv_dialog_cancel);
					TextView tv_dialog_confirm 	= (TextView) view.findViewById(R.id.tv_dialog_confirm);
					
					tv_dialog_cancel.setText(R.string.later_updata);
					tv_dialog_confirm.setText(R.string.updata);
					
					tv_dialog_cancel.setTypeface(TypefaceUtils.getStandardfFontFace());
					tv_dialog_confirm.setTypeface(TypefaceUtils.getStandardfFontFace());
					tvTitleName.setTypeface(TypefaceUtils.getStandardfFontFace());
					tvContent.setTypeface(TypefaceUtils.getStandardfFontFace());
					
					LinearLayout ll_dialog_cancel	= (LinearLayout) view.findViewById(R.id.ll_dialog_cancel);
					LinearLayout ll_dialog_confirm 	= (LinearLayout) view.findViewById(R.id.ll_dialog_confirm);
					
					tvTitleName.setText("有新版本" + updateEntity.getVersion());
					tvContent.setText(updateEntity.getFeatures());
					
					ll_dialog_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					ll_dialog_confirm.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MySetInfoActivity.this,UpdateActivity.class);
							intent.putExtra("url", updateEntity.getUrl());
							intent.putExtra("md5", updateEntity.getMd5());
							intent.putExtra("version", updateEntity.getVersion());
							startActivity(intent);
						}
					});
					
					dialog.setContentView(view);
					dialog.show();
				}else{
					ToastUtil.showMessageView(MySetInfoActivity.this, "已经是最新版本", 1);
				}
			}
		});
		
		set_aboutBox.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(PengDoctorApplication.mContext,AboutBoxActivity.class);
				startActivity(intent);
			}
		});
		
		set_weather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PengDoctorApplication.mContext,MyWeatherActivity.class);
				startActivity(intent);
			}
		});
		
		set_net.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PengDoctorApplication.mContext,NetSetActivity.class);
				startActivity(intent);
				/*Intent mIntent = new Intent();

				ComponentName comp = new ComponentName("com.android.settings",
						"com.android.settings.Settings");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.VIEW");
				startActivity(mIntent);*/
			}
		});
		
	}
	/**
	 * 获取所有的Textview并初始化字体
	 */
	private void initTypeface() 
	{
		TextView set_head = (TextView) findViewById(R.id.set_head);
		TextView user_state = (TextView) findViewById(R.id.user_state);
		TextView user_update = (TextView) findViewById(R.id.user_update1);
		TextView user_box_name = (TextView) findViewById(R.id.user_box_name);
		TextView net_set = (TextView) findViewById(R.id.net_set);
		TextView user_weather = (TextView) findViewById(R.id.user_weather);
		set_head.setTypeface(TypefaceUtils.getStandardfFontFace());
		user_state.setTypeface(TypefaceUtils.getStandardfFontFace());
		user_update.setTypeface(TypefaceUtils.getStandardfFontFace());
		user_box_name.setTypeface(TypefaceUtils.getCE35FontFace());
		net_set.setTypeface(TypefaceUtils.getStandardfFontFace());
		user_weather.setTypeface(TypefaceUtils.getStandardfFontFace());
	}
	/**
	 * 设置焦点移动
	 */
	private void setNextFocus()
	{

		set_user.setNextFocusLeftId(View.NO_ID);
		set_user.setNextFocusUpId(View.NO_ID);
		set_user.setNextFocusDownId(View.NO_ID);
		set_user.setNextFocusRightId(R.id.set_update);
		
		set_update.setNextFocusLeftId(R.id.set_user);
		set_update.setNextFocusUpId(View.NO_ID);
		set_update.setNextFocusDownId(R.id.set_net);
		set_update.setNextFocusRightId(R.id.set_aboutBox);
		
		set_aboutBox.setNextFocusLeftId(R.id.set_update);
		set_aboutBox.setNextFocusUpId(View.NO_ID);
		set_aboutBox.setNextFocusDownId(R.id.set_weather);
		set_aboutBox.setNextFocusRightId(View.NO_ID);
		
		set_net.setNextFocusLeftId(R.id.set_user);
		set_net.setNextFocusUpId(R.id.set_update);
		set_net.setNextFocusDownId(View.NO_ID);
		set_net.setNextFocusRightId(R.id.set_weather);
		
		set_weather.setNextFocusLeftId(R.id.set_net);
		set_weather.setNextFocusUpId(R.id.set_aboutBox);
		set_weather.setNextFocusDownId(View.NO_ID);
		set_weather.setNextFocusRightId(View.NO_ID);
	}
	/**
	 * 跑马灯显示的天气数据
	 * @return
	 */
	private String getWeatherInfo()
	{
		String place = sp.getString("place", "");
		String temperature = sp.getString("temperature", "");
		String day = sp.getString("day", "");
		return place + " " + temperature + " " + day ;  
	}
	
	/**
	 * 获取系统版本信息
	 * @return
	 */
	private void getVersionInfo(){
		
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					updateEntity = new HiveViewService().getInfo( AppConstant.APP_VERSION);
					String newversioVersion =updateEntity.getVersion();
					
					PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
					String version = info.versionName;
					if(!version.equals(newversioVersion)){
						handler.sendEmptyMessage(NEW_VERSION);
					}
				} catch (ServiceException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
			
	}
	
	
}
