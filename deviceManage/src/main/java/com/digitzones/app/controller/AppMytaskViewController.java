package com.digitzones.app.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.app.model.MytaskView;
import com.digitzones.app.service.IAppMytaskViewService;
import com.digitzones.model.Pager;

@Controller
@RequestMapping("/AppMytaskView")
public class AppMytaskViewController {

	@Autowired
	private IAppMytaskViewService appMytaskViewService;
	/**
	 * 根据用户编码查找发出的任务
	 */
	@RequestMapping("/queryMySendOutTaskByUsercode.do")
	@ResponseBody
	public List<MytaskView> queryMySendOutTaskByUsercode(String code){
		return appMytaskViewService.queryMySendOutTaskByUsercode(code);
	}
	
	/**
	 * 根据用户编码查找接收的任务
	 */
	@RequestMapping("/queryMyReceiveTaskByUsercode.do")
	@ResponseBody
	public List<MytaskView> queryMyReceiveTaskByUsercode(String code){
		return appMytaskViewService.queryMyReceiveTaskByUsercode(code);
	}
	
	/**
	 * 根据用户编码查找已完成的任务
	 */
	@RequestMapping("/queryMyCompleteTaskByUsercode.do")
	@ResponseBody
	public List<MytaskView> queryMyCompleteTaskByUsercode(String code){
		return appMytaskViewService.queryMyCompleteTaskByUsercode(code);
	}
	
	/**
	 * 根据用户编码查找发出的任务(分页)
	 */
	@RequestMapping("/queryMySendOutTaskByUsercodeWithPage.do")
	@ResponseBody
	public ModelMap queryMySendOutTaskByUsercodeWithPage(String code,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		String hql="from MytaskView where informantCode=?0 and status not in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List<Object> paramList = new ArrayList<>();
		paramList.add(code);
		Pager<MytaskView> pager=appMytaskViewService.queryObjs(hql, page, rows, paramList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	
	/**
	 * 根据用户编码查找接收的任务(分页)
	 */
	@RequestMapping("/queryMyReceiveTaskByUsercodeWithPage.do")
	@ResponseBody
	public ModelMap queryMyReceiveTaskByUsercodeWithPage(String code,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		String hql="from MytaskView where code=?0 and status not in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List<Object> paramList = new ArrayList<>();
		paramList.add(code);
		Pager<MytaskView> pager=appMytaskViewService.queryObjs(hql, page, rows, paramList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	
	/**
	 * 根据用户编码查找已完成的任务(分页)
	 */
	@RequestMapping("/queryMyCompleteTaskByUsercodeWithPage.do")
	@ResponseBody
	public ModelMap queryMyCompleteTaskByUsercodeWithPage(String code,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		ModelMap modelMap = new ModelMap();
		String hql="from MytaskView where (informantCode=?0 or code=?1) and status in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List<Object> paramList = new ArrayList<>();
		paramList.add(code);
		paramList.add(code);
		Pager<MytaskView> pager=appMytaskViewService.queryObjs(hql, page, rows, paramList.toArray());
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
}
