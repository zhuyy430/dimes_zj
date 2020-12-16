package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.INGProcessMethodDao;
import com.digitzones.dao.INGRecordDao;
import com.digitzones.service.IClassesService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.INGRecordService;
import com.digitzones.service.IWorkSheetDetailService;
import org.springframework.util.CollectionUtils;

@Service("ngRecordService")
public class NGRecordServiceImpl implements INGRecordService {
	private INGRecordDao ngRecordDao;
	private IWorkSheetDetailService workSheetDetailService;
	private INGProcessMethodDao ngProcessMethodDao;
	@Autowired
	private IClassesService classesService;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IWorkSheetDetailDao workSheetDetailDao;
	@Autowired
	public void setNgProcessMethodDao(INGProcessMethodDao ngProcessMethodDao) {
		this.ngProcessMethodDao = ngProcessMethodDao;
	}

	@Autowired
	public void setWorkSheetDetailService(IWorkSheetDetailService workSheetDetailService) {
		this.workSheetDetailService = workSheetDetailService;
	}

	@Autowired
	public void setNgRecordDao(INGRecordDao ngRecordDao) {
		this.ngRecordDao = ngRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return ngRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(NGRecord obj) {
		ngRecordDao.update(obj);
		List<NGProcessMethod> methods = ngProcessMethodDao.findByHQL("from NGProcessMethod method where method.ngRecord.id=?0", new Object[] {obj.getId()});
		String method = obj.getProcessingMethod();
		for(NGProcessMethod processMethod : methods) {
			if(processMethod.getProcessMethod().equals(method)) {
				processMethod.setNgCount(obj.getNgCount());
			}else {
				processMethod.setNgCount(0);
			}
			
			ngProcessMethodDao.update(processMethod);
		}
	}

	@Override
	public NGRecord queryByProperty(String name, String value) {
		return ngRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(NGRecord obj) {
		//班次查询？根据班次类别
		DeviceSite deviceSite = deviceSiteService.queryObjById(obj.getDeviceSiteId());
		Classes c = classesService.queryCurrentClassesByClassesTypeName(deviceSite.getDevice().getProductionUnit().getClassTypeName());
		obj.setClassesCode(c.getCode());
		obj.setClassesId(c.getId());
		obj.setClassesName(c.getName());
		Serializable ngRecordId = ngRecordDao.save(obj);
		//添加处理详情
		NGProcessMethod method = new NGProcessMethod();
		method.setNgRecord(obj);
		method.setProcessMethod(Constant.ProcessRecord.SCRAP);

		NGProcessMethod method1 = new NGProcessMethod();
		method1.setNgRecord(obj);
		method1.setProcessMethod(Constant.ProcessRecord.REPAIR);

		NGProcessMethod method2 = new NGProcessMethod();
		method2.setNgRecord(obj);
		method2.setProcessMethod(Constant.ProcessRecord.COMPROMISE);

		String processMethod = obj.getProcessingMethod();
		switch(processMethod) {
		case Constant.ProcessRecord.SCRAP:method.setNgCount(obj.getNgCount());break;
		case Constant.ProcessRecord.REPAIR:method1.setNgCount(obj.getNgCount());break;
		case Constant.ProcessRecord.COMPROMISE:method2.setNgCount(obj.getNgCount());break;
		}
		//添加处理详情
		ngProcessMethodDao.save(method);
		ngProcessMethodDao.save(method1);
		ngProcessMethodDao.save(method2);
		return ngRecordId;
	}

	@Override
	public NGRecord queryObjById(Serializable id) {
		return ngRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		ngRecordDao.deleteById(id);
	}

	@Override
	public void auditNGRecord(NGRecord record,User user,Map<String,Object> args) {
		String processMethod = record.getProcessingMethod();
		//判断是否存在工单
		Long workSheetId = record.getWorkSheetId();
		if(workSheetId!=null) {
			//获取设备站点id
			Long deviceSiteId = record.getDeviceSiteId();
			//获取工序代码
			String processCode = record.getProcessCode();

			if(Constant.ProcessRecord.REPAIR.equals(processMethod)) {
				//根据工单和设备站点查找
				List<WorkSheetDetail> details = workSheetDetailService.queryWorkSheetDetailByWorkSheetIdAndDeviceSiteIdAndProccessId(workSheetId, deviceSiteId, processCode);
				for(WorkSheetDetail detail : details) {
					detail.setRepairCount(detail.getRepairCount()+1);
					detail.setQualifiedCount(detail.getQualifiedCount()-1);
					detail.setUnqualifiedCount(detail.getUnqualifiedCount()+1);

					workSheetDetailService.updateObj(detail);
				}
			}
			if(Constant.ProcessRecord.SCRAP.equals(processMethod)) {
				//根据工单和设备站点查找
				List<WorkSheetDetail> details = workSheetDetailService.queryWorkSheetDetailByWorkSheetIdAndDeviceSiteIdAndProccessId(workSheetId, deviceSiteId, processCode);
				for(WorkSheetDetail detail : details) {
					detail.setScrapCount(detail.getScrapCount()+1);
					detail.setQualifiedCount(detail.getQualifiedCount()-1);
					detail.setUnqualifiedCount(detail.getUnqualifiedCount()+1);

					workSheetDetailService.updateObj(detail);
				}
			}
		}
		ngRecordDao.update(record);
	}

	@Override
	public Integer queryScrapCountByDateAndProcessId(Date date, Long processId) {
		return ngRecordDao.queryScrapCountByDateAndProcessId(date, processId);
	}
	@Override
	public List<?> queryScrapCountByDateAndProcessId(Date date) {
		return ngRecordDao.queryScrapCountByDateAndProcessId(date);
	}

	@Override
	public Integer queryNgCountByDeviceSiteId(Long deviceSiteId, Date today) {
		return ngRecordDao.queryNgCountByDeviceSiteId(deviceSiteId, today);
	}

	@Override
	public void reviewNGRecord(NGRecord record, User user, Map<String, Object> args) {
		record.setReviewDate(new Date());
		if(user!=null) {
			record.setReviewerId(user.getId());
			record.setReviewerName(user.getUsername());
		}
		ngRecordDao.update(record);
	}

	@Override
	public void confirmNGRecord(NGRecord record, User user, Map<String, Object> args) {
		record.setConfirmDate(new Date());
		if(user!=null) {
			record.setConfirmUserId(user.getId());
			record.setConfirmUsername(user.getUsername());
		}

		ngRecordDao.update(record);
	}

	@Override
	public void deleteNGRecord(NGRecord record) {
		record.setDeleted(true);
		ngRecordDao.update(record);
		deleteWorkSheetDetailUnqualifiedCount(record);
	}

	@Override
	public Serializable addNGRecord(NGRecord record, User user) {
		if(user!=null) {
			record.setInputUserId(user.getId());
			record.setInputDate(new Date());
			if(user.getEmployee()!=null) {
				record.setInputUsername(user.getEmployee().getName());
			}else{
				record.setInputUsername(user.getUsername());
			}
		}
		fillWorkSheetDetailUnqualifiedCount(record);
		return this.addObj(record);
	}

	public void fillWorkSheetDetailUnqualifiedCount(NGRecord form){
		List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.processCode=?0" +
				" and detail.deviceSiteCode=?1 and detail.workSheet.no=?2",new Object[]{form.getProcessCode(),form.getDeviceSiteCode(),form.getNo()});
		if(!CollectionUtils.isEmpty(workSheetDetails)){
			for(WorkSheetDetail workSheetDetail : workSheetDetails){
				workSheetDetail.setUnqualifiedCount(workSheetDetail.getUnqualifiedCount()+(int)((double)form.getNgCount()));
				if(form.getProcessingMethod().equals(Constant.ProcessRecord.SCRAP)){
					workSheetDetail.setScrapCount(workSheetDetail.getScrapCount()+(int)((double)form.getNgCount()));
				}else if(form.getProcessingMethod().equals(Constant.ProcessRecord.REPAIR)){
					workSheetDetail.setRepairCount(workSheetDetail.getRepairCount()+(int)((double)form.getNgCount()));
				}else if(form.getProcessingMethod().equals(Constant.ProcessRecord.COMPROMISE)){
					workSheetDetail.setCompromiseCount(workSheetDetail.getCompromiseCount()+(int)((double)form.getNgCount()));
				}
				workSheetDetailDao.update(workSheetDetail);
			}
		}
	}
	public void deleteWorkSheetDetailUnqualifiedCount(NGRecord form){
		List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.processCode=?0" +
				" and detail.deviceSiteCode=?1 and detail.workSheet.no=?2",new Object[]{form.getProcessCode(),form.getDeviceSiteCode(),form.getNo()});
		if(!CollectionUtils.isEmpty(workSheetDetails)){
			for(WorkSheetDetail workSheetDetail : workSheetDetails){
				workSheetDetail.setUnqualifiedCount(workSheetDetail.getUnqualifiedCount()-(int)((double)form.getNgCount()));
				if(form.getProcessingMethod().equals(Constant.ProcessRecord.SCRAP)){
					workSheetDetail.setScrapCount(workSheetDetail.getScrapCount()-(int)((double)form.getNgCount()));
				}else if(form.getProcessingMethod().equals(Constant.ProcessRecord.REPAIR)){
					workSheetDetail.setRepairCount(workSheetDetail.getRepairCount()-(int)((double)form.getNgCount()));
				}else if(form.getProcessingMethod().equals(Constant.ProcessRecord.COMPROMISE)){
					workSheetDetail.setCompromiseCount(workSheetDetail.getCompromiseCount()-(int)((double)form.getNgCount()));
				}
				workSheetDetailDao.update(workSheetDetail);
			}
		}
	}

	@Override
	public Integer queryNgCount4Class(Classes classes, Long deviceSiteId,Date date) {
		return ngRecordDao.queryNgCount4Class(classes, deviceSiteId, date);
	}
	@Override
	public Integer queryNgCount4ClassByClassesAndProductionUnit(Classes classes, Long productionUnitId,Date date) {
		return ngRecordDao.queryNgCount4ClassByClassesAndProductionUnit(classes, productionUnitId, date);
	}

	@Override
	public Integer queryNgCountFromBeginOfMonthUntilTheDate(Date date) {
		return ngRecordDao.queryNgCountFromBeginOfMonthUntilTheDate(date);
	}

	@Override
	public Long queryNgCount4TheDate(Date date) {
		Long count = ngRecordDao.queryNgCount4TheDate(date);
		return count==null?0:count;
	}

	@Override
	public Object[] queryNGRecordById(Long id) {
		return ngRecordDao.queryNGRecordById(id);
	}

	@Override
	public List<NGRecord> queryNgRecordByDeviceSiteId(Long deviceSiteId) {
		return ngRecordDao.queryNgRecordByDeviceSiteId(deviceSiteId);
	}

	@Override
	public List<NGRecord> queryNgRecordsByDate(String hql,List<Object> list) {
		return ngRecordDao.findByHQL(hql, list.toArray());
	}

	/**
	 * 查询日期区间的不合格品数量
	 *
	 * @param from             开始时间
	 * @param to               结束时间
	 * @param productionIdList 生产单元id列表
	 * @return
	 */
	@Override
	public List<String[]> queryNgRecordGroupByCategory(Date from, Date to, List<Long> productionIdList) {
		return ngRecordDao.queryNgRecordGroupByCategory(from,to,productionIdList);
	}

	/**
	 * 根据不合格品大类分组查找不合格品信息
	 *
	 * @param monthDate
	 * @param productionIdList
	 * @return
	 */
	@Override
	public List<String[]> queryNgRecord4MonthGroupByCategory(Date monthDate, List<Long> productionIdList) {
		return ngRecordDao.queryNgRecord4MonthGroupByCategory(monthDate,productionIdList);
	}

	@Override
	public Integer queryNgCount4ClassFromClassBegin2now(Classes classes, Long deviceSiteId) {
		return ngRecordDao.queryNgCount4ClassFromClassBegin2now(classes, deviceSiteId);
	}

	@Override
	public List<Object[]> queryNgCount4Class(Classes classes, Date date) {
		return ngRecordDao.queryNgCount4Class(classes,  date);
	}

	@Override
	public List<Object[]> queryNgCount4ClassToday(Classes classes, Date date) {
		return ngRecordDao.queryNgCount4ClassToday(classes, date);
	}

	@Override
	public List<Object[]> queryNgCount4DeviceSiteShow(List<Long> deviceSiteIdList, Date date) {
		return ngRecordDao.queryNgCount4DeviceSiteShow(deviceSiteIdList, date);
	}

	@Override
	public String queryMaxWarehouseNo() {
		return ngRecordDao.queryMaxWarehouseNo();
	}
}
