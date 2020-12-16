package com.digitzones.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 质量等级
 * @author zdq
 * 2018年6月5日
 */
@Entity
@Table(name="QUALITYGRADE")
public class QualityGrade {
	private Long id;
	/**质量等级名称*/
	private String name;
	/**质量等级编码*/
	private String code;
	/**说明*/
	private String note;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
