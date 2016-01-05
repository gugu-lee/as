package net.x_talker.as.im.container.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sip.address.Address;

public class XTalkerSipMsg implements Serializable {


	private static final long serialVersionUID = -5485137068333525270L;

	private String callId;

	private String toCallId;

	private String contentType;

	private Object content;

	private Address from;
	private Address to;
	private Date submitTime;
	private Date doneDate;
	private String msgEncode;
	private List<String> routes;
	private MessageBody messageBody;
	private int resendTimes;
	private int status;
	private int priority;

	/** 原始的sip数据 */
	private byte[] rawData;

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}

	// 支持关机短信延时发送字段，记录关机后再次开机时间
	private long rebootTime = 0;
	private int checkCount = 0;

	public XTalkerSipMsg(String callId, Address from, Address to, Object content, Timestamp submitTime) {
		this.callId = callId;
		this.from = from;
		this.to = to;
		this.content = content;
		this.submitTime = submitTime;
		this.rebootTime = 0;
	}

	public XTalkerSipMsg() {
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Address getFrom() {
		return from;
	}

	public void setFrom(Address from) {
		this.from = from;
	}

	public Address getTo() {
		return to;
	}

	public void setTo(Address to) {
		this.to = to;
	}

	public String getMsgEncode() {
		return msgEncode;
	}

	public void setMsgEncode(String msgEncode) {
		this.msgEncode = msgEncode;
	}

	public List<String> getRoutes() {
		return routes;
	}

	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}

	public void addRoute(String route) {
		if (this.routes == null) {
			this.routes = new ArrayList<String>();
		}
		this.routes.add(route);
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}

	public MessageBody getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(MessageBody messageBody) {
		this.messageBody = messageBody;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getToCallId() {
		return toCallId;
	}

	public void setToCallId(String toCallId) {
		this.toCallId = toCallId;
	}

	public int getResendTimes() {
		return resendTimes;
	}

	public void setResendTimes(int resendTimes) {
		this.resendTimes = resendTimes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setRebootTime() {
		this.rebootTime = System.currentTimeMillis();
	}

	public long getRebootTime() {
		return this.rebootTime;
	}

	public void increase() {
		this.checkCount++;
	}

	public int checkCount() {
		return this.checkCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof XTalkerSipMsg)) {
			return false;
		}

		XTalkerSipMsg sipMsg = (XTalkerSipMsg) obj;

		if ((this.callId == null && sipMsg.getCallId() != null) || (this.to == null && sipMsg.getTo() != null)
				|| (this.from == null && sipMsg.getFrom() != null)
				|| (this.content == null && sipMsg.getContent() != null)
				|| (this.submitTime == null && sipMsg.getSubmitTime() != null)
				|| (this.toCallId == null && sipMsg.getToCallId() != null)
				|| (this.messageBody == null && sipMsg.getMessageBody() != null)) {
			return false;
		}

		if (((this.callId == null && sipMsg.getCallId() == null) || this.callId.equals(sipMsg.getCallId()))
				&& ((this.toCallId == null && sipMsg.getToCallId() == null)
						|| this.toCallId.equals(sipMsg.getToCallId()))
				&& ((this.from == null && sipMsg.getFrom() == null) || this.from.equals(sipMsg.getFrom()))
				&& ((this.to == null && sipMsg.getTo() == null) || this.to.equals(sipMsg.getTo()))
				&& ((this.submitTime == null && sipMsg.getSubmitTime() == null)
						|| this.submitTime.equals(sipMsg.getSubmitTime()))
				&& ((this.messageBody == null && sipMsg.getMessageBody() == null)
						|| this.messageBody.equals(sipMsg.getMessageBody()))
				&& this.status == sipMsg.getStatus()) {
			return true;
		}
		return false;

	}
}
