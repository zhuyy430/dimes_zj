package com.digitzones.app.pda.controller;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.User;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IJobBookingFormService;
import com.digitzones.service.IUserService;
import com.digitzones.util.JwtTokenUnit;
import com.digitzones.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/AppProductionWarehousing")
public class AppProductionWarehousingController {
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IBoxBarService boxBarService;

    @Autowired
    private IJobBookingFormService jobBookingFormService;
    @Autowired
    private IUserService userService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");



    /**
     * 获取入库单号
     * @return
     */
    @RequestMapping("/queryOrderSerialNumber.do")
    @ResponseBody
    public String queryOrderSerialNumber(HttpSession session){
        String WarehouseNo=boxBarService.queryMaxWarehouseNoByboxBarType("报工单");
        String requestNo = "";
        if(!StringUtils.isEmpty(WarehouseNo)){
            requestNo = StringUtil.increment(WarehouseNo);
        }else{
            requestNo = "BGRK"+ dateFormat.format(new Date()) + "000";
        }

        return requestNo;
    }


    /**
     * 获取扫描的报工单详情内容(生产入库)
     **/
    @RequestMapping("/queryJobBookingFormDetailByBarCode.do")
    @ResponseBody
    public ModelMap queryJobBookingFormDetailByBarCode(Long BarCode, String workSheetNo){
        ModelMap modelMap=new ModelMap();
        BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);
        if(b!=null){
            JobBookingFormDetail detail = jobBookingFormDetailService.queryObjById(b.getFkey());
            if(detail!=null){
                if(!b.getBoxBarType().equals("报工单")){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","没有该入库条码");
                }else if(!workSheetNo.equals(detail.getNo())&&workSheetNo!=null&&!workSheetNo.equals("")){
                    modelMap.addAttribute("msg","工单单号不一致，请单独开单。");
                    modelMap.addAttribute("success",false);
                }else if(b.getEnterWarehouse()){
                    modelMap.addAttribute("msg","该箱已入库。");
                    modelMap.addAttribute("success",false);
                }else{
                    modelMap.addAttribute("box",b);
                    modelMap.addAttribute("jobBookingFormDetail",detail);
                    modelMap.addAttribute("success",true);
                }
            }else{
                modelMap.addAttribute("msg","该箱不属于报工箱");
                modelMap.addAttribute("success",false);
            }
        }else{
            modelMap.addAttribute("msg","箱条码不存在，请重新扫描");
            modelMap.addAttribute("success",false);
        }
        return modelMap;
    }

    /**
     * 生产入库
     * @param workSheetNo 工单编号
     * @param no          入库单号
     * @param warehouseCode 仓库编码
     * @param Amounts 入库数量
     * @param BarCodes 条码
     * @param request
     * @return
     */
    @RequestMapping("/updataJobBookingFormDetail.do")
    @ResponseBody
    public ModelMap updataJobBookingFormDetail(String workSheetNo,String no,String warehouseCode,String Amounts,String BarCodes,HttpServletRequest request){
        ModelMap modelMap=new ModelMap();
        String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
        User user  = userService.queryByProperty("username", username);

        String WarehouseNo=boxBarService.queryMaxWarehouseNoByboxBarType("报工单");
        String requestNo = StringUtil.increment(WarehouseNo);
        if(WarehouseNo!=null&&!no.equals(requestNo)) {
            no=requestNo;
            modelMap.addAttribute("changeNo", "提交成功 ，单号已存在，修改单号为"+requestNo+"。");
        }
        try {
            jobBookingFormService.intoWarehouse(user,no,warehouseCode,Amounts,BarCodes,workSheetNo);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("msg","入库成功");
        }catch (RuntimeException e){
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg",e.getMessage());
            return modelMap;
        }
        return modelMap;
    }
}
