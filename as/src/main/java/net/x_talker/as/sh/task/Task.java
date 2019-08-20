package net.x_talker.as.sh.task;

import java.util.Vector;

import org.apache.log4j.Logger;

import org.freeims.diameterpeer.DiameterPeer;
import org.freeims.diameterpeer.data.DiameterMessage;
import net.x_talker.as.sh.data.RepositoryDataElement;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.DiameterStack;
import net.x_talker.as.sh.operate.PNR;
import net.x_talker.as.sh.operate.PUA;
import net.x_talker.as.sh.operate.PUR;
import net.x_talker.as.sh.operate.SNA;
import net.x_talker.as.sh.operate.SNR;
import net.x_talker.as.sh.operate.UDA;
import net.x_talker.as.sh.operate.UDR;

public class Task {
	private static Logger logger = Logger.getLogger(Task.class);
	public final static int SENDING_REQUEST = 1;
	public final static int PROCESSING_REQUEST = 2;
	public final static int PROCESSING_RESPONSE = 3;
	public final static int TIMEOUT = 4;
	// generic variables
	public DiameterStack diameterStack;
	// "event_type" - can be 1 - Sending Request, 2 - Processing Request, 3 -
	// Processing Response, 4 - Timeout
	public int eventType;
	public int commandCode;
	public String FQDN;
	public DiameterMessage requestMessage;
	public DiameterMessage responseMessage;
	// "interface_type" can be Cx, Sh, Zh etc
	public int interfaceType;

	// specific variables
	public String userIdentity = null;
	// UDR specific variables
	public Vector<Integer> dataRefVector = null;
	public int identitySet = -1;
	// PUR specific variables
	public int dataRef = -1;
	public RepositoryDataElement repositoryData = null;
	// SNR specific variables
	public int subsReqType = -1;

	public Task(DiameterStack diameterStack, int eventType, String FQDN, int commandCode, int interfaceType,
			DiameterMessage requestMessage) {

		this.diameterStack = diameterStack;
		this.eventType = eventType;
		this.FQDN = FQDN;
		this.commandCode = commandCode;
		this.interfaceType = interfaceType;
		this.requestMessage = requestMessage;
	}

	public Task(DiameterStack diameterStack, int eventType, String FQDN, int commandCode, int interfaceType,
			DiameterMessage requestMessage, DiameterMessage responseMessage) {

		this.diameterStack = diameterStack;
		this.eventType = eventType;
		this.FQDN = FQDN;
		this.commandCode = commandCode;
		this.interfaceType = interfaceType;
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
	}

	public Task(DiameterStack diameterStack, int eventType, int commandCode, int interfaceType) {
		this.diameterStack = diameterStack;
		this.eventType = eventType;
		this.commandCode = commandCode;
		this.interfaceType = interfaceType;
	}

	public DiameterMessage execute() {
		DiameterMessage response = null;
		DiameterPeer peer = diameterStack.diameterPeer;
		if (interfaceType == DiameterConstants.Application.Sh) {
			// Sh Commands
			switch (commandCode) {

			case DiameterConstants.Command.UDR_OR_UDA:
				if (eventType == SENDING_REQUEST) {
					logger.debug("Processing Sh-UDR Request send!");
					UDR.sendRequest(diameterStack, userIdentity, dataRefVector, identitySet);
				} else if (eventType == PROCESSING_RESPONSE) {
					logger.debug("Processing Sh-UDA!");
					UDA.processResponse(peer, requestMessage, responseMessage);
				} else if (eventType == TIMEOUT) {
					logger.debug("Processing Sh-UDR timeout!");
					UDR.processTimeout(requestMessage);
				}
				break;

			case DiameterConstants.Command.PUR_OR_PUA:
				if (eventType == SENDING_REQUEST) {
					logger.debug("Processing Sh-PUR Request send!");
					PUR.sendRequest(diameterStack, userIdentity, dataRef, repositoryData);
				} else if (eventType == PROCESSING_RESPONSE) {
					logger.debug("Processing Sh-PUA!");
					PUA.processResponse(peer, requestMessage, responseMessage);
				} else if (eventType == TIMEOUT) {
					logger.debug("Processing Sh-PUR timeout!");
					PUR.processTimeout(requestMessage);
				}
				break;

			case DiameterConstants.Command.SNR_OR_SNA:
				if (eventType == SENDING_REQUEST) {
					logger.debug("Processing Sh-SNR Request send!");
					SNR.sendRequest(diameterStack, userIdentity, dataRefVector, subsReqType);
				} else if (eventType == PROCESSING_RESPONSE) {
					logger.debug("Processing Sh-SNA!");
					SNA.processResponse(peer, requestMessage, responseMessage);
				} else if (eventType == TIMEOUT) {
					logger.debug("Processing Sh-SNR timeout!");
					SNR.processTimeout(requestMessage);
				}
				break;

			case DiameterConstants.Command.PNR_OR_PNA:
				if (eventType == PROCESSING_REQUEST) {
					logger.debug("Processing Sh-PNR request!");
					response = PNR.processRequest(peer, requestMessage);
					peer.sendMessage(FQDN, response);
				}
				break;
			}
		}
		return response;
	}
}
