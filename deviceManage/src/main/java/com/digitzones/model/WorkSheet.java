package com.digitzones.model;
import com.digitzones.constants.Constant;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 工单
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="WORKSHEET")
public class WorkSheet implements Serializable {
	private Long id;
	/**工单编号*/
	private String no;
	/**生产日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date manufactureDate;
	/**制单人*/
	private String documentMaker;
	/**制单日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date makeDocumentDate;
	/**工件代码*/
	private String workPieceCode;
	/**工件名称*/
	private String workPieceName;
	/**计量单位编码*/
	private String unitCode;
	/**计量单位名称*/
	private String unitName;
	/**规格型号*/
	private String unitType;
	/**图号*/
	private String graphNumber;
	/**生产总数量*/
	private int productCount;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String stoveNumber;
	/**生产单元id*/
	private Long productionUnitId;
	/**生产单元代码*/
	private String productionUnitCode;
	/**生产单元名称*/
	private String productionUnitName;
	/**备注*/
	private String note;
	/**工单状态,默认为"计划"*/
	private String status = Constant.WorkSheet.Status.PLAN;
	/**完成时间*/
	private Date completeTime;
	/**工单类型：普通工单、返修工单*/
	private String workSheetType;
	/**返修单号*/
	private String repairWorkSheetNo;
	/**是否被删除*/
	private Boolean deleted = false;
	/**材料数量*/
	private Double Define33;
	/**子件代码*/
	private String moallocateInvcode;
	/**子件数量*/
	private Double moallocateQty;
	/**生产订单子件信息行id*/
	private String LotNo;
	/**子表id*/
	private Integer moDId;
	/**生产订单主表id*/
	private Integer moId;
	/**生产订单号*/
	private String mocode;
	/**部门编码*/
	private String departmentCode;
	/**部门部门名称*/
	private String departmentName;
	/**
	 * 是否是从erp导入的工单
	 */
	private Boolean fromErp=false;
	/**货位代码*/
	private String locationCodes;



	public Integer getMoId() {
		return moId;
	}

	public void setMoId(Integer moId) {
		this.moId = moId;
	}

	public String getMocode() {
		return mocode;
	}

	public void setMocode(String mocode) {
		this.mocode = mocode;
	}

	public Boolean getFromErp() {
		return fromErp;
	}

	public void setFromErp(Boolean fromErp) {
		this.fromErp = fromErp;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public Integer getMoDId() {
		return moDId;
	}

	public void setMoDId(Integer moDId) {
		this.moDId = moDId;
	}

	public Long getProductionUnitId() {
		return productionUnitId;
	}
	public void setProductionUnitId(Long productionUnitId) {
		this.productionUnitId = productionUnitId;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public String getDocumentMaker() {
		return documentMaker;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getWorkSheetType() {
		return workSheetType;
	}
	public void setWorkSheetType(String workSheetType) {
		this.workSheetType = workSheetType;
	}
	public String getRepairWorkSheetNo() {
		return repairWorkSheetNo;
	}
	public void setRepairWorkSheetNo(String repairWorkSheetNo) {
		this.repairWorkSheetNo = repairWorkSheetNo;
	}
	public void setDocumentMaker(String documentMaker) {
		this.documentMaker = documentMaker;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMakeDocumentDate() {
		return makeDocumentDate;
	}
	public void setMakeDocumentDate(Date makeDocumentDate) {
		this.makeDocumentDate = makeDocumentDate;
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
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
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
	public String getProductionUnitCode() {
		return productionUnitCode;
	}
	public void setProductionUnitCode(String productionUnitCode) {
		this.productionUnitCode = productionUnitCode;
	}
	public String getProductionUnitName() {
		return productionUnitName;
	}
	public void setProductionUnitName(String productionUnitName) {
		this.productionUnitName = productionUnitName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Double getDefine33() {
		return Define33;
	}
	public void setDefine33(Double define33) {
		Define33 = define33;
	}
	public String getMoallocateInvcode() {
		return moallocateInvcode;
	}
	public void setMoallocateInvcode(String moallocateInvcode) {
		this.moallocateInvcode = moallocateInvcode;
	}
	public Double getMoallocateQty() {
		return moallocateQty;
	}
	public void setMoallocateQty(Double moallocateQty) {
		this.moallocateQty = moallocateQty;
	}
	public String getLotNo() {
		return LotNo;
	}
	public void setLotNo(String lotNo) {
		LotNo = lotNo;
	}

	public String getLocationCodes() {
		return locationCodes;
	}

	public void setLocationCodes(String locationCodes) {
		this.locationCodes = locationCodes;
	}
	
}
