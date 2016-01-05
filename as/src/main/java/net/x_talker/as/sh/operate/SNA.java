
package net.x_talker.as.sh.operate;

import org.apache.log4j.Logger;

import net.x_talker.DiameterPeer.DiameterPeer;
import net.x_talker.DiameterPeer.data.DiameterMessage;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【SNA（订阅回应）处理实现】
 *
 * @version
 * @author xubo 2014-3-25 上午10:50:12
 * 
 */
public class SNA {

	private static Logger logger = Logger.getLogger(SNA.class);

	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage request, DiameterMessage response) {
		int resultCode = UtilAVP.getResultCode(response);
		int experimentalResultCode = UtilAVP.getExperimentalResultCode(response);
		String sessionId = UtilAVP.getSessionID(response);
		if (experimentalResultCode == -1 && resultCode == DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode()) {
			// TODO 根据以后业务再做处理
			logger.info("The SNA resultCode is:" + resultCode);
		} else {
			logger.warn(
					"The SNA resultCode is:" + resultCode + " and experimentalResultCode is:" + experimentalResultCode);
		}

	}

}
