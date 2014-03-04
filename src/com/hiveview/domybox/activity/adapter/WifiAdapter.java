package com.hiveview.domybox.activity.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.MyTextView;

public class WifiAdapter extends BaseAdapter{
	private List<ScanResult> scanResultList;
	private Context context;
	private WifiInfo wifiInfo;//当前连接的wifi热点
	private List<WifiConfiguration> configs;
	
	private int onclickItem = -1;
	

	public WifiAdapter(Context context,List<ScanResult> scanResultList){
		this.scanResultList = scanResultList;
		this.context = context;
		
	}
	
	
	public void setWifiInfo(WifiInfo wifiInfo){
		this.wifiInfo = wifiInfo;
	}
	
	public void setDataSource(List<ScanResult> scanResultList){
		this.scanResultList = scanResultList;
		this.notifyDataSetChanged();
	}
	
//	public void updateWifiInfo(WifiInfo wifiInfo){
//		this.wifiInfo = wifiInfo;
//	}

	public int getOnclickItem() {
		return onclickItem;
	}


	public void setOnclickItem(int onclickItem) {
		this.onclickItem = onclickItem;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = LayoutInflater.from(context).inflate(R.layout.wifi_item, null);
		ImageView iv_wifi_state_logo = (ImageView) view.findViewById(R.id.iv_wifi_state_logo);
		ImageView iv_wifi_single_level = (ImageView) view.findViewById(R.id.iv_wifi_single_level);
//		//第一个并且是已经连接的wifi热点则显示对号
//		if(position == 0)
//		{
//			iv_wifi_state_logo.setVisibility(View.VISIBLE);
//			iv_wifi_state_logo.setImageResource(R.drawable.right);
//		}
		final MyTextView tv_wifi_name = (MyTextView) view.findViewById(R.id.tv_wifi_name);
		tv_wifi_name.setTypeface(TypefaceUtils.getCE35FontFace());
		if(scanResultList!=null && !scanResultList.isEmpty())
			tv_wifi_name.setText(scanResultList.get(position).SSID.toString());
		//连接当前的热点的ssid
		if(wifiInfo!=null && wifiInfo.getSSID().equals("\"" + scanResultList.get(position).SSID+ "\"")){
			iv_wifi_state_logo.setImageResource(R.drawable.right);
		}

		int level = calculateSignalLevel(scanResultList.get(position).level,4);
		switch (level) 
		{
		case 0:
			iv_wifi_single_level.setImageResource(R.drawable.ic_wifi_signal_1);
			break;
		case 1:
			iv_wifi_single_level.setImageResource(R.drawable.ic_wifi_signal_2);
			break;
		case 2:
			iv_wifi_single_level.setImageResource(R.drawable.ic_wifi_signal_3);
			break;
		case 3:
			iv_wifi_single_level.setImageResource(R.drawable.ic_wifi_signal_4);
			break;
		default:
			iv_wifi_single_level.setImageResource(R.drawable.ic_wifi_signal_1);
			break;
		}
		return view;
	}
	
	private static final int MIN_RSSI = -100;//信号强度
	private static final int MAX_RSSI = -55;
	public int calculateSignalLevel(int rssi, int numLevels) {

	    if (rssi <= MIN_RSSI) 
	    {
	       return 0;
	    } 
	    else if (rssi >= MAX_RSSI) 
	    {
	        return numLevels - 1;
	    } 
	    else 
	    {
	        int partitionSize = (MAX_RSSI - MIN_RSSI) / (numLevels - 1);
	        return (rssi - MIN_RSSI) / partitionSize;
	    }
    }

	@Override
	public int getCount() {

		return scanResultList!=null?scanResultList.size():0;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}


	public void setWifiConfig(List<WifiConfiguration> configs) {
		this.configs = configs;
	}
	
	public WifiConfiguration getWifiConfig(int position){
		if(scanResultList!=null && !scanResultList.isEmpty()){
			String SSID = scanResultList.get(position).SSID;
			if(configs!=null && !configs.isEmpty()){
				for(WifiConfiguration config : configs){
					String ssid = (config == null ? "" : removeDoubleQuotes(config.SSID));
					if(ssid.equals(SSID)){
						return config;
					}
				}
			}
		}
		return null;
	}
	
	
    public static String removeDoubleQuotes(String string) {
        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }

}
