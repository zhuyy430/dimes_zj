package com.digitzones.controllers;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.config.QRConfig;
import com.digitzones.model.NGReason;
import com.digitzones.model.Pager;
import com.digitzones.service.INGReasonService;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.NGReasonVO;
/**
 * 不良原因管理控制器
 * @author zdq
 * 2018年6月7日
 */
@Controller
@RequestMapping("/ngReason")
public class NGReasonController {
	private QRConfig config ;
	@Autowired
	public void setConfig(QRConfig config) {
		this.config = config;
	}
	private QREncoder qrEncoder = new QREncoder();
	private INGReasonService ngReasonService;
	@Autowired
	public void setNgReasonService(INGReasonService ngReasonService) {
		this.ngReasonService = ngReasonService;
	}

	/**
	 * 分页查询不良原因信息
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGReasonsByNGReasonTypeId.do")
	@ResponseBody
	public ModelMap queryNGReasonsByNGReasonTypeId(@RequestParam(value="ngReasonTypeId",required=false)Long parameterTypeId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<Object[]> pager = null;
		pager = ngReasonService.queryObjs("select p from NGReason p inner join p.ngReasonType pt  where pt.id=?0", page, rows, new Object[] {parameterTypeId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNGReasonsByProcessId.do")
	@ResponseBody
	public ModelMap queryNGReasonsByProcessId(@RequestParam(value="processId",required=false)Long processId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<NGReason> pager = null;
		pager = ngReasonService.queryObjs("select p from NGReason p where p.process.id=?0", page, rows, new Object[] {processId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		mm.addAttribute("code", "0");
		mm.addAttribute("msg", "");
		return mm;
	}

	/**
	 * 添加不良原因
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/addNGReason.do")
	@ResponseBody
	public ModelMap addNGReason(NGReason parameter) {
		ModelMap modelMap = new ModelMap();
		NGReason parameter4Code = ngReasonService.queryByProperty("ngCode", parameter.getNgCode());
		if(parameter4Code!=null) {
			modelMap.addAttribute("success", false);
			modelMap.addAttribute("msg", "代码重复，请重新输入");
		}else {
			ngReasonService.addObj(parameter);
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("msg", "添加成功!");
		}
		return modelMap;
	}

	/**
	 * 查找所有的不良原因大类
	 * @return
	 */
	@RequestMapping("/queryAllCategories.do")
	@ResponseBody
	public List<String> queryAllCategories(){
		return ngReasonService.queryAllCategories();
	}
	/**
	 * 根据id查询不良原因
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryNGReasonById.do")
	@ResponseBody
	public NGReason queryNGReasonById(Long id) {
		NGReason parameter = ngReasonService.queryObjById(id);
		return parameter;
	}
	/**
	 * 更新不良原因
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateNGReason.do")
	@ResponseBody
	public ModelMap updateNGReason(NGReason parameter) {
		ModelMap modelMap = new ModelMap();
		ngReasonService.updateObj(parameter);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "编辑成功!");
		return modelMap;
	}
	/**
	 * 根据id删除不良原因
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteNGReason.do")
	@ResponseBody
	public ModelMap deleteNGReason(String id) {
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ModelMap modelMap = new ModelMap();
		String[] ids = id.split(",");
		for(String Id:ids){
			ngReasonService.deleteObj(Long.valueOf(Id));
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除!");
		return modelMap;
	}
	/**
	 * 根据不良原因类型查找不良原因
	 * @param ngReasonTypeId
	 * @return
	 */
	@RequestMapping("/queryNGReasonsByNGReasonTypeIdNoPager.do")
	@ResponseBody
	public List<NGReason> queryNGReasonsByNGReasonTypeIdNoPager(Long ngReasonTypeId){
		return ngReasonService.queryNGReasonsByNGReasonTypeId(ngReasonTypeId);
	}
	/**
	 * 打印ng的二维码
	 * @param ids 设备id字符串
	 * @return
	 */
	@RequestMapping("/printQr.do")
	@ResponseBody
	public List<NGReasonVO> printQr(String ids,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		List<NGReasonVO> vos = new ArrayList<>();
		String[] idStr = ids.split(",");
		for(int i = 0 ;i<idStr.length;i++) {
			String id = idStr[i];
			NGReason e = ngReasonService.queryObjById(Long.valueOf(id));
			NGReasonVO vo = model2VO(e);
			vo.setQrPath(qrEncoder.generatePR(e.getNgCode(),dir , config.getQrPath()));
			vos.add(vo);
		}
		return vos;
	}
	
	private NGReasonVO model2VO(NGReason ng) {
		if(ng == null) {
			return null;
		}
		NGReasonVO vo = new NGReasonVO();
		vo.setId(ng.getId());
		vo.setNgCode(ng.getNgCode());
		vo.setNgReason(ng.getNgReason());
		if(ng.getProcess()!=null) {
			vo.setProcessCode(ng.getProcess().getCode());
			vo.setProcessName(ng.getProcess().getName());
		}
		return vo;
	}
	/**
	 * 根据工序ID查找ng原因
	 * @param processId
	 * @return
	 */
	@RequestMapping("/queryNGReasonsByProcessIdNoPager.do")
	@ResponseBody
	public List<NGReason> queryNGReasonsByProcessIdNoPager(Long processId){
		return ngReasonService.queryNGReasonByProcessId(processId);
	}
	/**
	 * 根据工序Code查找ng原因
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/queryNGReasonsByProcessCode.do")
	@ResponseBody
	public List<NGReason> queryNGReasonsByProcessCode(String processCode){
		return ngReasonService.queryNGReasonByProcessCode(processCode);
	}
	/**
	 * 查找所有ng原因
	 * @param processId
	 * @return
	 */
	@RequestMapping("/queryAllNGReasons.do")
	@ResponseBody
	public List<NGReason> queryAllNGReasons(Long processId){
		return ngReasonService.queryAllNGReasons();
	}
} 
