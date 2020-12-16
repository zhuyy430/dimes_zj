package com.digitzones.procurement.controllers;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.Customer;
import com.digitzones.procurement.service.ICustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    /**
     * 查询客户信息
     * @param ccusCode
     * @param ccusName
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/queryCustomers.do")
    @ResponseBody
    public ModelMap queryCustomers(String ccusCode,String ccusName, @RequestParam(value="rows",defaultValue="20")Integer rows, @RequestParam(defaultValue="1")Integer page) {
        String hql = "from Customer c where 1=1 ";
        if(!StringUtils.isEmpty(ccusCode)){
            hql += " and ccusCode like '%" + ccusCode + "%'";
        }
        if(!StringUtils.isEmpty(ccusName)){
            hql += " and ccusName like '%" + ccusName + "%'";
        }
        Pager<Customer> pager = customerService.queryObjs(hql,page,rows,new Object[]{});
        ModelMap mm = new ModelMap();
        mm.addAttribute("rows",pager.getData());
        mm.addAttribute("total", pager.getTotalCount());
        return mm;
    }
}
