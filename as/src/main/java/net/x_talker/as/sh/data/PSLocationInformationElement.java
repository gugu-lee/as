package net.x_talker.as.sh.data;

public class PSLocationInformationElement {

	private String cellGlobalID = null;
	private String serviceAreaID = null;
	private String locationAreaID = null;
	private String routingAreaID = null;
	private String geographicalInformation = null;
	private String geodeticInformation = null;
	private String sgsnNumber = null;
	private int currentLocationRetrieved = -1;
	private int ageOfLocationInformation = -1;

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.PSLocationInformation_s);

		if (cellGlobalID != null) {
			sBuffer.append(ShDataTags.CellGlobalId_s);
			sBuffer.append(cellGlobalID);
			sBuffer.append(ShDataTags.CellGlobalId_e);
		}
		if (serviceAreaID != null) {
			sBuffer.append(ShDataTags.ServiceAreaId_s);
			sBuffer.append(serviceAreaID);
			sBuffer.append(ShDataTags.ServiceAreaId_e);
		}
		if (locationAreaID != null) {
			sBuffer.append(ShDataTags.LocationAreaId_s);
			sBuffer.append(locationAreaID);
			sBuffer.append(ShDataTags.LocationAreaId_e);
		}
		if (routingAreaID != null) {
			sBuffer.append(ShDataTags.RoutingAreaId_s);
			sBuffer.append(routingAreaID);
			sBuffer.append(ShDataTags.RoutingAreaId_e);
		}

		if (geographicalInformation != null) {
			sBuffer.append(ShDataTags.GeographicalInformation_s);
			sBuffer.append(geographicalInformation);
			sBuffer.append(ShDataTags.GeographicalInformation_e);
		}

		if (geodeticInformation != null) {
			sBuffer.append(ShDataTags.GeodeticInformation_s);
			sBuffer.append(geodeticInformation);
			sBuffer.append(ShDataTags.GeodeticInformation_e);
		}

		if (sgsnNumber != null) {
			sBuffer.append(ShDataTags.SGSNNumber_s);
			sBuffer.append(sgsnNumber);
			sBuffer.append(ShDataTags.SGSNNumber_e);
		}

		if (currentLocationRetrieved != -1) {
			sBuffer.append(ShDataTags.CurrentLocationRetrieved_s);
			sBuffer.append(currentLocationRetrieved);
			sBuffer.append(ShDataTags.CurrentLocationRetrieved_e);
		}

		if (ageOfLocationInformation != -1) {
			sBuffer.append(ShDataTags.AgeOfLocationInformation_s);
			sBuffer.append(ageOfLocationInformation);
			sBuffer.append(ShDataTags.AgeOfLocationInformation_e);
		}
		sBuffer.append(ShDataTags.PSLocationInformation_e);
		return sBuffer.toString();
	}

	public int getAgeOfLocationInformation() {
		return ageOfLocationInformation;
	}

	public void setAgeOfLocationInformation(int ageOfLocationInformation) {
		this.ageOfLocationInformation = ageOfLocationInformation;
	}

	public String getCellGlobalID() {
		return cellGlobalID;
	}

	public void setCellGlobalID(String cellGlobalID) {
		this.cellGlobalID = cellGlobalID;
	}

	public int getCurrentLocationRetrieved() {
		return currentLocationRetrieved;
	}

	public void setCurrentLocationRetrieved(int currentLocationRetrieved) {
		this.currentLocationRetrieved = currentLocationRetrieved;
	}

	public String getGeodeticInformation() {
		return geodeticInformation;
	}

	public void setGeodeticInformation(String geodeticInformation) {
		this.geodeticInformation = geodeticInformation;
	}

	public String getGeographicalInformation() {
		return geographicalInformation;
	}

	public void setGeographicalInformation(String geographicalInformation) {
		this.geographicalInformation = geographicalInformation;
	}

	public String getLocationAreaID() {
		return locationAreaID;
	}

	public void setLocationAreaID(String locationAreaID) {
		this.locationAreaID = locationAreaID;
	}

	public String getRoutingAreaID() {
		return routingAreaID;
	}

	public void setRoutingAreaID(String routingAreaID) {
		this.routingAreaID = routingAreaID;
	}

	public String getServiceAreaID() {
		return serviceAreaID;
	}

	public void setServiceAreaID(String serviceAreaID) {
		this.serviceAreaID = serviceAreaID;
	}

	public String getSgsnNumber() {
		return sgsnNumber;
	}

	public void setSgsnNumber(String sgsnNumber) {
		this.sgsnNumber = sgsnNumber;
	}

}
