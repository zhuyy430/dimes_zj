package com.digitzones.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IReportDao;
import com.digitzones.model.Pager;
@Repository
@Transactional
public class ReportDaoImpl implements IReportDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public Session getSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return this.hibernateTemplate.getSessionFactory();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String, String> params, int rows, int page) {
		String sql = "	select COUNT(ltr.id) _count, pu.code pu_code,pu.name pu_name,ds.code ds_code,ds.name ds_name,basic.name basic_name,type.name typeName,ltr.reason," + 
				"SUM(case endTime when null then datediff(minute,beginTime,:beginDate) else datediff(minute,beginTime,endTime) end) lostTime from LOSTTIMERECORD ltr inner join PRESSLIGHTTYPE type on ltr.lostTimeTypeCode=type.code" + 
				"	left join PRESSLIGHTTYPE basic on type.basicCode=basic.code" + 
				"	inner join DEVICESITE ds on ltr.DEVICESITE_ID = ds.id " + 
				"	inner join DEVICE d on ds.DEVICE_ID=d.id" + 
				"	inner join PRODUCTIONUNIT pu on d.PRODUCTIONUNIT_ID = pu.id where d.isDimesUse=1 ";


		String groupBy = "	group by pu.code ,pu.name ,ds.code ,ds.name ,basic.name ,type.name ,ltr.reason";

		String productionUnitId = params.get("productionUnitId");
		String deviceSite = params.get("deviceSiteCodes");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			sql += " and pu.id=:productionUnitId";
		}

		if(beginDateStr!=null && !"".equals(beginDateStr)) {
			sql += " and ltr.lostTimeTime >=:beginTime";
		}
		
		if(endDateStr!=null && !"".equals(endDateStr)) {
			sql += " and ltr.lostTimeTime<=:endTime";
		}
		
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			sql += " and ds.code in (:deviceSiteCodes)";
		}
		String countQuery = "select count(w.pu_code) from (" + sql + groupBy + " ) w";
		NativeQuery countQueryObj = getSession().createNativeQuery(countQuery);
		countQueryObj.setParameter("beginDate", new Date());
		
		String dataQuery =  sql + groupBy;

		NativeQuery dataQueryObj = getSession().createNativeQuery(dataQuery);
		dataQueryObj.setFirstResult((page-1)*rows)
		.setMaxResults(rows)
		.setParameter("beginDate", new Date());
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			countQueryObj.setParameter("productionUnitId", productionUnitId);
			dataQueryObj.setParameter("productionUnitId", productionUnitId);
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
		long count = (Integer) countQueryObj.list().get(0);
		if(count<1) {
			return new Pager<List<Object[]>>();
		}

		List list = dataQueryObj.list();
		return new Pager<List<Object[]>>((page-1)*rows, rows, (int)count, list);
	}
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
	public Pager<List<Object[]>> queryOutputBatchCountReport(Map<String, String> params, int rows, int page) {
		String sql = "select COUNT(*) AS _count, pu.code AS pu_code,pu.name AS pu_name,pr.deviceSiteCode AS pr_diviceSiteCode,pr.workPieceCode AS pr_workPieceCode, "
				+" pr.workPieceName AS pr_workPieceName,pr.unitType AS pr_unitType,pr.batchNumber AS pr_batchNumber,pr.stoveNumber AS pr_stoveNumber, "
				+" pr.processCode AS pr_processCode,pr.processName AS pr_processName " 
				+" from PROCESSRECORD pr inner join DEVICESITE ds on pr.deviceSiteId=ds.id inner join DEVICE d on ds.DEVICE_ID=d.id inner join PRODUCTIONUNIT pu on d.PRODUCTIONUNIT_ID=pu.id "
				+ " where d.isDimesUse=1 and pr.realRecord=1 and pr.batchNumber is not null and pr.workPieceCode is not null ";
		String groupBy = "	group by pu.code ,pu.name ,pr.deviceSiteCode ,pr.workPieceCode ,pr.workPieceName ,pr.unitType ,pr.batchNumber,pr.stoveNumber,pr.processCode,pr.processName";
		String productionUnitId = params.get("productionUnitId");
		String deviceSite = params.get("deviceSiteCodes");
		String batchNumber = params.get("batchNumber");
		String processess = params.get("processess");
		String workpiece = params.get("workpiece");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			sql += " and pu.id=:productionUnitId";
		}

		if(beginDateStr!=null && !"".equals(beginDateStr)) {
			sql += " and pr.collectionDate >=:beginTime";
		}
		
		if(endDateStr!=null && !"".equals(endDateStr)) {
			sql += " and pr.collectionDate<=:endTime";
		}
		
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			sql += " and ds.code in (:deviceSiteCodes)";
		}
		if(batchNumber!=null && !"".equals(batchNumber.trim())) {
			sql += " and pr.batchNumber in (:batchNumber)";
		}
		if(processess!=null && !"".equals(processess.trim())) {
			sql += " and pr.processCode in (:processCode)";
		}
		if(workpiece!=null && !"".equals(workpiece.trim())) {
			sql += " and pr.workPieceCode in (:workPieceCode)";
		}
		String countQuery = "select count(w.pu_code) from (" + sql + groupBy + " ) w";
		NativeQuery countQueryObj = getSession().createNativeQuery(countQuery);
		
		String dataQuery =  sql + groupBy;

		NativeQuery dataQueryObj = getSession().createNativeQuery(dataQuery);
		dataQueryObj.setFirstResult((page-1)*rows)
		.setMaxResults(rows);
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			countQueryObj.setParameter("productionUnitId", productionUnitId);
			dataQueryObj.setParameter("productionUnitId", productionUnitId);
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
		if(batchNumber!=null && !"".equals(batchNumber.trim())) {
			countQueryObj.setParameter("batchNumber", batchNumber);
		}
		if(processess!=null && !"".equals(processess.trim())) {
			String[] processesses = processess.split(",");
			countQueryObj.setParameterList("processCode", processesses);
			dataQueryObj.setParameterList("processCode", processesses);
		}
		if(workpiece!=null && !"".equals(workpiece.trim())) {
			String[] workpieces = workpiece.split(",");
			countQueryObj.setParameterList("workPieceCode", workpieces);
			dataQueryObj.setParameterList("workPieceCode", workpieces);
		}
		List obj = countQueryObj.list();
		long count = (Integer) countQueryObj.list().get(0);
		if(count<1) {
			return new Pager<List<Object[]>>();
		}

		List list = dataQueryObj.list();
		return new Pager<List<Object[]>>((page-1)*rows, rows, (int)count, list);
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
	public Pager<List<Object[]>> queryProductionUnitOutputCountReport(Map<String, String> params, int rows, int page) {
		String sql = "select COUNT(*) AS _count,convert(nvarchar(10),pr.collectionDate,120)  AS pr_collectionDate, pu.code AS pu_code,pu.name AS pu_name,pr.deviceSiteCode AS pr_diviceSiteCode,pr.workPieceCode AS pr_workPieceCode, "
				+"pr.workPieceName AS pr_workPieceName,pr.unitType AS pr_unitType, "
				+"pr.processCode AS pr_processCode,pr.processName AS pr_processName " 
				+"from PROCESSRECORD pr inner join DEVICESITE ds on pr.deviceSiteId=ds.id inner join DEVICE d on ds.DEVICE_ID=d.id inner join PRODUCTIONUNIT pu on d.PRODUCTIONUNIT_ID=pu.id where d.isDimesUse=1 and pr.realRecord=1";
		
		String groupBy = "	group by convert(nvarchar(10),pr.collectionDate,120),pu.code ,pu.name ,pr.deviceSiteCode ,pr.workPieceCode ,pr.workPieceName ,pr.unitType ,pr.processCode,pr.processName";
		
		String productionUnitId = params.get("productionUnitId");
		String deviceSite = params.get("deviceSiteCodes");
		/*String classes = params.get("classes");
		String employee = params.get("employee");*/
		String processess = params.get("processess");
		String workpiece = params.get("workpiece");
		String beginDateStr = params.get("beginDate");
		String endDateStr = params.get("endDate");
		
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			sql += " and pu.id=:productionUnitId";
		}
		
		if(beginDateStr!=null && !"".equals(beginDateStr)) {
			sql += " and pr.collectionDate >=:beginTime";
		}
		
		if(endDateStr!=null && !"".equals(endDateStr)) {
			sql += " and pr.collectionDate<=:endTime";
		}
		
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			sql += " and ds.code in (:deviceSiteCodes)";
		}
		/*if(classes!=null && !"".equals(classes.trim())) {
			sql += " and pr.classesCode =:classesCode";
		}
		if(employee!=null && !"".equals(employee.trim())) {
			sql += " and pr.productUserCode in (:productUserCode)";
		}*/
		if(processess!=null && !"".equals(processess.trim())) {
			sql += " and pr.processCode in (:processCode)";
		}
		if(workpiece!=null && !"".equals(workpiece.trim())) {
			sql += " and pr.workPieceCode in (:workPieceCode)";
		}
		String countQuery = "select count(w.pu_code) from (" + sql + groupBy + " ) w";
		NativeQuery countQueryObj = getSession().createNativeQuery(countQuery);
		
		String dataQuery =  sql + groupBy;
		
		NativeQuery dataQueryObj = getSession().createNativeQuery(dataQuery);
		dataQueryObj.setFirstResult((page-1)*rows)
		.setMaxResults(rows);
		if(productionUnitId!=null &&!"".equals(productionUnitId.trim())) {
			countQueryObj.setParameter("productionUnitId", productionUnitId);
			dataQueryObj.setParameter("productionUnitId", productionUnitId);
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
				String day = endDateStr.substring(endDateStr.length()-2);
				String endDate = endDateStr.substring(0,endDateStr.length()-2);
				int newDay = Integer.parseInt(day)+1;
				endDate = endDate+newDay;
				countQueryObj.setParameter("endTime", format.parse(endDate));
				dataQueryObj.setParameter("endTime", format.parse(endDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(deviceSite!=null && !"".equals(deviceSite.trim())) {
			String[] deviceSites = deviceSite.split(",");
			countQueryObj.setParameterList("deviceSiteCodes", deviceSites);
			dataQueryObj.setParameterList("deviceSiteCodes", deviceSites);
		}
		if(processess!=null && !"".equals(processess.trim())) {
			String[] processesses = processess.split(",");
			countQueryObj.setParameterList("processCode", processesses);
			dataQueryObj.setParameterList("processCode", processesses);
		}
		if(workpiece!=null && !"".equals(workpiece.trim())) {
			String[] workpieces = workpiece.split(",");
			countQueryObj.setParameterList("workPieceCode", workpieces);
			dataQueryObj.setParameterList("workPieceCode", workpieces);
		}
		List obj = countQueryObj.list();
		long count = (Integer) countQueryObj.list().get(0);
		if(count<1) {
			return new Pager<List<Object[]>>();
		}
		
		List list = dataQueryObj.list();
		return new Pager<List<Object[]>>((page-1)*rows, rows, (int)count, list);
	}
}
