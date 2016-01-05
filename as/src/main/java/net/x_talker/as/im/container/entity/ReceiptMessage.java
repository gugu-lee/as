package net.x_talker.as.im.container.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ReceiptMessage extends MessageBody {

	public static final String RECEIPT_STATE_SUCCESS = "success";
	public static final String RECEIPT_STATE_EXPIRED = "expired";
	public static final String RECEIPT_STATE_FAILURE = "failure";
	/**
	 * Comments for <code>serialVersionUID</code> 【请在此输入描述文字】
	 */
	private static final long serialVersionUID = 8449800121329264527L;
	/**
	 * 短消息发送状态：当接收方为IMS用户时，由短消息中心确定发送状态； 当接收方为其他网络用户时，发送状态拷贝SMPP消息中的相应内容
	 * 
	 * 最终消息状态 说明 success 短消息转发成功 expired 短消息超过有效期 failure 短消息转发失败
	 */
	private String msgstat;
	/**
	 * 短消息发送失败原因：当接收方为IMS用户时，由短消息中心确定错误原因； 当接收方为其他网络用户时，错误原因拷贝SMPP消息中的相应内容
	 * 取值为一个3位的可显示的十进制数字串。不够3位，前面须用0补齐。
	 * 
	 * 
	 * 原因值 含义 说明 对应状态 000 no error 成功 Success 001 destination absent 用户不能通信
	 * Expired 002 destination busy 用户忙 Expired 003 destination not found 用户不存在
	 * Failure 004 invalid destination 非法用户 Failure 005 destination in black
	 * list 用户在黑名单内 Failure 006 system error 系统错误 Failure 007 out of memory
	 * 用户内存满 Expired 008 protocol error 协议错误 Failure 009 data error 数据错误 Failure
	 * 010 deliver error 交互式信息下发失败 Failure 011 overdue bill 用户欠费 Failure 012
	 * destination unsupported 用户无短消息权限 Failure 013 in progess 短消息正在发送中 Expired
	 * 014 exceed validity period 短消息超过有效期 Expired 999 unknown error 未知错误
	 * Failure
	 * 
	 */
	private int errCode;
	private Date doneTime;
	private String ns;

	public String getMsgstat() {
		return msgstat;
	}

	public void setMsgstat(String msgstat) {
		this.msgstat = msgstat;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public Date getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

}
