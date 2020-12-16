package com.digitzones.procurement.controllers;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Vendor;
import com.digitzones.procurement.service.IVendorClassService;
import com.digitzones.procurement.service.IVendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {
	@Autowired
	private IVendorService vendorService;
	@Autowired
	private IVendorClassService vendorClassService;
	/**
	 * 根据供应商类型编码查找供应商信息
	 * @param cvcCode 供应商类别编码
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("queryVendorsByVendorClassesCode.do")
	public ModelMap queryVendorsByVendorClassesCode(String cvcCode,String vendorCode,String vendorName,
													@RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from Vendor v where 1=1 ";
		List<Object> data = new ArrayList<>();
		int i = 0;
		if(!StringUtils.isEmpty(cvcCode)){
			hql += " and v.cVCCode=?" + i++;
			data.add(cvcCode);
		}

		if(!StringUtils.isEmpty(vendorCode) && !vendorCode.trim().equals("")){
			hql += " and cVenCode like ?" + i++;
			data.add("%" + vendorCode.trim() + "%");
		}
		if(!StringUtils.isEmpty(vendorName) && !vendorName.trim().equals("")){
			hql += " and cVenName like ?" + i++;
			data.add("%" + vendorName.trim() + "%");
		}

		Pager<Vendor> pager = vendorService.queryObjs(hql,page,rows,data.toArray());
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", pager.getData());
		return modelMap;
	}
	
	/**
	 * 获取所有供应商
	 * @return
	 */
	@RequestMapping("/queryAllVendor.do")
	@ResponseBody
	public List<Vendor> queryAllVendor() {
		List<Vendor> vendors = vendorService.queryAllVendor();
		return vendors;
	}
	/**
	 * 获取所有供应商
	 * @return
	 */
	@RequestMapping("/queryVendor.do")
	@ResponseBody
	public Vendor queryVendor(Principal principal) {
		Vendor vendor = vendorService.queryObjById(principal.getName());
		return vendor;
	}
}
