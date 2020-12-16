package com.digitzones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 装备维修记录
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="EQUIPMENTREPAIRRECORD")
public class EquipmentRepairRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	/**唯一标识*/
	private Long id;
	/**装备*/
	private Equipment equipment;
	/**维修时间*/
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date repairDate;
	/**维修人员名称*/
	private String repairEmployeeName;
	/**维修人员代码*/
	private String repairEmployeeCode;
	/**维修说明*/
	private String note;
	/**维修原因*/
	private PressLight pressLight;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="PRESSLIGHT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public PressLight getPressLight() {
		return pressLight;
	}

	public void setPressLight(PressLight pressLight) {
		this.pressLight = pressLight;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	@ManyToOne
	@JoinColumn(name="EQUIPMENT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Date getRepairDate() {
		return repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}

	public String getRepairEmployeeName() {
		return repairEmployeeName;
	}

	public void setRepairEmployeeName(String repairEmployeeName) {
		this.repairEmployeeName = repairEmployeeName;
	}

	public String getRepairEmployeeCode() {
		return repairEmployeeCode;
	}

	public void setRepairEmployeeCode(String repairEmployeeCode) {
		this.repairEmployeeCode = repairEmployeeCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
