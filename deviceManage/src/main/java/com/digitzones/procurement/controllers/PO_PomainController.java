package com.digitzones.procurement.controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.model.Pager;
import com.digitzones.procurement.model.PO_Pomain;
import com.digitzones.procurement.service.IPO_PomainService;
@Controller
@RequestMapping("/PO_Pomain")
public class PO_PomainController {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private IPO_PomainService po_PomainService;
	
	/**
	 * 分页条件查询
	 * @param rows
	 * @param page
	 * @param search_from
	 * @param search_to
	 * @param cPOID
	 * @param cBusType
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPO_Pomain.do")
	@ResponseBody
	public ModelMap queryPO_Pomain(@RequestParam(defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page,String search_from,String search_to,
			String cVenCode,String cPOID,String cBusType) throws ParseException {
		ModelMap modelMap = new ModelMap();
		String hql = "FROM PO_Pomain p where 1=1";
		
		List<Object> args = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(cPOID)) {
			hql += "and p.cPOID like ?" + i;
			i++;
			args.add("%" + cPOID + "%");
		}


		if(!StringUtils.isEmpty(cBusType)) {
			hql+=" and p.cBusType=?" + (i++);
			args.add(cBusType);
		}

		if(!StringUtils.isEmpty(cVenCode)) {
			hql += " and (p.cVenCode like ?" + i +" or p.cVenName like ?" + i++ +")";
			args.add("%" + cVenCode + "%");
		}

		if(!StringUtils.isEmpty(search_from)) {
			hql += " and p.dPODate >=?" + (i++);
			args.add(search_from+" 00:00:00");
		}

		if(!StringUtils.isEmpty(search_to)) {
			hql += " and p.dPODate <=?" + (i++);
			args.add(search_to+" 23:59:59");
		}
		hql += " order by p.dPODate desc,p.cPOID desc";
		
		Pager<PO_Pomain> objPager = po_PomainService.queryObjs(hql, page, rows, args.toArray());
		List<PO_Pomain> objs = objPager.getData();
		modelMap.addAttribute("rows", objs);
		modelMap.addAttribute("total", objPager.getTotalCount());
		return modelMap;
	}
	
	/**
	 * 根据PIOID获取采购清单
	 * @param POID
	 * @return
	 */
	@RequestMapping("/queryPO_PomainByPOID.do")
	@ResponseBody
	public PO_Pomain queryPO_PomainByPOID(String POID) {
		PO_Pomain po_pomain = po_PomainService.queryByProperty("cPOID", POID);
		return po_pomain;
	}

} 
