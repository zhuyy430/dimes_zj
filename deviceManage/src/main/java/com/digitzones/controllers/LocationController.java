package com.digitzones.controllers;

import com.digitzones.model.Location;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.service.ILocationService;
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
 * 货位控制器
 */

@RestController
@RequestMapping("location")
public class LocationController {
    @Autowired
    private ILocationService locationService;


    @RequestMapping("/queryLocationByCodeAndwarehouseCode.do")
    @ResponseBody
    public ModelMap queryLocationByCodeAndwarehouseCode(String code, String warehouseCode){
        ModelMap modelMap=new ModelMap();
        List<Location> list=locationService.queryLocationByCodeAndwarehouseCode(code,warehouseCode);
        if(list!=null&&list.size()>0){
            modelMap.addAttribute("success",true);
        }else{
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg","您输入了非法的货位信息");
        }
        return modelMap;
    }

    /**
     * 查询货位信息
     * @param cWhCode 仓库编码
     * @param cPosCode 货位编码
     * @param cPosName 货位名称
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryLocations.do")
    public ModelMap queryLocations(String cWhCode, String cPosCode,String cPosName, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "from Location v where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        if(!StringUtils.isEmpty(cWhCode)&&null!=cWhCode){
            hql += " and v.cWhCode like ?" + i++;
            data.add("%" + cWhCode.trim() + "%");
        }
        if(!StringUtils.isEmpty(cPosCode) && !cPosCode.trim().equals("")){
            hql += " and cPosCode like ?" + i++;
            data.add("%" + cPosCode.trim() + "%");
        }
        if(!StringUtils.isEmpty(cPosName) && !cPosName.trim().equals("")){
            hql += " and cPosName like ?" + i++;
            data.add("%" + cPosName.trim() + "%");
        }
        Pager<Location> pager = locationService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("total",pager.getTotalCount());
        modelMap.addAttribute("rows", pager.getData());
        return modelMap;
    }
}
