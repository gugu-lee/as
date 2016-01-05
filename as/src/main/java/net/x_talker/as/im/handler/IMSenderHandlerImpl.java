package net.x_talker.as.im.handler;

import java.io.IOException;

import javax.sip.ClientTransaction;
import javax.sip.message.Request;

import org.springframework.stereotype.Component;

import net.x_talker.as.Main;

/**
 * 短消息发送流程处理
 * 
 * @author zengqiaowen
 *
 */
@Component
public class IMSenderHandlerImpl implements IMSenderHandler {

	/**
	 * 短消息发送处理
	 */
	@Override
	public int handleIM(Request request) {
		try {
			ClientTransaction clientTid = Main.getSipProvider().getNewClientTransaction(request);
			clientTid.sendRequest();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}

}
