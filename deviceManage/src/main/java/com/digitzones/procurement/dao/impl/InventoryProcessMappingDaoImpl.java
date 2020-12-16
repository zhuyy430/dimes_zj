package com.digitzones.procurement.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IInventoryProcessMappingDao;
import com.digitzones.procurement.model.InventoryProcessMapping;
@Repository
public class InventoryProcessMappingDaoImpl extends CommonDaoImpl<InventoryProcessMapping> implements IInventoryProcessMappingDao {
	public InventoryProcessMappingDaoImpl() {
		super(InventoryProcessMapping.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByInventoryCodeAndProcessId( Long processId) {
		getSession().createSQLQuery("delete from INVENTORY_PROCESS where id=?0 ")
		.setParameter(0, processId)
		.executeUpdate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteByInventoryCode(String InventoryCode) {
		getSession().createSQLQuery("delete from INVENTORY_PROCESS where INVENTORY_CODE=?0 ")
		.setParameter(0, InventoryCode)
		.executeUpdate();
	}

	@Override
	public List<InventoryProcessMapping> queryByInventoryCode(String InventoryCode) {
		return this.findByHQL("from InventoryProcessMapping wpm where wpm.inventory.cInvCode=?0", new Object[] {InventoryCode});
	}
	/**
	 * 工具工单单号查找工序编码和名称
	 * @param no
	 * @return
	 */
	@Override
	public List<Object[]> queryProcessCodeAndNameByNo(String no) {
		String sql = "select distinct p.code,p.name from INVENTORY_PROCESS ip inner join PROCESSES p on ip.PROCESS_ID=p.id" +
				" inner join WORKPIECE i on ip.INVENTORY_CODE=i.cInvCode " +
				" inner join WORKSHEET w on w.workPieceCode=i.cInvCode where w.no=?0";
		return getSession().createNativeQuery(sql).setParameter(0,no).list();
	}
}
