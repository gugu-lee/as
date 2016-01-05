package net.x_talker.as.im.container.entity;

import java.util.Date;

public class ShortMessage extends MessageBody {

	/**
	 * Comments for <code>serialVersionUID</code> 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = 3585851974724591988L;
	private String messageType;
	private boolean isReceipt;
	private int priority;
	private Date validityPeriod;
	private int longMsgId;
	private int longMsgSum;
	private int longMsgSequence;
	private int msgLength;
	private Date scheduleDeliveryTime;
	private String ieId;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public boolean getIsReceipt() {
		return isReceipt;
	}

	public void setIsReceipt(boolean isReceipt) {
		this.isReceipt = isReceipt;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Date validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public int getLongMsgId() {
		return longMsgId;
	}

	public void setLongMsgId(int longMsgId) {
		this.longMsgId = longMsgId;
	}

	public int getLongMsgSum() {
		return longMsgSum;
	}

	public void setLongMsgSum(int longMsgSum) {
		this.longMsgSum = longMsgSum;
	}

	public int getLongMsgSequence() {
		return longMsgSequence;
	}

	public void setLongMsgSequence(int longMsgSequence) {
		this.longMsgSequence = longMsgSequence;
	}

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public Date getScheduleDeliveryTime() {
		return scheduleDeliveryTime;
	}

	public void setScheduleDeliveryTime(Date scheduleDeliveryTime) {
		this.scheduleDeliveryTime = scheduleDeliveryTime;
	}

	public String getIeId() {
		return ieId;
	}

	public void setIeId(String ieId) {
		this.ieId = ieId;
	}
}
