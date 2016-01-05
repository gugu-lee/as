package net.x_talker.as.im.hanlder.contenttype;

import org.apache.log4j.Logger;

public class ContentTypeFactory {

	private static Logger logger = Logger.getLogger(ContentTypeFactory.class);

	public static ContentType createContentType(String typeStr) {
		ContentTypeEnum type = null;
		if (typeStr == null || typeStr.equals("")) {
			return null;
		}
		for (ContentTypeEnum typeEnum : ContentTypeEnum.values()) {
			if (typeEnum.getTypeString().equals(typeStr)) {
				type = typeEnum;
			}
		}
		if (type == null) {
			logger.warn("content type string is not support, type is:" + typeStr);
			return null;
		}

		return type.createContentType();
	}

	enum ContentTypeEnum {

		Application_text {
			@Override
			public String getTypeString() {
				return "application/text";
			}

			@Override
			public ContentType createContentType() {
				return new ApplicationTextType();
			}

		},
		Text_xml_smml {
			@Override
			public String getTypeString() {
				return "text/xml-smml";
			}

			@Override
			public ContentType createContentType() {
				return new TextXmlSmmlType();
			}

		},
		Message_CPIM {
			@Override
			public String getTypeString() {
				return "message/CPIM";

			}

			@Override
			public ContentType createContentType() {
				return new MessageCPIMType();
			}

		},
		Text_plain {
			@Override
			public String getTypeString() {
				return "text/plain";
			}

			@Override
			public ContentType createContentType() {
				return new TextplainType();
			}

		};
		public abstract String getTypeString();

		public abstract ContentType createContentType();

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
}
