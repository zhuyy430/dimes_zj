package com.digitzones.controllers;
import com.digitzones.model.JobBookingForm;
import com.digitzones.model.User;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IJobBookingFormService;
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
 * 报工单控制器
 */
@RestController
@RequestMapping("jobBookingForm")
public class JobBookingFormController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "BG-";
    @Autowired
    private IJobBookingFormService jobBookingFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    /**
     * 生成默认报工单号
     * @return
     */
    @RequestMapping("/generateRequestNo.do")
    public String generateRequestNo(){
        Date now = new Date();
        String formNo = jobBookingFormService.queryMaxFormNoByJobBookingDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "001";
        }
        return requestNo;
    }
    /**
     * 根据报工单号查找报工单信息
     * @param formNo 报工单号
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public JobBookingForm queryByFormNo(String formNo){
        return  jobBookingFormService.queryByProperty("formNo",formNo);
    }
    /**
     * 审核报工单
     * @param formNo 报工单号
     */
    @RequestMapping("/audit.do")
    public void audit(String formNo, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        //Employee employee = loginUser.getEmployee();
        JobBookingForm form =  jobBookingFormService.queryByProperty("formNo",formNo);
        form.setAuditDate(new Date());
        form.setAuditStatus("已审核");
        if(loginUser.getEmployee()!=null) {
            form.setAuditorCode(loginUser.getEmployee().getCode());
            form.setAuditorName(loginUser.getEmployee().getName());
        }else{
            form.setAuditorCode(loginUser.getId() + "");
            form.setAuditorName(loginUser.getUsername());
        }
        jobBookingFormService.audit(form);
    }
    /**
     * 反审核报工单
     * @param formNo 申请单号
     */
    @RequestMapping("/unaudit.do")
    public ModelMap unaudit(String formNo){
        ModelMap modelMap = new ModelMap();
        Long count = jobBookingFormDetailService.queryCountOfAmountOfInWarehouseBigThenZero(formNo);
        if(count>0){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","该单据中已有物料进行入库操作，请删除相应入库单据后再执行此操作。");
            return modelMap;
        }
        JobBookingForm form =  jobBookingFormService.queryByProperty("formNo",formNo);
        form.setAuditDate(null);
        form.setAuditStatus("未审核");
        form.setAuditorCode(null);
        form.setAuditorName(null);
        jobBookingFormService.unaudit(form);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","操作成功!");
        return modelMap;
    }
    /**
     * 根据报工单号删除申请单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByFormNo.do")
    public ModelMap deleteByFormNo(String formNo){
        ModelMap modelMap = new ModelMap();
        JobBookingForm form = jobBookingFormService.queryByProperty("formNo", formNo);
        if(form.getAuditStatus().equals("已审核")){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","已审核记录，不允许删除!");
            return modelMap;
        }
        jobBookingFormService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
