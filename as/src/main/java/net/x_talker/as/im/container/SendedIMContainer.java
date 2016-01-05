package net.x_talker.as.im.container;

import java.util.concurrent.ConcurrentHashMap;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.ha.HACache;

//已发送消息缓存MAP,key值为发送时的CALL_ID头域值,VALUE为组建SIPServletReqeust对象的XTalkerSipMsg
public class SendedIMContainer extends HACache<String, XTalkerSipMsg> {

	private static SendedIMContainer instance = new SendedIMContainer(BizConsts.SENDED_IM_CONTAINER_NAME);

	protected SendedIMContainer(String cacheName) {
		super(cacheName);
	}

	public static SendedIMContainer getInstance() {
		return instance;
	}

	@Override
	protected void initCache() {
		super.cache = new ConcurrentHashMap<String, XTalkerSipMsg>();

	}
}
