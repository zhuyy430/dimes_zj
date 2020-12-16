package com.digitzones.devmgr.vo;
/**
 * 点检项目vo实体
 * @author Administrator
 */
public class CheckStatisticsVo {
	/**日期*/
	private String date;
	/**内容分类，显示在最左侧的内容：如生产单元等*/
	private String category;
	/**计划次数*/
	private String planCount;
	/**完成次数*/
	private String completeCount;
	/**逾期 次数*/
	private String delayCount;
	/**逾期未完成次数*/
	private String delayAndUncompleteCount;
	
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getDelayCount() {
		return delayCount;
	}
	public void setDelayCount(String delayCount) {
		this.delayCount = delayCount;
	}
	public String getDelayAndUncompleteCount() {
		return delayAndUncompleteCount;
	}
	public void setDelayAndUncompleteCount(String delayAndUncompleteCount) {
		this.delayAndUncompleteCount = delayAndUncompleteCount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(String completeCount) {
		this.completeCount = completeCount;
	}
}
