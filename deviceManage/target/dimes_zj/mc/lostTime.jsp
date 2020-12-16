<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<style type="text/css">
	fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver;border-radius:8px}

legend{padding:.5em;border:0;width:auto;margin-bottom:-10px}
</style>
<script type="text/javascript">
	var tab="";//tab代码(dataid属性值)
	var rids=""; //记录id的字符串
	var id ="";  //table选中记录ID
	var param=""; //判断是修改提交，还是确认验证
	var Devid;   //站点ID
	var pressLightCode; //故障原因CODE
	var endVal="";  //拆分选中数据结束时间
	var startVal=""; //拆分选中数据开始时间
	var sum = 0;  //损时总时长
	var count = 0;//损时数量
	var b_time;
	var e_time;
	var date = new Date();

	var dateForm = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+" " + date.getHours() + ":"+ date.getMinutes() + ":" + date.getSeconds();
 	$(function(){
 		$("#deviceSiteCode").val("<%=request.getParameter("deviceSiteCode")%>");
 		<%-- $("#lostTimeDeviceSiteCode").text("<%=request.getParameter("deviceSiteCode")%>"); --%>
 		$('#startTime').val(new Date());
 		$('#endTime').val(new Date());
 		$("#alertDialog").css("z-index","10001")
 		initLostTimeTable();
 		setlostTimeType();
 		flowSuggestion();
 		initTab();
 		//获取站点的ID
 		/*$.get("mcequipment/findDeviceSiteId.do", {
 	         deviceSiteCode : $("#deviceSiteCode").val()
 	      }, function(data) {
 	         Devid = data.id;
 	      });*/
 		//扫码窗口改变事件
        $("#userCode").keydown(function(event) {
        	if (event.keyCode == 13) {
	        	lostTimeConfirm();
 			}
        })
         //扫码窗口改变事件
     	$("#lostTimeCode").keydown(function(event) {
        	if (event.keyCode == 13) {
	     		lostTimeTypeSelect(param);
 			} 
      	})
         //拆分扫码窗口改变事件
     	$("#splitLostTimeCode").keydown(function(event) {
        	if (event.keyCode == 13) {
        		splitLostTimeTypeSelect('split');
 			}
      	})
         //扫码窗口改变事件
     	$("#lostTimeCode").change(function(event) {
	     		lostTimeTypeSelect(param);
      	})
         //拆分扫码窗口改变事件
     	$("#splitLostTimeCode").change(function(event) {
        		splitLostTimeTypeSelect('split');
      	})
      //下拉框未选中显示
        $(".selectpicker").selectpicker({
            noneSelectedText: '请选择' //默认显示内容  
             });
        $(".flowSuggestion").selectpicker({

	           noneSelectedText: '请选择',

	           width:'100%'

	        });

	      }); 

	$(function () {
 		//NG产生时间初始化
 		
 		$("#searchStartTime").datebox({
 			timeSeparator:':',
 			showSeconds:true
		});
 		$("#searchEndTime").datebox({
 			timeSeparator:':',
 			showSeconds:true
		});
 		$("#startTime").datetimebox({
 			required: true,
 			timeSeparator:':',
 			showSeconds:true
		});
 		$("#endTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#updateEndTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#updateStartTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#splitAddEndTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#splitAddStartTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		
 		$("#startTime").datetimebox('setValue', dateForm);
 		$("#endTime").datetimebox('setValue', dateForm);
	})
	//tab选中样式
	$(function() {
		$("#myTab li:first").addClass("active");
	});
	
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
										$("#deviceSiteCode").val(
												deviceSite.deviceSiteCode);
										tab=deviceSite.deviceSiteCode;
									}else if (i == 0) {
										$("#deviceSiteCode").val(
												deviceSite.deviceSiteCode);
                                        tab=deviceSite.deviceSiteCode;
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
								$("#showLostTimeTable").bootstrapTable("refresh", {
									url : contextPath + "mcLostTime/findLostTimeList.do",
									cache:false,
									query : {
										deviceSiteCode : $("#deviceSiteCode").val()
									}
								});
                                $.get("mcequipment/findDeviceSiteId.do", {
                                    deviceSiteCode : $("#deviceSiteCode").val()
                                }, function(data) {
                                    Devid = data.id;
                                });
							}
						});
	}
	//tab切换事件
	function refreshNGRecordsTable(deviceSiteCode) {
		tab=deviceSiteCode;
		$("#deviceSiteCode").val(deviceSiteCode);
		$("#searchStartTime").datebox('setValue', '');
		$("#searchEndTime").datebox('setValue', '');
		$("#searchStatus").selectpicker('val',"");
		$("#searchLostTimeType").selectpicker('val',"");
		$("#showLostTimeTable").bootstrapTable("refresh", {
			url : contextPath + "mcLostTime/findLostTimeList.do",
			cache:false,
			query : {
				deviceSiteCode : deviceSiteCode
			}
		});
		$.get("mcequipment/findDeviceSiteId.do", {
			deviceSiteCode : deviceSiteCode
		}, function(data) {
			Devid = data.id;
			console.log(Devid);
		});
	}

	//条件查询事件
	function toolsearch() {
		var timeend= new Date($("#searchEndTime").val().replace(/-/g,"/")).getTime();
        var timestart = new Date($("#searchStartTime").val().replace(/-/g,"/")).getTime();
        if(timeend<timestart){
	 		$("#alertText").text("开始时间大于结束时间，请重新选择！");
			$("#alertDialog").modal();
			return false;
		}
		var deviceSiteCode = $("#deviceSiteCode").val();
		$("#showLostTimeTable").bootstrapTable("refresh", {
			url : contextPath + "mcLostTime/findLostTimeList.do",
			cache:false,
			query : {
				startTime:$("#searchStartTime").val(),
				endTime:$("#searchEndTime").val(),
				NGCode:$("#searchLostTimeType").val(),
				status:$("#searchStatus").val(),
				deviceSiteCode : deviceSiteCode
			}
		});
	}
	
	
	//初始化损时列表table
	var beginData = "";
	var endData = "";
	var sum = 0;
	var count = 0;
	   function initLostTimeTable() {
		id="";
		//alert(id);
		var deviceSiteCode = $("#deviceSiteCode").val();
		$("#showLostTimeTable").bootstrapTable({
			cache:false,
			height : longTableHeight,
			//idField : 'id',
			 //singleSelect : false,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
					searchText : "",
					deviceSiteCode :deviceSiteCode
				};
				return temp;
			},responseHandler :function(res){
				count = res.length;
				sum = 0;
				for(var i=0;i<res.length;i++){
					sum += res[i].sumOfLostTime;
				}
				sum = (sum).toFixed(2);
				$("#sum").text(sum);
				$("#count").text(count);
	            //将服务端你的数据转换成bootstrap table 能接收的类型
	            return res;
	        },
			columns : [{

	            checkbox:true,

	            field : '',

	            title : ''

	         },{
				field : 'id',
				title : 'Id'
			}, 
			{
				field : 'beginTime',
				align : 'center',
				title : '开始',
				formatter : function(value, row, index) {
					if (value) {
						var date = new Date(value);
						return date.getFullYear() + '-'
								+ ((date.getMonth() + 1)>9? (date.getMonth() + 1):("0"+(date.getMonth() + 1)))+'-'
								+ (date.getDate()>9? date.getDate():("0"+date.getDate())) + " "
								+ (date.getHours()>9? date.getHours():("0"+date.getHours())) + ":"
								+ (date.getMinutes()>9? date.getMinutes():("0"+date.getMinutes())) + ":"
								+ (date.getSeconds()>9? date.getSeconds():("0"+date.getSeconds()));
					}
				}
			}, {
				field : 'endTime',
				align : 'center',
				title : '结束',
				formatter : function(value, row, index) {
					if (value) {
						var date = new Date(value);
						return date.getFullYear() + '-'
						+ ((date.getMonth() + 1)>9? (date.getMonth() + 1):("0"+(date.getMonth() + 1)))+'-'
						+ (date.getDate()>9? date.getDate():("0"+date.getDate())) + " "
						+ (date.getHours()>9? date.getHours():("0"+date.getHours())) + ":"
						+ (date.getMinutes()>9? date.getMinutes():("0"+date.getMinutes())) + ":"
						+ (date.getSeconds()>9? date.getSeconds():("0"+date.getSeconds()));
					}else{
						var date = new Date();
						return date.getFullYear() + '-'
						+ ((date.getMonth() + 1)>9? (date.getMonth() + 1):("0"+(date.getMonth() + 1)))+'-'
						+ (date.getDate()>9? date.getDate():("0"+date.getDate())) + " "
						+ (date.getHours()>9? date.getHours():("0"+date.getHours())) + ":"
						+ (date.getMinutes()>9? date.getMinutes():("0"+date.getMinutes())) + ":"
						+ (date.getSeconds()>9? date.getSeconds():("0"+date.getSeconds()));
					}
				}
			}, {
				field : 'sumOfLostTime',
				align : 'center',
				title : '损时合计(分)'
			}, {
				field : 'recordUserName',
				align : 'center',
				title : '记录人'
			}, {
				field : 'confirmUserName',
				align : 'center',
				title : '确认人'
			},{
				field : 'confirmTime',
				align : 'center',
				title : '确认时间',
				formatter : function(value, row, index) {
					if (value) {
						var date = new Date(value);
						return date.getFullYear() + '-'
								+ ((date.getMonth() + 1)>9? (date.getMonth() + 1):("0"+(date.getMonth() + 1)))+'-'
								+ (date.getDate()>9? date.getDate():("0"+date.getDate())) + " "
								+ (date.getHours()>9? date.getHours():("0"+date.getHours())) + ":"
								+ (date.getMinutes()>9? date.getMinutes():("0"+date.getMinutes())) + ":"
								+ (date.getSeconds()>9? date.getSeconds():("0"+date.getSeconds()));
					}
				}
			}, {
				field : 'lostTimeTypeName',
				align : 'center',
				title : '损时类型',
				formatter: function (value, row, index) {  
					if(row.reason==null||row.reason==''){
       					 return "等待";
					}else{
						return value;
					}
			    }
			}, {
				field : 'reason',
				align : 'center',
				title : '损时原因',
				formatter: function (value, row, index) {
					if(value==null||value==''){
       					 return "未定义";
					}else{
						return value;
					}
			    }
			} , {
				field : 'description',
				align : 'center',
				title : '详情描述'
			} ]
		});
		$('#showLostTimeTable').bootstrapTable('hideColumn', 'id');
		$("#showLostTimeTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					b_time=row.beginTime;
					e_time=row.endTime;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
					
					var beginTime = new Date(row.beginTime);
					beginData = beginTime.getFullYear() + '-'
							+ ((beginTime.getMonth() + 1)>9? (beginTime.getMonth() + 1):("0"+(beginTime.getMonth() + 1)))+'-'
							+ (beginTime.getDate()>9? beginTime.getDate():("0"+beginTime.getDate())) + " "
							+ (beginTime.getHours()>9? beginTime.getHours():("0"+beginTime.getHours())) + ":"
							+ (beginTime.getMinutes()>9? beginTime.getMinutes():("0"+beginTime.getMinutes())) + ":"
							+ (beginTime.getSeconds()>9? beginTime.getSeconds():("0"+beginTime.getSeconds()));
					var endTime = new Date(row.endTime);
					endData = endTime.getFullYear() + '-'
							+ ((endTime.getMonth() + 1)>9? (endTime.getMonth() + 1):("0"+(endTime.getMonth() + 1)))+'-'
							+ (endTime.getDate()>9? endTime.getDate():("0"+endTime.getDate())) + " "
							+ (endTime.getHours()>9? endTime.getHours():("0"+endTime.getHours())) + ":"
							+ (endTime.getMinutes()>9? endTime.getMinutes():("0"+endTime.getMinutes())) + ":"
							+ (endTime.getSeconds()>9? endTime.getSeconds():("0"+endTime.getSeconds()));
					
					//未处理禁用确认，未确认禁用拆分
					var $splitLostTime= $("#splitLostTime")
					var $confirmLostTime= $("#confirmLostTime")
			    	//$confirmLostTime.attr("onclick","updateLostTime('confirm')");
					if(!row.reason){
						$splitLostTime.removeAttr("onclick");
						var onclickAttr = $splitLostTime.attr("onclick");
						console.log(!onclickAttr)
			    		if(!onclickAttr){
			    			$splitLostTime.attr("onclick","showSplitDialog()");
			    		}
					}else{
						$splitLostTime.removeAttr("onclick");
						var onclickAttr = $splitLostTime.attr("onclick");
						console.log(!onclickAttr)
			    		if(!onclickAttr){
			    			$splitLostTime.attr("onclick","showNoSplitDialog()");
			    		}
					}
					if(!row.confirmUserId){
						$confirmLostTime.removeAttr("onclick");
						var onclickAttr = $confirmLostTime.attr("onclick");
			    		if(!onclickAttr){
			    			$confirmLostTime.attr("onclick","updateLostTime('confirm')");
			    		}
					}else{
						$confirmLostTime.removeAttr("onclick");
						var onclickAttr = $confirmLostTime.attr("onclick");
			    		if(!onclickAttr){
			    			$confirmLostTime.attr("onclick","showComfirmDialog()");
			    		}
					}
				});
	}  
	//点击删除事件
	function deletelosttime() {
		if (id!="") {
			//显示删除确认框
			$("#deleteConfirmDlg").modal();
			
		} else {
			$("#alertText").text("请选择要删除的数据!");
			$("#alertDialog").modal();
			return false;
		}
	};
	//确认删除
	function deleteSure() {
		$.ajax({
			type : "post",
			url : contextPath
					+ "mcLostTime/mcdeleteLostTimeRecord.do",
			data : {
				"rids" : id
			},
			cache:false,
			dataType : "json",
			success : function(data) {
				if (data.success) {
						if(tab=="untreated"){
						//console.log(lostTimeTypeCode);
						$("#showLostTimeTable").bootstrapTable("refresh", {
							url :"mcLostTime/findLostTimeList.do",
							cache:false,
							query : {
                                deviceSiteCode : $("#deviceSiteCode").val(),
								searchText : ""
							}
						});
						}else if(tab=="notSure"){
							$("#showLostTimeTable").bootstrapTable("refresh", {
								url :"mcLostTime/findLostTimeList.do",
								cache:false,
								query : {
                                    deviceSiteCode : $("#deviceSiteCode").val(),
									searchText : ""
								}
							});
							}else if(tab=="historicalRecords"){
								$("#showLostTimeTable").bootstrapTable("refresh", {
									url :"mcLostTime/findLostTimeList.do",
									cache:false,
									query : {
                                        deviceSiteCode : $("#deviceSiteCode").val(),
										searchText : ""
									}
								});
								}
				}
			}
		});
	}
	
	
	
	//损时类型下拉框
	function setlostTimeType() {
		 $.ajax({
				type : "post",
				url : contextPath + "pressLightType/queryFirstLevelType.do?type=PRESSLIGHTTYPE",
				data : {},
				cache:false,
				dataType : "json",
				success : function(data) {
				 	var htmlselect = "<option></option>";
					$.each(data,function(index, Type) {
						htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
						var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
								+ Type.code
								+ "\")' value='"+Type.code+"'>"
								+ Type.name
								+ "</button>");
						var splitButton = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='splitShowDetail(\""
								+ Type.code
								+ "\")' value='"+Type.code+"'>"
								+ Type.name
								+ "</button>");
					$("#lostType").append(button);		
					$("#splitLostType").append(splitButton);		
									})
					$("#lostTimeType").append(htmlselect);		
					$("#lostTimeType").selectpicker('refresh');
					$("#splitAddLostTimeType").append(htmlselect);		
					$("#splitAddLostTimeType").selectpicker('refresh');
					$("#updateLostTimeType").append(htmlselect);
					$("#updateLostTimeType").selectpicker('refresh');
					$("#searchLostTimeType").append(htmlselect);
					$("#searchLostTimeType").selectpicker('refresh');
					setReason();
					setaddReason();
				}
			}) 
	}
	//点击类型事件
	function showDetail(typeCode) {
		$("#lostType button").removeClass("btn btn-primary");
		$("#lostType button").addClass("btn btn-default");
		var btns = $("#lostType button");
		for(var i = 0;i<btns.length;i++){
			var btn = $(btns[i]);
			var textOnButton = btn.val();
			if(textOnButton === typeCode){
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}
		 $.ajax({
				type : "post",
				url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
				data : {"pcode":typeCode},
				cache:false,
				dataType : "json",
				success : function(data) {
					$("#lostReason").empty();
					$.each(data,function(index, Type) {
						var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;height:45px' onclick='showReasonBut(\""
								+ Type.code
								+ "\")' value ='"
								+ Type.code
								+ "'>"
								+ Type.reason
								+ "</button>");
					$("#lostReason").append(button);	
					})
				}
			}) 
	}
	//点击类型事件
	function splitShowDetail(typeCode) {
		$("#splitLostType button").removeClass("btn btn-primary");
		$("#splitLostType button").addClass("btn btn-default");
		var btns = $("#splitLostType button");
		for(var i = 0;i<btns.length;i++){
			var btn = $(btns[i]);
			var textOnButton = btn.val();
			if(textOnButton === typeCode){
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}
		 $.ajax({
				type : "post",
				url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
				data : {"pcode":typeCode},
				cache:false,
				dataType : "json",
				success : function(data) {
					$("#splitLostReason").empty();
					$.each(data,function(index, Type) {
						var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='splitShowReasonBut(\""
								+ Type.code
								+ "\")' value ='"
								+ Type.code
								+ "'>"
								+ Type.reason
								+ "</button>");
					$("#splitLostReason").append(button);	
					})
				}
			}) 
	}
	
	var lostReasonCode = "";  //扫描界面的损时原因code
	function showReasonBut(typeCode) {
		lostReasonCode = "";
		$("#lostReason button").removeClass("btn btn-primary");
		$("#lostReason button").addClass("btn btn-default");
		var btns = $("#lostReason button");
		for(var i = 0;i<btns.length;i++){
			var btn = $(btns[i]);
			var textOnButton = btn.val();
			if(textOnButton === typeCode){
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}
		lostReasonCode = typeCode;
	}
	
	function splitShowReasonBut(typeCode) {
		lostReasonCode = "";
		$("#splitLostReason button").removeClass("btn btn-primary");
		$("#splitLostReason button").addClass("btn btn-default");
		var btns = $("#splitLostReason button");
		for(var i = 0;i<btns.length;i++){
			var btn = $(btns[i]);
			var textOnButton = btn.val();
			if(textOnButton === typeCode){
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}
		lostReasonCode = typeCode;
	}
	//类型改变事件
	$(document).ready(function(){
		$("#updateLostTimeType").change(function() {
			setReason();
		});
		$("#lostTimeType").change(function() {
	         setaddReason();
	    });
		$("#splitAddLostTimeType").change(function() {
	         setSplitReason();
	    });
		 $("#flowSuggestion").change(function(){

	         var data = $("#flowSuggestion option:selected").text();

	         $("#suggestion").val(data)

	      })
	});
	
	//故障原因改变事件
	$(document).ready(function(){
		$("#updateLostTimeReason").change(function() {
		 	var code = $("#updateLostTimeReason option:selected").val()
			$.ajax({
            	type : "post",
	            url : contextPath + "mcPressLightRecord/queryPressLightByCode.do",
	            data : {"code":code},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	$("#updateLostTimeType").selectpicker('val',data.pressLightType.code);
	            }
	        })
		});
		$("#lostTimeReason").change(function() {
			var code = $("#lostTimeReason option:selected").val()
			$.ajax({
            	type : "post",
	            url : contextPath + "mcPressLightRecord/queryPressLightByCode.do",
	            data : {"code":code},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	$("#lostTimeType").selectpicker('val',data.pressLightType.code);
	            }
	        })
	    });
		$("#splitAddLostTimeReason").change(function() {
			var code = $("#splitAddLostTimeReason option:selected").val()
			$.ajax({
            	type : "post",
	            url : contextPath + "mcPressLightRecord/queryPressLightByCode.do",
	            data : {"code":code},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	$("#splitAddLostTimeType").selectpicker('val',data.pressLightType.code);
	            }
	        })
	    });
	});
	
	//新增损时原因下拉框
	   function setaddReason() {
	      var pcode = $("#lostTimeType option:selected").val()
	      if(!pcode){
	    	  getAllReasonAdd();
	      }else{
	       		$.ajax({
	            	type : "post",
		            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
		            data : {"pcode":pcode},
					cache:false,
		            dataType : "json",
		            success : function(data) {
		            	if(jQuery.isEmptyObject(data)){
							getAllReasonAdd();
		            	}else{
		                var htmlselect = "<option></option>";
			               $.each(data,function(index, Type) {
			                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
			               })
			               $("#lostTimeReason").empty();
			               $("#lostTimeReason").append(htmlselect);		
			               $("#lostTimeReason").selectpicker('refresh');
			               }
		            	}
		        })
	      }
				
	   }
	//拆分损时原因下拉框
	   function setSplitReason() {
	      var pcode = $("#splitAddLostTimeType option:selected").val()
	      if(!pcode){
	    	  getAllReasonAdd();
	      }else{
	       		$.ajax({
	            	type : "post",
		            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
		            data : {"pcode":pcode},
					cache:false,
		            dataType : "json",
		            success : function(data) {
		            	if(jQuery.isEmptyObject(data)){
							getAllReasonAdd();
		            	}else{
		                var htmlselect = "<option></option>";
			               $.each(data,function(index, Type) {
			                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
			                  /* var button = $("<button class='btn btn-default' type='button' style='margin-right:5px;width:80px' onclick='showDetail(\""
										+ Type.code
										+ "\")' value='"+Type.code+"'>"
										+ Type.reason
										+ "</button>");
							$("#splitLostType").append(button); */
			               })
			               $("#splitAddLostTimeReason").empty();
			               $("#splitAddLostTimeReason").append(htmlselect);		
			               $("#splitAddLostTimeReason").selectpicker('refresh');
			               }
		            	}
		        })
	      }
				
	   }
	//损时原因下拉框
	function setReason() {
		var pcode = $("#updateLostTimeType option:selected").val()
		if(!pcode){
			getAllReasonUpdate();
		}
		 $.ajax({
				type : "post",
				url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
				data : {"pcode":pcode},
				cache:false,
				dataType : "json",
				success : function(data) {
					if(jQuery.isEmptyObject(data)){
						getAllReasonUpdate();
					}else{
				 		var htmlselect = "<option></option>";
						$.each(data,function(index, Type) {
							htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
						})
						
						$("#updateLostTimeReason").empty();
						$("#updateLostTimeReason").append(htmlselect);		
						$("#updateLostTimeReason").selectpicker('refresh');
					}
					$("#updateLostTimeReason").selectpicker('val',pressLightCode);
					pressLightCode='';
				}
			}) 
	}
	//选中的类型下没有故障原因时，获取所有故障原因(修改)
	function getAllReasonUpdate(){
		$.ajax({
			type : "post",
			url : contextPath + "mcPressLight/getAllPressLight.do",
			data : {},
			cache:false,
			dataType : "json",
			success : function(data) {
			 	var htmlselect = "<option></option>";
				$.each(data,function(index, Type) {
					htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
				})
				
				$("#updateLostTimeReason").empty();
				$("#updateLostTimeReason").append(htmlselect);		
				$("#updateLostTimeReason").selectpicker('refresh');
			}
		}) 
	}
	//选中的类型下没有故障原因时，获取所有故障原因(新增)
	function getAllReasonAdd(){
		 $.ajax({
         	type : "post",
	            url : contextPath + "mcPressLight/getAllPressLight.do",
	            data : {},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	//alert(JSON.stringify(data));
	                var htmlselect = "<option></option>";
	               $.each(data,function(index, Type) {
	                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
	               })
	               $("#lostTimeReason").empty();
	               $("#lostTimeReason").append(htmlselect);		
	               $("#lostTimeReason").selectpicker('refresh');
	               $("#splitAddLostTimeReason").empty();
	               $("#splitAddLostTimeReason").append(htmlselect);		
	               $("#splitAddLostTimeReason").selectpicker('refresh');
						
	            }
	        })
	}
	//模态框点击返回事件(hide)
	function modelHide() {
		$('#showConfirmDlg').modal('hide');
		$('#showUpdateDlg').modal('hide');
		
	}
	//拆分模态框点击返回事件(hide)
	function splitModelHide() {
		$('#splitShowConfirmDlg').modal('hide');
		$('#splitShowUpdateDlg').modal('hide');
		
	}
	//显示增加、修改、确认页面
	function showConfirmDlg(e) {
		if(e=="add"){	
			var date = new Date();
			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
					+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
					+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
			$("#endTime").datetimebox('setValue', value);
			$("#startTime").datetimebox('setValue', value);
			$("#addLostTime").val("0");
		    //$('.selectpicker').selectpicker('val',null);	
		    $('#lostTimeType').selectpicker('val',"");
		    $('#lostTimeReason').selectpicker('val',"");
		    $('#showConfirmDlg').modal('show');
			$('#submitbtn').attr("onclick","submitclick('add')");
		}else if(e=="confirm"){
			$('#showUpdateDlg').modal('show');
		$('#submitbtn').attr("onclick","submitclick('confirm')");
	
		}else if(e=="update"){
			$('#showUpdateDlg').modal('show');
		$('#submitbtn').attr("onclick","submitclick('update')");
		}else if(e=="split"){
		    $('#splitShowConfirmDlg').modal('show');
			$('#splitAddLostTimeType').selectpicker('val',"");
			$('#splitAddLostTimeReason').selectpicker('val',"");
		}
		
	}
	//时间改变事件
	$(function(){
		 $("#updateStartTime").datetimebox({
			 onChange: function(date){
					var timeend= new Date($("#updateEndTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($("#updateStartTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         if(!$("#updateEndTime").datetimebox('getValue')||!$("#updateStartTime").datetimebox('getValue')){
			        	 
			         }else{
			        	 if(timeend>=timestart){
			        	        var time = (timeend-timestart)/(1000*60);
			       	            time = time.toFixed(2);
			      	            $("#updateLostTime").val(time);
			      	   }else{
			       	 			$("#alertText").text("开始时间大于结束时间，请重新选择！");
								$("#alertDialog").modal();
								updateLostTime();
			         }
			         }
				}
			});
	      $("#updateEndTime").datetimebox({
	    	  onChange: function(date){
	    		  //alert($("#updateEndTime").datetimebox('getValue'));
	    		//  alert($("#updateStartTime").datetimebox('getValue')); 
					var timeend= new Date($("#updateEndTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($("#updateStartTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         if(!$("#updateEndTime").datetimebox('getValue')||!$("#updateStartTime").datetimebox('getValue')){
			        	 
			         }else{
			        	 if(timeend>=timestart){
			        	        var time = (timeend-timestart)/(1000*60);
			       	            time = time.toFixed(2);
			      	            $("#updateLostTime").val(time);
			      	   }else{
			       	 			$("#alertText").text("开始时间大于结束时间，请重新选择！");
								$("#alertDialog").modal();
								updateLostTime();
			         }
			         }
				}
			});
		 $("#startTime").datetimebox({
				onChange: function(date){
					//alert($('#startTime').datetimebox('getValue'));
					var timeend= new Date($('#endTime').datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($('#startTime').datetimebox('getValue').replace(/-/g,"/")).getTime();
			         if(timeend>=timestart){
			            var time = (timeend-timestart)/(1000*60);
			            time = time.toFixed(2);
			            $("#addLostTime").val(time);
			         }else{
			        		$("#alertText").text("开始时间大于结束时间，请重新选择！");
							$("#alertDialog").modal();
							$("#startTime").datetimebox('setValue', dateForm);
					 		$("#endTime").datetimebox('setValue', dateForm);
			         }
				}
			});
	      $("#endTime").datetimebox({
				onChange: function(date){
					//alert($('#startTime').datetimebox('getValue'));
					var timeend= new Date($('#endTime').datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($('#startTime').datetimebox('getValue').replace(/-/g,"/")).getTime();
			         if(timeend>=timestart){
			            var time = (timeend-timestart)/(1000*60);
			            time = time.toFixed(2);
			            $("#addLostTime").val(time);
			         }else{
			        		$("#alertText").text("开始时间大于结束时间，请重新选择！");
							$("#alertDialog").modal();
							$("#startTime").datetimebox('setValue', dateForm);
					 		$("#endTime").datetimebox('setValue', dateForm);
			         }
				}
			});
	      $("#splitAddStartTime").datetimebox({
				onChange: function(date){
					var timeend= new Date($("#splitAddEndTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($("#splitAddStartTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var entTimeEnd = new Date(endVal.replace(/-/g,"/")).getTime();
			         var startTimeStart = new Date(startVal.replace(/-/g,"/")).getTime();
			         if(timeend&&timestart){
				         if(timestart>entTimeEnd){
				        	 	 $("#alertText").text("开始时间大于时间范围，请重新选择");
								 $("#alertDialog").modal();
								 $("#splitAddStartTime").datetimebox('setValue',startVal);
								 $("#splitAddEndTime").datetimebox('setValue',endVal);
				         }else if(timestart<startTimeStart){
				        	 	 $("#alertText").text("开始时间小于时间范围，请重新选择");
								 $("#alertDialog").modal();
								 $("#splitAddStartTime").datetimebox('setValue',startVal);
								 $("#splitAddEndTime").datetimebox('setValue',endVal);
				         }else if(timeend>=timestart){
				           		 var time = (timeend-timestart)/(1000*60);
				            	 time = time.toFixed(2);
				            	 $("#splitAddLostTime").val(time);
				         }else{
				        		 $("#alertText").text("开始时间大于结束时间，请重新选择！");
								 $("#alertDialog").modal();
								 $("#splitAddStartTime").datetimebox('setValue',startVal);
								 $("#splitAddEndTime").datetimebox('setValue',endVal);
				         }
			         }
			     }
			});
	      $("#splitAddEndTime").datetimebox({
	    	  onChange: function(date){
					var timeend= new Date($("#splitAddEndTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var timestart = new Date($("#splitAddStartTime").datetimebox('getValue').replace(/-/g,"/")).getTime();
			         var entTimeEnd = new Date(endVal.replace(/-/g,"/")).getTime();
			         var startTimeStart = new Date(startVal.replace(/-/g,"/")).getTime();
			         
				if(timeend&&timestart){
			    	 if(timeend>=timestart&&timeend<=entTimeEnd&&timestart>=startTimeStart){
			        	 var time = (timeend-timestart)/(1000*60);
		            	 time = time.toFixed(2);
		            	 $("#splitAddLostTime").val(time);
			         }else{
			        	 $("#alertText").text("不在时间范围内，请重新选择!");
						 $("#alertDialog").modal();
						 $("#splitAddStartTime").datetimebox('setValue',startVal);
						 $("#splitAddEndTime").datetimebox('setValue',endVal);
			         }
			       }
				}
			}); 
      }) 
	
	//弹出框确认点击事件(对勾)
	function submitclick(e) {
		if(e=="add"){
			var timeend=$("#endTime").val();
	        var timestart =$("#startTime").val();
	        var sumOfLostTime=$("#addLostTime").val()
	        if(sumOfLostTime==0){
	        	$("#alertText").text("损时合计时间不能等于0，请重新选择时间!");
				$("#alertDialog").modal();
				return false;
	        }
	            $.get("mcLostTime/mcAddLostTimeList.do",
			               {
							  beginTime:timestart,
	                		  endTime:timeend,
			                  lostTimeTypeCode:$("#lostTimeType").val(),
			                  lostTimeTypeName:$("#lostTimeType").find("option:selected").text(),
			                  sumOfLostTime:($("#addLostTime").val()),   
			                  'deviceSite.id':Devid,
			                  reason:$("#lostTimeReason").find("option:selected").text(),
			                  pressLightCode:$("#lostTimeReason").val(),
			                  pressLightCode:$("#lostTimeReason").val()
			               },function(data){
			                     $("#alertText").text(data.msg);
			                     $("#alertDialog").modal();
			                     $('#showConfirmDlg').modal('hide');
			                    
			                  if(data.success){
			                     /* if($("#lostTimeReason").val()==""||$("#lostTimeReason").val()==null||$("#lostTimeReason").val()==undefined){
			                    	  $('[dataid="untreated"]').tab("show");
			                          $("#showLostTimeTable").bootstrapTable("refresh", {
			                        	  url :"mcLostTime/findLostTimeList.do",
			              				  cache:false,
			                        	  query : {
				     							searchText : ""
				     						}
				     					});
			                     }else{
			                    	 $('[dataid="notSure"]').tab('show');
			                    	 $("#showLostTimeTable").bootstrapTable("refresh", {
			                    		 url :"mcLostTime/findLostTimeList.do",
			             				 cache:false,	
			                    		 query : {
				     							searchText : ""
				     						}
				     					});
			                     }  */
			                    	 $('[dataid='+tab+']').tab('show');
			                	   $("#showLostTimeTable").bootstrapTable("refresh", {
			              			url : contextPath + "mcLostTime/findLostTimeList.do",
			              			cache:false,
			              			query : {
			              				deviceSiteCode : tab
			              			}
			              		}); 
			                     
			                  }
			               })
			
		}
	}
	
	//判断损时代码
	function lostTimeTypeSelect(e) {
		var	lostTimeCode=$("#lostTimeCode").val();
		lostTimeCode = lostTimeCode.replace(/(^\s*)|(\s*$)/g, '');
		if(lostTimeCode==null||lostTimeCode==undefined || lostTimeCode==""){
			return false;
		}else{
			$.get("mcPressLightRecord/queryPressLightByCode.do",{
				code:lostTimeCode
			},function(data){
				if(!data){
					$("#lostTimeCode").val("");
					$("#alertText").text("损时类别不存在，请重新输入");
					$("#alertDialog").modal();
					return false;
				}
					$('#scanLostTimeCodeDialog').modal('hide');
					
					showConfirmDlg(param);
					if(e=="add"){
						/* $("#lostTimeReason").html("<option></option><option value='"+data.code+"'>"+data.reason+"</option>");
						$('#lostTimeReason').selectpicker('val',data.code);
			            $("#lostTimeReason").selectpicker('refresh'); */
						$.get("mcPressLight/getParentTypeBytypeId.do",{
							typeid:data.pressLightType.id
						},function(type){
							if(type.basicCode==null){
								$('#lostTimeType').selectpicker('val',type.code);
								$.ajax({
					            	type : "post",
						            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
						            data : {"pcode":type.code},
									cache:false,
						            dataType : "json",
						            success : function(datas) {
						            	if(jQuery.isEmptyObject(datas)){
											getAllReasonAdd();
						            	}else{
						                var htmlselect = "<option></option>";
							               $.each(datas,function(index, Type) {
							                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
							               })
							               $("#lostTimeReason").empty();
							               $("#lostTimeReason").append(htmlselect);		
    										$('#lostTimeReason').selectpicker('val',data.code);
								            $("#lostTimeReason").selectpicker('refresh');
							               }
						            	}
						        })
			               }else{
			            	    $('#lostTimeType').selectpicker('val',type.basicCode);
			            	    $.ajax({
					            	type : "post",
						            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
						            data : {"pcode":type.basicCode},
									cache:false,
						            dataType : "json",
						            success : function(datas) {
						            	if(jQuery.isEmptyObject(datas)){
											getAllReasonAdd();
						            	}else{
						                var htmlselect = "<option></option>";
							               $.each(datas,function(index, Type) {
							                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
							               })
							               $("#lostTimeReason").empty();
							               $("#lostTimeReason").append(htmlselect);		
    										$('#lostTimeReason').selectpicker('val',data.code);
								            $("#lostTimeReason").selectpicker('refresh');
							               }
						            	}
						        })
			               }
						})
					}else{
						/* $("#updateLostTimeReason").html("<option></option><option value='"+data.code+"'>"+data.reason+"</option>");
						$('#updateLostTimeReason').selectpicker('val',data.code);
			            $("#updateLostTimeReason").selectpicker('refresh'); */
						$.get("mcPressLight/getParentTypeBytypeId.do",{
							typeid:data.pressLightType.id
						},function(type){
							if(type.basicCode==null){
								$('#updateLostTimeType').selectpicker('val',type.code);
								 $.ajax({
						            	type : "post",
							            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
							            data : {"pcode":type.code},
										cache:false,
							            dataType : "json",
							            success : function(datas) {
							            	if(jQuery.isEmptyObject(datas)){
												getAllReasonAdd();
							            	}else{
							                var htmlselect = "<option></option>";
								               $.each(datas,function(index, Type) {
								                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
								               })
								               $("#updateLostTimeReason").empty();
								               $("#updateLostTimeReason").append(htmlselect);		
												$('#updateLostTimeReason').selectpicker('val',data.code);
									            $("#updateLostTimeReason").selectpicker('refresh');
								               }
							            	}
							        })
			               }else{
			            	    $('#updateLostTimeType').selectpicker('val',type.basicCode);
			            	    $.ajax({
					            	type : "post",
						            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
						            data : {"pcode":type.basicCode},
									cache:false,
						            dataType : "json",
						            success : function(datas) {
						            	if(jQuery.isEmptyObject(datas)){
											getAllReasonAdd();
						            	}else{
						                var htmlselect = "<option></option>";
							               $.each(datas,function(index, Type) {
							                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
							               })
							               $("#updateLostTimeReason").empty();
							               $("#updateLostTimeReason").append(htmlselect);		
											$('#updateLostTimeReason').selectpicker('val',data.code);
								            $("#updateLostTimeReason").selectpicker('refresh');
							               }
						            	}
						        })
			               }
						})
					}
			})
		}
	}
	//判断损时代码
	function splitLostTimeTypeSelect(e) {
		var	lostTimeCode=$("#splitLostTimeCode").val();
		lostTimeCode = lostTimeCode.replace(/(^\s*)|(\s*$)/g, '');
		if(lostTimeCode==null||lostTimeCode==undefined || lostTimeCode==""){
			$("#alertText").text("请输入损时代码！");
			$("#alertDialog").modal();
		}else{
			$.get("mcPressLightRecord/queryPressLightByCode.do",{
				code:lostTimeCode
			},function(data){
				if(!data){
					$("#splitLostTimeCode").val("");
					$("#alertText").text("损时类别不存在，请重新输入");
					$("#alertDialog").modal();
					return false;
				}
					$('#splitScanLostTimeCodeDialog').modal('hide');
					
					showConfirmDlg("split");
					
					$("#splitAddLostTimeReason").html("<option></option><option value='"+data.code+"'>"+data.reason+"</option>");
					$('#splitAddLostTimeReason').selectpicker('val',data.code);
					$("#splitAddLostTimeReason").selectpicker('refresh');
		      //      $("#lostTimeReason").val(data.reason);
					$.get("mcPressLight/getParentTypeBytypeId.do",{
						typeid:data.pressLightType.id
					},function(type){
						if(type.basicCode==null){
							$('#splitAddLostTimeType').selectpicker('val',type.code);
							$.ajax({
				            	type : "post",
					            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
					            data : {"pcode":type.code},
								cache:false,
					            dataType : "json",
					            success : function(datas) {
					            	if(jQuery.isEmptyObject(datas)){
										getAllReasonAdd();
					            	}else{
					                var htmlselect = "<option></option>";
						               $.each(datas,function(index, Type) {
						                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
						               })
						               $("#splitAddLostTimeReason").empty();
						               $("#splitAddLostTimeReason").append(htmlselect);		
										$('#splitAddLostTimeReason').selectpicker('val',data.code);
							            $("#splitAddLostTimeReason").selectpicker('refresh');
						               }
					            	}
					        })
		               }else{
		            	    $('#splitAddLostTimeType').selectpicker('val',type.basicCode);
		            	    $.ajax({
				            	type : "post",
					            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
					            data : {"pcode":type.basicCode},
								cache:false,
					            dataType : "json",
					            success : function(datas) {
					            	if(jQuery.isEmptyObject(datas)){
										getAllReasonAdd();
					            	}else{
					                var htmlselect = "<option></option>";
						               $.each(datas,function(index, Type) {
						                  htmlselect += "<option value='"+Type.code+"'>"+Type.reason+"</option>";
						               })
						               $("#splitAddLostTimeReason").empty();
						               $("#splitAddLostTimeReason").append(htmlselect);		
										$('#splitAddLostTimeReason').selectpicker('val',data.code);
							            $("#splitAddLostTimeReason").selectpicker('refresh');
						               }
					            	}
					        })
		               }
					})
			})
		}
	}
	 
	
	//显示扫描条码窗口
	function showScanLostTimeCodeDialog() {
		$("#lostType button").removeClass("btn btn-primary");
		$("#lostType button").addClass("btn btn-default");
	 	$('#lostReason').empty();
	 	$('#showConfirmDlg').modal('hide');
	 	$('#showUpdateDlg').modal('hide');
	 	$('#scanLostTimeCodeDialog').modal('show');
	 	$("#lostTimeCode").val("");
	 	$('#scanLostTimeCodeDialog').on('shown.bs.modal', function(e) {
			$('#lostTimeCode').focus();
		});
	}
	//显示拆分扫描条码窗口
	function splitShowScanLostTimeCodeDialog(e) {
		$("#splitLostType button").removeClass("btn btn-primary");
		$("#splitLostType button").addClass("btn btn-default");
	 	$('#splitLostReason').empty();
		$("#splitAddEndTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
		$("#splitAddStartTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
	 	$('#splitShowConfirmDlg').modal('hide');
	 	if(e=='add'){
	 		if(splitObject.sumOfLostTime==0){
				$("#alertText").text("可拆分的损时时间为0，不可再新增！");
	 			$("#alertDialog").modal();
	 			return false;
			}
			endVal = splitObject.endTime;
			startVal = splitObject.beginTime;
			$("#splitAddEndTime").datetimebox('setValue',splitObject.endTime)
			$("#splitAddStartTime").datetimebox('setValue',splitObject.beginTime)
			$("#splitAddLostTime").val(splitObject.sumOfLostTime)
		 	$('#splitScanLostTimeCodeDialog').modal('show');
		 	$("#splitLostTimeCode").val("");
		 	$('#splitScanLostTimeCodeDialog').on('shown.bs.modal', function(e) {
				$('#splitLostTimeCode').focus();
			});
	 	}else if(e=='update'){
	 		var data = $('#splitLostTimeTab').bootstrapTable('getSelections');
	 		if(data[0].id==id){
	 			$("#alertText").text("该记录不可操作!");
	 			$("#alertDialog").modal();
	 			return false;
	 		}else{//  $('#splitAddLostTimeType').selectpicker('val',type.basicCode);
	 			startVal = data[0].beginTime;
	 			endVal = data[0].endTime;
	 			$("#splitAddLostTimeType").selectpicker('val',data[0].pressLightTypeCode);
	 			$("#splitAddLostTimeReason").selectpicker('val',data[0].pressLightCode);
	 			$("#splitAddLostTime").val(data[0].sumOfLostTime);
	 			$("#splitAddStartTime").datetimebox('setValue',data[0].beginTime);
	 			$("#splitAddEndTime").datetimebox('setValue',data[0].endTime);
	 			$('#splitScanLostTimeCodeDialog').modal('show');
			 	$("#splitLostTimeCode").val("");
			 	$('#splitScanLostTimeCodeDialog').on('shown.bs.modal', function(e) {
					$('#splitLostTimeCode').focus();
				});
	 		}
	 	}
	}

	//点击手工输入事件
	function manualinput(){
		$('#scanLostTimeCodeDialog').modal('hide');
		showConfirmDlg("add");
		$('#lostTimeCode').val(lostReasonCode);
		$('#lostTimeCode').keydown();
		
	}
	//损时原因点击确认事件
	function confirminput(){
		if(lostReasonCode){
			$('#lostTimeCode').val(lostReasonCode);
			$('#lostTimeCode').change();
		}else{
			$("#alertText").text("请选择损时原因！");
			$("#alertDialog").modal();
		}
		lostReasonCode = "";
	}
	//损时原因点击确认事件
	function splitConfirminput(){
		if(lostReasonCode){
			$('#splitLostTimeCode').val(lostReasonCode);
			$('#splitLostTimeCode').change();
		}else{
			$("#alertText").text("请选择损时原因！");
			$("#alertDialog").modal();
		}
		lostReasonCode = "";
	}
	//点击手工输入事件
	function splitManualinput(){
		$('#splitScanLostTimeCodeDialog').modal('hide');
		showConfirmDlg("split");
		$('#splitLostTimeCode').val(lostReasonCode);
		$('#splitLostTimeCode').keydown();
		
	}
	//修改二维码扫描框改变事件
	$('#pressLightCode').change(function() {
		$('#updateRcodeDialog').modal('hide');
		$("#pressLightCode").val($(this).val()).change();//key值
	});
	
//修改损时记录
var ids ="";//数据的id字符串
function updateLostTime(pmt){
	var alldata=$("#showLostTimeTable").bootstrapTable('getSelections');

	   var sum = alldata.length;
	param="";
	 if(sum<1){//id==''
      $("#alertText").text("请选择数据");
		$("#alertDialog").modal();
		return false;
	}
	$("#updateEndTime").datetimebox({
			required: true,
			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
	});
	$("#updateStartTime").datetimebox({
			required: true,
			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
	});
	$("#updateLostTimeReason").empty();
	if(sum==1){

	       $.ajax({

	            type : "post",

	            url : contextPath + "lostTimeRecord/queryLostTimeRecordById.do",

	            data : {"id":alldata[0].id},

	            cache:false,

	            dataType : "json",

	            success : function(data) {

	               var sTime =data.beginTime;

	               var eTime =data.endTime;
	               setTime(sTime,eTime);

	               $("#updateLostTimeType").selectpicker('val',data.lostTimeTypeCode);

	               $("#updateLostTimeType").change();

	               pressLightCode = data.pressLightCode;
					$('#showUpdateDlg').modal('show');
	            }

	         }) 

	      }else{

	         var begindate =  new Date(alldata[sum-1].beginTime);

	         var sTime =   begindate.getFullYear() + '-'

	         + ((begindate.getMonth() + 1)>9? (begindate.getMonth() + 1):("0"+(begindate.getMonth() + 1)))+'-'

	         + (begindate.getDate()>9? begindate.getDate():("0"+begindate.getDate())) + " "

	         + (begindate.getHours()>9? begindate.getHours():("0"+begindate.getHours())) + ":"

	         + (begindate.getMinutes()>9? begindate.getMinutes():("0"+begindate.getMinutes())) + ":"

	         + (begindate.getSeconds()>9? begindate.getSeconds():("0"+begindate.getSeconds()));

	         var enddate =  new Date(alldata[0].endTime);

	         var eTime =   enddate.getFullYear() + '-'

	         + ((enddate.getMonth() + 1)>9? (enddate.getMonth() + 1):("0"+(enddate.getMonth() + 1)))+'-'

	         + (enddate.getDate()>9? enddate.getDate():("0"+enddate.getDate())) + " "

	         + (enddate.getHours()>9? enddate.getHours():("0"+enddate.getHours())) + ":"

	         + (enddate.getMinutes()>9? enddate.getMinutes():("0"+enddate.getMinutes())) + ":"

	         + (enddate.getSeconds()>9? enddate.getSeconds():("0"+enddate.getSeconds()));
	         setTime(sTime,eTime);
	         ids = alldata[0].id;

	         for(var i=1;i<alldata.length;i++){

	            ids+=","+alldata[i].id;
			}
	         $("#updateLostTimeType").selectpicker('val',alldata[0].lostTimeTypeCode);
	         $("#updateLostTimeType").change();
	         pressLightCode = alldata[0].pressLightCode;
	         $('#showUpdateDlg').modal('show');
	      }
	      param=pmt;
}
//时间点设置
function setTime(sTime,eTime){
	$('#updateEndTime').datetimebox('setValue', eTime);
	$('#updateStartTime').datetimebox('setValue', sTime);
	var timestart = new Date(sTime.replace(/-/g,"/")).getTime();
	var timeend = new Date(eTime.replace(/-/g,"/")).getTime();
	var time = (timeend-timestart)/(1000*60);
	time = time.toFixed(2);
	$("#updateLostTime").val(time);
}

//修改
function updateLostTimeRecord(){
	//多条数据修改
	   if(ids){

	      var timeend= $("#updateEndTime").val();

	       var timestart = $("#updateStartTime").val();
	      $.get("mcLostTime/mcupdateLostTimeByIds.do", {

	         ids : ids,

	         reasonCode : $("#updateLostTimeReason").val(),

	      }, function(data) {

	         if (data.success) {

	            $("#alertText").text("修改成功");

	            $("#alertDialog").modal();

	            $('#showUpdateDlg').modal('hide');

	            if(tab=="notSure"){

	               $("#showLostTimeTable").bootstrapTable("refresh", {

	                  url :"mcLostTime/findLostTimeList.do",

	                  cache:false,

	                  query : {
                          deviceSiteCode : $("#deviceSiteCode").val(),
	                     searchText : ""

	                  }

	               });

	            }else{

	               $("#showLostTimeTable").bootstrapTable("refresh", {

	                  url :"mcLostTime/findLostTimeList.do",

	                  cache:false,

	                  query : {
                          deviceSiteCode : $("#deviceSiteCode").val(),
							searchText : ""
						}
					});
	   			

	            }

	            ids="";

	            id="";

	             if($("#updateLostTimeReason").val()==""||$("#updateLostTimeReason").val()==null||$("#updateLostTimeReason").val()==undefined){

	                   $('[dataid="untreated"]').tab("show");

	                    $("#showLostTimeTable").bootstrapTable("refresh", {

	                       url :"mcLostTime/findLostTimeList.do",

	                     cache:false,	

	                       query : {
                               deviceSiteCode : $("#deviceSiteCode").val(),
	                        searchText : ""

	                     }

	                  });

	               }else{

	                  $('[dataid="notSure"]').tab('show');

	                  $("#showLostTimeTable").bootstrapTable("refresh", {

	                         url :"mcLostTime/findLostTimeList.do",

	                   cache:false,		

	                        query : {
                                deviceSiteCode : $("#deviceSiteCode").val(),
	                        searchText : ""

	                        }

	                  });

	               }

	         } 

	      });

	   }else{

	      var timeend= $("#updateEndTime").val();

	       var timestart = $("#updateStartTime").val();

		     	

	      $.get("mcLostTime/mcUpdateLostTimeList.do", {
	         id : id,

	         lostTimeTypeCode : $("#updateLostTimeType").val(),

	         reason :$("#updateLostTimeReason option:selected").text(),

	         pressLightCode : $("#updateLostTimeReason").val(),

	         sumOfLostTime : ($('#updateLostTime').val()),

	         beginTime : timestart,

	         endTime : timeend,

	         'deviceSite.code' : $("#deviceSiteCode").val()

	      }, function(data) {

	         if (data.success) {

	            $("#alertText").text("修改成功");

	            $("#alertDialog").modal();

	            $('#showUpdateDlg').modal('hide');

	            /* if(tab=="notSure"){

	               $("#showLostTimeTable").bootstrapTable("refresh", {

	                  url :"mcLostTime/findLostTimeList.do",

	                  cache:false,

	                  query : {
                          deviceSiteCode : $("#deviceSiteCode").val(),
							searchText : ""
							}
					});
	            }else{

		               $("#showLostTimeTable").bootstrapTable("refresh", {

		                  url :"mcLostTime/findLostTimeList.do",

		                  cache:false,

		                  query : {
                              deviceSiteCode : $("#deviceSiteCode").val(),
		                     searchText : ""

		                  }

		               });
		            } */
		            id="";

		             if($("#updateLostTimeReason").val()==""||$("#updateLostTimeReason").val()==null||$("#updateLostTimeReason").val()==undefined){

		                   $('[dataid="untreated"]').tab("show");

		                    $("#showLostTimeTable").bootstrapTable("refresh", {

		                       url :"mcLostTime/findLostTimeList.do",

		                     cache:false,	

		                       query : {
                                   deviceSiteCode : $("#deviceSiteCode").val(),

		                        searchText : ""
		                     }

		                  });

		               }else{

		                  $('[dataid="notSure"]').tab('show');

		                  $("#showLostTimeTable").bootstrapTable("refresh", {

		                         url :"mcLostTime/findLostTimeList.do",

		                   cache:false,		

		                        query : {
                                    deviceSiteCode : $("#deviceSiteCode").val(),
		                        searchText : ""

		                        }

		                  });

		               }

		         }else{
	        	 	$("#alertText").text(data.message);
		            $("#alertDialog").modal();
		            $('#showUpdateDlg').modal('hide');
	         	}
		      });

		   }
}
//修改或确认
function submitUpdate(){
	if(param=="update"){
		$('#updateConfirmDlg').modal('show');
	}else{
		//$('#scanUserCodeDialog').modal('show');
		$('#scanUserCodeDialog').modal('show');
		$('#showUpdateDlg').modal('hide');
	 	$("#userCode").val("");
	 	$('#scanUserCodeDialog').on('shown.bs.modal', function(e) {
			$('#userCode').focus();
	 	});
	}
}
//NG确认

var userid=''; //确认人ID

function submitConfirm(){

	if(ids){

	      var timeend= $("#updateEndTime").val();

	       var timestart = $("#updateStartTime").val();

	       $.get("mcLostTime/mcConfirmLostTimeByIds.do",{

	           ids : ids,

	          reasonCode : $("#updateLostTimeReason").val(),

	          confirmUserId : userid,

	          suggestion : $("#suggestion").val()

	        },function(type){

	            $("#alertText").text(type.message);

	            $("#alertDialog").modal();

					

	            $('[dataid="historicalRecords"]').tab("show");

	            $("#showLostTimeTable").bootstrapTable("refresh", {

	               url :"mcLostTime/findLostTimeList.do",

	                 cache:false,

	               query : {
                         deviceSiteCode : $("#deviceSiteCode").val(),
	                     searchText : ""

	                  }

	               });

	        })

	   }else{

	       var timeend= $("#updateEndTime").val();

	       var timestart = $("#updateStartTime").val();

	       $.get("mcLostTime/mcConfirmLostTimeList.do",{

	           id : id,

	          lostTimeTypeCode : $("#updateLostTimeType").val(),

	          //lostTimeTypeName : $("#updateLostTimeType").text(),

	          reason :$("#updateLostTimeReason option:selected").text(),

	          pressLightCode : $("#updateLostTimeReason").val(),

	          sumOfLostTime : ($('#updateLostTime').val()),

	          beginTime : timestart,

	          endTime : timeend,

	          'deviceSite.code' : $("#deviceSiteCode").val(),

	          confirmUserId : userid,

	          recoverMethod : $("#suggestion").val()

	        },function(type){

	            $("#alertText").text(type.message);

	            $("#alertDialog").modal();

					

	            $('[dataid="historicalRecords"]').tab("show");

	            $("#showLostTimeTable").bootstrapTable("refresh", {

	               url :"mcLostTime/findLostTimeList.do",

	                 cache:false,

	               query : {
                         deviceSiteCode : $("#deviceSiteCode").val(),
	                     searchText : ""

	                  }

	               });

	        })

	   }
}
//损时确认人扫码框失去焦点事件
function lostfocus(){
	$("#userCode").focus();
	$("#lostTimeCode").focus();
	$("#splitLostTimeCode").focus();
}

//判断损时确认权限
function lostTimeConfirm() {
    var    userCode=$("#userCode").val();
   // lostTimeCode = lostTimeCode.replace(/(^\s*)|(\s*$)/g, '');
    if(userCode==null||userCode=='undefined' || userCode==""){
        $("#alertText").text("请输入员工代码！");
        $("#alertDialog").modal();
        return false
        //$("#alertDialog").css("z-index","1000000")
    }else{
        $.get("mcuser/mcQueryUserExistPowerWithChexkLostTime.do",{
        	empCode:userCode
        },function(data){
            if(!data){
            	$("#userCode").val("");
                $("#alertText").text("员工不存在或该操作员无此权限，请联系管理员");
                $("#alertDialog").modal();
                return false;
            }
            $('#scanUserCodeDialog').modal('hide');
            var username = data.username;
            userid = data.id;
            $('#confirmDialog').modal('show');
        })
    }
}
//确认弹出提示窗口
function showComfirmDialog(){
	$("#alertText").text("该记录已确认，请重新选择");
	$("#alertDialog").modal();
	return false;
}
//拆分弹出提示窗口
function showNoSplitDialog(){
	$("#alertText").text("只有未定义的数据能拆分，请重新选择");
	$("#alertDialog").modal();
	return false;
}
//弹出拆分窗口
function showSplitDialog(){
	if(!id){
		$("#alertText").text("请选择一条数据");
		$("#alertDialog").modal();
		return false;
	}
	 $.ajax({
			type : "post",
			url : contextPath + "lostTimeRecord/queryLostTimeRecordById.do",
			data : {"id":id},
			cache:false,
			dataType : "json",
			success : function(data) {//splitStartTime splitEndTime splitSumOfLostTime splitLostTimeType  splitLostTimeReason
				//alert(JSON.stringify(data));
				$("#splitStartTime").html(data.beginTime);
				$("#splitEndTime").html(data.endTime);
				$("#splitSumOfLostTime").html(data.sumOfLostTime);
				if($("#splitAddLostTime").val()){
					/* $("#splitAddLostTime").val($("#splitAddLostTime").val()-) */
				}
				$("#splitAddLostTime").val(data.sumOfLostTime);
				if(!data.lostTimeTypeName){
					$("#splitLostTimeType").html("等待");
				}else{
					$("#splitLostTimeType").html(data.lostTimeTypeName);
				}
				if(!data.reason){
					$("#splitLostTimeReason").html("未定义");
				}else{
					$("#splitLostTimeReason").html(data.lostTimeTypeName);
				}
			}
	 });
	 initSplitLostTimeTable();
	 $("#splitLostTimeTab").bootstrapTable("refresh", {
			cache:false,	
			query : {
				id:id
			},
		});
	$("#splitUnhandledLostTimeRecordDialog").modal();
}

//初始化拆分损时列表table
var splitObject ;//拆分的数据对象
function initSplitLostTimeTable() {
	$("#splitLostTimeTab").bootstrapTable({
		url :"lostTimeRecord/queryLostTimeRecordById.do",
		cache:false,
		height : 260,
		//idField : 'id',
		singleSelect : true,
		clickToSelect : true,
		uniqueId: "id",
		striped : true, //隔行换色
		queryParams : function(params) {
			var temp = {
				id :id
			};
			return temp;
		},	responseHandler :function(res){
			splitObject = res;
            //将服务端你的数据转换成bootstrap table 能接收的类型
            var lt = [res]
            return lt;
        },
		columns : [{
			radio:true,
			field : 'radio',
			title : ''
		}, {
			field : 'id',
			title : 'ID'
		},  {
			field : 'beginTime',
			align : 'center',
			title : '开始时间',
		}, {
			field : 'endTime',
			align : 'center',
			title : '结束时间',
		}, {
			field : 'sumOfLostTime',
			align : 'center',
			title : '损时合计(分)'
		}, {
			field : 'pressLightCode',
			align : 'center',
			title : '故障代码'
		},{
			field : 'pressLightTypeCode',
			align : 'center',
			title : '类型代码'
		}, {
			field : 'lostTimeTypeName',
			align : 'center',
			title : '损时类型',
			formatter: function (value, row, index) {  
				if(row.reason==null||row.reason==''){
					 return "等待";
				}else{
					return value;
				}
		    }
		}, {
			field : 'reason',
			align : 'center',
			title : '损时原因',
			formatter: function (value, row, index) {
				if(value==null||value==''){
					 return "未定义";
				}else{
					return value;
				}
		    }
		}]
	});
	$('#splitLostTimeTab').bootstrapTable('hideColumn', 'id');
	$('#splitLostTimeTab').bootstrapTable('hideColumn', 'pressLightCode');
	$('#splitLostTimeTab').bootstrapTable('hideColumn', 'pressLightTypeCode');
	$("#splitLostTimeTab").on('click-row.bs.table',
			function(e, row, element) { //splitLostTime confirmLostTime
				$('.success').removeClass('success'); //去除之前选中的行的，选中样式
				$(element).addClass('success'); //添加当前选中的 success样式用于区别
				
			});
}
//拆分新增
function splitAddLostTime(){
		var splitType = $("#splitAddLostTimeType option:selected").text();
		var splitTypeVal = $("#splitAddLostTimeType option:selected").val();
		var splitReason = $("#splitAddLostTimeReason option:selected").text();
		var splitSumLostTime = $("#splitAddLostTime").val();
		var splitStartTime = $("#splitAddStartTime").datetimebox('getValue');
		var splitEndTime = $("#splitAddEndTime").datetimebox('getValue');
		var splitLostTimeCode = $("#splitAddLostTimeReason option:selected").val();
		
		var timeEndAfter = new Date(splitEndTime.replace(/-/g,"/")).getTime();
		var timeStartBefore = new Date(startVal.replace(/-/g,"/")).getTime();
		
		if(timeStartBefore>=timeEndAfter){
			$("#splitAddEndTime").datetimebox('setValue',endVal);
			$("#splitAddLostTime").datetimebox('setValue',splitObject.sumOfLostTime);
			$("#alertText").text("您的损时结束时间已超时，请重新选择");
			$("#alertDialog").modal();
			return false;
		}else if(splitStartTime==splitEndTime){
			$("#splitAddEndTime").datetimebox('setValue',endVal);
			$("#alertText").text("损时时长为0，请重新选择");
			$("#alertDialog").modal();
			return false;
		}
		splitObject.sumOfLostTime = (splitObject.sumOfLostTime-splitSumLostTime).toFixed(2);
		splitObject.beginTime = splitEndTime;
		$("#splitAddStartTime").datetimebox('setValue',splitEndTime)
		$("#splitAddEndTime").datetimebox('setValue',splitObject.endTime)
		$("#splitAddLostTime").val(splitObject.sumOfLostTime);
		$('#splitLostTimeTab').bootstrapTable('append', {"pressLightTypeCode":splitTypeVal,"pressLightCode":splitLostTimeCode,"id":"","beginTime":splitStartTime,"endTime":splitEndTime,"sumOfLostTime":splitSumLostTime,"lostTimeTypeName":splitType,"reason":splitReason});
		if(splitObject.sumOfLostTime==0){
			$('#splitLostTimeTab').bootstrapTable('hideRow', {index: 0});
		}
		$('#splitLostTimeTab').bootstrapTable('updateRow', {index: 0, row:splitObject });
		$("#splitShowConfirmDlg").modal("hide");
		 $("#splitAddLostTimeReason").selectpicker('refresh');
}
//拆分删除
function splitRemoveLostTime(){
		var data = $('#splitLostTimeTab').bootstrapTable('getSelections');
		//alert(JSON.stringify(data));
		if(data[0].id==id){
			$("#alertText").text("该记录不可删除!");
			$("#alertDialog").modal();
			return false;
		}else{
			$('#splitLostTimeTab').bootstrapTable('remove', {field: 'radio', values: [true]});
			data[0].lostTimeTypeName='';
			data[0].reason='';
			data[0].pressLightCode="";
			data[0].pressLightTypeCode="";
			
			$('#splitLostTimeTab').bootstrapTable('append',data[0]);
			
		}
}
//拆分保存
function splitSaveLostTime(){
		var json = $('#splitLostTimeTab').bootstrapTable('getOptions');
		//alert(JSON.stringify(json.data));
		var deviceSite = $("#deviceSiteCode").val();
		 $.ajax({
				type : "post",
				url : contextPath + "mcLostTime/mcSplitSaveLostTime.do",
				data : {"data":JSON.stringify(json.data),"deviceSite":deviceSite},
				dataType : "json",
				success : function(data) {
					$("#splitUnhandledLostTimeRecordDialog").modal("hide");
					$("#alertText").text(data.message);
					$("#alertDialog").modal();
					$("#showLostTimeTable").bootstrapTable("refresh", {
						url :"mcLostTime/findLostTimeList.do",
						cache:false,	
						query : {
                            deviceSiteCode : $("#deviceSiteCode").val(),
							searchText : ""
						},
					});
				}
		 })
}
//拆分修改显示数据
function showSplitUpdateLostTime(){
	$("#addSplitShowScanLostTimeCodeDialog").remove("onclick");
	$("#addSplitShowScanLostTimeCodeDialog").attr("onclick","splitShowScanLostTimeCodeDialog('update')");
	var data = $('#splitLostTimeTab').bootstrapTable('getSelections');
	//alert(JSON.stringify(data));
	if(data[0].id==id){
		$("#alertText").text("该记录不可操作!");
		$("#alertDialog").modal();
		return false;
	}else{//  $('#splitAddLostTimeType').selectpicker('val',type.basicCode);
		startVal = data[0].beginTime;
		endVal = data[0].endTime;
		$("#splitAddLostTimeType").selectpicker('val',data[0].pressLightTypeCode);
		$("#splitAddLostTimeReason").selectpicker('val',data[0].pressLightCode);
		$("#splitAddLostTime").val(data[0].sumOfLostTime);
		$("#splitAddStartTime").datetimebox('setValue',data[0].beginTime);
		$("#splitAddEndTime").datetimebox('setValue',data[0].endTime);
		$("#splitShowConfirmDlg").modal("show");//splitSbmitbtn
		$("#splitSbmitbtn").removeAttr("onclick");
		$("#splitSbmitbtn").attr("onclick","splitUpdateLostTime()");
	}
}
//拆分修改提交
function splitUpdateLostTime(){
	var data = $('#splitLostTimeTab').bootstrapTable('getSelections');
	var splitType = $("#splitAddLostTimeType option:selected").text();
	var splitTypeVal = $("#splitAddLostTimeType option:selected").val();
	var splitReason = $("#splitAddLostTimeReason option:selected").text();
	var splitSumLostTime = $("#splitAddLostTime").val();
	var splitStartTime = $("#splitAddStartTime").datetimebox('getValue');
	var splitEndTime = $("#splitAddEndTime").datetimebox('getValue');
	var splitLostTimeCode = $("#splitAddLostTimeReason option:selected").val();
	//修改的开始时间
	var timeEndBefore = new Date(splitStartTime.replace(/-/g,"/")).getTime();
	//剩余拆分的开始时间
	var timeStartBefore = new Date(startVal.replace(/-/g,"/")).getTime();
	//剩余拆分的结束时间
	var timeEndAfter = new Date(endVal.replace(/-/g,"/")).getTime();
	//修改的结束时间
	var timeStartAfter = new Date(splitEndTime.replace(/-/g,"/")).getTime();
	if(splitObject.sumOfLostTime<0){
		$("#alertText").text("您的损时结束时间已超时，请重新选择");
		$("#alertDialog").modal();
		return false;
	}
	if(timeEndBefore>timeStartBefore){
		var startTimeVal=startVal;
		var endTimeVal=splitStartTime;
		var time = (timeEndBefore-timeStartBefore)/(1000*60);
		time = time.toFixed(2);
		
		$('#splitLostTimeTab').bootstrapTable('append', {"pressLightTypeCode":"","pressLightCode":"","id":"","beginTime":startTimeVal,"endTime":endTimeVal,"sumOfLostTime":time,"lostTimeTypeName":"","reason":""});
	}
	if(timeEndAfter>timeStartAfter){
		var startTimeVal=splitEndTime;
		var endTimeVal=endVal;
		//var time = (timeEndAfter-timeStartAfter)/(1000*60);
		//time = time.toFixed(2);
		time = (data[0].sumOfLostTime-splitSumLostTime).toFixed(2);
		
		$('#splitLostTimeTab').bootstrapTable('append', {"pressLightTypeCode":"","pressLightCode":"","id":"","beginTime":startTimeVal,"endTime":endTimeVal,"sumOfLostTime":time,"lostTimeTypeName":"","reason":""});
	}
	$('#splitLostTimeTab').bootstrapTable('remove', {field: 'radio', values: [true]});
	$('#splitLostTimeTab').bootstrapTable('append', {"pressLightTypeCode":splitTypeVal,"pressLightCode":splitLostTimeCode,"id":"","beginTime":splitStartTime,"endTime":splitEndTime,"sumOfLostTime":splitSumLostTime,"lostTimeTypeName":splitType,"reason":splitReason});
	$("#splitShowConfirmDlg").modal("hide");
	/* $("#splitSbmitbtn").removeAttr("onclick");
	$("#splitSbmitbtn").attr("onclick","splitAddLostTime()"); 
	$("#splitAddLight").remove("onclick");
	$("#splitAddLight").attr("onclick","splitShowScanLostTimeCodeDialog('add')"); */
}
//损时新增
function addLostTime(e){
	param =e;
	showScanLostTimeCodeDialog();
}
//损时新增
function splitAddLostTimeCodeDialog(){
	$("#splitSbmitbtn").removeAttr("onclick");
	$("#splitSbmitbtn").attr("onclick","splitAddLostTime()");
	//$("#splitAddLight").remove("onclick");
	//$("#splitAddLight").attr("onclick","splitShowScanLostTimeCodeDialog('add')");
	$("#addSplitShowScanLostTimeCodeDialog").remove("onclick");
	$("#addSplitShowScanLostTimeCodeDialog").attr("onclick","splitShowScanLostTimeCodeDialog('add')");
	splitShowScanLostTimeCodeDialog('add');
}
//意见选择初始化lightOut,recover,confirm
function flowSuggestion(e){

   var code = "LostTimeConfirm";

   $.get(contextPath + "mcSuggestion/getCommonSuggestion.do",{

      code:code

   },function(data){

         var htmlselect = "<option></option>";

         $.each(data,function(index, Type) {

            htmlselect += "<option value='"+Type.id+"'>"+Type.text+"</option>";

                     })
         $("#flowSuggestion").append(htmlselect);		
         $("#flowSuggestion").selectpicker('refresh');
   })
}

//获取损时总时长
function getAllLostTime(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	 $.ajax({
			type : "post",
			url : contextPath + "mcLostTime/getAllLostTime.do",
			data : {	
			"untreated":"untreated",
			"notSure":"",
			"historicalRecords":"",
			"searchText" : "",
			"deviceSiteCode" :deviceSiteCode
			},
			dataType : "json",
			success : function(data) {
				if(data){
					sum = (data/60).toFixed(2);
					$("#sum").text(sum);
				}
			}
	 })
}
//获取损时数量
function getCountLostTime(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	 $.ajax({
			type : "post",
			url : contextPath + "mcLostTime/getCountLostTime.do",
			data : {	
			"untreated":"untreated",
			"notSure":"",
			"historicalRecords":"",
			"searchText" : "",
			"deviceSiteCode" :deviceSiteCode
			},
			dataType : "json",
			success : function(data) {
				if(data){
					count = data;
					$("#count").text(count);
				}
			}
	 })
}
</script>

<style type="text/css">
</style>


<div class="tyPanelSize" >
	<div class="tytitle" style="height: 7%; text-align: center;">
		<!-- <button class='btn btn-success' id="lostTimeDeviceSiteCode" disabled="disabled" style="float:left;margin: 1%;"></button> -->
		<div style="overflow: hidden; display: inline-block" >
			<!-- <img style="float: left; margin-top: 7px;"
				src="mc/assets/css/images/tb.png"> -->
				<span class="fa fa-user-circle fa-2x" aria-hidden="true" style="float: left; margin-top: 10px;margin-right: 10px"></span>
				<span style="float: left; margin-top: 17px;">损失时间</span>
		</div>
		
	</div>
	<div class="mc_workmanage_center"
		style="height: 73%;">

		<div style="text-align: center">

			<!-- Nav tabs -->
			<ul id="myTab" class="nav nav-tabs" role="tablist"
				style="overflow: hidden; display: inline-block;">
				<!-- <li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="untreated"><a href='#home' aria-controls="untreated"
					role='tab' data-toggle='tab'>未处理</a></li>
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="notSure"><a href='#home' aria-controls="untreated"
					role='tab' data-toggle='tab'>未确认</a></li>
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="historicalRecords"><a href='#home'
					aria-controls="untreated" role='tab' data-toggle='tab'>历史记录</a></li> -->
			</ul>
			<!-- Tab panes -->
			<div class="tab-content"
				style="border-top: 1px solid darkgray; margin-top: -6px;">
				<div role="tabpanel" class="tab-pane active" id="home"
					style="width: 96%; margin-left: 2%;">
					<div style='width: 100%; float: right; margin-top: 0px;'>
						<div  style='float: left;margin: 7px;font-size: 18px;'>
						<!-- <span onclick='toolsearch()' class='fa fa-search' aria-hidden='true'></span> -->
						  日期范围: <input id="searchStartTime"  style="margin:8px;width: 200px" name="searchStartTime" editable="false"> 
						  至 <input id="searchEndTime"  style="margin:8px;width: 200px;"	name="searchEndTime" editable="false">
						  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					  损时类型: <select id="searchLostTimeType" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select>
    					  &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
    					 状态: <select id="searchStatus" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
									<option value="" ></option>
									<option value="untreated" >未定义</option>
									<option value="notSure" >未确认</option>
									<option value="historicalRecords" >已确认</option>
								</select>
					</div>
    					 <input type="button" value="查询" style="margin-left:16px;margin-bottom:8px;margin-top:7px;width: 80px;font-size: 18px;float: right;" 
    					 onclick='toolsearch()'>
						<table id='showLostTimeTable'></table>
					</div>
				</div>
			</div>

		</div>
	</div>
	<div class="mc_workmanage_bottom"
		style="height: 20%;overflow:hidden;margin-left: 2%; margin-right: 2%;">
		<div>
			<div 
			 class="container-fluid functionButton"
				id="confirmLostTime" >
				<h6></h6>
				<span class="fa fa-circle-thin" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">确认</h4><!-- onclick="updateLostTime('comfirmdlg')" -->
			</div>
			<div
			 class="container-fluid functionButton"
				id="splitLostTime">
				<h6></h6>
				<span class="fa fa-bars" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">拆分</h4><!-- onclick="showSplitDialog()" -->
			</div>


			<div onclick="addLostTime('add')" id="addLight" style="float: right;margin-right: 0;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-plus-circle" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">新增</h4>
			</div>
			
			<div onclick="updateLostTime('update')" id="updateLight" style="float: right;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">修改</h4>
			</div>
		</div>

	</div>
</div>
<!-- 添加弹出框 -->
<div class="modal fade" id="showConfirmDlg" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="">
	<div class="modal-dialog" style="width: 1000px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:70px; width: 800px; overflow: hidden;">
				<span class="fa fa-lightbulb-o"
					style="font-size: 55px; width: 80px; text-align: center; margin: 0 auto; margin-top: 15px; display: block;"></span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 900px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">损时类型</span> <select id="lostTimeType" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">	
								</select><input type="hidden" id="deviceSiteCode" name="deviceSite.code" />
								<input type="hidden" name="id" id="id" /><br>
					<br>
					<!-- <br> <span style="font-size: 20px;">损时原因</span> <input
									id="lostTimeReason" type="text" -->
									<br> <span style="font-size: 20px;">损时原因</span> 
               <select id="lostTimeReason" style="width: 60px; display: inline-block;" equipID=""
                           class="selectpicker" data-live-search="true">
                        </select>
              	<!-- <img src="mc/assets/css/images/cx.png"
									style="margin-top: 0px; margin-left: 5px;"> --><br>
					<br>
					<br> <span style="font-size: 20px;">合计时间</span> <input
									id="addLostTime" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control" readonly="readonly" /><span style="font-size: 20px;">&nbsp;分</span><br>
					<br>
					<br>
				</form>
				<div
					style="width: 400px; height: 200px; float: right; margin-top: 100px; margin-right: 50px">
				<!-- timepicker 组件-->
					<div style="margin-bottom: 10px;">
						<span style="font-size: 20px; margin-left: 40px;" for="startTime">损时开始</span>
						<span style="font-size: 20px; margin-left: 140px;" for="endTime">损时结束</span>
					</div>
					<div class="col-md-4"
						style="display: -webkit-inline-box; padding-left: 0px">
						<div class="input-group">
							<input type="text" name="startTime"  editable="true"
									id="startTime" style="width: 170px"/>
						</div>

					</div>
					<div class="col-md-4" style="margin-left: 70px">
						<div class="input-group">
							<input type="text" name="endTime" 
									id="endTime" style="width: 170px"/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span class="fa fa-qrcode fa-4x"
						onclick="showScanLostTimeCodeDialog()" style="margin-right: 30px;"></span> <span
						class="fa fa-rotate-left fa-4x" onclick="modelHide()"  style="margin-right: 25px;"></span> <span id="submitbtn"
						class="fa fa-check fa-4x" onclick=""></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 拆分添加弹出框 -->
<div class="modal fade" id="splitShowConfirmDlg" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="z-index:9999">
	<div class="modal-dialog" style="width: 1000px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:70px; width: 800px; overflow: hidden;">
				<span class="fa fa-lightbulb-o"
					style="font-size: 55px; width: 80px; text-align: center; margin: 0 auto; margin-top: 15px; display: block;"></span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 900px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">损时类型</span> <select id="splitAddLostTimeType" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">	
								</select>
								<input type="hidden" name="splitId" id="id" /><br>
					<br>
					<!-- <br> <span style="font-size: 20px;">损时原因</span> <input
									id="lostTimeReason" type="text" -->
									<br> <span style="font-size: 20px;">损时原因</span> 
               <select id="splitAddLostTimeReason" style="width: 60px; display: inline-block;" equipID=""
                           class="selectpicker" data-live-search="true">
                        </select>
             <img src="mc/assets/css/images/cx.png"
									style="margin-top: 0px; margin-left: 5px;"><br>
					<br>
					<br> <span style="font-size: 20px;">合计时间</span> <input
									id="splitAddLostTime" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control" readonly="readonly" /><span style="font-size: 20px;">&nbsp;分</span><br>
					<br>
					<br>
				</form>
				<div
					style="width: 400px; height: 200px; float: right; margin-top: 100px; margin-right: 50px">
				<!-- timepicker 组件-->
					<div style="margin-bottom: 10px;">
						<span style="font-size: 20px; margin-left: 40px;"for="splitAddStartTime">损时开始</span>
						<span style="font-size: 20px; margin-left: 140px;"for="splitAddEndTime">损时结束</span>
					</div>
					<div class="col-md-4"
						style="display: -webkit-inline-box; padding-left: 0px">
						<div class="input-group">
							<input type="text" name="startTime" class="form-control"
									id="splitAddStartTime" style="width: 170px" readonly="readonly"/>
						</div>

					</div>
					<div class="col-md-4" style="margin-left: 70px">
						<div class="input-group">
							<input type="text" name="endTime" class="form-control"
									id="splitAddEndTime" style="width: 170px"/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span id="addSplitShowScanLostTimeCodeDialog" class="fa fa-qrcode fa-4x"
						onclick="splitShowScanLostTimeCodeDialog('add')" style="margin-right: 30px;"></span> <span
						class="fa fa-rotate-left fa-4x" onclick="splitModelHide()"  style="margin-right: 25px;"></span> <span id="splitSbmitbtn"
						class="fa fa-check fa-4x" onclick="splitAddLostTime()"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 修改弹出框 -->
<div class="modal fade" id="showUpdateDlg" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 1000px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:70px; width: 800px; overflow: hidden;">
				<span class="fa fa-lightbulb-o"
					style="font-size: 55px; width: 80px; text-align: center; margin: 0 auto; margin-top: 15px; display: block;"></span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 900px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">损时类型</span> <select id="updateLostTimeType" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><!-- <input type="hidden" id="deviceSiteCode" name="deviceSite.code" />
								<input type="hidden" name="id" id="id" /> --><br>
					<br>
					<br> <span style="font-size: 20px;">损时原因</span> <select id="updateLostTimeReason" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><!-- <input
									id="" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control"  /><img src="mc/assets/css/images/cx.png"
									style="margin-top: 0px; margin-left: 5px;"> --><br>
					<br>
					<br> <span style="font-size: 20px;">合计时间</span> <input
									id="updateLostTime" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control" readonly="readonly" /><span style="font-size: 20px;">&nbsp;分</span><br>
					<br>
					<br>
				</form>			
				<div
					style="width: 400px; height: 200px; float: right; margin-top: 100px; margin-right: 50px">
				<!-- timepicker 组件-->
					<div style="margin-bottom: 10px;">
						<span style="font-size: 20px; margin-left: 40px;"for="updateStartTime">损时开始</span><span
							style="font-size: 20px; margin-left: 140px;"for="updateEndTime">损时结束</span>
					</div>
					<div class="col-md-4"
						style="display: -webkit-inline-box; padding-left: 0px">
						<div class="input-group">
							<input type="text" name="updateStartTime" class="form-control"
									id="updateStartTime" style="width: 170px"/>
						</div>

					</div>
					<div class="col-md-4" style="margin-left: 70px">
						<div class="input-group">
							<input type="text" name="updateEndTime" class="form-control"
									id="updateEndTime" style="width: 170px"/>
						</div>
					</div>
				</div>
					
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span class="fa fa-qrcode fa-4x"
						onclick="showScanLostTimeCodeDialog()" style="margin-right: 30px;"></span> <span
						class="fa fa-rotate-left fa-4x" onclick="modelHide()"  style="margin-right: 25px;"></span> <span id="submitbtn"
						class="fa fa-check fa-4x" onclick="submitUpdate()"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 删除确认框 -->
<div class="modal fade" id="deleteConfirmDlg">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要解除吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="deleteSure()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 修改确认框 -->
<div class="modal fade" id="updateConfirmDlg">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要修改吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="updateLostTimeRecord()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 拆分删除确认框 -->
<div class="modal fade" id="splitDelectConfirmDlg" style="z-index: 10000">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要删除吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="splitRemoveLostTime()" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 损时扫描条码窗口 -->
<div class="modal fade" id="scanLostTimeCodeDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 930px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<span class="fa fa-qrcode fa-4x"></span>
			</div>
			<div class="modal-body" style="height: 450px">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<div style="float: left;background-color: ;width: 150px">
							<div >
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时条码:</label>
							</div>
							<div style="margin-top: 60px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时类型:</label>
							</div>
							<div style="margin-top: 80px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时原因:</label>
							</div>
						</div>	
						<div class="col-sm-9" style="float: left;width: 730px">
							<input type="text" name="lostTimeCode" onblur="lostfocus()"
								class="form-control" id="lostTimeCode" autofocus="autofocus" />
							<div id="lostType" style="display: inline;float:left;margin-top: 30px" ></div>
							<div id="lostReason" style="display: inline;float:left;margin-top: 5px;height:120px;overflow: auto;" ></div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span class='btn btn-default' style="margin-right: 20px;"
						onclick="confirminput()">确认</span>
					<span  class='btn btn-default' style="margin-right: 20px;"
						onclick="manualinput()">手动输入</span>
					<span  class='btn btn-default'
						onclick="" data-dismiss="modal">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 拆分损时扫描条码窗口 -->
<div class="modal fade" id="splitScanLostTimeCodeDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="z-index:10000">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<span class="fa fa-qrcode fa-4x"></span>
			</div>
			<div class="modal-body" style="height: 450px">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<div style="float: left;background-color: ;width: 150px">
							<div >
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时条码:</label>
							</div>
							<div style="margin-top: 60px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时类型:</label>
							</div>
							<div style="margin-top: 80px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">损时原因:</label>
							</div>
						</div>
						<div class="col-sm-9" style="float: left;width: 700px">
							<input type="text" name="splitLostTimeCode" onblur="lostfocus()"
								class="form-control" id="splitLostTimeCode" autofocus="autofocus" />
							<div id="splitLostType" style="display: inline;float:left;margin-top: 30px" ></div>
							<div id="splitLostReason" style="display: inline;float:left;margin-top: 5px;" ></div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span  style="text-decoration: underline;margin-right: 20px"
						onclick="splitConfirminput()">确认</span>
					<span  style="text-decoration: underline;"
						onclick="splitManualinput()">手动输入</span>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 人员扫描条码窗口 -->
<div class="modal fade" id="scanUserCodeDialog" role="dialog"
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
						<label class="col-sm-2 control-label" for="pressLightCode">人员条码</label>
						<div class="col-sm-9">
							<input type="text" name="userCode" onblur="lostfocus()"
								class="form-control" id="userCode" autofocus="autofocus" />
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span data-dismiss="modal" data-target="#showUpdateDlg" data-toggle="modal" style="text-decoration: underline;"
						>返回确认界面</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 拆分未处理损时窗口 -->
<div class="modal fade" id="splitUnhandledLostTimeRecordDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="false">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<header>
					<form>
						<fieldset>
							<legend>待拆分记录</legend>
							<label>开始时间:</label> <span id="splitStartTime" style="width:170px;display:inline-block;border-bottom: 1px solid gray; "></span>
							<label>结束时间:</label> <span id="splitEndTime" style="width:170px;display:inline-block;border-bottom: 1px solid gray;"></span>
							<label>损时合计:</label> <span id="splitSumOfLostTime" style="width:50px;display:inline-block;border-bottom: 1px solid gray;"></span>
							<label>损时类型:</label> <span id="splitLostTimeType" style="width:50px;display:inline-block;border-bottom: 1px solid gray;"></span>
							<label>损时原因:</label> <span id="splitLostTimeReason" style="width:50px;display:inline-block;border-bottom: 1px solid gray;"></span>
						</fieldset>
					</form>
				</header>
				<div class="pre-scrollable">
				<table id="splitLostTimeTab" style="margin-left: 0%;margin-right: 0%">
				</table>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="margin-top: 25px;">

			<div onclick="splitSaveLostTime()"
			 class="container-fluid functionButton"
				id="splitSaveLostTime" style="margin-left: 20px;float:left;">
				<h6></h6>
				<span class="fa fa-circle-thin" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">保存</h4>
			</div>
			
			<div onclick="showSplitUpdateLostTime()" id="splitUpdateLight" 
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">修改</h4>
			</div>
			<div onclick="" id="splitDeleteLight" data-toggle="modal" data-target="#splitDelectConfirmDlg"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-times-circle" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">删除</h4>
			</div>
				<div onclick="splitAddLostTimeCodeDialog()" id="splitAddLight" 
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-plus-circle" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">新增</h4>
			</div>
		</div>
			</div>
		</div>
	</div>
</div>
<!-- 熄灯、恢复、确认框 -->

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

			            <a onclick="submitConfirm()" class="btn btn-success"

			               data-dismiss="modal">确定</a>

			         </div>

			      </div>

			   </div>

			</div>
<%@ include file="end.jsp"%>
