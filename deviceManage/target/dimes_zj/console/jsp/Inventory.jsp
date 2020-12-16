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
	 var Inventory = $departmentDg.iDatagrid("getSelected");
	 if(!Inventory){
		 alert("请选择物料!");
		 return false;
	 }
	 var $process = $("#position");
	 var process = $process.iDatagrid("getSelected");
	 if(!process){
		 alert("请选择工序!");
		 return false;
	 }
	 $.get('inventory/updateShiftDownProcessRoute.do',{
		 InventoryCode:Inventory.code,
		 processesId:process.id
	 },function(result){
		 $process.iDatagrid('reload'); 
	 });
 }
 
 //工序上移
 function moveUp(){
	 var $departmentDg = $("#departmentDg");
	 var Inventory = $departmentDg.iDatagrid("getSelected");
	 if(!Inventory){
		 alert("请选择物料!");
		 return false;
	 }
	 var $process = $("#position");
	 var process = $process.iDatagrid("getSelected");
	 if(!process){
		 alert("请选择工序!");
		 return false;
	 }
	 $.get('inventory/updateShiftUpProcessRoute.do',{
		 InventoryCode:Inventory.code,
		 processesId:process.id
	 },function(result){
		 $process.iDatagrid('reload'); 
		 _processId = process.code;
	 });
 }
 //搜索
 function reloadInventory(){
     var InvenClass = $("#departmentTg").iTreegrid('getSelected');
     var InvenClassCode = "";
     if(InvenClass){
    	 InvenClassCode = InvenClass.code;
		}
     $('#departmentDg').iDatagrid("load",{//cInvCode,cInvName,cInvStd,cEngineerFigNo
    	 //code:InvenClassCode,
    	 cInvCode:$("#cInvCode").val(),
    	 cInvStd:$("#cInvStd").val(),
    	 cEngineerFigNo:$("#cEngineerFigNo").val(),
    	 cInvName:$("#cInvName").val()
     });
 }
 //工艺路线确定
 function confirmCraftsRoute(){
	 var id = $('#craftsRouteTable').iDatagrid('getSelected').id;
	 $.iMessager.confirm('确认','替换工艺路线将删除现有的所有工序，您确认要替换吗？',function(r){
		 if(r){
			 if(!id){
					alert('请选择要添加的工艺路线!');
				}else{
					$.get('inventory/addCraftsRoute4Inventory.do',{
						InventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
						craftsRouteId:id
					},function(data){
							numberList=[];
						if(data.success){
							$('#position').iDatagrid('reload');
							$('#craftsRouteAddDialog').iDialog('close');
						}else{
							alert(data.msg);
							$('#craftsRouteAddDialog').iDialog('close');
						}
					});
				}
		 }
	 })
 }
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">

		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table data-toggle="topjui-treegrid"
				data-options="id:'departmentTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'inventoryClass/queryInventoryClassesTree.do',
			   childGrid:{
			   	   param:'code:code',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   },
				onSelect:function(index, row){
					var InvenClass = $('#departmentTg').iTreegrid('getSelected');
					var InvenClassCode = '';
					if(InvenClass){
					InvenClassCode = InvenClass.code;
					}
					$('#departmentDg').iDatagrid('load',{
					code:InvenClassCode
					});
				}">
				<thead>
					<tr>
						<th data-options="field:'name',width:'100%',title:'物料类别'"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'inventory/queryInventoryByTyClassCode.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
							param:'InventoryCode:code',
                       	type:'treegird',
                       	id:'departmentTg',
                       }, childTab: [{id:'southTabs'}],
                       onSelect:function(index,row){
					           		switchButton('workpieceSwitchBtn',row.disabled);
					           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{workpieceId:''});
					           		$('#relateDoc').iDatagrid('reload',{workpieceId:'',});
					           		$('#statusTroubleCode').iDatagrid('reload',{workpieceId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'code',title:'物料代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'name',title:'物料名称',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'graphNumber',title:'工程图号',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'measurementUnit',title:'计量单位',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'iSafeNum',title:'安全库存',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'iTopSum',title:'最高库存',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'iLowSum',title:'最低库存',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'bPurchase',title:'是否外购',width:'150px',align:'center',sortable:false,
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
								<th
									data-options="field:'bSelf',title:'是否自制',width:'150px',align:'center',sortable:false,
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
								<th
									data-options="field:'bSale',title:'是否销售',width:'150px',align:'center',sortable:false,
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
                         param:'InventoryCode:code,relatedId:code'
                     }">
						<div title="工艺路线" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
                               idField:'id',
						       url:'processes/queryProcessessByInventoryCode.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:200,align:'center'"></th>
										<th
											data-options="field:'code',title:'工序代码',width:200,align:'center',
                        formatter:function(value,row,index){
                        	if(row.process){
                        		return row.process.code;
                        	}else{
                        		return ;
                        	}
                        }"></th>
										<th
											data-options="field:'name',title:'工序名称',width:200,align:'center',
                        formatter:function(value,row,index){
                        	if(row.process){
                        		return row.process.name;
                        	}else{
                        		return ;
                        	}
                        }"></th>
										<th
											data-options="field:'note',title:'备注',width:200,align:'center',
                        formatter:function(value,row,index){
                        	if(row.process){
                        		return row.process.note;
                        	}else{
                        		return ;
                        	}
                        }"></th>
									</tr>
								</thead>
							</table>
						</div>
						<%--<div title="BOM设定" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								   data-options="id:'BOM',
                               initCreate: false,
                               fitColumns:true,
						       data: [
								{id:'1', processCode:'6020611201',processName:'外星轮',parameterCode:'',parameterName:'9020611201',unit:'1',upLine:'1',lowLine:'0',standardValue:'',note:''},
								{id:'2', processCode:'6020611202',processName:'内星轮',parameterCode:'',parameterName:'9020611202',unit:'1',upLine:'1',lowLine:'0',standardValue:'',note:''},
								{id:'3', processCode:'6020611203',processName:'连接轴',parameterCode:'',parameterName:'9020611203',unit:'1',upLine:'1',lowLine:'0',standardValue:'',note:''},
								{id:'4', processCode:'6500161301',processName:'挡圈',parameterCode:'',parameterName:'9500161301',unit:'1',upLine:'1',lowLine:'0',standardValue:'',note:''},
							]">
								<thead>
								<tr>
									<th data-options=	"field:'id',title:'id',checkbox:false,hidden:true,width:50"></th>
									<th data-options="field:'processCode',title:'序号',width:50 "></th>
									<th data-options="field:'processName',title:'子件代码',width:50 "></th>
									<th data-options="field:'parameterTypeName',title:'子件名称',width:50 "></th>
									<th data-options="field:'parameterCode',title:'规格型号',width:50"></th>
									<th data-options="field:'parameterName',title:'图号',width:50"></th>
									<th data-options="field:'unit',title:'母件批量',width:50"></th>
									<th data-options="field:'upLine',title:'批量用量',width:50,sortable:false"></th>
									<th data-options="field:'lowLine',title:'损耗率%',width:50,sortable:false"></th>
									<th data-options="field:'standardValue',title:'备注',width:50,sortable:false"></th>
									<th data-options="field:'note',title:'是否明细',width:50,sortable:false"></th>
								</tr>
								</thead>
							</table>
						</div>--%>
						<div title="工序参数" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'statusTroubleCode',
                               initCreate: false,
                               fitColumns:true,
						       url:'inventory/queryWorkpieceProcessParameterMappingsByWorkpieceCode.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:50"></th>
										<%--<th
											data-options="field:'processTypeName',title:'工序类别',width:50,formatter:function(value,row,index){
											if(row.workpieceProcess.process.processType){
												return row.workpieceProcess.process.processType.name;
											}else{
												return '';
											}
										}"></th>--%>
										<th
											data-options="field:'processCode',title:'工序代码',width:50,formatter:function(value,row,index){
											if(row.workpieceProcess.process){
												return row.workpieceProcess.process.code;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'processName',title:'工序名称',width:50,formatter:function(value,row,index){
											if(row.workpieceProcess.process){
												return row.workpieceProcess.process.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterTypeName',title:'参数类别',width:50,formatter:function(value,row,index){
											if(row.parameter){
                                                return row.parameter.parameterType.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterCode',title:'参数代码',width:50,formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.code;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterName',title:'参数名称',width:50,formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'unit',title:'单位',width:50"></th>
										<th data-options="field:'upLine',title:'控制线UL',width:50,sortable:false"></th>
										<th data-options="field:'lowLine',title:'控制线LL',width:50,sortable:false"></th>
										<th data-options="field:'standardValue',title:'标准值',width:50,sortable:false"></th>
										<th data-options="field:'note',title:'备注',width:50,sortable:false"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="文档资料" data-options="id:'tab3',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'relatedDoc/queryDocsByRelatedIdAndType.do?moduleCode=workpiece'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:80"></th>
										<th
											data-options="field:'contentType',title:'文件类型',width:120,align:'center'"></th>
										<th
											data-options="field:'name',title:'文件名称',width:120,align:'center'"></th>
										<th
											data-options="field:'srcName',title:'源文件名称',width:120,align:'center'"></th>
										<th
											data-options="field:'fileSize',title:'大小(KB)',width:120,align:'center',formatter:function(value,row,index){
												if(value){
													return (value/1024).toFixed(2);
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'url',title:'存储路径',width:120,align:'center'"></th>
										<th
											data-options="field:'uploadDate',title:'上传时间',width:120,align:'center',formatter:function(value,row,index){
											if(value){
												return getDateTime(new Date(value));
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'note',title:'说明',width:120,align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="装备信息" data-options="id:'tab4',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								   data-options="id:'equipmentMapping',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'inventoryEquipmentTypeMapping/queryPageByInventoryEquipmentTypeMappingByInventoryCode.do'">
								<thead>
								<tr>
									<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:80"></th>
									<th
											data-options="field:'equipmentCode',title:'装备编码',width:120,align:'center',formatter:function(value,row,index){
											if(row.equipmentType){
													return row.equipmentType.code;
											}else{
												return '';
											}
										}"></th>
									<th
											data-options="field:'equipmentName',title:'装备名称',width:120,align:'center',formatter:function(value,row,index){
											if(row.equipmentType){
													return row.equipmentType.name;
											}else{
												return '';
											}
										}"></th>
									<th
											data-options="field:'equipmentUnitType',title:'规格型号',width:120,align:'center',formatter:function(value,row,index){
											if(row.equipmentType){
													return row.equipmentType.unitType;
											}else{
												return '';
											}
										}"></th>
									<th
											data-options="field:'useFrequency',title:'使用频次',width:120,align:'center'"></th>
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
	<sec:authorize access="hasAuthority('QUERY_WORKPIECE')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadInventory()">查询</a>
	</sec:authorize>
	<sec:authorize access="hasAuthority('ADD_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg'
       },
       dialog:{
           id:'classesAddDialog',
           width:600,
           height:700,
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入物料代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入物料名称');
           				return false;
           			}
           			$.get('workpiece/addWorkpiece.do',{
           			code:code,
           			name:name,
           			unitType:$('#unitType').val(),
           			graphNumber:$('#graphNumber').val(),
           			customerGraphNumber:$('#customerGraphNumber').val(),
           			version:$('#version').val(),
           			measurementUnit:$('#measurementUnit').val(),
           			'workpieceType.id':$('#departmentTg').iTreegrid('getSelected').id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#classesAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
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
		<sec:authorize access="hasAuthority('EDIT_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 700,
                url:'workpiece/queryWorkpieceById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请工件名称');
           				return false;
           			}
           			$.get('workpiece/updateWorkpiece.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			unitType:$('#unitType').val(),
           			graphNumber:$('#graphNumber').val(),
           			customerGraphNumber:$('#customerGraphNumber').val(),
           			version:$('#version').val(),
           			measurementUnit:$('#measurementUnit').val(),
           			'workpieceType.id':$('#departmentTg').iTreegrid('getSelected').id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#classEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
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
		<sec:authorize access="hasAuthority('DEL_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
		</sec:authorize>
		<sec:authorize access="hasAuthority('DISABLE_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       grid: {uncheckedMsg:'请选择操作的数据',id:'departmentDg',param:'id:id'}" id="workpieceSwitchBtn">停用</a>
		</sec:authorize>
       <div style="margin-top: 10px;margin-left: 10px;">
		物料代码:<input id="cInvCode"
					name="cInvCode" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		物料名称:<input id="cInvName"
					name="cInvName" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
       	规格型号:<input id="cInvStd"
					name="cInvStd" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
       	图号:<input id="cEngineerFigNo"
					name="cEngineerFigNo" style="width:200px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		   </div>
	</div>

	<!-- 职位表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
	<sec:authorize access="hasAuthority('SELECT_PROCESSROUTE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       dialog:{
           id:'craftsRouteAddDialog',
            width:620,
           height:500,
           href:'console/jsp/Inventory_craftsRoute_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var id = $('#craftsRouteTable').iDatagrid('getSelected').id;
           		if(!id){
           			alert('请选择要添加的工艺路线!');
           		}else{
           			$.get('inventory/addCraftsRoute4Inventory.do',{
           				InventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
           				craftsRouteId:id
           			},function(data){
           					numberList=[];
           				if(data.success){
           					$('#position').iDatagrid('reload');
           					$('#craftsRouteAddDialog').iDialog('close');
           				}else{
           					alert(data.msg);
           					$('#craftsRouteAddDialog').iDialog('close');
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#craftsRouteAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">选择</a>
	</sec:authorize>
	<sec:authorize access="hasAuthority('ADDPROCESS_WORKPIECE')">
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
           href:'console/jsp/Inventory_processes_add.jsp',
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
           			$.get('inventory/addProcesses4Inventory.do',{
           				InventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
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
	<sec:authorize access="hasAuthority('DELPROCESS_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'inventory/deleteProcessesFromInventory.do?InventoryCode={parent.code}',
       parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'processesId:id'}">删除</a>
	</sec:authorize>
	<sec:authorize access="hasAuthority('UPMOVE_WORKPIECE')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-arrow-up'"
       data-toggle="topjui-menubutton" onclick='moveUp()'>上移</a>
	</sec:authorize>
	<sec:authorize access="hasAuthority('DOWNMOVE_WORKPIECE')">
       <a href="javascript:void(0)"  data-options="iconCls:'fa fa-arrow-down'"
       data-toggle="topjui-menubutton" onclick='moveDown()'>下移</a>
	</sec:authorize>
	</div>
	<!-- 职位表格工具栏结束 -->
	<!-- 相关文档表格工具栏开始 -->
	<div id="relateDoc-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">

		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
      	 extend: '#relateDoc-toolbar',
         parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       iconCls: 'fa fa-plus',
              dialog:{
           id:'parameterAddDialog',
            width:900,
           height:500,
           href:'console/jsp/workpiece_processDeviceSite_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#processesDeviceSiteTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的数据!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('workpiece/addProcessDeviceSite4Workpiece.do',{
           				workpieceId:$('#departmentDg').iDatagrid('getSelected').id,
           				processDeviceSiteIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#relateDoc').iDatagrid('reload');
           					$('#processesDeviceSiteTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>

		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
      	 extend: '#relateDoc-toolbar',
         parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       iconCls: 'fa fa-pencil',
              dialog:{
           id:'parameterUpdateDialog',
            width:600,
           height:300,
           href:'console/jsp/workpiece_processDeviceSite_edit.jsp',
           url:'workpiece/queryWorkpieceProcessDeviceSiteMappingById.do?id={id}',
           buttons:[
           	{text:'修改',handler:function(){
           		
           	
           			$.get('workpiece/updateWorkpieceProcessDeviceSiteMapping.do',{
           				id:$('#relateDoc').iDatagrid('getSelected').id,
           				processingBeat:$('#processingBeat').val()
           			},function(data){
           				if(data.success){
           					$('#relateDoc').iDatagrid('reload');
           					$('#parameterUpdateDialog').iDialog('close');
           				}else{
           					alert(data.msg);
           				}
           			}); 
           	},iconCls:'fa fa-pencil',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterUpdateDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
       }">修改加工节拍</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#relateDoc-toolbar',
       iconCls:'fa fa-trash',
       url:'workpiece/deleteProcessDeviceSiteFromWorkpiece.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'relateDoc',param:'id:id'}">删除</a>
	</div>


	<div id="BOM-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'BOM'
       }">

		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'openDialog',
       extend: '#statusTroubleCode-toolbar',
       iconCls: 'fa fa-plus',
         parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
           dialog:{
           id:'parameterAddDialog',
            width:900,
           height:500,
           href:'console/jsp/workpiece_processParameter_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#processesDeviceSiteTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的数据!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('inventory/addProcessParameter4Workpiece.do',{
           				inventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
           				processDeviceSiteIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#statusTroubleCode').iDatagrid('reload');
           					$('#processesDeviceSiteTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>


		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method: 'openDialog',
            extend: '#statusTroubleCode-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/workpiece_processParameter_edit.jsp',
                url:'inventory/queryWorkpieceProcessParameterMappingById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('inventory/updateWorkpieceProcessParameterMapping.do',{
           				id:$('#statusTroubleCode').iDatagrid('getSelected').id,
           				unit:$('#unit').val(),
           				upLine:$('#upLine').val(),
           				lowLine:$('#lowLine').val(),
           				standardValue:$('#standardValue').val(),
           				note:$('#note').val()
           			},function(data){
           			if(data.success){
           					$('#statusTroubleCode').iDatagrid('reload');
           					$('#classEditDialog').iDialog('close');
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


		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'doAjax',
       extend: '#statusTroubleCode-toolbar',
       iconCls:'fa fa-trash',
       url:'inventory/deleteProcessParameterFromWorkpiece.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'statusTroubleCode',param:'id:id'}">删除</a>

	</div>


	<!-- 状态故障代码工具栏 -->
	<div id="statusTroubleCode-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'statusTroubleCode'
       }">
<sec:authorize access="hasAuthority('ADDPARAMETER_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#statusTroubleCode-toolbar',
       iconCls: 'fa fa-plus',
         parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
           dialog:{
           id:'parameterAddDialog',
            width:900,
           height:500,
           href:'console/jsp/workpiece_processParameter_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#processesDeviceSiteTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的数据!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('inventory/addProcessParameter4Workpiece.do',{
           				inventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
           				processDeviceSiteIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#statusTroubleCode').iDatagrid('reload');
           					$('#processesDeviceSiteTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDITPARAMETER_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#statusTroubleCode-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/workpiece_processParameter_edit.jsp',
                url:'inventory/queryWorkpieceProcessParameterMappingById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('inventory/updateWorkpieceProcessParameterMapping.do',{
           				id:$('#statusTroubleCode').iDatagrid('getSelected').id,
           				unit:$('#unit').val(),
           				upLine:$('#upLine').val(),
           				lowLine:$('#lowLine').val(),
           				standardValue:$('#standardValue').val(),
           				note:$('#note').val()
           			},function(data){
           			if(data.success){
           					$('#statusTroubleCode').iDatagrid('reload');
           					$('#classEditDialog').iDialog('close');
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
<sec:authorize access="hasAuthority('DELPARAMETER_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#statusTroubleCode-toolbar',
       iconCls:'fa fa-trash',
       url:'inventory/deleteProcessParameterFromWorkpiece.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'statusTroubleCode',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 文档资料表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
<sec:authorize access="hasAuthority('UPLOADDOC_WORKPIECE')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#documentData-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'departmentDg',
           param:'relatedId:id'
       },
       dialog:{
           id:'uploadDocDialog',
           title:'上传',
           width: 600,
           height: 400,
           href:'console/jsp/workpiece_doc_upload.jsp',
           buttons:[
               {text:'上传',handler:function(){
               var file = $('#file').val();
               if(!file){
               	$.iMessager.alert('提示','请选择要上传的文件!');
					  return false;
               }
               
               var docType = $('#docType').iCombobox('getText');
				if(!docType){
					  $.iMessager.alert('提示','请选择文档类别!');
					  return false;
				}
               	 $.ajaxFileUpload({
                url: 'relatedDoc/upload.do', 
                type: 'post',
                data: {
                		'relatedDocumentType.id':$('#docType').iCombobox('getValue'),
                		relatedId:$('#departmentDg').iDatagrid('getSelected').code,
                		name:$('#name').iTextbox('getValue'), 
                		note:$('#note').iTextbox('getValue')
                	  },
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'file', //文件上传域的ID
                dataType: 'text', //返回值类型 一般设置为json
                success: function (data, status) {
                	if(data.statusCode==300){
                		 $.iMessager.alert('提示',data.message);
                		 return false;
                	}
                	 $('#uploadDocDialog').iDialog('close');
					 $('#documentData').iDatagrid('reload');
                },
                error:function(error){
                	alert(error);
                }
            }
        );
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#uploadDocDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">上传</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELDOC_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#documentData-toolbar',
       iconCls:'fa fa-trash',
       url:'relatedDoc/deleteRelatedDocument.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'documentData',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DOWNLOADDOC_WORKPIECE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
	</div>
	<!-- 文档资料表格工具栏结束 -->


	<!-- 装备表格工具栏开始 -->
	<div id="equipmentMapping-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'equipmentMapping'
       }">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'openDialog',
       extend: '#equipmentMapping-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       dialog:{
           id:'equipmentMappingAddDialog',
            width:750,
           height:500,
           href:'console/jsp/inventory_equipment_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var id = $('#equipmentTable').iDatagrid('getSelected').id;
           		if(!id){
           			alert('请选择要关联的装备!');
           		}else{
           			$.get('inventoryEquipmentTypeMapping/addInventoryEquipmentTypeMapping.do',{
           				inventoryCode:$('#departmentDg').iDatagrid('getSelected').code,
           				equipmentTypeId:id
           			},function(data){
           					numberList=[];
           				if(data.success){
           					$('#equipmentMapping').iDatagrid('reload');
           					$('#equipmentMappingAddDialog').iDialog('close');
           				}else{

           					$('#equipmentMappingAddDialog').iDialog('close');
           					$.iMessager.alert('提示',data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#equipmentMappingAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>


		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'openDialog',
       extend: '#equipmentMapping-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'equipmentMappingEditDialog',
            width:500,
           height:300,
           href:'console/jsp/inventory_equipment_edit.jsp',
           url:'inventoryEquipmentTypeMapping/queryEquipmentMappingById.do?id={id}',
           buttons:[
           	{text:'保存',handler:function(){
           		var useFrequency = $('#useFrequency').val();
           		if(useFrequency==null||useFrequency==''){
           			$.iMessager.alert('提示','请输入使用频次');
           		}else{
					$.get('inventoryEquipmentTypeMapping/updateInventoryEquipmentTypeMapping.do',{
           				id:$('#equipmentMapping').iDatagrid('getSelected').id,
           				useFrequency:useFrequency
           			},function(data){
           				if(data.success){
           					$('#equipmentMapping').iDatagrid('reload');
           					$('#equipmentMappingEditDialog').iDialog('close');
           				}else{
           					$('#equipmentMappingEditDialog').iDialog('close');
           					$.iMessager.alert('提示',data.msg);

           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#equipmentMappingEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">编辑</a>


		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'doAjax',
       extend: '#equipmentMapping-toolbar',
       iconCls:'fa fa-trash',
       url:'inventoryEquipmentTypeMapping/deleteInventoryEquipmentTypeMapping.do',
       parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'equipmentMapping',param:'id:id'}">删除</a>
	</div>
	<!-- 装备表格工具栏结束 -->
</body>
</html>