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
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'locationTable',
                       url:'location/queryLocations.do?cWhCode=<%=request.getParameter("cWhCode")==null?"":request.getParameter("cWhCode")%>',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false">
			<thead>
			<tr>
				<th data-options="field:'cPosCode',title:'编码',width:'50%',align:'center'"></th>
				<th data-options="field:'cPosName',title:'货位名称',width:'50%',align:'center'"></th>
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
		货位编码:<input id="cPosCode"
					 name="cPosCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<input type="hidden" id="cWhCode" name="cWhCode" value="<%=request.getParameter("cWhCode")%>">
		货位名称:<input id="cPosName"
					 name="cPosName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadLocations()">搜索</a>
	</div>
</div>