package net.x_talker.as.common.vo;

public class ValidateError implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5123860324674948142L;
	private Integer id;
	private String errorMessage;
	public ValidateError() {}	
	public ValidateError(String errorMessage) {
		this.errorMessage = errorMessage;
	}		
	/** 错误ID */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 *  错误消息.
	 * */
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
