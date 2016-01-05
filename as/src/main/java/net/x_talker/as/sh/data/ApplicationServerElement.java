package net.x_talker.as.sh.data;

public class ApplicationServerElement {
	private String serverName = null;
	private int defaultHandling = -1;
	private String serviceInfo = null;

	public ApplicationServerElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append(ShDataTags.ApplicationServer_s);
		if (serverName != null) {
			sBuffer.append(ShDataTags.ServerName_s);
			sBuffer.append(serverName);
			sBuffer.append(ShDataTags.ServerName_e);
		}

		if (defaultHandling > -1) {
			sBuffer.append(ShDataTags.DefaultHandling_s);
			sBuffer.append(defaultHandling);
			sBuffer.append(ShDataTags.Defaulthandling_e);
		}

		if (serviceInfo != null) {
			sBuffer.append(ShDataTags.ServiceInfo_s);
			sBuffer.append(serviceInfo);
			sBuffer.append(ShDataTags.ServiceInfo_e);
		}

		sBuffer.append(ShDataTags.ApplicationServer_e);
		return sBuffer.toString();
	}

	public int getDefaultHandling() {
		return defaultHandling;
	}

	public void setDefaultHandling(int defaultHandling) {
		this.defaultHandling = defaultHandling;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(String serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
}
