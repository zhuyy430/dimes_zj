<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
<script type="text/javascript">
	var weeks = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	//星期对应的数值
	var weekValues = new Array(0, 1, 2, 3, 4, 5, 6);
	//显示班次信息
	function showClassInfo(obj) {
		$(obj).tooltip("show");
	}
	//显示设备点检记录
	function showRecord(param) {
		var plan = 0;
		var complete = 0;
		var uncomplete = 0;
		//获取请求数据
		$.get("checkingPlanRecord/queryCheckingPlanRecordByDeviceCodeAndMonth.do",
						param,
						function(data) {
							//当月的日期集合
							var days = data.days;
							//设备点检记录
							var records = data.records;
							//显示点检记录的table
							var $calendarTable = $("#calendarTable");
							$calendarTable.empty();
							//第一行：星期
							var $week = $("<tr>");
							for (var i = 0; i < weeks.length; i++) {
								var $weekTd = $("<td>");
								$weekTd.append(weeks[i]);
								$week.append($weekTd);
							}
							//将星期行放入table中
							$calendarTable.append($week);
							//获取当月的第一天
							var day1 = days[0];
							//当月的第一天的星期值:java从1开始 ，js从0开始表示周日
							var weekValue = new Date(day1).getDay();
							//为年月div赋值
							$("#yearDiv").text(new Date(day1).getFullYear());
							$("#monthDiv").text(
									new Date(day1).getMonth() + 1 + " 月");

							var $tr = $("<tr>");
							//遍历日期数组，生成点检数据表格
							for (var i = 0, j = 0; j < days.length; i++) {
								//填充当月第一天前面的星期
								if (i < weekValue) {
									$tr.append("<td style='border:0px;'></td>");
								} else {
									var currDay = new Date(days[j]);
									//另起一行
									if (i % 7 == 0) {
										$calendarTable.append($tr);
										$tr = $("<tr>");
									}
									var $td = $("<td>");
									$tr.append($td);
									var $dayDiv = $("<div class='dayDiv'>");
									var $statusDiv = $("<div class='statusDiv'>");
									$td.append($dayDiv);
									$td.append($statusDiv);
									//为状态div填充值
									if (records && records.length > 0) {
										plan = records.length;
										for (var recordIndex = 0; recordIndex < records.length; recordIndex++) {
											var record = records[recordIndex];
											var checkingDate = new Date(
													record.checkingDate);
											if (checkingDate.getFullYear() == currDay
													.getFullYear()
													&& checkingDate.getMonth() == currDay
															.getMonth()
													&& checkingDate.getDate() == currDay
															.getDate()) {
												if (record.classCode) {
													var $classDiv = $("<div class='classDiv' classCode='"
															+ record.classCode
															+ "'"
															+ "className='"
															+ record.className
															+ "'"
															+ "id='"
															+ record.id
															+ "' onclick='showCheckingDetail(this)'"
															+ " onmouseover='showClassInfo(this)'>")
													switch (record.status) {
													case '未完成':
														$classDiv
																.css(
																		"background-color",
																		"red");
														uncomplete++;
														break;
													case '已完成':
														$classDiv
																.css(
																		"background-color",
																		"#DDDDE1");
														complete++;
														break;
													case '计划':
														$classDiv
																.css(
																		"background-color",
																		"#A3E2DE");
														break;
													}
													$classDiv
															.append(record.status);
													$statusDiv
															.append($classDiv);
													$classDiv
															.tooltip({
																position : 'right',
																content : '<span>'
																		+ record.className
																		+ ":"
																		+ record.classCode
																		+ '</span>'
															});
												} else {
													var $status = $("<div class='status' id='"+record.id+"' onclick='showCheckingDetail(this)'>")
													$status
															.append(record.status);
													$statusDiv.append($status);
													switch (record.status) {
													case '未完成':
														$statusDiv
																.css(
																		"background-color",
																		"red");
														uncomplete++;
														break;
													case '已完成':
														$statusDiv
																.css(
																		"background-color",
																		"#DDDDE1");
														complete++;
														break;
													case '计划':
														$statusDiv
																.css(
																		"background-color",
																		"#A3E2DE");
														break;
													}
												}
											}
										}
									}
									$dayDiv.append(new Date(days[j]).getDate());
									j++;
								}
							}
							$calendarTable.append($tr);

							$("#plan").text(plan);
							$("#complete").text(complete);
							$("#uncomplete").text(uncomplete);
						});
	}
	//搜索
	function searchData() {
		var device = $('#departmentTg').iTreegrid('getSelected');
		if(!device){
			alert("请先选择设备!");
			return ;
		}
		showRecord({
			deviceCode : device.code,
			month : $("#month").datebox("getValue")
		});
	}
	//向页面填充点检记录信息
	function setCheckingPlanRecord(record){
		$("#id").val(record.id);
		$("#deviceCode").iTextbox("setValue",record.deviceCode);
		$("#deviceName").iTextbox("setValue",record.deviceName);
		$("#unitType").iTextbox("setValue",record.unitType);
		if(!record.checkedDate){
			$("#checkDate").iDatetimebox("setValue",getDateTime(new Date()));
		}else{
			$("#checkDate").iDatetimebox("setValue",getDateTime(new Date(record.checkedDate)));
		}
	}
	//填充文档信息(设备点检图片)
	function setDocs(docs){
	/* 	if(docs && docs.length>0){
			var docsDiv = $("#docs");
			for(var i = 0;i<docs.length;i++){
				if(i%2==0){
					var leftDiv = $("<div class='leftDiv' style='height:240px;width:48%;float:left; margin:5px;border-radius: 10px;'>");
					var leftImg = $("<img  src='" + docs[i].url + "' style='width:100%;height:100%;border-radius: 10px;'/>");
					leftDiv.append(leftImg);
					docsDiv.append(leftDiv);
				}else{
					var rightDiv = $("<div class='rightDiv' style='height:240px;width:48%;float:right;margin:5px;border-radius: 10px;'>");
					var rightImg = $("<img  src='" + docs[i].url + "' style='width:100%;height:100%;border-radius: 10px;'/>");
					rightDiv.append(rightImg);
					docsDiv.append(rightDiv);
				}
			}
		} */
		 //文档索引
	    var index = 0;
	    //显示图片的img对象
	     var img = $("#deviceCheckImg");
	    if(docs!=null && docs.length>0){
	        //默认显示第0张图片
	        img.attr("src",docs[index].url);
	    }
	}
	//显示点检项目标准信息
	function setProjectRecords(projectRecords){
		var tbody = $("#checkingPlanRecordItem");
		tbody.empty();
		if(projectRecords && projectRecords.length>0){
			for(var i = 0;i<projectRecords.length;i++){
				var record = projectRecords[i];
				var tr = $("<tr>");
				//序号
				var serNo = $("<td>");
				serNo.append(i+1);
				tr.append(serNo);
				//项目代码
				var code = $("<td>");
				code.append(record.code);
				tr.append(code);
				//点检项目名称和id列
				var nameAndId = $("<td>"); 
				nameAndId.append("<input type='hidden' class='ids' value='" + record.id + "' />");
				nameAndId.append(record.name);
				
				tr.append(nameAndId);
				//标准列
				var standard = $("<td style='width:100px;'>");
				standard.append(record.standard);
				tr.append(standard);
				//方法列
				var method = $("<td style='width:100px;'>");
				method.append(record.method);
				tr.append(method);
				
				// 频次列
				var frequency = $("<td style='width:100px;'>");
				frequency.append(record.frequency);
				tr.append(frequency);
				//结果列
				var result = $("<td>");
				var combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;'><option selected='selected'>OK</option><option>NG</option></select>");
				if(record.result){
					if(record.result=='NG'){
						combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;'><option>OK</option><option selected='selected'>NG</option></select>");;
					}
				}
				result.append(combobox);
				tr.append(result);
				
				//备注列
				var note =$("<td>");
				var recordNote=" ";
				if(record.note!=null){
					recordNote=record.note;
				}
				note.append("<input type='text' class='noteValue' value='" + recordNote + "' />");
				tr.append(note);
				tbody.append(tr);
			}
		}
	}
	//显示点检详情
	function showCheckingDetail(obj) {
		var id = $(obj).attr("id");
		 $("#checkingPlanRecordId").val(id);
		/*  $('#checkDialog').dialog("open");  */
		
		var params = {title:"设备点检作业",href:"console/jsp/deviceManagement/checkingDevice_check.jsp?id="+id}; 
		addParentTab(params);
	}
	var _docs ;
	var index = 0;
	var init = "on";
	$(function() {
		//点检设备弹出框
		$('#checkDialog').dialog({
			title : '设备点检',
			width : 1500,
			height : 800,
			closed : true,
			cache : false,
			href : 'console/jsp/deviceManagement/checkingDevice_check.jsp',
			modal : true,
			buttons:[{
			    text:'保存',
			    width:100,
			    handler:function(){
			    	//获取所有图片的url
			    	var imgs = $("#docs img");
			    	//存储图片url
			    	var urls = "";
			    	if(imgs && imgs.length>0){
			    		for(var i = 0;i<imgs.length;i++){
			    			urls+=$(imgs[i]).attr("src") + ",";
			    		}
			    		urls = urls.substring(0,urls.length-1);
			    	}
			    	//获取点检项的id数组，以逗号间隔
			    	var idsObj = $(".ids");
			    	var ids = "";
			    	if(idsObj && idsObj.length>0){
			    		for(var i = 0;i<idsObj.length;i++){
			    			ids+=$(idsObj[i]).attr("value")+",";
			    		}
			    		ids = ids.substring(0,ids.length-1);
			    	}
			    	//获取点检项的结果数组
			    	var resultObj = $(".resultValue");
			    	var results = "";
			    	
			    	if(resultObj && resultObj.length>0){
			    		for(var i = 0;i<resultObj.length;i++){
			    			results += $(resultObj[i]).val() + ",";
			    		}
			    		results = results.substring(0,results.length-1);
			    	}
			    	
			    	var noteObj=$(".noteValue");
			    	var notes = "";
			    	if(noteObj && noteObj.length>0){
			    		for(var i = 0;i<noteObj.length;i++){
			    			notes+=$(noteObj[i]).val()+",@";
			    		}
			    		notes =notes.substring(0,notes.length-2);
			    	} 
			    	//发送异步请求
			    	$.get("checkingPlanRecord/deviceCheck.do",{
			    		id:$("#id").val(),
			    		checkedDate:$("#checkDate").iDatetimebox("getValue"),
			    		employeeName:$("#checkUsername").iTextbox("getValue"),
			    		employeeCode:$("#checkUsercode").val(),
			    		picPath:urls,
			    		itemIds:ids,
			    		results:results,
			    		notes:notes
			    		
			    	},function(result){
			    		$.iMessager.alert("提示","已点检");
			    		$('#checkDialog').dialog("close");
			    		showRecord({
			    			deviceCode : $('#departmentTg').iTreegrid('getSelected').code,
			    			month : $("#month").datebox("getValue")
			    		});
			    	});
			    }
			},{
			    text:'取消',
			    width:100,
			    handler:function(){
			    	$('#checkDialog').dialog("close");
			    }
			}],
			onLoad:function(){
				$.get("checkingPlanRecord/queryCheckingPlanRecordById.do",{id:$("#checkingPlanRecordId").val()},function(result){
					//设备点检记录对象
					var record = result.record;
					//点检人对象
				 	var checkUsername = result.checkUsername;
					var checkUsercode = result.checkUsercode;
					$("#checkUsername").iTextbox("setValue",checkUsername);
					$("#checkUsercode").val(checkUsercode);
					//关联文档集合
					var docs = result.docs;
					//点检项目标准记录
					var projectRecords = result.projectRecords;
					setCheckingPlanRecord(record);
					_docs = docs;
					setDocs(docs);
					setProjectRecords(projectRecords); 
				});
			}
		});
		
		var nowDate = new Date();
		$("#yearDiv").text(nowDate.getFullYear());
		$("#monthDiv").text(nowDate.getMonth() + 1 + " 月");

		var device = $('#departmentTg').iTreegrid('getSelected');
		if (!device) {
			showRecord();
		} else {
			showRecord({
				deviceCode : device.code
			});
		}
		
		//月份查询条件组件初始化
		$('#month')
				.datebox(
						{
							onChange : function(date) {
								if(init=="off"){
									searchData();
								}
								init = "off";
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
															$('#month')
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
		var p = $('#month').datebox('panel'),
		//日期选择对象中月份
		tds = false,
		//显示月份层的触发控件
		span = p.find('span.calendar-text');
		var curr_time = new Date();

		//设置前当月
		$("#month").datebox("setValue",
				curr_time.getFullYear() + "-" + (curr_time.getMonth() + 1));
	});
	
	
</script>
<style type="text/css">
#calendarTable {
	width: 800px;
	height: 500px;
	margin-left: 20px;
}
/*星期样式*/
#calendarTable tr:nth-child(1) {
	background-color: #257670;
	color: white;
	height: 30px;
}

#calendarTable tr td {
	text-align: center;
}

#checkingPlanRecordItemTb tr td{
	text-align:center;
	font-size:14px;
}

.statusDiv {
	height: 60px;
	line-height: 60px;
}

.statusDiv div {
	vertical-align: middle;
	margin: auto 0;
	cursor: pointer;
}

#calendarTable tr td div {
	margin: 2px 0;
}

.dayDiv {
	font-weight: bold;
	vertical-align: top;
	font-size: 20px;
}

.classDiv {
	cursor: pointer;
}

.dateDiv {
	margin-top: 5px;
	height: 60px;
	width: 80px;
	background-color: #257670;
	color: white;
	line-height: 60px;
	text-align: center;
	font-size: 20px;
	font-weight: bold;
	float: left;
	margin-left: 20px;
	height: 60px;
}
</style>
</head>
<body>
	<input type="hidden" id="checkingPlanRecordId" />
	<div id="checkDialog" style="background-color:#1C2437;"></div>
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
			   onSelect:function(param){
			   	showRecord({deviceCode:param.code});
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
				<form style="margin-top: 5px; margin-left: 20px;" id="search_form">
					<input type="text" name="month" id="month" style="width: 150px;"
						editable="false">
					<!-- <a href="javascript:void(0)"
						data-toggle="topjui-menubutton"
						data-options="iconCls:'fa fa-search'" onclick="searchData()"></a> -->
				</form>
				<div>
					<div id="yearDiv" class="dateDiv"></div>
					<div id="monthDiv" class="dateDiv"
						style="margin-left: 0px; background-color: white; color: #257670;"></div>
				</div>
				<table id="calendarTable" border="1" style="clear: both;"></table>
				<div style="margin-left: 20px; font-size: 20px; margin-top: 30px;">
					本月计划点检 <span id="plan"></span> 次,已完成 <span id="complete"></span>
					次，到期未完成 <span id="uncomplete"></span> 次.
				</div>
			</div>
		</div>
	</div>
</body>
</html>