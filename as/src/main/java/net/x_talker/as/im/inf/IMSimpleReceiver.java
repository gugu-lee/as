package net.x_talker.as.im.inf;

import java.util.List;

import javax.annotation.Resource;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import net.x_talker.as.common.vo.BizConsts;
import net.x_talker.as.im.domain.DomainSelect;
import net.x_talker.as.im.filter.IMRuleFactory;
import net.x_talker.as.im.filter.IMRuleFilter;
import net.x_talker.as.im.handler.IMReceiveHandler;
import net.x_talker.as.im.handler.IMRegisterHandler;
import net.x_talker.as.im.servlet.IMResponse;


@Repository
public class IMSimpleReceiver implements IMReceiverInf {

	@Resource(name = "IMReceiveHandler")
	private IMReceiveHandler imReceiveHandler;

	private DomainSelect domainSelect = new DomainSelect();

	@Resource(name = "IMRegisterHandler")
	private IMRegisterHandler registerHanler;

	private Logger logger = Logger.getLogger(IMSimpleReceiver.class);

	/**
	 */
	@Override
	public IMResponse onMessageReceive(Request request) {
		logger.info("into onMessageReceive");
		IMResponse resp = new IMResponse();

		Address to = ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress();
		URI toUri = to.getURI();
		//
		if (domainSelect.isThisDomain(toUri)) {

			List<IMRuleFilter> ruleList = IMRuleFactory.getInstance().getUserFilterRules();
			if (ruleList != null) {
				for (IMRuleFilter filter : ruleList) {

				}
			}
			logger.info("start imReceiveHandler.handleIM as soon");
			resp = imReceiveHandler.handleIM(request);
		} else {
			logger.info("receive message is not belong this domain!");
			resp.setStatuscode(Response.OK);
		}
		return resp;
	}


	@Override
	public void onRegisterReceive(Request request) {

		URI user = ((ToHeader) request.getHeader(ToHeader.NAME)).getAddress().getURI();
		if (((ExpiresHeader) request.getHeader(ExpiresHeader.NAME)).getExpires() != BizConsts.REGISTER_EXPIRES_LOGOUT) {
			registerHanler.registerUser(user);
		} else {
			registerHanler.unRegisterUser(user);
		}

	}
}
