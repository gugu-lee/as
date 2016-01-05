package net.x_talker.as.im.handler.receipt;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.sip.address.Address;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.MessageContentFileContainer;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.XTalkerSipMsgOpFile;
import net.x_talker.as.im.handler.online.IMOnlineHandler;
import net.x_talker.as.im.hanlder.contenttype.ContentType;
import net.x_talker.as.im.hanlder.contenttype.ContentTypeFactory;
import net.x_talker.as.im.container.entity.MessageBody;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.persist.entity.MessageState;

/**
 * 
 * 
 *
 */
@Component("IMReceiptHandler")
public class IMReceiptHandlerImpl implements IMReceiptHandler {

	private Logger logger = Logger.getLogger(IMReceiptHandlerImpl.class);
	@Resource(name = "IMOnlineHandler")
	private IMOnlineHandler imOnlineHandler;

	@Override
	public void receiptHandle(XTalkerSipMsg sipMsg) {
		if (sipMsg == null) {
			logger.info("receipt handle message is null");
			return;
		}
		MessageBody msgBody = sipMsg.getMessageBody();
		if (msgBody == null) {
			logger.info("receipt handle message body is null");
			return;
		}

		if (msgBody instanceof ShortMessage) {

			ShortMessage shortMessage = (ShortMessage) msgBody;
			if (shortMessage.getIsReceipt()) {
				XTalkerSipMsg receiptMsg = createReceiptXTalkerSipMsg(sipMsg);
				imOnlineHandler.onlineHandle(receiptMsg);
				// SendIMContainer.getInstance().addSendIMMsg(receiptMsg);
			} else {
				logger.info("client is not need receipt, not send receipt,meesage is from:" + sipMsg.getFrom() + ",to:"
						+ sipMsg.getTo() + ",submit time:" + sipMsg.getSubmitTime());
			}

			// 修改消息状态为已生成回执
			MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
					new Timestamp(System.currentTimeMillis()),
					BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_MESSAGE_HAS_RECEIPT);
			MessageStateContainer.getInstance().addMessageState(state);

			// 无论发送是否成功都会走到这里，到这里消息流程已经结束，删除消息文件
			MessageContentFileContainer.getInstance()
					.addMsgToWriteFile(new XTalkerSipMsgOpFile(sipMsg, BizConsts.X_TALKER_SIP_MSG_OP_FILE_TYPE_DELETE));

		} else {
			logger.info("receipt message is not need send receipt");
		}

	}

	/**
	 * 
	 * a) Request URI：发送方SIP URI b) To：发送方tel URI c)
	 * From/P-Asserted-Identity：接收方tel URI d) User-Agent：sms-serv e)
	 * Content-Type：text/xml-smml f) 消息体-id：与原短消息的id一致
	 * 
	 * @param sipMsg
	 * @return
	 */
	private XTalkerSipMsg createReceiptXTalkerSipMsg(XTalkerSipMsg sipMsg) {
		Address from = sipMsg.getFrom();
		Address to = sipMsg.getTo();
		sipMsg.setFrom(to);
		sipMsg.setTo(from);

		ReceiptMessage receipt = new ReceiptMessage();
		ShortMessage shortMsg = (ShortMessage) sipMsg.getMessageBody();
		receipt.setMessageId(shortMsg.getMessageId());
		receipt.setNs(shortMsg.getNs());
		receipt.setErrCode(sipMsg.getStatus());
		String msgStat = getMsgStatFromErrorCode(sipMsg.getStatus());
		receipt.setMsgstat(msgStat);
		sipMsg.setResendTimes(0);
		sipMsg.setMessageBody(receipt);

		// String receiptStr = MessageBodyParseUtil.encodeReceipt(sipMsg);
		ContentType contentType = ContentTypeFactory.createContentType(sipMsg.getContentType());

		sipMsg.setContent(contentType.encodeReceipt(sipMsg));

		return sipMsg;
	}

	private static String getMsgStatFromErrorCode(int errorCode) {
		if (errorCode == Response.ACCEPTED || errorCode == Response.OK) {
			return ReceiptMessage.RECEIPT_STATE_SUCCESS;
		} else if (errorCode == Response.REQUEST_TIMEOUT) {
			return ReceiptMessage.RECEIPT_STATE_EXPIRED;
		} else {
			return ReceiptMessage.RECEIPT_STATE_FAILURE;
		}
	}

}
