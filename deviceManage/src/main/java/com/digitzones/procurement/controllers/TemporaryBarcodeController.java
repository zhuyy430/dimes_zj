package com.digitzones.procurement.controllers;
import com.digitzones.model.Employee;
import com.digitzones.model.User;
import com.digitzones.procurement.model.TemporaryBarcode;
import com.digitzones.procurement.service.ITemporaryBarcodeDetailService;
import com.digitzones.procurement.service.ITemporaryBarcodeService;
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
 * 临时条码控制器
 */
@RestController
@RequestMapping("temporaryBarcode")
public class TemporaryBarcodeController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "LSTM-";
    @Autowired
    private ITemporaryBarcodeService temporaryBarcodeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITemporaryBarcodeDetailService temporaryBarcodeDetailService;
    @Autowired
    private IEmployeeService employeeService;
    /**
     * 生成默认申请单号
     * @return
     */
    @RequestMapping("/generateRequestNo.do")
    public String generateRequestNo(){
        Date now = new Date();
        String formNo = temporaryBarcodeService.queryMaxRequestNoByBillDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "001";
        }
        return requestNo;
    }
    /**
     * 根据临时条码号查找临时条码信息
     * @param formNo 临时条码号
     * @return
     */
    @RequestMapping("/queryByNo.do")
    public TemporaryBarcode queryByNo(String formNo){
        return  temporaryBarcodeService.queryByProperty("formNo",formNo);
    }
    /**
     * 审核临时条码
     * @param formNo 申请单号
     */
    @RequestMapping("/audit.do")
    public void audit(String formNo, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        Employee employee = loginUser.getEmployee();
        TemporaryBarcode form =  temporaryBarcodeService.queryByProperty("formNo",formNo);
        form.setAuditDate(new Date());
        form.setAuditStatus("已审核");
        if(employee!=null) {
            form.setAuditorCode(employee.getCode());
            form.setAuditorName(employee.getName());
        }else{
            form.setAuditorCode(loginUser.getId() + "");
            form.setAuditorName(loginUser.getUsername());
        }
        temporaryBarcodeService.audit(form);
    }
    /**
     * 反审核临时条码
     * @param formNo 申请单号
     */
    @RequestMapping("/unaudit.do")
    public ModelMap unaudit(String formNo){
        ModelMap modelMap = new ModelMap();
        TemporaryBarcode form =  temporaryBarcodeService.queryByProperty("formNo",formNo);
        form.setAuditDate(null);
        form.setAuditStatus("未审核");
        form.setAuditorCode(null);
        form.setAuditorName(null);
        temporaryBarcodeService.unaudit(form);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","操作成功!");
        return modelMap;
    }
    /**
     * 根据临时条码号删除申请单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByNo.do")
    public ModelMap deleteByNo(String formNo){
        ModelMap modelMap = new ModelMap();
        TemporaryBarcode form = temporaryBarcodeService.queryByProperty("formNo", formNo);
        if(form.getAuditStatus().equals("已审核")){
            modelMap.addAttribute("statusCode",300);
            modelMap.addAttribute("message","已审核记录，不允许删除!");
            return modelMap;
        }
        temporaryBarcodeService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
