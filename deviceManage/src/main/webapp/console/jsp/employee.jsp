<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
 	function openWindow(){
		//获取所有被选中的员工
		var employeesArray = $("#employeeDg").iDatagrid("getChecked");
		if(employeesArray.length>0){
			var ids = "";
			for(var i = 0;i<employeesArray.length;i++){
				var employee = employeesArray[i];
				ids += employee.code +",";
			}
			
			ids = ids.substring(0,ids.length-1);
			$("#ids").val(ids);
 			var newWin = window.open("console/jsp/employee_print.jsp"); 
		}else{
			alert("请选择要打印二维码的记录!");
			return false;
		}
	} 
 	
 	function downloadFile(){
 		 var dg = $("#documentData").iDatagrid('getSelected');
 		 if(!dg){
 			 $.messager.alert("提示","请选择要下载的文件!");
 			 return ;
 		 }
 		 var id = dg.id;
 		 window.location.href="relatedDoc/download.do?id=" + id;
 	 }
</script>
</head>
<body>
	<input type="hidden" id="ids" />
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<table data-toggle="topjui-treegrid"
				data-options="id:'departmentTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'department/queryDepartmentsTree.do',
			   childGrid:{
			   	   param:'departmentCode:code',
                   grid:[
                       {type:'datagrid',id:'employeeDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'机构名称'"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'employeeDg',
                       url:'employee/queryEmployeesByDepartmentCodeOnZJ.do',
                       fitColumns:true,
                       pagination:true,
                       singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg'
                       },
			           childTab: [{id:'southTabs'}],
			            onSelect:function(index,row){
					           		switchButton('employeeSwitchBtn',row.disabled);
					           },onLoadSuccess:function(){
					           		$('#skill').iDatagrid('reload',{employeeId:''});
					           }">
						<thead>
							<tr>
								<th data-options="field:'id',title:'id',checkbox:true,width:'30px'"></th>
								<th data-options="field:'code',title:'员工代码',width:'120px',align:'center'"></th>
								<th data-options="field:'name',title:'员工名称',width:'130px',align:'center'"></th>
								<th data-options="field:'cDept_Num',title:'部门代码',width:'130px',align:'center'"></th>
								<th data-options="field:'cDepName',title:'部门名称',width:'130px',align:'center'"></th>
								<th data-options="field:'tel',title:'联系方式',width:'130px',align:'center'"></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 员工表格工具栏开始 -->
	<div id="employeeDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'employeeDg'
       }">
       <sec:authorize access="hasAuthority('PRINTCODE_EMPLOYEE')">
      <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls: 'fa fa-print',
            modal:true,
            parentGrid:{
		       	type:'treegrid',
		       	id:'departmentTg',
		       	param:'departmentId:id'
		      }" onClick="openWindow()">打印二维码</a>
		       </sec:authorize>
       <!-- 二维码按钮 -->
	</div>
	<!-- 员工表格工具栏结束 -->
</body>
</html>