package net.x_talker.as.im.hook;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.consumer.ASLoopThread;

public class ShutdownService {

	private static final Logger logger = Logger.getLogger(ShutdownService.class);

	private static List<Hook> hooks;

	private static ShutdownService instance = new ShutdownService();;

	private ShutdownService() {
		logger.debug("Creating shutdown service");
		hooks = new ArrayList<Hook>();
		createShutdownHook();
	}

	public static ShutdownService getInstance() {
		return instance;
	}

	/**
	 * Protected for testing
	 */
	// @VisibleForTesting
	protected void createShutdownHook() {
		ShutdownDaemonHook shutdownHook = new ShutdownDaemonHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	protected class ShutdownDaemonHook extends Thread {

		/**
		 * 循环并使用hook关闭所有后台线程
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				logger.info("Running shutdown sync");

				for (Hook hook : hooks) {
					hook.onHook();
				}
			} catch (Exception e) {
				logger.error("hook shut down error:" + e.getMessage(), e);
			}

		}
	}

	/**
	 * 创建hook class的新实例
	 */
	public ThreadShutdownHook createHook(ASLoopThread thread) {

		// thread.setDaemon(true);
		ThreadShutdownHook retVal = new ThreadShutdownHook(thread);
		hooks.add(retVal);
		return retVal;
	}

	// @VisibleForTesting
	List<Hook> getHooks() {
		return hooks;
	}
}