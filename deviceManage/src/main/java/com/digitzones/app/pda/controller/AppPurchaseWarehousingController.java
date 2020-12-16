package com.digitzones.app.pda.controller;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.Location;
import com.digitzones.model.User;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.vo.BoxBarVO;
import com.digitzones.service.ILocationService;
import com.digitzones.service.IUserService;
import com.digitzones.util.JwtTokenUnit;
import com.digitzones.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * pda采购入库控制器
 */
@Controller
@RequestMapping("/AppPurchaseWarehousing")
public class AppPurchaseWarehousingController {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILocationService locationService;
    /**
     * 获取入库单号
     * @return
     */
    @RequestMapping("/queryOrderSerialNumber.do")
    @ResponseBody
    public String queryOrderSerialNumber(HttpSession session){
        String WarehouseNo=boxBarService.queryMaxWarehouseNoByboxBarType("采购入库单");
        String requestNo = "";
        if(!StringUtils.isEmpty(WarehouseNo)){
            requestNo = StringUtil.increment(WarehouseNo);
        }else{
            requestNo = "CG-"+ dateFormat.format(new Date()) + "001";
        }

        return requestNo;
    }

    /**
     * 获取扫描的箱子内容(采购入库)
     **/
    @RequestMapping("/queryBoxBarByBarCode.do")
    @ResponseBody
    public ModelMap queryBoxBarByBarCode(Long BarCode,String formDetailId){
        ModelMap modelMap=new ModelMap();
        if(formDetailId!=null&&!formDetailId.equals("")){
            BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);
            if(b!=null){
                WarehousingApplicationFormDetail warehousingApplicationFormDetail = warehousingApplicationFormDetailService.queryObjById(b.getFkey());
                if(!b.getBoxBarType().equals("采购入库单")){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","没有该入库条码");
                    return modelMap;
                }



                WarehousingApplicationFormDetail w=warehousingApplicationFormDetailService.queryObjById(formDetailId);
                if(!warehousingApplicationFormDetail.getWarehouseCode().equals(w.getWarehouseCode())){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","所入仓库不同，请重新开单");
                    return modelMap;
                }else if(!warehousingApplicationFormDetail.getWarehousingApplicationForm().getVendorCode().equals(w.getWarehousingApplicationForm().getVendorCode())){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","供应商不同，请重新开单");
                    return modelMap;
                }else if(b.getEnterWarehouse()){
                    modelMap.addAttribute("msg","该箱已入库。");
                    modelMap.addAttribute("success",false);
                    return modelMap;
                }else{
                    BoxBarVO vo = model2VO(b);
                    WarehousingApplicationFormDetail wafd=warehousingApplicationFormDetailService.queryByProperty("id",warehousingApplicationFormDetail.getId());
                    modelMap.addAttribute("success",true);
                    modelMap.addAttribute("box",vo);
                    modelMap.addAttribute("warehouseReceipt",wafd);
                    return modelMap;
                }
            }else{
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","该箱号不存在");
                return modelMap;
            }
        }else{
            BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);
            if(b!=null){
                if(!b.getBoxBarType().equals("采购入库单")){
                    modelMap.addAttribute("success",false);
                    modelMap.addAttribute("msg","没有该入库条码");
                    return modelMap;
                }
                if(b.getEnterWarehouse()){
                    modelMap.addAttribute("msg","该箱已入库。");
                    modelMap.addAttribute("success",false);
                    return modelMap;
                }else{
                    WarehousingApplicationFormDetail warehousingApplicationFormDetail = warehousingApplicationFormDetailService.queryObjById(b.getFkey());
                    BoxBarVO vo = model2VO(b);
                    WarehousingApplicationFormDetail wafd=warehousingApplicationFormDetailService.queryByProperty("id",warehousingApplicationFormDetail.getId());
                    modelMap.addAttribute("success",true);
                    modelMap.addAttribute("box",vo);
                    modelMap.addAttribute("warehouseReceipt",wafd);
                    return modelMap;
                }
            }else{
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","该箱号不存在");
                return modelMap;
            }


        }
    }
    /**
     * 将领域模型转换为值对象
     */
    private BoxBarVO model2VO(BoxBar boxBar) {
        if(boxBar == null) {
            return null;
        }
        WarehousingApplicationFormDetail warehousingApplicationFormDetail = warehousingApplicationFormDetailService.queryObjById(boxBar.getFkey());
        BoxBarVO vo=new BoxBarVO();
        BeanUtils.copyProperties(boxBar,vo);
        WarehousingApplicationFormDetail w=warehousingApplicationFormDetailService.queryByProperty("id",warehousingApplicationFormDetail.getId());
        vo.setWarehousingDate(sdf.format(w.getWarehousingApplicationForm().getReceivingDate()));
        vo.setSpecificationType(w.getSpecificationType());
        vo.setBatchNumber(w.getBatchNumber());
        vo.setFurnaceNumber(w.getFurnaceNumber());
        return vo;
    }


    /**
     * 更新入库单
     * @param No        入库单号
     * @param BarCodes  箱子的集合的字符串
     * @param Amounts   箱子对应数量的集合的字符串
     * @param request
     * @return
     */
    @RequestMapping("/updataWarehousing.do")
    @ResponseBody
    public ModelMap updataWarehousing(String No,String BarCodes,String Amounts,String posCodes,HttpServletRequest request){
        ModelMap modelMap=new ModelMap();
        String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
        User user  = userService.queryByProperty("username", username);
        Date now = new Date();
        String WarehouseNo=boxBarService.queryMaxWarehouseNoByboxBarType("采购入库单");
        String requestNo = StringUtil.increment(WarehouseNo);
        if(WarehouseNo!=null&&!No.equals(requestNo)) {
            No=requestNo;
            modelMap.addAttribute("changeNo", "提交成功 ，单号已存在，修改单号为"+requestNo+"。");
        }
        if(Amounts!=null && Amounts.contains("'")) {
            Amounts = Amounts.replace("'", "");
        }
        String[] ramounts= Amounts.split(",");

        if(BarCodes!=null && BarCodes.contains("'")) {
            BarCodes = BarCodes.replace("'", "");
        }
        String[] barcodes= BarCodes.split(",");

        if(posCodes!=null && posCodes.contains("'")) {
            posCodes = posCodes.replace("'", "");
        }
        boolean end=false;
        if(posCodes.endsWith(",")){
            posCodes+="null";
            end=true;
        }

        String[] poscodes= posCodes.split(",");
        if(end){
            poscodes[poscodes.length-1]="";
        }



        for(int i=0;i<barcodes.length;i++){
            BoxBar b=boxBarService.queryBoxBarBybarCode(Long.valueOf(barcodes[i]));
            WarehousingApplicationFormDetail w= warehousingApplicationFormDetailService.queryObjById(b.getFkey());
            List<BoxBar> blist=boxBarService.queryBoxBarByFormNo("WarehousingApplicationFormDetail",b.getFormNo());



            if(b.getAmountOfPerBox()>Double.valueOf(ramounts[i])){
                w.setAmount(w.getAmount()-(b.getAmountOfPerBox()-Double.valueOf(ramounts[i])));
            }else if(b.getAmountOfPerBox()<Double.valueOf(ramounts[i])){
                w.setAmount(w.getAmount()+(Double.valueOf(ramounts[i])-b.getAmountOfPerBox()));
            }
            if(w.getAmountOfInWarehouse()==null){
                w.setAmountOfInWarehouse(Double.valueOf(ramounts[i]));
            }else{
                w.setAmountOfInWarehouse(w.getAmountOfInWarehouse()+Double.valueOf(ramounts[i]));
            }


            for(BoxBar box:blist){
                box.setAmount(w.getAmount());
                boxBarService.updateObj(box);
            }

            b.setAmount(w.getAmount());
            b.setAmountOfPerBox(Double.valueOf(ramounts[i]));
            b.setSurplusNum(Double.valueOf(ramounts[i]));
            b.setWarehouseNo(No);
            b.setWarehouseDate(now);
            if(user.getEmployee()!=null) {
                b.setEmployeeCode(user.getEmployee().getCode());
                b.setEmployeeName(user.getEmployee().getName());
            }
            b.setEnterWarehouse(true);
            b.setWarehouseCode(w.getWarehouseCode());
            b.setWarehouseName(w.getWarehouseName());

            if(poscodes[i]!=null&&!poscodes[i].equals("")){
                Location location=locationService.queryByProperty("cPosCode",poscodes[i]);
                b.setPositonCode(location.getcPosCode());
                b.setPositonName(location.getcPosName());
            }

            boxBarService.updateObj(b);
            warehousingApplicationFormDetailService.updateObj(w);
        }


        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","入库单添加成功");
        return modelMap;
    }
}
