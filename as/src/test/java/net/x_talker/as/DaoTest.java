package net.x_talker.as;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.x_talker.as.persist.IMessageDao;
import net.x_talker.as.persist.entity.MessageLog;
import net.x_talker.as.persist.entity.MessageState;

public class DaoTest  extends TestCase
{

	private static  ApplicationContext applicationContext = null;
	@Autowired
	private IMessageDao messageDao;
	public DaoTest(String testName)
	{
		super(testName);
	}
	public static Test suite() {
		return new TestSuite(DaoTest.class);
	}
	public void setUp() throws Exception 
	{
		String configLocations[]={"applicationContext.xml","applicationContext-mybatis.xml"};
		applicationContext = new ClassPathXmlApplicationContext(configLocations);
		messageDao = (IMessageDao)applicationContext.getBean("messageDao");
	}

	public void testDao()
	{
		System.out.println("aaa");
		MessageState state = new MessageState();
		state.setMessageId("123");
		state.setState(1);
		
		MessageLog log2 = messageDao.getMessageLogByCallId("19d90154-f0f0-f56e-ddd9-424542261701");
		
		if (log2==null){
		MessageLog log = new MessageLog();
		
		log.setCallId("19d90154-f0f0-f56e-ddd9-424542261701");
		log.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
		log.setFrom("bob@x_talker.net");
		log.setTo("alice@x_talker.net");
		log.setIsRegularly(1);
		log.setMessageContent("aa");
		log.setMessageId("19d90154-f0f0-f56e-ddd9-424542261701");
		messageDao.persistIM(log);
		}
	
		
		//messageDao.insertMState(state);
	}

}
