package net.x_talker.as.sh.container;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import net.x_talker.as.sh.diameter.DiameterStack;
import net.x_talker.as.sh.task.Task;
import net.x_talker.as.sh.task.Worker;

public class AsShContainer {
	private Logger logger = Logger.getLogger(AsShContainer.class);

	public LinkedBlockingQueue<Task> tasksQueue;
	public Worker[] workers;

	// diameter stack
	public DiameterStack diamStack;

	private static AsShContainer instance = new AsShContainer();

	private AsShContainer() {
		logger.info("AsShContainer start begin...");
		// initialise the tasksQueue and start the workers
		tasksQueue = new LinkedBlockingQueue<Task>();
		workers = new Worker[10];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker(this);
			workers[i].start();
		}
		// Initialise the diameter stack
		diamStack = new DiameterStack(this);

		logger.info("AsShContainer start end");

	}

	public static AsShContainer getInstance() {
		return instance;
	}

}
