package com.digitzones.app.pda.controller;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.DeliveryPlanDetail;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.ITemporaryBarcodeDetailService;
import com.digitzones.service.IDeliveryPlanDetailBoxBarMappingService;
import com.digitzones.service.IDeliveryPlanDetailService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.vo.DeliveryPlanDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AppSellingOutOfWarehouse")
public class AppSellingOutOfWarehouseController {
    @Autowired
    private IDeliveryPlanDetailService deliveryPlanDetailService;
    @Autowired
    private IBoxBarService boxBarService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IDeliveryPlanDetailBoxBarMappingService deliveryPlanDetailBoxBarMappingService;
    @Autowired
    private ITemporaryBarcodeDetailService temporaryBarcodeDetailService;
    /**
     * 通过分页查询发货单
     */
    @RequestMapping("/queryAllDeliveryPlanDetailBySearchCriteriaAndPage.do")
    @ResponseBody
    public ModelMap queryAllDeliveryPlanDetailBySearchCriteriaAndPage(@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page,
                      String startDate,String endDate,String formNo,String customerName,String inventoryCode,String batchNumber){
        ModelMap modelMap=new ModelMap();
        List<Object> paramList = new ArrayList<>();
        String hql="from DeliveryPlanDetail d where 1=1 ";
        int i=0;
        if(formNo!=null&&!formNo.equals("")){
            hql+=" and d.formNo=?"+(i++);
            paramList.add(formNo);
        }

        if(customerName!=null&&!customerName.equals("")){
            hql+=" and d.customerName=?"+(i++);
            paramList.add(customerName);
        }

        if(inventoryCode!=null&&!inventoryCode.equals("")){
            hql+=" and d.inventoryCode=?"+(i++);
            paramList.add(inventoryCode);
        }

        if(batchNumber!=null&&!batchNumber.equals("")){
            hql+=" and d.batchNumber=?"+(i++);
            paramList.add(batchNumber);
        }

        try {
            if(!StringUtils.isEmpty(startDate)) {
                hql += " and d.deliverDateOfPlan>=?" + (i++);
                paramList.add(format.parse(startDate+" 00:00:00"));
            }
            if(!StringUtils.isEmpty(endDate)) {
                hql += " and d.deliverDateOfPlan<=?" + (i++);
                paramList.add(format.parse(endDate+" 23:59:59"));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        hql+=" order by d.deliverDateOfPlan desc";

        Pager<DeliveryPlanDetail> pager=deliveryPlanDetailService.queryObjs(hql, page, rows, paramList.toArray());
        List<DeliveryPlanDetailVo> voList=new ArrayList<>();
        for(DeliveryPlanDetail d:pager.getData()){
            voList.add(model2VO(d));
        }

        modelMap.addAttribute("total", pager.getTotalCount());
        modelMap.addAttribute("rows", voList);
        return modelMap;
    }

    private DeliveryPlanDetailVo model2VO(DeliveryPlanDetail deliveryPlanDetail) {
        if(deliveryPlanDetail == null) {
            return null;
        }
        DeliveryPlanDetailVo vo = new DeliveryPlanDetailVo();
        BeanUtils.copyProperties(deliveryPlanDetail,vo);
        if(deliveryPlanDetail.getDeliverDateOfPlan()!=null) {
            vo.setDeliverDateOfPlan(sdf.format(deliveryPlanDetail.getDeliverDateOfPlan()));
        }
        return vo;
    }

    /**
     * 通过id查询订单
     **/
    @RequestMapping("/queryByDeliveryPlanDetailId.do")
    @ResponseBody
    public  DeliveryPlanDetailVo queryByDeliveryPlanDetailId(String deliveryPlanDetailId){
        DeliveryPlanDetail d=deliveryPlanDetailService.queryObjById(deliveryPlanDetailId);
        return  model2VO(d);
    }


   /**
     * 获取扫描的箱条码内容
     **/
    @RequestMapping("/queryBoxBar.do")
    @ResponseBody
    public ModelMap queryBoxBar(Long BarCode,String DeliveryPlanDetailId){
        ModelMap modelMap=new ModelMap();
        BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);
        DeliveryPlanDetail deliveryPlanDetail=deliveryPlanDetailService.queryObjById(DeliveryPlanDetailId);
        if(b!=null){
            if(b.getBoxBarType().equals("报工单")){
                JobBookingFormDetail detail = jobBookingFormDetailService.queryObjById(b.getFkey());
                if(detail==null){
                    modelMap.addAttribute("msg","该箱物料所对应的报工单不存在");
                    modelMap.addAttribute("success",false);
                    return modelMap;
                }



                if(!b.getEnterWarehouse()){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","该箱未入库");
                }else if(!b.getInventoryCode().equals(deliveryPlanDetail.getInventoryCode())){
                    modelMap.addAttribute("msg","该箱物料与订单不符，请重新扫描");
                    modelMap.addAttribute("success",false);
                }else if(!StringUtils.isEmpty(deliveryPlanDetail.getBatchNumber())){
                        if (!detail.getBatchNumber().equals(deliveryPlanDetail.getBatchNumber())) {
                            modelMap.addAttribute("msg", "该箱批号与订单不符，请重新扫描");
                            modelMap.addAttribute("success", false);
                        }else{
                            modelMap.addAttribute("box",b);
                            modelMap.addAttribute("jobBookingFormDetail",detail);
                            modelMap.addAttribute("success",true);
                        }

                }else{
                        modelMap.addAttribute("box",b);
                        modelMap.addAttribute("jobBookingFormDetail",detail);
                        modelMap.addAttribute("success",true);


                }
            }else if(b.getBoxBarType().equals("临时条码单")){
                TemporaryBarcodeDetail detail=temporaryBarcodeDetailService.queryObjById(b.getFkey());
                if(detail==null){
                    modelMap.addAttribute("msg","该箱物料所对应的报工单不存在");
                    modelMap.addAttribute("success",false);
                    return modelMap;
                }


                if(!b.getEnterWarehouse()){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","该箱未入库");
                }else if(!b.getInventoryCode().equals(deliveryPlanDetail.getInventoryCode())){
                    modelMap.addAttribute("msg","该箱物料与订单不符，请重新扫描");
                    modelMap.addAttribute("success",false);
                }else if(!StringUtils.isEmpty(deliveryPlanDetail.getBatchNumber())){
                        if (!detail.getBatchNumber().equals(deliveryPlanDetail.getBatchNumber())) {
                            modelMap.addAttribute("msg", "该箱批号与订单不符，请重新扫描");
                            modelMap.addAttribute("success", false);
                        }else {
                            modelMap.addAttribute("box",b);
                            modelMap.addAttribute("jobBookingFormDetail",detail);
                            modelMap.addAttribute("success",true);
                        }
                }else{
                        modelMap.addAttribute("box",b);
                        modelMap.addAttribute("jobBookingFormDetail",detail);
                        modelMap.addAttribute("success",true);
                }
            }

        }else{
            modelMap.addAttribute("msg","箱条码不存在，请重新扫描");
            modelMap.addAttribute("success",false);
        }
        return modelMap;
    }
    /**
     * 销售出库
     **/
    @RequestMapping("/updataDeliveryPlanDetail.do")
    @ResponseBody
    public ModelMap updataDeliveryPlanDetail(String DeliveryPlanDetailId,String Amounts,String BarCodes,String warehouseCode){
        ModelMap modelMap=new ModelMap();

        if(BarCodes!=null && BarCodes.contains("'")) {
            BarCodes = BarCodes.replace("'", "");
        }
        String[] barcodes= BarCodes.split(",");
        if(Amounts!=null && Amounts.contains("'")) {
            Amounts = Amounts.replace("'", "");
        }
        String[] ramounts= Amounts.split(",");
        try {
            deliveryPlanDetailService.outWarehouse(barcodes, ramounts, DeliveryPlanDetailId,warehouseCode);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("msg","出库成功");
        }catch (RuntimeException re){
            re.printStackTrace();
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg",re.getMessage());
        }
        return modelMap;
    }
}
