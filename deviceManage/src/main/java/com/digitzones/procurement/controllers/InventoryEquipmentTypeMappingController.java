package com.digitzones.procurement.controllers;

import com.digitzones.model.Equipment;
import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.EquipmentType;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.InventoryEquipmentTypeMapping;
import com.digitzones.procurement.service.IInventoryEquipmentTypeMappingService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.IEquipmentDeviceSiteMappingService;
import com.digitzones.service.IEquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventoryEquipmentTypeMapping")
public class InventoryEquipmentTypeMappingController {
    @Autowired
    private IInventoryEquipmentTypeMappingService inventoryEquipmentTypeMappingService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IEquipmentTypeService equipmentTypeService;
    @Autowired
    private IEquipmentDeviceSiteMappingService equipmentDeviceSiteMappingService;
    @Autowired
    private IDeviceSiteService deviceSiteService;


    @RequestMapping("/addInventoryEquipmentTypeMapping.do")
    public ModelMap addInventoryEquipmentTypeMapping(String inventoryCode,Long equipmentTypeId){
        ModelMap modelMap=new ModelMap();
        List<InventoryEquipmentTypeMapping> list=inventoryEquipmentTypeMappingService.queryByInventoryCodeAndEquipmentTypeId(inventoryCode,equipmentTypeId);
        if(list.size()>0){
            modelMap.addAttribute("msg", "改装备和物料已经关联！");
            modelMap.addAttribute("success",false);
            return modelMap;
        }else{
            Inventory inventory=inventoryService.queryObjById(inventoryCode);
            EquipmentType equipmentType=equipmentTypeService.queryObjById(equipmentTypeId);
            InventoryEquipmentTypeMapping ie=new InventoryEquipmentTypeMapping();
            ie.setEquipmentType(equipmentType);
            ie.setInventory(inventory);
            inventoryEquipmentTypeMappingService.addObj(ie);
        }

        modelMap.addAttribute("msg", "操作完成！");
        modelMap.addAttribute("success",true);
        return modelMap;
    }

    @RequestMapping("/deleteInventoryEquipmentTypeMapping.do")
    public ModelMap deleteInventoryEquipmentTypeMapping(String id){
        if(id!=null && id.contains("'")) {
            id = id.replace("'", "");
        }
        ModelMap modelMap = new ModelMap();
        inventoryEquipmentTypeMappingService.deleteObj(Long.valueOf(id));
        modelMap.addAttribute("success", true);
        modelMap.addAttribute("statusCode", 200);
        modelMap.addAttribute("title", "操作提示");
        modelMap.addAttribute("message", "成功删除!");
        return modelMap;
    }

    @RequestMapping("/updateInventoryEquipmentTypeMapping.do")
    public ModelMap updateInventoryEquipmentTypeMapping(Long id,String useFrequency){
        ModelMap modelMap=new ModelMap();
        InventoryEquipmentTypeMapping iem=inventoryEquipmentTypeMappingService.queryObjById(id);
        iem.setUseFrequency(Double.valueOf(useFrequency));
        inventoryEquipmentTypeMappingService.updateObj(iem);
        modelMap.addAttribute("msg", "操作完成！");
        modelMap.addAttribute("success",true);
        return modelMap;
    }

    @RequestMapping("/queryPageByInventoryEquipmentTypeMappingByInventoryCode.do")
    public ModelMap queryPageByInventoryEquipmentTypeMappingByInventoryCode(String InventoryCode,
                          @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page,
                          HttpServletRequest request){
        ModelMap modelMap=new ModelMap();
        Pager<InventoryEquipmentTypeMapping> pager = inventoryEquipmentTypeMappingService.queryObjs("from InventoryEquipmentTypeMapping ie where ie.inventory.code=?0",page, rows, new Object[] {InventoryCode});

        modelMap.addAttribute("total", pager.getTotalCount());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("code", "0");
        modelMap.addAttribute("msg", "");
        return modelMap;
    }

    /**
     * 通过工件编码查询工件装备关联
     * @param InventoryCode
     * @return
     */
    @RequestMapping("/queryListByInventoryCodeAndDeviceSiteCode.do")
    public ModelMap queryListByInventoryCode(String InventoryCode,String deviceSiteCode,String workSheetNo){
        ModelMap modelMap=new ModelMap();
        List<EquipmentDeviceSiteMapping> edmList=new ArrayList<>();


        List<InventoryEquipmentTypeMapping> list= inventoryEquipmentTypeMappingService.queryListByInventoryCode(InventoryCode);

        if(list!=null&&list.size()>0){
            for(InventoryEquipmentTypeMapping iem:list){
                EquipmentType equipmentType=iem.getEquipmentType();
                List<EquipmentDeviceSiteMapping> edms=equipmentDeviceSiteMappingService.queryListByDeviceSiteCodeAndEquipmentTypeId(deviceSiteCode,equipmentType.getId(),workSheetNo);
                if(edms!=null&&edms.size()>0){
                    edmList.add(edms.get(0));
                }else{
                    EquipmentDeviceSiteMapping edm=new EquipmentDeviceSiteMapping();
                    Equipment equipment=new Equipment();
                    equipment.setEquipmentType(equipmentType);
                    edm.setEquipment(equipment);
                    edmList.add(edm);
                }
            }
            modelMap.addAttribute("success", true);
            modelMap.addAttribute("data", edmList);
        }else{
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg","该工单的工件未关联装备类别，请关联后再进行站点与装备的关联！");
        }

        return modelMap;
    }

    /**
     * 通过关联id查询工件装备关联
     * @param id
     * @return
     */
    @RequestMapping("/queryEquipmentMappingById.do")
    public InventoryEquipmentTypeMapping queryEquipmentMappingById(Long id){
        return inventoryEquipmentTypeMappingService.queryObjById(id);
    }

    /**
     * 通过工件id和装备代码查询关联
     */
    @RequestMapping("/queryMappingByInventoryCodeAndEquipmentTypeId.do")
    public InventoryEquipmentTypeMapping queryMappingByInventoryCodeAndEquipmentTypeId(String inventoryCode,Long equipmentTypeId){
        List<InventoryEquipmentTypeMapping> list=inventoryEquipmentTypeMappingService.queryByInventoryCodeAndEquipmentTypeId(inventoryCode,equipmentTypeId);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

}
