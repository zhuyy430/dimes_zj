<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../../common/jsp/head.jsp" %>
<script type="text/javascript">
function downloadFile(){
	 	window.location.href="relatedDoc/downloadTemplates.do?name=设备项目模版";
}
</script>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
        <!-- 设备点检 -->
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'deviceProject/queryDeviceProjectByType2.do?type=SPOTINSPECTION',
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
                        <th data-options="field:'code',title:'项目编码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'项目名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'standard',title:'标准',width:'180px',sortable:false,align:'center'"></th>
                        <th data-options="field:'method',title:'方法',width:'180px',sortable:false,align:'center'"></th>
                        <th data-options="field:'frequency',title:'频次',width:'180px',sortable:false,align:'center'"></th>
                        <th data-options="field:'note',title:'说明',width:'180px',sortable:false,align:'center'"></th>
                        <th data-options="field:'deviceTypeName',title:'设备类别',width:'180px',align:'center'
                        ,formatter:function(value,row,index){
                        	if(row.projectType){
                        		return row.projectType.name;
                        	}else{
                        		return '';
                        	}
                        }"></th>
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
<sec:authorize access="hasAuthority('ADD_SPOTCHECKPROJECT')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'DeviceTypeAddDialog',
           width:600,
           height:600,
           href:'console/jsp/deviceManagement/deviceProject_add.jsp',
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
           			'projectType.id':$('#deviceTypeId').val(),
           			note:$('#note').val(),
           			standard:$('#standard').val(),
           			method:$('#method').val(),
           			frequency:$('#frequency').val(),
           			type:'SPOTINSPECTION'
           			},function(data){
           				if(data.success){
	           				$('#DeviceTypeAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
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
<sec:authorize access="hasAuthority('EDIT_SPOTCHECKPROJECT')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/deviceProject_edit.jsp',
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
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			'projectType.id':$('#deviceTypeId').val(),
           			note:$('#note').val(),
           			standard:$('#standard').val(),
           			method:$('#method').val(),
           			frequency:$('#frequency').val(),
           			type:'SPOTINSPECTION'
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
<sec:authorize access="hasAuthority('DEL_SPOTCHECKPROJECT')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProject/deleteDeviceProject.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}
       ">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('IMPORT_SPOTCHECKPROJECT_DOC')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-level-down',
            dialog: {
            	id:'importDialog',
                width: 400,
                height: 300,
                href: 'console/jsp/deviceManagement/deviceInspectionProject_import.jsp',
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
             		formData.append('type', 'SPOTINSPECTION');   
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
           						 $('#departmentDg').iDatagrid('reload');
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
<sec:authorize access="hasAuthority('EXPORT_SPOTCHECKPROJECT_DOC')">
             <a href="javascript:void(0)"  data-options="iconCls:'fa fa-download'"
       data-toggle="topjui-menubutton" onclick='downloadFile()'>导出模板</a>
</sec:authorize>
    </div>
<!-- 职位表格工具栏开始 -->
</body>
</html>