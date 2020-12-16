package com.digitzones.service;
import com.digitzones.model.MaterialRequisition;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.model.User;

import java.util.Date;
import java.util.List;
/**
 * 领用单service
 */
public interface IMaterialRequisitionService extends ICommonService<MaterialRequisition>{
    /**
     * 根据领用日期查找最大领用单号
     * @param now 领用日期
     * @return
     */
  public  String queryMaxFormNoByPickingDate(Date now);
    /**
     * 新增领用工单
     * @param form 领用工单对象
     * @param details 领用工单详情
     */
   public void addMaterialRequisition(MaterialRequisition form, List<MaterialRequisitionDetail> details);
    /**
     * 新增领用工单
     * @param form 领用工单对象
     * @param details 领用工单详情
     * @param deletedIds 删除的id
     */
    public void addMaterialRequisition(MaterialRequisition form, List<MaterialRequisitionDetail> details, List<String> deletedIds);
    /**
     * 根据领料单号删除领料单及详情
     * @param formNo
     */
   public void deleteByFormNo(String formNo);
    /**
     * app领料出库
     */
   public void outWarehouse(User user, String workSheetNo, String BarCodes, String receiveAmounts,String posCodes, String warehouseCode);
    /**
     * 生成ERP领料单
     * @param formNo 领料单号
     * @param no 工单单号
     */
    public void generateERPMaterialRequisition(String formNo, String no);
}
