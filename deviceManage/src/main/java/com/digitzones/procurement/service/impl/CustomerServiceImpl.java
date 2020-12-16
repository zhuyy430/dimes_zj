package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.ICustomerDao;
import com.digitzones.procurement.model.Customer;
import com.digitzones.procurement.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private ICustomerDao customerDao;
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
        return customerDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(Customer obj) {
        customerDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public Customer queryByProperty(String name, String value) {
        return customerDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(Customer obj) {
        return customerDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public Customer queryObjById(Serializable id) {
        return customerDao.findById(id);
    }

    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        customerDao.deleteById(id);
    }
}
