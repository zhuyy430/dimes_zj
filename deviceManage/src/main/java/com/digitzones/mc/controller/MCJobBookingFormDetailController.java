package com.digitzones.mc.controller;

import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.*;
import com.digitzones.service.*;
import com.digitzones.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * 报工单控制器
 */
@RestController
@RequestMapping("mcjobBookingFormDetail")
public class MCJobBookingFormDetailController {
    /**内存中存放JobBookingFormDetail对象List的名称*/
    public static final String JOB_BOOKING_LIST_NAME = "JobBookingFormDetailList";
    /**内存中存放报工单单号的名称*/
    public static final String JOB_BOOKING_FORMNO = "formNo";
    /**内存中存放删除的报工单id的名称*/
    public static final String JOB_BOOKING_DELETED_LIST = "deletedIds";
    /**是否执行了只清空JOB_BOOKING_LIST_NAME的操作(非清空session)*/
    public static final String JOB_BOOKING_IS_CLEAR = "isClear";
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdf = new SimpleDateFormat("HH");
    private static final String FORMNO_PREFIX = "BG-";
    @Autowired
    private IJobBookingFormService jobBookingFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMCUserService mcUserService;
    @Autowired
    private IWorkSheetDetailService workSheetDetailService;
    @Autowired
    private IDeviceSiteService deviceSiteService;
    @Autowired
    private IClassesService classesService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IEmployeeService employeeService;

    /**
     *  保存报工单的校验方法
     * @param form 报工单对象
     * @param list 报工单详情对象
     * @return
     */
    private ModelMap checkJobBookingForm(JobBookingForm form, List<JobBookingFormDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="报工单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getProductionUnitCode()) || StringUtils.isEmpty(form.getProductionUnitCode())){
            msg = "请选择生产单元!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }

        if(form.getJobBookingDate()==null){
            msg = "请选择报工日期!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
           msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(JobBookingFormDetail detail : list){
                if(detail.getAmountOfJobBooking()==null || detail.getAmountOfJobBooking()<=0){
                    msg = "数量值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }

                if(detail.getAmountOfPerBox()==null || detail.getAmountOfPerBox()<=0){
                    msg = "每箱数值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }
    /**
     * 保存报工单
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/saveJobBookingForm.do")
    public ModelMap saveJobBookingForm(JobBookingForm form , HttpSession session,HttpServletRequest request,Long workSheetId,String deviceSiteCode,
    		String num,Integer boxnum,String classesCode,String checkValues,String codeValues,String idValues,String forJobBookingDate){
    	Date now = new Date();
        String formNo = jobBookingFormService.queryMaxFormNoByJobBookingDate(now);
        String requestNo = "";
        String[] values;
        String[] codes; 
        String[] ids; 
        
        if(checkValues!=null)
        	values = checkValues.replace(" ","").split(",");
        else
        	values = null;
        
        if(codeValues!=null)
        	codes = codeValues.replace(" ","").split(",");
        else
        	codes = null;
        
        if(idValues!=null)
        	ids = idValues.replace(" ","").split(",");
        else
        	ids = null;
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format2.format(now) + "000";
        }
        
        DeviceSite deviceSite = deviceSiteService.queryByProperty("code", deviceSiteCode);
        form.setDeviceSiteCode(deviceSite.getCode());
        form.setDeviceSiteName(deviceSite.getName());
        form.setProductionUnitCode(deviceSite.getDevice().getProductionUnit().getCode());
        form.setProductionUnitName(deviceSite.getDevice().getProductionUnit().getName());
        
        
		String ip = request.getRemoteAddr();
		MCUser user = mcUserService.queryLoginUserByClientIp(ip);
        User loginUser = userService.queryByProperty("username",user.getUsername());
        Employee employee = loginUser.getEmployee();
        
        WorkSheetDetail detail = workSheetDetailService.queryObjById(workSheetId);//查询工单
		//从session中取出报工单详情列表
        List<JobBookingFormDetail> list = new ArrayList<>();
        JobBookingFormDetail details = copyProperties(detail);
        Classes classes = classesService.queryByProperty("code", classesCode);
        form.setClassCode(classes.getCode());
        form.setClassName(classes.getName());
        form.setStoveNumber(details.getFurnaceNumber());
        details.setBatchNumber(form.getBatchNumber());

        if(StringUtils.isEmpty(forJobBookingDate)) {
            Long startTime = Long.valueOf(sdf.format(classes.getStartTime()));
            Long endTime = Long.valueOf(sdf.format(classes.getEndTime()));
            Long nowTime = Long.valueOf(sdf.format(new Date()));
            if (startTime > endTime) {
                if (startTime <= nowTime) {
                    details.setForJobBookingDate(format2.format(new Date()));
                } else {
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(new Date());
                    ca.add(Calendar.DAY_OF_MONTH, -1);
                    Date nowdate = ca.getTime();
                    details.setForJobBookingDate(format2.format(nowdate));
                }
            } else {
                details.setForJobBookingDate(format2.format(new Date()));
            }
        }else{
            details.setForJobBookingDate(forJobBookingDate);
        }
        details.setClassCode(classes.getCode());
        details.setClassName(classes.getName());
        details.setAmountOfPerBox(Double.parseDouble(num));
        details.setAmountOfBoxes(1d);
        details.setAmountOfJobBooking(Double.parseDouble(num));
        details.setBoxNum(boxnum);
        details.setInventoryCode(detail.getWorkSheet().getWorkPieceCode());
        details.setInventoryName(detail.getWorkSheet().getWorkPieceName());

        String allClassesCode = "";
        if(codes!=null&&codes.length>0){
            JobBookingFormDetail jobBookingFormDetail = jobBookingFormDetailService.queryByProperty("barCode", codes[0]);
            if(jobBookingFormDetail!=null){
                allClassesCode = jobBookingFormDetail.getAllClassCodes();
            }
        }
        details.setAllClassCodes(allClassesCode+classesCode);
        list.add(details);
        
        form.setWorkSheetNo(detail.getWorkSheet().getNo());
        
      //当前为新增操作
        JobBookingForm waf = jobBookingFormService.queryByProperty("formNo",requestNo);
        if(waf!=null){
        	requestNo=jobBookingFormService.queryMaxFormNo();
        	requestNo = StringUtil.increment(requestNo);
        	form.setFormNo(requestNo);
        }else{
        	form.setFormNo(requestNo);
        }
        
        ModelMap  modelMap = checkJobBookingForm(form,list);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }
        
        if(employee!=null) {
            form.setJobBookerCode(employee.getCode());
            form.setJobBookerName(employee.getName());
        }else{
            form.setJobBookerCode(loginUser.getId() + "");
            form.setJobBookerName(loginUser.getUsername());
        }
        form.setInventoryCode(detail.getWorkSheet().getWorkPieceCode());
        form.setInventoryName(detail.getWorkSheet().getWorkPieceName());
        form.setUnitType(detail.getWorkSheet().getUnitType());
        form.setFormDate(new Date());
        jobBookingFormService.addJobBookingFormAndRawMaterial(form, list, values, codes,ids);
        session.setAttribute(JOB_BOOKING_FORMNO,form.getFormNo());
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("formNo",requestNo);
        modelMap.addAttribute("msg","操作成功!");

        return modelMap;
    }

    /**
	 * 属性拷贝
	 * @param detail
	 * @return
	 */
	private JobBookingFormDetail copyProperties(WorkSheetDetail detail){
		JobBookingFormDetail formDetail = new JobBookingFormDetail();
		formDetail.setId(detail.getId()+"");
		formDetail.setNo(detail.getWorkSheet().getNo());
		formDetail.setInventoryName(detail.getWorkSheet().getWorkPieceName());
		formDetail.setInventoryCode(detail.getWorkSheet().getWorkPieceCode());
		formDetail.setSpecificationType(detail.getWorkSheet().getUnitType());
		formDetail.setProcessCode(detail.getProcessCode());
		formDetail.setProcessName(detail.getProcessName());
		formDetail.setDeviceSiteCode(detail.getDeviceSiteCode());
		formDetail.setDeviceSiteName(detail.getDeviceSiteName());
		formDetail.setUnitCode(detail.getWorkSheet().getUnitCode());
		formDetail.setUnitName(detail.getWorkSheet().getUnitName());
		formDetail.setBatchNumber(detail.getWorkSheet().getBatchNumber());
		formDetail.setFurnaceNumber(detail.getWorkSheet().getStoveNumber());
		return formDetail;
	}
}
