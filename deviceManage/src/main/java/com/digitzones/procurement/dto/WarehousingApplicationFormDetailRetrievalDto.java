package com.digitzones.procurement.dto;
import java.util.Date;
/**
 * 入库申请单查询条件类
 */
public class WarehousingApplicationFormDetailRetrievalDto {
    /**来料日期开始日期*/
    private String from;
    /**来料日期结束日期*/
    private String to;
    /**供应商编码*/
    private String vendor;
    /**申请单号*/
    private String requestNo;
    /**采购单号*/
    private String purchasingOrderNo;
    /**入库仓库：编码或名称*/
    private String warehouse;
    /**检验状态:默认未检验,通过检验，未通过检验*/
    private String checkStatus;
    /**材料编码*/
    private String furnaceNumber;
    
    public String getCheckStatus() {
        return checkStatus;
    }
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getVendor() {
        return vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getRequestNo() {
        return requestNo;
    }
    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }
    public String getPurchasingOrderNo() {
        return purchasingOrderNo;
    }
    public void setPurchasingOrderNo(String purchasingOrderNo) {
        this.purchasingOrderNo = purchasingOrderNo;
    }
    public String getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
	public String getFurnaceNumber() {
		return furnaceNumber;
	}
	public void setFurnaceNumber(String furnaceNumber) {
		this.furnaceNumber = furnaceNumber;
	}
}
