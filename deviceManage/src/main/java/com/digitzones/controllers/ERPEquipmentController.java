package com.digitzones.controllers;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.model.ERPEquipment;
import com.digitzones.model.Equipment;
import com.digitzones.model.EquipmentType;
import com.digitzones.model.Pager;
import com.digitzones.service.IERPEquipmentService;
import com.digitzones.service.IEquipmentPrintService;
import com.digitzones.service.IEquipmentService;
import com.digitzones.service.IEquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 14:28
 */
@Controller
@RequestMapping("/ERPEquipment")
public class ERPEquipmentController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IERPEquipmentService erpEquipmentService;
    @Autowired
    private IEquipmentService equipmentService;
    @Autowired
    private IEquipmentTypeService equipmentTypeService;
    @Autowired
    private IEquipmentPrintService equipmentPrintService;

    /**
     * 分页查询参数信息
     * @param rows
     * @param page
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/queryERPEquipments.do")
    @ResponseBody
    public ModelMap queryERPEquipments(@RequestParam Map<String,String> params, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page, HttpServletRequest request)throws ParseException {
        ModelMap modelMap = new ModelMap();
        int i = 0;
        List<Object> paramList = new ArrayList<>();
        String search_from = params.get("search_from");
        String search_to = params.get("search_to");
        String search_code = params.get("search_code");
        String search_status = params.get("search_status");
        String search_batchNo = params.get("search_batchNo");
        String hql = "select ee from ERPEquipment ee left join EquipmentPrint ep on ee.code=ep.code and ee.batchNo=ep.batchNo where 1=1";
        if(!StringUtils.isEmpty(search_from)) {
            hql += " and ee.inWarehouseDate>=?" + (i++);
            paramList.add(format.parse(search_from));
        }
        if(!StringUtils.isEmpty(search_to)) {
            hql += " and ee.inWarehouseDate<=?" + (i++);
            paramList.add(format.parse(search_to));
        }
        if(!StringUtils.isEmpty(search_code)) {
            hql += " and (ee.code like ?" + (i) + " or ee.name like ?" + (i++) + ") ";
            paramList.add("%" + search_code + "%");
        }
        if(!StringUtils.isEmpty(search_batchNo)) {
            hql += " and ee.batchNo=?" + (i++);
            paramList.add(search_batchNo);
        }
        if("已打印".equals(search_status)) {
            hql += " and ep.status=?" + i;
            paramList.add(true);
        }else{
            hql += " and (ep.status=?" + i +" or  ep.status is Null)";
            paramList.add(false);
        }
        hql +=" order by ee.code";
        Pager<CheckingPlanRecord> pager = erpEquipmentService.queryObjs(hql, page, rows, paramList.toArray());
        modelMap.addAttribute("total", pager.getTotalCount());
        modelMap.addAttribute("rows", pager.getData());
        return modelMap;
    }


    /**
     * 装备条码打印
     */
    @RequestMapping("/addPrintEquipment4ERPEquipment.do")
    @ResponseBody
    public ModelMap addPrintEquipment4ERPEquipment(String codes,String batchNos){
        ModelMap modelMap=new ModelMap();
        if(codes!=null && codes.contains("'")) {
            codes = codes.replace("'", "");
        }
        String[] codeList= codes.split(",");
        if(batchNos!=null && batchNos.contains("'")) {
            batchNos = batchNos.replace("'", "");
        }
        String[] batchNoList= batchNos.split(",");

        List<String> ids=new ArrayList<>();

        for(int i=0;i<codeList.length;i++){
            Equipment equipment=equipmentService.queryByProperty("code",codeList[i]+"_"+batchNoList[i]);
            ERPEquipment erpEquipment=erpEquipmentService.queryERPEquipmentByCodeAndBatchNo(codeList[i],batchNoList[i]);
            if(equipment!=null){
                ids.add(equipment.getId().toString());
                continue;
            }else{
                EquipmentType equipmentType=equipmentTypeService.queryByProperty("code",codeList[i]);
                if(equipmentType!=null){
                    Equipment equipmentNew=new Equipment();
                    equipmentNew.setCode(codeList[i]+"_"+batchNoList[i]);
                    equipmentNew.setBaseCode(Constant.EquipmentType.EQUIPMENT);
                    equipmentNew.setEquipmentType(equipmentType);
                    equipmentNew.setStatus("在库");
                    equipmentNew.setOutFactoryDate(erpEquipment.getInWarehouseDate());
                    equipmentService.addObj(equipmentNew);
                    ids.add(equipmentNew.getId().toString());
                }else{
                    EquipmentType  top=equipmentTypeService.queryByProperty("code",Constant.EquipmentType.EQUIPMENT);
                    EquipmentType  equipmentTypeNew =new EquipmentType();
                    equipmentTypeNew.setCode(codeList[i]);
                    equipmentTypeNew.setName(erpEquipment.getName());
                    equipmentTypeNew.setUnitType(erpEquipment.getUnitType());
                    equipmentTypeNew.setBaseCode(Constant.EquipmentType.EQUIPMENT);
                    equipmentTypeNew.setMeasurementObjective(Float.valueOf(erpEquipment.getUseLife()));
                    equipmentTypeNew.setMeasurementType("计数型");
                    equipmentTypeNew.setParent(top);
                    equipmentTypeService.addObj(equipmentTypeNew);
                    Equipment equipmentNew=new Equipment();
                    equipmentNew.setCode(codeList[i]+"_"+batchNoList[i]);
                    equipmentNew.setBaseCode(Constant.EquipmentType.EQUIPMENT);
                    equipmentNew.setEquipmentType(equipmentTypeNew);
                    equipmentNew.setStatus("在库");
                    equipmentNew.setOutFactoryDate(erpEquipment.getInWarehouseDate());
                    equipmentService.addObj(equipmentNew);
                    ids.add(equipmentNew.getId().toString());
                }
            }
        }

        modelMap.addAttribute("success",true);
        modelMap.addAttribute("ids",ids);
        return modelMap;
    }
}
