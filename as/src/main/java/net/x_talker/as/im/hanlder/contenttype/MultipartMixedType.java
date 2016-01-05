package net.x_talker.as.im.hanlder.contenttype;

import gov.nist.javax.sip.header.SIPHeader;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.util.MessageBodyParseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sip.message.Request;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MultipartMixedType extends MessageCPIMType {

	private Logger logger = Logger.getLogger(getClass());

	public static final String MESSAGE_BODY_BOUNDARY = "boundary";
	public static final String MESSAGE_NEW_LINE = "\r\n";
	public static final String MESSAGE_TYPE_RECIPIENTLIST = "application/resource-lists+xml";

	@Override
	public MessageBody parseMessageBody(Request sipRequest) {
		String msgText = MessageBodyParseUtil.getContentStr(sipRequest);
		String contentTypeStr = ((gov.nist.javax.sip.header.ContentType) sipRequest.getHeader(SIPHeader.CONTENT_TYPE))
				.getContentType();
		CpimHeader ContentTypeheader = new CpimHeader(SIPHeader.CONTENT_TYPE, contentTypeStr);
		Map<String, String> params = ContentTypeheader.getParams();
		String boundary = params.get(MESSAGE_BODY_BOUNDARY);
		StringReader sr = null;
		BufferedReader br = null;
		try {
			sr = new StringReader(msgText);
			br = new BufferedReader(sr);
			String line = null;
			List<String> recieptList = null;
			MessageBody msg = null;
			while ((line = br.readLine()) != null) {
				CpimHeader header = new CpimHeader(line);
				if (header.getHeader() == null) {
					continue;
				} else {
					if (header.getHeader().equals("Content-Type")
							&& header.getValue().equals("application/resource-lists+xml")) {
						br.readLine();
						recieptList = recipientlistParse(br, boundary);
					}

					if (header.getHeader().equals("Content-Type") && header.getValue().equals("Message/CPIM")) {
						msg = parse(br);
					}
				}
			}
			if (msg == null) {
				logger.info("parse message header is null, header str:" + msgText);
				return null;
			}
			if (recieptList != null) {
				msg.setRecieptList(recieptList);
			}
			return msg;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				br.close();
				sr.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

		}

		return null;
	}
	//
	// private List<String> parseRecieptList(BufferedReader br) throws
	// IOException, DocumentException {
	//// StringReader sr = new StringReader(recieptPart);
	//// BufferedReader br = new BufferedReader(sr);
	// String line = br.readLine();
	//// String[] headers = line.split(CPIM_HEADER_VALUE_SPLITER);
	// CpimHeader contentTypeHeader = new CpimHeader(line);
	//
	// if(contentTypeHeader.getHeader().equals("Content-Type") &&
	// contentTypeHeader.getValue().equals("application/resource-lists+xml")) {
	// line = br.readLine();
	// CpimHeader ContentDispositionHeader = new CpimHeader(line);
	// if(ContentDispositionHeader.getHeader().equals("Content-Disposition") &&
	// ContentDispositionHeader.getValue().equals("recipient-list")) {
	// Map<String, String> params = contentTypeHeader.getParams();
	// return recipientlistParse(br, params.get("boundary"));
	// }
	// }
	// logger.info("part is not set correct headers, parts is:" + recieptPart);
	// return null;
	// }
	//
	// private Map<String,String> getHeaderParams(String headerStr) {
	// Map<String,String> map = new HashMap<String,String>();
	// String[] params = headerStr.split(";");
	// int index = 0;
	// for(String param : params) {
	// String[] strs = param.split("=");
	// if(strs.length == 2) {
	// map.put(strs[0], strs[1]);
	// } else {
	// if(index > 0)
	// logger.info("not a param,param str:" + param + ",header:" + headerStr);
	// }
	// index++;
	// }
	// return map;
	// }

	@SuppressWarnings("unchecked")
	public List<String> recipientlistParse(BufferedReader br, String boundary) throws DocumentException {
		SAXReader saxR = new SAXReader();
		StringBuffer sb = new StringBuffer();

		try {
			String line = br.readLine();
			while (line.indexOf("--" + boundary) == -1) {
				sb.append(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		StringReader sr = new StringReader(sb.toString());
		Document doc = saxR.read(sr);
		Element root = doc.getRootElement();
		Element eltList = root.element("list");
		List<Element> recipients = eltList.elements();
		List<String> list = new ArrayList<String>();
		for (Element elt : recipients) {
			list.add(elt.attributeValue("uri"));
		}
		sr.close();
		return list;
	}

}
