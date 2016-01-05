package net.x_talker.as.im.facility.log;

import org.apache.log4j.Logger;

/**
 * 日志记录工具抽象类
 * 
 * @author zengqiaowen
 *
 */
public abstract class Facility {

	protected Logger logger;
	protected String name;
	protected boolean enable = true;

	public Facility(String name) {
		this.name = name;
		initLog();
	}

	public static Facility getFacility(FacilityEnum facilityEnum, String name) {
		switch (facilityEnum) {
		case DIAGNOZE:
			return new DiagnozeFacility(name);
		case PMANALYSIS:
			return new PMAnalysisFacility(name);
		default:
			return null;
		}

	}

	public abstract void initLog();

	public void log(Object message) {
		if (enable)
			logger.info(message);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
