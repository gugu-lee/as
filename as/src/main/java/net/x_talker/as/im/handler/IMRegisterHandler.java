package net.x_talker.as.im.handler;

import javax.sip.address.Address;
import javax.sip.address.URI;

public interface IMRegisterHandler {

	/**
	 * 用户注册事件处理
	 * 
	 * @param to
	 */
	public void registerUser(URI toUser);

	/**
	 * 用户注册事件处理
	 * 
	 * @param to
	 */
	public void registerUser(String toUser);

	/**
	 * 用户解注册处理
	 * 
	 * @param to
	 */
	public void unRegisterUser(URI toUser);

	/**
	 * 用户解注册处理
	 * 
	 * @param to
	 */
	public void unRegisterUser(String toUser);

	/**
	 * 用户在线判断处理
	 * 
	 * @param to
	 * @return
	 */
	public boolean getUserState(URI toUser, boolean isSendUdr);
}
