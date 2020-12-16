package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.ISteelMillDao;
import com.digitzones.procurement.model.SteelMill;
import com.digitzones.procurement.service.ISteelMillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class SteelMillServiceImpl implements ISteelMillService {
    @Autowired
    private ISteelMillDao steelMillDao;
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
        return steelMillDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(SteelMill obj) {
        steelMillDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public SteelMill queryByProperty(String name, String value) {
        return steelMillDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(SteelMill obj) {
        return steelMillDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public SteelMill queryObjById(Serializable id) {
        return steelMillDao.findById(id);
    }

    /**
     * 根据id删除对象
     *
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        steelMillDao.deleteById(id);
    }

    /**
     * 查询所有钢厂
     *
     * @return
     */
    @Override
    public List<SteelMill> queryAll() {
        return steelMillDao.findAll();
    }
}
