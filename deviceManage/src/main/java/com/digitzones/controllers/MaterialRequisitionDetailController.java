package com.digitzones.controllers;
import com.digitzones.dto.MaterialRequisitionDetailDto;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IMaterialRequisitionDetailService;
import com.digitzones.vo.MaterialRequisitionDetailVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * 领料单详情
 */
@RestController
@RequestMapping("materialRequisitionDetail")
public class MaterialRequisitionDetailController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**领料单号存放在session中的key*/
    public static final String MATERIALREQUISITION_FORMNO="materialRequisitionFormNo";
    /**内存中存放MaterialRequisitionDetail对象List的名称*/
    public static final String LIST_NAME = "MaterialRequisitionDetailList";
    /**是否执行了只清空LIST_NAME的操作(非清空session)*/
    public static final String IS_CLEAR = "isClearMaterialRequisitionDetail";
    /**存储删除的id*/
    public static final String DELETED_DETAIL_IDS = "deletedDetailIds";
    @Autowired
    private IMaterialRequisitionDetailService materialRequisitionDetailService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    /**
     * 查询领用单详情
     * @param from 领料日期开始
     * @param to 领料日期结束
     * @param inventoryCode 物料编码
     * @param no 工单单号
     * @param barCode 箱条码
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryMaterialRequisitionDetails.do")
    public ModelMap queryMaterialRequisitionDetails(String from,String to,String inventoryCode,String no,String barCode,String furnaceNum,
                                                    @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select detail from MaterialRequisitionDetail detail inner join fetch detail.materialRequisition mr" +
                " inner join fetch mr.workSheet ws where 1=1 ";
        List<Object> params = new ArrayList<>();
        int i = 0;
        try {
            if(!StringUtils.isEmpty(from)){
                hql += " and mr.pickingDate>=?" + i++;
                params.add(format.parse(from + " 00:00:00"));
            }
            if(!StringUtils.isEmpty(to)){
                hql += " and mr.pickingDate<=?" + i++;
                params.add(format.parse(to + " 23:59:59"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(inventoryCode)){
            hql += " and detail.inventoryCode like ?" + i++;
            params.add("%" + inventoryCode + "%");
        }

        if(!StringUtils.isEmpty(barCode)){
            hql += " and detail.barCode like ?" + i++;
            params.add("%" + barCode + "%");
        }
        if(!StringUtils.isEmpty(no)){
            hql += " and ws.no like ?" + i++;
            params.add("%" + no + "%");
        }

        if(!StringUtils.isEmpty(furnaceNum)){
            hql += " and detail.furnaceNumber like ?" + i++;
            params.add("%" + furnaceNum + "%");
        }

        hql += " order by mr.pickingDate desc,mr.formNo desc";
        Pager<MaterialRequisitionDetail> pager = materialRequisitionDetailService.queryObjs(hql,page,rows,params.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 清空session
     * @param session
     */
    @RequestMapping("/clearSession.do")
    public void clearSession(HttpSession session){
        session.removeAttribute(MATERIALREQUISITION_FORMNO);
        session.removeAttribute(LIST_NAME);
        session.removeAttribute(IS_CLEAR);
        session.removeAttribute(DELETED_DETAIL_IDS);
    }
    /**
     * 根据领料单号查找领料单详情
     * @param formNo 入库申请单号
     * @param session
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public List<MaterialRequisitionDetail> queryByFormNo(String formNo, HttpSession session){
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);;
        //查看入库申请单
        if(!StringUtils.isEmpty(formNo)){
            Boolean isClear = (Boolean) session.getAttribute(IS_CLEAR);
            if(isClear==null || !isClear) {
                if (CollectionUtils.isEmpty(list)) {
                    list = materialRequisitionDetailService.queryByFormNo(formNo);
                    session.setAttribute(MATERIALREQUISITION_FORMNO, formNo);
                    if (!CollectionUtils.isEmpty(list)) {
                        session.setAttribute(LIST_NAME, list);
                    }
                }
            }
        }
        return list==null?new ArrayList<>():list;
    }

    /**
     * 根据工单号查找领料单详情
     */
    @RequestMapping("/queryByWorkSheetNo.do")
    public List<MaterialRequisitionDetail> queryByWorkSheetNo(String workSheetNo){
    	//查看入库申请单
    	List<MaterialRequisitionDetail> list = materialRequisitionDetailService.queryByWorkSheetNo(workSheetNo);
    	return list==null?new ArrayList<>():list;
    }
    /**
     * 根据领料单号查找领料单详情
     */
    @RequestMapping("/queryMaterialRequisitionDetailByBarCode.do")
    public List<MaterialRequisitionDetail> queryMaterialRequisitionDetailByBarCode(String barCode){
    	//查看入库申请单
    	List<MaterialRequisitionDetail> list = materialRequisitionDetailService.queryByBarCode(barCode);
    	return list==null?new ArrayList<>():list;
    }
    /**
     * 根据工单号和箱条码查找领料单详情
     */
    @RequestMapping("/queryByWorkSheetNoAndBarCode.do")
    public List<MaterialRequisitionDetail> queryByWorkSheetNoAndBarCode(String workSheetNo,String barCode){
    	//查看入库申请单
    	List<MaterialRequisitionDetail> list = materialRequisitionDetailService.queryByWorkSheetNoAndBarCode(workSheetNo,barCode);
    	return list==null?new ArrayList<>():list;
    }
    /**
     * 更新session中的数据
     * @param detailDto
     * @param session
     */
    @RequestMapping("/updateSession.do")
    public void updateSession(MaterialRequisitionDetailDto detailDto, HttpSession session){
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);
        for(MaterialRequisitionDetail detail : list){
            if(detail.getId().equals(detailDto.getId())){
                BeanUtils.copyProperties(detailDto,detail);
                break;
            }
        }
    }
    /**
     * 将新增的领料单详情保存到session
     * @param detail 领料单详情对象
     * @param session
     */
    @RequestMapping("/saveSession.do")
    public void saveSession(MaterialRequisitionDetail detail,HttpSession session){
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);
        if(list == null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
        }
        detail.setId(UUID.randomUUID().toString());
        list.add(detail);
    }
    /**
     * 根据id删除
     * @param id 领料单详情id
     * @param session
     * @return
     */
    @RequestMapping("/deleteById.do")
    public ModelMap deleteById(String id,HttpSession session){
        String formNo = (String) session.getAttribute(MATERIALREQUISITION_FORMNO);
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);
        if(!StringUtils.isEmpty(formNo)){
            List<String> deletedList = (List<String>) session.getAttribute(DELETED_DETAIL_IDS);
            if(CollectionUtils.isEmpty(deletedList)){
                deletedList = new ArrayList<>();
                session.setAttribute(DELETED_DETAIL_IDS,deletedList);
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
    private void removeFromSession(List<MaterialRequisitionDetail> list,String id){
        for(int i = 0;i<list.size();i++){
            MaterialRequisitionDetail detail = list.get(i);
            if(detail.getId().equals(id)){
                list.remove(i);
                break;
            }
        }
    }
    /**
     * 根据箱条码查找
     * @param barCode
     * @param session
     * @return
     */
    @RequestMapping("queryByBarCode.do")
    public MaterialRequisitionDetail queryByBarCode(Long barCode,HttpSession session){
        ModelMap modelMap = new ModelMap();
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);
        if(list==null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
        }
       BoxBar boxBar = boxBarService.queryObjById(barCode);
        if(boxBar==null){
            return null;
        }
        MaterialRequisitionDetail detail = new MaterialRequisitionDetail();
        detail.setBarCode(boxBar.getBarCode()+"");
        detail.setAmountOfBoxes(boxBar.getAmountOfBoxes());
        detail.setBatchNumber(boxBar.getBatchNumber());
        detail.setBoxNum(boxBar.getBoxNum());
        detail.setUnitCode(boxBar.getUnitCode());
        detail.setPositionCode(boxBar.getPositonCode());
        detail.setPositionName(boxBar.getPositonName());
        detail.setUnitName(boxBar.getUnitName());
        detail.setFurnaceNumber(boxBar.getFurnaceNumber());
        detail.setInventoryCode(boxBar.getInventoryCode());
        detail.setInventoryName(boxBar.getInventoryName());
        detail.setSpecificationType(boxBar.getSpecificationType());
        detail.setAmount(boxBar.getAmount());
        detail.setId(UUID.randomUUID().toString());
        if(boxBar.getBoxBarType().equals("报工单")){
        	JobBookingFormDetail jobBookingFormDetail = jobBookingFormDetailService.queryObjById(boxBar.getFkey());
        	if(null!=jobBookingFormDetail){
        		detail.setAllClassCodes(jobBookingFormDetail.getAllClassCodes());
        		detail.setClassName(jobBookingFormDetail.getClassName());
        	}
        }
        list.add(detail);
        return detail;
    }
    /**
     * 根据箱条码和工单号查找
     * @param barCode
     * @param session
     * @return
     */
    @RequestMapping("queryByBarCodeAndNo.do")
    public ModelMap queryByBarCodeAndNo(Long barCode,String no,HttpSession session){
        ModelMap modelMap = new ModelMap();
        List<MaterialRequisitionDetail> list = (List<MaterialRequisitionDetail>) session.getAttribute(LIST_NAME);
        if(list==null){
            list = new ArrayList<>();
            session.setAttribute(LIST_NAME,list);
        }

        BoxBar boxBar = boxBarService.queryObjById(barCode);
       if(boxBar==null){
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg","不存在该箱号条码!");
            return modelMap;
        }else{
            BoxBar boxBar1 = boxBarService.queryByBarCodeAndNo(barCode,no);
            if(boxBar1==null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","非当前工单所需材料，不能领取!");
                return modelMap;
            }
        }
       
        MaterialRequisitionDetail detail = new MaterialRequisitionDetail();
        detail.setBarCode(boxBar.getBarCode()+"");
        detail.setAmountOfBoxes(boxBar.getAmountOfBoxes());
        detail.setBatchNumber(boxBar.getBatchNumber());
        detail.setBoxNum(boxBar.getBoxNum());
        detail.setUnitCode(boxBar.getUnitCode());
        detail.setUnitName(boxBar.getUnitName());
        detail.setFurnaceNumber(boxBar.getFurnaceNumber());
        detail.setPositionCode(boxBar.getPositonCode());
        detail.setPositionName(boxBar.getPositonName());
        detail.setInventoryCode(boxBar.getInventoryCode());
        detail.setInventoryName(boxBar.getInventoryName());
        detail.setSpecificationType(boxBar.getSpecificationType());
        detail.setAmount(boxBar.getAmountOfPerBox());
        detail.setId(UUID.randomUUID().toString());
        if(boxBar.getBoxBarType().equals("报工单")){
        	JobBookingFormDetail jobBookingFormDetail = jobBookingFormDetailService.queryObjById(boxBar.getFkey());
        	if(null!=jobBookingFormDetail){
        		detail.setAllClassCodes(jobBookingFormDetail.getAllClassCodes());
        		detail.setClassName(jobBookingFormDetail.getClassName());
        	}
        }
        list.add(detail);
        modelMap.addAttribute("detail",detail);
        modelMap.addAttribute("success",true);
        return modelMap;
    }

    /**
     * 根据生产单号查询领料状况
     */
    @RequestMapping("/queryMaterialRequisitionDetailByWorkSheetNo.do")
    public ModelMap queryMaterialRequisitionDetailByWorkSheetNo(String workSheetNo, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page){
        ModelMap modelMap=new ModelMap();
        String hql = "from MaterialRequisitionDetail detail where detail.materialRequisition.workSheet.no=?0 order by detail.materialRequisition.pickingDate desc";
        Pager<MaterialRequisitionDetail> pager = materialRequisitionDetailService.queryObjs(hql,page,rows,new Object[]{workSheetNo});
        List<MaterialRequisitionDetailVO> voList=new ArrayList<>();
        for(MaterialRequisitionDetail m:pager.getData()){
            voList.add(model2VO(m));
        }
        modelMap.addAttribute("rows", voList);
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }


    /**
     * 将领域模型转换为值对象
     */
    private MaterialRequisitionDetailVO model2VO(MaterialRequisitionDetail mrd) {

        MaterialRequisitionDetailVO vo=new MaterialRequisitionDetailVO();
        vo.setPickingDate(sdf.format(mrd.getMaterialRequisition().getPickingDate()));
        vo.setFormNo(mrd.getMaterialRequisition().getFormNo());
        vo.setBarCode(mrd.getBarCode());
        vo.setInventoryCode(mrd.getInventoryCode());
        vo.setInventoryName(mrd.getInventoryName());
        vo.setSpecificationType(mrd.getSpecificationType());
        vo.setAmount(mrd.getAmount());
        vo.setPositionCode(mrd.getPositionCode());
        vo.setPositionName(mrd.getPositionName());
        vo.setBatchNumber(mrd.getBatchNumber());
        vo.setFurnaceNumber(mrd.getFurnaceNumber());
        vo.setBoxNum(mrd.getBoxNum());
        vo.setAmountOfBoxes(mrd.getAmountOfBoxes());
        return vo;
    }
}
