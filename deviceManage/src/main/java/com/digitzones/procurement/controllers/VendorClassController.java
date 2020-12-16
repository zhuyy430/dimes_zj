package com.digitzones.procurement.controllers;

import com.digitzones.procurement.model.VendorClass;
import com.digitzones.procurement.service.IVendorClassService;
import com.digitzones.vo.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
/**
 * 供应商类别控制器
 */
@RestController
@RequestMapping("vendorClass")
public class VendorClassController {
    @Autowired
    private IVendorClassService vendorClassService;
    /**
     * 查询所有供应商类别
     * @return
     */
    @RequestMapping("queryAllVendorClasses.do")
    public List<Node> queryAllVendorClasses(){
        List<Node> nodes = new ArrayList<>();
        Node topNode = new Node();
        topNode.setCode("000");
        topNode.setName("供应商类型");
         nodes.add(topNode);

         List<Node> children = new ArrayList<>();
         topNode.setChildren(children);
        List<VendorClass> vendorClassList = vendorClassService.queryAllVendorClasses();
        if(!CollectionUtils.isEmpty(vendorClassList)){
            for(VendorClass vc : vendorClassList){
                Node node = new Node();
                node.setCode(vc.getCvcCode());
                node.setName(vc.getCvcName());
                children.add(node);
            }
        }
         return nodes;
    }
}
