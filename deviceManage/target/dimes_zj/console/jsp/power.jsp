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
                       url:'power/queryPowers.do',
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
                        <th data-options="field:'powerCode',title:'权限码',width:'180px',align:'center'"></th>
                        <th data-options="field:'powerName',title:'权限名',width:'180px',align:'center'"></th>
                        <th data-options="field:'group',title:'所属模块',width:'180px',align:'center'"></th>
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
      <%-- <sec:authorize access="hasAuthority('ADD_POWER')">
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
        </sec:authorize>--%>
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