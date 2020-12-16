package com.digitzones.procurement.dao.impl;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.ISteelMillDao;
import com.digitzones.procurement.model.SteelMill;
import com.digitzones.procurement.vo.SteelMillVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SteelMillDaoImpl extends CommonDaoImpl<SteelMill> implements ISteelMillDao {
    public SteelMillDaoImpl() {
        super(SteelMill.class);
    }
}
