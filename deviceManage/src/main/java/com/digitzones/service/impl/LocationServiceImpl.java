package com.digitzones.service.impl;

import com.digitzones.dao.ILocationDao;
import com.digitzones.model.Location;
import com.digitzones.model.Pager;
import com.digitzones.service.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class LocationServiceImpl implements ILocationService {
    @Autowired
    private ILocationDao locationDao;
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
        return locationDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(Location obj) {
        locationDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public Location queryByProperty(String name, String value) {
        return locationDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(Location obj) {
        return locationDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public Location queryObjById(Serializable id) {
        return locationDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        locationDao.deleteById(id);
    }

    @Override
    public List<Location> queryLocationByCodeAndwarehouseCode(String code, String warehouseCode) {
        return locationDao.findByHQL("from Location l where l.cPosCode=?0 and l.cWhCode=?1 ",new Object[]{code,warehouseCode});
    }
}
