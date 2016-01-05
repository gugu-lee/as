package net.x_talker.as.im.container.consumer;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.RebootSendIMContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;

public class RebootSendIMConsumer extends ASLoopThread {
	private final long delayTime = 5 * 1000;

	public RebootSendIMConsumer(String threadName) {
		super(threadName);
	}

	public RebootSendIMConsumer() {
		this("RebootSendIMConsumer");
	}

	private Logger logger = Logger.getLogger(RebootSendIMConsumer.class);

	@Override
	public void doService() {
		try {
			XTalkerSipMsg sipMsg = RebootSendIMContainer.getInstance().doPoll();
			logger.info("RebootSendIMContainer's size is :" + RebootSendIMContainer.getInstance().size());
			if (sipMsg == null) {
				shutdown();
				sleep(50);
				return;
			}
			long remander = System.currentTimeMillis() - sipMsg.getRebootTime();
			if (logger.isInfoEnabled()) {
				logger.info("send message to:" + sipMsg.getTo());
			}
			if ((remander - delayTime) < 0) {
				logger.info("sipMsg:" + sipMsg + "reboottime:" + sipMsg.getRebootTime() + " remander:" + remander);
				RebootSendIMContainer.getInstance().doAdd(sipMsg);
				logger.info(
						"after add :RebootSendIMContainer's size is :" + RebootSendIMContainer.getInstance().size());
				return;
			} else {
				SendIMContainer.getInstance().addItem(sipMsg); // 将未下发的缓存消息加入到待发送队列当中
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void exceptionHandle() {
		// TODO Auto-generated method stub

	}

}
