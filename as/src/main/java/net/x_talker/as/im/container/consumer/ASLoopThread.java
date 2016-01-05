package net.x_talker.as.im.container.consumer;

import org.apache.log4j.Logger;

import net.x_talker.as.im.hook.IMUncaughtExceptionHandler;
import net.x_talker.as.im.hook.ShutdownService;
import net.x_talker.as.im.hook.ThreadShutdownHook;

/**
 * 死循环线程抽象父类 所有死循环业务线程都应该继承此类,以方便应用程序统一管理线程及处理线程异常事件
 * 该类线程运行体中已增加死循环逻辑,子类只需求实现业务逻辑 该类仅用于死循环执行线程继承
 * 一次性执行线程不能继承该类,如需继承该类在业务逻辑执行完毕后,显式调用stopThread方法以停止线程
 */
public abstract class ASLoopThread implements Runnable {
	private Logger logger = Logger.getLogger(ASLoopThread.class);

	private ShutdownService shutdownService;
	private ThreadShutdownHook hook;
	private String name;
	public final int DEFAULT_SLEEP_TIME = 100;
	protected int sleeptime = DEFAULT_SLEEP_TIME;

	/** 当前是否启运用，是指当前状态 */
	private volatile boolean started = false;

	/** 准备停止 ，是指将要进行的动作 */
	private volatile boolean shutdownFlag = false;

	public ASLoopThread(String threadName) {
		shutdownService = ShutdownService.getInstance();
		this.name = threadName;
		hook = shutdownService.createHook(this);
		// startThread(threadName);
	}

	/**
	 * 增加死循环实现
	 */
	@Override
	public void run() {
		started = true;
		Thread.setDefaultUncaughtExceptionHandler(new IMUncaughtExceptionHandler(this));
		try {
			while (hook.keepRunning()) {
				if (shutdownFlag) {
					shutdownFlag = false;
					break;
				}
				doService();
				sleep();
			}
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info(name + " is stop!");
			}
		}

		started = false;
	}

	private void sleep() {
		sleep(sleeptime);
	}

	protected void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 主动停止线程方法
	 */
	protected void stopThread() {
		hook.onHook();
	}

	/*
	 * public void setStop() { started = false; }
	 */
	public boolean isStart() {
		return started;
	}

	public String getName() {
		return name;
	}

	/**
	 * 线程业务逻辑实现抽象方法 实现线程执行具体业务逻辑,不包括死循环部分
	 * 
	 */
	public abstract void doService();

	/**
	 * 线程停止时,需要触发的逻辑 该方法在线程定义逻辑执行完毕后调用 该方法定义为保证线程停止时保证关联数据不丢失
	 * 
	 */
	public final void shutdown() {
		shutdownFlag = true;
	}

	/**
	 * 线程发生异常时需要进行修复处理
	 * 
	 */
	public abstract void exceptionHandle();

}
