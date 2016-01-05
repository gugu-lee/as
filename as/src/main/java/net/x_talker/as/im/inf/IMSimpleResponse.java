package net.x_talker.as.im.inf;

import javax.annotation.Resource;
import javax.sip.header.CallIdHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import net.x_talker.as.im.container.SendedIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.handler.exceptionhandler.IMExceptionHandler;
import net.x_talker.as.im.handler.receipt.IMReceiptHandler;
import net.x_talker.as.im.container.entity.ShortMessage;

@Repository
public class IMSimpleResponse implements IMResponseInf {

	private Logger logger = Logger.getLogger(IMSimpleResponse.class);

	@Resource
	private IMReceiptHandler imReceipt;
	@Resource(name = "IMExceptionHandler")
	private IMExceptionHandler imException;

	public void onErrorResponse(Response resp) {
		String callId = resp.getHeader("CallID").toString();
		XTalkerSipMsg sipMsg = SendedIMContainer.getInstance().removeItem(callId);
		if (sipMsg == null) {
			logger.warn("not send this message,callId:" + callId);
		} else {
			sipMsg.setStatus(resp.getStatusCode());
			imException.exceptionResponseHandle(sipMsg);
		}
		return ;
	}

	public void onSuccessResponse(Response resp) {
		String callId = ((CallIdHeader)resp.getHeader(CallIdHeader.NAME)).getCallId();
		XTalkerSipMsg sipMsg = SendedIMContainer.getInstance().removeItem(callId);
		if (sipMsg == null) {
			logger.warn("not send this message,callId:" + callId);
			return ;
		}

		if (sipMsg.getMessageBody() instanceof ShortMessage) {
			sipMsg.setStatus(resp.getStatusCode());
			imReceipt.receiptHandle(sipMsg);
		}

	}

}
