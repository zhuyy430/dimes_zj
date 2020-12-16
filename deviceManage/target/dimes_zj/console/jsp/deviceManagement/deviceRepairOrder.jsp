<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
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
                        data-options="id:'productionUnitDg',
                       url:'deviceRepairOrder/queryDeviceRepairOrder.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
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
                                <th data-options="field:'informant',width:'150px',align:'center',title:'报修人员',sortable:false"></th>
                                <th data-options="field:'productionUnit',width:'150px',align:'center',title:'生产单元',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	if(row.device.productionUnit){
	                                    		return row.device.productionUnit.name
	                                    	}
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
                                <th
                                    data-options="field:'productCount',title:'安装位置',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.installPosition;
	                                    }
                                    }"></th>
                                <th data-options="field:'batchNumber',title:'故障原因',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.ngreason) {
	                                    	return row.ngreason.name;
	                                    }else{return ''}
                                    }"></th>
								<th data-options="field:'ngDescription',title:'故障描述',width:'200px',align:'center',sortable:false"></th>
								<th data-options="field:'failSafeOperation',width:'150px',align:'center',title:'带病运行',sortable:false,formatter:function(value,row,index){
	                                    if (row.failSafeOperation) {
	                                    	return '是'
	                                    }else{
	                                    	return '否'
	                                    }
                                    }"></th>
                                <th data-options="field:'closed',width:'150px',align:'center',title:'是否关闭',sortable:false,formatter:function(value,row,index){
	                                    if (row.closed) {
	                                    	return '是'
	                                    }else{
	                                    	return '否'
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
<sec:authorize access="hasAuthority('ADD_DEVICEREPAIR')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'productionUnitAddDialog',
           width:1600,
           height:800,
           href:'console/jsp/deviceManagement/deviceRepairOrder_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               var code = $('#code').val();
                       $.get('deviceRepairOrder/addDeviceRepairOrder.do',{
                       serialNumber:$('#serialNumber').val(),
                       createDate:$('#createDate').val(),
                       'device.id':$('#deviceId').val(),
                       'projectType.id':$('#deviceTypeId').val(),
                       Informant:$('#Informant').val(),
                       'productionUnit.id':$('#productionUnitId').val(),
                       'ngreason.id':$('#pressLightId').val(),
                       ngDescription:$('#ngDescription').val(),
                       idList:$('#ids').val(),
                       originalFailSafeOperationNo:$('#ofsoNo').val()
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
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_DEVICEREPAIR')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method: 'openDialog',
            extend: '#productionUnitDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'productionUnitEditDialog',
                width:1600,
                   height:800,
                href: 'console/jsp/deviceManagement/deviceRepairOrder_edit.jsp',
                url:'deviceRepairOrder/queryDeviceRepairOrderById.do?id={id}',
                 buttons:[
               {text:'保存',handler:function(){
					$.get('deviceRepairOrder/updateDeviceRepairOrder.do',{
           				id:$('#productionUnitDg').iDatagrid('getSelected').id,
           				serialNumber:$('#serialNumber').val(),
                        createDate:$('#createDate').val(),
                        'device.id':$('#deviceId').val(),
                        'projectType.id':$('#projectTypeId').val(),
                        'ngreason.id':$('#pressLightId').val(),
                        Informant:$('#informant').val(),
                        'productionUnit.id':$('#productionUnitId').val(),
                        'ngType.id':$('#ngTypeId').val(),
                        ngDescription:$('#ngDescription').val()
                        },function(data){
                           if(data.success){
                               $('#productionUnitEditDialog').iDialog('close');
                               $('#productionUnitDg').iDatagrid('reload');
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
<sec:authorize access="hasAuthority('DEL_DEVICEREPAIR')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceRepairOrder/deleteDeviceRepairOrderById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'productionUnitDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitDg').iDatagrid('reload');}">删除</a>
</sec:authorize>
    </div>
</body>
</html>