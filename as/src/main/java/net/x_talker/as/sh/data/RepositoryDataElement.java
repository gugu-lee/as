package net.x_talker.as.sh.data;

public class RepositoryDataElement {
	private String serviceIndication = null;
	private int sqn = 0;
	/** can have any value; tag used is Service-Data */
	private String serviceData = null;

	public RepositoryDataElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.RepositoryData_s);
		if (serviceIndication != null) {
			sBuffer.append(ShDataTags.ServiceIndication_s);
			sBuffer.append(serviceIndication);
			sBuffer.append(ShDataTags.ServiceIndication_e);
		}

		if (sqn >= 0) {
			sBuffer.append(ShDataTags.SequenceNumber_s);
			sBuffer.append(sqn);
			sBuffer.append(ShDataTags.SequenceNumber_e);
		}

		if (serviceData != null) {
			sBuffer.append(ShDataTags.ServiceData_s);
			sBuffer.append(serviceData);
			sBuffer.append(ShDataTags.ServiceData_e);
		}

		sBuffer.append(ShDataTags.RepositoryData_e);
		return sBuffer.toString();
	}

	public String getServiceData() {
		return serviceData;
	}

	public void setServiceData(String serviceData) {
		this.serviceData = serviceData;
	}

	public String getServiceIndication() {
		return serviceIndication;
	}

	public void setServiceIndication(String serviceIndication) {
		this.serviceIndication = serviceIndication;
	}

	public int getSqn() {
		return sqn;
	}

	public void setSqn(int sqn) {
		this.sqn = sqn;
	}

}
