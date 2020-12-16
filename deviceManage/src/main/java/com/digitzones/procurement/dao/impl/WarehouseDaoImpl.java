package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IWarehouseDao;
import com.digitzones.procurement.model.Warehouse;
import org.springframework.stereotype.Repository;
@Repository("warehouseDao")
public class WarehouseDaoImpl extends CommonDaoImpl<Warehouse> implements IWarehouseDao {
    public WarehouseDaoImpl() {
        super(Warehouse.class);
    }
}
