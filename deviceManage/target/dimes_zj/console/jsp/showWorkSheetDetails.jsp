<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    $(function () {
        $("#productionUnitCode").val($('#showWorkSheetsDialog').dialog('options')["queryParams"]["productionUnitCode"]);
        $("#workSheetTable").iDatagrid({
            idField:'id',
            fitColumns:true,
            pagination:true,
            selectOnCheck:true,
            checkOnSelect:false,
            url:'workSheet/queryWorkSheetDetailsByProductionUnitCode.do',
            columns:[[
                { field:'id',checkbox:true,width:'18',align:'center'},
                { field:'no',title:'工单单号',width:'11%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.no;
                        }
                        else{
                            return '';
                        }
                    }
                },
                { field:'workpieceCode',title:'工件编码',width:'10%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.workPieceCode;
                        }
                        else{
                            return '';
                        }
                    }
                },
                { field:'cInvCName',title:'工件名称',width:'10%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.workPieceName;
                        }
                        else{
                            return '';
                        }
                    }},
                {field:'cInvStd',title:'批号',width:'10%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.batchNumber;
                        }
                        else{
                            return '';
                        }
                    }},
                {field:'cInvStd',title:'材料编号',width:'10%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.stoveNumber;
                        }
                        else{
                            return '';
                        }
                    }},
                {field:'cInvStd',title:'规格型号',width:'8%',align:'center',formatter:function(value,row,index){
                        if(row.workSheet){
                            return row.workSheet.unitType;
                        }
                        else{
                            return '';
                        }
                    }},
                {field:'deviceCode',title:'设备编码',width:'10%',align:'center'},
                {field:'deviceName',title:'设备名称',width:'10%',align:'center'},
                {field:'deviceSiteCode',title:'设备站点编码',width:'10%',align:'center'},
                {field:'deviceSiteName',title:'设备站点名称',width:'10%',align:'center'},
                {field:'processCode',title:'工序编码',width:'10%',align:'center'},
                {field:'processName',title:'工序名称',width:'10%',align:'center'}
            ]],
            queryParams:{ productionUnitCode: $("#productionUnitCode").val()}
        });
    });
    function reloadWorkSheetDetails(){
        $('#workSheetTable').iDatagrid("load",{
            no:$("#noSearch").val(),
            workpiece:$("#workpieceSearch").val(),
            device:$("#deviceSearch").val(),
            process:$("#processSearch").val(),
            productionUnitCode: $("#productionUnitCode").val()
        });
    }
</script>
<input type="hidden" id="productionUnitCode" />
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table id="workSheetTable">
		</table>
	</div>
</div>
<div>
	<div id="workSheetTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'workSheetTable'
       }">
		工单单号:<input id="noSearch"
					name="noSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		工件:<input id="workpieceSearch"
				  name="workpieceSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		设备:<input id="deviceSearch"
					name="deviceSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		工序:<input id="processSearch"
				  name="processSearch" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadWorkSheetDetails()">搜索</a>
	</div>
</div>