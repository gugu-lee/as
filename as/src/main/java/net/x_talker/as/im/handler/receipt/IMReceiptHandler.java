package net.x_talker.as.im.handler.receipt;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

public interface IMReceiptHandler {

	/**
	 * 回执处理,根据返回的状态码生成短消息回执请求
	 * 生成回执请求时,应根据收到短消息请求中的路由消息反转后填入到回执请求中的路由头域中,以确定回执消息的路由信息
	 * 
	 * @param sipMsg
	 */
	public void receiptHandle(XTalkerSipMsg sipMsg);
}
