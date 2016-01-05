package net.x_talker.as.im.handler.regularlysend;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

/**
 * 定时发送消息处理接口 针对需要定时发送MESSAGE请求进行延时发送处理 根据定时发送时间与当前时间差值建立Delayed对象实现定时发送处理
 * 
 * @author zengqiaowen
 *
 */
public interface IMRegularlysendHandler {

	public int regularlySendIM(XTalkerSipMsg sipMsg);
}
