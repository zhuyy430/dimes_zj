package com.digitzones.util;

import com.digitzones.service.IEquipmentService;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxf
 * @date 2020-6-28
 * @time 10:09
 */
public class EquipmentExcelUtil extends ExcelUtil{

    private IEquipmentService equipmentService;
    private static List<String> cols = new ArrayList<>();
    static {
        cols.add("装备代码");
        cols.add("装备名称");
        cols.add("规格型号");
        cols.add("计量类型");
        cols.add("生产厂家");
        cols.add("经销商");
        cols.add("生产日期");
        cols.add("计量目标");
        cols.add("备注");
        cols.add("装备序号");
        cols.add("计量累计");
        cols.add("计量差异");
        cols.add("质保期");
        cols.add("状态");
    }






    @Override
    public List<?> readExcelValue(Workbook wb) {
        return null;
    }
}
