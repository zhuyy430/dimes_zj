package com.digitzones.vo;

/**
 * 包装条码
 * @author zhuyy430
 *
 */
public class PackageCodeVO {
	private Long id;
	/**条码*/
	private String code;
	/**物料代码*/
	private String inventoryCode;
	/**物料名称*/
	private String inventoryName;
	/**批号*/
	private String batchNumber;
	/**材料编号*/
	private String furnaceNumber;
	/**规格型号*/
	private String specificationType;
	/**每箱数量*/
	private Double boxamount;
	/**箱号*/
	private Integer boxnum;
	/**发货计划单号*/
	private String formNo;
	/**客户名称*/
	private String customer;
	/**生成时间*/
	private Long createDate;
	/**二维码路径*/
    private String qrPath;
	public String getSpecificationType() {
		return specificationType;
	}
	public void setSpecificationType(String specificationType) {
		this.specificationType = specificationType;
	}

	public String getFormNo() {
		return formNo;
	}

	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInventoryCode() {
		return inventoryCode;
	}
	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getFurnaceNumber() {
		return furnaceNumber;
	}
	public void setFurnaceNumber(String furnaceNumber) {
		this.furnaceNumber = furnaceNumber;
	}
	public Double getBoxamount() {
		return boxamount;
	}
	public void setBoxamount(Double boxamount) {
		this.boxamount = boxamount;
	}
	public Integer getBoxnum() {
		return boxnum;
	}
	public void setBoxnum(Integer boxnum) {
		this.boxnum = boxnum;
	}
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public String getInventoryName() {
		return inventoryName;
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	public String getQrPath() {
		return qrPath;
	}
	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
	}
}
