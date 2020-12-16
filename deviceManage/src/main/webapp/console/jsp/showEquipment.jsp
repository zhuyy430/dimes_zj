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
            equipmentCode:$("#equipmentCode4Query").val(),
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'west',title:'',split:true,border:false,width:'25%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
		<table id="inventoryClassTg" data-toggle="topjui-treegrid"
			   data-options="id:'inventoryClassTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'equipmentType/queryTopEquipmentTypes.do',
			   childGrid:{
			   	   param:'equipmentTypeId:id',
                   grid:[
                       {type:'datagrid',id:'inventoryTable'},
                   ]
			   },onSelect:function(index, row){
					var InvenClass = $('#inventoryClassTg').iTreegrid('getSelected');
					var InvenClassCode = '';
					if(InvenClass){
					InvenClassId = InvenClass.id;
					}
					$('#departmentDg').iDatagrid('load',{
					equipmentTypeId:InvenClassId
					});
				}">
			<thead>
			<tr>
				<th data-options="field:'name',width:'100%',title:'装备信息'"></th>
			</tr>
			</thead>
		</table>
	</div>
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'inventoryTable',
                       url:'equipment/queryEquipmentsByEquipmentTypeId.do',
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
			    <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
				<th data-options="field:'code',title:'装备序号',width:'180px'"></th>
				<th data-options="field:'cumulation',title:'计量累计',width:'180px',align:'center'"></th>
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
		装备序号:<input id="equipmentCode4Query"
					 name="inventoryCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadInventories()">搜索</a>
	</div>
</div>