package com.digitzones.controllers;

import com.digitzones.dto.InspectionRecordDetailDto;
import com.digitzones.model.InspectionRecordDetail;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IInspectionRecordDetailService;
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
 * 检验单详情
 */
@RestController
@RequestMapping("inspectionRecordDetail")
public class InspectionRecordDetailController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**检验单号存放在session中的key*/
    public static final String INSPECTION_FORMNO="inspectionRecordFormNo";
    /**内存中存放InspectionRecordDetail对象List的名称*/
    public static final String LIST_NAME = "InspectionRecordDetailList";
    @Autowired
    private IInspectionRecordDetailService inspectionRecordDetailService;
    @Autowired
    private IBoxBarService boxBarService;

    /**
     * 清空session
     * @param session
     */
    @RequestMapping("/clearSession.do")
    public void clearSession(HttpSession session){
        session.removeAttribute(INSPECTION_FORMNO);
        session.removeAttribute(LIST_NAME);
    }
    /**
     * 根据检验单号查找检验单详情
     * @param formNo 检验单号
     * @param session
     * @return
     */
    @RequestMapping("/queryByFormNo.do")
    public List<InspectionRecordDetail> queryByFormNo(String formNo, HttpSession session){
        List<InspectionRecordDetail> list = (List<InspectionRecordDetail>) session.getAttribute(LIST_NAME);
        //查看检验单
        if(!StringUtils.isEmpty(formNo)){
                if (CollectionUtils.isEmpty(list)) {
                    list = inspectionRecordDetailService.queryByFormNo(formNo);
                    session.setAttribute(INSPECTION_FORMNO, formNo);
                    if (!CollectionUtils.isEmpty(list)) {
                        session.setAttribute(LIST_NAME, list);
                    }
            }
        }
        return list==null?new ArrayList<>():list;
    }
    /**
     * 更新session中的数据
     * @param detailDto
     * @param session
     */
    @RequestMapping("/updateSession.do")
    public void updateSession(InspectionRecordDetailDto detailDto, HttpSession session){
        List<InspectionRecordDetail> list = (List<InspectionRecordDetail>) session.getAttribute(LIST_NAME);
        for(InspectionRecordDetail detail : list){
            if(detail.getId().equals(detailDto.getId())){
                BeanUtils.copyProperties(detailDto,detail);
                break;
            }
        }
    }
}
