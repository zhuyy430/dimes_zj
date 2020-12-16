package com.digitzones.procurement.model;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitzones.model.Employee;
import com.digitzones.model.ProductionUnit;
/**
 * 工件工序关联实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PERSON_PRODUCTIONUNIT")
public class PersonProductionUnitMapping {
	/**对象标识*/
	private Long id;
	/**工件*/
	private Employee person;
	/**工序*/
	private ProductionUnit productionUnit;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="PERSON_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Employee getPerson() {
		return person;
	}
	public void setPerson(Employee person) {
		this.person = person;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PRODUCTIONUNIT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
}
