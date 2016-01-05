package net.x_talker.as.im.ha;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.util.PropertiesUtil;

/**
 * HA消息处理帮助类
 * 
 * @author zengqiaowen
 *
 */
public class HAMessageUtil {

	private Logger logger = Logger.getLogger(HAMessageUtil.class);

	/**
	 * 发送HA同步消息
	 * 
	 * @param item
	 *            操作涉及的元素
	 * @param method
	 *            回调方法名称
	 */
	public void sendHAMessage(JGroupMessageSender sender, HAContainerItem<?> item, String method,
			Class<?> callBackClass) {
		try {
			sender.sendEvent(item, method, callBackClass);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 是否是本JVM发送的HA同步消息判断 根据配置的JGROUP_MEMBER_ID判断 在部署时应严格限制不同JVM的配置值不一样
	 * 
	 * @param item
	 * @return
	 */
	public boolean isSelfMessage(HAContainerItem<?> item) {
		String srcMember = item.getGroupId();
		String localMember = PropertiesUtil.getInstance().getPropVal(BizConsts.CONFKEY_JGROUP_MEMBER_ID);
		if (srcMember.equals(localMember)) {
			return true;
		}
		return false;
	}

	/**
	 * 构建HA同步消息对象
	 * 
	 * @param item
	 *            操作元素
	 * @return
	 */
	public HAContainerItem<?> constructHaItem(Object item) {
		HAContainerItem<Object> haItem = new HAContainerItem<Object>();
		haItem.setItem(item);
		return haItem;
	}

	/**
	 * 构建HA同步消息对象
	 * 
	 * @param item
	 *            操作元素
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected HAContainerItem<Map> constructHaItem(Object key, Object value) {
		HAContainerItem<Map> haItem = new HAContainerItem<Map>();
		Map map = new HashMap();
		map.put(key, value);
		haItem.setItem(map);
		return haItem;
	}
}
