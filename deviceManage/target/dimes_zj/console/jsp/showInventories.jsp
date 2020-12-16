<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    function reloadInventories(){
        var inventoryClass = $("#inventoryClassTg").iTreegrid('getSelected');
        var inventoryClassCode = "";
        if(inventoryClass){
            inventoryClassCode = inventoryClass.code;
		}
        $('#inventoryTable').iDatagrid("load",{
            //code:inventoryClassCode,
            cInvCode:$("#inventoryCode4Query").val(),
            cInvName:$("#inventoryName4Query").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'west',title:'',split:true,border:false,width:'25%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
		<table id="inventoryClassTg" data-toggle="topjui-treegrid"
			   data-options="id:'inventoryClassTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'inventoryClass/queryInventoryClassesTree.do',
			   childGrid:{
			   	   param:'code:code',
                   grid:[
                       {type:'datagrid',id:'inventoryTable'},
                   ]
			   },onSelect:function(index, row){
					var InvenClass = $('#inventoryClassTg').iTreegrid('getSelected');
					var InvenClassCode = '';
					if(InvenClass){
					InvenClassCode = InvenClass.code;
					}
					$('#departmentDg').iDatagrid('load',{
					code:InvenClassCode
					});
				}">
			<thead>
			<tr>
				<th data-options="field:'name',width:'100%',title:'物料类型'"></th>
			</tr>
			</thead>
		</table>
	</div>
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'inventoryTable',
                       url:'inventory/queryInventoryByTyClassCode.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                          singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false,
						onDblClickRow :function(rowIndex,rowData){
  				 confirmInventory(rowData);
  				},
                       parentGrid:{
                           type:'treegrid',
                           id:'inventoryClassTg',
                       }">
			<thead>
			<tr>
				<th data-options="field:'code',title:'物料编码',width:'180px'"></th>
				<th data-options="field:'name',title:'物料名称',width:'180px',align:'center'"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
<div>
	<div id="inventoryTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'inventoryTable'
       }">
		物料编码:<input id="inventoryCode4Query"
					 name="inventoryCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		物料名称:<input id="inventoryName4Query"
					 name="inventoryName" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadInventories()">搜索</a>
	</div>
</div>