package net.x_talker.as.im.filter;

import javax.sip.message.Message;

public interface IMRuleFilter {

	public static final int BREAK_RESULT = 0;
	public static final int CONTINUE_RESULT = 1;

	public int filter(Message sipMessage);

}
