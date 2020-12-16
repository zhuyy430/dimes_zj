package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.dao.IWorkSheetDetailParametersRecordDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkSheetDetail;
import com.digitzones.model.WorkSheetDetailParametersRecord;
import com.digitzones.service.IWorkSheetDetailParametersRecordService;
@Service
public class WorkSheetDetailParametersRecordServiceImpl implements IWorkSheetDetailParametersRecordService {
	private IWorkSheetDetailParametersRecordDao workSheetDetailParametersRecordDao;
	@Autowired
	private IWorkSheetDetailDao workSheetDetailDao;
	@Autowired
	public void setWorkSheetDetailParametersRecordDao(
			IWorkSheetDetailParametersRecordDao workSheetDetailParametersRecordDao) {
		this.workSheetDetailParametersRecordDao = workSheetDetailParametersRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return workSheetDetailParametersRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(WorkSheetDetailParametersRecord obj) {
		WorkSheetDetail workSheetDetail = obj.getWorkSheetDetail();
		workSheetDetail.setFirstReport("已填写");
		workSheetDetailDao.update(workSheetDetail);
		workSheetDetailParametersRecordDao.update(obj);
	}

	@Override
	public WorkSheetDetailParametersRecord queryByProperty(String name, String value) {
		return workSheetDetailParametersRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(WorkSheetDetailParametersRecord obj) {
		return workSheetDetailParametersRecordDao.save(obj);
	}

	@Override
	public WorkSheetDetailParametersRecord queryObjById(Serializable id) {
		return workSheetDetailParametersRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		workSheetDetailParametersRecordDao.deleteById(id);
	}

	@Override
	public WorkSheetDetailParametersRecord queryByWorkSheetDetailIdAndParameterCode(Long workSheetDetailId,
			String parameterCode) {
		String hql = "from WorkSheetDetailParametersRecord record where record.workSheetDetail.id=?0 and record.parameterCode=?1";
		Object[] paramValues = {workSheetDetailId,parameterCode};
		List<WorkSheetDetailParametersRecord> list = workSheetDetailParametersRecordDao.findByHQL(hql, paramValues);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 根据工单单号和工序编码查找工单详情参数信息
	 *
	 * @param no          工单单号
	 * @param processCode 工序代码
	 * @return
	 */
	@Override
	public List<WorkSheetDetailParametersRecord> queryByNoAndProcessCode(String no, String processCode) {
		String hql = "from WorkSheetDetailParametersRecord record where record.workSheetDetail.workSheet.no=?0 and record.workSheetDetail.processCode=?1";
		return workSheetDetailParametersRecordDao.findByHQL(hql,new Object[]{no,processCode});
	}
}
