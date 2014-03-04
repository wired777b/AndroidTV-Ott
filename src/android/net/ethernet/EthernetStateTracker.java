package android.net.ethernet;
/*package com.hiveview.domybox.Ethernet;

import java.net.UnknownHostException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.ethernet.EthernetDevInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EthernetStateTracker extends NetworkStateTracker {
	private static final String TAG="EthernetStateTracker";
	private static final int EVENT_DHCP_START			= 0;
	private static final int EVENT_INTERFACE_CONFIGURATION_SUCCEEDED = 1;
	private static final int EVENT_INTERFACE_CONFIGURATION_FAILED	= 2;
	private static final int EVENT_HW_CONNECTED			= 3;
	private static final int EVENT_HW_DISCONNECTED			= 4;
	private static final int EVENT_HW_PHYCONNECTED			= 5;
	private static final int NOTIFY_ID				= 6;

	private EthernetManager mEM;
	private boolean mServiceStarted;

	private boolean mStackConnected;
	private boolean mHWConnected;
	private boolean mInterfaceStopped;
	private DhcpHandler mDhcpTarget;
	private String mInterfaceName ;
	private DhcpInfo mDhcpInfo;
	private EthernetMonitor mMonitor;
	private String[] sDnsPropNames;
	private boolean mStartingDhcp;
	private NotificationManager mNotificationManager;
	private Notification mNotification;

	public EthernetStateTracker(Context context, Handler target) {

		super(context, target, 9, 0, "ETH", "");
		Log.i(TAG,"Starts...");


		if(EthernetNative.initEthernetNative() != 0 )
		{
			Log.e(TAG,"Can not init ethernet device layers");
			return;
		}
		Log.i(TAG,"Successed");
		mServiceStarted = true;
		HandlerThread dhcpThread = new HandlerThread("DHCP Handler Thread");
		dhcpThread.start();
		mDhcpTarget = new DhcpHandler(dhcpThread.getLooper(), this);
		mMonitor = new EthernetMonitor(this);
		mDhcpInfo = new DhcpInfo();
	}

	public boolean stopInterface(boolean suspend) {
		if (mEM != null) {
			EthernetDevInfo info = mEM.getSavedEthConfig();
			if (info != null && mEM.ethConfigured())
			{
				synchronized (mDhcpTarget) {
					mInterfaceStopped = true;
					Log.i(TAG, "stop dhcp and interface");
					mDhcpTarget.removeMessages(EVENT_DHCP_START);
					String ifname = info.getIfName();

					if (!NetworkUtils.stopDhcp(ifname)) {
						Log.e(TAG, "Could not stop DHCP");
						}
					NetworkUtils.resetConnections(ifname);
					if (!suspend)
						NetworkUtils.disableInterface(ifname);
				}
			}
		}

		return true;
	}

	private static int stringToIpAddr(String addrString) throws UnknownHostException {
		try {
		if (addrString == null)
			return 0;
			String[] parts = addrString.split("\\.");
			if (parts.length != 4) {
				throw new UnknownHostException(addrString);
			}

			int a = Integer.parseInt(parts[0])	  ;
			int b = Integer.parseInt(parts[1]) <<  8;
			int c = Integer.parseInt(parts[2]) << 16;
			int d = Integer.parseInt(parts[3]) << 24;

			return a | b | c | d;
		} catch (NumberFormatException ex) {
			throw new UnknownHostException(addrString);
		}
	}

	private boolean configureInterface(EthernetDevInfo info) throws UnknownHostException {

		mStackConnected = false;
		mHWConnected = false;
		mInterfaceStopped = false;
		mStartingDhcp = true;
		sDnsPropNames = new String[] {
						"dhcp." + mInterfaceName + ".dns1",
						"dhcp." + mInterfaceName + ".dns2"};
		if (info.getConnectMode().equals(EthernetDevInfo.ETH_CONN_MODE_DHCP)) {
			Log.i(TAG, "trigger dhcp for device " + info.getIfName());

				mDhcpTarget.sendEmptyMessage(EVENT_DHCP_START);

			} else {
				int event;

			mDhcpInfo.ipAddress =  stringToIpAddr(info.getIpAddress());
			mDhcpInfo.gateway = stringToIpAddr(info.getRouteAddr());
			mDhcpInfo.netmask = stringToIpAddr(info.getNetMask());
			mDhcpInfo.dns1 = stringToIpAddr(info.getDnsAddr());
			mDhcpInfo.dns2 = 0;

			Log.i(TAG, "set ip manually " + mDhcpInfo.toString());
			NetworkUtils.removeDefaultRoute(info.getIfName());
			if (NetworkUtils.configureInterface(info.getIfName(), mDhcpInfo)) {
				event = EVENT_INTERFACE_CONFIGURATION_SUCCEEDED;
				Log.v(TAG, "Static IP configuration succeeded");
			} else {
				event = EVENT_INTERFACE_CONFIGURATION_FAILED;
				Log.v(TAG, "Static IP configuration failed");

			}
			this.sendEmptyMessage(event);
		}
		return true;
	}


	public boolean resetInterface()  throws UnknownHostException{
		
		 * This will guide us to enabled the enabled device
		 
		if (mEM != null) {
			EthernetDevInfo info = mEM.getSavedEthConfig();
			if (info != null && mEM.ethConfigured())
			{
				synchronized(this) {
					mInterfaceName = info.getIfName();
					Log.i(TAG, "reset device " + mInterfaceName);
					NetworkUtils.resetConnections(mInterfaceName);
					 // Stop DHCP
						if (mDhcpTarget != null) {
						mDhcpTarget.removeMessages(EVENT_DHCP_START);
					}
					if (!NetworkUtils.stopDhcp(mInterfaceName)) {
						Log.e(TAG, "Could not stop DHCP");
					}
					configureInterface(info);
				}
			}
		}
		return true;
	}

	@Override
	public String[] getNameServers() {
		Log.i(TAG,"get dns from " + sDnsPropNames);
		 return getNameServerList(sDnsPropNames);
	}

	@Override
	public String getTcpBufferSizesPropName() {
		// TODO Auto-generated method stub
		return "net.tcp.buffersize.default";
	}

	public void StartPolling() {
		Log.i(TAG, "start polling");
		mMonitor.startMonitoring();
	}
	@Override
	public boolean isAvailable() {
		//Only say available if we have interfaces and user did not disable us.
		return ((mEM.getTotalInterface() != 0) && (mEM.getEthState() != EthernetManager.ETH_STATE_DISABLED));
	}

	@Override
	public boolean reconnect() {
		try {
			synchronized (this) {
				if (mHWConnected && mStackConnected)
					return true;
			}
			if (mEM.getEthState() != EthernetManager.ETH_STATE_DISABLED ) {
				// maybe this is the first time we run, so set it to enabled
				mEM.setEthEnabled(true);
				if (!mEM.ethConfigured()) {
					mEM.ethSetDefaultConf();
				}
				return resetInterface();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean setRadio(boolean turnOn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startMonitoring() {
		Log.i(TAG,"start to monitor the ethernet devices");
		if (mServiceStarted )	{
			mEM = (EthernetManager)mContext.getSystemService(Context.ETH_SERVICE);
			int state = mEM.getEthState();
			if (state !=mEM.ETH_STATE_DISABLED) {
				if (state == mEM.ETH_STATE_UNKNOWN){
					// maybe this is the first time we run, so set it to enabled
					mEM.setEthEnabled(true);
				} else {
					try {
						resetInterface();
					} catch (UnknownHostException e) {
						Log.e(TAG, "Wrong ethernet configuration");
					}
				}
			}
		}
	}


	@Override
	public int startUsingNetworkFeature(String feature, int callingPid,
			int callingUid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stopUsingNetworkFeature(String feature, int callingPid,
			int callingUid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean teardown() {
		if (mEM != null)
			return stopInterface(false);
		else
			return false;

	}

	private void postNotification(int event) {
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager)mContext.getSystemService(ns);
		if (mNotificationManager != null) {
			int icon;
			CharSequence title = "Ethernet Status";
			CharSequence detail;
			if(mNotification == null) {
				mNotification = new Notification();
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotification.contentIntent = PendingIntent.getActivity(mContext, 0,
						new Intent(EthernetManager.NETWORK_STATE_CHANGED_ACTION), 0);
			}
			mNotification.when = System.currentTimeMillis();

			switch (event) {
			case EVENT_HW_CONNECTED:
			case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
				mNotification.icon=icon = com.android.internal.R.drawable.connect_established;
				String ipprop="dhcp."+mInterfaceName+".ipaddress";
				String ipaddr = SystemProperties.get(ipprop);
				detail = "Ethernet is connected. IP address: " + ipaddr ;

				break;
			case EVENT_HW_DISCONNECTED:
			case EVENT_INTERFACE_CONFIGURATION_FAILED:
				mNotification.icon = icon = com.android.internal.R.drawable.connect_no;
				detail = "Ethernet is disconnected.";
				break;
			default:
				mNotification.icon=icon = com.android.internal.R.drawable.connect_creating;
				detail = "Unknown event with Ethernet.";
			}
			Log.i(TAG, "post event to notification manager "+detail);
			mNotification.setLatestEventInfo(mContext, title, detail,mNotification.contentIntent );
			Message message = mTarget.obtainMessage(EVENT_NOTIFICATION_CHANGED, 1,icon,mNotification);
			mTarget.sendMessageDelayed(message, 0);
		} else {
			Log.i(TAG, "notification manager is not up yet");
		}

	}
	public void handleMessage(Message msg) {

		synchronized (this) {
			switch (msg.what) {
			case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED:
				Log.i(TAG, "received configured events, stack: " + mStackConnected + " HW " + mHWConnected );
				mStackConnected = true;
				if (mHWConnected) {
					setDetailedState(DetailedState.CONNECTED);
					mTarget.sendEmptyMessage(EVENT_CONFIGURATION_CHANGED);
					postNotification(msg.what);
				}

			break;

			case EVENT_INTERFACE_CONFIGURATION_FAILED:
			mStackConnected = false;
			//start to retry ?
			break;
			case EVENT_HW_CONNECTED:
				Log.i(TAG, "received connected events, stack: " + mStackConnected + " HW " + mHWConnected );
				mHWConnected = true;
				if (mStackConnected) {
					setDetailedState(DetailedState.CONNECTED);
					mTarget.sendEmptyMessage(EVENT_CONFIGURATION_CHANGED);
					postNotification(msg.what);
				}

			break;
			case EVENT_HW_DISCONNECTED:
				Log.i(TAG, "received disconnected events, stack: " + mStackConnected + " HW " + mHWConnected );
				mHWConnected = false;
				setDetailedState(DetailedState.DISCONNECTED);
				stopInterface(true);
				postNotification(msg.what);
			break;
			case EVENT_HW_PHYCONNECTED:
				Log.i(TAG, "interface up event, kick off connection request");
				if (!mStartingDhcp) {
					int state = mEM.getEthState();
					if (state !=mEM.ETH_STATE_DISABLED) {
						EthernetDevInfo info = mEM.getSavedEthConfig();
						if (info != null && mEM.ethConfigured()) {
							try {
								configureInterface(info);
							} catch (UnknownHostException e) {
								 // TODO Auto-generated catch block
								 e.printStackTrace();
							}
						}
					}
				}
			break;
			}
		}
	}

	private class DhcpHandler extends Handler {
		private Handler mTrackerTarget;

		 public DhcpHandler(Looper looper, Handler target) {
				super(looper);
				mTrackerTarget = target;

			}

		  public void handleMessage(Message msg) {
				int event;

				switch (msg.what) {
					case EVENT_DHCP_START:
				synchronized (mDhcpTarget) {
					if (!mInterfaceStopped) {
						Log.d(TAG, "DhcpHandler: DHCP request started");
						if (NetworkUtils.runDhcp(mInterfaceName, mDhcpInfo)) {
							event = EVENT_INTERFACE_CONFIGURATION_SUCCEEDED;
							Log.v(TAG, "DhcpHandler: DHCP request succeeded");
								  } else {
									  event = EVENT_INTERFACE_CONFIGURATION_FAILED;
									  Log.i(TAG, "DhcpHandler: DHCP request failed: " +
										  NetworkUtils.getDhcpError());
								  }
								  mTrackerTarget.sendEmptyMessage(event);
					} else {
						mInterfaceStopped = false;
					}
					mStartingDhcp = false;
				}
				break;
				}

		  }
	}

	public void notifyPhyConnected(String ifname) {
		Log.i(TAG, "report interface is up for " + ifname);
		synchronized(this) {
			this.sendEmptyMessage(EVENT_HW_PHYCONNECTED);
		}

	}
	public void notifyStateChange(String ifname,DetailedState state) {
		Log.i(TAG, "report new state " + state.toString() + " on dev " + ifname);
		if (ifname.equals(mInterfaceName)) {

			Log.i(TAG, "update network state tracker");
			synchronized(this) {
				if (state.equals(DetailedState.CONNECTED) ) {

					this.sendEmptyMessage(EVENT_HW_CONNECTED);
				} else {

					this.sendEmptyMessage(EVENT_HW_DISCONNECTED);
				}

			}
		}
	}
}
*/