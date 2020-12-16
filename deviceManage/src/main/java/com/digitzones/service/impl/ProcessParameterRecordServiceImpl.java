package com.digitzones.service.impl;

import com.digitzones.dao.IProcessParameterRecordDao;
import com.digitzones.dao.IProcessRecordDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessParameterRecord;
import com.digitzones.model.ProcessRecord;
import com.digitzones.service.IProcessParameterRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Service
public class ProcessParameterRecordServiceImpl implements IProcessParameterRecordService {
	private IProcessParameterRecordDao processParameterRecordDao;
	@Autowired
	private IProcessRecordDao processRecordDao;
	@Autowired
	public void setProcessParameterRecordDao(IProcessParameterRecordDao processParameterRecordDao) {
		this.processParameterRecordDao = processParameterRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processParameterRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ProcessParameterRecord obj) {
		if("ng".equalsIgnoreCase(obj.getStatus())){
			ProcessRecord processRecord = processRecordDao.findById(obj.getProcessRecord().getId());
			processRecord.setStatus("ng");
			processRecordDao.update(processRecord);
		}
		processParameterRecordDao.update(obj);
	}

	@Override
	public ProcessParameterRecord queryByProperty(String name, String value) {
		return processParameterRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProcessParameterRecord obj) {
		ProcessRecord record = obj.getProcessRecord();
		if("ng".equalsIgnoreCase(obj.getStatus())){
			ProcessRecord processRecord = processRecordDao.findById(record.getId());
			processRecord.setStatus("ng");
			processRecordDao.update(processRecord);
		}
		return processParameterRecordDao.save(obj);
	}

	@Override
	public ProcessParameterRecord queryObjById(Serializable id) {
		return processParameterRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		processParameterRecordDao.deleteById(id);
	}

	@Override
	public boolean queryIsExistByProcessRecordId(Long processRecordId) {
		Long count = processParameterRecordDao.findCount("from ProcessParameterRecord ppr where ppr.processRecord.id=?0",new Object[] {processRecordId});
		if(count == null || count == 0) {
			return false;
		}
		return true;
	}

	@Override
	public ProcessParameterRecord queryByProcessRecordIdAndParamterCode(Long processRecordId, String parameterCode) {
		List<ProcessParameterRecord> list = processParameterRecordDao.findByHQL("select ppr from ProcessParameterRecord ppr inner join fetch ppr.processRecord record where record.id=?0 and ppr.parameterCode=?1",new Object[] {processRecordId,parameterCode});
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
}
