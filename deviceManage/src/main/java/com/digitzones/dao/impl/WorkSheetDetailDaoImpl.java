package com.digitzones.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProcessDeviceSiteMapping;
import com.digitzones.model.WorkSheetDetail;
@Repository
public class WorkSheetDetailDaoImpl extends CommonDaoImpl<WorkSheetDetail> implements IWorkSheetDetailDao {

	public WorkSheetDetailDaoImpl() {
		super(WorkSheetDetail.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager<ProcessDeviceSiteMapping> queryDeviceSiteOutOfListByProcessId(List<Long> deviceSiteIdList,Long productionUnitId, Long processId,int pageNo, int pageSize) {
		String hql = "from ProcessDeviceSiteMapping pdsm where pdsm.deviceSite.id not in (:deviceSiteIds) and pdsm.processes.id=:processId"
				+ " and pdsm.deviceSite.device.productionUnit.id=:productionUnitId ";
		
		int startIndex = Pager.getStartOfPage(pageNo, pageSize);
		
		long totalCount = 0;
		List<ProcessDeviceSiteMapping>  list = null;
		
		if(deviceSiteIdList.size()<=0) {
			totalCount = (long) getSession().createQuery("from ProcessDeviceSiteMapping pdsm where pdsm.processes.id=:processId"
					+ " and pdsm.deviceSite.device.productionUnit.id=:productionUnitId")
					.setParameter("processId",processId)
					.setParameter("productionUnitId", productionUnitId)
					.list().get(0);
			
			if(totalCount<=0) {
				return new Pager<ProcessDeviceSiteMapping>();
			}
			hql = "from ProcessDeviceSiteMapping pdsm where pdsm.processes.id=:processId and pdsm.deviceSite.device.productionUnit.id=:productionUnitId";
			list = getSession().createQuery(hql)
					.setParameter("processId",processId)
					.setParameter("productionUnitId", productionUnitId)
					.setMaxResults(pageSize)
					.setFirstResult(startIndex)
					.list();
		}else {
			totalCount = (long) getSession().createQuery("select count(*) from ProcessDeviceSiteMapping pdsm where pdsm.deviceSite.id not in (:deviceSiteIds) and pdsm.processes.id=:processId"
					+ " and pdsm.deviceSite.device.productionUnit.id=:productionUnitId")
					.setParameter("processId",processId)
					.setParameterList("deviceSiteIds", deviceSiteIdList)
					.setParameter("productionUnitId", productionUnitId)
					.list().get(0);
			
			if(totalCount<=0) {
				return new Pager<ProcessDeviceSiteMapping>();
			}
			list = getSession().createQuery(hql)
					.setParameter("processId",processId)
					.setParameterList("deviceSiteIds", deviceSiteIdList)
					.setParameter("productionUnitId", productionUnitId)
					.setMaxResults(pageSize)
					.setFirstResult(startIndex)
					.list();
		}
		return new Pager<ProcessDeviceSiteMapping>(startIndex,  pageSize, (int)totalCount,list);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long queryCountByProcessIdAndWorkSheetId(Long processId, Long workSheetID) {
		return (Long) this.getHibernateTemplate().find("select count(*) from WorkSheetDetail wsd where wsd.processId=?0 and wsd.workSheet.id=?1", new Object[] {processId,workSheetID}).get(0);
	}

	/**
	 * 根据工单单号 查找工序编码和名称
	 * @param no
	 * @return
	 */
	@Override
	public List<Object[]> queryProcessCodeAndNameByNo(String no) {
		return getSession().createQuery("select distinct processCode,processName from WorkSheetDetail detail where detail.workSheet.no=?0")
				.setParameter(0,no).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long querySumByProductionUnitId(Date from ,Date to,String productionUnitId) {
		return (Long)getSession().createQuery("select SUM(wd.productionCount) from WorkSheetDetail wd inner join wd.workSheet w  where w.productionUnitId=?0 and w.makeDocumentDate  between ?1 and ?2")
				.setParameter(0,Long.parseLong(productionUnitId))
				.setParameter(1, from)
				.setParameter(2, to).list().get(0);
	}

	/**
	 * 根据工单单号和工序编码查找设备站点信息
	 * @param no          工单单号
	 * @param processCode 工序编码
	 * @return
	 */
	@Override
	public List<Object[]> queryDeviceSiteByNoAndProcessCode(String no, String processCode) {
		return getSession().createNativeQuery("select deviceSiteCode,deviceSiteName from WorkSheetDetail detail inner join WorkSheet sheet" +
				" on detail.workSheet_Id=sheet.id where sheet.no=?0 and detail.processCode=?1")
				.setParameter(0,no)
				.setParameter(1,processCode)
				.list();
	}

}
