package com.digitzones.app.pda.controller;

import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.model.User;
import com.digitzones.model.WorkSheet;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.vo.BoxBarVO;
import com.digitzones.service.*;
import com.digitzones.util.JwtTokenUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
/**
 * pda生产领料控制器
 */
@Controller
@RequestMapping("/AppProductionConsumption")
public class AppProductionConsumptionController {
    @Autowired
    private IMaterialRequisitionService materialRequisitionService;
    @Autowired
    private IMaterialRequisitionDetailService materialRequisitionDetailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IWorkSheetService workSheetService;
    @Autowired
    private IInventoryService inventoryService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取扫描的箱子的内容(no)
     */
    @RequestMapping("/queryMaterialRequisitionDetailByBarCode.do")
    @ResponseBody
    public ModelMap queryMaterialRequisitionDetailByBarCode(String BarCode,String workSheetNo){
        ModelMap modelMap=new ModelMap();
        if(workSheetNo!=null&&!workSheetNo.equals("")){
            MaterialRequisitionDetail mrd=materialRequisitionDetailService.queryByProperty("barCode",BarCode);
            if(!mrd.getMaterialRequisition().getWorkSheet().getNo().equals(workSheetNo)){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","工单单号不一致，请单独开单。");
                return modelMap;
            }else{
                modelMap.addAttribute("success",true);
                modelMap.addAttribute("materialRequisitionDetail",mrd);
                return modelMap;
            }
        }else{
            MaterialRequisitionDetail mrd=materialRequisitionDetailService.queryByProperty("barCode",BarCode);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("materialRequisitionDetail",mrd);
            return modelMap;
        }
    }



    /**
     * 获取扫描的箱子内容(生产领料)
     **/
    @RequestMapping("/queryBoxBarByBarCode.do")
    @ResponseBody
    public ModelMap queryBoxBarByBarCode(Long BarCode,String workSheetNo,Long firstBarCode){
        if(firstBarCode==null || firstBarCode==0){
            firstBarCode = BarCode;
        }
        ModelMap modelMap = new ModelMap();
        BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);

        WorkSheet workSheet=workSheetService.queryByProperty("no",workSheetNo);
        if(b!=null){
            if(workSheet!=null){
                if(workSheet.getMoallocateInvcode()!=null&&!workSheet.getMoallocateInvcode().equals("")){
                    if(!workSheet.getMoallocateInvcode().equals(b.getInventoryCode())){
                        modelMap.addAttribute("success",false);
                        modelMap.addAttribute("msg"," 扫描的物料不属于当前工单子件。");
                        return modelMap;
                    }
                }
                if(!workSheet.getStoveNumber().equals(b.getFurnaceNumber())){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg"," 扫描的物料与当前工单的材料编码不同。");
                    return modelMap;
                }
                /*if(workSheet.getLocationCodes()!=null&&!workSheet.getLocationCodes().equals(b.getPositonCode())){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg"," 扫描物料的货位与当前工单的货位不同。");
                    return modelMap;
                }*/
            }
            if(b.getBoxBarType().equals("报工单")){
                BoxBar firstb=boxBarService.queryBoxBarBybarCode(firstBarCode);
                JobBookingFormDetail firstj=jobBookingFormDetailService.queryObjById(firstb.getFkey());
                JobBookingFormDetail j=jobBookingFormDetailService.queryObjById(b.getFkey());
                if(!firstj.getAllClassCodes().equals(j.getAllClassCodes())){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","该物料不属于当前班次。");
                    return modelMap;
                }else{
                    if(firstj.getForJobBookingDate()!=null ) {
                        if (!firstj.getForJobBookingDate().equals(j.getForJobBookingDate())) {
                            modelMap.addAttribute("success", false);
                            modelMap.addAttribute("msg", "该物料不属于当前班次。");
                            return modelMap;
                        }
                    }
                }
            }
            if(b.getEnterWarehouse()){
                BoxBarVO vo = model2VO(b);
                Inventory inventory=inventoryService.queryByProperty("code",b.getInventoryCode());
                vo.setDefaultWarehouseCode(inventory.getcDefWareHouse());
                modelMap.addAttribute("success",true);
                modelMap.addAttribute("box",vo);
            }else{
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","该箱未入库");
            }
        }else{
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg","该箱号不存在");
        }

        return modelMap;

    }
    /**
     * 将领域模型转换为值对象
     */
    private BoxBarVO model2VO(BoxBar boxBar) {
        if(boxBar == null) {
            return null;
        }
        BoxBarVO vo=new BoxBarVO();
        BeanUtils.copyProperties(boxBar,vo);
        vo.setBatchNumber(boxBar.getBatchNumber());
        if(boxBar.getBoxBarType().equals("采购入库单")){
            WarehousingApplicationFormDetail warehousingApplicationFormDetail =  warehousingApplicationFormDetailService.queryObjById(boxBar.getFkey());
            WarehousingApplicationFormDetail w=warehousingApplicationFormDetailService.queryByProperty("id",warehousingApplicationFormDetail.getId());
            vo.setWarehousingDate(sdf.format(w.getWarehousingApplicationForm().getReceivingDate()));
            vo.setSpecificationType(w.getSpecificationType());
           // vo.setBatchNumber(w.getBatchNumber());
            vo.setFurnaceNumber(w.getFurnaceNumber());
        }else if(boxBar.getBoxBarType().equals("报工单")){
            JobBookingFormDetail jobBookingFormDetail=jobBookingFormDetailService.queryObjById(boxBar.getFkey());
            //vo.setWarehousingDate(sdf.format(jobBookingFormDetail.getJobBookingForm().getReceivingDate()));
            vo.setSpecificationType(jobBookingFormDetail.getSpecificationType());
           //vo.setBatchNumber(jobBookingFormDetail.getBatchNumber());
            vo.setFurnaceNumber(jobBookingFormDetail.getFurnaceNumber());
        }

        return vo;
    }
    /**
     * 新增领料单
     */
    @RequestMapping("/addMaterialRequisition.do")
    @ResponseBody
    public ModelMap addMaterialRequisition(String workSheetNo,String BarCodes,String receiveAmounts,String warehouseCode,String posCodes,HttpServletRequest request){
        ModelMap modelMap=new ModelMap();
        String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
        User user  = userService.queryByProperty("username", username);
        try {
            materialRequisitionService.outWarehouse(user,workSheetNo,BarCodes,receiveAmounts,posCodes,warehouseCode);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("msg","领料单添加成功");
        }catch (Exception e){
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg",e.getMessage());
        }
        return modelMap;
    }
}
