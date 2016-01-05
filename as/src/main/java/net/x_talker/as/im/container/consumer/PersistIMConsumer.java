package net.x_talker.as.im.container.consumer;

import net.x_talker.as.im.container.PersistIMContainer;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.persist.IMPersist;
import net.x_talker.as.persist.IMPersistImpl;
import net.x_talker.as.persist.entity.MessageLog;

public class PersistIMConsumer extends ASLoopThread {

	private IMPersist persister;

	public PersistIMConsumer(String threadName) {
		super(threadName);
		persister = (IMPersistImpl) SpringBeanUtil.getBeanByName("IMPersistImpl");
	}

	public PersistIMConsumer() {
		this("PersistIMConsumer");
		persister = (IMPersistImpl) SpringBeanUtil.getBeanByName("IMPersistImpl");
	}

	@Override
	public void doService() {
		MessageLog msg = PersistIMContainer.getInstance().getPersistIM();
		if (msg == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			persister.persistIM(msg);
		}
	}

	@Override
	public void exceptionHandle() {

	}

}
