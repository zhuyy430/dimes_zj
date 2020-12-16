package com.digitzones.model;
/**
 * 员工和员工技能技能关联实体
 * @author zdq
 * 2018年6月2日
 */
import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="EMPLOYEE_SKILL")
public class EmployeeSkillMapping {
	private Long id;
	private Employee employee;
	private Date updateDate;
	private ProcessSkillLevel skillLevel;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="SKILLLEVEL_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProcessSkillLevel getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(ProcessSkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}
	@ManyToOne
	@JoinColumn(name="EMPLOYEE_CODE",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
