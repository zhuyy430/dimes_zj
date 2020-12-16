package com.digitzones.procurement.service;
import com.digitzones.procurement.model.VendorClass;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 供应商类别service
 */
public interface IVendorClassService extends ICommonService<VendorClass> {
    /**
     * 查询所有供应商类别
     * @return
     */
    public List<VendorClass> queryAllVendorClasses();
}
