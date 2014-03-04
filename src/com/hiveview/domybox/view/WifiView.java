package com.hiveview.domybox.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.TypefaceUtils;

public class WifiView extends FrameLayout{

	private Context mContext;
	private TextView 	connectName;
	private ImageView 	ivWifiState;
	protected static final int MSG_REFRESH_WIFI_RSSI = 4001;
	protected static final int MSG_PING_WIFI = 4002;
	protected static final int MSG_PING_LOCATION = 4003;
	protected static final String TAG = "WifiView";
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private WifiManager wifiService;
	private WifiInfo wifiInfo;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			 switch (msg.what) {  
	            case MSG_REFRESH_WIFI_RSSI:  
	            	checkNetwork();
	            	mHandler.sendEmptyMessageDelayed(MSG_REFRESH_WIFI_RSSI, 5000);
	                break;
	                
	            case MSG_PING_WIFI:  
	            	if(-1==msg.arg1){
	            		wifiConnect(false);
	            	}else {
	            		wifiConnect(true);
					}
	                break;
	                
	            case MSG_PING_LOCATION:  
	            	if(-1==msg.arg1){
	            		locationConnect(false);
	            	}else {
						locationConnect(true);
					}
	                break;
	            default:
	            	break;
	            }  
			
			super.handleMessage(msg);
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkNetwork();
		}
	};
	
	
	
	public WifiView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WifiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}

	public WifiView(Context context) {
		super(context);
		mContext=context;
		init();
	}

	private void init() {
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (View) mLayoutInflater.inflate(R.layout.view_layout_wifi,null);
		
		addView(view);
		
		connectName	 	=	(TextView)view.findViewById(R.id.tv_wifi_view_connect_name);
		
		ivWifiState 	=	(ImageView)view.findViewById(R.id.iv_wifi_view_state);
		
		wifiService = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE); 
		
		/*注册广播*/
//		setBroadCast();
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH_WIFI_RSSI, 5000);
		checkNetwork();
		
		connectName.setTypeface(TypefaceUtils.getCE35FontFace());
	}

	@SuppressWarnings("unused")
	private void setBroadCast() {
		Logger.i("广播", "注册");
		//生成广播处理 
//		smsBroadCastReceiver = new SmsBroadCastReceiver();
         //实例化过滤器并设置要过滤的广播 
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"); 
        
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
         //注册广播 
        mContext.registerReceiver(mReceiver, intentFilter); 
		
	}

	
	/**检查 网络改变*/
	public  void checkNetwork(){
		connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo();  
        
        if(info != null && info.isAvailable()) {
        	int type=info.getType();
        	switch (type) {
        	case ConnectivityManager.TYPE_ETHERNET:
				checkNetworkavAilable(MSG_PING_LOCATION);
				break;
			case ConnectivityManager.TYPE_WIFI:
				checkNetworkavAilable(MSG_PING_WIFI);
				break;
			default:
				unConnect();
				//mHandler.sendEmptyMessageDelayed(MSG_REFRESH_WIFI_RSSI, 5000);
				break;
			}
        	
        }else {
			//网络不可用
        	unConnect();
		}
        
            
        
	}

	/**未连接网络*/
	private void unConnect() {
		connectName.setText("未连接");
		ivWifiState.setImageResource(R.drawable.network_unconnected);
	}

	/** 设置wifi连接 */
	 private void wifiConnect(Boolean connect) {
//		Logger.i("wifi", "连接");
		
		wifiInfo = wifiService.getConnectionInfo();
		int rssi=wifiInfo.getRssi();
		
		connectName.setVisibility(View.VISIBLE);
		if(info.getExtraInfo()==null|info.getExtraInfo()==""){
			connectName.setText("");
		}else {
			String name=info.getExtraInfo();
			connectName.setText(name.subSequence(1, name.length()-1));
		}
		
		if(connect){
//			Logger.e("wifi连接", "正常使用");
			ivWifiState.setImageResource(getResByRssi(rssi));
		}else {
			//不可用	
//			Logger.e("wifi连接", "不能正常使用");
			ivWifiState.setImageResource(R.drawable.wifi_connect_abnormal);
		}
		
	}

	

	/** 设置本地连接 */
	private void locationConnect(Boolean connect) {
		
		connectName.setText("本地连接");
		
		if(connect){
//			Logger.e("本地连接", "正常使用");
			ivWifiState.setImageResource(R.drawable.location_connected);
		}else {
			//不可用	
//			Logger.e("本地连接", "不能正常使用");
			ivWifiState.setImageResource(R.drawable.location_connected_abnormal);
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	
	// -100 -55
	private int getResByRssi(int rssi){
		int level = WifiManager.calculateSignalLevel(rssi, 3);
//		Logger.i("", "level :" + level);
		int drawableId = 0;
		switch (level) {
		case 0:
			drawableId = R.drawable.wifi_signal_2;
			break;
		case 1:
			drawableId = R.drawable.wifi_signal_3;
			break;
		case 2:
			drawableId = R.drawable.wifi_signal_4;
			break;
//		case 3:
//			drawableId = R.drawable.main_page_image_wifi_connected_04;	
//			break;
		default:
			drawableId = R.drawable.wifi_signal_2;	
			break;
		}
		return drawableId;
	}
	
	
	/**检查网络可用*/
	private void checkNetworkavAilable(final int what) {
		
		DownLoadTaskManager.getInstance().submit(new Runnable() {
	 	int result; 
		InputStream is;
		String ip="www.baidu.com";
		
		@Override
		public void run() {
			try {
				InetAddress address = InetAddress.getByName("www.baidu.com");
				if(address!=null){
					
				}else {
					//网络未通
					result	=	-1;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} finally{
            	Message msg=Message.obtain();
            	msg.what=what;
            	msg.arg1=result;
            	mHandler.sendMessage(msg);
            }
			
		}
		
		});	
	}
	
}
