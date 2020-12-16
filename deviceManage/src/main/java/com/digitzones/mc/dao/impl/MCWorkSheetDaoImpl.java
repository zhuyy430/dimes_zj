package com.digitzones.mc.dao.impl;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCWorkSheetDao;
import com.digitzones.mc.model.MCWorkSheet;
import com.digitzones.model.WorkSheet;
import com.digitzones.model.WorkSheetDetail;

@Repository
public class MCWorkSheetDaoImpl extends CommonDaoImpl<WorkSheetDetail> implements IMCWorkSheetDao{
	public MCWorkSheetDaoImpl() {
		super(WorkSheetDetail.class);
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<WorkSheetDetail> findWorkSheetDetailList(String deviceSiteCode) {
		return (List<WorkSheetDetail>)this.getHibernateTemplate().find("from WorkSheetDetail wsd where wsd.deviceSiteCode=?0 ", deviceSiteCode);
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCWorkSheet> queryNotCompleteWorkSheetByDeviceCode(String deviceSiteCode) {
		/*String sql = "select detail.id, detail.devicesitecode,detail.reportCount,detail.completeCount,sheet.manufactureDate ,sheet.no,sheet.workPieceCode,sheet.workPieceName" + 
				",sheet.customerGraphNumber,sheet.graphNumber,sheet.batchNumber,sheet.stoveNumber" + 
				",sheet.version,detail.productionCount,detail.status "
				+ ",sheet.workPieceCode,sheet.workPieceName,detail.processCode,detail.processName from WORKSHEET sheet inner join " + 
				" WORKSHEETDETAIL detail on sheet.id=detail.WORKSHEET_ID where detail.status!=?1 and detail.deviceSiteCode=?0 and sheet.deleted=?2";
		return getSession().createSQLQuery(sql).setParameter(0, deviceSiteCode).setParameter(1, Constant.WorkSheet.Status.COMPLETE).
				setParameter(2, false).addEntity(MCWorkSheet.class).list();*/
		String sql = "select distinct  detail.id, detail.devicesitecode,detail.reportCount,detail.completeCount,sheet.manufactureDate ,sheet.no,sheet.workPieceCode,sheet.workPieceName" +
				",sheet.graphNumber,sheet.batchNumber,sheet.stoveNumber,sheet.unitType" + 
				",detail.productionCount,detail.status "
				+ ",sheet.workPieceCode,sheet.workPieceName,detail.processCode,detail.processName,sheet.productionUnitCode from WORKSHEET sheet inner join " + 
				/*" WORKSHEETDETAIL detail on sheet.id=detail.WORKSHEET_ID where detail.deviceSiteCode=?0 and sheet.deleted=?1";*/
				" WORKSHEETDETAIL detail on sheet.id=detail.WORKSHEET_ID where detail.status IN (?1,?3,?4) and detail.deviceSiteCode=?0 and sheet.deleted=?2";
		return getSession().createSQLQuery(sql).setParameter(0, deviceSiteCode).setParameter(1, Constant.WorkSheet.Status.PLAN).setParameter(3, Constant.WorkSheet.Status.PROCESSING).setParameter(4, Constant.WorkSheet.Status.STOP).
				setParameter(2, false).addEntity(MCWorkSheet.class).list();
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<MCWorkSheet> queryProcessingWorkSheetsByDeviceCode(String deviceSiteCode) {
		String sql = "select detail.id, detail.devicesitecode,detail.reportCount,detail.completeCount,sheet.manufactureDate ,sheet.no,sheet.workPieceCode,sheet.workPieceName" + 
				",sheet.graphNumber,sheet.batchNumber,sheet.stoveNumber,sheet.unitType" + 
				",detail.productionCount,detail.status "
				+ ",sheet.workPieceCode,sheet.workPieceName,detail.processCode,detail.processName,sheet.productionUnitCode from WORKSHEET sheet inner join " + 
				" WORKSHEETDETAIL detail on sheet.id=detail.WORKSHEET_ID where detail.status=?1 and detail.deviceSiteCode=?0 and sheet.deleted=?2";
		return getSession().createSQLQuery(sql).setParameter(0, deviceSiteCode).setParameter(1, Constant.WorkSheet.Status.PROCESSING)
				.setParameter(2, false).addEntity(MCWorkSheet.class).list();
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<MCWorkSheet> queryProcessingWorkSheets(String clientIp) {
		String sql = "select detail.id, detail.devicesitecode,detail.reportCount,detail.completeCount,sheet.manufactureDate ,sheet.no,sheet.workPieceCode,sheet.workPieceName" + 
				",sheet.graphNumber,sheet.batchNumber,sheet.stoveNumber,sheet.unitType" + 
				",detail.productionCount,detail.status"
				+ ",sheet.workPieceCode,sheet.workPieceName,detail.processCode,detail.processName,sheet.productionUnitCode from WORKSHEET sheet inner join " + 
				" WORKSHEETDETAIL detail on sheet.id=detail.WORKSHEET_ID where detail.status=?0 and sheet.deleted=?2 and detail.deleted=?3 "
				+ " and detail.deviceSiteCode in ("
				+ " select mc.deviceSiteCode from MC_DEVICESITE mc where mc.clientIp=?1)";
		return getSession().createSQLQuery(sql).setParameter(0, Constant.WorkSheet.Status.PROCESSING).setParameter(1, clientIp).setParameter(2, false).setParameter(3, false).addEntity(MCWorkSheet.class).list();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<WorkSheet> queryWorkSheetByNo(String workSheetNo) {
		String hql ="from WorkSheet where no=?0";
		return (List<WorkSheet>)this.getHibernateTemplate().find(hql, workSheetNo);
	}
}
