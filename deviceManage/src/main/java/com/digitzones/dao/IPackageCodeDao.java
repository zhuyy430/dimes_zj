package com.digitzones.dao;

import com.digitzones.model.PackageCode;
import com.digitzones.procurement.model.BoxBar;

import java.util.List;

/**
 * 设备dao
 * @author zdq
 * 2018年6月11日
 */
public interface IPackageCodeDao extends ICommonDao<PackageCode> {
    /**
     * 根据包装条码查找BoxBar
     * @param packageCode
     * @return
     */
   public  List<BoxBar> queryBoxBarByPackageCode(String packageCode);
}
