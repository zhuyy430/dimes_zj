<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/inspectionRecord.js"></script>
<script type="text/javascript">
    $(function(){
        $("#inventoryCodeSearch").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showInventoryDialog').dialog("open");
            }
        });
        $('#showInventoryDialog').dialog({
            title: '工件信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'console/jsp/showInventories.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var inventory = $('#inventoryTable').iDatagrid('getSelected');
                    confirmInventory(inventory);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showInventoryDialog').dialog("close");
                }
            }]
        });
    });
    //工件代码确定
    function confirmInventory(inventory) {
    	 $("#inventoryCode").val(inventory.code);
         $("#inventoryCodeSearch").iTextbox('setValue',inventory.name);
         $('#showInventoryDialog').dialog("close");
    }
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'inspectionRecordDg',
                       url:'inspectionRecord/queryInspectionRecords.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'检验单号',width:'150px',align:'center'"></th>
                        <th data-options="field:'inspectionDate',title:'检验日期',width:'100px',align:'center',
                        formatter:function(value,row,index){
                             if(value){
                                return getDate(new Date(value));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'productionUnitName',title:'生产单元名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'no',title:'工单单号',width:'150px',align:'center'"></th>
                        <th data-options="field:'processCode',title:'工序代码',width:'100px',align:'center'"></th>
                        <th data-options="field:'processName',title:'工序名称',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'工件代码',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'工件名称',width:'100px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'100px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'100px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'100px',align:'center'"></th>
                        <th data-options="field:'inspectionResult',title:'检验结果',width:'100px',align:'center'"></th>
                        <th data-options="field:'inspectorName',title:'检验员',width:'100px',align:'center'"></th>
                        <th data-options="field:'className',title:'班次',width:'100px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="inspectionRecordDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'inspectionRecordDg'
       }">
<sec:authorize access="hasAuthority('QUERY_INSPECTIONRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#inspectionRecordDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryInspectionRecordDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_INSPECTIONRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#inspectionRecordDg-toolbar',
       iconCls: 'fa fa-sign-out',
      ">导出</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ADD_INSPECTIONRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#inspectionRecordDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addInspectionRecord()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_INSPECTIONRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-trash'" onclick="deleteInspectionRecord()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_INSPECTIONRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#inspectionRecordDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryInspectionRecordDetailsByFormNo()">查看</a>
</sec:authorize>
    <form id="searchForm" method="post">
        <div title="检验结果" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 10px"></div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">检验日期</label>
                        <div class="topjui-input-block">
                            <input id="from" type="text" data-toggle="topjui-datebox" style="width:47%;"> 至
                            <input id="to" type="text" data-toggle="topjui-datebox" style="width:47%;">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工件代码</label>
                        <div class="topjui-input-block">
                            <input id="inventoryCodeSearch" data-toggle="topjui-textbox">
                            <input type="hidden" id="inventoryCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">生产单元</label>
                        <div class="topjui-input-block">
                            <input type="text" id="productionUnitNameSearch"
                                   name="productionUnitName" data-toggle="topjui-combotree"
                                   data-options="required:false,
                       valueField:'code',
                       textField:'name',
                       url:'productionUnit/queryProductionUnitSiteTree.do',
                       onSelect: function(rec){
                       	$('#productionUnitCodeSearch').val(rec.code);
                       }
                       ">
                            <input type="hidden" name="productionUnitCodeSearch" id="productionUnitCodeSearch">
                        </div>
                    </div>
                    <div class="topjui-col-sm2">
                        <label class="topjui-form-label">检验结果</label>
                        <div class="topjui-input-block">
                            <input id="inspectionResultSearch" data-toggle="topjui-combobox" data-options="
                            valueField: 'text',
                            textField: 'text',
                            data: [{
                                text: 'OK'
                            },{
                                text: 'NG'
                            }]">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">工单单号</label>
                        <div class="topjui-input-block">
                            <input id="noSearch" data-toggle="topjui-textbox">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工序名称</label>
                        <div class="topjui-input-block">
                            <input id="processNameSearch" type="text" data-toggle="topjui-textbox">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label"> 批号</label>
                        <div class="topjui-input-block">
                            <input id="batchNumberSearch" data-toggle="topjui-textbox">
                        </div>
                    </div>
                    <div class="topjui-col-sm2">
                        <label class="topjui-form-label">班次</label>
                        <div class="topjui-input-block">
                            <input id="classCode" data-toggle="topjui-combobox" data-options="
                            valueField: 'code',
                            textField: 'name',
                            url:'classes/queryAllClasses.do'">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showInventoryDialog"></div>
</body>
</html>
