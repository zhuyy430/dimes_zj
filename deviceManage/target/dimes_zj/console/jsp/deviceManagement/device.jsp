<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
function openWindow(){
	var checkedArray = $("#deviceDg").iDatagrid("getChecked");
	if(checkedArray.length>0){
		var ids = "";
		for(var i = 0;i<checkedArray.length;i++){
			var device = checkedArray[i];
			ids += device.id +",";
		}
		
		ids = ids.substring(0,ids.length-1);
		$("#ids").val(ids);
			var newWin = window.open("console/jsp/deviceManagement/device_print.jsp"); 
	}else{
		alert("请选择要打印二维码的记录!");
		return false;
	}
} 
	function downloadFile() {
		var dg = $("#documentData").iDatagrid('getSelected');
		if (!dg) {
			$.messager.alert("提示", "请选择要下载的文件!");
			return;
		}
		var id = dg.id;
		window.location.href = "relatedDoc/download.do?id=" + id;
	}
	//导出保养记录
	function exportMaintenancePlanRecord() {
		var dg = $("#deviceDg").iDatagrid('getSelected');
		if (!dg) {
			$.messager.alert("提示", "请选择设备信息!");
			return;
		}
		var id = dg.id;
		window.location.href = "maintenancePlanRecord/queryAllMaintenancePlanRecords4CurrentYearNoPager.do?deviceId="
				+ id;
	}
	//导出维修记录
	function exportDeviceRepairRecord() {
		var dg = $("#deviceDg").iDatagrid('getSelected');
		if (!dg) {
			$.messager.alert("提示", "请选择设备信息!");
			return;
		}
		var id = dg.id;
		window.location.href = "deviceRepairOrder/exportDeviceRepairOrderByDeviceId.do?deviceId="
				+ id;
	}
	//预览
	function preview() {
		var $doc = $("#documentData").iDatagrid("getSelected");
		if (!$doc) {
			alert("请选择预览的文档!");
			return false;
		}
		var url = $doc.url;
		//显示文档内容的iframe对象
		var $previewDiv = $("#fullScreenDiv");
		$previewDiv.empty();
		var suffix = '';
		//截取后缀
		if (url != null && $.trim(url) != '') {
			var point = url.lastIndexOf(".");
			suffix = url.substr(point + 1);
		} else {
			return false;
		}
		var $docIframe = $("<iframe style='width:100%;height:100%;'>");
		switch (suffix) {
			case "pdf":
			case "PDF": {
				/* $docIframe.attr("src", url);
				$previewDiv.append($docIframe); */
				window.open(url);
				break;
			}
			case "png":
			case "PNG":
			case "jpg":
			case "JPG":
			case "JPEG":
			case "jpeg":
			case "gif":
			case "GIF":
			case "bmp":
			case "BMP": {
				window.open(url);			
				break;
			}
			default: {
				$.iMessager.alert('提示','选择文件不支持预览!');
			}
		}
	}
</script>
<style>
#dialog-layer {
	z-index: 99;
	position: absolute;
	left: 0; 
	right: 0;
	top: 30px;
	bottom: 0;
	height: 800px;
	width: 800px;
	display: none;
	margin-left:auto;
	margin-right:auto;
	text-align:center;
}
</style>
</head>
<body>
	<!-- 全屏预览 #EAF0F6 -->
	<div id="dialog-layer">
		<a style="font-size:20px;color:red;  cursor: pointer;height:25px;"
			onClick="$('#dialog-layer').css('display','none')">关闭</a>
		<div
			style="display: table-cell;vertical-align: middle; width:800px;
    		text-align: center; height:770px;background-color:gray;"
			id="fullScreenDiv"></div>
	</div>
	<input type="hidden" id="ids" />
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
                      param:'productionUnitId:id',
                   grid:[
                       {type:'datagrid',id:'deviceDg'},
                   ]
               },onBeforeExpand: function (row) {
			   		if(row){
			   			$(this).iTreegrid('options').url='productionUnit/queryTopProductionUnits.do?parentId=' + row.id;
			   		}
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
						data-options="id:'deviceDg',
                       url:'device/queryDevicesByProductionUnitId.do?module=deviceManage',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                          singleSelect:true,
                        selectOnCheck:false,
                        checkOnSelect:false,
                       parentGrid:{
                           type:'treegrid',
                           id:'productionUnitTg',
                       },
                       childTab: [{id:'southTabs'}],
                       onLoadSuccess:function(){
                       		$('#southTabs').iTabs('select',0);
                       		$('#parameter').iDatagrid('load');
                       }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:true,width:'80px'"></th>
								<th
									data-options="field:'code',title:'设备代码',width:'100px',align:'center'"></th>
								<th
									data-options="field:'name',title:'设备名称',width:'100px',align:'center'"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'100px',align:'center'"></th>
								<th
									data-options="field:'manufacturer',title:'生产厂家',width:'100px',align:'center'"></th>
								<th
									data-options="field:'outFactoryCode',title:'出厂编号',width:'100px',align:'center'"></th>
								<th
									data-options="field:'inFactoryDate',title:'到厂日期',width:'100px',align:'center',formatter:function(value,row,index){
                            if(value){
                                var date = new Date(value);
                                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                            }else{
                                return '';
                            }
                        }"></th>
								<th
									data-options="field:'checkDate',title:'验收日期',width:'100px',align:'center',formatter:function(value,row,index){
                            if(value){
                                var date = new Date(value);
                                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                            }else{
                                return '';
                            }
                        }"></th>
								<th
									data-options="field:'status',title:'设备状态',width:'100px',align:'center',formatter:function(value,row,index){
                                        if(row.status=='0'){
                                            return '正常运行';
                                        }else if(row.status=='2'){
                                            return '停机';
                                        }else{
                                            return '';
                                        }
                                    }"></th>
								<th
									data-options="field:'projectType',title:'设备类别',width:'100px',align:'center',formatter:function(value,row,index){
                                        if(row.projectType){
                                            return row.projectType.name;
                                        }else{
                                            return '';
                                        }
                                    }"></th>
								<th
									data-options="field:'deviceLevelName',title:'设备等级',width:'100px',align:'center'"></th>
								<th
									data-options="field:'installPosition',title:'安装位置',width:'100px',align:'center'"></th>
								<th
									data-options="field:'assetNumber',title:'资产编号',width:'100px',align:'center'"></th>
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
                         id:'deviceDg',
                         param:'deviceId:id,relatedId:id,deviceCode:code'
                     }">
						<div title="设备参数" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'parameter',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'parameter/queryDeviceSiteParameterByDeviceId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'parameterCode',title:'参数代码',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.code;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterName',title:'参数名称',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterBaseCode',title:'参数值类型',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.baseCode;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'standardValue',title:'标准值',width:'180px',align:'center',align:'center'"></th>
										<th
											data-options="field:'upLine',title:'上限值',width:'180px',align:'center',align:'center'"></th>
										<th
											data-options="field:'lowLine',title:'下限值',width:'180px',align:'center',align:'center'"></th>
										<th
											data-options="field:'note',title:'备注',width:'180px',align:'center',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="备品备件" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'spareparts',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'deviceSparepartMapping/queryDeviceSparepartMappingByDeviceCode.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',width:'80px',hidden:true"></th>
										<th
											data-options="field:'sparepartCode',title:'备件代码',width:'180px',align:'center',formatter:function(value,row,index){
			                        	if(row.sparepart){
			                        		return row.sparepart.code;
			                        	}else{
			                        		return '';
			                        	}
			                        }"></th>
										<th
											data-options="field:'sparepartName',title:'备件名称',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
			                        	if(row.sparepart){
			                        		return row.sparepart.name;
			                        	}else{
			                        		return '';
			                        	}
			                        }"></th>
										<th
											data-options="field:'unitType',title:'规格型号',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
			                        	if(row.sparepart){
			                        		return row.sparepart.unitType;
			                        	}else{
			                        		return '';
			                        	}
			                        }"></th>
										<th
											data-options="field:'measurementUnit',title:'计量单位',width:'180px',align:'center',align:'center',formatter:function(value,row,index){
			                        	if(row.sparepart){
			                        		return row.sparepart.measurementUnit;
			                        	}else{
			                        		return '';
			                        	}
			                        }"></th>
										<th
											data-options="field:'lastUseDate',title:'最近更换日期',width:'180px',align:'center',formatter:function(value,row,index){
			                        	if(value){
			                        		var date = new Date(value);
			                        		return getDateTime(date);
			                        	}else{
			                        		return '';
			                        	}
			                        }"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="点检项目" data-options="id:'tab1',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'spotCheck',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                               url:'deviceProjectRecord/queryDeviceProjectRecordByDeviceIdAndType.do?type=SPOTINSPECTION'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'code',title:'项目编码',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'name',title:'点检项目',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'classesCode',title:'班次组别代码',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'classesName',title:'班次组别名称',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'standard',title:'标准',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'upperLimit',title:'上限值',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'lowerLimit',title:'下限值',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'method',title:'方法',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'frequency',title:'频次',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'note',title:'说明',width:50,align:'center',width:'200px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="保养项目" data-options="id:'tab4',iconCls:'fa fa-th'">
							<table data-toggle="topjui-datagrid"
								data-options="id:'maintain',
                                   initCreate: false,
                                   fitColumns:true,
                                   singleSelect:true,
                                   url:'deviceProjectRecord/queryDeviceProjectRecordByDeviceIdAndType.do?type=MAINTAIN'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'recordTypeName',title:'保养类别',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'code',title:'项目编码',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'name',title:'保养项目',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'standard',title:'标准',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'method',title:'方法',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'frequency',title:'频次',width:50,align:'center',width:'200px',align:'center'"></th>
										<th
											data-options="field:'note',title:'说明',width:50,align:'center',width:'200px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="文档资料" data-options="id:'tab5',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'relatedDoc/queryDocsByRelatedIdAndType.do?moduleCode=device'">
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
						<div title="维修记录" data-options="id:'tab6',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'RepairRecord',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'deviceRepairOrder/queryDeviceRepairOrderByDeviceId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'serialNumber',title:'单据编号',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'createDate',title:'报修时间',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        return getDateTime(date);
                                    }else{
                                        return '';
                                    }
                                }"></th>
										<th
											data-options="field:'informant',width:'150px',align:'center',title:'报修人员',sortable:false"></th>
										<th
											data-options="field:'batchNumber',title:'故障类型',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.ngreason) {
	                                    	return row.ngreason.name;
	                                    }else{return ''}
                                    }"></th>
										<th
											data-options="field:'ngDescription',title:'故障描述',width:'200px',align:'center',sortable:false"></th>

										<th
											data-options="field:'maintainName',width:'150px',align:'center',title:'维修人员',sortable:false"></th>

										<th
											data-options="field:'status',width:'150px',align:'center',title:'维修状态',sortable:false,formatter:function(value,row,index){
	                                    if (row.status=='WAITINGASSIGN'){//等待派单
									            return '等待派单'; 
									        }else if(row.status=='WAITINCOMFIRM'){//等待接单确认
									            return '等待接单';
									        }else if(row.status=='MAINTAINING'){//维修中
									            return '维修中';
									        }else if(row.status=='WORKSHOPCOMFIRM'){//车间确认
									            return '车间确认'; 
									        }else if(row.status=='MAINTAINCOMPLETE'){//维修完成
									            return '维修完成'; 
									        }else if(row.status=='WAITWORKSHOPCOMFIRM'){//待车间确认
									            return '待车间确认'; 
									        }else if(row.status=='FAIL_SAFEOPERATION'){
									            return '带病运行'; 
									        }else{
									            return ''; 
									        }
                                    }"></th>
										<th
											data-options="field:'isClosed',width:'80px',align:'center',title:'是否关闭',formatter:function(value,row,index){
                                    	if(value==true){
                                    		return 'Y';
                                    	}else if(value==false){
                                    		return 'N';
                                    	}else{
                                    		return '';
                                    	}
                                    }"></th>
										<th
											data-options="field:'submitDate',width:'220px',align:'center',title:'完成时间',formatter:function(value,row,index){
                                    	if(value){
                                    		var date = new Date(value);
                                    		return getDateTime(date);
                                    	}else{
                                    		return '';
                                    	}
                                    }"></th>
										<th
											data-options="field:'completeDate',width:'220px',align:'center',title:'确认时间',formatter:function(value,row,index){
                                    	if(value){
                                    		var date = new Date(value);
                                    		return getDateTime(date);
                                    	}else{
                                    		return '';
                                    	}
                                    }"></th>
										<th
											data-options="field:'confirmName',width:'150px',align:'center',title:'确认人',sortable:false"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="保养记录" data-options="id:'tab7',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'maintainRecord',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'maintenancePlanRecord/queryAllMaintenancePlanRecords4CurrentYear.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:'80px'"></th>
										<th
											data-options="field:'maintenanceDate',title:'计划日期',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(value){
                                    		var date = new Date(value);
                                    		return getDate(date);
                                    	}else{
                                    		return '';
										}                                    	
                                    }"></th>
										<th
											data-options="field:'maintenancedDate',title:'保养日期',width:'150px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(value){
                                    		var date = new Date(value);
                                    		return getDate(date);
                                    	}else{
                                    		return '';
										}                                    	
                                    }"></th>
										<th
											data-options="field:'maintenanceType',title:'保养类别',width:'120px',align:'center'"></th>
										<th
											data-options="field:'employeeName',title:'责任人',width:'120px',align:'center'"></th>
										<th
											data-options="field:'status',title:'保养状态',width:'120px',align:'center'"></th>
										<th
											data-options="field:'completeDate',title:'完成时间',width:'120px',align:'center'"></th>
										<th
											data-options="field:'confirmName',title:'确认人',width:'120px',align:'center'"></th>
										<th
											data-options="field:'confirmDate',title:'确认时间',width:'120px',align:'center',formatter:function(value,row,index){
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
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 生产单元表格工具栏开始 -->
	<div id="deviceDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'deviceDg'
       }">
<sec:authorize access="hasAuthority('ADD_DEVICEMANAGE')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#deviceDg-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            },
       dialog:{
           id:'deviceAddDialog',
           width:800,
           height:600,
           href:'console/jsp/deviceManagement/device_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
                       var code = $('#code').val();
                       if(code==null || ''===$.trim(code)){
                           $.iMessager.alert('提示','请输入设备代码!');
                           return false;
                       }
                       
                       var name = $('#name').val();
                       if(name==null || ''===$.trim(name)){
                           $.iMessager.alert('提示','请输入设备名称!');
                           return false;
                       }
                       var projectType=$('#projectType').combobox('getValue');
                       if(projectType==null || ''===$.trim(projectType)){
                           $.iMessager.alert('提示','请选择设备类型!');
                           return false;
                       }
                       
                       var deviceLevel=$('#deviceLevel').combobox('getValue');
                       if(deviceLevel==null || ''===$.trim(deviceLevel)){
                           $.iMessager.alert('提示','请选择设备等级!');
                           return false;
                       }
                       $.get('device/addDeviceAtManage.do',{
                       code:code,
                       name:name,
                       isDeviceManageUse:true,
                       unitType:$('#unitType').val(),
                       'projectType.id':$('#projectType').combobox('getValue'),
                       'deviceLevel.id':$('#deviceLevel').combobox('getValue'),
                       outFactoryCode:$('#outFactoryCode').val(),
                       manufacturer:$('#manufacturer').val(),
                       trader:$('#trader').val(),
                       status:$('#status').combobox('getValue'),
                       outFactoryDate:$('#outFactoryDate').val(),
                       inFactoryDate:$('#inFactoryDate').val(),
                       installDate:$('#installDate').val(),
                       checkDate:$('#checkDate').val(),
                       assetNumber:$('#assetNumber').val(),
                       weight:$('#weight').val(),
                       shapeSize:$('#shapeSize').val(),
                       installPosition:$('#installPosition').val(),
                       power:$('#power').val(),
                       actualPower:$('#actualPower').val(),
                       note:$('#note').val(),
                       'productionUnit.id':$('#productionUnitTg').iTreegrid('getSelected').id
                       },function(data){
                           if(data.success){
                               $('#deviceAddDialog').iDialog('close');
                               $('#deviceDg').iDatagrid('reload');
                               $('#productionUnitTg').iTreegrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#deviceAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_DEVICEMANAGE')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#deviceDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'deviceEditDialog',
                width: 800,
                height: 600,
                href: 'console/jsp/deviceManagement/device_edit.jsp',
                url:'device/queryDeviceById.do?id={id}',
                 buttons:[
                   {text:'保存',handler:function(){
                       var code = $('#code').val();
                       var name = $('#name').val();
                       if(name==null || ''===$.trim(name)){
                           $.iMessager.alert('提示','请输入设备名称!');
                           return false;
                       }
                       
                       $.get('device/updateDeviceAtManage.do',{
                       id:$('#deviceDg').iDatagrid('getSelected').id,
                       code:code,
                       name:name,
                       isDeviceManageUse:true,
                       unitType:$('#unitType').val(),
                       'projectType.id':$('#projectTypeId').combobox('getValue'),
                       'deviceLevel.id':$('#deviceLevel').combobox('getValue'),
                       outFactoryCode:$('#outFactoryCode').val(),
                       manufacturer:$('#manufacturer').val(),
                       trader:$('#trader').val(),
                       status:$('#status').combobox('getValue'),
                       outFactoryDate:$('#outFactoryDate').val(),
                       inFactoryDate:$('#inFactoryDate').val(),
                       installDate:$('#installDate').val(),
                       checkDate:$('#checkDate').val(),
                       assetNumber:$('#assetNumber').val(),
                       weight:$('#weight').val(),
                       shapeSize:$('#shapeSize').val(),
                       installPosition:$('#installPosition').val(),
                       power:$('#power').val(),
                       actualPower:$('#actualPower').val(),
                       note:$('#note').val(),
                       'productionUnit.id':$('#productionUnitTg').iTreegrid('getSelected').id
                       },function(data){
                           if(data.success){
                               $('#deviceEditDialog').iDialog('close');
                               $('#deviceDg').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#deviceEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
		<div style="float: right; margin-right: 10px">
<sec:authorize access="hasAuthority('IMPORT_DEVICEMANAGE')">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton"
					data-options="method: 'openDialog',
            extend: '#deviceDg-toolbar',
            iconCls: 'fa fa-level-down',
            dialog: {
                id:'deviceImportDialog',
                width: 800,
                height: 600,
                href: 'console/jsp/deviceManagement/device_import.jsp',
                 buttons:[
                   {text:'保存',handler:function(){
                   		var ids = $('#deviceTab').iDatagrid('getSelections');
	           		var idsArray = new Array();
	           		if(ids.length<=0){
	           			alert('请选择要添加的数据!');
	           		}else{
	           			for(var i = 0;i < ids.length;i++){
	           				idsArray.push(ids[i].id);
	           			}
	           		}
                       $.get('device/updateDimesDeviceToManage.do',{
						deviceIds:JSON.stringify(idsArray)                      
                       },function(data){
                           if(data.success){
                               $('#deviceImportDialog').iDialog('close');
                               $('#deviceDg').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#deviceImportDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">基础资料导入</a>
</sec:authorize>
		</div>
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-trash',
       url:'device/deleteDevice.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'deviceDg',param:'id:id,isDeviceMgrUse:isDeviceMgrUse'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('COPY_DEVICEMANAGE')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#deviceDg-toolbar',
            iconCls: 'fa fa-copy',
            dialog: {
                id:'deviceCopyDialog',
                width: 800,
                height: 600,
                url:'device/queryDeviceByIdWithCopy.do?id={id}',
                href: 'console/jsp/deviceManagement/device_copy.jsp',
                title:'复制',
                 buttons:[
                   {text:'复制',handler:function(){
                       var code = $('#code').val();
                       var name = $('#name').val();
                       var productionUnitId=$('#productionUnitId').val();
                       if(name==null || ''===$.trim(name)){
                           $.iMessager.alert('提示','请输入设备名称!');
                           return false;
                       }
                       if(code==null || ''===$.trim(code)){
                           $.iMessager.alert('提示','请输入设备代码!');
                           return false;
                       }
                       if(productionUnitId==null || ''===$.trim(productionUnitId)){
                       	   $.iMessager.alert('提示','请选择生产单元!');
                           return false;
                       }
                       
                       $.get('device/copyDevice.do',{
                       copyId:$('#deviceDg').iDatagrid('getSelected').id,
                       code:code,
                       name:name,
                       unitType:$('#unitType').val(),
                       'projectType.id':$('#projectTypeId').combobox('getValue'),
                       outFactoryCode:$('#outFactoryCode').val(),
                       manufacturer:$('#manufacturer').val(),
                       trader:$('#trader').val(),
                       status:$('#status').combobox('getValue'),
                       outFactoryDate:$('#outFactoryDate').val(),
                       inFactoryDate:$('#inFactoryDate').val(),
                       installDate:$('#installDate').val(),
                       checkDate:$('#checkDate').val(),
                       assetNumber:$('#assetNumber').val(),
                       weight:$('#weight').val(),
                       shapeSize:$('#shapeSize').val(),
                       installPosition:$('#installPosition').val(),
                       power:$('#power').val(),
                       actualPower:$('#actualPower').val(),
                       note:$('#note').val(),
                       'productionUnit.id':productionUnitId
                       },function(data){
                           if(data.success){
                               $('#deviceCopyDialog').iDialog('close');
                               $('#deviceDg').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#deviceCopyDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">复制</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINTQR_DEVICEMANAGE')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls: 'fa fa-print',
            modal:true,
            parentGrid:{
		       	type:'treegrid',
		       	id:'productionUnitTg'
		      }" onClick="openWindow()">打印二维码</a>
</sec:authorize>
	</div>
	<!-- 生产单元表格工具栏结束 -->
	<!-- 设备参数表格工具栏开始 -->
	<div id="parameter-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'parameter'
       }">
<sec:authorize access="hasAuthority('ADD_DEVICEMANAGE_PARAMETER')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#parameter-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'deviceDg',
       	param:'parentDeviceId:id'
       },
       dialog:{
           id:'parameterAddDialog',
           href:'console/jsp/deviceManagement/device_parameter_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			/*var deviceSiteId = $('#deviceSiteId').val();
           			if(!deviceSiteId){
           				$.iMessager.alert('提示','请选择站点!');
           				return false;
           			}*/
           			$.get('parameter/addDeviceSiteParameter.do',{
           			/*'deviceSite.id':deviceSiteId,*/
           			deviceId:$('#deviceDg').iDatagrid('getSelected').id,
           			'parameter.id':$('#parameterId').val(),
           			upLine:$('#upLine').val(),
           			lowLine:$('#lowLine').val(),
           			standardValue:$('#standardValue').val(),
           			note:$('#note').val(),
           			trueValue:$('#trueValue').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#parameter').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_DEVICEMANAGE_PARAMETER')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#parameter-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'parameterEditDialog',
                	    width: 600,
                		height: 700,
		           href:'console/jsp/deviceManagement/device_parameter_edit.jsp',
		           url:'parameter/queryDeviceSiteParameterById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			$.get('parameter/updateDeviceSiteParameter.do',{
           			id:$('#parameter').iDatagrid('getSelected').id,
           			upLine:$('#upLine').val(),
           			lowLine:$('#lowLine').val(),
           			standardValue:$('#standardValue').val(),
           			note:$('#note').val(),
           			trueValue:$('#trueValue').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#parameter').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE_PARAMETER')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#parameter-toolbar',
       iconCls:'fa fa-trash',
       url:'parameter/deleteDeviceSiteParameterById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 设备参数表格工具栏结束 -->
	<!-- 备品备件表格工具栏开始 -->
	<div id="spareparts-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'spareparts'
       }">
<sec:authorize access="hasAuthority('ADD_DEVICEMANAGE_SPAREPART')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#spareparts-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'deviceDg',
       	param:'parentDeviceId:id'
       },
       dialog:{
           id:'parameterAddDialog',
           width: 700,
           height: 700,
           href:'console/jsp/deviceManagement/device_sparepart_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           		var ids = $('#sparepartTab').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的数据!');
           			return ;
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           		}
           			$.get('deviceSparepartMapping/addSparepart4Devices.do',{
          			deviceId:$('#deviceDg').iDatagrid('getSelected').id,
           			sparepartIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#spareparts').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
		<%-- <sec:authorize access="hasAuthority('DEIT_DEVICEMANAGE_SPAREPART')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#spareparts-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'parameterEditDialog',
                	    width: 600,
                		height: 400,
		           href:'console/jsp/deviceManagement/device_sparepart_edit.jsp',
		           url:'deviceSparepartRecord/queryDeviceSparepartRecordById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			$.get('deviceSparepartRecord/updateDeviceSparepartRecord.do',{
           			id:$('#spareparts').iDatagrid('getSelected').id,
           			useCount:$('#useCount').val(),
           			note:$('#note').val(),
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#spareparts').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
            </sec:authorize> --%>
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE_SPAREPART')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#spareparts-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceSparepartMapping/deleteDeviceFromSparepart.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 备品备件表格工具栏结束 -->
	<!-- 点检项目表格工具栏开始 -->
	<div id="spotCheck-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'spotCheck'
       }">
<sec:authorize access="hasAuthority('ADD_DEVICEMANAGE_SPOTINSPECTION')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#spotCheck-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'spotCheckAddDialog',
           width: 700,
           height: 700,
           href:'console/jsp/deviceManagement/device_spotcheck_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               	var ids = $('#projectTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的点检项目!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
                    $.get('deviceProjectRecord/addDeviceProjectRecordWithMore.do',{
                           deviceId:$('#deviceDg').iDatagrid('getSelected').id,
                           projectsIds:JSON.stringify(idsArray),
                           type:'SPOTINSPECTION',
                           classesCode:$('#classesType').iCombobox('getValue'),
                           classesName:$('#classesType').iCombobox('getText')
                    },function(data){
                           if(data.success){
                               $('#spotCheck').iDatagrid('reload');
                               $('#spotCheckAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                   }
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#spotCheckAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_DEVICEMANAGE_SPOTINSPECTION')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#spotCheck-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'spotCheckEditDialog',
                width: 600,
                   height: 400,
                href:'console/jsp/deviceManagement/device_spotcheck_edit.jsp',
                url:'deviceProjectRecord/queryDeviceProjectRecordById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
                       $.get('deviceProjectRecord/updateDeviceProjectRecord.do',{
                           id:$('#spotCheck').iDatagrid('getSelected').id,
                           'device.id':$('#deviceDg').iDatagrid('getSelected').id,
                           name:$('#name').iTextbox('getValue'),
                           standard:$('#standard').val(),
                           method:$('#method').val(),
                           upperLimit:$('#upperLimit').val(),
 						   lowerLimit:$('#lowerLimit').val(),
                           frequency:$('#frequency').val()
                       },function(data){
                           if(data.success){
                               $('#spotCheckEditDialog').iDialog('close');
                               $('#spotCheck').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#spotCheckEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE_SPOTINSPECTION')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#spotCheck-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProjectRecord/deleteDeviceProjectRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'spotCheck',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 点检项目表格工具栏结束 -->
	<!-- 润滑项目表格工具栏开始 -->
	<div id="lubrication-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'lubrication'
       }">

			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#lubrication-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'lubricationAddDialog',
           width: 600,
           height: 400,
           href:'console/jsp/deviceManagement/device_lubrication_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               	var ids = $('#projectTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的设备!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
                    $.get('deviceProjectRecord/addDeviceProjectRecordWithMore.do',{
                           deviceId:$('#deviceDg').iDatagrid('getSelected').id,
                           projectsIds:JSON.stringify(idsArray),
                           type:'LUBRICATION'
                    },function(data){
                           if(data.success){
                               $('#lubrication').iDatagrid('reload');
                               $('#lubricationAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                   }
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#lubricationAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>


			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#lubrication-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'lubricationEditDialog',
                width: 600,
                 height: 400,
                href:'console/jsp/deviceManagement/device_lubrication_edit.jsp',
                url:'deviceProjectRecord/queryDeviceProjectRecordById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
                       $.get('deviceProjectRecord/updateDeviceProjectRecord.do',{
                           id:$('#lubrication').iDatagrid('getSelected').id,
                           'device.id':$('#deviceDg').iDatagrid('getSelected').id,
                           name:$('#name').combobox('getText'),
                           standard:$('#standard').val(),
                           method:$('#method').val(),
                           frequency:$('#frequency').val()
                       },function(data){
                           if(data.success){
                               $('#lubricationEditDialog').iDialog('close');
                               $('#lubrication').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#lubricationEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>


			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#lubrication-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProjectRecord/deleteDeviceProjectRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'lubrication',param:'id:id'}">删除</a>

	</div>
	<!-- 润滑项目表格工具栏结束 -->
	<!-- 保养项目表格工具栏开始 -->
	<div id="maintain-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'maintain'
       }">
<sec:authorize access="hasAuthority('ADD_DEVICEMANAGE_MAINTAIN')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#maintain-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'maintainAddDialog',
           width: 700,
           height: 700,
           href:'console/jsp/deviceManagement/device_maintain_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               	var recordTypeCode = $('#maintainType').iCombobox('getValue');
               	if(!recordTypeCode){
               		alert('请选择保养类别');
               		return false;
               	}
               	var ids = $('#projectTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的保养项目!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
                    $.get('deviceProjectRecord/addDeviceProjectRecordWithMore.do',{
                           deviceId:$('#deviceDg').iDatagrid('getSelected').id,
                           projectsIds:JSON.stringify(idsArray),
                           type:'MAINTAIN',
                           recordTypeCode:$('#maintainType').iCombobox('getValue'),
                           recordTypeName:$('#maintainType').iCombobox('getText')
                    },function(data){
                           if(data.success){
                               $('#maintain').iDatagrid('reload');
                               $('#maintainAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                   }
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#maintainAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_DEVICEMANAGE_MAINTAIN')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#maintain-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'maintainEditDialog',
                width: 600,
                height: 400,
                href:'console/jsp/deviceManagement/device_maintain_edit.jsp',
                url:'deviceProjectRecord/queryDeviceProjectRecordById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
                       $.get('deviceProjectRecord/updateDeviceProjectRecord.do',{
                           id:$('#maintain').iDatagrid('getSelected').id,
                           'device.id':$('#deviceDg').iDatagrid('getSelected').id,
                           name:$('#name').iTextbox('getValue'),
                           standard:$('#standard').val(),
                           method:$('#method').val(),
                           frequency:$('#frequency').val(),
                           recordTypeName:$('#recordTypeName').iTextbox('getValue')
                       },function(data){
                           if(data.success){
                               $('#maintainEditDialog').iDialog('close');
                               $('#maintain').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#maintainEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE_MAINTAIN')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#maintain-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProjectRecord/deleteDeviceProjectRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'maintain',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 保养项目表格工具栏结束 -->
	<!-- 文档资料表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
<sec:authorize access="hasAuthority('UPLOAD_DEVICEMANAGE_DOC')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#documentData-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'relatedId:id'
       },
       dialog:{
           id:'uploadDocDialog',
           title:'上传',
           width: 600,
           height: 400,
           href:'console/jsp/deviceManagement/device_doc_upload.jsp',
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
                		note:$('#note').iTextbox('getValue'),
                		relatedId:$('#deviceDg').iDatagrid('getSelected').id
                	  },
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'file', //文件上传域的ID
                dataType: 'text', //返回值类型 一般设置为json
                success: function (data, status) {
                var obj = JSON.parse(data);
                	if(!obj.success){
                		 $.iMessager.alert('提示',obj.message);
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
<sec:authorize access="hasAuthority('DEL_DEVICEMANAGE_DOC')">
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
<sec:authorize access="hasAuthority('DOWNLOAD_DEVICEMANAGE_DOC')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
		 <sec:authorize access="hasAuthority('PREVIEW_DEVICEMANAGE_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			onclick="preview()" data-options="iconCls:'fa fa-eye'">预览</a>
		 </sec:authorize>
			
	</div>
	<!-- 保养记录 -->
	<div id="maintainRecord-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'maintainRecord'
       }">
<sec:authorize access="hasAuthority('EXPORT_DEVICEMANAGE_MAINTAINERECORD')">
			<a href="javascript:void(0)" onclick="exportMaintenancePlanRecord()"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-sign-out'">导出</a>
</sec:authorize>
	</div>
	<!-- 维修记录 -->
	<div id="RepairRecord-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'RepairRecord'
       }">
<sec:authorize access="hasAuthority('EXPORT_DEVICEMANAGE_SEARVICERECORD')">
			<a href="javascript:void(0)" onclick="exportDeviceRepairRecord()"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-sign-out'">导出</a>
</sec:authorize>
	</div>
</body>
</html>