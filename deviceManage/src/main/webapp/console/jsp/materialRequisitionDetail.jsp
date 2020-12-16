<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/materialRequisition.js"></script>
<script type="text/javascript">
    $(function () {
        var  materialRequisitionFormNo = '<%=request.getParameter("materialRequisitionFormNo")%>';
        if(materialRequisitionFormNo=='null'){
            materialRequisitionFormNo = '<%=session.getAttribute("materialRequisitionFormNo")%>';
            if(materialRequisitionFormNo=='null'){
                materialRequisitionFormNo='';
            }
        }
        if(!materialRequisitionFormNo){
            $("#pickingDate").iDatebox("setValue",getDate(new Date()));
            generateMaterialRequisitionFormNo();
            materialRequisitionFormNo='';
            //显示可编辑表格
            showMaterialRequisitionDetail(materialRequisitionFormNo);
            $("#erpMaterialRequisition").iMenubutton("disable");
        }else{//查看入库申请单
            queryMaterialRequisitionByFormNo(materialRequisitionFormNo);
            $("#formNo").iTextbox("readonly");
            $("#erpMaterialRequisition").iMenubutton("enable");
        }
    });
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table id='materialRequisitionDetailDg'>
                    <thead>
                    <tr>
                        <th field='barCode' width='180px' align='center' editor="{type:'numberbox',options:{precision:0}}">条码信息</th>
                        <th field='inventoryCode' width='180px' align='center'>物料代码</th>
                        <th field='inventoryName' width='180px' align='center'>物料名称</th>
                        <th field='specificationType' width='180px' align='center'>规格型号</th>
                        <th field='amount' width='80px' align='center' editor="{type:'numberbox',options:{precision:2}}">领用数量</th>
                        <th field='batchNumber' width='180px' align='center'>批号</th>
                        <th field='furnaceNumber' width='180px' align='center'>材料编号</th>
                        <th field='positionCode' width='180px' align='center' editor="text">货位</th>
                        <th field='boxNum' width='80px' align='center'>箱号</th>
                        <th field='amountOfBoxes' width='80px' align='center'>总箱数</th>
                        <th field='note' width='80px' align='center' editor="text">备注</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="materialRequisitionDetailDg-toolbar" class="topjui-toolbar">
<sec:authorize access="hasAuthority('ADDLINE_MATERIALREQUISITIONDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus'"
       onclick="addLine()">添加行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELLINE_MATERIALREQUISITIONDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="iconCls: 'fa fa-trash'"
       onclick="deleteLine()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_MATERIALREQUISITIONDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveMaterialRequisition()">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('GENERATEERP_MATERIALREQUISITIONDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="generateERPMaterialRequisition()" id="erpMaterialRequisition">生成ERP领料单</a>
</sec:authorize>
    <form id="ff" method="post">
        <div title="入库申请单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    生产领料单
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">领料单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="formNo" data-toggle="topjui-textbox"
                                   data-options="required:true" id="formNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">领料日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="pickingDate" data-toggle="topjui-datebox"
                                   id="pickingDate">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">领料人</label>
                        <div class="topjui-input-block">
                            <input type="text" name="pickerName" data-toggle="topjui-textbox"
                                   readonly id="pickerName">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">状态</label>
                        <div class="topjui-input-block">
                            <input type="text" name="status" data-toggle="topjui-textbox"
                                   data-options="required:false" id="status" readonly value="未生成">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工单单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="no" data-toggle="topjui-textbox"
                                   data-options="required:false" id="no">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">领料仓库</label>
                        <div class="topjui-input-block">
                            <input type="text" name="warehouseName" data-toggle="topjui-textbox"
                                   id="warehouseName" editable="false">
                            <input type="hidden" id="warehouseCode">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showWarehousesDialog"></div>
</body>
</html>
