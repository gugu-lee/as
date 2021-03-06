package net.x_talker.as.sh.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.*; // The main SAX package
import org.xml.sax.helpers.*; // SAX helper classes

public class ShDataParser extends DefaultHandler {

	// private XMLReader parser;
	private InputSource inputSource;

	private StringBuffer accumulator;
	// private String element;

	private ShDataElement shData = null;
	private PublicIdentityElement publicIdentity = null;
	private RepositoryDataElement repositoryData = null;
	private AliasesRepositoryDataElement aliasesRepData = null;
	private ShIMSDataElement shIMSData = null;
	private CSLocationInformationElement csLocationInformation = null;
	private PSLocationInformationElement psLocationInformation = null;
	private ShDataExtensionElement shDataExtension = null;
	private ChargingInformationElement chgInformation = null;

	private InitialFilterCriteriaElement ifc = null;
	private TriggerPointElement tp = null;
	private ApplicationServerElement as = null;
	private SPTElement spt = null;
	private DSAIElement dsai = null;

	// flags
	private boolean registeredIdentities = false;
	private boolean implicitIdentities = false;
	private boolean allIdentities = false;
	private boolean aliasIdentities = false;
	private boolean sipHeader = false;
	private boolean sdp = false;

	public ShDataParser(InputSource inputSource) {
		this.inputSource = inputSource;
		initParser();
	}

	public ShDataElement getShData() {
		return shData;
	}

	public void setShData(ShDataElement shData) {
		this.shData = shData;
	}

	public void initParser() {

		try {
			org.xml.sax.XMLReader parser = new org.apache.xerces.parsers.SAXParser();

			// Specify that we don't want validation. This is the SAX2
			// API for requesting parser features.

			parser.setFeature("http://xml.org/sax/features/validation", false);

			// handlers
			parser.setContentHandler(this);
			parser.setErrorHandler(this);

			// Then tell the parser to parse input from that source
			parser.parse(inputSource);
		} catch (IOException e) {
			e.printStackTrace();
			shData = null;
		} catch (SAXException e) {
			e.printStackTrace();
			shData = null;
		} catch (Exception e) {
			e.printStackTrace();
			shData = null;
		}

	}

	// Called at the beginning of parsing. We use it as an init() method
	public void startDocument() {
		accumulator = new StringBuffer();
	}

	// When the parser encounters plain text (not XML elements), it calls
	// this method, which accumulates them in a string buffer.
	// Note that this method may be called multiple times, even with no
	// intervening elements.
	public void characters(char[] buffer, int start, int length) {
		accumulator.append(buffer, start, length);
	}

	// At the beginning of each new element, erase any accumulated text.

	public void startElement(String namespaceURL, String localName, String qname, Attributes attr) {
		accumulator.setLength(0);
		if (localName.equalsIgnoreCase(ShDataTags.ShData)) {
			shData = new ShDataElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.PublicIdentifiers)) {
			publicIdentity = new PublicIdentityElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.RepositoryData)) {
			repositoryData = new RepositoryDataElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.AliasesRepositoryData)) {
			aliasesRepData = new AliasesRepositoryDataElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.Sh_IMS_Data)) {
			shIMSData = new ShIMSDataElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.CSLocationInformation)) {
			csLocationInformation = new CSLocationInformationElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.PSLocationInformation)) {
			psLocationInformation = new PSLocationInformationElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.ShDataExtension)) {
			shDataExtension = new ShDataExtensionElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.RegisteredIdentities)) {
			registeredIdentities = true;
		} else if (localName.equalsIgnoreCase(ShDataTags.ImplicitIdentities)) {
			implicitIdentities = true;
		} else if (localName.equalsIgnoreCase(ShDataTags.AllIdentities)) {
			allIdentities = true;
		} else if (localName.equalsIgnoreCase(ShDataTags.AliasIdentities)) {
			aliasIdentities = true;
		} else if (localName.equalsIgnoreCase(ShDataTags.IFCs)) {
		} else if (localName.equalsIgnoreCase(ShDataTags.ChargingInformation)) {
			chgInformation = new ChargingInformationElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.ApplicationServer)) {
			as = new ApplicationServerElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.InitialFilterCriteria)) {
			ifc = new InitialFilterCriteriaElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.TriggerPoint)) {
			tp = new TriggerPointElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.SPT)) {
			spt = new SPTElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.DSAI)) {
			dsai = new DSAIElement();
		} else if (localName.equalsIgnoreCase(ShDataTags.SIPHeader)) {
			sipHeader = true;
		} else if (localName.equalsIgnoreCase(ShDataTags.SessionDescription)) {
			sdp = true;
		}
	}

	// Take special action when we reach the end of selected elements.
	// Although we don't use a validating parser, this method does assume
	// that the web.xml file we're parsing is valid.

	public void endElement(String namespaceURL, String localName, String qname) {

		String elementContent = accumulator.toString().trim();

		if (localName.equals(ShDataTags.ShData)) {
		} else if (localName.equals(ShDataTags.PublicIdentifiers)) {
			if (shDataExtension != null && registeredIdentities) {
				shDataExtension.setRegisteredIdentities(publicIdentity);
				registeredIdentities = false;
			} else if (shDataExtension != null && implicitIdentities) {
				shDataExtension.setImplicitIdentities(publicIdentity);
				implicitIdentities = false;
			} else if (shDataExtension != null && allIdentities) {
				shDataExtension.setAllIdentities(publicIdentity);
				allIdentities = false;
			} else if (shDataExtension != null && aliasIdentities) {
				shDataExtension.setAliasIdentities(publicIdentity);
				aliasIdentities = false;
			} else {
				shData.setPublicIdentifiers(publicIdentity);
			}
			publicIdentity = null;
		} else if (localName.equals(ShDataTags.RepositoryData)) {
			shData.addRepositoryData(repositoryData);
			repositoryData = null;
		} else if (localName.equals(ShDataTags.AliasesRepositoryData)) {
			shDataExtension.addAliasesRepositoryData(aliasesRepData);
			aliasesRepData = null;
		} else if (localName.equals(ShDataTags.Sh_IMS_Data)) {
			shData.setShIMSData(shIMSData);
			shIMSData = null;
		} else if (localName.equals(ShDataTags.CSLocationInformation)) {
			shData.setCsLocationInformation(csLocationInformation);
			csLocationInformation = null;
		} else if (localName.equals(ShDataTags.PSLocationInformation)) {
			shData.setPsLocationInformation(psLocationInformation);
			psLocationInformation = null;
		} else if (localName.equals(ShDataTags.CSUserState)) {
			shData.setCsUserState(Integer.parseInt(elementContent));
		} else if (localName.equals(ShDataTags.PSUserState)) {
			shData.setPsUserState(Integer.parseInt(elementContent));
		} else if (localName.equals(ShDataTags.ShDataExtension)) {
			shData.setShDataExtension(shDataExtension);
			shDataExtension = null;
		} else if (localName.equalsIgnoreCase(ShDataTags.IMSPublicIdentity)) {
			publicIdentity.addPublicIdentity(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.MSISDN)) {
			publicIdentity.addMSISDN(accumulator.toString().trim());
		} else if (localName.equalsIgnoreCase(ShDataTags.IdentityType)) {
			publicIdentity.setIdentityType(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.WildcardedPSI)) {
			publicIdentity.setWildcardedPSI(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.ServiceIndication)) {
			if (repositoryData != null) {
				repositoryData.setServiceIndication(elementContent);
			} else if (aliasesRepData != null) {
				aliasesRepData.setServiceIndication(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.SequenceNumber)) {
			if (repositoryData != null) {
				repositoryData.setSqn(Integer.parseInt(elementContent));
			} else if (aliasesRepData != null) {
				aliasesRepData.setSqn(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.ServiceData)) {
			if (repositoryData != null) {
				repositoryData.setServiceData(elementContent);
			} else if (aliasesRepData != null) {
				aliasesRepData.setServiceData(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.IFCs)) {
		} else if (localName.equalsIgnoreCase(ShDataTags.SCSCFName)) {
			shIMSData.setScscfName(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.IMSUserState)) {
			shIMSData.setImsUserState(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.ChargingInformation)) {
			shIMSData.setChgInformation(chgInformation);
			chgInformation = null;
		} else if (localName.equalsIgnoreCase(ShDataTags.PrimaryEventChargingFunctionName)) {
			if (chgInformation != null) {
				chgInformation.setPriECFName(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.SecondaryEventChargingFunctionName)) {
			if (chgInformation != null) {
				chgInformation.setSecECFName(elementContent);
			}

		} else if (localName.equalsIgnoreCase(ShDataTags.PrimaryChargingCollectionFunctionName)) {
			if (chgInformation != null) {
				chgInformation.setPriCCFName(elementContent);
			}

		} else if (localName.equalsIgnoreCase(ShDataTags.SecondaryChargingCollectionFunctionName)) {
			if (chgInformation != null) {
				chgInformation.setSecCCFName(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.InitialFilterCriteria)) {
			if (ifc != null) {
				shIMSData.addInitialFilterCriteria(ifc);
				ifc = null;
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.PSIActivation)) {
			if (shIMSData != null) {
				shIMSData.setPsiActivation(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.DSAI)) {
			if (shIMSData != null && dsai != null) {
				shIMSData.addDSAI(dsai);
				dsai = null;
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.DSAI_Tag)) {
			if (dsai != null) {
				dsai.setTag(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.DSAI_Value)) {
			if (dsai != null) {
				dsai.setValue(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.ServerName)) {
			if (as != null) {
				as.setServerName(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.DefaultHandling)) {
			if (as != null) {
				as.setDefaultHandling(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.ServiceInfo)) {
			if (as != null) {
				as.setServiceInfo(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.ApplicationServer)) {
			if (ifc != null) {
				ifc.setApplicationServer(as);
			}
			as = null;
		} else if (localName.equalsIgnoreCase(ShDataTags.Header)) {
			spt.setSipHeader(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.Content)) {
			if (sipHeader) {
				spt.setSipHeaderContent(elementContent);
				sipHeader = false;
			} else if (sdp) {
				spt.setSessionDescContent(elementContent);
				sdp = false;
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.SDPLine)) {
			spt.setSessionDescLine(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.ConditionNegated)) {
			spt.setConditionNegated(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.Group)) {
			spt.setGroupID(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.RequestURI)) {
			spt.setRequestURI(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.Method)) {
			spt.setMethod(elementContent);
		} else if (localName.equalsIgnoreCase(ShDataTags.SessionCase)) {
			spt.setSessionCase(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.RegistrationType)) {
			if (spt != null) {
				spt.addRegistrationType(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.SPT)) {
			if (tp != null) {
				tp.addSPT(spt);
			}
			spt = null;
		} else if (localName.equalsIgnoreCase(ShDataTags.ConditionTypeCNF)) {
			tp.setConditionTypeCNF(Integer.parseInt(elementContent));
		} else if (localName.equalsIgnoreCase(ShDataTags.TriggerPoint)) {
			if (ifc != null) {
				ifc.setTriggerPoint(tp);
			}
			tp = null;
		} else if (localName.equalsIgnoreCase(ShDataTags.ProfilePartIndicator)) {
			if (ifc != null) {
				ifc.setProfilePartIndicator(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.LocationNumber)) {
			if (csLocationInformation != null) {
				csLocationInformation.setLocationNumber(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.CellGlobalId)) {
			if (csLocationInformation != null) {
				csLocationInformation.setCellGlobalID(elementContent);
			} else if (psLocationInformation != null) {
				psLocationInformation.setCellGlobalID(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.ServiceAreaId)) {
			if (csLocationInformation != null) {
				csLocationInformation.setServiceAreaID(elementContent);
			} else if (psLocationInformation != null) {
				psLocationInformation.setServiceAreaID(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.LocationAreaId)) {
			if (csLocationInformation != null) {
				csLocationInformation.setLocationAreaID(elementContent);
			} else if (psLocationInformation != null) {
				psLocationInformation.setLocationAreaID(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.GeographicalInformation)) {
			if (csLocationInformation != null) {
				csLocationInformation.setGeographicalInformation(elementContent);
			} else if (psLocationInformation != null) {
				psLocationInformation.setGeographicalInformation(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.GeodeticInformation)) {
			if (csLocationInformation != null) {
				csLocationInformation.setGeodeticInformation(elementContent);
			} else if (psLocationInformation != null) {
				psLocationInformation.setGeodeticInformation(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.VLRNumber)) {
			if (csLocationInformation != null) {
				csLocationInformation.setVlrNumber(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.MSCNumber)) {
			if (csLocationInformation != null) {
				csLocationInformation.setMscnNumber(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.CurrentLocationRetrieved)) {
			if (csLocationInformation != null) {
				csLocationInformation.setCurrentLocationRetrieved(Integer.parseInt(elementContent));
			} else if (psLocationInformation != null) {
				psLocationInformation.setCurrentLocationRetrieved(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.AgeOfLocationInformation)) {
			if (csLocationInformation != null) {
				csLocationInformation.setAgeOfLocationInformation(Integer.parseInt(elementContent));
			} else if (psLocationInformation != null) {
				psLocationInformation.setAgeOfLocationInformation(Integer.parseInt(elementContent));
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.RoutingAreaId)) {
			if (psLocationInformation != null) {
				psLocationInformation.setRoutingAreaID(elementContent);
			}
		} else if (localName.equalsIgnoreCase(ShDataTags.SGSNNumber)) {
			if (psLocationInformation != null) {
				psLocationInformation.setSgsnNumber(elementContent);
			}
		}
	}

	// Called at the end of parsing. Used here to print our results.
	public void endDocument() {
	}

	// Issue a warning
	public void warning(SAXParseException exception) {
		System.err.println("WARNING: line " + exception.getLineNumber() + ": " + exception.getMessage());
	}

	// Report a parsing error
	public void error(SAXParseException exception) {
		System.err.println("ERROR: line " + exception.getLineNumber() + ": " + exception.getMessage());
	}

	// Report a non-recoverable error and exit
	public void fatalError(SAXParseException exception) throws SAXException {
		System.err.println("FATAL: line " + exception.getLineNumber() + ": " + exception.getMessage());
		throw (exception);
	}

	public static void main(String args[]) {

		InputSource input;// = new InputSource(new
							// FileReader("files//online1.xml"));

		String inputString =
		/*
		 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		 * "<Sh-Data xmlns=\"urn:ietf:params:xml:ns:pidf\" >" +
		 * "<PublicIdentifiers>" + "<IMSPublicIdentity>" + "<PublicIdentity>" +
		 * "sip:alice@open-ims.test" + "</PublicIdentity>" +
		 * "</IMSPublicIdentity>" + "<IMSPublicIdentity>" + "<PublicIdentity>" +
		 * "sip:alice2@open-ims.test" + "</PublicIdentity>" +
		 * "</IMSPublicIdentity>" + "<MSISDN>" + "msisdn_id" + "</MSISDN>" +
		 * "<MSISDN>" + "msisdn_id2" + "</MSISDN>" + "<IdentityType>" + "1" +
		 * "</IdentityType>" + "<WildcardedPSI>" + "wildcarded-psi" +
		 * "</WildcardedPSI>" + "</PublicIdentifiers>" +
		 * 
		 * "</Sh-Data>";
		 */

		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Sh-Data>" + "<RepositoryData>"
				+ "<ServiceIndication>CLIR</ServiceIndication>" + "<SequenceNumber>0</SequenceNumber>"
				+ "</RepositoryData>" + "</Sh-Data>";

		input = new InputSource(new ByteArrayInputStream(inputString.getBytes()));
		ShDataParser parser = new ShDataParser(input);

		ShDataElement shData = parser.getShData();
		if (shData != null)
			System.out.println(shData.toString());

		/*
		 * catch(IOException e){ e.printStackTrace(); }
		 */
	}

}
