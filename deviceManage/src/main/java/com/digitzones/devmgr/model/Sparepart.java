package com.digitzones.devmgr.model;
import java.io.Serializable;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.digitzones.model.ProjectType;
/**
 * 备件实体
 * @author zdq
 * 2018年12月25日
 */
@Entity
@Table(name="SPAREPART")
public class Sparepart implements Serializable {
	private static final long serialVersionUID = -4711833140793307948L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**备件代码*/
	private String code;
	/**备件名称*/
	private String name;
	/**规格型号*/
	private String unitType;
	/**制造商，生产厂家*/
	private String manufacturer;
	/**图号*/
	private String graphNumber;
	/**计量单位*/
	private String measurementUnit;
	/**助记码*/
	private String mnemonicCode;
	/**安全库存*/
	private String inventory;
	/**所属类别*/
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	private ProjectType projectType;
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
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	public String getMnemonicCode() {
		return mnemonicCode;
	}
	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
