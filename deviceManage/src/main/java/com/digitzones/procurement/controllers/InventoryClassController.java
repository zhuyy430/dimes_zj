package com.digitzones.procurement.controllers;
import java.util.ArrayList;
import java.util.List;

import com.digitzones.procurement.model.Inventory;
import com.digitzones.vo.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.InventoryClass;
import com.digitzones.procurement.service.IInventoryClassService;

/**
 * 物料类别控制器
 * @author zhuyy430
 *
 */
@RestController
@RequestMapping("/inventoryClass")
public class InventoryClassController {
	@Autowired
	private IInventoryClassService inventoryClassService;
	/**
	 * 物料类别信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("queryInventoryClass.do")
	public ModelMap queryInventoryClass(@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from InventoryClass i where 1=1 ";

		Pager<InventoryClass> pager = inventoryClassService.queryObjs(hql,page,rows,new Object[]{});
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}

	/**
	 * 获取顶级物料类别信息
	 * @return
	 */
	/*@RequestMapping("queryTopInventoryClass.do")
	public List<InventoryClass> queryTopInventoryClass() {
		List<InventoryClass> list = inventoryClassService.queryTopInventoryClass();
		for(InventoryClass l:list){
			List<InventoryClass> children = inventoryClassService.queryChildrenInventoryClass(l.getcInvCCode());
			l.setChildren(children);
		}
		return list;
	}*/

	/**
	 * 查询物料类别树
	 * @return
	 */
	@RequestMapping("/queryInventoryClassesTree.do")
	public List<Node> queryInventoryClassesTree(){
		List<Node> nodes = new ArrayList<>();
		List<Node> roots = new ArrayList<>();
		List<InventoryClass> list = inventoryClassService.queryTopInventoryClass();
		int level = 1;
		buildInventoryClassTree(nodes,list,level);
		Node root = new Node();
		root.setChildren(nodes);
		root.setName("物料类别");
		root.setCode("-1");
		
		roots.add(root);
		return roots;
	}

	/**
	 * 构建物料类别树
	 * @param nodes
	 * @param list
	 */
	private void buildInventoryClassTree(List<Node> nodes,List<InventoryClass> list,int level){
		if(CollectionUtils.isEmpty(list)){
			return ;
		}
		level++;
		for(InventoryClass l : list){
			Node node = new Node();
			List<InventoryClass> children = new ArrayList<>();
			if(!l.getbInvCEnd()){
				children = inventoryClassService.queryChildrenInventoryClass(l.getcInvCCode(),level);
			}
			node.setCode(l.getcInvCCode());
			node.setName(l.getcInvCName());
			List<Node> childrenNodes = new ArrayList<>();
			node.setChildren(childrenNodes);
			if(!CollectionUtils.isEmpty(children)){
				node.setState("closed");
			}

			nodes.add(node);
			buildInventoryClassTree(childrenNodes,children,level);
		}
	}
	/**
	 * 根据物料类别code查找
	 * @return
	 */
	@RequestMapping("/queryInventoryClassByPcode.do")
	@SuppressWarnings("unchecked")
	public ModelMap queryInventoryClassByPcode(Long code,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<InventoryClass> pager = null;
			pager = inventoryClassService.queryObjs("from InventoryClass ic where  ic.cInvCCode!="+code+" and ic.cInvCCode like '"+code+"%'", page, rows, new Object[] {});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	
}
