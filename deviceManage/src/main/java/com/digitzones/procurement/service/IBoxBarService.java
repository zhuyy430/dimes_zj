package com.digitzones.procurement.service;

import com.digitzones.procurement.model.BoxBar;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 箱号条码service
 */
public interface IBoxBarService extends ICommonService<BoxBar> {
    /**
     * 查询最大箱号条码
     * @return Long 最大的箱号条码
     */
   public Integer queryMaxBoxBarCode();
    /**
     * 根据入库申请单号删除箱号条码信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);

    /**
     * 通过单号查询箱子
     */
    public List<BoxBar> queryBoxBarByFormNo(String  tableName,String formNo);
    /**
     * 通过工单号查询箱子
     */
    public List<BoxBar> queryBoxBarByWorkSheetNo(String  workSheetNo);

    /**
     * 通过箱条码查询箱子
     */
    public BoxBar queryBoxBarBybarCode(Long barCode);
    /**
     * 根据工单号和工序号查找最大箱号
     * @param no
     * @param processCode
     * @return
     */
   public Integer queryMaxBoxNumByNoAndProcessCode(String no, String processCode);
    /**
     * 根据箱号类型查找最大入库单号
     */
    public String queryMaxWarehouseNoByboxBarType(String type);
    /**
     * 根据箱号条码查找原材料箱号条码信息
     * @param boxBar
     * @return
     */
    public List<BoxBar> queryRawMaterialByBoxBar(String boxBar);

 /**
  * 根据报工单详情id删除箱号条码信息
  * @param detailId
  */
    void deleteByJobBookingFormDetailId(String detailId);
    /**
     * 通过PackageCode的code查找扫描过的boxbar
     */
    public List<BoxBar> queryBoxBerByPackageCode(String code);

    /***
     * 根据箱条码和工单号查找
     * @param barCode
     * @param no
     * @return
     */
    public BoxBar queryByBarCodeAndNo(Long barCode, String no);

    /**
     * 根据条码查询，是否同一个班次
     * @param barCodes
     * @return
     */
   public boolean queryIsTheSameClass(String[] barCodes);
    /**
     * 判断条码是否属于同一种单据类型
     * @param barCodeArr
     * @return
     */
    int queryIsTheSameBillType(String[] barCodeArr);

    /**
     * 查询是否为报工单类型
     * @param barCodeArr
     * @return
     */
    boolean queryIsTheJobBookingType(String[] barCodeArr);

    /**
     * 查询钢材库货位和数量
     * @param  ANum  区域（A，B,X）
     * @param  rowNum  行号
     * @param  rowNum  前30列，或者后30列（填写before或者 after）
     * @return
     */
    List<Object[]> queryInventoryByPositon(String ANum,String rowNum,String type);
}
