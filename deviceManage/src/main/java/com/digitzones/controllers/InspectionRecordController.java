package com.digitzones.controllers;
import com.digitzones.model.*;
import com.digitzones.service.IInspectionRecordService;
import com.digitzones.service.IUserService;
import com.digitzones.util.StringUtil;
import com.digitzones.vo.InspectionRecordVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 检验单控制器
 */
@RestController
@RequestMapping("inspectionRecord")
public class InspectionRecordController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final String FORMNO_PREFIX = "QC-";
    @Autowired
    private IInspectionRecordService inspectionRecordService;
    @Autowired
    private IUserService userService;
    /**
     * 查询检验单详情
     * @param from 检验日期开始
     * @param to 检验日期结束
     * @param inventoryCode 物料编码
     * @param no 工单单号
     * @param productionUnitCode 生产单元编码
     * @param classCode 班次编码
     * @param batchNumber 批次
     * @param processName 工序名称
     * @param inspectionResult 检验结果
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryInspectionRecords.do")
    public ModelMap queryInspectionRecords(String from, String to, String inventoryCode, String no, String productionUnitCode,String classCode,
                                                 String batchNumber,String processName,String inspectionResult,
                                                 @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        ModelMap modelMap = new ModelMap();
        String hql = "select ir from InspectionRecord ir where 1=1 ";
        List<Object> params = new ArrayList<>();
        int i = 0;
        try {
            format.applyPattern("yyyy-MM-dd HH:mm:ss");
            if(!StringUtils.isEmpty(from)){
                hql += " and ir.inspectionDate>=?" + i++;
                params.add(format.parse(from + " 00:00:00"));
            }
            if(!StringUtils.isEmpty(to)){
                hql += " and ir.inspectionDate<=?" + i++;
                params.add(format.parse(to + " 23:59:59"));
            }
            format.applyPattern("yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isEmpty(inventoryCode)){
            hql += " and ir.inventoryCode =?" + i++;
            params.add(inventoryCode);
        }

        if(!StringUtils.isEmpty(batchNumber)){
            hql += " and ir.batchNumber like ?" + i++;
            params.add("%" + batchNumber + "%");
        }
        if(!StringUtils.isEmpty(processName)){
            hql += " and ir.processName like ?" + i++;
            params.add("%" + processName + "%");
        }
        if(!StringUtils.isEmpty(inspectionResult)){
            hql += " and ir.inspectionResult=?" + i++;
            params.add(inspectionResult);
        }
        if(!StringUtils.isEmpty(productionUnitCode)){
            hql += " and ir.productionUnitCode=?" + i++;
            params.add(productionUnitCode);
        }
        if(!StringUtils.isEmpty(classCode)){
            hql += " and ir.classCode=?" + i++;
            params.add(classCode);
        }
        if(!StringUtils.isEmpty(no)){
            hql += " and ir.no like ?" + i++;
            params.add("%" + no + "%");
        }

        hql += " order by ir.inspectionDate desc,ir.formNo desc";
        Pager<InspectionRecord> pager = inspectionRecordService.queryObjs(hql,page,rows,params.toArray());
        modelMap.addAttribute("rows", pager.getData());
        modelMap.addAttribute("total",pager.getTotalCount());
        return modelMap;
    }
    /**
     * 查询检验单详情
     * @param from 检验日期开始
     * @param to 检验日期结束
     * @param no 工单单号
     * @param batchNumber 批次
     * @param inspectionResult 检验结果
     * @return
     */
    @RequestMapping("/queryInspectionRecordsByDeviceSite.do")
    public List<InspectionRecord> queryInspectionRecordsByDeviceSite(String from, String to, String no,String batchNumber,String inspectionResult,String deviceSiteCode) {
    	String hql = "select distinct ir from InspectionRecord ir inner join WorkSheetDetail wsd on ir.processCode=wsd.processCode "
    			+ " inner join wsd.workSheet ws on ir.no=ws.no where 1=1 ";
    	List<Object> params = new ArrayList<>();
    	int i = 0;
    	try {
    		format.applyPattern("yyyy-MM-dd HH:mm:ss");
    		if(!StringUtils.isEmpty(from)){
    			hql += " and ir.inspectionDate>=?" + i++;
    			params.add(format.parse(from + " 00:00:00"));
    		}
    		if(!StringUtils.isEmpty(to)){
    			hql += " and ir.inspectionDate<=?" + i++;
    			params.add(format.parse(to + " 23:59:59"));
    		}
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	
    	if(!StringUtils.isEmpty(batchNumber)){
    		hql += " and ir.batchNumber like ?" + i++;
    		params.add("%" + batchNumber + "%");
    	}
    	if(!StringUtils.isEmpty(deviceSiteCode)){
    		hql += " and wsd.deviceSiteCode = ?" + i++;
    		params.add(deviceSiteCode);
    	}
    	if(!StringUtils.isEmpty(inspectionResult)){
    		hql += " and ir.inspectionResult=?" + i++;
    		params.add(inspectionResult);
    	}
    	if(!StringUtils.isEmpty(no)){
    		hql += " and ir.no like ?" + i++;
    		params.add("%" + no + "%");
    	}
    	
    	hql += " order by ir.inspectionDate desc";
    	List<InspectionRecord> list = inspectionRecordService.queryInspectionRecordsByDeviceSite(hql, params);
    	return list;
    }
    /**
     * 生成默认申请单号
     * @return
     */
    @RequestMapping("/generateInspectionRecordFormNo.do")
    public String generateInspectionRecordFormNo(){
        Date now = new Date();
        String formNo = inspectionRecordService.queryMaxFormNoByInspectionDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "001";
        }
        return requestNo;
    }
    /**
     * 根据检验单号查找检验单信息
     * @param formNo 检验单单号
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public InspectionRecord queryByFormNo(String formNo){
        return  inspectionRecordService.queryByProperty("formNo",formNo);
    }
    /**
     * 保存检验单
     * @param formVo
     * @param session
     * @return
     */
    @RequestMapping("/saveInspectionRecord.do")
    public ModelMap saveInspectionRecord(InspectionRecordVo formVo , HttpSession session, Principal principal){
        InspectionRecord form = new InspectionRecord();
        BeanUtils.copyProperties(formVo,form);
        User loginUser = userService.queryByProperty("username",principal.getName());
        String formNo = (String) session.getAttribute(InspectionRecordDetailController.INSPECTION_FORMNO);
        List<InspectionRecordDetail> details = (List<InspectionRecordDetail>) session.getAttribute(InspectionRecordDetailController.LIST_NAME);

        ModelMap  modelMap = checkInspectionRecord(form,details);
        if(!StringUtils.isEmpty((String)modelMap.get("msg"))){
            return modelMap;
        }

        //没有检验单号，说明当前为新增操作
        if(StringUtils.isEmpty(formNo)){
            InspectionRecord waf = inspectionRecordService.queryByProperty("formNo",form.getFormNo());
            if(waf!=null){
                modelMap.addAttribute("success",false);
                modelMap.addAttribute("msg","检验单号已被使用！");
                return modelMap;
            }
            if(loginUser.getEmployee()!=null) {
               form.setInspectorCode(loginUser.getEmployee().getCode());
               form.setInspectorName(loginUser.getEmployee().getName());
            }else{
                form.setInspectorCode(loginUser.getId() + "");
                form.setInspectorName(loginUser.getUsername());
            }
            inspectionRecordService.addInspectionRecord(form,details);
            session.setAttribute(InspectionRecordDetailController.INSPECTION_FORMNO,form.getFormNo());
        }else{//有检验单号，说明当前为查看操作
            inspectionRecordService.updateInspectionRecord(form,details);
        }
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("msg","操作成功!");
        return modelMap;
    }

    /**
     *  保存检验单的校验方法
     * @param form 检验单对象
     * @param list 检验单详情对象
     * @return
     */
    private ModelMap checkInspectionRecord(InspectionRecord form,List<InspectionRecordDetail> list){
        ModelMap modelMap = new ModelMap();
        //申请单缺少订单信息
        modelMap.addAttribute("success",false);
        String msg = null;
        if(StringUtils.isEmpty(form.getFormNo())){
            msg ="检验单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(form.getInspectionDate()==null){
            msg = "检验日期不能为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(StringUtils.isEmpty(form.getNo())){
            msg ="工单单号不允许为空!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }
        if(CollectionUtils.isEmpty(list)){
            msg ="单据为空，不允许保存!";
            modelMap.addAttribute("msg",msg);
            return modelMap;
        }else{
            for(InspectionRecordDetail detail : list){
                /*if(detail.getParameterValue()==null || "".equals(detail.getParameterValue())){
                    msg = "参数值不合法!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }*/
                if(detail.getInspectionResult()==null || "".equals(detail.getInspectionResult())){
                    msg = "请选择检验结果!";
                    modelMap.addAttribute("msg",msg);
                    return modelMap;
                }
            }
        }
        return modelMap;
    }
    /**
     * 根据领料单单号删除领料单及详情
     * @param formNo
     * @return
     */
    @RequestMapping("/deleteByFormNo.do")
    public ModelMap deleteByFormNo(String formNo){
        ModelMap modelMap = new ModelMap();
        inspectionRecordService.deleteByFormNo(formNo);
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("message","删除成功!");
        return modelMap;
    }
}
