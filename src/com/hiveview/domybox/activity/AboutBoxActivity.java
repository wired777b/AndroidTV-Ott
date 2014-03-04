package com.hiveview.domybox.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.util.Enumeration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.MyTextView;

public class AboutBoxActivity extends BaseActivity {
	
	private StatFs statfsROM = new StatFs(Environment.getDataDirectory().getAbsolutePath());
	private StatFs statfsSD = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	private NumberFormat numberFormat = NumberFormat.getInstance();
	private LinearLayout ll_btn ;
	private LinearLayout resume_factory;
	private TextView resume;
	private TextView tv_box_name;
	private TextView tv_box_value;
	
	private MyTextView tv_wifi_value;
	private MyTextView textView1;
	private MyTextView tv_m_number;
	private MyTextView tv_ytadd_value;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_box_activity);
		sp = getSharedPreferences("setinfo", Context.MODE_PRIVATE);
		TypefaceInit();
		init();
	}
	/**
	 * 初始化盒子名称和恢复出厂设置按钮
	 */
	private void init() 
	{
		ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
		ll_btn.requestFocus();
		resume_factory = (LinearLayout) findViewById(R.id.resume_factory);
		ll_btn.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				  tv_wifi_value.setStart(true);
				  textView1.setStart(true);
				  tv_m_number.setStart(true);
				  tv_ytadd_value.setStart(true);
				  tv_wifi_value.setText(tv_wifi_value.getText());
				  textView1.setText(textView1.getText());
				  tv_m_number.setText(tv_m_number.getText());
				  tv_ytadd_value.setText(tv_ytadd_value.getText());
				
			}
		});
		resume = (TextView) findViewById(R.id.resume);
		resume.setTypeface(TypefaceUtils.getStandardfFontFace());
		ll_btn.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				changeBoxNameDialog();
				//ToastUtil.showMessageView(AboutBoxActivity.this, "  功能尚未开放，敬请期待！ ", 1);
			}

			
		});
		resume_factory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ToastUtil.showMessageView(AboutBoxActivity.this, "  功能尚未开放，敬请期待！ ", 1);
			}
		});
		
	}
	/**
	 * 检查版本更新
	 */
	public static void getVersion()
	{
		
	}
	/**
	 * 改变盒子名称的对话框
	 */
	private void changeBoxNameDialog() {
		final Dialog dialog = new Dialog(AboutBoxActivity.this, R.style.dialog);
		//AlertDialog dialog = new AlertDialog.Builder(AboutBoxActivity.this).create();
		View view = View.inflate(AboutBoxActivity.this, R.layout.input_view, null);	
		TextView tv_change_name = (TextView) view.findViewById(R.id.tv_change_name);
		TextView tv_box_name = (TextView) view.findViewById(R.id.tv_box_name);
		tv_change_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_box_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		final EditText tv_box_inputname = (EditText) view.findViewById(R.id.tv_box_inputname);
		tv_box_inputname.requestFocus();
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Editor editor = sp.edit();
				String boxname = tv_box_inputname.getText().toString();
				if(null == boxname || "".equals(boxname))
				{
					boxname = "DomyBox";
				}
				editor.putString("boxname", boxname);
				editor.commit();
				tv_box_value.setText(boxname);
				tv_box_value.invalidate();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		// ToastUtil.showMessageView(AboutBoxActivity.this, "  账户体系还未开放，敬请期待！ ", 3);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*if(keyCode == KeyEvent.)
		{
			
		}*/
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 设置字体及数据
	 */
	@SuppressLint("ServiceCast")
	private void TypefaceInit() {
		//标题
		TextView user_set = (TextView) findViewById(R.id.user_set);
		TextView user_set_about_box = (TextView) findViewById(R.id.user_set_about_box);
		TextView TextView09 = (TextView) findViewById(R.id.TextView09);
		TextView TextView12 = (TextView) findViewById(R.id.TextView12);
		TextView tv_box_rl = (TextView) findViewById(R.id.tv_box_rl);
		TextView tv_boxleft = (TextView) findViewById(R.id.tv_boxleft);
		TextView tv_sd_rl = (TextView) findViewById(R.id.tv_sd_rl);
		TextView tv_sdleft = (TextView) findViewById(R.id.tv_sdleft);
		
		//信息项名
		TextView tv_m_no = (TextView) findViewById(R.id.tv_m_no);
		TextView tv_m_version = (TextView) findViewById(R.id.tv_m_version);
		TextView tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
		TextView tv_ytadd_name = (TextView) findViewById(R.id.tv_ytadd_name);
		TextView tv_ip_name = (TextView) findViewById(R.id.tv_ip_name);
		TextView tv_hotline_name = (TextView) findViewById(R.id.tv_hotline_name);
		textView1 = (MyTextView) findViewById(R.id.textView1);
		tv_m_number = (MyTextView) findViewById(R.id.tv_m_number);
		tv_wifi_value = (MyTextView) findViewById(R.id.tv_wifi_value);
		tv_ytadd_value = (MyTextView) findViewById(R.id.tv_ytadd_value);
		TextView tv_ip_value = (TextView) findViewById(R.id.tv_ip_value);
		TextView tv_hotline_value = (TextView) findViewById(R.id.tv_hotline_value);
		//进度条
		ProgressBar pb_rom = (ProgressBar) findViewById(R.id.progressBar1);
		ProgressBar pb_sd = (ProgressBar) findViewById(R.id.ProgressBar01);
		
		user_set.setText("设置");
		user_set.setTypeface(TypefaceUtils.getStandardfFontFace());
		user_set_about_box.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_m_no.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_m_version.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_wifi_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_ytadd_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_ip_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_hotline_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		textView1.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_m_number.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_wifi_value.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_ytadd_value.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_ip_value.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_hotline_value.setTypeface(TypefaceUtils.getCE35FontFace());
		TextView09.setTypeface(TypefaceUtils.getStandardfFontFace());
		TextView12.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_box_rl.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_sd_rl.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_boxleft.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_sdleft.setTypeface(TypefaceUtils.getCE35FontFace());
		
		tv_ytadd_value.setText(getMacAddress());
		tv_wifi_value.setText(getWIFIAddress());
		tv_ip_value.setText(getPsdnIp());
		
		tv_box_name = (TextView) findViewById(R.id.tv_box_name);
		tv_box_value = (TextView) findViewById(R.id.tv_box_value);
		
		tv_box_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_box_value.setTypeface(TypefaceUtils.getCE35FontFace());
		tv_box_value.setText(sp.getString("boxname", "DomyBox"));
		tv_m_number.setText(Build.MODEL);
		textView1.setText("Android"+Build.VERSION.RELEASE);
		tv_box_rl.setText(getUsedROM() + "/" + getAllROM());
		tv_sd_rl.setText(getUsedSD() + "/" +getAllSD());
		tv_boxleft.setText("剩余" + getROMPercent());
		tv_sdleft.setText("剩余" + getSDPercent());
		pb_rom.setProgress(getROM());
		pb_sd.setProgress(getSD());
	}

		/**
		 * 获取手机内部总存储空间
		 * @return
		 */
		public String getAllROM() {
			int blocks = statfsROM.getBlockCount();
			int size = statfsROM.getBlockSize();
			long total = blocks * size;
			return Formatter.formatFileSize(this, total);
		}
		/**
		 * 已使用的ROM空间
		 * @return
		 */
		public String getUsedROM()
		{
			int blocks = statfsROM.getBlockCount() - statfsROM.getAvailableBlocks();
			int size = statfsROM.getBlockSize();
			long total = blocks * size;
			return Formatter.formatFileSize(this, total).contains(".")?Formatter.formatFileSize(this, total).split(".")[0]:Formatter.formatFileSize(this, total);
		}
		/**
		 * 获取手机外部总存储空间
		 * @return
		 */
		public String getAllSD() {
			int blocks = statfsSD.getBlockCount();
			int size = statfsSD.getBlockSize();
			long total = blocks * size;
			return Formatter.formatFileSize(this, total);
		}
		/**
		 * 获得已使用的SD空间大小
		 * @return
		 */
		public String getUsedSD()
		{
			int blocks = statfsSD.getBlockCount() - statfsSD.getAvailableBlocks();
			int size = statfsSD.getBlockSize();
			long total = blocks * size;
			//return Formatter.formatFileSize(this, total).split(".")[0];
			return Formatter.formatFileSize(this, total).contains(".")?Formatter.formatFileSize(this, total).split(".")[0]:Formatter.formatFileSize(this, total);
		}
		/**
		 * 获取rom的剩余量的百分比
		 * @return
		 */
		public String getROMPercent()
		{
			numberFormat.setMaximumFractionDigits(2);
			String result = numberFormat.format((float)statfsROM.getAvailableBlocks()/(float)statfsROM.getBlockCount()*100);
			int percent = (int) Float.parseFloat(result);
			return percent +"%";
		}
		/**
		 * 获取SD卡的剩余量的百分比
		 * @return
		 */
		public String getSDPercent()
		{
			numberFormat.setMaximumFractionDigits(2);
			String result = numberFormat.format((float)statfsSD.getAvailableBlocks()/(float)statfsSD.getBlockCount()*100);
			int percent = (int) Float.parseFloat(result);
			return percent +"%";
		}
		/**
		 * 获取ROM进度信息
		 * @return
		 */
		public int getROM()
		{
			float flo = Float.parseFloat(getROMPercent().split("%")[0]);
			return 100-(int)flo;
		}
		/**
		 * 获取SD进度信息
		 * @return
		 */
		public int getSD()
		{
			float flo = Float.parseFloat(getSDPercent().split("%")[0]);
			return 100-(int)flo;
		}
		
		/**
		 * 获取以太网的mac地址
		 * @return
		 */
		@SuppressLint("DefaultLocale")
		public String getMacAddress(){
		     try {
		         return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
		     } catch (IOException e) {
		         e.printStackTrace();
		         return null;
		     }
		 }
		/**
		 * 读取文件获取以太网mac地址
		 * @param filePath
		 * @return
		 * @throws java.io.IOException
		 */
		public static String loadFileAsString(String filePath) throws java.io.IOException{
		     StringBuffer fileData = new StringBuffer(1000);
		     BufferedReader reader = new BufferedReader(new FileReader(filePath));
		     char[] buf = new char[1024];
		     int numRead=0;
		     while((numRead=reader.read(buf)) != -1){
		         String readData = String.valueOf(buf, 0, numRead);
		         fileData.append(readData);
		     }
		     reader.close();
		     return fileData.toString();
		 }
		/**
		 * 获取wifi的MAC地址
		 * @return
		 */
		public String getWIFIAddress() 
		{ 
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
			WifiInfo info = wifi.getConnectionInfo(); 
			return info.getMacAddress(); 
		} 
		

		/**
		 * 获取IP地址
		 * @return
		 */
		private static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
		
		
		/*private Button.OnClickListener mFinalClickListener = new Button.OnClickListener() {
            public void onClick(View v) {
                if (Utils.isMonkeyRunning()) {
                    return;
                }
                if (mExternalStorage.isChecked()) {
                    Intent intent = new Intent(ExternalStorageFormatter.FORMAT_AND_FACTORY_RESET);
                    intent.setComponent(ExternalStorageFormatter.COMPONENT_NAME);
                    startService(intent);
                } else {
                    sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
                    // Intent handling is asynchronous -- assume it will happen soon.
                }
            }
        };*/

}
