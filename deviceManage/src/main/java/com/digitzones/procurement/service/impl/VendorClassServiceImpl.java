package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IVendorClassDao;
import com.digitzones.procurement.model.VendorClass;
import com.digitzones.procurement.service.IVendorClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class VendorClassServiceImpl implements IVendorClassService {
    @Autowired
    private IVendorClassDao vendorClassDao;
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
        return vendorClassDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(VendorClass obj) {
        vendorClassDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public VendorClass queryByProperty(String name, String value) {
        return vendorClassDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(VendorClass obj) {
        return vendorClassDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public VendorClass queryObjById(Serializable id) {
        return vendorClassDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        vendorClassDao.deleteById(id);
    }
    /**
     * 查询所有供应商类别
     * @return
     */
    @Override
    public List<VendorClass> queryAllVendorClasses() {
        return vendorClassDao.findAll();
    }
}
