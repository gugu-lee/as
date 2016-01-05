package net.x_talker.as.im.hanlder.contenttype;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.util.MessageBodyParseUtil;

public enum ContentTypeEnum {

	Application_text {
		@Override
		public String getTypeString() {
			return "application/text";
		}

		@Override
		public MessageBody parseMessageBody(byte[] bytes, String charEncode) {
			MessageBody msg = new MessageBody();
			try {
				String text = new String(bytes, charEncode);
				msg.setText(text);
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger(ContentTypeEnum.class).error(e.getMessage(), e);
			}
			return msg;
		}
	},
	Text_xml_smml {
		@Override
		public String getTypeString() {
			return "text/xml-smml";
		}

		@Override
		public MessageBody parseMessageBody(byte[] bytes, String charEncode) {
			return null;
		}
	},
	Message_CPIM {
		@Override
		public String getTypeString() {
			return "message/CPIM";
		}

		@Override
		public MessageBody parseMessageBody(byte[] bytes, String charEncode) {
			return null;
		}
	},
	Text_plain {
		@Override
		public String getTypeString() {
			return "text/plain";
		}

		@Override
		public MessageBody parseMessageBody(byte[] bytes, String charEncode) {
			return null;
		}
	};
	public abstract String getTypeString();

	public abstract MessageBody parseMessageBody(byte[] bytes, String charEncode);

	public static ContentTypeEnum getContentTypeEnum(String typeStr) {
		if (typeStr == null || typeStr.equals("")) {
			return null;
		}
		for (ContentTypeEnum type : ContentTypeEnum.values()) {
			if (type.getTypeString().equals(typeStr)) {
				return type;
			}
		}
		return null;
	}
}
