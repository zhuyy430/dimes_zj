package com.digitzones.devmgr.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 仓库物品映射实体(库存信息)：物品可以是任何物品
 * 如：备品备件等
 * @author Administrator
 */
@Entity
@Table(name="WAREHOUSE_GOODS")
public class WarehouseGoodsMapping implements Serializable {
	private static final long serialVersionUID = -7727037581496812601L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**物品类型:参考Constant.GoodType类
	 * sparepartType:备品备件类型*/
	private String goodType;
	/**物品代码*/
	private String goodCode;
	/**物品名称*/
	private String goodName;
	/**仓库代码*/
	private String warehouseCode;
	/**仓库名称*/
	private String warehouseName;
	/**批号*/
	private String batchNum;
	/**数量*/
	@Column(name="_COUNT")
	private Integer count;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGoodType() {
		return goodType;
	}
	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}
	public String getGoodCode() {
		return goodCode;
	}
	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
