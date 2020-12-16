package com.digitzones.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 按灯信息
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PRESSLIGHT")
public class PressLight implements Serializable {
	private static final long serialVersionUID = 1L;
	/**唯一标识*/
	private Long id;
	/**代码*/
	private String code;
	/**原因*/
	private String reason;
	/**是否停机*/
	private Boolean halt = false;
	/**详细描述*/
	private String description;
	/**备注*/
	private String note;
	/**是否停用*/
	private Boolean disabled;
	/**是否为计划停机*/
	private Boolean planHalt = false;
	/**按灯类别*/
	private PressLightType pressLightType;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Boolean getHalt() {
		return halt;
	}
	public void setHalt(Boolean halt) {
		this.halt = halt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	public Boolean getPlanHalt() {
		return planHalt;
	}
	public void setPlanHalt(Boolean planHalt) {
		this.planHalt = planHalt;
	}
	@ManyToOne
	@JoinColumn(name="PRESSLIGHTTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public PressLightType getPressLightType() {
		return pressLightType;
	}
	public void setPressLightType(PressLightType pressLightType) {
		this.pressLightType = pressLightType;
	}
}
