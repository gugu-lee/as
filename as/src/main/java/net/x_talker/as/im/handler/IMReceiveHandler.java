package net.x_talker.as.im.handler;

import javax.sip.message.Request;

import net.x_talker.as.im.servlet.IMResponse;

public interface IMReceiveHandler {
	public IMResponse handleIM(Request request);
}
