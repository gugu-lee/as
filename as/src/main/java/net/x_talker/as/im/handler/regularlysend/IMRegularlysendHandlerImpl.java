
package net.x_talker.as.im.handler.regularlysend;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.DelayedContainer;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.persist.entity.MessageState;

@Component("IMRegularlysendHandler")
public class IMRegularlysendHandlerImpl implements IMRegularlysendHandler {

	private Logger logger = Logger.getLogger(IMRegularlysendHandlerImpl.class);

	/**
	 * 【定时发送消息处理，将需要定时发送的消息加入到定时发送队列中】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.handler.regularlysend.IMRegularlysendHandler#regularlySendIM(net.x_talker.as.im.container.entity.XTalkerSipMsg)
	 */
	@Override
	public int regularlySendIM(XTalkerSipMsg sipMsg) {
		if (sipMsg.getMessageBody() instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) sipMsg.getMessageBody();
			long delayTime = shortMessage.getScheduleDeliveryTime().getTime() - System.currentTimeMillis();
			DelayedMessage<XTalkerSipMsg> msg = new DelayedMessage<XTalkerSipMsg>(delayTime, TimeUnit.MILLISECONDS,
					DelayedTypeEnum.ScheduleMessage);
			msg.setItem(sipMsg);
			logger.info("regularly send message, from:" + sipMsg.getFrom() + ",to:" + sipMsg.getTo() + ",send time:"
					+ shortMessage.getScheduleDeliveryTime());
			DelayedContainer.getInstance().addItem(msg);

			// 修改消息状态为等待定时发送
			MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
					new Timestamp(System.currentTimeMillis()),
					BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_WAIT_REGULARLY_SEND);
			MessageStateContainer.getInstance().addMessageState(state);
		} else {
			logger.warn("this message is not a short message,callId=" + sipMsg.getCallId());
		}
		return Response.ACCEPTED;
	}

}
