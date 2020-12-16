<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<script>
    var numberList=[];

    $(function(){
        $('#equipmentTable').iDatagrid({
            idField:'id',
            singleSelect:true,
            url:'equipmentType/queryPageBySearch.do',
            columns:[[
                {field:'id',title:'id',width:60,hidden:true},
                {field:'code',title:'装备代码',width:100},
                {field:'name',title:'名称',width:100},
                {field:'unitType',title:'规格型号',width:120}
            ]]
        });
    });

    //搜索
    function reloadEquipment(){
        $('#equipmentTable').iDatagrid("load",{//cInvCode,cInvName,cInvStd,cEngineerFigNo
            //code:InvenClassCode,
            queryCode:$("#queryCode").val(),
            queryName:$("#queryName").val(),
            queryUnitType:$("#queryUnitType").val()
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'north',title:'',border:false,height:50">
        <div style="height: 30px; margin-top: 5px;">
            <div style="float: left; width: 90%;">
                <div style="margin-top: 5px;margin-left: 10px;">
                    装备编码:<input id="queryCode"
                                name="queryCode" style="width:120px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
                    装备名称:<input id="queryName"
                                name="queryName" style="width:120px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
                    规格型号:<input id="queryUnitType"
                                name="queryUnitType" style="width:120px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
                    <a href="javascript:void(0)" data-toggle="topjui-menubutton" style="float: right;"
                       data-options="iconCls:'fa fa-search'" onclick="reloadEquipment()">搜索</a>
                </div>

            </div>
        </div>
    </div>
    <div style="padding-bottom:60px;background:#eee;"
         data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
        <table id="equipmentTable"></table>
    </div>
</div>