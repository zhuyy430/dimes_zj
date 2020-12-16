package com.digitzones.xml;
import com.digitzones.model.WorkSheet;
import com.digitzones.xml.model.Result;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;
/**
 * 生成ERP报工入库单工具
 */
@Component
public  class JobBookingSlipUtil extends ParentUtil{
    /**
     * 生成ERP报工入库单
     * @param workSheet  工单
     * @param map  入库总数量
     * @return
     */
    public  Result generateJobBookingSlip(WorkSheet workSheet, Map<String,Object> map){
        Result result = init();
        if(result.getStatusCode().equals("300")){
            return result;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<ufinterface sender=\""+getConfig().getSender()+"\" receiver=\""+getConfig().getReceiver()+"\" " +
                "roottag=\"storein\" docid=\"\" proc=\"add\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" " +
                "display=\"入库单\" family=\"库存管理\" dynamicdate=\""+getSdf().format(new Date())+"\" maxdataitems=\"20000\" " +
                "bignoreextenduserdefines=\"y\" timestamp=\"\" lastquerydate=\"\">" +
                "    <storein>" +
                "    <header>" +
                "      <id></id>" +
                "      <receiveflag>1</receiveflag><!--默认-->" +
                "      <vouchtype>10</vouchtype><!--默认-->" +
                "      <businesstype>成品入库</businesstype><!--默认-->" +
                "      <source>生产订单</source><!--默认-->" +
                "      <businesscode/>" +
                "      <warehousecode>"+map.get("warehouseCode")+"</warehousecode><!--PDA入库仓库-->" +
                "      <date>"+getSdf().format((Date) map.get("warehouseDate"))+"</date>" +
                "      <code>"+map.get("warehouseNo")+"</code><!--入库单号-->" +
                "      <sourcecodels/>" +
                "      <receivecode>102</receivecode>" +
                "      <departmentcode>"+workSheet.getDepartmentCode()+"</departmentcode><!--生产订单上的部门代码-->" +
                "      <personcode/>" +
                "      <purchasetypecode/>" +
                "      <saletypecode/>" +
                "      <customercode/>" +
                "      <vendorcode/>" +
                "      <ordercode/>" +
                "      <quantity/>" +
                "      <arrivecode/>" +
                "      <billcode/>" +
                "      <consignmentcode/>" +
                "      <arrivedate/>" +
                "      <checkcode/>" +
                "      <checkdate/>" +
                "      <checkperson/>" +
                "      <templatenumber>63</templatenumber><!--模板号\\默认-->" +
                "      <serial>"+workSheet.getBatchNumber()+"</serial><!--工单批号/测试不给值-->" +
                "      <handler/>" +
                "      <memory/>" +
                "      <maker>"+map.get("makerName")+"</maker><!--制单人-->" +
                "      <chandler/>" +
                "      <define1/>" +
                "      <define2/>" +
                "      <define3/>" +
                "      <define4/>" +
                "      <define5/>" +
                "      <define6/>" +
                "      <define7/>" +
                "      <define8/>" +
                "      <define9/>" +
                "      <define10>"+workSheet.getWorkPieceCode()+"</define10>" +
                "      <define11>"+workSheet.getMoId()+"</define11>" +
                "      <define12>"+workSheet.getMocode()+"</define12>" +
                "      <define13>" + workSheet.getMoDId() +  "</define13>" +
                "      <define14/>" +
                "      <define15/>" +
                "      <define16/>" +
                "      <auditdate/>" +
                "      <taxrate/>" +
                "      <exchname/>" +
                "      <exchrate/>" +
                "      <discounttaxtype/>" +
                "      <contact/>" +
                "      <phone/>" +
                "      <mobile/>" +
                "      <address/>" +
                "      <conphone/>" +
                "      <conmobile/>" +
                "      <deliverunit/>" +
                "      <contactname/>" +
                "      <officephone/>" +
                "      <mobilephone/>" +
                "      <psnophone/>" +
                "      <psnmobilephone/>" +
                "      <shipaddress/>" +
                "      <addcode/>" +
                "      <bomfirst>0</bomfirst><!--默认-->" +
                "      <bpufirst>0</bpufirst><!--默认-->" +
                "      <cvenpuomprotocol/>" +
                "      <dcreditstart/>" +
                "      <icreditperiod/>" +
                "      <dgatheringdate/>" +
                "      <bcredit/>" +
                "    </header>" +
                "    <body>" +
                "      <entry>" +
                "        <id></id>" +
                "        <autoid></autoid>" +
                "        <barcode/>" +
                "        <inventorycode>"+workSheet.getWorkPieceCode()+"</inventorycode><!--物料代码-->" +
                "        <invname>"+workSheet.getWorkPieceName()+"</invname><!--物料名称-->" +
                "        <free1/>" +
                "        <free2/>" +
                "        <free3/>" +
                "        <free4/>" +
                "        <free5/>" +
                "        <free6/>" +
                "        <free7/>" +
                "        <free8/>" +
                "        <free9/>" +
                "        <free10/>" +
                "        <shouldquantity>"+workSheet.getProductCount()+"</shouldquantity><!--生产工单上的生产数量-->" +
                "        <shouldnumber/>" +
                "        <quantity>"+map.get("amount")+"</quantity><!--PDA入库数量-->" +
                "        <cmassunitname>"+workSheet.getUnitName()+"</cmassunitname><!--计量单位-->" +
                "        <assitantunit/>" +
                "        <assitantunitname/>" +
                "        <irate/>" +
                "        <number/>" +
                "        <price/>" +
                "        <cost/>" +
                "        <facost/>" +
                "        <iaprice/>" +
                "        <plancost/>" +
                "        <planprice/>" +
                "        <serial>"+map.get("detailBatchNumber")+"</serial><!--批号-->" +
                "        <makedate/>" +
                "        <validdate/>" +
                "        <transitionid/>" +
                "        <subbillcode/>" +
                "        <subpurchaseid/>" +
                "        <position/>" +
                "        <itemclasscode/>" +
                "        <itemclassname/>" +
                "        <itemcode/>" +
                "        <itemname/>" +
                "        <define22></define22>" +
                "        <define23></define23>" +
                "        <define24>"+workSheet.getStoveNumber()+"</define24>" +
                "        <define25/>" +
                "        <define26/>" +
                "        <define27>"+workSheet.getMoallocateQty()+"</define27>" +
                "        <define28>"+workSheet.getMoDId()+"</define28>" +
                "        <define29>DIMES</define29>" +
                "        <define30>" + workSheet.getBatchNumber() + "</define30>" +
                "        <define31/>" +
                "        <define32/>" +
                "        <define33/>" +
                "        <define34/>" +
                "        <define35/>" +
                "        <define36/>" +
                "        <define37/>" +
                "        <subconsignmentid/>" +
                "        <delegateconsignmentid/>" +
                "        <subproducingid>"+workSheet.getMoDId()+"</subproducingid><!--生产订单子表ID-->" +
                "        <subcheckid/>" +
                "        <cRejectCode/>" +
                "        <iRejectIds/>" +
                "        <cCheckPersonCode/>" +
                "        <dCheckDate/>" +
                "        <cCheckCode/>" +
                "        <iMassDate/>" +
                "        <ioritaxcost/>" +
                "        <ioricost/>" +
                "        <iorimoney/>" +
                "        <ioritaxprice/>" +
                "        <iorisum/>" +
                "        <taxrate/>" +
                "        <taxprice/>" +
                "        <isum/>" +
                "        <massunit/>" +
                "        <vmivencode/>" +
                "        <materialfee/>" +
                "        <processcost/>" +
                "        <processfee/>" +
                "        <dmsdate/>" +
                "        <batchproperty1/>" +
                "        <batchproperty2/>" +
                "        <batchproperty3/>" +
                "        <batchproperty4/>" +
                "        <batchproperty5/>" +
                "        <batchproperty6/>" +
                "        <batchproperty7/>" +
                "        <batchproperty8/>" +
                "        <batchproperty9/>" +
                "        <batchproperty10/>" +
                "        <iexpiratdatecalcu>0</iexpiratdatecalcu><!--默认-->" +
                "        <dexpirationdate/>" +
                "        <cexpirationdate/>" +
                "        <memory/>" +
                "      </entry>" +
                "    </body>" +
                "  </storein>" +
                "</ufinterface>";
        checkResult(xml,result);
        return result;
    }

    /**
     * 生成ERP NG入库单
     * @param workSheet  工单
     * @param map  入库总数量
     * @return
     */
    public  Result generateNGJobBookingSlip(WorkSheet workSheet, Map<String,Object> map){
        Result result = init();
        if(result.getStatusCode().equals("300")){
            return result;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<ufinterface sender=\""+getConfig().getSender()+"\" receiver=\""+getConfig().getReceiver()+"\" " +
                "roottag=\"storein\" docid=\"\" proc=\"add\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" " +
                "display=\"入库单\" family=\"库存管理\" dynamicdate=\""+getSdf().format(new Date())+"\" maxdataitems=\"20000\" " +
                "bignoreextenduserdefines=\"y\" timestamp=\"\" lastquerydate=\"\">" +
                "    <storein>" +
                "    <header>" +
                "      <id></id>" +
                "      <receiveflag>1</receiveflag><!--默认-->" +
                "      <vouchtype>08</vouchtype><!--默认-->" +
                "      <businesstype>其他入库</businesstype><!--默认-->" +
                "      <source>库存</source><!--默认-->" +
                "      <businesscode/>" +
                "      <warehousecode>"+map.get("warehouseCode")+"</warehousecode><!--PDA入库仓库-->" +
                "      <date>"+getSdf().format((Date) map.get("warehouseDate"))+"</date>" +
                "      <code>"+map.get("warehouseNo")+"</code><!--入库单号-->" +
                "      <sourcecodels/>" +
                "      <receivecode>102</receivecode>" +
                "      <departmentcode>"+workSheet.getDepartmentCode()+"</departmentcode><!--生产订单上的部门代码-->" +
                "      <personcode/>" +
                "      <purchasetypecode/>" +
                "      <saletypecode/>" +
                "      <customercode/>" +
                "      <vendorcode/>" +
                "      <ordercode/>" +
                "      <quantity/>" +
                "      <arrivecode/>" +
                "      <billcode/>" +
                "      <consignmentcode/>" +
                "      <arrivedate/>" +
                "      <checkcode/>" +
                "      <checkdate/>" +
                "      <checkperson/>" +
                "      <templatenumber>63</templatenumber><!--模板号\\默认-->" +
                "      <serial>"+workSheet.getBatchNumber()+"</serial><!--工单批号/测试不给值-->" +
                "      <handler/>" +
                "      <memory/>" +
                "      <maker>"+map.get("makerName")+"</maker><!--制单人-->" +
                "      <chandler/>" +
                "      <define1/>" +
                "      <define2/>" +
                "      <define3/>" +
                "      <define4/>" +
                "      <define5/>" +
                "      <define6/>" +
                "      <define7/>" +
                "      <define8/>" +
                "      <define9/>" +
                "      <define10>"+workSheet.getWorkPieceCode()+"</define10>" +
                "      <define11>"+workSheet.getMoId()+"</define11>" +
                "      <define12>"+workSheet.getMocode()+"</define12>" +
                "      <define13>" + workSheet.getMoDId() +  "</define13>" +
                "      <define14/>" +
                "      <define15/>" +
                "      <define16/>" +
                "      <auditdate/>" +
                "      <taxrate/>" +
                "      <exchname/>" +
                "      <exchrate/>" +
                "      <discounttaxtype/>" +
                "      <contact/>" +
                "      <phone/>" +
                "      <mobile/>" +
                "      <address/>" +
                "      <conphone/>" +
                "      <conmobile/>" +
                "      <deliverunit/>" +
                "      <contactname/>" +
                "      <officephone/>" +
                "      <mobilephone/>" +
                "      <psnophone/>" +
                "      <psnmobilephone/>" +
                "      <shipaddress/>" +
                "      <addcode/>" +
                "      <bomfirst>0</bomfirst><!--默认-->" +
                "      <bpufirst>0</bpufirst><!--默认-->" +
                "      <cvenpuomprotocol/>" +
                "      <dcreditstart/>" +
                "      <icreditperiod/>" +
                "      <dgatheringdate/>" +
                "      <bcredit/>" +
                "    </header>" +
                "    <body>" +
                "      <entry>" +
                "        <id></id>" +
                "        <autoid></autoid>" +
                "        <barcode/>" +
                "        <inventorycode>"+workSheet.getWorkPieceCode()+"</inventorycode><!--物料代码-->" +
                "        <invname>"+workSheet.getWorkPieceName()+"</invname><!--物料名称-->" +
                "        <free1/>" +
                "        <free2/>" +
                "        <free3/>" +
                "        <free4/>" +
                "        <free5/>" +
                "        <free6/>" +
                "        <free7/>" +
                "        <free8/>" +
                "        <free9/>" +
                "        <free10/>" +
                "        <shouldquantity>"+workSheet.getProductCount()+"</shouldquantity><!--生产工单上的生产数量-->" +
                "        <shouldnumber/>" +
                "        <quantity>"+map.get("amount")+"</quantity><!--PDA入库数量-->" +
                "        <cmassunitname>"+workSheet.getUnitName()+"</cmassunitname><!--计量单位-->" +
                "        <assitantunit/>" +
                "        <assitantunitname/>" +
                "        <irate/>" +
                "        <number/>" +
                "        <price/>" +
                "        <cost/>" +
                "        <facost/>" +
                "        <iaprice/>" +
                "        <plancost/>" +
                "        <planprice/>" +
                "        <serial>"+map.get("detailBatchNumber")+"</serial><!--批号-->" +
                "        <makedate/>" +
                "        <validdate/>" +
                "        <transitionid/>" +
                "        <subbillcode/>" +
                "        <subpurchaseid/>" +
                "        <position/>" +
                "        <itemclasscode/>" +
                "        <itemclassname/>" +
                "        <itemcode/>" +
                "        <itemname/>" +
                "        <define22></define22>" +
                "        <define23></define23>" +
                "        <define24>"+workSheet.getStoveNumber()+"</define24>" +
                "        <define25/>" +
                "        <define26/>" +
                "        <define27>"+workSheet.getMoallocateQty()+"</define27>" +
                "        <define28>"+workSheet.getMoDId()+"</define28>" +
                "        <define29>DIMES</define29>" +
                "        <define30>" + workSheet.getBatchNumber() + "</define30>" +
                "        <define31/>" +
                "        <define32/>" +
                "        <define33/>" +
                "        <define34/>" +
                "        <define35/>" +
                "        <define36/>" +
                "        <define37/>" +
                "        <subconsignmentid/>" +
                "        <delegateconsignmentid/>" +
                "        <subproducingid>"+workSheet.getMoDId()+"</subproducingid><!--生产订单子表ID-->" +
                "        <subcheckid/>" +
                "        <cRejectCode/>" +
                "        <iRejectIds/>" +
                "        <cCheckPersonCode/>" +
                "        <dCheckDate/>" +
                "        <cCheckCode/>" +
                "        <iMassDate/>" +
                "        <ioritaxcost/>" +
                "        <ioricost/>" +
                "        <iorimoney/>" +
                "        <ioritaxprice/>" +
                "        <iorisum/>" +
                "        <taxrate/>" +
                "        <taxprice/>" +
                "        <isum/>" +
                "        <massunit/>" +
                "        <vmivencode/>" +
                "        <materialfee/>" +
                "        <processcost/>" +
                "        <processfee/>" +
                "        <dmsdate/>" +
                "        <batchproperty1/>" +
                "        <batchproperty2/>" +
                "        <batchproperty3/>" +
                "        <batchproperty4/>" +
                "        <batchproperty5/>" +
                "        <batchproperty6/>" +
                "        <batchproperty7/>" +
                "        <batchproperty8/>" +
                "        <batchproperty9/>" +
                "        <batchproperty10/>" +
                "        <iexpiratdatecalcu>0</iexpiratdatecalcu><!--默认-->" +
                "        <dexpirationdate/>" +
                "        <cexpirationdate/>" +
                "        <memory/>" +
                "      </entry>" +
                "    </body>" +
                "  </storein>" +
                "</ufinterface>";
        checkResult(xml,result);
        return result;
    }
}
