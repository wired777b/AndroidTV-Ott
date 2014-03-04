package com.hiveview.domybox.utils;

import java.io.File;

import com.hiveview.domybox.common.AppConstant;

import android.os.FileObserver;

public class MyFileObserver extends FileObserver{ 
	
    private static final String TAG = "MyFileObserver";

	// 这种构造方法是默认监听所有事件的，如果使用super(String,int)这种构造方法，则int参数是要监听的事件类型。  
    // 为了防止嵌套消息调用，建议使用 super(String,int) 按需创建监控消息值  
    public MyFileObserver(String path) {  
        super(path, FileObserver.CLOSE_WRITE | FileObserver.CREATE | FileObserver.DELETE  | FileObserver.DELETE_SELF );  
        //super(path,FileObserver.ALL_EVENTS);  
    }  

    @Override  
    public void onEvent(int event, String path) {  
            switch(event){

            case android.os.FileObserver.ALL_EVENTS:
               //所有事件 相当于default的功能
            	Logger.e(TAG,"所有事件 相当于default的功能"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.CREATE:
               //文件被创建
            	Logger.e(TAG,"文件被创建"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.OPEN :
               //文件被打开
            	Logger.e(TAG,"文件被打开"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.ACCESS:
               //打开文件后，读文件内容操作
            	Logger.e(TAG,"打开文件后，读文件内容操作"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.MODIFY:
               //文件被修改
            	Logger.e(TAG,"文件被修改"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.ATTRIB:
               //未明操作
            	Logger.e(TAG,"未明操作"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.CLOSE_NOWRITE:
               //没有编辑文件，关闭
            	Logger.e(TAG,"没有编辑文件，关闭"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.CLOSE_WRITE:
               //编辑完文件，关闭
            	Logger.e(TAG,"编辑完文件，关闭"+ path);
//            	删除源文件
            	new File(AppConstant.SYSTEM_APP_PATH, path).delete();
            	
//            	mHandler.sendEmptyMessage(AppConstant.INSTALL_SUCCEE);
               break;
            case android.os.FileObserver.DELETE:
               //文件被删除
            	Logger.e(TAG,"删除"+ path);
               /**
                * 相关操作
                */
               break;
            case android.os.FileObserver.MOVED_FROM:
               //文件被移动
            	Logger.e(TAG,"移动"+ path);
               /**
                * 相关操作
                */
               break;

        }

    }  
}