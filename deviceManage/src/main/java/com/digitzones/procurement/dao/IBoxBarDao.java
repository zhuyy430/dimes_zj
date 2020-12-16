package com.digitzones.procurement.dao;

import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.BoxBar;

import java.util.List;

/**
 *  箱号条码dao
 */
public interface IBoxBarDao extends ICommonDao<BoxBar> {
    /**
     * 查找最大箱号条码
     * @return
     */
   public Integer queryMaxBoxBarCode();
    /**
     * 根据入库申请单号删除箱号条码信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);
    /**
     * 根据工单编号工序查找最大报工单箱号
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
   * 根据报工单 详情id删除箱号条码信息
   * @param detailId
   */
 void deleteByJobBookingFormDetailId(String detailId);
    /**
     * 根据箱条码和工单单号查找
     * @param barCode
     * @param no
     * @return
     */
   public  BoxBar queryByBarCodeAndNo(Long barCode, String no);
    /**
     * 根据条码查询，是否同一个班次
     * @param barCodes
     * @return
     */
    boolean queryIsTheSameClass(String[] barCodes);

    /**
     * 查询是否为同一个单据类型
     * @param barCodeArr
     * @return
     */
    int queryIsTheSameBillType(String[] barCodeArr);
    /**
     * 判断是否为报工单
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
