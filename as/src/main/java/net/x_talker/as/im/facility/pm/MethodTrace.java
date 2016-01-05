package net.x_talker.as.im.facility.pm;

import org.aspectj.lang.JoinPoint;

import net.x_talker.as.im.facility.log.Facility;
import net.x_talker.as.im.facility.log.FacilityEnum;

/**
 * 方法体调用日志记录诊断 以确定业务流程是否正确完成
 * 
 * @author zengqiaowen
 *
 */
public class MethodTrace {

	private Facility logger = Facility.getFacility(FacilityEnum.DIAGNOZE, "");

	/**
	 * 方法调用前日志记录
	 * 
	 * @param joinPoint
	 */
	public void traceIn(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.log("Trace In " + className + "." + methodName);
	}

	/**
	 * 方法调用后日志输出
	 * 
	 * @param joinPoint
	 */
	public void traceOut(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.log("Trace Out " + className + "." + methodName);
	}

}
