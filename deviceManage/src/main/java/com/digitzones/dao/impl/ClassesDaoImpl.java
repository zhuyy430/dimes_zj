package com.digitzones.dao.impl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IClassesDao;
import com.digitzones.model.Classes;
@Repository
public class ClassesDaoImpl extends CommonDaoImpl<Classes> implements IClassesDao{
	public ClassesDaoImpl() {
		super(Classes.class);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Classes queryCurrentClasses() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sql = "select * from Classes c where "
				+ " (c.beginTime<c.endTime and CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <= CONVERT(varchar(100),c.endTime,108)) " + 
				" or ((c.beginTime>c.endTime and ((CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <='23:59')  or CONVERT(varchar(100),?0,108)>='00:00' and CONVERT(varchar(100),?0,108)<=CONVERT(varchar(100),c.endTime,108))))";
		List<Classes> list = null;
		try {
			list = getSession().createNativeQuery(sql)
					.setParameter(0, sdf.parse(sdf.format(new Date()))).addEntity(Classes.class)
					.list();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (list!=null && list.size()>0)?list .get(0):null;
	}

	@SuppressWarnings({ "deprecation", "unchecked"})
	@Override
	public Classes queryClassesByTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String sql = "select * from Classes c where "
				+ " (c.beginTime<c.endTime and CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <= CONVERT(varchar(100),c.endTime,108)) " + 
				" or (c.beginTime>c.endTime and ((CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <='23:59')  or CONVERT(varchar(100),?0,108)>='00:00' and CONVERT(varchar(100),?0,108)<=CONVERT(varchar(100),c.endTime,108)))";
		List<Classes> list = null;
		try {
			Date d = sdf.parse(sdf.format(date));
			list = getSession().createSQLQuery(sql)
					.setParameter(0, d).addEntity(Classes.class)
					.list();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (list!=null && list.size()>0)?list.get(0):null;
	}
	@SuppressWarnings({ "deprecation", "unchecked"})
	@Override
	public Classes queryClassesByTimeAndClassTypeName(Date date,String classTypeName) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String sql = "select * from Classes c where "
				+ " (c.beginTime<c.endTime and CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <= CONVERT(varchar(100),c.endTime,108)) and c.classTypeName=?1" + 
				" or (c.beginTime>c.endTime and ((CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <='23:59')  or CONVERT(varchar(100),?0,108)>='00:00' and CONVERT(varchar(100),?0,108)<=CONVERT(varchar(100),c.endTime,108)))"
				+ "and c.classTypeName=?1";
		List<Classes> list = null;
		try {
			Date d = sdf.parse(sdf.format(date));
			list = getSession().createSQLQuery(sql)
					.setParameter(0, d).addEntity(Classes.class)
					.setParameter(1, classTypeName)
					.list();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (list!=null && list.size()>0)?list.get(0):null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public Classes queryClassesByDeviceSiteCode(String deviceSiteCode) {
		String sql = "select * from (select c.*,CONVERT(CHAR(20),endTime,108)as endDate,CONVERT(CHAR(20),beginTime,108)as beginDate, convert(char(8),getdate(),108) as nowDate"
					+" from DEVICESITE ds inner join DEVICE d on ds.DEVICE_ID=d.id inner join PRODUCTIONUNIT p on d.PRODUCTIONUNIT_ID = p.id"
					+" inner join CLASSTYPE ct on p.CLASSTYPE_ID=ct.id inner join CLASSES c on ct.id=c.CLASSTYPE_ID where ds.code=?0) as class"
					+" where nowDate BETWEEN beginDate and endDate";
		List<Classes> list = null;
		list = getSession().createSQLQuery(sql)
				.setParameter(0, deviceSiteCode).addEntity(Classes.class)
				.list();
		return (list!=null && list.size()>0)?list.get(0):null;
	}
	@SuppressWarnings({"unchecked"})
	@Override
	public Classes queryCurrentClassesByClassTypeName(String classTypeName) {
		
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sql = "select * from Classes c where "
					+ " (c.beginTime<c.endTime and CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <= CONVERT(varchar(100),c.endTime,108)) and c.classTypeName=?1 " + 
					" or ((c.beginTime>c.endTime and ((CONVERT(varchar(100),?0,108)>CONVERT(varchar(100),c.beginTime,108) and CONVERT(varchar(100),?0,108) <='23:59')  or CONVERT(varchar(100),?0,108)>='00:00' and CONVERT(varchar(100),?0,108)<=CONVERT(varchar(100),c.endTime,108))))"
					+ " and c.classTypeName=?1";
			List<Classes> list = null;
			try {
				list = getSession().createNativeQuery(sql)
						.setParameter(0, sdf.parse(sdf.format(new Date()))).addEntity(Classes.class)
						.setParameter(1, classTypeName)
						.list();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return (list!=null && list.size()>0)?list .get(0):null;
	}
}
