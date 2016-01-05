package net.x_talker.as.im.domain;

import javax.sip.address.URI;


public class DomainSelect {

	public boolean isThisDomain(URI sipUri) {
		return true;
	}

	public URI getHomeDomain(URI sipUri) {
		return null;
	}
}
