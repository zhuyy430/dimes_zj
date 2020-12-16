package com.digitzones.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;
/**
 * 装备类别
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="EQUIPMENTTYPE")
public class EquipmentType{
	/**父类别*/
	private EquipmentType parent;
	/**记录是量具还是装备*/
	private String baseCode;
	/**子类别*/
	private Set<EquipmentType> children;
	@OneToMany(fetch=FetchType.EAGER,mappedBy="parent")
	@OrderBy("ID DESC")
	public Set<EquipmentType> getChildren() {
		return children;
	}
	public void setChildren(Set<EquipmentType> children) {
		this.children = children;
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
	/**规格型号*/
	private String unitType;
	/**制造商*/
	private String manufacturer;
	/**经销商*/
	private String trader;
	/**出厂日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date outFactoryDate;
	/**计量目标*/
	private Float measurementObjective = 0f;
	/**计量类型*/
	private String measurementType;
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public Date getOutFactoryDate() {
		return outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	public Float getMeasurementObjective() {
		return measurementObjective;
	}

	public void setMeasurementObjective(Float measurementObjective) {
		this.measurementObjective = measurementObjective;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	@JsonIgnore
	public EquipmentType getParent() {
		return parent;
	}
	public void setParent(EquipmentType parent) {
		this.parent = parent;
	}
}
