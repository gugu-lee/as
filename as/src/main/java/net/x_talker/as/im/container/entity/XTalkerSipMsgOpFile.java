
package net.x_talker.as.im.container.entity;

import java.io.Serializable;

public class XTalkerSipMsgOpFile implements Serializable {

	private static final long serialVersionUID = -5544878789069434816L;

	private XTalkerSipMsg sipMsg;
	private int operate;

	public XTalkerSipMsgOpFile(XTalkerSipMsg sipMsg, int operate) {
		this.sipMsg = sipMsg;
		this.operate = operate;
	}

	public void setSipMsg(XTalkerSipMsg sipMsg) {
		this.sipMsg = sipMsg;
	}

	public XTalkerSipMsg getSipMsg() {
		return sipMsg;
	}

	public void setOperate(int operate) {
		this.operate = operate;
	}

	public int getOperate() {
		return operate;
	}

}
