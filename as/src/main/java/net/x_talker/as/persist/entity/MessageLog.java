package net.x_talker.as.persist.entity;

import java.sql.Timestamp;

public class MessageLog implements java.io.Serializable {
	
	/**
	 * Comments for <code>serialVersionUID</code>
	 * 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = 7923390175886195308L;
	private String messageId;
	private String callId;
	private Timestamp createTime;
	private String from;
	private String to;
	private Integer isRegularly;
	private String messageContent;
	private Timestamp regularlyTime;
	private String subject;
	private Timestamp expireTime;
	private MessageState state;
	
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public Timestamp getRegularlyTime() {
		return regularlyTime;
	}
	public void setRegularlyTime(Timestamp regularlyTime) {
		this.regularlyTime = regularlyTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Timestamp getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}
	public void setState(MessageState state) {
		this.state = state;
	}
	public MessageState getState() {
		return state;
	}
	public void setIsRegularly(Integer isRegularly) {
		this.isRegularly = isRegularly;
	}
	public Integer getIsRegularly() {
		return isRegularly;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageId() {
		return messageId;
	}
	
}
