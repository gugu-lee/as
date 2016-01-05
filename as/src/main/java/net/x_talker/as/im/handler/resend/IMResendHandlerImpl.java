
package net.x_talker.as.im.handler.resend;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.DelayedContainer;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.handler.receipt.IMReceiptHandler;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.persist.entity.MessageState;

@Component("IMResendHandler")
public class IMResendHandlerImpl implements IMResendHandler {

	private Logger logger = Logger.getLogger(IMResendHandlerImpl.class);
	@Resource(name = "IMReceiptHandler")
	private IMReceiptHandler imReceipt;

	private int resendTimes;
	private int resendInterval;

	public IMResendHandlerImpl() {
		PropertiesUtil util = PropertiesUtil.getInstance();
		resendTimes = util.getPropIntVal(BizConsts.CONFKEY_MESSAGE_RESEND_TIME);
		if (resendTimes == 0) {
			resendTimes = BizConsts.DEFAULT_MESSAGE_RESEND_TIMES;
		}
		resendInterval = util.getPropIntVal(BizConsts.CONFKEY_MESSAGE_RESEND_INTERVAL);

		if (resendInterval == 0) {
			resendInterval = BizConsts.DEFAULT_MESSAGE_RESEND_INTERVAL;
		}
	}

	/**
	 * 【重发短消息方法】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.handler.resend.IMResendHandler#resendIM(net.x_talker.as.im.container.entity.XTalkerSipMsg,
	 *      int)
	 */
	@Override
	public void resendIM(XTalkerSipMsg sipMsg) {
		logger.info("resend message, from:" + sipMsg.getFrom() + ", to:" + sipMsg.getTo());
		Map<ResendParamEnum, Integer> resendParams = getResendParams(sipMsg.getStatus());

		// 如果重发次数未达到设置的最大重发次数,则再次重发消息
		if (sipMsg.getResendTimes() < resendParams.get(ResendParamEnum.resendTimes)) {
			sipMsg.setResendTimes(sipMsg.getResendTimes() + 1);
			long delayTime = resendParams.get(ResendParamEnum.resendInterval);
			// 重发消息设置为定时发送消息,发送时间为当前时间向后推一个重发间隔
			DelayedMessage<XTalkerSipMsg> msg = new DelayedMessage<XTalkerSipMsg>(delayTime, TimeUnit.SECONDS,
					DelayedTypeEnum.ResendMessage);
			msg.setItem(sipMsg);
			DelayedContainer.getInstance().addItem(msg);
			// 如果为短消息则修改消息状态为重发中
			if (sipMsg.getMessageBody() instanceof ShortMessage) {
				MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
						new Timestamp(System.currentTimeMillis()), BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_RESEND);
				MessageStateContainer.getInstance().addMessageState(state);
			}
			// 如果消息是短消息生成失败回执，如果是回执不用处理
		} else if (sipMsg.getMessageBody() instanceof ShortMessage) {
			logger.info("this message has send " + BizConsts.DEFAULT_MESSAGE_RESEND_TIMES + " times,send fail receipt");
			imReceipt.receiptHandle(sipMsg);
		}
	}

	public void setImReceipt(IMReceiptHandler imReceipt) {
		this.imReceipt = imReceipt;
	}

	private Map<ResendParamEnum, Integer> getResendParams(int status) {
		Map<ResendParamEnum, Integer> params = new HashMap<ResendParamEnum, Integer>();
		params.put(ResendParamEnum.resendTimes, resendTimes);
		// long interval = Long.parseLong(String.valueOf(resendInterval));
		params.put(ResendParamEnum.resendInterval, resendInterval);
		return params;
	}

	enum ResendParamEnum {
		resendTimes, resendInterval
	}

}
