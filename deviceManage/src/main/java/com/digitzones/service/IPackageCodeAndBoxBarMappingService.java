package com.digitzones.service;

import com.digitzones.model.PackageCodeAndBoxBarMapping;

import java.util.List;

/**
 * 报工箱与条码箱关联service
 */
public interface IPackageCodeAndBoxBarMappingService extends ICommonService<PackageCodeAndBoxBarMapping> {
    /**
     * 通过PackageCode的code查找扫描过的boxbar
     */
    public List<PackageCodeAndBoxBarMapping> queryBoxBerByPackageCode(String code);
}
