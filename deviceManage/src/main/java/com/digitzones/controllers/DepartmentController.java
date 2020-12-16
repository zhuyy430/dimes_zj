package com.digitzones.controllers;

import com.digitzones.model.Department;
import com.digitzones.model.Pager;
import com.digitzones.model.Position;
import com.digitzones.service.IDepartmentService;
import com.digitzones.service.IPositionService;
import com.digitzones.vo.DepartmentVO;
import com.digitzones.vo.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
/**
 * 部门管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
	private IDepartmentService departmentService;
	@Autowired
	private IPositionService positionService;
	@Autowired
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	/**
	 * 查询公司名称
	 * @return
	 */
	@RequestMapping("/queryTopDepartments.do")
	@ResponseBody
	public List<Department> queryTopDepartments(){
		return departmentService.queryTopDepartment();
	}
	/**
	 * 更新部门
	 * @param department
	 * @return
	 */
	@RequestMapping("/updateDepartment.do")
	@ResponseBody
	public ModelMap updateDepartment(Department department) {
		ModelMap modelMap = new ModelMap();
		Department d = departmentService.queryDepartmentByProperty("name",department.getName());
		if(d!=null && !department.getCode().equals(d.getCode())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "部门名称已被使用");
		}else {
			departmentService.updateObj(department);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除部门，如果该部门存在子部门，则不允许删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDepartment.do")
	@ResponseBody
	public ModelMap deleteDepartment(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Long count = departmentService.queryCountOfSubDepartment(Long.valueOf(id));
		if(count>0) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "该部门下存在子部门，不允许删除!");
		}else {
			List<Position> positionList = positionService.queryPositionByDepartmentId(Long.valueOf(id));
			if(!CollectionUtils.isEmpty(positionList)) {
				modelMap.addAttribute("statusCode", 300);
				modelMap.addAttribute("title", "操作提示");
				modelMap.addAttribute("message", "该部门下存在岗位，不允许删除!");
			}else {
				departmentService.deleteDepartment(Long.valueOf(id));
				modelMap.addAttribute("statusCode", 200);
				modelMap.addAttribute("title", "操作提示");
				modelMap.addAttribute("message", "成功删除!");
			}
		}
		return modelMap;
	}
	/**
	 * 停用该部门
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledDepartment.do")
	@ResponseBody
	public ModelMap disabledDepartment(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Department d = departmentService.queryDepartmentById(Long.valueOf(id));
		departmentService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据id查询部门
	 * @param
	 * @return
	 */
	@RequestMapping("/queryDepartmentByCode.do")
	@ResponseBody
	public DepartmentVO queryDepartmentByCode(String departmentCode) {
		Department department = departmentService.queryDepartmentByProperty("code",departmentCode);
		DepartmentVO vo = model2VO(department);
		return vo;
	}

	private DepartmentVO model2VO(Department department) {
		DepartmentVO vo = new DepartmentVO();
		vo.setName(department.getName());
		vo.setCode(department.getCode());
		return vo;
	}
	/**
	 * 查找所有部门
	 * @return
	 */
	@RequestMapping("/queryAllDepartments.do")
	@ResponseBody
	public List<Department> queryAllDepartments(Long id){
		return departmentService.queryAllDepartments();
	}
	/**
	 * 查找所有部门
	 * @return
	 */
	@RequestMapping("/queryOtherDepartments.do")
	@ResponseBody
	public List<Department> queryOtherDepartments(Long id){
		return departmentService.queryOtherDepartments(id);
	}
	/**
	 * 分页查询部门
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDepartmentsByParentId.do")
	@ResponseBody
	public ModelMap queryDepartmentsByParentId(@RequestParam(value="pid",required=false)Long pid,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = departmentService.queryObjs("from Department d inner join d.parent p  where p.id=?0 order by d.code", page, rows, new Object[] {pid});
		Pager<DepartmentVO> pagerDeptVO = new Pager<>();
		model2VO(pager, pagerDeptVO);
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pagerDeptVO.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}

	private void model2VO(Pager<Object[]> pagerDept,Pager<DepartmentVO> pagerDeptVO) {
		List<Object[]> depts = pagerDept.getData();
		List<DepartmentVO> deptVOs = pagerDeptVO.getData();
		for(Object[] obj:depts) {
			DepartmentVO vo = new DepartmentVO();
			Department son = (Department) obj[0];
			Department parent = (Department) obj[1];
			if(parent!=null) {
				vo.setPname(parent.getName());
				vo.setPcode(parent.getCode());
			}
			vo.setName(son.getName());
			vo.setCode(son.getCode());
			deptVOs.add(vo);
		}
	}
	/**
	 * 添加部门
	 * @param department
	 * @return
	 */
	@RequestMapping("/addDepartment.do")
	@ResponseBody
	public ModelMap addDepartment(Department department) {
		ModelMap modelMap = new ModelMap();
		//检测部门编码和名称是否重复
		Department dept4Code = departmentService.queryDepartmentByProperty("code", department.getCode());
		if(dept4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "部门编码已被使用");
		}else {
			Department	dept4Name = departmentService.queryDepartmentByProperty("name",department.getName());
			if(dept4Name!=null) {
				modelMap.addAttribute("success", false);
				modelMap.addAttribute("msg", "部门名称已被使用");
			}else {
				departmentService.addDepartment(department);
				modelMap.addAttribute("success", true);
				modelMap.addAttribute("msg", "添加成功!");
			}
		}
		return modelMap;
	}


	/**
	 * 查询物料类别树
	 * @return
	 */
	@RequestMapping("/queryDepartmentsTree.do")
	@ResponseBody
	public List<Node> queryDepartmentsTree(){
		List<Node> nodes = new ArrayList<>();
		List<Node> roots = new ArrayList<>();
		List<Department> list = departmentService.queryTopDepartmentByzj();
		int level = 1;
		buildInventoryClassTree(nodes,list,level);
		/*Node root = new Node();
		root.setChildren(nodes);
		root.setName("部门");
		root.setCode("-1");

		roots.add(root);*/
		return nodes;
	}



	/**
	 * 构建部门树
	 * @param nodes
	 * @param list
	 */
	private void buildInventoryClassTree(List<Node> nodes,List<Department> list,int level){
		if(CollectionUtils.isEmpty(list)){
			return ;
		}
		level++;
		for(Department l : list){
			Node node = new Node();
			List<Department> children = new ArrayList<>();
			//if(!l.getbInvCEnd()){
				children = departmentService.queryChildrenDepartment(l.getCode(),level);
			//}
			node.setCode(l.getCode());
			node.setName(l.getName());
			List<Node> childrenNodes = new ArrayList<>();
			node.setChildren(childrenNodes);
			if(!CollectionUtils.isEmpty(children)){
				node.setState("closed");
			}

			nodes.add(node);
			buildInventoryClassTree(childrenNodes,children,level);
		}
	}


} 
