<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
</head>
<script type="text/javascript">
	//填充id数组
	function fillArray(idsArray,ids){
		for(var i = 0;i<ids.length;i++){
			var idObj = ids[i];
			fillParent(idsArray,idObj);
		}
	}
	//递归查找父节点
	function fillParent(idsArray,idObj){
		var exist = false;
		for(var i = 0;i<idsArray.length;i++){
			var id = idsArray[i];
			if(id==idObj.id){
				exist = true;
				break;
			}
		}
		if(!exist){
			idsArray.push(idObj.id);
		}
		var parent = $('#menus').iTreegrid("getParent",idObj.id);
		if(parent){
			fillParent(idsArray,parent);
		}
	}
</script>
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
                       url:'role/queryRoles.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('roleSwitchBtn',row.disable);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'roleName',title:'角色名',width:'180px',align:'center'"></th>
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
                        <th data-options="field:'note',title:'说明',sortable:false,width:'180px'"></th>
                        <th data-options="field:'disable',title:'停用',sortable:false,width:'180px',formatter:function(value,row,index){
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
<sec:authorize access="hasAuthority('ADD_ROLE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:400,
           href:'console/jsp/role_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var roleName = $('#roleName').val();
           			if(roleName==null || ''===$.trim(roleName)){
           				$.iMessager.alert('提示','请输入角色名称');
           				return false;
           			}
           			$.get('role/addRole.do',{
           			roleName:roleName,
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
<sec:authorize access="hasAuthority('EDIT_ROLE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                onBeforeOpen:function(){
                	var isAllowedDelete = $('#departmentDg').iDatagrid('getSelected').allowDelete;
                	if(isAllowedDelete==false){
                		alert('系统预设角色，不允许修改!');
                		return false;
                	}
                },
                href: 'console/jsp/role_edit.jsp',
                url:'role/queryRoleById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var roleName = $('#roleName').val();
           			if(roleName==null || ''===$.trim(roleName)){
           				$.iMessager.alert('提示','请输入角色名称');
           				return false;
           			}
           			$.get('role/updateRole.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			roleName:roleName,
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
<sec:authorize access="hasAuthority('DEL_ROLE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'role/deleteRole.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_ROLE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'role/disabledRole.do',
       grid: {uncheckedMsg:'请选择操作的记录',id:'departmentDg',param:'id:id'}" id="roleSwitchBtn">停用</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SIGNPOWERS_ROLE')">
        <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-key',
            dialog: {
            	id:'signPowersDialog',
                width: 800,
                height: 700,
                href: 'console/jsp/role_signPowers.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var ids = $('#powers').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			//alert('请选择要添加的权限!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('role/signPowers.do',{
           				roleId:$('#departmentDg').iDatagrid('getSelected').id,
           				powerIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$.iMessager.alert('提示','操作完成','info',function(r){
           						$('#signPowersDialog').iDialog('close');
           					});
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signPowersDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">分配权限</a>
        <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-key',
            dialog: {
            	id:'signMCPowersDialog',
                width: 400,
                height: 500,
                href: 'console/jsp/role_signMCPowers.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var arr_v = new Array(); 
				$('input[type=checkbox]:checked').each(function(){ 
				     arr_v.push($(this).val()); 
				}); 
           		if(arr_v.length>0){
           			 $.get('role/signMCPowers.do',{
           				roleId:$('#departmentDg').iDatagrid('getSelected').id,
           				powerNames:JSON.stringify(arr_v)
           			},function(data){
           				if(data.success){
           					$.iMessager.alert('提示','操作完成','info',function(r){
           						$('#signMCPowersDialog').iDialog('close');
           					});
           				}else{
           					alert(data.msg);
           				}
           			}); 
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signMCPowersDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">MC端权限</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SIGNMODULES_ROLE')">
        <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-cube',
            parentGrid:{
            	id:'departmentDg',
            	type:'datagrid',
            	param:'roleId:id',
            	unselectedMsg:'请选择菜单'
            },
            dialog: {
            	id:'signMenuDialog',
                width: 800,
                height: 700,
                href: 'console/jsp/role_signMenus.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var ids = $('#menus').iTreegrid('getCheckedNodes');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			//alert('请选择要添加的菜单!');
           		}else{
           			fillArray(idsArray,ids);
           			$.get('role/signMenus.do',{
           				roleId:$('#departmentDg').iDatagrid('getSelected').id,
           				menuIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$.iMessager.alert('提示','操作完成','info',function(r){
           						$('#signMenuDialog').iDialog('close');
           					});
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signMenuDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">分配菜单</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SIGNUSER_ROLE')">
             <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-user',
            parentGrid:{
            	id:'departmentDg',
            	type:'datagrid',
            	param:'roleId:id',
            	unselectedMsg:'请选择用户'
            },
            dialog: {
            	id:'signUserDialog',
                width: 800,
                height: 700,
                href: 'console/jsp/role_signUsers.jsp',
                 buttons:[
           	{text:'确定',handler:function(){
           		var ids = $('#users').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			//alert('请选择要添加的菜单!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('role/signUsers.do',{
           				roleId:$('#departmentDg').iDatagrid('getSelected').id,
           				userIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$.iMessager.alert('提示','操作完成','info',function(r){
           						$('#signUserDialog').iDialog('close');
           					});
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#signUserDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">分配用户</a>
</sec:authorize>
    </div>
<!-- 部门表格工具栏结束 -->
</body>
</html>