package com.digitzones.vo;
import com.digitzones.model.ParameterType;
/**
 * 参数
 * @author zhuyy430
 *
 */
public class ParametersVO {
	private String kfc;
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
	public ParameterType getParameterType() {
		return parameterType;
	}
	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}
	public String getKfc() {
		return kfc;
	}
	public void setKfc(String kfc) {
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
