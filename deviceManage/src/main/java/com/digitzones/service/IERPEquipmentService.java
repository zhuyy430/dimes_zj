package com.digitzones.service;

import com.digitzones.model.ERPEquipment;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 12:48
 */
public interface IERPEquipmentService extends ICommonService<ERPEquipment>{

    /**
     * 通过代码和批号查找erp读取的装备
     * @param code
     * @param batchNo
     * @return
     */
    public ERPEquipment queryERPEquipmentByCodeAndBatchNo(String code,String batchNo);
}
