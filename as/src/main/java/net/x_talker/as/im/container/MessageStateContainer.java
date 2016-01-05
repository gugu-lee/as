
package net.x_talker.as.im.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.consumer.MessageStateConsumer;
import net.x_talker.as.im.ha.HACache;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.persist.entity.MessageState;

/**
 *
 * @version
 * @author xubo 2014-4-15 下午02:21:23
 * 
 */
public class MessageStateContainer extends HACache<String, MessageState> {

	private Logger logger = Logger.getLogger(MessageStateContainer.class);
	private Long statePeriod = 0L;

	protected MessageStateContainer(String cacheName) {
		super(cacheName);
		statePeriod = Util.secondsToMilliseconds(
				PropertiesUtil.getInstance().getPropIntVal(BizConsts.CONFKEY_MESSAGE_STATE_FLUSH_PERIOD));
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			logger.info("start message state IM consumer.....");
			MessageStateConsumer t = new MessageStateConsumer();
			pool.execute(t);
		}
	}

	private static MessageStateContainer instance = new MessageStateContainer(BizConsts.MESSAGE_STATE_CONTAINER_NAME);

	public static MessageStateContainer getInstance() {
		return instance;
	}

	/**
	 * 【请在此输入描述文字】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.ha.HACache#initCache()
	 */
	@Override
	protected void initCache() {
		this.cache = new ConcurrentHashMap<String, MessageState>();

	}

	public void addMessageState(MessageState state) {
		if (state.getMessageId() != null) {
			this.addItem(state.getMessageId(), state);
		} else {
			logger.warn("the MessageState's MessageId is null");
		}
	}


	public List<MessageState> getAndRemoveAllTimeoutMessageState() {
		Long currentTime = System.currentTimeMillis();
		List<MessageState> stateList = new ArrayList<MessageState>();
		for (String messageId : this.cache.keySet()) {
			MessageState stateCache = this.get(messageId);
			if (stateCache != null && (currentTime - stateCache.getRecordTime().getTime()) >= statePeriod) {
				MessageState state = this.removeItem(messageId);
				if (state != null) {
					stateList.add(state);
				}
			}
		}
		return stateList;
	}

}
