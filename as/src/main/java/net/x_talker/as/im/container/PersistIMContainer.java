package net.x_talker.as.im.container;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.consumer.PersistIMConsumer;
import net.x_talker.as.im.ha.HAQueue;
import net.x_talker.as.persist.entity.MessageLog;

public class PersistIMContainer extends HAQueue<MessageLog> {

	private static PersistIMContainer instance = new PersistIMContainer(BizConsts.PERSIST_IM_CONTAINER);

	private Logger logger = Logger.getLogger(PersistIMContainer.class);

	protected PersistIMContainer(String queueName) {
		super(queueName);

		ExecutorService pool = Executors.newCachedThreadPool();

		for (int i = 0; i < 5; i++) {
			logger.info("start persist IM consumer.....");
			PersistIMConsumer t = new PersistIMConsumer();
			pool.execute(t);
		}
	}

	/**
	 * 【请在此输入描述文字】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.ha.HAQueue#initQueue()
	 */
	@Override
	protected void initQueue() {
		super.queue = new ConcurrentLinkedQueue<MessageLog>();

	}

	public static PersistIMContainer getInstance() {
		return instance;
	}

	public void addIM(MessageLog messageLog) {
		// MessageLog messageLog = SipServletRequest2MessageLog(sipServletReq);
		this.addItem(messageLog);
	}

	public MessageLog getPersistIM() {
		return this.pollItem();
	}

}
