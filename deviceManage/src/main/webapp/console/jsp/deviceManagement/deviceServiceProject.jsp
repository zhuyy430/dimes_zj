<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
function downloadFile(){
	 	window.location.href="relatedDoc/downloadTemplates.do?name=维修项目模版";
}
</script>
</head>
<body>
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
			   url:'projectType/queryTopProjectTypes.do?rootType=ROOTMAINTENANCEITEMTYPE',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'productionUnitDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'维修类别'"></th>
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
						data-options="id:'productionUnitDg',
                       url:'projectType/queryProjectTypes.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'productionUnitTg',
                       },
			           childTab: [{id:'southTabs'}],onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{productionUnitId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'10px',hidden:true"></th>
								<th
									data-options="field:'code',title:'类别编号',width:'180px',align:'center'"></th>
								<th data-options="field:'name',title:'类别名称',width:'180px',align:'center'"></th>
                        		<th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
								<th
									data-options="field:'disabled',title:'停用',width:'180px',align:'center',
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
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
                         id:'productionUnitDg',
                         param:'projectTypeId:id'
                     }">
						<div title="维修项目" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'deviceProject/queryDevicesProjectByProjectTypeId.do?type=MAINTENANCEITEM'">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
										<th data-options="field:'code',title:'项目代码',width:'180px',align:'center'"></th>
										<th data-options="field:'name',title:'项目名称',width:'180px',align:'center'"></th>
										<th data-options="field:'deviceTypeName',title:'类别名称',width:'180px',align:'center'"></th>
										<th data-options="field:'deviceTypeCode',title:'类别编码',width:'180px',align:'center'"></th>
										<th data-options="field:'note',title:'说明',width:'180px',align:'center'"></th>
										<th data-options="field:'method',title:'方法',width:'180px',align:'center'"></th>
										<th data-options="field:'remark',title:'备注',width:'180px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="productionUnitDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'productionUnitDg'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCEITEMTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            },
       dialog:{
           id:'productionUnitAddDialog',
           width:600,
           height:400,
           href:'console/jsp/deviceManagement/projectType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var typeCode = $('#typeCode').val();
           			if(typeCode==null || ''===$.trim(typeCode)){
           				$.iMessager.alert('提示','请输入类别代码');
           				return false;
           			}
           			
           			var typeName = $('#typeName').val();
           			if(typeName==null || ''===$.trim(typeName)){
           				$.iMessager.alert('提示','请输入类别名称');
           				return false;
           			}
           			$.get('projectType/addProjectType.do',{
           			code:typeCode,
           			name:typeName,
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			note:$('#typeNote').val(),
           			type:'maintenanceItemType'
           			},function(data){
           				if(data.success){
	           				$('#productionUnitAddDialog').iDialog('close');
	           				$('#productionUnitDg').iDatagrid('reload');
	           				$('#productionUnitTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#productionUnitAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCEITEMTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#productionUnitDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'productionUnitEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/projectType_edit.jsp',
                url:'projectType/queryProjectTypeById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var typeCode = $('#typeCode_edit').val();
           			var typeName = $('#typeName_edit').val();
           			if(typeName==null || ''===$.trim(typeName)){
           				$.iMessager.alert('提示','请输入类别名称');
           				return false;
           			}
           			$.get('projectType/updateProjectType.do',{
           			id:$('#productionUnitDg').iDatagrid('getSelected').id,
           			code:typeCode,
           			name:typeName,
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			note:$('#typeNote_edit').val()
           			},function(data){
           				if(data.success){
	           				$('#productionUnitEditDialog').iDialog('close');
	           				$('#productionUnitDg').iDatagrid('reload');
	           				$('#productionUnitTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#productionUnitEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCEITEMTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-trash',
       url:'projectType/deleteProjectType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'productionUnitDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_MAINTENANCEITEMTYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-stop',
       url:'projectType/disabledProjectType.do',
       grid: {uncheckedMsg:'请选择操作的维修类别',id:'productionUnitDg',param:'id:id'}" id="productionUnitSwitchBtn">停用</a>
</sec:authorize>
	</div>
	<!-- 生产单元表格工具栏结束 -->
	<!-- 设备表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCEITEM')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
         parentGrid:{
               type:'datagrid',
               id:'productionUnitDg'
            },
       dialog:{
       		beforeOpenCheckUrl: 'productionUnit/existSubProductionUnit.do?id={id}',
           id:'deviceAddDialog',
            width:600,
           height:400,
           href:'console/jsp/deviceManagement/deviceServiceProject_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入设备项目代码!');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入设备项目名称!');
           				return false;
           			}
           			$.get('deviceProject/addDeviceProject.do',{
           			code:code,
           			name:name,
           			'projectType.id':$('#productionUnitDg').iDatagrid('getSelected').id,
           			note:$('#note').val(),
           			method:$('#method').val(),
           			remark:$('#remark').val(),
           			type:'MAINTENANCEITEM'
           			},function(data){
           				if(data.success){
	           				$('#deviceAddDialog').iDialog('close');
           					$('#position').iDatagrid('reload');
           					$('#deviceTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCEITEM')">
		 <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/deviceServiceProject_edit.jsp',
                url:'deviceProject/queryDeviceProjectById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入设备类别名称!');
           				return false;
           			}
           			$.get('deviceProject/updateDeviceProject.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			'projectType.id':$('#productionUnitDg').iDatagrid('getSelected').id,
           			note:$('#note').val(),
           			method:$('#method').val(),
           			remark:$('#remark').val(),
           			type:'MAINTENANCEITEM'
           			},function(data){
           				if(data.success){
	           				$('#positionEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCEITEM')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProject/deleteDeviceProject.do',
       grid: {uncheckedMsg:'请先勾选要移除的数据',id:'position',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('IMPORT_MAINTENANCEITEM_DOC')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls:'fa fa-level-down',
        parentGrid:{
               type:'datagrid',
               id:'productionUnitDg'
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
			                    console.log(file)
			                     if(!file){
			                   alert('请选择文件');
			                   return ;
			                }
			                    var formData = new FormData();
			                   formData.append('file', file);   
			                   formData.append('type', 'MAINTENANCEITEM');   
			                   $.ajax({
			                       url: 'deviceProject/uploadTemplate.do',
			                       type: 'POST',
			                       data: formData,
			                       contentType: false,
			                       processData: false,
			                       success: function (data) {
			                           if (data.success) {
			                               alert('导入成功！');
			                              $('#importDialog').iDialog('close');
			                             $('#position').iDatagrid('reload');
			                           }else{
			                               alert(data.msg);
			                           }
			                       }
			                   });
        
        
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#importDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">导入</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_MAINTENANCEITEM_DOC')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-download'"
       data-toggle="topjui-menubutton" onclick='downloadFile()'>导出模板</a>
</sec:authorize>
	</div>
</body>
</html>