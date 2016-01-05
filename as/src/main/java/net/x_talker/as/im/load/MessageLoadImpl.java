
package net.x_talker.as.im.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import net.x_talker.as.common.util.Util;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.CacheIMContainer;
import net.x_talker.as.im.container.DelayedContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.SendedIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.DelayedMessage;
import net.x_talker.as.im.container.entity.DelayedTypeEnum;
import net.x_talker.as.im.container.entity.ShortMessage;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.persist.IMPersist;
import net.x_talker.as.persist.entity.MessageLog;

/**
 * 【从数据库中获取不同状态的消息并加到不同的缓存队列中去】
 *
 */
@Repository
public class MessageLoadImpl implements MessageLoad {

	private static Logger logger = Logger.getLogger(MessageLoadImpl.class);
	private static final String destPath = PropertiesUtil.getInstance().getPropVal(BizConsts.CONFKEY_MESSAGE_FILE_PATH);

	@Resource(name = "IMPersistImpl")
	private IMPersist persister;


	@Override
	public boolean loadRegularlyMessage() {
		List<MessageLog> messages = persister
				.getMessageLogsByState(BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_WAIT_REGULARLY_SEND);
		if (messages != null && messages.size() > 0) {
			logger.info("load regularly message,message size:" + messages.size());
			for (MessageLog message : messages) {
				XTalkerSipMsg sipMsg = getXTalkerSipMsgFromFile(destPath, message.getCallId());
				if (sipMsg != null) {
					ShortMessage shortMessage = (ShortMessage) sipMsg.getMessageBody();
					long delayTime = shortMessage.getScheduleDeliveryTime().getTime() - System.currentTimeMillis();
					if (delayTime < 0) {
						// TODO 给个默认值还是怎么样？
					}
					DelayedMessage<XTalkerSipMsg> msg = new DelayedMessage<XTalkerSipMsg>(delayTime,
							TimeUnit.MILLISECONDS, DelayedTypeEnum.ScheduleMessage);
					msg.setItem(sipMsg);
					DelayedContainer.getInstance().addItem(msg);
				}
			}
		}
		return true;
	}

	/**
	 * 【加载缓存的消息，消息状态码为：3】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.load.MessageLoad#loadCacheMessage()
	 */
	@Override
	public boolean loadCacheMessage() {
		List<MessageLog> cachedMessages = persister
				.getMessageLogsByState(BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_USER_NOT_ONLINE_WAIT);
		if (cachedMessages != null && cachedMessages.size() > 0) {
			logger.info("load cache message,message size:" + cachedMessages.size());
			for (MessageLog message : cachedMessages) {
				XTalkerSipMsg sipMsg = getXTalkerSipMsgFromFile(destPath, message.getCallId());

				if (sipMsg != null) {
					long delayTime = 0;
					delayTime = Util.hourToMilliseconds(PropertiesUtil.getInstance()
							.getPropIntVal(BizConsts.CONFKEY_DEFAULT_MESSAGE_VALIDITY_PERIOD));
					ShortMessage shortMessage = (ShortMessage) sipMsg.getMessageBody();
					Date earlierTime = Util.getEarlierTime(shortMessage.getValidityPeriod(),
							new Timestamp(System.currentTimeMillis() + delayTime));
					delayTime = earlierTime.getTime() - System.currentTimeMillis();

					if (delayTime < 0) {
						// 由于服务器原因导致消息过期了，怎么办？
					}

					CacheIMContainer.getInstance().addCacheMsg(sipMsg);
					DelayedMessage<XTalkerSipMsg> delayMsg = new DelayedMessage<XTalkerSipMsg>(delayTime,
							TimeUnit.MILLISECONDS, DelayedTypeEnum.CacheMessage);
					delayMsg.setItem(sipMsg);
					DelayedContainer.getInstance().addItem(delayMsg);
				}
			}
		}
		return true;
	}

	/**
	 * 【加载缓存在发送队列的消息，状态码为：0,4】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.load.MessageLoad#loadSendMessage()
	 */
	@Override
	public boolean loadSendMessage() {
		List<MessageLog> recivedMessages = persister
				.getMessageLogsByState(BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_RECIVED);
		List<MessageLog> resendMessages = persister
				.getMessageLogsByState(BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_RESEND);
		if (recivedMessages != null && recivedMessages.size() > 0) {
			logger.info("load recived message,message size:" + recivedMessages.size());
			for (MessageLog message : recivedMessages) {
				XTalkerSipMsg sipMsg = getXTalkerSipMsgFromFile(destPath, message.getCallId());
				if (sipMsg != null) {
					SendIMContainer.getInstance().addSendIMMsg(sipMsg);
				}
			}
		}

		if (resendMessages != null && resendMessages.size() > 0) {
			logger.info("load resend message,message size:" + resendMessages.size());
			for (MessageLog message : resendMessages) {
				XTalkerSipMsg sipMsg = getXTalkerSipMsgFromFile(destPath, message.getCallId());
				if (sipMsg != null) {
					SendIMContainer.getInstance().addSendIMMsg(sipMsg);
				}
			}
		}
		return true;
	}

	/**
	 * 【加载已发送还没有回执的消息，状态码为：1】
	 * 
	 * (non-Javadoc)
	 * 
	 * @see net.x_talker.as.im.load.MessageLoad#loadSendedMessage()
	 */
	@Override
	public boolean loadSendedMessage() {
		List<MessageLog> sendedMessages = persister
				.getMessageLogsByState(BizConsts.MessageStateCode.MESSAGE_SATTE_SMC_SENDED);
		if (sendedMessages != null && sendedMessages.size() > 0) {
			logger.info("load sended message,message size:" + sendedMessages.size());
			for (MessageLog message : sendedMessages) {
				XTalkerSipMsg sipMsg = getXTalkerSipMsgFromFile(destPath, message.getCallId());
				if (sipMsg != null) {
					SendedIMContainer.getInstance().addItem(sipMsg.getCallId(), sipMsg);
				}
			}
		}
		return true;
	}

	private XTalkerSipMsg getXTalkerSipMsgFromFile(String path, String fileName) {
		XTalkerSipMsg sipMsg = null;
		try {
			if (fileName == null || fileName.trim().equals("")) {
				return sipMsg;
			}
			File file = Util.getFileInPath(path, fileName);
			if (file != null) {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
				sipMsg = (XTalkerSipMsg) in.readObject();
				in.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return sipMsg;
	}
	
	public XTalkerSipMsg loadSingleXTalkerSipMsgFromFile(String callID)
	{
		return getXTalkerSipMsgFromFile(destPath,callID);
	}
}
