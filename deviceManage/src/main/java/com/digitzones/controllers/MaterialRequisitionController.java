package com.digitzones.controllers;

import com.digitzones.model.MaterialRequisition;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.model.User;
import com.digitzones.service.IMaterialRequisitionService;
import com.digitzones.service.IUserService;
import com.digitzones.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 领用单控制器
 */
@RestController
@RequestMapping("materialRequisition")
public class MaterialRequisitionController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "SCLL-";
    @Autowired
    private IMaterialRequisitionService materialRequisitionService;
    @Autowired
    private IUserService userService;
  /*  @Autowired
    private IWorkSheetService workSheetService;*/
    /**
     * 生成默认申请单号
     * @return
     */
    @RequestMapping("/generateMaterialRequisitionFormNo.do")
    public String generateMaterialRequisitionFormNo(){
        Date now = new Date();
        String formNo = materialRequisitionService.queryMaxFormNoByPickingDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "000";
        }
        return requestNo;
    }
    /**
     * 根据领用单号查找领用单信息
     * @param formNo 领用单单号
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public MaterialRequisition queryByFormNo(String formNo){
        return  materialRequisitionService.queryByProperty("formNo",formNo);
    }
    /**
     * 生成ERP领料单
     * @param formNo 领用单单号
     * @return
     */
    @RequestMapping("/generateERPMaterialRequisition.do")
    public ModelMap generateERPMaterialRequisition(String formNo,String no){
        ModelMap modelMap = new ModelMap();
        try {
            materialRequisitionService.generateERPMaterialRequisition(formNo, no);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("msg","ERP领料单已生成，请前往ERP系统查看!");
        }catch (RuntimeException re){
            re.printStackTrace();
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg",re.getMessage());
        }
        return modelMap;
    }
    /**
     * 保存入库申请单
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/saveMaterialRequisition.do")
    public ModelMap saveMaterialRequisition(MaterialRequisition form , HttpSession session, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        //Employee employee = loginUser.getEmployee();
        String formNo = (String) session.getAttribute(MaterialRequisitionDetailController.MATERIALREQUISITION_FORMNO);
        @SuppressWarnings("unchecked")
		List<MaterialRequisitionDetail> details = (List<MaterialRequisitionDetail>) session.getAttribute(MaterialRequisitionDetailController.LIST_NAME);
        ModelMap  modelMap = checkMaterialRequisition(form,details);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }
        //没有入库申请单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            MaterialRequisition waf = materialRequisitionService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","入库申请单号已被使用！");
                return modelMap;
            }
            if(loginUser.getEmployee()!=null) {
               form.setPickerCode(loginUser.getEmployee().getCode());
               form.setPickerName(loginUser.getEmployee().getName());
            }else{
                form.setPickerCode(loginUser.getId() + "");
                form.setPickerName(loginUser.getUsername());
            }
            materialRequisitionService.addMaterialRequisition(form,details);
            session.setAttribute(MaterialRequisitionDetailController.MATERIALREQUISITION_FORMNO,form.getFormNo());
        }else{//有入库申请单号，说明当前为查看操作
        	try {
            @SuppressWarnings("unchecked")
			List<String> deletedIds = (List<String>) session.getAttribute(MaterialRequisitionDetailController.DELETED_DETAIL_IDS);
            materialRequisitionService.addMaterialRequisition(form,details,deletedIds);
            session.removeAttribute(MaterialRequisitionDetailController.DELETED_DETAIL_IDS);
        	}catch(RuntimeException e) {
        	    e.printStackTrace();
        		modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg",e.getMessage());
                return modelMap;
        	}
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }
    /**
     *  保存入库申请单的校验方法
     * @param form 入库申请单对象
     * @param list 入库申请单详情对象
     * @return
     */
    private ModelMap checkMaterialRequisition(MaterialRequisition form,List<MaterialRequisitionDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        //WorkSheet workSheet = workSheetService.queryByProperty("no",form.getWorkSheet().getNo());
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="领用单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(form.getPickingDate()==null){
            msg = "领用日期不能为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getWorkSheet().getNo())){
            msg ="工单单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
            msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            Double amount = 0d;
            for(MaterialRequisitionDetail detail : list){
                if(detail.getAmount()==null || detail.getAmount()<=0){
                    msg = "数量值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
                amount+=detail.getAmount()==null?0:detail.getAmount();
            }
        }
        return modelMap;
    }
    /**
     * 根据领料单单号删除领料单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByFormNo.do")
    public ModelMap deleteByFormNo(String formNo){
        ModelMap modelMap = new ModelMap();
        materialRequisitionService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
