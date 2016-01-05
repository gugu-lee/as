package net.x_talker.as.sh.data;

import java.util.Vector;

public class ShDataElement {

	private PublicIdentityElement publicIdentifiers = null;
	private Vector<RepositoryDataElement> repositoryDataList = null;
	private ShIMSDataElement shIMSData = null;
	private CSLocationInformationElement csLocationInformation = null;
	private PSLocationInformationElement psLocationInformation = null;
	private int csUserState = -1;
	private int psUserState = -1;
	private ShDataExtensionElement shDataExtension;

	public ShDataElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ShData_s);

		// append all the child elements
		if (publicIdentifiers != null) {
			sBuffer.append(publicIdentifiers.toString());
		}
		if (repositoryDataList != null && repositoryDataList.size() > 0) {
			for (int i = 0; i < repositoryDataList.size(); i++) {
				sBuffer.append(repositoryDataList.get(i).toString());
			}
		}
		if (shIMSData != null) {
			sBuffer.append(shIMSData.toString());
		}
		if (csLocationInformation != null) {
			sBuffer.append(csLocationInformation.toString());
		}
		if (psLocationInformation != null) {
			sBuffer.append(psLocationInformation.toString());
		}
		if (csUserState != -1) {
			sBuffer.append(ShDataTags.CSUserState_s);
			sBuffer.append(csUserState);
			sBuffer.append(ShDataTags.CSUserState_e);
		}
		if (psUserState != -1) {
			sBuffer.append(ShDataTags.PSUserState_s);
			sBuffer.append(psUserState);
			sBuffer.append(ShDataTags.CSUserState_e);
		}
		if (shDataExtension != null) {
			sBuffer.append(shDataExtension.toString());
		}
		sBuffer.append(ShDataTags.ShData_e);
		return sBuffer.toString();
	}

	public void addRepositoryData(RepositoryDataElement data) {
		if (repositoryDataList == null) {
			repositoryDataList = new Vector<RepositoryDataElement>();
		}
		repositoryDataList.add(data);
	}

	// getters & setters
	public CSLocationInformationElement getCsLocationInformation() {
		return csLocationInformation;
	}

	public void setCsLocationInformation(CSLocationInformationElement csLocationInformation) {
		this.csLocationInformation = csLocationInformation;
	}

	public PSLocationInformationElement getPsLocationInformation() {
		return psLocationInformation;
	}

	public void setPsLocationInformation(PSLocationInformationElement psLocationInformation) {
		this.psLocationInformation = psLocationInformation;
	}

	public int getCsUserState() {
		return csUserState;
	}

	public void setCsUserState(int csUserState) {
		this.csUserState = csUserState;
	}

	public int getPsUserState() {
		return psUserState;
	}

	public void setPsUserState(int psUserState) {
		this.psUserState = psUserState;
	}

	public Vector<RepositoryDataElement> getRepositoryDataList() {
		return repositoryDataList;
	}

	public void setRepositoryDataList(Vector<RepositoryDataElement> repositoryDataList) {
		this.repositoryDataList = repositoryDataList;
	}

	public PublicIdentityElement getPublicIdentifiers() {
		return publicIdentifiers;
	}

	public void setPublicIdentifiers(PublicIdentityElement publicIdentifiers) {
		this.publicIdentifiers = publicIdentifiers;
	}

	public ShDataExtensionElement getShDataExtension() {
		return shDataExtension;
	}

	public void setShDataExtension(ShDataExtensionElement shDataExtension) {
		this.shDataExtension = shDataExtension;
	}

	public ShIMSDataElement getShIMSData() {
		return shIMSData;
	}

	public void setShIMSData(ShIMSDataElement shIMSData) {
		this.shIMSData = shIMSData;
	}

}
