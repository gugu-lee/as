
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
import net.x_talker.as.sh.data.ShDataElement;
import net.x_talker.as.sh.data.ShDataParser;
import net.x_talker.as.sh.data.ShIMSDataElement;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【UDA（读取数据回应）处理实现】
 *
 * @version
 * @author xubo 2014-3-21 下午03:45:03
 * 
 */
public class UDA {

	private static Logger logger = Logger.getLogger(UDA.class);
	private static IMRegisterHandler registerHanler = new IMRegisterHandlerImpl();

	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage request, DiameterMessage response) {
		int resultCode = UtilAVP.getResultCode(response);
		int experimentalResultCode = UtilAVP.getExperimentalResultCode(response);
		String sessionId = UtilAVP.getSessionID(response);
		if (experimentalResultCode == -1 && resultCode == DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode()) {
			String userData = UtilAVP.getShUserData(response);
			if (userData == null) {
				logger.warn("The UDA user_data is null");
				return;
			}
			InputSource input = new InputSource(new ByteArrayInputStream(userData.getBytes()));
			ShDataParser parser = new ShDataParser(input);
			ShDataElement shData = parser.getShData();
			if (request != null) {
				Vector dataRefVector = UtilAVP.getAllDataReference(request);
				String userIdentity = UtilAVP.getShUserIdentity(request);
				for (int i = 0; i < dataRefVector.size(); i++) {
					int crtDataRef = (Integer) dataRefVector.get(i);
					if (crtDataRef == ShConstants.Data_Ref_IMS_User_State) {
						ShIMSDataElement shIMSData = shData.getShIMSData();
						if (shIMSData != null) {
							int userState = shIMSData.getImsUserState();
							// 用户已注册
							if (userState == ShConstants.IMPU_user_state_Registered) {
								logger.info("user registered by UDA,userIdentity:" + userIdentity);
								registerHanler.registerUser(userIdentity);
							} else {
								logger.info("user unregister by UDA,userIdentity:" + userIdentity);
								registerHanler.unRegisterUser(userIdentity);
							}
						}
						// TODO UDA的其他业务处理，以后需要时补充
					}
				}
			}
			logger.info("The UDA resultCode is:" + resultCode);
		} else {
			logger.warn(
					"The UDA resultCode is:" + resultCode + " and experimentalResultCode is:" + experimentalResultCode);
		}

	}

}
