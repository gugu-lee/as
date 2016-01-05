package net.x_talker.as.im.hanlder.contenttype;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

import javax.sip.message.Request;

import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.util.MessageBodyParseUtil;

public class ApplicationTextType implements ContentType {

	@Override
	public byte[] encodeReceipt(XTalkerSipMsg sipMsg) {
		return null;
	}

	@Override
	public MessageBody parseMessageBody(Request sipRequest) {
		MessageBody msg = new MessageBody();
		String msgText = MessageBodyParseUtil.getContentStr(sipRequest);
		msg.setText(msgText);
		return msg;
	}

}
