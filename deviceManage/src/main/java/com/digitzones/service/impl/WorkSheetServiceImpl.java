package com.digitzones.service.impl;

import com.digitzones.bo.WorkSheetBo;
import com.digitzones.constants.Constant;
import com.digitzones.dao.IWorkSheetDao;
import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.dao.IWorkSheetDetailParametersRecordDao;
import com.digitzones.model.*;
import com.digitzones.procurement.dao.IMom_orderdetailStatusDao;
import com.digitzones.procurement.model.Mom_recorddetailStatus;
import com.digitzones.service.IEquipmentDeviceSiteMappingService;
import com.digitzones.service.IWorkSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Service
public class WorkSheetServiceImpl implements IWorkSheetService {
	private IWorkSheetDao workSheetDao;
	@Autowired
	private IMom_orderdetailStatusDao mom_orderdetailStatusDao;
	@Autowired
	private IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService;
	private IWorkSheetDetailDao workSheetDetailDao;
	private IWorkSheetDetailParametersRecordDao workSheetDetailParametersRecordDao;
	@Autowired
	public void setWorkSheetDetailParametersRecordDao(
			IWorkSheetDetailParametersRecordDao workSheetDetailParametersRecordDao) {
		this.workSheetDetailParametersRecordDao = workSheetDetailParametersRecordDao;
	}
	@Autowired
	public void setWorkSheetDetailDao(IWorkSheetDetailDao workSheetDetailDao) {
		this.workSheetDetailDao = workSheetDetailDao;
	}
	@Autowired
	public void setWorkSheetDao(IWorkSheetDao workSheetDao) {
		this.workSheetDao = workSheetDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workSheetDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(WorkSheet obj) {
		workSheetDao.update(obj);
	}

	@Override
	public WorkSheet queryByProperty(String name, String value) {
		return workSheetDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(WorkSheet obj) {
		return workSheetDao.save(obj);
	}

	@Override
	public WorkSheet queryObjById(Serializable id) {
		return workSheetDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.workSheet.id=?",new Object[] {id});
		if(workSheetDetails!=null && workSheetDetails.size()>0) {
			for(WorkSheetDetail detail:workSheetDetails) {
				List<WorkSheetDetailParametersRecord> records = workSheetDetailParametersRecordDao.findByHQL("from WorkSheetDetailParametersRecord record where record.workSheetDetail.id=?",new Object[]{detail.getId()});
				for(WorkSheetDetailParametersRecord record : records){
					workSheetDetailParametersRecordDao.deleteById(record.getId());
				}
				workSheetDetailDao.delete(detail);
			}
		}
		
		//WorkSheet workSheet = workSheetDao.findById(id);
		//workSheet.setDeleted(true);
		workSheetDao.deleteById(id);
	}
	@Override
	public void deleteDetails(Serializable id) {
		List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.workSheet.id=?",new Object[] {id});
		if(workSheetDetails!=null && workSheetDetails.size()>0) {
			for(WorkSheetDetail detail:workSheetDetails) {
				List<WorkSheetDetailParametersRecord> records = workSheetDetailParametersRecordDao.findByHQL("from WorkSheetDetailParametersRecord record where record.workSheetDetail.id=?",new Object[]{detail.getId()});
				for(WorkSheetDetailParametersRecord record : records){
					workSheetDetailParametersRecordDao.deleteById(record.getId());
				}
				workSheetDetailDao.delete(detail);
			}
		}
	}

	@Override
	public Pager queryByMonitoring(String sql, int pageNo, int pageSize, Object... values) {
		return workSheetDao.queryByMonitoring(sql,pageNo,pageSize,values);
	}

	@Override
	public List<WorkSheetBo> queryByMonitoring(String sql, Object... values) {
		return workSheetDao.queryByMonitoring(sql,values);
	}


	@Override
	public List<WorkSheet> queryWorkSheetByDeviceSiteId(Long deviceSiteId) {
		String hql = "from WorkSheet ws where ws.id in "
				+ "(select wsd.workSheet.id from WorkSheetDetail wsd where wsd.deviceSiteId=?0)"
				/*	+ " and ws.id not in (select pr.workSheetId from ProcessRecord pr where pr.deviceSiteId=?0 and pr.deleted=?2)"*/
				+ " and ws.deleted=?1";
		return workSheetDao.findByHQL(hql, new Object[] {deviceSiteId,false});
	}

	/*	public List<WorkSheet> queryWorkSheetByDeviceSiteIdAndConditions(Long deviceSiteId,String q){
		String hql = "from WorkSheet ws where ws.id in "
				+ "(select wsd.workSheet.id from WorkSheetDetail wsd where wsd.deviceSiteId=?0)"
				+ " and ws.id not in (select pr.workSheetId from ProcessRecord pr where pr.deviceSiteId=?0 and pr.deleted=?3)"
				+ " and ws.deleted=?1 and (ws.no like ?2 or ws.workPieceCode like ?2 or ws.customerGraphNumber like ?2"
				+ " or ws.batchNumber like ?2 or ws.stoveNumber like ?2 or ws.productCount like ?2)";
		return workSheetDao.findByHQL(hql, new Object[] {deviceSiteId,false,"%"+q+"%",false});
	}*/

	@Override
	public void addWorkSheet(WorkSheet workSheet,List<WorkSheetDetail> list,Boolean change) {
		if(change){
			workSheetDao.update(workSheet);
			deleteDetails(workSheet.getId());
		}else{
			workSheetDao.save(workSheet);
		}
		if(workSheet.getFromErp()&&!change){
			Integer modId = workSheet.getMoDId();
			Mom_recorddetailStatus mom = new Mom_recorddetailStatus();
			mom.setModId(modId.toString());
			mom.setStatus(true);
			mom_orderdetailStatusDao.save(mom);
		}
		//for(WorkSheetDetail detail : Constant.workSheetDetail) {
		for(int i = 0;i<list.size();i++) {
			WorkSheetDetail detail = list.get(i);
			detail.setWorkSheet(workSheet);
			workSheetDetailDao.save(detail);

			Set<WorkSheetDetailParametersRecord> parameterRecords = detail.getParameterRecords();
			if(parameterRecords!=null) {
				for(WorkSheetDetailParametersRecord wsdpr : parameterRecords) {
					WorkSheetDetailParametersRecord paramRecord = new WorkSheetDetailParametersRecord();
					paramRecord.setWorkSheetDetail(detail);
					paramRecord.setCurrentDate(wsdpr.getCurrentDate());
					paramRecord.setDeleted(wsdpr.getDeleted());
					paramRecord.setId(wsdpr.getId());
					paramRecord.setLowLine(wsdpr.getLowLine());
					paramRecord.setUpLine(wsdpr.getUpLine());
					paramRecord.setStandardValue(wsdpr.getStandardValue());
					paramRecord.setNote(wsdpr.getNote());
					paramRecord.setParameterCode(wsdpr.getParameterCode());
					paramRecord.setParameterName(wsdpr.getParameterName());
					paramRecord.setParameterValue(wsdpr.getParameterValue());
					paramRecord.setStatus(wsdpr.getStatus());
					paramRecord.setStatusCode(wsdpr.getStatusCode());
					paramRecord.setUnit(wsdpr.getUnit());
					workSheetDetailParametersRecordDao.save(paramRecord);
				}
			}
		}
	}

	@Override
	public void updateWorkSheetAndWorkSheetDetails(WorkSheet workSheet, List<WorkSheetDetail> workSheetDetails) {
		WorkSheet ws = workSheetDao.findById(workSheet.getId());
		ws.setBatchNumber(workSheet.getBatchNumber());
		ws.setManufactureDate(workSheet.getManufactureDate());
		ws.setProductCount(workSheet.getProductCount());
		ws.setStoveNumber(workSheet.getStoveNumber());
		ws.setNote(workSheet.getNote());
		ws.setProductionUnitId(workSheet.getProductionUnitId());
		ws.setProductionUnitName(workSheet.getProductionUnitName());
		ws.setProductionUnitCode(workSheet.getProductionUnitCode());
		//更新工单
		workSheetDao.update(ws);
		//更新工单详情
		if(workSheetDetails!=null ) {
			for(int i = 0;i<workSheetDetails.size();i++) {
				WorkSheetDetail detail = workSheetDetails.get(i);
				WorkSheetDetail d = workSheetDetailDao.findById(detail.getId());
				d.setCompleteCount(detail.getCompleteCount());
				d.setStatus(detail.getStatus());
				d.setNote(detail.getNote());
				d.setUnqualifiedCount(detail.getUnqualifiedCount());
				d.setQualifiedCount(detail.getQualifiedCount());
				d.setRepairCount(detail.getRepairCount());
				d.setReportCount(detail.getReportCount());
				d.setScrapCount(detail.getScrapCount());
				d.setProductionCount(detail.getProductionCount());
				workSheetDetailDao.update(d);
			}
		}
	}

	@Override
	public List<WorkSheet> queryWorkSheetsByDeviceSiteId(Long deviceSiteId) {
		String hql = "select ws from WorkSheetDetail wsd inner join wsd.workSheet ws  where wsd.deviceSiteId=?0 and ws.deleted=?1";
		return workSheetDao.findByHQL(hql, new Object[] {deviceSiteId,false});
	}
	@Override
	public WorkSheet queryRunningWorkSheetByDeviceSiteId(Long deviceSiteId) {
		String hql = "select ws from WorkSheetDetail wsd inner join wsd.workSheet ws  where wsd.deviceSiteId=?0 and ws.deleted=?1" +
				" and ws.status=?2 ";
		List<WorkSheet> list = workSheetDao.findByHQL(hql, new Object[]{deviceSiteId, false, Constant.WorkSheet.Status.PROCESSING});
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else{
			return list.get(0);
		}
	}
	@Override
	public List<WorkSheet> queryWorkSheetsByDeviceSiteIdAndConditions(Long deviceSiteId, String q) {
		String hql = "select ws from WorkSheetDetail wsd inner join wsd.workSheet ws where wsd.deviceSiteId=?0 and ws.deleted=?1"
				+ " and (ws.no like ?2 or ws.workPieceCode like ?2 or ws.customerGraphNumber like ?2" + 
				" or ws.batchNumber like ?2 or ws.stoveNumber like ?2 or ws.productCount like ?2 or ws.status like ?2)";
		return workSheetDao.findByHQL(hql, new Object[] {deviceSiteId,false,"%"+q+"%"});
	}

	@Override
	public void start(Long id){
		WorkSheet workSheet = workSheetDao.findById(id);
		workSheet.setStatus(Constant.WorkSheet.Status.PROCESSING);
		workSheetDao.update(workSheet);
		List<WorkSheetDetail> curlist = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deleted!=?1", new Object[] {id,true});
		if(!CollectionUtils.isEmpty(curlist)) {
			for(WorkSheetDetail detail : curlist) {
				if(detail.getCompleteCount()>=workSheet.getProductCount()) {
					detail.setStatus(Constant.WorkSheet.Status.COMPLETE);
					//detail.setCompleteTime(new Date());
				}else {
					detail.setStatus(Constant.WorkSheet.Status.PROCESSING);
				}
				workSheetDetailDao.update(detail);
			}
		}
		//查询当前生产单元下开工中的工单
		/*List<WorkSheet> workSheetsList = workSheetDao.findByHQL("from WorkSheet ws where ws.productionUnitId=?0 and ws.status=?1 "
				+ " and ws.deleted=?2 and ws.id !=?3", 
				new Object[] {workSheet.getProductionUnitId(),Constant.WorkSheet.Status.PROCESSING,false,id});
		if(!CollectionUtils.isEmpty(workSheetsList)) {
			for(WorkSheet ws : workSheetsList) {
				boolean isComplete = true;
				List<WorkSheetDetail> list = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deleted!=?1", new Object[] {ws.getId(),true});
				if(!CollectionUtils.isEmpty(list)) {
					for(WorkSheetDetail detail : list) {
						if(detail.getCompleteCount()>=ws.getProductCount()) {
							detail.setStatus(Constant.WorkSheet.Status.COMPLETE);
							//detail.setCompleteTime(new Date());
						}else {
							detail.setStatus(Constant.WorkSheet.Status.STOP);
							isComplete = false;
						}
						workSheetDetailDao.update(detail);
					}
				}
				if(isComplete) {
					ws.setStatus(Constant.WorkSheet.Status.COMPLETE);
					//ws.setCompleteTime(new Date());
				}else {
					ws.setStatus(Constant.WorkSheet.Status.STOP);
				}
				workSheetDao.update(ws);
			}
		}*/
	}
	@Override
	public void stop(Long id){
		//将工单状态设置为"开工"
		WorkSheet ws = workSheetDao.findById(id);
		
		boolean isComplete = true;
		List<WorkSheetDetail> list = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deleted!=?1", new Object[] {ws.getId(),true});
		if(!CollectionUtils.isEmpty(list)) {
			for(WorkSheetDetail detail : list) {
				if(detail.getCompleteCount()>=ws.getProductCount()) {
					detail.setStatus(Constant.WorkSheet.Status.COMPLETE);
					//detail.setCompleteTime(new Date());
				}else {
					detail.setStatus(Constant.WorkSheet.Status.STOP);
					isComplete = false;
				}
				workSheetDetailDao.update(detail);
			}
		}
		if(isComplete) {
			ws.setStatus(Constant.WorkSheet.Status.COMPLETE);
			//ws.setCompleteTime(new Date());
		}else {
			ws.setStatus(Constant.WorkSheet.Status.STOP);
		}
		workSheetDao.update(ws);
	}
	
	
	
	//工单开工，其他使用当前工单中的设备站点的工单停工
/*	@Override
	public void start(Long id) throws CloneNotSupportedException {
		//将工单状态设置为"开工"
		WorkSheet workSheet = workSheetDao.findById(id);
		workSheet.setStatus(Constant.WorkSheet.Status.PROCESSING);
		workSheetDao.update(workSheet);
		
		List<Long> deviceSiteIds = new ArrayList<>();
		//将工单详情中的设备站点设置为"开工"状态
		List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.workSheet.id=? and detail.deleted=? and detail.deviceSiteId is not null",new Object[] {id,false});
		if(workSheetDetails!=null && workSheetDetails.size()>0) {
			for(WorkSheetDetail detail : workSheetDetails) { 
				detail.setStatus(Constant.WorkSheet.Status.PROCESSING);
				workSheetDetailDao.update(detail);
				
				deviceSiteIds.add(detail.getDeviceSiteId());
			}
		}
		//将其他使用当前工单下的设备站点的工单详情设置为"停工"
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkSheetDetail.class);
		criteria.add(Restrictions.in("deviceSiteId", deviceSiteIds))
		.add(Restrictions.eq("status", Constant.WorkSheet.Status.PROCESSING))
		.add(Restrictions.eq("deleted", false));
		
		DetachedCriteria workSheetCriteria = DetachedCriteria.forClass(WorkSheet.class);
		workSheetCriteria.add(Restrictions.ne("id", id));
		criteria.add(Property.forName("workSheet").in(workSheetDetails));
		List<WorkSheetDetail> workSheetDetailList = workSheetDetailDao.findByCriteria(criteria);
		if(workSheetDetailList!=null && workSheetDetailList.size()>0) {
			//标识工单是否完工
			boolean complete = false;
			for(WorkSheetDetail detail : workSheetDetailList) {
				
				detail.setStatus(Constant.WorkSheet.Status.STOP);
				workSheetDetailDao.update(detail);
			}
			
			WorkSheet ws = workSheetDetailList.get(0).getWorkSheet();
			ws.setStatus(Constant.WorkSheet.Status.STOP);
			workSheetDao.update(workSheet);
		}
	}
*/

	@Override
	public List<WorkSheet> queryWorkingWorkSheetsByProductionUnitCode(String productionUnitCode) {
		String hql = "from WorkSheet ws where ws.productionUnitCode=?0 and exists (from WorkSheetDetail detail where detail.status=1 and detail.workSheet.id=ws.id)";
		return workSheetDao.findByHQL(hql,new Object[] {productionUnitCode});
	}

	@Override
	public void updateOver(Long id, User user) {
		WorkSheet ws = workSheetDao.findById(id);
		List<WorkSheetDetail> list = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deleted!=?1", new Object[] {ws.getId(),true});
		if(!CollectionUtils.isEmpty(list)) {
			for(WorkSheetDetail detail : list) {
				detail.setStatus(Constant.WorkSheet.Status.COMPLETE);
				detail.setCompleteTime(new Date());
				workSheetDetailDao.update(detail);

				//解除站点装备关联
				List<EquipmentDeviceSiteMapping> edms=equipmentDeviceSiteMappingService.queryListByDeviceSiteCodeAndEquipmentTypeId(detail.getDeviceSiteCode(),null,ws.getNo());
				for(EquipmentDeviceSiteMapping edm:edms){
					EquipmentDeviceSiteMapping equipmentDeviceSiteMapping = equipmentDeviceSiteMappingService.queryObjById(edm.getId());
					if(user!=null) {
						equipmentDeviceSiteMapping.setUnbindUserId(user.getId());
						equipmentDeviceSiteMapping.setUnbindUsername(user.getUsername());
					}
					equipmentDeviceSiteMapping.setUnbind(true);
					equipmentDeviceSiteMapping.setUnbindDate(new Date());
					equipmentDeviceSiteMappingService.updateObj(equipmentDeviceSiteMapping);
				}
			}
		}
		ws.setCompleteTime(new Date());
		ws.setStatus(Constant.WorkSheet.Status.COMPLETE);
		workSheetDao.update(ws);
	}

	/**
	 * 根据制单日期查找最大单号
	 *
	 * @param now
	 * @return
	 */
	@Override
	public String queryMaxNoByMakeDocumentDate(Date now) {
		return workSheetDao.queryMaxNoByMakeDocumentDate(now);
	}
	/**
	 * 根据报工条码查找工单
	 * @param barCode
	 * @return
	 */
	@Override
	public WorkSheet queryByBarCode(String barCode) {
		return workSheetDao.queryByBarCode(barCode);
	}
}
