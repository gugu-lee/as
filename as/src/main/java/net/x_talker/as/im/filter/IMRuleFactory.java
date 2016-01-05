package net.x_talker.as.im.filter;

import java.util.List;

public class IMRuleFactory {

	private static IMRuleFactory instance = new IMRuleFactory();

	private IMRuleFactory() {
	}

	public static IMRuleFactory getInstance() {
		return instance;
	}

	public List<IMRuleFilter> getUserFilterRules() {
		return null;
	}
}
