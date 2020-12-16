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
                       url:'productionUnitMapping/queryProductionUnitMappings.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}]">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'clientIp',title:'绑定IP',width:'180px',align:'center'"></th>
                        <th data-options="field:'productionUnitCode',title:'生产单元代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'productionUnitName',title:'生产单元名称',width:'180px',sortable:false"></th>
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
<sec:authorize access="hasAuthority('ADD_PRODUCTIONUNITKANBAN')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:600,
           href:'console/jsp/productionUnitMapping_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var clientIp = $('#clientIp').val();
           			if(clientIp==null || ''===$.trim(clientIp)){
           				$.iMessager.alert('提示','请输入客户端IP地址!');
           				$('clientIp').focus();
           				return false;
           			}
           			 var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    				var reg = clientIp.match(exp);
           			if(reg==null){
           				$.iMessager.alert('提示','您输入的IP地址不合法!');
           				return false;
           			}
           			
           			var productionUnitId = $('#productionUnitId').val();
           			if(productionUnitId==null || ''===$.trim(productionUnitId)){
           				$.iMessager.alert('提示','请选择绑定的生产单元!');
           				return false;
           			}
           			$.get('productionUnitMapping/addProductionUnitMapping.do',{
           			clientIp:clientIp,
           			'productionUnit.id':productionUnitId
           			},function(data){
           				if(data.success){
	           				$('#classesAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#classesAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_PRODUCTIONUNITKANBAN')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/productionUnitMapping_edit.jsp',
                url:'productionUnitMapping/queryProductionUnitMappingById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			var productionUnitId = $('#productionUnitId').val();
           			if(productionUnitId==null || ''===$.trim(productionUnitId)){
           				$.iMessager.alert('提示','请选择绑定的生产单元!');
           				return false;
           			}
           			$.get('productionUnitMapping/updateProductionUnitMapping.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			clientIp:$('#clientIp').val(),
           			'productionUnit.id':productionUnitId
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
<sec:authorize access="hasAuthority('DEL_PRODUCTIONUNITKANBAN')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'productionUnitMapping/deleteProductionUnitMapping.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
</body>
</html>