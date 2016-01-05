package net.x_talker.as.im.hook;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 未捕捉异常处理类
 *
 */
public class IMUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private Runnable rannable;

	private Logger logger = Logger.getLogger(getClass());

	public IMUncaughtExceptionHandler(Runnable rannable) {
		this.rannable = rannable;
	}

	public void uncaughtException(Thread t, Throwable e) {
		logger.info("uncaught exception process, thread:" + rannable.getClass());
		new Thread(rannable).start();
	}

}
