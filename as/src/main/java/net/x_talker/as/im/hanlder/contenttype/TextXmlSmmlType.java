package net.x_talker.as.im.hanlder.contenttype;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sip.message.Request;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.MessageBodyParseUtil;

public class TextXmlSmmlType implements ContentType {

	public static final String MESSAGE_BODY_ROOT = "sipmessage";

	public static final String MESSAGE_BODY_MESSAGE_FLAG = "shortmessage";
	public static final String MESSAGE_BODY_ID = "id";
	public static final String MESSAGE_BODY_MESSAGE_CLASS = "shortmessageclass";
	public static final String MESSAGE_BODY_MESSAGE_REGDEL = "registereddelivery";
	public static final String MESSAGE_BODY_MESSAGE_PRIORITY = "priority";
	public static final String MESSAGE_BODY_MESSAGE_SCHETIME = "scheduledeliverytime";
	public static final String MESSAGE_BODY_MESSAGE_VALPER = "validityperiod";
	public static final String MESSAGE_BODY_MESSAGE_DATACOD = "datacoding";
	public static final String MESSAGE_BODY_MESSAGE_LMSGID = "longmessageid";
	public static final String MESSAGE_BODY_MESSAGE_SUM = "sum";
	public static final String MESSAGE_BODY_MESSAGE_SEQ = "sequence";
	public static final String MESSAGE_BODY_MESSAGE_MSGLENG = "shortmessagelength";
	public static final String MESSAGE_BODY_MESSAGE_TEXT = "text";

	public static final String MESSAGE_BODY_MESSAGE_CLASS_ABBREVIATE = "msgclass";
	public static final String MESSAGE_BODY_MESSAGE_REGDEL_ABBREVIATE = "regdel";
	public static final String MESSAGE_BODY_MESSAGE_PRIORITY_ABBREVIATE = "prio";
	public static final String MESSAGE_BODY_MESSAGE_SCHETIME_ABBREVIATE = "schetime";
	public static final String MESSAGE_BODY_MESSAGE_VALPER_ABBREVIATE = "valper";
	public static final String MESSAGE_BODY_MESSAGE_DATACOD_ABBREVIATE = "datacod";
	public static final String MESSAGE_BODY_MESSAGE_LMSGID_ABBREVIATE = "lmsgid";
	public static final String MESSAGE_BODY_MESSAGE_SUM_ABBREVIATE = "sum";
	public static final String MESSAGE_BODY_MESSAGE_SEQ_ABBREVIATE = "seq";
	public static final String MESSAGE_BODY_MESSAGE_MSGLENG_ABBREVIATE = "msgleng";
	public static final String MESSAGE_BODY_MESSAGE_TEXT_ABBREVIATE = "text";

	public static final String MESSAGE_BODY_RECEIPT_SUBMITDATE_ABBREVIATE = "subdate";
	public static final String MESSAGE_BODY_RECEIPT_DONEDATE_ABBREVIATE = "donedate";
	public static final String MESSAGE_BODY_RECEIPT_MSGSTAT_ABBREVIATE = "msgstat";
	public static final String MESSAGE_BODY_RECEIPT_ERRCODE_ABBREVIATE = "errcode";
	public static final String MESSAGE_BODY_RECEIPT_DATACOD_ABBREVIATE = "datacod";
	public static final String MESSAGE_BODY_RECEIPT_TEXT_ABBREVIATE = "text";

	public static final String MESSAGE_BODY_RECEIPT_FLAG = "receipt";
	public static final String MESSAGE_BODY_RECEIPT_SUBMITDATE = "submitdate";
	public static final String MESSAGE_BODY_RECEIPT_DONEDATE = "donedate";
	public static final String MESSAGE_BODY_RECEIPT_MSGSTAT = "shortmessagestatus";
	public static final String MESSAGE_BODY_RECEIPT_ERRCODE = "errorcode";
	public static final String MESSAGE_BODY_RECEIPT_DATACOD = "datacoding";
	public static final String MESSAGE_BODY_RECEIPT_TEXT = "text";

	public static final String SHORT_MESSAGE_ISRECEIPT_NEED = "need receipt";
	public static final String SHORT_MESSAGE_ISRECEIPT_NONEED = "no need receipt";

	public static final String PERIORITY_NONURGENT = "non-urgent";
	public static final String PERIORITY_NORMAL = "normal";
	public static final String PERIORITY_URGENT = "urgent";
	public static final String PERIORITY_EMERGENCY = "emergency";

	public static final int PERIORITY_NONURGENT_FLAG = 0;
	public static final int PERIORITY_NORMAL_FLAG = 1;
	public static final int PERIORITY_URGENT_FLAG = 2;
	public static final int PERIORITY_EMERGENCY_FLAG = 3;

	private static Logger logger = Logger.getLogger(TextXmlSmmlType.class);

	@Override
	public byte[] encodeReceipt(XTalkerSipMsg sipMsg) {
		ReceiptMessage receiptMsg = (ReceiptMessage) sipMsg.getMessageBody();
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement(MESSAGE_BODY_ROOT);
		Element receiptElt = root.addElement(MESSAGE_BODY_RECEIPT_FLAG);
		Element idElt = receiptElt.addElement(MESSAGE_BODY_ID);
		idElt.setText(receiptMsg.getMessageId());
		Date submitTime = sipMsg.getSubmitTime();
		DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String timeStr = format.format(submitTime);
		Element submitTimeElt = receiptElt.addElement(MESSAGE_BODY_RECEIPT_SUBMITDATE_ABBREVIATE);
		submitTimeElt.setText(timeStr);

		Date doneDate = sipMsg.getSubmitTime();
		if (doneDate != null) {
			String doneDateStr = format.format(doneDate);
			Element doneDateElt = receiptElt.addElement(MESSAGE_BODY_RECEIPT_DONEDATE_ABBREVIATE);
			doneDateElt.setText(doneDateStr);
		} else {
			logger.info("encode receipt is exception, done date is null! Message id:" + receiptMsg.getMessageId()
					+ ", from:" + sipMsg.getFrom() + ",to:" + sipMsg.getTo() + ",submit time:"
					+ sipMsg.getSubmitTime());
		}

		String dataCode = receiptMsg.getDataCoding();
		if (dataCode != null) {
			Element dataCodeElt = receiptElt.addElement(MESSAGE_BODY_RECEIPT_DATACOD_ABBREVIATE);
			dataCodeElt.setText(dataCode);
		}

		int errorCode = receiptMsg.getErrCode();
		Element errorCodeElt = receiptElt.addElement(MESSAGE_BODY_RECEIPT_ERRCODE_ABBREVIATE);
		if (errorCode != 0) {
			errorCodeElt.setText(String.valueOf(errorCode));
		} else {
			errorCodeElt.setText("200");
		}
		String msgStat = receiptMsg.getMsgstat();
		if (msgStat != null) {
			Element msgStatElt = receiptElt.addElement(MESSAGE_BODY_RECEIPT_MSGSTAT_ABBREVIATE);
			msgStatElt.setText(msgStat);
		}

		return doc.asXML().getBytes();
	}

	@Override
	public MessageBody parseMessageBody(Request sipRequest) {
		try {
			String msgText = MessageBodyParseUtil.getContentStr(sipRequest);

			StringReader sr = new StringReader(msgText);
			SAXReader reader = new SAXReader();

			Document doc = reader.read(sr);
			Element root = doc.getRootElement();

			Element base = root.element(MESSAGE_BODY_MESSAGE_FLAG);

			if (base != null) {
				return parseShortMessage(base);
			} else {
				base = root.element(MESSAGE_BODY_RECEIPT_FLAG);

			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static ShortMessage parseShortMessage(Element shotMsgElt) {
		ShortMessage msgBody = new ShortMessage();
		Element idElt = shotMsgElt.element(MESSAGE_BODY_ID);

		// id id 短消息标识：终端发送短消息或短消息中心转发短消息时生成
		if (idElt != null) {
			msgBody.setMessageId(idElt.getText());
		}

		// registereddelivery regdel 回执需求
		String isReceiptText = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_REGDEL_ABBREVIATE);
		if (isReceiptText == null) {
			isReceiptText = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_REGDEL);
		}
		if (isReceiptText.equals(SHORT_MESSAGE_ISRECEIPT_NEED)) {
			msgBody.setIsReceipt(true);
		} else {
			msgBody.setIsReceipt(false);
		}

		// shortmessageclass msgclass 短消息类型
		String classType = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_CLASS_ABBREVIATE);
		if (classType == null) {
			classType = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_CLASS);
		}
		msgBody.setMessageType(classType);

		// priority prio 优先级
		String prioStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_PRIORITY_ABBREVIATE);
		if (prioStr == null || prioStr.equals("")) {
			prioStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_PRIORITY);
		}
		if (prioStr.equals(PERIORITY_EMERGENCY)) {
			msgBody.setPriority(PERIORITY_EMERGENCY_FLAG);
		} else if (prioStr.equals(PERIORITY_NONURGENT)) {
			msgBody.setPriority(PERIORITY_NONURGENT_FLAG);
		} else if (prioStr.equals(PERIORITY_URGENT)) {
			msgBody.setPriority(PERIORITY_URGENT_FLAG);
		} else {
			msgBody.setPriority(PERIORITY_NORMAL_FLAG);
		}

		// scheduledeliverytime schetime 短消息发送时间：
		// 发送方用户填写期望发送时间；短消息中心填写第一次下发时间
		String scheTimeStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SCHETIME_ABBREVIATE);
		if (scheTimeStr == null || scheTimeStr.equals("")) {
			scheTimeStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SCHETIME);
		}

		if (scheTimeStr != null && !scheTimeStr.equals("")) {
			Timestamp scheTime = getShortMsgTimestamp(scheTimeStr);
			msgBody.setScheduleDeliveryTime(scheTime);
		}

		// validityperiod valper 短消息有效期
		String valperStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_VALPER_ABBREVIATE);

		if (valperStr == null || valperStr.equals("")) {
			valperStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_VALPER);
		}

		if (valperStr != null && !valperStr.equals("")) {
			Timestamp valper = getShortMsgTimestamp(valperStr);
			msgBody.setValidityPeriod(valper);
		}

		// datacoding datacod 短消息编码格式
		String datacod = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_DATACOD_ABBREVIATE);

		if (datacod == null || datacod.equals("")) {
			datacod = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_DATACOD);
		}
		msgBody.setDataCoding(datacod);

		// longmessageid lmsgid 长消息序号
		String lmsgidStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_LMSGID_ABBREVIATE);
		if (lmsgidStr == null || lmsgidStr.equals("")) {
			lmsgidStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_LMSGID);
		}
		if (lmsgidStr != null && !lmsgidStr.equals("")) {
			int lsmgid = Integer.parseInt(lmsgidStr);
			msgBody.setLongMsgId(lsmgid);
		}

		// sum sum 长消息一共拆分成几条
		String sumStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SUM_ABBREVIATE);
		if (sumStr == null || sumStr.equals("")) {
			sumStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SUM);
		}
		if (sumStr != null && !sumStr.equals("")) {
			int sum = Integer.parseInt(sumStr);
			msgBody.setLongMsgSum(sum);
		}

		// sequence seq 是长消息被拆分后的第几条
		String seqStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SEQ_ABBREVIATE);
		if (seqStr == null || seqStr.equals("")) {
			seqStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_SEQ);
		}
		if (seqStr != null && !seqStr.equals("")) {
			int seq = Integer.parseInt(seqStr);
			msgBody.setLongMsgSequence(seq);
		}

		// shortmessagelength msgleng 短消息内容（即text参数）的长度
		String lengthStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_MSGLENG_ABBREVIATE);
		if (lengthStr == null || lengthStr.equals("")) {
			lengthStr = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_MSGLENG);
		}
		if (lengthStr != null && !lengthStr.equals("")) {
			int length = Integer.parseInt(lengthStr);
			msgBody.setMsgLength(length);
		}

		// text text 短消息内容
		String text = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_TEXT_ABBREVIATE);
		if (text == null || text.equals("")) {
			text = shotMsgElt.elementText(MESSAGE_BODY_MESSAGE_TEXT);
		}

		if (text == null) {
			logger.info("not set message text! message id:" + msgBody.getMessageId());
			return null;
		} else {
			msgBody.setText(text);
		}

		return msgBody;
	}

	private static Timestamp getShortMsgTimestamp(String timeStr) {
		if (timeStr == null || timeStr.length() < 17) {
			return null;
		}
		try {
			DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
			String dateStr = timeStr.substring(0, 14);
			Date date = format.parse(dateStr);
			String dateDiff = timeStr.substring(14, 16);
			int diff = Integer.parseInt(dateDiff);
			if (diff > 0) {
				String diffType = timeStr.substring(16);
				if (diffType.equals("+")) {
					return new Timestamp(date.getTime() + diff * 15 * 60 * 1000);
				} else if (diffType.equals("-")) {
					return new Timestamp(date.getTime() - diff * 15 * 60 * 1000);
				}
			}

			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}

}
