package com.digitzones.dao.impl;
import com.digitzones.procurement.model.BoxBar;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IPackageCodeDao;
import com.digitzones.model.PackageCode;

import java.util.List;

@Repository
public class PackageCodeDaoImpl extends CommonDaoImpl<PackageCode> implements IPackageCodeDao {
	public PackageCodeDaoImpl() {
		super(PackageCode.class);
	}
	/**
	 * 根据包装条码查找BoxBar
	 * @param packageCode
	 * @return
	 */
	@Override
	public List<BoxBar> queryBoxBarByPackageCode(String packageCode) {
		String sql = "select bb.* from PackageCodeAndBoxBarMapping m inner join BoxBar bb on m.barCode=bb.barCode where m.PackageCode=?0";
		return getSession().createNativeQuery(sql).setParameter(0,packageCode).addEntity(BoxBar.class).list();
	}
}
