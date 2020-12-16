package com.digitzones.controllers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.DeliveryPlanDetail;
import com.digitzones.model.PackageCode;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeliveryPlanDetailService;
import com.digitzones.service.IPackageCodeService;
import com.digitzones.util.QREncoder;
import com.digitzones.vo.PackageCodeVO;
/**
 * 包装条码
 * @author zhuyy430
 *
 */
@Controller
@RequestMapping("/packageCode")
public class PackageCodeController {
	@Autowired
	private IPackageCodeService packageCodeService;
	@Autowired
	private IDeliveryPlanDetailService deliveryPlanDetailService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	public static final String PACKAGECODE_LIST = "PACKAGECODE_LIST";
	 private QREncoder qrEncoder = new QREncoder();
	/**
	 * 查询最新的数据
	 * @return
	 */
	@RequestMapping("/queryPackageCodeCode.do")
	@ResponseBody
	public String queryPackageCodeCode(HttpSession session){
		PackageCode packageCode = packageCodeService.queryMaxPackageCode();
		String code = "";
		if(null==packageCode){
			code = "NYCT-"+format.format(new Date())+"0001";
		}else{
			List<PackageCode> list = (List<PackageCode>) session.getAttribute(PACKAGECODE_LIST);
			if(!CollectionUtils.isEmpty(list)){
				packageCode = list.get(0);
				session.removeAttribute(PACKAGECODE_LIST);
				return packageCode.getCode();
			}
			String oldCode=packageCode.getCode();
			String[] oldcodeList = oldCode.split("-");
			String oldtime = oldcodeList[1].substring(0, 8);
			String num = "";
			if(oldtime.equals(format.format(new Date()))){
				num=oldcodeList[1].substring(6,12);
				num=(Integer.parseInt(num)+1)+"";
				code = "NYCT-"+format.format(new Date()).substring(0, format.format(new Date()).length()-2)+num;
			}else{
				code = "NYCT-"+format.format(new Date())+"0001";
			}
		}
		return code;
	}
	
	@RequestMapping("/generatePackageCode.do")
	@ResponseBody
	public ModelMap generatePackageCode(String codeStart,String codeEnd,Integer boxNumStart,Double boxAmount,
			String batchNumber,String furnaceNumber,String inventoryCode,String deliveryPlanDetailId,HttpSession session){
		ModelMap modelMap = new ModelMap();
		List<PackageCode> pcList = new ArrayList<>();
		int start = Integer.parseInt(codeStart.substring(11, codeStart.length()));
		int end = Integer.parseInt(codeEnd.substring(11, codeEnd.length()));
		for(;start<=end;start++,boxNumStart++){
			PackageCode pc = new PackageCode();
			DeliveryPlanDetail dp = new DeliveryPlanDetail();
			dp=deliveryPlanDetailService.queryObjById(deliveryPlanDetailId);
			pc.setBatchNumber(batchNumber);
			pc.setBoxamount(boxAmount);
			pc.setBoxnum(boxNumStart);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int day = cal.get(Calendar.DATE);
			String code ="";
			if(day<10)
				code  = "NYCT-"+format.format(new Date()).substring(0,format.format(new Date()).length()-1)+start;
			else
				code = "NYCT-"+format.format(new Date()).substring(0,format.format(new Date()).length()-2)+start;
			pc.setCode(code);
			pc.setCreateDate(new Date());
			pc.setFurnaceNumber(furnaceNumber);
			pc.setInventoryCode(inventoryCode);
			pc.setInventoryName(dp.getInventoryName());
			pc.setCustomer(dp.getCustomerName());
			pc.setFormNo(dp.getDeliveryPlan().getFormNo());
			pc.setSpecificationType(dp.getSpecificationType());
			pc.setInventoryName(dp.getInventoryName());
			pc.setSaleOrderNo(dp.getFormNo());
			pcList.add(pc);
		}
		session.setAttribute(PACKAGECODE_LIST, pcList);
		modelMap.addAttribute("success", true);
		modelMap.addAttribute(PACKAGECODE_LIST, pcList);
		modelMap.addAttribute("msg", "生成成功!");
		return modelMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPackageCode.do")
	@ResponseBody
	public List<PackageCode> queryPackageCode(HttpSession session,String codeStart,String codeEnd){
		List<PackageCode> list = (List<PackageCode>) session.getAttribute(PACKAGECODE_LIST);
		return list==null?new ArrayList<>():list;
	}
	@RequestMapping("/addPackageCode.do")
	@ResponseBody
	public ModelMap addPackageCode(HttpSession session){
		ModelMap modelMap = new ModelMap();
		List<PackageCode> list = (List<PackageCode>) session.getAttribute(PACKAGECODE_LIST);
		if(!CollectionUtils.isEmpty(list)){
			for(PackageCode p:list){
				PackageCode pc = packageCodeService.queryByProperty("code", p.getCode());
				if(null!=pc){
					pc.setBoxamount(p.getBoxamount());
					packageCodeService.updateObj(pc);
				}else
					packageCodeService.addObj(p);
			}
			modelMap.addAttribute("success", true);
			modelMap.addAttribute("data", list);
			modelMap.addAttribute("msg", "保存成功!");
			return modelMap;
		}
		modelMap.addAttribute("success", false);
		modelMap.addAttribute("msg", "还未生成包装条码!");
		return modelMap;
	}
	
	@RequestMapping("/deleteSession.do")
	@ResponseBody
	public void deleteSession(HttpSession session){
		session.removeAttribute(PACKAGECODE_LIST);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/deletePackageCode.do")
	@ResponseBody
	public ModelMap deletePackageCode(String code,HttpSession session){
		List<PackageCode> plist = (List<PackageCode>) session.getAttribute(PACKAGECODE_LIST);
		if(!CollectionUtils.isEmpty(plist)){
			PackageCode dp = new PackageCode();
			for(PackageCode p:plist){
				if(p.getCode().equals(code)){
					dp=p;
				}
			}
			plist.remove(dp);
			session.setAttribute(PACKAGECODE_LIST, plist);
		}
		PackageCode pc = packageCodeService.queryByProperty("code", code);
		if(null!=pc){
			packageCodeService.deleteObj(pc.getId());
		}
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "删除成功!");
		return modelMap;
	}

	@RequestMapping("/updatePackageCodeSession.do")
	@ResponseBody
	public void updatePackageCodeSession(PackageCodeVO pckageCode,HttpSession session){
		List<PackageCode> plist = (List<PackageCode>) session.getAttribute(PACKAGECODE_LIST);
		PackageCode dp = new PackageCode();
		for(PackageCode p:plist){
			if(p.getCode().equals(pckageCode.getCode())){
				p.setBoxamount(pckageCode.getBoxamount());
			}
		}
	}

	@RequestMapping("/queryPackageCodeByIds.do")
	@ResponseBody
	public List<PackageCodeVO> queryPackageCodeByIds(String ids,HttpServletRequest request){
		List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		List<PackageCode> list = packageCodeService.queryPackageCodeByIds(listIds);
		 String dir = request.getServletContext().getRealPath("/");
	        List<PackageCodeVO> vos = new ArrayList<>();
	        for(int i = 0 ;i<list.size();i++) {
	        	PackageCode packageCode = list.get(i);
	        	PackageCodeVO vo = new PackageCodeVO(); 
	        	BeanUtils.copyProperties(packageCode,vo);
	        	vo.setCreateDate(packageCode.getCreateDate().getTime());
	            vo.setQrPath(qrEncoder.generatePR(packageCode.getCode().toString(),dir, "console/qr/"));
	            vos.add(vo);
	        }
		return vos; 
	}
	/**
	 * 根据code查询包装条码信息
	 * @param code
	 * @return
	 */
	@RequestMapping("/queryPackageCodeByCode.do")
	@ResponseBody
	public PackageCode queryPackageCodeByCode(String code){
		PackageCode pc = packageCodeService.queryByProperty("code", code);
		return pc;
	}
	/**
	 * 查询包装条码信息
	 * @return
	 */
	@RequestMapping("/queryPackageCodes.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ModelMap queryPackageCodes(String from,String to,String customer,String inventoryCode,String batchNumber,String saleOrderNo
			,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		ModelMap modelMap = new ModelMap();
		String hql = "from PackageCode pc where 1=1 ";
		List<Object> params = new ArrayList<>();
		int i = 0;
		try {
			format.applyPattern("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(from)){
				hql += " and pc.createDate>=?" + i++;
				params.add(format.parse(from + " 00:00:00"));
			}
			if(!StringUtils.isEmpty(to)){
				hql += " and pc.createDate<=?" + i++;
				params.add(format.parse(to + " 23:59:59"));
			}
			format.applyPattern("yyyyMMdd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(!StringUtils.isEmpty(inventoryCode)){
			hql += " and pc.inventoryCode =?" + i++;
			params.add(inventoryCode);
		}
		if(!StringUtils.isEmpty(batchNumber)){
			hql += " and pc.batchNumber like ?" + i++;
			params.add("%" + batchNumber + "%");
		}
		if(!StringUtils.isEmpty(customer)){
			hql += " and pc.customer like ?" + i++;
			params.add("%" + customer + "%");
		}
		if(!StringUtils.isEmpty(saleOrderNo)){
			hql += " and saleNo like ?" + i++;
			params.add("%" + saleOrderNo + "%");
		}
		hql += " order by pc.createDate desc";
		Pager<PackageCode> pager = packageCodeService.queryObjs(hql, page, rows, params.toArray());
		modelMap.addAttribute("rows", pager.getData());
		modelMap.addAttribute("total",pager.getTotalCount());
		return modelMap;
	}
    /**
     * 根据id删除包装条码
     * @param id
     * @return
     */
    @RequestMapping("deleteById.do")
    @ResponseBody
	public ModelMap deleteById(String id){
	    ModelMap modelMap = new ModelMap();
        if(id!=null && id.contains("'")) {
            id = id.replace("'", "");
        }
        packageCodeService.deleteObj(Long.valueOf(id));
        modelMap.addAttribute("statusCode",200);
        modelMap.addAttribute("title", "操作提示");
        modelMap.addAttribute("message","删除成功!");
	    return modelMap;
    }
} 
