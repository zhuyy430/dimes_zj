package com.digitzones.dao.impl;

import com.digitzones.bo.WorkSheetBo;
import com.digitzones.dao.IWorkSheetDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkSheet;
import org.apache.poi.hssf.record.formula.functions.T;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class WorkSheetDaoImpl extends CommonDaoImpl<WorkSheet> implements IWorkSheetDao {
	public WorkSheetDaoImpl() {
		super(WorkSheet.class);
	}

	/**
	 * 根据制单日期 查找最大单号
	 *
	 * @param now
	 * @return
	 */
	@Override
	public String queryMaxNoByMakeDocumentDate(Date now) {
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		return (String) getSession().createNativeQuery("select MAX(no) from WORKSHEET where year(makeDocumentDate)=?0" +
				" and month(makeDocumentDate)=?1 and day(makeDocumentDate)=?2")
				.setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
				.setParameter(2,c.get(Calendar.DATE)).uniqueResult();
	}

	/**
	 * 根据报工条码查找工单
	 *
	 * @param barCode
	 * @return
	 */
	@Override
	public WorkSheet queryByBarCode(String barCode) {
		List<WorkSheet> obj = getSession().createNativeQuery("select ws.* from JobBookingForm form inner join JobBookingFormDetail detail on detail.JOBBOOKING_CODE=form.formNo " +
				" inner join WORKSHEET ws on form.workSheetNo=ws.no " +
				" where detail.id in (select bar.fkey from BoxBar bar where bar.tableName='JobBookingFormDetail' and bar.barCode=?0)")
				.setParameter(0,barCode)
				.addEntity(WorkSheet.class)
				.list();
		return CollectionUtils.isEmpty(obj)?null:obj.get(0);
	}

	/*@Override
	public List<WorkSheet> queryByMonitoring(String sql) {

		List<WorkSheet> list= this.getSession().createSQLQuery(sql).addEntity(WorkSheet.class).list();

		return list;
	}*/
	@Override
	public Pager<T> queryByMonitoring(String sql, int pageNo, int pageSize, Object... values) {
		// Count查询
		String countQueryString = " select count (*) " + removeSelect(removeOrderBy(sql));
		SQLQuery query1=this.getSession().createSQLQuery(countQueryString);
		for (int i = 0; i < values.length; i++) {
			query1.setParameter(i, values[i]);
		}
		List countlist= query1.list();
		//List countlist = getHibernateTemplate().find(countQueryString.replace("fetch", ""), values);
		long totalCount = countlist.size()<=0?0:Long.parseLong(countlist.get(0).toString()) ;

		if (totalCount < 1)
			return new Pager();
		// 实际查询返回分页对象
		int startIndex = Pager.getStartOfPage(pageNo, pageSize);
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("batchNumber", StandardBasicTypes.STRING);
		query.addScalar("no", StandardBasicTypes.STRING);
		query.addScalar("productionUnitName", StandardBasicTypes.STRING);
		query.addScalar("workPieceCode", StandardBasicTypes.STRING);
		query.addScalar("workPieceName", StandardBasicTypes.STRING);
		query.addScalar("unitType", StandardBasicTypes.STRING);
		query.addScalar("stoveNumber", StandardBasicTypes.STRING);
		query.addScalar("productCount", StandardBasicTypes.INTEGER);
		query.addScalar("firstMaterialDate", StandardBasicTypes.DATE);
		query.addScalar("materialCount", StandardBasicTypes.INTEGER);
		query.addScalar("materialBoxNum", StandardBasicTypes.INTEGER);
		query.addScalar("lastJobbookingDate", StandardBasicTypes.DATE);
		query.addScalar("jobbookingCount", StandardBasicTypes.INTEGER);
		query.addScalar("jobbookingBoxNum", StandardBasicTypes.INTEGER);
		query.addScalar("jobbookingInwarehouseCount", StandardBasicTypes.INTEGER);
		query.addScalar("unqualifiedCounts", StandardBasicTypes.INTEGER);
		query.addScalar("cInvDefine14", StandardBasicTypes.DOUBLE);


		query.setResultTransformer(Transformers.aliasToBean(WorkSheetBo.class));
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Pager(startIndex,  pageSize, (int)totalCount,list);
	}

	@Override
	public List<WorkSheetBo> queryByMonitoring(String sql, Object... values) {

		// 实际查询返回分页对象
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("batchNumber", StandardBasicTypes.STRING);
		query.addScalar("no", StandardBasicTypes.STRING);
		query.addScalar("productionUnitName", StandardBasicTypes.STRING);
		query.addScalar("workPieceCode", StandardBasicTypes.STRING);
		query.addScalar("workPieceName", StandardBasicTypes.STRING);
		query.addScalar("unitType", StandardBasicTypes.STRING);
		query.addScalar("stoveNumber", StandardBasicTypes.STRING);
		query.addScalar("productCount", StandardBasicTypes.INTEGER);
		query.addScalar("firstMaterialDate", StandardBasicTypes.DATE);
		query.addScalar("materialCount", StandardBasicTypes.INTEGER);
		query.addScalar("materialBoxNum", StandardBasicTypes.INTEGER);
		query.addScalar("lastJobbookingDate", StandardBasicTypes.DATE);
		query.addScalar("jobbookingCount", StandardBasicTypes.INTEGER);
		query.addScalar("jobbookingBoxNum", StandardBasicTypes.INTEGER);
		query.addScalar("jobbookingInwarehouseCount", StandardBasicTypes.INTEGER);
		query.addScalar("unqualifiedCounts", StandardBasicTypes.INTEGER);


		query.setResultTransformer(Transformers.aliasToBean(WorkSheetBo.class));
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		List<WorkSheetBo> list = query.list();

		return list;
	}

	/**
	 * 去掉hql语句中的select部分
	 * @param hql
	 * @return
	 */
	private  String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	private String removeOrderBy(String hql) {
		if(hql.toLowerCase().contains("order")) {
			int endPos = hql.toLowerCase().indexOf("order");
			return hql.substring(0, endPos);
		}else {
			return hql;
		}
	}
}
