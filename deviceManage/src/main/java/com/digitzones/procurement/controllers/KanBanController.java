package com.digitzones.procurement.controllers;

import com.digitzones.procurement.service.IBoxBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author xuxf
 * @date 2020-12-14
 * @time 17:12
 */
@RestController
@RequestMapping("kanBan")
public class KanBanController {

    @Autowired
    private IBoxBarService boxBarService;

    /**
     *  钢材库看板
     * @return
     */
    @RequestMapping("queryKanbanByPositon.do")
    public ModelMap queryKanbanByPositon(HttpSession session){
        ModelMap modelMap = new ModelMap();
        List<Object[]> A1List=boxBarService.queryInventoryByPositon("A","1","before");
        List<Object[]> A2List=boxBarService.queryInventoryByPositon("A","2","before");
        List<Object[]> A3List=boxBarService.queryInventoryByPositon("A","3","before");
        List<Object[]> A4List=boxBarService.queryInventoryByPositon("A","4","before");
        List<Object[]> A5List=boxBarService.queryInventoryByPositon("A","5","before");

        List<Object[]> XList=boxBarService.queryInventoryByPositon("X","1","before");

        List<Object[]> B1List=boxBarService.queryInventoryByPositon("B","1","before");
        List<Object[]> B2List=boxBarService.queryInventoryByPositon("B","2","before");
        List<Object[]> B3List=boxBarService.queryInventoryByPositon("B","3","before");
        List<Object[]> B4List=boxBarService.queryInventoryByPositon("B","4","before");
        List<Object[]> B5List=boxBarService.queryInventoryByPositon("B","5","before");

        modelMap.addAttribute("A1List",A1List);
        modelMap.addAttribute("A2List",A2List);
        modelMap.addAttribute("A3List",A3List);
        modelMap.addAttribute("A4List",A4List);
        modelMap.addAttribute("A5List",A5List);

        modelMap.addAttribute("XList",XList);

        modelMap.addAttribute("B1List",B1List);
        modelMap.addAttribute("B2List",B2List);
        modelMap.addAttribute("B3List",B3List);
        modelMap.addAttribute("B4List",B4List);
        modelMap.addAttribute("B5List",B5List);

        return modelMap;
    }
}
