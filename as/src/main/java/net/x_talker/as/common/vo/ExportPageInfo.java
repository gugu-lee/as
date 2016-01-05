
package net.x_talker.as.common.vo;


/**
 * 导出分页类
 * @author liyinghui
 *
 * 2012-2-23 上午10:55:52
 */
public class ExportPageInfo {
	
	public int pageNo;
	public int startNum;
	public int endNum;
	public int pageSize;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getStartNum() {
		return startNum;
	}
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	public int getEndNum() {
		return endNum;
	}
	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
