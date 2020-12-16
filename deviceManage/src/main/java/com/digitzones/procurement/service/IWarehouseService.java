package com.digitzones.procurement.service;

import com.digitzones.procurement.model.Warehouse;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 仓库业务逻辑接口
 */
public interface IWarehouseService extends ICommonService<Warehouse> {
    public List<Warehouse> queryAllWarehouse();
}
