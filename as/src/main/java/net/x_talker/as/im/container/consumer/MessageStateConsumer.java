
package net.x_talker.as.im.container.consumer;

import java.util.List;

import net.x_talker.as.im.container.MessageStateContainer;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.persist.IMPersist;
import net.x_talker.as.persist.IMPersistImpl;
import net.x_talker.as.persist.entity.MessageState;

/**
 *
 * 
 * 
 */
public class MessageStateConsumer extends ASLoopThread {

	private IMPersist persister;

	/**
	 * 
	 * @param threadName
	 */
	public MessageStateConsumer(String threadName) {
		super(threadName);
	}

	public MessageStateConsumer() {
		this("MessageStateConsumer");
		persister = (IMPersistImpl) SpringBeanUtil.getBeanByName("IMPersistImpl");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.container.consumer.ASLoopThread#doService()
	 */
	@Override
	public void doService() {
		List<MessageState> stateList = MessageStateContainer.getInstance().getAndRemoveAllTimeoutMessageState();
		if (stateList == null || stateList.size() == 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			for (MessageState state : stateList) {
				persister.updateMState(state.getMessageId(), state.getState());
			}
		}

	}

	/**
	 * 
	 * @see net.x_talker.as.im.container.consumer.ASLoopThread#exceptionHandle()
	 */
	@Override
	public void exceptionHandle() {

	}

}
