package net.x_talker.as.im.hanlder.contenttype;

import javax.sip.message.Request;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.MessageBody;

/**
 * 消息类型处理接口,根据Content_type分别进行不同实现,完成不同Content_type类型消息的兼容
 */
public interface ContentType {

	public MessageBody parseMessageBody(Request request);

	public byte[] encodeReceipt(XTalkerSipMsg sipMsg);
}
