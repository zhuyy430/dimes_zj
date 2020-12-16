package com.digitzones.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
/**
 * 生产单元实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="PRODUCTIONUNIT")
public class ProductionUnit{
	/**父单元*/
	private ProductionUnit parent;
	/**目标产量*/
	private Integer goalOutput = 0;
	/**产线目标oee*/
	private Float goalOee=0f;
	/**产线损时目标 */
	private Float goalLostTime=0f;
	/**产线不合格目标 */
	private Float goalNg=0f;
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
	/**班次类别*/
	private ClassType classType;
	/**班次类别名称*/
	private String classTypeName;
	/**损耗率阀值*/
	private Float threshold = 0f;
	/**默认节拍*/
	private Integer beat;
    public String getClassTypeName() {
        return classType==null?"":classType.getName();
    }
    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }
    public String getCode() {
		return code;
	}
	/**是否允许多张工单同时开工*/
	private Boolean allowMultiWorkSheetRunning = false;
	public Boolean getAllowMultiWorkSheetRunning() {
		if(allowMultiWorkSheetRunning==null){
			allowMultiWorkSheetRunning=false;
		}
		return allowMultiWorkSheetRunning;
	}
	public void setAllowMultiWorkSheetRunning(Boolean allowMultiWorkSheetRunning) {
		this.allowMultiWorkSheetRunning = allowMultiWorkSheetRunning;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public Float getGoalLostTime() {
		return goalLostTime;
	}
	public Float getGoalNg() {
		return goalNg;
	}
	public Float getGoalOee() {
		return goalOee;
	}
	public Integer getGoalOutput() {
		return goalOutput;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getNote() {
		return note;
	}
	@ManyToOne
	@JoinColumn(name="PARENT_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	@JsonIgnore
	public ProductionUnit getParent() {
		return parent;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public void setGoalLostTime(Float goalLostTime) {
		this.goalLostTime = goalLostTime;
		if(this.goalLostTime==null) {
			this.goalLostTime = 0f;
		}
	}
	public void setGoalNg(Float goalNg) {
		this.goalNg = goalNg;
		if(this.goalNg == null) {
			this.goalNg = 0f;
		}
	}
	public void setGoalOee(Float goalOee) {
		this.goalOee = goalOee;
		if(this.goalOee == null) {
			this.goalOee = 0f;
		}
	}
	public void setGoalOutput(Integer goalOutput) {
		this.goalOutput = goalOutput;
		if(this.goalOutput == null) {
			this.goalOutput = 0;
		}
	}
    @ManyToOne
    @JoinColumn(name = "CLASSTYPE_ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setParent(ProductionUnit parent) {
		this.parent = parent;
	}
	public Float getThreshold() {
		return threshold;
	}
	public void setThreshold(Float threshold) {
		this.threshold = threshold;
		if(this.threshold==null)
			this.threshold = 0f;
	}
	public Integer getBeat() {
		return beat;
	}
	public void setBeat(Integer beat) {
		this.beat = beat;
	}
}
