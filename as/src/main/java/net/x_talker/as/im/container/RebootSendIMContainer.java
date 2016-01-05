package net.x_talker.as.im.container;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.consumer.DelayedMessageConsumer;
import net.x_talker.as.im.container.consumer.RebootSendIMConsumer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.IMPriorityComparator;
import net.x_talker.as.im.ha.HAQueue;

//关机短信延时发送缓存

public class RebootSendIMContainer extends HAQueue<XTalkerSipMsg> {

	private Logger logger = Logger.getLogger(DelayedMessageConsumer.class);
	private static RebootSendIMContainer instance = new RebootSendIMContainer("RebootSendIMContainer");
	private RebootSendIMConsumer t;
	private ExecutorService pool;

	protected RebootSendIMContainer(String queueName) {
		super(queueName);
		pool = Executors.newCachedThreadPool();
		logger.info("start RebootSendIM consumer.....");
		t = new RebootSendIMConsumer();
	}

	private void execute() {
		if (!t.isStart()) {
			pool.execute(t);
		}
	}

	public static final int SendIMContainerDefaultSize = 10000;

	public static RebootSendIMContainer getInstance() {

		return instance;
	}

	public boolean doAdd(XTalkerSipMsg sipMsg) {
		instance.execute();
		return super.doAdd(sipMsg);
	}

	public XTalkerSipMsg doPoll() {
		return super.doPoll();
	}

	public int size() {
		return super.queue.size();
	}

	@Override
	protected void initQueue() {
		super.queue = new PriorityQueue<XTalkerSipMsg>(10000, new IMPriorityComparator());

	}

}
