
package net.x_talker.as.im.container;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.consumer.MessageContentFileConsumer;
import net.x_talker.as.im.container.entity.XTalkerSipMsgOpFile;
import net.x_talker.as.im.ha.HAQueue;

/**
 * 【消息内容写入文件保存的缓存容器】
 *
 * @version
 * @author xubo 2014-4-21 上午10:21:26
 * 
 */
public class MessageContentFileContainer extends HAQueue<XTalkerSipMsgOpFile> {

	private static MessageContentFileContainer instance = new MessageContentFileContainer(
			BizConsts.MESSAGE_CONTENT_FILE_CONTAINER);

	private Logger logger = Logger.getLogger(MessageContentFileContainer.class);

	/**
	 * MessageContentFileContainer 构造器 【请在此输入描述文字】
	 * 
	 * @param queueName
	 */
	protected MessageContentFileContainer(String queueName) {
		super(queueName);

		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			logger.info("start message content file consumer.....");
			MessageContentFileConsumer t = new MessageContentFileConsumer();
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
		super.queue = new ConcurrentLinkedQueue<XTalkerSipMsgOpFile>();

	}

	public static MessageContentFileContainer getInstance() {
		return instance;
	}

	public void addMsgToWriteFile(XTalkerSipMsgOpFile sipMsgOpFile) {
		this.addItem(sipMsgOpFile);
	}

	public XTalkerSipMsgOpFile getMsgToWriteFile() {
		return this.pollItem();
	}

}
