package com.digitzones.xml;
import com.digitzones.model.*;
import com.digitzones.xml.model.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.Date;
import java.util.List;
/**
 * 生成ERP领料出库单工具
 */
@Component
public  class MaterialRequisitionSlipUtil extends ParentUtil{
    /**
     * 生成ERP领料出库单
     * @param materialRequisition  领料单
     * @param workSheet  工单
     * @return
     */
    public  Result generateMaterialRequisitionSlip(WorkSheet workSheet,MaterialRequisition materialRequisition,List<MaterialRequisitionDetail> detailList){
        Result result = init();
        if(result.getStatusCode().equals("300")){
            return result;
        }
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<ufinterface sender=\""+getConfig().getSender()+"\" receiver=\""+getConfig().getReceiver()+"\" roottag=\"storeout\" docid=\"\" proc=\"add\" codeexchanged=\"N\" exportneedexch=\"N\" paginate=\"0\" display=\"出库单\" family=\"库存管理\" dynamicdate=\""+getSdf().format(new Date())+"\" maxdataitems=\"20000\" bignoreextenduserdefines=\"y\" timestamp=\"0x000000000042D974\" lastquerydate=\"2019-08-29 09:24:14\">" +
                "    <storeout>" +
                "    <header>" +
                "      <id></id>" +
                "      <receiveflag>0</receiveflag><!--默认-->" +
                "      <vouchtype>11</vouchtype><!--默认-->" +
                "      <businesstype>领料</businesstype><!--默认-->" +
                "      <source>生产订单</source><!--默认-->" +
                "      <businesscode/>" +
                "      <warehousecode>"+materialRequisition.getWarehouseCode()+"</warehousecode><!--领料仓库-->" +
                "      <date>"+getSdf().format(materialRequisition.getPickingDate())+"</date>" +
                "      <code>"+materialRequisition.getFormNo()+"</code><!--领料单号-->" +
                "     <receivecode>202</receivecode>"+
                "      <departmentcode>"+workSheet.getDepartmentCode()+"</departmentcode><!--生产订单上的部门代码-->" +
                "      <personcode/>" +
                "      <purchasetypecode/>" +
                "      <saletypecode/>" +
                "      <customercode/>" +
                "      <customerccode/>" +
                "      <cacauthcode/>" +
                "      <vendorcode/>" +
                "      <ordercode/>" +
                "      <quantity>"+workSheet.getProductCount()+"</quantity><!--生产订单的生产数量-->" +
                "      <arrivecode/>" +
                "      <billcode/>" +
                "      <consignmentcode/>" +
                "      <arrivedate/>" +
                "      <checkcode/>" +
                "      <checkdate/>" +
                "      <checkperson/>" +
                "      <templatenumber>65</templatenumber><!--模板号\\默认-->" +
                "      <serial>"+workSheet.getBatchNumber()+"</serial><!--工单批号/测试不给值-->" +
                "      <handler/>" +
                "      <memory/>" +
                "      <maker>"+materialRequisition.getPickerName()+"</maker><!--制单人-->" +
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
                "      <iscomplement>0</iscomplement><!--默认-->" +
                "    </header>" +
                "    <body>";
          String entries = "";
            for(MaterialRequisitionDetail detail : detailList) {
                entries = "<entry>" +
                        "        <id></id>" +
                        "        <barcode/>" +
                        "        <inventorycode>" + workSheet.getMoallocateInvcode() + "</inventorycode><!--领料物料代码-->" +
                        "        <free1/>" +
                        "        <free2/>" +
                        "        <free3/>" +
                        "        <free4/>" +
                        "        <free5/>" +
                        "        <free6/>" +
                        "        <free7/>" +
                        "        <free8/>" +
                        "        <free9/>" +
                       /* "        <free10>" + workSheet.getMoDId() + "</free10>" +*/
                        "        <free10></free10>"+
                "        <shouldquantity>" + workSheet.getMoallocateQty() + "</shouldquantity><!--生产工单上的子件数量-->" +
                        "        <shouldnumber/>" +
                        "        <quantity>" + detail.getAmount() + "</quantity><!--本次领料数量-->" +
                        "        <cmassunitname>" + workSheet.getUnitName() + "</cmassunitname><!--计量单位-->" +
                        "        <assitantunit/>" +
                        "        <assitantunitname/>" +
                        "        <irate/>" +
                        "        <number/>" +
                        "        <price/>" +
                        "        <cost/>" +
                        "        <plancost/>" +
                        "        <planprice/>" +
                        "        <serial>" + detail.getBatchNumber() + "</serial><!--子件批号-->" +
                        "        <makedate/>" +
                        "        <validdate/>" +
                        "        <transitionid/>" +
                        "        <subbillcode/>" +
                        "        <subpurchaseid/>" +
                        "        <position>"+ (detail.getPositionCode()==null?"":detail.getPositionCode())+"</position><!--货位-->" +
                        "        <itemclasscode/>" +
                        "        <itemclassname/>" +
                        "        <itemcode/>" +
                        "        <itemname/>" +
                        "        <define22>" + workSheet.getBatchNumber() + "</define22>" +
                        "        <define23>DIMES</define23>" +
                        "        <define24>"+workSheet.getLotNo()+"</define24>" +
                        "        <define25/>" +
                        "        <define26/>" +
                        "        <define27/>" +
                        "        <define28/>" +
                        "        <define29/>" +
                        "        <define30/>" +
                        "        <define31/>" +
                        "        <define32/>" +
                        "        <define33/>" +
                        "        <define34/>" +
                        "        <define35/>" +
                        "        <define36/>" +
                        "        <define37/>" +
                        "        <subconsignmentid/>" +
                        "        <delegateconsignmentid/>" +
                        "        <subproducingid>" + workSheet.getMoDId() + "</subproducingid><!--生产订单子表ID-->" +
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
                        "        <whpersoncode/>" +
                        "        <whpersonname/>" +
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
                        "        <planlotcode/>" +
                        "      </entry>";
                xml += entries;
            }
                xml+= "    </body>" +
                "  </storeout>" +
                "</ufinterface>";
        checkResult(xml,result);
        return result;
    }
}
