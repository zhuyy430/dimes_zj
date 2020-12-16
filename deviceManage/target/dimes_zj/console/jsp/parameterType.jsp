<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <!-- treegrid表格 -->
        <table
         data-toggle="topjui-treegrid"
               data-options="id:'parameterTypeTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'parameterType/queryTopParameterTypes.do',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
            <thead>
            <tr>
               <!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
                <th data-options="field:'name',width:'100%',title:'参数类型'"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'parameterType/queryParameterTypesByParentId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'parameterTypeTg',
                       },
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('parameterTypeSwitchBtn',row.disabled);
					           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{parameterTypeId:''});
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'类别代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'baseCode',title:'baseCode',width:'180px',align:'center',hidden:true"></th>
                        <th data-options="field:'name',title:'类别名称',width:'180px',align:'center',sortable:false"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',align:'center',sortable:true"></th>
                        <th data-options="field:'parentCode',title:'父类别代码',width:'180px',align:'center',sortable:true,
                        formatter:function(value,row,index){
                            if (row.parent) {
                                return row.parent.code;
                            } else {
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'parentName',title:'父类别名称',width:'180px',align:'center',sortable:true, 
                        formatter:function(value,row,index){
                            if (row.parent) {
                                return row.parent.name;
                            } else {
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'disabled',title:'停用',width:'180px',align:'center',sortable:true,
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
                 style="height:40%">
                <div data-toggle="topjui-tabs"
                     data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'parameterTypeId:id'
                     }">
                    <div title="参数信息" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <!-- datagrid表格 -->
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'parameter/queryParametersByParameterTypeId.do',
						       onSelect:function(index,row){
					           		switchButton('parameterSwitchBtn',row.disabled);
					           }">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                <th data-options="field:'code',title:'参数代码',width:'180px',align:'center',sortable:true"></th>
                                <th data-options="field:'name',title:'参数名称',width:'180px',align:'center',sortable:true"></th>
                                <th data-options="field:'kfc',title:'是否kfc',width:'180px',align:'center',sortable:true,formatter:function(value,rows,index){
                                	if(value){
                                		return 'Y';
                                	}else{
                                		return 'N';
                                	}
                                }"></th>
                                <th data-options="field:'note',title:'备注',width:'180px',align:'center',sortable:true"></th>
                                <th data-options="field:'disabled',title:'停用',width:'180px',align:'center',sortable:true,
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
                    <!-- <div title="相关文档" data-options="id:'tab1',iconCls:'fa fa-th'">
                        datagrid表格
                        <table data-toggle="topjui-datagrid"
                               data-options="id:'relateDoc',
                               initCreate: false,
                               fitColumns:true,
						       url:'department/queryRaletedDocuments.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:true"></th>
                                <th data-options="field:'documentName',title:'文档名称',sortable:false"></th>
                                <th data-options="field:'note',title:'文档说明',sortable:false"></th>
                                <th data-options="field:'sex',title:'性别',sortable:true,
                                formatter:function(value,row,index){
                                    if (value == '1') {
                                        return '男';
                                    } else if (value == '2') {
                                        return '女';
                                    } else {
                                        return '';
                                    }
                                }"></th>
                                <th data-options="field:'relatedCode',title:'关联代码',sortable:false"></th>
                                <th data-options="field:'relatedName',title:'关联名称',sortable:false"></th>
                                <th data-options="field:'uploadUsername',title:'上传人员',sortable:false"></th>
                                <th data-options="field:'uploadDate',title:'上传时间',sortable:false,
                                 formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                                        				' ' + hourStr + ':' + minuteStr + ':' + secondStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                                }"></th>
                            </tr>
                            </thead>
                        </table>
                    </div> -->
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
    <sec:authorize access="hasAuthority('ADDTYPE_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'treegrid',
               id:'parameterTypeTg'
            },
       dialog:{
           id:'parameterTypeAddDialog',
           width:600,
           height:400,
           href:'console/jsp/parameterType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入类别代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入类别名称');
           				return false;
           			}
           			var baseCode = $('#parameterTypeTg').iTreegrid('getSelected').baseCode;
           			if(!baseCode){
           				baseCode = $('#parameterTypeTg').iTreegrid('getSelected').code;
           			}
           			
           			$.get('parameterType/addParameterType.do',{
           			code:code,
           			name:name,
           			baseCode:baseCode,
           			'parent.id':$('#parameterTypeTg').iTreegrid('getSelected').id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterTypeAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#parameterTypeTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterTypeAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('EDITTYPE_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'parameterTypeEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/parameterType_edit.jsp',
                url:'parameterType/queryParameterTypeById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入类别名称');
           				return false;
           			}
           			$.get('parameterType/updateParameterType.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			baseCode:$('#departmentDg').iDatagrid('getSelected').baseCode,
           			'parent.id':$('#departmentDg').iDatagrid('getSelected').parent.id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterTypeEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterTypeEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('DELTYPE_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'parameterType/deleteParameterType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#parameterTypeTg').iTreegrid('reload');}">删除</a>
    </sec:authorize>
<!--     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'filter',
       extend: '#userDg-toolbar'
       ">过滤</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'search',
       extend: '#userDg-toolbar'">查询</a> -->
    <sec:authorize access="hasAuthority('DISABLETYPE_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'parameterType/disabledParameterType.do',
       grid: {uncheckedMsg:'请选择操作的参数类型',id:'departmentDg',param:'id:id'}" id="parameterTypeSwitchBtn">停用</a>
    </sec:authorize>
    </div>
<!-- 部门表格工具栏结束 -->
<!-- 职位表格工具栏开始 -->
<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
    <sec:authorize access="hasAuthority('ADD_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       dialog:{
           id:'parameterAddDialog',
            width:600,
           height:400,
           href:'console/jsp/parameter_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入参数代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入参数名称');
           				return false;
           			}
           			$.get('parameter/addParameter.do',{
           			code:code,
           			name:name,
           			baseCode:$('#departmentDg').iDatagrid('getSelected').baseCode,
           			'parameterType.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#note').val(),
           			kfc:$('input[name=kfc]:checked').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
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
    <sec:authorize access="hasAuthority('EDIT_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'parameterEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/parameter_edit.jsp',
                url:'parameter/queryParameterVOById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入参数名称');
           				return false;
           			}
           			$.get('parameter/updateParameter.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			'parameterType.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#note').val(),
           			kfc:$('#kfc').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
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
    <sec:authorize access="hasAuthority('DEL_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'parameter/deleteParameter.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('DISABLE_PARAMETER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
      data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'parameter/disabledParameter.do',
       grid: {uncheckedMsg:'请选择操作的参数',id:'position',param:'id:id'}" id="parameterSwitchBtn">停用</a>
    </sec:authorize>
    </div>
<!-- 职位表格工具栏结束 -->
<!-- 相关文档表格工具栏开始 -->
<div id="relateDoc-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
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
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
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
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
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
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'search',
       extend: '#relateDoc-toolbar'">停用</a>
    </div>
<!-- 相关文档表格工具栏结束 -->
</body>
</html>