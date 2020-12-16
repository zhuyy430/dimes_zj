package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCPressLightRecordDao;
import com.digitzones.model.PressLightRecord;
@Repository
public class MCPressLightRecordDaoImpl extends CommonDaoImpl<PressLightRecord> implements IMCPressLightRecordDao {
	public MCPressLightRecordDaoImpl() {
		super(PressLightRecord.class);
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<PressLightRecord> queryPressLightRecordByBasicCode(String typeCode,String deviceSiteCode) {
		String sql  = "select record.* from pressLightRecord record inner join pressLightType type on record.pressLightTypeId = type.id "
				+ " inner join DeviceSite ds on record.DEVICESITE_ID=ds.id"
				+ " where (type.code=?0 or type.basicCode=?0) and record.deleted=?1 and ds.code=?2";
		return getSession().createSQLQuery(sql).addEntity(PressLightRecord.class)
				.setParameter(0, typeCode)
				.setParameter(1, false)
				.setParameter(2, deviceSiteCode)
				.list();
	}
}
