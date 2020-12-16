<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<script>
    var numberList=[];

    $(function(){
        $('#warehouseTable').iDatagrid({
            idField:'cWhCode',
            singleSelect:true,
            pageSize:10,
            url:'warehouse/queryAllWarehouses.do',
            columns:[[
                {field:'cWhCode',title:'仓库代码',width:250},
                {field:'cWhName',title:'仓库名称',width:250}
            ]]
        });
    });
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div style="padding-bottom:0px;background:#eee;"
         data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
        <table id="warehouseTable"></table>
    </div>
</div>