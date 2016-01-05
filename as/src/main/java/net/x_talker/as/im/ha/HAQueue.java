package net.x_talker.as.im.ha;

import java.util.Queue;

import org.apache.log4j.Logger;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.util.PropertiesUtil;

/**
 * 
 * HA同步Queue抽象实现类 建议需要进行HA的Queue继承该类而非实现HAQueueInf接口
 * 该类已实现缓存队列数据同步的基本逻辑,并实现数据的基本操作 对数据操作有其他逻辑处理要求,可重写对应的protected方法
 * 如:数据增加可重写doAdd方法,数据移除重写doRemove方法,数据获取并移除重写doPoll方法
 * 
 * 子类必须实现initQueue方法,以初始化缓存队列
 * 该类已将构造方法实现为protected,因此子类实现时必须实现为单例模式,以此保证同一JVM中Queue的唯一性
 * 
 * 
 */
public abstract class HAQueue<T> implements HAQueueInf<T> {

	protected Queue<T> queue;
	private JGroupMessageSender sender;
	private Logger logger = Logger.getLogger(HAQueue.class);

	protected HAQueue(String queueName) {
		try {
			this.sender = new JGroupMessageSender(queueName, this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		initQueue();
	}

	/**
	 * 缓存队列初始化抽象方法,子类实现该方法以初始化缓存队列,以实现子类缓存队列的个性化
	 */
	protected abstract void initQueue();

	/**
	 * 元素增加逻辑实现 向缓存队列增加元素前发送HA同步消息 然后调用doAdd方法,子类可通过重写doAdd方法以个性化元素增加逻辑
	 */
	@Override
	public boolean addItem(T item) {
		HAContainerItem<T> haItem = constructHaItem(item);
		sendHAMessage(haItem, "handleAddMsg");
		return doAdd(item);
	}

	/**
	 * 向队列中增加元素
	 * 
	 * @param item
	 * @return
	 */
	protected boolean doAdd(T item) {
		return queue.add(item);
	}

	/**
	 * 从缓存队列中获取并移除头元素逻辑实现 执行队列元素获取及移除之前先发送HA同步消息
	 * 然后调用doPoll方法,子类可通过重写doPoll方法以个性化逻辑
	 * 
	 */
	@Override
	public T pollItem() {
		T item = doPoll();
		// 只有在item不为空的情况下才发同步消息
		if (item != null) {
			HAContainerItem<T> haItem = constructHaItem(item);
			sendHAMessage(haItem, "handleRemoveMsg");
		}
		return item;
	}

	/**
	 * 从缓存队列中获取并移除头元素
	 * 
	 * @return
	 */
	protected T doPoll() {

		try {
			return queue.poll();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 从缓存队列中移除元素 执行队列元素获取及移除之前先发送HA同步消息 然后调用doRemove方法,子类可通过重写doRemove方法以个性化逻辑
	 */
	@Override
	public boolean removeItem(T item) {
		HAContainerItem<T> haItem = constructHaItem(item);
		sendHAMessage(haItem, "handleRemoveMsg");
		return doRemove(item);
	}

	/**
	 * 从缓存队列中移除元素
	 * 
	 * @param item
	 * @return
	 */
	protected boolean doRemove(T item) {
		return queue.remove(item);
	}

	/**
	 * 发送HA同步消息
	 * 
	 * @param item
	 *            操作涉及的元素
	 * @param method
	 *            回调方法名称
	 */
	protected void sendHAMessage(HAContainerItem<T> item, String method) {
		try {
			this.sender.sendEvent(item, method, getClass());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 元素增加回调方法
	 */
	public boolean handleAddMsg(HAContainerItem<T> haItem) {
		if (isSelfMessage(haItem)) {
			logger.debug("receive self message!");
			return true;
		} else {
			return doAdd(haItem.getItem());
		}
	}

	/**
	 * 元素删除回调方法
	 */
	public boolean handleRemoveMsg(HAContainerItem<T> haItem) {
		if (isSelfMessage(haItem)) {
			return true;
		} else {
			return removeItem(haItem.getItem());
		}
	}

	/**
	 * 是否是本JVM发送的HA同步消息判断 根据配置的JGROUP_MEMBER_ID判断 在部署时应严格限制不同JVM的配置值不一样
	 * 
	 * @param item
	 * @return
	 */
	protected boolean isSelfMessage(HAContainerItem<T> item) {
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
	protected HAContainerItem<T> constructHaItem(T item) {
		HAContainerItem<T> haItem = new HAContainerItem<T>();
		haItem.setItem(item);
		return haItem;
	}

	/**
	 * 缓存容器销毁处理
	 */
	public void destroy() {
		this.sender.close();
	}

	public int size() {
		return queue.size();
	}
}
