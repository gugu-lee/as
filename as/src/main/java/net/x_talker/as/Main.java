package net.x_talker.as;

import java.util.Properties;

import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.message.MessageFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.x_talker.as.im.load.MessageLoad;
import net.x_talker.as.init.AsInitializer;

/**
 * Hello world!
 *
 */
public class Main {
	private static IMStartup imStartup = null;
	private static AsInitializer asInit = null;
	private static  ApplicationContext applicationContext = null;
	public static void main(String[] args) {

		String configLocations[]={"applicationContext.xml","applicationContext-mybatis.xml"};
		 applicationContext = new ClassPathXmlApplicationContext(configLocations);

		asInit = new AsInitializer();
		asInit.init();

		imStartup = new IMStartup();
		imStartup.init();
	}

	public static MessageFactory getMessageFactory() {
		return imStartup.getMessageFactory();
	}

	public static SipProvider getSipProvider() {
		return imStartup.getSipProvider();
	}
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
	public static MessageLoad getMessageLoad()
	{
		return asInit.getMessageLoad();
	}
}
