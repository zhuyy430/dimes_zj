<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
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
 			var newWin = window.open("console/jsp/device_print.jsp"); 
		}else{
			alert("请选择要打印二维码的记录!");
			return false;
		}
	} 
</script>
</head>
<body>
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
                       url:'device/queryDevicesByProductionUnitId.do?module=dimes',
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
			            onSelect:function(index,row){
					           		switchButton('deviceSwitchBtn',row.disabled);
					           },
					           onLoadSuccess:function(){
					           		$('#deviceSite').iDatagrid('reload',{deviceSiteId:''});
					           		$('#parameter').iDatagrid('reload',{deviceSiteId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:true,width:'80px'"></th>
								<th
									data-options="field:'code',title:'设备代码',width:'100px',align:'center'"></th>
								<th data-options="field:'name',title:'设备名称',width:'100px',align:'center'"></th>
								<th data-options="field:'unitType',title:'规格型号',width:'100px',align:'center'"></th>
								<th
									data-options="field:'status',title:'设备状态',width:'100px',align:'center',formatter:function(value,row,index){
									if(value){
										switch(value){
											case '0':return '运行';
											case '1':return '待机';
											case '2':return '停机';
										}
									}else{
										return '';
									}
								}"></th>
								<th
									data-options="field:'manufacturer',title:'出产厂家',width:'100px',align:'center'"></th>
								<th data-options="field:'trader',title:'经销商',width:'100px',align:'center'"></th>
								<th
									data-options="field:'installDate',title:'安装日期',width:'100px',align:'center',formatter:function(value,row,index){
                        	if(value){
                        		var date = new Date(value);
                        		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                        	}else{
                        		return '';
                        	}
                        }"></th>
								<th
									data-options="field:'outFactoryDate',title:'出厂日期',width:'100px',align:'center',formatter:function(value,row,index){
                        	if(value){
                        		var date = new Date(value);
                        		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                        	}else{
                        		return '';
                        	}
                        }"></th>
								<th
									data-options="field:'outFactoryCode',title:'出厂编号',width:'100px',align:'center'"></th>
								<th
									data-options="field:'installPosition',title:'安装位置',width:'100px',align:'center'"></th>
								<th
									data-options="field:'parameterValueType',title:'参数取值',width:'100px',align:'center'"></th>
								<th
									data-options="field:'disabled',title:'停用',width:'100px',align:'center',formatter:function(value,row,index){
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
                         id:'deviceDg',
                         param:'deviceId:id'
                     }">
						<div title="设备站点" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'deviceSite',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'deviceSite/queryDeviceSitesByDeviceId.do',
						       onSelect:function(index,row){
					           		switchButton('deviceSiteSwitchBtn',row.disabled);
					           }">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
										<th data-options="field:'code',title:'站点代码',width:'100px',align:'center'"></th>
										<th data-options="field:'name',title:'站点名称',width:'100px',align:'center'"></th>
										<th
											data-options="field:'barCodeAddress',title:'条码读头地址',width:'100px',align:'center'"></th>
										<th
											data-options="field:'status',title:'状态',width:'100px',align:'center',formatter:function(value,row,index){
											if(value){
										switch(value){
											case '0':return '运行';
											case '1':return '待机';
											case '2':return '停机';
										}
									}else{
										return '';
									}
										}"></th>
										<th data-options="field:'note',title:'备注',width:'100px',align:'center'"></th>
										<th data-options="field:'goalOee',title:' 目标oee',width:'100px',align:'center'"></th>
										<th
									data-options="field:'bottleneck',title:'瓶颈站点',width:'100px',align:'center',formatter:function(value,row,index){
				                        	if(value==true){
				                        		return 'Y';
				                        	}else{
				                        		return 'N';
				                        	}
			                        }"></th>
										<th
											data-options="field:'disabled',title:'停用',width:'100px',align:'center',formatter:function(value,rows,index){
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
						<div title="设备参数" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'parameter',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'parameter/queryDeviceSiteParameterByDeviceId.do'">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
										<th
											data-options="field:'deviceSiteCode',title:'站点代码',width:'100px',align:'center',formatter:function(value,row,index){
											if(row.deviceSite){
												return row.deviceSite.code;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'deviceSiteName',title:'站点名称',width:'100px',align:'center',formatter:function(value,row,index){
											if(row.deviceSite){
												return row.deviceSite.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterCode',title:'参数代码',width:'100px',align:'center',formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.code;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'parameterName',title:'参数名称',width:'100px',align:'center',formatter:function(value,row,index){
											if(row.parameter){
												return row.parameter.name;
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'upLine',title:'控制线UL',width:'100px',align:'center'"></th>
										<th
											data-options="field:'lowLine',title:'控制线LL',width:'100px',align:'center'"></th>
										<th
											data-options="field:'standardValue',title:'标准值',width:'100px',align:'center'"></th>
										<th
											data-options="field:'trueValue',title:'实际值',width:'100px',align:'center'"></th>
										<th
											data-options="field:'parameterNote',title:'备注',width:'100px',align:'center'"></th>
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
<sec:authorize access="hasAuthority('ADD_DEVICE')">
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
           width:500,
           height:700,
           href:'console/jsp/device_add.jsp',
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
           			
           			$.get('device/addDevice.do',{
           			code:code,
           			name:name,
           			isDimesUse:true,
           			'productionUnit.id':$('#productionUnitTg').iTreegrid('getSelected').id,
           			unitType:$('#unitType').val(),
           			status:$('#deviceStatus').val(),
           			manufacturer:$('#manufacturer').val(),
           			trader:$('#trader').val(),
           			installDate:$('#installDate').val(),
           			outFactoryDate:$('#outFactoryDate').val(),
           			outFactoryCode:$('#outFactoryCode').val(),
           			installPosition:$('#installPosition').val(),
           			photoName:$('#photoName').val(),
           			parameterValueType:$('#parameterValueType').val()
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
<sec:authorize access="hasAuthority('EDIT_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#deviceDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'deviceEditDialog',
                width: 500,
                height: 700,
                href: 'console/jsp/device_edit.jsp',
                url:'device/queryDeviceById.do?id={id}',
                 buttons:[
           		{text:'保存',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入设备名称!');
           				return false;
           			}
           			$.get('device/updateDevice.do',{
           			id:$('#deviceDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			isDimesUse:true,
           			unitType:$('#unitType').val(),
           			status:$('#deviceStatus').val(),
           			manufacturer:$('#manufacturer').val(),
           			'productionUnit.id':$('#deviceDg').iDatagrid('getSelected').productionUnit.id,
           			trader:$('#trader').val(),
           			installDate:$('#installDate').val(),
           			outFactoryDate:$('#outFactoryDate').val(),
           			outFactoryCode:$('#outFactoryCode').val(),
           			installPosition:$('#installPosition').val(),
           			photoName:$('#photoName').val(),
           			parameterValueType:$('#parameterValueType').val()
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
<sec:authorize access="hasAuthority('DEL_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-trash',
       url:'device/deleteDevice.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'deviceDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-stop',
       url:'device/disabledDevice.do',
       grid: {uncheckedMsg:'请选择操作的设备',id:'deviceDg',param:'id:id'}" id="deviceSwitchBtn">停用</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINTQR_DEVICE')">
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
	<!-- 设备站点表格工具栏开始 -->
	<div id="deviceSite-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'deviceSite'
       }">
<sec:authorize access="hasAuthority('ADDDEVICESITE_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#deviceSite-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'deviceDg'
            },
       dialog:{
           id:'deviceSiteAddDialog',
            width:600,
           height:600,
           href:'console/jsp/deviceSite_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入站点代码!');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入站点名称!');
           				return false;
           			}
           			var bottleneck = $('#bottleneck').combobox('getValue');
           			if(bottleneck=='是'){
           				bottleneck=true;
           			}
           			if(bottleneck=='否'){
           				bottleneck=false;
           			}
           			$.get('deviceSite/addDeviceSite.do',{
           			code:code,
           			name:name,
           			show:$('#show').val(),
           			goalOee:$('#goalOee').val(),
           			status:$('#status').val(),
           			'device.id':$('#deviceDg').iDatagrid('getSelected').id,
           			note:$('#note').val(),
           			barCodeAddress:$('#barCodeAddress').val(),
           			bottleneck:bottleneck
           			},function(data){
           				if(data.success){
	           				$('#deviceSiteAddDialog').iDialog('close');
	           				$('#deviceSite').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceSiteAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EIDTDEVICESITE_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#deviceSite-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'deviceSiteEditDialog',
                width: 600,
                height: 600,
                href: 'console/jsp/deviceSite_edit.jsp',
                url:'deviceSite/queryDeviceSiteById.do?id={id}',
                buttons:[ 	{
                 				text:'保存',handler:function(){
           								 var code = $('#code').val();
           								 if(!code){
           								 	$.iMessager.alert('提示','请输入站点代码!');
           								 	return false;
           								 }
					           			var name = $('#name').val();
					           			if(name==null || ''===$.trim(name)){
					           				$.iMessager.alert('提示','请输入站点名称!');
					           				return false;
						           	    }
						           	    
						     var show = $('#show').val();
						     if(show=='是'){
						     	show='true';
						     }
						     if(show=='否'){
						     	show='false';
						     }
						 	var bottleneck = $('#bottleneck').combobox('getValue');
		           			if(bottleneck=='是'){
		           				bottleneck=true;
		           			}
		           			if(bottleneck=='否'){
		           				bottleneck=false;
		           			}
	           			$.get('deviceSite/updateDeviceSite.do',{
		           			id:$('#deviceSite').iDatagrid('getSelected').id,
		           			code:code,
		           			name:name,
		           			show:show,
		           			status:$('#status').val(),
		           			goalOee:$('#goalOee').val(),
		           			'device.id':$('#deviceDg').iDatagrid('getSelected').id,
		           			'barCodeAddress':$('#barCodeAddress').val(),
		           			note:$('#note').val(),
		           			bottleneck:bottleneck
	           			},function(data){
           				if(data.success){
	           				$('#deviceSiteEditDialog').iDialog('close');
	           				$('#deviceSite').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceSiteEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELDEVICESITE_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceSite-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceSite/deleteDeviceSite.do',
       grid: {uncheckedMsg:'请先勾选要移除的数据',id:'deviceSite',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLEDEVICESITE_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceSite-toolbar',
       iconCls:'fa fa-stop',
       url:'deviceSite/disabledDeviceSite.do',
       grid: {uncheckedMsg:'请选择操作的设备站点',id:'deviceSite',param:'id:id'}" id="deviceSiteSwitchBtn">停用</a>
</sec:authorize>
	</div>
	<!-- 设备表格工具栏结束 -->
	<!-- 相关文档表格工具栏开始 -->
	<div id="parameter-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'parameter'
       }">
<sec:authorize access="hasAuthority('ADDPARAMETER_DEVICE')">
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
           href:'console/jsp/device_parameter_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var deviceSiteId = $('#deviceSiteId').val();
           			if(!deviceSiteId){
           				$.iMessager.alert('提示','请选择站点!');
           				return false;
           			}
           			$.get('parameter/addDeviceSiteParameter.do',{
           			'deviceSite.id':deviceSiteId,
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
<sec:authorize access="hasAuthority('EDITPARAMETER_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#parameter-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'parameterEditDialog',
                	    width: 600,
                		height: 700,
		           href:'console/jsp/device_parameter_edit.jsp',
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
<sec:authorize access="hasAuthority('DELPARAMETER_DEVICE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#parameter-toolbar',
       iconCls:'fa fa-trash',
       url:'parameter/deleteDeviceSiteParameterById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
</body>
</html>