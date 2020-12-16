package com.digitzones.procurement.controllers;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpSession;

import com.digitzones.procurement.dao.IBoxBarDao;
import com.digitzones.procurement.dao.IPO_PodetailsDao;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IEmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.Employee;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.procurement.dto.WarehousingApplicationFormDetailDto;
import com.digitzones.procurement.dto.WarehousingApplicationFormDetailRetrievalDto;
import com.digitzones.procurement.model.PO_Podetails;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IPO_PodetailsService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.procurement.service.IWarehousingApplicationFormService;
import com.digitzones.service.IUserService;
import com.digitzones.xml.ArrivalSlipUtil;
import com.digitzones.xml.model.Result;

/**
 * 入库申请单控制器
 */
@RestController
@RequestMapping("warehousingApplicationFormDetail")
public class WarehousingApplicationFormDetailController {
    /**内存中存放WarehousingApplicationFormDetail对象List的名称*/
    public static final String LIST_NAME= "WarehousingApplicationFormDetailList";
    public static final String LIST_NAME_CREATE = "WarehousingApplicationFormDetailList_create";
    public static final String LIST_NAME_QUERY = "WarehousingApplicationFormDetailList_query";
    /**内存中存放入库申请单单号的名称*/
    public static final String FORMNO = "formNo";
    /**内存中存放删除的入库申请单id的名称*/
    public static final String DELETED_LIST = "deletedIds";
    /**是否执行了只清空LIST_NAME的操作(非清空session)*/
    public static final String IS_CLEAR = "isClear";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat df = new DecimalFormat("#.00");
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IWarehousingApplicationFormService warehousingApplicationFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPO_PodetailsDao podetailsDao;
    @Autowired
    private ArrivalSlipUtil arrivalSlipUtil;
    @Autowired
    private IBoxBarDao boxBarDao;
    @Autowired
    private IEmployeeService employeeService;
    /**
     * 查询入库申请单详情
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryWarehousingApplicationFormDetail.do")
    public ModelMap queryWarehousingApplicationFormDetail(WarehousingApplicationFormDetailRetrievalDto warehousingApplicationFormDetailRetrievalDto,Boolean isOut,
            @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page,Principal principal) {
        ModelMap modelMap = new ModelMap();
        if(null!=isOut&&isOut){
            if(!"admin".equals(principal.getName()))
                warehousingApplicationFormDetailRetrievalDto.setVendor(principal.getName());
        }
        String hql = "select detail from WarehousingApplicationFormDetail detail inner join fetch detail.warehousingApplicationForm form where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        try {
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getFrom()) && !warehousingApplicationFormDetailRetrievalDto.getFrom().trim().equals("")){
            hql += " and form.receivingDate>=?" + i++;
                data.add(format.parse(warehousingApplicationFormDetailRetrievalDto.getFrom()+ " 00:00:00"));
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getTo())&&!warehousingApplicationFormDetailRetrievalDto.getTo().trim().equals("")){
            hql += " and form.receivingDate<=?" + i++;
            data.add(format.parse(warehousingApplicationFormDetailRetrievalDto.getTo() + " 23:59:59"));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getPurchasingOrderNo())){
            hql += " and detail.purchasingNo like ?" + i++;
            data.add("%"+warehousingApplicationFormDetailRetrievalDto.getPurchasingOrderNo()+"%");
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getCheckStatus())&&!"''".equals(warehousingApplicationFormDetailRetrievalDto.getCheckStatus())){
        	hql += " and detail.checkStatus in("+warehousingApplicationFormDetailRetrievalDto.getCheckStatus()+")";
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getRequestNo())){
            hql += " and form.formNo like ?" + i++;
            data.add("%"+warehousingApplicationFormDetailRetrievalDto.getRequestNo()+"%");
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getVendor())){
            hql += " and (form.vendorCode like ?" + i +" or form.vendorName like ?" + i++ +")";
            data.add("%"+warehousingApplicationFormDetailRetrievalDto.getVendor()+"%");
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getWarehouse())){
            hql += " and (form.warehouseCode like ?" + i +" or form.warehouseName like ?" + i++ +")";
            data.add("%"+warehousingApplicationFormDetailRetrievalDto.getWarehouse()+"%");
        }
        if(!StringUtils.isEmpty(warehousingApplicationFormDetailRetrievalDto.getFurnaceNumber())){
        	hql += " and (detail.furnaceNumber like ?" + i +" or detail.furnaceNumber like ?" + i++ +")";
        	data.add("%"+warehousingApplicationFormDetailRetrievalDto.getFurnaceNumber()+"%");
        }
        hql += " order by form.receivingDate desc,form.formNo desc";
        Pager<WarehousingApplicationFormDetail> pager = warehousingApplicationFormDetailService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 根据入库申请单号查找入库申请单详情
     * @param requestNo 入库申请单号
     * @param session
     * @return
     */
    @RequestMapping("/queryByRequestNo.do")
    public List<WarehousingApplicationFormDetail> queryByRequestNo(String requestNo, HttpSession session){
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);;
        //查看入库申请单
       if(!StringUtils.isEmpty(requestNo)){
           Boolean isClear = (Boolean) session.getAttribute(IS_CLEAR);
           if(isClear==null || !isClear) {
               if (CollectionUtils.isEmpty(list)) {
                   list = warehousingApplicationFormDetailService.queryByWarehouseingApplicationFormNo(requestNo);
                   session.setAttribute(FORMNO, requestNo);
                   if (!CollectionUtils.isEmpty(list)) {
                       session.setAttribute(LIST_NAME, list);
                   }
               }
           }
       }
       if(!CollectionUtils.isEmpty(list)) {
           for (WarehousingApplicationFormDetail l : list) {
               l.setAmountOfPerBox(Double.parseDouble(df.format(l.getAmountOfPerBox())));
           }
       }
        return list==null?new ArrayList<>():list;
    }
    /**
     * 根据入库申请单id查找入库申请单详情
     * @param id 入库申请单id
     * @return
     */
    @RequestMapping("/queryWarehousingApplicationFormDetailById.do")
    public WarehousingApplicationFormDetail queryWarehousingApplicationFormDetailById(String id){
    	return warehousingApplicationFormDetailService.queryObjById(id);
    }
    /**
     * 根据id删除
     * @param id 入库申请单详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteById.do")
    public ModelMap deleteById(String id,HttpSession session){
        String requestNo = (String) session.getAttribute(FORMNO);
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);
        //查看入库申请单
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
    private void removeFromSession(List<WarehousingApplicationFormDetail> list,String id){
        for(int i = 0;i<list.size();i++){
            WarehousingApplicationFormDetail detail = list.get(i);
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
    public void clearSession(HttpSession session,String source){
        removeWarehousingApplicationDetailFromSession(session);
    }
    /**
     * 从session中清除入库申请单详情信息
     * @param session
     */
    private void removeWarehousingApplicationDetailFromSession(HttpSession session){
        session.removeAttribute(LIST_NAME);
        session.removeAttribute(DELETED_LIST);
        session.removeAttribute(FORMNO);
        session.removeAttribute(IS_CLEAR);
    }

    /**
     *  保存入库申请单的校验方法
     * @param form 入库申请单对象
     * @param list 入库申请单详情对象
     * @return
     */
    private ModelMap checkWareHousingApplicationForm(WarehousingApplicationForm form,List<WarehousingApplicationFormDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="申请单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getVendorCode()) || StringUtils.isEmpty(form.getVendorName())){
            msg = "请选择供应商!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
           msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(WarehousingApplicationFormDetail detail : list){
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
                if(StringUtils.isEmpty(detail.getManufacturer())){
                    msg = "入库申请单缺少钢厂信息!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }

                if(StringUtils.isEmpty(detail.getWarehouseCode()) || "".equals(detail.getWarehouseCode().trim())){
                    msg = "入库申请单表体缺少仓库信息!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }

    /**
     * 保存入库申请单
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/saveWarehousingApplicationForm.do")
    public ModelMap saveWarehousingApplicationForm(WarehousingApplicationForm form , HttpSession session, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        Employee employee = loginUser.getEmployee();
        String formNo = (String) session.getAttribute(FORMNO);
        List<WarehousingApplicationFormDetail> details = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);

        ModelMap  modelMap = checkWareHousingApplicationForm(form,details);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }
        //没有入库申请单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            WarehousingApplicationForm waf = warehousingApplicationFormService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","入库申请单号已被使用！");
                return modelMap;
            }
            if(employee!=null) {
                form.setApplierCode(employee.getCode());
                form.setApplierName(employee.getName());
            }else{
                form.setApplierCode(loginUser.getId() + "");
                form.setApplierName(loginUser.getUsername());
            }
            form.setFormDate(new Date());
            warehousingApplicationFormService.addWarehousingApplicationForm(form,details);
            session.setAttribute(FORMNO,form.getFormNo());
        }else{//有入库申请单号，说明当前为查看操作
            List<String> deletedIds = (List<String>) session.getAttribute(DELETED_LIST);
            warehousingApplicationFormService.addWarehousingApplicationForm(form,details,deletedIds);
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
    public void updateSession(WarehousingApplicationFormDetailDto detailDto, HttpSession session){
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);
        for(WarehousingApplicationFormDetail detail : list){
            if(detail.getId().equals(detailDto.getId())){
                BeanUtils.copyProperties(detailDto,detail);
                detail.setAmountOfPerBox(detail.getAmount()/detail.getAmountOfBoxes());
                break;
            }
        }
    }
    /**
     * 清空session中LIST_NAME，如果是查看，则保存清空的入库申请单详情id
     * @param session
     */
    @RequestMapping("/clearWarehousingApplicationDetailList.do")
    public void clearWarehousingApplicationDetailList(HttpSession session){
        String requestNo = (String) session.getAttribute(FORMNO);
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);
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
               for(WarehousingApplicationFormDetail detail : list){
                   deletedIds.add(detail.getId());
               }
               list.clear();
           }
        }

        session.setAttribute(IS_CLEAR,true);
    }

    /**
     * 修改session中入库申请单详情的仓库信息
     * @param warehouseCode
     * @param warehouseName
     * @param session
     */
    @RequestMapping("/modifyWarehouseInSession.do")
    public void modifyWarehouseInSession(String warehouseCode,String warehouseName,HttpSession session){
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);
        if(!CollectionUtils.isEmpty(list)){
            for(WarehousingApplicationFormDetail detail : list){
                detail.setWarehouseCode(warehouseCode);
                detail.setWarehouseName(warehouseName);
            }
        }
    }

    /**
     * 拆箱
     * @param id
     */
    @RequestMapping("/unpacking.do")
    public void unpacking(String id,HttpSession session){
        List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(LIST_NAME);
        WarehousingApplicationFormDetail unpackingDetail = null;
        //将拆箱的数据从Session中清除
        for(int i = 0;i<list.size();i++){
            WarehousingApplicationFormDetail detail = list.get(i);
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
        int boxesCount = unpackingDetail.getAmountOfBoxes().intValue();
      //  int boxesCount = (int)Math.floor(unpackingDetail.getAmount()/unpackingDetail.getAmountOfPerBox());
       // double lastBoxCount = unpackingDetail.getAmount() % unpackingDetail.getAmountOfPerBox();
        double sum = 0;
        double amount = unpackingDetail.getAmount();
        if(boxesCount>0){
            for(int i = 0;i<boxesCount;i++){
                WarehousingApplicationFormDetail d = new WarehousingApplicationFormDetail();
                BeanUtils.copyProperties(unpackingDetail,d);
                d.setAmount(unpackingDetail.getAmountOfPerBox());
                d.setAmountOfBoxes(1d);
                d.setId(UUID.randomUUID().toString());
                if(i==boxesCount-1){
                    d.setAmount(amount-sum);
                    d.setAmountOfPerBox(d.getAmount());
                }else{
                    sum+=unpackingDetail.getAmountOfPerBox();
                }
                list.add(d);
            }
           /* if(lastBoxCount>0) {
                WarehousingApplicationFormDetail d = new WarehousingApplicationFormDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmount(lastBoxCount);
                d.setAmountOfBoxes(1d);
                d.setAmountOfPerBox(lastBoxCount);
                d.setId(UUID.randomUUID().toString());

                list.add(d);
            }*/
        }else{
            list.add(unpackingDetail);
        }
    }
    /**
     * 检验入库申请单详情
     * @return
     */
    @RequestMapping("/checkWarehousingApplicationFormDetail.do")
    public ModelMap checkWarehousingApplicationFormDetail(String status,String id) {
        if(id!=null && id.contains("'")) {
            id = id.replace("'", "");
        }
        ModelMap modelMap = new ModelMap();
        WarehousingApplicationFormDetail detail = warehousingApplicationFormDetailService.queryByProperty("id", id);
        detail.setCheckStatus(status);
        warehousingApplicationFormDetailService.updateObj(detail);
        modelMap.addAttribute("success", true);
        modelMap.addAttribute("statusCode", 200);
        modelMap.addAttribute("title", "操作提示");
        modelMap.addAttribute("msg", "操作成功!");
        modelMap.addAttribute("message", "操作成功!");
        return modelMap;
    }

    /**
     * 生成ERP到货单
     * @param formNo
     * @return
     */
    @RequestMapping("generateArrivalSlip.do")
    public Result generateArrivalSlip(String formNo){
        WarehousingApplicationForm form = warehousingApplicationFormService.queryObjById(formNo);
        List<WarehousingApplicationFormDetail> details = warehousingApplicationFormDetailService.queryByWarehouseingApplicationFormNo(formNo);
      /* List<BoxBar> boxBarList = boxBarDao.findByHQL("from BoxBar bar where tableName='WarehousingApplicationFormDetail' and " +
               " fkey in (select id from WarehousingApplicationFormDetail detail where detail.warehousingApplicationForm.formNo=?0)",new Object[]{formNo});*/
        //存放所有boxbar信息
          List<Map<PO_Podetails,List<BoxBar>>> mapList = new ArrayList<>();
        Result result = new Result();
        if("1".equals(form.getWarehousingStatus())){
            result.setStatusCode(300);
            result.setMessage("该申请单已入库,无需重复操作!");
            return result;
        }
        if(!CollectionUtils.isEmpty(details)){
            for(WarehousingApplicationFormDetail detail : details){
                if(detail.getAmountOfInWarehouse()==null||detail.getAmountOfInWarehouse()<=0){
                    result.setMessage("该物料尚未确认，不允许入库!");
                    result.setStatusCode(300);
                    return result;
                }
                //根据WarehousingApplicationFormDetail id查找BoxBar
                List<BoxBar> boxBars = boxBarDao.findByHQL("from BoxBar where fkey=?0",new Object[]{detail.getId()});
                ////根据WarehousingApplicationFormDetail id查找采购订单详情
                PO_Podetails po_podetails = podetailsDao.findById(detail.getPo_poDetailId());
                Map<PO_Podetails,List<BoxBar>>  map = new HashMap<>();
                map.put(po_podetails,boxBars);
                mapList.add(map);

                if(po_podetails==null){
                    result.setMessage("采购订单信息丢失!");
                    result.setStatusCode(300);
                    return result;
                }
            }

            result = arrivalSlipUtil.generateArrivalSlip(form,mapList);
        }
        //更改入库状态
        if(result.getStatusCode()==200){
            form.setWarehousingStatus("1");
            warehousingApplicationFormService.updateObj(form);
        }
        return result;
    }
}
