<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'DispatchedLevel/queryDispatchedLevel.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'等级编码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'等级名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'orderType',title:'类别',width:'180px',align:'center'"></th>
                        <th data-options="field:'timing',title:'定时时间(分)',width:'180px',align:'center'"></th>
                        <th data-options="field:'color',title:'颜色',width:'180px',align:'center'"></th>
                        <th data-options="field:'employee',title:'推送人',width:'180px',align:'center',formatter:function(value,row,index){
                       		if(row.employee){
                       			return row.employee.name;
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
       <sec:authorize access="hasAuthority('ADD_ORDERLEVEL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'orderLevelAddDialog',
           width:600,
           height:600,
           href:'console/jsp/orderLevel_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           	var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入等级编码!');
           				return false;
           			}
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入等级名称!');
           				return false;
           			}
           			$.get('DispatchedLevel/addDispatchedLevel.do',{
           			code:code,
           			name:name,
           			timing:$('#timing').val(),
           			orderType:$('#orderType').iCombobox('getValue'),
           			color:$('#color').val(),
           			'employee.id':$('#employee').iCombobox('getValue')
           			},function(data){
           				if(data.success){
	           				$('#orderLevelAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#orderLevelAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
           </sec:authorize>
           <sec:authorize access="hasAuthority('EDIT_ORDERLEVEL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'relatedDocumentTypeEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/orderLevel_edit.jsp',
                url:'DispatchedLevel/queryDispatchedLevelById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入关联文档类型编码!');
           				return false;
           			}
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入关联文档类型名称!');
           				return false;
           			}
           			$.get('DispatchedLevel/updateDispatchedLevel.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			timing:$('#timing').val(),
           			orderType:$('#orderType').iCombobox('getValue'),
           			color:$('#color').val(),
           			'employee.id':$('#employee').iCombobox('getValue')
           			},function(data){
           				if(data.success){
	           				$('#relatedDocumentTypeEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#relatedDocumentTypeEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
            </sec:authorize>
            <sec:authorize access="hasAuthority('DEL_ORDERLEVEL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'DispatchedLevel/deleteDispatchedLevel.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
       </sec:authorize>
       <%-- <sec:authorize access="hasAuthority('DISABLE_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'relatedDocumentType/disabledRelatedDocumentType.do',
       grid: {uncheckedMsg:'请选择操作的关联文档类型',id:'departmentDg',param:'id:id'}" id="relatedDocumentTypeSwitchBtn">停用</a>
       </sec:authorize> --%>
    </div>
</body>
</html>