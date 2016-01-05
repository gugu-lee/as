package net.x_talker.as.im.ha;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

/**
 * 缓存Value为List情形下HACache的实现 相对于HACache增加单个Value增加的方法
 * 
 * @author zengqiaowen
 *
 * @param <K>
 * @param <V>
 */
public abstract class HACacheListValue<K, V> extends HACache<K, List<V>> {

	private Logger logger = Logger.getLogger(HACacheListValue.class);

	protected HACacheListValue(String cacheName) {
		super(cacheName);
	}

	/**
	 * 向ValueList中添加单个元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean singleAdd(K key, V value) {
		HAContainerItem<Map> haItem = haUtil.constructHaItem(key, value);
		haUtil.sendHAMessage(sender, haItem, "handleSingleAddItem", getClass());
		return doSingleAdd(key, value);
	}

	protected boolean doSingleAdd(K key, V value) {
		List<V> valueList = cache.get(key);
		if (valueList == null) {
			valueList = new CopyOnWriteArrayList<V>();
			cache.put(key, valueList);
		}
		return valueList.add(value);
	}

	/**
	 * 添加单个元素HA同步消息回调方法
	 * 
	 * @param haItem
	 * @return
	 */
	public boolean handleSingleAddItem(HAContainerItem<Map<K, V>> haItem) {
		if (haUtil.isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return true;
		} else {
			Map<K, V> item = haItem.getItem();
			if (item == null) {
				logger.info("receive ha message item is null");
				return true;
			}
			Entry<K, V> itemEntry = item.entrySet().iterator().next();
			return doSingleAdd(itemEntry.getKey(), itemEntry.getValue());
		}
	}

	/**
	 * 删除单元素HA同步消息回调方法
	 * 
	 * @param haItem
	 * @return
	 */
	public Boolean handleRemoveSingleItem(HAContainerItem<Map<K, V>> haItem) {
		if (haUtil.isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return true;
		} else {
			Map<K, V> item = haItem.getItem();
			if (item == null) {
				logger.info("receive ha message item is null");
				return true;
			}
			Entry<K, V> itemEntry = item.entrySet().iterator().next();
			return doSingleRemove(itemEntry.getKey(), itemEntry.getValue());
		}
	}

	/**
	 * 删除单个元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean removeSingleItem(K key, V value) {
		HAContainerItem<Map> haItem = haUtil.constructHaItem(key, value);
		haUtil.sendHAMessage(sender, haItem, "handleRemoveSingleItem", getClass());
		return doSingleRemove(key, value);
	}

	protected Boolean doSingleRemove(K key, V value) {
		List<V> valueList = cache.get(key);
		if (valueList == null) {
			return null;
		} else {
			return valueList.remove(value);
		}
	}
}
