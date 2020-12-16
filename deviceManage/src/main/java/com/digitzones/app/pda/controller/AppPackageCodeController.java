package com.digitzones.app.pda.controller;

import com.digitzones.model.PackageCode;
import com.digitzones.model.PackageCodeAndBoxBarMapping;
import com.digitzones.model.User;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.vo.BoxBarVO;
import com.digitzones.service.IPackageCodeAndBoxBarMappingService;
import com.digitzones.service.IPackageCodeService;
import com.digitzones.service.IUserService;
import com.digitzones.util.JwtTokenUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AppPackageCode")
public class AppPackageCodeController {
    @Autowired
    private IPackageCodeService packageCodeService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPackageCodeAndBoxBarMappingService packageCodeAndBoxBarMappingService;

    /**
     * 根据packageCode查询packagecode类
     */
    @RequestMapping("/queryPackageCodeByCode.do")
    @ResponseBody
    public ModelMap queryPackageCodeByCode(String code){
        ModelMap modelMap=new ModelMap();
        PackageCode packageCode=packageCodeService.queryByProperty("code",code);
        if(packageCode!=null){
            List<PackageCodeAndBoxBarMapping> list= packageCodeAndBoxBarMappingService.queryBoxBerByPackageCode(code);
            List<BoxBarVO> volist=new ArrayList<>();
            if(list.size()>0){
                for(PackageCodeAndBoxBarMapping  pbm:list){
                    volist.add(model2VO(pbm));
                }

            }

            modelMap.addAttribute("success",true);
            modelMap.addAttribute("packageCode",packageCode);
            modelMap.addAttribute("list",volist);
        }else {
            modelMap.addAttribute("success",false);
        }
        return modelMap;
    }

    /**
     * 扫描报工箱条码
     */
    @RequestMapping("/queryBoxBarbybarCode.do")
    @ResponseBody
    public ModelMap queryBoxBarbybarCode(Long BarCode,String packNo){
        ModelMap modelMap=new ModelMap();
        BoxBar b=boxBarService.queryBoxBarBybarCode(BarCode);
        PackageCode packageCode=packageCodeService.queryByProperty("code",packNo);
        if(b!=null){
            if(!b.getEnterWarehouse()){
                modelMap.addAttribute("msg","该箱未入库。");
                modelMap.addAttribute("success",false);
            }else if(packageCode!=null&&!packageCode.getInventoryCode().equals(b.getInventoryCode())){
                modelMap.addAttribute("msg","该箱物料与包装箱物料不符，请重新扫描。");
                modelMap.addAttribute("success",false);
            }else if(b.getPackingNum()==null||b.getPackingNum()==0){
                modelMap.addAttribute("msg","该箱没有可包装数量，请先销售出库。");
                modelMap.addAttribute("success",false);
            }else{
                modelMap.addAttribute("box",b);
                modelMap.addAttribute("success",true);
            }
        }else{
            modelMap.addAttribute("msg","箱条码不存在，请重新扫描");
            modelMap.addAttribute("success",false);
        }
        return modelMap;
    }


    /**
     *点击出货（添加报工箱条码与包装箱条码的关联）
     */
    @RequestMapping("/addPackageCodeAndBoxBarMapping.do")
    @ResponseBody
    public ModelMap addPackageCodeAndBoxBarMapping(String packNo,String Amounts,String BarCodes,HttpServletRequest request){
        ModelMap modelMap=new ModelMap();
        String username=JwtTokenUnit.getUsername(request.getHeader("accessToken"));
        User user=userService.queryByProperty("username", username);
        if(BarCodes!=null && BarCodes.contains("'")) {
            BarCodes = BarCodes.replace("'", "");
        }
        String[] barcodes= BarCodes.split(",");
        if(Amounts!=null && Amounts.contains("'")) {
            Amounts = Amounts.replace("'", "");
        }
        String[] ramounts= Amounts.split(",");

        try {
            packageCodeService.outWarehouse(packNo,barcodes,ramounts,user);
            modelMap.addAttribute("success",true);
            modelMap.addAttribute("msg","包装成功");
        }catch (Exception e){
            modelMap.addAttribute("success",false);
            modelMap.addAttribute("msg",e.getMessage());
        }
        return  modelMap;
    }

    /**
     * 将领域模型转换为值对象
     */
    private BoxBarVO model2VO(PackageCodeAndBoxBarMapping pbm) {
        if(pbm == null) {
            return null;
        }
        BoxBarVO vo=new BoxBarVO();
        BeanUtils.copyProperties(pbm.getBoxBar(),vo);
        vo.setAmountOfPerBox(pbm.getNumber());
        return vo;
    }

}
