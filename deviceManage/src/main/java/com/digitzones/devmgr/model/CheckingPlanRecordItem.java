package com.digitzones.devmgr.model;
import java.io.Serializable;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 * 设备点检记录项
 * @author zdq
 * 2018年12月25日
 */
@Entity
public class CheckingPlanRecordItem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 点检项目代码
	 */
	private String code;
	/**
	 * 点检项目名称
	 */
	private String name;
	/**
	 * 标准
	 */
	private String standard;
	/**上限值*/
	private String upperLimit;
	/**下限值*/
	private String lowerLimit;
	/**点检值*/
	private String checkValue;
	/**方法*/
	private String method;
	/**频次*/
	private String frequency;
	/**结果*/
	private String result;
	/**备注*/
	private String note;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@ManyToOne
	@JoinColumn(name="CHECKINGPLANRECORD_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private CheckingPlanRecord checkingPlanRecord;
	public CheckingPlanRecord getCheckingPlanRecord() {
		return checkingPlanRecord;
	}
	public void setCheckingPlanRecord(CheckingPlanRecord checkingPlanRecord) {
		this.checkingPlanRecord = checkingPlanRecord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public String getCheckValue() {
		return checkValue;
	}
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
}
