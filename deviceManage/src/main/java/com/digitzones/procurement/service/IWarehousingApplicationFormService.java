package com.digitzones.procurement.service;
import com.digitzones.model.WorkSheet;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.service.ICommonService;

import java.util.Date;
import java.util.List;

/**
 * 入库申请单service
 */
public interface IWarehousingApplicationFormService extends ICommonService<WarehousingApplicationForm> {
    /**
     * 根据收料日期查找最大入库申请单单号
     * @param date 收料日期
     * @return 最大入库申请单单号
     */
    public String queryMaxRequestNoByReceivingDate(Date date);

    /**
     * 保存入库申请单(新增)
     * @param form
     * @param details
     */
    public void addWarehousingApplicationForm(WarehousingApplicationForm form, List<WarehousingApplicationFormDetail> details);
    /**
     * 保存入库申请单(查看)
     * @param form 入库申请单对象
     * @param details 新增或更新的入库申请单详情列表
     * @param deletedIds 删除的入库申请单详情id
     */
    public void addWarehousingApplicationForm(WarehousingApplicationForm form, List<WarehousingApplicationFormDetail> details,List<String> deletedIds);
    /**
     * 根据入库申请单号删除入库申请单及详情信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);
    /**
     * 审核入库申请单
     * @param form 入库申请单对象
     */
    public void audit(WarehousingApplicationForm form);
    /**
     * 反审核
     * @param form 入库申请单对象
     */
   public  void unaudit(WarehousingApplicationForm form);

    /**
     * 根据箱号条码查找入库申请单
     * @param barCode
     * @return
     */
   public WarehousingApplicationForm queryByBarCode(String barCode);
}
