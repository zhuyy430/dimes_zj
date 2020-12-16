<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		$('#menus').iTreegrid(
				{
					url : 'role/queryMenus.do',
					idField : 'id',
					treeField : 'text',
					singleSelect : false,
					checkOnSelect : true,
					selectOnCheck : true,
					checkbox : true,
					cascadeCheck : false,
					columns : [ [
					/*  {field:'id',title:'id',checkbox:true}, */
					{
						field : 'text',
						title : '名称',
						width : 200
					}, {
						field : 'url',
						title : 'url',
						width : 400
					}, {
						field : 'note',
						title : '说明',
						width : 150
					} ] ],
					queryParams : {
						roleId : $("#departmentDg").iDatagrid("getSelected").id
					},
					onCheckNode : function(row, checked) {
						if (checked) {
							/* var parent = $('#menus').iTreegrid("getParent",row.id);
							if(parent){
								$('#menus').iTreegrid("checkNode",parent.id);
							} */
							var children = $('#menus').iTreegrid("getChildren",
									row.id);
							if (children && children.length > 0) {
								for (var i = 0; i < children.length; i++) {
									var ch = children[i];
									if(ch!=null){
										$('#menus').iTreegrid("checkNode", ch.id);
									}
								}
							}
						} else {
							var children = $('#menus').iTreegrid("getChildren",
									row.id);
							if (children && children.length > 0) {
								for (var i = 0; i < children.length; i++) {
									var ch = children[i];
									if(ch!=null){
										$('#menus').iTreegrid("uncheckNode", ch.id);
									}
								}
							}
							var parent = $('#menus').iTreegrid("getParent",
									row.id);
							if (parent) {
								var brotherNodes = $('#menus').iTreegrid("getChildren", parent.id);
								if(brotherNodes && brotherNodes.length>0){
									var flag = false;
									for(var j = 0;j<brotherNodes.length;j++){
										var brotherNode = brotherNodes[j];
										if(brotherNode.checked){
											flag = true;
											break;
										}
									}
									if(!flag){
										$('#menus').iTreegrid("uncheckNode", parent.id);
									}
								}
							}
							
						}
					}
				});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="menus"></table>
		<input type="hidden" name="roleId" />
	</div>
</div>