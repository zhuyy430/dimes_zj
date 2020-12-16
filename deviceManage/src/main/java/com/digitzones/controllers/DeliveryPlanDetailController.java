package com.digitzones.controllers;

import com.digitzones.dto.DeliveryPlanDetailDto;
import com.digitzones.dto.DeliveryPlanDetailRetrievalDto;
import com.digitzones.model.*;
import com.digitzones.service.IDeliveryPlanDetailService;
import com.digitzones.service.IDeliveryPlanService;
import com.digitzones.service.ISalesSlipService;
import com.digitzones.service.IUserService;
import com.digitzones.util.StringUtil;
import com.digitzones.vo.DeliveryPlanDetailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 发货单控制器
 */
@RestController
@RequestMapping("deliveryPlanDetail")
public class DeliveryPlanDetailController {
    /**内存中存放DeliveryPlanDetail对象List的名称*/
    public static final String DELIVERY_PLAN_LIST_NAME = "DeliveryPlanDetailList";
    /**内存中存放发货单单号的名称*/
    public static final String DELIVERY_PLAN_FORMNO = "delivery_plan_formNo";
    /**内存中存放删除的发货单id的名称*/
    public static final String DELIVERY_PLAN_DELETED_LIST = "delivery_plan_deleted_list";
    /**是否执行了只清空DELIVERY_PLAN_LIST_NAME的操作(非清空session)*/
    public static final String DELIVERY_PLAN_IS_CLEAR = "delivery_plan_is_clear";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private IDeliveryPlanDetailService deliveryPlanDetailService;
    @Autowired
    private IDeliveryPlanService deliveryPlanService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISalesSlipService salesSlipService;
    /**
     * 查询发货单详情
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryDeliveryPlanDetail.do")
    public ModelMap queryDeliveryPlanDetail(DeliveryPlanDetailRetrievalDto deliveryPlanDetailRetrievalDto,
                                            @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select detail from DeliveryPlanDetail detail inner join fetch detail.deliveryPlan form where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        try {
            if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getFrom()) && !deliveryPlanDetailRetrievalDto.getFrom().trim().equals("")){
                hql += " and detail.deliverDateOfPlan>=?" + i++;
                data.add(format.parse(deliveryPlanDetailRetrievalDto.getFrom()+ " 00:00:00"));
            }
            if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getTo())&&!deliveryPlanDetailRetrievalDto.getTo().trim().equals("")){
                hql += " and detail.deliverDateOfPlan<=?" + i++;
                data.add(format.parse(deliveryPlanDetailRetrievalDto.getTo() + " 23:59:59"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getFormNo())){
            hql += " and detail.formNo like ?" + i++;
            data.add("%"+deliveryPlanDetailRetrievalDto.getFormNo()+"%");
        }
        if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getInventoryCode())){
            hql += " and (detail.inventoryCode like ?" + i + " or detail.inventoryName like ?"+i++ + ")";
            data.add("%"+deliveryPlanDetailRetrievalDto.getInventoryCode()+"%");
        }
        if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getBatchNumber())){
            hql += " and detail.batchNumber like ?" + i++;
            data.add("%"+deliveryPlanDetailRetrievalDto.getBatchNumber()+"%");
        }
        if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getStatus())){
            hql += " and detail.status =?" +  i++;
            data.add(deliveryPlanDetailRetrievalDto.getStatus());
        }
        if(!StringUtils.isEmpty(deliveryPlanDetailRetrievalDto.getCustomerCode())){
            hql += " and (detail.customerCode like ?" +  i + " or detail.customerName like ?"+i++ + ")";
            data.add("%" + deliveryPlanDetailRetrievalDto.getCustomerCode() + "%");
        }
        hql += " order by convert(varchar(100),form.deliverDate,23) desc,form.formNo desc";
        Pager<DeliveryPlanDetail> pager = deliveryPlanDetailService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 根据发货单号查找发货单详情
     * @param formNo 发货单号
     * @param session
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public List<DeliveryPlanDetailVo> queryByFormNo(String formNo, HttpSession session){
        List<DeliveryPlanDetailVo> voList = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);;
        //查看发货单
        if(!StringUtils.isEmpty(formNo)){
            Boolean isClear = (Boolean) session.getAttribute(DELIVERY_PLAN_IS_CLEAR);
            if(isClear==null || !isClear) {
                if (CollectionUtils.isEmpty(voList)) {
                    voList = new ArrayList<>();
                    List<DeliveryPlanDetail> list = deliveryPlanDetailService.queryByDeliveryPlanNo(formNo);
                    session.setAttribute(DELIVERY_PLAN_FORMNO, formNo);
                    if (!CollectionUtils.isEmpty(list)) {
                        format.applyPattern("yyyy-MM-dd");
                        for(DeliveryPlanDetail d : list){
                            DeliveryPlanDetailVo vo = new DeliveryPlanDetailVo();
                            BeanUtils.copyProperties(d,vo);
                            if(d.getDeliverDateOfPlan()!=null){
                                vo.setDeliverDateOfPlan(format.format(d.getDeliverDateOfPlan()));
                            }
                            voList.add(vo);
                        }
                        session.setAttribute(DELIVERY_PLAN_LIST_NAME, voList);
                        format.applyPattern("yyyy-MM-dd HH:mm:ss");
                    }
                }
            }
        }
        return voList==null?new ArrayList<>():voList;
    }

    /**
     * 根据发货单Id查找发货单详情
     * @param session
     * @return
     */
    @RequestMapping("/queryById.do")
    public DeliveryPlanDetail queryById(String Id, HttpSession session){
    	return deliveryPlanDetailService.queryObjById(Id);
    }
    /**
     * 根据id删除
     * @param id 发货单详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteById.do")
    public ModelMap deleteById(String id,HttpSession session){
        String requestNo = (String) session.getAttribute(DELIVERY_PLAN_FORMNO);
        List<DeliveryPlanDetailVo> list = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        //查看发货单
        if(!StringUtils.isEmpty(requestNo)){
            List<String> deletedList = (List<String>) session.getAttribute(DELIVERY_PLAN_DELETED_LIST);
            if(CollectionUtils.isEmpty(deletedList)){
                deletedList = new ArrayList<>();
                session.setAttribute(DELIVERY_PLAN_DELETED_LIST,deletedList);
            }
            //保存已删除的id
            deletedList.add(id);
        }
        removeFromSession(list,id);
        if(list.size()<=0){
            session.setAttribute(DELIVERY_PLAN_IS_CLEAR,true);
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
    private void removeFromSession(List<DeliveryPlanDetailVo> list,String id){
        for(int i = 0;i<list.size();i++){
            DeliveryPlanDetailVo detail = list.get(i);
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
        removeWarehousingApplicationDetailFromSession(session);
    }
    /**
     * 从session中清除发货单详情信息
     * @param session
     */
    private void removeWarehousingApplicationDetailFromSession(HttpSession session){
        session.removeAttribute(DELIVERY_PLAN_LIST_NAME);
        session.removeAttribute(DELIVERY_PLAN_DELETED_LIST);
        session.removeAttribute(DELIVERY_PLAN_FORMNO);
        session.removeAttribute(DELIVERY_PLAN_IS_CLEAR);
    }

    /**
     *  保存发货单的校验方法
     * @param form 发货单对象
     * @param list 发货单详情对象
     * @return
     */
    private ModelMap checkDeliveryPlan(DeliveryPlan form, List<DeliveryPlanDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="发货单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(form.getDeliverDate()==null){
            msg = "请选择发货日期!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
            msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(DeliveryPlanDetail detail : list){
                if(detail.getAmountOfPlan()==null || detail.getAmountOfPlan()<=0){
                    msg = "计划数量值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }

                if(detail.getDeliverDateOfPlan()==null){
                    msg = "请输入计划发货日期!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }

    /**
     * 保存发货单
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/saveDeliveryPlan.do")
    public ModelMap saveDeliveryPlan(DeliveryPlan form , HttpSession session, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
        if(loginUser.getEmployee()!=null) {
            form.setAuditorCode(loginUser.getEmployee().getCode());
            form.setAuditorName(loginUser.getEmployee().getName());
        }else{
            form.setMakerCode(loginUser.getUsername());
            form.setMakerName(loginUser.getUsername());
        }
        String formNo = (String) session.getAttribute(DELIVERY_PLAN_FORMNO);
        List<DeliveryPlanDetailVo> details = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        List<DeliveryPlanDetail> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(details)){
            format.applyPattern("yyyy-MM-dd");
            for(DeliveryPlanDetailVo vo : details){
                DeliveryPlanDetail detail = new DeliveryPlanDetail();
                BeanUtils.copyProperties(vo,detail);
                if(!StringUtils.isEmpty(vo.getDeliverDateOfPlan())){
                    try {
                        detail.setDeliverDateOfPlan(format.parse(vo.getDeliverDateOfPlan()));
                    } catch (ParseException e) {
                        ModelMap modelMap = new ModelMap();
                        modelMap.addAttribute("msg","计划发货日期格式不正确!");
                        return modelMap;
                    }
                }

                list.add(detail);
            }
            format.applyPattern("yyyy-MM-dd HH:mm:ss");
        }
        ModelMap  modelMap = checkDeliveryPlan(form,list);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }

        //没有发货单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            DeliveryPlan waf = deliveryPlanService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","发货单号已被使用！");
                return modelMap;
            }
            form.setDeliverDate(new Date());
            deliveryPlanService.addDeliveryPlan(form,list);
            session.setAttribute(DELIVERY_PLAN_FORMNO,form.getFormNo());
        }else{//有发货单号，说明当前为查看操作
            List<String> deletedIds = (List<String>) session.getAttribute(DELIVERY_PLAN_DELETED_LIST);
            deliveryPlanService.addDeliveryPlan(form,list,deletedIds);
            session.removeAttribute(DELIVERY_PLAN_DELETED_LIST);
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }
    /**
     * 更新session中的数据
     * @param deliveryPlanDetail
     * @param session
     */
    @RequestMapping("/updateSession.do")
    public void updateSession(DeliveryPlanDetailDto deliveryPlanDetail, HttpSession session){
        List<DeliveryPlanDetailVo> list = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        format.applyPattern("yyyy-MM-dd");
        for(DeliveryPlanDetailVo detail : list){
            if(detail.getId().equals(deliveryPlanDetail.getId())){
                BeanUtils.copyProperties(deliveryPlanDetail,detail);
                if(deliveryPlanDetail.getDeliverDateOfPlan()!=null){
                    detail.setDeliverDateOfPlan(format.format(deliveryPlanDetail.getDeliverDateOfPlan()));
                }
                break;
            }
        }
        format.applyPattern("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 清空session中DELIVERY_PLAN_LIST_NAME，如果是查看，则保存清空的发货单详情id
     * @param session
     */
    @RequestMapping("/clearDeliveryPlanDetailList.do")
    public void clearDeliveryPlanDetailList(HttpSession session){
        String requestNo = (String) session.getAttribute(DELIVERY_PLAN_FORMNO);
        List<DeliveryPlanDetailVo> list = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        if(list==null){
            list = new ArrayList<>();
            session.setAttribute(DELIVERY_PLAN_LIST_NAME,list);
        }
        if(StringUtils.isEmpty(requestNo)){
            if(!CollectionUtils.isEmpty(list)){
                list.clear();
            }
        }else{
            List<String> deletedIds = (List<String>) session.getAttribute(DELIVERY_PLAN_DELETED_LIST);
            if(CollectionUtils.isEmpty(deletedIds)){
                deletedIds = new ArrayList<>();
                session.setAttribute(DELIVERY_PLAN_DELETED_LIST,deletedIds);
            }
            if(!CollectionUtils.isEmpty(list)){
                for(DeliveryPlanDetailVo detail : list){
                    deletedIds.add(detail.getId());
                }
                list.clear();
            }
        }
        session.setAttribute(DELIVERY_PLAN_IS_CLEAR,true);
    }
    /**
     * 将ids表示的订单详情存入session
     * @param ids  销售订单主键
     * @param session
     */
    @RequestMapping("/addDeliveryPlanDetails2Session.do")
    @ResponseBody
    public void addDeliveryPlanDetails2Session(String ids ,HttpSession session){
        ids = StringUtil.remove(ids,"[");
        ids = StringUtil.remove(ids,"]");
        ids = StringUtil.remove(ids,"\"");
        List<Integer> idsList = new ArrayList<>();
        if(!StringUtils.isEmpty(ids)){
            String[] idsArray = ids.split(",");
            if(idsArray!=null&&idsArray.length>0){
                for(String id : idsArray){
                    idsList.add(Integer.parseInt(id));
                }
            }
        }
        //根据id查找销售订单
        List<SalesSlip> detailsList = salesSlipService.queryByIds(idsList);
        //从session中取出发货单详情列表
        List<DeliveryPlanDetailVo> list = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        format.applyPattern("yyyy-MM-dd");
        if(list == null){
            list = new ArrayList<>();
            session.setAttribute(DELIVERY_PLAN_LIST_NAME,list);
            for(SalesSlip detail:detailsList){
                list.add(copyProperties(detail));
            }
        }else{
            for(SalesSlip detail:detailsList){
                boolean isExist = false;
                for(DeliveryPlanDetailVo formDetail : list){
                    String formDetailId = formDetail.getId();
                    String[] array = formDetailId.split("-");
                    String autoId = array[array.length-1];
                    if(String.valueOf(detail.getAutoId()).equals(autoId)){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    list.add(copyProperties(detail));
                }
            }
        }
        format.applyPattern("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 将销售订单中的属性拷贝到发货详情
     * @param detail
     * @return
     */
    private DeliveryPlanDetailVo copyProperties(SalesSlip detail) {
        DeliveryPlanDetailVo deliveryPlanDetail = new DeliveryPlanDetailVo();
        deliveryPlanDetail.setId(UUID.randomUUID().toString()+ "-" + detail.getAutoId());
        deliveryPlanDetail.setAutoId(detail.getAutoId());
        deliveryPlanDetail.setFormNo(detail.getFormNo());
        deliveryPlanDetail.setCustomerCode(detail.getCustomerCode());
        deliveryPlanDetail.setCustomerName(detail.getCustomerName());
        deliveryPlanDetail.setInventoryCode(detail.getInventoryCode());
        deliveryPlanDetail.setInventoryName(detail.getInventoryName());
        deliveryPlanDetail.setBatchNumber(detail.getBatchNumber());
        if(detail.getPreDate()!=null) {
            deliveryPlanDetail.setDeliverDateOfPlan(format.format(detail.getPreDate()));
        }
        deliveryPlanDetail.setSpecificationType(detail.getSpecificationType());
        deliveryPlanDetail.setAmountOfPlan(detail.getQuantity());
        return deliveryPlanDetail;
    }
    /**
     * 拆分行
     * @param id
     * @param session
     */
    @RequestMapping("/splitLine")
    public void splitLine(String id,HttpSession session){
        List<DeliveryPlanDetailVo> voList = (List<DeliveryPlanDetailVo>) session.getAttribute(DELIVERY_PLAN_LIST_NAME);
        String formNo = (String) session.getAttribute(DELIVERY_PLAN_FORMNO);

        DeliveryPlanDetailVo removedVo = null;
        for(int i = 0;i<voList.size();i++){
            DeliveryPlanDetailVo vo = voList.get(i);
            if(vo.getId().equals(id)){
                removedVo = voList.remove(i);
                break;
            }
        }
        if(removedVo!=null){
            //查看，记录删除的id
            if(!StringUtils.isEmpty(formNo)){
                List<String> deletedIds = (List<String>) session.getAttribute(DELIVERY_PLAN_DELETED_LIST);
                if(CollectionUtils.isEmpty(deletedIds)){
                    deletedIds = new ArrayList<>();
                    session.setAttribute(DELIVERY_PLAN_DELETED_LIST,deletedIds);
                }
                deletedIds.add(removedVo.getId());
            }

            //拆分
            Double amountOfPlan = removedVo.getAmountOfPlan();
            DeliveryPlanDetailVo vo1 = new DeliveryPlanDetailVo();
            DeliveryPlanDetailVo vo2 = new DeliveryPlanDetailVo();

            voList.add(vo1);
            voList.add(vo2);

            BeanUtils.copyProperties(removedVo,vo1);
            BeanUtils.copyProperties(removedVo,vo2);

            vo1.setId(UUID.randomUUID().toString());
            vo2.setId(UUID.randomUUID().toString());

            vo1.setAmountOfPlan(amountOfPlan/2);
            vo2.setAmountOfPlan(amountOfPlan/2);
        }
    }
}
