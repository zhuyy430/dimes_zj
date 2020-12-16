package com.digitzones.dao.impl;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.digitzones.dao.ITraceDao;
import com.digitzones.model.TraceParameter;
import com.digitzones.model.TracePositive;
import com.digitzones.model.TraceReverse;
@Repository
public class TraceDaoImpl implements ITraceDao {

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
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<TraceReverse> findTracereverse(String serialNo, String batchNumber, String deviceSiteCode,
			Integer start, Integer end) {
		String sql1 = "select top "+(end-start+1)+" convert(varchar(100),p.collectionDate,20) collectionDate, pp.id,p.deviceSiteCode,p.deviceSiteName,p.productNum,p.processCode,p.processName,pp.parameterCode,pp.parameterName,pp.upLine,pp.lowLine, "
		  +" pp.standardValue,pp.parameterValue,pp.status,pp.statusCode,p.batchNumber,p.stoveNumber "
		  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
		  +" where 1=1";
	  
		if(serialNo!=null&&!"".equals(serialNo)){
			sql1 +=" and p.opcNo like '%"+serialNo+"%'";
		}
		if(batchNumber!=null&&!"".equals(batchNumber)){
			sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
		}
		if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
			sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
		}
		String sql2="select top "+(start-1)+" pp.id from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				+" where 1=1";
		if(serialNo!=null&&!"".equals(serialNo)){
			sql2 +=" and p.productNum like '%"+serialNo+"%'";
		}
		if(batchNumber!=null&&!"".equals(batchNumber)){
			sql2 +=" and p.batchNumber like '%"+batchNumber+"%'";
		}
		if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
			sql2 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
		}
		String sql =sql1+" and pp.id not in("+sql2+" order by p.collectionDate desc,p.deviceSiteCode,pp.parameterCode) order by p.collectionDate desc,p.deviceSiteCode,pp.parameterCode" ;
		return getSession().createNativeQuery(sql).addEntity(TraceReverse.class).list();
	}

	@Override
	public int findTracereverseCount(String serialNo, String batchNumber, String deviceSiteCode) {
		String sql1 = "select count(pp.id) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
				  
		if(serialNo!=null&&!"".equals(serialNo)){
			sql1 +=" and p.opcNo like '%"+serialNo+"%'";
		}
		if(batchNumber!=null&&!"".equals(batchNumber)){
			sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
		}
		if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
			sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
		}
		return (int) getSession().createNativeQuery(sql1).uniqueResult();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<TracePositive> findTracepositive(String batchNumber, String stoveNumber, String deviceSiteCode,String ng,
			Integer start, Integer end) {
		String sql1 = "select top "+(end-start+1)+" convert(varchar(100),p.collectionDate,20) collectionDate, pp.id,p.opcNo,p.productNum,p.workPieceCode,p.workPieceName,p.processCode,p.processName,pp.parameterCode, "
				  +" pp.parameterName,pp.upLine,pp.lowLine,pp.standardValue,pp.parameterValue,pp.status,pp.statusCode "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
			  
				if(batchNumber!=null&&!"".equals(batchNumber)){
					sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
				}
				if(stoveNumber!=null&&!"".equals(stoveNumber)){
					sql1 +=" and p.stoveNumber like '%"+stoveNumber+"%'";
				}
				if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
					sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
				}
				String sql2="select top "+(start-1)+" pp.id from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
						+" where 1=1";
				if(batchNumber!=null&&!"".equals(batchNumber)){
					sql2 +=" and p.batchNumber like '%"+batchNumber+"%'";
				}
				if(stoveNumber!=null&&!"".equals(stoveNumber)){
					sql2 +=" and p.stoveNumber like '%"+stoveNumber+"%'";
				}
				if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
					sql2 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
				}
				if(!StringUtils.isEmpty(ng)) {
					sql1 += " and (p.status='ng' or p.status='NG')";
					sql2 += " and (p.status='ng' or p.status='NG')";
				}
				String sql =sql1+"and pp.id not in("+sql2+"order by p.collectionDate desc,p.deviceSiteCode,pp.parameterCode) order by p.collectionDate desc,p.deviceSiteCode,pp.parameterCode";
				return getSession().createNativeQuery(sql).addEntity(TracePositive.class).list();
	}
	@Override
	public int findTracepositiveCount(String batchNumber, String stoveNumber, String deviceSiteCode,String ng) {
		String sql1 = "select count(p.id) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
				  
		if(batchNumber!=null&&!"".equals(batchNumber)){
			sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
		}
		if(stoveNumber!=null&&!"".equals(stoveNumber)){
			sql1 +=" and p.stoveNumber like '%"+stoveNumber+"%'";
		}
		if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
			sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
		}
		if(!StringUtils.isEmpty(ng)) {
			sql1 += " and (p.status='ng' or p.status='NG')";
		}
		return (int) getSession().createNativeQuery(sql1).uniqueResult();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<TraceParameter> findTraceparameter(String parameterCode,String worksheetNo,String minValue,String maxValue, Integer start, Integer end) {
		String sql1 = "select top "+(end-start+1)+" convert(varchar(100),p.collectionDate,20) collectionDate, pp.id,p.opcNo,p.workPieceCode,p.workPieceName,p.batchNumber,p.stoveNumber,p.processCode,p.processName,pp.parameterCode, "
				  +" pp.parameterName,pp.upLine,  pp.lowLine,pp.standardValue,pp.parameterValue,pp.status,pp.statusCode "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
			  
				if(parameterCode!=null&&!"".equals(parameterCode)){
					sql1 +=" and pp.parameterCode like '%"+parameterCode+"%'";
				}
				if(worksheetNo!=null&&!"".equals(worksheetNo)){
					sql1 +=" and p.no like '%"+worksheetNo+"%'";
				}
				
				if(!StringUtils.isEmpty(minValue)) {
					sql1 += " and pp.parameterValue>=" + minValue;
				}
				if(!StringUtils.isEmpty(maxValue)) {
					sql1 += " and pp.parameterValue<=" + maxValue;
				}
				
				String sql2="select top "+(start-1)+" pp.id from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
						+" where 1=1";
				if(parameterCode!=null&&!"".equals(parameterCode)){
					sql2 +=" and pp.parameterCode like '%"+parameterCode+"%'";
				}
				if(worksheetNo!=null&&!"".equals(worksheetNo)){
					sql1 +=" and p.no like '%"+worksheetNo+"%'";
				}
				
				if(!StringUtils.isEmpty(minValue)) {
					sql1 += " and pp.parameterValue>=" + minValue;
				}
				if(!StringUtils.isEmpty(maxValue)) {
					sql1 += " and pp.parameterValue<=" + maxValue;
				}
				String sql =sql1+"and pp.id not in("+sql2+")";
				return getSession().createNativeQuery(sql).addEntity(TraceParameter.class).list();
	}
	@Override
	public int findTraceparameterCount(String parameterCode,String worksheetNo,String batchNum,String minValue,String maxValue) {
		String sql1 = "select count(pp.id) "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
				  
		if(parameterCode!=null&&!"".equals(parameterCode)){
			sql1 +=" and pp.parameterCode like '%"+parameterCode+"%'";
		}
		if(worksheetNo!=null&&!"".equals(worksheetNo)){
			sql1 +=" and p.no like '%"+worksheetNo+"%'";
		}
		if(!StringUtils.isEmpty(batchNum)){
			sql1 +=" and p.batchNumber like '%"+batchNum+"%'";
		}
		
		if(!StringUtils.isEmpty(minValue)) {
			sql1 += " and pp.parameterValue>=" + minValue;
		}
		if(!StringUtils.isEmpty(maxValue)) {
			sql1 += " and pp.parameterValue<=" + maxValue;
		}
		return (int) getSession().createNativeQuery(sql1).uniqueResult();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<TraceReverse> findTracereverse(String serialNo, String batchNumber, String deviceSiteCode) {
		String sql1 = "select  pp.id,convert(varchar(100),p.collectionDate,20) collectionDate,p.deviceSiteCode,p.deviceSiteName,p.processCode,p.processName,pp.parameterCode,pp.parameterName,pp.upLine,pp.lowLine, "
				  +" pp.standardValue,pp.parameterValue,pp.status,pp.statusCode,p.batchNumber,p.stoveNumber "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
			  
				if(serialNo!=null&&!"".equals(serialNo)){
					sql1 +=" and p.opcNo like '%"+serialNo+"%'";
				}
				if(batchNumber!=null&&!"".equals(batchNumber)){
					sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
				}
				if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
					sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
				}
				return getSession().createNativeQuery(sql1).addEntity(TraceReverse.class).list();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<TracePositive> findTracepositive(String batchNumber, String stoveNumber, String deviceSiteCode,String ng) {
		String sql1 = "select  pp.id,p.productNum,convert(varchar(100),p.collectionDate,20) collectionDate, p.opcNo,p.workPieceCode,p.workPieceName,p.processCode,p.processName,pp.parameterCode, "
				  +" pp.parameterName,pp.upLine,pp.lowLine,pp.standardValue,pp.parameterValue,pp.status,pp.statusCode "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
				if(batchNumber!=null&&!"".equals(batchNumber)){
					sql1 +=" and p.batchNumber like '%"+batchNumber+"%'";
				}
				if(stoveNumber!=null&&!"".equals(stoveNumber)){
					sql1 +=" and p.stoveNumber like '%"+stoveNumber+"%'";
				}
				if(deviceSiteCode!=null&&!"".equals(deviceSiteCode)){
					sql1 +=" and p.deviceSiteCode like '%"+deviceSiteCode+"%'";
				}
				
				if(!StringUtils.isEmpty(ng)) {
					sql1 += " and (p.status='ng' or p.status='NG')";
				}
				return getSession().createNativeQuery(sql1).addEntity(TracePositive.class).list();
	}
	@SuppressWarnings({"unchecked" })
	@Override
	public List<TraceParameter> findTraceparameter(String parameterCode, String worksheetNo, String batchNum,
			String minValue, String maxValue) {
		String sql1 = "select  pp.id,p.opcNo,p.workPieceCode,p.workPieceName,p.batchNumber,p.stoveNumber,p.processCode,p.processName,pp.parameterCode, "
				  +" pp.parameterName,pp.upLine,  pp.lowLine,pp.standardValue,pp.parameterValue,pp.status,pp.statusCode "
				  +" from PROCESSRECORD p inner join PROCESSPARAMETERRECORD pp on p.id=pp.PROCESSRECORD_ID "
				  +" where 1=1";
			  
				if(parameterCode!=null&&!"".equals(parameterCode)){
					sql1 +=" and pp.parameterCode like '%"+parameterCode+"%'";
				}
				if(worksheetNo!=null&&!"".equals(worksheetNo)){
					sql1 +=" and p.no like '%"+worksheetNo+"%'";
				}
				if(!StringUtils.isEmpty(batchNum)){
					sql1 +=" and p.batchNumber like '%"+batchNum+"%'";
				}
				if(!StringUtils.isEmpty(minValue)) {
					sql1 += " and pp.parameterValue>=" + minValue;
				}
				if(!StringUtils.isEmpty(maxValue)) {
					sql1 += " and pp.parameterValue<=" + maxValue;
				}
				return getSession().createNativeQuery(sql1).addEntity(TraceParameter.class).list();
	}

}
