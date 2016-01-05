package net.x_talker.as.im.facility.pm;

import org.aspectj.lang.ProceedingJoinPoint;

import net.x_talker.as.im.facility.log.Facility;
import net.x_talker.as.im.facility.log.FacilityEnum;

/**
 * 功能执行耗时分析 对方法执行整个过程进行性能分析
 * 
 * @author zengqiaowen
 *
 */
public class PMAnalysis {

	private Facility logger = Facility.getFacility(FacilityEnum.PMANALYSIS, "");

	public void timeConsumeAnalysis(ProceedingJoinPoint joinPoint) throws Throwable {
		long beginTime = System.currentTimeMillis();
		joinPoint.proceed();
		long endTime = System.currentTimeMillis();
		// 获取被调用的方法名
		String targetMethodName = joinPoint.getSignature().getName();

		String className = joinPoint.getTarget().getClass().getName();
		// 日志格式字符串
		String logInfoText = "excute " + className + "." + targetMethodName + ", cost(" + (endTime - beginTime)
				+ "(ms))";
		// 将日志信息写入配置的文件中
		logger.log(logInfoText);
	}
}
