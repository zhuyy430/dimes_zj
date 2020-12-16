package com.digitzones.service.impl;

import com.digitzones.dao.IPackageCodeAndBoxBarMappingDao;
import com.digitzones.model.PackageCodeAndBoxBarMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IPackageCodeAndBoxBarMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class PackageCodeAndBoxBarMappingImpl implements IPackageCodeAndBoxBarMappingService {
    @Autowired
    public IPackageCodeAndBoxBarMappingDao packageCodeAndBoxBarMappingDao;


    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return packageCodeAndBoxBarMappingDao.findByPage(hql, pageNo, pageSize, values);
    }

    @Override
    public void updateObj(PackageCodeAndBoxBarMapping obj) {
        packageCodeAndBoxBarMappingDao.update(obj);
    }

    @Override
    public PackageCodeAndBoxBarMapping queryByProperty(String name, String value) {
        return packageCodeAndBoxBarMappingDao.findSingleByProperty(name,value);
    }

    @Override
    public Serializable addObj(PackageCodeAndBoxBarMapping obj) {
        return packageCodeAndBoxBarMappingDao.save(obj);
    }

    @Override
    public PackageCodeAndBoxBarMapping queryObjById(Serializable id) {
        return packageCodeAndBoxBarMappingDao.findById(id);
    }

    @Override
    public void deleteObj(Serializable id) {
        packageCodeAndBoxBarMappingDao.deleteById(id);
    }

    @Override
    public List<PackageCodeAndBoxBarMapping> queryBoxBerByPackageCode(String code) {
        return packageCodeAndBoxBarMappingDao.findByHQL("from PackageCodeAndBoxBarMapping pbm where pbm.PackageCode=?0",new Object[]{code});
    }
}
