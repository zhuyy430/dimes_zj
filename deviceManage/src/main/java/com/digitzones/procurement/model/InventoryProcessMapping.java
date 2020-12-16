package com.digitzones.procurement.model;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitzones.model.Processes;
/**
 * 工件工序关联实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="INVENTORY_PROCESS")
public class InventoryProcessMapping {
	/**对象标识*/
	private Long id;
	/**工件*/
	private Inventory inventory;
	/**工序*/
	private Processes process;
	/**参数取值
	 * 固定值
	 * 变动值
	 * */
	private String parameterValueType;
	/**工艺路线 */
	private Long processRoute;
	/**标准节拍*/
	private Float standardBeat=0f;
	public Float getStandardBeat() {
		return standardBeat;
	}
	public void setStandardBeat(Float standardBeat) {
		this.standardBeat = standardBeat;
	}
	public Long getProcessRoute() {
		return processRoute;
	}
	public void setProcessRoute(Long processRoute) {
		this.processRoute = processRoute;
	}
	public String getParameterValueType() {
		return parameterValueType;
	}
	public void setParameterValueType(String parameterValueType) {
		this.parameterValueType = parameterValueType;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PROCESS_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Processes getProcess() {
		return process;
	}
	public void setProcess(Processes process) {
		this.process = process;
	}
	@ManyToOne
	@JoinColumn(name="INVENTORY_CODE",foreignKey= @ForeignKey(ConstraintMode.NO_CONSTRAINT),referencedColumnName = "cInvCode")
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}
