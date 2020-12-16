<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#roles').iDatagrid({
			    url:'user/queryRoles.do',
			    fitColumns:true,
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'roleName',title:'角色名称',width:100},
			        {field:'note',title:'说明',width:100}
			    ]],
			    queryParams:{
			    	userId:$("#departmentDg").iDatagrid("getSelected").id
			    },
			    onLoadSuccess:function(data){
			    	 var rowData = data.rows;
			    	    $.each(rowData, function (idx, val) {
			    	        if (val.checked) {
			    	            $("#roles").datagrid("selectRow", idx);
			    	        }
			    	    });
			    }
			});
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="roles"></table>
	</div>
</div>