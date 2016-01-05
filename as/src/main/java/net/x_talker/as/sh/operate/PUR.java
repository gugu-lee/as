
package net.x_talker.as.sh.operate;

import org.apache.log4j.Logger;

import net.x_talker.DiameterPeer.DiameterPeer;
import net.x_talker.DiameterPeer.data.DiameterMessage;
import net.x_talker.as.sh.ShConstants;
import net.x_talker.as.sh.data.RepositoryDataElement;
import net.x_talker.as.sh.data.ShDataElement;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.DiameterStack;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【PUR（更新数据请求）处理实现】
 *
 * @version
 * @author xubo 2014-3-24 下午05:13:18
 * 
 */
public class PUR {

	private static Logger logger = Logger.getLogger(PUR.class);

	public static void sendRequest(DiameterStack diameterStack, String userIdentity, int dataRef,
			RepositoryDataElement repositoryData) {
		DiameterPeer diameterPeer = diameterStack.diameterPeer;
		DiameterMessage request = diameterPeer.newRequest(DiameterConstants.Command.PUR_OR_PUA,
				DiameterConstants.Application.Sh);
		request.flagProxiable = true;

		// add SessionId
		UtilAVP.addSessionID(request, diameterPeer.FQDN, diameterStack.getNextSessionID());

		// add Auth-Session-State and Vendor-Specific-Application-ID
		UtilAVP.addAuthSessionState(request, DiameterConstants.AVPValue.ASS_No_State_Maintained);
		UtilAVP.addVendorSpecificApplicationID(request, DiameterConstants.Vendor.V3GPP,
				DiameterConstants.Application.Sh);

		// add Origin Host
		UtilAVP.addOriginHost(request, diameterPeer.FQDN);
		// add Origin Realm
		UtilAVP.addOriginRealm(request, diameterPeer.Realm);
		// add Requested domain
		UtilAVP.addDestinationRealm(request, diameterPeer.Realm);
		// add the User-Identity
		UtilAVP.addShUserIdentity(request, userIdentity);
		// add Application Server Identity
		UtilAVP.addApplicationServerIdentity(request, diameterPeer.FQDN);
		// add Requested data
		UtilAVP.addDataReference(request, dataRef);
		// add RepositoryData
		ShDataElement shData = new ShDataElement();
		if (dataRef == ShConstants.Data_Ref_Repository_Data && repositoryData != null) {
			shData.addRepositoryData(repositoryData);
			UtilAVP.addShData(request, shData.toString());
		}

		if (diameterPeer.sendRequestTransactional(diameterPeer.peerManager.peers.get(0).FQDN, request, diameterStack)) {
			String sessionId = UtilAVP.getSessionID(request);
			logger.info("PUR send succcess,userIdentity is " + userIdentity);
		} else {
			logger.warn("PUR send fail,userIdentity is " + userIdentity);
		}

	}

	public static void processTimeout(DiameterMessage request) {
		logger.info("processTimeout");
		// TODO
	}

}
