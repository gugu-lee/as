package net.x_talker.as.im.servlet;

public class IMResponse {

	private int statuscode;
	private String reasonPhrase;

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}

	public String getReasonPhrase() {
		if (reasonPhrase == null) {
			return "";
		}
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
}
