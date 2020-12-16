<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	//点击产线，查询该产线的oee
    	function requestLostTime(deviceId,date){
			$.get("deviceOperationalRecord/queryDeviceOperationalRecordForDeverId.do",{deviceId:deviceId,date:date},function(result){
				lostTimeReasonGraph(result);
    		});
    	}
    	//损时分析
    	function lostTimeReasonGraph(result){
    		var series = new Array();
    		for(var i = 0;i<result.classes.length;i++){
    			var jsonObj =  {
    		            name:result.classes[i].name,
    		            type:'bar',
    		            stack:'班次',
    		            data:result.lostTimeList[i]
    		        }
    			
    			series[i] = jsonObj;
    		}
    		
    		series.push({
				name : '目标',
				type : 'line',
				itemStyle : { 
					normal : {
						color:'#008000',
					}
				},
				lineStyle:{
					width:5
				},
				data : result.goalLostTimeList
			});
    		
    		option = {
    			    tooltip : {
    			        trigger: 'axis',
    			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
    			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    			        }
    			    },
    			    legend: {
    			        data:result.classes
    			    },
    			    grid: {
    			        left: '3%',
    			        right: '4%',
    			        bottom: '3%',
    			        containLabel: true
    			    },
    			    xAxis : [
    			        {
    			            type : 'category',
    			            data : result.days
    			        }
    			    ],
    			    yAxis : [
    			        {
    			            type : 'value'
    			        }
    			    ],
    			    series : series
    			};
    		
    		// 基于准备好的dom，初始化echarts实例
    		var myChart = echarts.init(document.getElementById('lostTimeReasonGraphBody'));
    		// 使用刚指定的配置项和数据显示图表。
    		myChart.setOption(option); 
    	}
    	
    	$(function(){
    	//月份查询条件组件初始化
		$('#qdate')
				.datebox(
						{
							onChange : function(date) {
								var device=$('#departmentTg').iTreegrid('getSelected');
						        if(device==null || ''===$.trim(device)){
			           				//$.iMessager.alert('提示','请选择设备');
			           				return false;
			           			}
						        var deviceId=$('#departmentTg').iTreegrid('getSelected').id;
					           	var date =   $('#qdate').val();
					          	requestLostTime(deviceId,date);
							},
							//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
							onShowPanel : function() {
								//触发click事件弹出月份层
								span.trigger('click');
								if (!tds)
									//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
									setTimeout(
											function() {
												tds = p
														.find('div.calendar-menu-month-inner td');
												tds
														.click(function(e) {
															//禁止冒泡执行easyui给月份绑定的事件
															e.stopPropagation();
															//得到年份
															var year = /\d{4}/
																	.exec(span
																			.html())[0],
															//月份
															//之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1; 
															month = parseInt($(
																	this).attr(
																	'abbr'), 10);

															//隐藏日期对象                     
															$('#qdate')
																	.datebox(
																			'hidePanel')
																	//设置日期的值
																	.datebox(
																			'setValue',
																			year
																					+ '-'
																					+ month);
														});
											}, 0);
							},
							//配置parser，返回选择的日期
							parser : function(s) {
								if (!s)
									return new Date();
								var arr = s.split('-');
								return new Date(parseInt(arr[0], 10), parseInt(
										arr[1], 10) - 1, 1);
							},
							//配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth()); 
							formatter : function(d) {
								var currentMonth = (d.getMonth() + 1);
								var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth)
										: (currentMonth + '');
								return d.getFullYear() + '-' + currentMonthStr;
							}
						});

		//日期选择对象
		var p = $('#qdate').datebox('panel'),
		//日期选择对象中月份
		tds = false,
		//显示月份层的触发控件
		span = p.find('span.calendar-text');
		var curr_time = new Date();

		//设置前当月
		$("#qdate").datebox("setValue",
				curr_time.getFullYear() + "-" + (curr_time.getMonth() + 1));
    	});
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
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryProductionUnitDeviceTree.do?module=deviceManage',
			   childGrid:{
			   	   param:'deviceId:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   },onSelect:function(index,row){
						           		 deviceId=$('#departmentTg').iTreegrid('getSelected').id;
				           	date =   $('#qdate').val();
				          	requestLostTime(deviceId,date);
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
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<div  data-toggle="topjui-tabs"
                        data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     singleSelect:true,
                       fitColumns:true,
                       pagination:true
                    ">
                    <div title="运行记录">
						<!-- datagrid表格 -->
						<table data-toggle="topjui-datagrid"
							data-options="id:'departmentDg',
	                       url:'deviceOperationalRecord/queryDeviceOperationalRecordByDeviceId.do',
	                       singleSelect:true,
	                       fitColumns:true,
	                       pagination:true,
	                       parentGrid:{
	                       	type:'treegird',
	                       	id:'departmentTg'
	                       },
	                       onSelect:function(index,row){
						           		switchButton('workpieceTypeSwitchBtn',row.disabled);
						           }">
							<thead>
								<tr>
									<th
										data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
									<th
										data-options="field:'date',title:'日期',width:'180px',align:'center',formatter:function(value,row,index){
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
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
									<th
										data-options="field:'classesName',title:'班次',width:'180px',align:'center',sortable:false, 
										 formatter:function(value,row,index){
				                        	if(row.classes){
				                        		return row.classes.name;
				                        	}else{
				                        		return '';
				                        	}
				                        }"></th>
									<th
										data-options="field:'note',title:'班次时间(分钟)',width:'180px',align:'center',sortable:false,
										formatter:function(value,row,index){
				                        	if(row.classes){
				                        		return Math.abs((row.classes.startTime-row.classes.endTime)/1000/60);
				                        	}else{
				                        		return '';
				                        	}
				                       	}"></th>
									<th
										data-options="field:'sumTime',title:'累计开机时间',width:'180px',align:'center',sortable:false"></th>
	                        		<th
										data-options="field:'runTime',title:'运行时间',width:'180px',align:'center',sortable:false"></th>
	                        		<th
										data-options="field:'ngTime',title:'故障停机时间',width:'180px',align:'center',sortable:false"></th>
	                        		<th
										data-options="field:'informant',title:'填报人',width:'180px',align:'center',sortable:false"></th>
	                        		<th
										data-options="field:'createDate',title:'填报时间',width:'180px',align:'center',sortable:false,formatter:function(value,row,index){
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
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                                        				' ' + hourStr + ':' + minuteStr + ':' + secondStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                       			 }"></th>
								</tr>
							</thead>
						</table>
						</div>
                    <div title="记录报表">
						<div style="width: 50%;margin:auto auto;" >
					        <input name="date" id="qdate" type="text" style="width: 150px;"	editable="false" data-options="required:true,width:200,showSeconds:true,
					          fitColumns:true,
	                       pagination:true,
					         parentGrid:{
	                       	type:'treegird',
	                       	id:'departmentTg'
	                       },
	                       onSelect:function(index,row){
						           	device=$('#departmentTg').iTreegrid('getSelected');
					        if(device==null || ''===$.trim(device)){
		           				$.iMessager.alert('提示','请选择设备');
		           				return false;
		           			}
					        deviceId=$('#departmentTg').iTreegrid('getSelected').id;
				           	date =   $('#qdate').val();
				          	requestLostTime(deviceId,date);
						    }
					        ">
						</div>
							<div id="lostTimeReasonGraphBody" style="width: 1800px; height: 800px;"></div>
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
<sec:authorize access="hasAuthority('ADD_DEVICERUNRECORD')">
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
           height:400,
           href:'console/jsp/deviceManagement/deviceOperationalRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			$.get('deviceOperationalRecord/addDeviceOperationalRecord.do',{
           			date:$('#date').val(),
           			'classes.id':$('#classesName').val(),
           			'device.id':$('#departmentTg').iTreegrid('getSelected').id,
           			runTime:$('#runTime').val(),
           			ngTime:$('#ngTime').val(),
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
         <%--   <sec:authorize access="hasAuthority('EDIT_WORKPIECETYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/deviceManagement/deviceOperationalRecord_edit.jsp',
                url:'deviceOperationalRecord/queryDeviceOperationalRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var classes = $('#classesName').val();
           			if(classes==null || ''===$.trim(classes)){
           				$.iMessager.alert('提示','请选择班次');
           				return false;
           			}
           			$.get('deviceOperationalRecord/updateDeviceOperationalRecord.do',{
           			id:$('#departmentDg').iTreegrid('getSelected').id,
           			date:$('#date').val(),
           			'classes.id':$('#classesName').val(),
           			'device.id':$('#departmentTg').iTreegrid('getSelected').id,
           			runTime:$('#runTime').val(),
           			ngTime:$('#ngTime').val(),
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
            <sec:authorize access="hasAuthority('DEL_WORKPIECETYPE')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'deviceOperationalRecord/deleteDeviceOperationalRecordById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
       </sec:authorize> --%>
	</div>
</body>
</html>