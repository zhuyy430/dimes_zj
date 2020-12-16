<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script type="text/javascript">
	$.ajaxSetup({cache:false});
	var employeeID="";
	var employeeName="";
	var workPieceCode="";
	var processCode="";
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
			$('#scanNGQRcodeDialog').modal('hide');
			$("#ngReasonCode").val($(this).val()).change();
		});
		//NG二维码扫描框回车事件
		$('#NGCode').keydown(function(event) {
			if (event.keyCode == 13) {
				$('#scanNGQRcodeDialog').modal('hide');
				$("#ngReasonCode").val($(this).val()).change();
			}
		});
		
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
								$("#showNGRecordListTable").bootstrapTable(
										"refresh",
										{
											url : contextPath
													+ "inspectionRecord/queryInspectionRecordsByDeviceSite.do",
											cache:false,
											query : {
												deviceSiteCode : $("#currentDeviceSiteCode").val()
											}
										});
							}
						});
	}
	//tab切换事件
	function refreshNGRecordsTable(deviceSiteCode) {
		$("#currentDeviceSiteCode").val(deviceSiteCode);
		
		$("#endTime").datebox('setValue', '');
		$("#startTime").datebox('setValue', '');
		$("#searchBatchNumber").val("");
		$("#workNo").val("");
		$("#searchStatus").selectpicker('val',"");
		
		$("#showNGRecordListTable").bootstrapTable("refresh", {
			url : contextPath + "inspectionRecord/queryInspectionRecordsByDeviceSite.do",
			cache:false,
			query : {
				deviceSiteCode : deviceSiteCode
			}
		});
	}
	//tab条件查询
	function toolsearch() {
		var timeend= new Date($("#endTime").val().replace(/-/g,"/")).getTime();
        var timestart = new Date($("#startTime").val().replace(/-/g,"/")).getTime();
        if(timeend<timestart){
	 		$("#alertText").text("开始时间大于结束时间，请重新选择！");
			$("#alertDialog").modal();
			return false;
		}
		var deviceSiteCode=$("#currentDeviceSiteCode").val();
		$("#showNGRecordListTable").bootstrapTable("refresh", {
			url : contextPath + "inspectionRecord/queryInspectionRecordsByDeviceSite.do",
			cache:false,
			query : {
				from:$("#startTime").val(),
				to:$("#endTime").val(),
				no:$("#workNo").val(),
				inspectionResult:$("#searchStatus").val(),
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
					singleSelect : true,
					clickToSelect : true,
					striped : true, //隔行换色
					/* rowStyle : function(row, index) {
						if (!row.auditDate) {
							return {
								css : {
									"background-color" : "#F2F5A9"
								}
							};
						} else {
							return {};
						}
					}, */
					columns : [
							{
								radio : true,
								field : '',
								title : ''
							},
							{
								field : 'inspectionDate',
								title : '检验时间',
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
								title : '工序代码'
							},
							{
								field : 'processName',
								title : '工序名称'
							},
							{
								field : 'inventoryCode',
								title : '工件代码'
							},
							{
								field : 'inventoryName',
								title : '工件名称'
							},
							{
								field : 'specificationType',
								title : '规格型号'
							},
							{
								field : '',
								title : '图号'
							},
							{
								field : 'batchNumber',
								title : '批号'
							},
							{
								field : 'furnaceNumber',
								title : '材料编号'
							},
							{
								field : 'inspectionResult',
								title : '检验结果'
							},
							{
								field : 'inspectorName',
								title : '检验员'
							},
							{
								field : 'className',
								title : '班次'
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
		switch (type) {
		//新增ng记录
		case "add": {
			$("#cancel").show(); 
			$("#save").show();
			operationType = "add";
			initNGRecordForm4Add();
			break;
		}
			//修改ng记录
		case "update": {
			operationType = "update";
			if (initNGRecordForm4Update()) {
				/* $("#cancel").css("display","none");
				$("#save").css("display","none"); */
				$("#cancel").hide(); 
				$("#save").hide(); 
				$("#addOrUpdateNGRecordDialog").modal();
			}
			break;
		}
		}
	}
	var projectRecordsList;
	//添加ng记录时，初始化form表单
	function initNGRecordForm4Add() {
		workPieceCode="";
		$("#ngRecordForm")[0].reset();
		var date = new Date();
		var value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
		$("#occurDate").val(value);
		//根据IP地址查询本机是否有登录用户
		$.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
				mcUser) {
			if (mcUser.mcUser) {
				$("#inspectorName").val(mcUser.mcUser.employeeName);
				$("#inspectorempCode").val(mcUser.mcUser.employeeCode);
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
				//工件id获取工序参数
				workPieceCode = mcWorkSheet.workPieceCode;
				processCode = mcWorkSheet.processCode;
				$.get(contextPath+"inventory/queryAllWorkpieceProcessParameterMappingsByWorkpieceCode.do",{
					InventoryCode:mcWorkSheet.workPieceCode,
					processCode:mcWorkSheet.processCode
				},function(data){
					 $("#processesItem").empty();
					projectRecordsList=data;
					var str="";
					$.each(data, function (index, item) {  
		                 //循环获取数据
		                 var i =index+1;
					
					var parametercode="";//参数代码
					if(item.parameter.code!=null){
						parametercode=item.parameter.code
					}
					
					var parametername="";//参数名称
					if(item.parameter.name!=null){
						parametername=item.parameter.name
					}
					
					var parameternote="";//参数说明
					if(item.parameter.note!=null){
						parameternote=item.parameter.note
					}
					
					var upLine="";
					if(item.upLine!=null){
						upLine=item.upLine
					}
					
					var lowLine="";
					if(item.lowLine!=null){
						lowLine=item.lowLine
					}
					
					var standardValue="";
					if(item.standardValue!=null){
						standardValue=item.standardValue
					}

					var unit="";
					if(item.unit!=null){
						unit=item.unit
					}
					
					str += '<tr>' +   
		                     '<td style="display:none">' + "<input type='hidden' class='ids' value='" + item.id + "' />" + '</td>' +  
		                     '<td>' + i +'</td>' +  
		                     '<td>' + parametercode + '</td>' +  
		                     '<td>' + parametername + '</td>' +  
		                     '<td>' + parameternote + '</td>' +  
		                     '<td>' + upLine + '</td>' +  
		                     '<td>' + lowLine + '</td>' +  
		                     '<td>' + standardValue + '</td>' +  
		                     '<td>' + unit + '</td>' +  
		                     '<td >'  +"<input type='number' class='checkValue' value='" +"' style='border:1px solid #FF0000;width:100%;height: 100%' onblur='showResult(this,"+i+")'/> "+ '</td>' +  
		                     '<td >'  +"<select class='resultValue' onchange='changeResult(this,"+i+")' id='"+i+"' style='border-style:none;background-color:#63D096;width:100%;height: 100%;margin:3px auto;text-align: center;'><option selected='selected' value='OK'>OK</option><option value='NG'>NG</option></select>"+ '</td>' +  
		                     '<td >'  +"<input type='text' class='noteValue' style='border:1px solid #FF0000;width:100%;height: 100%;' value='"  + "' />"+ '</td>' +  
		                     '</tr>'; 
		             }); 
		             $("#processesItem").prepend(str);
				});
				$("#addOrUpdateNGRecordDialog").modal();
			}else{
				$("#processCode").val("");
				$("#alertText").text("该站点当前没有加工的工单！");
				$("#alertDialog").modal();
			}
		});
	}
	
	function showResult(obj,id){
		//id即为数组索引
		var projectRecord = projectRecordsList[id-1];
		//点检值
		var value = parseFloat(obj.value);
		if(projectRecord.lowLine!=null && projectRecord.upLine!=null){
			if(value>=projectRecord.lowLine && value<=projectRecord.upLine){
				$('#'+id).val("OK");
				$('#'+id).css({"backgroundColor":"#63D096"},{"readonly":"readonly"});
				$('#'+id).attr("disabled","disabled");
			}else{
				$('#'+id).val("NG");
				$('#'+id).css({"backgroundColor":"#DF969A"},{"readonly":"readonly"});
				$('#'+id).attr("disabled","disabled");
			}
		}
		return false;
	}

	function changeResult(obj,id){
		//id即为数组索引
		//点检值
		console.log(obj)
		var value = obj.value;
			if(value=='OK'){
				$('#'+id).css({"backgroundColor":"#63D096"});
			}else{
				$('#'+id).css({"backgroundColor":"#DF969A"});
			}
		return false;
	}
	/**
	 * 检验单详情中，显示检验单详情
	 * @param formNo
	 */
	function showInspectionRecordDetail(formNo) {
	    $('#inspectionRecordDetailDg').iEdatagrid({
	        pagination:false,
	        autoSave:true,
	        url: 'inspectionRecordDetail/queryByFormNo.do',
	        updateUrl:'inspectionRecordDetail/updateSession.do',
	        queryParams:{
	            formNo:formNo
	        },onEndEdit:function(index,row,changes){
	            if(row.parameterValue){
	                var pv = parseFloat(row.parameterValue);
	                if(pv>=row.lowLine && pv<=row.upLine){
	                    row.inspectionResult = "OK";
	                }else{
	                    row.inspectionResult = "NG";
	                }
	            }
	        }
	    });
	}
	
	//更新ng记录时，初始化form表单
	function initNGRecordForm4Update() {
		$("#ngRecordForm")[0].reset();
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要查看的检验记录!");
			$("#alertDialog").modal();
			return false;
		}

		var selection = selections[0];
		$.get(contextPath + "mcInspectionRecord/queryInspectionRecord.do", {
			formNo : selection.formNo
		}, function(data) {
				$("#no").val(data.no);
				$("#workpieceCode").val(data.inventoryCode);
				$("#workpieceName").val(data.inventoryName);
				$("#unitType").val(data.specificationType);
				$("#batchNumber").val(data.batchNumber);
				$("#stoveNumber").val(data.furnaceNumber);
				$("#processName").val(data.processName);
				$("#inspectorName").val(data.inspectorName);
				$("#inspectClass").selectpicker('val', data.inspectionType);
				$('#inspectClass').selectpicker('refresh');
				//查询详细信息
				$.get(contextPath + "mcInspectionRecord/queryInspectionRecordDetaile.do", {
					formNo : selection.formNo
				}, function(data) {
					$("#processesItem").empty();
					var str="";
					$.each(data, function (index, item) {  
		                 //循环获取数据
		                 var i =index+1;
					
					var parametercode="";//参数代码
					if(item.parameterCode!=null){
						parametercode=item.parameterCode
					}
					
					var parametername="";//参数名称
					if(item.parameterName!=null){
						parametername=item.parameterName
					}
					
					var parameternote="";//参数说明
					/* if(item.parameter.note!=null){
						parameternote=item.parameter.note
					} */
					
					var upLine="";
					if(item.upLine!=null){
						upLine=item.upLine
					}
					
					var lowLine="";
					if(item.lowLine!=null){
						lowLine=item.lowLine
					}
					
					var standardValue="";
					if(item.standardValue!=null){
						standardValue=item.standardValue
					}

					var unit="";
					if(item.unit!=null){
						unit=item.unit
					}
					var parameterValue="";//参数值
					if(item.parameterValue!=null){
						parameterValue=item.parameterValue
					}

					var inspectionResult="";//结果
					if(item.inspectionResult!=null){
						inspectionResult=item.inspectionResult
					}
					var color="#63D096";
					if(inspectionResult=="NG")
						color="#DF969A"

					var note="";//备注
					if(item.note!=null){
						note=item.note
					}
					
					str += '<tr>' +   
		                     '<td style="display:none">' + "<input type='hidden' class='ids' value='" + item.id + "' />" + '</td>' +  
		                     '<td>' + i +'</td>' +  
		                     '<td>' + parametercode + '</td>' +  
		                     '<td>' + parametername + '</td>' +  
		                     '<td>' + parameternote + '</td>' +  
		                     '<td>' + upLine + '</td>' +  
		                     '<td>' + lowLine + '</td>' +  
		                     '<td>' + standardValue + '</td>' +  
		                     '<td>' + unit + '</td>' +  
		                     '<td >'  +parameterValue+ '</td>' +
		                     '<td style="background-color:'+color+'">'  +inspectionResult+ '</td>' +  
		                     '<td >'  +note+ '</td>' +  
		                     '</tr>'; 
		             }); 
		             $("#processesItem").prepend(str);
				});
		});

		return true;
	}

	//添加或更新ng记录
	function addOrUpdateNGRecord() {
		//获取检验记录项的id数组，以逗号间隔
		var idsObj = $(".ids");
		var ids = "";
		if(idsObj && idsObj.length>0){
			for(var i = 0;i<idsObj.length;i++){
				ids+=$(idsObj[i]).attr("value")+",";
			}
			ids = ids.substring(0,ids.length-1);
		}
		//获取检验记录项的结果数组
		var resultObj = $(".resultValue");
		var results = "";
		//是否存在NG记录
		var tf=false;
		
		if(resultObj && resultObj.length>0){
			for(var i = 0;i<resultObj.length;i++){
				results += $(resultObj[i]).val() + ",";
				if($(resultObj[i]).val()=="NG"){
					tf=true;
				}
			}
			results = results.substring(0,results.length-1);
		}
		
		var noteObj=$(".noteValue");
		var notes = "";
		if(noteObj && noteObj.length>0){
			for(var i = 0;i<noteObj.length;i++){
				notes+=$(noteObj[i]).val()+" @";
			}
			notes =notes.substring(0,notes.length-1);
		} 
		var checkValueObj=$(".checkValue");
		var checkValue = "";
		if(checkValueObj && checkValueObj.length>0){
			for(var i = 0;i<checkValueObj.length;i++){
				checkValue+=$(checkValueObj[i]).val()+" ,";
			}
			checkValue =checkValue.substring(0,checkValue.length-1);
		}
			$.get(contextPath + "mcInspectionRecord/saveInspectionRecord.do",{
				
					no:$("#no").val(),
					workSheetId:$("#workSheetId").val(),
					processCode:$("#processCode").val(),
					employeeCode:$("#inspectorempCode").val(),
					inspectClass:$("#inspectClass").val(),
					itemIds:ids,
					results:results,
					checkValue:checkValue,
					notes:notes
			},
					function(data) {
						//刷新表格
						$("#showNGRecordListTable").bootstrapTable("refresh", {
							url : contextPath + "inspectionRecord/queryInspectionRecordsByDeviceSite.do",
							cache:false,
							query : {
								deviceSiteCode : $("#currentDeviceSiteCode").val()
							}
						});
					});
		
	}
	//取消新增
	function showScanNGQRCodeDialog() {
		$.get(contextPath+"inventory/queryAllWorkpieceProcessParameterMappingsByWorkpieceCode.do",{
			InventoryCode:workPieceCode,
			processCode:processCode
		},function(data){
			 $("#processesItem").empty();
			projectRecordsList=data;
			var str="";
			$.each(data, function (index, item) {  
                 //循环获取数据
                 var i =index+1;
			
			var parametercode="";//参数代码
			if(item.parameter.code!=null){
				parametercode=item.parameter.code
			}
			
			var parametername="";//参数名称
			if(item.parameter.name!=null){
				parametername=item.parameter.name
			}
			
			var parameternote="";//参数说明
			if(item.parameter.note!=null){
				parameternote=item.parameter.note
			}
			
			var upLine="";
			if(item.upLine!=null){
				upLine=item.upLine
			}
			
			var lowLine="";
			if(item.lowLine!=null){
				lowLine=item.lowLine
			}
			
			var standardValue="";
			if(item.standardValue!=null){
				standardValue=item.standardValue
			}

			var unit="";
			if(item.unit!=null){
				unit=item.unit
			}
			
			str += '<tr>' +   
                     '<td style="display:none">' + "<input type='hidden' class='ids' value='" + item.id + "' />" + '</td>' +  
                     '<td>' + i +'</td>' +  
                     '<td>' + parametercode + '</td>' +  
                     '<td>' + parametername + '</td>' +  
                     '<td>' + parameternote + '</td>' +  
                     '<td>' + upLine + '</td>' +  
                     '<td>' + lowLine + '</td>' +  
                     '<td>' + standardValue + '</td>' +  
                     '<td>' + unit + '</td>' +  
                     '<td >'  +"<input type='number' class='checkValue' value='" +"' style='border:1px solid #FF0000;width:100%;height: 100%' onblur='showResult(this,"+i+")'/> "+ '</td>' +  
                     '<td >'  +"<select class='resultValue' id='"+i+"' onchange='changeResult(this,"+i+")' style='border-style:none;background-color:#63D096;width:100%;height: 100%;margin:3px auto;text-align: center;'><option selected='selected'>OK</option><option>NG</option></select>"+ '</td>' +  
                     '<td >'  +"<input type='text' class='noteValue' style='border:1px solid #FF0000;width:100%;height: 100%;' value='"  + "' />"+ '</td>' +  
                     '</tr>'; 
             }); 
             $("#processesItem").prepend(str);
		});
	}
	//删除NG记录
	function deleteNGRecord() {
		var selections = $("#showNGRecordListTable").bootstrapTable(
				"getSelections");
		if (selections.length <= 0) {
			$("#alertText").text("请选择要删除的检验记录!");
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
		var formNo = selections[0].formNo;
		$.get(contextPath + "mcInspectionRecord/deleteInspectionRecord.do", {
			formNo : formNo
		}, function(data) {
			if (data.success) {
				$("#showNGRecordListTable").bootstrapTable("refresh", {
					url : contextPath + "inspectionRecord/queryInspectionRecordsByDeviceSite.do",
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
			$("#alertText").text("请选择要操作的检验记录!");
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
	      NGCode = typeCode;
	   }
	   //损时原因点击确认事件
	   function confirminput(){	  
	      if(NGCode){
	         $('#NGCode').val(NGCode);
	         $('#NGCode').change();
	      }else{
	         $("#alertText").text("请选择工序！");
	         $("#alertDialog").modal();
	      }
	      NGCode = "";
	   }
</script>
<style type="text/css">

.modal-footer span {
	cursor: pointer;
	margin-right: 20px;
}
.table1 td, .table1 th {
	border: 1px solid #000000;
	font-size: 14px;
	text-align: center;
	height: 50px;
}
</style>
<!-- 存放当前选中tab的设备站点代码 -->
<input type="hidden" id="currentDeviceSiteCode" />
<div class="tyPanelSize" >
	<div class="tytitle" style="height: 7%; text-align: center">
		<div style="overflow: hidden; display: inline-block">
				<span class="fa fa-user-circle fa-2x" aria-hidden="true" style="float: left; margin-top: 10px;margin-right: 10px"></span>
				<span style="float: left; margin-top: 17px;">检验记录</span>
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
						  日期范围: <input id="startTime"  style="margin:8px;width: 200px;" name="startTime" editable="false"> 
						  至 <input id="endTime"  style="margin:8px;width: 200px;"	name="endTime" editable="false">
						  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					  工单单号: <input id="workNo"  style="margin-right:20px;width: 200px;" name="workNo">
    					  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					  批号: <input id="searchBatchNumber"  style="margin:8px;width: 200px" name="searchBatchNumber">
    					  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					 检验结果: <select id="searchStatus" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
									<option value="" ></option>
									<option value="OK" >OK</option>
									<option value="NG" >NG</option>
								</select>
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
		<div style=" float: right;">
		    <div data-toggle="modal" 
				onclick="showAddOrUpdateNGRecordDialog('update')"
				data-target="#gdModal"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-search fa-3x" aria-hidden="true"
				style=" margin: 0 auto"></span>
				<h4 style="text-align: center">查看</h4>
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
	<div class="modal-dialog" style="width: 1900px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">添加/修改检验记录</h4>
			</div>
			<div class="modal-body" style="height: 850px">
				<form id="ngRecordForm" class="form-horizontal">
					<input type="hidden" id="workSheetId" name="workSheetId" />
					<input type="hidden" id="workpieceId" name="workpieceId" />
					<input type="hidden" id="processCode" name="processCode" />
					<input type="hidden" id="inspectorempCode" name="inspectorempCode" />
					
					<div class="form-group">
						<div class=".col-xs-1">
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="occurDate">工单单号</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="no"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="inputUsername">工件代码</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="workpieceCode"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="workpieceName">工件名称</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="workpieceName"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="unitType">规格型号</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="unitType"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="graphNumber">批号</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="batchNumber"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
					</div>

					<div class="form-group">
						<div class=".col-xs-1">
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="occurDate">材料编号</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="stoveNumber"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="inputUsername">图号</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="graphNumber"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="workpieceCode">工序名称</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="processName"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="workpieceName">检验人</label>
							<div class="col-sm-1">
								<input type="text" readonly="readonly" id="inspectorName"
								style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
							</div>
						</div>
						<div class=".col-xs-2">
							<label class="col-sm-1 control-label" for="workpieceName">检验类别</label>
							<div class="col-sm-1">
								<%--<input type="text" readonly="readonly" id="inspectClass"
									   style="width: 210px; text-align: center; border-left-width: 0px; border-top-width: 0px; border-right-width: 0px; border-bottom-color: black">
								--%><select id="inspectClass" style="width: 60px; display: inline-block;" equipID=""
										class="selectpicker" data-live-search="true">
									<option value="首检" >首检</option>
									<option value="巡检" >巡检</option>
									<option value="班检" selected>班检</option>
									<option value="末检" >末检</option>
								</select>
							</div>
						</div>
					</div>
				</form>
				<div class="topjui-input-block">
							<div style="width: 100%;height:700px;overflow-y:auto;">
								<table id="processes" class="table1"
									style="width: 98%; margin-left: auto; margin-right: auto;">
									<thead>
										<tr>
											<th>序号</th>
											<th>参数代码</th>
											<th>参数名称</th>
											<th>参数说明</th>
											<th>上限值</th>
											<th>下限值</th>
											<th>标准值</th>
											<th>单位</th>
											<th>参数值</th>
											<th>结果</th>
											<th>备注</th>
										</tr>
									</thead>
								<tbody id="processesItem"></tbody>
								</table>
							</div>
						</div>
			</div>
			<div class="modal-footer" style="margin-top: 0px;background-color: #999999">
				<div style="float: left; margin-left: 200px;width: 800px">
					<input class="btn btn-default" type="button" id="cancel" value="重置" onclick="showScanNGQRCodeDialog()" 
					style="background-color: #999966;width: 100px;margin-right: 200px;">
					<input class="btn btn-default" type="button" data-dismiss="modal" value="退出" 
					style="background-color: #EFB402;width: 100px;float: right;">
				</div>
				<div style="float: left; ">
					<input class="btn btn-default" type="button" id="save" data-dismiss="modal" value="保存" onclick="addOrUpdateNGRecord()" 
					style="margin-left: 200px;background-color: #2786C7;width: 100px">
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
								onblur="lostfocus()" autofocus="autofocus" style="width: 650px;margin-left: -15px;"/>
						</div>
					</div>
				</div>
				<div class="form-group" style="margin-top: 50px;">
			        <div class=".col-xs-12" style="vertical-align: middle;">
			            <div>
			                 <label class="col-sm-2 control-label" for="pressLightCode">选择NG原因</label>
			            </div>
			         	<div id="ngReasonList" style="display:inline-block ;margin-top: 0px;width: 700px;" ></div>
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
