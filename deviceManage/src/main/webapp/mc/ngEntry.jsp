<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script type="text/javascript">
	$.ajaxSetup({cache:false});
	var employeeID="";
	var employeeName="";
	$(function() {
		//初始化工单表列
		initNGRecordsTable();
		//初始化tab
		initTab();
		//ngReasonCode回车事件
		$("#ngReasonCode").keydown(function(event) {
			if (event.keyCode == 13) {
				setNGInfo();
			}
		});

		//NG代码改变事件
		$("#ngReasonCode").change(function() {
			setNGInfo();
		});
		//扫描NG二维码窗口显示事件
		$('#scanNGQRcodeDialog').on('shown.bs.modal', function(e) {
			initProcesses();
			$('#NGCode').focus();
			$('#NGCode').val("");
			$("#processes button").removeClass("btn btn-primary");
		    $("#processes button").addClass("btn btn-default");
		});
		//扫描员工二维码窗口显示事件
		$('#scanEmployeeCodeDialog').on('shown.bs.modal', function(e) {
			$('#employeeCode').focus();
		});
		//NG二维码扫描框改变事件
		$('#NGCode').change(function() {
			if (!$(this).val()) {
				$("#alertText").text("请输入NG代码！");
				$("#alertDialog").modal();
				return false;
			}
			$('#scanNGQRcodeDialog').modal('hide');
			$("#ngReasonCode").val($(this).val()).change();
		});
		//NG二维码扫描框回车事件
		/* $('#NGCode').keydown(function(event) {
			if (event.keyCode == 13) {
				if (!$(this).val()) {
					$("#alertText").text("请输入NG代码！");
					$("#alertDialog").modal();
					return false;
				}
				$('#scanNGQRcodeDialog').modal('hide');
				$("#ngReasonCode").val($(this).val()).change();
			}
		}); */
		
		//设置所有的下拉框，没有数据时的内容
		  $(".selectpicker").selectpicker({
            noneSelectedText: '请选择'
         });
		  $(".flowSuggestion").selectpicker({
              noneSelectedText: '请选择',
              width:'100%'
           });

		//员工二维码扫描框改变事件
		$('#employeeCode').change(function() {
			$('#scanEmployeeCodeDialog').modal('hide');
			$.get(contextPath + "mcNGRecord/hasThePermission.do",{employeeCode:$(this).val(),operType:ngWorkFlow },function(data){
				if(data.success){
					employeeID=data.employeeID;
					employeeName=data.employeeName;
					$("#suggestion").val("");
					$("#confirmDialog").modal();
				}else{
					$("#alertText").text(data.message);
					$("#alertDialog").modal();
				}
			});
		});
		$('#employeeCode').keydown(function() {
			if (event.keyCode == 13) { 
			$('#scanEmployeeCodeDialog').modal('hide');
			$.get(contextPath + "mcNGRecord/hasThePermission.do",{employeeCode:$(this).val(),operType:ngWorkFlow },function(data){
				if(data.success){
					employeeID=data.employeeID;
					employeeName=data.employeeName;
					$("#suggestion").val("");
					$("#confirmDialog").modal();
				}else{
					$("#alertText").text(data.message);
					$("#alertDialog").modal();
				}
			});
			}
		});
		
		//员工扫码框失去焦点事件
		$("#employeeCode").blur(function(){
			$(this).focus();
		});
		//NG产生时间初始化
		$("#occurDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true
		});
		$("#startTime").datebox({
 			timeSeparator:':',
 			showSeconds:true
		});
 		$("#endTime").datebox({
 			timeSeparator:':',
 			showSeconds:true
		});
	});
	//扫描框失去焦点事件
	function lostfocus() {
		$("#NGCode").focus();
	}
	//设置NG信息:回车或扫码
	function setNGInfo() {
		var ngReasonCode = $("#ngReasonCode").val();
		if (!ngReasonCode) {
			$("#alertText").text("请输入NG代码！");
			$("#alertDialog").modal();
		} else {
			//根据NG代码查询NG原因
			$.get(contextPath + "mcNGRecord/queryNGReasonByCode.do", {
				ngReasonCode : ngReasonCode
			}, function(data) {
				if (data) {
					$("#ngReasonId").val(data.id);
					$("#ngReason").val(data.ngReason);
					var method = "";
					if(data.processingMethod){
						switch (data.processingMethod) {
						case "报废":
							method = "scrap";
							break;
						case "返修":
							method = "repair";
							break;
						case "让步接收":
							method = "compromise";
							break;
						}
						$("#processingMethod").selectpicker("val", method);
					}
				}
			});
		}
	}
	//查询所有设备站点，初始化tab页
	function initTab() {
		$
				.get(
						contextPath + "mcdeviceSite/getAllMCDeviceSite.do",
						function(data) {
							var $ul = $("#myTab");
							if (data.length > 0) {
								var tabeq ;
								var first = true;
								for (var i = 0; i < data.length; i++) {
									var deviceSite = data[i];
									if(deviceSite.bottleneck&&first){
										tabeq=i;
										first=false;
										$("#currentDeviceSiteCode").val(
												deviceSite.deviceSiteCode);
									}
									if (i == 0) {
										$("#currentDeviceSiteCode").val(
												deviceSite.deviceSiteCode);
									}
									var $li = $('<li role="presentation"><a onclick="refreshNGRecordsTable(\''
											+ deviceSite.deviceSiteCode
											+ '\')" href="#showWorkSheetsListPanel" aria-controls="showWorkSheetsListPanel" id="'+deviceSite.deviceSiteCode+'" role="tab" data-toggle="tab">'
											+ deviceSite.deviceSiteName
											+ '</a></li>');
									
									$ul.append($li);
								}
								if(tabeq){
									$('#myTab li:eq('+tabeq+') a').tab('show');
								}else{
									$('#myTab a:first').tab('show');
								}
										refreshNGRecordsTable($("#currentDeviceSiteCode").val())
							}
						});
	}
	//tab切换事件
	function refreshNGRecordsTable(deviceSiteCode) {
		$("#currentDeviceSiteCode").val(deviceSiteCode);
		$("#startTime").datebox('setValue', '');
		$("#endTime").datebox('setValue', '');
		$("#workNo").val("");
		$("#searchNGReasonCode").val("");
		$("#searchBatchNumber").val("");
		$("#showNGRecordListTable").bootstrapTable("refresh", {
			url : contextPath + "mcNGRecord/queryNGRecordsByDate.do",
			cache:false,
			query : {
				deviceSiteCode : deviceSiteCode
			}
		});
	}
	//tab条件查询
	function toolsearch(deviceSiteCode) {
		var timeend= new Date($("#endTime").val().replace(/-/g,"/")).getTime();
        var timestart = new Date($("#startTime").val().replace(/-/g,"/")).getTime();
        if(timeend<timestart){
	 		$("#alertText").text("开始时间大于结束时间，请重新选择！");
			$("#alertDialog").modal();
			return false;
		}
		var deviceSiteCode=$("#currentDeviceSiteCode").val();
		$("#showNGRecordListTable").bootstrapTable("refresh", {
			url : contextPath + "mcNGRecord/queryNGRecordsByDate.do",
			cache:false,
			query : {
				startTime:$("#startTime").val(),
				endTime:$("#endTime").val(),
				workNo:$("#workNo").val(),
				NGCode:$("#searchNGReasonCode").val(),
				BatchNumber:$("#searchBatchNumber").val(),
				deviceSiteCode : deviceSiteCode
			}
		});
	}
	//初始化NG记录列表table
	function initNGRecordsTable() {
		$("#showNGRecordListTable").bootstrapTable(
				{
					height : longTableHeight,
					idField : 'id',
					cache:false,
					singleSelect : false,
					ctrlSelect : true,
					clickToSelect : true,
					striped : true, //隔行换色
					rowStyle : function(row, index) {
						if (!row.auditDate) {
							return {
								css : {
									"background-color" : "#F2F5A9"
								}
							};
						} else {
							return {};
						}
					},
					columns : [ {
								checkbox : true
							},
							// {
							// 	radio : true,
							// 	field : '',
							// 	title : ''
							// },
							{
								field : 'occurDate',
								title : '时间',
								formatter : function(value, row, index) {
									if (value) {
										var date = new Date(value);
										return date.getFullYear() + '-'
												+ (date.getMonth() + 1) + '-'
												+ date.getDate() + " "
												+ date.getHours() + ":"
												+ date.getMinutes() + ":"
												+ date.getSeconds();
									}
								}
							},
							{
								field : 'no',
								title : '工单单号'
							},
							{
								field : 'processName',
								title : '工序名称'
							},
							{
								field : 'workpieceCode',
								title : '工件代码'
							},
							{
								field : 'workpieceName',
								title : '工件名称'
							},
							{
								field : 'unitType',
								title : '规格型号'
							},
							{
								field : 'graphNumber',
								title : '图号'
							},
							{
								field : 'batchNumber',
								title : '批号'
							},
							{
								field : 'ngCount',
								title : '数量'
							},
							{
								field : 'ngReasonCode',
								title : 'NG原因代码'
							},
							{
								field : 'ngReason',
								title : 'NG原因'
							},
							{
								field : 'processingMethod',
								title : '处理方法',
								formatter : function(value, row, index) {
									switch(value){
									case 'scrap':return '报废';
									case 'repair':return '返修';
									case 'compromise':return '让步接收';
									}
									 return value;
								}
							},
							{
								field : 'inputUsername',
								title : '填报人员'
							}
							]
				});
		$("#showNGRecordListTable").on(
				'click-row.bs.table',
				function(e, row, element) {
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
					var $confirmBtn = $("#confirmA");
		    		var $auditBtn = $("#auditA");
		    		var $reviewBtn = $("#reviewA");
		    		
		    		if(row.auditDate){
		    			$auditBtn.removeAttr("onclick");
		    			$auditBtn.removeAttr("href");
		    		}else{
		    			var onclickAttr = $auditBtn.attr("onclick");
		    			if(!onclickAttr){
		    				$auditBtn.attr("onclick","ngWorkFlowOperation('audit')");
		    				$auditBtn.attr("href","javascript:void(0)");
		    			}
		    		}
		    		
		    		if(!row.reviewDate && row.auditDate){
		    			var onclickAttr = $reviewBtn.attr("onclick");
		    			if(!onclickAttr){
		    				$reviewBtn.attr("onclick","ngWorkFlowOperation('review')");
		    				$reviewBtn.attr("href","javascript:void(0)");
		    			}
		    		}else{
		    			$reviewBtn.removeAttr("onclick");
		    			$reviewBtn.removeAttr("href");
		    		}
		    		if(!row.confirmDate && row.reviewDate){
		    			var onclickAttr = $confirmBtn.attr("onclick");
		    			if(!onclickAttr){
		    				$confirmBtn.attr("onclick","ngWorkFlowOperation('confirm')");
		    				$confirmBtn.attr("href","javascript:void(0)");
		    			}
		    		}else{
		    			$confirmBtn.removeAttr("onclick");
		    			$confirmBtn.removeAttr("href");
		    		}
				});
		
	}

	var operationType = "add";
	/**弹出新增或修改ng记录框
	type:
		add:新增
		update:修改
	 */
	function showAddOrUpdateNGRecordDialog(type) {
			//光标ngReasonCode
		switch (type) {
		//新增ng记录
		case "add": {
			operationType = "add";
			initNGRecordForm4Add();
			break;
		}
			//修改ng记录
		case "update": {
			operationType = "update";
			if (initNGRecordForm4Update()) {
				$("#addOrUpdateNGRecordDialog").modal();
			}
			break;
		}
		}
	}
    //显示入库确认界面
	function showInWarehouseDialog() {
        var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要入库的NG记录!");
			$("#alertDialog").modal();
			return false;
		}
				$("#inWarehouseConfirmDialog").modal();
	}
	//NG入库
	function inWarehousr() {
		var selections = $("#showNGRecordListTable").bootstrapTable(
						"getSelections");
			var ids = "";
			for (let i = 0; i < selections.length; i++) {
				var id = selections[i].id;
				ids += id  +",";
			}
			ids = ids.slice(0,-1);
			$.get(contextPath + "mcNGRecord/inWarehouseByNGRecord.do", {
				ids : ids
			}, function(data) {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			})
	}
	//添加ng记录时，初始化form表单
	function initNGRecordForm4Add() {
		$("#ngRecordForm")[0].reset();
		var date = new Date();
		var value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
		$("#occurDate").val(value);
		//根据IP地址查询本机是否有登录用户
		$.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
				mcUser) {
			if (mcUser) {
				$("#inputUsername").val(mcUser.mcUser.employeeName);
			} else {
				$("#alertText").text("请先登录系统！");
				$("#alertDialog").modal();
			}
		});
		//当前站点的加工工单
		$.get(contextPath + "mcWorkSheet/queryProcessingWorkSheets.do", {
			deviceSiteCode : $("#currentDeviceSiteCode").val()
		}, function(data) {
			if (data && data.length > 0) {

                document.getElementById("noButton").style.display="";

				var mcWorkSheet = data[0];
				$("#workpieceCode").val(mcWorkSheet.workPieceCode);
				$("#workpieceName").val(mcWorkSheet.workPieceName);
				$("#graphNumber").val(mcWorkSheet.graphNumber);
				$("#customerGraphNumber").val(mcWorkSheet.customerGraphNumber);
				$("#batchNumber").val(mcWorkSheet.batchNumber);
				$("#stoveNumber").val(mcWorkSheet.stoveNumber);
				$("#processName").val(mcWorkSheet.processName);
				$("#processCode").val(mcWorkSheet.processCode);
				$("#version").val(mcWorkSheet.version);
				$("#no").val(mcWorkSheet.no);
				//工单id
				$.get(contextPath+"mcWorkSheet/queryWorkSheetByNo.do",{
					no:mcWorkSheet.no
					},function(data){
						$("#workSheetId").val(data.id);
					});
				//工件id
				$.get(contextPath+"inventory/queryWorkpiecesByCode.do",{
					code:mcWorkSheet.workPieceCode
				},function(data){
					$("#workpieceId").val(data.code);
				});
				$("#addOrUpdateNGRecordDialog").modal();
				initProcesses();
				setTimeout("$('#ngReasonCode').focus();", 500 )
			}else{
				$("#processCode").val("");
				$("#alertText").text("该站点当前没有加工的工单！");
				$("#alertDialog").modal();
				return false;
			}
		});
	}
	//更新ng记录时，初始化form表单
	function initNGRecordForm4Update() {
		$("#ngRecordForm")[0].reset();
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要修改的NG记录!");
			$("#alertDialog").modal();
			return false;
		}

        if (selections.length > 1) {
            $("#alertText").text("请选择一条NG记录修改!");
            $("#alertDialog").modal();
            return false;
        }

		var selection = selections[0];
		$.get(contextPath + "mcNGRecord/queryNGRecordById.do", {
			id : selection.id
		}, function(data) {
			if (data) {

				document.getElementById("noButton").style.display="none";
				$("#id").val(data.id);
				$("#no").val(data.no);
				$("#deviceSiteCode").val(data.deviceSiteCode);
				var date = new Date(data.occurDate);
				var value = date.getFullYear() + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate() + " " + date.getHours() + ":"
						+ date.getMinutes() + ":" + date.getSeconds();
				$("#occurDate").val(value);
				$("#inputUsername").val(data.inputUsername);
				$("#ngReasonCode").val(data.ngReasonCode);
				$("#ngReason").val(data.ngReason);
				$("#ngReasonId").val(data.ngReasonId);
				$("#workpieceCode").val(data.workpieceCode);
				$("#workpieceName").val(data.workpieceName);
				$("#graphNumber").val(data.graphNumber);
				$("#customerGraphNumber").val(data.customerGraphNumber);
				$("#batchNumber").val(data.batchNumber);
				$("#stoveNumber").val(data.stoveNumber);
				$("#processName").val(data.processName);
				$("#version").val(data.version);
				$("#ngCount").val(data.ngCount);
				$("#processCode").val(data.processCode);
				var method = "";
				switch (data.processingMethod) {
				case "报废":
					method = "scrap";
					break;
				case "返修":
					method = "repair";
					break;
				case "让步接收":
					method = "compromise";
					break;
				default:
					method = data.processingMethod;
				}
				$("#processingMethod").selectpicker("val", method);
				initProcesses();
			}
		});

		return true;
	}

	//添加或更新ng记录
	function addOrUpdateNGRecord() {
		$("#deviceSiteCode").val($("#currentDeviceSiteCode").val());
		if(operationType=="add"){
			var formData = $("#ngRecordForm").serialize();
			$.get(
							contextPath + "mcNGRecord/addNGRecord.do",
							formData,
							function(data) {
								if (data.success) {
									$("#showNGRecordListTable")
											.bootstrapTable(
													"refresh",
													{
														url : contextPath
																+ "mcNGRecord/queryNGRecordsByDate.do",
																cache:false,
														query : {
															deviceSiteCode : $(
																	"#currentDeviceSiteCode")
																	.val()
														}
													});
								}
							});
		}
			//更新ng记录
		if(operationType=="update"){
			$.get(
							contextPath + "mcNGRecord/updateNGRecord.do",
							$("#ngRecordForm").serialize(),
							function(data) {
								if (data.success) {
									$("#showNGRecordListTable")
											.bootstrapTable(
													"refresh",
													{
														url : contextPath
																+ "mcNGRecord/queryNGRecordsByDate.do",
																cache:false,
														query : {
															deviceSiteCode : $(
																	"#currentDeviceSiteCode")
																	.val()
														}
													});
								}else{
								    $("#alertText").text(data.msg);
                                    $("#alertDialog").modal();
								}
							});
		}
		
	}
	//显示扫描NG条码窗口
	function showScanNGQRCodeDialog() {
		$('#scanNGQRcodeDialog').modal();
		$("#NGCode").val("");
	}
	//删除NG记录
	function deleteNGRecord() {
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要删除的NG记录!");
			$("#alertDialog").modal();
			return false;
		}

		//弹出删除确认框
		$("#deleteConfirmDialog").modal();
	}
	//删除ng记录
	function deleteNgRecord() {
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		var ids = "";

        for (let i = 0; i < selections.length; i++) {
            var id = selections[i].id;
            ids += id  +",";
        }
        ids = ids.slice(0,-1);

        $.get(contextPath + "mcNGRecord/deleteNGRecord.do", {
			ids : ids
		}, function(data) {
			if (data.success) {
				$("#showNGRecordListTable").bootstrapTable("refresh", {
					url : contextPath + "mcNGRecord/queryNGRecordsByDate.do",
					cache:false,
					query : {
						deviceSiteCode : $("#currentDeviceSiteCode").val()
					}
				});
			} else {
				$("#alertText").text(data.message);
				$("#alertDialog").modal();
			}
		});
	}
	
	var ngWorkFlow = "";
	//审核、复核、确认弹出框
	function ngWorkFlowOperation(workFlowType) {
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要操作的NG记录!");
			$("#alertDialog").modal();
			return false;
		}
		ngWorkFlow = workFlowType;
		flowSuggestion(workFlowType);
		$("#employeeCode").val("");
		$("#scanEmployeeCodeDialog").modal();
	}
	//执行审核、复核和确认的方法
	function ngWorkFlowGo(){
		var selections = $("#showNGRecordListTable").bootstrapTable("getSelections");
		var selection = selections[0];
		var url = "";
		var auditorId=null;
		var auditorName=null;
		var reviewerId=null;
		var reviewerName=null;
		var confirmUserId=null;
		var confirmUsername=null;
		switch(ngWorkFlow){
		//审核
		case 'audit':{
			url = contextPath + "mcNGRecord/auditNGRecord.do";
			auditorId=employeeID;
			auditorName=employeeName;
			break;
		}
		//复核
		case 'review':{
			url = contextPath + "mcNGRecord/reviewNGRecord.do";
			reviewerId=employeeID;
			reviewerName=employeeName;
			break;
		}
		//确认
		case 'confirm':{
			url = contextPath + "mcNGRecord/confirmNGRecord.do";
			confirmUserId=employeeID;
			confirmUsername=employeeName;
			break;
		}
		}
		
		$.get(url,{
			id:selection.id,
			suggestion:$("#suggestion").val(),
			auditorId:auditorId,
			auditorName:auditorName,
			reviewerId:reviewerId,
			reviewerName:reviewerName,
			confirmUserId:confirmUserId,
			confirmUsername:confirmUsername
		},function(data){
			if (data.success) {
				$("#showNGRecordListTable").bootstrapTable("refresh", {
					url : contextPath + "mcNGRecord/queryNGRecordsByDate.do",
					cache:false,
					query : {
						deviceSiteCode : $("#currentDeviceSiteCode").val()
					}
				});
			} else {
				$("#alertText").text(data.message);
				$("#alertDialog").modal();
			}
		});
	}
	//意见选择初始化audit,review,confirm
	function flowSuggestion(e){
		if(e=='audit'){
			code = 'auditNG';
		}else if(e=='review'){
			code = 'reviewNG';
		}else if(e=='confirm'){
			code = 'confirmNG';
		}
		$.get(contextPath + "mcSuggestion/getCommonSuggestion.do",{
			code:code
		},function(data){
			$("#flowSuggestion").empty();
			var htmlselect = "<option></option>";
			$.each(data,function(index, Type) {
				htmlselect += "<option value='"+Type.id+"'>"+Type.text+"</option>";
							})
			$("#flowSuggestion").append(htmlselect);		
			$("#flowSuggestion").selectpicker('refresh');
		})
	}
	$(document).ready(function(){
		$("#flowSuggestion").change(function(){
			var data = $("#flowSuggestion option:selected").text();
			$("#suggestion").val(data)
		})
	})
	
	//初始化工序
	function initProcesses(){
		//初始化按灯类型
		$.get(contextPath + "ngReason/queryNGReasonsByProcessCode.do",{processCode:$("#processCode").val() },function(data){
			$("#ngReasonList").empty();
			if(data && data.length>0){
				for(var i = 0;i<data.length;i++){
					var ngReason = data[i];
					var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;width:150px;height:50px;margin-bottom:10px;' onclick='showDetail(\""
	                        + ngReason.ngCode+ "\")'  value='"+ngReason.ngCode+"'>"+ ngReason.ngReason+"</button>");
					
		           
		            $("#ngReasonList").append(button);	
				}
			}
		});
		$.get(contextPath + "ngReason/queryNGReasonsByProcessCode.do",{processCode:$("#processCode").val() },function(data){
			$("#ngReasonList2").empty();
			if(data && data.length>0){
				for(var i = 0;i<data.length;i++){
					var ngReason = data[i];
					var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;width:150px;height:50px;margin-bottom:10px;' onclick='showDetail2(\""
	                        + ngReason.ngCode+ "\")'  value='"+ngReason.ngCode+"'>"+ ngReason.ngReason+"</button>");
					
		           
		            $("#ngReasonList2").append(button);	
				}
			}
		});
	}
	var NGCode = "";  //扫描界面的损时原因cod
	   function showDetail(typeCode) {
		  NGCode = "";
	      $("#ngReasonList button").removeClass("btn btn-primary");
	      $("#ngReasonList button").addClass("btn btn-default");
	      var btns = $("#ngReasonList button");
	      for(var i = 0;i<btns.length;i++){
	         var btn = $(btns[i]);
	         var textOnButton = btn.val();
	         if(textOnButton === typeCode){
	            btn.removeClass("btn btn-default");
	            btn.addClass("btn btn-primary");
	         }
	      }
	      $('#ngReasonCode').val(typeCode);
	      $('#ngReasonCode').change();
	      NGCode = typeCode;
	   }
	   function showDetail2(typeCode) {
		  NGCode = "";
	      $("#ngReasonList2 button").removeClass("btn btn-primary");
	      $("#ngReasonList2 button").addClass("btn btn-default");
	      var btns = $("#ngReasonList2 button");
	      for(var i = 0;i<btns.length;i++){
	         var btn = $(btns[i]);
	         var textOnButton = btn.val();
	         if(textOnButton === typeCode){
	            btn.removeClass("btn btn-default");
	            btn.addClass("btn btn-primary");
	         }
	      }
	      $('#ngReasonCode').val(typeCode);
	      $('#ngReasonCode').change();
	      NGCode = typeCode;
	   }
	   //损时原因点击确认事件
	   function confirminput(){
	      if(NGCode){
	         $('#NGCode').val(NGCode);
	         $('#NGCode').change();
	      }else{
	         $("#alertText").text("请输入NG原因代码！");
	         $("#alertDialog").modal();
	      }
	      NGCode = "";
	   }

	   //显示工单选择框
	   function showSelectWorkSheet(){
		   $("#showWorkSheetsListTable").bootstrapTable({
			   height : longTableHeight,
			   idField : 'id',
			   singleSelect : true,
			   clickToSelect : true,
			   cache : false,
			   striped : true, //隔行换色
			   rowStyle : function(row, index) {
				   if (row.status == '1') {
					   return {
						   css : {
							   "background-color" : "#AED0DD"
						   }
					   };
				   } else {
					   return {};
				   }
			   },
			   columns : [
				   {
					   radio : true,
					   field : '',
					   title : ''
				   },
				   {
					   field : 'manufactureDate',
					   title : '生产日期',
					   formatter : function(value, row, index) {
						   if (value) {
							   var date = new Date(value);
							   return date.getFullYear() + '-'
									   + (date.getMonth() + 1) + '-'
									   + date.getDate();
						   }
					   }
				   }, {
					   field : 'no',
					   title : '工单号',
				   }, {
					   field : 'workPieceCode',
					   title : '工件代码'
				   }, {
					   field : 'workPieceName',
					   title : '工件名称',
				   }, {
					   field : 'customerGraphNumber',
					   title : '客户图号'
				   }, {
					   field : 'graphNumber',
					   title : '图号'
				   }, {
					   field : 'batchNumber',
					   title : '批号'
				   }, {
					   field : 'stoveNumber',
					   title : '材料编号'
				   }, {
					   field : 'version',
					   title : '版本号'
				   }, {
					   field : 'productionCount',
					   title : '计划数量'
				   }, {
					   field : 'status',
					   title : '状态',
					   formatter : function(value, row, index) {
						   if (value) {
							   switch (value) {
								   case '0':
									   return '计划';
								   case '1':
									   return '加工中...';
								   case '2':
									   return '停工';
								   case '3':
									   return '完工';
							   }
						   }
					   }
				   } ]
		   });
		   $("#showWorkSheetsListTable")
				   .bootstrapTable(
						   "refresh",
						   {
							   url : contextPath
									   + "mcWorkSheet/queryProcessingWorkSheets.do",
							   query : {
								   deviceSiteCode : $("#currentDeviceSiteCode").val()
							   }
						   });
		   $("#selectWorkSheetDialog").modal();

	   }

	   //选择工单
	   function selectWorkSheet(){
		   var selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
		   if (selections && selections.length > 0) {
			   var mcWorkSheet = selections[0];
			   $("#workpieceCode").val(mcWorkSheet.workPieceCode);
			   $("#workpieceName").val(mcWorkSheet.workPieceName);
			   $("#graphNumber").val(mcWorkSheet.graphNumber);
			   $("#customerGraphNumber").val(mcWorkSheet.customerGraphNumber);
			   $("#batchNumber").val(mcWorkSheet.batchNumber);
			   $("#stoveNumber").val(mcWorkSheet.stoveNumber);
			   $("#processName").val(mcWorkSheet.processName);
			   $("#processCode").val(mcWorkSheet.processCode);
			   $("#version").val(mcWorkSheet.version);
			   $("#no").val(mcWorkSheet.no);
			   //工单id
			   $.get(contextPath+"mcWorkSheet/queryWorkSheetByNo.do",{
				   no:mcWorkSheet.no
			   },function(data){
				   $("#workSheetId").val(data.id);
			   });
			   //工件id
			   $.get(contextPath+"inventory/queryWorkpiecesByCode.do",{
				   code:mcWorkSheet.workPieceCode
			   },function(data){
				   $("#workpieceId").val(data.code);
			   });
			   $("#addOrUpdateNGRecordDialog").modal();
			   initProcesses();
			   setTimeout("$('#ngReasonCode').focus();", 500 )
		   }else{
			   $("#processCode").val("");
			   $("#alertText").text("该站点当前没有加工的工单！");
			   $("#alertDialog").modal();
			   return false;
		   }
	   }
</script>
<style type="text/css">

.modal-footer span {
	cursor: pointer;
	margin-right: 20px;
}
</style>
<!-- 存放当前选中tab的设备站点代码 -->
<input type="hidden" id="currentDeviceSiteCode" />
<div class="tyPanelSize" >
	<div class="tytitle" style="height: 7%; text-align: center">
		<div style="overflow: hidden; display: inline-block">
				<span class="fa fa-user-circle fa-2x" aria-hidden="true" style="float: left; margin-top: 10px;margin-right: 10px"></span>
				<span style="float: left; margin-top: 17px;">NG录入</span>
		</div>
	</div>
	<div class="mc_workmanage_center" style="height:73%;">
		<div style="text-align: center">
			<ul id="myTab" class="nav nav-tabs" role="tablist"
				style="overflow: hidden; display: inline-block;">
			</ul>
			<!-- Tab panes -->
			<div id="myTabpanes" class="tab-content"
				style="border-top: 1px solid darkgray; margin-top: -6px;">
				<div role="tabpanel" class="tab-pane active"
					id="showNGRecordsListPanel" style="text-align: center;margin-left: 2%;margin-right: 2%">
					<div  style='float: left;margin: 7px;font-size: 18px;'>
						<!-- <span onclick='toolsearch()' class='fa fa-search' aria-hidden='true'></span> -->
						  日期范围: <input id="startTime"  style="margin:8px;width: 200px" name="startTime" editable="false"> 
						  至 <input id="endTime"  style="margin:8px;width: 200px;"	name="endTime" editable="false">
						  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					  工单单号: <input id="workNo"  style="margin-right:20px;width: 200px;" name="workNo">
    					  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					  批号: <input id="searchBatchNumber"  style="margin:8px;width: 200px" name="searchBatchNumber">
    					  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					 NG代码: <input id="searchNGReasonCode"  style="margin:8px;width: 200px" name="searchNGReasonCode">
					</div>
    					 <input type="button" value="查询" style="margin-left:16px;margin-bottom16px;margin-top:16px;width: 80px;font-size: 18px;float: right;" 
    					 onclick='toolsearch()'>
					<!-- NG列表 -->
					<table id="showNGRecordListTable" data-cache=false style='overflow: hidden;text-overflow: ellipsis;white-space: nowrap;'>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="height: 20%; overflow: hidden;margin-left: 2%; margin-right: 2%;">
        <div style=" float: left;">
            <div data-toggle="modal"
                onclick="showInWarehouseDialog()"
                data-target="#gdModal"
                class="container-fluid functionButton">
                <h6></h6>
                <span class="fa fa-download fa-3x" aria-hidden="true"
                style=" margin: 0 auto"></span>
                <h4 style="text-align: center">入库</h4>
            </div>
        </div>
		<div style=" float: right;">
		    <div data-toggle="modal" 
				onclick="showAddOrUpdateNGRecordDialog('update')"
				data-target="#gdModal"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench fa-3x" aria-hidden="true"
				style=" margin: 0 auto"></span>
				<h4 style="text-align: center">修改</h4>
			</div>
			<div onclick="deleteNGRecord()"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-times-circle fa-3x" aria-hidden="true"
				style=" margin: 0 auto"></span>
				<h4 style="text-align: center">删除</h4>
			</div>
			<div onclick="showAddOrUpdateNGRecordDialog('add')"
				class="container-fluid functionButton" style="margin-right: 0">
				<h6></h6>
				<span class="fa fa-plus-circle fa-3x" aria-hidden="true"
				style=" margin: 0 auto"></span>
				<h4 style="text-align: center">新增</h4>
			</div>
		</div>
	</div>
</div>
<%@ include file="end.jsp"%>
<!-- 新增或更新ng记录窗口 -->
<div class="modal fade" id="addOrUpdateNGRecordDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">添加/修改NG记录</h4>
			</div>
			<div class="modal-body">
				<form id="ngRecordForm" class="form-horizontal">
					<input type="hidden" id="workSheetId" name="workSheetId" />
					<%--<input type="hidden" id="no" name="no" />--%>
					<input type="hidden" id="workpieceId" name="workpieceId" />
					<input type="hidden" id="processCode" name="processCode" />
					<input type="hidden" name="version" id="version">
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="occurDate">产生时间</label>
							<div class="col-sm-3">
								<input type="text" name="occurDate" class="form-control"
									id="occurDate" />

								<!-- <div class="input-append date form_datetime">
									<input size="16" type="text"  readonly name="occurDate" id="occurDate" class="form-control"> 
									<span
										class="fa fa-calendar"><i class="icon-th"></i></span>
								</div> -->
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="inputUsername">填报人员</label>
							<div class="col-sm-3">
								<input type="text" name="inputUsername" class="form-control"
									readonly="readonly" id="inputUsername" style="width:220px">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="workpieceCode">工件代码</label>
							<div class="col-sm-3">
								<input type="text" name="workpieceCode" class="form-control"
									readonly="readonly" id="workpieceCode">
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="workpieceName">工件名称</label>
							<div class="col-sm-3">
								<input type="text" name="workpieceName" class="form-control"
									readonly="readonly" id="workpieceName" style="width:220px">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="graphNumber">图号</label>
							<div class="col-sm-3">
								<input type="text" name="graphNumber" class="form-control"
									readonly="readonly" id="graphNumber">
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="customerGraphNumber">客户图号</label>
							<div class="col-sm-3">
								<input type="text" name="customerGraphNumber"
									readonly="readonly" class="form-control"
									id="customerGraphNumber" style="width:220px">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="batchNumber">批号</label>
							<div class="col-sm-3">
								<input type="text" name="batchNumber" class="form-control"
									id="batchNumber">
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="stoveNumber">材料编号</label>
							<div class="col-sm-3">
								<input type="text" name="stoveNumber" class="form-control"
									readonly="readonly" id="stoveNumber" style="width:220px">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="processName">工序名称</label>
							<div class="col-sm-3">
								<input type="text" name="processName" class="form-control"
									readonly="readonly" id="processName">
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="no">工单号</label>
							<div class="col-sm-4">
								<input type="text" name="no" class="form-control" style="width: 220px;display: inline-block;"
									readonly="readonly" id="no">
								<span id="noButton" onclick='showSelectWorkSheet()' class='fa fa-search' aria-hidden='true' style='float: right;font-size: 30px;'></span>
						</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="ngReasonCode">NG代码</label>
							<div class="col-sm-3">
								<input type="text" name="ngReasonCode" class="form-control"
									id="ngReasonCode" autofocus="autofocus" onblur="lostfocus()"> <input type="hidden" id="ngReasonId"
									name="ngReasonId" />
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="ngReason">NG原因</label>
							<div class="col-sm-3">
								<input type="text" name="ngReason" class="form-control"
									id="ngReason" style="width:220px">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="ngCount">数量</label>
							<div class="col-sm-3">
								<input type="text" name="ngCount" class="form-control"
									id="ngCount" value="1">
							</div>
						</div>
						<div class=".col-xs-6">
							<label class="col-sm-2 control-label" for="processingMethod">处理方法</label>
							<div class="col-sm-3">
								<select class="selectpicker" name="processingMethod"
									id="processingMethod">
									<option value="scrap">报废</option>
									<option value="repair">返修</option>
									<option value="compromise">让步接收</option>
								</select> <input type="hidden" name="id" id="id" /> <input type="hidden"
									id="deviceSiteCode" name="deviceSiteCode" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span class="fa fa-qrcode fa-4x" onclick="showScanNGQRCodeDialog()"></span>
					<span class="fa fa-rotate-left fa-4x" data-dismiss="modal"></span>
					<span class="fa fa-check fa-4x" data-dismiss="modal"
						onclick="addOrUpdateNGRecord()"></span>
				</div>
			</div>
			<div class="form-group" style="margin-top: 20px;">
		        <div class=".col-xs-12" style="vertical-align: middle;height:400px;overflow-y:auto">
		            <div>
		                 <label class="col-sm-2 control-label" for="pressLightCode">选择NG原因</label>
		            </div>
		         	<div id="ngReasonList" style="display:inline-block ;margin-top: 0px;width: 700px;" ></div>
		     	</div>
		     </div>
		</div>
	</div>
</div>


<!-- 选择工单窗口 -->
<div class="modal fade" id="selectWorkSheetDialog" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true"
	 data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">选择工单</h4>
			</div>
			<div class="modal-body">
				<table id="showWorkSheetsListTable"
					   style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
				</table>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span class="fa fa-rotate-left fa-4x" data-dismiss="modal"></span>
					<span class="fa fa-check fa-4x" data-dismiss="modal"
						  onclick="selectWorkSheet()"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 扫描NG条码窗口 -->
<div class="modal fade" id="scanNGQRcodeDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<span class="fa fa-qrcode fa-4x"></span>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<label class="col-sm-2 control-label" for="NGCode">NG条码</label>
						<div class="col-sm-9">
							<input type="text" name="NGCode" class="form-control" id="NGCode"
								onblur="" autofocus="autofocus" style="width: 650px;margin-left: -15px;"/>
						</div>
					</div>
				</div>
				<div class="form-group" style="margin-top: 50px;">
			        <div class=".col-xs-12" style="vertical-align: middle;">
			            <div>
			                 <label class="col-sm-2 control-label" for="pressLightCode">选择NG原因</label>
			            </div>
			         	<div id="ngReasonList2" style="display:inline-block ;margin-top: 0px;width: 700px;" ></div>
			     	</div>
			     </div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
				 <span class='btn btn-default'  style="margin-right: 20px;"
 onclick="confirminput()">确认</span>
					<span data-dismiss="modal" class='btn btn-default'
						onclick="$('#ngReasonCode').focus();">手工输入</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 扫描员工条码窗口 -->
<div class="modal fade" id="scanEmployeeCodeDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<span class="fa fa-qrcode fa-4x"></span>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<label class="col-sm-2 control-label" for="employeeCode">员工条码</label>
						<div class="col-sm-9">
							<input type="text" name="employeeCode" class="form-control" id="employeeCode"
								autofocus="autofocus" />
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
<!-- 删除确认框 -->
<div class="modal fade" id="deleteConfirmDialog">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要删除吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="deleteNgRecord()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 入库确认 -->
<div class="modal fade" id="inWarehouseConfirmDialog">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要入库吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="inWarehousr()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 审核、复核、确认框 -->
<div class="modal fade" id="confirmDialog" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-body">
				<form role="form">
					<div class="form-group">
						<label for="name">意见</label>
						<textarea class="form-control" rows="3" id="suggestion" name="suggestion"></textarea>
						<div style="margin-top: 3% ;width: 568px;">
						<select class="flowSuggestion" name="flowSuggestion" id="flowSuggestion" style="width: 568px;float: left" >
						</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="ngWorkFlowGo()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
