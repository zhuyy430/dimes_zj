package com.digitzones.controllers;
import com.digitzones.model.Employee;
import com.digitzones.model.DeliveryPlan;
import com.digitzones.model.User;
import com.digitzones.service.IDeliveryPlanDetailService;
import com.digitzones.service.IDeliveryPlanService;
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
 * 发货单控制器
 */
@RestController
@RequestMapping("deliveryPlan")
public class DeliveryPlanController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "FHJH-";
    @Autowired
    private IDeliveryPlanService deliveryPlanService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeliveryPlanDetailService deliveryPlanDetailService;
    /**
     * 生成默认发货单号
     * @return
     */
    @RequestMapping("/generateRequestNo.do")
    public String generateRequestNo(){
        Date now = new Date();
        String formNo = deliveryPlanService.queryMaxFormNoByDeliverDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "001";
        }
        return requestNo;
    }
    /**
     * 根据发货单号查找发货单信息
     * @param formNo 发货单号
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public DeliveryPlan queryByFormNo(String formNo){
        return  deliveryPlanService.queryByProperty("formNo",formNo);
    }
    /**
     * 审核发货单
     * @param formNo 发货单号
     */
    @RequestMapping("/audit.do")
    public void audit(String formNo, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        DeliveryPlan form =  deliveryPlanService.queryByProperty("formNo",formNo);
        form.setAuditDate(new Date());
        form.setAuditStatus("已审核");
        if(loginUser.getEmployee()!=null) {
            form.setAuditorCode(loginUser.getEmployee().getCode());
            form.setAuditorName(loginUser.getEmployee().getName());
        }else{
            form.setAuditorCode(loginUser.getId() + "");
            form.setAuditorName(loginUser.getUsername());
        }
        deliveryPlanService.audit(form);
    }
    /**
     * 反审核发货单
     * @param formNo 申请单号
     */
    @RequestMapping("/unaudit.do")
    public ModelMap unaudit(String formNo){
        ModelMap modelMap = new ModelMap();
        DeliveryPlan form =  deliveryPlanService.queryByProperty("formNo",formNo);
        form.setAuditDate(null);
        form.setAuditStatus("未审核");
        form.setAuditorCode(null);
        form.setAuditorName(null);
        deliveryPlanService.unaudit(form);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","操作成功!");
        return modelMap;
    }
    /**
     * 根据发货单号删除申请单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByFormNo.do")
    public ModelMap deleteByFormNo(String formNo){
        ModelMap modelMap = new ModelMap();
        DeliveryPlan form = deliveryPlanService.queryByProperty("formNo", formNo);
        if(form.getAuditStatus().equals("已审核")){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","已审核记录，不允许删除!");
            return modelMap;
        }
        deliveryPlanService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
