<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style type="text/css">
.imageDiv {
	display:inline-block;
	width:370px;
	height:240px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px
}
.cover {
	position:absolute;
	z-index:1;
	top:0;
	left:0;
	width:370px;
	height:240px;
	background-color:rgba(0,0,0,.3);
	display:none;
	line-height:125px;
	text-align:center;
	cursor:pointer;
}
.cover .delbtn {
	color:red;
	font-size:20px;
}
.imageDiv:hover .cover {
	display:block;
}
.addImages {
	display:inline-block;
	width:370px;
	height:240px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px;
}
.text-detail {
	margin-top:40px;
	text-align:center;
}
.text-detail span {
	font-size:40px;
}
.file {
	position:absolute;
	top:0;
	left:0;
	width:370px;
	height:240px;
	opacity:0;
}
.input {
}

</style>
<script>
	$(function() {
		//条码信息
		$('#barCode').iDatagrid({
	    url:'processRecord/queryProcessRecordByWorkSheetId.do',
	    columns:[[
	        {field:'id',title:'id',checkbox:false,hidden:true,width:200},
	        {field:'deviceSiteCode',title:'设备站点代码',width:200},
	        {field:'deviceSiteName',title:'设备站点名称',width:200},
	        {field:'collectionDate',title:'采集时间',width:200,formatter:function(value,row,index){
				if(value){
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
					}
	        },
	        {field:'serialNo',title:'二维码',width:200}
	    ]],
	    queryParams:{
	    	workSheetId:$("#productionUnitDg").iDatagrid("getSelected").id
	    }
	});
		//修改生产单元				
		$("#productionUnitName").iCombotreegrid({
			url : 'productionUnit/queryTopProductionUnits.do',
			idField : 'name',
			treeField : 'name',
			columns : [ [ {
				field : 'name',
				title : '名称',
				width : 300
			} ] ],
			onClickRow : function(row) {
				$("#productionUnitId").val(row.id);
				$("#productionUnitCode").val(row.productionUnitCode);
			}
		});
		// 可编辑工单 详情
		$('#workSheetDetail').iEdatagrid(
				{
					url : 'workSheet/queryWorkSheetDetailByWorkSheetId.do',
					queryParams : {
						workSheetId : $('#productionUnitDg').iDatagrid(
								'getSelected').id
					},
					pagination : false,
					onClickCell : function(rowIndex, field, value) {
						if (field == 'firstReport') {
							$('#parameterDialog').dialog("open");
						}
					}
				});
		$("#pressLight").iTextbox({
		    width:200,
		    buttonIcon:'fa fa-search',
		    onClickButton:function(){
		    	$('#showPressLightsDialog').dialog("open");
		    }
		});
		//故障类型列表
		$('#showPressLightsDialog').dialog({
				    title: '故障原因',
				    width: 700,
				    height: 600,
				    closed: true,
				    cache: false,
				    href: 'console/jsp/deviceManagement/deviceRepairOrder_showPressLight.jsp',
				    modal: true,
				    buttons:[{
				        text:'保存',
				        handler:function(){
				         	var pressLight = $('#pressLightTable').iDatagrid('getSelected');
				         	if(!pressLight){
				         		$('#showPressLightsDialog').dialog("close");
				         		return ;
				         	}
			           		$('#pressLight').iTextbox('setValue',pressLight.code);
			           		$('#pressLight').iTextbox('setText',pressLight.name);
			           		$('#pressLightId').val(pressLight.id);
				    		$('#showPressLightsDialog').dialog("close");
				        }
				    },{
				        text:'关闭',
				        handler:function(){
				        	$('#showPressLightsDialog').dialog("close");
				        }
				    }]
				});
		//设备列表
		$('#deviceCode').iCombogrid(
				{
					idField : 'code',
					textField : 'name',
					delay : 500,
					mode : 'remote',
					url : 'device/queryAllDevices.do?module=deviceManage',
					columns : [ [ {
						field : 'id',
						title : 'id',
						width : 60,
						hidden : true
					}, {
						field : 'code',
						title : '代码',
						width : 100
					}, {
						field : 'name',
						title : '设备名称',
						width : 100
					} ] ],
					onClickRow : function(index, row) {
						//alert(JSON.stringify(row.projectType));
						$("#deviceName").textbox("setValue", row.name);
						$("#deviceUnitType").textbox("setValue", row.unitType);
						$("#deviceId").val(row.id);
						if(row.projectType){
							$("#deviceTypeName").textbox("setValue",row.projectType.name);
							$("#deviceTypeId").val(row.projectType.id);
						}else{
							$("#deviceTypeName").textbox("setValue",'');
							$("#deviceTypeId").val('');
						}
						$("#deviceInstallPosition").textbox("setValue", row.installPosition);
					}
				});
		
			 var url ="deviceRepairOrder/queryDeviceRepairOrderById.do?id="+$('#productionUnitDg').iDatagrid('getSelected').id;            
		     $.ajax({
		         type:'POST',
		         url:url,
		         contentType:false,
		         processData:false,
		         dataType:'json',
		         mimeType:"multipart/form-data",
		         success:function(data){
		        	 var picDiv = $("#picDiv");
		        	 //picDiv.empty();
		         	if(data.picName){
		         		var picName = data.picName
		         		  for (var i = 0; i < picName.length; i++) {
		         		var picHtml = "<div class='imageDiv' style='float: left;'> <img  id='img" + i + "' src='"+contextPath+picName[i]+"' style='height: 240px;width: 370px;margin: 10px;' /> </div>";
		         		console.log(picHtml);
		         		picDiv.append(picHtml);
		         		  }
		         	}
		         }
		     });
	});
	function querydevice(id){
		$('#deviceCode').iCombogrid(
				{
					idField : 'code',
					textField : 'name',
					delay : 500,
					mode : 'remote',
					url : 'device/queryAllDevicesByProductionUnitId.do?module=deviceManage&productionUnitId='+id,
					columns : [ [ {
						field : 'id',
						title : 'id',
						width : 60,
						hidden : true
					}, {
						field : 'code',
						title : '代码',
						width : 100
					}, {
						field : 'name',
						title : '设备名称',
						width : 100
					} ] ],
					onClickRow : function(index, row) {
						//alert(JSON.stringify(row.projectType));
						$("#deviceName").textbox("setValue", row.name);
						$("#unitType").textbox("setValue", row.unitType);
						$("#deviceId").val(row.id);
						$("#productionUnitId").val(id);
						if(row.projectType){
							$("#projectType").textbox("setValue",row.projectType.name);
							$("#projectTypeId").val(row.projectType.id);
						}
						$("#installPosition").textbox("setValue", row.installPosition);
					}
				});
	}
</script>
<div id="showPressLightsDialog">
	</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post">
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div
						style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;">
						设备报修单</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">单据编号</label>
							<div class="topjui-input-block">
								<input type="text" name="serialNumber" data-toggle="topjui-textbox"
									data-options="required:true" id="serialNumber" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报修时间</label>
							<div class="topjui-input-block">
								<input type="text" name="createDate" style="width: 100%;"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true,formatter:function(value,row,index){
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
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                    }"
									id="createDate">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报修人员</label>
							<div class="topjui-input-block">
								<input type="text" name="informant"
									data-toggle="topjui-textbox" data-options="required:true"
									id="informant"  readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
									<input data-toggle="topjui-combotreegrid" data-options="width:'100%',required:true,
							        panelWidth:500,
							        url:'',
							        idField:'id',
							        treeField:'name',
							        url:'productionUnit/queryTopProductionUnits.do',
							        columns:[[
							            {field:'id',title:'id',hidden:true},
							            {field:'code',title:'编码',width:200},
							            {field:'name',title:'单元名称',width:200}
							        ]],
							        onSelect: function(rec){
            querydevice(rec.id);
        }"  name="productionUnitName" id="productionUnitName">
								<input type="hidden" name="productionUnitId"
									id="productionUnitId"> 
								<input type="hidden" name="productionUnitCode"
									id="productionUnitCode"> 
							</div>
					</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceCode"
									data-toggle="topjui-textbox" data-options="required:true,"
									id="deviceCode"/>
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceName" data-toggle="topjui-textbox"
									data-options="required:false" id="deviceName" readonly="readonly">
									<input type="text" hidden="hidden" name="deviceId"
									id="deviceId">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceUnitType"
									data-toggle="topjui-textbox" data-options="required:false"
									id="deviceUnitType" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备类别</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceTypeName"
									data-toggle="topjui-textbox" data-options="required:false"
									id="deviceTypeName" readonly="readonly">
								<input type="text" hidden="hidden" name="deviceTypeId"
									id="deviceTypeId">
							</div>
						</div>
					</div>
						<div class="topjui-row">
							<div class="topjui-col-sm3">
								<label class="topjui-form-label">安装位置</label>
								<div class="topjui-input-block">
									<input type="text" name="deviceInstallPosition"
										data-toggle="topjui-textbox" data-options="required:false"
										id="deviceInstallPosition" readonly="readonly">
								</div>
							</div>
							<div class="topjui-col-sm3">
								<label class="topjui-form-label">故障原因</label>
								<div class="topjui-input-block">
									<input type="text" name="pressLight"
										data-toggle="topjui-textbox" data-options="required:false"
										id="pressLight">
										<input type="text" hidden="hidden" name="pressLightId"
									id="pressLightId">
								</div>
							</div>
						</div>
					<div class="topjui-col-sm12">
							<label class="topjui-form-label">故障描述</label>
							<div class="topjui-input-block">
									<input type="text" name="ngDescription"
								data-toggle="topjui-textarea" data-options="required:true" id="ngDescription">
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>


	<div data-options="region:'south',fit:false,border:false"
		style="height: 50%">
		<div data-toggle="topjui-tabs"
			data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
			 <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic">
							<!-- datagrid表格 -->
							<div class="picDiv" id="picDiv">
							<!-- <div class="addImages">
				                <input type="file" class="file" id="fileInput" multiple="" accept="image/png, image/jpeg, image/gif, image/jpg">
				                <div class="text-detail">
				                    <span>+</span>
				                    <p>点击上传</p>
				                </div>
				                <input id="ids" hidden="true"/>
				            </div> -->
      					  </div>
						</div>
		</div>
	</div>
</div>
<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>

<!-- 工具按钮 -->