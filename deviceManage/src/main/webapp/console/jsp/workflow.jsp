<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#deployDialog').dialog({
			title : '流程部署',
			width : 400,
			height : 200,
			closed : true,
			cache : false,
			modal : true,
			buttons : [ {
				text : '部署',
				iconCls : 'icon-plus',
				handler : function() {
					$("#deployWorkflowForm").form({
						url : 'workflow/deploy.do',
						onSubmit : function() {
						},
						success : function(data) {
							data = JSON.parse(data);
							if(data.success){
								$('#deployDialog').dialog("close");
								$("#departmentDg").iDatagrid("reload");
							}else{
								alert(data.msg);
							}
						}
					});
					$("#deployWorkflowForm").submit();
				}
			}, {
				text : '取消',
				iconCls : 'icon-cancel',
				handler : function() {
					$("#deployWorkflowForm").form("reset");
					$('#deployDialog').dialog("close");
					
				}
			} ]
		});
		//显示部署图片对话框
		$('#showDeployPicDialog').dialog({
			title : '流程图',
			width : 600,
			height : 600,
			closed : true,
			cache : false,
			modal : true
		});
	});

	function openDeployDialog() {
		$('#deployDialog').dialog("open");
	}
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'workflow/queryAllProcessDefinitions.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:false,
			           childTab: [{id:'southTabs'}],
			           onDblClickRow:function(index, row){
			           	//查询图片
			           	$.get('workflow/queryProcessPic.do',{deploymentId:row.deploymentId,resourceName:row.diagramResourceName},
			           	function(data){
			           		if(data.success){
			           			$('#showDeployPic').attr('src',data.path);
			           			$('#showDeployPicDialog').dialog('open');
			           		}else{
			           			alert(data.msg);
			           		}
			           	});
			           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'name',title:'流程名称',width:'180px',align:'center'"></th>
								<th
									data-options="field:'key',title:'流程定义ID',width:'180px',align:'center'"></th>
								<th
									data-options="field:'version',title:'版本',width:'180px',align:'center'"></th>
								<th
									data-options="field:'resourceName',title:'流程定义文件名称',sortable:false,width:'180px'"></th>
								<th
									data-options="field:'diagramResourceName',title:'流程定义图片名称',sortable:false,width:'180px'"></th>
								<th
									data-options="field:'description',title:'描述',sortable:false,width:'180px'"></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 部门表格工具栏开始 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
       <sec:authorize access="hasAuthority('DEPLOY_WORKFLOW')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			onclick="openDeployDialog()">部署流程</a> 
			 </sec:authorize>
		<!-- <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'workflow/deleteProcessDeployment.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'deploymentId:deploymentId'}">删除</a> -->
	</div>
	<!-- 部门表格工具栏结束 -->
	
	<!-- 上传部署流程文件对话框 -->
	<div id="deployDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<form id="deployWorkflowForm" method="post"
					enctype="multipart/form-data">
					<div title="流程部署" data-options="iconCls:'fa fa-th'">
						<div class="topjui-fluid">
							<div style="height: 30px"></div>
							<div class="topjui-row">
								<div class="topjui-col-sm12" style="color: red;">
									允许的文件扩展名为：zip、bar、bpmn</div>
							</div>
							<div class="topjui-row">
								<div class="topjui-col-sm12">
									<label class="topjui-form-label">请选择文件</label>
									<div class="topjui-input-block">
										<input type="file" name="file" data-options="required:true"
											id="file">
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- 显示流程图片对话框 -->
	<div id="showDeployPicDialog">
		<img alt="流程图片" style="height:100%;width:100%;" id="showDeployPic">
	</div>
</body>
</html>