package net.x_talker.as.im.container;

import java.util.PriorityQueue;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.container.entity.XTalkerSipMsg;
import net.x_talker.as.im.container.entity.IMPriorityComparator;
import net.x_talker.as.im.ha.HAQueue;


public class SendIMContainer extends HAQueue<XTalkerSipMsg> {

	private static SendIMContainer instance = new SendIMContainer(BizConsts.SEND_IM_CONTAINER_NAME);

	public static final int SendIMContainerDefaultSize = 10000;

	protected SendIMContainer(String queueName) {
		super(queueName);
	}

	public static SendIMContainer getInstance() {
		return instance;
	}

	public void addSendIMMsg(XTalkerSipMsg sipMsg) {
		addItem(sipMsg);
	}

	@Override
	protected void initQueue() {
		super.queue = new PriorityQueue<XTalkerSipMsg>(10000, new IMPriorityComparator());
	}

}
