package net.x_talker.as.im.handler;

import javax.sip.message.Request;

/**
 * 短消息核心业务处理定义接口
 * 
 * @author zengqiaowen
 *
 */
public interface IMSenderHandler {

	public int handleIM(Request request);
}
