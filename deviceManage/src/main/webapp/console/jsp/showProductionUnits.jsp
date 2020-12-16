<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    function reloadLocations(){
        $('#locationTable').iDatagrid("load",{
            cPosCode:$("#cPosCode").val(),
            cPosName:$("#cPosName").val(),
            cWhCode:$("#cWhCode").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
		<!-- treegrid表格 -->
		<table id="showProductionUnitTg" data-toggle="topjui-treegrid"
			   data-options="id:'showProductionUnitTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryTopProductionUnits.do'">
			<thead>
			<tr>
				<th data-options="field:'name',width:'100%',title:'生产单元'"></th>
			</tr>
			</thead>
		</table>
	</div>
<div>
	<div id="warehouseTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'warehouseTable'
       }">
		货位编码:<input id="cPosCode"
					 name="cPosCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<input type="hidden" id="cWhCode" name="cWhCode" value="<%=request.getParameter("cWhCode")%>">
		货位名称:<input id="cPosName"
					 name="cPosName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadLocations()">搜索</a>
	</div>
</div>