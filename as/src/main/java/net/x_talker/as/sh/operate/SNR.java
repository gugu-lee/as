
package net.x_talker.as.sh.operate;

import java.util.Vector;

import org.apache.log4j.Logger;

import org.freeims.diameterpeer.DiameterPeer;
import org.freeims.diameterpeer.data.DiameterMessage;
import net.x_talker.as.sh.ShConstants;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.DiameterStack;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【SNR（订阅请求）处理实现】
 *
 * @version
 * 
 */
public class SNR {

	private static Logger logger = Logger.getLogger(SNR.class);

	public static void sendRequest(DiameterStack diameterStack, String userIdentity, Vector<Integer> dataRefVector,
			int subsReqType) {
		DiameterPeer diameterPeer = diameterStack.diameterPeer;
		DiameterMessage request = diameterPeer.newRequest(DiameterConstants.Command.SNR_OR_SNA,
				DiameterConstants.Application.Sh);
		request.flagProxiable = true;

		// add SessionId
		UtilAVP.addSessionID(request, diameterPeer.FQDN, diameterStack.getNextSessionID());

		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(request, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(request, DiameterConstants.Vendor.V3GPP,
				DiameterConstants.Application.Sh);

		// add the User-Identity
		UtilAVP.addShUserIdentity(request, userIdentity);

		// add Origin Host
		UtilAVP.addOriginHost(request, diameterPeer.FQDN);
		// add Origin Realm
		UtilAVP.addOriginRealm(request, diameterPeer.Realm);
		// add Requested domain
		UtilAVP.addDestinationRealm(request, diameterPeer.Realm);
		// add Current Location
		UtilAVP.addCurrentLocation(request, ShConstants.Do_Not_Need_Initiate_Active_Location_Retrieval);
		// add Service Indication
		UtilAVP.addServiceIndication(request, diameterPeer.FQDN);
		// add Application Server Identity
		UtilAVP.addApplicationServerIdentity(request, diameterPeer.FQDN);
		// add Application Server Name
		UtilAVP.addServerName(request, diameterPeer.FQDN);
		// add Subscription request type
		UtilAVP.addSubsReqType(request, subsReqType);
		// add Requested data
		for (int i = 0; i < dataRefVector.size(); i++) {
			int crtDataRef = dataRefVector.get(i);
			UtilAVP.addDataReference(request, crtDataRef);
		}

		if (diameterPeer.sendRequestTransactional(diameterPeer.peerManager.peers.get(0).FQDN, request, diameterStack)) {
			String sessionId = UtilAVP.getSessionID(request);
			logger.info("SNR send succcess,userIdentity is " + userIdentity);
		} else {
			logger.warn("SNR send fail,userIdentity is " + userIdentity);
		}

	}

	public static void processTimeout(DiameterMessage request) {
		String sessionId = UtilAVP.getSessionID(request);
		logger.info("processTimeout");
	}

}
