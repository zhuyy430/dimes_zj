package com.digitzones.vo;

/**
 * 工单
 * @author zdq
 * 2018年6月4日
 */
public class WorkSheetVO {
	private Long id;
	/**工单编号*/
	private String no;
	/**生产日期*/
	private String manufactureDate;
	/**制单人*/
	private String documentMaker;
	/**制单日期*/
	private String makeDocumentDate;
	/**工件代码*/
	private String workPieceCode;
	/**工件名称*/
	private String workPieceName;
	/**规格型号*/
	private String unitType;
	/**客户图号*/
	private String customerGraphNumber;
	/**图号*/
	private String graphNumber;
	/**版本号*/
	private String version;
	/**生产数量*/
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
	/**工单状态*/
	private String status;
	/**工单类型：普通工单、返修工单*/
	private String workSheetType;
	/**返修单号*/
	private String repairWorkSheetNo;
	/**完成时间*/
	private String completeTime;
	/**是否被删除*/
	private String deleted ;
	/**二维码路径*/
	private String qrCodeUrl;
	/**材料数量*/
	private Double Define33;
	/**子件代码*/
	private String moallocateInvcode;
	/**子件数量*/
	private Double moallocateQty;
	/**子件批号*/
	private String LotNo;
	/**子表id*/
	private Integer moDId;
	/**部门编码*/
	private String departmentCode;
	/**部门部门名称*/
	private String departmentName;
	/**
	 * 是否是从erp导入的工单
	 */
	private Boolean fromErp;
	/**货位代码*/
	private String locationCodes;
	/**报工总数*/
	private Double sumOfAllJobBooking;
	/**入库总数*/
	private Double sumOfAllInWarehouse;
	/**默认仓库*/
	private String defaultWarehouseCode;

	public String getDefaultWarehouseCode() {
		return defaultWarehouseCode;
	}

	public void setDefaultWarehouseCode(String defaultWarehouseCode) {
		this.defaultWarehouseCode = defaultWarehouseCode;
	}

	public Double getSumOfAllJobBooking() {
		return sumOfAllJobBooking;
	}

	public void setSumOfAllJobBooking(Double sumOfAllJobBooking) {
		this.sumOfAllJobBooking = sumOfAllJobBooking;
	}

	public Double getSumOfAllInWarehouse() {
		return sumOfAllInWarehouse;
	}

	public void setSumOfAllInWarehouse(Double sumOfAllInWarehouse) {
		this.sumOfAllInWarehouse = sumOfAllInWarehouse;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
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
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public String getDocumentMaker() {
		return documentMaker;
	}
	public void setDocumentMaker(String documentMaker) {
		this.documentMaker = documentMaker;
	}
	public String getMakeDocumentDate() {
		return makeDocumentDate;
	}
	public void setMakeDocumentDate(String makeDocumentDate) {
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
	public String getCustomerGraphNumber() {
		return customerGraphNumber;
	}
	public void setCustomerGraphNumber(String customerGraphNumber) {
		this.customerGraphNumber = customerGraphNumber;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public Long getProductionUnitId() {
		return productionUnitId;
	}
	public void setProductionUnitId(Long productionUnitId) {
		this.productionUnitId = productionUnitId;
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
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
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
	public Integer getMoDId() {
		return moDId;
	}
	public void setMoDId(Integer moDId) {
		this.moDId = moDId;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Boolean getFromErp() {
		return fromErp;
	}
	public void setFromErp(Boolean fromErp) {
		this.fromErp = fromErp;
	}
	public String getLocationCodes() {
		return locationCodes;
	}
	public void setLocationCodes(String locationCodes) {
		this.locationCodes = locationCodes;
	}
	
}
