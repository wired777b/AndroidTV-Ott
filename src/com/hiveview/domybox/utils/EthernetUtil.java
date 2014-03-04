package com.hiveview.domybox.utils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.ethernet.EthernetDevInfo;
import android.net.ethernet.EthernetManager;
import android.util.Log;

import com.hiveview.domybox.service.entity.EthernetEntity;


public class EthernetUtil {
	private Context context = null;
	
	public EthernetUtil(Context context)
	{
		this.context = context;
		mEthernetManager = (EthernetManager) context.getSystemService(Context.ETH_SERVICE);
	}
	
	public void setEnable(boolean enable){
		mEthernetManager.setEthEnabled(enable);
	}
	
//	private Object mManager = context.getSystemService("ethernet");
	private Object mManager = null;
	private Object mEthInfo;
	
	private EthernetManager mEthernetManager;
	
	
	
	public void getDevicesInfo(){
		Log.i("tag", "etherNetState code:"+mEthernetManager.getEthState());
		DhcpInfo info = mEthernetManager.getDhcpInfo();
		DhcpInfo info1 = mEthernetManager.getDhcpInfo();
		DhcpInfo info2 = mEthernetManager.getDhcpInfo();
		DhcpInfo info3 = mEthernetManager.getDhcpInfo();
		DhcpInfo info4 = mEthernetManager.getDhcpInfo();
		mEthernetManager.getEthState();
		String[] devicesNames = mEthernetManager.getDeviceNameList();
		String connectMode = mEthernetManager.getSavedEthConfig().getConnectMode();
		Log.i("tag", "etherNetState code:"+mEthernetManager.getSavedEthConfig().toString());
		Log.i("tag", "etherNetState getIfName:"+info.ipAddress);
		Log.i("tag", "etherNetState getIfName:"+info.gateway);
		Log.i("tag", "etherNetState getIpAddress:"+info.netmask);
		Log.i("tag", "etherNetState getIpAddress:"+info.dns1);
		Log.i("tag", "etherNetState getIpAddress:"+info.dns2);
	}
	
	
//	public boolean isEtherNetEnable(){
//		return mEthernetManager.
//	}
	
	public EthernetDevInfo getEthernetDevInfo(){
		return mEthernetManager.getSavedEthConfig();
	}
	
	public static DhcpInfo dhcpInfo;
	
	public DhcpInfo getDhcpInfo(){
//		if(dhcpInfo==null)
			dhcpInfo = mEthernetManager.getDhcpInfo();
		return dhcpInfo;
	}
	
	public String connectMode(){
		return mEthernetManager.getSavedEthConfig().getConnectMode();
	}
	
	
	public boolean isEnable(){
		int code = mEthernetManager.getEthState();
		if(code == EthernetManager.ETH_STATE_ENABLED){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 通用以太网连接
	 * @throws Exception 
	 * 
	 */
	public void ethernetConnectCommon(EthernetEntity entity) throws Exception
	{
	    Class clazz = Class.forName("android.net.ethernet.EthernetDevInfo");
        Method getSavedEthConfig = mManager.getClass().getMethod("getSavedEthernetIpInfo", null);
        mEthInfo = getSavedEthConfig.invoke(mManager, null);
        //获取设备名称
        Method getIfName = mEthInfo.getClass().getMethod("getIfName", null);
        String ifName = getIfName.invoke(mEthInfo, null) == null ?"eth0":getIfName.invoke(mEthInfo, null).toString();
        
        clazz.getMethod("setIfName", String.class).invoke(mEthInfo, ifName);
    	//info.setIfName(mEthInfo.getIfName());
        //info.setConnectMode(mEthInfo.getConnectMode());
        clazz.getMethod("setConnectMode", String.class).invoke(mEthInfo, entity.getConnectMode());
        //info.setIpAddress("192.168.0.45");
        clazz.getMethod("setIpAddress", String.class).invoke(mEthInfo, entity.getIp());
        
        //info.setRouteAddr(tv_netset_wg_value.getText().toString());
        clazz.getMethod("setRouteAddr", String.class).invoke(mEthInfo, entity.getRoute());
        
        //info.setDnsAddr(tv_netset_dns_value.getText().toString());
        clazz.getMethod("setDnsAddr", String.class).invoke(mEthInfo, entity.getDns());
        
        //info.setNetMask(tv_netset_ym_value.getText().toString());
        clazz.getMethod("setNetMask", String.class).invoke(mEthInfo, entity.getNetMask());
		Method UpdateEthDevInfo = mManager.getClass().getMethod("updateEthDevInfo", Class.forName("android.net.ethernet.EthernetDevInfo"));
		UpdateEthDevInfo.invoke(mManager, mEthInfo);
		Method setEthEnabled = mManager.getClass().getMethod("setEthEnabled", boolean.class);
		setEthEnabled.invoke(mManager, true);
	}
	
	/**
	 * 银河连接
	 * @param entity
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws UnknownHostException 
	 * @throws IllegalArgumentException 
	 */
	public void ethernetConnectYH(EthernetEntity entity) throws Exception
	{
		Class clazz = Class.forName("android.net.DhcpInfo");
		//IP
		clazz.getDeclaredField("ipAddress").set(mEthInfo, inetAddressToInt(InetAddress.getByAddress(ipToBytesByInet(entity.getIp()))));
		//掩码
		clazz.getDeclaredField("gateway").set(mEthInfo, inetAddressToInt(InetAddress.getByAddress(ipToBytesByInet(entity.getRoute()))));
		//网关
		clazz.getDeclaredField("netmask").set(mEthInfo, inetAddressToInt(InetAddress.getByAddress(ipToBytesByInet(entity.getNetMask()))));
		//DNS
		clazz.getDeclaredField("dns1").set(mEthInfo, inetAddressToInt(InetAddress.getByAddress(ipToBytesByInet(entity.getDns()))));
		Method saveEthernetIpInfo = mManager.getClass().getMethod("saveEthernetIpInfo", Class.forName("android.net.DhcpInfo"),String.class);
        
		//银河的盒子没有设备名
		saveEthernetIpInfo.invoke(mManager, mEthInfo,"eth0");
        
        Method enableEthernet = mManager.getClass().getMethod("enableEthernet", boolean.class);
        enableEthernet.invoke(mManager, true);
	}
	
	
	/**
	 * 将IP字符串转换成byte数组
	 * @param ipAddr
	 * @return
	 */
    public static byte[] ipToBytesByInet(String ipAddr) 
    {
	    try 
	    {
	        return InetAddress.getByName(ipAddr).getAddress();
	    } 
	    catch (Exception e) 
	    {
	       throw new IllegalArgumentException(ipAddr + " is invalid IP");
	    }
	   }
    /**
     * IP转化成int
     * @param inetAddr
     * @return
     * @throws IllegalArgumentException
     */
    public static int inetAddressToInt(InetAddress inetAddr) throws IllegalArgumentException 
    {
        byte [] addr = inetAddr.getAddress();
        if (addr.length != 4) 
        {
            throw new IllegalArgumentException("Not an IPv4 address");
        }
        return ((addr[3] & 0xff) << 24) | ((addr[2] & 0xff) << 16) |
                ((addr[1] & 0xff) << 8) | (addr[0] & 0xff);
    }
    
    public static  boolean getAvailableNetWorkInfo(Context context) {
        if(context == null)
        	return false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
      if (activeNetInfo != null && activeNetInfo.isAvailable()) 
                return true;
       else
                return false;
        }
    
}
