
package net.x_talker.as.im.container.consumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.MessageContentFileContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsgOpFile;
import net.x_talker.as.im.util.PropertiesUtil;

/**
 * 【消息内容写入文件保存的缓存容器的消费线程，取出消息并写入文件】
 *
 * @version
 * @author xubo 2014-4-21 上午10:33:04
 * 
 */
public class MessageContentFileConsumer extends ASLoopThread {

	private static Logger logger = Logger.getLogger(MessageContentFileConsumer.class);

	/**
	 * MessageContentFileConsumer 构造器 【请在此输入描述文字】
	 * 
	 * @param threadName
	 */
	public MessageContentFileConsumer(String threadName) {
		super(threadName);
		// TODO 这是系统自动生成描述，请在此补完后续代码
	}

	public MessageContentFileConsumer() {
		this("MessageContentFileConsumer");
	}

	/**
	 * 【请在此输入描述文字】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.container.consumer.ASLoopThread#doService()
	 */
	@Override
	public void doService() {
		String destPath = PropertiesUtil.getInstance().getPropVal(BizConsts.CONFKEY_MESSAGE_FILE_PATH);
		XTalkerSipMsgOpFile sipMsgOpFile = MessageContentFileContainer.getInstance().getMsgToWriteFile();
		if (sipMsgOpFile == null) {
			sleep(50);
		} else if (sipMsgOpFile.getSipMsg() != null && sipMsgOpFile.getSipMsg().getCallId() != null
				&& !sipMsgOpFile.getSipMsg().getCallId().trim().equals("")) {
			switch (sipMsgOpFile.getOperate()) {
			case BizConsts.X_TALKER_SIP_MSG_OP_FILE_TYPE_CREATE:
				try {
					File file = Util.createFileInPath(destPath, sipMsgOpFile.getSipMsg().getCallId());
					FileOutputStream outstream = new FileOutputStream(file);
					ObjectOutputStream out = new ObjectOutputStream(outstream);
					out.writeObject(sipMsgOpFile.getSipMsg());
					out.close();
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage(), e);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				break;
			case BizConsts.X_TALKER_SIP_MSG_OP_FILE_TYPE_DELETE:
				Util.deleteFileInPath(destPath, sipMsgOpFile.getSipMsg().getCallId());
				break;
			default:
				break;
			}
		} else {
			logger.warn("message is wrong");
		}

	}

	/**
	 * 【请在此输入描述文字】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.container.consumer.ASLoopThread#exceptionHandle()
	 */
	@Override
	public void exceptionHandle() {
		// TODO 这是系统自动生成描述，请在此补完后续代码

	}

}
