package com.digitzones.dao.impl;

import com.digitzones.dao.IPackageCodeAndBoxBarMappingDao;
import com.digitzones.model.PackageCodeAndBoxBarMapping;
import org.springframework.stereotype.Repository;

@Repository
public class PackageCodeAndBoxBarMappingDaoImpl extends CommonDaoImpl<PackageCodeAndBoxBarMapping> implements IPackageCodeAndBoxBarMappingDao {
        public PackageCodeAndBoxBarMappingDaoImpl(){
            super(PackageCodeAndBoxBarMapping.class);
        }
    /**
     * 根据包装条码删除关联信息
     * @param packageCode
     */
    @Override
    public void deleteByPackageCode(String packageCode) {
        getSession().createNativeQuery("delete from PackageCodeAndBoxBarMapping where packageCode=?0").setParameter(0,packageCode).executeUpdate();
    }
}
