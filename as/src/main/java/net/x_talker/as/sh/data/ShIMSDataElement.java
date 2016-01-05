package net.x_talker.as.sh.data;

import java.util.Vector;

public class ShIMSDataElement {
	private String scscfName = null;
	private Vector<InitialFilterCriteriaElement> ifcList;
	private int imsUserState = -1;
	private ChargingInformationElement chgInformation = null;

	private boolean addEmptySCSCFName = false;
	private boolean addEmptyIFCs = false;

	// Extensions
	private int psiActivation = -1;
	private Vector<DSAIElement> dsaiList = null;

	public ShIMSDataElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.Sh_IMS_Data_s);

		if (scscfName != null) {
			sBuffer.append(ShDataTags.SCSCFName_s);
			sBuffer.append(scscfName);
			sBuffer.append(ShDataTags.SCSCFName_e);
		} else if (addEmptySCSCFName) {
			sBuffer.append(ShDataTags.SCSCFName_s);
			sBuffer.append(ShDataTags.SCSCFName_e);
		}

		if (ifcList != null) {
			sBuffer.append(ShDataTags.IFCs_s);
			InitialFilterCriteriaElement ifc;
			for (int i = 0; i < ifcList.size(); i++) {
				ifc = ifcList.get(i);
				sBuffer.append(ifc.toString());
			}
			sBuffer.append(ShDataTags.IFCs_e);
		} else if (addEmptyIFCs) {
			sBuffer.append(ShDataTags.IFCs_s);
			sBuffer.append(ShDataTags.IFCs_e);
		}

		if (imsUserState != -1) {
			sBuffer.append(ShDataTags.IMSUserState_s);
			sBuffer.append(imsUserState);
			sBuffer.append(ShDataTags.IMSUserState_e);
		}
		if (chgInformation != null) {
			sBuffer.append(chgInformation.toString());
		}

		if (psiActivation != -1 || (dsaiList != null && dsaiList.size() > 0)) {
			sBuffer.append(ShDataTags.Extension_s);
			// Extensions
			if (psiActivation != -1) {
				sBuffer.append(ShDataTags.PSIActivation_s);
				sBuffer.append(psiActivation);
				sBuffer.append(ShDataTags.PSIActivation_e);
			}

			if (dsaiList != null && dsaiList.size() > 0) {
				sBuffer.append(ShDataTags.Extension_s);
				for (int i = 0; i < dsaiList.size(); i++) {
					DSAIElement dsai = dsaiList.get(i);
					sBuffer.append(dsai.toString());
				}
				sBuffer.append(ShDataTags.Extension_e);
			}
			sBuffer.append(ShDataTags.Extension_e);
		}
		sBuffer.append(ShDataTags.Sh_IMS_Data_e);
		return sBuffer.toString();
	}

	public void addInitialFilterCriteria(InitialFilterCriteriaElement ifc) {
		if (ifcList == null) {
			ifcList = new Vector<InitialFilterCriteriaElement>();
		}
		ifcList.add(ifc);
	}

	public void addDSAI(DSAIElement dsai) {
		if (dsaiList == null) {
			dsaiList = new Vector<DSAIElement>();
		}
		dsaiList.add(dsai);
	}

	public ChargingInformationElement getChgInformation() {
		return chgInformation;
	}

	public void setChgInformation(ChargingInformationElement chgInformation) {
		this.chgInformation = chgInformation;
	}

	public Vector<InitialFilterCriteriaElement> getIfcList() {
		return ifcList;
	}

	public void setIfcList(Vector<InitialFilterCriteriaElement> ifcList) {
		this.ifcList = ifcList;
	}

	public int getImsUserState() {
		return imsUserState;
	}

	public void setImsUserState(int imsUserState) {
		this.imsUserState = imsUserState;
	}

	public String getScscfName() {
		return scscfName;
	}

	public void setScscfName(String scscfName) {
		this.scscfName = scscfName;
	}

	public Vector<DSAIElement> getDsaiList() {
		return dsaiList;
	}

	public void setDsaiList(Vector<DSAIElement> dsaiList) {
		this.dsaiList = dsaiList;
	}

	public int getPsiActivation() {
		return psiActivation;
	}

	public void setPsiActivation(int psiActivation) {
		this.psiActivation = psiActivation;
	}

	public boolean isAddEmptyIFCs() {
		return addEmptyIFCs;
	}

	public void setAddEmptyIFCs(boolean addEmptyIFCs) {
		this.addEmptyIFCs = addEmptyIFCs;
	}

	public boolean isAddEmptySCSCFName() {
		return addEmptySCSCFName;
	}

	public void setAddEmptySCSCFName(boolean addEmptySCSCFName) {
		this.addEmptySCSCFName = addEmptySCSCFName;
	}

}
