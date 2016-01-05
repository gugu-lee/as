package net.x_talker.as.sh;

import net.x_talker.as.sh.diameter.DiameterConstants;

public class ShExperimentalResultException extends Exception {
	/**
	 * Comments for <code>serialVersionUID</code> 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = -5053729427758290420L;
	private int errorCode;
	private int vendor;

	public ShExperimentalResultException(String message, int errorCode, int vendor) {
		super(message);
		this.errorCode = errorCode;
		this.vendor = vendor;
	}

	public ShExperimentalResultException(DiameterConstants.ResultCode resultCode) {
		super(resultCode.getName());
		this.errorCode = resultCode.getCode();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getVendor() {
		return vendor;
	}

	public void setVendor(int vendor) {
		this.vendor = vendor;
	}

}
