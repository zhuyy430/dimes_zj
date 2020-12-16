package com.digitzones.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 加工记录
 * @author zdq
 * 2018年6月4日
 */
@Entity
@Table(name="PROCESSRECORD")
public class ProcessRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**设备站点id*/
	private Long deviceSiteId;
	/**设备站点代码*/
	private String deviceSiteCode;
	/**设备站点名称*/
	private String deviceSiteName;
	/**采集时间,实际的生产时间*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private Date collectionDate;
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
	private  String classesName;
	/**pattern:yyyyMMdd  解决跨班问题，记录当前班次的开始日期*/
	private String forProductionDate;
	/**工单编号*/
	private String no;
	/**工单id*/
	private Long workSheetId;
	/**生产日期*/
	private Date manufactureDate;
	/**制单人*/
	private String documentMaker;
	/**制单日期*/
	private Date makeDocumentDate;
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
	/**材料编号*/
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
	/**生产序号：其他数据库传过来的字符串，每个工件对应唯一一个生产序号*/
	private String productNum;
	/**OPC端的序号,二维码*/
	private String opcNo;

	public String getForProductionDate() {
		return forProductionDate;
	}

	public void setForProductionDate(String forProductionDate) {
		this.forProductionDate = forProductionDate;
	}

	public String getOpcNo() {
		return opcNo;
	}
	public void setOpcNo(String opcNo) {
		this.opcNo = opcNo;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public Long getShortHalt() {
		return shortHalt;
	}
	public void setShortHalt(Long shortHalt) {
		this.shortHalt = shortHalt;
	}
	public Boolean getRealRecord() {
		return realRecord;
	}
	public void setRealRecord(Boolean realRecord) {
		this.realRecord = realRecord;
	}
	public float getStandardBeat() {
		return standardBeat;
	}
	public void setStandardBeat(float standardBeat) {
		this.standardBeat = standardBeat;
	}
	public float getRealBeat() {
		return realBeat;
	}
	public void setRealBeat(float realBeat) {
		this.realBeat = realBeat;
	}
	public Long getWorkSheetId() {
		return workSheetId;
	}
	public void setWorkSheetId(Long workSheetId) {
		this.workSheetId = workSheetId;
	}
	public Long getClassesId() {
		return classesId;
	}
	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}
	public Long getWorkPieceId() {
		return workPieceId;
	}
	public void setWorkPieceId(Long workPieceId) {
		this.workPieceId = workPieceId;
	}
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public Long getDeviceSiteId() {
		return deviceSiteId;
	}
	public void setDeviceSiteId(Long deviceSiteId) {
		this.deviceSiteId = deviceSiteId;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getNo() {
		return no;
	}
	public String getProductUserCode() {
		return productUserCode;
	}
	public void setProductUserCode(String productUserCode) {
		this.productUserCode = productUserCode;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public String getDocumentMaker() {
		return documentMaker;
	}
	public void setDocumentMaker(String documentMaker) {
		this.documentMaker = documentMaker;
	}
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
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	public Long getProductUserId() {
		return productUserId;
	}
	public void setProductUserId(Long productUserId) {
		this.productUserId = productUserId;
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
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
}
