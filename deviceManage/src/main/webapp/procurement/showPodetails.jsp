<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    $(function () {
        $("#cVenCode").val($('#showPodetailDialog').dialog('options')["queryParams"]["cVenCode"]);
        $("#podetailTable").iDatagrid({
            fitColumns:true,
            pagination:true,
            selectOnCheck:true,
            checkOnSelect:true,
			url:'PO_Podetails/queryPO_Podetails.do',
			columns:[[
			    { field:'id',checkbox:true,width:'18',align:'center'},
			    { field:'cpoId',title:'采购单号',width:'18',align:'center'},
			    { field:'cDepCode',title:'部门编码',hidden:true,width:'18',align:'center',formatter:function(value,row,index){
			        if(row.po_pomain){
			            return row.po_pomain.cDepCode;
					}else{
			            return '';
					}
					}},
			    { field:'cPersonCode',title:'业务员编码', hidden:true,width:'18',align:'center',formatter:function(value,row,index){
                        if(row.po_pomain){
                            return row.po_pomain.cPersonCode;
                        }else{
                            return '';
                        }
                    }},
			    { field:'cInvName',title:'物料名称',width:'18%',align:'center'},
			    { field:'cInvCode',title:'物料编码',width:'18%',align:'center'},
			    {field:'cInvStd',title:'规格型号',width:'14%',align:'center'},
			    {field:'iQuantity',title:'订单数量',width:'10%',align:'center'},
			    {field:'cDefine7',title:'已申请数量',width:'10%',align:'center'},
			    {field:'cComUnitName',title:'单位',width:'10%',align:'center'}
			    ]],
            queryParams:{ cVenCode: $("#cVenCode").val()}
        });
    });
    function reloadPodetail(){
        $('#podetailTable').iDatagrid("load",{
            cInvName:$("#cInvName").val(),
            cInvCode:$("#cInvCode").val(),
            cpoId:$("#cpoId").val(),
            cVenCode:$("#cVenCode").val()
        });
    }
</script>
<input type="hidden" id="cVenCode" />
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table id="podetailTable">
		</table>
	</div>
</div>
<div>
	<div id="podetailTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'podetailTable'
       }" style="padding-left:10px;">
		采购单号:<input id="cpoId"
					name="cpoId" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		物料代码:<input id="cInvCode"
					name="cInvCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		物料名称:<input id="cInvName"
					name="cInvName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadPodetail()">搜索</a>
	</div>
</div>