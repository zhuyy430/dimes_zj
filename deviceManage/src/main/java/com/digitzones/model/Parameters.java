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
 * 参数
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PARAMETERS")
public class Parameters {
	private Boolean kfc;
	private ParameterType parameterType;
	/**记录是工艺参数还是设备参数
	 * DEVICE:设备参数
	 * ART:工艺参数
	 * */
	private String baseCode;
	/**取值规则*/
	private String rules;
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	@ManyToOne
	@JoinColumn(name="PARAMETERTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ParameterType getParameterType() {
		return parameterType;
	}
	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}
	public Boolean getKfc() {
		return kfc;
	}
	public void setKfc(Boolean kfc) {
		this.kfc = kfc;
	}
	public String getBaseCode() {
		return baseCode;
	}
	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}
	/**对象唯一标识*/
	protected Long id;
	/**编号*/
	protected String code;
	/**名称*/
	protected String name;
	/**备注*/
	protected String note;
	/**是否停用*/
	protected Boolean disabled = false;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
