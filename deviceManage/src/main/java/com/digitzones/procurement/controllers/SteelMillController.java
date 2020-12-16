package com.digitzones.procurement.controllers;

import com.digitzones.procurement.model.SteelMill;
import com.digitzones.procurement.service.ISteelMillService;
import com.digitzones.procurement.vo.SteelMillVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("steelMill")
public class SteelMillController {
    @Autowired
    private ISteelMillService steelMillService;

    /**
     * 查询所有钢厂
     * @return
     */
    @RequestMapping("queryAll.do")
    public List<SteelMillVO> queryAll(){
        List<SteelMill> list = steelMillService.queryAll();
        List<SteelMillVO> vos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(SteelMill steelMill : list){
                SteelMillVO vo = new SteelMillVO();
                vo.setcId(steelMill.getSteelMillPK().getcId());
                vo.setcValue(steelMill.getSteelMillPK().getcValue());
                vo.setcAlias(steelMill.getcAlias());

                vos.add(vo);
            }
        }
        return vos;
    }
}
