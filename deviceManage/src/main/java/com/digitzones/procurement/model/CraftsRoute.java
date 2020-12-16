package com.digitzones.procurement.model;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
/**
 * 工艺路线
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class CraftsRoute implements Serializable {
	private static final long serialVersionUID = 1L;
    /**编码*/
	@Id
    @GeneratedValue(generator = "id_generator")
	private String id;
	/**工艺路线代码*/
	private String code;
	/**工艺路线名称*/
	private String name;
	/**版本*/
	private String version;
	/**备注*/
	private String note;
	/**是否停用*/
	private Boolean disabled = false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
