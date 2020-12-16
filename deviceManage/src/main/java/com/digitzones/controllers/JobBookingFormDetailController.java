package com.digitzones.controllers;

import com.digitzones.dto.JobBookingFormDetailDto;
import com.digitzones.dto.JobBookingFormDetailRetrievalDto;
import com.digitzones.dto.RawMaterialDto;
import com.digitzones.model.*;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IJobBookingFormService;
import com.digitzones.service.IRawMaterialService;
import com.digitzones.service.IUserService;
import com.digitzones.vo.JobBookingFormDetailVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报工单控制器
 */
@RestController
@RequestMapping("jobBookingFormDetail")
public class JobBookingFormDetailController {
    /**内存中存放JobBookingFormDetail对象List的名称*/
    public static final String JOB_BOOKING_LIST_NAME = "JobBookingFormDetailList";
    /**内存中存放报工单单号的名称*/
    public static final String JOB_BOOKING_FORMNO = "jobBookingFormNo";
    /**内存中存放删除的报工单详情id的名称*/
    public static final String JOB_BOOKING_DELETED_LIST = "deletedIds";
    /**内存中存放删除的原材料id的名称*/
    public static final String JOB_BOOKING_DELETED_MATERIAL_LIST = "deletedMaterialIds";
    /**是否执行了只清空JOB_BOOKING_LIST_NAME的操作(非清空session)*/
    public static final String JOB_BOOKING_IS_CLEAR = "isClear";
    public static final String JOB_BOOKING_IS_QUERY_DB = "isQueryDB";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IJobBookingFormService jobBookingFormService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRawMaterialService rawMaterialService;
    @Autowired
    private IBoxBarService boxBarService;
    /**
     * 查询报工单详情
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryJobBookingFormDetail.do")
    public ModelMap queryJobBookingFormDetail(JobBookingFormDetailRetrievalDto jobBookingFormDetailRetrievalDto,
                                              @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select detail from JobBookingFormDetail detail inner join fetch detail.jobBookingForm form where 1=1 ";
        List<Object> data = new ArrayList<>();
        int i = 0;
        try {
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getFrom()) && !jobBookingFormDetailRetrievalDto.getFrom().trim().equals("")){
            hql += " and form.jobBookingDate>=?" + i++;
                data.add(format.parse(jobBookingFormDetailRetrievalDto.getFrom()+ " 00:00:00"));
        }
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getTo())&&!jobBookingFormDetailRetrievalDto.getTo().trim().equals("")){
            hql += " and form.jobBookingDate<=?" + i++;
            data.add(format.parse(jobBookingFormDetailRetrievalDto.getTo() + " 23:59:59"));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getNo())){
            hql += " and detail.no like ?" + i++;
            data.add("%"+jobBookingFormDetailRetrievalDto.getNo()+"%");
        }
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getInventoryCode())){
            hql += " and detail.inventoryCode like ?" + i++;
            data.add("%"+jobBookingFormDetailRetrievalDto.getInventoryCode()+"%");
        }
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getProductionUnitCode())){
            hql += " and form.productionUnitCode like ?" + i++;
            data.add("%"+jobBookingFormDetailRetrievalDto.getProductionUnitCode()+"%");
        }
        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getBarCode())){
            hql += " and detail.barCode like ?" +  i++;
            data.add("%"+jobBookingFormDetailRetrievalDto.getBarCode()+"%");
        }

        if(!StringUtils.isEmpty(jobBookingFormDetailRetrievalDto.getBatchNo())){
            hql += " and detail.batchNumber like ?" +  i++;
            data.add("%"+jobBookingFormDetailRetrievalDto.getBatchNo()+"%");
        }

        hql += " order by form.jobBookingDate desc,form.formNo desc";
        Pager<JobBookingFormDetail> pager = jobBookingFormDetailService.queryObjs(hql,page,rows,data.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 根据报工单号查找报工单详情
     * @param formNo 报工单号
     * @param session
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public List<JobBookingFormDetail> queryByFormNo(String formNo, HttpSession session){
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);;
        //查看报工单
       if(!StringUtils.isEmpty(formNo)){
           Boolean isClear = (Boolean) session.getAttribute(JOB_BOOKING_IS_CLEAR);
           if(isClear==null || !isClear) {
               if (CollectionUtils.isEmpty(list)) {
                   list = jobBookingFormDetailService.queryByJobBookingFormNo(formNo);
                   session.setAttribute(JOB_BOOKING_FORMNO, formNo);
                   if (!CollectionUtils.isEmpty(list)) {
                       session.setAttribute(JOB_BOOKING_LIST_NAME, list);
                       Map<String,Boolean> map = (Map<String, Boolean>) session.getAttribute(JOB_BOOKING_IS_QUERY_DB);
                       if(map == null){
                           map = new HashMap<>();
                       }
                       for(JobBookingFormDetail detail : list){
                           map.put(detail.getId(),true);
                       }

                       session.setAttribute(JOB_BOOKING_IS_QUERY_DB,map);
                   }
               }
           }
       }
        return list==null?new ArrayList<>():list;
    }
    /**
     * 根据报工单号查找报工单详情
     * @return
     */
    @RequestMapping("/queryJobBookingFormDetailById.do")
    public JobBookingFormDetail queryJobBookingFormDetailById(String id){
    	JobBookingFormDetail jobBooking = jobBookingFormDetailService.queryObjById(id);
    	return jobBooking;
    }
   
    /**
     * 根据报工单号查找报工单详情
     * @return
     */
    @RequestMapping("/queryJobBookingFormDetailByBarCode.do")
    public JobBookingFormDetail queryJobBookingFormDetailByBarCode(String barCode){
    	JobBookingFormDetail jobBooking = jobBookingFormDetailService.queryByProperty("barCode", barCode);
    	return jobBooking;
    }
    /**
     * 根据id删除
     * @param id 报工单详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteById.do")
    public ModelMap deleteById(String id,HttpSession session){
        String requestNo = (String) session.getAttribute(JOB_BOOKING_FORMNO);
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        //查看报工单
        if(!StringUtils.isEmpty(requestNo)){
            List<String> deletedList = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_LIST);
            if(CollectionUtils.isEmpty(deletedList)){
               deletedList = new ArrayList<>();
               session.setAttribute(JOB_BOOKING_DELETED_LIST,deletedList);
            }
            //保存已删除的id
            deletedList.add(id);
        }
        removeFromSession(list,id);
        Map<String,Boolean> map = (Map<String, Boolean>) session.getAttribute(JOB_BOOKING_IS_QUERY_DB);
        map.remove(id);
        if(list.size()<=0){
            session.setAttribute(JOB_BOOKING_IS_CLEAR,true);
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","数据已移除!");
        return modelMap;
    }
    /**
     * 根据id删除
     * @param id 报工单详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteRawMaterialById.do")
    public ModelMap deleteRawMaterialById(String id,HttpSession session){
        String requestNo = (String) session.getAttribute(JOB_BOOKING_FORMNO);
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        //查看报工单
        if(!StringUtils.isEmpty(requestNo)){
            List<String> deletedList = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST);
            if(CollectionUtils.isEmpty(deletedList)){
               deletedList = new ArrayList<>();
               session.setAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST,deletedList);
            }
            //保存已删除的id
            deletedList.add(id);
        }
        removeMaterialFromSession(list,id);
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
    private void removeFromSession(List<JobBookingFormDetail> list,String id){
        for(int i = 0;i<list.size();i++){
            JobBookingFormDetail detail = list.get(i);
            if(detail.getId().equals(id)){
                list.remove(i);
                break;
            }
        }
    }
    /**
     * 将对象从session中移除
     * @param list
     * @param id
     */
    private void removeMaterialFromSession(List<JobBookingFormDetail> list,String id){
        for(int i = 0;i<list.size();i++){
            JobBookingFormDetail detail = list.get(i);
            List<RawMaterial> rawMaterialList = detail.getRawMaterialList();
            if(!CollectionUtils.isEmpty(rawMaterialList)){
                for(int j = 0;j< rawMaterialList.size();j++){
                    RawMaterial rawMaterial = rawMaterialList.get(j);
                    if(rawMaterial.getId().equals(id)){
                        rawMaterialList.remove(j);
                        return ;
                    }
                }
            }
        }
    }
    /**
     * 清空session
     * @param session
     */
    @RequestMapping("/clearSession.do")
    public void clearSession(HttpSession session){
        removeJobBookingDetailFromSession(session);
    }
    /**
     * 从session中清除报工单详情信息
     * @param session
     */
    private void removeJobBookingDetailFromSession(HttpSession session){
        session.removeAttribute(JOB_BOOKING_LIST_NAME);
        session.removeAttribute(JOB_BOOKING_DELETED_LIST);
        session.removeAttribute(JOB_BOOKING_FORMNO);
        session.removeAttribute(JOB_BOOKING_IS_CLEAR);
        session.removeAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST);
        session.removeAttribute(JOB_BOOKING_IS_QUERY_DB);
    }

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
                    msg = "报工数不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
               /* List<RawMaterial> rawMaterialList = detail.getRawMaterialList();
                if(CollectionUtils.isEmpty(rawMaterialList)){
                    msg = "请填写原材料信息!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }else{
                    for(RawMaterial rawMaterial : rawMaterialList){
                        if(rawMaterial.getAmountOfUsed()==null||rawMaterial.getAmountOfUsed()<0||rawMaterial.getAmountOfUsed()>rawMaterial.getAmount()){
                            msg = "消耗数量不合法，请检查该值是否存在空、负数或大于原材料总量的情况!";
                            modelMap.addAttribute("msg",msg);
                            return modelMap;
                        }
                    }
                }*/
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
    public ModelMap saveJobBookingForm(JobBookingForm form , HttpSession session, Principal principal){
        User loginUser = userService.queryByProperty("username",principal.getName());
       // Employee employee = loginUser.getEmployee();
        String formNo = (String) session.getAttribute(JOB_BOOKING_FORMNO);
        List<JobBookingFormDetail> details = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);

        ModelMap  modelMap = checkJobBookingForm(form,details);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }

        //没有报工单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            JobBookingForm waf = jobBookingFormService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","报工单号已被使用！");
                return modelMap;
            }
            if(loginUser.getEmployee()!=null) {
                form.setJobBookerCode(loginUser.getEmployee().getCode());
                form.setJobBookerName(loginUser.getEmployee().getName());
            }else{
                form.setJobBookerCode(loginUser.getId() + "");
                form.setJobBookerName(loginUser.getUsername());
            }
            form.setFormDate(new Date());
            jobBookingFormService.addJobBookingForm(form,details);
            session.setAttribute(JOB_BOOKING_FORMNO,form.getFormNo());
        }else{//有报工单号，说明当前为查看操作
            List<String> deletedIds = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_LIST);
            List<String> deletedRawMaterialIds = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST);
            jobBookingFormService.addJobBookingForm(form,details,deletedIds,deletedRawMaterialIds);
            session.removeAttribute(JOB_BOOKING_DELETED_LIST);
            session.removeAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST);
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }
    /**
     * 更新session中的数据
     * @param jobBookingFormDetail
     * @param session
     */
    @RequestMapping("/updateSession.do")
    public void updateSession(JobBookingFormDetailDto jobBookingFormDetail, HttpSession session){
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        if(list == null){
            list = new ArrayList<>();
            session.setAttribute(JOB_BOOKING_LIST_NAME,list);
        }
        boolean isExsit = false;
        for(JobBookingFormDetail detail : list){
            if(detail.getId().equals(jobBookingFormDetail.getId())){
                isExsit = true;
                detail.setAmountOfJobBooking(jobBookingFormDetail.getAmountOfJobBooking());
                break;
            }
        }
        if(!isExsit){
            JobBookingFormDetail detail = new JobBookingFormDetail();
            detail.setBoxNum(jobBookingFormDetail.getBoxNum());
            detail.setAmountOfJobBooking(jobBookingFormDetail.getAmountOfJobBooking());
            detail.setId(jobBookingFormDetail.getId());
            list.add(detail);
        }
    }

    /**
     * 根据报工详情id查找原材料信息
     * @param jobBookingDetailId
     * @param session
     * @return
     */
    @RequestMapping("queryByJobBookingDetailId.do")
    public List<RawMaterial> queryByJobBookingDetailId(String jobBookingDetailId,HttpSession session){
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        String formNo = (String) session.getAttribute(JOB_BOOKING_FORMNO);
        if(formNo!=null && jobBookingDetailId!=null){
            Map<String,Boolean> map = (Map<String, Boolean>) session.getAttribute(JOB_BOOKING_IS_QUERY_DB);
            if(map == null){
                map = new HashMap<>();
                session.setAttribute(JOB_BOOKING_IS_QUERY_DB,map);
            }
            Boolean isQueryDB= map.get(jobBookingDetailId);
            if(isQueryDB!=null&&isQueryDB) {
                map.put(jobBookingDetailId,false);
                List<RawMaterial> rawMaterialList = rawMaterialService.queryByJobBookingDetailId(jobBookingDetailId);
                if (!CollectionUtils.isEmpty(rawMaterialList)) {
                    for (JobBookingFormDetail detail : list) {
                        if (detail.getId().equals(jobBookingDetailId)) {
                            detail.setRawMaterialList(rawMaterialList);
                            return rawMaterialList;
                        }
                    }
                }
            }
        }

        for(JobBookingFormDetail detail : list){
            if(detail.getId().equals(jobBookingDetailId)){
                List<RawMaterial> retList = detail.getRawMaterialList();
                if(retList == null){
                    retList = new ArrayList<>();
                }
                return retList;
            }
        }
        return new ArrayList<>();
    }
    /**
     * 根据报工详情id更新原材料信息
     * @param session
     * @return
     */
    @RequestMapping("updateByJobBookingDetailId.do")
    public void  updateByJobBookingDetailId(RawMaterialDto rawMaterial, HttpSession session){
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        for(JobBookingFormDetail detail : list){
            
            if(detail.getId().equals(rawMaterial.getJobBookingFormDetailId())){
                boolean isExsit = false;
                //新增原材料
                List<RawMaterial> rawMaterialList = detail.getRawMaterialList();
                if(rawMaterialList == null){
                    rawMaterialList = new ArrayList<>();
                    detail.setRawMaterialList(rawMaterialList);
                }

                for(RawMaterial rm : rawMaterialList){
                    if(rm.getId().equals(rawMaterial.getId())){
                        BeanUtils.copyProperties(rawMaterial,rm);
                        isExsit = true;
                    }
                }
                if(!isExsit){
                    RawMaterial material = new RawMaterial();
                    BeanUtils.copyProperties(rawMaterial,material);
                    rawMaterialList.add(material);
                }
            }
        }
    }
    /**
     * 在session中查找最大的箱号
     * @param session
     * @param no 工单单号
     * @return
     */
    @RequestMapping("queryMaxBoxNumInSession.do")
    public ModelMap queryMaxBoxNumInSession(String no,String processCode,HttpSession session){
        ModelMap modelMap = new ModelMap();
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        //根据工单单号查找最大箱号
        int boxNum = jobBookingFormDetailService.queryMaxBoxNumByNoAndProcessCode(no,processCode);
        if(CollectionUtils.isEmpty(list)){
            modelMap.addAttribute("maxBoxNum",boxNum+1);
            modelMap.addAttribute("newId",UUID.randomUUID().toString());
            return modelMap;
        }

        int maxBoxNum = boxNum;
        String id = UUID.randomUUID().toString();
        for(JobBookingFormDetail detail : list){
            if(detail.getBoxNum()>maxBoxNum){
                maxBoxNum = detail.getBoxNum();
            }
        }
        modelMap.addAttribute("maxBoxNum",maxBoxNum+1);
        modelMap.addAttribute("newId",id);
        return modelMap;
    }

    @RequestMapping("removeJobBookingListFromSession.do")
    public void removeJobBookingListFromSession(HttpSession session){
     session.removeAttribute(JOB_BOOKING_LIST_NAME);
     session.removeAttribute(JOB_BOOKING_DELETED_LIST);
     session.removeAttribute(JOB_BOOKING_DELETED_MATERIAL_LIST);
    }

    /**
     * 清空session中JOB_BOOKING_LIST_NAME，如果是查看，则保存清空的报工单详情id
     * @param session
     */
    @RequestMapping("/clearWarehousingApplicationDetailList.do")
    public void clearWarehousingApplicationDetailList(HttpSession session){
        String requestNo = (String) session.getAttribute(JOB_BOOKING_FORMNO);
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        if(list==null){
            list = new ArrayList<>();
            session.setAttribute(JOB_BOOKING_LIST_NAME,list);
        }
        if(StringUtils.isEmpty(requestNo)){
            if(!CollectionUtils.isEmpty(list)){
                list.clear();
            }
        }else{
            List<String> deletedIds = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_LIST);
           if(CollectionUtils.isEmpty(deletedIds)){
               deletedIds = new ArrayList<>();
               session.setAttribute(JOB_BOOKING_DELETED_LIST,deletedIds);
           }
           if(!CollectionUtils.isEmpty(list)){
               for(JobBookingFormDetail detail : list){
                   deletedIds.add(detail.getId());
               }
               list.clear();
           }
        }
        session.setAttribute(JOB_BOOKING_IS_CLEAR,true);
    }

    /**
     * 修改session中报工单详情的仓库信息
     * @param warehouseCode
     * @param warehouseName
     * @param session
     */
    @RequestMapping("/modifyWarehouseInSession.do")
    public void modifyWarehouseInSession(String warehouseCode,String warehouseName,HttpSession session){
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        if(!CollectionUtils.isEmpty(list)){
            for(JobBookingFormDetail detail : list){
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
        List<JobBookingFormDetail> list = (List<JobBookingFormDetail>) session.getAttribute(JOB_BOOKING_LIST_NAME);
        JobBookingFormDetail unpackingDetail = null;
        //将拆箱的数据从Session中清除
        for(int i = 0;i<list.size();i++){
            JobBookingFormDetail detail = list.get(i);
            if(detail.getId().equals(id)){
                unpackingDetail = list.remove(i);
                break;
            }
        }
        //将清除数据的id存入删除id列表
       List<String> deletedIds = (List<String>) session.getAttribute(JOB_BOOKING_DELETED_LIST);
        if(CollectionUtils.isEmpty(deletedIds)){
            deletedIds  = new ArrayList<>();
            session.setAttribute(JOB_BOOKING_DELETED_LIST,deletedIds);
        }
        deletedIds.add(id);
        //计算要拆的箱数
        int boxesCount = (int)Math.floor(unpackingDetail.getAmountOfJobBooking()/unpackingDetail.getAmountOfPerBox());
        double lastBoxCount = unpackingDetail.getAmountOfJobBooking() % unpackingDetail.getAmountOfPerBox();

        if(boxesCount>0){
            for(int i = 0;i<boxesCount;i++){
                JobBookingFormDetail d = new JobBookingFormDetail();
                BeanUtils.copyProperties(unpackingDetail,d);
                d.setAmountOfJobBooking(unpackingDetail.getAmountOfPerBox());
                d.setAmountOfBoxes(1d);
                d.setId(UUID.randomUUID().toString());
                list.add(d);
            }
            if(lastBoxCount>0) {
                JobBookingFormDetail d = new JobBookingFormDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmountOfJobBooking(lastBoxCount);
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
     * 根据工单单号分页查询报工单详情
     */
    @RequestMapping("/queryJobBookingFormDetailPageByWorkSheetNo.do")
     public ModelMap queryJobBookingFormDetailPageByWorkSheetNo(String workSheetNo, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
         ModelMap modelMap=new ModelMap();
         String hql = "from JobBookingFormDetail detail where detail.jobBookingForm.workSheetNo=?0 order by detail.jobBookingForm.jobBookingDate desc";
         Pager<JobBookingFormDetail> pager = jobBookingFormDetailService.queryObjs(hql,page,rows,new Object[]{workSheetNo});
         List<JobBookingFormDetailVO> voList=new ArrayList<>();
         for(JobBookingFormDetail m:pager.getData()){
             voList.add(model2VO(m));
         }
         modelMap.addAttribute("rows", voList);
         modelMap.addAttribute("total",pager.getTotalCount());

         return  modelMap;
     }

    /**
     * 将领域模型转换为值对象
     */
    private JobBookingFormDetailVO model2VO(JobBookingFormDetail jfd) {
        JobBookingFormDetailVO vo=new JobBookingFormDetailVO();
        vo.setJobBookingDate(sdfDate.format(jfd.getJobBookingForm().getJobBookingDate()));
        vo.setJobBookingTime(sdfTime.format(jfd.getJobBookingForm().getJobBookingDate()));
        vo.setBarCode(jfd.getBarCode());
        vo.setBoxNum(jfd.getBoxNum());
        vo.setAmountOfBoxes(jfd.getAmountOfBoxes());
        vo.setAmountOfJobBooking(jfd.getAmountOfJobBooking());
        vo.setJobBookerName(jfd.getJobBookingForm().getJobBookerName());
        vo.setAmountOfInWarehouse(jfd.getAmountOfInWarehouse());
        return vo;
    }

    /**
     * 根据包装条码查找报工详情
     * @param packageCode
     * @return
     */
    @RequestMapping("queryJobBookingFormDetailByPackageCode.do")
    public ModelMap queryJobBookingFormDetailByPackageCode(String packageCode, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap = new ModelMap();
        String hql = "select new map(bb.barCode as barCode,detail.inventoryCode as inventoryCode," +
                " detail.inventoryName as inventoryName,detail.specificationType as specificationType,detail.furnaceNumber as furnaceNumber," +
                "detail.batchNumber as batchNumber,m.number as number,detail.boxNum as boxNum," +
                "m.employeeName as employeeName,form.productionUnitName as productionUnitName," +
                "detail.className as className,form.workSheetNo as workSheetNo " +
                ") from PackageCodeAndBoxBarMapping m inner join  m.boxBar bb " +
                " inner join JobBookingFormDetail detail on bb.barCode=detail.barCode" +
                " inner join detail.jobBookingForm form  " +
                " where m.PackageCode=?0";
        Pager<Map<String,Object>> pager = jobBookingFormDetailService.queryObjs(hql,page,rows,new Object[]{packageCode});
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
     }


    /**
     * 修改报工单数量
     */
    @RequestMapping("updateJobBookingNumberAndBoxbarNumber.do")
    public ModelMap updateJobBookingNumberAndBoxbarNumber(String barcode,String boxbarNumber,String materialIds,String materialNumbers){
        ModelMap modelMap=new ModelMap();
        jobBookingFormService.updateJobBookingNumberAndBoxbarNumber(barcode,boxbarNumber,materialIds,materialNumbers);
        return modelMap;
    }
}
