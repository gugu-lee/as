package net.x_talker.as.im.hanlder.contenttype;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;

import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;

import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.MessageBodyParseUtil;

public class TextplainType implements ContentType {

	@Override
	public byte[] encodeReceipt(XTalkerSipMsg sipMsg) {
		return null;
	}

	@Override
	public MessageBody parseMessageBody(Request sipRequest) {
		ShortMessage msg = new ShortMessage();
		String msgText = MessageBodyParseUtil.getContentStr(sipRequest);
		msg.setMessageId(((CallIdHeader) sipRequest.getHeader(CallIdHeader.NAME)).getCallId());
		msg.setText(msgText);
		return msg;
	}

}
