package net.x_talker.as.sh.diameter;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;

import org.freeims.diameterpeer.DiameterPeer;
import org.freeims.diameterpeer.EventListener;
import org.freeims.diameterpeer.data.DiameterMessage;
import org.freeims.diameterpeer.transaction.TransactionListener;
import net.x_talker.as.sh.container.AsShContainer;
import net.x_talker.as.sh.task.Task;

public class DiameterStack implements EventListener, TransactionListener {
	private static Logger logger = Logger.getLogger(DiameterStack.class);

	public DiameterPeer diameterPeer = null;
	public AsShContainer asShContainer = null;

	public DiameterStack(AsShContainer asShContainer) {
		this.asShContainer = asShContainer;

		diameterPeer = new DiameterPeer();
		String filePath = Loader.getResource("DiameterPeerAS.xml").getPath();
		diameterPeer.configure(filePath, true);
		diameterPeer.enableTransactions(10, 1);
		diameterPeer.addEventListener(this);

	}

	private int sessionID = 0;

	/**
	 * Gets the next value for the Session ID Counter. It is used in the second
	 * part of the Session-Id field together with the time stamp
	 */
	public int getNextSessionID() {
		synchronized (this) {
			return sessionID++;
		}
	}

	public void recvMessage(String FQDN, DiameterMessage request) {
		if (!request.flagRequest)
			return;
		logger.info("recive a request message,FQDN:" + FQDN + ",commandCode:" + request.commandCode);
		Task task = new Task(asShContainer.diamStack, 2, FQDN, request.commandCode, request.applicationID, request);
		try {
			asShContainer.tasksQueue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void receiveAnswer(String FQDN, DiameterMessage request, DiameterMessage response) {
		if (response.flagRequest)
			return;
		logger.info("recive a answer,FQDN:" + FQDN + ",commandCode:" + request.commandCode);
		Task task = new Task(asShContainer.diamStack, 3, FQDN, request.commandCode, request.applicationID, request,
				response);
		try {
			asShContainer.tasksQueue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void timeout(DiameterMessage request) {
		logger.info("recive a timeout message,commandCode:" + request.commandCode);
		Task task = new Task(asShContainer.diamStack, 4, request.commandCode, request.applicationID);
		task.requestMessage = request;
		try {
			asShContainer.tasksQueue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
