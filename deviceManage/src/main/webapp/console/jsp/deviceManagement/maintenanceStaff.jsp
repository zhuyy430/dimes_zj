<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../../common/jsp/head.jsp" %>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'maintenanceStaff/queryAllMaintenanceStaff.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		<!-- switchButton('classSwitchBtn',row.disabled); -->
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'员工代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'员工姓名',width:'180px',align:'center'"></th>
                        <th data-options="field:'departmentName',title:'部门姓名',width:'180px',align:'center',formatter:function(value,row,index){
                            if (row.departmentName) {
                                return row.departmentName.name;
                            } else {
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'positionName',title:'岗位名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'queue',title:'派单顺序',width:'180px',align:'center'"></th>
                       <!--  <th data-options="field:'onDutyStatus',title:'在岗状态',width:'180px',align:'center',formatter:function(value,row,index){
                        		if (value=='ONDUTY') {
                                       return '在岗';
                                    }else if(value=='REST'){
                                    	return '休息';
                                    }else if(value=='NIGHTSHIFT'){
                                    	return '夜班';
                                    }else if(value=='MAINTENANCE'){
                                    	return '保养';
                                    }else if(value=='MAINTAIN'){
                                    	return '维修';
                                    }else{
                                    	return '';
                                    }
                       			 }"></th> -->
                        <th data-options="field:'workStatus',title:'工作状态',width:'180px',align:'center',formatter:function(value,row,index){
                        		if (value=='ONDUTY') {
                                       return '在岗';
                                    }else if(value=='REST'){
                                    	return '休息';
                                    }else if(value=='BEOUT'){
                                    	return '公出';
                                    }else if(value=='MAINTENANCE'){
                                    	return '保养';
                                    }else if(value=='MAINTAIN'){
                                    	return '维修';
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
                        <th data-options="field:'changeDate',title:'切换时间',width:'180px',align:'center',formatter:function(value,row,index){
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
                        <th data-options="field:'tel',title:'联系方式',width:'180px',align:'center'"></th>
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
                         param:'maintenanceStaffCode:code'
                     }">
                    <div title="历史记录" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <!-- datagrid表格 -->
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'maintenanceStaffStatusRecord/queryAllMaintenanceStaffStatusRecord.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
                                <th data-options="field:'code',title:'员工代码',width:'180px',align:'center'"></th>
                                <th data-options="field:'name',title:'员工姓名',width:'180px',align:'center'"></th>
                                <th data-options="field:'changeBeforeStatus',title:'切换前状态',width:'180px',align:'center',formatter:function(value,row,index){
                        		if (value=='ONDUTY') {
                                       return '在岗';
                                    }else if(value=='REST'){
                                    	return '休息';
                                    }else if(value=='NIGHTSHIFT'){
                                    	return '夜班';
                                    }else if(value=='MAINTENANCE'){
                                    	return '保养';
                                    }else if(value=='MAINTAIN'){
                                    	return '维修';
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
                                <th data-options="field:'changeAfterStatus',title:'切换后状态',width:'180px',align:'center',formatter:function(value,row,index){
                        		if (value=='ONDUTY') {
                                       return '在岗';
                                    }else if(value=='REST'){
                                    	return '休息';
                                    }else if(value=='NIGHTSHIFT'){
                                    	return '夜班';
                                    }else if(value=='MAINTENANCE'){
                                    	return '保养';
                                    }else if(value=='MAINTAIN'){
                                    	return '维修';
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
                                <th data-options="field:'changeDate',title:'切换时间',width:'180px',align:'center',formatter:function(value,row,index){
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
                                <th data-options="field:'operator',title:'操作人',width:'180px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div title="人员排班" data-options="id:'tab1',iconCls:'fa fa-th'">
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'employeeScheduling',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'employeeSchedulingRecord/queryEmployeeSchedulingRecordsByMaintenanceStaffCode.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
                                <th data-options="field:'schedulingDate',title:'排班日期',width:'180px',align:'center',formatter:function(value,row,index){
                                	if(value){
                                		return getDate(new Date(value));
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'employeeName',title:'员工姓名',width:'180px',align:'center'"></th>
                                <th data-options="field:'className',title:'班次',width:'180px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div title="生产单元" data-options="id:'tab2',iconCls:'fa fa-th'">
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'employeeProductionunit',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'employeeProductionUnitRecord/queryEmployeeProductionUnitRecordByMaintenanceStaffCode.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
                                <th data-options="field:'name',title:'生产单元名称',width:'180px',align:'center',formatter:function(value,row,index){
                                	if(row.productionUnit){
                                		return row.productionUnit.name;
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'code',title:'生产单元代码',width:'180px',align:'center',formatter:function(value,row,index){
                                	if(row.productionUnit){
                                		return row.productionUnit.code;
                                	}else{
                                		return '';
                                	}
                                }"></th>
                                <th data-options="field:'note',title:'备注',width:'180px',align:'center',formatter:function(value,row,index){
                                	if(row.productionUnit){
                                		return row.productionUnit.note;
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
    </div>
</div>

<!-- 部门表格工具栏开始 -->
<div id="departmentDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'AddDialog',
           width:600,
           height:600,
           href:'console/jsp/deviceManagement/maintenanceStaff_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
	           	var ids = $('#employeeTab').iDatagrid('getSelections');
	           		var idsArray = new Array();
	           		if(ids.length<=0){
	           			alert('请选择要添加的数据!');
	           		}else{
	           			for(var i = 0;i < ids.length;i++){
	           				idsArray.push(ids[i].code);
	           			}
	           		}
           			$.get('maintenanceStaff/addMaintenanceStaff.do',{
           			employeeCodes:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
	           				$('#AddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#AddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/maintenanceStaff_edit.jsp',
                url:'maintenanceStaff/queryById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('maintenanceStaff/update.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			queue:$('#queue').val(),
           			workStatus:$('#workStatus').iCombobox('getValue')
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
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceStaff/delete.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
      <%-- <sec:authorize access="hasAuthority('ONDUTY_MAINTENANCESTAFF')">
     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'maintenanceStaff/updateStatus.do?status=ONDUTY',
       grid: {uncheckedMsg:'请选择要操作的员工',id:'departmentDg',param:'id:id'}" >在岗</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('REST_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'maintenanceStaff/updateStatus.do?status=REST',
       grid: {uncheckedMsg:'请选择要操作的员工',id:'departmentDg',param:'id:id'}" >休息</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('NIGHTSHIFT_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'maintenanceStaff/updateStatus.do?status=NIGHTSHIFT',
       grid: {uncheckedMsg:'请选择要操作的员工',id:'departmentDg',param:'id:id'}" >夜班</a>
       </sec:authorize>
       <sec:authorize access="hasAuthority('MAINTENANCE_MAINTENANCESTAFF')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'maintenanceStaff/updateStatus.do?status=MAINTENANCE',
       grid: {uncheckedMsg:'请选择要操作的员工',id:'departmentDg',param:'id:id'}">保养</a>
       </sec:authorize> --%>
    </div>
<div id="employeeScheduling-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'employeeScheduling'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFF_SCHEDULING')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#employeeScheduling-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'departmentDg',
           param:'employeeCodes:code,employeeNames:name'
       },
       dialog:{
           id:'AddEmployeeSchedulingDialog',
           width:600,
           height:400,
           href:'console/jsp/deviceManagement/maintenanceStaff_employeeScheduling_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
	           	var  from = $('#from').val();
           			var to = $('#to').val();
           			if(!from || !to){
           				$.iMessager.alert('警告','请选择日期区间!');
           				return false;
           			}
           			var fromDate = new Date(from.replace(/-/g,'/'));
           			var toDate = new Date(to.replace(/-/g,'/'));
           			if(fromDate.getTime()>toDate.getTime()){
           				$.iMessager.alert('警告','开始日期不能大于结束日期!');
           				return false;
           			}
           			//获取班次编码
           			var classesCode = $('#classes').iCombobox('getValue');
           			if(!classesCode){
           				$.iMessager.alert('警告','请选择班次!');
           				return false;
           			}
           			//员工编码
           			var employeeCodes = $('#employeeCodes').val();
           			var employeeNames = $('#employeeNames').val();
           			if(!employeeCodes){
           				$.iMessager.alert('警告','请输入员工信息!');
           				return false;
           			}
           			$.get('employeeSchedulingRecord/generateEmployeeScheduling.do',{
           			from:from,
           			to:to,
           			classCode:classesCode,
           			className:$('#classes').iCombobox('getText'),
           			employeeCodes:employeeCodes,
           			employeeNames:employeeNames
           			},function(data){
           				if(data.success){
	           				$('#AddEmployeeSchedulingDialog').iDialog('close');
	           				$('#employeeScheduling').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#AddEmployeeSchedulingDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFF_SCHEDULING')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#employeeScheduling-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'updateEmployeeSchedulingDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/maintenanceStaff_employeeScheduling_edit.jsp',
                url:'employeeSchedulingRecord/queryEmployeeSchedulingRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('employeeSchedulingRecord/updateEmployeeSchedulingRecord.do',{
           			id:$('#id').val(),
           			classCode:$('#classCode').iCombobox('getValue'),
           			className:$('#classCode').iCombobox('getText')
           			},function(data){
           				if(data.success){
	           				$('#updateEmployeeSchedulingDialog').iDialog('close');
	           				$('#employeeScheduling').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#updateEmployeeSchedulingDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFF_SCHEDULING')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#employeeScheduling-toolbar',
       iconCls:'fa fa-trash',
       url:'employeeSchedulingRecord/deleteEmployeeSchedulingRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'employeeScheduling',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
<div id="employeeProductionunit-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'employeeProductionunit'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFF_PRODUCTIONLINE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: 'employeeProductionunit-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'departmentDg',
           param:'employeeCodes:code,employeeNames:name'
       },
       dialog:{
           id:'AddEmployeeProductionunitDialog',
           width:600,
           height:400,
           href:'console/jsp/deviceManagement/maintenanceStaff_ProductionUnit_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           	var ids = $('#employeProductionUnitTab').iDatagrid('getSelections');
	           		var idsArray = new Array();
	           		if(ids.length<=0){
	           			alert('请选择要添加的数据!');
	           		}else{
	           			for(var i = 0;i < ids.length;i++){
	           				idsArray.push(ids[i].id);
	           			}
	           		}
           			$.get('employeeProductionUnitRecord/addEmployeeProductionUnitRecord.do',{
           			id:JSON.stringify(idsArray),
           			employeeCode:$('#departmentDg').iTreegrid('getSelected').code
           			},function(data){
           				if(data.success){
	           				$('#AddEmployeeProductionunitDialog').iDialog('close');
	           				$('#employeeProductionunit').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#AddEmployeeProductionunitDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFF_PRODUCTIONLINE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#employeeProductionunit-toolbar',
       iconCls:'fa fa-trash',
       url:'employeeProductionUnitRecord/deleteEmployeeProductionUnitRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'employeeProductionunit',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
</body>
</html>