package com.digitzones.service;

import com.digitzones.model.Location;

import java.util.List;

/**
 * 货位
 */
public interface ILocationService extends ICommonService<Location> {

    public List<Location> queryLocationByCodeAndwarehouseCode(String code, String warehouseCode);
}
