package com.hiveview.domybox.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.domybox.R;

public final class ToastUtil {

	private static Handler handler = new Handler(Looper.getMainLooper());

	private static Toast toast = null;

	private static Object synObj = new Object();

	public static void showMessage(final Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context act, final int msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context act, final String msg,final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								toast.cancel();
								toast.setText(msg);
								toast.setDuration(len);
							} else {
								toast = Toast.makeText(act, msg, len);
							}
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
				});
			}
		}).start();
	}

	public static void showMessage(final Context act, final int msg,final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								toast.cancel();
								toast.setText(msg);
								toast.setDuration(len);
							} else {
								toast = Toast.makeText(act, msg, len);
							}
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
				});
			}
		}).start();
	}
	
	
	public static void showMessageView(final Context mContext,final String msg,final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
							LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View toastRoot = mLayoutInflater.inflate(R.layout.unopenedtoast, null);  
					        TextView message = (TextView) toastRoot.findViewById(R.id.message);  
					        message.setText(msg);  
								
//							Toast toastStart = new Toast(mContext);  
//					        toastStart.setGravity(Gravity.BOTTOM, 0, 50);  
//					        toastStart.setDuration(Toast.LENGTH_LONG);  
					        toast.setView(toastRoot);  
							toast.setDuration(len);
							} else {
								toast =  new Toast(mContext); 
								LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								View toastRoot = mLayoutInflater.inflate(R.layout.unopenedtoast, null);  
						        TextView message = (TextView) toastRoot.findViewById(R.id.message);  
						        message.setText(msg);  
						        toast.setView(toastRoot);  
								toast.setDuration(len);
							}
							toast.setGravity(Gravity.BOTTOM, 0, 50);
							toast.show();
						}
					}
				});
			}
		}).start();
	}


}
