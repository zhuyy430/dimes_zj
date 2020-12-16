package com.digitzones.xml;

import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.PO_Podetails;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.xml.model.Result;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 生成ERP到货单工具
 */
@Component
public  class ArrivalSlipUtil extends ParentUtil{
    /**
     * 生成ERP采购到货单
     * @param form  入库申请单
     * @param mapList
     * @return
     */
    public  Result generateArrivalSlip(WarehousingApplicationForm form, List<Map<PO_Podetails,List<BoxBar>>> mapList){
        Result result = init();
        if(result.getStatusCode().equals("300")){
            return result;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<ufinterface sender=\""+getConfig().getSender()+"\" receiver=\""+getConfig().getReceiver()+"\" roottag=\"arrivedgoods\" " +
                "docid=\"\" proc=\"add\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" display=\"采购到货单\" " +
                "family=\"采购管理\" dynamicdate=\""+getSdf().format(new Date())+"\" maxdataitems=\"\" bignoreextenduserdefines=\"y\" " +
                "timestamp=\"\" lastquerydate=\"\">" +
                "    <arrivedgoods>" +
                "        <header>" +
                "            <id></id>" +
                "            <code>"+form.getFormNo()+"</code>" +
                "            <businesstype>普通采购</businesstype>" +
                "            <purchasetypecode>10</purchasetypecode>" +
                "            <date>"+(form.getFormDate()==null?"":getSdf().format(form.getFormDate()))+" 00:00:00</date>" +
                "            <vendorcode>"+form.getVendorCode()+"</vendorcode>" +
                "            <departmentcode>"+form.getcDepCode()+"</departmentcode>" +
                "            <personcode>"+form.getcPersonCode()+"</personcode>" +
                "            <payconditioncode/>" +
                "            <foreigncurrency>人民币</foreigncurrency>" +
                "            <foreigncurrencyrate>1</foreigncurrencyrate>" +
                "            <idiscounttaxtype>0</idiscounttaxtype>" +
                "            <shipcode/>" +
                "            <memory/>" +
                "            <tax_rate>13</tax_rate>" +
                "            <maker>"+form.getApplierName()+"</maker>" +
                "            <rejecttag>0</rejecttag>" +
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
                "            <define11/>" +
                "            <define12/>" +
                "            <define13/>" +
                "            <define14/>" +
                "            <define15/>" +
                "            <define16/>" +
                "            <billtype>0</billtype>" +
                "            <cvenpuomprotocol/>" +
                "            <cvenname>"+form.getVendorName()+"</cvenname>" +
                "            <cvenabbname>"+form.getVendorName()+"</cvenabbname>" +
                "            <cverifier/>" +
                "            <cauditdate/>" +
                "        </header>" +
                "        <body>";
        String entries = "";
        if(!CollectionUtils.isEmpty(mapList)) {
            for (int i = 0; i < mapList.size(); i++) {
                Map<PO_Podetails, List<BoxBar>> map = mapList.get(i);
                for (Map.Entry<PO_Podetails, List<BoxBar>> entry : map.entrySet()) {
                    List<BoxBar> boxBarList = entry.getValue();
                    PO_Podetails po_podetails = entry.getKey();
                    for (BoxBar detail : boxBarList) {
                        entries += "            <entry>" +
                                "                <mainid></mainid>" +
                                "                <warehousecode>" + detail.getWarehouseCode() + "</warehousecode>" +
                                "                <inventorycode>" + detail.getInventoryCode() + "</inventorycode>" +
                                "                <number/>" +
                                "                <quantity>" + detail.getAmountOfPerBox()+ "</quantity>" +
                                "                <taxrate>" + po_podetails.getiPerTaxRate() + "</taxrate>" +
                                "                <ischecked>1</ischecked>" +
                                "                <originalprice>" + po_podetails.getiNatUnitPrice() + "</originalprice>" +
                                "                <originaltaxedprice>" + po_podetails.getiTaxPrice() + "</originaltaxedprice>" +
                                "                <originalmoney>" + po_podetails.getiNatUnitPrice() * detail.getAmountOfPerBox() + "</originalmoney>" +
                                "                <assistantunit/>" +
                                "                <originaltax>" + (po_podetails.getiTaxPrice() - po_podetails.getiNatUnitPrice()) * detail.getAmountOfPerBox() + "</originaltax>" +
                                "                <originalsum>" + po_podetails.getiTaxPrice() * detail.getAmountOfPerBox() + "</originalsum>" +
                                "                <price>" + po_podetails.getiUnitPrice() + "</price>" +
                                "                <money>" + po_podetails.getiNatUnitPrice() * detail.getAmountOfPerBox() + "</money>" +
                                "                <tax>" + (po_podetails.getiTaxPrice() - po_podetails.getiNatUnitPrice()) * detail.getAmountOfPerBox() + "</tax>" +
                                "                <sum>" + po_podetails.getiTaxPrice() * detail.getAmountOfPerBox() + "</sum>" +
                                "                <free1/>" +
                                "                <free2/>" +
                                "                <taxrate>" + po_podetails.getiPerTaxRate() + "</taxrate>" +
                                "                <imassdate/>" +
                                "                <cmassunit/>" +
                                "                <bexigency>0</bexigency>" +
                                "                <define22/>" +
                                "                <define23/>" +
                                "                <define24/>" +
                                "                <define25>" + detail.getPositonCode() + "</define25>" +
                                "                <define26/>" +
                                "                <define27/>" +
                                "                <define28/>" +
                                "                <define29/>" +
                                "                <define30/>" +
                                "                <define31/>" +
                                "                <define32/>" +
                                "                <define33/>" +
                                "                <define34/>" +
                                "                <define35/>" +
                                "                <define36/>" +
                                "                <define37/>" +
                                "                <itemclasscode/>" +
                                "                <itemcode/>" +
                                "                <itemname/>" +
                                "                <free3/>" +
                                "                <free4/>" +
                                "                <free5/>" +
                                "                <free6/>" +
                                "                <free7/>" +
                                "                <free8/>" +
                                "                <free9/>" +
                                "                <free10>" + po_podetails.getId() + "</free10>" +
                                "                <unitid/>" +
                                "                <closer/>" +
                                "                <serial>" + detail.getFurnaceNumber() + "</serial>" +
                                "                <makedate/>" +
                                "                <invaliddate/>" +
                                "                <realnumber/>" +
                                "                <realquantity></realquantity>" +
                                "                <refusenumber/>" +
                                "                <refusequantity/>" +
                                "                <validquantity>0</validquantity>" +
                                "                <validNum/>" +
                                "                <iexpiratdatecalcu>0</iexpiratdatecalcu>" +
                                "                <cexpirationdate/>" +
                                "                <dexpirationdate/>" +
                                "                <cbatchproperty1/>" +
                                "                <cbatchproperty2/>" +
                                "                <cbatchproperty3/>" +
                                "                <cbatchproperty4/>" +
                                "                <cbatchproperty5/>" +
                                "                <cbatchproperty6>" + detail.getStoveNumber() + "</cbatchproperty6>" +
                                "                <cbatchproperty7>" + detail.getManufacturer() + "</cbatchproperty7>" +
                                "                <cbatchproperty8/>" +
                                "                <cbatchproperty9/>" +
                                "                <cbatchproperty10/>" +
                                "                <cinvname>" + detail.getInventoryName() + "</cinvname>" +
                                "                <cinvaddcode/>" +
                                "                <cinvstd>" + detail.getSpecificationType() + "</cinvstd>" +
                                "                <ccomunitcode></ccomunitcode>" +
                                "                <cinvm_unit>" + detail.getUnitName() + "</cinvm_unit>" +
                                "                <cinva_unit/>" +
                                "                <cordercode>" + detail.getPurchasingNo() + "</cordercode>" +
                                "                <iordertype>0</iordertype>" +
                                "                <iorderdid/>" +
                                "                <csoordercode/>" +
                                "                <iorderseq/>" +
                                "                <cinvdefine1/>" +
                                "                <cinvdefine2/>" +
                                "                <cinvdefine3/>" +
                                "                <cinvdefine4/>" +
                                "                <cinvdefine5/>" +
                                "                <cinvdefine6>钢材</cinvdefine6>" +
                                "                <cinvdefine7/>" +
                                "                <cinvdefine8/>" +
                                "                <cinvdefine9/>" +
                                "                <cinvdefine10/>" +
                                "                <cinvdefine11/>" +
                                "                <cinvdefine12/>" +
                                "                <cinvdefine13/>" +
                                "                <cinvdefine14/>" +
                                "                <cinvdefine15/>" +
                                "                <cinvdefine16/>" +
                                "                <cfactorycode/>" +
                                "            </entry>";
                    }
                }
            }
        }
              entries+= "        </body>" +
                "    </arrivedgoods>" +
                "</ufinterface>";

              xml+=entries;
        checkResult(xml,result);
        return result;
    }
}
