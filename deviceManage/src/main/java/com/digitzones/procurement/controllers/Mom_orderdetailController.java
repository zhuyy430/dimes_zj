package com.digitzones.procurement.controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Mom_recorddetail;
import com.digitzones.procurement.model.Mom_recorddetailStatus;
import com.digitzones.procurement.service.IMom_orderdetailService;
import com.digitzones.procurement.service.IMom_orderdetailStatusService;
import com.digitzones.procurement.vo.Mom_recorddetailVO;
@Controller
@RequestMapping("/Mom_orderdetail")
public class Mom_orderdetailController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IMom_orderdetailService mom_orderdetailService;
	@Autowired
	private IMom_orderdetailStatusService mom_orderdetailStatusService;
	
	/**
	 * 分页条件查询
	 * @param rows
	 * @param page
	 * @param search_from
	 * @param search_to
	 * @param MDeptCode  生产部门
	 * @param mocode   生产订单号
	 * @param moallocateInvcode  工件代码
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMom_recorddetail.do")
	@ResponseBody
	public ModelMap queryMom_recorddetail(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,String search_from,String search_to,
			String MDeptCode,String mocode,String moallocateInvcode,Boolean status) throws ParseException {
		ModelMap modelMap = new ModelMap();
		String hql = "from Mom_recorddetail m where 1=1";
		
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(MDeptCode)) {
			hql += "and m.mdeptCode like ?" + i;
			i++;
			args.add("%" + MDeptCode + "%");
		}


		if(!StringUtils.isEmpty(mocode)) {
			hql+=" and m.mocode=?" + (i++);
			args.add(mocode);
		}

		if(!StringUtils.isEmpty(moallocateInvcode)) {
			hql += " and m.moallocateInvcode like ?" + (i++);
			args.add("%" + moallocateInvcode + "%");
		}

		if(!StringUtils.isEmpty(search_from)) {
			hql += " and m.createTime >=?" + (i++);
			args.add(format.parse(search_from+" 00:00:00"));
		}

		if(!StringUtils.isEmpty(search_to)) {
			hql += " and m.createTime <=?" + (i++);
			args.add(format.parse(search_to+" 23:59:59"));
		}
		if(null!=status) {
			if(status)
				hql += " and m.moDId in (select mom.modId from Mom_recorddetailStatus mom)";
				//hql += " and  exists  (select mom.modId from Mom_recorddetailStatus mom where mom.modId=m.moDId)";
			else
				hql += " and m.moDId not in (select mom.modId from Mom_recorddetailStatus mom)";
				//hql += " and not exists (select mom.modId from Mom_recorddetailStatus mom where mom.modId=m.moDId)";
		}else{
			hql += " and m.moDId not in (select mom.modId from Mom_recorddetailStatus mom)";
			//hql += " and not exists (select mom.modId from Mom_recorddetailStatus mom where mom.modId=m.moDId)";
		}

		hql+=" order by m.createTime desc";
		Pager<Mom_recorddetail> objPager = mom_orderdetailService.queryObjs(hql, page, rows, args.toArray());
		List<Mom_recorddetail> objs = objPager.getData();
		modelMap.addAttribute("rows", objs);
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 根据PIOID获取采购清单
	 * @return
	 */
	@RequestMapping("/queryMom_recorddetailById.do")
	@ResponseBody
	public Mom_recorddetailVO queryMom_recorddetailById(String id) {
		 Mom_recorddetail m = mom_orderdetailService.queryObjById(id);
		 return model2VO(m);
	}

	/**
	 * 根据PIOID获取采购清单
	 * @return
	 */
	@RequestMapping("/queryMom_recorddetailByCodeAndSeq.do")
	@ResponseBody
	public Mom_recorddetailVO queryMom_recorddetailByCodeAndSeq(String code) {
		String vcode = code.substring(0,2);
		if(!"Mo".equals(vcode)){
			return null;
		}
		String[] codelist = code.split("-");
		code = codelist[0];
		String seq = codelist[1];
		 Mom_recorddetail m = mom_orderdetailService.queryMom_recorddetailByCodeAndSeq(code, seq);
		 if(m!=null)
		 	return model2VO(m);
		 else
		 	return null;
	}

	/**
	 * 根据PIOID获取采购清单
	 * @return
	 */
	@RequestMapping("/queryMom_recorddetailIsTrun.do")
	@ResponseBody
	public Boolean queryMom_recorddetailIsTrun(String id) {
		Mom_recorddetailStatus m = mom_orderdetailStatusService.queryByProperty("modId", id);
		if(null!=m){
			return true;
		}
		return false;
	}

	/**
	 * 将领域模型转换为值对象
	 * @param m
	 * @return
	 */
	private Mom_recorddetailVO model2VO(Mom_recorddetail m) {
		Mom_recorddetailVO vo = new Mom_recorddetailVO();
		 BeanUtils.copyProperties(m,vo);
		 vo.setStartDate(format.format(m.getStartDate()));
		 vo.setRelsTime(format.format(m.getRelsTime()));
		 vo.setCreateTime(format.format(m.getCreateTime()));
		 return vo;
	}
} 
