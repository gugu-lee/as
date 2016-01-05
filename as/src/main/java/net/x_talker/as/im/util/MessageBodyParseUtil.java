package net.x_talker.as.im.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sip.header.ContentEncodingHeader;
import javax.sip.message.Request;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.persist.entity.MessageLog;
import net.x_talker.as.persist.entity.MessageState;

public class MessageBodyParseUtil {

	private static Logger logger = Logger.getLogger(MessageBodyParseUtil.class);

	public static MessageLog XTalkerSipMsg2MessageLog(XTalkerSipMsg sipMsg) {
		MessageBody messageBody = sipMsg.getMessageBody();
		if (messageBody == null || !(messageBody instanceof ShortMessage)) {
			return null;
		}

		MessageLog messageLog = new MessageLog();
		messageLog.setIsRegularly(0);
		ShortMessage shortMsg = (ShortMessage) messageBody;
		if (shortMsg.getMessageId() == null) {
			return null;
		}
		Timestamp messageTime = new Timestamp(System.currentTimeMillis());
		messageLog.setMessageId(shortMsg.getMessageId());
		messageLog.setCallId(sipMsg.getCallId());
		messageLog.setFrom(sipMsg.getFrom().getURI().toString());
		messageLog.setTo(sipMsg.getTo().getURI().toString());
		messageLog.setCreateTime(messageTime);
		messageLog.setMessageContent(shortMsg.getText());

		if (shortMsg.getScheduleDeliveryTime() != null) {
			messageLog.setIsRegularly(1);
			Timestamp date = new Timestamp(shortMsg.getScheduleDeliveryTime().getTime());
			messageLog.setRegularlyTime(date);
		}
		// Timestamp expireTime = null;
		// long
		// delayTime=Util.hourToMilliseconds(PropertiesUtil.getInstance().getPropIntVal(BizConsts.CONFKEY_DEFAULT_MESSAGE_VALIDITY_PERIOD));
		// Timestamp defaultTime = new
		// Timestamp(messageTime.getTime()+delayTime);
		// if(sipServletReq.getExpires()!=0){
		// expireTime =
		// Util.getEarlierTimeNotBeforeCurrentTime(shortMsg.getValidityPeriod(),
		// new
		// Timestamp(messageTime.getTime()+sipServletReq.getExpires()*1000),defaultTime);
		// }else{
		// expireTime =
		// Util.getEarlierTime(shortMsg.getValidityPeriod(),defaultTime);
		// if(expireTime.before(new Timestamp(messageTime.getTime()))){
		// expireTime=defaultTime;
		// }
		// }
		if (shortMsg.getValidityPeriod() != null) {
			Timestamp exprieTime = new Timestamp(shortMsg.getValidityPeriod().getTime());
			messageLog.setExpireTime(exprieTime);
		}

		// TODO subject暂时不获取保存
		// state
		MessageState messageState = new MessageState();
		messageState.setMessageId(shortMsg.getMessageId());
		messageState.setRecordTime(messageTime);
		messageState.setState(0);
		messageLog.setState(messageState);

		return messageLog;
	}

	public static MessageBody parse(byte[] bytes, String charEncode) {
		try {
			ShortMessage shortMessage = new ShortMessage();
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			InputStreamReader isr = new InputStreamReader(bis, charEncode);
			BufferedReader reader = new BufferedReader(isr);
			String line;
			String extHeaderPre = "";
			;
			boolean isExtend = false;
			while ((line = reader.readLine()) != null) {
				String[] props = line.split(":");
				String header = props[0];
				if (isExtend) {
					header = header.replace(extHeaderPre + ".", "");
				} else {
					if (header.equalsIgnoreCase("NS")) {
						isExtend = true;
						extHeaderPre = props[1];
						int index = extHeaderPre.indexOf("<");
						if (index == 0) {
							logger.info("message format is error,get extends namespace is null, NS:" + line);
						} else {
							extHeaderPre = extHeaderPre.substring(0, index);
						}
					} else {

					}
				}
			}
			return shortMessage;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getContentStr(Request request) {
		ContentEncodingHeader header = request.getContentEncoding();
		
		String charEncode ;
		if (header == null ) {
			charEncode = "US-ASCII";
		}else{
			charEncode = header.getEncoding();
		}
		String msgText = null;
		try {
			Object msgContentObj = request.getContent();
			if (msgContentObj instanceof byte[]) {
				msgText = new String((byte[]) msgContentObj, charEncode);
			} else if (msgContentObj instanceof String) {
				msgText = (String) msgContentObj;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return msgText;
	}

}
