
package net.x_talker.as.init;

import org.apache.log4j.Logger;

import net.x_talker.as.im.load.MessageLoad;
import net.x_talker.as.im.util.SpringBeanUtil;
import net.x_talker.as.sh.container.AsShContainer;


public class AsInitializer {

	private final Logger logger = Logger.getLogger(AsInitializer.class);

	private MessageLoad load;

	public void destroyed() {
		logger.info("AS Sh destroyed!");

	}


	public void init() {
		load = (MessageLoad) SpringBeanUtil.getBeanByName("messageLoadImpl");

		logger.info("AS Sh contextInitialized.....");
		AsShContainer.getInstance();

		logger.info("load message begin");
		load.loadCacheMessage();
		load.loadRegularlyMessage();
		load.loadSendMessage();
		load.loadSendedMessage();
		logger.info("load message end");

		logger.info("AS Sh initialize success!");

	}
	public MessageLoad getMessageLoad()
	{
		return load;
	}
}
