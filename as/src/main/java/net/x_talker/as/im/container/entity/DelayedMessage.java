package net.x_talker.as.im.container.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue实体对象类 泛型实现,T为DelayMessage关联延对象引用
 * 
 * @author zengqiaowen
 *
 * @param <T>
 */
public class DelayedMessage<T> implements Delayed, java.io.Serializable {

	/**
	 * Comments for <code>serialVersionUID</code> 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = 4654726747216355949L;
	/** 对象到期时间,默认毫秒单位 **/
	private Long triggerTime;
	private DelayedTypeEnum type;
	private T item;

	public DelayedMessage(Long delayTime, TimeUnit unit, DelayedTypeEnum type) {
		this(delayTime, unit, null, type);
	}

	public DelayedMessage(Long delayTime, TimeUnit unit, T item, DelayedTypeEnum type) {
		this.triggerTime = System.currentTimeMillis() + unit.toMillis(delayTime);
		this.item = item;
		this.type = type;
	}

	@Override
	public int compareTo(Delayed o) {
		return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
	}

	@Override
	public long getDelay(TimeUnit unit) {
		Long diffTime = triggerTime - System.currentTimeMillis();
		return unit.convert(diffTime, TimeUnit.MILLISECONDS);
	}

	public Long getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Long triggerTime) {
		this.triggerTime = triggerTime;
	}

	public DelayedTypeEnum getType() {
		return type;
	}

	public void setType(DelayedTypeEnum type) {
		this.type = type;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

}
