package net.x_talker.as.im.container.consumer;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.inf.IMSendInf;
import net.x_talker.as.im.inf.IMSimpleSender;

public class SendIMConsumer extends ASLoopThread {

	public SendIMConsumer(String threadName) {
		super(threadName);
	}

	public SendIMConsumer() {
		this("SendIMConsumer");
	}

	private Logger logger = Logger.getLogger(SendIMConsumer.class);

	private IMSendInf imSender = new IMSimpleSender();

	@Override
	public void doService() {
		try {
			XTalkerSipMsg sipMsg = SendIMContainer.getInstance().pollItem();
			if (sipMsg == null) {

				sleep(50);
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("send message to:" + sipMsg.getTo());
				}

				imSender.sendIMMessage(sipMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void exceptionHandle() {


	}
}
