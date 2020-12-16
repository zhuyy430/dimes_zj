package com.digitzones.devmgr.model;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.digitzones.model.ProductionUnit;

/**
 * 维修人员与生产单元的关联
 * @author zhuyy430
 *
 */
@Entity
public class EmployeeProductionUnitRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**员工编码*/
	private String employeeCode;
	/**员工名称*/
	private String employeeName;
	/**生产单元*/
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PRODUCTIONUNIT_ID",foreignKey=@ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))	
	private ProductionUnit productionUnit;
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
}
