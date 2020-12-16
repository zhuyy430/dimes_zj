package com.digitzones.procurement.controllers;

import com.digitzones.model.Employee;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.procurement.dto.TemporaryBarcodeDetailDto;
import com.digitzones.procurement.dto.TemporaryBarcodeDetailRetrievalDto;
import com.digitzones.procurement.model.*;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.procurement.service.ITemporaryBarcodeDetailService;
import com.digitzones.procurement.service.ITemporaryBarcodeService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IUserService;
import com.digitzones.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 临时条码控制器
 */
@RestController
@RequestMapping("temporaryBarcodeDetail")
public class TemporaryBarcodeDetailController {
    /**内存中存放temporaryBarcodeDetail对象List的名称*/
    public static final String LIST_NAME = "temporaryBarcodeDetailList";
    /**内存中存放临时条码单号的名称*/
    public static final String FORMNO = "TemporaryBarcodeDetailFormNo";
    /**内存中存放删除的临时条码id的名称*/
    public static final String DELETED_LIST = "deletedTemporaryBarcodeDetailIds";
    /**是否执行了只清空LIST_NAME的操作(非清空session)*/
    public static final String IS_CLEAR = "TemporaryBarcodeDetailIsClear";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private ITemporaryBarcodeDetailService temporaryBarcodeDetailService;
    @Autowired
    private ITemporaryBarcodeService temporaryBarcodeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IEmployeeService employeeService;
    /**
     * 查询临时条码详情
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryTemporaryBarcodeDetail.do")
    public ModelMap querytemporaryBarcodeDetail(TemporaryBarcodeDetailRetrievalDto temporaryBarcodeDetailRetrievalDto,
                                                @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select detail from TemporaryBarcodeDetail detail inner join fetch detail.temporaryBarcode form where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        try {
        if(!StringUtils.isEmpty(temporaryBarcodeDetailRetrievalDto.getFrom()) && !temporaryBarcodeDetailRetrievalDto.getFrom().trim().equals("")){
            hql += " and form.billDate>=?" + i++;
                data.add(format.parse(temporaryBarcodeDetailRetrievalDto.getFrom()+ " 00:00:00"));
        }
        if(!StringUtils.isEmpty(temporaryBarcodeDetailRetrievalDto.getTo())&&!temporaryBarcodeDetailRetrievalDto.getTo().trim().equals("")){
            hql += " and form.billDate<=?" + i++;
            data.add(format.parse(temporaryBarcodeDetailRetrievalDto.getTo() + " 23:59:59"));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(temporaryBarcodeDetailRetrievalDto.getInventoryCode())){
            hql += " and detail.inventoryCode like ?" + i++;
            data.add("%"+temporaryBarcodeDetailRetrievalDto.getInventoryCode()+"%");
        }
        if(!StringUtils.isEmpty(temporaryBarcodeDetailRetrievalDto.getBillType())){
            hql += " and detail.billType=?" + i++;
            data.add(temporaryBarcodeDetailRetrievalDto.getBillType());
        }
        hql += " order by form.billDate desc";
        Pager<TemporaryBarcodeDetail> pager = temporaryBarcodeDetailService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 根据临时条码号查找临时条码详情
     * @param requestNo 临时条码号
     * @param session
     * @return
     */
    @RequestMapping("/queryByRequestNo.do")
    public List<TemporaryBarcodeDetail> queryByRequestNo(String requestNo, HttpSession session){
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);;
        //查看临时条码
       if(!StringUtils.isEmpty(requestNo)){
           Boolean isClear = (Boolean) session.getAttribute(IS_CLEAR);
           if(isClear==null || !isClear) {
               if (CollectionUtils.isEmpty(list)) {
                   list = temporaryBarcodeDetailService.queryByTemporaryBarcodeNo(requestNo);
                   session.setAttribute(FORMNO, requestNo);
                   if (!CollectionUtils.isEmpty(list)) {
                       session.setAttribute(LIST_NAME, list);
                   }
               }
           }
       }
        return list==null?new ArrayList<>():list;
    }
    /**
     * 根据id删除
     * @param id 临时条码详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteById.do")
    public ModelMap deleteById(String id,HttpSession session){
        String requestNo = (String) session.getAttribute(FORMNO);
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        //查看临时条码
        if(!StringUtils.isEmpty(requestNo)){
            List<String> deletedList = (List<String>) session.getAttribute(DELETED_LIST);
            if(CollectionUtils.isEmpty(deletedList)){
               deletedList = new ArrayList<>();
               session.setAttribute(DELETED_LIST,deletedList);
            }
            //保存已删除的id
            deletedList.add(id);
        }
        removeFromSession(list,id);
        if(list.size()<=0){
            session.setAttribute(IS_CLEAR,true);
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","数据已移除!");
        return modelMap;
    }

    /**
     * 将对象从session中移除
     * @param list
     * @param id
     */
    private void removeFromSession(List<TemporaryBarcodeDetail> list,String id){
        for(int i = 0;i<list.size();i++){
            TemporaryBarcodeDetail detail = list.get(i);
            if(detail.getId().equals(id)){
                list.remove(i);
                break;
            }
        }
    }
    /**
     * 清空session
     * @param session
     */
    @RequestMapping("/clearSession.do")
    public void clearSession(HttpSession session){
        removeTemporaryBarcodeDetailFromSession(session);
    }
    /**
     * 从session中清除临时条码详情信息
     * @param session
     */
    private void removeTemporaryBarcodeDetailFromSession(HttpSession session){
        session.removeAttribute(LIST_NAME);
        session.removeAttribute(DELETED_LIST);
        session.removeAttribute(FORMNO);
        session.removeAttribute(IS_CLEAR);
    }

    /**
     *  保存临时条码的校验方法
     * @param form 临时条码对象
     * @param list 临时条码详情对象
     * @return
     */
    private ModelMap checkTemporaryBarcode(TemporaryBarcode form,List<TemporaryBarcodeDetail> list){
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="临时条码编号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
           msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(TemporaryBarcodeDetail detail : list){
                if(detail.getAmount()==null || detail.getAmount()<=0){
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
     * 保存临时条码
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/saveTemporaryBarcode.do")
    public ModelMap saveTemporaryBarcode(TemporaryBarcode form , HttpSession session, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        Employee employee = loginUser.getEmployee();
        String formNo = (String) session.getAttribute(FORMNO);
        List<TemporaryBarcodeDetail> details = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);

        ModelMap  modelMap = checkTemporaryBarcode(form,details);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }
        //没有临时条码号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            TemporaryBarcode waf = temporaryBarcodeService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","临时条码号已被使用！");
                return modelMap;
            }
            if(employee!=null) {
                form.setInputPersonCode(employee.getCode());
                form.setInputPersonName(employee.getName());
            }else{
                form.setInputPersonCode(loginUser.getId() + "");
                form.setInputPersonName(loginUser.getUsername());
            }
           form.setInputDate(new Date());
            temporaryBarcodeService.addTemporaryBarcode(form,details);
            session.setAttribute(FORMNO,form.getFormNo());
        }else{//有临时条码号，说明当前为查看操作
            List<String> deletedIds = (List<String>) session.getAttribute(DELETED_LIST);
            temporaryBarcodeService.addTemporaryBarcode(form,details,deletedIds);
            session.removeAttribute(DELETED_LIST);
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }
    /**
     * 更新session中的数据
     * @param detailDto
     * @param session
     */
    @RequestMapping("/updateSession.do")
    public void updateSession(TemporaryBarcodeDetailDto detailDto, HttpSession session){
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        for(TemporaryBarcodeDetail detail : list){
            if(detail.getId().equals(detailDto.getId())){
                BeanUtils.copyProperties(detailDto,detail);
                break;
            }
        }
    }
    /**
     * 清空session中LIST_NAME，如果是查看，则保存清空的临时条码详情id
     * @param session
     */
    @RequestMapping("/clearTemporaryBarcodeDetailList.do")
    public void clearTemporaryBarcodeDetailList(HttpSession session){
        String requestNo = (String) session.getAttribute(FORMNO);
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        if(list==null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
        }
        if(StringUtils.isEmpty(requestNo)){
            if(!CollectionUtils.isEmpty(list)){
                list.clear();
            }
        }else{
            List<String> deletedIds = (List<String>) session.getAttribute(DELETED_LIST);
           if(CollectionUtils.isEmpty(deletedIds)){
               deletedIds = new ArrayList<>();
               session.setAttribute(DELETED_LIST,deletedIds);
           }
           if(!CollectionUtils.isEmpty(list)){
               for(TemporaryBarcodeDetail detail : list){
                   deletedIds.add(detail.getId());
               }
               list.clear();
           }
        }

        session.setAttribute(IS_CLEAR,true);
    }
    /**
     * 拆箱
     * @param id
     */
    @RequestMapping("/unpacking.do")
    public void unpacking(String id,HttpSession session){
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        TemporaryBarcodeDetail unpackingDetail = null;
        //将拆箱的数据从Session中清除
        for(int i = 0;i<list.size();i++){
            TemporaryBarcodeDetail detail = list.get(i);
            if(detail.getId().equals(id)){
                unpackingDetail = list.remove(i);
                break;
            }
        }
        //将清除数据的id存入删除id列表
       List<String> deletedIds = (List<String>) session.getAttribute(DELETED_LIST);
        if(CollectionUtils.isEmpty(deletedIds)){
            deletedIds  = new ArrayList<>();
            session.setAttribute(DELETED_LIST,deletedIds);
        }
        deletedIds.add(id);
        //计算要拆的箱数
        int boxesCount = (int)Math.floor(unpackingDetail.getAmount()/unpackingDetail.getAmountOfPerBox());
        double lastBoxCount = unpackingDetail.getAmount() % unpackingDetail.getAmountOfPerBox();

        if(boxesCount>0){
            for(int i = 0;i<boxesCount;i++){
                TemporaryBarcodeDetail d = new TemporaryBarcodeDetail();
                BeanUtils.copyProperties(unpackingDetail,d);
                d.setAmount(unpackingDetail.getAmountOfPerBox());
                d.setAmountOfBoxes(1d);
                d.setId(UUID.randomUUID().toString());
                list.add(d);
            }
            if(lastBoxCount>0) {
                TemporaryBarcodeDetail d = new TemporaryBarcodeDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmount(lastBoxCount);
                d.setAmountOfBoxes(1d);
                d.setAmountOfPerBox(lastBoxCount);
                d.setId(UUID.randomUUID().toString());
                list.add(d);
            }
        }else{
            list.add(unpackingDetail);
        }
    }

    /**
     * 将codes表示的订单详情存入session
     * @param codes 物料编码
     * @param session
     */
    @GetMapping("/addTemporaryBarcodes2Session.do")
    public void addTemporaryBarcodes2Session(String codes ,HttpSession session){
        codes = StringUtil.remove(codes,"[");
        codes = StringUtil.remove(codes,"]");
        codes = StringUtil.remove(codes,"\"");
        List<String> codesList = new ArrayList<>();
        if(!StringUtils.isEmpty(codes)){
            String[] codesArray = codes.split(",");
            if(codesArray!=null&&codesArray.length>0){
                for(String code : codesArray){
                    codesList.add(code);
                }
            }
        }
        //根据id查找订单详情
        List<Inventory> detailsList = inventoryService.queryByCodes(codesList);
        //从session中取出入库申请单详情列表
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        if(list == null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
            for(Inventory inventory:detailsList){
                list.add(copyProperties(inventory));
            }
        }else{
            for(Inventory detail:detailsList){
                boolean isExist = false;
                for(TemporaryBarcodeDetail formDetail : list){
                    if(String.valueOf(detail.getCode()).equals(formDetail.getId())){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    list.add(copyProperties(detail));
                }
            }
        }
    }
    /**
     * @param barCode 条形码
     * @param billType 单据类型 0：新增，1：报工条码 2：材料条码
     * @param session
     */
    @PostMapping("/addTemporaryBarcodes2Session.do")
    public void addTemporaryBarcodes2Session(String barCode,String billType ,HttpSession session){
        //根据id查找订单详情
       BoxBar boxBar = boxBarService.queryObjById(Long.valueOf(barCode));
        //从session中取出入库申请单详情列表
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        if(list == null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
            if(boxBar!=null){
                list.add(copyProperties(boxBar,billType));
            }
        }else{
                boolean isExist = false;
                for(TemporaryBarcodeDetail formDetail : list){
                    if(String.valueOf(boxBar.getBarCode()).equals(formDetail.getId())){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    list.add(copyProperties(boxBar,billType));
                }
        }
    }

    private TemporaryBarcodeDetail copyProperties(Inventory inventory) {
        TemporaryBarcodeDetail detail = new TemporaryBarcodeDetail();
        detail.setId(inventory.getCode());
        detail.setUnit(inventory.getMeasurementUnit());
        detail.setInventoryCode(inventory.getCode());
        detail.setInventoryName(inventory.getName());
        detail.setSpecificationType(inventory.getUnitType());
        detail.setBillType("0");
        detail.setUnit(inventory.getcComUnitCode());
        return detail;
    }
    private TemporaryBarcodeDetail copyProperties(BoxBar boxBar,String billType) {
        TemporaryBarcodeDetail detail = new TemporaryBarcodeDetail();
        detail.setId(boxBar.getBarCode()+"");
    /*    switch (billType){
            //报工单
            case "1":{
                detail.setBillType("1");
                JobBookingFormDetail jobBookingFormDetail = jobBookingFormDetailService.queryObjById(boxBar.getFkey());
                if(jobBookingFormDetail!=null){
                    detail.setSpecificationType(jobBookingFormDetail.getSpecificationType());
                    detail.setFurnaceNumber(jobBookingFormDetail.getFurnaceNumber());
                    detail.setUnit(jobBookingFormDetail.getUnitName());
                }
                break;
                //临时条码单
            }case "2":{
                detail.setBillType("2");
                TemporaryBarcodeDetail temporaryBarcodeDetail =temporaryBarcodeDetailService.queryObjById(boxBar.getFkey());
                if(temporaryBarcodeDetail!=null){
                    detail.setSpecificationType(temporaryBarcodeDetail.getSpecificationType());
                    detail.setFurnaceNumber(temporaryBarcodeDetail.getFurnaceNumber());
                    detail.setUnit(temporaryBarcodeDetail.getUnit());
                }
                break;
            }
        }*/
         detail.setBillType(billType);
        detail.setSpecificationType(boxBar.getSpecificationType());
        detail.setUnit(boxBar.getUnitName());
        detail.setFurnaceNumber(boxBar.getFurnaceNumber());
        detail.setInventoryCode(boxBar.getInventoryCode());
        detail.setInventoryName(boxBar.getInventoryName());
        detail.setAmount(boxBar.getSurplusNum());
        detail.setAmountOfBoxes(boxBar.getAmountOfBoxes());
        detail.setAmountOfPerBox(boxBar.getAmountOfPerBox());
        detail.setSourceBarcode(boxBar.getBarCode()+"");
        detail.setBatchNumber(boxBar.getBatchNumber());
        return detail;
    }

    /**
     * 查看session中是否存在临时条码详情
     * @param session
     * @return
     */
    @RequestMapping("queryTemporaryBarcodeCountInSession.do")
    public Integer queryTemporaryBarcodeCountInSession(HttpSession session){
        List<TemporaryBarcodeDetail> list = (List<TemporaryBarcodeDetail>) session.getAttribute(LIST_NAME);
        return list==null?0:list.size();
    }
}
