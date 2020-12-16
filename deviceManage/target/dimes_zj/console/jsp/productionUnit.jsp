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
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table id="productionUnitTg" data-toggle="topjui-treegrid"
				data-options="id:'productionUnitTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryTopProductionUnits.do',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'productionUnitDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'生产单元'"></th>
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
						data-options="id:'productionUnitDg',
                       url:'productionUnit/queryProductionUnitsByParentId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'productionUnitTg',
                       },
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
			          	 switchButton('productionUnitSwitchBtn',row.disabled);
			           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{productionUnitId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'10px',hidden:true"></th>
								<th
									data-options="field:'code',title:'生产单元代码',width:'100px',align:'center'"></th>
								<th data-options="field:'name',title:'生产单元名称',width:'150px',align:'center'"></th>
								<th data-options="field:'goalOutput',title:'产量目标',width:'100px',align:'center'"></th>
								<th data-options="field:'goalOee',title:'OEE目标',width:'100px',align:'center'"></th>
								<th data-options="field:'goalLostTime',title:'损时目标(小时)',width:'100px',align:'center'"></th>
								<th data-options="field:'threshold',title:'损耗率阀值(%)',width:'100px',align:'center'"></th>
								<th data-options="field:'goalNg',title:'不合格品目标',width:'100px',align:'center'"></th>
								<th
									data-options="field:'parentCode',title:'上级单元代码',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if (row.parent) {
                                return row.parent.code;
                            } else {
                                return '';
                            }
                        }"></th>
								<th
									data-options="field:'parentName',title:'上级单元名称',width:'150px',align:'center', 
                        formatter:function(value,row,index){
                            if (row.parent) {
                                return row.parent.name;
                            } else {
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
								<th
									data-options="field:'disabled',title:'停用',width:'80px',align:'center',
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
                     singleSelect:true,
                     parentGrid:{
                         type:'datagrid',
                         id:'productionUnitDg',
                         param:'productionUnitId:id,relatedId:id'
                     }">
						<div title="设备信息" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'device/queryDevicesByProductionUnitId.do?module=dimes'">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
										<th data-options="field:'code',title:'设备代码',width:'180px',align:'center'"></th>
										<th data-options="field:'name',title:'设备名称',width:'180px',align:'center'"></th>
										<th data-options="field:'unitType',title:'规格型号',width:'180px',align:'center'"></th>
										<th data-options="field:'status',title:'设备状态',width:'180px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="人员信息" data-options="id:'tab1',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'relateDoc',
                               initCreate: false,
                               fitColumns:true,
						       url:'employee/queryEmployeeByProductionUnitId.do'">
								<thead>
									<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
								<th
									data-options="field:'code',title:'员工编码',width:'150px',align:'center'"></th>
								<th data-options="field:'name',title:'员工名称',width:'150px',align:'center'"></th>
								<th data-options="field:'cDept_Num',title:'部门代码',width:'150px',align:'center'"></th>
								<th data-options="field:'tel',title:'手机号',width:'150px',align:'center'"></th>
								<th data-options="field:'email',title:'邮箱地址',width:'150px',align:'center'"></th>
							</tr>
								</thead>
							</table>
						</div>
						<div title="文档资料" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'relatedDoc/queryDocsByRelatedIdAndType.do?moduleCode=production'">
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
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 生产单元表格工具栏开始 -->
	<div id="productionUnitDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'productionUnitDg'
       }">
		<sec:authorize access="hasAuthority('ADD_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            },
       dialog:{
       		beforeOpenCheckUrl: 'productionUnit/existDevices.do?module=dimes&id={id}',
           id:'productionUnitAddDialog',
           width:600,
           height:600,
           href:'console/jsp/productionUnit_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var productionUnitCode = $('#productionUnitCode').val();
           			if(productionUnitCode==null || ''===$.trim(productionUnitCode)){
           				$.iMessager.alert('提示','请输入生产单元代码');
           				return false;
           			}
           			
           			var productionUnitName = $('#productionUnitName').val();
           			if(productionUnitName==null || ''===$.trim(productionUnitName)){
           				$.iMessager.alert('提示','请输入生产单元名称');
           				return false;
           			}
           			var classTypeName = $('#classTypeName').iCombobox('getText');
           			if(!classTypeName){
           			    $.iMessager.alert('提示','请选择班次类别!');
           				return false;
           			 }
           			$.get('productionUnit/addProductionUnit.do',{
           			code:productionUnitCode,
           			name:productionUnitName,
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			goalOutput:$('#goalOutput').val(),
           			'classType.name':$('#classTypeName').iCombobox('getText'),
           			classTypeName:$('#classTypeName').iCombobox('getText'),
           			goalOee:$('#goalOee').val(),
           			goalLostTime:$('#goalLostTime').val(),
           			goalNg:$('#goalNg').val(),
           			threshold:$('#threshold').val(),
           			beat:$('#beat').val(),
           			note:$('#productionUnitNote').val(),
           			allowMultiWorkSheetRunning:$('#allowMultiWorkSheetRunning').iCombobox('getValue')
           			},function(data){
           				if(data.success){
	           				$('#productionUnitAddDialog').iDialog('close');
	           				$('#productionUnitDg').iDatagrid('reload');
	           				$('#productionUnitTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#productionUnitAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
		</sec:authorize>
		<sec:authorize access="hasAuthority('EDIT_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#productionUnitDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'productionUnitEditDialog',
                width: 600,
                height: 600,
                href: 'console/jsp/productionUnit_edit.jsp',
                url:'productionUnit/queryProductionUnitById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var productionUnitCode = $('#productionUnitCode_edit').val();
           			var productionUnitName = $('#productionUnitName_edit').val();
           			if(productionUnitName==null || ''===$.trim(productionUnitName)){
           				$.iMessager.alert('提示','请输入生产单元名称');
           				return false;
           			}
           			var classTypeName = $('#classTypeName').iCombobox('getText');
           			if(!classTypeName){
           			    $.iMessager.alert('提示','请选择班次类别!');
           				return false;
           			 }
           			$.get('productionUnit/updateProductionUnit.do',{
           			id:$('#productionUnitDg').iDatagrid('getSelected').id,
           			code:productionUnitCode,
           			name:productionUnitName,
           			'parent.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			goalOutput:$('#goalOutput').val(),
           			'classType.name':$('#classTypeName').iCombobox('getText'),
           			classTypeName:$('#classTypeName').iCombobox('getText'),
           			goalOee:$('#goalOee').val(),
           			goalLostTime:$('#goalLostTime').val(),
           			goalNg:$('#goalNg').val(),
           			threshold:$('#threshold').val(),
           			beat:$('#beat').val(),
           			note:$('#productionUnitNote_edit').val(),
           			allowMultiWorkSheetRunning:$('#allowMultiWorkSheetRunning').iCombobox('getValue')
           			},function(data){
           				if(data.success){
	           				$('#productionUnitEditDialog').iDialog('close');
	           				$('#productionUnitDg').iDatagrid('reload');
	           				$('#productionUnitTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#productionUnitEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
		</sec:authorize>
		<sec:authorize access="hasAuthority('DEL_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-trash',
       url:'productionUnit/deleteProductionUnit.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'productionUnitDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
		</sec:authorize>
		<sec:authorize access="hasAuthority('DISABLE_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-stop',
       url:'productionUnit/disabledProductionUnit.do',
       grid: {uncheckedMsg:'请选择操作的生产部门',id:'productionUnitDg',param:'id:id'}" id="productionUnitSwitchBtn">停用</a>
		</sec:authorize>
	</div>
	<!-- 生产单元表格工具栏结束 -->
	<!-- 设备表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
		<sec:authorize access="hasAuthority('ADDDEVICE_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
         parentGrid:{
               type:'datagrid',
               id:'productionUnitDg'
            },
       dialog:{
       		beforeOpenCheckUrl: 'productionUnit/existSubProductionUnit.do?id={id}',
           id:'deviceAddDialog',
            width:600,
           height:400,
           href:'console/jsp/productionUnit_device_add.jsp',
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
           			$.get('device/updateDeviceProductionUnit.do',{
           				productionUnitId:$('#productionUnitDg').iDatagrid('getSelected').id,
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
		</sec:authorize>
		<!--  <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#device-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/productionUnit_device_edit.jsp',
                url:'position/queryPositionById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var positionCode = $('#positionCode_edit').val();
           			var positionName = $('#positionName_edit').val();
           			if(positionName==null || ''===$.trim(positionName)){
           				return false;
           			}
           			$.get('position/updatePosition.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:positionCode,
           			name:positionName,
           			'department.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#positionNote_edit').val()
           			},function(data){
           				if(data.success){
	           				$('#positionEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a> -->
		<sec:authorize access="hasAuthority('DELDEVICE_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'device/deleteDeviceFromProductionUnit.do',
       grid: {uncheckedMsg:'请先勾选要移除的数据',id:'position',param:'id:id'}">删除</a>
		</sec:authorize>
		<!--  <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
      data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-stop',
       url:'device/disabledDevice.do',
       grid: {uncheckedMsg:'请选择操作的设备',id:'position',param:'id:id'}">停用</a> -->
	</div>
	<!-- 设备表格工具栏结束 -->
	<!-- 员工表格工具栏开始 -->
	<div id="relateDoc-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
		<sec:authorize access="hasAuthority('ADDEMPLOYEE_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#relateDoc-toolbar',
       iconCls: 'fa fa-plus',
         parentGrid:{
               type:'datagrid',
               id:'productionUnitDg'
            },
       dialog:{
           id:'employeeAddDialog',
            width:600,
           height:400,
           href:'console/jsp/productionUnit_employee_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           		var ids = $('#employeeTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的设备!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].code);
           			}
           			$.get('employee/updateEmployeeProductionUnitId.do',{
           				productionUnitId:$('#productionUnitDg').iDatagrid('getSelected').id,
           				employeeIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#relateDoc').iDatagrid('reload');
           					$('#employeeTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#employeeAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
		</sec:authorize>
		<sec:authorize access="hasAuthority('DELEMPLOYEE_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#relateDoc-toolbar',
       iconCls:'fa fa-trash',
       url:'employee/deleteEmployeeProductionUnitId.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'employeeId:code'}">删除</a>
		</sec:authorize>
	</div>
	<!-- 文档资料表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
		<sec:authorize access="hasAuthority('UPLOADDOC_PRODUCTIONUNIT')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#documentData-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'productionUnitDg',
           param:'relatedId:id'
       },
       dialog:{
           id:'uploadDocDialog',
           title:'上传',
           width: 600,
           height: 400,
           href:'console/jsp/production_doc_upload.jsp',
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
                		relatedId:$('#relatedId').val(), 
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
		<sec:authorize access="hasAuthority('DELDOC_PRODUCTIONUNIT')">
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
		<sec:authorize access="hasAuthority('DOWNLOADDOC_PRODUCTIONUNIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
		</sec:authorize>
	</div>
	<!-- 文档资料表格工具栏结束 -->
</body>
</html>