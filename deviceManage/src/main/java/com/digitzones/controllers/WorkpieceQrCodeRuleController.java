package com.digitzones.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceQrCodeRule;
import com.digitzones.service.IWorkpieceQrCodeRuleService;
import com.digitzones.util.SMBUtil;
import com.digitzones.vo.WorkpieceQrCodeRuleVO;
/**
 * 工件二维码规则控制器
 * @author Administrator
 */
@RestController
@RequestMapping("/workpieceQrCodeRule")
public class WorkpieceQrCodeRuleController {
	@Autowired
	private IWorkpieceQrCodeRuleService workpieceQrCodeRuleService;
/*	@Autowired
	private SMBConfig smbConfig;*/
	/**
	 * 查询工件二位码规则
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWorkpieceQrCodeRules.do")
	public ModelMap queryWorkpieceQrCodeRules(@RequestParam Map<String,String> params) {
		ModelMap modelMap = new ModelMap();
		String rows = params.get("rows");
		String page = params.get("page");
		String hql = "select w from WorkpieceQrCodeRule w left join w.workpiece where w.enabled=?0";
		if(StringUtils.isNotBlank(rows) && StringUtils.isNotBlank(page)) {
			Pager<WorkpieceQrCodeRule> result = workpieceQrCodeRuleService.queryObjs(hql, Integer.parseInt(page), Integer.parseInt(rows), new Object[] {true});
			modelMap.addAttribute("total", result.getTotalCount());
			modelMap.addAttribute("rows", result.getData());
		}
		return modelMap;
	}

	/**
	 * 根据id查找工件二维码规则
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryWorkpieceQrCodeRuleById.do")
	public WorkpieceQrCodeRuleVO queryWorkpieceQrCodeRuleById(Long id) {
		WorkpieceQrCodeRuleVO vo = new WorkpieceQrCodeRuleVO();
		WorkpieceQrCodeRule workpieceQrCodeRule = workpieceQrCodeRuleService.queryObjById(id);
		BeanUtils.copyProperties(workpieceQrCodeRule, vo);
		if(workpieceQrCodeRule.getWorkpiece()!=null) {
			vo.setWorkpieceId(workpieceQrCodeRule.getId());
			vo.setWorkpieceName(workpieceQrCodeRule.getWorkpiece().getName());
			vo.setWorkpieceCode(workpieceQrCodeRule.getWorkpiece().getCode());
		}
		return vo;
	}
	/**
	 * 添加工件二维码规则
	 * @return
	 */
	@RequestMapping("/addWorkpieceQrCodeRule.do")
	public ModelMap addWorkpieceQrCodeRule(WorkpieceQrCodeRule workpieceQrCodeRule) {
		ModelMap modelMap = new ModelMap();
		
		//判断当前打印机IP是否已被使用
		List<WorkpieceQrCodeRule> list = workpieceQrCodeRuleService.queryByPrinterIp(workpieceQrCodeRule.getPrinterIp(), true);
		if(list!=null && list.size()>=1) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "当前打印机正在使用中...!");
			return modelMap;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(workpieceQrCodeRule.getWorkpiece().getCode())
				.append(workpieceQrCodeRule.getManufacturerCode());
		workpieceQrCodeRule.setTm1(builder.toString());
		workpieceQrCodeRule.setTm2(workpieceQrCodeRule.getManufacturerCode());
		workpieceQrCodeRule.setCreateDate(new Date());
		workpieceQrCodeRuleService.addObj(workpieceQrCodeRule);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	}
	
	/**
	 * 更新工件二维码规则
	 * @return
	 */
	@RequestMapping("/updateWorkpieceQrCodeRule.do")
	@ResponseBody
	public ModelMap updateWorkpieceQrCodeRule(WorkpieceQrCodeRule workpieceQrCodeRule) {
		ModelMap modelMap = new ModelMap();
		StringBuilder builder = new StringBuilder();
		builder.append(workpieceQrCodeRule.getWorkpiece().getCode())
		.append(workpieceQrCodeRule.getManufacturerCode());
		workpieceQrCodeRule.setTm1(builder.toString());
		workpieceQrCodeRule.setTm2(workpieceQrCodeRule.getManufacturerCode());
		workpieceQrCodeRuleService.updateObj(workpieceQrCodeRule);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	}
	/**
	 *删除工件二维码规则
	 * @return
	 */
	@RequestMapping("/deleteWorkpieceQrCodeRule.do")
	@ResponseBody
	public ModelMap deleteWorkpieceQrCodeRule(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		workpieceQrCodeRuleService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 *删除工件二维码规则
	 * @return
	 */
	@RequestMapping("/send2printer.do")
	@ResponseBody
	public ModelMap send2printer(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		WorkpieceQrCodeRule workpieceQrCodeRule = workpieceQrCodeRuleService.queryObjById(Long.parseLong(id));
		
		String remoteIp = workpieceQrCodeRule.getPrinterIp();
		String remoteUser = workpieceQrCodeRule.getRemoteUser();
		String remotePass = workpieceQrCodeRule.getRemotePass();
		String sharedDir = workpieceQrCodeRule.getSharedDir();
		
		if(StringUtils.isBlank(remoteIp)) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "请设置打印机服务器IP地址!");
			return modelMap;
		}
		if(StringUtils.isBlank(remoteUser)) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "请设置打印机服务器用户名!");
			return modelMap;
		}
		if(StringUtils.isBlank(sharedDir)) {
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "请设置打印机服务器共享目录名称!");
			return modelMap;
		}
		String tm1 = "/" + sharedDir + "/TM1.txt";
		String tm2 = "/" + sharedDir + "/TM2.txt";
		try {
			SMBUtil.upload2SharedDir(remoteIp, remoteUser, remotePass==null?"":remotePass, tm1, workpieceQrCodeRule.getTm1().getBytes());
			SMBUtil.upload2SharedDir(remoteIp, remoteUser, remotePass==null?"":remotePass, tm2, workpieceQrCodeRule.getTm2().getBytes());
			
			workpieceQrCodeRule.setSendDate(new Date());
			workpieceQrCodeRule.setSended(true);
			
			workpieceQrCodeRuleService.updateWorkpieceQRCodeRule(workpieceQrCodeRule);
		} catch (IOException e) {
			e.printStackTrace();
			modelMap.addAttribute("statusCode", 300);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "远程打印机拒绝访问!");
			return modelMap;
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "发送成功!");
		return modelMap;
	}
}
