<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript">
 function downloadFile(){
	 var dg = $("#relateDoc").iDatagrid('getSelected');
	 if(!dg){
		 $.messager.alert("提示","请选择要下载的文件!");
		 return ;
	 }
	 var id = dg.id;
	 window.location.href="relatedDoc/download.do?id=" + id;
 }
//搜索备品备件记录
	function reloadRecord(){
		$("#departmentDg").iDatagrid('load',{
			spareCode:$("#spareCode").iTextbox('getValue'),
			spareName:$("#spareName").iTextbox('getValue'),
			spareType:$("#spareType").iTextbox('getValue'),
			typeId:$('#departmentTg').iTreegrid('getSelected').id
		});
	}
	//重置
	function resetSearch(){
		$("#spareCode").iTextbox('setValue','');
		$("#spareName").iTextbox('setValue','');
		$("#spareType").iTextbox('setValue','');
		reloadRecord();
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
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'projectType/queryTopProjectTypes.do',
			   queryParams:{rootType:'ROOTSPAREPARTTYPE'},
			   childGrid:{
			   	   param:'typeId:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'备件类别'"></th>
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
                       url:'sparepart/querySparepartsByTypeId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                       	type:'treegird',
                       	id:'departmentTg'
                       }, childTab: [{id:'southTabs'}]">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'code',title:'备件代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'name',title:'备件名称',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'graphNumber',title:'图号',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'manufacturer',title:'厂商',width:'150px',align:'center',sortable:false"></th>
								<th
									data-options="field:'measurementUnit',title:'计量单位',width:'150px',align:'center',sortable:false"></th>
								<th data-options="field:'mnemonicCode',title:'助记码',width:'150px',align:'center',sortable:false"></th>
								<th data-options="field:'inventory',title:'安全库存',width:'150px',align:'center',formatter:function(value,row ,index){
									if(value){
										return parseFloat(value).toFixed(3);
									}else{
										return '';
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
                         param:'relatedId:id,sparepartCode:code'
                     }">
						<div title="库存信息" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'warehouse/queryWarehousesBySparepartId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:200,align:'center'"></th>
										<th
											data-options="field:'warehouseCode',title:'仓库代码',checkbox:false,width:200,align:'center'"></th>
										<th
											data-options="field:'warehouseName',title:'仓库名称',width:200,align:'center'"></th>
										<th
											data-options="field:'count',title:'现有库存',width:200,align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="所属设备" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'relateDevice',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'deviceSparepartMapping/queryDeviceSparepartMappingBySparepartCode.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:80"></th>
										<th
											data-options="field:'productionUnitName',title:'生产单元名称',width:150,align:'center',formatter:function(value,row,index){
												if(row.device){
													if(row.device.productionUnit){
														return row.device.productionUnit.name;
													}else{
														return '';
													}
												}else{
													return '';
												}
											}"></th>
										<th data-options="field:'deviceCode',title:'设备代码',width:150,align:'center',formatter:function(value,row,index){
												if(row.device){
													return row.device.code;
												}else{
													return '';
												}
											}"></th>
										<th data-options="field:'deviceName',title:'设备名称',width:150,align:'center',formatter:function(value,row,index){
												if(row.device){
													return row.device.name;
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'url',title:'规格型号',width:150,align:'center',formatter:function(value,row,index){
												if(row.device){
													return row.device.unitType;
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'lastUseDate',title:'最近领用日期',width:150,align:'center',formatter:function(value,row,index){
											if(value){
												return getDateTime(new Date(value));
											}else{
												return '';
											}
										}"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="文档资料" data-options="id:'tab1',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'relateDoc',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'relatedDoc/queryDocsByRelatedIdAndType.do?relatedType=sparepartDoc'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:80"></th>
										<th
											data-options="field:'contentType',title:'文件类型',width:120,align:'center'"></th>
										<th data-options="field:'name',title:'文件名称',width:120,align:'center'"></th>
										<th data-options="field:'srcName',title:'源文件名称',width:120,align:'center'"></th>
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

	<!-- 部门表格工具栏开始 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('ADD_SPAREPART')">
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
           href:'console/jsp/deviceManagement/sparepart_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入备件代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入备件名称');
           				return false;
           			}
           			$.get('sparepart/addSparepart.do',{
           			code:code,
           			name:name,
           			unitType:$('#unitType').val(),
           			graphNumber:$('#graphNumber').val(),
           			manufacturer:$('#manufacturer').val(),
           			measurementUnit:$('#measurementUnit').val(),
           			mnemonicCode:$('#mnemonicCode').val(),
           			inventory:$('#inventory').val(),
           			'projectType.id':$('#departmentTg').iTreegrid('getSelected').id
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
<sec:authorize access="hasAuthority('EDIT_SPAREPART')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 700,
                href: 'console/jsp/deviceManagement/sparepart_edit.jsp',
                url:'sparepart/querySparepartById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入备件名称');
           				return false;
           			}
           			$.get('sparepart/updateSparepart.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			unitType:$('#unitType').val(),
           			graphNumber:$('#graphNumber').val(),
           			manufacturer:$('#manufacturer').val(),
           			measurementUnit:$('#measurementUnit').val(),
           			mnemonicCode:$('#mnemonicCode').val(),
           			inventory:$('#inventory').val(),
           			'projectType.id':$('#departmentTg').iTreegrid('getSelected').id
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
<sec:authorize access="hasAuthority('DEL_SPAREPART')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'sparepart/deleteSparepart.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
        <div></div>
        <span style="margin-right:20px;"> 条件查询:<!-- <input type="text" id="condition" name="condition"
      	data-toggle="topjui-textbox" data-options="prompt:'代码、名称、规格型号或责任人',width:200"/>  -->
      	备件代码:<input type="text" id="spareCode" name="spareCode" 
      	data-toggle="topjui-textbox" data-options="prompt:'备件代码',width:200"/>
      	备件名称:<input type="text" id="spareName" name="spareName" 
      	data-toggle="topjui-textbox" data-options="prompt:'备件名称',width:200"/>
		规格型号:<input type="text" id="spareType" name="spareType" 
      	data-toggle="topjui-textbox" data-options="prompt:'规格型号',width:200"/>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadRecord()">搜索</a>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-ban'" 
				onclick="resetSearch()">重置</a>
      	</span>
	</div>
	<div id="relateDoc-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
<sec:authorize access="hasAuthority('UPLOAD_SPAREPART_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'upload',
      	 extend: '#relateDoc-toolbar',
         parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       iconCls: 'fa fa-upload',
       uploadUrl:'relatedDoc/upload.do?type=sparepartDoc&relatedId={id}',
       accept:'file',
              dialog:{
              id:'uploadDocDialog',
           title:'上传',
           buttons:[
           	{text:'关闭',handler:function(){
           		$('#uploadDocDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-red'}
           ]
       }">上传</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_SPAREPART_DOC')">
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
           href:'console/jsp/deviceManagement/sparepart_relatedDocuments_edit.jsp',
           url:'relatedDoc/queryById.do?id={id}',
           buttons:[
           	{text:'修改',handler:function(){
           			$.get('relatedDoc/updateRelatedDocument.do',{
           				id:$('#relateDoc').iDatagrid('getSelected').id,
           				name:$('#name').val(),
           				note:$('#note').val()
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
       }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_SPAREPART_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#relateDoc-toolbar',
       iconCls:'fa fa-trash',
       url:'relatedDoc/deleteRelatedDocument.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的文件',id:'relateDoc',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DOWNLOAD_SPAREPART_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
	</div>
	<div id="relateDevice-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'relateDevice'
       }">
<sec:authorize access="hasAuthority('ADD_SPAREPART_MAPPINGDEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
      	 extend: '#relateDevice-toolbar',
       iconCls: 'fa fa-plus',
          dialog:{
           id:'parameterUpdateDialog',
            width:650,
           height:500,
           href:'console/jsp/deviceManagement/sparepart_device_add.jsp',
           buttons:[
           	{text:'新增',handler:function(){
           		var ids = $('#sparepartDeviceTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的设备!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('deviceSparepartMapping/addDevices4Sparepart.do',{
           				sparepartId:$('#departmentDg').iDatagrid('getSelected').id,
           				deviceIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#parameterUpdateDialog').iDialog('close');
           					$('#relateDevice').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-pencil',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterUpdateDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_SPAREPART_MAPPINGDEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#relateDevice-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceSparepartMapping/deleteDeviceFromSparepart.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的文件',id:'relateDevice',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
</body>
</html>