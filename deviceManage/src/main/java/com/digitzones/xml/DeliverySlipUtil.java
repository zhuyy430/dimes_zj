package com.digitzones.xml;

import com.digitzones.model.DeliveryPlanDetail;
import com.digitzones.model.SalesSlip;
import com.digitzones.xml.model.Result;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
 * 生成ERP发货单工具
 */
@Component
public  class DeliverySlipUtil extends ParentUtil{
    /**
     * 生成ERP发货单
     * @param detail  发货计划详情
     * @param salesSlip 销售订单
     * @return
     */
    public  Result generateDeliverySlip(DeliveryPlanDetail detail, SalesSlip salesSlip){
        Result result = init();
        if(result.getStatusCode().equals("300")){
            return result;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<ufinterface sender=\""+getConfig().getSender()+"\" receiver=\""+getConfig().getReceiver()+"\" roottag=\"consignment\" docid=\"\" proc=\"add\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" display=\"销售发货单\" family=\"销售管理\" dynamicdate=\""+getSdf().format(new Date())+"\" maxdataitems=\"20000\" bignoreextenduserdefines=\"y\" timestamp=\"0x000000000042D974\" lastquerydate=\"2019-08-29 09:24:14\">" +
                "    <consignment>" +
                "        <header>" +
                "            <id></id>" +
                "            <outid/>" +
                "            <code></code>" +
                "            <vouchertype>05</vouchertype><!--默认-->" +
                "            <saletype>10</saletype><!--默认-->" +
                "            <date>"+getSdf().format(detail.getDeliveryPlan().getDeliverDate())+"</date>" +
                "            <deptcode>103</deptcode><!--默认-->" +
                "            <personcode>"+salesSlip.getPersonCode()+"</personcode><!--业务人员-->" +
                "            <custcode>"+salesSlip.getCustomerCode()+"</custcode>" +
                "            <paycondition_code/>" +
                "            <shippingchoice/>" +
                "            <address/>" +
                "            <currency_name>"+salesSlip.getCurrency()+"</currency_name>" +
                "            <currency_rate>"+salesSlip.getExchangeRate()+"</currency_rate>" +
                "            <taxrate>"+(salesSlip.getTaxRate()==null?13:salesSlip.getTaxRate())+"</taxrate>" +
                "            <beginflag>0</beginflag><!--默认-->" +
                "            <returnflag>0</returnflag><!--默认-->" +
                "            <remark/>" +
                "            <define1/>" +
                "            <define2/>" +
                "            <define3/>" +
                "            <define4/>" +
                "            <define5/>" +
                "            <define6/>" +
                "            <define7/>" +
                "            <define8/>" +
                "            <define9/>" +
                "            <define10/>" +
                "            <define11>"+salesSlip.getFormNo()+"</define11>" +
                "            <define12>DIMES</define12>" +
                "            <define13/>" +
                "            <define14/>" +
                "            <define15/>" +
                "            <define16/>" +
                "            <maker>demo</maker>" +
                "            <sale_cons_flag>0</sale_cons_flag>" +
                "            <retail_custname>"+salesSlip.getCustomerName()+"</retail_custname>" +
                "            <operation_type>普通销售</operation_type><!--默认-->" +
                "            <verifydate/>" +
                "            <modifydate/>" +
                "            <caddcode/>" +
                "            <cverifier/>" +
                "            <cdeliverunit/>" +
                "            <cdeliveradd/>" +
                "            <ccontactname/>" +
                "            <cofficephone/>" +
                "            <cmobilephone/>" +
                "            <cgatheringplan/>" +
                "            <dcreditstart/>" +
                "            <icreditdays/>" +
                "            <dgatheringdate/>" +
                "            <bcredit>否</bcredit><!--默认-->" +
                "            <cbooktype/>" +
                "            <cbookdepcode/>" +
                "            <ccuspersoncode/>" +
                "            <ccusperson/>" +
                "        </header>" +
                "        <body>" +
                "            <entry>" +
                "                <headid></headid>" +
                "                <body_outid/>" +
                "                <warehouse_code>"+detail.getWarehouseCode()+"</warehouse_code>" +
                "                <inventory_code>"+detail.getInventoryCode()+"</inventory_code>" +
                "                <quantity>"+detail.getAmountOfSended()+"</quantity><!--发货数量-->" +
                "                <num/>" +
                "                <ccomunitcode>"+detail.getUnitCode()+"</ccomunitcode>" +
                "                <cinvm_unit>"+detail.getUnitName()+"</cinvm_unit>" +
                "                <cinva_unit/>" +
                "                <quotedprice>0</quotedprice><!--默认-->" +
                "                <price>"+salesSlip.getUnitPrice()+"</price>" +
                "                <taxprice>"+salesSlip.getTaxUnitPrice()+"</taxprice>" +
                "                <money>"+salesSlip.getUnitPrice()*detail.getAmountOfSended()+"</money><!--计算-->" +
                "                <tax>"+(salesSlip.getTaxUnitPrice()-salesSlip.getUnitPrice())*detail.getAmountOfSended()+"</tax><!--计算-->" +
                "                <sum>"+salesSlip.getTaxUnitPrice()*detail.getAmountOfSended()+"</sum><!--计算-->" +
                "                <discount>"+salesSlip.getDiscount()+"</discount>" +
                "                <natprice>"+salesSlip.getNatUnitPrice()+"</natprice>" +
                "                <natmoney>"+salesSlip.getNatUnitPrice()*detail.getAmountOfSended()+"</natmoney>" +
                "                <nattax>"+(salesSlip.getTaxUnitPrice()-salesSlip.getUnitPrice())*detail.getAmountOfSended()+"</nattax>" +
                "                <natsum>"+salesSlip.getTaxUnitPrice()*detail.getAmountOfSended()+"</natsum>" +
                "                <natdiscount>0</natdiscount><!--默认-->" +

                "                <batch>"+detail.getBatchNumber()+"</batch>" +

                "                <remark/>" +
                "                <backflag>正常</backflag><!--默认-->" +
                "                <overdate/>" +
                "                <backquantity>"+detail.getAmountOfSended()+"</backquantity><!--同quantity-->" +
                "                <backnum/>" +
                "                <discount1>100</discount1><!--默认-->" +
                "                <discount2>100</discount2><!--默认-->" +
                "                <inventory_printname>"+detail.getInventoryName()+"</inventory_printname>" +
                "                <taxrate>"+(salesSlip.getTaxRate()==null?13:salesSlip.getTaxRate())+"</taxrate>" +
                "                <item_class/>" +
                "                <item_classname/>" +
                "                <item_code/>" +
                "                <item_name/>" +
                "                <retail_price>0</retail_price><!--默认-->" +
                "                <retail_money>0</retail_money><!--默认-->" +
                "                <vendor_name/>" +
                "                <unitrate/>" +
                "                <unit_code/>" +
                "                <free1/>" +
                "                <free2/>" +
                "                <free3/>" +
                "                <free4/>" +
                "                <free5/>" +
                "                <free6/>" +
                "                <free7/>" +
                "                <free8/>" +
                "                <free9/>" +
                "                <free10></free10>" +
                "                <define22/>" +
                "                <define23>"+detail.getInspectionReport()+"</define23>" +
                "                <define24>"+detail.getBatchNumberOfSended()+"</define24>" +
                "                <define25>DIMES</define25>" +
                "                <define26/>" +
                "                <define27/>" +
                "                <define28/>" +
                "                <define29/>" +
                "                <define30>"+salesSlip.getSosId()+"</define30>" +
                "                <define31/>" +
                "                <define32/>" +
                "                <define33/>" +
                "                <define34/>" +
                "                <define35/>" +
                "                <define36/>" +
                "                <define37/>" +
                "                <batchproperty1/>" +
                "                <batchproperty2/>" +
                "                <batchproperty3/>" +
                "                <batchproperty4/>" +
                "                <batchproperty5/>" +
                "                <batchproperty6/>" +
                "                <batchproperty7/>" +
                "                <batchproperty8/>" +
                "                <batchproperty9/>" +
                "                <batchproperty10/>" +
                "                <ccorcode/>" +
                "                <ccusinvcode/>" +
                "                <ccusinvname/>" +
                "                <ippartseqid/>" +
                "                <ippartqty/>" +
                "                <ippartid/>" +
                "                <bqaneedcheck>否</bqaneedcheck><!--默认-->" +
                "                <bqaurgency>否</bqaurgency><!--默认-->" +
                "                <cmassunit/>" +
                "                <imassdate/>" +
                "                <dmdate/>" +
                "                <cordercode>"+detail.getFormNo()+"</cordercode><!--销售订单主表ID-->" +
                "                <iorderrowno>"+salesSlip.getRowNo()+"</iorderrowno><!--销售订单详情行号-->" +
                "                <cvmivencode/>" +
                "                <irowno>1</irowno><!--发货单行号-->" +
                "                <ExpirationDate/>" +
                "                <ExpiratDateCalcu/>" +
                "                <ExpirationItem/>" +
                "                <ReasonCode/>" +
                "                <cposition/>" +
                "                <cbookwhcode/>" +
                "                <retailpredate/>" +
                "                <retailpromode/>" +
                "                <bsaleprice>0</bsaleprice><!--默认-->" +
                "                <bgift>0</bgift><!--默认-->" +
                "                <fcusminprice>0</fcusminprice><!--默认-->" +
                "                <cparentcode/>" +
                "                <cchildcode/>" +
                "                <icalctype/>" +
                "                <fchildqty/>" +
                "                <fchildrate/>" +
                "                <retailrealamount>0</retailrealamount><!--默认-->" +
                "                <retailsettleamount>0</retailsettleamount><!--默认-->" +
                "                <cfactorycode/>" +
                "            </entry>" +
                "        </body>" +
                "    </consignment>" +
                "</ufinterface>";
        checkResult(xml,result);
        return result;
    }
}
