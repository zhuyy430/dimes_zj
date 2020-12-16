package com.digitzones.devmgr.model;
import java.io.Serializable;
import java.util.Date;
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
import com.digitzones.model.ProjectType;
/**
 * 全局项目标准
 * 设备项目：点检项目，润滑项目，保养 项目，故障原因，维修项目
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="DEVICEPROJECT")
public class DeviceProject implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	/**项目编号*/
	private String code;
	/**项目名称*/
	private String name;
	/**标准*/
	private String standard;
	/**方法*/
	private String method;
	/**备注*/
	private String remark;
	/**频次*/
	private String frequency;
	/**说明*/
	private String note;
	/**设备类别*/
	private ProjectType projectType;
	/**设备项目类型：
	 * SPOTINSPECTION:点检
	 * LUBRICATION：润滑
	 * MAINTAIN:保养
	 * BREAKDOWNREASON：故障原因
	 * MAINTENANCEITEM：维修项目
	 * 字段值参照Constant.DeviceProject中的常量
	 * */
	private String type;
	private Date createTime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PROJECTTYPE_ID",foreignKey=@ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
