package net.x_talker.as;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import net.x_talker.as.im.container.consumer.SendIMConsumer;
import net.x_talker.as.im.load.MessageLoad;
import net.x_talker.as.im.util.PropertiesUtil;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.sh.container.AsShContainer;

public class IMStartup {
	private SipStack sipStack;

	private SipProvider sipProvider;
	private AddressFactory addressFactory;

	private MessageFactory messageFactory;

	private HeaderFactory headerFactory;


	protected void init() {
		SipFactory sipFactory = null;
		sipStack = null;
		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "net.x_talker.as");
		// You need 16 for logging traces. 32 for debug + traces.
		// Your code will limp at 32 but it is best for debugging.
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "shootmedebug.txt");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "shootmelog.txt");
		properties.setProperty("gov.nist.javax.sip.AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
		properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");
		//properties.setProperty("gov.nist.javax.sip.SIP_MESSAGE_VALVE", Main.class.getCanonicalName());

		try {
			// Create SipStack object
			sipStack = sipFactory.createSipStack(properties);
			messageFactory = sipFactory.createMessageFactory();
			System.out.println("sipStack = " + sipStack);
		} catch (PeerUnavailableException e) {
			// could not find
			// gov.nist.jain.protocol.ip.sip.SipStackImpl
			// in the classpath
			e.printStackTrace();
			System.err.println(e.getMessage());
			if (e.getCause() != null)
				e.getCause().printStackTrace();
			System.exit(0);
		}

		try {
			PropertiesUtil util = PropertiesUtil.getInstance();
			
			ListeningPoint lp = sipStack.createListeningPoint(
					util.getPropVal("sipStack.listeningPoint.ipAddress"),
					util.getPropIntVal("sipStack.listeningPoint.port"), 
					util.getPropVal("sipStack.listeningPoint.transport"));

			AsSipListener listener = new AsSipListener();
			listener.init();
			sipProvider = sipStack.createSipProvider(lp);
			System.out.println("udp provider " + sipProvider);
			sipProvider.addSipListener(listener);

			ExecutorService pool = Executors.newCachedThreadPool();
			
			for(int i = 0; i < 5; i++) {
				System.out.println("start send IM consumer.....");
				SendIMConsumer consumer = new SendIMConsumer();
				pool.execute(consumer);
			}
			
			
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected MessageFactory getMessageFactory() {
		return messageFactory;
	}

	protected SipProvider getSipProvider() {
		return sipProvider;
	}
}
