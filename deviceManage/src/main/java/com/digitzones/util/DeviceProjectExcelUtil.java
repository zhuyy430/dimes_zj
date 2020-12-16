package com.digitzones.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;
/**
 * 设备信息导入导出工具类
 * @author zdq
 * 2019年2月26日
 */
public class DeviceProjectExcelUtil extends ExcelUtil {
	private static List<String> cols = new ArrayList<>();
	static {
		cols.add("项目编码");
		cols.add("项目名称");
		cols.add("标准");
		cols.add("方法");
		cols.add("频次");
		cols.add("说明");
		cols.add("备注");
		cols.add("设备类别");
	}
	/**设备类别业务对象*/
	private IProjectTypeService projectTypeService;
	private String type;
	public DeviceProjectExcelUtil(IProjectTypeService projectTypeService,String type) {
		this.projectTypeService = projectTypeService;
		this.type=type;
	}
	@Override
	public List<?> readExcelValue(Workbook wb) {
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);
		// 得到Excel的行数
		this.setTotalRows(sheet.getPhysicalNumberOfRows());
		// 得到Excel的列数(前提是有行数)
		if (getTotalRows() > 1 && sheet.getRow(0) != null) {
			this.setTotalCells(sheet.getRow(0).getPhysicalNumberOfCells());
		}else {
			throw new RuntimeException("无导入数据!");
		}
			Row firstRow = sheet.getRow(0);
			int i = 0;
			for(;i<cols.size()-1;i++) {
				if(!cols.get(i).equals(firstRow.getCell(i).getStringCellValue())) {
					throw new RuntimeException("请使用正确模版!");
				}
			}
			if(!cols.get(i).equals("项目类别") && !cols.get(i).equals("设备类别")) {
				throw new RuntimeException("请使用正确模版!");
			}
		
		List<DeviceProject> list = new ArrayList<DeviceProject>();
		// 循环Excel行数
		for (int r = 1; r < getTotalRows(); r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			DeviceProject deviceproject = new DeviceProject();
			for (int c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				if(c==0&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+(c+1)+"列,项目编码为空!");
				}
				if(c==7&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+(c+1)+"列,设备类别为空!");
				}
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					switch(c) {
					case 0:{
						String code = cell.getStringCellValue();
						if(StringUtils.isEmpty(code)) {
								throw new RuntimeException("第"+r+"行,第"+(c+1)+"列,项目编码为空!");
						}
						deviceproject.setCode(code);
						break;
					}
					case 1:{
						String name = cell.getStringCellValue();
						deviceproject.setName(name);
						break;
					}
					case 2:{
						String standard = cell.getStringCellValue();
						deviceproject.setStandard(standard);
						break;
					}
					case 3:{
						String method = cell.getStringCellValue();
						deviceproject.setMethod(method);
						break;
					}
					case 4:{
						String frequency = cell.getStringCellValue();
							deviceproject.setFrequency(frequency);
						break;
					}
					case 5:{
						String note = cell.getStringCellValue();
						deviceproject.setNote(note);
						break;
					}
					case 6:{
						String remark = cell.getStringCellValue();
						deviceproject.setRemark(remark);
						break;
					}
					case 7:{
						String name = cell.getStringCellValue();
						String typeCode = "";
						if(type.equals("SPOTINSPECTION")||type.equals("MAINTAIN")){
							typeCode = "deviceType";
						}else{
							typeCode = "maintenanceItemType";
						}
						ProjectType deviceType = projectTypeService.queryProjectTypeByCodeAndType(name, typeCode);
						if(deviceType==null) {
							throw new RuntimeException("第"+r+"行,第"+(c+1)+"列,设备类别不存在!");
						}
						deviceproject.setProjectType(deviceType);
						break;
					}
					}
				}
			}
			// 添加到list
			deviceproject.setCreateTime(new Date());
			deviceproject.setType(type);
			list.add(deviceproject);
		}
		return list;
	}
}
