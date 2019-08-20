package net.x_talker.as;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.freeims.javax.sip.SipStackImpl;
import org.freeims.javax.sip.message.SIPMessage;
import org.freeims.javax.sip.message.SIPRequest;
import org.freeims.javax.sip.message.SIPResponse;
import org.freeims.javax.sip.stack.SIPServerTransaction;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.inf.IMReceiverInf;
import net.x_talker.as.im.inf.IMResponseInf;
import net.x_talker.as.im.inf.IMSimpleReceiver;
import net.x_talker.as.im.inf.IMSimpleResponse;
import net.x_talker.as.im.servlet.IMResponse;


public class AsSipListener implements SipListener {
	private Logger logger = Logger.getLogger(AsSipListener.class);
	//@Resource(name="IMSimpleResponse")
	private IMResponseInf imResponse;

	//@Resource(name="IMSimpleReceiver")
	private IMReceiverInf imReceiver;

	public void init() {
		imResponse = (IMSimpleResponse) Main.getApplicationContext().getBean("IMSimpleResponse");
		imReceiver = (IMSimpleReceiver) Main.getApplicationContext().getBean("IMSimpleReceiver");
	}

	public void processRequest(RequestEvent requestEvent) {
		//first deal for protocol ,example for send back response.
		preprocessRequestEvent(requestEvent);
		SIPRequest request = (SIPRequest)requestEvent.getRequest();

		if (request.getMethod().equals(Request.MESSAGE)) {
			doMessage(requestEvent);
		} else if (request.getMethod().equals(Request.REGISTER)) {
			doRegister(requestEvent);
		}
		
	}

	public void processResponse(ResponseEvent responseEvent) {
		Response resp = responseEvent.getResponse();
		
		if (resp.getStatusCode() == Response.OK) {
			MessageFactory messageFactory = Main.getMessageFactory();// SipFactories.messageFactory;
			XTalkerSipMsg sipMsg = Main.getMessageLoad().loadSingleXTalkerSipMsgFromFile(
					((CallIdHeader)resp.getHeader(CallIdHeader.NAME)).getCallId());
			
			String messageText = new String(sipMsg.getRawData());
			try{
				SIPRequest request = (SIPRequest) messageFactory.createRequest(messageText);
				
				SIPResponse respReturnOK = request.createResponse(Response.OK);
	
				
				SIPServerTransaction st = null;
				
				SipStackImpl sipStack = (SipStackImpl)Main.getSipProvider().getSipStack();
				st=(SIPServerTransaction) sipStack.findTransaction((SIPMessage)request, true);
				
				if (st ==null){
					logger.info("new server transaction");
					st = (SIPServerTransaction)Main.getSipProvider().getNewServerTransaction(request);
				}
				
				logger.info("server transaction state:"+st.getState().getValue());
				st.sendMessage(respReturnOK);
				logger.info("sent OK response");
			}catch(Exception e){
				logger.info(e.getMessage(),e);
			}
			imResponse.onSuccessResponse(resp);
		}
		//else if (resp.getStatusCode() >=400 && resp.getStatusCode()<500){
			
		//}
		
	}

	public void processTimeout(TimeoutEvent timeoutEvent) {

	}

	public void processIOException(IOExceptionEvent exceptionEvent) {

	}

	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

	}

	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

	}

	private Response doMessage(RequestEvent requestEvent) {

		SIPRequest request = (SIPRequest)requestEvent.getRequest();
		IMResponse resp = imReceiver.onMessageReceive(request);
		Response response = request.createResponse(resp.getStatuscode(), resp.getReasonPhrase());
		return response;
		
	}

	private Response doRegister(RequestEvent requestEvent) {
		SIPRequest request = (SIPRequest)requestEvent.getRequest();
		imReceiver.onRegisterReceive(request);
		return null;
	}

	private void preprocessRequestEvent(RequestEvent requestEvent)
	{
		SIPRequest request = (SIPRequest)requestEvent.getRequest();
		Response resp = null;
		if (request == null){
			return ;
		}else if (request.getMethod().equals(Request.MESSAGE)) {
			resp = request.createResponse(Response.ACCEPTED);
		} else if (request.getMethod().equals(Request.REGISTER)) {
			resp = request.createResponse(Response.OK);
		}
		ServerTransaction st = null;
		st = requestEvent.getServerTransaction();
		try {
			if (st==null){
				st = Main.getSipProvider().getNewServerTransaction(requestEvent.getRequest());
			}
			if (st!=null){
				st.sendResponse(resp);
			}else{
				logger.info("could not get serverTransaction,so could not send back response");
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		
	}
	
}
