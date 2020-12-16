package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDetailDao;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class WarehousingApplicationFormDetailServiceImpl implements IWarehousingApplicationFormDetailService {
    @Autowired
    private IWarehousingApplicationFormDetailDao warehousingApplicationFormDetailDao;
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
        return warehousingApplicationFormDetailDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(WarehousingApplicationFormDetail obj) {
        warehousingApplicationFormDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public WarehousingApplicationFormDetail queryByProperty(String name, String value) {
        return warehousingApplicationFormDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(WarehousingApplicationFormDetail obj) {
        return warehousingApplicationFormDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public WarehousingApplicationFormDetail queryObjById(Serializable id) {
        return warehousingApplicationFormDetailDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        warehousingApplicationFormDetailDao.deleteById(id);
    }

    /**
     * 根据入库申请单编号查找入库申请单详情信息
     *
     * @param requestNo 入库申请单编号
     * @return
     */
    @Override
    public List<WarehousingApplicationFormDetail> queryByWarehouseingApplicationFormNo(String requestNo) {
        return warehousingApplicationFormDetailDao.findByHQL("from WarehousingApplicationFormDetail detail where detail.warehousingApplicationForm.formNo=?0",new Object[]{requestNo});
    }
    /**
     * 根据入库申请单号查找入库数大于0的记录数
     * @param formNo
     * @return
     */
    @Override
    public Long queryCountOfAmountOfInWarehouseBigThenZero(String formNo) {
        return warehousingApplicationFormDetailDao.findCount("from WarehousingApplicationFormDetail detail where detail.warehousingApplicationForm.formNo=?0" +
                " and detail.amountOfInWarehouse>0",new Object[]{formNo});
    }
}
