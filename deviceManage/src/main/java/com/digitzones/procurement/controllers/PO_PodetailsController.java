package com.digitzones.procurement.controllers;
import java.util.ArrayList;
import java.util.List;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.PO_Pomain;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IPO_PomainService;
import com.digitzones.procurement.vo.PO_PodetailsVo;
import com.digitzones.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.procurement.model.PO_Podetails;
import com.digitzones.procurement.service.IPO_PodetailsService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/PO_Podetails")
public class PO_PodetailsController {
	@Autowired
	private IPO_PodetailsService po_PodetailsService;
	@Autowired
	private IPO_PomainService pomainService;
	/**
	 * 根据POID获取采购子清单
	 * @param POID
	 * @return
	 */
	@RequestMapping("/queryPO_PodetailsByPOID.do")
	@ResponseBody
	public List<PO_Podetails> queryPO_PodetailsByPOID(String POID) {
		List<PO_Podetails> list = po_PodetailsService.queryPO_PodetailsByPOID(POID);
		return list;
	}
	/**
	 * 查询采购订单详情
	 * @param cpoId 采购订单号
	 * @param cInvCode 物料编码
	 * @param cInvName 物料名称
	 * @param cVenCode 供应商编码
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryPO_Podetails.do")
	@ResponseBody
	public ModelMap queryPO_Podetails(String cpoId, String cInvCode, String cInvName,String cVenCode,
									  @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from PO_Podetails v where v.poId in (select POID from PO_Pomain po where po.cVenCode=?0) ";
		List<Object> data = new ArrayList<>();
		data.add(cVenCode);
		int i = 1;
		if(!StringUtils.isEmpty(cpoId) && !cpoId.equals("")){
			hql += " and v.cpoId like ?" + i++;
			data.add("%" + cpoId.trim() + "%");
		}
		if(!StringUtils.isEmpty(cInvCode) && !cInvCode.trim().equals("")){
			hql += " and cInvCode like ?" + i++;
			data.add("%" + cInvCode.trim() + "%");
		}
		if(!StringUtils.isEmpty(cInvName) && !cInvName.trim().equals("")){
			hql += " and cInvName like ?" + i++;
			data.add("%" + cInvName.trim() + "%");
		}

		hql += " order by v.cpoId desc,v.id desc";
		Pager<PO_Podetails> pager = po_PodetailsService.queryObjs(hql,page,rows,data.toArray());
		List<PO_PodetailsVo> vos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(pager.getData())){
			for(PO_Podetails detail:pager.getData()){
				PO_PodetailsVo vo = new PO_PodetailsVo();
				BeanUtils.copyProperties(detail,vo);
				PO_Pomain po_pomain = pomainService.queryObjById(detail.getPoId());
				vo.setPo_pomain(po_pomain);

				vos.add(vo);
			}
		}
		modelMap.addAttribute("total",pager.getTotalCount());
		modelMap.addAttribute("rows", vos);
		return modelMap;
	}
	/**
	 * 将ids表示的订单详情存入session
	 * @param ids
	 * @param warehouseCode 仓库编码
	 * @param warehouseName 仓库名称
	 * @param session
	 */
	@RequestMapping("/addPodetails2Session.do")
	@ResponseBody
	public void addPodetails2Session(String ids ,String warehouseName,String warehouseCode,HttpSession session){
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
		//根据id查找订单详情
		List<PO_Podetails> detailsList = po_PodetailsService.queryByIds(idsList);
		//从session中取出入库申请单详情列表
		List<WarehousingApplicationFormDetail> list = (List<WarehousingApplicationFormDetail>) session.getAttribute(WarehousingApplicationFormDetailController.LIST_NAME);
		if(list == null){
			list = new ArrayList<>();
			session.setAttribute(WarehousingApplicationFormDetailController.LIST_NAME,list);
			for(PO_Podetails detail:detailsList){
				list.add(copyProperties(detail,warehouseCode,warehouseName));
			}
		}else{
			for(PO_Podetails detail:detailsList){
				boolean isExist = false;
				for(WarehousingApplicationFormDetail formDetail : list){
					if(String.valueOf(detail.getId()).equals(formDetail.getId())){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					list.add(copyProperties(detail,warehouseCode,warehouseName));
				}
			}
		}
	}
	/**
	 * 属性拷贝
	 * @param detail
	 * @return
	 */
	private WarehousingApplicationFormDetail copyProperties(PO_Podetails detail,String warehouseCode,String warehouseName){
		WarehousingApplicationFormDetail formDetail = new WarehousingApplicationFormDetail();
		formDetail.setPo_poDetailId(detail.getId());
		formDetail.setId(detail.getId()+"");
		formDetail.setPurchasingNo(detail.getCpoId());
		formDetail.setInventoryName(detail.getcInvName());
		formDetail.setInventoryCode(detail.getcInvCode());
		formDetail.setSpecificationType(detail.getcInvStd());
		Double define7 = detail.getcDefine7();
		formDetail.setAmount(detail.getiQuantity().doubleValue()-(define7==null?0:define7));
		formDetail.setUnit(detail.getcComUnitName());
		formDetail.setInventoryTypeName(detail.getcInvCName());
		formDetail.setAmountOfPerBox(formDetail.getAmount());
		formDetail.setAmountOfBoxes(1d);
		formDetail.setWarehouseName(warehouseName);
		formDetail.setWarehouseCode(warehouseCode);
		return formDetail;
	}
}
