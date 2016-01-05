package net.x_talker.as.im.hook;

import org.apache.log4j.Logger;

import net.x_talker.as.im.container.consumer.ASLoopThread;

/**
 * 线程退出Hook类
 *
 */
public class ThreadShutdownHook implements Hook {

	private static final Logger logger = Logger.getLogger(ThreadShutdownHook.class);

	// 线程运行标识
	private boolean keepRunning = true;

	private final ASLoopThread thread;

	public final int THREAD_WAIT_TIMES = 10;

	ThreadShutdownHook(ASLoopThread thread) {
		this.thread = thread;
	}

	/**
	 * @return True 如果后台线程继续运行
	 */
	public boolean keepRunning() {
		return keepRunning;
	}

	/**
	 * 告诉客户端后台线程关闭并等待友好地关闭
	 */
	public void onHook() {
		// 设置线程运行标识为false
		keepRunning = false;
		int index = 0;
		// 判断线程逻辑是否执行完毕,未执行完毕进行等待
		while (thread.isStart() && index++ < THREAD_WAIT_TIMES) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			logger.info("wait thread[" + thread.getName() + "] times[" + index + "]");
		}
		// 调用线程实现的shutdown方法,处理关闭事件
		thread.shutdown();
	}
}
