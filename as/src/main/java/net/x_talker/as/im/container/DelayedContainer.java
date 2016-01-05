package net.x_talker.as.im.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.consumer.ASLoopThread;
import net.x_talker.as.im.container.consumer.DelayedMessageConsumer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.ha.HAQueue;
import net.x_talker.as.im.util.PropertiesUtil;

/**
 * 消息延时过期容器
 * 
 * 缓存未下发消息,延时设置到消息有效期 定时发送消息,延时设置到消息发送时间 已发送消息,延时设置到响应接收过期时间
 * 
 * 当用户上线后,下发缓存消息,应将对应缓存消息元素移除 已发送消息接收到响应应将已发送消息延时元素移除
 * !暂时不使用,在移除过期元素时清掉缓存中的对应元素来使的过期容器和缓存容器同步
 * 
 *
 */
public class DelayedContainer extends HAQueue<DelayedMessage<XTalkerSipMsg>> {

	public static final String DELAY_MESSAGE_CONSUMER_NUM = "com.ims.as.delay.mesage.consumer.num";
	public static final int DEFAULT_DELAY_MESSAGE_CONSUMER_NUM = 5;

	private Logger logger = Logger.getLogger(DelayedContainer.class);

	private static DelayedContainer instance = new DelayedContainer(BizConsts.DELAYED_MESSAGE_CONTAINER_NAME);

	private List<ASLoopThread> consumers;

	protected DelayedContainer(String queueName) {
		super(queueName);
		startConsumer();
	}

	public static DelayedContainer getInstance() {
		return instance;
	}

	public int getRemainSize() {
		return queue.size();
	}

	@Override
	protected void initQueue() {
		this.queue = new DelayQueue<DelayedMessage<XTalkerSipMsg>>();
	}

	public void startConsumer() {
		if (consumers != null && !consumers.isEmpty()) {
			return;
		}
		consumers = new ArrayList<ASLoopThread>();

		int num = 0;
		String consumerNum = PropertiesUtil.getInstance().getPropVal(DELAY_MESSAGE_CONSUMER_NUM);
		try {
			num = Integer.parseInt(consumerNum);
		} catch (Exception e) {
			num = DEFAULT_DELAY_MESSAGE_CONSUMER_NUM;
		}

		ExecutorService pool = Executors.newCachedThreadPool();

		for (int i = 0; i < num; i++) {
			logger.info("start delayed message consumer.....");
			DelayedMessageConsumer t = new DelayedMessageConsumer();
			pool.execute(t);
			consumers.add(t);
		}
	}

	public void stopConsumer() {
		if (consumers == null || consumers.isEmpty()) {
			return;
		}

		for (ASLoopThread r : consumers) {
			// r.setStop();
			r.shutdown();
		}
	}
}
