package net.x_talker.as.sh.data;

public class ChargingInformationElement {
	private String priECFName = null;
	private String secECFName = null;
	private String priCCFName = null;
	private String secCCFName = null;

	public ChargingInformationElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.ChargingInformation_s);

		if (priECFName != null) {
			sBuffer.append(ShDataTags.PrimaryEventChargingFunctionName_s);
			sBuffer.append(priECFName);
			sBuffer.append(ShDataTags.PrimaryEventChargingFunctionName_e);
		}

		if (secECFName != null) {
			sBuffer.append(ShDataTags.SecondaryEventChargingFunctionName_s);
			sBuffer.append(secECFName);
			sBuffer.append(ShDataTags.SecondaryEventChargingFunctionName_e);
		}

		if (priCCFName != null) {
			sBuffer.append(ShDataTags.PrimaryChargingCollectionFunctionName_s);
			sBuffer.append(priCCFName);
			sBuffer.append(ShDataTags.PrimaryChargingCollectionFunctionName_e);
		}

		if (secCCFName != null) {
			sBuffer.append(ShDataTags.SecondaryChargingCollectionFunctionName_s);
			sBuffer.append(secCCFName);
			sBuffer.append(ShDataTags.SecondaryChargingCollectionFunctionName_e);
		}

		sBuffer.append(ShDataTags.ChargingInformation_e);
		return sBuffer.toString();
	}

	public String getPriCCFName() {
		return priCCFName;
	}

	public void setPriCCFName(String priCCFName) {
		this.priCCFName = priCCFName;
	}

	public String getPriECFName() {
		return priECFName;
	}

	public void setPriECFName(String priECFName) {
		this.priECFName = priECFName;
	}

	public String getSecCCFName() {
		return secCCFName;
	}

	public void setSecCCFName(String secCCFName) {
		this.secCCFName = secCCFName;
	}

	public String getSecECFName() {
		return secECFName;
	}

	public void setSecECFName(String secECFName) {
		this.secECFName = secECFName;
	}

}
