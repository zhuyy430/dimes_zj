package com.digitzones.dao;

import com.digitzones.model.PackageCodeAndBoxBarMapping;
/**
 * 包装箱与报工箱关联dao
 */
public interface IPackageCodeAndBoxBarMappingDao extends ICommonDao<PackageCodeAndBoxBarMapping> {
    /**
     * 根据包装条码删除关联信息
     * @param packageCode
     */
   public void deleteByPackageCode(String packageCode);
}
