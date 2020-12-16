<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		$('#users').iDatagrid(
				{
					url : 'role/queryUsers.do',
					idField : 'id',
					singleSelect : false,
					checkOnSelect : true,
					selectOnCheck : true,
					checkbox : true,
					columns : [ [
					{
						field : 'id',
						title : 'id',
						checkbox:true
					},{
						field : 'username',
						title : '用户名',
						width : 200
					}/* , {
						field : 'realName',
						title : '真实姓名',
						width : 200
					}, {
						field : 'tel',
						title : '联系方式',
						width : 200
					} */, {
						field : 'createDate',
						title : '创建日期',
						width : 200
					} ] ],
					queryParams : {
						roleId : $("#departmentDg").iDatagrid("getSelected").id
					},
					onLoadSuccess : function(data) {
						for(var i=0;i<=data.length;i++){
							if(data[i].checked){
								 $('#users').iDatagrid("selectRow",i);
							}
						}
					}
				});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="users"></table>
		<input type="hidden" name="roleId" />
	</div>
</div>