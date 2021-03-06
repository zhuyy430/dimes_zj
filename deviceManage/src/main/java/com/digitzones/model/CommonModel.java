package com.digitzones.model;
import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 实体类的公共父类
 * @author zdq
 */
/*@Entity  
@Inheritance(strategy=InheritanceType.JOINED) 
//@Table(name="COMMONMODEL")
*/public class CommonModel implements Serializable {
	private static final long serialVersionUID = 1L;
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
