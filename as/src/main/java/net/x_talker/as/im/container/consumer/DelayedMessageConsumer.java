package net.x_talker.as.im.container.consumer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.CacheIMContainer;
import net.x_talker.as.im.container.DelayedContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.handler.receipt.IMReceiptHandler;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.util.SpringBeanUtil;

public class DelayedMessageConsumer extends ASLoopThread {

	private Logger logger = Logger.getLogger(DelayedMessageConsumer.class);

	public DelayedMessageConsumer(String threadName) {
		super(threadName);
	}

	public DelayedMessageConsumer() {
		this("DelayedMessageConsumer");
	}

	@Override
	public void doService() {
		DelayedContainer container = DelayedContainer.getInstance();
		if (container.size() == 0) {
			sleep(50);
			return;
		}
		DelayedMessage<XTalkerSipMsg> delayedMessage = DelayedContainer.getInstance().pollItem();
		if (delayedMessage == null) {
			sleep(50);
		} else {
			try {
				DelayedTypeEnum type = delayedMessage.getType();
				XTalkerSipMsg sipMsg = delayedMessage.getItem();
				type.doExpireService(sipMsg);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	@Override
	public void exceptionHandle() {
		// TODO Auto-generated method stub

	}

}
