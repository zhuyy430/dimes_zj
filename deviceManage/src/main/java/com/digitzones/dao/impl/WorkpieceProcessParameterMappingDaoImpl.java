package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IWorkpieceProcessParameterMappingDao;
import com.digitzones.model.WorkpieceProcessParameterMapping;

import java.util.List;

@Repository
public class WorkpieceProcessParameterMappingDaoImpl extends CommonDaoImpl<WorkpieceProcessParameterMapping>
		implements IWorkpieceProcessParameterMappingDao {

	public WorkpieceProcessParameterMappingDaoImpl() {
		super(WorkpieceProcessParameterMapping.class);
	}

	/**
	 * 根据工单单号和工序代码查找工件工序参数想信息
	 *
	 * @param no
	 * @param processCode
	 * @return
	 */
	@Override
	public List<WorkpieceProcessParameterMapping> queryByNoAndProcessCode(String no, String processCode) {
		String sql = "select wpp.* from WORKPIECEPROCESS_PARAMETER wpp inner join INVENTORY_PROCESS ip on wpp.WORKPIECEPROCESS_ID = ip.id" +
				" inner join WORKPIECE w on ip.INVENTORY_CODE=w.cInvCode " +
				" inner join WORKSHEET ws on ws.workPieceCode = w.cInvCode " +
				" inner join PROCESSES p on ip.PROCESS_ID = p.id where ws.no=?0 and p.code=?1";
		return getSession().createNativeQuery(sql)
				.setParameter(0,no)
				.setParameter(1,processCode)
				.addEntity(WorkpieceProcessParameterMapping.class)
				.list();
	}
}
