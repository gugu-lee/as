package net.x_talker.as.im.container.entity;

import java.sql.Timestamp;

import javax.sip.address.URI;
import javax.sip.message.Response;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.CacheIMContainer;
import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.handler.IMRegisterHandler;
import net.x_talker.as.im.handler.exceptionhandler.IMExceptionHandler;
import net.x_talker.as.im.handler.receipt.IMReceiptHandler;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.persist.entity.MessageState;

/**
 * 延时处理类型定义枚举类<br>
 * 抽象元素到期处理逻辑<br>
 * 
 * @author zengqiaowen
 *
 */
public enum DelayedTypeEnum {

	/**
	 * 缓存消息类型,针对接收用户不在线,无法下发暂时缓存消息类型<br>
	 * 该类型到期表示缓存消息到期仍未下发,需要对短消息缓存进行清除操作并生成超时回执<br>
	 */
	CacheMessage {
		private Logger logger = Logger.getLogger(getClass());

		@Override
		public void doExpireService(XTalkerSipMsg sipMsg) {
			CacheIMContainer cacheImContainer = CacheIMContainer.getInstance();
			URI to = sipMsg.getTo().getURI();
			// 从缓存队列中清除到期消息
			boolean isRemove = cacheImContainer.removeSingleItem(to, sipMsg);
			if (isRemove) {
				logger.info("cache message is expired, delete cache message" + "message from:" + sipMsg.getFrom()
						+ ", to:" + sipMsg.getTo() + ", submit time:" + sipMsg.getSubmitTime());

				IMRegisterHandler register = (IMRegisterHandler) SpringBeanUtil.getBeanByName("IMRegisterHandler");
				// 再次判断接收用户是否在线,如在线则发送短消息
				if (register.getUserState(to, false)) {
					SendIMContainer.getInstance().addItem(sipMsg);
				} else {// 不在线,超时回执处理
					// 如果为短消息则修改消息状态为消息过期
					if (sipMsg.getMessageBody() instanceof ShortMessage) {
						MessageState state = new MessageState(sipMsg.getMessageBody().getMessageId(),
								new Timestamp(System.currentTimeMillis()),
								BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_MESSAGE_EXPIRED);
						MessageStateContainer.getInstance().addMessageState(state);
					}

					sipMsg.setStatus(Response.REQUEST_TIMEOUT);
					IMReceiptHandler imReceipt = (IMReceiptHandler) SpringBeanUtil.getBeanByName("IMReceiptHandler");
					imReceipt.receiptHandle(sipMsg);
				}

			} else {// 如果未清除成功则认为该消息已下发
				logger.info("cache message is expired, delete cache message failure!" + "message from:"
						+ sipMsg.getFrom() + ", to:" + sipMsg.getTo() + ", submit time:" + sipMsg.getSubmitTime());
			}

		}
	},
	/**
	 * 定时发送消息类型,处理到期再发送类型<br>
	 * 该消息到期即转移消息对象到发送队列当中<br>
	 */
	ScheduleMessage {
		private Logger logger = Logger.getLogger(getClass());

		@Override
		public void doExpireService(XTalkerSipMsg sipMsg) {
			logger.info("schedule message is scheduled, message from:" + sipMsg.getFrom() + ", to:" + sipMsg.getTo()
					+ ", submit time:" + sipMsg.getSubmitTime());
			SendIMContainer.getInstance().addItem(sipMsg);

		}
	},
	/**
	 * 已发送消息类型,等回执定时处理<br>
	 * 该消息到期处理,表示规定时间内接收端未返回消息接收响应,此时认发送超时,将该消息转至发送异常处理流程<br>
	 * 
	 */
	SendedMessage {
		private Logger logger = Logger.getLogger(getClass());

		@Override
		public void doExpireService(XTalkerSipMsg sipMsg) {
			logger.info("sended message wait response is expried, message from:" + sipMsg.getFrom() + ", to:"
					+ sipMsg.getTo() + ", submit time:" + sipMsg.getSubmitTime());
			sipMsg.setStatus(Response.REQUEST_TIMEOUT);
			IMExceptionHandler imException = (IMExceptionHandler) SpringBeanUtil.getBeanByName("IMExceptionHandler");
			imException.exceptionResponseHandle(sipMsg);

		}
	},
	/**
	 * 重发消息类型,类似于定时发送类型<br>
	 * 该消息到期表未重发间隔已到,转移消息对象到发送队列<br>
	 */
	ResendMessage {
		private Logger logger = Logger.getLogger(getClass());

		@Override
		public void doExpireService(XTalkerSipMsg sipMsg) {
			logger.info("resend message is scheduled, message from:" + sipMsg.getFrom() + ", to:" + sipMsg.getTo()
					+ ", submit time:" + sipMsg.getSubmitTime());
			SendIMContainer.getInstance().addItem(sipMsg);

		}
	};

	/**
	 * 元素到期处理接口方法
	 * 
	 * @param sipMsg
	 */
	public abstract void doExpireService(XTalkerSipMsg sipMsg);
}
