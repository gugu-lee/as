package net.x_talker.as.im.container.entity;

import java.util.Comparator;

/**
 *
 */
public class IMPriorityComparator implements Comparator<XTalkerSipMsg> {

	@Override
	public int compare(XTalkerSipMsg o1, XTalkerSipMsg o2) {

		return 1;
		/*
		if (o1.getPriority() == o2.getPriority()) {
			if (o1.getSubmitTime() == null) {
				return -1;
			}
			if (o2.getSubmitTime() == null) {
				return 1;
			}
			// 提交时间早的优先级更高
			return o1.getSubmitTime().compareTo(o2.getSubmitTime());
		}
		return o2.getPriority() - o1.getPriority();
		*/
	}

}
