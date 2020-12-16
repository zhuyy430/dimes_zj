package com.digitzones.vo;
/**
 * 生产单元与客户端IP映射VO
 * @author zdq
 * 2018年9月7日
 */
public class ProductionUnitMappingVO {
	private Long id;
	private String clientIp;
	private Long productionUnitId;
	private String productionUnitName;
	private String productionUnitCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public Long getProductionUnitId() {
		return productionUnitId;
	}
	public void setProductionUnitId(Long productionUnitId) {
		this.productionUnitId = productionUnitId;
	}
	public String getProductionUnitName() {
		return productionUnitName;
	}
	public void setProductionUnitName(String productionUnitName) {
		this.productionUnitName = productionUnitName;
	}
	public String getProductionUnitCode() {
		return productionUnitCode;
	}
	public void setProductionUnitCode(String productionUnitCode) {
		this.productionUnitCode = productionUnitCode;
	}
}
