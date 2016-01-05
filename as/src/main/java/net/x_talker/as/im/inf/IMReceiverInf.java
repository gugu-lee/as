package net.x_talker.as.im.inf;

import javax.sip.message.Request;

import net.x_talker.as.im.servlet.IMResponse;

/**
 * 接收消息处理接口
 *
 */
public interface IMReceiverInf {

	/**
	 * 处理MESSAGE消息
	 * 
	 * @param sipServletRequest
	 * @return
	 */
	public IMResponse onMessageReceive(Request request);

	/**
	 * 处理REGISTER消息
	 * 
	 * @param sipServletRequest
	 */
	public void onRegisterReceive(Request request);
}
