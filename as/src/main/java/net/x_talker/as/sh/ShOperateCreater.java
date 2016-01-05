
package net.x_talker.as.sh;

import java.util.Vector;

import org.apache.log4j.Logger;

import net.x_talker.as.sh.container.AsShContainer;
import net.x_talker.as.sh.diameter.DiameterConstants;
import net.x_talker.as.sh.diameter.DiameterStack;
import net.x_talker.as.sh.task.Task;

/**
 * 【根据AS业务的需要，在此类中定义相关方法，由此类生成Task去执行相关操作】
 *
 * @version
 * @author xubo 2014-4-2 下午02:32:59
 * 
 */
public class ShOperateCreater {
	private static Logger logger = Logger.getLogger(ShOperateCreater.class);

	private ShOperateCreater() {

	}

	public static void getUserStateByUDR(String user) {
		logger.info("send UDR check user state,user:" + user);
		DiameterStack diamStack = AsShContainer.getInstance().diamStack;
		Vector<Integer> dataRefVector = new Vector<Integer>();
		// data_ref_vector.add(ShConstants.Data_Ref_IMS_Public_Identity);
		dataRefVector.add(ShConstants.Data_Ref_IMS_User_State);
		Task task = new Task(diamStack, Task.SENDING_REQUEST, DiameterConstants.Command.UDR_OR_UDA,
				DiameterConstants.Application.Sh);
		task.dataRefVector = dataRefVector;
		task.userIdentity = user;
		task.identitySet = ShConstants.Identity_Set_All_Identities;
		try {
			AsShContainer.getInstance().tasksQueue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void sendUserStateSubscriptionBySNR(String user) {
		logger.info("send SNR to subscribe user state,user:" + user);
		DiameterStack diamStack = AsShContainer.getInstance().diamStack;
		Vector<Integer> dataRefVector = new Vector<Integer>();
		dataRefVector.add(ShConstants.Data_Ref_IMS_User_State);
		Task task = new Task(diamStack, Task.SENDING_REQUEST, DiameterConstants.Command.SNR_OR_SNA,
				DiameterConstants.Application.Sh);
		task.dataRefVector = dataRefVector;
		task.userIdentity = user;
		task.subsReqType = ShConstants.Subs_Req_Type_Subscribe;
		try {
			AsShContainer.getInstance().tasksQueue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
