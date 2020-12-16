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
                       url:'relatedDocumentType/queryRelatedDocumentType.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('relatedDocumentTypeSwitchBtn',row.disabled);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'文档类型编码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'文档类型名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'moduleName',title:'所属模块',width:'180px',align:'center'"></th>
                        <th data-options="field:'createUsername',title:'创建人',width:'180px',align:'center'"></th>
                        <th data-options="field:'createDate',title:'创建时间',width:'180px',align:'center',formatter:function(value,row,index){
                       		if(value){
                       			var date = new Date(value);
                       			return getDateTime(date);
                       		}else{
                       			return '';
                       		}
                        }"></th>
                        <th data-options="field:'modifyUsername',title:'修改人',width:'180px',align:'center'"></th>
                        <th data-options="field:'modifyDate',title:'修改时间',width:'180px',align:'center',formatter:function(value,row,index){
                       		if(value){
                       			var date = new Date(value);
                       			return getDateTime(date);
                       		}else{
                       			return '';
                       		}
                        }"></th>
                        <th data-options="field:'disabled',title:'停用',width:'180px',align:'center',
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
                         <th data-options="field:'note',title:'说明',width:'180px',align:'center'"></th>
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
<sec:authorize access="hasAuthority('ADD_RELATEDOCUMENTTYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'relatedDocumentTypeAddDialog',
           width:600,
           height:600,
           href:'console/jsp/relatedDocumentType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
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
           			$.get('relatedDocumentType/addRelatedDocumentType.do',{
           			code:code,
           			name:name,
           			note:$('#note').val(),
           			moduleCode:$('#moduleCode').iCombobox('getValue'),
           			moduleName:$('#moduleCode').iCombobox('getText')
           			},function(data){
           				if(data.success){
	           				$('#relatedDocumentTypeAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#relatedDocumentTypeAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_RELATEDOCUMENTTYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'relatedDocumentTypeEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/relatedDocumentType_edit.jsp',
                url:'relatedDocumentType/queryRelatedDocumentTypeById.do?id={id}',
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
           			$.get('relatedDocumentType/updateRelatedDocumentType.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			note:$('#note').val()
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
<sec:authorize access="hasAuthority('DEL_RELATEDOCUMENTTYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'relatedDocumentType/deleteRelatedDocumentType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_RELATEDOCUMENTTYPE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'relatedDocumentType/disabledRelatedDocumentType.do',
       grid: {uncheckedMsg:'请选择操作的关联文档类型',id:'departmentDg',param:'id:id'}" id="relatedDocumentTypeSwitchBtn">停用</a>
</sec:authorize>
    </div>
</body>
</html>