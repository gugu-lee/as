package net.x_talker.as.im.ha;

import java.util.Map;

/**
 * 
 * 需要进行HA复制Cache接口定义类 对于需要保证缓存数据在故障情况下不丢失,应对该部分缓存实现HA复制
 * 该接口定义数据缓存数据增加、修改、移除接口以及数据同步增加、修改、移除
 * 
 * @author zengqiaowen
 *
 * @param <K>
 * @param <V>
 */
public interface HACacheInf<K, V> {

	/**
	 * 向缓存Cache增加元素 具体实现时应实现HA同步消息的发送
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V addItem(K key, V value);

	/**
	 * 从缓存Cache获取并移除Key值为key的元素 具体实现时应实现HA同步消息的发送
	 * 
	 * @param key
	 * @return
	 */
	public V getAndRemoveItem(K key);

	/**
	 * 根据KEY值获取元素,该方法不做HA同步
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * 从缓存Cache中移除Key值为key的元素 具体实现时应实现HA同步消息的发送
	 * 
	 * @param key
	 * @return
	 */
	public V removeItem(K key);

	/**
	 * 修改缓存Cache中Key值为key的元素为value 具体实现时应实现HA同步消息的发送
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V modifyItem(K key, V value);

	/**
	 * HA元素增加同步回调方法
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean handleAddItem(HAContainerItem<Map<K, V>> haItem);

	/**
	 * HA元素移除同步回调方法
	 * 
	 * @param key
	 * @return
	 */
	public V handleRemoveItem(HAContainerItem<K> keyItem);

	/**
	 * HA元素修改同步回调方法
	 * 
	 * @param key
	 * @return
	 */
	public Boolean handleModifyItem(HAContainerItem<Map<K, V>> haItem);

	/**
	 * 返回当前缓存容器元素数量
	 * 
	 * @return
	 */
	public int size();
}
