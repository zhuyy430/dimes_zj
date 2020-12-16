<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript">
$(function(){
	
	//搜索按钮点击事件
		  $.get('deviceRepairOrderCount/queryDeviceRepairCount.do', function(result){
			  console.log(result.newAlarms);
				$("#newalarms").text(result.newAlarms);
				$("#repaircompleted").text(result.repairCompleted);
				$("#outstandingalarms").text(result.outstandingAlarms);
			  });
	
	$('#deviceDg').iDatagrid({
	    rowStyler: function(index,row){
	        if (row.status=='WAITINGASSIGN'){//等待派单
	        	if(new Date().getTime()-row.createDate>30*60*1000){
		            return 'background-color:#FF000E;'; // 返回内联样式
	        	}
		            return 'background-color:#E076AF;'; // 返回内联样式
	        }else if(row.status=='WAITINCOMFIRM'){//等待接单确认
	            return 'background-color:#FFFF33;'; // 返回内联样式
	        }else if(row.status=='MAINTAINING'){//维修中
	            return 'background-color:#00FF00;'; // 返回内联样式
	        }else if(row.status=='WORKSHOPCOMFIRM'){//车间确认
	            return 'background-color:#00FFFF;'; // 返回内联样式
	        }else if(row.status=='MAINTAINCOMPLETE'){//维修完成
	            return 'background-color:#00FFFF;'; // 返回内联样式
	        }else if(row.status=='WAITWORKSHOPCOMFIRM'){//待车间确认
	            return 'background-color:#00BFA6;'; // 返回内联样式
	        }else{
	            //return 'background-color:#FF000E;'; // 返回内联样式
	        }
	    }
	});
	
	$('#dg').iDatagrid({
	    url:'maintenanceStaff/queryAllMaintenanceStaffByStatus.do?status=ONDUTY',
	    pagination:false,
	    rownumbers:false,
	    fitColumns:true,
	    columns:[[
	        {field:'queue',title:'派单顺序',width:100},
	        {field:'name',title:'名称',width:100},
	        {field:'workStatus',title:'在岗状态',width:100,formatter : function(value, row, index) {
	        	if (value=='ONDUTY') {
                    return '在岗';
                 }else if(value=='REST'){
                 	return '休息';
                 }else if(value=='NIGHTSHIFT'){
                 	return '夜班';
                 }else if(value=='MAINTENANCE'){
                 	return '保养';
                 }else if(value=='MAINTAIN'){
                 	return '维修';
                 }else{
                 	return '';
                 }
			}},
	    ]],
	    rowStyler: function(index,row){
	    	if (row.workStatus=='ONDUTY'){
	             // 该函数可以返回预定义的css类和内联样式
	             // rowStyle是一个已经定义了的ClassName(类名)。内联样式优先级大于rowStyle中的样式
	            
	        }else{
	        	return 'color:#FF000E;';
	        }
	    }
	});
	$('#dg2').iDatagrid({
	    url:'maintenanceStaff/queryAllMaintenanceStaffByStatus.do?status=REST',
	    pagination:false,
	    rownumbers:false,
	    fitColumns:true,
	    columns:[[
	        {field:'name',title:'夜班人员',width:100},
	        {field:'workStatus',title:'在岗状态',width:100,formatter : function(value, row, index) {
	        	if (value=='ONDUTY') {
                    return '在岗';
                 }else if(value=='REST'){
                 	return '休息';
                 }else if(value=='NIGHTSHIFT'){
                 	return '夜班';
                 }else if(value=='MAINTENANCE'){
                 	return '保养';
                 }else if(value=='MAINTAIN'){
                 	return '维修';
                 }else{
                 	return '';
                 }
			}},
	    ]]
	});
	
	/* setInterval(function(){  //table reload
		$('#deviceDg').datagrid('reload')
		},5*1000) */
		
		setTimeout(function(){location.reload()},60*1000); //指定30秒刷新一次
	
})
</script>
</head>
<body>
<input type="hidden" id="ids" />
    <div data-toggle="topjui-layout" data-options="fit:true">
        <div
            data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
            <div data-toggle="topjui-layout" data-options="fit:true">
                <div
                    data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                    style="height: 50%;">
                    <!-- datagrid表格 -->
                    <table data-toggle="topjui-datagrid"
                        data-options="id:'deviceDg',
                       url:'deviceRepairOrder/queryDeviceRepairOrder.do',
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
                                       
                               },
                               onLoadSuccess:function(){
                                       $('#deviceSite').iDatagrid('reload');
                                       $('#parameter').iDatagrid('reload');
                               }
                               ">
                        <thead>
                           <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                <th data-options="field:'serialNumber',title:'单据编号',width:'150px',align:'center',sortable:false"></th>
                                <th
                                    data-options="field:'createDate',title:'报修时间',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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
                                }"></th>
                                
                                <th data-options="field:'deviceCode',title:'设备代码',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.code;
	                                    }
                                    }"></th>
                                <th data-options="field:'deviceName',title:'设备名称',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.name;
	                                    }
                                    }"></th>
                                <th
                                    data-options="field:'unitType',width:'150px',align:'center',title:'规格型号',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.unitType;
	                                    }
                                    }"></th>
                                <th data-options="field:'projectTypeName',title:'设备类别',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	if(row.device.projectType){
	                                    		return row.device.projectType.name
	                                    	}
	                                    }
                                    }"></th>
                               
                                <th data-options="field:'batchNumber',title:'故障类型',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.pressLight) {
	                                    	return row.pressLight.reason;
	                                    }
                                    }"></th>
								<th data-options="field:'ngDescription',title:'故障描述',width:'200px',align:'center',sortable:false"></th>
								
								 <th data-options="field:'maintainName',width:'150px',align:'center',title:'维修人员',sortable:false"></th>
								 
								  <th data-options="field:'status',width:'150px',align:'center',title:'维修状态',sortable:false,formatter:function(value,row,index){
	                                    if (row.status=='WAITINGASSIGN'){//等待派单
									            return '等待派单'; 
									        }else if(row.status=='WAITINCOMFIRM'){//等待接单确认
									            return '等待接单确认';
									        }else if(row.status=='MAINTAINING'){//维修中
									            return '维修中';
									        }else if(row.status=='WORKSHOPCOMFIRM'){//车间确认
									            return '车间确认'; 
									        }else if(row.status=='MAINTAINCOMPLETE'){//维修完成
									            return '维修完成'; 
									        }else if(row.status=='WAITWORKSHOPCOMFIRM'){//待车间确认
									            return '待车间确认'; 
									        }else{
									            return ''; 
									        }
                                    }"></th>
								
								 <th
                                    data-options="field:'productCount',title:'安装位置',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.installPosition;
	                                    }
                                    }"></th>
                                    
                                 <th data-options="field:'informant',width:'150px',align:'center',title:'报修人员',sortable:false"></th>
                                 
                                <th data-options="field:'productionUnit',width:'150px',align:'center',title:'生产单元',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	if(row.device.productionUnit){
	                                    		return row.device.productionUnit.name
	                                    	}
	                                    }
                                    }"></th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div data-options="region:'south',fit:false,split:true,border:false"
                    style="height: 50%">
                    <div>
                       <div data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="float: left;width: 50%;height: 300px;margin-left: 45px;margin-top: 20px;border:1px solid #000">
							<div>
								<font size="6" >维修人员在岗状态</font>
							</div>
								<div style="width: 45%;height: 200px;float: left;margin: 10px">
									<table id="dg"></table>
								</div>
								<div style="width: 45%;height: 200px;float: right;margin: 10px">
									<table id="dg2"></table>
								</div>
							</div>
							<div style="float: right;width: 30%;height: 300px;margin-right: 45px;margin-top: 20px;border:1px solid #000">
								<div style="margin: 30px ;">
									<font size="6" >今日新增报警数</font>
									<span style="float: right;color: red;font-size: 40px">次</span>
									<span style="float: right;color: red;font-size: 40px" id="newalarms">0</span>
								</div>
								<div style="margin: 30px;">
									<font size="6" >维修处理完成</font>
									<span style="float: right;color: green;font-size: 40px">次</span>
									<span style="float: right;color: green;font-size: 40px" id="repaircompleted">0</span>
								</div>
								<div style="margin: 30px;">
									<font size="6" >未处理完成报警数</font>
									<span style="float: right;color: red;font-size: 40px">次</span>
									<span style="float: right;color: red;font-size: 40px"  id="outstandingalarms">0</span>
								</div>
							</div>
						</div>
                </div> 
            </div>
        </div>
    </div>
</div>
</body>
</html>