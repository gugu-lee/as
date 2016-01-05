package net.x_talker.as.persist;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import net.x_talker.as.persist.entity.MessageLog;
import net.x_talker.as.persist.entity.MessageState;

public interface IMessageDao {
	
	public boolean persistIM(MessageLog messageLog);

	public boolean insertMState(MessageState state);

	public boolean updateMState(MessageState state);

	public MessageState getMessageState(MessageState state);
	public List<MessageLog> getMessageLogsByState(int state);
	
	
	//@Select("select * from message_log where call_id=#{callId}")
	public MessageLog getMessageLogByCallId(String callId);
}
