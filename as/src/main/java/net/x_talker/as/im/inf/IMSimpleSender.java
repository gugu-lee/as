package net.x_talker.as.im.inf;

import java.sql.Timestamp;

import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import org.freeims.javax.sip.SipStackImpl;
import org.freeims.javax.sip.message.SIPMessage;
import org.freeims.javax.sip.message.SIPRequest;
import org.freeims.javax.sip.stack.SIPClientTransaction;
import net.x_talker.as.Main;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.SendedIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.im.util.SipMessageConvertUtil;
import net.x_talker.as.persist.entity.MessageState;

@Repository
public class IMSimpleSender implements IMSendInf {

	private Logger logger = Logger.getLogger(IMSimpleSender.class);
	// private DomainSelect domainSelect = new DomainSelect();

	public IMSimpleSender() {

	}

	@Override
	public void sendIMMessage(XTalkerSipMsg sipMsg) {
		try {
			Request req = null;

			URI fromUri = sipMsg.getFrom().getURI();

			String scscfAddressStr = getScscfAddress(sipMsg.getTo());
			req = SipMessageConvertUtil.createNewMessageRequest(sipMsg, scscfAddressStr);

			if (req == null) {
				logger.info("create message request failed.this message will be droped.");
				// SendIMContainer.getInstance().addSendIMMsg(sipMsg);
				return;
			}
			SIPRequest requestToSend = (SIPRequest)req.clone();
			SipStackImpl sipStack = (SipStackImpl)Main.getSipProvider().getSipStack();
			SIPClientTransaction clientTrst =null;
			clientTrst=(SIPClientTransaction) sipStack.findTransaction((SIPMessage)req, false);
			
			if (clientTrst ==null){
				logger.info("new client transaction");
				clientTrst =(SIPClientTransaction) Main.getSipProvider().getNewClientTransaction(req);
			}

			clientTrst.sendMessage(requestToSend);

			// 缓存到已发送缓存中,收到回应时,从发送缓存中取到发送对象以正确生成回执消息
			String sendCallId = ((CallIdHeader) req.getHeader(CallIdHeader.NAME)).getCallId();
			logger.info(
					"sned message :" + sipMsg + " to[" + sipMsg.getTo() + "] is success, send call id:" + sendCallId);

			sipMsg.setToCallId(sendCallId);
			SendedIMContainer.getInstance().addItem(sendCallId, sipMsg);
			// 如果是短信则修改消息状态为已发送
			if (sipMsg.getMessageBody() instanceof ShortMessage) {
				MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
						new Timestamp(System.currentTimeMillis()), BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_SENDED);
				MessageStateContainer.getInstance().addMessageState(state);
			}
		} catch (Exception ioEx) {
			logger.info(ioEx.getMessage(), ioEx);
		}
	}

	private String getScscfAddress(Address to) {
		String schema = to.getURI().getScheme();
		String domainStr = getDomainStr(to);
		String port = PropertiesUtil.getInstance().getPropVal("com.ims.out.scscf.port");
		String scscfAddress = schema + ":" + "scscf" + "." + domainStr + ":" + port;
		return scscfAddress;
	}

	/** NOTICE，this method return is SCSCF's address.same to getScscfAddress */
	/*
	 * private String getIcscfAddress(Address to) { String schema =
	 * to.getURI().getScheme(); String domainStr = getDomainStr(to); String port
	 * = PropertiesUtil.getInstance().getPropVal("com.ims.out.icscf.port");
	 * String icscfAddress = schema + ":" + "scscf" + "." + domainStr + ":" +
	 * port; return icscfAddress; }
	 */
	private String getDomainStr(Address addr) {
		URI toURI = addr.getURI();
		String toURIStr = toURI.toString();
		String[] uriStrs = toURIStr.split("@");
		if (uriStrs == null || uriStrs.length != 2) {
			logger.error("parse domian str is error, address:" + addr);
			return null;
		}
		return uriStrs[1];
	}
}
