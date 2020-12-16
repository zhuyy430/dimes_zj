package com.digitzones.dao.impl;
import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.model.DeviceSite;
@Repository
public class DeviceSiteDaoImpl extends CommonDaoImpl<DeviceSite> implements IDeviceSiteDao {
	public DeviceSiteDaoImpl() {
		super(DeviceSite.class);
	}
	@SuppressWarnings("deprecation")
	@Override
	public Long queryCountOfDeviceSite() {
		return (Long) this.getHibernateTemplate().find("select count(*) from DeviceSite ds where ds.device.productionUnit!=null"
				+ " and ds.device.isDimesUse=?0 ",new Object[] {true}).get(0);
	}
	@SuppressWarnings("deprecation")
	@Override
	public Long queryCountOfDeviceSiteByStatus(String status) {
		return (Long) this.getHibernateTemplate().find("select count(*) from DeviceSite ds where ds.status=?0"
				+ " and ds.device.isDimesUse=?1 ",new Object[] {status,true}).get(0);
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<DeviceSite> queryDeviceSitesByClassesId(Long classesId) {
		String sql = "select distinct ds.id,ds.code,ds.name,ds.disabled,ds.note,ds.barCodeAddress,ds.bottleneck, "
				+ " ds.goalOee,ds.status,ds.DEVICE_ID,ds.show from DEVICESITE ds inner join DEVICE d "
				+ " on ds.DEVICE_ID=d.id inner join CLASSES_DEVICE cd on d.id=cd.DEVICE_ID where cd.CLASSES_ID=?0"
				+ " and d.isDimesUse=1";
		return getSession().createNativeQuery(sql).setParameter(0, classesId).addEntity(DeviceSite.class).list();
	}
	@Override
	public Integer queryLostTimeCountByDeviceSiteId(Long deviceSiteId) {
		String sql = "select count(id) from LOSTTIMERECORD  where deviceSite_id=?0";
		Integer count = (Integer) getSession().createNativeQuery(sql).setParameter(0, deviceSiteId).uniqueResult();
		return count==null?0:count;
	}
	@Override
	public Integer queryPressLightCountByDeviceSiteId(Long deviceSiteId) {
		String sql = "select count(id) from PRESSLIGHTRECORD  where deviceSite_id=?0";
		Integer count = (Integer) getSession().createNativeQuery(sql).setParameter(0, deviceSiteId).uniqueResult();
		return count==null?0:count;
	}
	@Override
	public Integer queryProcessDeviceSiteMappingCountByDeviceSiteId(Long deviceSiteId) {
		String sql = "select count(id) from PROCESS_DEVICESITE  where deviceSite_id=?0";
		Integer count = (Integer) getSession().createNativeQuery(sql).setParameter(0, deviceSiteId).uniqueResult();
		return count==null?0:count;
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<BigInteger> queryDeviceSiteIdsByProductionUnitId(Long productionUnitId) {
		String sql = "select ds.id from devicesite ds inner join device d on ds.device_id = d.id"
				+ " inner join productionunit p on d.productionunit_id=p.id where p.id=?0 and d.isDimesUse=1";
		List<BigInteger> ids = getSession().createNativeQuery(sql).setParameter(0, productionUnitId).list();
		return ids;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> queryAllDeviceSiteIds() {
		String sql = "select ds.id from devicesite ds inner join device d on ds.device_id=d.id "
				+ "where d.isDimesUse=1";
		List<BigInteger> ids = getSession().createNativeQuery(sql).list();
		return ids;
	}
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<DeviceSite> queryAllDeviceSiteByIds(String delids,String queids) {
		String sql = "select distinct ds.id,ds.code,ds.name,ds.disabled,ds.note,ds.barCodeAddress, "
				+ " ds.goalOee,ds.status,ds.DEVICE_ID,ds.show from devicesite ds inner join device d "
				+ " on ds.device_id=d.id  where  ds.disabled=?0  and d.isDimesUse=1 ";
		if((delids!=null&&delids!="")&&queids==null){
			sql = sql+"and ds.code not in(?1)";
			return getSession().createNativeQuery(sql).setParameter(1, delids).setParameter(0, false).addEntity(DeviceSite.class).list();
		}else if((queids!=""&&queids!=null)&&delids==null){
			sql = sql+"and ds.code in(?1) ";
			return getSession().createNativeQuery(sql).setParameter(1, queids).setParameter(0, false).addEntity(DeviceSite.class).list();
		}else if(("".equals(delids)||null==delids)&&("".equals(queids)||null==queids)){
			return getSession().createNativeQuery(sql).setParameter(0, false).addEntity(DeviceSite.class).list();
		}
		sql = sql +" and ds.code not in(?1)"+" and ds.code in(?2) ";
		return getSession().createNativeQuery(sql).setParameter(1, delids).setParameter(0, false).
				setParameter(2, queids).addEntity(DeviceSite.class).list();
	}
	@SuppressWarnings({"unchecked"})
	@Override
	public List<DeviceSite> queryDeviceSiteByCordAndName(String deviceSiteCode, String deviceSiteName) {
		String sql = "select distinct ds.id,ds.code,ds.name,ds.disabled,ds.note,ds.barCodeAddress, "
				+ " ds.goalOee,ds.status,ds.DEVICE_ID,ds.show from devicesite ds "
				+ " inner join device  d on ds.device_id=d.id  where  ds.code=?0 and ds.name=?1"
				+ " and d.isDimesUse=1";
		return getSession().createNativeQuery(sql).setParameter(0, deviceSiteCode).setParameter(1, deviceSiteName)
				.addEntity(DeviceSite.class).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> queryBottleneckDeviceSiteIdsByProductionUnitId(Long productionUnitId) {
		String sql = "select ds.id from devicesite ds inner join device d on ds.device_id = d.id"
				+ " inner join productionunit p on d.productionunit_id=p.id where p.id=?0 and ds.bottleneck=1"
				+ " and d.isDimesUse=1";
		List<BigInteger> ids = getSession().createNativeQuery(sql).setParameter(0, productionUnitId)
				.list();
		return ids;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> queryBottleneckDeviceSiteIds() {
		String sql = "select ds.id from devicesite ds inner join device d on ds.device_id=d.id "
				+ "where d.isDimesUse=1 and ds.bottleneck=1";
		List<BigInteger> ids = getSession().createNativeQuery(sql).list();
		return ids;
	}
	@SuppressWarnings("deprecation")
	@Override
	public Long queryCountOfBottleneckDeviceSite() {
		return (Long) this.getHibernateTemplate().find("select count(ds.id) from DeviceSite ds where ds.device.productionUnit!=null"
				+ " and ds.device.isDimesUse=?0 and ds.bottleneck=?1",new Object[] {true,true}).get(0);
	}
	@Override
	public void deleteByDeviceId(Long deviceId) {
		getSession().createNativeQuery("delete from devicesite where device_id=?0")
		.setParameter(0, deviceId)
		.executeUpdate();
	}
}
