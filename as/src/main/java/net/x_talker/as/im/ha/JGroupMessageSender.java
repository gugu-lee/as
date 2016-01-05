package net.x_talker.as.im.ha;

import java.util.Collection;

import org.apache.log4j.helpers.Loader;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Util;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.util.PropertiesUtil;

/**
 * HA同步消息发送处理类
 * 
 *
 */
public class JGroupMessageSender {

	private JChannel jchannel;
	private RpcDispatcher disp;
	private boolean isCluster = false;

	public JGroupMessageSender(String groupName, Object server_obj) throws Exception {
		String clusterEnable = PropertiesUtil.getInstance().getPropVal(BizConsts.CONFKEY_JGROUP_CLUSTER_ENABLE);
		if (clusterEnable.equals("true")) {
			isCluster = true;
			String props = Loader.getResource("udp.xml").getPath();
			jchannel = new JChannel(props);
			disp = new RpcDispatcher(jchannel, server_obj);
			jchannel.connect(groupName);
		}

	}

	@SuppressWarnings("unchecked")
	public void sendEvent(HAContainerItem item, String method, Class callBackClass) throws Exception {
		if (!isCluster) {
			return;
		}

		MethodCall call = new MethodCall(callBackClass.getMethod(method, item.getClass()));
		call.setArgs(item);
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, BizConsts.JGROUP_RPC_TIMEOUT);
		disp.callRemoteMethods(null, call, opts);
	}

	@SuppressWarnings("unchecked")
	public void sendEvent(HAContainerItem item, String method, Class<?> callBackClass, Collection<Address> dests)
			throws Exception {
		if (!isCluster) {
			return;
		}
		MethodCall call = new MethodCall(callBackClass.getMethod(method, item.getClass()));
		call.setArgs(item);
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, BizConsts.JGROUP_RPC_TIMEOUT);
		disp.callRemoteMethods(dests, call, opts);
	}

	public void close() {
		Util.close(jchannel);
	}
}
