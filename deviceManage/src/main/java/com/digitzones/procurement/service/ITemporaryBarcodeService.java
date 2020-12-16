package com.digitzones.procurement.service;
import com.digitzones.procurement.model.TemporaryBarcode;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.service.ICommonService;

import java.util.Date;
import java.util.List;
/**
 * 临时条码service
 */
public interface ITemporaryBarcodeService extends ICommonService<TemporaryBarcode> {
    /**
     * 根据录入日期查找最大临时条码单号
     * @param date 录入日期
     * @return 最大临时条码单号
     */
    public String queryMaxRequestNoByBillDate(Date date);

    /**
     * 保存临时条码(新增)
     * @param form
     * @param details
     */
    public void addTemporaryBarcode(TemporaryBarcode form, List<TemporaryBarcodeDetail> details);
    /**
     * 保存临时条码(查看)
     * @param form 临时条码对象
     * @param details 新增或更新的临时条码详情列表
     * @param deletedIds 删除的临时条码详情id
     */
    public void addTemporaryBarcode(TemporaryBarcode form, List<TemporaryBarcodeDetail> details, List<String> deletedIds);
    /**
     * 根据临时条码号删除临时条码及详情信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);
    /**
     * 审核临时条码
     * @param form 临时条码对象
     */
    public void audit(TemporaryBarcode form);
    /**
     * 反审核
     * @param form 临时条码对象
     */
   public  void unaudit(TemporaryBarcode form);
}
