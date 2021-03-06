package com.digitzones.procurement.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Subselect;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * ERP生产订单
 */
@Entity
@Subselect("select * from View_Mom_orderdetail")
public class Mom_recorddetail {
	/*子表ID*/
	@Id
	@GeneratedValue
	private String moDId;
	/*生产订单主表ID */
	private String moId;
	/*生产订单号*/
	private String  mocode;
	/*生产订单行号 */
	@Column(name="sortSeq")
	private String  sortSeq;
	/*开工日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
	private Date  startDate;
	/*生产数量 */
	private Double  detailQty;
	/*生产批号 */
	private String  moLotCode;
	/*仓库代码 */
	private String  whCode;
	/*生产部门 */
	private String  mdeptCode;
	/*状态 */
	private String  status;
	/*物料ID*/
	private Integer  partId;
	/*存货编码*/
	private String  detailInvCode;
	/*审核时间*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
	private Date  RelsTime;
	/*单据日期*/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createTime;
	/*审核人*/
	private String  RelsUser;
	/*材料编号*/
	private String  define24;
	/*材料数量*/
	private String  define33;
	/*子件代码*/
	private String  moallocateInvcode;
	/*子件名称*/
	private String cInvName;
	/*规格型号*/
	private String cInvStd;
	/*子件数量*/
	private Double  moallocateQty;
	/*子件批号*/
	private String  lotNo;
	public String getMoDId() {
		return moDId;
	}
	public void setMoDId(String moDId) {
		this.moDId = moDId;
	}
	public String getMoId() {
		return moId;
	}
	public void setMoId(String moId) {
		this.moId = moId;
	}
	public String getMocode() {
		return mocode;
	}
	public void setMocode(String mocode) {
		this.mocode = mocode;
	}
	public String getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(String sortSeq) {
		this.sortSeq = sortSeq;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Double getDetailQty() {
		return detailQty;
	}
	public void setDetailQty(Double detailQty) {
		this.detailQty = detailQty;
	}
	public String getMoLotCode() {
		return moLotCode;
	}
	public void setMoLotCode(String moLotCode) {
		this.moLotCode = moLotCode;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public String getMdeptCode() {
		return mdeptCode;
	}
	public void setMdeptCode(String mdeptCode) {
		this.mdeptCode = mdeptCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPartId() {
		return partId;
	}
	public void setPartId(Integer partId) {
		this.partId = partId;
	}
	public String getDetailInvCode() {
		return detailInvCode;
	}
	public void setDetailInvCode(String detailInvCode) {
		this.detailInvCode = detailInvCode;
	}
	public Date getRelsTime() {
		return RelsTime;
	}
	public void setRelsTime(Date relsTime) {
		RelsTime = relsTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRelsUser() {
		return RelsUser;
	}
	public void setRelsUser(String relsUser) {
		RelsUser = relsUser;
	}
	public String getDefine24() {
		return define24;
	}
	public void setDefine24(String define24) {
		this.define24 = define24;
	}
	public String getDefine33() {
		return define33;
	}
	public void setDefine33(String define33) {
		this.define33 = define33;
	}
	public String getMoallocateInvcode() {
		return moallocateInvcode;
	}
	public void setMoallocateInvcode(String moallocateInvcode) {
		this.moallocateInvcode = moallocateInvcode;
	}
	public String getcInvName() {
		return cInvName;
	}
	public void setcInvName(String cInvName) {
		this.cInvName = cInvName;
	}
	public String getcInvStd() {
		return cInvStd;
	}
	public void setcInvStd(String cInvStd) {
		this.cInvStd = cInvStd;
	}
	public Double getMoallocateQty() {
		return moallocateQty;
	}
	public void setMoallocateQty(Double moallocateQty) {
		this.moallocateQty = moallocateQty;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
}
