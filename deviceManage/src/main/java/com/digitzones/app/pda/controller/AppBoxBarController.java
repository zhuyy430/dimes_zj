package com.digitzones.app.pda.controller;

import com.digitzones.app.vo.BoxBarTackoutVO;
import com.digitzones.model.DeliveryPlanDetailBoxBarMapping;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.vo.BoxBarVO;
import com.digitzones.service.IDeliveryPlanDetailBoxBarMappingService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IMaterialRequisitionDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AppBoxBar")
public class AppBoxBarController {
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IMaterialRequisitionDetailService materialRequisitionDetailService;
    @Autowired
    private IDeliveryPlanDetailBoxBarMappingService deliveryPlanDetailBoxBarMappingService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     *  查询箱号条码信息
     * @param barCode
     * @return
     */
    @RequestMapping("/queryDetailByBarCode.do")
    @ResponseBody
    public ModelMap queryDetailByBarCode(Long barCode){
        ModelMap modelMap=new ModelMap();
        BoxBar boxBar=boxBarService.queryBoxBarBybarCode(barCode);
        if(boxBar!=null){
            BoxBarVO vo=model2VO(boxBar);
            vo.setWarehousingDate(sdf.format(boxBar.getWarehouseDate()));


            BoxBarTackoutVO bFirst=new BoxBarTackoutVO();
            List<BoxBarTackoutVO> btVoList=new ArrayList<>();

            if(boxBar.getBoxBarType().equals("采购入库单")){
                WarehousingApplicationFormDetail warehousingApplicationFormDetail=warehousingApplicationFormDetailService.queryObjById(boxBar.getFkey());
                bFirst.setDate(warehousingApplicationFormDetail.getWarehousingApplicationForm().getFormDate());
                bFirst.setDateToString(sdf.format(warehousingApplicationFormDetail.getWarehousingApplicationForm().getFormDate()));
                bFirst.setNum(boxBar.getAmountOfPerBox().toString());
                bFirst.setType(boxBar.getBoxBarType());
                btVoList.add(bFirst);

                List<MaterialRequisitionDetail> mList=materialRequisitionDetailService.queryByBarCode(boxBar.getBarCode().toString());
                for (MaterialRequisitionDetail m:mList){
                    BoxBarTackoutVO bTackOut=new BoxBarTackoutVO();
                    bTackOut.setDate(m.getMaterialRequisition().getPickingDate());
                    bTackOut.setDateToString(sdf.format(m.getMaterialRequisition().getPickingDate()));
                    bTackOut.setNum("-"+boxBar.getAmount());
                    bTackOut.setType("生产领料单");
                    btVoList.add(bTackOut);
                }


            }else if(boxBar.getBoxBarType().equals("报工单")){
                JobBookingFormDetail jobBookingFormDetail=jobBookingFormDetailService.queryObjById(boxBar.getFkey());
                bFirst.setDate(jobBookingFormDetail.getJobBookingForm().getJobBookingDate());
                bFirst.setDateToString(sdf.format(jobBookingFormDetail.getJobBookingForm().getJobBookingDate()));
                bFirst.setNum(boxBar.getAmountOfPerBox().toString());
                bFirst.setType(boxBar.getBoxBarType());
                btVoList.add(bFirst);

                List<MaterialRequisitionDetail> mList=materialRequisitionDetailService.queryByBarCode(boxBar.getBarCode().toString());
                for (MaterialRequisitionDetail m:mList){
                    BoxBarTackoutVO bTackOut=new BoxBarTackoutVO();
                    bTackOut.setDate(m.getMaterialRequisition().getPickingDate());
                    bTackOut.setDateToString(sdf.format(m.getMaterialRequisition().getPickingDate()));
                    bTackOut.setNum("-"+boxBar.getAmount());
                    bTackOut.setType("生产领料单");
                    btVoList.add(bTackOut);
                }

                List<DeliveryPlanDetailBoxBarMapping> jList=deliveryPlanDetailBoxBarMappingService.queryDeliveryPlanDetailBoxBarMappingsBybarCode(boxBar.getBarCode().toString());
                for (DeliveryPlanDetailBoxBarMapping d:jList){
                    BoxBarTackoutVO bTackOut=new BoxBarTackoutVO();
                    bTackOut.setDate(d.getTakeOutTime());
                    bTackOut.setDateToString(sdf.format(d.getTakeOutTime()));
                    bTackOut.setNum("-"+d.getNumber());
                    bTackOut.setType("销售出库");
                    btVoList.add(bTackOut);
                }


            }

            List<BoxBarTackoutVO> fbtVoList=new ArrayList<>();
            for (int j=0;j<btVoList.size();j++){
                if(fbtVoList.size()==0){
                    fbtVoList.add(btVoList.get(j));
                }else{
                    for (int i=0;i<fbtVoList.size();i++){
                        if(btVoList.get(j).getDate().after(fbtVoList.get(i).getDate())&&(i+1)==fbtVoList.size()){
                            fbtVoList.add((i+1),btVoList.get(j));
                            break;
                        }else if(btVoList.get(j).getDate().after(fbtVoList.get(i).getDate())&&fbtVoList.get(i+1).getDate().after(btVoList.get(j).getDate())){
                            fbtVoList.add((i+1),btVoList.get(j));
                            break;
                        }
                    }
                }
            }

            modelMap.addAttribute("box",vo);
            modelMap.addAttribute("numChangeDetails",fbtVoList);
            modelMap.addAttribute("success",true);
        }else{
            modelMap.addAttribute("msg","该箱条码不存在");
            modelMap.addAttribute("success",false);
        }
        return  modelMap;
    }


    /**
     * 更新
     */
    @RequestMapping("/updateBoxbar.do")
    @ResponseBody
    public ModelMap updateBoxbar(Long barCode,String updateNum,String positonCode){
        ModelMap modelMap=new ModelMap();
        BoxBar boxBar=boxBarService.queryBoxBarBybarCode(barCode);
        boxBar.setAmountOfPerBox(Double.valueOf(updateNum));
        boxBar.setPositonCode(positonCode);
        boxBar.setPositonName(positonCode);
        boxBarService.updateObj(boxBar);
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","修改完成");
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

        return vo;
    }
}
