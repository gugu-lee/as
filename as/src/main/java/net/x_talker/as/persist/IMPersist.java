package net.x_talker.as.persist;

import java.util.List;

//import javax.servlet.sip.SipServletRequest;

import net.x_talker.as.persist.entity.MessageLog;

public interface IMPersist {

	public boolean persistIM(MessageLog messageLog);

	public boolean insertMState(String messageId, int state);

	public boolean updateMState(String messageId, int state);

	public List<MessageLog> getMessageLogsByState(int state);

	public MessageLog getMessageLogByCallID(String callID);
}
