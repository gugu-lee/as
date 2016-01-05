package net.x_talker.as.sh.data;

import java.util.Iterator;
import java.util.Vector;

public class ShDataExtensionElement {
	private PublicIdentityElement registeredIdentities = null;
	private PublicIdentityElement implicitIdentities = null;
	private PublicIdentityElement allIdentities = null;
	private PublicIdentityElement aliasIdentities = null;
	private Vector<AliasesRepositoryDataElement> aliasesRepositoryDataList = null;

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ShDataExtension_s);
		if (registeredIdentities != null) {
			sBuffer.append(ShDataTags.RegisteredIdentities_s);
			sBuffer.append(registeredIdentities.toString());
			sBuffer.append(ShDataTags.RegisteredIdentities_e);
		}

		if (implicitIdentities != null) {
			sBuffer.append(ShDataTags.ImplicitIdentities_s);
			sBuffer.append(implicitIdentities.toString());
			sBuffer.append(ShDataTags.ImplicitIdentities_e);
		}

		if (allIdentities != null) {
			sBuffer.append(ShDataTags.AllIdentities_s);
			sBuffer.append(allIdentities.toString());
			sBuffer.append(ShDataTags.AllIdentities_e);
		}

		if (aliasIdentities != null) {
			sBuffer.append(ShDataTags.AliasIdentities_s);
			sBuffer.append(aliasIdentities.toString());
			sBuffer.append(ShDataTags.AliasIdentities_e);
		}

		if (aliasesRepositoryDataList != null && aliasesRepositoryDataList.size() > 0) {
			Iterator<AliasesRepositoryDataElement> it = aliasesRepositoryDataList.iterator();
			AliasesRepositoryDataElement transparentData;
			while (it.hasNext()) {
				transparentData = it.next();
				sBuffer.append(transparentData.toString());
			}
		}

		sBuffer.append(ShDataTags.ShDataExtension_e);
		return sBuffer.toString();
	}

	public void addAliasesRepositoryData(AliasesRepositoryDataElement aliasesRepositoryData) {
		if (aliasesRepositoryDataList == null) {
			aliasesRepositoryDataList = new Vector<AliasesRepositoryDataElement>();
		}
		aliasesRepositoryDataList.add(aliasesRepositoryData);
	}

	public Vector<AliasesRepositoryDataElement> getAliasesRepositoryDataList() {
		return aliasesRepositoryDataList;
	}

	public void setAliasesRepositoryDataList(Vector<AliasesRepositoryDataElement> aliasesRepositoryDataList) {
		this.aliasesRepositoryDataList = aliasesRepositoryDataList;
	}

	public PublicIdentityElement getAliasIdentities() {
		return aliasIdentities;
	}

	public void setAliasIdentities(PublicIdentityElement aliasIdentities) {
		this.aliasIdentities = aliasIdentities;
	}

	public PublicIdentityElement getAllIdentities() {
		return allIdentities;
	}

	public void setAllIdentities(PublicIdentityElement allIdentities) {
		this.allIdentities = allIdentities;
	}

	public PublicIdentityElement getImplicitIdentities() {
		return implicitIdentities;
	}

	public void setImplicitIdentities(PublicIdentityElement implicitIdentities) {
		this.implicitIdentities = implicitIdentities;
	}

	public PublicIdentityElement getRegisteredIdentities() {
		return registeredIdentities;
	}

	public void setRegisteredIdentities(PublicIdentityElement registeredIdentities) {
		this.registeredIdentities = registeredIdentities;
	}

}
