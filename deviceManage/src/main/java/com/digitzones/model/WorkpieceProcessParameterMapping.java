package com.digitzones.model;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.digitzones.procurement.model.InventoryProcessMapping;

/**
 * 工件工序实体和参数的关联实体
 * @author zdq
 * 2018年6月5日
 */
@Entity
@Table(name="WORKPIECEPROCESS_PARAMETER")
public class WorkpieceProcessParameterMapping {
	private Long id;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine = 0f;
	/**下线*/
	private Float lowLine = 0f;
	/**标准值*/
	private Float standardValue = 0f;
	/**工件工序关联实体*/
	private InventoryProcessMapping workpieceProcess;
	/**参数*/
	private Parameters parameter;
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
	@ManyToOne
	@JoinColumn(name="WORKPIECEPROCESS_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public InventoryProcessMapping getWorkpieceProcess() {
		return workpieceProcess;
	}
	public void setWorkpieceProcess(InventoryProcessMapping workpieceProcess) {
		this.workpieceProcess = workpieceProcess;
	}
	@ManyToOne
	@JoinColumn(name="PARAMETER_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	public Parameters getParameter() {
		return parameter;
	}
	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}
	public Float getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Float standardValue) {
		this.standardValue = standardValue;
	}
}
