package net.x_talker.as.im.ha;

import java.io.Serializable;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.util.PropertiesUtil;

public class HAContainerItem<T> implements Serializable {

	private static final long serialVersionUID = System.nanoTime();
	private String groupId;
	private T item;

	public HAContainerItem() {
		this.groupId = PropertiesUtil.getInstance().getPropVal(BizConsts.CONFKEY_JGROUP_MEMBER_ID);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}
}
