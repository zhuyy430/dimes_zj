package com.digitzones.mc.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
/**
 * MC端工单实体类
 * @author Administrator
 */
@Entity
public class MCWorkSheet {
	/**工单详情id*/
	private Long id;
	/**生产时间*/
	private Date manufactureDate;
	/**工单号*/
	private String no;
	/**工件代码*/
	private String workPieceCode;
	/**工件名称*/
	private String workPieceName;
	/**图号*/
	private String graphNumber;
	/**规格型号*/
	private String unitType;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String stoveNumber;
	/**计划生产数量*/
	private int productionCount;
	/**状态 :0 表示‘计划’、 1 表示‘加工中’、 2 表示‘停工’、 3 表示‘已完工’*/
	private String status;
	/**站点代码*/
	private String deviceSiteCode;
	/**完工数量*/
	private Integer completeCount = 0;
	/**报工数*/
	private Integer reportCount = 0;
	/**工序代码*/
	private String processCode;
	/**工序名称*/
	private String processName;
	/**生产单元代码*/
	private String productionUnitCode;
	
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getWorkPieceCode() {
		return workPieceCode;
	}
	public void setWorkPieceCode(String workPieceCode) {
		this.workPieceCode = workPieceCode;
	}
	public String getWorkPieceName() {
		return workPieceName;
	}
	public void setWorkPieceName(String workPieceName) {
		this.workPieceName = workPieceName;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getStoveNumber() {
		return stoveNumber;
	}
	public void setStoveNumber(String stoveNumber) {
		this.stoveNumber = stoveNumber;
	}
	
	public int getProductionCount() {
		return productionCount;
	}
	public void setProductionCount(int productionCount) {
		this.productionCount = productionCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeviceSiteCode() {
		return deviceSiteCode;
	}
	public void setDeviceSiteCode(String deviceSiteCode) {
		this.deviceSiteCode = deviceSiteCode;
	}
	public Integer getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}
	public Integer getReportCount() {
		return reportCount;
	}
	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getProductionUnitCode() {
		return productionUnitCode;
	}
	public void setProductionUnitCode(String productionUnitCode) {
		this.productionUnitCode = productionUnitCode;
	}
}
