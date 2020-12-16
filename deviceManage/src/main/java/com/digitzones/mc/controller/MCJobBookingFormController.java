package com.digitzones.mc.controller;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.digitzones.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.Employee;
import com.digitzones.model.JobBookingForm;
import com.digitzones.model.User;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IJobBookingFormService;
import com.digitzones.service.IUserService;
/**
 * 报工单控制器
 */
@RestController
@RequestMapping("mcjobBookingForm")
public class MCJobBookingFormController {
    @Autowired
    private IJobBookingFormService jobBookingFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMCUserService mcUserService;
    @Autowired
    private IBoxBarService boxBarService;
@Autowired
    private IEmployeeService employeeService;
    /**
     * 根据报工单号和工序代码查找箱号
     * @param workNo 工单号
     * @param processCode 工序代码
     * @return
     */
    @RequestMapping("/queryBoxNo.do")
    public Integer queryBoxNo(String workNo,String processCode){
    	return  boxBarService.queryMaxBoxNumByNoAndProcessCode(workNo,processCode);
    }
    /**
     * 审核报工单
     * @param formNo 申请单号
     */
    @RequestMapping("/audit.do")
    public void audit(String formNo,HttpServletRequest request){
		String ip = request.getRemoteAddr();
		MCUser user = mcUserService.queryLoginUserByClientIp(ip);
        User loginUser = userService.queryByProperty("username",user.getUsername());
        Employee employee = loginUser.getEmployee();
        JobBookingForm form =  jobBookingFormService.queryByProperty("formNo",formNo);
        form.setAuditDate(new Date());
        form.setAuditStatus("已审核");
        if(employee!=null) {
            form.setAuditorCode(employee.getCode());
            form.setAuditorName(employee.getName());
        }else{
            form.setAuditorCode(loginUser.getId() + "");
            form.setAuditorName(loginUser.getUsername());
        }
        jobBookingFormService.audit(form);
    }

    /**
     * 根据报工单号查找报工数
     * @param workSheetNo 工单号
     * @return
     */
    @RequestMapping("/queryNumBerByWorkSheetNo.do")
    public Double queryNumBerByWorkSheetNo(String workSheetNo){
        return  jobBookingFormService.queryNumberByJobBookingForm(workSheetNo);
    }

    /**
     * 根据报工单号和当天班次查找报工数
     * @param workSheetNo 工单号
     * @return
     */
    @RequestMapping("/queryNumBerByWorkSheetNoAndClassCode.do")
    public Double queryNumBerByWorkSheetNoAndClassCode(String workSheetNo,String classesCode){
        return  jobBookingFormService.queryNumberByJobBookingFormAndClassesCode(workSheetNo,classesCode);
    }
}
