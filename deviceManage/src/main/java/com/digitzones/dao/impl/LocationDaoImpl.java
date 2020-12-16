package com.digitzones.dao.impl;

import com.digitzones.dao.ILocationDao;
import com.digitzones.model.Location;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDaoImpl extends CommonDaoImpl<Location> implements ILocationDao {
    public LocationDaoImpl() {
        super(Location.class);
    }
}
