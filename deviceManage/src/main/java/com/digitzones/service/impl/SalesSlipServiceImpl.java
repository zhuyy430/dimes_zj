package com.digitzones.service.impl;

import com.digitzones.dao.ISalesSlipDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SalesSlip;
import com.digitzones.service.ISalesSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class SalesSlipServiceImpl implements ISalesSlipService {
    @Autowired
    private ISalesSlipDao salesSlipDao;
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
        return salesSlipDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(SalesSlip obj) {
        salesSlipDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public SalesSlip queryByProperty(String name, String value) {
        return salesSlipDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(SalesSlip obj) {
        return salesSlipDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public SalesSlip queryObjById(Serializable id) {
        return salesSlipDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        salesSlipDao.deleteById(id);
    }
    /**
     * 根据销售订单号查找销售订单
     * @param idsList
     * @return
     */
    @Override
    public List<SalesSlip> queryByIds(List<Integer> idsList) {
        return salesSlipDao.findByHQL("from SalesSlip ss where ss.autoId in ?0",new Object[]{idsList});
    }
}
