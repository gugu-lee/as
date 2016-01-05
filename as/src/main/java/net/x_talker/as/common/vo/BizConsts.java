
package net.x_talker.as.common.vo;


/**
 * 
 *
 */
public class BizConsts {
	/**********************************Web部分开始****************************************************************/
	// 公共下拉查询部分
	public static final int NORMAL = 0;
	public static final int ODD = 1;
	//报表类型
	public static final String REAL_TIME_REPORT="realTimeReport";//实时报表
	//报表模板设置
	public static final String REPORT_MESSAGE_LOG_TEMPLATE = "report/messagelog.jrxml";
	public static final String REPORT_OPERATELOG_TEMPLATE = "report/operateLog.jrxml";
	
	/**********************************Web部分结束****************************************************************/
	
	/**********************************SMC部分开始****************************************************************/
	//用户注销
	public static final int REGISTER_EXPIRES_LOGOUT=0;
	//默认消息重发间隔时长(秒)
	public static final int DEFAULT_MESSAGE_RESEND_INTERVAL=3600;
	//默认消息重发最大次数
	public static final int DEFAULT_MESSAGE_RESEND_TIMES = 3;
	//JGROUP同步消息RPC请求超时时间
	public static final int JGROUP_RPC_TIMEOUT = 1000;

	public static final String CONFKEY_MESSAGE_RESEND_TIME = "com.ims.as.resend.times";
	
	public static final String CONFKEY_MESSAGE_RESEND_INTERVAL="com.ims.as.resend.interval";
	//JGROUP机器标识配置
	public static final String CONFKEY_JGROUP_MEMBER_ID = "com.ims.as.jgroup.member.id";
	//JGROUP机器标识配置
	public static final String CONFKEY_JGROUP_CLUSTER_ENABLE = "com.ims.as.cluster.enable";
	//消息文件存放目录
	public static final String CONFKEY_MESSAGE_FILE_PATH = "com.ims.as.message.file.path";
	
	//立即发送消息缓存队列名称
	public static final String SEND_IM_CONTAINER_NAME = "SendIMContainer";
	
	public static final String CACHE_IM_CONTAINER_NAME = "CacheIMContainer";
	
	public static final String SENDED_IM_CONTAINER_NAME = "SendedIMContainer";
	
	public static final String REGISTER_USER_CONTAINER_NAME = "RegisterUserContainer";
	
	public static final String MESSAGE_STATE_CONTAINER_NAME = "MessageStateContainer";
	
	public static final String PERSIST_IM_CONTAINER = "PersistIMContainer";
	
	public static final String MESSAGE_CONTENT_FILE_CONTAINER = "MessageContentFileContainer";
	//延迟消息缓存队列名称
	public static final String DELAYED_MESSAGE_CONTAINER_NAME = "DelayeMessageContainer";
	//用户注册状态已注册
	public static int USER_REGISTER_STATE_REGISTERED = 1;
	//用户注册状态未注册
	public static int USER_REGISTER_STATE_UNREGISTERED = 0;
	//用户状态已销户
	public static int USER_REGISTER_STATE_NOTEXIST = 2;
	
//	public static String CONTENT_TYPE_MSG = "text/xml-smml";
	
	public static String USER_AGENT_SMC = "sms-serv";
	//短消息有效期
	public static final String CONFKEY_DEFAULT_MESSAGE_VALIDITY_PERIOD = "com.ims.as.message.validity.period";
	//回执有效期
	public static final String CONFKEY_DEFAULT_RECEIPT_VALIDITY_PERIOD = "com.ims.as.receipt.validity.period";
	//短消息状态写入数据缓存时间
	public static final String CONFKEY_MESSAGE_STATE_FLUSH_PERIOD = "com.ims.as.message.state.flush.period";
	
	//新增并写入消息数据到文件
	public final static int X_TALKER_SIP_MSG_OP_FILE_TYPE_CREATE = 1;
	//删除消息文件
	public final static int X_TALKER_SIP_MSG_OP_FILE_TYPE_DELETE = 2;
	 
	
	/**********************************SMC部分结束****************************************************************/
	
	//MessageState
	public class MessageStateCode{
		public static final int MESSAGE_SATTE_UNKNOWN = -1;
		//SMC已收到消息
		public static final int MESSAGE_SATTE_SMC_RECIVED = 0;
		//消息已经发出
		public static final int MESSAGE_SATTE_SMC_SENDED = 1;
		//消息等待定时发送
		public static final int MESSAGE_SATTE_SMC_WAIT_REGULARLY_SEND = 2;
		//用户不在线，等待发送
		public static final int MESSAGE_SATTE_SMC_USER_NOT_ONLINE_WAIT = 3;
		//消息重发
		public static final int MESSAGE_SATTE_SMC_RESEND = 4;
		//消息过期，已丢弃
		public static final int MESSAGE_SATTE_SMC_MESSAGE_EXPIRED = 5;
		//消息已发回执
		public static final int MESSAGE_SATTE_SMC_MESSAGE_HAS_RECEIPT = 6;
	}
}




















