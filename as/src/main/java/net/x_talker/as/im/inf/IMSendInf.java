package net.x_talker.as.im.inf;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

/**
 * 短消息发送接口
 * 
 * @author zengqiaowen
 *
 */
public interface IMSendInf {

	public void sendIMMessage(XTalkerSipMsg sipMsg);
}
