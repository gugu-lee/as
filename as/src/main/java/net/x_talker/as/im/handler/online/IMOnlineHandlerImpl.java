package net.x_talker.as.im.handler.online;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.CacheIMContainer;
import net.x_talker.as.im.container.DelayedContainer;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.handler.IMRegisterHandler;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.container.entity.ReceiptMessage;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.servlet.IMResponse;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.persist.entity.MessageState;

/**
 * 接收用户是否在线处理
 */
@Component("IMOnlineHandler")
public class IMOnlineHandlerImpl implements IMOnlineHandler {

	@Resource(name = "IMRegisterHandler")
	private IMRegisterHandler registerHandler;

	private Logger logger = Logger.getLogger(IMOnlineHandlerImpl.class);

	/**
	 * 接收用户是否在线处理 接收用户在线将消息加入到发送流程 接收用户不在线将消息加入到缓存消息流程,并加入到过期机制中
	 */
	@Override
	public IMResponse onlineHandle(XTalkerSipMsg sipMsg) {
		IMResponse resp = new IMResponse();
		Address to = sipMsg.getTo();
		if (to == null) {
			logger.info("receive message to address is null");
			resp.setStatuscode(Response.BAD_REQUEST);
			resp.setReasonPhrase("receive message to address is null");
			return resp;
		}
		URI toUser = to.getURI();
		// String toUser = to.getURI().toString();
		boolean isOnline = registerHandler.getUserState(toUser, true);
		if (isOnline) {
			logger.info("user:" + to + " is online, add message to send, message from:" + sipMsg.getFrom());
			SendIMContainer.getInstance().addSendIMMsg(sipMsg);
			resp.setStatuscode(Response.ACCEPTED);
			return resp;
		} else {
			logger.info("user:" + to + " is offline, add message to cache, message from:" + sipMsg.getFrom());
			// 接收用户不在线,添加到缓存消息容器和消息延时过期容器
			long delayTime = 0;
			if (sipMsg.getMessageBody() instanceof ShortMessage) {
				delayTime = Util.hourToMilliseconds(
						PropertiesUtil.getInstance().getPropIntVal(BizConsts.CONFKEY_DEFAULT_MESSAGE_VALIDITY_PERIOD));
				ShortMessage message = (ShortMessage) sipMsg.getMessageBody();
				Date earlierTime = Util.getEarlierTime(message.getValidityPeriod(),
						new Timestamp(System.currentTimeMillis() + delayTime));
				delayTime = earlierTime.getTime() - System.currentTimeMillis();
			} else if (sipMsg.getMessageBody() instanceof ReceiptMessage) {
				delayTime = Util.hourToMilliseconds(
						PropertiesUtil.getInstance().getPropIntVal(BizConsts.CONFKEY_DEFAULT_RECEIPT_VALIDITY_PERIOD));
			}

			CacheIMContainer.getInstance().addCacheMsg(sipMsg);
			DelayedMessage<XTalkerSipMsg> delayMsg = new DelayedMessage<XTalkerSipMsg>(delayTime, TimeUnit.MILLISECONDS,
					DelayedTypeEnum.CacheMessage);
			delayMsg.setItem(sipMsg);
			DelayedContainer.getInstance().addItem(delayMsg);

			// 如果为短消息则修改消息状态为用户不在线，消息缓存
			if (sipMsg.getMessageBody() instanceof ShortMessage) {
				MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
						new Timestamp(System.currentTimeMillis()),
						BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_USER_NOT_ONLINE_WAIT);
				MessageStateContainer.getInstance().addMessageState(state);
			}
			resp.setStatuscode(Response.ACCEPTED);
			return resp;
		}
	}

	public void setRegisterHandler(IMRegisterHandler registerHandler) {
		this.registerHandler = registerHandler;
	}

}
