package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IWarehouseDao;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

@Service("warehouseService")
public class WarehouseServiceImpl implements IWarehouseService {
    @Autowired
    @Qualifier("warehouseDao")
    private IWarehouseDao warehouseDao;
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
        return warehouseDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(Warehouse obj) {
        warehouseDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public Warehouse queryByProperty(String name, String value) {
        return warehouseDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(Warehouse obj) {
        return warehouseDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public Warehouse queryObjById(Serializable id) {
        return warehouseDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        warehouseDao.deleteById(id);
    }

    @Override
    public List<Warehouse> queryAllWarehouse() {
        return warehouseDao.findAll();
    }
}
