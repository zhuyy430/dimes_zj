package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCEquipmentDeviceSiteMappingDao;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.EquipmentDeviceSiteMapping;

@Repository
public class MCEquipmentDeviceSiteMappingDaoImpl extends CommonDaoImpl<EquipmentDeviceSiteMapping> implements IMCEquipmentDeviceSiteMappingDao{

	public MCEquipmentDeviceSiteMappingDaoImpl() {
		super(EquipmentDeviceSiteMapping.class);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<EquipmentDeviceSiteMapping> findEquipmentDeviceSiteMappinglist(String deviceSiteCode,String hql) {	
		
		/*String sql="select * from EQUIPMENT_DEVICESITE e where e.[DEVICESITE_ID] in(select id from DEVICESITE where code=?0)"
				+"and e.unbind=?1 and e.[EQUIPMENT_ID] in (select id from EQUIPMENT where baseCode=?2)";*/
		/*String sql="select e.* from EQUIPMENT e left join EQUIPMENT_DEVICESITE ed on e.id=ed.EQUIPMENT_ID left join DEVICESITE ds on ed.DEVICESITE_ID=ds.id " + 
				"where ds.code=?0 and ed.unbind=?1 and e.baseCode=?2";*/
	    return  (List<EquipmentDeviceSiteMapping>)this.getHibernateTemplate().find(hql, new Object[] {deviceSiteCode,false,Constant.EquipmentType.EQUIPMENT});

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<DeviceSite> findDeviceSite(String deviceSiteCode) {
		// TODO Auto-generated method stub
		String hql="from DeviceSite d where d.code=?0";
		return (List<DeviceSite>)this.getHibernateTemplate().find(hql, deviceSiteCode);
	}

}
