package net.x_talker.as.persist;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import net.x_talker.as.persist.entity.MessageLog;
import net.x_talker.as.persist.entity.MessageState;

@Repository
@Transactional
@Component("IMPersistImpl")
public class IMPersistImpl implements IMPersist {

	private static Logger logger = Logger.getLogger(IMPersistImpl.class);

	@Autowired
	private IMessageDao messageDao;

	public IMPersistImpl() {

	}

	public boolean persistIM(MessageLog messageLog) 
	{
		if(messageLog!=null){
			
			if (getMessageLogByCallID(messageLog.getCallId())==null){
				logger.info("call id is not exist:"+messageLog.getCallId());
				messageDao.persistIM(messageLog);
			}else
			{
				logger.info("call id is exist:"+messageLog.getCallId());
			}
		}
		return true;
	}

	public boolean insertMState(String messageId, int state) {
		MessageState messageState=new MessageState();
		messageState.setMessageId(messageId);
		messageState.setState(state);
		messageDao.insertMState(messageState);
		return true;
	}



	public boolean updateMState(String messageId, int state) {
		MessageState messageState=new MessageState();
		messageState.setMessageId(messageId);
		messageState.setState(state);
		MessageState messageStateInDb=(MessageState) messageDao.getMessageState(messageState);
		if(messageStateInDb!=null){
			messageDao.updateMState(messageState);
			return true;
		}
		insertMState(messageId,state);
		return false;
	}


	public List<MessageLog> getMessageLogsByState(int state) {
		return messageDao.getMessageLogsByState(state);
	}

	public MessageLog getMessageLogByCallID(String callId) {
		MessageLog messageLog = messageDao.getMessageLogByCallId(callId);
		return messageLog;
	}

}
