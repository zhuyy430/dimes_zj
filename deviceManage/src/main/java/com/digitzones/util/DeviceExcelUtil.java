package com.digitzones.util;

import com.digitzones.devmgr.model.DeviceLevel;
import com.digitzones.devmgr.service.IDeviceLevelService;
import com.digitzones.model.Device;
import com.digitzones.model.ProductionUnit;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProductionUnitService;
import com.digitzones.service.IProjectTypeService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 设备信息导入导出工具类
 * @author zdq
 * 2019年2月26日
 */
public class DeviceExcelUtil extends ExcelUtil {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static List<String> cols = new ArrayList<>();
	static {
		cols.add("生产单元代码");
		cols.add("设备代码");
		cols.add("设备名称");
		cols.add("规格型号");
		cols.add("设备类别代码");
		cols.add("设备等级代码");
		cols.add("出厂编号");
		cols.add("生产厂家");
		cols.add("经销商");
		cols.add("设备状态");
		cols.add("出厂日期(yyyy-MM-dd)");
		cols.add("到厂日期(yyyy-MM-dd)");
		cols.add("安装日期(yyyy-MM-dd)");
		cols.add("验收日期(yyyy-MM-dd)");
		cols.add("资产编号");
		cols.add("设备重量");
		cols.add("外形尺寸");
		cols.add("安装位置");
		cols.add("单台功率");
		cols.add("实际功率");
		cols.add("备注");
	}
	/**生产单元业务对象*/
	private IProductionUnitService productionUnitService;
	/**设备类别业务对象*/
	private IProjectTypeService projectTypeService;
	/**设备等级业务对象*/
	private IDeviceLevelService deviceLevelService;
	public DeviceExcelUtil(IProductionUnitService productionUnitService,IProjectTypeService projectTypeService,IDeviceLevelService deviceLevelService) {
		this.productionUnitService = productionUnitService;
		this.projectTypeService = projectTypeService;
		this.deviceLevelService = deviceLevelService;
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
		List<Device> list = new ArrayList<Device>();
		// 循环Excel行数
		for (int r = 1; r < getTotalRows(); r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			Device device = new Device();
			device.setIsDimesUse(false);
			device.setIsDeviceManageUse(true);
			for (int c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				if(c==0&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+c+"列,生产单元代码不存在!");
				}
				if(c==1&&cell==null){
					throw new RuntimeException("第"+r+"行,第"+c+"列,设备代码为空!");
				}
				if (null != cell) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					switch(c) {
					//生产单元
					case 0:{
						String productionUnitCode = cell.getStringCellValue();
						if(!StringUtils.isEmpty(productionUnitCode)) {
							ProductionUnit productionUnit = productionUnitService.queryByProperty("code", productionUnitCode);
							if(productionUnit==null) {
								throw new RuntimeException("第"+r+"行,第"+c+"列,生产单元代码不存在!");
							}
							device.setProductionUnit(productionUnit);
						}
						break;
					}
					//设备代码
					case 1:{
						String deviceCode = cell.getStringCellValue();
						if(StringUtils.isEmpty(deviceCode)) {
							throw new RuntimeException("第"+r+"行,第"+c+"列,设备代码为空!");
						}
						device.setCode(deviceCode);
						break;
					}
					//设备名称
					case 2:{
						String deviceName = cell.getStringCellValue();
						device.setName(deviceName);
						break;
					}
					//规格型号
					case 3:{
						String unitType = cell.getStringCellValue();
						device.setUnitType(unitType);
						break;
					}
					//设备类别
					case 4:{
						String deviceTypeCode = cell.getStringCellValue();
						if(!StringUtils.isEmpty(deviceTypeCode)) {
							ProjectType pt = projectTypeService.queryByProperty("code", deviceTypeCode);
							device.setProjectType(pt);
						}
						break;
					}
					//设备等级
					case 5:{
						String deviceLevelCode = cell.getStringCellValue();
						if(!StringUtils.isEmpty(deviceLevelCode)) {
							DeviceLevel dl = deviceLevelService.queryByProperty("code", deviceLevelCode);
							device.setDeviceLevel(dl);
						}
						break;
					}
					//出厂编号
					case 6:{
						String outFactoryCode = cell.getStringCellValue();
						device.setOutFactoryCode(outFactoryCode);
						break;
					}
					//生产厂家
					case 7:{
						String manufacturer = cell.getStringCellValue();
						device.setManufacturer(manufacturer);
						break;
					}
					//经销商
					case 8:{
						String trader = cell.getStringCellValue();
						device.setTrader(trader);
						break;
					}
					//设备状态
					case 9:{
						String status = cell.getStringCellValue();
						device.setStatus(status);
						break;
					}
					//出厂日期
					case 10:{
						String outFactoryDate = cell.getStringCellValue();
						if(!StringUtils.isEmpty(outFactoryDate)) {
							try {
								device.setOutFactoryDate(format.parse(outFactoryDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					//到厂日期
					case 11:{
						String inFactoryDate = cell.getStringCellValue();
						if(!StringUtils.isEmpty(inFactoryDate)) {
							try {
								device.setInFactoryDate(format.parse(inFactoryDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					//安装日期
					case 12:{
						String installDate = cell.getStringCellValue();
						if(!StringUtils.isEmpty(installDate)) {
							try {
								device.setInstallDate(format.parse(installDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					//验收日期
					case 13:{
						String checkDate = cell.getStringCellValue();
						if(!StringUtils.isEmpty(checkDate)) {
							try {
								device.setCheckDate(format.parse(checkDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						break;
					}
					//资产编号
					case 14:{
						String assetNumber = cell.getStringCellValue();
						device.setAssetNumber(assetNumber);
						break;
					}
					//设备重量
					case 15:{
						String weight = cell.getStringCellValue();
						device.setWeight(weight);
						break;
					}
					//外形尺寸
					case 16:{
						String shapeSize = cell.getStringCellValue();
						device.setShapeSize(shapeSize);
						break;
					}
					//安装位置
					case 17:{
						String installPosition = cell.getStringCellValue();
						device.setInstallPosition(installPosition);
						break;
					}
					//单台功率
					case 18:{
						String power = cell.getStringCellValue();
						device.setPower(power);
						break;
					}
					//实际功率
					case 19:{
						String actualPower = cell.getStringCellValue();
						device.setActualPower(actualPower);
						break;
					}
					//说明
					case 20:{
						String note = cell.getStringCellValue();
						device.setNote(note);
						break;
					}
					}
				}
			}
			// 添加到list
			list.add(device);
		}
		return list;
	}
}
