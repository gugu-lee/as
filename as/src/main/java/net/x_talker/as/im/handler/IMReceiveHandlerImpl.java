package net.x_talker.as.im.handler;

import org.freeims.javax.sip.header.SIPHeader;
import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.MessageContentFileContainer;
import net.x_talker.as.im.container.PersistIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.XTalkerSipMsgOpFile;
import net.x_talker.as.im.handler.online.IMOnlineHandler;
import net.x_talker.as.im.handler.regularlysend.IMRegularlysendHandler;
import net.x_talker.as.im.hanlder.contenttype.ContentType;
import net.x_talker.as.im.hanlder.contenttype.ContentTypeFactory;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.servlet.IMResponse;
import net.x_talker.as.im.util.MessageBodyParseUtil;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.im.util.SipMessageConvertUtil;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.persist.IMPersist;
import net.x_talker.as.persist.IMPersistImpl;
import net.x_talker.as.persist.entity.MessageLog;

import java.util.Date;

import javax.annotation.Resource;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import org.freeims.javax.sip.message.SIPRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 短消息接收流程处理
 *
 */
@Component("IMReceiveHandler")
public class IMReceiveHandlerImpl implements IMReceiveHandler {

	@Resource(name = "IMOnlineHandler")
	private IMOnlineHandler imOnlineHandler;
	@Resource(name = "IMRegularlysendHandler")
	private IMRegularlysendHandler imRegularlyHandler;
	private Logger logger = Logger.getLogger(IMReceiveHandlerImpl.class);
	@Resource(name = "IMPersistImpl")
	private IMPersist persister;// =(IMPersistImpl)SpringBeanUtil.getBeanByName("IMPersistImpl");;

	public static final String PROPKEY_MULTISEND_PROTECTED = "com.ims.as.message.multisend.protected.enable";
	public static final String PROPKEY_MULTISEND_PROTECTED_INTERVAL = "com.ims.as.message.multisend.protected.interval";

	/**
	 * 接收到MESSAGE请求后由IMSERVLET调用 应对接收到的请求类型进行判断(ShortMessage或Receipt)
	 */
	public IMResponse handleIM(Request request) {
		SIPRequest sipRequest = (SIPRequest) request;
		IMResponse resp = new IMResponse();
		try {
			if (request == null) {
				logger.warn("sip servlet request is null!");
				resp.setStatuscode(Response.BAD_REQUEST);
				resp.setReasonPhrase("sip servlet request is null!");
				return resp;
			}


			// 获取消息体内容,如果消息体内容为空,直接返回400错误码
			Object msgContentObj = request.getContent();
			if (msgContentObj == null) {
				logger.warn(
						"message body is null! message from:" + sipRequest.getFrom() + ", to:" + sipRequest.getTo());
				resp.setStatuscode(Response.BAD_REQUEST);
				resp.setReasonPhrase("message body is null!");
				return resp;
			}
			String msgContentType = sipRequest.getContentTypeHeader().getHeaderValue();

			ContentType contentType = ContentTypeFactory.createContentType(msgContentType);
			if (contentType == null) {
				logger.warn("unsuport content type! type is:" + msgContentType);
				resp.setStatuscode(Response.BAD_REQUEST);
				resp.setReasonPhrase("unsuport content type!");
				return resp;
			}



			XTalkerSipMsg sipMsg = SipMessageConvertUtil.convertFromRequest(request);
			MessageBody messageBody = contentType.parseMessageBody(request);

			if (messageBody == null) {
				resp.setStatuscode(Response.BAD_REQUEST);
				resp.setReasonPhrase("parse message body is null!");
				return resp;

			}
			sipMsg.setMessageBody(messageBody);

			if (messageBody instanceof ShortMessage) {
				ShortMessage shortMsg = (ShortMessage) messageBody;

				if (shortMsg.getMessageId() == null) {
					if (shortMsg.getIsReceipt()) {
						resp.setStatuscode(Response.BAD_REQUEST);
						resp.setReasonPhrase("message is need receipt but not set message id!");
						return resp;
					} else {
						shortMsg.setMessageId(((CallIdHeader)request.getHeader("CallID")).getCallId());
					}
				}

				// 比较更新有效期
				// 有效期不能早于系统时间，取不早于系统时间的最早的那个作为有效期
				int expire=0;
				ExpiresHeader header = request.getExpires();
				if (header != null){
					 expire = header.getExpires();
				}

				if (expire != 0) {
					Date expireTime = new Date(System.currentTimeMillis() + expire * 1000);
					Date valper = shortMsg.getValidityPeriod();
					long delayTime = Util.hourToMilliseconds(PropertiesUtil.getInstance()
							.getPropIntVal(BizConsts.CONFKEY_DEFAULT_MESSAGE_VALIDITY_PERIOD));
					Date defaultTime = new Date(System.currentTimeMillis() + delayTime);
					Date validityPeriod = Util.getEarlierTimeNotBeforeCurrentTime(valper, expireTime, defaultTime);
					shortMsg.setValidityPeriod(validityPeriod);
				}
				sipMsg.setPriority(shortMsg.getPriority());

				if (shortMsg.getScheduleDeliveryTime() != null) {
					imRegularlyHandler.regularlySendIM(sipMsg);
					resp.setStatuscode(Response.ACCEPTED);
					return resp;
				}

				return messageSend(sipMsg);
			} else if (messageBody instanceof ReceiptMessage) {
				resp.setStatuscode(Response.ACCEPTED);
				return resp;
			} else {
				return messageSend(sipMsg);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatuscode(Response.SERVER_INTERNAL_ERROR);
			resp.setReasonPhrase(e.getMessage());
			return resp;
		}
	}

	private IMResponse messageSend(XTalkerSipMsg sipMsg) {

		IMResponse resp = imOnlineHandler.onlineHandle(sipMsg);

		MessageLog messageLog = MessageBodyParseUtil.XTalkerSipMsg2MessageLog(sipMsg);
		PersistIMContainer.getInstance().addIM(messageLog);

		MessageContentFileContainer.getInstance()
				.addMsgToWriteFile(new XTalkerSipMsgOpFile(sipMsg, BizConsts.X_TALKER_SIP_MSG_OP_FILE_TYPE_CREATE));

		return resp;
	}

	public void setImOnlineHandler(IMOnlineHandler imOnlineHandler) {
		this.imOnlineHandler = imOnlineHandler;
	}

	public void setImRegularlyHandler(IMRegularlysendHandler imRegularlyHandler) {
		this.imRegularlyHandler = imRegularlyHandler;
	}

}
