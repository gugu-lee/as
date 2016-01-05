package net.x_talker.as.im.container;

import java.util.concurrent.ConcurrentHashMap;

import javax.sip.address.URI;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.ha.HACache;

/**
 * 保存已注册用户信息
 * 
 * @author zengqiaowen
 *
 */
public class RegisterUserContainer extends HACache<URI, Integer> {

	protected RegisterUserContainer(String cacheName) {
		super(cacheName);
	}

	private static RegisterUserContainer instance = new RegisterUserContainer(BizConsts.REGISTER_USER_CONTAINER_NAME);

	public static RegisterUserContainer getInstance() {
		return instance;
	}

	@Override
	protected void initCache() {
		this.cache = new ConcurrentHashMap<URI, Integer>();
	}

	public void registerUser(URI user) {
		this.addItem(user, BizConsts.USER_REGISTER_STATE_REGISTERED);
	}

	public void unRegisterUser(URI user) {
		this.addItem(user, BizConsts.USER_REGISTER_STATE_UNREGISTERED);
	}

}
