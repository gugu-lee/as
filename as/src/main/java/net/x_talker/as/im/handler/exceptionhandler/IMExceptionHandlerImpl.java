package net.x_talker.as.im.handler.exceptionhandler;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.handler.receipt.IMReceiptHandler;
import net.x_talker.as.im.handler.resend.IMResendHandler;

/**
 * 异常处理接口实现类
 * 
 * @author zengqiaowen
 *
 */
@Component("IMExceptionHandler")
public class IMExceptionHandlerImpl implements IMExceptionHandler {

	private Logger logger = Logger.getLogger(getClass());

	@Resource(name = "IMResendHandler")
	private IMResendHandler resend;
	@Resource(name = "IMReceiptHandler")
	private IMReceiptHandler imReceipt;

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionResponseHandle(XTalkerSipMsg sipMsg) {
		int status = sipMsg.getStatus();
		if (status < 300) {
			logger.info("response status is:" + status + ", is not a exception status");
			return;
		}

		// 需要重发处理的短消息交由重发流程处理,否则回执处理
		if (isResend(status)) {
			logger.info("message is need resend deal, from:" + sipMsg.getFrom() + ",to:" + sipMsg.getTo());
			resend.resendIM(sipMsg);
		} else {
			logger.info("message is need resend deal,do receipt handle, from:" + sipMsg.getFrom() + ",to:"
					+ sipMsg.getTo());
			imReceipt.receiptHandle(sipMsg);
		}

	}

	public void setResend(IMResendHandler resend) {
		this.resend = resend;
	}

	/**
	 * 根据错误响应码确定该短消息是否需要重发处理
	 * 
	 * @param errorCode
	 * @return
	 */
	private boolean isResend(int errorCode) {
		// TODO 487表示IMSC返回目的手机未注册，待注册后重发
		return true;
	}

}
