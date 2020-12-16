package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IRelatedDocumentTypeDao;
import com.digitzones.devmgr.dao.IMaintenancePlanRecordDao;
import com.digitzones.devmgr.dao.IMaintenanceTypeDao;
import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.devmgr.service.IMaintenanceTypeService;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocumentType;
@Service
public class MaintenanceTypeServiceImpl implements IMaintenanceTypeService {
	@Autowired
	private IMaintenanceTypeDao maintenanceTypeDao;
	@Autowired
	private IMaintenancePlanRecordDao IMaintenancePlanRecordDao;
	@Autowired
	private IRelatedDocumentTypeDao relatedDocumentTypeDao;
	@Override
	public Pager<MaintenanceType> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(MaintenanceType obj) {
		maintenanceTypeDao.update(obj);
	}

	@Override
	public MaintenanceType queryByProperty(String name, String value) {
		return maintenanceTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(MaintenanceType obj) {
		RelatedDocumentType docType = relatedDocumentTypeDao.findSingleByProperty("code", obj.getCode());
		//增加文档类别
		if(docType==null) {
			docType = new RelatedDocumentType();
			docType.setCode(obj.getCode());
			docType.setName(obj.getName());
			docType.setAllowedDelete(false);
			docType.setCreateDate(new Date());
			docType.setModuleCode(Constant.RelatedDoc.MAINTENANCE);
			docType.setModuleName(Constant.RelatedDoc.MAINTENANCE_TXT);
			docType.setCreateUsername("系统创建");
			relatedDocumentTypeDao.save(docType);
		}
		return maintenanceTypeDao.save(obj);
	}

	@Override
	public MaintenanceType queryObjById(Serializable id) {
		return maintenanceTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		//删除文档类别
		MaintenanceType mt = maintenanceTypeDao.findById(id);
		RelatedDocumentType docType = relatedDocumentTypeDao.findSingleByProperty("code", mt.getCode());
		relatedDocumentTypeDao.delete(docType);
		maintenanceTypeDao.deleteById(id);
	}
	@Override
	public List<MaintenanceType> queryAllMaintenanceType() {
		return maintenanceTypeDao.findAll();
	}

	@Override
	public boolean isInUse(Long typeId) {
		Long count = IMaintenancePlanRecordDao.findCount("from MaintenancePlanRecord record where record.maintenanceType.id=?0", new Object[] {typeId});
		return (count==null || count == 0)?false:true;
	}
}
