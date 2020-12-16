<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
 function downloadFile(){
	 var dg = $("#documentData").iDatagrid('getSelected');
	 if(!dg){
		 $.messager.alert("提示","请选择要下载的文件!");
		 return ;
	 }
	 var id = dg.id;
	 window.location.href="relatedDoc/download.do?id=" + id;
 }
 //工序下移
 function moveDown(){
	 var $departmentDg = $("#departmentDg");
	 var craftsRoute = $departmentDg.iDatagrid("getSelected");
	 if(!craftsRoute){
		 alert("请选择工艺路线!");
		 return false;
	 }
	 var $process = $("#position");
	 var process = $process.iDatagrid("getSelected");
	 if(!process){
		 alert("请选择工序!");
		 return false;
	 }
	 $.get('craftsRoute/updateShiftDownProcessRoute.do',{
		 craftsId:craftsRoute.id,
		 processesId:process.id,
		 c_pMappingId:process.pid
	 },function(result){
		 $process.iDatagrid('reload'); 
	 });
 }
 
 //工序上移
 function moveUp(){
	 var $departmentDg = $("#departmentDg");
	 var craftsRoute = $departmentDg.iDatagrid("getSelected");
	 if(!craftsRoute){
		 alert("请选择工艺路线!");
		 return false;
	 }
	 var $process = $("#position");
	 var process = $process.iDatagrid("getSelected");
	 if(!process){
		 alert("请选择工序!");
		 return false;
	 }
	 $.get('craftsRoute/updateShiftUpProcessRoute.do',{
		 craftsId:craftsRoute.id,
		 processesId:process.id,
		 c_pMappingId:process.pid
	 },function(result){
		 $process.iDatagrid('reload'); 
		 _processId = process.id;
	 });
 }
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">

		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'craftsRoute/queryCraftsRoute.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       childTab: [{id:'southTabs'}],
                       onSelect:function(index,row){
					           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{workpieceId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'12px',hidden:true"></th>
								<th
									data-options="field:'pid',title:'pid',checkbox:false,width:'12px',hidden:true"></th>
								<th
									data-options="field:'code',title:'工艺路线代码',width:'200px',align:'center'"></th>
								<th
									data-options="field:'name',title:'工艺路线名称',width:'200px',align:'center',sortable:false"></th>
								<th
									data-options="field:'version',title:'版本号',width:'200px',align:'center',sortable:false"></th>
								<th
									data-options="field:'note',title:'备注',width:'200px',align:'center',sortable:false"></th>
								<th
									data-options="field:'disabled',width:'200px',title:'停用',sortable:false,
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
					style="height: 40%">
					<div data-toggle="topjui-tabs"
						data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'craftsId:id,relatedId:id'
                     }">
						<div title="工序" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
                               idField:'pid',
						       url:'processes/queryProcessessByCraftsId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'pid',title:'pid',checkbox:false,hidden:true,width:200,align:'center'"></th>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:200,align:'center'"></th>
										<th
											data-options="field:'code',title:'工序代码',width:200,align:'center'"></th>
										<th
											data-options="field:'name',title:'工序名称',width:200,align:'center'"></th>
										<th
											data-options="field:'note',title:'备注',width:200,align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 工艺路线表格工具栏开始 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('ADD_PROCESSROUTE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:400,
           href:'console/jsp/craftsRoute_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入工艺路线代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入工艺路线名称');
           				return false;
           			}
           			$.get('craftsRoute/addCraftsRoute.do',{
           			code:code,
           			name:name,
           			version:$('#version').val(),
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
<sec:authorize access="hasAuthority('EDIT_PROCESSROUTE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/craftsRoute_edit.jsp',
                url:'craftsRoute/queryCraftsRouteById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入工艺路线名称');
           				return false;
           			}
           			$.get('craftsRoute/updateCraftsRoute.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			version:$('#version').val(),
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
<sec:authorize access="hasAuthority('DEL_PROCESSROUTE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'craftsRoute/deleteCraftsRoute.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_PROCESSROUTE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'craftsRoute/disabledCraftsRoute.do',
       grid: {uncheckedMsg:'请选择操作的数据',id:'departmentDg',param:'id:id'}">停用</a>
</sec:authorize>
	</div>
	<!-- 工序工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
<sec:authorize access="hasAuthority('ADDPROCESSROUTE_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       dialog:{
           id:'deviceAddDialog',
            width:620,
           height:500,
           href:'console/jsp/craftsRoute_processes_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#processesTable').iDatagrid('getSelections');
           		var idsArray = numberList;
           		if(ids.length<=0){
           			alert('请选择要添加的工序!');
           		}else{
           			if(idsArray.length<ids.length){
							for(var i = 0; i <ids.length; i++){
								var exist = false;
								for(var j = 0; j <idsArray.length; j++){
									if(ids[i].id==idsArray[j]){
										exist=true;
									}		
								}
								if(!exist){
									idsArray.push(ids[i].id);	
								}
							}	
           			}
           			$.get('craftsRoute/addProcesses4CraftsRoute.do',{
           				craftsId:$('#departmentDg').iDatagrid('getSelected').id,
           				processesId:JSON.stringify(idsArray)
           			},function(data){
           					numberList=[];
           				if(data.success){
           					$('#position').iDatagrid('reload');
           					$('#deviceAddDialog').iDialog('close');
           				}else{
           					alert(data.msg);
           					$('#processesTable').iDatagrid('reload');
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELPROCESSROUTE_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'craftsRoute/deleteProcessesFromCraftsRoute.do?craftsId={parent.id}',
       parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'processesId:pid'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('UPMOVE_PROCESSROUTEWORKPIECE')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-arrow-up'"
       data-toggle="topjui-menubutton" onclick='moveUp()'>上移</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DOWNMOVE_PROCESSROUTEWORKPIECE')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-arrow-down'"
       data-toggle="topjui-menubutton" onclick='moveDown()'>下移</a>
</sec:authorize>
	</div>
	<!-- 职位表格工具栏结束 -->
</body>
</html>