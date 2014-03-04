package com.hiveview.domybox.service.exception;

/**
 * 接口异常
 * 
 * @author HiveView  - Arashmen
 *
 */
public class NetworkConnectException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NetworkConnectException() {
		super();
	}
	
	public NetworkConnectException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public NetworkConnectException(String detailMessage) {

		super(detailMessage);
	}
	
	public NetworkConnectException(String errorMessage, Throwable t) {
		super(errorMessage, t);
	}
	 
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}
