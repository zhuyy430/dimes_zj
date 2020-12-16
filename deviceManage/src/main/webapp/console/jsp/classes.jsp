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
                       url:'classes/queryClasses.do',
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
                        <th data-options="field:'classType',title:'班次类别',width:'180px',align:'center',formatter:function(value,row,index){
                            if (row.classType) {
                                return row.classType.name;
                            } else {
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'code',title:'班次代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'班次名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'startTime',title:'开始时间',width:'180px',align:'center'"></th>
                        <th data-options="field:'endTime',title:'结束时间',width:'180px',align:'center'"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
                        <th data-options="field:'disabled',title:'停用',width:'180px',align:'center',
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
            <%--<div data-options="region:'south',fit:false,split:true,border:false"
                 style="height:40%">
                <div data-toggle="topjui-tabs"
                     data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'classesId:id'
                     }">
                    <div title="设备排班" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <!-- datagrid表格 -->
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'classes/queryDevicesByClassesId.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
                                <th data-options="field:'deviceCode',title:'设备代码',width:'180px',align:'center',
                                formatter:function(value,row,index){
                                	if(row.device){
                                		return row.device.code;
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'deviceName',title:'设备名称',width:'180px',align:'center',
                                formatter:function(value,row,index){
                                	if(row.device){
                                		return row.device.name;
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'deviceUnitType',title:'规格型号',width:'180px',align:'center',
                                formatter:function(value,row,index){
                                	if(row.device){
                                		return row.device.unitType;
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'code',title:'设备站点代码',width:'180px',align:'center'"></th>
                                <th data-options="field:'name',title:'设备站点名称',width:'180px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>--%>
        </div>
    </div>
</div>

<!-- 部门表格工具栏开始 -->
<div id="departmentDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
    <sec:authorize access="hasAuthority('ADD_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:600,
           href:'console/jsp/classes_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入班次代码!');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入班次名称!');
           				return false;
           			}
           			var classTypeName = $('#classTypeName').iCombobox('getText');
           			if(!classTypeName){
           			    $.iMessager.alert('提示','请输入或选择班次类别!');
           				return false;
           			 }
           			$.get('classes/addClasses.do',{
           			code:code,
           			name:name,
           			'classType.name':classTypeName,
           			startTime:$('#startTime').val(),
           			endTime:$('#endTime').val(),
           			note:$('#note').val()
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
    <sec:authorize access="hasAuthority('EDIT_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/classes_edit.jsp',
                url:'classes/queryClassById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入班次名称!');
           				return false;
           			}
           			var classTypeName = $('#classTypeName').iCombobox('getText');
           			if(!classTypeName){
           			    $.iMessager.alert('提示','请输入或选择班次类别!');
           				return false;
           			 }
           			$.get('classes/updateClasses.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			'classType.name':classTypeName,
           			startTime:$('#startTime').val(),
           			endTime:$('#endTime').val(),
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
    <sec:authorize access="hasAuthority('DEL_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'classes/deleteClasses.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('DISABLE_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'classes/disabledClasses.do',
       grid: {uncheckedMsg:'请选择操作的班次',id:'departmentDg',param:'id:id'}" id="classSwitchBtn">停用</a>
    </sec:authorize>
    </div>

<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	id:'departmentDg',
       	type:'datagrid',
       	param:'classesId:id'
       },
       dialog:{
           id:'deviceAddDialog',
            width:620,
           height:500,
           href:'console/jsp/classes_device_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#deviceTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的设备!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('classes/addDevice4Classes.do',{
           				classesId:$('#departmentDg').iDatagrid('getSelected').id,
           				deviceIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#position').iDatagrid('reload');
           					$('#deviceTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'classes/deleteDeviceFromClasses.do?classesId={parent.id}',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'deviceSiteId:id'}">删除</a>
    </div>
</body>
</html>