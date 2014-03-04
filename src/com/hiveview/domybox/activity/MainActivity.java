package com.hiveview.domybox.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.common.image.ImageRequest;
import com.hiveview.domybox.service.HiveViewService;
import com.hiveview.domybox.service.dao.CommonPreferenceDAO;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.entity.WeatherInfo;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.BaseHttpGetConnector;
import com.hiveview.domybox.service.net.HttpGetConnector;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.service.request.ApiConstant;
import com.hiveview.domybox.service.request.BaseGetRequest;
import com.hiveview.domybox.utils.AnimationUtil;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.FileUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.MD5Utils;
import com.hiveview.domybox.utils.Rotate3dAnimation;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.MyTextView;
import com.hiveview.domybox.view.SettingsItemView;
import com.hiveview.domybox.view.TimerView;
import com.hiveview.domybox.view.VideoItemView;
import com.hiveview.domybox.view.WeatherView;
import com.hiveview.domybox.view.WifiView;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends BaseActivity {

	protected static final int FINISH_WEATHER_DATA 	= 467;
	
	protected static final int MSG_ANIMATION_MOVIE 	= 440;
	protected static final int MSG_ANIMATION_TV 	= 441;
	protected static final int MSG_ANIMATION_HAPPY_ENJOY_ARE = 442;
	protected static final int MSG_ANIMATION_APP 		= 443;
	protected static final int MSG_ANIMATION_SETTING 	= 444;
	protected static final int MSG_ANIMATION_APP_PB 	= 445;
	
	private RelativeLayout ll_main_movie;
	private RelativeLayout ll_main_tv;
	private RelativeLayout ll_main_happy_enjoy_area;
	private RelativeLayout ll_main_application;
	private RelativeLayout ll_main_setting;
	
	private LinearLayout ll_clock_main_time;
	private LinearLayout ll_main_weather;
	private LinearLayout ll_main_wifi;
	
	private RelativeLayout mContainer;
	private TextView tvDate;
	private TextView tvWeek;
	private TimerView timeView;
	private WeatherInfo weatherInfoDay;
	private WeatherInfo weatherInfoNight;
	
	@SuppressWarnings("unused")
	private WifiView wifiView;
	private VideoItemView item_view_video;
	private VideoItemView item_view_tv;
	private SettingsItemView item_view_happy_share_disk;
	private SettingsItemView item_view_application;
	private SettingsItemView item_view_settings;
	private WeatherView my_weather;
	private SharedPreferences sp ;
	public  String TAG	=	"MainActivity";
	protected Bitmap bitmapWallPaper;
	private TextView tv_main_week;	//星期
	private TextView tv_main_date;	//
	protected static boolean isWallPagerChange=false;
	private ImageView iv_main_bg;	//背景图
	private AnimationSet animationOneSet 	= AnimationUtil.getAppAnimationOneSet();;
	private AnimationSet animationTwoSet 	= AnimationUtil.getAppAnimationTwoSet();;
	private AnimationSet animationThreeSet	= AnimationUtil.getAppAnimationThreeSet();;
	
	private AnimationSet tVAnimationSet;	//tv Anumation
	
//	private ImageView iv_item_video_icon;
	private ImageView iv_item_tv_play;
	private ImageView iv_item_happy_share_icon;
	private ImageView iv_app_animation_title;
	private ImageView iv_item_settiongs_icon;
	private ImageView iv_app_animation_pb_one;
	private ImageView iv_app_animation_pb_two;
	private ImageView iv_app_animation_pb_three;
	private ImageView iv_item_video_frame_demo;
	
	private ImageView iv_item_happy_share_icon_frame;
	
	private LinearLayout ll_main_date;

	private ImageView iv_main_welcom;

	private ImageView iv_main_logo;

	protected File fileApk;
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@SuppressLint("ServiceCast")
		public void handleMessage(android.os.Message msg) {
			int size = 0;
			switch (msg.what) {
			case FINISH_WEATHER_DATA:
				if(null != weatherInfoDay && null != weatherInfoNight) {
					updateData();
				}
				break;
				
			case AppConstant.MSG_WALLPAPER_CHANGE:
				setWallpaper();
				break;
				
			case MSG_ANIMATION_MOVIE:
//				item_view_video.startAnimal(R.drawable.animation_item_video);
				break;
			case MSG_ANIMATION_TV:
				tVAnimationSet.startNow();
				handler.sendEmptyMessageDelayed(MSG_ANIMATION_TV, 2500);
				break;
			case MSG_ANIMATION_HAPPY_ENJOY_ARE:
//				item_view_happy_share_disk.startAnimal(R.drawable.animation_item_happy_share_disk);
				break;
			case MSG_ANIMATION_APP:
				if(iv_app_animation_title.isShown()){
					iv_app_animation_title.setVisibility(View.GONE);
				}else {
					iv_app_animation_title.setVisibility(View.VISIBLE);
				}
				handler.sendEmptyMessageDelayed(MSG_ANIMATION_APP, 600);
				break;
				
			case MSG_ANIMATION_APP_PB:
				
				startAppAnimation();
				
				break;
			case MSG_ANIMATION_SETTING:
//				item_view_settings.startAnimal(R.drawable.animation_item_settings);
				break;
				
				
				
			//创建文件成功 返回文件大小
			case AppConstant.MSG_DIALOG_GETSIZE:
				size	=	msg.arg1;
				fileApk	=	(File) msg.obj;
				pbComponent.setMax(size);
				break;
				
				//下载文件进度
			case AppConstant.MSG_DIALOG_PROGRESS:
				int add	=	msg.arg1;
				pbComponent.incrementProgressBy(add);
				
				if(size==pbComponent.getProgress()){
					Logger.e(TAG, "下载完成");
					
					// 文件Md5校验
					String md5 = null;
					try {
						md5 = MD5Utils.getFileMD5String(fileApk);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(updataApk.getMd5().equals(md5)){
						//	移动至system/app
//						FileUtils.copyFileToSystemApp(fileApk);
					}
					
				}
				
				break;
				
			default:
				break;
			}
		};
	};

	private ProgressBar pbComponent;

	protected AppMarketEntity updataApk;

	private SharedPreferences spUpdata;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		setAction();
		
		//设置壁纸
		setWallpaper();
		
//		组件升级
		componentUpData();
		
		startAnimation();
		
	}

	

	private void setAction() {
//		mFilter = new IntentFilter();
//        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
	}

	//	通过imageUrl，保存至本地
	protected void getWallPaper(final String imageUrl) {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				BaseGetRequest request = new ImageRequest(imageUrl);
				BaseHttpGetConnector connector = new HttpGetConnector(request);
				InputStream in = connector.getGetResponse();
				Bitmap bitmapWallPaper = BitmapFactory.decodeStream(in);
				
				switch (weatherInfoDay.getType()) {
				case 1:
					PengDoctorApplication.wallpaperUrl=weatherInfoDay.getDayWallpaper();
					break;
				case 2:
					PengDoctorApplication.wallpaperUrl=weatherInfoDay.getNightWallpaper();
					break;
				default:
					break;
				}
				
				PengDoctorApplication.wallPager = bitmapWallPaper;
				handler.sendEmptyMessage(AppConstant.MSG_WALLPAPER_CHANGE);
			}
		});
		
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	private void init() {
		sp = getSharedPreferences("weatherSet", Context.MODE_PRIVATE);
		PengDoctorApplication.wallpaperUrl = sp.getString(AppConstant.SP_WALL_PAGER, "");
		
		// 初始化 sharedpreference 
		spUpdata = this.getSharedPreferences(AppConstant.SP_UPDATA, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
		
		
		//tv 正在升级的组件
		pbComponent 		= (ProgressBar)this.findViewById(R.id.pb_component);
		
		
		iv_item_video_frame_demo = (ImageView)this.findViewById(R.id.iv_item_video_frame_demo);
		
		
		
		iv_main_logo 	= (ImageView) findViewById(R.id.iv_main_logo);
		iv_main_welcom 	= (ImageView) findViewById(R.id.iv_main_welcom);
		ll_main_date 	= (LinearLayout) findViewById(R.id.ll_main_date);
		
		
		mContainer 	= (RelativeLayout)this.findViewById(R.id.layout_main_container);
		tv_main_week = (TextView) findViewById(R.id.tv_main_week);
		tv_main_date = (TextView) findViewById(R.id.tv_main_date);
		ll_main_movie 	= (RelativeLayout)this.findViewById(R.id.ll_main_movie);
		item_view_video = (VideoItemView)this.findViewById(R.id.item_view_video);
		
		item_view_video.tv_item_name.setText(R.string.string_main_video);
		
		ll_main_tv 		= (RelativeLayout)this.findViewById(R.id.ll_main_tv);
		item_view_tv = (VideoItemView)this.findViewById(R.id.item_view_tv);
		iv_item_tv_play = (ImageView)this.findViewById(R.id.iv_item_tv_play);//动画
		item_view_tv.rl_item_bg.setBackgroundResource(R.drawable.tv_bg);
		item_view_tv.tv_item_name.setText(R.string.string_main_tv);
		
		
		ll_main_happy_enjoy_area = (RelativeLayout)this.findViewById(R.id.ll_main_happy_enjoy_area);
		item_view_happy_share_disk = (SettingsItemView)this.findViewById(R.id.item_view_happy_share_disk);
		
		iv_item_happy_share_icon_frame = (ImageView)this.findViewById(R.id.iv_item_happy_share_icon_frame);//动画
		iv_item_happy_share_icon = (ImageView)this.findViewById(R.id.iv_item_happy_share_icon);//动画
		item_view_happy_share_disk.rl_item_bg.setBackgroundResource(R.drawable.happy_share_disk);
		item_view_happy_share_disk.tv_item_name.setText(R.string.string_main_happy_share_disk);
		
		
		ll_main_application 	= (RelativeLayout)this.findViewById(R.id.ll_main_application);
		item_view_application 	= (SettingsItemView)this.findViewById(R.id.item_view_application);
		iv_app_animation_title 	= (ImageView)this.findViewById(R.id.iv_app_animation_title);//动画
		iv_app_animation_pb_one 	= (ImageView)this.findViewById(R.id.iv_app_animation_pb_one);//动画
		iv_app_animation_pb_two 	= (ImageView)this.findViewById(R.id.iv_app_animation_pb_two);//动画
		iv_app_animation_pb_three 	= (ImageView)this.findViewById(R.id.iv_app_animation_pb_three);//动画
		item_view_application.rl_item_bg.setBackgroundResource(R.drawable.application_bg);
		item_view_application.tv_item_name.setText(R.string.string_main_app);
		
		
		ll_main_setting 	= (RelativeLayout)this.findViewById(R.id.ll_main_setting);
		item_view_settings 	= (SettingsItemView)this.findViewById(R.id.item_view_settings);
		iv_item_settiongs_icon = (ImageView)this.findViewById(R.id.iv_item_settiongs_icon);//动画
		item_view_settings.rl_item_bg.setBackgroundResource(R.drawable.setting_bg);
		item_view_settings.tv_item_name.setText(R.string.string_main_setting);
		
		ll_clock_main_time 		= (LinearLayout)this.findViewById(R.id.ll_clock_main_time);
		ll_main_weather		= (LinearLayout)this.findViewById(R.id.ll_main_weather);
		ll_main_wifi		= (LinearLayout)this.findViewById(R.id.ll_main_wifi);
		wifiView			= (WifiView)this.findViewById(R.id.view_main_wifi);
		
		iv_main_bg	= (ImageView)this.findViewById(R.id.iv_main_bg);
		
		
		tvDate		= (TextView)this.findViewById(R.id.tv_main_date);
		tvWeek		= (TextView)this.findViewById(R.id.tv_main_week);
		
		timeView    = (TimerView) this.findViewById(R.id.myTV_time);
		my_weather  = (WeatherView) this.findViewById(R.id.my_weather);
		ll_main_movie.setOnFocusChangeListener(new OnFocusChangeListener() {

			private AnimationDrawable mAnimationDrawable;

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("NewApi")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
					
					iv_item_video_frame_demo.setImageResource(R.drawable.animation_item_video);
					mAnimationDrawable = (AnimationDrawable) iv_item_video_frame_demo.getDrawable();
					mAnimationDrawable.start();
					
				}else {

					AnimationUtil.getLittleAnimation(v);
					
					mAnimationDrawable.stop();
					iv_item_video_frame_demo.setImageResource(R.drawable.animation_item_video_1);
					
				}
				mContainer.invalidate();
			}
		});
		
		ll_main_tv.setOnFocusChangeListener(new OnFocusChangeListener() {

			@SuppressLint("NewApi")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					v.startAnimation(AnimationUtil.getBigAnimation(MainActivity.this));
					tVAnimationSet = AnimationUtil.getTVAnimatorSet();
					iv_item_tv_play.startAnimation(tVAnimationSet);
					handler.sendEmptyMessageDelayed(MSG_ANIMATION_TV, 3000);
					
				}else {
					v.startAnimation(AnimationUtil.getLitterAnimation(MainActivity.this));
					handler.removeMessages(MSG_ANIMATION_TV);
					tVAnimationSet.cancel();
					iv_item_tv_play.clearAnimation();
				}
				mContainer.invalidate();
			}
		});
		
		ll_main_happy_enjoy_area.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					v.startAnimation(AnimationUtil.getBigAnimation(MainActivity.this));
					
					iv_item_happy_share_icon.startAnimation(AnimationUtil.getRotateAnimation());
					iv_item_happy_share_icon.bringToFront();
					iv_item_happy_share_icon_frame.bringToFront();
					
					
				}else {
					v.startAnimation(AnimationUtil.getLitterAnimation(MainActivity.this));
					iv_item_happy_share_icon.getAnimation().cancel();
				}
				mContainer.invalidate();
			}
		});
		
		ll_main_application.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					AnimationUtil.getBigAnimation(v);
					iv_app_animation_title.setVisibility(View.VISIBLE);
					handler.sendEmptyMessageDelayed(MSG_ANIMATION_APP, 600);
					startAppAnimation();
				}else {
					AnimationUtil.getLittleAnimation(v);
					handler.removeMessages(MSG_ANIMATION_APP);
					iv_app_animation_title.setVisibility(View.GONE);
					
					handler.removeMessages(MSG_ANIMATION_APP_PB);
					animationOneSet.cancel();
					animationTwoSet.cancel();
					animationThreeSet.cancel();
					
					iv_app_animation_pb_one.setVisibility(View.GONE);
					iv_app_animation_pb_two.setVisibility(View.GONE);
					iv_app_animation_pb_three.setVisibility(View.GONE);
				}
			}
		});
		
		ll_main_setting.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.bringToFront();
					v.startAnimation(AnimationUtil.getBigAnimation(MainActivity.this));
					iv_item_settiongs_icon.startAnimation(AnimationUtil.getRotateAnimation());
				}else {
					v.startAnimation(AnimationUtil.getLitterAnimation(MainActivity.this));
					iv_item_settiongs_icon.getAnimation().cancel();
				}
				mContainer.invalidate();
			}
		});
		
		ll_main_weather.setOnFocusChangeListener(new OnFocusChangeListener() {
			MyTextView tv = (MyTextView) findViewById(R.id.tv_weather_district);
			MyTextView tv_weather_wind = (MyTextView) findViewById(R.id.tv_weather_wind);
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					tv.setText(tv.getText());
					tv.setStart(true);
					tv_weather_wind.setText(tv_weather_wind.getText());
					tv_weather_wind.setStart(true);
				}else {
					tv.setText(tv.getText());
					tv.setStart(false);
					tv_weather_wind.setText(tv_weather_wind.getText());
					tv_weather_wind.setStart(false);
				}
				mContainer.invalidate();
			}
		});
		setcharacters();
		
		setOnclickListener();
		
		initChildrenFocusID();
		//获取数据
		getWeatherData();
		
		setDate();
	}

	@SuppressLint("SimpleDateFormat")
	private void setDate() {
		Calendar c = Calendar.getInstance();  
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
		Integer day = c.get(Calendar.DAY_OF_WEEK);  
		switch (day) {
		case 1:
			tv_main_week.setText("今天是星期天");
			break;
		case 2:
			tv_main_week.setText("今天是星期一");
			break;
		case 3:
			tv_main_week.setText("今天是星期二");
			break;
		case 4:
			tv_main_week.setText("今天是星期三");
			break;
		case 5:
			tv_main_week.setText("今天是星期四");
			break;
		case 6:
			tv_main_week.setText("今天是星期五");
			break;
		case 7:
			tv_main_week.setText("今天是星期六");
			break;
		}
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		  tv_main_date.setText(date.format(new Date()));
		
	}

	private void getWeatherData() 
	{
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				HiveViewService service = new HiveViewService();
				CommonPreferenceDAO dao = new CommonPreferenceDAO(getApplicationContext());
				try {
					ArrayList<WeatherInfo> list = service.getWeatherInfoList(dao.getCityID(),ApiConstant.APP_VERSION);
					weatherInfoDay = list.get(0);
					weatherInfoNight = list.get(1);
					
					String wallpaperUrl = null;
					switch (weatherInfoDay.getType()) {
					case 1:
						wallpaperUrl=weatherInfoDay.getDayWallpaper();
						break;
					case 2:
						wallpaperUrl=weatherInfoDay.getNightWallpaper();
						break;
					default:
						break;
					}
					
					if(!PengDoctorApplication.wallpaperUrl.equals(wallpaperUrl)){
						getWallPaper(wallpaperUrl);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(FINISH_WEATHER_DATA);
			}
		});
	}
	/**
	 * 更新天气数据
	 */
	@SuppressLint("CommitPrefEdits")
	private void updateData()
	{
		MyTextView area = (MyTextView) my_weather.findViewById(R.id.tv_weather_district);
		TextView tv_weather_wind = (TextView) my_weather.findViewById(R.id.tv_weather_wind);
		TextView tv_weather_day = (TextView) my_weather.findViewById(R.id.tv_weather_day);
		TextView tv_weather_temperature = (TextView) my_weather.findViewById(R.id.tv_weather_temperature);
		ImageLoadView image = (ImageLoadView) my_weather.findViewById(R.id.iv_weather);
		String areaName = weatherInfoDay.getHsienName();
		area.setText(areaName);
		area.setTextSize(30);
		/*if(areaName.length() <=3 )
		{
			area.setTextSize(25);
			area.setLines(1);
			area.setText(weatherInfoDay.getCityName());
		}
		else if(areaName.length() > 3 && areaName.length() <=6)
		{
			area.setTextSize(25);
			area.setLines(2);
			area.setText(weatherInfoDay.getCityName());
		}
		else
		{
			area.setTextSize(20);
			area.setLines(3);
			area.setText(weatherInfoDay.getCityName());
		}*/
		
		tv_weather_wind.setText( weatherInfoDay.getWind() + " " + weatherInfoDay.getWindDir());
		tv_weather_day.setText( weatherInfoDay.getWeather());
		tv_weather_temperature.setText(weatherInfoDay.getTemperature());
		Editor edit = sp.edit();
		
		switch (weatherInfoDay.getType()) {
		case 1:
			image.setImageUrl(weatherInfoDay.getDayIcon(), handler);
			break;
		case 2:
			image.setImageUrl(weatherInfoDay.getNightIcon(), handler);
			break;
		default:
			break;
		}
		//------------------------------

		edit.putString("day",   weatherInfoDay.getWeather());
		edit.putString("wind",  weatherInfoDay.getWind() + " " + weatherInfoDay.getWindDir());
		edit.putString("temperature", weatherInfoDay.getTemperature());
		switch (weatherInfoDay.getType()) {
		case 1:
			edit.putString("logo", weatherInfoDay.getDayIcon());
			edit.putString(AppConstant.SP_WALL_PAGER, weatherInfoDay.getDayWallpaper());
			break;
		case 2:
			edit.putString("logo", weatherInfoDay.getNightIcon());
			edit.putString(AppConstant.SP_WALL_PAGER, weatherInfoDay.getNightWallpaper());
			break;
		default:
			break;
		}
		edit.putString("place", weatherInfoDay.getHsienName());
		edit.commit();
	}

	private void setcharacters() {
		tvWeek.setTypeface(TypefaceUtils.getStandardfFontFace());
		tvDate.setTypeface(TypefaceUtils.getCE35FontFace());
	}

	/**设置点击监听事件*/
	private void setOnclickListener() {
		
		ll_main_movie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try {
					Intent mIntent = new Intent();
					
					ComponentName comp = new ComponentName(
					        "com.qiyi.video",
					        "com.qiyi.video.WelcomeActivity");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					startActivity(mIntent);
				} catch (Exception e) {
					ToastUtil.showMessageView(MainActivity.this, "影视剧院组件未安装！ ", 3);
					e.printStackTrace();
				}
			}
		});
		
		
		ll_main_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				ToastUtil.showMessageView(MainActivity.this, "乐享电视即将开放，敬请期待！", 3);
				
				
				try {
					Intent mIntent = new Intent();
					
					ComponentName comp = new ComponentName(
					        "com.hiveview.tv.onlive",
					        "com.hiveview.tv.onlive.activity.InitializationActivity");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					startActivity(mIntent);
				} catch (Exception e) {
					ToastUtil.showMessageView(MainActivity.this, "  乐享电视组件未安装！ ", 3);
					e.printStackTrace();
				}
				
			}
		});
		
		ll_main_happy_enjoy_area.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				ToastUtil.showMessageView(MainActivity.this, "蓝光极清即将开放，敬请期待！", 3);
				
				
				try {
					Intent mIntent = new Intent();
					
					ComponentName comp = new ComponentName(
					        "com.videoplayer",
					        "com.apk.leediskview.MainActivity");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					startActivity(mIntent);
				} catch (Exception e) {
					ToastUtil.showMessageView(MainActivity.this, "蓝光极清组件未安装！ ", 3);
					e.printStackTrace();
				}
				
			}
		});
		
		ll_main_application.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				os  applaction
//				try {
//					Intent mIntent = new Intent();
//					
//					ComponentName comp = new ComponentName(
//					        "com.yunos.tv.appstore",
//					        "com.yunos.tv.appstore.HomeActivity");
//					mIntent.setComponent(comp);
//					mIntent.setAction("android.intent.action.VIEW");
//					startActivity(mIntent);
//				} catch (Exception e) {
//					ToastUtil.showMessageView(MainActivity.this, "  应用 组件未安装！ ", 3);
//					e.printStackTrace();
//				}
				
				startActivity(new Intent(MainActivity.this, ApplicationActivity.class));
			}
		});
		
		ll_main_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(PengDoctorApplication.mContext,MySetInfoActivity.class);
                startActivity(mIntent);
			}
		});
		
		
		ll_clock_main_time.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {

				timeView.showDialog();
			}
		});
	
		ll_main_weather.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MyWeatherActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		ll_main_wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent mIntent = new Intent();
				
                ComponentName comp = new ComponentName("com.android.settings","com.android.settings.Settings");
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");
                startActivity(mIntent);
			
			}
		});

	}
	@Override
	protected void onStart() {
		super.onStart();
//		startAnimation();
		ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();  
		if(null != info && info.isAvailable())
		{
			getWeatherData();
		}
		else
		{
			findDataFromDB();
		}
		
		if(PengDoctorApplication.wallPager!=null )
		{
			if(!sp.getString(AppConstant.SP_WALL_PAGER, "").equals(PengDoctorApplication.wallpaperUrl))
			 {
				getWallPaper(PengDoctorApplication.wallpaperUrl);
			}else
			{
				iv_main_bg.setImageBitmap(PengDoctorApplication.wallPager);
			}
		}
	}

	
	private void findDataFromDB() {
		String day = sp.getString("day", "白天 ");
		String wind = sp.getString("wind", "风力 ");
		String logo = sp.getString("logo", "");
		String place = sp.getString("place", "");
		String temp = sp.getString("temperature", "");
		
		MyTextView area = (MyTextView) my_weather.findViewById(R.id.tv_weather_district);
		TextView tv_weather_wind = (TextView) my_weather.findViewById(R.id.tv_weather_wind);
		TextView tv_weather_day = (TextView) my_weather.findViewById(R.id.tv_weather_day);
		TextView tv_weather_temperature = (TextView) my_weather.findViewById(R.id.tv_weather_temperature);
		ImageLoadView image = (ImageLoadView) my_weather.findViewById(R.id.iv_weather);
		area.setText(place);
		area.setTextSize(30);
		tv_weather_wind.setText(wind);
		tv_weather_day.setText(day);
		tv_weather_temperature.setText(temp);
		image.setImageUrl(logo, handler);
	}

	/**设置焦点 */
	private void initChildrenFocusID() {
		
		ll_main_movie.setNextFocusUpId(View.NO_ID);
		ll_main_movie.setNextFocusLeftId(View.NO_ID);
		ll_main_movie.setNextFocusRightId(R.id.ll_main_tv);
		ll_main_movie.setNextFocusDownId(View.NO_ID);
		
		
		ll_main_tv.setNextFocusUpId(View.NO_ID);
		ll_main_tv.setNextFocusLeftId(R.id.ll_main_movie);
		ll_main_tv.setNextFocusRightId(R.id.ll_main_application);
		ll_main_tv.setNextFocusDownId(View.NO_ID);


		ll_main_happy_enjoy_area.setNextFocusUpId(View.NO_ID);
		ll_main_happy_enjoy_area.setNextFocusLeftId(R.id.ll_main_tv);
		ll_main_happy_enjoy_area.setNextFocusRightId(R.id.ll_clock_main_time);
		ll_main_happy_enjoy_area.setNextFocusDownId(R.id.ll_main_application);
		
		ll_main_application.setNextFocusUpId(R.id.ll_main_happy_enjoy_area);
		ll_main_application.setNextFocusLeftId(R.id.ll_main_tv);
		ll_main_application.setNextFocusRightId(R.id.ll_main_weather);
		ll_main_application.setNextFocusDownId(R.id.ll_main_setting);
		
		ll_main_setting.setNextFocusUpId(R.id.ll_main_application);
		ll_main_setting.setNextFocusLeftId(R.id.ll_main_tv);
		ll_main_setting.setNextFocusRightId(R.id.ll_main_wifi);
		ll_main_setting.setNextFocusDownId(View.NO_ID);
		
		ll_clock_main_time.setNextFocusUpId(View.NO_ID);
		ll_clock_main_time.setNextFocusLeftId(R.id.ll_main_happy_enjoy_area);
		ll_clock_main_time.setNextFocusRightId(View.NO_ID);
		ll_clock_main_time.setNextFocusDownId(R.id.ll_main_weather);
		
		ll_main_weather.setNextFocusUpId(R.id.ll_main_date);
		ll_main_weather.setNextFocusLeftId(R.id.ll_main_application);
		ll_main_weather.setNextFocusRightId(View.NO_ID);
		ll_main_weather.setNextFocusDownId(R.id.ll_main_wifi);
		
		ll_main_wifi.setNextFocusUpId(R.id.ll_main_weather);
		ll_main_wifi.setNextFocusLeftId(R.id.ll_main_setting);
		ll_main_wifi.setNextFocusRightId(View.NO_ID);
		ll_main_wifi.setNextFocusDownId(View.NO_ID);
		
		
	}


	private void setWallpaper() {
		if(PengDoctorApplication.wallPager!=null){
//			WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);  
//			wallpaperManager.suggestDesiredDimensions(PengDoctorApplication.Display_width, PengDoctorApplication.Display_hight);
//			Resources res = getResources();  
//			Bitmap bitmap=BitmapFactory.decodeResource(res, getResources().getIdentifier("wallpaper" + imagePosition, "drawable", "com.ch"));   
			
			/*try {
//				wallpaperManager.setBitmap(PengDoctorApplication.wallPager);
			} catch (IOException e) {
				e.printStackTrace();
			}  */
			iv_main_bg.setImageBitmap(PengDoctorApplication.wallPager);
		}
		
		
////		if(isWallPagerChange){
////			isWallPagerChange=false;
//			if(PengDoctorApplication.wallPager!=null){
//				Logger.e(TAG, "壁纸");
//				iv_main_bg.setImageBitmap(PengDoctorApplication.wallPager);
//			}
////		}
	}
	
	//start application animaction
	protected void startAppAnimation() {
		animationOneSet 	= AnimationUtil.getAppAnimationOneSet();
		animationTwoSet 	= AnimationUtil.getAppAnimationTwoSet();
		animationThreeSet 	= AnimationUtil.getAppAnimationThreeSet();
		
		
		iv_app_animation_pb_one.setAnimation(animationOneSet);
		iv_app_animation_pb_two.setAnimation(animationTwoSet);
		iv_app_animation_pb_three.setAnimation(animationThreeSet);
		

		iv_app_animation_pb_one.setVisibility(View.VISIBLE);
		iv_app_animation_pb_one.startAnimation(animationOneSet);
		iv_app_animation_pb_two.setVisibility(View.VISIBLE);
		iv_app_animation_pb_two.startAnimation(animationTwoSet);
		iv_app_animation_pb_three.setVisibility(View.VISIBLE);
		iv_app_animation_pb_three.startAnimation(animationThreeSet);
		
		handler.sendEmptyMessageDelayed(MSG_ANIMATION_APP_PB, 3000);
		
	}

	/**检查 组件  升级*/
	private void componentUpData() {
		//检测是否升级
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				//获取应用信息 ---
				AppUtils.getAppInfo(MainActivity.this);
				ArrayList<AppInfoEntity> appSystemList = PengDoctorApplication.appSystemList;
				ArrayList<AppMarketEntity> entity = null;
				
				for (AppInfoEntity appSystemEntity : appSystemList) {
					 //查询最新的信息
					try {
						entity = new HiveViewService().getAppInfo(AppConstant.APP_VERSION, appSystemEntity.getPackageName());
						
						updata(entity,appSystemEntity);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
	}

	
	protected void updata(ArrayList<AppMarketEntity> entity, AppInfoEntity appSystemEntity) {
		 //判断是否需要升级//可用空间大于150M
		 if(entity!=null && !appSystemEntity.getVersionName().equals(entity.get(0).getVersionNo())&&FileUtils.isDownload()){
			 // 下载    升级
			 appSystemEntity.setIsUpdating(true);
			 try {
				updataApk = entity.get(0);
				Logger.e(TAG, "文件下载"+entity.get(0).getAppName());
				FileUtils.systemDownLoad(MainActivity.this, entity.get(0), handler);
				
				// 得到sharedpreference的编辑器 
				Editor editor =  spUpdata.edit();
				//sp 保存   文件名  =  MD5
				editor.putString(entity.get(0).getVersionUrl().substring(entity.get(0).getVersionUrl().lastIndexOf("/")+1), entity.get(0).getMd5());
				editor.commit();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	}



	/**开启动画*/
	public void startAnimation(){
		 
		 ArrayList<View> views = new ArrayList<View>();
		 views.add(iv_main_logo);
		 views.add(iv_main_welcom);
		 views.add(ll_main_date);
		 
		 views.add(ll_clock_main_time);
		 views.add(ll_main_weather);
		 views.add(ll_main_wifi);

		 ll_main_movie.setVisibility(View.INVISIBLE);
		 ll_main_tv.setVisibility(View.INVISIBLE);
		 ll_main_happy_enjoy_area.setVisibility(View.INVISIBLE);
		 ll_main_application.setVisibility(View.INVISIBLE);
		 ll_main_setting.setVisibility(View.INVISIBLE);
		 
		 views.add(ll_main_movie);
		 views.add(ll_main_tv);
		 views.add(ll_main_happy_enjoy_area);
		 
		 views.add(ll_main_application);
		 views.add(ll_main_setting);
		 
		TranslateAnimation transAnimation0 = new TranslateAnimation(-500, 0, 0,0);
		TranslateAnimation transAnimation1 = new TranslateAnimation(500, 0, 0,0);
		TranslateAnimation transAnimation2 = new TranslateAnimation(500, 0, 0,0);
		
		TranslateAnimation transAnimation3 = new TranslateAnimation(500, 0, 0,0);
		TranslateAnimation transAnimation4 = new TranslateAnimation(500, 0, 0,0);
		TranslateAnimation transAnimation5 = new TranslateAnimation(500, 0, 0,0);
		
		Rotate3dAnimation  rotate3dMovie 	=	AnimationUtil.apply3DRotation(ll_main_movie, -90, 0);
		Rotate3dAnimation  rotate3dTv 		=	AnimationUtil.apply3DRotation(ll_main_tv, -90, 0);
		Rotate3dAnimation  rotate3dHappyShare 	=	AnimationUtil.apply3DRotation(ll_main_happy_enjoy_area, -90, 0);
		Rotate3dAnimation  rotate3dApplaction 	=	AnimationUtil.apply3DRotation(ll_main_application, -90, 0);
		Rotate3dAnimation  rotate3dSetting 		=	AnimationUtil.apply3DRotation(ll_main_setting, -90, 0);

		transAnimation0.setFillAfter(true);
		transAnimation1.setFillAfter(true);
		transAnimation2.setFillAfter(true);
		
		transAnimation3.setFillAfter(true);
		transAnimation4.setFillAfter(true);
		transAnimation5.setFillAfter(true);
		

		transAnimation2.setStartOffset(500);//执行前的等待时间
		transAnimation3.setStartOffset(1000);//执行前的等待时间
		transAnimation4.setStartOffset(1200);//执行前的等待时间
		transAnimation5.setStartOffset(1400);//执行前的等待时间
		rotate3dMovie.setStartOffset(1600);//执行前的等待时间
		rotate3dTv.setStartOffset(1800);//执行前的等待时间
		rotate3dHappyShare.setStartOffset(2000);//执行前的等待时间
		rotate3dApplaction.setStartOffset(2200);//执行前的等待时间
		rotate3dSetting.setStartOffset(2200);//执行前的等待时间
		

		transAnimation0.setDuration(2000);
		transAnimation1.setDuration(2000);
		transAnimation2.setDuration(2000);
		transAnimation3.setDuration(2000);
		transAnimation4.setDuration(2000);
		transAnimation5.setDuration(2000);
//		transAnimation6.setDuration(1000);
//		transAnimation7.setDuration(1000);
//		transAnimation8.setDuration(1000);
//		transAnimation9.setDuration(1000);
//		transAnimation10.setDuration(1000);
		

		ArrayList<Animation> animationList = new ArrayList<Animation>();
		animationList.add(transAnimation0);
		animationList.add(transAnimation1);
		animationList.add(transAnimation2);
		animationList.add(transAnimation3);
		animationList.add(transAnimation4);
		animationList.add(transAnimation5);
		
		animationList.add(rotate3dMovie);
		animationList.add(rotate3dTv);
		animationList.add(rotate3dHappyShare);
		animationList.add(rotate3dApplaction);
		animationList.add(rotate3dSetting);

		int count = views.size();

		for (int i = 0; i < count; i++) {
			views.get(i).clearAnimation();
			views.get(i).setAnimation(animationList.get(i));
		}

		for (int i = 0; i < count; i++) {
			animationList.get(i).start();
		}
	 }
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		startAnimation();
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		 if (KeyEvent.ACTION_DOWN == event.getAction()) {
			if(event.getKeyCode() ==  KeyEvent.KEYCODE_BACK) return false;
			
			if(event.getKeyCode() ==  KeyEvent.KEYCODE_DPAD_DOWN){
				
				View view = this.getCurrentFocus();
				switch (view.getId()) {
				case R.id.ll_main_movie:
					return true;
				case R.id.ll_main_tv:
					return true;
				default:
					break;
				}
			} 
		}
		return super.dispatchKeyEvent(event);
	}
}
