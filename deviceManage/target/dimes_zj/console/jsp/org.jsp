<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table id="departmentTg" data-toggle="topjui-treegrid"
				data-options="id:'departmentTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'department/queryTopDepartments.do',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'机构名称'"></th>
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
						data-options="id:'departmentDg',
                       singleSelect:true,
                       fitColumns:true,
                       url:'department/queryDepartmentsByParentId.do',
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg'
                       },
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
			           		switchButton('departmentSwitchBtn',row.disabled);
			           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{id:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'code',title:'部门代码',width:'180px',align:'center'"></th>
								<th data-options="field:'name',title:'部门名称',width:'180px'"></th>
								<th
									data-options="field:'pcode',title:'上级部门代码',width:'180px'"></th>
								<th
									data-options="field:'pname',title:'上级部门名称',width:'180px'"></th>
								<th
									data-options="field:'disabled',title:'停用',width:'180px',
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
                        <th data-options="field:'note',title:'备注',width:'180px'"></th>
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
                         id:'departmentDg',
                         param:'id:id'
                     }">
						<div title="岗位信息" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
							   url:'position/queryPositionsByDepartmentId.do',
					           onSelect:function(index,row){
					           		switchButton('positionSwitchBtn',row.disabled);
					           }">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',checkbox:false,width:'180px',hidden:'true'"></th>
										<th data-options="field:'code',title:'岗位代码',width:'180px'"></th>
										<th data-options="field:'name',title:'岗位名称',width:'180px'"></th>
										<th
											data-options="field:'deptCode',title:'部门代码',width:'180px',
                                formatter:function(value,row,index){
                                    if (row.department) {
                                        return row.department.code;
                                    }else {
                                        return '';
                                    }
                                }"></th>
										<th
											data-options="field:'deptName',title:'部门名称',width:'180px',
                                formatter:function(value,row,index){
                                    if (row.department) {
                                        return row.department.name;
                                    }else {
                                        return '';
                                    }
                                }"></th>
										<th
											data-options="field:'disabled',title:'停用',width:'180px',
		                        formatter:function(value,row,index){
		                        	if(value){
		                        		return 'Y';
		                        	}else{
		                        		return 'N';
		                        	}
                        		}"></th>
                        		<th data-options="field:'note',title:'备注',width:'180px'"></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
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
       <sec:authorize access="hasAuthority('ADD_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg'
       },
       dialog:{
           id:'departmentAddDialog',
           width:600,
           height:400,
           href:'console/jsp/department_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           		if($('#departmentTg').iTreegrid('getSelected')){
           			var deptCode = $('#deptCode').val();
           			if(deptCode==null || ''===$.trim(deptCode)){
           				$.iMessager.alert('提示','请输入部门代码');
           				return false;
           			}
           			
           			var deptName = $('#deptName').val();
           			if(deptName==null || ''===$.trim(deptName)){
           				$.iMessager.alert('提示','请输入部门名称');
           				return false;
           			}
           			$.get('department/addDepartment.do',{
           			code:deptCode,
           			name:deptName,
           			'parent.id':$('#departmentTg').iTreegrid('getSelected').id,
           			note:$('#deptNote').val()
           			},function(data){
           				if(data.success){
	           				$('#departmentAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}else{
           			alert('请选择父部门');
           			$('#departmentAddDialog').iDialog('close');
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#departmentAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
           </sec:authorize>
           <sec:authorize access="hasAuthority('EDIT_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/department_edit.jsp',
                url:'department/queryDepartmentById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           		if($('#departmentDg').iDatagrid('getSelected')){
           			var deptCode = $('#deptCode_edit').val();
           			var deptName = $('#deptName_edit').val();
           			if(deptName==null || ''===$.trim(deptName)){
           				$.iMessager.alert('提示','请输入部门名称');
           				return false;
           			}
           			$.get('department/updateDepartment.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:deptCode,
           			name:deptName,
           			'parent.id':$('#pid').val(),
           			note:$('#deptNote_edit').val()
           			},function(data){
           				if(data.success){
	           				$('#departmentEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}else{
           			alert('请选择要编辑的记录!');
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#departmentEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
            </sec:authorize>
            <sec:authorize access="hasAuthority('DEL_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'department/deleteDepartment.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('DISABLE_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'department/disabledDepartment.do',
       grid: {uncheckedMsg:'请选择操作的部门',id:'departmentDg',param:'id:id'}"
			id="departmentSwitchBtn">停用</a>
			</sec:authorize>
	</div>
	<!-- 部门表格工具栏结束 -->
	<!-- 职位表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
       <sec:authorize access="hasAuthority('ADDPOSITION_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'departmentDg'
       },
       dialog:{
           id:'positionAddDialog',
            width:600,
           height:400,
           href:'console/jsp/position_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           		if($('#departmentDg').iDatagrid('getSelected')){
           			var positionCode = $('#positionCode').val();
           			if(positionCode==null || ''===$.trim(positionCode)){
           				$.iMessager.alert('提示','请输入职位代码');
           				return false;
           			}
           			
           			var positionName = $('#positionName').val();
           			if(positionName==null || ''===$.trim(positionName)){
           			$.iMessager.alert('提示','请输入职位名称');
           				return false;
           			}
           			$.get('position/addPosition.do',{
           			code:positionCode,
           			name:positionName,
           			'department.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#positionNote').val()
           			},function(data){
           				if(data.success){
	           				$('#positionAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}else{
           			alert('请选择部门');
           			$('#positionAddDialog').iDialog('close');
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('EDITPOSITION_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/position_edit.jsp',
                url:'position/queryPositionById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var positionCode = $('#positionCode_edit').val();
           			var positionName = $('#positionName_edit').val();
           			if(positionName==null || ''===$.trim(positionName)){
           				$.iMessager.alert('提示','请输入职位名称');
           				return false;
           			}
           			$.get('position/updatePosition.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:positionCode,
           			name:positionName,
           			'department.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#positionNote_edit').val()
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
            <sec:authorize access="hasAuthority('DELPOSITION_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'position/deletePosition.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('DISABLEPOSITION_DEPARTMENT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'position/disabledPosition.do',
       grid: {uncheckedMsg:'请选择操作的职位',id:'position',param:'id:id'}" id="positionSwitchBtn">停用</a>
       </sec:authorize>
	</div>
	<!-- 职位表格工具栏结束 -->
	<!-- 相关文档表格工具栏开始 -->
	<div id="relateDoc-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#relateDoc-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'userAddDialog',
           href:_ctx + '/html/complex/dialog_add.html',
           buttonsGroup:[
               {text:'保存',url:_ctx + '/json/response/success.json',iconCls:'fa fa-plus',handler:'ajaxForm',btnCls:'topjui-btn-brown'}
           ]
       }">新增</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#relateDoc-toolbar',
            iconCls: 'fa fa-pencil',
            grid: {
                type: 'datagrid',
                id: 'userDg'
            },
            dialog: {
                width: 950,
                height: 500,
                href: _ctx + '/html/complex/user_edit.html?uuid={uuid}',
                url: _ctx + '/json/product/detail.json?uuid={uuid}',
                buttonsGroup: [
                    {
                        text: '更新',
                        url: _ctx + '/json/response/success.json',
                        iconCls: 'fa fa-save',
                        handler: 'ajaxForm',
                        btnCls: 'topjui-btn-green'
                    }
                ]
            }">编辑</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#relateDoc-toolbar',
       iconCls:'fa fa-trash',
       url:_ctx + '/json/response/success.json',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'uuid:uuid,code:code'}">删除</a>
		<!--     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'filter',
       extend: '#userDg-toolbar'
       ">过滤</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'search',
       extend: '#userDg-toolbar'">查询</a> -->
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'search',
       extend: '#relateDoc-toolbar'">停用</a>
	</div>
	<!-- 相关文档表格工具栏结束 -->
</body>
</html>