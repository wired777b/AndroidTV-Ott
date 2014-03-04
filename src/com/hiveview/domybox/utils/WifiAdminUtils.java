package com.hiveview.domybox.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.service.net.HttpTaskManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiAdminUtils {
	public static final int CMNET 	= 100;
	public static final int CMWAP 	= 102;
	public static final int WIFI 	= 103;
	
	public WifiManager mWifiManager;  
    //定义一个WifiInfo对象  
    private WifiInfo mWifiInfo;  
    //扫描出的网络连接列表  
    private List<ScanResult> mWifiList;  
    //网络连接列表  
    private List<WifiConfiguration> mWifiConfigurations;  
	private Context mContext;  
	WifiLock mWifiLock;
    
    public WifiAdminUtils(Context context){  
    	mContext=context;
        //取得WifiManager对象  
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        //取得WifiInfo对象  
        mWifiInfo=mWifiManager.getConnectionInfo();  
    }  

    /**打开wifi*/  
    public void openWifi(){  
        if(!mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(true);  
        }  
    }  
    	
    /**关闭wifi*/  
    public void closeWifi(){  
        if(!mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(false);  
        }  
    }  
     
     	/**检查当前wifi状态*/    
    public int checkState() {    
        return mWifiManager.getWifiState();    
    }    
    
    //锁定wifiLock  
    public void acquireWifiLock(){  
        mWifiLock.acquire();  
    }  
    
    //解锁wifiLock  
    public void releaseWifiLock(){  
        //判断是否锁定  
        if(mWifiLock.isHeld()){  
            mWifiLock.acquire();  
        }  
    }  
    //创建一个wifiLock  
    public void createWifiLock(){  
        mWifiLock=mWifiManager.createWifiLock("test");  
    }  
    
    //得到配置好的网络  
    public List<WifiConfiguration> getConfiguration(){  
        return mWifiConfigurations;  
    }  
    //指定配置好的网络进行连接  
    public void connetionConfiguration(int index){  
        if(index > mWifiConfigurations.size()){  
            return ;  
        }  
        //连接配置好指定ID的网络  
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);  
    }  
    
    public void startScan(){  
        mWifiManager.startScan();  
        //得到扫描结果  
        mWifiList=mWifiManager.getScanResults();  
        //得到配置好的网络连接  
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();  
    }  
    
    //得到网络列表  
    public List<ScanResult> getWifiList(){  
        return mWifiList;  
    }  
    
    //查看扫描结果  
    public StringBuffer lookUpScan(){  
        StringBuffer sb=new StringBuffer();  
        for(int i=0;i<mWifiList.size();i++){  
            sb.append("Index_" + new Integer(i + 1).toString() + ":");  
             // 将ScanResult信息转换成一个字符串包    
            // 其中把包括：BSSID、SSID、capabilities、frequency、level    
            sb.append((mWifiList.get(i)).toString()).append("\n");  
        }  
        return sb;    
    }  
    
    public String getMacAddress(){  
        return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();  
    }  
    public String getBSSID(){  
        return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();  
    }  
    public int getIpAddress(){  
        return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();  
    }  
    //得到连接的ID  
    public int getNetWordId(){  
        return (mWifiInfo==null)?0:mWifiInfo.getNetworkId();  
    }  
    //得到wifiInfo的所有信息  
    public String getWifiInfo(){  
        return (mWifiInfo==null)?"NULL":mWifiInfo.toString();  
    }  
    //添加一个网络并连接  
    public void addNetWork(WifiConfiguration configuration){  
        int wcgId=mWifiManager.addNetwork(configuration);  
        mWifiManager.enableNetwork(wcgId, true);  
    }  
    //断开指定ID的网络  
    public void disConnectionWifi(int netId){  
        mWifiManager.disableNetwork(netId);  
        mWifiManager.disconnect();  
    }  
    
    /**
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络2：wap网络3：net网络
     * @param context
     * @return
     */ 
    public static int getAPNType(Context context){ 

        int netType = -1;  

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 

        if(networkInfo==null){ 

            return netType; 

        } 

        int nType = networkInfo.getType(); 

        if(nType==ConnectivityManager.TYPE_MOBILE){ 

            Log.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is "+networkInfo.getExtraInfo()); 

            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){ 
                netType = CMNET; 
            } 
            else{ 
                netType = CMWAP; 
            } 
        } 

        else if(nType==ConnectivityManager.TYPE_WIFI){ 
            netType = WIFI; 
        } 

        return netType; 

    } 
    
    
//    获取网络是否可用
    public static boolean isConnect() { 
    	
    	InetAddress address;
		try {
			address = InetAddress.getByName("www.baidu.com");
			System.out.println(address);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

    }
    	
    
    	
//        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
//	    try { 
//	        ConnectivityManager connectivity = (ConnectivityManager) PengDoctorApplication.mContext .getSystemService(Context.CONNECTIVITY_SERVICE); 
//	        if (connectivity != null) { 
//	            // 获取网络连接管理的对象 
//	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
//	            if (info != null&& info.isConnected()) { 
//	                // 判断当前网络是否已经连接 
//	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
//	                    return true; 
//	                } 
//	            } 
//	        } 
//	    } catch (Exception e) { 
//	    	// TODO: handle exception 
//	    Log.v("error",e.toString()); 
//	    } 
//	    return false; 
//	} 
    
}  