<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    function reloadWarehouses(){
        $('#warehouseTable2').iDatagrid("load",{
            cWhCode:$("#cWhCode").val(),
            cWhName:$("#cWhName").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'warehouseTable2',
                       url:'warehouseController/queryWarehouses.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false">
			<thead>
			<tr>
				<th data-options="field:'cWhCode',title:'仓库编码',width:'50%',align:'center'"></th>
				<th data-options="field:'cWhName',title:'仓库名称',width:'50%',align:'center'"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
<div>
	<div id="warehouseTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'warehouseTable'
       }">
		仓库编码:<input id="cWhCode"
					 name="cWhCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		仓库名称:<input id="cWhName"
					 name="cWhName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadWarehouses()">搜索</a>
	</div>
</div>