package net.x_talker.as.im.util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;

import net.x_talker.as.common.util.Util;

public class PropertiesUtil {

	private Logger logger = Logger.getLogger(PropertiesUtil.class);

	private static PropertiesUtil instance = new PropertiesUtil();

	private Properties props;

	private PropertiesUtil() {
		init();
	}

	private void init() {
		props = new Properties();
		URL url = Loader.getResource("conf.properties");

		try {
			props.load(url.openStream());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static PropertiesUtil getInstance() {
		return instance;
	}

	public String getPropVal(String key) {
		return props.getProperty(key);
	}

	public Integer getPropIntVal(String key) {
		String val = props.getProperty(key);
		return Util.strToInt(val);
	}
}
