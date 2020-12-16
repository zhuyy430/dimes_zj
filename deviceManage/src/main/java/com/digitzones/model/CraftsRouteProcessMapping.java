package com.digitzones.model;

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

import com.digitzones.procurement.model.CraftsRoute;
/**
 * 工艺路线工序关联实体
 * @author zdq
 * 2018年6月3日
 */
@Entity
@Table(name="CRAFTSROUTE_PROCESS")
public class CraftsRouteProcessMapping {
	/**对象标识*/
	private Long id;
	/**工艺路线*/
	private CraftsRoute craftsRoute;
	/**工序*/
	private Processes process;
	/**参数取值
	 * 固定值
	 * 变动值
	 * */
	private String parameterValueType;
	/**工艺路线 */
	private Long processRoute;
	public Long getProcessRoute() {
		return processRoute;
	}
	public void setProcessRoute(Long processRoute) {
		this.processRoute = processRoute;
	}
	public String getParameterValueType() {
		return parameterValueType;
	}
	public void setParameterValueType(String parameterValueType) {
		this.parameterValueType = parameterValueType;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PROCESS_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public Processes getProcess() {
		return process;
	}
	@ManyToOne
	@JoinColumn(name="CRAFTSROUTE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public CraftsRoute getCraftsRoute() {
		return craftsRoute;
	}
	public void setCraftsRoute(CraftsRoute craftsRoute) {
		this.craftsRoute = craftsRoute;
	}
	public void setProcess(Processes process) {
		this.process = process;
	}
}
