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
                       url:'workpieceQrCodeRule/queryWorkpieceQrCodeRules.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                        <th data-options="field:'tm1',title:'TM1',width:'200px',align:'center'"></th>
                        <th data-options="field:'tm2',title:'TM2',width:'100px',align:'center'"></th>
                        <th data-options="field:'printerIp',title:'打印机IP',sortable:false,align:'center',width:'180px'"></th>
                        <th data-options="field:'remoteUser',title:'登录用户',sortable:false,align:'center',width:'180px'"></th>
                        <th data-options="field:'sharedDir',title:'共享目录',sortable:false,align:'center',width:'180px'"></th>
                        <th data-options="field:'workpieceCode',title:'工件代码',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(row.workpiece){
                        		return row.workpiece.code;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'workpieceName',title:'工件名称',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(row.workpiece){
                        		return row.workpiece.name;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'customerGraph',title:'客户图号',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(row.workpiece){
                        		return row.workpiece.customerGraphNumber;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'version',title:'版本号',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(row.workpiece){
                        		return row.workpiece.version;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'manufacturerCode',title:'厂商代码',sortable:false,align:'center',width:'100px'"></th>
                        <th data-options="field:'generateDate',title:'日期',sortable:false,align:'center',width:'100px'"></th>
                        <th data-options="field:'serNum',title:'流水号',sortable:false,align:'center',width:'100px'"></th>
                        <th data-options="field:'createDate',title:'创建日期',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(value){
                        		var date = new Date(value);
                        		return date.getFullYear() +'-' + (date.getMonth()+1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'sendDate',title:'发送日期',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
                        	if(value){
                        		var date = new Date(value);
                        		return date.getFullYear() +'-' + (date.getMonth()+1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'sended',title:'状态',sortable:false,align:'center',width:'100px',formatter:function(value,row,index){
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
           href:'console/jsp/workpieceQRCodeRule_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var workpieceId = $('#workpieceId').val();
           			if(workpieceId==null || ''===$.trim(workpieceId)){
           				$.iMessager.alert('提示','请选择工件代码!');
           				return false;
           			}
           			var manufacturerCode = $('#manufacturerCode').val();
           			if(manufacturerCode==null || ''===$.trim(manufacturerCode)){
           				$.iMessager.alert('提示','请输入厂商代码!');
           				$('#manufacturerCode').focus();
           				return false;
           			}
           			var generateDate = $('#generateDate').val();
           			if(generateDate==null || ''===$.trim(generateDate)){
           				$.iMessager.alert('提示','请输入日期!');
           				$('#generateDate').focus();
           				return false;
           			}
           			var serNum = $('#serNum').val();
           			if(serNum==null || ''===$.trim(serNum)){
           				$.iMessager.alert('提示','请输入流水号!');
           				$('#serNum').focus();
           				return false;
           			}
           			var printerIp = $('#printerIp').val();
           			if(printerIp==null || ''===$.trim(printerIp)){
           				$.iMessager.alert('提示','请输入打印机IP!');
           				$('#printerIp').focus();
           				return false;
           			}
           			
           			 var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
				    var reg = printerIp.match(exp);
				    if(reg==null)
				    {
					    $.iMessager.alert('提示','打印机IP地址不合法!');
					    $('#printerIp').iTextbox('clear');
					    $('#printerIp').focus();
					    return false;
				    }
				    var sharedDir = $('#sharedDir').val();
				    if(!sharedDir){
				    	$.iMessager.alert('提示','请输入共享目录名称!');
				    	return false;
				    }
           			$.get('workpieceQrCodeRule/addWorkpieceQrCodeRule.do',{
           			'workpiece.id':workpieceId,
           			'workpiece.code':$('#workpieceId').iTextbox('getText'),
           			'workpiece.name':$('#workpieceName').iTextbox('getValue'),
           			'workpiece.customerGraphNumber':$('#customerGraphNumber').iTextbox('getValue'),
           			'workpiece.version':$('#version').iTextbox('getValue'),
           			'workpiece.id':workpieceId,
           			manufacturerCode:manufacturerCode,
           			generateDate:generateDate,
           			serNum:serNum,
           			remoteUser:$('#remoteUser').val(),
           			remotePass:$('#remotePass').val(),
           			sharedDir:sharedDir,
           			printerIp:printerIp
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
                height:600,
                href: 'console/jsp/workpieceQRCodeRule_edit.jsp',
                url:'workpieceQrCodeRule/queryWorkpieceQrCodeRuleById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var workpieceId = $('#workpieceId').val();
           			if(workpieceId==null || ''===$.trim(workpieceId)){
           				$.iMessager.alert('提示','请选择工件代码!');
           				return false;
           			}
           			var manufacturerCode = $('#manufacturerCode').val();
           			if(manufacturerCode==null || ''===$.trim(manufacturerCode)){
           				$.iMessager.alert('提示','请输入厂商代码!');
           				$('#manufacturerCode').focus();
           				return false;
           			}
           			var generateDate = $('#generateDate').val();
           			if(generateDate==null || ''===$.trim(generateDate)){
           				$.iMessager.alert('提示','请输入日期!');
           				$('#generateDate').focus();
           				return false;
           			}
           			var serNum = $('#serNum').val();
           			if(serNum==null || ''===$.trim(serNum)){
           				$.iMessager.alert('提示','请输入流水号!');
           				$('#serNum').focus();
           				return false;
           			}
           			var printerIp = $('#printerIp').val();
           			if(printerIp==null || ''===$.trim(printerIp)){
           				$.iMessager.alert('提示','请输入打印机IP!');
           				$('#printerIp').focus();
           				return false;
           			}
           			var sharedDir = $('#sharedDir').val();
				    if(!sharedDir){
				    	$.iMessager.alert('提示','请输入共享目录名称!');
				    	return false;
				    }
           			$.get('workpieceQrCodeRule/updateWorkpieceQrCodeRule.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			'workpiece.id':workpieceId,
           			'workpiece.code':$('#workpieceCode').iTextbox('getText'),
           			'workpiece.name':$('#workpieceName').iTextbox('getValue'),
           			'workpiece.customerGraphNumber':$('#customerGraphNumber').iTextbox('getValue'),
           			'workpiece.version':$('#version').iTextbox('getValue'),
           			'workpiece.id':workpieceId,
           			manufacturerCode:manufacturerCode,
           			generateDate:generateDate,
           			serNum:serNum,
           			remoteUser:$('#remoteUser').val(),
           			remotePass:$('#remotePass').val(),
           			sharedDir:sharedDir,
           			printerIp:printerIp
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
       url:'workpieceQrCodeRule/send2printer.do',
       grid: {uncheckedMsg:'请先勾选发送到打印机的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentDg').iDatagrid('reload');}">发送到打印机</a>
       </sec:authorize>
<%--             <sec:authorize access="hasAuthority('DEL_CLASS')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'workpieceQrCodeRule/deleteWorkpieceQrCodeRule.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
       </sec:authorize> --%>
    </div>
<!-- 部门表格工具栏结束 -->
</body>
</html>