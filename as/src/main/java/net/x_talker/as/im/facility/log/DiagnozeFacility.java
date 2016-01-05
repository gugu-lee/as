package net.x_talker.as.im.facility.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.NullEnumeration;

import net.x_talker.as.im.util.PropertiesUtil;

public class DiagnozeFacility extends Facility {

	private final String DIAGNOZE_DEFAULT_LOG_NAME = "diagnozelog";
	public static final String DIAGNOZE_LOG_NAME = "com.ims.as.facility.diagonze.logname";

	public DiagnozeFacility(String name) {
		super(name);
	}

	@Override
	public void initLog() {
		String logName = PropertiesUtil.getInstance().getPropVal(DIAGNOZE_LOG_NAME);
		logger = Logger.getLogger(logName);
		if (logger.getAllAppenders() instanceof NullEnumeration) {
			initDefaultLogger();
		}
	}

	private void initDefaultLogger() {
		logger = Logger.getLogger(DIAGNOZE_DEFAULT_LOG_NAME);
		// 清空Appender。特別是不想使用現存實例時一定要初期化
		logger.removeAllAppenders();
		// 設定Logger級別。
		logger.setLevel(Level.INFO);
		// 設定是否繼承父Logger。
		// 默認為true。繼承root輸出。
		// 設定false後將不輸出root。
		logger.setAdditivity(true);
		// 生成新的Appender
		FileAppender appender = new RollingFileAppender();
		PatternLayout layout = new PatternLayout();
		// log的输出形式
		String conversionPattern = "[%d] %p %t %c - %m%n";
		layout.setConversionPattern(conversionPattern);
		appender.setLayout(layout);
		// log输出路径
		// 这里使用了环境变量[catalina.home]，只有在tomcat环境下才可以取到
		String tomcatPath = java.lang.System.getProperty("catalina.home");
		appender.setFile(tomcatPath + "/logs/" + DIAGNOZE_DEFAULT_LOG_NAME + ".log");
		// log的文字码
		appender.setEncoding("UTF-8");
		// true:在已存在log文件后面追加 false:新log覆盖以前的log
		appender.setAppend(true);
		// 适用当前配置
		appender.activateOptions();
		// 将新的Appender加到Logger中
		logger.addAppender(appender);
	}
}
