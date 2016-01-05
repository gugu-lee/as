package net.x_talker.as.im.container.entity;

import java.util.List;

public class MessageBody implements java.io.Serializable {

	/**
	 * Comments for <code>serialVersionUID</code> 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = -9208751285487985055L;
	// 消息ID,用于回执消息匹配
	private String messageId;
	private String text;
	private String dataCoding;
	private String ns;
	private List<String> recieptList;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getDataCoding() {
		return dataCoding;
	}

	public void setDataCoding(String dataCoding) {
		this.dataCoding = dataCoding;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public List<String> getRecieptList() {
		return recieptList;
	}

	public void setRecieptList(List<String> recieptList) {
		this.recieptList = recieptList;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MessageBody)) {
			return false;
		}

		MessageBody msg = (MessageBody) obj;

		if (this.messageId.equals(msg.getMessageId())) {
			return true;
		}
		return false;
	}
}
