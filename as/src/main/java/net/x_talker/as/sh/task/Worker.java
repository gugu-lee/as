package net.x_talker.as.sh.task;

import org.apache.log4j.Logger;

import net.x_talker.as.sh.container.AsShContainer;

public class Worker extends Thread {
	private static Logger logger = Logger.getLogger(Worker.class);

	private AsShContainer asShContainer;

	public Worker(AsShContainer asShContainer) {
		this.asShContainer = asShContainer;
	}

	public void run() {
		Task task;
		while (true) {
			try {
				task = (Task) asShContainer.tasksQueue.take();
				task.execute();
			} catch (InterruptedException e) {
				logger.error("Interrupted Exception ocurred in Worker!");
				e.printStackTrace();
			}
		}
	}
}
