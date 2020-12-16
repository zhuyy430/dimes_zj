package com.digitzones.procurement.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IPO_PomainDao;
import com.digitzones.procurement.model.PO_Pomain;

import java.util.List;

@Repository
public class PO_PomainDaoImpl extends CommonDaoImpl<PO_Pomain> implements IPO_PomainDao {
	public PO_PomainDaoImpl() {
		super(PO_Pomain.class);
	}
	/**
	 * 根据入库申请单单号查找采购订单
	 *
	 * @param formNo
	 * @return
	 */
	@Override
	public List<PO_Pomain> queryByWarehousingApplicationFormNoAndBarCode(String formNo,String barCode) {
		return getSession().createNativeQuery("select po.* from View_PO_Pomain po inner join WarehousingApplicationFormDetail detail on po.cPOID=detail.purchasingNo " +
				" inner join WarehousingApplicationForm form on form.formNo=detail.WAREHOUSINGAPPLICATIONFORM_CODE " +
				" inner join BoxBar bar on detail.id=bar.fkey and bar.tableName='WarehousingApplicationFormDetail' where bar.barCode=?0 and form.formNo=?1")
				.setParameter(0,Long.valueOf(barCode))
				.setParameter(1,formNo)
				.addEntity(PO_Pomain.class)
				.list();
	}
}
