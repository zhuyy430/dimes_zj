package com.digitzones.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.dao.IEquipmentDeviceSiteMappingDao;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class EquipmentDeviceSiteMappingServiceImpl implements IEquipmentDeviceSiteMappingService {
	private IEquipmentDeviceSiteMappingDao equipmentDeviceSiteMappingDao;

	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IMCUserService mcUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEquipmentService equipmentService;

	@Autowired
	public void setEquipmentDeviceSiteMappingDao(IEquipmentDeviceSiteMappingDao equipmentDeviceSiteMappingDao) {
		this.equipmentDeviceSiteMappingDao = equipmentDeviceSiteMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return equipmentDeviceSiteMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EquipmentDeviceSiteMapping obj) {
		equipmentDeviceSiteMappingDao.update(obj);
	}

	@Override
	public EquipmentDeviceSiteMapping queryByProperty(String name, String value) {
		return equipmentDeviceSiteMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EquipmentDeviceSiteMapping obj) {
		return equipmentDeviceSiteMappingDao.save(obj);
	}

	@Override
	public EquipmentDeviceSiteMapping queryObjById(Serializable id) {
		return equipmentDeviceSiteMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		equipmentDeviceSiteMappingDao.deleteById(id);
	}

	@Override
	public EquipmentDeviceSiteMapping queryByNo(String no) {
		List<EquipmentDeviceSiteMapping> list = equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.equipment.code=?0 and edsm.unbind=?1", new Object[] {no,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public EquipmentDeviceSiteMapping queryByEquipmentCode(String code) {
		List<EquipmentDeviceSiteMapping> list = equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.equipment.code=?0 and edsm.unbind=?1", new Object[] {code,false});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Pager<List<Object[]>> queryEquipmentCountReport(Map<String, String> params, int rows, int page) {
		return equipmentDeviceSiteMappingDao.queryEquipmentCountReport(params, rows, page);
	}

	@Override
	public List<EquipmentDeviceSiteMapping> queryListByDeviceSiteCodeAndEquipmentTypeId(String deviceSiteCode, Long equipmentTypeId,String workSheetNo) {
		if(equipmentTypeId!=null) {
			return equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.code=?0 and edsm.equipment.equipmentType.id=?1 and edsm.unbind=?2 and edsm.workSheetCode=?3", new Object[]{deviceSiteCode, equipmentTypeId, false, workSheetNo});
		}else{
			return equipmentDeviceSiteMappingDao.findByHQL("from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.code=?0 and edsm.unbind=?1 and edsm.workSheetCode=?2", new Object[]{deviceSiteCode, false, workSheetNo});
		}
	}

	@Override
	public void addMCEquipmentsMappingRecordInWorkSheet(String equipmentNos, String deviceSiteCode, String helperId, String usageRates, HttpServletRequest request,String workSheetNo) {
		if(equipmentNos!=null && equipmentNos.contains("'")) {
			equipmentNos = equipmentNos.replace("'", "");
		}
		String[] equipmentNoList= equipmentNos.split(",");

		if(usageRates!=null && usageRates.contains("'")) {
			usageRates = usageRates.replace("'", "");
		}
		String[] usageRateList= usageRates.split(",");


		//设备站点
		DeviceSite ds = deviceSiteService.queryByProperty("code",deviceSiteCode);
		if(ds==null) {
			throw  new RuntimeException("请选择设备站点");
		}

		//辅助人设置
		//User hUser = userService.queryUserByEmployeeCode(equipmentDeviceSiteMapping.getHelperId());
		Employee emp = employeeService.queryEmployeeByCode(helperId);

		//获取当前登录用户
		MCUser mcUser  = mcUserService.queryLoginUserByClientIp(request.getRemoteAddr());

		//获取当前登录用户
		User user = userService.queryUserByUsername(mcUser.getUsername());


		for(int i=0;i<equipmentNoList.length;i++){

			EquipmentDeviceSiteMapping eqm=new EquipmentDeviceSiteMapping();
			eqm.setDeviceSite(ds);
			if(!usageRateList[i].equals("")) {
				eqm.setUsageRate(Float.valueOf(usageRateList[i]));
			}

			Equipment equipment=equipmentService.queryByProperty("code",equipmentNoList[i]);
			//判断该装备是否已经被关联到设备上
			EquipmentDeviceSiteMapping edm1=queryByEquipmentCode(equipmentNoList[i]);
			if(edm1!=null){
				throw  new RuntimeException("序号为"+equipment.getCode()+"的装备"+equipment.getEquipmentType().getName()+"已经被设备站点"+edm1.getDeviceSite().getName()+"关联!");
			}
			if(equipment!=null){
				eqm.setEquipment(equipment);
			}else{
				throw  new RuntimeException("该装备不存在");
			}

			if(null!=emp) {
				eqm.setHelperId(emp.getCode());
				eqm.setHelperName(emp.getName());
			}

			if(eqm.getMappingDate()==null) {
				eqm.setMappingDate(new Date());
			}

			if(mcUser == null) {
				throw  new RuntimeException("请登录");
			}

			if(!StringUtils.isEmpty(workSheetNo)) {
				eqm.setWorkSheetCode(workSheetNo);
			}


			if(user!=null) {
				eqm.setBindUserId(user.getId());
				eqm.setBindUsername(user.getUsername());
			}
			equipmentDeviceSiteMappingDao.save(eqm);
		}
	}

	@Override
	public List<EquipmentDeviceSiteMapping> queryEquipmentMappingRecordByWorkSheetNo(Long workSheetId) {
		return equipmentDeviceSiteMappingDao.findByHQL("select edsm from EquipmentDeviceSiteMapping edsm where edsm.unbind=?0 and edsm.deviceSite.id in (select wd.deviceSiteId from WorkSheetDetail wd where wd.workSheet.id=?1) ", new Object[] {false,workSheetId});
	}


}
