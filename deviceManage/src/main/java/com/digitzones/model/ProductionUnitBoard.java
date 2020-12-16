package com.digitzones.model;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 生产单元实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="ProductionUnitBoard")
public class ProductionUnitBoard{
	private long id;
	private ProductionUnit productionUnit;
	private String title_1;
	private String title_2;
	private String body_1;
	private String body_2;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PRODUCTIONUNIT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProductionUnit getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}
	public String getTitle_1() {
		return title_1;
	}
	public void setTitle_1(String title_1) {
		this.title_1 = title_1;
	}
	public String getTitle_2() {
		return title_2;
	}
	public void setTitle_2(String title_2) {
		this.title_2 = title_2;
	}
	public String getBody_1() {
		return body_1;
	}
	public void setBody_1(String body_1) {
		this.body_1 = body_1;
	}
	public String getBody_2() {
		return body_2;
	}
	public void setBody_2(String body_2) {
		this.body_2 = body_2;
	}
}
