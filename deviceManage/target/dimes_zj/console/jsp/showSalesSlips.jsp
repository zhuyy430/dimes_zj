<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    $(function () {
        $("#salesSlipTable").iDatagrid({
            idField:'id',
            fitColumns:true,
            pagination:true,
            selectOnCheck:true,
            checkOnSelect:false,
            url:'salesSlip/querySalesSlips.do',
            columns:[[
                { field:'id',checkbox:true,width:'18',align:'center'},
                { field:'formNo',title:'销售单号',width:'11%',align:'center'},
                { field:'customerName',title:'客户名称',width:'10%',align:'center'},
                { field:'inventoryName',title:'物料名称',width:'10%',align:'center'},
                {field:'inventoryCode',title:'物料编码',width:'10%',align:'center'},
                {field:'specificationType',title:'规格型号',width:'10%',align:'center'},
                {field:'quantity',title:'订单数量',width:'10%',align:'center'},
                {field:'fhQuantity',title:'已发货数量',width:'10%',align:'center'},
                {field:'preDate',title:'计划发货日期',width:'10%',align:'center',formatter:function(value,row,index){
                    if(value){
                        return getDate(new Date(value));
					}else{
                        return '';
					}
					}},
                {field:'batchNumber',title:'批号',width:'10%',align:'center'},
            ]]
        });
    });
    function reloadSalesSlips(){
        $('#salesSlipTable').iDatagrid("load",{
            formNo:$("#formNoSearch").val(),
            customerName:$("#customerNameSearch").val(),
            inventoryName:$("#inventoryNameSearch").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table id="salesSlipTable">
		</table>
	</div>
</div>
<div>
	<div id="salesSlipTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'salesSlipTable'
       }">
		销售单号:<input id="formNoSearch"
					name="formNoSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		客户名称:<input id="customerNameSearch"
				  name="customerNameSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		物料名称:<input id="inventoryNameSearch"
					name="inventoryNameSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadSalesSlips()">搜索</a>
	</div>
</div>