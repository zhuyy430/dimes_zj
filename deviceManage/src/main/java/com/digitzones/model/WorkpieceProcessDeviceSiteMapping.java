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

import com.digitzones.procurement.model.InventoryProcessMapping;
/**
 *  工件工序和站点关联实体
 * @author zdq
 * 2018年6月5日
 */
@Entity
@Table(name="WORKPIECEPROCESS_DEVICESITE")
public class WorkpieceProcessDeviceSiteMapping {
	private Long id;
	/**加工节拍*/
	private Float processingBeat=0f;
	/**工件工序关联实体*/
	private InventoryProcessMapping workpieceProcess;
	/**站点*/
	private DeviceSite deviceSite;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getProcessingBeat() {
		return processingBeat;
	}
	public void setProcessingBeat(Float processingBeat) {
		this.processingBeat = processingBeat;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="WORKPIECEPROCESS_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public InventoryProcessMapping getWorkpieceProcess() {
		return workpieceProcess;
	}
	public void setWorkpieceProcess(InventoryProcessMapping workpieceProcess) {
		this.workpieceProcess = workpieceProcess;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DEVICESITE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
	public DeviceSite getDeviceSite() {
		return deviceSite;
	}
	public void setDeviceSite(DeviceSite deviceSite) {
		this.deviceSite = deviceSite;
	}
}
