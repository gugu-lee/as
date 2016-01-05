package net.x_talker.as.sh;

public class ShConstants {

	// Data-Reference Constants
	public static final int Data_Ref_Repository_Data = 0;
	public static final int Data_Ref_IMS_Public_Identity = 10;
	public static final int Data_Ref_IMS_User_State = 11;
	public static final int Data_Ref_SCSCF_Name = 12;
	public static final int Data_Ref_iFC = 13;
	public static final int Data_Ref_Location_Info = 14;
	public static final int Data_Ref_User_State = 15;
	public static final int Data_Ref_Charging_Info = 16;
	public static final int Data_Ref_MSISDN = 17;
	public static final int Data_Ref_PSI_Activation = 18;
	public static final int Data_Ref_DSAI = 19;
	public static final int Data_Ref_Aliases_Repository_Data = 20;

	// Send-Data-Indication
	public static final int User_Data_Not_Requested = 0;
	public static final int User_Data_Requested = 1;

	// Subs-Req-Type
	public static final int Subs_Req_Type_Subscribe = 0;
	public static final int Subs_Req_Type_UnSubscribe = 1;

	// Identity-Set
	public static final int Identity_Set_All_Identities = 0;
	public static final int Identity_Set_Registered_Identities = 1;
	public static final int Identity_Set_Implicit_Identities = 2;
	public static final int Identity_Set_Alias_Identities = 3;

	// DSAI_value
	public static final String DSAI_value_Inactive_Name = "Inactive";
	public static final String DSAI_value_Active_Name = "Active";
	public static final int DSAI_value_Inactive = 1;
	public static final int DSAI_value_Active = 0;

	// Current-Location AVP
	public static final int Initiate_Active_Location_Retrieval = 1;
	public static final int Do_Not_Need_Initiate_Active_Location_Retrieval = 0;

	// IMPU_user_state
	public static final short IMPU_user_state_Not_Registered = 0;
	public static final short IMPU_user_state_Registered = 1;
	public static final short IMPU_user_state_Unregistered = 2;
	public static final short IMPU_user_state_Auth_Pending = 3;

}
