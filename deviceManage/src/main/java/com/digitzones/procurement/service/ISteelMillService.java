package com.digitzones.procurement.service;

import com.digitzones.procurement.model.SteelMill;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 钢厂业务接口
 */
public interface ISteelMillService extends ICommonService<SteelMill> {
    /**
     * 查询所有钢厂
     * @return
     */
    List<SteelMill> queryAll();
}
