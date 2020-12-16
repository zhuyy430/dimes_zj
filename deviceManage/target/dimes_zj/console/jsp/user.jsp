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
                       url:'user/queryUsers.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('userSwitchBtn',row.disable);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'username',title:'用户名',width:'180px',align:'center'"></th>
                        <th data-options="field:'createDate',title:'创建日期',width:'180px',align:'center',formatter:function(value,row,index){
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
                        <th data-options="field:'modifyDate',title:'修改日期',width:'180px',align:'center',formatter:function(value,row,index){
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
                        <th data-options="field:'employeeName',title:'关联员工',sortable:false,width:'80px',
                        formatter:function(value,row,index){
                        	if(row.employee){
                        		return row.employee.name;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'note',title:'说明',sortable:false,width:'80px'"></th>
                        <th data-options="field:'disable',title:'停用',sortable:false,width:'80px',formatter:function(value,row,index){
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
        </div>
    </div>
</div>

<!-- 部门表格工具栏开始 -->
<div id="departmentDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('ADD_USER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:400,
           href:'console/jsp/user_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var username = $('#username').val();
           			if(username==null || ''===$.trim(username)){
           				$.iMessager.alert('提示','请输入用户名称');
           				return false;
           			}
           			
           			var password = $('#password').val();
           			if(password==null || ''===$.trim(password)){
           				$.iMessager.alert('提示','请输入用户密码');
           				return false;
           			}
           			var confirmPassword = $('#confirmPassword').val();
           			if(password!=confirmPassword){
           				$.iMessager.alert('提示','用户密码与确认密码不一致 ！');
           				return false;
           			}
           			$.get('user/addUser.do',{
           			username:username,
           			password:password,
           			'employee.code':$('#employee').val(),
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
<sec:authorize access="hasAuthority('EDIT_USER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/user_edit.jsp',
                url:'user/queryUserById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var username = $('#username').val();
           			if(username==null || ''===$.trim(username)){
           				$.iMessager.alert('提示','请输入用户名');
           				return false;
           			}
           			$.get('user/updateUser.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			username:username,
           			'employee.code':$('#employee').val(),
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
<sec:authorize access="hasAuthority('DEL_USER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'user/deleteUser.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_USER')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'user/disabledUser.do',
       grid: {uncheckedMsg:'请选择操作的班次',id:'departmentDg',param:'id:id'}" id="userSwitchBtn">停用</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SIGNROLES_USER')">
      <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'signRolesDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/user_signRoles.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var ids = $('#roles').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的角色!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('user/signRoles.do',{
           				userId:$('#departmentDg').iDatagrid('getSelected').id,
           				roleIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#signRolesDialog').iDialog('close');
           					$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signRolesDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">分配角色</a>
</sec:authorize>
<sec:authorize access="hasAuthority('IMPORT_USER')">
      <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-user-plus',
            dialog: {
            	id:'signRolesDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/user_importUsers.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var ids = $('#employees').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要导入的员工!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].code);
           			}
           			$.get('employee/importUsers.do',{
           				employeeIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#signRolesDialog').iDialog('close');
           					$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signRolesDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">导入用户</a>
</sec:authorize>
    </div>
<!-- 部门表格工具栏结束 -->
</body>
</html>