package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCDeviceSiteDao;
import com.digitzones.mc.model.MCDeviceSite;
@Repository
public class MCDeviceSiteDaoImpl extends CommonDaoImpl<MCDeviceSite> implements IMCDeviceSiteDao {
	public MCDeviceSiteDaoImpl() {
		super(MCDeviceSite.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCDeviceSite> selectAllMCDeviceSiteByIP(String IP) {
		String sql = "select ds.id,ds.clientIp,ds.deviceName,ds.deviceCode,ds.deviceSiteCode,ds.deviceSiteName "
				+ "from MC_DEVICESITE ds  where ds.clientIp=?0";
		return getSession().createSQLQuery(sql).setParameter(0,IP).addEntity(MCDeviceSite.class).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCDeviceSite> queryAvailableDeviceSites(String clientIp) {
		String sql = "select ds.id id,ds.code deviceSiteCode,ds.name deviceSiteName,d.code deviceCode,d.name  deviceName,'' clientIp"
				+ " from devicesite ds inner join device d on ds.device_id = d.id where ds.disabled=0 and ds.code not in ("
				+ " select deviceSiteCode from mc_deviceSite where clientIp=?0)";
		return getSession().createSQLQuery(sql).addEntity(MCDeviceSite.class).setParameter(0, clientIp).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MCDeviceSite> queryAvailableDeviceSitesByCondition(String clientIp, String deviceName,
			String deviceCode, String productionUnitCode) {
			String sql = "select ds.id id,ds.code deviceSiteCode,ds.name deviceSiteName,ds.bottleneck bottleneck,d.code deviceCode,d.name  deviceName,'' clientIp"
					+ " from devicesite ds inner join device d on ds.device_id = d.id inner join ProductionUnit p on d.ProductionUnit_id=p.id where ds.disabled=0  and ds.code not in ("
					+ " select deviceSiteCode from mc_deviceSite where clientIp=?0)";
			if(!StringUtils.isEmpty(deviceName)){
				sql+=" and d.name like '%"+deviceName+"%' ";
			}
			if(!StringUtils.isEmpty(deviceCode)){
				sql+="  and d.code like '%"+deviceCode+"%' ";
			}
			if(!StringUtils.isEmpty(productionUnitCode)){
				sql+="  and p.code like '%"+productionUnitCode+"%' ";
			}
			return getSession().createSQLQuery(sql).addEntity(MCDeviceSite.class).setParameter(0, clientIp)
					.list();
	}
	
}
