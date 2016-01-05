package net.x_talker.as.im.ha;

/**
 * 
 * 需要进行HA复制Queue接口定义类 对于需要保证数据的完整性队列数据就实现HA 该接口定义队列数据常用的增加、移除、获取等常用方法及数据同步增加、移除
 * 该接口采取泛型实现
 * 
 * @author zengqiaowen
 *
 * @param <T>
 */
public interface HAQueueInf<T> {

	/**
	 * 从缓存队列中获取并移除头元素
	 * 
	 * @return
	 */
	public T pollItem();

	/**
	 * 向缓存队列中增加元素 增加成功则返回true,增加失败返回false
	 * 
	 * @param item
	 * @return
	 */
	public boolean addItem(T item);

	/**
	 * 从缓存队列中移除元素 移除元素成功返回true,移除元素失败返回false
	 * 
	 * @param item
	 * @return
	 */
	public boolean removeItem(T item);

	/**
	 * HA元素数据增加同步时回调方法 数据同步成功返回true,数据同步失败返回false
	 * 
	 * @param item
	 * @return
	 */
	public boolean handleAddMsg(HAContainerItem<T> item);

	/**
	 * HA元素数据移除同步时回调方法 数据同步成功返回true,数据同步失败返回false
	 * 
	 * @param item
	 * @return
	 */
	public boolean handleRemoveMsg(HAContainerItem<T> item);

	public void destroy();

	public int size();
}
