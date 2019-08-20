
package net.x_talker.as.sh.operate;

import org.apache.log4j.Logger;

import org.freeims.diameterpeer.DiameterPeer;
import org.freeims.diameterpeer.data.DiameterMessage;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.UtilAVP;

/**
 * 【PUA（更新数据回应）处理实现】
 *
 * @version
 * @author xubo 2014-3-24 下午05:14:25
 * 
 */
public class PUA {

	private static Logger logger = Logger.getLogger(PUA.class);

	public static void processResponse(DiameterPeer diameterPeer, DiameterMessage request, DiameterMessage response) {
		int resultCode = UtilAVP.getResultCode(response);
		int experimentalResultCode = UtilAVP.getExperimentalResultCode(response);
		String sessionId = UtilAVP.getSessionID(response);
		if (experimentalResultCode == -1 && resultCode == DiameterConstants.ResultCode.DIAMETER_SUCCESS.getCode()) {
			// TODO 根据以后业务再做处理
			logger.info("The PUA resultCode is:" + resultCode);
		} else {
			logger.warn(
					"The PUA resultCode is:" + resultCode + " and experimentalResultCode is:" + experimentalResultCode);
		}

	}

}
