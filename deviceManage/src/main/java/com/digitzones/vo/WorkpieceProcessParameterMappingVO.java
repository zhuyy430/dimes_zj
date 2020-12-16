package com.digitzones.vo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.digitzones.procurement.model.InventoryProcessMapping;

/**
 * 工件工序实体和参数的关联实体
 * @author zdq
 * 2018年6月5日
 */
public class WorkpieceProcessParameterMappingVO {
	private Long id;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine;
	/**下线*/
	private Float lowLine;
	/**标准值*/
	private Float standardValue;
	/**参数名*/
	private String parameterName;
	/**参数值*/
	private String parameterCode;
	/**工件工序关联实体*/
	private InventoryProcessMapping workpieceProcess;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Float getUpLine() {
		return upLine;
	}
	public void setUpLine(Float upLine) {
		this.upLine = upLine;
	}
	public Float getLowLine() {
		return lowLine;
	}
	public void setLowLine(Float lowLine) {
		this.lowLine = lowLine;
	}
	public InventoryProcessMapping getWorkpieceProcess() {
		return workpieceProcess;
	}
	public void setWorkpieceProcess(InventoryProcessMapping workpieceProcess) {
		this.workpieceProcess = workpieceProcess;
	}
	public Float getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
}
