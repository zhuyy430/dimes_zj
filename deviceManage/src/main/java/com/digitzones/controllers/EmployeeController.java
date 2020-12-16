package com.digitzones.controllers;
import com.digitzones.config.QRConfig;
import com.digitzones.constants.Constant;
import com.digitzones.model.*;
import com.digitzones.procurement.model.PersonProductionUnitMapping;
import com.digitzones.procurement.service.IPersonProductionUnitMappingService;
import com.digitzones.service.*;
import com.digitzones.util.PasswordEncoder;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.EmployeeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private QRConfig config ;
	@Autowired
	public void setConfig(QRConfig config) {
		this.config = config;
	}
	private IEmployeeService employeeService;
	private IEmployeeProcessMappingService employeeProcessMappingService;
	private IProcessesService processesService;
	@Autowired
	private IPersonProductionUnitMappingService personProductionUnitMappingService;
	@Autowired
	private IProductionUnitService productionUnitService;
	private QREncoder qrEncoder = new QREncoder();
	@Autowired
	private IUserService userService;
	@Autowired
	public void setProcessesService(IProcessesService processesService) {
		this.processesService = processesService;
	}
	@Autowired
	public void setEmployeeProcessMappingService(IEmployeeProcessMappingService employeeProcessMappingService) {
		this.employeeProcessMappingService = employeeProcessMappingService;
	}
	@Autowired
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@RequestMapping("/queryEmployeesByDepartmentId.do")
	@ResponseBody
	public ModelMap queryEmployeesByDepartmentId(Long departmentId,Integer rows,Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from Employee e left join e.position p left join e.department d left join e.productionUnit pu where d.id=?0";
		@SuppressWarnings("unchecked")
		Pager<Object[]> objPager = employeeService.queryObjs(hql, page, rows, new Object[] {departmentId});
		List<Object[]> objs = objPager.getData();
		modelMap.addAttribute("rows", objArray2EmployeeVO(objs));
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}

	/**
	 * 将模型对象List转换为VO对象List
	 * @param objList
	 * @return
	 */
	private List<EmployeeVO> objArray2EmployeeVO(List<Object[]> objList){
		List<EmployeeVO> voList = new ArrayList<>();
		if(objList!=null) {
			for(int i = 0;i<objList.size();i++) {
				Object[] o = objList.get(i);
				EmployeeVO vo = new EmployeeVO();
				if(o[0]!=null) {
					Employee e = (Employee) o[0];
					vo.setName(e.getName());
					vo.setCode(e.getCode());
					vo.setTel(e.getTel());
				}

				if(o[1]!=null) {
					Position p = (Position)o[1];
					vo.setPositionId(p.getId());
					vo.setPositionCode(p.getCode());
					vo.setPositionName(p.getName());
				}
				if(o[2]!=null) {
					Department d = (Department)o[2];
					vo.setDepartmentCode(d.getCode());
					vo.setDepartmentName(d.getName());
				}
				if(o[3]!=null) {
					ProductionUnit d = (ProductionUnit)o[3];
					vo.setProductionUnitId(d.getId());
					vo.setProductionUnitCode(d.getCode());
					vo.setProductionUnitName(d.getName());
				}

				voList.add(vo);
			}
		}
		return voList;
	}
	/**
	 * 将模型对象List转换为VO对象List
	 * @param objList
	 * @return
	 */
	private List<EmployeeVO> employee2EmployeeVO(List<Employee> objList){
		List<EmployeeVO> voList = new ArrayList<>();
		if(objList!=null) {
			for(int i = 0;i<objList.size();i++) {
				Employee o = objList.get(i);
				EmployeeVO vo = new EmployeeVO();
				if(o!=null) {
					Employee e = (Employee) o;
					vo.setName(e.getName());
					vo.setCode(e.getCode());
					vo.setTel(e.getTel());
				}
				
				voList.add(vo);
			}
		}
		return voList;
	}
	/**
	 * 添加员工信息
	 * @param employee 员工对象
	 * @return
	 */
	@RequestMapping("/addEmployee.do")
	@ResponseBody
	public ModelMap addEmployee(Employee employee,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
		Employee e = employeeService.queryByProperty("code",employee.getCode());
		if(e!=null) {
			modelMap.addAttribute("msg", "员工代码已被使用!");
			modelMap.addAttribute("success", false);
		}else {
			employeeService.addObj(employee);
			modelMap.addAttribute("msg", "添加成功!");
			modelMap.addAttribute("success", true);
		}
		return modelMap;
	}
	/**
	 * 根据员工id查询员工信息
	 * @param id 员工对象id
	 * @return
	 */
	@RequestMapping("/queryEmployeeById.do")
	@ResponseBody
	public EmployeeVO queryEmployeeById(Long id,HttpServletRequest request) {
		Employee e = employeeService.queryObjById(id);
		return model2vo(e,request);
	}

	private EmployeeVO model2vo(Employee e,HttpServletRequest request) {
		if(e == null) {
			return null;
		}

		EmployeeVO vo = new EmployeeVO();
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setTel(e.getTel());
		vo.setDepartmentCode(e.getcDept_Num());
		vo.setDepartmentName(e.getcDepName());
		return vo;
	}
	/**
	 * 查询所有员工对象
	 * @return
	 */
	@RequestMapping("/queryAllEmployees.do")
	@ResponseBody
	public List<EmployeeVO> queryAllEmployees(HttpServletRequest request) {
		List<EmployeeVO> empVoList = new ArrayList<>();
		List<Employee> empList = employeeService.queryAllEmployees();
		for(Employee e:empList){
			empVoList.add(model2vo(e, request));
		}
		return empVoList;
	}
	
	@RequestMapping("/queryEmployees.do")
	@ResponseBody
	public ModelMap queryEmployees(Integer rows,Integer page,
			String employeeCode,String employeeName,String department) {
		ModelMap modelMap = new ModelMap();
		String hql = "from Employee e where 1=1 ";
		int i = 0;
		List<Object> params = new ArrayList<>();
		if(!StringUtils.isEmpty(employeeCode)) {
			hql += " and e.code like ?" + (i++);
			params.add("%"+employeeCode + "%");
		}
		if(!StringUtils.isEmpty(employeeName)) {
			hql += " and e.name like ?" + (i++);
			params.add("%"+employeeName + "%");
		}
		if(!StringUtils.isEmpty(department)) {
			hql += " and (e.position.department.code like ?" + i + " or e.position.department.name like ?" + (i++) + ")";
			params.add("%"+department + "%");
		}
		@SuppressWarnings("unchecked")
		Pager<Employee> objPager = employeeService.queryObjs(hql, page, rows, params.toArray());
		modelMap.addAttribute("rows", objPager.getData());
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
	/**
	 * 查询所有员工对象
	 * @return
	 */
	@RequestMapping("/queryAllEmployeesByProductionUnitId.do")
	@ResponseBody
	public List<EmployeeVO> queryAllEmployeesByProductionUnitId(Long productionUnitId,HttpServletRequest request) {
		List<EmployeeVO> empVoList = new ArrayList<>();
		if(productionUnitId==null) {
			List<Employee> list = employeeService.queryAllEmployees();
			for(Employee e:list){
				empVoList.add(model2vo(e, request));
			}
			return empVoList;
		}
		List<Employee> list = employeeService.queryAllEmployeesByProductionUnitId(productionUnitId);
		for(Employee e:list){
			empVoList.add(model2vo(e, request));
		}
		return empVoList;
	}
	/**
	 * 更新员工信息
	 * @return
	 */
	@RequestMapping("/updateEmployee.do")
	@ResponseBody
	public ModelMap updateEmployee(Employee employee,HttpServletRequest request) {
		ModelMap modelMap = new ModelMap();
			employeeService.updateObj(employee);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 删除员工
	 * @return
	 */
	@RequestMapping("/deleteEmployee.do")
	@ResponseBody
	public ModelMap deleteEmployee(String ids) {
		
		if(ids!=null && ids.contains("'")) {
			ids = ids.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		if(employeeService.deleteEmployees(ids.split(","))) {
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "成功删除!");
			modelMap.addAttribute("message", "成功删除!");
		}else {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("msg", "操作失败,存在关联用户的员工!");
			modelMap.addAttribute("message", "操作失败,存在关联用户的员工!");
		}
		return modelMap;
	}
	/**
	 * 停用该员工
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledEmployee.do")
	@ResponseBody
	public ModelMap disabledEmployee(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Employee d = employeeService.queryObjById(Long.valueOf(id));
		employeeService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}

	/**
	 * 添加非当前员工的工序
	 * @param employeeId
	 * @return
	 */
	@RequestMapping("/queryOtherProcesses.do")
	@ResponseBody
	public List<Processes> queryOtherProcesses(Long employeeId,String q) {
		if(q==null||"".equals(q.trim())) {
			return processesService.queryOtherProcessesByEmployeeId(employeeId);
		}else {
			return processesService.queryOtherProcessesByEmployeeIdAndCondition(employeeId, q);
		}
	}
	
	/**
	 * 为员工添加技能
	 * @param esm
	 * @return
	 */
	@RequestMapping("/addProcess4Employee.do")
	@ResponseBody
	public ModelMap addSkill4Employee(EmployeeProcessMapping esm) {
		ModelMap modelMap = new ModelMap();
		employeeProcessMappingService.addObj(esm);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功！");
		return modelMap;
	}
	/**
	 * 打印员工的二维码
	 * @param ids 员工id字符串
	 * @return
	 */
	@RequestMapping("/printQr.do")
	@ResponseBody
	public List<EmployeeVO> printQr(String ids,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<EmployeeVO> vos = new ArrayList<>();
		String[] idStr = ids.split(",");
		for(int i = 0 ;i<idStr.length;i++) {
			String id = idStr[i];
			Employee e = employeeService.queryObjById(Long.valueOf(id));
			EmployeeVO vo = model2vo(e,request);
			vo.setQrPath(qrEncoder.generatePR(e.getCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 打印员工的二维码(中京)
	 * @param codes 员工code字符串
	 * @return
	 */
	@RequestMapping("/printQrByCodes.do")
	@ResponseBody
	public List<EmployeeVO> printQrByCodes(String codes,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<EmployeeVO> vos = new ArrayList<>();
		String[] codeStr = codes.split(",");
		for(int i = 0 ;i<codeStr.length;i++) {
			String code = codeStr[i];
			Employee e = employeeService.queryEmployeeByCode(code);
			EmployeeVO vo = model2vo(e,request);
			vo.setQrPath(qrEncoder.generatePR(e.getCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}
	/**
	 * 根据员工代码查找员工信息
	 * @param code 员工代码
	 * @return
	 */
	@RequestMapping("/queryEmployeeByCode.do")
	@ResponseBody
	public EmployeeVO queryEmployeeByCode(String code,HttpServletRequest request) {
		Employee emp = employeeService.queryByProperty("code", code);
		return model2vo(emp, request);
	}
	/**
	 * 查找所有非用户的员工
	 * @return
	 */
	@RequestMapping("/queryEmployees4UserImport.do")
	@ResponseBody
	public List<EmployeeVO> queryEmployees4UserImport(HttpServletRequest request){
		List<EmployeeVO> dataList = new ArrayList<>();
		List<Employee> data = employeeService.queryEmployees4UserImport();
		for(Employee d:data){
			EmployeeVO empVo = model2vo(d, request);
			dataList.add(empVo);
		}
		return dataList;
	}
	/**
	 * 为用户分配角色
	 * @return
	 */
	@RequestMapping("/importUsers.do")
	@ResponseBody
	public ModelMap importUsers(String employeeIds,Principal principal) {
		ModelMap modelMap = new ModelMap();
		if(employeeIds!=null) {
			if(employeeIds.contains("[")) {
				employeeIds = employeeIds.replace("[", "");
			}
			if(employeeIds.contains("]")) {
				employeeIds = employeeIds.replace("]", "");
			}
			if(employeeIds.contains("\"")) {
				employeeIds = employeeIds.replace("\"", "");
			}

			/*HttpSession session = request.getSession();
			User loginUser  = (User) session.getAttribute(Constant.User.LOGIN_USER);*/
			User loginUser = userService.queryUserByUsername(principal.getName());
			String[] idArray = employeeIds.split(",");
			
			//查询当前用户数
			int count = userService.queryCount();
			//导入用户
			if(idArray!=null && idArray.length>0) {
				for(String id : idArray) {
					if(count>=Constant.USER_COUNT) {
						modelMap.addAttribute("success", false);
						modelMap.addAttribute("msg", Constant.USER_EXCEED_MSG);
						
						return modelMap;
					}
					Employee e = employeeService.queryObjById(id);
					User exist = userService.queryUserByUsername(e.getCode());
					if(null!=exist)
						continue;
					User user = new User();
					user.setUsername(e.getCode());
					user.setPassword(new PasswordEncoder(user.getUsername()).encode(e.getCode()));
					user.setCreateDate(new Date());
					user.setEmployee(e);
					if(loginUser!=null) {
						user.setCreateUserId(loginUser.getId());
						user.setCreateUsername(loginUser.getUsername());
						user.setModifyUserId(loginUser.getId());
						user.setModifyUsername(loginUser.getUsername());
					}
					user.setModifyDate(new  Date());
					userService.addObj(user);
					count++;
				}
			}
			modelMap.addAttribute("success",true);
			modelMap.addAttribute("msg","操作完成!");
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}
		return modelMap;
	}
	/**
	 * 图片上传
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload.do")
	@ResponseBody
	public ModelMap upload(Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/employeeImgs/";
		String realName = file.getSubmittedFileName();
		ModelMap modelMap = new ModelMap();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File out = new File(dir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/employeeImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
	/**
	 * 根据生产单元获取员工
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEmployeeByProductionUnitId.do")
	@ResponseBody
	public ModelMap queryEmployeeByProductionUnitId(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,Long productionUnitId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<Employee> pager = employeeService.queryObjs("select e from Employee e left join PersonProductionUnitMapping pp on e.code=pp.person.code"
				+ " left join ProductionUnit pu on pp.productionUnit.id=pu.id where pu.id=?0", page, rows, new Object[] {productionUnitId});
		List<Employee> emp = pager.getData();
		modelMap.addAttribute("rows", emp);
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 根据生产单元获取非该单元的员工
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEmployeeNotInByProductionUnitId.do")
	@ResponseBody
	public ModelMap queryEmployeeNotInByProductionUnitId(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,@RequestParam(value="productionUnitId")Long productionUnitId,HttpServletRequest request){
		ModelMap modelMap = new ModelMap();
		Pager<Object[]> pager = employeeService.queryObjs("from Employee e left join e.position p left join p.department d left join e.productionUnit pu where e.disabled=?0 and (pu.id!=?1 or pu.id is null)", page, rows, new Object[] {false,productionUnitId});
		List<Object[]> emp = pager.getData();
		modelMap.addAttribute("rows", objArray2EmployeeVO(emp));
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	/**
	 * 修改员工的生产单元
	 * @return
	 */
	@RequestMapping("/updateEmployeeProductionUnitId.do")
	@ResponseBody
	public ModelMap updateEmployeeProductionUnitId(Long productionUnitId,String employeeIds){
		ModelMap modelMap = new ModelMap();
		if(employeeIds!=null) {
			if(employeeIds.contains("[")) {
				employeeIds = employeeIds.replace("[", "");
			}
			if(employeeIds.contains("]")) {
				employeeIds = employeeIds.replace("]", "");
			}

			String[] idArray = employeeIds.split(",");
			for(int i = 0;i<idArray.length;i++) {
				PersonProductionUnitMapping ppMapping = personProductionUnitMappingService.queryByPersonCode(idArray[i].replaceAll("\"", ""));
				if(null!=ppMapping&&ppMapping.getProductionUnit().getId()!=productionUnitId){
					personProductionUnitMappingService.deleteObj(ppMapping.getId());
				}
				
				Employee emp = employeeService.queryByProperty("code", idArray[i].replaceAll("\"", ""));
				ProductionUnit pu = productionUnitService.queryObjById(productionUnitId);
				PersonProductionUnitMapping newMapping = new PersonProductionUnitMapping();
				newMapping.setPerson(emp);
				newMapping.setProductionUnit(pu);
				personProductionUnitMappingService.addObj(newMapping);
				modelMap.addAttribute("success",true);
				modelMap.addAttribute("msg","操作完成!");
			}
		}else {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("msg","操作失败!");
		}

		return modelMap;
	}
	/**
	 * 删除员工的生产单元
	 * @return
	 */
	@RequestMapping("/deleteEmployeeProductionUnitId.do")
	@ResponseBody
	public ModelMap deleteEmployeeProductionUnitId(String employeeId){
		ModelMap modelMap = new ModelMap();
		if(employeeId!=null && employeeId.contains("'")) {
			employeeId = employeeId.replace("'", "");
		}
		
		PersonProductionUnitMapping exist = personProductionUnitMappingService.queryByPersonCode(employeeId);
		if(null!=exist){
			personProductionUnitMappingService.deleteObj(exist.getId());
		}
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		return modelMap;
	}
	/**
	 * 删除员工的生产单元
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryEmployeeNotInMaintenanceStaff.do")
	@ResponseBody
	public ModelMap queryEmployeeNotInMaintenanceStaff(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		Pager<Employee> pager = employeeService.queryObjs("from Employee e where e.code not in (select m.code from MaintenanceStaff m)", page, rows, new Object[] {});
		List<Employee> emp = pager.getData();
		modelMap.addAttribute("rows", employee2EmployeeVO(emp));
		modelMap.addAttribute("total", pager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 查询所有非维修人员
	 */
	@RequestMapping("/queryAllEmployeeAndNotMaintenanceStaff.do")
	@ResponseBody
	public List<Employee> queryAllEmployeeAndNotMaintenanceStaff(){
		return employeeService.queryAllEmployeeAndNotMaintenanceStaff();
	}


	/**
	 * 根据部门编码查询部门下人员（中京）
	 */
	@RequestMapping("/queryEmployeesByDepartmentCodeOnZJ.do")
	@ResponseBody
	public ModelMap queryEmployeesByDepartmentCodeOnZJ(String departmentCode,@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from Employee e where e.cDept_Num=?0";
		@SuppressWarnings("unchecked")
		Pager<Employee> objPager = employeeService.queryObjs(hql, page, rows, new Object[] {departmentCode});
		modelMap.addAttribute("rows", objPager.getData());
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
}
