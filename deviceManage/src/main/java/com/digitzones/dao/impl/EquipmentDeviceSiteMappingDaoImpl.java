package com.digitzones.dao.impl;

import com.digitzones.dao.IEquipmentDeviceSiteMappingDao;
import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
@Repository
public class EquipmentDeviceSiteMappingDaoImpl extends CommonDaoImpl<EquipmentDeviceSiteMapping> implements IEquipmentDeviceSiteMappingDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public EquipmentDeviceSiteMappingDaoImpl() {
		super(EquipmentDeviceSiteMapping.class);
	}

	@Override
	public Pager<List<Object[]>> queryEquipmentCountReport(Map<String, String> params, int rows, int page) {

		 String sql ="select EquDev.id EquDev_id,dev.code DeviceSite_Code,dev.name DeviceSite_Name,EquDev.mappingDate,EquDev.unbindDate,equ.code Equipment_Code,equ.name Equipment_Name,equ.unitType Equipment_UnitType,EquDev.no,equ.measurementObjective Equipment_measurementObjective,equ.cumulation Equipment_Cumulation,equ.measurementDifference Equipment_measurementDifference,EquDev.usageRate,equType.name "+
				 	"from EQUIPMENT_DEVICESITE EquDev inner join EQUIPMENT equ on EquDev.EQUIPMENT_ID=equ.id "+
				 	"inner join EQUIPMENTTYPE equType on equ.EQUIPMENTTYPE_ID=equType.id "+
				 	"inner join DEVICESITE dev on EquDev.DEVICESITE_ID=dev.id where 1=1 and equ.baseCode='EQUIPMENT'";
		 
		 
		 	String deviceSite = params.get("deviceSiteCodes");
		 	long helperId=-2;
		 	if(params.get("helperId")!=null&&!"".equals(params.get("helperId").trim())) {
		 		helperId = Long.parseLong(params.get("helperId"));
		 	}
		 	
			if(helperId!=-2) {
				sql += " and EquDev.helperId=:helperId";
			}
		 	String equipment=params.get("equipmentCodes");
		 	String no=params.get("no");
		 	String beginDateStr = params.get("beginDate");
			String endDateStr = params.get("endDate");
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
				sql += " and EquDev.mappingDate >=:beginTime";
			}
			
			if(endDateStr!=null && !"".equals(endDateStr)) {
				sql += " and EquDev.mappingDate<=:endTime";
			}
			
			if(deviceSite!=null && !"".equals(deviceSite.trim())) {
				sql += " and dev.code in (:deviceSiteCodes)";
			}
			if(equipment!=null && !"".equals(equipment.trim())) {
				sql += " and equ.code in (:equipmentCodes)";
			}
			if(no!=null && !"".equals(no.trim())) {
				sql += " and EquDev.no like '%'+:No+'%'";
			}
			String countQuery = "select count(w.EquDev_id) from (" + sql + " ) w";
			NativeQuery countQueryObj = getSession().createSQLQuery(countQuery);
			//countQueryObj.setParameter("beginDate", new Date());
			
			String dataQuery =  sql;

			NativeQuery dataQueryObj = getSession().createSQLQuery(dataQuery);
			dataQueryObj.setFirstResult((page-1)*rows)
			.setMaxResults(rows);
			//.setParameter("beginDate", new Date());
			if(helperId!=-2 ) {
				countQueryObj.setParameter("helperId", helperId);
				dataQueryObj.setParameter("helperId", helperId);
			}
			if(beginDateStr!=null && !"".equals(beginDateStr)) {
				try {
					countQueryObj.setParameter("beginTime", format.parse(beginDateStr));
					dataQueryObj.setParameter("beginTime", format.parse(beginDateStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(endDateStr!=null && !"".equals(endDateStr)) {
				try {
					countQueryObj.setParameter("endTime", format.parse(endDateStr));
					dataQueryObj.setParameter("endTime", format.parse(endDateStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(deviceSite!=null && !"".equals(deviceSite.trim())) {
				String[] deviceSites = deviceSite.split(",");
				countQueryObj.setParameterList("deviceSiteCodes", deviceSites);
				dataQueryObj.setParameterList("deviceSiteCodes", deviceSites);
			}
			if(equipment!=null && !"".equals(equipment.trim())) {
				String[] equipments = equipment.split(",");
				countQueryObj.setParameterList("equipmentCodes", equipments);
				dataQueryObj.setParameterList("equipmentCodes", equipments);
			}
			if(no!=null && !"".equals(no.trim())) {
				countQueryObj.setParameter("No", no);
				dataQueryObj.setParameter("No", no);
			}
			long count = (Integer) countQueryObj.list().get(0);
			if(count<1) {
				return new Pager<List<Object[]>>();
			}

			List list = dataQueryObj.list();
			return new Pager<List<Object[]>>((page-1)*rows, rows, (int)count, list);
	}
}
