package net.x_talker.as.im.handler;

import java.util.List;

import javax.sip.address.URI;

//import javax.servlet.sip.ServletParseException;
//import javax.servlet.sip.URI;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.CacheIMContainer;
import net.x_talker.as.im.container.RebootSendIMContainer;
import net.x_talker.as.im.container.RegisterUserContainer;
import net.x_talker.as.im.container.SendIMContainer;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.util.SipMessageConvertUtil;
import net.x_talker.as.sh.ShOperateCreater;

@Component("IMRegisterHandler")
public class IMRegisterHandlerImpl implements IMRegisterHandler {

	private Logger logger = Logger.getLogger(IMRegisterHandlerImpl.class);

	RegisterUserContainer userCache = RegisterUserContainer.getInstance();

	@Override
	public void registerUser(URI user) {
		logger.info("register user:" + user);
		// 向容器中添加用户注册信息
		userCache.registerUser(user);
		// 查询注册用户是否有缓存消息未下发
		List<XTalkerSipMsg> msgList = CacheIMContainer.getInstance().removeItem(user);
		if (msgList != null) {
			// 将未下发的缓存消息加入到关机延时发送缓存
			for (XTalkerSipMsg sipMsg : msgList) {
				sipMsg.setRebootTime(); // 保存开机时间
				// SendIMContainer.getInstance().addItem(sipMsg);
				RebootSendIMContainer.getInstance().doAdd(sipMsg);
			}
		}
		// 向hss订阅此用户的状态信息，如果下次状态变化通知as
		ShOperateCreater.sendUserStateSubscriptionBySNR(user.toString());
	}

	public void unRegisterUser(URI toUser) {
		// 更新用户注册状态
		userCache.unRegisterUser(toUser);

		// 向hss订阅此用户的状态信息，如果下次状态变化通知as
		ShOperateCreater.sendUserStateSubscriptionBySNR(toUser.toString());
	}

	/**
	 * 判断用户是否在线处理 首先从容器中获取用户数据,如有注册数据将返回相应状态 如没有注册数据应主动向HSS同步用户数据
	 * 如HSS没有用户注册数据,则向容器中添加用户注册状态为未知数据
	 */
	public boolean getUserState(URI toUser, boolean isSendUdr) {
		// 从容器中获取用户状态
		Integer state = userCache.get(toUser);
		logger.info("get user[ " + toUser + " ] state is:" + String.valueOf(state));
		if (state != null) {
			if (state == BizConsts.USER_REGISTER_STATE_REGISTERED) {
				return true;
			} else {
				return false;
			}
		} else {
			// UDR查询用户状态
			if (isSendUdr) {
				logger.info("Not Found user information,user:" + toUser);
				ShOperateCreater.getUserStateByUDR(toUser.toString());
			}
		}

		return false;
	}

	public void registerUser(String toUser) {
		logger.info("register user:" + toUser);

		URI addr = SipMessageConvertUtil.createURI(toUser);
		registerUser(addr);

	}

	public void unRegisterUser(String toUser) {
		logger.info("unregister user:" + toUser);
		/// try {
		URI addr = SipMessageConvertUtil.createURI(toUser);
		unRegisterUser(addr);
		// } catch (ServletParseException e) {
		// logger.error(e.getMessage(), e);
		// }

	}

}
