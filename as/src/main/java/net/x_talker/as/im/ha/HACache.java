package net.x_talker.as.im.ha;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * HA同步复制CACHE抽象实现类 建议需要同步复制缓存内数据的缓存容器继承该抽象类 该抽象类已实现缓存数据的基本逻辑操作,包括:新增、修改、删除
 * 相应操作中都实现了消息同步逻辑,并抽取相应新增、修改、删除操作为protected方法,以方便子类自定义重写
 * 如:数据增加可重写doAdd方法,数据移除重写doRemove方法,数据修改并移除重写doRemove方法
 * 
 * 子类必须实现initCache方法,以初始化缓存容器对象 构造方法实现为protected,需要子类实现为单例模式,以保证缓存容器的唯一性
 * 
 * @author zengqiaowen
 *
 * @param <K>
 * @param <V>
 */
public abstract class HACache<K, V> implements HACacheInf<K, V> {

	protected Map<K, V> cache;
	protected JGroupMessageSender sender;
	private Logger logger = Logger.getLogger(HACache.class);
	protected HAMessageUtil haUtil = new HAMessageUtil();

	protected HACache(String cacheName) {
		try {
			sender = new JGroupMessageSender(cacheName, this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		initCache();
	}

	/**
	 * 缓存初始化抽象方法
	 */
	protected abstract void initCache();

	/**
	 * 向缓存增加元素方法实现 每次元素增加首先发送HA同步增加消息 子类重写该方法时,需要保证HA同步消息的发送
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V addItem(K key, V value) {
		HAContainerItem<Map> haItem = haUtil.constructHaItem(key, value);
		haUtil.sendHAMessage(sender, haItem, "handleAddItem", getClass());
		return doAdd(key, value);
	}

	/**
	 * 向缓存中添加元素 子类可以重写该方法以自定义添加逻辑
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected V doAdd(K key, V value) {
		return cache.put(key, value);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V modifyItem(K key, V value) {
		HAContainerItem<Map> haItem = haUtil.constructHaItem(key, value);
		haUtil.sendHAMessage(sender, haItem, "handleModifyItem", getClass());
		return doModify(key, value);
	}

	/**
	 * 修改缓存中已有元素 子类可以重写该方法以自定义修改逻辑
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected V doModify(K key, V value) {
		return cache.put(key, value);
	}

	@Override
	public V getAndRemoveItem(K key) {
		return cache.get(key);
	}

	/**
	 * 删除缓存中已有元素 子类可以重写该方法以自定义删除逻辑
	 * 
	 * @param key
	 * @return
	 */
	protected V doRemove(K key) {
		return cache.remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V removeItem(K key) {
		HAContainerItem haItem = haUtil.constructHaItem(key);
		haUtil.sendHAMessage(sender, haItem, "handleRemoveItem", getClass());
		return doRemove(key);
	}

	public V get(K key) {
		return cache.get(key);
	}

	@Override
	public Boolean handleAddItem(HAContainerItem<Map<K, V>> haItem) {
		if (haUtil.isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return null;
		} else {
			Map<K, V> item = haItem.getItem();
			if (item == null) {
				logger.info("receive ha message item is null");
				return null;
			}
			Entry<K, V> itemEntry = item.entrySet().iterator().next();
			V value = doAdd(itemEntry.getKey(), itemEntry.getValue());
			if (value != null) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public Boolean handleModifyItem(HAContainerItem<Map<K, V>> haItem) {
		if (haUtil.isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return null;
		} else {
			Map<K, V> item = haItem.getItem();
			if (item == null) {
				logger.info("receive ha message item is null");
				return null;
			}
			Entry<K, V> itemEntry = item.entrySet().iterator().next();
			V value = doModify(itemEntry.getKey(), itemEntry.getValue());
			if (value != null) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public V handleRemoveItem(HAContainerItem<K> haItem) {
		if (haUtil.isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return null;
		} else {
			K key = haItem.getItem();
			if (key == null) {
				logger.info("receive ha message item is null");
				return null;
			}
			return doRemove(key);
		}
	}

	/**
	 * 缓存容器销毁处理
	 */
	public void destroy() {
		this.sender.close();
	}

	public int size() {
		return cache.size();

	}
}
