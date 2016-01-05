package net.x_talker.as.persist.entity;

import java.sql.Timestamp;

public class MessageState implements java.io.Serializable {

	/**
	 * Comments for <code>serialVersionUID</code>
	 * 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = 4073104437136084396L;
	private String messageId;
	private Timestamp recordTime;
	private int state;
	public MessageState(){}
	public MessageState(String messageId,Timestamp recordTime,int state){
		this.messageId=messageId;
		this.recordTime=recordTime;
		this.state=state;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Timestamp getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
