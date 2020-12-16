package com.digitzones.util;
import com.digitzones.model.ParameterType;
import com.digitzones.model.Parameters;
import com.digitzones.model.Processes;
import com.digitzones.model.WorkpieceProcessParameterMapping;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.service.IParameterService;
import com.digitzones.service.IParameterTypeService;
import com.digitzones.service.IProcessesService;
import com.digitzones.service.IWorkpieceProcessParameterMappingService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 工件工序参数信息导入导出工具类
 * @author zdq
 * 2019年2月26日
 */
public class WorkpieceProcessParameterExcelUtil extends ExcelUtil {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static List<String> cols = new ArrayList<>();
	static {
		cols.add("工件代码");
		cols.add("工序代码");
		cols.add("工序名称");
		cols.add("参数代码");
		cols.add("参数名称");
		cols.add("单位");
		cols.add("上限UL");
		cols.add("下限LL");
		cols.add("标准值");
		cols.add("参数类别");
	}
	private IParameterService parameterService;
	private IParameterTypeService parameterTypeService;
	private IInventoryService inventoryService;
	private IProcessesService processesService;
	private IInventoryProcessMappingService inventoryProcessMappingService;
	private IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService;
	public WorkpieceProcessParameterExcelUtil(IParameterTypeService parameterTypeService,IProcessesService processesService,IInventoryService inventoryService, IParameterService parameterService, IInventoryProcessMappingService inventoryProcessMappingService, IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService) {
		this.parameterService = parameterService;
		this.inventoryService = inventoryService;
		this.processesService = processesService;
		this.parameterTypeService = parameterTypeService;
		this.inventoryProcessMappingService = inventoryProcessMappingService;
		this.workpieceProcessParameterMappingService = workpieceProcessParameterMappingService;
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
		int cellNum = firstRow.getPhysicalNumberOfCells();
		if(cellNum!=cols.size()) {
			throw new RuntimeException("请使用正确模版!");
		}else {
			for(int i = 0;i<cellNum;i++) {
				if(!cols.get(i).equals(firstRow.getCell(i).getStringCellValue())) {
					throw new RuntimeException("请使用正确模版!");
				}
			}
		}
		// 循环Excel行数
		for (int r = 1; r < getTotalRows(); r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			WorkpieceProcessParameterMapping workpieceProcessParameterMapping = null;
			Parameters parameters = null;
			InventoryProcessMapping inventoryProcessMapping = null;
			boolean isAdd=false;
			//工件代码
			String workpieceCode = null;
			for (int c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				if(c==0&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+c+"列,工件代码不能为空!");
				}
				if(c==1&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+c+"列,工序代码不能为空!");
				}
				if(c==3&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+c+"列,参数代码不能为空!");
				}
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					switch(c) {
						//工件代码
						case 0:{
							workpieceCode = cell.getStringCellValue();
							break;
						}
						//工序代码
						case 1:{
							String processCode = cell.getStringCellValue();
							inventoryProcessMapping = inventoryProcessMappingService.queryByInventoryCodeAndProcessCode(workpieceCode,processCode);
							if(inventoryProcessMapping==null){
								inventoryProcessMapping = new InventoryProcessMapping();
								Inventory inventory = inventoryService.queryByProperty("code",workpieceCode);
								Processes processes = processesService.queryByProperty("code",processCode);
								if(processes == null){
									throw new RuntimeException("工序 " + processCode +" 不存在!");
								}
								inventoryProcessMapping.setInventory(inventory);
								inventoryProcessMapping.setProcess(processes);
								Serializable id = inventoryProcessMappingService.addObj(inventoryProcessMapping);
								inventoryProcessMapping.setId((Long) id);
							}
							break;
						}
						//工序名称
						case 2 : {
							String processName = cell.getStringCellValue();
							if(inventoryProcessMapping.getProcess().getName()==null) {
								inventoryProcessMapping.getProcess().setName(processName);
							}
							break;
						}
						//参数代码
						case 3:{
							String parameterCode = cell.getStringCellValue();
							parameters = parameterService.queryByProperty("code",parameterCode);
							if(parameters==null){
								parameters = new Parameters();
								parameters.setCode(parameterCode);
								parameters.setBaseCode("ART");
								Serializable id = parameterService.addObj(parameters);
								parameters.setId((Long) id);
							}

							workpieceProcessParameterMapping = workpieceProcessParameterMappingService.queryByWorkpieceCodeAndProcessCodeAndParameterCode(workpieceCode,inventoryProcessMapping.getProcess().getCode(),parameterCode);
							if(workpieceProcessParameterMapping==null){
								workpieceProcessParameterMapping = new WorkpieceProcessParameterMapping();
								//新增，否则为更新
								isAdd = true;
							}
							workpieceProcessParameterMapping.setWorkpieceProcess(inventoryProcessMapping);
							break;
						}
						//参数名称
						case 4:{
							String parameterName = cell.getStringCellValue();
							if(parameters.getName()==null) {
								parameters.setName(parameterName);
							}
							break;
						}
						//单位
						case 5:{
							String unit = cell.getStringCellValue();
							workpieceProcessParameterMapping.setUnit(unit);
							break;
						}
						//上限UL
						case 6:{
							String upLine = cell.getStringCellValue();
							try {
								workpieceProcessParameterMapping.setUpLine(Float.parseFloat(upLine));
							}catch (Exception e){
								workpieceProcessParameterMapping.setUpLine(null);
							}
							break;
						}
						//下线
						case 7:{
							String lowLine = cell.getStringCellValue();
							try {
								workpieceProcessParameterMapping.setLowLine(Float.parseFloat(lowLine));
							}catch (Exception e){
								workpieceProcessParameterMapping.setLowLine(null);
							}
							break;
						}
						//标准值
						case 8:{
							String standardValue = cell.getStringCellValue();
							try {
								workpieceProcessParameterMapping.setStandardValue(Float.parseFloat(standardValue));
							}catch (Exception e){
								workpieceProcessParameterMapping.setStandardValue(null);
							}
							break;
						}
						//参数类别
						case 9:{
							String parameterTypeCode = cell.getStringCellValue();
							ParameterType parameterType = parameterTypeService.queryByProperty("code", parameterTypeCode);
							if(parameterType==null){
								throw new RuntimeException("参数类别 " + parameterTypeCode + " 不存在!");
							}

							parameters.setParameterType(parameterType);
							workpieceProcessParameterMapping.setParameter(parameters);
							break;
						}
					}
				}
			}
			if(isAdd){
				workpieceProcessParameterMappingService.addObj(workpieceProcessParameterMapping);
			}else{
				workpieceProcessParameterMappingService.updateObj(workpieceProcessParameterMapping);
			}
		}
		return null;
	}
}
