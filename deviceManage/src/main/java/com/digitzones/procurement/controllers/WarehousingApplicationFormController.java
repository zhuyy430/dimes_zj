package com.digitzones.procurement.controllers;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.service.IWarehousingApplicationFormService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IUserService;
import com.digitzones.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 入库申请单控制器
 */
@RestController
@RequestMapping("warehousingApplicationForm")
public class WarehousingApplicationFormController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "RKSQ-";
    @Autowired
    private IWarehousingApplicationFormService warehousingApplicationFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IEmployeeService employeeService;
    /**
     * 生成默认申请单号
     * @return
     */
    @RequestMapping("/generateRequestNo.do")
    public String generateRequestNo(){
        Date now = new Date();
        String formNo = warehousingApplicationFormService.queryMaxRequestNoByReceivingDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "001";
        }
        return requestNo;
    }
    /**
     * 根据入库申请单号查找入库申请单信息
     * @param formNo 入库申请单号
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public WarehousingApplicationForm queryByFormNo(String formNo){
        return  warehousingApplicationFormService.queryByProperty("formNo",formNo);
    }
    /**
     * 审核入库申请单
     * @param formNo 申请单号
     */
    @RequestMapping("/audit.do")
    public void audit(String formNo, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        Employee employee = loginUser.getEmployee();
        WarehousingApplicationForm form =  warehousingApplicationFormService.queryByProperty("formNo",formNo);
        form.setAuditDate(new Date());
        form.setAuditStatus("已审核");
        if(employee!=null) {
            form.setAuditorCode(employee.getCode());
            form.setAuditorName(employee.getName());
        }else{
            form.setAuditorCode(loginUser.getId() + "");
            form.setAuditorName(loginUser.getUsername());
        }
        warehousingApplicationFormService.audit(form);
    }
    /**
     * 反审核入库申请单
     * @param formNo 申请单号
     */
    @RequestMapping("/unaudit.do")
    public ModelMap unaudit(String formNo){
        ModelMap modelMap = new ModelMap();
        Long count = warehousingApplicationFormDetailService.queryCountOfAmountOfInWarehouseBigThenZero(formNo);
        if(count>0){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","已有物料入库，禁止删除。");
            return modelMap;
        }
        WarehousingApplicationForm form =  warehousingApplicationFormService.queryByProperty("formNo",formNo);
        form.setAuditDate(null);
        form.setAuditStatus("未审核");
        form.setAuditorCode(null);
        form.setAuditorName(null);
        warehousingApplicationFormService.unaudit(form);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","操作成功!");
        return modelMap;
    }

    /**
     * 根据入库申请单号删除申请单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByFormNo.do")
    public ModelMap deleteByFormNo(String formNo){
        ModelMap modelMap = new ModelMap();
        WarehousingApplicationForm form = warehousingApplicationFormService.queryByProperty("formNo", formNo);
        if(form.getAuditStatus().equals("已审核")){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","已审核记录，不允许删除!");
            return modelMap;
        }
        warehousingApplicationFormService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
