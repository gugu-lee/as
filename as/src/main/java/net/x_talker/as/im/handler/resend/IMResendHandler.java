package net.x_talker.as.im.handler.resend;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

public interface IMResendHandler {

	/**
	 * 重发短消息
	 * 
	 * @param sipMsg
	 * @param responseCode
	 */
	public void resendIM(XTalkerSipMsg sipMsg);
}
