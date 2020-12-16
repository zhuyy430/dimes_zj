<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    function reloadVendors(){
        var vendorClass = $("#vendorClassTg").iTreegrid('getSelected');
        var vendorClassCode = "";
        if(vendorClass){
            vendorClassCode = vendorClass.code;
		}
        $('#vendorTable').iDatagrid("load",{
            cvcCode:vendorClassCode,
            vendorCode:$("#vendorCode4Query").val(),
            vendorName:$("#vendorName4Query").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'west',title:'',split:true,border:false,width:'25%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
		<table id="vendorClassTg" data-toggle="topjui-treegrid"
			   data-options="id:'vendorClassTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'vendorClass/queryAllVendorClasses.do',
			   childGrid:{
			   	   param:'cvcCode:code',
                   grid:[
                       {type:'datagrid',id:'vendorTable'},
                   ]
			   }">
			<thead>
			<tr>
				<th data-options="field:'name',width:'100%',title:'供应商类型'"></th>
			</tr>
			</thead>
		</table>
	</div>
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'vendorTable',
                       url:'vendor/queryVendorsByVendorClassesCode.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                          singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false,
                       parentGrid:{
                           type:'treegrid',
                           id:'vendorClassTg',
                       },
                       onDblClickRow :function(rowIndex,rowData){
  				 confirm(rowData);
  				}">
			<thead>
			<tr>
				<th data-options="field:'cVenCode',title:'供应商编码',width:'180px'"></th>
				<th data-options="field:'cVenName',title:'供应商名称',width:'250px',align:'center'"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
<div>
	<div id="vendorTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'vendorTable'
       }">
		供应商编码:<input id="vendorCode4Query"
					 name="vendorCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		供应商名称:<input id="vendorName4Query"
					 name="vendorName" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadVendors()">搜索</a>
	</div>
</div>