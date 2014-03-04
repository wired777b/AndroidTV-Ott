package com.hiveview.domybox.view.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.ethernet.EthernetDevInfo;
import android.net.ethernet.EthernetManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.EthernetUtil;

public class EtherNetView extends LinearLayout {
	//连上网络的状态  
	private static final int ETH_CONNECTED = 1;
	//拔掉网线的状态  断网状态
	private static final int ETH_DISENABLE = 4;
	//插入网线的状态   open的状态
	private static final int ETH_ENABLE = 5;
	private static final int ETH_CHANGING = 7;
	private boolean isClosed = false;
	//有线是否能用
	private boolean isEnable = true;
	
	private Context mContext;
	private EtherNetReceiver mEtherNetReceiver;
	//有线的ip
	private TextView tv_netset_ip_value;
	//有线的掩码
	private TextView tv_netset_ym_value;
	//有线的网关
	private TextView tv_netset_wg_value;
	//dns
	private TextView tv_netset_dns_value;
	//是否连接的状态
	private ImageView iv_set_logo;
	
	private TextView tvConnecting;
	
	public EtherNetView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		mContext = context;
		init();
		
	}

	public EtherNetView(Context context) {
		
		super(context);
		mContext = context;
		init();
		
	}
	
	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		initView();
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mContext.unregisterReceiver(mEtherNetReceiver);
	}

	private void init(){
		
		mEtherNetReceiver = new EtherNetReceiver();
		IntentFilter ether_filter = new IntentFilter();
		ether_filter.addAction(EthernetManager.ETH_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mEtherNetReceiver, ether_filter);
		
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
//		if(visibility==View.VISIBLE){
//			if(!isEnable)
//				Toast.makeText(mContext, mContext.getResources().getString(R.string.extract_ether),Toast.LENGTH_LONG).show();
//		}
	}
	
	private void initView(){
		
		tv_netset_ip_value = (TextView) findViewById(R.id.tv_netset_ip_value);
		tv_netset_ym_value = (TextView) findViewById(R.id.tv_netset_ym_value);
		tv_netset_wg_value = (TextView) findViewById(R.id.tv_netset_wg_value);
		tv_netset_dns_value = (TextView) findViewById(R.id.tv_netset_dns_value);
		
		tvConnecting = (TextView) findViewById(R.id.tv_ether_waiting);
		
		iv_set_logo = (ImageView) findViewById(R.id.iv_set_logo);
	}
	
	
	//有线广播 监听广播为插拔网线 网络状态的改变
    public class EtherNetReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			 if (EthernetManager.ETH_STATE_CHANGED_ACTION.equals(intent.getAction())){
//				 Log.i("msg", "intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)----"+intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0));
//				 Toast.makeText(mContext, intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)+"--",Toast.LENGTH_SHORT).show();
				 if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==ETH_ENABLE){
					 isEnable = true;
//					 Toast.makeText(mContext, mContext.getResources().getString(R.string.insert_ether_waiting),Toast.LENGTH_LONG).show();
				 }else if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==ETH_DISENABLE){
					 isClosed = true;
					 isEnable = false;
					 notifacationEtherView(false);
//					 Toast.makeText(mContext, mContext.getResources().getString(R.string.extract_ether),Toast.LENGTH_LONG).show();
				 }else if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==ETH_CONNECTED && isEnable){
					 isClosed = false;
					 notifacationEtherView(true);
				 }else if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==ETH_CHANGING && !isClosed && isEnable){
					 notifacationEtherView(true);
				 }
			 }
		}

    }
    
    
    public void setConnectVisible(){
    	tvConnecting.setVisibility(View.VISIBLE);
    }
    
    private void notifacationEtherView(boolean enable) {
    	
    	EthernetUtil ethernetUtil = new EthernetUtil(mContext);
		if(ethernetUtil.isEnable() && enable && isEnable){
//			tvConnecting.setVisibility(View.GONE);
			iv_set_logo.setBackgroundResource(R.drawable.right);
			setEtherInfo();
		}else if(!enable){
//			tvConnecting.setVisibility(View.GONE);
			iv_set_logo.setBackgroundResource(R.drawable.wrong);
			tv_netset_ip_value.setText("");
			tv_netset_ym_value.setText("");
			tv_netset_wg_value.setText("");
			tv_netset_dns_value.setText("");
			 
		}
		
	}
    
    public void setEtherInfo(){
    	
    	EthernetUtil ethernetUtil = new EthernetUtil(mContext);
    	if(EthernetDevInfo.ETH_CONN_MODE_DHCP.equals(ethernetUtil.connectMode())){
			DhcpInfo dhcpInfo = ethernetUtil.getDhcpInfo();
			tv_netset_ip_value.setText(ipIntToString(dhcpInfo.ipAddress));
			tv_netset_ym_value.setText(ipIntToString(dhcpInfo.netmask));
			tv_netset_wg_value.setText(ipIntToString(dhcpInfo.serverAddress));
			tv_netset_dns_value.setText(ipIntToString(dhcpInfo.dns1));
		}else if(EthernetDevInfo.ETH_CONN_MODE_MANUAL.equals(ethernetUtil.connectMode())){
			tv_netset_ip_value.setText(ethernetUtil.getEthernetDevInfo().getIpAddress());
			tv_netset_ym_value.setText(ethernetUtil.getEthernetDevInfo().getNetMask());
			tv_netset_wg_value.setText(ethernetUtil.getEthernetDevInfo().getRouteAddr());
			tv_netset_dns_value.setText(ethernetUtil.getEthernetDevInfo().getDnsAddr());
		}
    	
    }
    
    private String ipIntToString(int ipAddress){
    	
		String ipString = "";
		  
		if (ipAddress != 0) {  
		       ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."   
		        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));  
		}  
		return ipString;
		
	}
	

}
