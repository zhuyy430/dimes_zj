package com.digitzones.model;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 安环等级
 * @author zdq
 * 2018年6月5日
 */
@Entity
@Table(name="SECUREENVIRONMENTGRADE")
public class SecureEnvironmentGrade implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**安环等级名称*/
	private String name;
	/**安环编码*/
	private String code;
	/**说明*/
	private String note;
	/**
	 * 权重：值越大，代表越严重
	 * 预定义：1~10代表严重等级从1到10
	 */
	private Integer weight;
	/** 颜色 */
	private String color;
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
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
