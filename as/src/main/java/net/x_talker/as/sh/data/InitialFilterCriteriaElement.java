package net.x_talker.as.sh.data;

public class InitialFilterCriteriaElement {
	private int priority = 1;
	private TriggerPointElement triggerPoint = null;
	private ApplicationServerElement applicationServer = null;
	private int profilePartIndicator = -1;

	public InitialFilterCriteriaElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.InitialFilterCriteria_s);

		sBuffer.append(ShDataTags.Priority_s);
		sBuffer.append(priority);
		sBuffer.append(ShDataTags.Priority_e);

		if (triggerPoint != null) {
			sBuffer.append(triggerPoint.toString());
		}
		if (applicationServer != null) {
			sBuffer.append(applicationServer.toString());
		}
		if (profilePartIndicator != -1) {
			sBuffer.append(ShDataTags.ProfilePartIndicator_s);
			sBuffer.append(profilePartIndicator);
			sBuffer.append(ShDataTags.ProfilePartIndicator_e);
		}

		sBuffer.append(ShDataTags.InitialFilterCriteria_e);
		return sBuffer.toString();
	}

	public ApplicationServerElement getApplicationServer() {
		return applicationServer;
	}

	public void setApplicationServer(ApplicationServerElement applicationServer) {
		this.applicationServer = applicationServer;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProfilePartIndicator() {
		return profilePartIndicator;
	}

	public void setProfilePartIndicator(int profilePartIndicator) {
		this.profilePartIndicator = profilePartIndicator;
	}

	public TriggerPointElement getTriggerPoint() {
		return triggerPoint;
	}

	public void setTriggerPoint(TriggerPointElement triggerPoint) {
		this.triggerPoint = triggerPoint;
	}

}
