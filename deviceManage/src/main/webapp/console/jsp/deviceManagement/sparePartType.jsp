<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../../common/jsp/head.jsp" %>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
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
			   url:'projectType/queryTopProjectTypes.do',
			   queryParams:{
			   		rootType:'ROOTSPAREPARTTYPE'
			   },
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'设备类别'"></th>
					</tr>
				</thead>
			</table>
		</div>
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'projectType/queryProjectTypes.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('classSwitchBtn',row.disabled);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'类别代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'类别名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',sortable:false"></th>
                        <th data-options="field:'parentCode',title:'上级类别代码',width:'180px',sortable:false,formatter:function(value,row,index){
                        	if(row.parent){
                        		return row.parent.code;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'parentName',title:'上级类别名称',width:'180px',sortable:false,formatter:function(value,row,index){
                        	if(row.parent){
                        		return row.parent.name;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <!-- <th data-options="field:'disabled',title:'停用',width:'180px',sortable:false,
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th> -->
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
<sec:authorize access="hasAuthority('ADD_SPAREPART_TYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'DeviceTypeAddDialog',
           width:600,
           height:600,
           href:'console/jsp/deviceManagement/sparePartType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入备件类别代码!');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入备件类别名称!');
           				return false;
           			}
           			$.get('projectType/addProjectType.do',{
           			code:code,
           			name:name,
           			type:'sparePartType',
           			'parent.id':$('#departmentTg').treegrid('getSelected').id,
           			note:$('#note').val(),
           			type:'sparePartType'
           			},function(data){
           				if(data.success){
	           				$('#DeviceTypeAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').treegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#DeviceTypeAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_SPAREPART_TYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/sparePartType_edit.jsp',
                url:'projectType/queryProjectTypeById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入设备类别名称!');
           				return false;
           			}
           			$.get('projectType/updateProjectType.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			type:'sparePartType',
           			'parent.id':$('#departmentTg').treegrid('getSelected').id,
           			note:$('#note').val(),
           			type:'sparePartType'
           			},function(data){
           				if(data.success){
	           				$('#classEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#classEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_SPAREPART_TYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'projectType/deleteProjectType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
       <%-- <sec:authorize access="hasAuthority('DISABLE_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'deviceType/disabledDeviceType.do',
       grid: {uncheckedMsg:'请选择操作的班次',id:'departmentDg',param:'id:id'}" id="classSwitchBtn">停用</a>
       </sec:authorize> --%>
    </div>
<!-- 职位表格工具栏开始 -->
</body>
</html>