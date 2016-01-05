package net.x_talker.as.im.handler.online;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.servlet.IMResponse;

/**
 * 接收用户是否在线处理
 */
public interface IMOnlineHandler {

	/**
	 * 接收用户是否在线处理
	 */
	public IMResponse onlineHandle(XTalkerSipMsg sipMsg);
}
