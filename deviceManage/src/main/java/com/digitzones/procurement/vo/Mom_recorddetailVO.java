package com.digitzones.procurement.vo;

/**
 * ERP生产订单
 */
public class Mom_recorddetailVO {
	/*子表ID*/
	private Integer moDId;
	/*生产订单主表ID */
	private String moId;
	/*生产订单号*/
	private String  mocode;
	/*生产订单行号 */
	private Integer  sortSeq;
	/*开工日期 */
	private String  startDate;
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
	private String  RelsTime;
	/*单据日期*/
	private String  createTime;
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
	public Integer getMoDId() {
		return moDId;
	}
	public void setMoDId(Integer moDId) {
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
	public Integer getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(Integer sortSeq) {
		this.sortSeq = sortSeq;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
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
	public String getRelsTime() {
		return RelsTime;
	}
	public void setRelsTime(String relsTime) {
		RelsTime = relsTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
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
