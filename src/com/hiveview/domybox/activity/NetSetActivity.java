package com.hiveview.domybox.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.EthernetUtil;
import com.hiveview.domybox.view.settings.EtherNetView;
import com.hiveview.domybox.view.settings.WifiSettingView;

public class NetSetActivity extends BaseActivity implements OnClickListener{
	//wifi tab
	private LinearLayout llWifiConn;
	//ether tab
	private LinearLayout llEtherConn;
	private LinearLayout llWifiConnContent;
	private LinearLayout llEtherConnContent;
	
	private LinearLayout llNoNetWork;
	
	private EtherNetView etherNetView;
	private WifiSettingView wifiView;
	
	private EthernetUtil mEthernetUtil;
	private WifiManager mWifiManager;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net_set_activity);
		initView();
		initEth();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!mWifiManager.isWifiEnabled() && !EthernetUtil.getAvailableNetWorkInfo(getApplicationContext())){
			llNoNetWork.setVisibility(View.VISIBLE);
			wifiView.setVisibility(View.INVISIBLE);
			etherNetView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initEth(){
//		initView();
		setEvent();
		mEthernetUtil = new EthernetUtil(this);
		mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		
		if(mEthernetUtil.isEnable()){
			llEtherConn.requestFocus();
			wifiView.setVisibility(View.INVISIBLE);
			llEtherConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_select));
		}else if(mWifiManager.isWifiEnabled()){
			llWifiConn.requestFocus();
			etherNetView.setVisibility(View.INVISIBLE);
			llWifiConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_select));
		}
		
	}
	
	
	private void initView(){
		llWifiConn = (LinearLayout) this.findViewById(R.id.ll_wifi_conn);
		llEtherConn = (LinearLayout) this.findViewById(R.id.ll_net_conn);
		
		llWifiConnContent = (LinearLayout) this.findViewById(R.id.ll_content_wifi);
		llEtherConnContent = (LinearLayout) this.findViewById(R.id.ll_content_net);
		
		llNoNetWork = (LinearLayout) this.findViewById(R.id.ll_no_connect_bg);
		
		etherNetView = (EtherNetView) this.findViewById(R.id.ll_ethernet_info);
		wifiView = (WifiSettingView) this.findViewById(R.id.fl_wifi_info);
	}
	
	
	private void setEvent(){
		llWifiConn.setOnClickListener(this);
		llEtherConn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		if(llWifiConn==v){
			if(!wifiView.isShown()){
				mWifiManager.setWifiEnabled(true);
				mEthernetUtil.setEnable(false);
				etherNetView.setVisibility(View.INVISIBLE);
				wifiView.setVisibility(View.VISIBLE);
				llNoNetWork.setVisibility(View.GONE);

				llWifiConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_select));
				llEtherConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_nomarl));
			}
		}else if(llEtherConn==v){
			if(!etherNetView.isShown()){
				
				llNoNetWork.setVisibility(View.GONE);
				
				wifiView.setVisibility(View.INVISIBLE);
				etherNetView.setVisibility(View.VISIBLE);
				mWifiManager.setWifiEnabled(false);
				mEthernetUtil.setEnable(true);
				Toast.makeText(this, this.getResources().getString(R.string.ethernet_connecting),Toast.LENGTH_LONG).show();
				
				llWifiConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_nomarl));
				llEtherConnContent.setBackgroundColor(this.getResources().getColor(R.color.setting_tab_select));
			}
			
		}
	}
	
}
