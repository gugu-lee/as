package net.x_talker.as.im.container;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.consumer.DelayedMessageConsumer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.container.entity.ShortMessage;

/**
 * 定时发送消息缓存容器
 * 
 * @author zengqiaowen
 *
 */
@Deprecated
public class RegularlyIMContainer {

	private Logger logger = Logger.getLogger(RegularlyIMContainer.class);
	private static DelayQueue<DelayedMessage<XTalkerSipMsg>> regularlyIMQueue = new DelayQueue<DelayedMessage<XTalkerSipMsg>>();

	private static RegularlyIMContainer instance = new RegularlyIMContainer();

	private RegularlyIMContainer() {

		// ExecutorService pool = Executors.newCachedThreadPool();
		//
		// for(int i = 0; i < 5; i++) {
		// logger.info("start regularly IM consumer.....");
		// DelayedMessageConsumer t = new DelayedMessageConsumer();
		// pool.execute(t);
		// }
	}

	public static RegularlyIMContainer getInstance() {
		return instance;
	}

	public void addRegularlyIMMsg(DelayedMessage<XTalkerSipMsg> msg) {
		try {
			if (msg.getItem().getMessageBody() instanceof ShortMessage) {
				if (!((ShortMessage) msg.getItem().getMessageBody()).getIsReceipt()
						&& ((ShortMessage) msg.getItem().getMessageBody()).getScheduleDeliveryTime() != null
						&& msg.getType() == DelayedTypeEnum.ScheduleMessage) {
					regularlyIMQueue.add(msg);
				}
			} else {
				logger.warn("this message is not a short message,callId=" + msg.getItem().getCallId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public DelayedMessage<XTalkerSipMsg> pollRegularlyIMMsg() {
		return regularlyIMQueue.poll();
	}

	public int getRegularlyIMMsgCount() {
		return regularlyIMQueue.size();
	}

}
