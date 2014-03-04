package com.hiveview.domybox.view.settings;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hiveview.domybox.R;
import com.hiveview.domybox.activity.adapter.WifiAdapter;
import com.hiveview.domybox.utils.TypefaceUtils;

public class WifiSettingView extends FrameLayout{
    // Combo scans can take 5-6s to complete - set to 10s.
    private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;
	
	private Context mContext;
	private WifiManager wifiManager;
	private ListView wifiListView;
	private Animation animation;
	private TextView tvWaiting;
	private ImageView iv_wifi_state_logo;
	private TextView tv_wifi_state;
	
    private IntentFilter mFilter;
    private BroadcastReceiver mReceiver;
    private Scanner mScanner;
    private WifiAdapter adapter;
    private List<ScanResult> wifiList;
    
    private WifiConfiguration selectConfig;
	
	
	public WifiSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
		initData();
	}

	public WifiSettingView(Context context) {
		super(context);
		this.mContext = context;
		init();
		initData();
	}

	private void init(){
		animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_animation);
		wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		wifiListView = new ListView(mContext);
		wifiListView.setBackgroundColor(mContext.getResources().getColor(R.color.setting_tab_select));
		
		wifiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectConfig = adapter.getWifiConfig(position);
				iv_wifi_state_logo = (ImageView) view.findViewById(R.id.iv_wifi_state_logo);
				tv_wifi_state = (TextView) view.findViewById(R.id.tv_wifi_state);
				if(selectConfig == null){
					setNetDialog(position);
				}else{
					forgetDialog(selectConfig, position);
				}
			}
			
		});
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		wifiListView.setLayoutParams(params);
		
		tvWaiting = new TextView(mContext);
		tvWaiting.setText("正在扫描wifi...");
		tvWaiting.setTextSize(24);
		tvWaiting.setVisibility(View.GONE);
		tvWaiting.setGravity(Gravity.CENTER);
		tvWaiting.setLayoutParams(params);
		
		addView(wifiListView);
		addView(tvWaiting);
		
		
		initWifiInfos();
	}
	
	
	private void initData(){
		mFilter = new IntentFilter();
		mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//		mFilter.addAction(WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION);
//		mFilter.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
		mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				handleEvent(context, intent);
			}
		};
		
		mScanner = new Scanner();
		mContext.registerReceiver(mReceiver, mFilter);
	}
	
	private void initWifiInfos(){

		wifiList = wifiManager.getScanResults();
		//当前连接上的wifiInfo
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		adapter = new WifiAdapter(getContext(), wifiList);
		adapter.setWifiInfo(wifiInfo);
		wifiListView.setSelector(R.drawable.lv_selector);
		wifiListView.setAdapter(adapter);
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mScanner.pause();
		mContext.unregisterReceiver(mReceiver);
	}
	
	@SuppressLint("HandlerLeak")
	private class Scanner extends Handler {
        private int mRetry = 0;

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void pause() {
            mRetry = 0;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message) {
            if (wifiManager.startScan()) {
                mRetry = 0;
            } else if (++mRetry >= 3) {
                mRetry = 0;
                Toast.makeText(mContext, R.string.wifi_fail_to_scan,Toast.LENGTH_LONG).show();
                return;
            }
            sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
        }
    }
	
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if(visibility==View.VISIBLE){
			tvWaiting.setVisibility(View.VISIBLE);
		}else if(visibility==View.INVISIBLE){
			adapter.setDataSource(null);
		}
	}
	NetworkInfo networkInfo;
	private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN));
        }else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
        	 Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
	            if (null != parcelableExtra){
	                networkInfo = (NetworkInfo) parcelableExtra;
	                if(null != networkInfo){
	                	if(networkInfo.isConnected()){
	                		updateAPPoint(networkInfo);
	                		wifiManager.saveConfiguration();
	                	}
	                }
	            }    
        }else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
        	updateAPPoint(networkInfo);
        	if(tvWaiting.isShown())
        		tvWaiting.setVisibility(View.GONE);
        } 
    }
	
	private void updateAPPoint(NetworkInfo networkInfo){
		wifiList = wifiManager.getScanResults();
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		
		
		if(configs!=null){
			adapter.setWifiConfig(configs);
		}
		
		
		if (selectConfig != null) {
//			selectConfig.d
			if(selectConfig.status == WifiConfiguration.Status.DISABLED){
//				Toast.makeText(mContext, selectConfig.SSID+"连接失败",Toast.LENGTH_LONG).show();
			}
        }
		if(networkInfo!=null&&networkInfo.isConnected()){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			adapter.setWifiInfo(wifiInfo);
		}else{
			adapter.setWifiInfo(null);
		}
		adapter.setDataSource(wifiList);
	}
	
	private void updateWifiState(int state) {

        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED:
                mScanner.resume();
//                Toast.makeText(mContext, "wifi enable",Toast.LENGTH_LONG).show();
                return; // not break, to avoid the call to pause() below

            case WifiManager.WIFI_STATE_ENABLING:
                break;

            case WifiManager.WIFI_STATE_DISABLED:
                break;
        }

        mScanner.pause();
    }
	
	/**
	 * @param pwd
	 */
	public void connect2(ScanResult result2,String pwd)
	{
		try {  
			 selectConfig = new WifiConfiguration();
			 selectConfig.BSSID = result2.BSSID;
			 selectConfig.SSID = "\"" + result2.SSID + "\""; 
			
			 selectConfig.hiddenSSID = false;
//			 selectConfig.status = WifiConfiguration.Status.ENABLED;
			 selectConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			 selectConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			 selectConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			 selectConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			 selectConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			 selectConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			 if(null != result2.capabilities && result2.capabilities.contains("WEP"))
			 { 
			    selectConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			    selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			    selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			    selectConfig.wepKeys[0] ="\"" + pwd + "\""; //This is the WEP Password
			    selectConfig.wepTxKeyIndex = 0;
			 }
			 else if(null != result2.capabilities && result2.capabilities.contains("WPA-EAP"))
			 {
			 selectConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
			 selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			 selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			 selectConfig.preSharedKey = "\"" + pwd + "\"";
			 }
			 else if(null != result2.capabilities && (result2.capabilities.contains("WPA2-PSK") || result2.capabilities.contains("WPA-PSK")))
			 {
			 selectConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			 selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			 selectConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			 selectConfig.preSharedKey = "\"" + pwd + "\"";
			 }
			 else
			 selectConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			 
			 int integer = wifiManager.addNetwork(selectConfig);
             if (integer != -1) {
            	wifiManager.enableNetwork(integer, false);
                selectConfig.networkId = integer;
                connect(integer,selectConfig.priority);
			}
		} catch (Exception e) {
		}
	}
	
    private void connect(int networkId,int mLastPriority) 
    {
        if (networkId == -1) 
            return;
        wifiManager.updateNetwork(selectConfig);
        wifiManager.enableNetwork(networkId, true);
        wifiManager.reconnect();
    }
    
    private Dialog dialog;
    private void forgetDialog(final WifiConfiguration config,int position){
    	dialog = new Dialog(mContext,R.style.dialog);
		View viewContent = View.inflate(mContext, R.layout.dialog_wifi_ip, null);
		TextView tvContent =  (TextView) viewContent.findViewById(R.id.tv_netset_content);
		TextView tvTitle = (TextView) viewContent.findViewById(R.id.tv_netset_title);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String SSID = wifiInfo.getSSID();
		if(null != wifiList.get(position).SSID && !"".equals(wifiList.get(position)) && ("\"" + wifiList.get(position).SSID+ "\"").equals(SSID)){
			tvTitle.setText("IP");
			int ipAddress = wifiInfo.getIpAddress();  
			String ipString = "";
			  
			if (ipAddress != 0) {  
			       ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."   
			        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));  
			}  
			tvContent.setText(ipString);
			tvContent.setTextSize(28);
		}else{
			tvTitle.setText("安全性");
			tvContent.setText(getSecurity(config));
			tvContent.setTextSize(23);
		}
		LinearLayout btnForgetPsw = (LinearLayout) viewContent.findViewById(R.id.ll_forget_password);
		btnForgetPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				forget(config.networkId);
			}
		});
		LinearLayout llConnect = (LinearLayout) viewContent.findViewById(R.id.ll_connect);
		llConnect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				wifiManager.updateNetwork(config);
		        wifiManager.enableNetwork(config.networkId, true);
		        wifiManager.reconnect();
		        wifiManager.saveConfiguration();
		        Toast.makeText(mContext, selectConfig.SSID+"连接中...",Toast.LENGTH_LONG).show();
		        networkInfo = null;
		        updateAPPoint(networkInfo);
			}
			
		});
		dialog.setContentView(viewContent);
		dialog.show();
    }
    
    
    private void forget(int networkId) {
        wifiManager.removeNetwork(networkId);
        saveNetworks();
    }
    
    private void saveNetworks() {
        // Always save the configuration with all networks enabled.
    	wifiManager.saveConfiguration();
    	updateAPPoint(networkInfo);
    	selectConfig = null;
    }
    
    static String getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return "PSK";
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return "EAP";
        }
        return (config.wepKeys[0] != null) ? "WEP" : "无";
    }
    
    
    private void setNetDialog(final int position){
    	dialog = new Dialog(mContext,R.style.dialog); 
		View viewContent = View.inflate(mContext, R.layout.net_set_dialog_layout, null);
		TextView tv_dialog_title = (TextView) viewContent.findViewById(R.id.tv_dialog_title);
		final ScanResult click_result = wifiList.get(position);
		tv_dialog_title.setText(wifiList.get(position).SSID);
		tv_dialog_title.setTypeface(TypefaceUtils.getCE35FontFace());
		final EditText et_pwd = (EditText) viewContent.findViewById(R.id.et_pwd);
		et_pwd.clearFocus();
		et_pwd.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{
				if(actionId==EditorInfo.IME_ACTION_DONE ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
				{
					dialog.dismiss();
					if(!et_pwd.getText().toString().equals(""))
					{
						connect2(click_result,et_pwd.getText().toString());
						networkInfo = null;
						updateAPPoint(networkInfo);
						Toast.makeText(mContext, wifiList.get(position).SSID + "连接中...",Toast.LENGTH_LONG).show();
					}
					return true;
				}
				
				return false;
			}
		});
		dialog.setContentView(viewContent);
		dialog.show();
    }

}
