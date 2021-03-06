<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript">
	//搜索点检记录
	function searchData(){
		$("#departmentDg").iDatagrid("reload",{
			deviceCode:$("#departmentTg").iTreegrid("getSelected").code,
			search_from:$("#search_from").val(),
			search_to:$("#search_to").val(),
			search_class:$("#search_class").val(),
			search_status:$("#search_status").val()
		});
	}
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table id="departmentTg" data-toggle="topjui-treegrid"
				data-options="id:'departmentTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   expandAll:false,
			   expandRoots:true,
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryProductionUnitDeviceTree.do?module=deviceManage',
			   childGrid:{
			   	   param:'deviceCode:code',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   },onBeforeExpand: function (row) {
			   		if(row){
			   			$(this).iTreegrid('options').url='productionUnit/queryProductionUnitDeviceTree.do?module=deviceManage&parentId=' + row.id;
			   		}
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'设备信息'"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<!-- datagrid表格 -->
				<table data-toggle="topjui-datagrid"
					data-options="id:'departmentDg',
	                       url:'checkingPlanRecord/queryCheckingPlanRecordByDeviceCode.do',
	                       singleSelect:false,
	                       fitColumns:true,
	                       pagination:true,
	                       parentGrid:{
	                       	type:'treegird',
	                       	id:'departmentTg'
	                       }">
					<thead>
						<tr>
							<th
								data-options="field:'id',title:'id',checkbox:true,width:'80px'"></th>
							<th
								data-options="field:'checkingDate',title:'日期',width:'180px',align:'center',formatter:function(value,row,index){
                        		if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                       /* var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        */
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
							<th
								data-options="field:'classCode',title:'班次编码',width:'180px',align:'center',sortable:false"></th>
							<th
								data-options="field:'className',title:'班次名称',width:'180px',align:'center',sortable:false"></th>
							<th
								data-options="field:'status',title:'计划状态',width:'180px',align:'center',sortable:false"></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<!-- 部门表格工具栏开始 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('ADD_CHECKINGPLAN')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg',
       	param:'deviceCodes:code,deviceName:name,unitType'
       },
       dialog:{
           id:'classesAddDialog',
           width:800,
           height:400,
           href:'console/jsp/deviceManagement/checkingPlan_add.jsp',
           buttons:[
           	{text:'生成计划',handler:function(){
           			var  from = $('#from').val();
           			var to = $('#to').val();
           			if(!from || !to){
           				$.iMessager.alert('警告','请选择日期区间!');
           				return false;
           			}
           			
           			var fromDate = new Date(from.replace(/-/g,'/'));
           			var toDate = new Date(to.replace(/-/g,'/'));
           			if(fromDate.getTime()>toDate.getTime()){
           				$.iMessager.alert('警告','开始日期不能大于结束日期!');
           				return false;
           			}
           			//点检周期类型
           			var cycleType = $('input[type=radio]:checked').val();
           			var value = '';
           			var startDate =null;
           			switch(cycleType){
           				case 'forClass': {
	           				var classesCode = $('#classesType').iCombobox('getValue');
			               	if(!classesCode){
			               	$.iMessager.alert('提示','请选择班次!');
			               		return false;
	               			}
           					break;
           				}
           				case 'forDay': break;
           				case 'forWeek':value=$('#forWeekValue').val(); break;
           				case 'forMonth': value=$('#forMonthValue').val();break;
           				case 'forDuration':{
           					var forDuration = $('#forDurationValue').val();
           					if(!forDuration){
           						$.iMessager.alert('警告','请输入间隔时间!');
           						return false;
           					}else{
           						startDate = $('#startDate').val();
           						if(startDate){
           							var startDateTime = new Date(startDate.replace(/-/g,'/'));
           							if(fromDate.getTime()>startDateTime.getTime()){
           								$.iMessager.alert('警告','起始时间不能小于区间日期的开始时间!');
           								return false;
           							}
           							
           							if(startDateTime.getTime()>toDate.getTime()){
           								$.iMessager.alert('警告','起始时间不能大于区间日期的结束时间!');
           								return false;
           							}
           						}
           						value=forDuration;
           					}
           				 	break;
           				 }
           			}
           			//设备代码
           			var deviceCodes = $('#deviceCodes').val();
           			if(!deviceCodes){
           				$.iMessager.alert('警告','请选择设备信息!');
           				return false;
           			}
           			$.get('checkingPlan/generateCheckingPlan.do',{
           			from:from,
           			to:to,
           			cycleType:cycleType,
           			value:value,
           			startDate:startDate,
           			deviceCodes:deviceCodes,
           			classesCode:classesCode
           			},function(data){
           				if(data.success){
	           				$('#classesAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'重置',handler:function(){
           		$('#addDeviceForm')[0].reset();
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#classesAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_CHECKINGPLAN')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'checkingPlanRecord/deleteCheckingPlanRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'ids:id'}">删除</a>
</sec:authorize>
		<form  id="search_form">
			<label style="text-align: center;">日期</label>
			<input type="text" name="search_from" data-toggle="topjui-datebox"
				id="search_from" style="width:150px;">
			TO <input type="text" name="search_to" data-toggle="topjui-datebox"
				id="search_to" style="width:150px;">
			<label style="text-align: center;">班次</label>
			<input type="text" data-toggle="topjui-textbox" id="search_class" name="search_class" 
			data-options="width:150,prompt:'班次编码或名称'" style="width:150px;"/>
			<label style="text-align: center;">状态</label>
			<input type="text" data-toggle="topjui-combobox"  style="width:150px;" id="search_status" name="search_status" 
			data-options="valueField:'text',textField:'text',data:[{text:'','selected':true},
			{text:'计划'},{text:'完成'},{text:'未完成'}]"/>
			<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',plain:false"
			style="width:100px;" onclick="searchData()">搜索</a>
			<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',plain:false"
			style="width:100px;" onclick="$('#search_form')[0].reset();searchData();">重置</a>
		</form>
	</div>
</body>
</html>