package com.digitzones.controllers;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 参数类型管理控制器
 * @author zdq
 * 2018年6月7日
 */
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.constants.Constant;
import com.digitzones.model.DeviceSiteParameterMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.Parameters;
import com.digitzones.service.IDeviceSiteParameterMappingService;
import com.digitzones.service.IParameterService;
import com.digitzones.vo.ParametersVO;
@Controller
@RequestMapping("/parameter")
public class ParameterController {
	private IParameterService parameterService;
	private IDeviceSiteParameterMappingService deviceSiteParameterMappingService;
	@Autowired
	public void setDeviceSiteParameterMappingService(IDeviceSiteParameterMappingService deviceSiteParameterMappingService) {
		this.deviceSiteParameterMappingService = deviceSiteParameterMappingService;
	}
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	/**
	 * 分页查询参数信息
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryParametersByParameterTypeId.do")
	@ResponseBody
	public ModelMap queryParametersByParameterTypeId(@RequestParam(value="parameterTypeId",required=false)Long parameterTypeId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = parameterService.queryObjs("select p from Parameters p inner join p.parameterType pt  where pt.id=?0", page, rows, new Object[] {parameterTypeId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	/**
	 * 根据设备id分页查询参数信息
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDeviceSiteParameterByDeviceId.do")
	@ResponseBody
	public ModelMap queryDeviceSiteParameterByDeviceId(@RequestParam(value="deviceId",required=false)Long deviceId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = deviceSiteParameterMappingService.queryObjs("from DeviceSiteParameterMapping dspm where dspm.deviceSite.device.id=?0", page, rows, new Object[] {deviceId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	/**
	 * 根据设备站点id查询非当前设备站点的参数信息
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherParametersByDeviceSiteId.do")
	@ResponseBody
	public List<Parameters> queryOtherParametersByDeviceSiteId(@RequestParam(value="deviceSiteId",required=false)Long deviceSiteId) {
		List<Parameters> list = parameterService.queryOtherParametersByDeviceSiteId(deviceSiteId);
		return list;
	}
	/**
	 * 根据设备id查询非当前设备的参数信息
	 * @param pid
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherParametersByDeviceId.do")
	@ResponseBody
	public List<Parameters> queryOtherParametersByDeviceId(Long deviceId) {
		List<Parameters> list = parameterService.queryOtherParametersByDeviceId(deviceId);
		return list;
	}
	
	
	/**
	 * 添加设备站点和参数的映射
	 * @param deviceSiteParameter
	 * @return
	 */
	@RequestMapping("/addDeviceSiteParameter.do")
	@ResponseBody
	public ModelMap addDeviceSiteParameter(Long deviceId,DeviceSiteParameterMapping deviceSiteParameter) {
		ModelMap modelMap = new ModelMap();
		if(deviceSiteParameter.getParameter().getId()==null) {
			deviceSiteParameter.setParameter(null);
		}
		deviceSiteParameterMappingService.addDeviceSiteParameterMappings(deviceId, deviceSiteParameter);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "添加成功!");
		return modelMap;
	};
	/**
	 *更新设备站点和参数的映射
	 * @param deviceSiteParameter
	 * @return
	 */
	@RequestMapping("/updateDeviceSiteParameter.do")
	@ResponseBody
	public ModelMap updateDeviceSiteParameter(DeviceSiteParameterMapping deviceSiteParameter) {
		ModelMap modelMap = new ModelMap();
		DeviceSiteParameterMapping dspm = deviceSiteParameterMappingService.queryObjById(deviceSiteParameter.getId());
		dspm.setLowLine(deviceSiteParameter.getLowLine());
		dspm.setUpLine(deviceSiteParameter.getUpLine());
		dspm.setStandardValue(deviceSiteParameter.getStandardValue());
		dspm.setNote(deviceSiteParameter.getNote());
		dspm.setTrueValue(deviceSiteParameter.getTrueValue());
		deviceSiteParameter.setUpdateDate(new Date());
		deviceSiteParameterMappingService.updateObj(dspm);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "更新成功!");
		return modelMap;
	};
	/**
	 * 根据id删除设备站点和参数的映射对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteDeviceSiteParameterById.do")
	@ResponseBody
	public ModelMap deleteDeviceSiteParameterById(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		deviceSiteParameterMappingService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "删除成功!");
		
		return modelMap;
	}
	/**
	 * 根据id 查询设备站点和参数的映射对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryDeviceSiteParameterById.do")
	@ResponseBody
	public DeviceSiteParameterMapping queryDeviceSiteParameterById(Long id) {
		DeviceSiteParameterMapping dspm = deviceSiteParameterMappingService.queryObjById(id);
		return dspm;
	}
	
	
	/**
	 * 添加参数
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addParameter.do")
	@ResponseBody
	public ModelMap addParameter(Parameters parameter) {
		ModelMap modelMap = new ModelMap();
		Parameters parameter4Code = parameterService.queryByProperty("code", parameter.getCode());
		if(parameter4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "参数编码已被使用");
		}else {
			parameterService.addObj(parameter);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id查询参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryParameterById.do")
	@ResponseBody
	public Parameters queryParameterById(Long id) {
		Parameters parameter = parameterService.queryObjById(id);
		return parameter;
	}
	/**
	 * 根据id查询参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryParameterVOById.do")
	@ResponseBody
	public ParametersVO queryParameterVOById(Long id) {
		Parameters parameter = parameterService.queryObjById(id);
		return model2Vo(parameter);
	}
	
	private ParametersVO model2Vo(Parameters p) {
		ParametersVO vo = new ParametersVO();
		if(p == null) {
			return null;
		}

		vo.setId(p.getId());
		vo.setName(p.getName());
		vo.setCode(p.getCode());
		vo.setBaseCode(p.getBaseCode());
		vo.setDisabled(p.getDisabled());
		vo.setKfc(p.getKfc()?"true":"false");
		vo.setRules(p.getRules());
		vo.setParameterType(p.getParameterType());
		vo.setNote(p.getNote());
		return vo;
	}
	/**
	 * 更新参数信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateParameter.do")
	@ResponseBody
	public ModelMap updateParameter(Parameters parameter) {
		ModelMap modelMap = new ModelMap();
		Parameters pu = parameterService.queryByProperty("name", parameter.getName());
		if(pu!=null && !parameter.getId().equals(pu.getId())) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "参数名称已被使用");
		}else {
			parameter.setBaseCode(pu.getBaseCode());
			parameter.setRules(pu.getRules());
			parameterService.updateObj(parameter);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "编辑成功!");
		}
		return modelMap;
	}
	/**
	 * 根据id删除参数信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteParameter.do")
	@ResponseBody
	public ModelMap deleteParameter(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		parameterService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 停用该参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/disabledParameter.do")
	@ResponseBody
	public ModelMap disabledParameter(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		Parameters d = parameterService.queryObjById(Long.valueOf(id));
		d.setDisabled(!d.getDisabled());

		parameterService.updateObj(d);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("message", "操作完成!");
		modelMap.addAttribute("title", "操作提示!");
		return modelMap;
	}
	/**
	 * 根据工序id查询参数 信息
	 * @param processId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryParametersByProcessId.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryParametersByProcessId(Long processId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "select ds from ProcessesParametersMapping pdm inner join  pdm.parameters ds  inner join pdm.processes p where p.id=?0";
		Pager<Parameters> pager = parameterService.queryObjs(hql, page, rows, new Object[] {processId});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	/**
	 * 根据工序id查询非当前工序下的参数信息
	 * @param processId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryOtherParameters.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryOtherParameters(Long processId,@RequestParam(value="rows",defaultValue="20")int rows,@RequestParam(defaultValue="1")int page) {
		ModelMap modelMap = new ModelMap();
		String hql = "select ds from Parameters ds where ds.id not in ("
				+ "select pdm.parameters.id from ProcessesParametersMapping pdm where pdm.processes.id=?0) and ds.baseCode=?1";
		Pager<Parameters> pager = parameterService.queryObjs(hql, page, rows, new Object[] {processId,Constant.ParameterType.ART});
		modelMap.addAttribute("total", pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
} 
