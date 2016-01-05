package net.x_talker.as.im.hanlder.contenttype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sip.message.Request;

import org.apache.log4j.Logger;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.MessageBodyParseUtil;

/**
 * message/CPIM类型消息解析处理类
 * 
 * @author zengqiaowen
 *
 */
public class MessageCPIMType implements ContentType {

	/**
	 * 消息类型 所有消息都应携带。不带则默认为该消息的类型为IM消息。 msgType的值 说明 IM 即时消息 TM 定时消息 SYSTEM
	 * 系统广播消息 REPORT 递送报告 SM 短信
	 **/
	public static final String CPIM_MESSAGE_TYPE = "msgType";
	/**
	 * 是否需要递送报告 所有消息都应携带。不带则默认为该消息不需要递送报告 msgReport的值 说明 YES 需要 NO 不需要
	 * 
	 */
	public static final String CPIM_MESSAGE_REPORT = "msgReport";
	/**
	 * 所有消息都应携带，要求递送报告的消息必须携带。 终端使用该ID在终端范围内定位一条消息，可用在递送报告匹配和历史消息下载。
	 * 对于所有不带此字段的消息，即时消息业务平台会为其生成该字段。
	 */
	public static final String CPIM_LOCAL_MEESAGE_ID = "localMsgID";
	/**
	 * 定时消息需要的字段。指明消息发送的时间 Mon, 09 Jul 2001 09:00:07 GMT
	 */
	public static final String CPIM_TIME_TO_DELIVER = "timeToDeliver";
	/**
	 * 递送报告需要的字段。指明递送报告的类型。 reportType的值 说明 FAILED 失败递送报告。表明AS确定原消息未能发送到接收方用户。
	 * SUCCESSFUL 成功递送报告。表明AS确定原消息已被接收方用户接收。 REP_NOT_RECEIVED
	 * 递送报告失败。发方AS在指定时间（2天）内未能收到接收方AS发来的关于原消息的递送报告，亦即不能确定原消息是否已被接收方接收时，
	 * 需要向原消息的发起方发送该类型的递送报告。
	 */
	public static final String CPIM_REPORT_TYPE = "reportType";
	/**
	 * 递送报告需要的字段。终端收到的递送报告中该字段携带原消息的localMsgID。
	 */
	public static final String CPIM_REPORT_ID = "reportID";
	/**
	 * 递送报告需要的字段。指明生成递送报告的AS最终下发原消息的时间。 Mon, 09 Jul 2001 09:00:07 GMT
	 */
	public static final String CPIM_DONE_TIME = "doneTime";
	/**
	 * 失败递送报告需要的字段。指明原消息发送失败的原因代码。
	 */
	public static final String CPIM_ERROR_CODE = "errorCode";
	/**
	 * 该消息是否携带附件，附件是指除了文本信息外的其他消息体，比如图片，声音文件，word文档等等
	 */
	public static final String CPIM_HAS_ATTACHMENT = "hasAttachment";
	/**
	 * 该消息是否为历史消息库发出的通知消息，该字段对终端没有任何影响，终端如果收到该字段的处理方法为忽略
	 */
	public static final String CPIM_HISTORY_MESSAGE_NOTIFY = "historyMsgNotify";
	/**
	 * 表明是对原来定时消息的修改消息
	 */
	public static final String CPIM_OP_TYPE = "OpType";
	/**
	 * 表明是对原来定时消息的修改消息
	 */
	public static final String CPIM_NS = "NS";
	/**
	 * 为支持匿名业务扩展
	 */
	public static final String CPIM_ANON_MESSAGE = "anonMsg";

	public static final String CPIM_DATA_CODING = "datacoding";
	public static final String CPIM_LONG_MESSAGEID = "longmessageid";
	public static final String CPIM_LONG_MESSAGESUM = "sum";
	public static final String CPIM_LONG_MESSAGE_SEQUENCE = "sequence";
	public static final String CPIM_LONG_MESSAGE_LENGTH = "shortmessagelength ";

	public static final String CPIM_TEXT = "text";

	// 即时消息
	public static final String CPIM_MSGTYPE_IM = "IM";
	// 定时消息
	public static final String CPIM_MSGTYPE_TM = "TM";
	// 系统消息
	public static final String CPIM_MSGTYPE_SYSTEM = "SYSTEM";
	// 回执
	public static final String CPIM_MSGTYPE_REPORT = "REPORT";
	// 短信
	public static final String CPIM_MSGTYPE_SM = "SM";

	public static final String CPIM_MSGREPORT_YES = "YES";
	public static final String CPIM_MSGREPORT_NO = "NO";

	// 由于头与值之间冒号后跟有空格
	public static final String CPIM_HEADER_VALUE_SPLITER = ": ";

	private Logger logger = Logger.getLogger(MessageCPIMType.class);

	/**
	 * 回执消息组织
	 */
	@Override
	public byte[] encodeReceipt(XTalkerSipMsg sipMsg) {
		MessageBody msgBody = sipMsg.getMessageBody();
		if (!(msgBody instanceof ReceiptMessage)) {
			logger.info("encode receipt entity is not a receipt message");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		ReceiptMessage receipt = (ReceiptMessage) msgBody;
		String ns = receipt.getNs();
		if (ns == null) {
			return null;
		}
		if (ns != null && !ns.equals("")) {
			sb.append(CPIM_NS).append(CPIM_HEADER_VALUE_SPLITER).append(ns).append("\r\n");
		}
		sb.append(ns).append(".").append(CPIM_MESSAGE_TYPE).append(CPIM_HEADER_VALUE_SPLITER)
				.append(CPIM_MSGTYPE_REPORT).append("\r\n");
		String localMsgId = "AS" + UUID.randomUUID().toString();
		sb.append(ns).append(".").append(CPIM_LOCAL_MEESAGE_ID).append(CPIM_HEADER_VALUE_SPLITER).append(localMsgId)
				.append(receipt.getMessageId()).append("\r\n");
		if (receipt.getMessageId() != null) {
			sb.append(ns).append(".").append(CPIM_REPORT_ID).append(CPIM_HEADER_VALUE_SPLITER)
					.append(receipt.getMessageId()).append("\r\n");
		}
		if (receipt.getMsgstat() != null) {
			sb.append(ns).append(".").append(CPIM_REPORT_TYPE).append(CPIM_HEADER_VALUE_SPLITER)
					.append(receipt.getMsgstat()).append("\r\n");
		}
		sb.append(ns).append(".").append(CPIM_ERROR_CODE).append(CPIM_HEADER_VALUE_SPLITER).append(receipt.getErrCode())
				.append("\r\n");

		Date doneTime = receipt.getDoneTime();
		if (doneTime == null) {
			doneTime = new Date(System.currentTimeMillis());
		}
		String doneTimeStr = Util.formatGMTTime(doneTime);
		sb.append(ns).append(".").append(CPIM_DONE_TIME).append(CPIM_HEADER_VALUE_SPLITER).append(doneTimeStr)
				.append("\r\n");
		try {
			return sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 
	 */
	@Override
	public MessageBody parseMessageBody(Request request) {
		try {

			String msgText = MessageBodyParseUtil.getContentStr(request);
			StringReader sr = new StringReader(msgText);

			BufferedReader reader = new BufferedReader(sr);
			MessageBody messageBody = parse(reader);
			reader.close();
			return messageBody;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	protected MessageBody parse(BufferedReader reader) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String line;
		String extHeaderPre = "";
		;
		boolean isExtend = false;
		while ((line = reader.readLine()) != null) {
			if (line.equals("")) {
				continue;
			}
			CpimHeader header = new CpimHeader(line);
			// String[] props = line.split(": ");
			// String header = props[0];
			if (header == null) {
				continue;
			}
			String headerName = header.getHeader();
			String value = header.getValue();
			if (isExtend) {
				if (headerName.equals("Content-Type")) {
					if (value.equals("text/plain")) {
						String msgText = readMessageText(reader);
						map.put(CPIM_TEXT, msgText);
					}
				} else {
					headerName = headerName.replace(extHeaderPre + ".", "");
					map.put(headerName, value);
				}

			} else {
				if (headerName.equalsIgnoreCase("NS")) {
					isExtend = true;
					extHeaderPre = value;
					int index = extHeaderPre.indexOf("<");
					if (index == 0) {
						logger.info("message format is error,get extends namespace is null, NS:" + line);
					} else {
						extHeaderPre = extHeaderPre.substring(0, index);
					}
					map.put(headerName, value);
				} else if (header.equals("Content-Type")) {
					String msgText = readMessageText(reader);
					map.put(CPIM_TEXT, msgText);
					break;
				}
			}
		}
		return constructMessageBody(map);
	}

	private MessageBody constructMessageBody(Map<String, String> map) {
		String msgType = map.get(CPIM_MESSAGE_TYPE);
		if (CPIM_MSGTYPE_REPORT.equals(msgType)) {
			ReceiptMessage receipt = new ReceiptMessage();
			if (map.get(CPIM_REPORT_TYPE) != null) {
				receipt.setMsgstat(map.get(CPIM_REPORT_TYPE));
			}
			if (map.get(CPIM_REPORT_ID) != null) {
				receipt.setMessageId(map.get(CPIM_REPORT_ID));
			}
			if (map.get(CPIM_ERROR_CODE) != null) {
				int errCode = Integer.parseInt(map.get(CPIM_ERROR_CODE));
				receipt.setErrCode(errCode);
			}
			if (map.get(CPIM_DONE_TIME) != null) {
				String val = map.get(CPIM_DONE_TIME);
				try {
					Date doneTime = Util.parseGMTTime(val);
					receipt.setDoneTime(doneTime);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}

			}

			return receipt;
		} else {
			ShortMessage shortMsg = new ShortMessage();
			if (map.get(CPIM_MESSAGE_REPORT) != null) {
				String val = map.get(CPIM_MESSAGE_REPORT);
				if (val.equals(CPIM_MSGREPORT_YES)) {
					shortMsg.setIsReceipt(true);
				} else {
					shortMsg.setIsReceipt(false);
				}
			}
			if (map.get(CPIM_LOCAL_MEESAGE_ID) != null) {
				shortMsg.setMessageId(map.get(CPIM_LOCAL_MEESAGE_ID));
			}

			if (map.get(CPIM_TIME_TO_DELIVER) != null) {
				String val = map.get(CPIM_TIME_TO_DELIVER);
				try {
					Date time = Util.parseGMTTime(val);
					shortMsg.setScheduleDeliveryTime(time);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}

			}
			if (map.get(CPIM_NS) != null) {
				String val = map.get(CPIM_NS);
				shortMsg.setNs(val);
			}
			if (map.get(CPIM_DATA_CODING) != null) {
				String val = map.get(CPIM_NS);
				shortMsg.setDataCoding(val);
			}
			if (map.get(CPIM_LONG_MESSAGEID) != null) {
				String val = map.get(CPIM_LONG_MESSAGEID);
				shortMsg.setLongMsgId(Integer.parseInt(val));
			}
			if (map.get(CPIM_LONG_MESSAGESUM) != null) {
				String val = map.get(CPIM_LONG_MESSAGESUM);
				shortMsg.setLongMsgSum(Integer.parseInt(val));
			}
			if (map.get(CPIM_LONG_MESSAGE_SEQUENCE) != null) {
				String val = map.get(CPIM_LONG_MESSAGE_SEQUENCE);
				shortMsg.setLongMsgSequence(Integer.parseInt(val));
			}
			if (map.get(CPIM_TEXT) != null) {
				String val = map.get(CPIM_TEXT);
				shortMsg.setText(val);
			}
			return shortMsg;
		}
	}

	private String readMessageText(BufferedReader reader) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = "";
		int index = 0;
		while ((line = reader.readLine()) != null) {
			if (line.equals("<body>")) {
				continue;
			}
			if (line.equals("</body>")) {
				break;
			}
			if (index++ == 0) {
				sb.append(line);
			} else {
				sb.append("\r\n").append(line);
			}
		}
		return sb.toString();
	}

}
