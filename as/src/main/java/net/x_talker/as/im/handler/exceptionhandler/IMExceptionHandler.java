package net.x_talker.as.im.handler.exceptionhandler;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

/**
 * 消息发送接收异常处理接口
 * 
 * @author zengqiaowen
 *
 */
public interface IMExceptionHandler {

	/**
	 * 消息发送收到异常响应时处理接口 <br>
	 * 统一对异常进行分析并决定处理策略 <br>
	 * 
	 * 当为接收用户不在线时,应同时将消息置入重发处理与缓存消息处理逻辑中
	 * 
	 * 
	 * @param sipMsg
	 *            发送的消息对象
	 */
	public void exceptionResponseHandle(XTalkerSipMsg sipMsg);
}
