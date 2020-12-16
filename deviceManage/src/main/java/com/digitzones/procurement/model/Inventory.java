package com.digitzones.procurement.model;
import org.hibernate.annotations.Subselect;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * 物料
 */
@Entity
@Subselect("select * from WORKPIECE")
public class Inventory {
	/*物料编码（key）*/
	@Id
	/*物料代码*/
	@Column(name="cInvCode")
	private String code;
	/*物料名称*/
	@Column(name="cInvName")
	private String name;
	/*物料规格型号*/
	@Column(name="cInvStd")
	private String unitType;
	/*物料大类编码*/
	@Column(name="cInvCCode")
	private String cInvCCode;
	/**料耗*/
	private Double cInvDefine14;
	/*物料大类名称*/
	@Column(name="cInvCName")
	private String cInvCName;
	/*货位编码*/
	@Column(name="cPosition")
	private String cPosition;
	/*是否外购*/
	@Column(name="bPurchase")
	private Boolean bPurchase;
	/*是否自制*/
	@Column(name="bSelf")
	private Boolean bSelf;
	/*是否销售*/
	@Column(name="bSale")
	private Boolean bSale;
	/*安全库存*/
	@Column(name="iSafeNum")
	private String iSafeNum;
	/*最高库存*/
	@Column(name="iTopSum")
	private String iTopSum;
	/*最低库存*/
	@Column(name="iLowSum")
	private String iLowSum;
	/*主计量单位编码*/
	@Column(name="cComUnitCode")
	private String cComUnitCode;
	/*计量单位名称*/
	@Column(name="cComUnitName")
	private String measurementUnit;
	/*工程图号*/
	@Column(name="cEngineerFigNo")
	private String graphNumber;
	/*默认仓库*/
	@Column(name="cDefWareHouse")
	private String cDefWareHouse;

	public String getcDefWareHouse() {
		return cDefWareHouse;
	}

	public void setcDefWareHouse(String cDefWareHouse) {
		this.cDefWareHouse = cDefWareHouse;
	}

	public Double getcInvDefine14() {
		if(cInvDefine14==null||cInvDefine14<=0){
			cInvDefine14=1d;
		}

		if(!code.startsWith("JL")){
			cInvDefine14=1d;
		}
		return cInvDefine14;
	}
	public void setcInvDefine14(Double cInvDefine14) {
		this.cInvDefine14 = cInvDefine14;
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
	public String getcInvCCode() {
		return cInvCCode;
	}
	public void setcInvCCode(String cInvCCode) {
		this.cInvCCode = cInvCCode;
	}
	public String getcInvCName() {
		return cInvCName;
	}
	public void setcInvCName(String cInvCName) {
		this.cInvCName = cInvCName;
	}
	public String getcPosition() {
		return cPosition;
	}
	public void setcPosition(String cPosition) {
		this.cPosition = cPosition;
	}
	public Boolean getbPurchase() {
		return bPurchase;
	}
	public void setbPurchase(Boolean bPurchase) {
		this.bPurchase = bPurchase;
	}
	public Boolean getbSelf() {
		return bSelf;
	}
	public void setbSelf(Boolean bSelf) {
		this.bSelf = bSelf;
	}
	public Boolean getbSale() {
		return bSale;
	}
	public void setbSale(Boolean bSale) {
		this.bSale = bSale;
	}
	public String getiSafeNum() {
		return iSafeNum;
	}
	public void setiSafeNum(String iSafeNum) {
		this.iSafeNum = iSafeNum;
	}
	public String getiTopSum() {
		return iTopSum;
	}
	public void setiTopSum(String iTopSum) {
		this.iTopSum = iTopSum;
	}
	public String getiLowSum() {
		return iLowSum;
	}
	public void setiLowSum(String iLowSum) {
		this.iLowSum = iLowSum;
	}
	public String getcComUnitCode() {
		return cComUnitCode;
	}
	public void setcComUnitCode(String cComUnitCode) {
		this.cComUnitCode = cComUnitCode;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	public String getGraphNumber() {
		return graphNumber;
	}
	public void setGraphNumber(String graphNumber) {
		this.graphNumber = graphNumber;
	}
	
}
