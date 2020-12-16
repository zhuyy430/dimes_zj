<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
</head>
<script type="text/javascript">
function checkChange(){
	console.log($('#record').is(':checked'));
	$('#productionUnitDg').iDatagrid("reload",'TransferRecordController/queryTransferRecord.do?status='+$('#record').is(':checked'));
	
}
</script>
<body>
    <div data-toggle="topjui-layout" data-options="fit:true">
        <div
            data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
            <div data-toggle="topjui-layout" data-options="fit:true">
                <div
                    data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                    style="height: 60%;">
                    <!-- datagrid表格 console.log($(('#record').is(':checked')));-->
                    <table data-toggle="topjui-datagrid"
                        data-options="id:'productionUnitDg',
                       url:'TransferRecordController/queryTransferRecord.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       ">
                        <thead>
                           <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                <th data-options="field:'transferDate',title:'转单时间',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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
                                <th
                                    data-options="field:'transferCode',title:'转单员工代码',width:'150px',align:'center',sortable:false"></th>
                                <th data-options="field:'transferName',width:'150px',align:'center',title:'转单员工姓名',sortable:false"></th>
                                <th data-options="field:'reason',width:'150px',align:'center',title:'转单原因',sortable:false"></th>
                                
                                <th data-options="field:'acceptCode',title:'新接单员工代码',width:'150px',align:'center',sortable:false"></th>
                                <th data-options="field:'acceptName',title:'新接单员工姓名',width:'150px',align:'center',sortable:false"></th>
                                <th
                                    data-options="field:'serialNumber',width:'150px',align:'center',title:'单据编号',sortable:false,formatter:function(value,row,index){
	                                    if (row.deviceRepair) {
	                                    	return row.deviceRepair.serialNumber;
	                                    }
                                    }"></th>
                                <th data-options="field:'createDate',title:'报修时间',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.deviceRepair) {
	                                    var date = new Date(row.deviceRepair.createDate);
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
	                                    }
                                    }"></th>
                                <th
                                    data-options="field:'DeviceLevel',title:'设备等级',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.deviceRepair) {
	                                    	if(row.deviceRepair.deviceLevel){
	                                    		return row.deviceRepair.deviceLevel.name;
	                                    	}
	                                    }
                                    }"></th>
                                <th data-options="field:'deviceCode',title:'设备代码',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    	if(row.deviceRepair.device){
	                                    		return row.deviceRepair.device.code;
	                                    	}
	                                    }
                                    }"></th>
								<th data-options="field:'deviceName',title:'设备名称',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    	if(row.deviceRepair.device){
	                                    		return row.deviceRepair.device.name;
	                                    	}
	                                    }
                                    }"></th>
								<th data-options="field:'unitType',title:'规格型号',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    	if(row.deviceRepair.device){
	                                    		return row.deviceRepair.device.unitType;
	                                    	}
	                                    }
                                    }"></th>
								<th data-options="field:'ngDescription',title:'故障类型',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    	if(row.deviceRepair.ngreason){
	                                    		return row.deviceRepair.ngreason.name;
	                                    	}
	                                    }
                                    }"></th>
								<th data-options="field:'ngDescription',title:'故障描述',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    		return row.deviceRepair.ngDescription;
	                                    }
                                    }"></th>
								<th data-options="field:'productionUnit',title:'生产单元',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                   		if(row.deviceRepair.productionUnit){
	                                    		return row.deviceRepair.productionUnit.name;
	                                    		}
	                                    }
                                    }"></th>
								<th data-options="field:'maintainName',title:'报修人员',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                   if (row.deviceRepair) {
	                                    		return row.deviceRepair.maintainName;
	                                    }
                                    }"></th>
								<th data-options="field:'status',title:'维修状态',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.deviceRepair.status=='WAITINGASSIGN'){//等待派单
									            return '等待派单'; 
									        }else if(row.deviceRepair.status=='WAITINCOMFIRM'){//等待接单确认
									            return '等待接单确认';
									        }else if(row.deviceRepair.status=='MAINTAINING'){//维修中
									            return '维修中';
									        }else if(row.deviceRepair.status=='WORKSHOPCOMFIRM'){//车间确认
									            return '车间确认'; 
									        }else if(row.deviceRepair.status=='MAINTAINCOMPLETE'){//维修完成
									            return '维修完成'; 
									        }else if(row.deviceRepair.status=='WAITWORKSHOPCOMFIRM'){//待车间确认
									            return '待车间确认'; 
									        }else if(row.deviceRepair.status=='FAIL_SAFEOPERATION'){//待车间确认
									            return '带病运行'; 
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
    <!-- 生产单元表格工具栏开始 -->
    <div id="productionUnitDg-toolbar" class="topjui-toolbar"
        data-options="grid:{
           type:'datagrid',
           id:'productionUnitDg'
       }">
<sec:authorize access="hasAuthority('DISPATCH_TRANSFERCENTER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'productionUnitAddDialog',
           width:600,
           height:600,
           href:'console/jsp/deviceManagement/MaintenanceTransferCenter_assign.jsp',
           buttons:[
               {text:'保存',handler:function(){
              		var ids = $('#employeeTab').iDatagrid('getSelected');
	           		if(!ids){
	           			alert('请选择要添加的数据!');
	           			$('#productionUnitAddDialog').iDialog('close');
	           			return;
	           		}
	           		var id = ids.id;
	           		
	           		var status=$('#productionUnitDg').iDatagrid('getSelected').status
	           		if(status){
	           			alert('该数据已经派单，请重新选择!');
	           			$('#productionUnitAddDialog').iDialog('close');
	           			return;
	           		}
	           		
           			$.get('TransferRecordController/assignTransferRecordRecord.do',{
           			employeeIds:id,
           			transId:$('#productionUnitDg').iDatagrid('getSelected').id,
           			},function(data){
           				if(data.success){
	           				$('#productionUnitAddDialog').iDialog('close');
	           				$('#productionUnitDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#productionUnitAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">派单</a>
</sec:authorize>
           <span style="float: right;margin-right: 20px;margin-top: 7px;height:10px;line-height:10px;text-align: center;"><input id="record" type="checkbox" value="" onclick="checkChange()" style="margin-right:5px;vertical-align:middle;  "/>
           		<span style="line-height:100%;font-size:14px;vertical-align:middle;  ">历史记录</span> </span> 
    </div>
</body>
</html>