package net.x_talker.as;

//import java.util.Properties;
//
//import javax.sip.ListeningPoint;
//import javax.sip.PeerUnavailableException;
//import javax.sip.SipFactory;
//import javax.sip.SipProvider;
//import javax.sip.SipStack;
//import javax.sip.address.Address;
//import javax.sip.address.AddressFactory;
//import javax.sip.header.ViaHeader;
//import javax.sip.message.MessageFactory;
//import javax.sip.message.Request;
//
//import gov.nist.javax.sip.address.AddressFactoryImpl;
//import gov.nist.javax.sip.address.AddressImpl;
//import gov.nist.javax.sip.header.Route;
//import gov.nist.javax.sip.header.RouteList;
//import gov.nist.javax.sip.header.SIPHeader;
//import gov.nist.javax.sip.header.Via;
//import gov.nist.javax.sip.message.SIPRequest;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * Unit test for simple App.
// */
//public class AppTest extends TestCase {
//
//	private SipStack sipStack;
//
//	private SipProvider sipProvider;
//	private AddressFactory addressFactory;
//
//	private MessageFactory messageFactory;
//
//	/**
//	 * Create the test case
//	 *
//	 * @param testName
//	 *            name of the test case
//	 */
//	public AppTest(String testName) {
//		super(testName);
//	}
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	public static Test suite() {
//		return new TestSuite(AppTest.class);
//	}
//
//	/**
//	 * Rigourous Test :-)
//	 */
//	public void testApp() {
//		SipFactory sipFactory = null;
//		sipStack = null;
//		sipFactory = SipFactory.getInstance();
//		sipFactory.setPathName("gov.nist");
//		Properties properties = new Properties();
//		properties.setProperty("javax.sip.STACK_NAME", "net.x_talker.as");
//		// You need 16 for logging traces. 32 for debug + traces.
//		// Your code will limp at 32 but it is best for debugging.
//		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
//		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "shootmedebug.txt");
//		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "shootmelog.txt");
//		properties.setProperty("gov.nist.javax.sip.AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
//		properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");
//		//properties.setProperty("gov.nist.javax.sip.SIP_MESSAGE_VALVE", Main.class.getCanonicalName());
//
//		try {
//			// Create SipStack object
//			sipStack = sipFactory.createSipStack(properties);
//			messageFactory = sipFactory.createMessageFactory();
//			System.out.println("sipStack = " + sipStack);
//
//			String requestText = "MESSAGE sip:alice@saygreet.com SIP/2.0\r\n"
//					+ "Route: <sip:158.85.12.36:5080;lr>, <sip:iscmark@scscf.saygreet.com:6060;lr;s=1;h=0;d=0;a=7369703a626f624073617967726565742e636f6d>\r\n"
//					+ "Via: SIP/2.0/UDP 120.24.95.155:6060;branch=z9hG4bK3333.6a103102.0\r\n"
//					+ "Via: SIP/2.0/UDP 120.24.95.155:4060;branch=z9hG4bK3333.744b0d32.0\r\n"
//					+ "Via: SIP/2.0/UDP 0.0.0.0:55025;received=14.19.159.83;received=14.19.159.83;branch=z9hG4bK-1304623124;rport=10261\r\n"
//					+ "From: <sip:bob@saygreet.com>;tag=155500979\r\n" + "To: <sip:alice@saygreet.com>\r\n"
//					+ "Call-ID: ccbe9be9-b650-5229-7432-5aeaf7ee5448\r\n" + "CSeq: 149310522 MESSAGE\r\n"
//					+ "Content-Length: 65\r\n" + "Max-Forwards: 15\r\n" + "Accept-Contact: *;+g.oma.sip-im\r\n"
//					+ "Accept-Contact: *;language=\"en,fr\"\r\n" + "Content-Type: text/plain\r\n"
//					+ "Allow: INVITE, ACK, CANCEL, BYE, MESSAGE, OPTIONS, NOTIFY, PRACK, UPDATE, REFER\r\n"
//					+ "Privacy: none\r\n" + "P-Access-Network-Info: ADSL;utran-cell-id-3gpp=00000000\r\n"
//					+ "User-Agent: IM-client/OMA1.0 ios-ngn-stack/v00 (doubango r000)\r\n"
//					+ "P-Asserted-Identity: <sip:bob@saygreet.com>\r\n"
//					+ "P-Charging-Vector: icid-value=\"P-CSCFabcd00000000567baf60000000ea\";icid-generated-at=120.24.95.155;orig-ioi=\"saygreet.com\"\r\n"
//					+ "\r\n" + "{  \"version\" : \"1.0\",  \"type\" : \"message\",  \"content\" : \"The \"}\r\n";
//
//			SIPRequest request = (SIPRequest)messageFactory.createRequest(requestText);
//			ViaHeader viaHeader = request.getTopmostVia();
//			if (viaHeader==null || (!viaHeader.getHost().equals("158.85.12.36"))){
//				Via via = new Via();
//				via.setHost("158.85.12.36");
//				via.setPort(5080);
//				via.setTransport("UDP");
//				request.addHeader(via);
//			}
//			request.addHeader("" + SIPHeader.IN_REPLY_TO + ":" + request.getCallId().getCallId() + "\"");
//
//
//			
//			RouteList routeList = request.getRouteHeaders();
//
//	
//
//			Route iscMark = null;
//			for (int i=0;i<routeList.size();i++){
//				
//				iscMark =routeList.get(i);
//				  if (iscMark.getAddress().getURI().toString().startsWith("sip:iscmark")){
//					  String s = iscMark.getParameter("s");
//					  if (s==null){
//						  s="0";
//					  }
//					  int sd = Integer.parseInt(s);
//					  iscMark.setParameter("s",String.valueOf(sd+1));
//					  break;
//				  } else{
//					  routeList.remove(i);
//				  }
//			  }
//			  
//
//			
//			Route sCscfRoute = new Route((AddressImpl)createAddress("sip:127.0.0.1:6060"));
//			routeList.addFirst(sCscfRoute);
//			request.setContent("test", request.getContentTypeHeader());
//			
//			System.out.println(new String(request.encodeAsBytes("UDP")));
//			ListeningPoint lp = sipStack.createListeningPoint(
//					"192.168.1.101",
//					5080, 
//					"udp");
//
//			sipProvider = sipStack.createSipProvider(lp);
//			sipProvider.getNewClientTransaction(request);
//			System.out.println("---------------");
//			System.out.println(new String(request.encodeAsBytes("UDP")));
//		} catch (Exception e) {
//			// could not find
//			// gov.nist.jain.protocol.ip.sip.SipStackImpl
//			// in the classpath
//			e.printStackTrace();
//			System.err.println(e.getMessage());
//			if (e.getCause() != null)
//				e.getCause().printStackTrace();
//			System.exit(0);
//		}
//	}
//	public Address createAddress(String address) {
//		try {
//			AddressFactoryImpl addrFact = new AddressFactoryImpl();
//			Address addr = addrFact.createAddress(address);
//			return addr;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
