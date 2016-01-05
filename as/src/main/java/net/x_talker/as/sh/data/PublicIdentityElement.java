package net.x_talker.as.sh.data;

import java.util.Vector;

public class PublicIdentityElement {
	private Vector<String> publicIdentityList = null;
	private Vector<String> msisdnList = null;

	// Extension
	private int identityType = -1;
	private String wildcardedPSI = null;

	public PublicIdentityElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.PublicIdentifiers_s);
		if (publicIdentityList != null && publicIdentityList.size() > 0) {
			for (int i = 0; i < publicIdentityList.size(); i++) {
				sBuffer.append(ShDataTags.IMSPublicIdentity_s);
				sBuffer.append(publicIdentityList.get(i));
				sBuffer.append(ShDataTags.IMSPublicIdentity_e);
			}
		}

		if (msisdnList != null && msisdnList.size() > 0) {
			for (int i = 0; i < msisdnList.size(); i++) {
				sBuffer.append(ShDataTags.MSISDN_s);
				sBuffer.append(msisdnList.get(i));
				sBuffer.append(ShDataTags.MSISDN_e);
			}
		}

		if (identityType != -1 || wildcardedPSI != null) {
			sBuffer.append(ShDataTags.Extension_s);
			if (identityType != -1) {
				sBuffer.append(ShDataTags.IdentityType_s);
				sBuffer.append(identityType);
				sBuffer.append(ShDataTags.IdentityType_e);
			}

			if (wildcardedPSI != null) {
				sBuffer.append(ShDataTags.WildcardedPSI_s);
				sBuffer.append(wildcardedPSI);
				sBuffer.append(ShDataTags.WildcardedPSI_e);
			}
			sBuffer.append(ShDataTags.Extension_e);
		}

		sBuffer.append(ShDataTags.PublicIdentifiers_e);
		return sBuffer.toString();
	}

	public void addPublicIdentity(String publicIdentity) {
		if (publicIdentityList == null) {
			publicIdentityList = new Vector<String>();
		}
		publicIdentityList.add(publicIdentity);
	}

	public void addMSISDN(String msisdn) {
		if (msisdnList == null) {
			msisdnList = new Vector<String>();
		}
		msisdnList.add(msisdn);
	}

	public int getIdentityType() {
		return identityType;
	}

	public void setIdentityType(int identityType) {
		this.identityType = identityType;
	}

	public Vector<String> getMsisdnList() {
		return msisdnList;
	}

	public void setMsisdnList(Vector<String> msisdnList) {
		this.msisdnList = msisdnList;
	}

	public Vector<String> getPublicIdentityList() {
		return publicIdentityList;
	}

	public void setPublicIdentityList(Vector<String> publicIdentityList) {
		this.publicIdentityList = publicIdentityList;
	}

	public String getWildcardedPSI() {
		return wildcardedPSI;
	}

	public void setWildcardedPSI(String wildcardedPSI) {
		this.wildcardedPSI = wildcardedPSI;
	}

}
