package net.x_talker.as.im.facility.log;

public enum FacilityEnum {

	DIAGNOZE {
		@Override
		public String getClassName() {
			return "com.x_talker.ims.as.im.facility.log.DiagnozeFacility";
		}
	},
	PMANALYSIS {
		@Override
		public String getClassName() {
			// TODO Auto-generated method stub
			return appender;
		}
	};

	public abstract String getClassName();

	protected String appender;

}
