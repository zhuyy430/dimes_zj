package com.digitzones.service.impl;

import com.digitzones.constants.Constant;
import com.digitzones.dao.*;
import com.digitzones.model.*;
import com.digitzones.procurement.dao.IInventoryProcessMappingDao;
import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.service.IWorkSheetDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
@Service
public class WorkSheetDetailServiceImpl implements IWorkSheetDetailService {
	private IWorkSheetDetailDao workSheetDetailDao;
	private IInventoryProcessMappingDao workPieceProcessMappingDao;
	private IWorkpieceProcessParameterMappingDao workpieceProcessParameterMappingDao;
	@Autowired
	private IWorkSheetDao workSheetDao;
	@Autowired
	private IProcessDeviceSiteMappingDao processDeviceSiteMappingDao;
	@Autowired
	private IWorkpieceProcessDeviceSiteMappingDao workpieceProcessDeviceSiteMappingDao;
	@Autowired
	public void setWorkpieceProcessParameterMappingDao(
			IWorkpieceProcessParameterMappingDao workpieceProcessParameterMappingDao) {
		this.workpieceProcessParameterMappingDao = workpieceProcessParameterMappingDao;
	}

	@Autowired
	public void setWorkPieceProcessMappingDao(IInventoryProcessMappingDao workPieceProcessMappingDao) {
		this.workPieceProcessMappingDao = workPieceProcessMappingDao;
	}

	@Autowired
	public void setWorkSheetDetailDao(IWorkSheetDetailDao workSheetDetailDao) {
		this.workSheetDetailDao = workSheetDetailDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workSheetDetailDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(WorkSheetDetail obj) {
		workSheetDetailDao.update(obj);
	}

	@Override
	public WorkSheetDetail queryByProperty(String name, String value) {
		return workSheetDetailDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(WorkSheetDetail obj) {
		return workSheetDetailDao.save(obj);
	}

	@Override
	public WorkSheetDetail queryObjById(Serializable id) {
		return workSheetDetailDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		workSheetDetailDao.deleteById(id);
	}

	@Override
	public List<WorkSheetDetail> queryWorkSheetDetailsByWorkSheetId(Long workSheetId) {
		return workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 ",new Object[] {workSheetId});
	}

	@Override
	public void buildWorkSheetDetailListInMemoryByWorkpieceId(Integer count,String workpieceCode,String productionUnitCode,List<WorkSheetDetail> workSheetDetails) {
		//根据工件id查询工件工序和站点对象
		//String hql = "from WorkpieceProcessMapping wpdsm where wpdsm.workpiece.code=?0";
		//List<InventoryProcessMapping> workpieceProcessMappings = workPieceProcessMappingDao.findByHQL(hql, new Object[] {workpieceCode});
			//根据工序查找设备站点

			List<InventoryProcessMapping> ipmList = workPieceProcessMappingDao.findByHQL("from InventoryProcessMapping ipm where ipm.inventory.code=?0 order by ipm.processRoute asc",
					new Object[] {workpieceCode});
			int index = 0;
			for (InventoryProcessMapping ipm:ipmList){
				List<ProcessDeviceSiteMapping> pdmList=processDeviceSiteMappingDao.findByHQL("from ProcessDeviceSiteMapping pdm where  pdm.processes.id=?0 and pdm.deviceSite.device.productionUnit.code=?1 ",new Object[] {ipm.getProcess().getId(),productionUnitCode});
				if(!pdmList.isEmpty()){

					int avgCount = 0;
					int yuShu = 0;
					if(count!=null && count!=0) {
						avgCount = count/pdmList.size();
						yuShu = count%pdmList.size();
					}
					for(ProcessDeviceSiteMapping pdsm : pdmList) {
						WorkSheetDetail detail = new WorkSheetDetail();
						detail.setId(Long.valueOf(index+1));
						detail.setProcessCode(pdsm.getProcesses().getCode());
						detail.setProcessId(pdsm.getProcesses().getId());
						detail.setProcessName(pdsm.getProcesses().getName());
						detail.setParameterSource(ipm.getParameterValueType());
						detail.setDeviceCode(pdsm.getDeviceSite().getDevice().getCode());
						detail.setDeviceName(pdsm.getDeviceSite().getDevice().getName());
						detail.setDeviceSiteCode(pdsm.getDeviceSite().getCode());
						detail.setDeviceSiteId(pdsm.getDeviceSite().getId());
						detail.setDeviceSiteName(pdsm.getDeviceSite().getName());
						detail.setProcessRoute(ipm.getProcessRoute());
						if(index<pdmList.size()-1) {
							detail.setProductionCount(avgCount);
						}else {
							detail.setProductionCount(avgCount+yuShu);
						}
						index++;
						workSheetDetails.add(detail);
					}
				}
			}
		long recordId = 1;
		//查询工件工序的参数
		for(WorkSheetDetail detail : workSheetDetails) {
			String hql1 = "from WorkpieceProcessParameterMapping wppm where wppm.workpieceProcess.inventory.code=?0 and wppm.workpieceProcess.process.id=?1";
			List<WorkpieceProcessParameterMapping>  list = workpieceProcessParameterMappingDao.findByHQL(hql1, new Object[] {workpieceCode,detail.getProcessId()});

			if(list!=null && list.size()>0) {
				Set<WorkSheetDetailParametersRecord> parametersRecords = new HashSet<>();
				for(WorkpieceProcessParameterMapping wppm : list) {
					WorkSheetDetailParametersRecord wdpr = new WorkSheetDetailParametersRecord();
					wdpr.setLowLine(wppm.getLowLine());
					wdpr.setUpLine(wppm.getUpLine());
					wdpr.setStandardValue(wppm.getStandardValue());
					if(wppm.getParameter()!=null){
						wdpr.setParameterCode(wppm.getParameter().getCode());
						wdpr.setParameterName(wppm.getParameter().getName());
					}
					wdpr.setId(recordId++);
					parametersRecords.add(wdpr);
				}
				detail.setParameterRecords(parametersRecords);
			}
		}
	}

	@Override
	public Pager<ProcessDeviceSiteMapping> queryOtherDeviceSitesByProcessId(Long processId, Long productionUnitId,int pageNo, int pageSize,List<WorkSheetDetail> workSheetDetails) {
		List<Long> deviceSiteIdList = new ArrayList<>();
		for(WorkSheetDetail detail : workSheetDetails) {
			Long deviceSiteId = detail.getDeviceSiteId();
			if(deviceSiteId!=null && deviceSiteId>0) {
				deviceSiteIdList.add(detail.getDeviceSiteId());
			}
		}
		return workSheetDetailDao.queryDeviceSiteOutOfListByProcessId(deviceSiteIdList,productionUnitId, processId, pageNo, pageSize);
	}

	@Override
	public Long queryCountByProcessId(Long processId,Long workSheetId) {
		return workSheetDetailDao.queryCountByProcessIdAndWorkSheetId(processId, workSheetId);
	}

	@Override
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(Long workSheetId, Long deviceSiteId) {
		String hql = "from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deviceSiteId=?1";
		return workSheetDetailDao.findByHQL(hql, new Object[] {workSheetId,deviceSiteId});
	}

	@Override
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteIdAndProccessId(Long workSheetId,
			Long deviceSiteId, String processCode) {
		String hql = "from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deviceSiteId=?1 and wsd.processCode=?2";
		return workSheetDetailDao.findByHQL(hql, new Object[] {workSheetId,deviceSiteId,processCode});
	}
	@Override
	public List<WorkSheetDetail> workOver(Long deviceSiteId) {
		String hql = "from WorkSheetDetail work where work.deviceSiteId=?0 and work.status=?1 and work.deleted=?2";
		List<WorkSheetDetail> list = workSheetDetailDao.findByHQL(hql, new Object[]{deviceSiteId,"1",false});
		if(!list.isEmpty()&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public WorkSheetDetail queryProcessingWorkSheetDetailByDeviceSiteCode(String deviceSiteCode) {
		String hql = " from WorkSheetDetail detail where detail.deviceSiteCode=?0 and detail.status=?1 and detail.deleted=?2";
		List<WorkSheetDetail> list = workSheetDetailDao.findByHQL(hql, new Object[] {deviceSiteCode,Constant.WorkSheet.Status.PROCESSING,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return  null;
	}

	@Override
	public void complete(Long workSheetDetailId) {
		//根据id查找工单详情
		WorkSheetDetail workSheetDetail = queryObjById(workSheetDetailId);
		WorkSheet workSheet = workSheetDetail.getWorkSheet();
		//根据工单id查找其他工单详情
		List<WorkSheetDetail> details = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.deleted!=?1", 
				new Object[] {workSheet.getId(),true});
		//完工数大于等于计划数
		if(workSheetDetail.getCompleteCount()>=workSheet.getProductCount()) {
			workSheetDetail.setStatus(Constant.WorkSheet.Status.COMPLETE);
			if(!CollectionUtils.isEmpty(details)) {
				boolean completeStatus = true;
				for(WorkSheetDetail detail : details) {
					//工单详情中 存在一个非停工或完工的记录，则工单状态不做修改
					if(!Constant.WorkSheet.Status.COMPLETE.equals(detail.getStatus())) {
						completeStatus = false;
						break;
					}
				}
				//工单设置为停工状态
				if(completeStatus) {
					workSheet.setStatus(Constant.WorkSheet.Status.COMPLETE);
					workSheetDao.update(workSheet);
				}
			}
		}else {
			workSheetDetail.setStatus(Constant.WorkSheet.Status.STOP);
			if(!CollectionUtils.isEmpty(details)) {
				boolean stopStatus = true;
				for(WorkSheetDetail detail : details) {
					//工单详情中 存在一个非停工或完工的记录，则工单状态不做修改
					if(!Constant.WorkSheet.Status.STOP.equals(detail.getStatus()) && !Constant.WorkSheet.Status.COMPLETE.equals(detail.getStatus())) {
						stopStatus = false;
						break;
					}
				}
				//工单设置为停工状态
				if(stopStatus) {
					workSheet.setStatus(Constant.WorkSheet.Status.STOP);
					workSheetDao.update(workSheet);
				}
			}
		}
		workSheetDetailDao.update(workSheetDetail);
	}

	@Override
	public List<WorkSheetDetail> queryWorkSheetDetailByDeviceSiteCode(String deviceSiteCode) {
		return workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.deviceSiteCode=?0" , new Object[] {deviceSiteCode});
	}

	@Override
	public Long querySumCompleteByWorkSheetId(Long id) {
		String hql = "select min(detail.completeCount) from WorkSheetDetail detail where detail.workSheet.id=?0 ";
		return workSheetDetailDao.findCount(hql, new Object[] {id});
	}

	/**
	 * 根据id数组查找工单详情
	 *
	 * @param idsList
	 * @return
	 */
	@Override
	public List<WorkSheetDetail> queryByIds(List<Long> idsList) {
		return workSheetDetailDao.findByHQL(" from WorkSheetDetail wsd where wsd.id in ?0",new Object[]{idsList});
	}

	/**
	 * 根据工单单号查找工序代码和名称
	 *
	 * @param no
	 * @return
	 */
	@Override
	public List<Object[]> queryProcessCodeAndNameByNo(String no) {
		return workSheetDetailDao.queryProcessCodeAndNameByNo(no);
	}

	@Override
	public Long querySumByProductionUnitId(Date from, Date to, String productionUnitId) {
		// TODO Auto-generated method stub
		return workSheetDetailDao.querySumByProductionUnitId(from, to, productionUnitId);
	}

	/**
	 * 根据工单单号和工序编码查找设备站点信息
	 *
	 * @param no          工单单号
	 * @param processCode 工序编码
	 * @return
	 */
	@Override
	public List<Object[]> queryDeviceSiteByNoAndProcessCode(String no, String processCode) {
		return workSheetDetailDao.queryDeviceSiteByNoAndProcessCode(no,processCode);
	}

	@Override
	public List<WorkSheetDetail> queryAllWorkSheetDetail() {
		return workSheetDetailDao.findByHQL("from WorkSheetDetail w (select )",new Object[]{});
	}
}
