
package net.x_talker.as.sh.operate;

import java.io.ByteArrayInputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import org.freeims.diameterpeer.DiameterPeer;
import org.freeims.diameterpeer.data.DiameterMessage;
import net.x_talker.as.im.handler.IMRegisterHandler;
import net.x_talker.as.im.handler.IMRegisterHandlerImpl;
import net.x_talker.as.sh.ShConstants;
import net.x_talker.as.sh.ShExperimentalResultException;
import net.x_talker.as.sh.ShFinalResultException;
import net.x_talker.as.sh.data.ShDataElement;
import net.x_talker.as.sh.data.ShDataParser;
import net.x_talker.as.sh.data.ShIMSDataElement;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【PNR（通知请求）处理】
 *
 * @version
 * @author xubo 2014-3-25 下午03:07:22
 * 
 */
public class PNR {

	private static Logger logger = Logger.getLogger(PNR.class);

	private static IMRegisterHandler registerHanler = new IMRegisterHandlerImpl();

	public static DiameterMessage processRequest(DiameterPeer diameterPeer, DiameterMessage request) {
		DiameterMessage response = diameterPeer.newResponse(request);
		try {
			if (request.flagProxiable == false) {
				logger.warn("You should notice that the Proxiable flag for UDR request was not set!");
			}
			// set the proxiable flag for the response
			response.flagProxiable = true;

			// add Auth-Session-State and Vendor-Specific-Application-ID to
			// Response
			UtilAVP.addAuthSessionState(response, DiameterConstants.AVPValue.ASS_No_State_Maintained);
			UtilAVP.addVendorSpecificApplicationID(response, DiameterConstants.Vendor.V3GPP,
					DiameterConstants.Application.Sh);

			// -0- check for mandatory fields in the message
			String vendorSpecificId = UtilAVP.getVendorSpecificApplicationID(request);
			String authSessionState = UtilAVP.getAuthSessionState(request);

			String userIdentity = UtilAVP.getShUserIdentity(request);

			if (vendorSpecificId == null || authSessionState == null || userIdentity == null) {
				throw new ShExperimentalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			}
			String userData = UtilAVP.getShUserData(request);
			if (userData == null) {
				logger.warn("The PNR user_data is null");
				throw new ShFinalResultException(DiameterConstants.ResultCode.DIAMETER_MISSING_AVP);
			} else {
				InputSource input = new InputSource(new ByteArrayInputStream(userData.getBytes()));
				ShDataParser parser = new ShDataParser(input);
				ShDataElement shData = parser.getShData();
				if (shData != null) {
					ShIMSDataElement shIMSData = shData.getShIMSData();
					if (shIMSData != null) {
						int userState = shIMSData.getImsUserState();
						// 用户已注册
						if (userState == ShConstants.IMPU_user_state_Registered) {
							logger.info("user registered by PNR,userIdentity:" + userIdentity);
							registerHanler.registerUser(userIdentity);
						} else {
							logger.info("user unregister by PNR,userIdentity:" + userIdentity);
							registerHanler.unRegisterUser(userIdentity);
						}
					}
					// TODO UDA的其他业务处理，以后需要时补充
				}
			}

			UtilAVP.addResultCode(response, DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode());
		} catch (ShExperimentalResultException e) {
			UtilAVP.addExperimentalResultCode(response, e.getErrorCode(), e.getVendor());
			logger.error(e.getMessage(), e);
		} catch (ShFinalResultException e) {
			UtilAVP.addResultCode(response, e.getErrorCode());
			logger.error(e.getMessage(), e);
		}
		return response;
	}

}
