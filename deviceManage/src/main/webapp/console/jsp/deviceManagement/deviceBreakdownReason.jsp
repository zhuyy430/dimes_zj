<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
function downloadFile(){
	 	window.location.href="relatedDoc/downloadTemplates.do?name=故障原因模版";
}
</script>
</head>
<body>
<input type="hidden" id="ids" />
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table id="productionUnitTg" data-toggle="topjui-treegrid"
				data-options="id:'productionUnitTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'projectType/queryTopProjectTypes.do?rootType=ROOTBREAKDOWNREASONTYPE',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'deviceDg'},
                   ]
			   }">
				<thead>
					<tr>
						<th data-options="field:'name',width:'100%',title:'故障类别'"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'deviceDg',
                       url:'projectType/queryProjectTypes.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'productionUnitTg',
                       },
			           childTab: [{id:'southTabs'}],
			            onSelect:function(index,row){
					           		
					           },
					           onLoadSuccess:function(){
					           		$('#deviceSite').iDatagrid('reload');
					           		$('#parameter').iDatagrid('reload');
					           }">
						<thead>
							<tr>
								<th data-options="field:'id',title:'id',hidden:true,checkbox:false,width:'80px'"></th>
								<th data-options="field:'code',title:'类别编号',width:'200px',align:'center'"></th>
								<th data-options="field:'name',title:'类别名称',width:'200px',sortable:false,align:'center'"></th>
								<th data-options="field:'note',title:'备注',width:'200px',sortable:false,align:'center'"></th>
								<th data-options="field:'disabled',title:'停用',width:'200px',sortable:false,align:'center',formatter:function(value,row,index){
										if(row.disabled=='0'){
											return '否';
										}else if(row.disabled=='1'){
											return '是';
										}else{
											return '';
										}
									}"></th>
							</tr>
						</thead>
					</table>
				</div>
				<div data-options="region:'south',fit:false,split:true,border:false"
					style="height: 40%">
					<div data-toggle="topjui-tabs"
						data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     singleSelect:true,
                     parentGrid:{
                         type:'datagrid',
                         id:'deviceDg',
                         param:'projectTypeId:id'
                     }">
						<div title="故障原因" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'parameter',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'deviceProject/queryDevicesProjectByProjectTypeId.do?type=BREAKDOWNREASON'">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',hidden:true,checkbox:false,width:'80px'"></th>
										<th data-options="field:'code',title:'故障原因代码',sortable:false,width:'200px',align:'center'"></th>
										<th data-options="field:'name',title:'故障原因名称',sortable:false,width:'200px',align:'center'"></th>
										<th data-options="field:'note',title:'说明',sortable:false,width:'200px',align:'center'"></th>
										<th data-options="field:'method',title:'方法',sortable:false,width:'200px',align:'center'"></th>
										<th data-options="field:'remark',title:'备注',sortable:false,width:'200px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
				</div> 
			</div>
		</div>
	</div>
</div>
	<!-- 故障类别表格工具栏开始 -->
	<div id="deviceDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'deviceDg'
       }">
<sec:authorize access="hasAuthority('ADD_BREAKDOWNREASONTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#deviceDg-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            },
       dialog:{
           id:'deviceAddDialog',
           width:500,
           height:400,
           href:'console/jsp/deviceManagement/deviceBreakdownType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#typeCode').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入类别编号!');
           				return false;
           			}
           			
           			var name = $('#typeName').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入类别名称!');
           				return false;
           			}
           			$.get('projectType/addProjectType.do',{
           			code:code,
           			name:name,
           			note:$('#typeNote').val(),
           			disabled:$('#typeDisabled').combobox('getValue'),
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			type:'breakdownReasonType'
           			},function(data){
           				if(data.success){
	           				$('#deviceAddDialog').iDialog('close');
	           				$('#deviceDg').iDatagrid('reload');
	           				$('#productionUnitTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_BREAKDOWNREASONTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#deviceDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'deviceEditDialog',
                width: 500,
                height: 400,
                href: 'console/jsp/deviceManagement/deviceBreakdownType_edit.jsp',
                url:'projectType/queryProjectTypeById.do?id={id}',
                 buttons:[
           		{text:'保存',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入类别名称!');
           				return false;
           			}
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入类别编号!');
           				return false;
           			}
           			$.get('projectType/updateProjectType.do',{
           			id:$('#deviceDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			note:$('#note').val(),
           			disabled:$('#disabled').combobox('getValue'),
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			type:'breakdownReasonType'
           			},function(data){
           				if(data.success){
	           				$('#deviceEditDialog').iDialog('close');
	           				$('#deviceDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_BREAKDOWNREASONTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-trash',
       url:'projectType/deleteProjectType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'deviceDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
	<!-- 故障原因表格工具栏开始 -->
	<div id="parameter-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'parameter'
       }">
<sec:authorize access="hasAuthority('ADD_BREAKDOWNREASON')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#parameter-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'deviceDg',
       },
       dialog:{
           id:'parameterAddDialog',
           width:500,
           height:400,
           href:'console/jsp/deviceManagement/deviceBreakdownReason_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			$.get('deviceProject/addDeviceProjectWithBreakDown.do',{
           			'projectType.id':$('#deviceDg').iDatagrid('getSelected').id,
           			code:$('#code').val(),
           			name:$('#name').val(),
           			note:$('#note').val(),
           			remark:$('#remark').val(),
           			method:$('#method').val(),
           			type:'BREAKDOWNREASON'
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#parameter').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_BREAKDOWNREASON')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#parameter-toolbar',
            iconCls: 'fa fa-pencil',
             parentGrid:{
		       	type:'datagrid',
		       	id:'deviceDg',
		       	param:'parentDeviceId:id'
		       },
            dialog: {
                	id:'parameterEditDialog',
                	width:500,
           			height:400,
		           href:'console/jsp/deviceManagement/deviceBreakdownReason_edit.jsp',
		           url:'deviceProject/queryDeviceProjectById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			$.get('deviceProject/updateDeviceProjectWithBreakDown.do',{
           			id:$('#parameter').iDatagrid('getSelected').id,
           			'projectType.id':$('#deviceDg').iDatagrid('getSelected').id,
           			code:$('#code').val(),
           			name:$('#name').val(),
           			note:$('#note').val(),
           			remark:$('#remark').val(),
           			method:$('#method').val(),
           			type:'BREAKDOWNREASON'
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#parameter').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_BREAKDOWNREASON')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#parameter-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProject/deleteDeviceProject.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'parameter',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('IMPORT_BREAKDOWNREASON_DOC')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#parameter-toolbar',
       iconCls:'fa fa-level-down',
        parentGrid:{
               type:'datagrid',
               id:'deviceDg'
            },
       dialog:{
           id:'importDialog',
           title:'导入',
           width:600,
           height:400,
           href:'console/jsp/deviceManagement/deviceInspectionProject_import.jsp',
           buttons:[
           	{text:'导入',handler:function(){
           			var file = document.getElementById('file1').files[0];
			               if(!file){
			                   alert('请选择文件');
			                   return ;
			                }
			                    var formData = new FormData();
			                   formData.append('file', file);   
			                   formData.append('type', 'BREAKDOWNREASON');   
			                   $.ajax({
			                       url: 'deviceProject/uploadDeviceBreakDown.do',
			                       type: 'POST',
			                       data: formData,
			                       contentType: false,
			                       processData: false,
			                       success: function (data) {
			                           if (data.success) {
			                               alert('导入成功！');
			                              $('#importDialog').iDialog('close');
			                             $('#parameter').iDatagrid('reload');
			                           }else{
			                               alert(data.msg);
			                           }
			                       }
			                   });
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#importDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
       }">导入</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_BREAKDOWNREASON_DOC')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-download'"
       data-toggle="topjui-menubutton" onclick='downloadFile()'>导出模板</a>
</sec:authorize>
	</div>
</div>
	<!-- 故障原因表格工具栏结束 -->
</body>
</html>