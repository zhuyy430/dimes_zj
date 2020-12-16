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
                       url:'warehouse/queryWarehouses.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('powerSwitchBtn',row.disable);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'warehouseCode',title:'仓库代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'warehouseName',title:'仓库名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'count',title:'现有库存',width:'180px',align:'center'"></th>
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
       <sec:authorize access="hasAuthority('ADD_POWER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:400,
           href:'console/jsp/power_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var powerName = $('#powerName').val();
           			if(powerName==null || ''===$.trim(powerName)){
           				$.iMessager.alert('提示','请输入权限名称');
           				return false;
           			}
           			var powerCode = $('#powerCode').val();
           			if(powerCode==null || ''===$.trim(powerCode)){
           				$.iMessager.alert('提示','请输入权限码');
           				return false;
           			}
           			$.get('power/addPower.do',{
           			powerCode:powerCode,
           			powerName:powerName,
           			group:$('#group').iCombobox('getText'),
           			note:$('#note').iTextarea('getText')
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
           <sec:authorize access="hasAuthority('EDIT_POWER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/power_edit.jsp',
                url:'power/queryPowerById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           	var powerCode = $('#powerCode').val();
           			var powerName = $('#powerName').val();
           			if(powerName==null || ''===$.trim(powerName)){
           				$.iMessager.alert('提示','请输入权限名称');
           				return false;
           			}
           			$.get('power/updatePower.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			powerName:powerName,
           			group:$('#group').iCombobox('getText'),
           			note:$('#note').val()
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
            <sec:authorize access="hasAuthority('DEL_POWER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'power/deletePower.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
        </sec:authorize>
       <sec:authorize access="hasAuthority('DISABLE_POWER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'power/disabledPower.do',
       grid: {uncheckedMsg:'请选择操作的记录',id:'departmentDg',param:'id:id'}" id="powerSwitchBtn">停用</a>
        </sec:authorize>
    </div>
<!-- 部门表格工具栏结束 -->
</body>
</html>