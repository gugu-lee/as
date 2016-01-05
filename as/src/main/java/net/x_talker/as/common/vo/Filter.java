
package net.x_talker.as.common.vo;

import java.util.Map;


/**
 * 【分页信息类】
 *
 * @version 
 * @author mengfang  2012-7-1 下午08:35:03
 * 
 */
public class Filter implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7262439737206649174L;
	//过滤器运算表达式
	private String op;
	//过滤器条件名值对
	private Map<?,?> condition;
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Map<?, ?> getCondition() {
		return condition;
	}
	public void setCondition(Map<?, ?> condition) {
		this.condition = condition;
	}
}
