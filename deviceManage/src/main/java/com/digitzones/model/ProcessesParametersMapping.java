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
 * 工序和参数关联实体
 * @author zdq
 * 2018年6月14日
 */
@Entity
@Table(name="PROCESSES_PARAMETERS")
public class ProcessesParametersMapping {
	private Long id;
	private Processes processes;
	private Parameters parameters;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="PROCESS_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Processes getProcesses() {
		return processes;
	}
	public void setProcesses(Processes processes) {
		this.processes = processes;
	}
	@ManyToOne
	@JoinColumn(name="PARAMETER_ID")
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
}
