package com.hiveview.domybox.service.exception;


/**
 * 异常基类（暂时还没用上）
 * @author Arashmen
 *
 */
public class BaseException extends RuntimeException{
	
	protected int errorCode ;
	
	/**
	 * serial UID 
	 */
	private static final long serialVersionUID = 1L;


	public BaseException() {
		super();
	}
	
	public BaseException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	/**
	 * Constructor
	 * @param e
	 */
	public BaseException(Throwable e){
		super(e);
	}
	
	
	protected BaseException(String detailMessage) {
		super(detailMessage);
	}
	
	protected BaseException(String errorMessage, Throwable t) {
		super(errorMessage, t);
	}
	
	
	public String getMessage(){
		return super.getMessage();
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode){
		this.errorCode = errorCode;
	}

}
