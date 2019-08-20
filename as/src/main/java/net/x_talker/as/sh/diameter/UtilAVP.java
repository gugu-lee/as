package net.x_talker.as.sh.diameter;

import java.util.Iterator;
import java.util.Vector;

import org.freeims.diameterpeer.data.AVP;
import org.freeims.diameterpeer.data.AVPDecodeException;
import org.freeims.diameterpeer.data.DiameterMessage;

public class UtilAVP {

	public static String getPublicIdentity(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY, true, DiameterConstants.Vendor.V3GPP);

		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static AVP getNextPublicIdentityAVP(DiameterMessage message, AVP last_avp) {
		Iterator it;
		if (message.avps != null) {
			it = message.avps.iterator();
			while (it.hasNext()) {
				AVP crt_avp = (AVP) it.next();
				if (crt_avp != null && crt_avp.code == DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY) {
					if (crt_avp.equals(last_avp))
						continue;
					else {
						return crt_avp;
					}
				}
			}
		}

		return null;
	}

	public static String getUserName(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.USER_NAME, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static int getUserAuthorizationType(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_AUTHORIZATION_TYPE, true,
				DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return avp.getIntData();
		}
		return 0;
	}

	public static String getVisitedNetwork(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_VISITED_NETWORK_IDENTIFIER, true,
				DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static AVP getSipAuthDataItem(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_AUTH_DATA_ITEM, true,
				DiameterConstants.Vendor.V3GPP);
		return avp;
	}

	public static String getServerName(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SERVER_NAME, true, DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static int getSipNumberAuthItems(DiameterMessage message) {

		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SIP_NUMBER_AUTH_ITEMS, true,
				DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return avp.int_data;
		}
		return -1;
	}

	public static String getOriginatingHost(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.ORIGIN_HOST, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static String getOriginatingRealm(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.ORIGIN_REALM, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static String getDestinationHost(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.DESTINATION_HOST, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static String getDestinationRealm(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.DESTINATION_REALM, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static String getVendorSpecificApplicationID(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true,
				DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static String getAuthSessionState(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.AUTH_SESSION_STATE, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static void addResultCode(DiameterMessage message, int resultCode) {
		AVP avp = new AVP(DiameterConstants.AVPCode.RESULT_CODE, true, DiameterConstants.Vendor.DIAM);
		avp.setData(resultCode);
		message.addAVP(avp);
	}

	public static int getResultCode(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.RESULT_CODE, true, DiameterConstants.Vendor.DIAM);
		if (avp == null)
			return -1;

		return avp.int_data;
	}

	public static void addExperimentalResultCode(DiameterMessage message, int resultCode, int vendor) {
		AVP group = new AVP(DiameterConstants.AVPCode.EXPERIMENTAL_RESULT, true, DiameterConstants.Vendor.DIAM);
		AVP vendorID = new AVP(DiameterConstants.AVPCode.VENDOR_ID, true, DiameterConstants.Vendor.DIAM);
		vendorID.setData(vendor);
		group.addChildAVP(vendorID);

		AVP expResult = new AVP(DiameterConstants.AVPCode.EXPERIMENTAL_RESULT_CODE, true,
				DiameterConstants.Vendor.DIAM);
		expResult.setData(resultCode);
		group.addChildAVP(expResult);
		message.addAVP(group);
	}

	public static int getExperimentalResultCode(DiameterMessage message) {
		AVP group = message.findAVP(DiameterConstants.AVPCode.EXPERIMENTAL_RESULT, true, DiameterConstants.Vendor.DIAM);
		if (group == null || group.childs == null || group.childs.size() == 0)
			return -1;

		for (AVP avp : (Vector<AVP>) group.childs) {
			if (avp.code == DiameterConstants.AVPCode.EXPERIMENTAL_RESULT_CODE && avp.flag_mandatory == true
					&& avp.vendor_id == DiameterConstants.Vendor.DIAM)
				return avp.int_data;
		}
		return -1;
	}

	public static void addServerName(DiameterMessage message, String serverName) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SERVER_NAME, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(serverName);
		message.addAVP(avp);
	}

	public static void addVendorSpecificApplicationID(DiameterMessage message, int vendorID, int appID) {
		AVP vendorAppID_AVP = new AVP(DiameterConstants.AVPCode.VENDOR_SPECIFIC_APPLICATION_ID, true,
				DiameterConstants.Vendor.DIAM);

		AVP vendorID_AVP = new AVP(DiameterConstants.AVPCode.VENDOR_ID, true, DiameterConstants.Vendor.DIAM);
		vendorID_AVP.setData(vendorID);
		vendorAppID_AVP.addChildAVP(vendorID_AVP);

		AVP appID_AVP = new AVP(DiameterConstants.AVPCode.AUTH_APPLICATION_ID, true, DiameterConstants.Vendor.DIAM);
		appID_AVP.setData(appID);
		vendorAppID_AVP.addChildAVP(appID_AVP);

		message.addAVP(vendorAppID_AVP);
	}

	public static void addAuthSessionState(DiameterMessage message, int state) {
		AVP authSession = new AVP(DiameterConstants.AVPCode.AUTH_SESSION_STATE, true, DiameterConstants.Vendor.DIAM);
		authSession.setData(state);
		message.addAVP(authSession);
	}

	public static void addPublicIdentity(DiameterMessage message, String publicIdentity) {
		AVP publicIdentityAVP = new AVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY, true,
				DiameterConstants.Vendor.V3GPP);
		publicIdentityAVP.setData(publicIdentity);
		message.addAVP(publicIdentityAVP);
	}

	public static void addUserName(DiameterMessage message, String userName) {
		AVP userNameAVP = new AVP(DiameterConstants.AVPCode.USER_NAME, true, DiameterConstants.Vendor.DIAM);
		userNameAVP.setData(userName);
		message.addAVP(userNameAVP);
	}

	public static void addUserData(DiameterMessage message, String data) {
		AVP userData = new AVP(DiameterConstants.AVPCode.IMS_USER_DATA, true, DiameterConstants.Vendor.V3GPP);
		userData.setData(data);
		message.addAVP(userData);
	}

	public static void addShData(DiameterMessage message, String data) {
		AVP userData = new AVP(DiameterConstants.AVPCode.IMS_USER_DATA_SH, true, DiameterConstants.Vendor.V3GPP);
		userData.setData(data);
		message.addAVP(userData);
	}

	public static void addDestinationHost(DiameterMessage message, String host) {
		AVP destHostAVP = new AVP(DiameterConstants.AVPCode.DESTINATION_HOST, true, DiameterConstants.Vendor.DIAM);
		destHostAVP.setData(host);
		message.addAVP(destHostAVP);
	}

	public static void addDestinationRealm(DiameterMessage message, String realm) {
		AVP destRealm = new AVP(DiameterConstants.AVPCode.DESTINATION_REALM, true, DiameterConstants.Vendor.DIAM);
		destRealm.setData(realm);
		message.addAVP(destRealm);
	}

	public static void addOriginRealm(DiameterMessage message, String realm) {
		AVP originRealm = new AVP(DiameterConstants.AVPCode.ORIGIN_REALM, true, DiameterConstants.Vendor.DIAM);
		originRealm.setData(realm);
		message.addAVP(originRealm);
	}

	public static void addOriginHost(DiameterMessage message, String host) {
		AVP originRealm = new AVP(DiameterConstants.AVPCode.ORIGIN_HOST, true, DiameterConstants.Vendor.DIAM);
		originRealm.setData(host);
		message.addAVP(originRealm);
	}

	public static void addSIPNumberAuthItems(DiameterMessage message, int cnt) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SIP_NUMBER_AUTH_ITEMS, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(cnt);
		message.addAVP(avp);
	}

	public static void addSessionID(DiameterMessage message, String FQDN, int sessionID) {
		AVP avp = new AVP(DiameterConstants.AVPCode.SESSION_ID, true, DiameterConstants.Vendor.DIAM);
		StringBuffer sBuff = new StringBuffer();
		sBuff.append(FQDN);
		sBuff.append(";");
		sBuff.append(System.currentTimeMillis());
		sBuff.append(";");
		sBuff.append(sessionID);
		avp.setData(sBuff.toString());
		message.addAVP(avp);
	}

	public static String getSessionID(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.SESSION_ID, true, DiameterConstants.Vendor.DIAM);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	// methods for fields used by Sh Interface

	public static Vector getAllDataReference(DiameterMessage message) {

		Vector result = null;
		if (message.avps != null && message.avps.size() > 0) {
			Iterator it = message.avps.iterator();
			AVP avp;
			while (it.hasNext()) {
				avp = (AVP) it.next();
				if (avp.code == DiameterConstants.AVPCode.IMS_DATA_REFERENCE && avp.flag_mandatory == true
						&& avp.vendor_id == DiameterConstants.Vendor.V3GPP) {
					if (result == null) {
						result = new Vector();
					}
					result.add(new Integer(avp.int_data));
				}
			}
		}
		return result;
	}

	public static int getDataReference(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_DATA_REFERENCE, true, DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return avp.int_data;
		}

		return -1;
	}

	public static String getShUserIdentity(DiameterMessage message) {
		// return the Public Identity or the MSISDN AVP
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_IDENTITY, true, DiameterConstants.Vendor.V3GPP);

		if (avp != null) {
			try {
				avp.ungroup();
				if (avp.childs != null && avp.childs.size() > 0) {
					AVP child_avp = (AVP) avp.childs.get(0);
					return new String(child_avp.data);
				}
			} catch (AVPDecodeException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getServiceIndication(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SERVICE_INDICATION, true,
				DiameterConstants.Vendor.V3GPP);
		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static Vector getAllServiceIndication(DiameterMessage message) {
		Vector result = null;

		if (message.avps != null && message.avps.size() > 0) {
			Iterator it = message.avps.iterator();
			AVP avp;
			while (it.hasNext()) {
				avp = (AVP) it.next();
				if (avp.code == DiameterConstants.AVPCode.IMS_SERVICE_INDICATION && avp.flag_mandatory == true
						&& avp.vendor_id == DiameterConstants.Vendor.V3GPP) {
					if (result == null) {
						result = new Vector();
					}
					result.add(new String(avp.data));
				}
			}
		}

		return result;
	}

	public static String getShUserData(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_USER_DATA_SH, true, DiameterConstants.Vendor.V3GPP);

		if (avp != null) {
			return new String(avp.data);
		}
		return null;
	}

	public static int getSubsReqType(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SUBSCRIBTION_REQ_TYPE, true,
				DiameterConstants.Vendor.V3GPP);

		if (avp == null) {
			return -1;
		}

		return avp.int_data;
	}

	public static int getExpiryTime(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_EXPIRY_TIME, false, DiameterConstants.Vendor.V3GPP);
		if (avp == null) {
			return -1;
		}
		return avp.int_data;
	}

	public static int getSendDataIndication(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_SEND_DATA_INDICATION, false,
				DiameterConstants.Vendor.V3GPP);
		if (avp == null) {
			return -1;
		}
		return avp.int_data;
	}

	public static String getDSAITag(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_DSAI_TAG, true, DiameterConstants.Vendor.V3GPP);
		if (avp == null)
			return null;

		return new String(avp.data);
	}

	public static int getIdentitySet(DiameterMessage message) {
		AVP avp = message.findAVP(DiameterConstants.AVPCode.IMS_IDENTITY_SET, false, DiameterConstants.Vendor.V3GPP);
		if (avp == null)
			return -1;

		return avp.int_data;
	}

	// add methods for Sh
	public static void addDataReference(DiameterMessage message, int dataReference) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_DATA_REFERENCE, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(dataReference);
		message.addAVP(avp);
	}

	public static void addSubsReqType(DiameterMessage message, int subsReqType) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SUBSCRIBTION_REQ_TYPE, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(subsReqType);
		message.addAVP(avp);
	}

	public static void addCurrentLocation(DiameterMessage message, int currentLocation) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_CURRENT_LOCATION, false, DiameterConstants.Vendor.V3GPP);
		avp.setData(currentLocation);
		message.addAVP(avp);
	}

	public static void addApplicationServerIdentity(DiameterMessage message, String applicationServerIdentity) {
		AVP avp = new AVP(DiameterConstants.AVPCode.AUTH_APPLICATION_ID, false, DiameterConstants.Vendor.V3GPP);
		avp.setData(applicationServerIdentity);
		message.addAVP(avp);
	}

	public static void addIdentitySet(DiameterMessage message, int identitySet) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_IDENTITY_SET, false, DiameterConstants.Vendor.V3GPP);
		avp.setData(identitySet);
		message.addAVP(avp);
	}

	public static void addServiceIndication(DiameterMessage message, String serviceIndication) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_SERVICE_INDICATION, true, DiameterConstants.Vendor.V3GPP);
		avp.setData(serviceIndication);
		message.addAVP(avp);
	}

	public static void addExpiryTime(DiameterMessage message, int expiry_time) {
		AVP avp = new AVP(DiameterConstants.AVPCode.IMS_EXPIRY_TIME, false, DiameterConstants.Vendor.V3GPP);
		avp.setData(expiry_time);
		message.addAVP(avp);
	}

	public static void addShUserIdentity(DiameterMessage message, String userIdentity) {
		AVP shUserIdentity = new AVP(DiameterConstants.AVPCode.IMS_USER_IDENTITY, true, DiameterConstants.Vendor.V3GPP);
		AVP publicIdentity = new AVP(DiameterConstants.AVPCode.IMS_PUBLIC_IDENTITY_SH, true,
				DiameterConstants.Vendor.V3GPP);
		publicIdentity.setData(userIdentity);
		shUserIdentity.addChildAVP(publicIdentity);
		message.addAVP(shUserIdentity);
	}
}
