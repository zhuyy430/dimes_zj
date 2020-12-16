package com.digitzones.controllers;

import com.digitzones.model.Pager;
import com.digitzones.model.SalesSlip;
import com.digitzones.service.ISalesSlipService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单视图控制器(只能做查询)
 */
@RestController
@RequestMapping("salesSlip")
public class SalesSlipController {
    @Autowired
    private ISalesSlipService salesSlipService;

    @RequestMapping("/querySalesSlips.do")
    @ResponseBody
    public ModelMap querySalesSlips(String formNo,  String customerName, String inventoryName,
                                                              @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select distinct d from SalesSlip d where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        if(!StringUtils.isEmpty(formNo)){
            hql += " and formNo like ?" + i++;
            data.add("%"+formNo+"%");
        }
        if(!StringUtils.isEmpty(customerName) && !customerName.trim().equals("")){
            hql += " and customerName like ?" + i++;
            data.add("%" + customerName.trim() + "%");
        }
        if(!StringUtils.isEmpty(inventoryName) && !inventoryName.trim().equals("")){
            hql += " and inventoryName like ?" + i++;
            data.add("%" + inventoryName.trim() + "%");
        }

        hql += " order by formNo desc";
        Pager<SalesSlip> pager = salesSlipService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("total",pager.getTotalCount());
        modelMap.addAttribute("rows", pager.getData());
        return modelMap;
    }
}
