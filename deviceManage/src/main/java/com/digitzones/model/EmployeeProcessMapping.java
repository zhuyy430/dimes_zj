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
/**
 * 人员和工序关联实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="EMPLOYEE_PROCESS")
public class EmployeeProcessMapping {
	private Long id;
	private Employee employee;
	private Processes process;
	private SkillLevel skillLevel;
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
	public SkillLevel getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(SkillLevel skillLevel) {
		this.skillLevel = skillLevel;
	}
	@ManyToOne
	@JoinColumn(name="EMPLOYEE_ID",referencedColumnName = "cPsn_Num",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	@ManyToOne
	@JoinColumn(name="PROCESS_ID",referencedColumnName = "code")
	public Processes getProcess() {
		return process;
	}
	public void setProcess(Processes process) {
		this.process = process;
	}
}
