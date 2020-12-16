<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    function reloadCustomers(){
        $('#customerTable').iDatagrid("load",{
            ccusCode:$("#customerCode4Query").val(),
            ccusName:$("#customerName4Query").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'customerTable',
                       url:'customer/queryCustomers.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                          singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false,
						onDblClickRow :function(rowIndex,rowData){
  				 confirmCustomers(rowData);
  				},">
			<thead>
			<tr>
				<th data-options="field:'ccusCode',title:'客户编码',width:'150px'"></th>
				<th data-options="field:'ccusName',title:'客户名称',width:'250px',align:'center'"></th>
				<th data-options="field:'ccusAddress',title:'客户地址',width:'300px',align:'center'"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
<div>
	<div id="customerTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'customerTable'
       }" style="padding-left: 10px;">
		客户编码:<input id="customerCode4Query"
					 name="customerCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		客户名称:<input id="customerName4Query"
					 name="customerName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadCustomers()">搜索</a>
	</div>
</div>