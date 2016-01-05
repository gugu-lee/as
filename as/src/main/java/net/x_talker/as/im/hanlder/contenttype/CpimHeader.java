package net.x_talker.as.im.hanlder.contenttype;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class CpimHeader {

	private String header;
	private String value;
	private Map<String, String> params;

	private Logger logger = Logger.getLogger(getClass());

	public CpimHeader(String headerStr) {
		params = new HashMap<String, String>();
		String[] paramStrs = headerStr.split(";");

		String[] headers = paramStrs[0].split(MessageCPIMType.CPIM_HEADER_VALUE_SPLITER);
		if (headers.length > 1) {
			this.header = headers[0];
			this.value = headers[1];

			for (int index = 1; index < paramStrs.length; index++) {
				String param = paramStrs[index];
				String[] strs = param.split("=");
				if (strs.length == 2) {
					params.put(strs[0], strs[1].replace("\"", ""));
				} else {
					logger.info("not a param,param str:" + param + ",header:" + headerStr);
				}
			}
		}

	}

	public CpimHeader(String headerName, String headerValueStr) {
		this(headerName + MessageCPIMType.CPIM_HEADER_VALUE_SPLITER + headerValueStr);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
