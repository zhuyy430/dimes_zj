<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<script>
    $(function () {
        $("#workSheetTable").iDatagrid({
            idField:'id',
            fitColumns:true,
            singleSelect:true,
            pagination:true,
            selectOnCheck:true,
            checkOnSelect:false,
            onDblClickRow :function(rowIndex,rowData){
            	confirmWorkSheets(rowData);
 				},
            url:'workSheet/queryWorkSheets.do',
            columns:[[
                { field:'id',checkbox:false,hidden:true,width:'18',align:'center'},
                { field:'no',title:'工单单号',width:'11%',align:'center'},
                { field:'productionUnitCode',title:'生产单元编码',width:'11%',align:'center'},
                { field:'productionUnitName',title:'生产单元名称',width:'11%',align:'center'},
                { field:'workPieceCode',title:'工件编码',width:'11%',align:'center'},
                { field:'workPieceName',title:'工件名称',width:'11%',align:'center'},
                {field:'batchNumber',title:'批号',width:'10%',align:'center'},
                {field:'stoveNumber',title:'材料编号',width:'10%',align:'center'},
                {field:'unitType',title:'规格型号',width:'10%',align:'center'},
                {field:'manufactureDate',title:'生产日期',width:'10%',align:'center',formatter:function(value,row,index){
                        if(value){
                            return getDate(new Date(value));
                        }else{
                            return "";
                        }
                    }
                }
            ]]
        });
    });
    function reloadWorkSheets(){
        $('#workSheetTable').iDatagrid("load",{
            workpiece:$("#workpieceSearch").iTextbox("getValue"),
            productionUnit: $("#productionUnitSearch").iTextbox("getValue"),
            from:$("#from").iDatebox("getValue"),
            to:$("#to").iDatebox("getValue"),
            batchNumber:$("#batchNumberSearch").iTextbox("getValue")
        });
    }
</script>
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
        <form id="searchForm" method="post">
            <div title="工单信息" data-options="iconCls:'fa fa-th'">
                <div class="topjui-fluid">
                    <div class="topjui-row">
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">生产日期</label>
                            <div class="topjui-input-block">
                                <input id="from" type="text" data-toggle="topjui-datebox">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">至</label>
                            <div class="topjui-input-block">
                                <input id="to" type="text" data-toggle="topjui-datebox">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">生产单元</label>
                            <div class="topjui-input-block">
                                <input id="productionUnitSearch" data-toggle="topjui-textbox"
                                       name="productionUnitSearch" data-options="prompt:'编码或名称'">
                            </div>
                        </div>
                    </div>
                    <div class="topjui-row">
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">工件</label>
                            <div class="topjui-input-block">
                                <input id="workpieceSearch" data-toggle="topjui-textbox" data-options="prompt:'编码或名称'">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label"> 批号</label>
                            <div class="topjui-input-block">
                                <input id="batchNumberSearch" data-toggle="topjui-textbox">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label"></label>
                            <div class="topjui-input-block">
                                <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
                                   onclick="reloadWorkSheets()">搜索</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>