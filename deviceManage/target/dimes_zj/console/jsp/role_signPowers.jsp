<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script>
    $(function(){
        loadTable();
    });
    function checkAll(obj,rows){
        if(rows&&rows.length>0){
            for(var i = 0;i<rows.length;i++){
                var row = rows[i];
                var index = $('#powers').propertygrid("getRowIndex",row.id);
                if(obj.checked){
                    $('#powers').propertygrid("checkRow",index);
                }else{
                    $('#powers').propertygrid("uncheckRow",index);
                }
            }
        }
    }
    //权限表格载入
    function loadTable(){
        $('#powers').propertygrid({
            idField:'id',
            url:'role/queryPowers.do',
            showGroup:true,
            singleSelect:false,
            checkOnSelect : true,
            selectOnCheck : true,
            fitColumns:false,
            groupField:'note',
            showGroup: true,
            groupFormatter:function(group,rows){
                var rowData = JSON.stringify(rows).replace(/\"/g,"'");
                var str = "<input type=\"checkbox\" name=\"checkAllPowers\" onclick=\"checkAll(this,"+rowData+")\"/> " + '[' + group +']' ;
                return  str;
            }
            ,columns:[[
                {field:'id',title:'id',checkbox:true},
                {field:'powerName',title:'权限名',width:200},
                {field:'group',title:'所属模块',width:200},
                {field:'note',title:'说明',width:300}
            ]],
            queryParams:{
                roleId:$("#departmentDg").iDatagrid("getSelected").id
            },onLoadSuccess : function(data) {
                for(var i=0;i<=data.length;i++){
                    if(data[i].checked){
                        $('#powers').iDatagrid("selectRow",i);
                    }
                }
                $('#powers').propertygrid("collapseGroup");
            }
        });
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table  id="powers"></table>
	</div>
</div>