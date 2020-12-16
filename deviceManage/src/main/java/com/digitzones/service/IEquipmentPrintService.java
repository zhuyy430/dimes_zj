package com.digitzones.service;

import com.digitzones.model.EquipmentPrint;

import java.util.List;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 13:30
 */
public interface IEquipmentPrintService extends ICommonService<EquipmentPrint>{

    public List<EquipmentPrint> queryEquipmentPrintByCodeAndBatchNo(String code,String batchNo);
}
