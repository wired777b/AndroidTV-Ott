package com.hiveview.domybox.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import com.hiveview.domybox.service.entity.BaseEntity;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.HttpTaskManager;

/**
 * @author Arashmen
 *
 */
public abstract class BaseParser {
	
	protected final String ENCODE = "UTF-8";
	
	protected String errorCode;
	protected String date;
	protected HttpTaskManager manager = HttpTaskManager.getInstance();
	
	/**
	 * Invoke parser method
	 * @author Arashmen
	 * @param <T>
	 * */
	public abstract  ArrayList<? extends BaseEntity> executeToObject(InputStream in)throws ServiceException;
	
	/**
	 * check errorcode
	 * @author Arashmen
	 * @param <T>
	 * */
	public abstract String getErrorCode();
	
	
}
