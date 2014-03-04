package com.hiveview.domybox.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;
/**
 * 自定义time的linearLayout
 * @author xlk
 *
 */
public class TimerView extends LinearLayout {
	//上下文
	private Context mContext;
	//AM或PM
	private TextView tv_ap;
	//分秒DigitalClock
	private MyDigitalClock tv_timer;
	private LinearLayout linear;
	//下方进度图片
	private ImageView iv_progressor;
	private SharedPreferences sp;
	private int width;
	private int progress_width;
	//包含进度条的布局
	private FrameLayout frame_layout;
	//时间格式
	private static String timeStyle = "24";//默认时间格式为24小时制
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			getTime(timeStyle);
			handler.sendEmptyMessageDelayed(0, 1000);
			
		};
	};
	private String[] now_date_arrays;
	private TranslateAnimation animation;

	@SuppressLint("NewApi")
	public TimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TimerView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	/**
	 * 填充布局文件
	 * 初始化最重要的三个控件对象，
	 * 上下午的text
	 * 时间的text
	 * 进度条的image
	 */
	@SuppressLint("CutPasteId")
	public void init()
	{
		
		View view = View.inflate(mContext, R.layout.time, null);
		addView(view);
		tv_ap = (TextView) findViewById(R.id.tv_ap);
		tv_timer = (MyDigitalClock) findViewById(R.id.tv_time);
		/*LinearLayout linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
		frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
		linear = (LinearLayout) view.findViewById(R.id.linear_layout);*/
		
		//tv_ap = (TextView) linear_layout.findViewById(R.id.tv_ap);
		tv_ap.setTypeface(TypefaceUtils.getStandardfFontFace());
		//tv_timer = (MyDigitalClock) linear_layout.findViewById(R.id.tv_time);
		tv_timer.setTextSize(50);
		tv_timer.setContext(mContext);
		tv_timer.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_ap.setText(tv_timer.getHourIn24());
		iv_progressor = (ImageView) findViewById(R.id.iv_progressor);
		//iv_progressor.set
		sp = mContext.getSharedPreferences("timeSet", Activity.MODE_PRIVATE);
		timeStyle = sp.getString("timeStyle", "24");
		if("24".equals(timeStyle))
		{
			tv_timer.setFormat("kk:mm");
			tv_ap.setVisibility(View.INVISIBLE);
		}
		else
		{
			tv_timer.setFormat("hh:mm");
			tv_ap.setVisibility(View.VISIBLE);
		}
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	/**
	 * 获取指定日期格式的字符串
	 * @param dataFormat
	 */
	public void getTime(String dataFormat)
	{
		tv_ap.setText(tv_timer.getHourIn24());
		setAnimation();
	}

	/**
	 * 设置AM或PM
	 * 只能使用24小时制度来判断
	 */
	@SuppressLint("SimpleDateFormat")
	private void amOrpm() {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		Date now = new Date();
		String this_time = format.format(now);
		String value = Integer.parseInt(this_time)>=12 ? "下午-":"上午-";
		//设置标题的字体为幼圆体
		Typeface standardfFontFace = TypefaceUtils.getStandardfFontFace();
		tv_ap.setTypeface(standardfFontFace);
		tv_ap.setText(value);
	}
	/**
	 * 一秒跑一次
	 * @param point
	 */
	public void setAnimation()
	{
		// 重新测量下frameLayout
        OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        TimerView.this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        animation = new TranslateAnimation(-1 * progress_width, width , 0, 0);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        iv_progressor.setVisibility(View.VISIBLE);
        iv_progressor.startAnimation(animation);
	}
	
	

	/**
	 * 显示设置时间格式界面
	 */
	@SuppressLint("NewApi")
	public void showDialog()
	{

		
		//final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		final Dialog dialog = new Dialog(mContext, R.style.dialog); 
		View view = View.inflate(mContext, R.layout.time_setter, null);
		TextView time_title = (TextView) view.findViewById(R.id.time_title);
		ImageButton iv_24 = (ImageButton) view.findViewById(R.id.time24);
		ImageButton iv_12 = (ImageButton) view.findViewById(R.id.time12);
		//设置初始化的显示的焦点问题
		String mytimeStyle = sp.getString("timeStyle", "24");
		if(mytimeStyle.equals("24"))
			iv_24.requestFocus();
		else
		{
			iv_12.requestFocus();
		}
		//设置标题的字体为幼圆体
		Typeface standardfFontFace = TypefaceUtils.getStandardfFontFace();
		time_title.setTypeface(standardfFontFace);
		//设置时间格式
		iv_24.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//DateFormat
				timeStyle = "24";
				Editor edit = sp.edit();
				edit.putString("timeStyle", timeStyle);
				edit.commit();
				tv_timer.setFormat("kk:mm");
				tv_ap.setVisibility(View.INVISIBLE);
				dialog.dismiss();
			}
		});
		iv_12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timeStyle = "12";
				Editor edit = sp.edit();
				edit.putString("timeStyle", timeStyle);
				edit.commit();
				tv_timer.setFormat("hh:mm");
				tv_ap.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
			}
		});
		dialog.setContentView(view);
		  //取消状态栏
		dialog.show();
	}
	
	
	 class MyOnGlobalLayoutListener implements OnGlobalLayoutListener{

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				width = TimerView.this.getWidth();
				progress_width = iv_progressor.getWidth();

			}
			 
		 }
}
