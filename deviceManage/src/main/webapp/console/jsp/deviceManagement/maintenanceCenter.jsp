<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<link type="text/css" href="console/js/topjui/css/topjui.timeaxis.css" rel="stylesheet">
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
	//显示设备分布图
	function showMaintenanceImages(row){
		$.get("maintenancePlanRecord/queryMaintenanceImages.do",{
			deviceId:row.device.id,
			docTypeCode:row.maintenanceTypeCode
		},function(result){
			fillTab0(result);
		});
	}
	//填充tab0，tab0为显示设备分布图的div
	function fillTab0(raletedDocuments){
		var tab0 = $("#tab0");
		tab0.empty();
		if(raletedDocuments && raletedDocuments.length>0){
			for(var i = 0;i<raletedDocuments.length;i++){
				var $img = $("<img style='height:300px;width:300px;margin:5px;'>");
				$img.attr("src",raletedDocuments[i].url);
				$img.attr("alt",raletedDocuments[i].name);
				tab0.append($img);
			}
		}
	}
	//保养项目确认
	function confirm(){
		var maintenancePlanRecord = $("#deviceDg").iDatagrid("getSelected");
		if(!maintenancePlanRecord){
			$.iMessager.alert("警告","请选择需要确认的保养记录!");
			return false;
		}
		
		$.iMessager.confirm("提示","确认执行该操作吗？",function(r){
			if(r){
				$.get("maintenancePlanRecord/confirm.do",{id:maintenancePlanRecord.id},function(result){
					if(result.statusCode==200){
						$("#deviceDg").iDatagrid("reload");
						$("#spotCheck").iDatagrid("reload");
					}else{
						$.iMessager.alert("警告",result.message);
					}
				});
			}
		});
	}
	//时间轴
	function timeAxis(id){
		 $.get("maintenancePlanRecord/queryWorkflow.do",{businessKey:id},function(data){
			var $div = $("<div  class='topjui-timeaxis-container' style='height:100%;width:100%;' data-toggle='topjui-timeaxis'>");
			 var list = JSON.stringify(data.data);
			//去除key中的单引号
			list = list.replace(/"(\w+)":/g, "$1:");
			var reg = new RegExp("\"","g");//g,表示全部替换。
			var l = list.replace(reg,"'");
			 $div.attr("data-options","title:'设备保养',list:" + l);
			 
			 var $wrapper=$("<div class='wrapper'>");
			 $div.append($wrapper);
			 var $light = "<div class='light'><i></i></div>";
			 $wrapper.append($light);
			 
			 var $topjui_timeaxis_main = $("<div class='topjui-timeaxis-main'>");
			 $wrapper.append($topjui_timeaxis_main);
			 
			 var $title = $("<h1 class='title'>");
			 $topjui_timeaxis_main.append($title);
			 $title.text("设备保养");
			 for(var i = 0;i<data.data.length;i++){
				 var $year = $("<div class='year year" + i +"'>");
				 $topjui_timeaxis_main.append($year);
				 
				 var $yearValue = $("<h2>");
				 $year.append($yearValue);
				 $yearValue.append("<a>" + data.data[i].year + "<i></i></a>");
				 for(var j = 0;j<data.data[i].list.length;j++){
				 var $list = $("<div class='list' style='height:116px;'>");
				 $year.append($list);
					 var obj = data.data[i].list[j];
					 var $ul = $("<ul class='ul"+j+"' style='position:absolute;'>");
					 $list.append($ul);
					 
					 var $li = $("<li class='cls'>");
					 $ul.append($li);
					 
					 var $date = $("<p class='date'>"+obj.date+"</p>");
					 $li.append($date);
					 var $intro = $("<p class='intro'>" + obj.intro + "</p>");
					 $li.append($intro);
					 var $version = $("<p class='version'>&nbsp;" +''  + "</p>");
					 $li.append($version);
					 var $more = $("<div class='more more00'>")
					 $li.append($more);
					 $more.append("<p>"+obj.more[0]?obj.more:'' + "</p>");
				 }
			 }
			var tab4 = $("#tab4"); 
			tab4.html($div);
		}); 
	}
	//搜索保养记录
	function reloadRecord(){
		$("#deviceDg").iDatagrid('load',{
			condition:$("#condition").iTextbox('getValue'),
			employeeName:$("#employeeName").iTextbox('getValue'),
			maintainType:$("#maintainType").iCombobox('getValue'),
			maintainStatus:$("#maintainStatus").iCombobox('getValue'),
			search_from:$("#search_from").val(),
			search_to:$("#search_to").val()
		});
	}
	//重置
	function resetSearch(){
		$("#condition").iTextbox('setValue','');
		$("#employeeName").iTextbox('setValue','');
		$("#maintainType").iCombobox('setValue','');
		$("#maintainStatus").iCombobox('setValue','');
		$("#search_from").iTextbox('setValue','');
		$("#search_to").iTextbox('setValue','');
		reloadRecord();
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
			$docIframe.attr("src", url);
			$previewDiv.append($docIframe);
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
			var $img = $("<img style='max-width:100%;max-height:100%;text-align:center;'>");
			$img.attr("src", url);
			$previewDiv.append($img);
			break;
		}
		default: {
			var $p = $("<p style='font-size:20px;color:red;'>");
			$p.append(suffix + "不支持预览!");
			$previewDiv.append($p);
		}
		}
		$("#dialog-layer").css("display", "block");
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
					<table style='padding-bottom:50px;background:#eee;'
					data-toggle="topjui-datagrid"
						data-options="id:'deviceDg',
						idField:'id',
						pagePosition:'bottom',
						pagination:true,
                       url:'maintenancePlanRecord/queryAllMaintenancePlanRecords.do',
                       singleSelect:true,
                       fitColumns:true,
                       childTab: [{id:'southTabs'}],
                        onSelect:function(index,row){
                                   showMaintenanceImages(row);  
                                   timeAxis(row.id);  
                               },
                               onLoadSuccess:function(){
                                       $('#deviceSite').iDatagrid('reload');
                                       $('#parameter').iDatagrid('reload');
                               }">
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
                                    		return getDateTime(date);
                                    	}else{
                                    		return '';
										}                                    	
                                    }"></th>
								<th
									data-options="field:'deviceType',title:'设备类别',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
	                                    	if(row.device.projectType){
	                                    		return row.device.projectType.name;
	                                    	}else{
	                                    		return '';
											} 
										}else{
											return '';
										}                                   	
                                    }"></th>
								<th
									data-options="field:'deviceCode',title:'设备代码',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
	                                    	return row.device.code;
										}else{
											return '';
										}                                   	
                                    }"></th>
								<th
									data-options="field:'deviceName',title:'设备名称',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
	                                    	return row.device.name;
										}else{
											return '';
										}                                   	
                                    }"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
	                                    	return row.device.unitType;
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
									data-options="field:'productionUnit',title:'生产单元',width:'120px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
	                                    	if(row.device.productionUnit){
	                                    		return row.device.productionUnit.name;
	                                    	}else{
	                                    		return '';
											} 
										} else{
											return '';
										}                                  	
                                    }"></th>
								<th
									data-options="field:'installPosition',title:'安装位置',width:'150px',align:'center',
                                    formatter:function(value,row,index){
                                    	if(row.device){
                                    		return row.device.installPosition;
                                    	}else{
                                    		return '';
										}                                    	
                                    }"></th>
                                    <th	data-options="field:'confirmName',title:'确认人',width:'120px',align:'center'"></th>
                                    <th	data-options="field:'confirmDate',title:'确认时间',width:'120px',align:'center',formatter:function(value,row,index){
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
                         param:'recordId:id'
                     }">
						<div title="设备分布图" data-options="id:'tab0',iconCls:'fa fa-th'"
							id="tab0" style="margin-top: 10px; overflow: auto;"></div>
						<div title="保养人员" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'lubrication',
                                   initCreate: false,
                                   fitColumns:true,
                                   singleSelect:true,
                                   url:'maintenanceUser/queryMaintenanceUserByMaintenancePlanRecordId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'code',title:'员工代码',width:'200px',align:'center'"></th>
										<th
											data-options="field:'name',title:'员工姓名',width:'200px',align:'center'"></th>
										<th
											data-options="field:'orderType',title:'接单类型',width:'200px',align:'center'"></th>
										<th
											data-options="field:'dispatchDate',title:'派单时间',width:'200px',align:'center',
											formatter:function(value,row,index){
												if(value){
													var date = new Date(value);
													return getDateTime(date);
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'receiptDate',title:'接单时间',width:'200px',align:'center',
											formatter:function(value,row,index){
												if(value){
													var date = new Date(value);
													return getDateTime(date);
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'completeDate',title:'完成时间',width:'200px',align:'center',
											formatter:function(value,row,index){
												if(value){
													var date = new Date(value);
													return getDateTime(date);
												}else{
													return '';
												}
											}"></th>
											<th
											data-options="field:'maintenanceTime',title:'保养用时',width:'200px',align:'center'"></th>
											<th
											data-options="field:'occupyTime',title:'占用工时',width:'200px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="保养项目" data-options="id:'tab1',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'spotCheck',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                               url:'maintenanceItem/queryMaintenanceItemByMaintenancePlanRecordId.do',
                               onLoadSuccess:function(){
                               	$('input[type=checkbox]').click(function(){
                               		var confirmDate = $(this).attr('confirmDate');
                               		var isChecked = $(this)[0].checked;
                               		var maintenanceItemId = $(this).attr('id');
                               		//没有确认，可以随便修改结果
                               		if(!confirmDate){
                               			if(isChecked){
		                               			$.get('maintenanceItem/setMaintenanceDate.do',{id:maintenanceItemId},function(){
		                               				//$('#spotCheck').iDatagrid('reload');
		                               			});
		                               		}else{
		                               			$.get('maintenanceItem/setMaintenanceDate2Null.do',{id:maintenanceItemId},function(){
		                               				//$('#spotCheck').iDatagrid('reload');
		                               			});
		                               		}
		                               		return ;
                               		}
                               	});
                               }">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'recordTypeName',title:'保养类别',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'code',title:'项目代码',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'name',title:'保养项目',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'standard',title:'标准',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'method',title:'方法',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'frequency',title:'频次',width:50,sortable:false,width:'200px',align:'center'"></th>
										<!-- <th
											data-options="field:'confirmUser',title:'确认人',width:50,sortable:false,width:'200px',align:'center'"></th>
										<th
											data-options="field:'confirmDate',title:'确认时间',width:50,sortable:false,width:'200px',align:'center',
											formatter:function(value,row,index){
												if(value){
													var date = new Date(value);
													return getDateTime(date);
												}else{
													return '';
												}
											}"></th> -->
										<th
											data-options="field:'maintenanceDate',title:'结果',width:50,width:'200px',align:'center',
											formatter:function(value,row,index){
												var div = $('<div>');
												var $result = $('<input type=checkbox name=result />');
												div.append($result);
												$result.attr('id',row.id);
												$result.attr('confirmDate',row.confirmDate);
												if(value){
													$result.attr('checked','checked');
												}else{
													$result.removeAttr('checked');
												}
												
												if(row.confirmDate){
													$result.attr('disabled',true);
												}
												return div.html();
											}"></th>
											<th
											data-options="field:'remarks',title:'备注',width:50,sortable:false,width:'200px',align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
						<div title="备件信息" data-options="id:'tab3',iconCls:'fa fa-th'">
							<table data-toggle="topjui-datagrid"
								data-options="id:'spareparts',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'maintenanceSparepart/queryMaintenanceSparepartByMaintenancePlanRecordId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',width:'80px',hidden:true"></th>
										<th
											data-options="field:'code',title:'备件代码',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'name',title:'备件名称',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'unitType',title:'规格型号',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'mnemonicCode',title:'助记码',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'batchNumber',title:'批号',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'graphNumber',title:'图号',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'manufacturer',title:'厂商',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'measurementUnit',title:'计量单位',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'count',title:'数量',width:'120px',align:'center',sortable:false"></th>
										<th
											data-options="field:'note',title:'备注',width:'150px',align:'center',sortable:false"></th>
										<th
											data-options="field:'useDate',title:'耗用日期',width:'150px',align:'center',formatter:function(value,row,index){
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
						<div title="流程记录" data-options="id:'tab4',iconCls:'fa fa-th'">
						</div>
						<div title="文档资料" data-options="id:'tab5',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'relatedDoc/queryDocsByRelatedIdAndTypeCode.do?moduleCode=MAINTENANCEDOC'">
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
	<!-- 设备保养记录表格工具栏开始 -->
	<div id="deviceDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'deviceDg'
       }">
<sec:authorize access="hasAuthority('COMPLETE_MAINTENANCEPLAN')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-square-o',
       url:'maintenancePlanRecord/updateMaintenancePlanRecordStatusById.do?status=待确认',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'}">保养完成</a>
</sec:authorize>
<sec:authorize access="hasAuthority('CONFIRM_MAINTENANCEPLAN')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-check',
       url:'maintenancePlanRecord/updateMaintenancePlanRecordStatusById.do?status=已完成',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'}">确认</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REJECT_MAINTENANCEPLAN')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-reply',
       url:'maintenancePlanRecord/updateMaintenancePlanRecordConfirmAndReword.do?status=REWORK',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'},
       reload:[
        {type:'datagrid',id:'deviceDg'}
    ]">驳回</a>
</sec:authorize>
       <div></div>
      	<div style="margin-right:20px;"> 过滤条件:<input type="text" id="condition" name="condition" 
      	data-toggle="topjui-textbox" data-options="prompt:'代码、名称、规格型号',width:200"/> 
      	<label style="text-align: center;">日期:</label>
			<input type="text" name="search_from" data-toggle="topjui-datebox"
				id="search_from" style="width:150px;">
			TO <input type="text" name="search_to" data-toggle="topjui-datebox"
				id="search_to" style="width:150px;">
      	保养类别:<input id="maintainType" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="maintainType"
					data-options="width:200,valueField:'code',textField:'name',url:'maintenanceType/queryAllMaintenanceType.do'">
      	保养状态:<input id="maintainStatus" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="maintainStatus"
					data-options="width:200,valueField:'text',textField:'text',
					data:[
					{text:'待派单'},
					{text:'待接单'},
					{text:'保养中'},
					{text:'待确认'},
					{text:'已完成'},
					{text:'未完成'},
					]">
		责任人:<input type="text" id="employeeName" name="employeeName" 
      	data-toggle="topjui-textbox" data-options="prompt:'责任人',width:200"/>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadRecord()">搜索</a>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-ban'" 
				onclick="resetSearch()">重置</a>
      	</div>
	</div>
	<!-- 备品备件表格工具栏开始 -->
	<div id="spareparts-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'spareparts'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCEPLAN_MAINTENANCESPAREPART')">
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
           width: 600,
           height: 800,
           href:'console/jsp/deviceManagement/maintenance_sparepart_add.jsp',
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
           			$.get('maintenanceSparepart/addMaintenanceSpareparts.do',{
          			maintenancePlanRecordId:$('#deviceDg').iDatagrid('getSelected').id,
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
<sec:authorize access="hasAuthority('EDIT_MAINTENANCEPLAN_MAINTENANCESPAREPART')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#spareparts-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'parameterEditDialog',
                	    width: 600,
                		height: 600,
		           href:'console/jsp/deviceManagement/maintenance_sparepart_edit.jsp',
		           url:'maintenanceSparepart/queryById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			var count = $('#count').val();
           			if(count<=0){
           				alert('备件数量不得少于1');
           				return ;
           			}
           			$.get('maintenanceSparepart/updateMaintenanceSparepart.do',{
           			id:$('#spareparts').iDatagrid('getSelected').id,
           			count:count,
           			useDate:$('#useDate').iDatetimebox('getValue'),
           			note:$('#note').val()
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
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCEPLAN_MAINTENANCESPAREPART')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#spareparts-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceSparepart/deleteMaintenanceSparepart.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 保养项目表格工具栏开始 -->
	<div id="spotCheck-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'spotCheck'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCEPLAN_MAINTENANCEITEM')">
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
           href:'console/jsp/deviceManagement/maintenance_maintenanceItem_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               	var ids = $('#projectTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		var recordTypeCode = $('#mmaintainType').iCombobox('getValue');
               	if(!recordTypeCode){
               		alert('请选择保养类别');
               		return false;
               	}
           		if(ids.length<=0){
           			alert('请选择要添加的保养项目!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
                    $.get('maintenanceItem/addMaintenanceItems.do',{
                           maintenancePlanRecordId:$('#deviceDg').iDatagrid('getSelected').id,
                           itemIds:JSON.stringify(idsArray),
                           recordTypeCode:$('#mmaintainType').iCombobox('getValue'),
                           recordTypeName:$('#mmaintainType').iCombobox('getText')
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
<sec:authorize access="hasAuthority('EDIT_MAINTENANCEPLAN_MAINTENANCEITEM')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#spotCheck-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'spotCheckEidtDialog',
                	    width: 600,
                		height:650,
		           href:'console/jsp/deviceManagement/maintenance_maintenanceItem_edit.jsp',
		           url:'maintenanceItem/queryById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			$.get('maintenanceItem/updateMaintenanceItem.do',{
           			id:$('#spotCheck').iDatagrid('getSelected').id,
           			standard:$('#standard').val(),
           			method:$('#method').val(),
           			frequency:$('#frequency').val(),
           			result:$('input:radio:checked').val(),
           			note:$('#note').val(),
           			Remarks:$('#Remarks').val()
           			},function(data){
           				if(data.success){
	           				$('#spotCheck').iDatagrid('reload');
                            $('#spotCheckEidtDialog').iDialog('close');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#spotCheckEidtDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCEPLAN_MAINTENANCEITEM')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#spotCheck-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceItem/deleteMaintenanceItem.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'spotCheck',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('CONFIRM_MAINTENANCEPLAN_MAINTENANCEITEM')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" onclick='confirm()'>确认</a>
</sec:authorize>
	</div>
	<!-- 保养人员表格工具栏开始 -->
	<div id="lubrication-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'lubrication'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCEPLAN_MAINTENANCESTAFF')">
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
           href:'console/jsp/deviceManagement/maintenance_employee_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
                    $.get('maintenanceUser/addMaintenanceUser.do',{
                           orderType:$('#orderType').iCombobox('getValue'),
                           code:$('#employee').iCombogrid('getValue'),
                           name:$('#employee').iCombogrid('getText'),
                           'maintenancePlanRecord.id':$('#deviceDg').iDatagrid('getSelected').id
                    },function(data){
                           if(data.success){
                               $('#lubrication').iDatagrid('reload');
                               $('#lubricationAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#lubricationAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCEPLAN_MAINTENANCESTAFF')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#lubrication-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'lubricationUpdateDialog',
                	    width: 600,
                		height:650,
		           href:'console/jsp/deviceManagement/maintenance_employee_edit.jsp',
		           url:'maintenanceUser/queryMaintenanceUserById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           	var maintenancePlanRecordId = $('#deviceDg').iDatagrid('getSelected');
	           		if(maintenancePlanRecordId==null){
	           			alert('请选中报修单!');
	           			return ;
	           		}
           			$.get('maintenanceUser/updateMaintenanceUser.do',{
           			id:$('#lubrication').iDatagrid('getSelected').id,
           			orderType:$('#orderType').iCombobox('getValue'),
                           code:$('#code').iCombogrid('getValue'),
                           name:$('#code').iCombogrid('getText'),
                           occupyTime:$('#occupyTime').val(),
                           'maintenancePlanRecord.id':maintenancePlanRecordId.id
           			},function(data){
           				if(data.success){
	           				$('#lubrication').iDatagrid('reload');
                            $('#lubricationUpdateDialog').iDialog('close');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#lubricationUpdateDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ACCEPT_MAINTENANCEPLAN_MAINTENANCESTAFF')">
            <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#lubrication-toolbar',
       iconCls:'fa fa-chain',
       url:'maintenancePlanRecord/updateMaintenancePlanRecordStatusById.do?status=待接单',
       grid: {uncheckedMsg:'请先勾选要操作的数据',param:'id:id'},
       reload:[
        {type:'datagrid',id:'deviceDg'}
    	]">接单</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCEPLAN_MAINTENANCESTAFF')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#lubrication-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceUser/deleteMaintenanceUser.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'lubrication',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 润滑项目表格工具栏结束 -->
	<!-- 保养项目表格工具栏开始 -->
	<div id="maintain-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'maintain'
       }">

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
           width: 600,
           height: 400,
           href:'console/jsp/deviceManagement/device_maintain_add.jsp',
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
                           type:'MAINTAIN'
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
                           name:$('#name').combobox('getText'),
                           standard:$('#standard').val(),
                           method:$('#method').val(),
                           frequency:$('#frequency').val()
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
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#maintain-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceProjectRecord/deleteDeviceProjectRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'maintain',param:'id:id'}">删除</a>
	</div>
	<!-- 文档资料表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
<sec:authorize access="hasAuthority('UPLOAD_MAINTENANCEPLAN_DOC')">
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
           href:'console/jsp/deviceManagement/maintenance_doc_upload.jsp',
           buttons:[
               {text:'上传',handler:function(){
               var file = $('#file').val();
               if(!file){
               	$.iMessager.alert('提示','请选择要上传的文件!');
					  return false;
               }
               
               	 $.ajaxFileUpload({
                url: 'relatedDoc/upload.do', 
                type: 'post',
                data: {
                		'relatedDocumentType.id':$('#docTypeId').val(),
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
<sec:authorize access="hasAuthority('DEL_MAINTENANCEPLAN_DOC')">
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
<sec:authorize access="hasAuthority('DOWNLOAD_MAINTENANCEPLAN_DOC')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
	</div>
</body>
</html>