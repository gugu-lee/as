package net.x_talker.as.im.util;

import gov.nist.javax.sip.address.AddressFactoryImpl;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.header.ContentLength;
import gov.nist.javax.sip.header.RequestLine;
import gov.nist.javax.sip.header.Route;
import gov.nist.javax.sip.header.RouteList;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.SIPHeaderList;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.header.ViaList;
import gov.nist.javax.sip.header.ims.SIPHeaderNamesIms;
import gov.nist.javax.sip.message.SIPMessage;
import net.x_talker.as.Main;
import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;

/*
import org.mobicents.servlet.sip.JainSipUtils;
import org.mobicents.servlet.sip.SipFactories;
import org.mobicents.servlet.sip.address.AddressImpl;
import org.mobicents.servlet.sip.core.session.MobicentsSipApplicationSession;
import org.mobicents.servlet.sip.core.session.MobicentsSipSession;
import org.mobicents.servlet.sip.core.session.SessionManagerUtil;
import org.mobicents.servlet.sip.core.session.SipManager;
import org.mobicents.servlet.sip.core.session.SipSessionKey;
import org.mobicents.servlet.sip.message.SipServletRequestImpl;
import org.mobicents.servlet.sip.message.SipFactoryFacade;
import org.mobicents.servlet.sip.message.SipFactoryImpl;
import org.mobicents.servlet.sip.SipFactories;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;
*/
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.Header;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import gov.nist.javax.sip.message.SIPRequest;

import org.apache.log4j.Logger;

public class SipMessageConvertUtil {

	private static Logger logger = Logger.getLogger(SipMessageConvertUtil.class);


	public static XTalkerSipMsg convertFromRequest(Request req) {
		SIPRequest request = (SIPRequest) req;
		try {
			XTalkerSipMsg sipMsg = new XTalkerSipMsg(request.getCallId().getCallId(), request.getFrom().getAddress(),
					request.getTo().getAddress(), request.getContent(), new Timestamp(System.currentTimeMillis()));

			sipMsg.setContentType(request.getContentTypeHeader().getContentType());
			sipMsg.setContent(request.getContent());

			SIPMessage sipMessage = (SIPMessage) request;
			// String messageText = ;
			// logger.info("From Request text:\n"+messageText);
			sipMsg.setRawData(sipMessage.encodeAsBytes("UDP"));

			RouteList routeList = request.getRouteHeaders();
			if (routeList != null) {
				for (int i = 0; i < routeList.size(); i++) {

					sipMsg.addRoute(routeList.get(i).getHeaderValue());
				}
			}

			return sipMsg;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	//ISC marking structure 
	//typedef struct _isc_mark{
	//	int skip;		/**< how many IFCs to skip */
	//	char handling;	/**< handling to apply on failure to contact the AS */
	//	char direction;	/**< session case: orig,term,term unreg */
	//	str aor;		/**< the save user aor - terminating or originating */
	//} isc_mark;
	/**
	 * a) Request URI：接收方SIP URI b) To：接收方tel URI c)
	 * From/P-Asserted-Identity：发送方tel URI d)
	 * 删除包含"+g.ctc.ims-sms"的Accept-Contact（抑制被叫触发） e)
	 * User-Agent：sms-serv（表示请求来自于SMC） f) Content-Type：text/xml-smml
	 * 
	 * @param sipMsg
	 * @param routeStr
	 * @return
	 */
	public static SIPRequest createNewMessageRequest(XTalkerSipMsg sipMsg, String routeStr) {
		try {

			MessageFactory messageFactory = Main.getMessageFactory();// SipFactories.messageFactory;
			String messageText = new String(sipMsg.getRawData());
			SIPRequest request = (SIPRequest) messageFactory.createRequest(messageText);


			ViaHeader viaHeader = request.getTopmostVia();
			if (viaHeader==null || (!viaHeader.getHost().equals("158.85.12.36"))){
				Via via = new Via();
				via.setHost("158.85.12.36");
				via.setPort(5080);
				via.setTransport("UDP");
				request.addHeader(via);
			}
			request.addHeader("" + SIPHeader.IN_REPLY_TO + ":" + sipMsg.getCallId() + "\"");


			
			RouteList routeList = request.getRouteHeaders();

	

			Route iscMark = null;
			for (int i=0;i<routeList.size();i++){
				
				iscMark =routeList.get(i);
				  if (iscMark.getAddress().getURI().toString().startsWith("sip:iscmark")){
					  String s = iscMark.getParameter("s");
					  if (s==null){
						  s="0";
					  }
					  int sd = Integer.parseInt(s);
					  iscMark.setParameter("s",String.valueOf(sd+1));
					  break;
				  } else{
					  routeList.remove(i);
				  }
			  }
			  

			
			Route sCscfRoute = new Route((AddressImpl)createAddress(routeStr));
			routeList.addFirst(sCscfRoute);
	

			
			  Object content = sipMsg.getContent();
			  
			  request.setContent(content, request.getContentTypeHeader());
			  logger.info(request.encode());
			return request;
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage() + ",please wait", e);
			return null;
		} catch (IllegalStateException e1) {
			logger.warn(e1.getMessage() + ",please wait", e1);
			return null;

		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
			return null;
		}
	}

	/**
	 * a) Request URI：发送方SIP URI b) To：发送方tel URI c)
	 * From/P-Asserted-Identity：接收方tel URI d) User-Agent：sms-serv e)
	 * Content-Type：text/xml-smml f) 消息体-id：与原短消息的id一致
	 *
	 * @param sipMsg
	 * @param receipt
	 * @return
	 */
	/*
	 * public static SipServletRequest createNewReceiptRequest(XTalkerSipMsg
	 * sipMsg, String receipt) { try {
	 * logger.info("createNewReceiptRequest:"+receipt); SipApplicationSession
	 * session = factory.createApplicationSession(); SipServletRequest
	 * sipServletReq = factory.createRequest(session, "MESSAGE",
	 * sipMsg.getFrom(), sipMsg.getTo());
	 * sipServletReq.addAddressHeader(SIPHeaderNamesIms.P_ASSERTED_IDENTITY,
	 * sipMsg.getFrom(), true); sipServletReq.addHeader(SIPHeader.USER_AGENT,
	 * BizConsts.USER_AGENT_SMC);
	 * sipServletReq.setHeader(SIPHeader.CONTENT_TYPE, "text/xml-smml");
	 * sipServletReq.setHeader(SIPHeader.CALL_ID, sipMsg.getCallId());
	 * sipServletReq.setContent(sipMsg.getContent(), sipMsg.getContentType());
	 * 
	 * return sipServletReq; } catch (IllegalArgumentException e) {
	 * logger.warn(e.getMessage()+",please wait",e); return null; } catch
	 * (IllegalStateException e) { logger.warn(e.getMessage()+",please wait",e);
	 * return null; } catch (Exception e) { logger.error(e.getMessage(), e);
	 * return null; } }
	 */

	public static Address createAddress(String address) {
		try {
			AddressFactoryImpl addrFact = new AddressFactoryImpl();
			Address addr = addrFact.createAddress(address);
			return addr;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static URI createURI(String uriStr) {
		try {
			AddressFactoryImpl addrFact = new AddressFactoryImpl();
			URI addr = addrFact.createURI(uriStr);
			return addr;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}
