package net.x_talker.as.im.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.address.Address;
import javax.sip.address.URI;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.ha.HACacheListValue;

/**
 * 缓存消息容器实现类
 *
 */
public class CacheIMContainer extends HACacheListValue<URI, XTalkerSipMsg> {

	private static CacheIMContainer instance = new CacheIMContainer(BizConsts.CACHE_IM_CONTAINER_NAME);

	public static CacheIMContainer getInstance() {
		return instance;
	}

	protected CacheIMContainer(String cacheName) {
		super(cacheName);
	}

	/**
	 * 获取用户缓存的短消息 从缓存容器中获取待发送消息
	 * 
	 * @param to
	 * @return
	 */
	public Boolean addCacheMsg(XTalkerSipMsg sipMsg) {
		Address to = sipMsg.getTo();
		if (to != null) {
			URI uri = to.getURI();
			return this.singleAdd(uri, sipMsg);
		}
		return false;
	}

	@Override
	protected void initCache() {
		this.cache = new ConcurrentHashMap<URI, List<XTalkerSipMsg>>();
	}

}
