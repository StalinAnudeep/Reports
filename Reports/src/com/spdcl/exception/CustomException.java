/**
 * 
 *//*
package com.spdcl.exception;

*//**
 * @author udaysurya.g
 * @since 02-02-2019
 *
 *//*
public class CustomException extends RuntimeException {

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;
	
	private String errorCode;

	private String exceptionMsg;

	public CustomException(String exceptionMsg) {
		super();
		this.errorCode = "500";
		this.exceptionMsg = exceptionMsg;
	}

	public CustomException(String errorCode, String exceptionMsg) {
		super();
		this.errorCode = errorCode;
		this.exceptionMsg = exceptionMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getExceptionMsg() {
		return this.exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

}
*/