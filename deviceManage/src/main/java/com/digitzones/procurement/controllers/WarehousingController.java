package com.digitzones.procurement.controllers;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IWarehouseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
/**
 * 仓库管理控制器
 */
@RestController
@RequestMapping("warehouseController")
public class WarehousingController {
    @Autowired
    @Qualifier("warehouseService")
    private IWarehouseService warehouseService;
    /**
     * 查询仓库信息
     * @param cWhCode
     * @param cWhName
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryWarehouses.do")
    public ModelMap queryWarehouses(String cWhCode, String cWhName,@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "from Warehouse v where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        if(!StringUtils.isEmpty(cWhCode)){
            hql += " and v.cWhCode like ?" + i++;
            data.add("%" + cWhCode.trim() + "%");
        }
        if(!StringUtils.isEmpty(cWhName) && !cWhName.trim().equals("")){
            hql += " and cWhName like ?" + i++;
            data.add("%" + cWhName.trim() + "%");
        }
        Pager<Warehouse> pager = warehouseService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("total",pager.getTotalCount());
        modelMap.addAttribute("rows", pager.getData());
        return modelMap;
    }

    /**
     * 根据仓库编码查找仓库信息
     * @param warehouseCode 仓库编码
     * @return
     */
    @RequestMapping("/queryByWarehouseCode.do")
    public Warehouse queryByWarehouseCode(String warehouseCode){
        return warehouseService.queryByProperty("cWhCode",warehouseCode);
    }


    /**
     * 查询所有仓库信息
     */
    @RequestMapping("/queryAllWarehouse.do")
    public List<Warehouse> queryAllWarehouse(){
        return warehouseService.queryAllWarehouse();
    }
}
