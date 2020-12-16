package com.digitzones.procurement.controllers;

import com.digitzones.controllers.JobBookingFormDetailController;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.ITemporaryBarcodeDetailService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.vo.BoxBarVO;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.util.QREncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 箱号条码控制器
 */
@RestController
@RequestMapping("boxBar")
public class BoxBarController {
    /**箱号条码的初始值*/
    public static final long START_BOXBAR_CODE = 100000;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private ITemporaryBarcodeDetailService temporaryBarcodeDetailService;
    private QREncoder qrEncoder = new QREncoder();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    /**
     *  查询箱号条码信息
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("queryTemporaryBarcodeBoxBars.do")
    public ModelMap queryTemporaryBarcodeBoxBars(HttpSession session, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String formNo = (String) session.getAttribute(TemporaryBarcodeDetailController.FORMNO);
        String hql = "from BoxBar bar where bar.formNo=?0";
        Pager<BoxBar> pager = boxBarService.queryObjs(hql,page,rows,new Object[]{formNo});
        modelMap.addAttribute("rows",pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     *  查询箱号条码信息
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("queryBoxBars.do")
    public ModelMap queryBoxBars(HttpSession session, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String formNo = (String) session.getAttribute(WarehousingApplicationFormDetailController.FORMNO);
        String hql = "from BoxBar bar where bar.formNo=?0";
        Pager<BoxBar> pager = boxBarService.queryObjs(hql,page,rows,new Object[]{formNo});
        modelMap.addAttribute("rows",pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     *  查询箱号条码信息
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("queryJobBookingDetailBoxBars.do")
    public ModelMap queryJobBookingDetailBoxBars(HttpSession session, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String formNo = (String) session.getAttribute(JobBookingFormDetailController.JOB_BOOKING_FORMNO);
        String hql = "from BoxBar bar where bar.formNo=?0";
        Pager<BoxBar> pager = boxBarService.queryObjs(hql,page,rows,new Object[]{formNo});

        List<BoxBarVO> vos = new ArrayList<>();
        for(int i = 0 ;i<pager.getData().size();i++) {
            BoxBar boxBar = pager.getData().get(i);
            BoxBarVO vo = model2VO(boxBar);
            //WarehousingApplicationFormDetail w=warehousingApplicationFormDetailService.queryByProperty("id",boxBar.getFkey());
            if(boxBar.getWarehouseDate()!=null){
                vo.setWarehousingDate(sdfDate.format(boxBar.getWarehouseDate()));
            }
            if(boxBar.getRequisitionDate()!=null){
                vo.setRequisitionDate(sdfDate.format(boxBar.getRequisitionDate()));
            }
            vos.add(vo);
        }




        modelMap.addAttribute("rows",vos);
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     *  查询箱号条码信息
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("queryDeliveryPlanDetailBoxBars.do")
    public ModelMap queryDeliveryPlanDetailBoxBars(String deliveryPlanDetailId, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String hql = "from BoxBar bar where bar.fkey=?0";
        Pager<BoxBar> pager = boxBarService.queryObjs(hql,page,rows,new Object[]{deliveryPlanDetailId});
        modelMap.addAttribute("rows",pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }

    /**
     *  查询箱号条码信息
     * @param formNo
     * @param tablename
     * @return
     */
    @RequestMapping("queryBoxBarsByFormNo.do")
    public List<BoxBar> queryBoxBarsByFormNo(String formNo,String tablename){
    	return boxBarService.queryBoxBarByFormNo(tablename, formNo);
    }
    /**
     *  查询箱号条码信息
     * @return
     */
    @RequestMapping("queryBoxBarsByWorkSheetNo.do")
    public List<BoxBar> queryBoxBarsByWorkSheetNo(String workSheetNo){
    	return boxBarService.queryBoxBarByWorkSheetNo(workSheetNo);
    }
    /**
     *  查询箱号条码信息
     * @param barCode
     * @return
     */
    @RequestMapping("queryBoxBarsByBarCode.do")
    public BoxBar queryBoxBarsByBarCode(Long barCode){
    	return boxBarService.queryBoxBarBybarCode(barCode);
    }
    /**
     * 根据箱条码查找箱条码信息
     * @param barCode 箱条码
     * @return
     */
    @RequestMapping("queryByBarCode.do")
    public BoxBar queryByBarCode(Long barCode){
        return boxBarService.queryObjById(barCode);
    }

    /**
     * 打印箱号的二维码
     * @param formNo 设备id字符串
     * @return
     */
    @RequestMapping("/printQr.do")
    @ResponseBody
    public List<BoxBarVO> printQr(String formNo, HttpServletRequest request, HttpSession session) {
        //String formNo = (String) session.getAttribute(WarehousingApplicationFormDetailController.FORMNO);
        List<BoxBar> list=boxBarService.queryBoxBarByFormNo("WarehousingApplicationFormDetail",formNo);
        String dir = request.getServletContext().getRealPath("/");
        List<BoxBarVO> vos = new ArrayList<>();
        for(int i = 0 ;i<list.size();i++) {
            BoxBar boxBar = list.get(i);
            BoxBarVO vo = model2VO(boxBar);
            vo.setQrPath(qrEncoder.generatePR(boxBar.getBarCode().toString(),dir, "console/qr/"));
            WarehousingApplicationFormDetail w=warehousingApplicationFormDetailService.queryByProperty("id",boxBar.getFkey());
            vo.setWarehousingDate(sdfDate.format(w.getWarehousingApplicationForm().getReceivingDate()));
            vo.setSpecificationType(w.getSpecificationType());
            vo.setBatchNumber(w.getBatchNumber());
            vo.setFurnaceNumber(w.getFurnaceNumber());
            vo.setStoveNumber(w.getStoveNumber());
            vo.setManufacturer(w.getManufacturer());
            vos.add(vo);
        }
        return vos;
    }
    /**
     * 打印箱号的二维码
     * @param ids 设备id字符串
     * @return
     */
    @RequestMapping("/printTemporaryBarcodeQr.do")
    @ResponseBody
    public List<BoxBarVO> printTemporaryBarcodeQr(String ids, HttpServletRequest request, HttpSession session) {
        String formNo = (String) session.getAttribute(TemporaryBarcodeDetailController.FORMNO);
        List<BoxBar> list=boxBarService.queryBoxBarByFormNo("TemporaryBarcodeDetail",formNo);
        String dir = request.getServletContext().getRealPath("/");
        List<BoxBarVO> vos = new ArrayList<>();
        for(int i = 0 ;i<list.size();i++) {
            BoxBar boxBar = list.get(i);
            BoxBarVO vo = model2TemporaryBarcodeVO(boxBar);
            vo.setQrPath(qrEncoder.generatePR(boxBar.getBarCode().toString(),dir, "console/qr/"));
            vos.add(vo);
        }
        return vos;
    }
    /**
     * 打印报工单箱号的二维码
     * @return
     */
    @RequestMapping("/printJobBookingQr.do")
    @ResponseBody
    public List<BoxBarVO> printJobBookingQr(String formNo, HttpServletRequest request, HttpSession session) {
        //String formNo = (String) session.getAttribute(JobBookingFormDetailController.JOB_BOOKING_FORMNO);
        List<BoxBar> list=boxBarService.queryBoxBarByFormNo("JobBookingFormDetail",formNo);
        String dir = request.getServletContext().getRealPath("/");
        List<BoxBarVO> vos = new ArrayList<>();
        for(int i = 0 ;i<list.size();i++) {
            BoxBar boxBar = list.get(i);
            BoxBarVO vo = model2JobBBookingVO(boxBar);
            vo.setQrPath(qrEncoder.generatePR(boxBar.getBarCode().toString(),dir, "console/qr/"));
            JobBookingFormDetail j=jobBookingFormDetailService.queryObjById(boxBar.getFkey());
            vo.setJobBookingDate(sdf.format(j.getJobBookingForm().getJobBookingDate()));
            vo.setProductionLine(j.getJobBookingForm().getProductionUnitName());
            vo.setClassCode(j.getAllClassCodes());
            vo.setClassName(j.getClassName());
            vo.setEmployeeName(j.getJobBookingForm().getJobBookerName());
            vos.add(vo);
        }
        return vos;
    }
    /**
     * 将领域模型转换为值对象
     */
    private BoxBarVO model2TemporaryBarcodeVO(BoxBar boxBar) {
        if(boxBar == null) {
            return null;
        }
        BoxBarVO vo=new BoxBarVO();
        BeanUtils.copyProperties(boxBar,vo);
        TemporaryBarcodeDetail w=temporaryBarcodeDetailService.queryByProperty("id",boxBar.getFkey());
        vo.setWarehousingDate(sdf.format(w.getTemporaryBarcode().getBillDate()));
        vo.setSpecificationType(w.getSpecificationType());
        vo.setBatchNumber(w.getBatchNumber());
        vo.setFurnaceNumber(w.getFurnaceNumber());
        vo.setAmountOfPerBox(w.getAmountOfPerBox());
        vo.setAmount(w.getAmount());
        return vo;
    }
    /**
     * 将领域模型转换为值对象
     */
    private BoxBarVO model2JobBBookingVO(BoxBar boxBar) {
        if(boxBar == null) {
            return null;
        }
        BoxBarVO vo=new BoxBarVO();
        BeanUtils.copyProperties(boxBar,vo);
        JobBookingFormDetail w=jobBookingFormDetailService.queryByProperty("id",boxBar.getFkey());
        if (w.getJobBookingForm()!=null) {
            if(w.getJobBookingForm().getJobBookingDate()!=null) {
                vo.setWarehousingDate(sdf.format(w.getJobBookingForm().getJobBookingDate()));
            }
        }
        vo.setSpecificationType(w.getSpecificationType());
        vo.setBatchNumber(w.getBatchNumber());
        vo.setFurnaceNumber(w.getFurnaceNumber());
        vo.setAmountOfPerBox(w.getAmountOfPerBox());
        vo.setAmount(w.getAmountOfJobBooking());
        return vo;
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

    /**
     * 查询原材料是否是同一个班次
     * @param barCodes
     * @return
     */
    @RequestMapping("queryIsTheSameClass.do")
    public ModelMap queryIsTheSameClass(String barCodes){
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("success",true);
        if(StringUtils.isEmpty(barCodes)){
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg","请选择材料!");
            return modelMap;
        }
        barCodes = barCodes.replace("[","");
        barCodes = barCodes.replace("]","");
        barCodes = barCodes.replace("\"","");

        String[] barCodeArr = barCodes.split(",");
        //判断是否属于同一种单据类型
        /* int count = boxBarService.queryIsTheSameBillType(barCodeArr);
         if(count>1){
             modelMap.addAttribute("success",false);
             modelMap.addAttribute("msg","单据类型不同，请重新选择!");
             return modelMap;
         }*/

         //查询是否是报工单
        boolean result = boxBarService.queryIsTheJobBookingType(barCodeArr);
         //是报工单
         if(result){
            boolean flag = boxBarService.queryIsTheSameClass(barCodeArr);
            if(!flag){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","材料班次不同，不允许合并报工!");
                return modelMap;
            }
         }
        return modelMap;
    }
}
