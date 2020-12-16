package com.digitzones.vo;
/**
 * 设备执行记录值对象
 * @author zdq
 * 2018年9月6日
 */
public class ProcessRecordVO {
	private Long id;
	/**设备站点id*/
	private Long deviceSiteId;
	/**设备站点代码*/
	private String deviceSiteCode;
	/**设备站点名称*/
	private String deviceSiteName;
	/**采集时间,实际的生产时间*/
	private String collectionDate;
	/**生产序号
	 * 二维码
	 * */
	private String serialNo;
	/**生产人员ID*/
	private Long productUserId;
	/**生产人员员工编号*/
	private String productUserCode;
	/**生产人员姓名*/
	private String productUserName;
	/**班次代码*/
	private String classesCode;
	/**班次id*/
	private Long classesId;
	/**班次名称*/
	private String classesName;
	/**工单编号*/
	private String no;
	/**工单id*/
	private Long workSheetId;
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
	/**工件id*/
	private Long workPieceId;
	/**规格型号*/
	private String unitType;
	/**客户图号*/
	private String customerGraphNumber;
	/**图号*/
	private String graphNumber;
	/**版本号*/
	private String version;
	/**生产数量,目前未使用:2018-7-9*/
	private int productCount;
	/**批号*/
	private String batchNumber;
	/**炉号*/
	private String stoveNumber;
	/**工序代码*/
	private String processCode;
	/**工序id*/
	private Long processId;
	/**工序名称*/
	private String processName;
	/**是否被删除*/
	private Boolean deleted = false;
	/**加工状态,NG,OK*/
	private String status;
	/**即时节拍*/
	private float realBeat;
	/**标准节拍*/
	private float standardBeat;
	/**是否为真正记录，如果为false，则每天开班时新增的记录,工件加工数并不增加*/
	private Boolean realRecord=true;
	/**短停机秒数*/
	private Long shortHalt = 0l;
	private String productNum;
	private String opcNo;
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getOpcNo() {
		return opcNo;
	}
	public void setOpcNo(String opcNo) {
		this.opcNo = opcNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDeviceSiteId() {
		return deviceSiteId;
	}
	public void setDeviceSiteId(Long deviceSiteId) {
		this.deviceSiteId = deviceSiteId;
	}
	public String getDeviceSiteCode() {
		return deviceSiteCode;
	}
	public void setDeviceSiteCode(String deviceSiteCode) {
		this.deviceSiteCode = deviceSiteCode;
	}
	public String getDeviceSiteName() {
		return deviceSiteName;
	}
	public void setDeviceSiteName(String deviceSiteName) {
		this.deviceSiteName = deviceSiteName;
	}
	public String getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public Long getProductUserId() {
		return productUserId;
	}
	public void setProductUserId(Long productUserId) {
		this.productUserId = productUserId;
	}
	public String getProductUserCode() {
		return productUserCode;
	}
	public void setProductUserCode(String productUserCode) {
		this.productUserCode = productUserCode;
	}
	public String getProductUserName() {
		return productUserName;
	}
	public void setProductUserName(String productUserName) {
		this.productUserName = productUserName;
	}
	public String getClassesCode() {
		return classesCode;
	}
	public void setClassesCode(String classesCode) {
		this.classesCode = classesCode;
	}
	public Long getClassesId() {
		return classesId;
	}
	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Long getWorkSheetId() {
		return workSheetId;
	}
	public void setWorkSheetId(Long workSheetId) {
		this.workSheetId = workSheetId;
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
	public Long getWorkPieceId() {
		return workPieceId;
	}
	public void setWorkPieceId(Long workPieceId) {
		this.workPieceId = workPieceId;
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
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getRealBeat() {
		return realBeat;
	}
	public void setRealBeat(float realBeat) {
		this.realBeat = realBeat;
	}
	public float getStandardBeat() {
		return standardBeat;
	}
	public void setStandardBeat(float standardBeat) {
		this.standardBeat = standardBeat;
	}
	public Boolean getRealRecord() {
		return realRecord;
	}
	public void setRealRecord(Boolean realRecord) {
		this.realRecord = realRecord;
	}
	public Long getShortHalt() {
		return shortHalt;
	}
	public void setShortHalt(Long shortHalt) {
		this.shortHalt = shortHalt;
	}
	
}
