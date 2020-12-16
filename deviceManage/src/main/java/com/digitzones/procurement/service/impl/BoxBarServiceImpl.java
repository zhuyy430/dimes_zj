package com.digitzones.procurement.service.impl;

import com.digitzones.dao.IWorkSheetDao;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkSheet;
import com.digitzones.procurement.dao.IBoxBarDao;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class BoxBarServiceImpl  implements IBoxBarService {
    @Autowired
    private IBoxBarDao boxBarDao;
    @Autowired
    private IWorkSheetDao workSheetDao;
    /**
     * 分页查询对象
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return boxBarDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(BoxBar obj) {
        boxBarDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public BoxBar queryByProperty(String name, String value) {
        return boxBarDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(BoxBar obj) {
        return boxBarDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public BoxBar queryObjById(Serializable id) {
        return boxBarDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        boxBarDao.deleteById(id);
    }
    /**
     * 查询最大箱号条码
     * @return Long 最大的箱号条码
     */
    @Override
    public Integer queryMaxBoxBarCode() {
        return boxBarDao.queryMaxBoxBarCode();
    }

    /**
     * 根据入库申请单号删除箱号条码信息
     *
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        boxBarDao.deleteByFormNo(formNo);
    }

    @Override
    public List<BoxBar> queryBoxBarByFormNo(String  tableName,String formNo) {
        return boxBarDao.findByHQL("from BoxBar bar where bar.formNo=?0 and bar.tableName=?1",new Object[]{formNo,tableName});
    }
    @Override
    public List<BoxBar> queryBoxBarByWorkSheetNo(String  workSheetNo) {
    	String hql ="from BoxBar b where (tableName='WarehousingApplicationFormDetail' or tableName='JobBookingFormDetail') "+
    			" and (b.barCode in  (select j.barCode from JobBookingFormDetail j where j.no=?0) "+
    			" or b.barCode in (select m.barCode from MaterialRequisitionDetail m where m.materialRequisition.workSheet.no=?0))";
    	return boxBarDao.findByHQL(hql,new Object[]{workSheetNo});
    }

    @Override
    public BoxBar queryBoxBarBybarCode(Long barCode) {
        List<BoxBar> b= boxBarDao.findByHQL("from BoxBar bar where bar.barCode=?0",new Object[]{barCode});
        if(b!=null&&b.size()>0){
            return b.get(0);
        }
        return null;
    }

    /**
     * 根据工单号查找最大箱号
     *
     * @param no
     * @return
     */
    @Override
    public Integer queryMaxBoxNumByNoAndProcessCode(String no,String processCode) {
        return boxBarDao.queryMaxBoxNumByNoAndProcessCode(no,processCode);
    }

    @Override
    public String queryMaxWarehouseNoByboxBarType(String type) {
        return boxBarDao.queryMaxWarehouseNoByboxBarType(type);
    }

    /**
     * 根据箱号条码查找原材料箱号条码信息
     * @param barCode
     * @return
     */
    @Override
    public List<BoxBar> queryRawMaterialByBoxBar(String barCode) {
        return boxBarDao.queryRawMaterialByBoxBar(barCode);
    }

    /**
     * 根据报工单详情id删除箱号条码信息
     * @param detailId 报工单详情id
     */
    @Override
    public void deleteByJobBookingFormDetailId(String detailId) {
        boxBarDao.deleteByJobBookingFormDetailId(detailId);
    }

    @Override
    public List<BoxBar> queryBoxBerByPackageCode(String code) {
        return boxBarDao.findByHQL("select pbm.boxBar from PackageCodeAndBoxBarMapping pbm where pbm.PackageCode=?0",new Object[]{code});
    }
    /***
     * 根据箱条码和工单号查找
     * @param barCode
     * @param no
     * @return
     */
    @Override
    public BoxBar queryByBarCodeAndNo(Long barCode, String no) {
        WorkSheet workSheet = workSheetDao.findSingleByProperty("no",no);
        if(workSheet!=null){
            if(!StringUtils.isEmpty(workSheet.getStoveNumber())){
                return boxBarDao.queryByBarCodeAndNo(barCode,no);
            }
        }
       return boxBarDao.findById(barCode);
    }

    /**
     * 根据条码查询，是否同一个班次
     *
     * @param barCodes
     * @return
     */
    @Override
    public boolean queryIsTheSameClass(String[] barCodes) {
        return boxBarDao.queryIsTheSameClass(barCodes);
    }

    /**
     * 判断条码是否属于同一种单据类型
     *
     * @param barCodeArr
     * @return
     */
    @Override
    public int queryIsTheSameBillType(String[] barCodeArr) {
      /* List<BoxBar> list = ( List<BoxBar>) boxBarDao.findByHQL("select boxBarType from BoxBar where barCode in (?0) group by boxBarType",new Object[]{barCodeArr});
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }else{
            return list.size();
        }*/
      return boxBarDao.queryIsTheSameBillType(barCodeArr);
    }

    /**
     * 查询是否为报工单类型
     *
     * @param barCodeArr
     * @return
     */
    @Override
    public boolean queryIsTheJobBookingType(String[] barCodeArr) {
       /* Long count = boxBarDao.findCount("from BoxBar where barCode in (?0) and boxBarType='报工单'",new Object[]{barCodeArr});
        if(count!=null&&count==barCodeArr.length){
            return true;
        }
        return false;*/
       return boxBarDao.queryIsTheJobBookingType(barCodeArr);
    }

    @Override
    public List<Object[]> queryInventoryByPositon(String ANum, String rowNum, String type) {
        return boxBarDao.queryInventoryByPositon(ANum,rowNum,type);
    }
}
