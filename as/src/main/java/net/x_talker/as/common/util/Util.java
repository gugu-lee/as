
package net.x_talker.as.common.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 【公用工具类】
 *
 * @version 
 * @author xubo  2014-4-3 上午10:43:19
 * 
 */
public class Util {
	
	private static Logger logger = Logger.getLogger(Util.class);
	
	public static int strToInt(String s) {
		int i = 0;
		if (isEmpty(s)) {
			return i;
		} else {
			try {
				String str = s.replaceAll("[^0-9-]", "");
				i = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return i;
	}
	
	public static boolean isEmpty(String srcStr) {
		return nvl(srcStr, "").trim().length() == 0
				|| nvl(srcStr, "").equals("null");
	}
	
	public static String nvl(String srcStr, String objStr) {
		if (srcStr == null || 0 == srcStr.trim().length()
				|| "null".equalsIgnoreCase(srcStr.trim())) {
			return objStr;
		} else {
			return srcStr;
		}
	}
	
	public static long hourToMilliseconds(int hour){
		long milliseconds=hour*60*60*1000;
		return milliseconds;
	}
	
	public static long secondsToMilliseconds(int seconds){
		long milliseconds=seconds*1000;
		return milliseconds;
	}
	
	public static Date getEarlierTime(Date time1,Date time2){
		if(time1==null&&time2==null)
			return null;
		if(time1==null)
			return time2;
		if(time2==null)
			return time1;
		return time1.before(time2)?time1:time2;
	}
	
	public static Date getEarlierTimeNotBeforeCurrentTime(Date time1,Date time2,Date defaultTime){
		Date currentTime=new Date(System.currentTimeMillis());
		if(time1==null&&time2==null)
			return null;
		if(time1==null||(time1.before(currentTime)&&time2!=null))
			return time2.before(currentTime)?defaultTime:time2;
		if(time2==null||(time2.before(currentTime)&&time1!=null))
			return time1.before(currentTime)?defaultTime:time1;
		return time1.before(time2)?time1:time2;
	}
	
	/**
	 * 
	 * 【byte[]用BASE64加密转成String】
	 * 
	 * @author xubo 2014-4-17
	 * @param buffer
	 * @return
	 */
	public static String byte2BASE64(byte[] buffer){
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(buffer);
	}
	
	/**
	 * 
	 * 【String用BASE64解密并转成byte[]】
	 * 
	 * @author xubo 2014-4-17
	 * @param str
	 * @return
	 */
	public static byte[] getByteFromBASE64(String str) {
		if (str == null) return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(str);
			return bytes;
		} catch (Exception e) {
			return null;
		}
	} 
	
	/**
	 * 
	 * 【指定文件路径和文件名创建文件】
	 * 
	 * @author xubo 2014-4-21
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static File createFileInPath(String path,String fileName){
		//如果目录不存在,创建目录;
		String realPath = getRealPath(path);
		File dir = new File(realPath);
		if(!dir.exists())
			dir.mkdirs();
		File file = new File(realPath + File.separator+fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return file;
		
	}
	/**
	 * 
	 * 【获取指定目录的指定文件】
	 * 
	 * @author xubo 2014-4-21
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static File getFileInPath(String path,String fileName){
		File file = new File(getRealPath(path) + File.separator+fileName);
		if(file.exists()){
			return file;
		}
		logger.warn("file not exist,fileName:"+fileName);
		return null;
	}
	/**
	 * 
	 * 【删除指定目录的指定文件】
	 * 
	 * @author xubo 2014-4-21
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFileInPath(String path,String fileName){
		File file = new File(getRealPath(path) + File.separator+fileName);
		if(file.exists()){
			file.delete();
			return true;
		}
		return false;
	}
	
	public static String getRealPath(String strPath)
	{
		String regx="\\$\\{(.*)\\}";
		Pattern p =Pattern.compile(regx);
		Matcher m = p.matcher(strPath);
		String envName ="";
		
		if(m.find()){
			envName = m.group(1);
		}
		
		strPath = strPath.replace("\\", File.separator);
		
		if(envName!=""){
			//获取环境变量路径
			String envpath = System.getProperty(envName);
			if (envpath==null)
				return strPath;
			strPath = strPath.replace(String.format("${%s}", envName), envpath);
		}
		return strPath;
	}

	public static Date parseGMTTime(String timeStr) throws ParseException {
		String pattern = "EEE, dd MMM yyyy HH:mm:ss z";
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);  
		return format.parse(timeStr);
	}
	
	public static String formatGMTTime(Date date) {
		String pattern = "EEE, dd MMM yyyy HH:mm:ss z";
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);  
		return format.format(date);
	}
}
