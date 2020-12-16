package com.digitzones.test;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.digitzones.dao.IRoleModuleDao;
import com.digitzones.model.Module;
import com.digitzones.model.Power;
import com.digitzones.model.Role;
import com.digitzones.model.RoleModule;
import com.digitzones.model.RolePower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
import com.digitzones.service.IModuleService;
import com.digitzones.service.IPowerService;
import com.digitzones.service.IRolePowerService;
import com.digitzones.service.IRoleService;
import com.digitzones.service.IUserRoleService;
import com.digitzones.service.IUserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:springContext-*.xml" })
public class ImportExcelTest {
	@Autowired
	private IPowerService powerService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IRolePowerService rpService;
	@Autowired
	private IModuleService moduleService;
	@Autowired
	private IRoleModuleDao roleModuleDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserRoleService userRoleService;
	@Test
	public void ImportExcel() {
		//导入的Excel
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("power.xlsx");
		MultipartFile multipartFile = null;
		try {
			multipartFile= new MockMultipartFile("file", "power.xlsx", "text/plain", IOUtils.toByteArray(input));
		} catch (IOException e) {
			e.printStackTrace();
		}        
		// 创建处理EXCEL的类
		ReadExcel readExcel = new ReadExcel();
		// 解析excel，获取上传的事件单
		List<Map<String, Object>> powersList = readExcel.getExcelInfo(multipartFile);
		Role role = roleService.queryByProperty("roleName", "admin");
		if(role==null){
			role = new Role();
			role.setRoleName("admin");
			role.setNote("超级管理员");
			role.setAllowDelete(false);
			role.setCreateDate(new Date());
			role.setDisable(false);

			roleService.addObj(role);
		}
		// 至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
		for (Map<String, Object> p : powersList) {
			if(StringUtils.isEmpty(p.get("powerCode").toString()))
				break;
			String note = p.get("note").toString();
			String _group = p.get("_group").toString();
			String powerName = p.get("powerName").toString();
			String powerCode = p.get("powerCode").toString();
			Power power = powerService.queryByProperty("powerCode", powerCode);
			if(power!=null) {
				continue;
			}

			power = new Power();
			power.setNote(note);
			power.setGroup(_group);
			power.setPowerCode(powerCode);
			power.setPowerName(powerName);
			power.setCreateDate(new Date());
			powerService.addObj(power);
			if(power.getId()!=null){
				RolePower rp = new RolePower();
				rp.setPower(power);
				rp.setRole(role);
				rpService.addObj(rp);
			}
		}
		User user = userService.queryByProperty("username", "admin");
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userRoleService.addObj(userRole);
	}
	public class ReadExcel {
		// 总行数
		private int totalRows = 0;
		// 总条数
		private int totalCells = 0;
		// 错误信息接收器
		private String errorMsg;
		// 构造方法
		public ReadExcel() {
		}
		// 获取总行数
		public int getTotalRows() {
			return totalRows;
		}
		// 获取总列数
		public int getTotalCells() {
			return totalCells;
		}
		// 获取错误信息
		public String getErrorInfo() {
			return errorMsg;
		}
		/**
		 * 读EXCEL文件，获取信息集合
		 * @return
		 */
		public List<Map<String, Object>> getExcelInfo(MultipartFile mFile) {
			String fileName = mFile.getOriginalFilename();// 获取文件名
			//        List<Map<String, Object>> userList = new LinkedList<Map<String, Object>>();
			try {
				if (!validateExcel(fileName)) {// 验证文件名是否合格
					return null;
				}
				boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
				if (isExcel2007(fileName)) {
					isExcel2003 = false;
				}
				return createExcel(mFile.getInputStream(), isExcel2003);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 根据excel里面的内容读取客户信息
		 * @param is      输入流
		 * @param isExcel2003   excel是2003还是2007版本
		 * @return
		 * @throws IOException
		 */
		public List<Map<String, Object>> createExcel(InputStream is, boolean isExcel2003) {
			try {
				Workbook wb = null;
				if (isExcel2003) {// 当excel是2003时,创建excel2003
					wb = new HSSFWorkbook(is);
				} else {// 当excel是2007时,创建excel2007
					wb = new XSSFWorkbook(is);
				}
				return readExcelValue(wb);// 读取Excel里面客户的信息
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 读取Excel里面客户的信息
		 * @param wb
		 * @return
		 */
		private List<Map<String, Object>> readExcelValue(Workbook wb) {
			// 得到第一个shell
			Sheet sheet = wb.getSheetAt(0);
			// 得到Excel的行数
			this.totalRows = sheet.getPhysicalNumberOfRows();
			// 得到Excel的列数(前提是有行数)
			if (totalRows > 1 && sheet.getRow(0) != null) {
				this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
			}
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			// 循环Excel行数
			for (int r = 1; r < totalRows; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				// 循环Excel的列
				Map<String, Object> map = new HashMap<String, Object>();
				for (int c = 0; c < this.totalCells; c=c+2) {
					Cell cell = row.getCell(c);
					if (null != cell) {
						if (c == 0) {
							//{sex=, name=ADD_WORKSHEET, age=新增生产工单}
							// 如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25
							if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								String name = String.valueOf(cell.getNumericCellValue());
								map.put("powerCode", name.substring(0, name.length() - 2 > 0 ? name.length() - 2 : 1));// 名称
							} else {
								map.put("powerCode", cell.getStringCellValue());// 名称
							}
						} else if (c == 2) {
							if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								String sex = String.valueOf(cell.getNumericCellValue());
								map.put("powerName",sex.substring(0, sex.length() - 2 > 0 ? sex.length() - 2 : 1));// 性别
							} else {
								map.put("powerName",cell.getStringCellValue());// 性别
							}
						} else if (c == 4) {
							if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								String age = String.valueOf(cell.getNumericCellValue());
								map.put("_group", age.substring(0, age.length() - 2 > 0 ? age.length() - 2 : 1));// 年龄
							} else {
								map.put("_group", cell.getStringCellValue());// 年龄
							}
						} else if (c == 6) {
							if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								String age = String.valueOf(cell.getNumericCellValue());
								map.put("note", age.substring(0, age.length() - 2 > 0 ? age.length() - 2 : 1));// 年龄
							} else {
								map.put("note", cell.getStringCellValue());// 年龄
							}
						}
					}
				}
				// 添加到list
				userList.add(map);
			}
			return userList;
		}
		/**
		 * 验证EXCEL文件
		 * 
		 * @param filePath
		 * @return
		 */
		public boolean validateExcel(String filePath) {
			if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
				errorMsg = "文件名不是excel格式";
				return false;
			}
			return true;
		}
		// @描述：是否是2003的excel，返回true是2003
		public boolean isExcel2003(String filePath) {
			return filePath.matches("^.+\\.(?i)(xls)$");
		}
		// @描述：是否是2007的excel，返回true是2007
		public boolean isExcel2007(String filePath) {
			return filePath.matches("^.+\\.(?i)(xlsx)$");
		}
	}
}
