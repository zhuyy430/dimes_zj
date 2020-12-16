package com.digitzones.mc.controller;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.dto.InspectionRecordDetailDto;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mcInspectionRecord")
public class MCInspectionRecordController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "QC-";
	@Autowired
	private IProcessesService processesService;
	@Autowired
	private IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService;
	@Autowired
	private IEmployeeService EmployeeService;
	@Autowired
	private IInspectionRecordService inspectionRecordService;
	@Autowired
	private IInspectionRecordDetailService inspectionRecordDetaileService;
	@Autowired
	private IWorkSheetService workSheetService;
	@Autowired
	private IMCUserService mcUserService;
	/**
	 * 根据工件id查找所有参数等信息
	 * @return
	 */
	@RequestMapping("/queryAllWorkpieceProcessParameterMappingsByWorkpieceCode.do")
	@ResponseBody
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingsByWorkpieceCode(String InventoryCode,HttpSession session) {
		List<WorkpieceProcessParameterMapping> pager = workpieceProcessParameterMappingService.queryAllWorkpieceProcessParameterMappingByWorkpieceCode(InventoryCode);
        return pager;
	}

	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/deleteInspectionRecord.do")
	@ResponseBody
	public ModelMap  deleteInspectionRecord(String formNo) {
		ModelMap modelMap = new ModelMap();
		inspectionRecordService.deleteByFormNo(formNo);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("statusCode", 200);
		modelMap.addAttribute("title", "操作提示");
		modelMap.addAttribute("message", "成功删除");
		return modelMap;
	}

	/**
	 * 查询检验记录
	 * @return
	 */
	@RequestMapping("/queryInspectionRecord.do")
	@ResponseBody
	public InspectionRecord  queryInspectionRecord(String formNo) {
		return inspectionRecordService.queryByProperty("formNo", formNo);
	}

	/**
	 * 查询检验记录详情
	 * @return
	 */
	@RequestMapping("/queryInspectionRecordDetaile.do")
	@ResponseBody
	public List<InspectionRecordDetail>  queryInspectionRecordDetaile(String formNo) {
		return inspectionRecordDetaileService.queryByFormNo(formNo);
	}
	
    /**
     * 保存检验单
     * @param formVo
     * @param session
     * @return
     */
    @RequestMapping("/saveInspectionRecord.do")
    @ResponseBody
    public ModelMap saveInspectionRecord(String no,String workSheetId,String processCode ,String employeeCode,String inspectClass,String itemIds,String results,String notes,String checkValue,HttpServletRequest request){
        InspectionRecord form = new InspectionRecord();
        Employee employee = EmployeeService.queryEmployeeByCode(employeeCode);
        
        WorkSheet workSheet = workSheetService.queryObjById(Long.parseLong(workSheetId));
        
        Processes processes = processesService.queryByProperty("code", processCode);
        
        Date now = new Date();
        String requestNo = inspectionRecordService.queryMaxFormNoByInspectionDate(now);
        String formNo = "";
        if(!StringUtils.isEmpty(requestNo)){
        	formNo = StringUtil.increment(requestNo);
        }else{
        	formNo = FORMNO_PREFIX + format.format(now) + "000";
        }
        
        form.setFormNo(formNo);
        form.setInspectionDate(now);
        form.setProcessCode(processes.getCode());
        form.setProcessName(processes.getName());
        form.setNo(workSheet.getNo());
        form.setInventoryCode(workSheet.getWorkPieceCode());
        form.setInventoryName(workSheet.getWorkPieceName());
        form.setSpecificationType(workSheet.getUnitType());
        form.setBatchNumber(workSheet.getBatchNumber());
        form.setFurnaceNumber(workSheet.getStoveNumber());
        form.setProductionUnitCode(workSheet.getProductionUnitCode());
        form.setProductionUnitName(workSheet.getProductionUnitName());
        form.setInspectionType(inspectClass);
        
        List<InspectionRecordDetail> list = new ArrayList<>(); 
        String[] ids = itemIds.split(",");
        String[] resultses = results.split(",");
        String[] noteses = notes.split("@");
        String[] checkValues = checkValue.split(",");
        
        int i=0;
        for(String id:ids){
        	WorkpieceProcessParameterMapping mapping = workpieceProcessParameterMappingService.queryObjById(Long.parseLong(id));
        	InspectionRecordDetail detaile = new InspectionRecordDetail();
        	detaile.setParameterCode(mapping.getParameter().getCode());
        	detaile.setParameterName(mapping.getParameter().getName());
        	detaile.setUnit(mapping.getUnit());
        	detaile.setUpLine(mapping.getUpLine());
        	detaile.setLowLine(mapping.getLowLine());
        	detaile.setStandardValue(mapping.getStandardValue());
        	detaile.setNote(noteses[i]);
        	detaile.setInspectionResult(resultses[i]);
        	detaile.setParameterValue(checkValues[i]);
        	list.add(detaile);
        	i++;
        }
        
        ModelMap  modelMap = checkInspectionRecord(form,list);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }

        //没有检验单号，说明当前为新增操作
     /*   if(StringUtils.isEmpty(formNo)){*/
            InspectionRecord waf = inspectionRecordService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","检验单号已被使用！");
                return modelMap;
            }
            if(employee!=null) {
               form.setInspectorCode(employee.getCode());
               form.setInspectorName(employee.getName());
            }else{
            	String ip = request.getRemoteAddr();
        		MCUser mcUser = mcUserService.queryLoginUserByClientIp(ip);
                form.setInspectorCode(mcUser.getId() + "");
                form.setInspectorName(mcUser.getUsername());
            }
            inspectionRecordService.addInspectionRecord(form,list);
        /*}else{//有检验单号，说明当前为查看操作
            inspectionRecordService.updateInspectionRecord(form,list);
        }*/
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }

    /**
     *  保存检验单的校验方法
     * @param form 检验单对象
     * @param list 检验单详情对象
     * @return
     */
    private ModelMap checkInspectionRecord(InspectionRecord form,List<InspectionRecordDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="检验单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(form.getInspectionDate()==null){
            msg = "检验日期不能为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getNo())){
            msg ="工单单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
            msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(InspectionRecordDetail detail : list){
                if(detail.getParameterValue()==null || "".equals(detail.getParameterValue())){
                    msg = "参数值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }

                if(detail.getInspectionResult()==null || "".equals(detail.getInspectionResult())){
                    msg = "请选择检验结果!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }
    
    
	private List<InspectionRecordDetailDto> WorkpieceProcessParameterMappingToInspectionRecordDetail(List<WorkpieceProcessParameterMapping> pager,List<InspectionRecordDetailDto> list){
		for(WorkpieceProcessParameterMapping wpp:pager){
			InspectionRecordDetailDto detail = new InspectionRecordDetailDto();
			detail.setId(wpp.getId()+"");
			detail.setParameterCode(wpp.getParameter().getCode());
			detail.setParameterName(wpp.getParameter().getName());
			detail.setUnit(wpp.getUnit());
			detail.setUpLine(wpp.getUpLine());
			detail.setLowLine(wpp.getLowLine());
			detail.setStandardValue(wpp.getStandardValue());
			list.add(detail);
		}
		return list;
	}
	private List<InspectionRecordDetail> InspectionRecordDetailDtoToInspectionRecordDetail(List<InspectionRecordDetailDto> list){
		List<InspectionRecordDetail> details = new ArrayList<>();
		for(InspectionRecordDetailDto l:list){
			InspectionRecordDetail detail = new InspectionRecordDetail();
			 BeanUtils.copyProperties(l,detail);
			 details.add(detail);
		}
		return details;
	}
}
