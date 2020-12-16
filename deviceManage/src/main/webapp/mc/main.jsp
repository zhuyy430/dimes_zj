<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script type="text/javascript">
var classesCode ="";//班次代码
	$.ajaxSetup({
		cache : false
	});
	//设置时间
	function showTime() {
		var date = new Date();
		var timeStr = date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();
		$("#showTime").text(timeStr);
	}
	var mcLoginUser = "";
	//设备运行时间
	function showMyChart() {
		if (_deviceSiteCode != "" && _deviceSiteCode != null) {
			//查找设备运行表所需数据
			$.get(contextPath + "mcdeviceSite/queryDeviceRunningTimes.do", {
				deviceSiteCode : _deviceSiteCode
			}, function(data) {
				myChart(data);
			});
		}
	}
	//显示mc主页上的oee数据
	function showOee() {
		if (_deviceSiteCode != "" && _deviceSiteCode != null) {
			//查询当前设备站点的oee数据
			$.get(contextPath + "oee/queryOee4CurrentDeviceSite.do", {
				deviceSiteCode : _deviceSiteCode
			}, function(data) {
				$("#qualityRate").text(data.qualityRate);
				$("#performanceRate").text(data.performanceRate);
				$("#motionRate").text(data.motionRate);
				$("#oee").text(data.oee);
				//$("#divID").css("background-color","red");
				var color=data.oee.split("%");
				if(color[0]>70){
					$("#oee").css("color","green");
				}else if(color[0]>60){
					$("#oee").css("color","orange");
				}else{
					$("#oee").css("color","red");
				}
			});
		}
	}
	//时间改变事件
	$(function(){
	})
	var newday="";
	$(function() {
		$('#startTime').val(new Date());
 		$('#endTime').val(new Date());
		var data = new Object();
		data.titles = new Array();
		data.assistants = new Array();
		data.minutes = new Array();
		//显示当前时间
		window.setInterval(showTime, 1000);
		var name =sessionStorage.getItem("classesname")
		if(name){
			$("#classesName").text(name);
		}
		
		$("#startTime").datetimebox({
 			required: true,
 			timeSeparator:':',
 			showSeconds:true,
 			 parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#endTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		var date = new Date();
 		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
		+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
		+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		$("#endTime").datetimebox('setValue', value);
		$("#startTime").datetimebox('setValue', value);
		    //开始时间改变事件
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
							$("#startTime").datetimebox('setValue', value);
					 		$("#endTime").datetimebox('setValue', value);
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
							$("#startTime").datetimebox('setValue', value);
					 		$("#endTime").datetimebox('setValue', value);
			         }
				}
			});
		
		$.ajax({
        	type : "post",
            url : contextPath + "classes/queryAllClassesforMc.do",
            data : {},
			cache:false,
            dataType : "json",
            success : function(data) {
            	   var code = "";
                   var htmlselect = "";
                   var myDate = new Date();
                   var y = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
                   var m = myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
                   var d = myDate.getDate();        //获取当前日(1-31)
                   
	               $.each(data,function(index, Type) {
	                  htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
	                  var startTime= new Date(Date.parse(y+"-"+m+"-"+d+" "+Type.startTime));
	                  var endTime= new Date(Date.parse(y+"-"+m+"-"+d+" "+Type.endTime));
	                  
	                  if(startTime<myDate&&endTime>myDate){
							code=Type.code;            	  
	                  }
	               })
	               $("#classes").empty();
	               $("#classes").append(htmlselect);
	               $("#classes").selectpicker('refresh');
	               $('#classes').selectpicker('val',code);
	               $("#classes2").empty();
	               $("#classes2").append(htmlselect);
	               $("#classes2").selectpicker('refresh');
	               $('#classes2').selectpicker('val',code);
            }
		})
		setlostTimeType();
		  //扫码窗口改变事件
     	$("#lostTimeCode").keydown(function(event) {
        	if (event.keyCode == 13) {
	     		lostTimeTypeSelect('add');
 			} 
      	})
      	 //扫码窗口改变事件
     	$("#lostTimeCode").change(function(event) {
	     		lostTimeTypeSelect('add');
      	})
		window.setInterval(function(){showDetail(_deviceSiteCode)}, 1000 * 60);
		//用户扫描登录自动获取焦点
		$('#userLoginByEmployeeCodeDialog').on('shown.bs.modal', function(e) {
			$('#employeeCodeInput').focus();
		});
		//用户扫描登出自动获取焦点
		$('#userLogOutByEmployeeCodeDialog').on('shown.bs.modal', function(e) {
			$('#employeeCode4Logout').focus();
		});
		//下拉框未选中显示
        $(".selectpicker").selectpicker({
            noneSelectedText: '请选择' //默认显示内容  
        });
		//根据IP地址查询本机是否有登录用户
		$.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
				result) {
			var mcUser = result.mcUser;
			if (mcUser) {
				mcLoginUser = mcUser.username;
				$("#classesName").text(mcUser.className);
				$("#login").css("background-color", "red");
                $("#login h5").text("人员签退");
                $("#showUsernameLabel").text(mcUser.employeeName);
				//登录成功，将登录签退标识变为签退
				$("#loginOrLogout").val("1");
				classesCode = mcUser.classCode;
				addClickEvent(result.mcPowers);
			}
		});
		//查询所有本地设备站点
		$.get(
						contextPath + "mcdeviceSite/getAllMCDeviceSite.do",
						function(data) {
							if (data && data.length > 0) {
								//显示所有设备站点的div
								var allDeviceSites = $("#allDeviceSites");
								for (var i = 0; i < data.length; i++) {
									var mcDeviceSite = data[i];
									var $button = $("<button class='btn btn-default' id='"+
											mcDeviceSite.deviceSiteCode
											+"' type='button' style='margin-right:5px;width:200px;height:50px;text-overflow:clip;'  onclick='showDetail(\""
											+ mcDeviceSite.deviceSiteCode
											+ "\",\""
											+ mcDeviceSite.deviceSiteName
											+ "\")'>"
											+ "<font size='4' style='width:80px;text-overflow:hidden'>"+mcDeviceSite.deviceSiteName+"</font><br/>"+mcDeviceSite.deviceSiteCode
											+ "</button>");
									allDeviceSites.append($button);
								}
							}
							//点击第一个按钮
							$("#allDeviceSites button:first").click();
						});
		//扫描员工二维码事件
		$("#employeeCodeInput").change(function() {
			scanLogin();
		});
		//扫描员工登出二维码事件
		$("#employeeCode4Logout").change(
				function() {
					var employeeCode = $(this).val();
					if (!employeeCode) {
						$("#alertText").text("请扫码!");
						$("#alertDialog").modal();
						return;
					}
					$("#employeeCode4Logout").val("");

					_employeeCode = employeeCode;
					//查询是否有未处理损时记录
					$
							.get(contextPath
									+ "mcuser/queryUnhandledLostTimeRecord.do",
									function(data) {
										if (data.hasUnhandledLostTimeRecords) {
											$("#unhandledLostTimeRecordDialog")
													.modal();
										} else {
											employeeLogout();
										}
									});
				});
		//扫描员工登出二维码事件(回车事件)
		$("#employeeCode4Logout").keydown(
				function() {
					if (event.keyCode == 13) {
						var employeeCode = $(this).val();
						if (!employeeCode) {
							$("#alertText").text("请扫码!");
							$("#alertDialog").modal();
							return;
						}
						$("#employeeCode4Logout").val("");

						_employeeCode = employeeCode;
						//查询是否有未处理损时记录
						$.get(contextPath
								+ "mcuser/queryUnhandledLostTimeRecord.do",
								function(data) {
									if (data.hasUnhandledLostTimeRecords) {
										$("#unhandledLostTimeRecordDialog")
												.modal();
									} else {
										employeeLogout();
									}
								});
					}
				});
		//扫描员工二维码事件(回车事件)
		$("#employeeCodeInput").keydown(function(event) {
			if (event.keyCode == 13) {
				scanLogin();
			}
		});
	});
	var _employeeCode = "";
	//员工登出
	function employeeLogout() {
		$('#userLogOutByEmployeeCodeDialog').modal('hide');
		//发送异步登录请求
		$.post(contextPath + "mcuser/scanQrLogOut.do", {
			employeeCode : _employeeCode
		}, function(data) {
			if (data.success) {
				//登出成功，将登录签退标识变为登录
				$("#loginOrLogout").val("0");
				sessionStorage.clear();
				//刷新当前页面，已启动各个功能模块的点击事件
				window.location.reload();
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			}
		});
	}

	//人员登录
	function userLogin() {

		switch ($("#loginOrLogout").val()) {
		//弹出登录框
		case "0": {
			$("#employeeCodeInput").val("");
			$("#employeeCodeInput")[0].focus();
			$("#userLoginByEmployeeCodeDialog").modal();
			break;
		}
			//弹出签退框
		case "1": {
			$("#employeeCode4Logout").val("");
			$("#employeeCode4Logout")[0].focus();
			$("#userLogOutByEmployeeCodeDialog").modal();
			break;
		}
		}
	}
	//用户名密码登录
	function loginByUsernameAndPassword() {
		var classesname=$("#classes2 option:selected").text();
		var classescode=$("#classes2").val();
		var username = $("#username").val();
		if (!username) {
			$("#alertText").text("请输入用户名!");
			$("#alertDialog").modal("show");
			$("#username")[0].focus();
			return false;
		}
		var password = $("#password").val();
		if (!password) {
			$("#alertText").text("请输入密码！");
			$("#alertDialog").modal("show");
			$("#password")[0].focus();
			return false;
		}
		$.get(contextPath + "mcuser/mcuserLogin.do", {
			username : username,
			password : password,
			classCode:classescode,
			className:classesname
		}, function(data) {
			if (data.success) {
				//刷新当前页面，已启动各个功能模块的点击事件
				window.location.reload();
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			}
		});
	}
	//用户名密码登出
	function logoutByUsernameAndPassword() {
		var username = $("#usernameout").val();
		if (!username) {
			$("#alertText").text("请输入用户名!");
			$("#alertDialog").modal("show");
			$("#username")[0].focus();
			return false;
		}
		var password = $("#passwordout").val();
		if (!username) {
			$("#alertText").text("请输入密码！");
			$("#alertDialog").modal("show");
			$("#password")[0].focus();
			return false;
		}
		$.get(contextPath + "mcuser/mcuserLogout.do", {
			username : username,
			password : password
		}, function(data) {
			if (data.success) {
				sessionStorage.clear();
				//刷新当前页面，已启动各个功能模块的点击事件
				window.location.reload();
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			}
		});
	}
	//弹出用户名密码登录框
	function showUsernameAndPasswordLoginDialog() {
		//隐藏扫码登录框
		$("#userLoginByEmployeeCodeDialog").modal("hide");
		//清空用户名密码框
		$("#username").val("");
		$("#username")[0].focus();
		$("#password").val("");
		//弹出用户名密码登录框
		$("#userLoginByUsernameAndPasswordDialog").modal();
	}
	//弹出用户名密码登出框
	function showUsernameAndPasswordLogoutDialog() {
		//隐藏扫码登出框
		$("#userLogoutByEmployeeCodeDialog").modal("hide");
		//清空用户名密码框
		$("#usernameout").val("");
		$("#usernameout")[0].focus();
		$("#passwordout").val("");
		//弹出用户名密码登出框
		$("#userLogoutByUsernameAndPasswordDialog").modal();
	}
	//弹出扫描二维码登录框
	function showQrLoginDialog() {
		//隐藏用户名密码登录框
		$("#userLoginByUsernameAndPasswordDialog").modal("hide");
		$("#employeeCodeInput").val("");
		//弹出扫码登录框
		$("#userLoginByEmployeeCodeDialog").modal("show");
		$("#employeeCodeInput")[0].focus();
	}
	//弹出扫描二维码登出框
	function showQrLogoutDialog() {
		//隐藏用户名密码登出框
		$("#userLogoutByUsernameAndPasswordDialog").modal("hide");
		$("#employeeCode4Logout").val("");
		//弹出扫码登出框
		$("#userLogOutByEmployeeCodeDialog").modal("show");
		$("#employeeCode4Logout")[0].focus();
	}

	//扫二维码登录
	function scanLogin() {
		var classesname=$("#classes option:selected").text();
		var classescode=$("#classes").val();
		//隐藏登录框
		$("#userLoginByEmployeeCodeDialog").modal("hide");
		var employeeCode = $("#employeeCodeInput").val();
		if (!employeeCode) {
			$("#alertText").text("请扫码!");
			$("#alertDialog").modal();
			return;
		}
		$("#employeeCodeInput").val("");
		//发送异步登录请求
		$.get(contextPath + "mcuser/scanQrLogin.do", {
			employeeCode:employeeCode,
			classCode:classescode,
			className:classesname
		}, function(data) {
			if (data.success) {
				//刷新当前页面，已启动各个功能模块的点击事件
				window.location.reload();
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			}
		});
	}
	var _deviceSiteCode = "";
	var _deviceSiteId = "";
	//本地设备站点按钮点击事件
	function showDetail(deviceSiteCode,deviceSiteName) {
		_deviceSiteCode = deviceSiteCode;
		$("#allDeviceSites button").removeClass("btn btn-primary");
		$("#allDeviceSites button").addClass("btn btn-default");
		var btns = $("#allDeviceSites button");
		for(var i = 0;i<btns.length;i++){
			var btn = $(btns[i]);
			var btnObj = btns[i];
			var textOnButton = btnObj.id;
			if(textOnButton === _deviceSiteCode){
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}
		
		if(!_deviceSiteCode){
			return ;
		}
		$("#deviceSiteIdInEnd").val(_deviceSiteCode);
		$
				.get(
						contextPath
								+ "mcdeviceSite/queryProcessingWorkSheetDetailByDeviceSiteCode.do",
						{
							deviceSiteCode : deviceSiteCode
						}, function(data) {
								_deviceSiteId = data.deviceSiteId;
							if (data.workSheet != null) {
								$("#workSheetCode").text(data.workSheet.no);
								$("#workPieceName").text(
										data.workSheet.workPieceName);
								$("#workPieceCode").text(
										data.workSheet.workPieceCode);
								$("#production").text(
										data.reportCount + " / "
												+ data.workSheet.productCount);
							} else {
								$("#workSheetCode").text("");
								$("#workPieceName").text("");
								$("#workPieceCode").text("");
								$("#production").text("/");
							}
							$("#processName").text(data.processName==null?"":data.processName);
							$("#processCode").text(data.processCode==null?"":data.processCode);
							$("#deviceName").text(data.deviceName==null?"":data.deviceName);
							$("#deviceCode").text(data.deviceCode==null?"":data.deviceCode);
						});
		$.get(contextPath+ "mcdeviceSite/queryClassesByDeviceSiteCode.do",
				{
					deviceSiteCode : deviceSiteCode
				}, function(data) {
					if(data){
						$("#classesName").text(data.name);
						classesCode = data.code;
					}
					else{
						$("#classesName").text("");
						classesCode = "";
					}
				})
		//显示MC端首页图表
		showMyChart();
		//显示oee数据
		showOee();
	}

	//为各个模块添加点击事件
	function addClickEvent(mcPowers) {
		if(mcPowers&&mcPowers.length>0){
		for(var i = 0;i<mcPowers.length;i++){
		switch(mcPowers[i][1]){
		case "工单管理":
			//工单管理点击事件
			$("#worksheetModule").css("background-color", "#00CCFF");
			$("#worksheetModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/work.jsp?deviceSiteCode=" + _deviceSiteCode+"&classesCode="+classesCode;
					});
			break;
		case "装备关联":
			//装备关联点击事件
			$("#equipmentMappingModule").css("background-color", "#00FFFF");
			$("#equipmentMappingModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/employeeMapping.jsp?deviceSiteCode="
								+ _deviceSiteCode;
					});
			break;
		case "量具关联":
			//量具关联点击事件
			$("#measuringToolMappingModule").css("background-color", "#66FF99");
			$("#measuringToolMappingModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/measuringMapping.jsp?deviceSiteCode="
								+ _deviceSiteCode;
					});
			break;
		case "NG录入":
			//NG录入点击事件
			$("#ngRecordModule").css("background-color", "#FFFFCC");
			$("#ngRecordModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/ngEntry.jsp?deviceSiteCode="
								+ _deviceSiteCode;
					});
			break;
		case "损失时间":
			//损失时间点击事件
			$("#lostTimeModule").css("background-color", "#F6F600");
			$("#lostTimeModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/lostTime.jsp?deviceSiteCode="
								+ _deviceSiteCode;
					});
			break;
		case "检验记录":
			//检验记录点击事件
			$("#checkrecord").css("background-color", "#99BAFC");
			$("#checkrecord").click(
					function() {
						window.location.href = contextPath
								+ "mc/checkRecord.jsp";
					});
			break;
		case "无纸化系统":
			//无纸化系统点击事件
			$("#paperless").css("background-color", "#66CC00");
			$("#paperless").click(
					function() {
						window.open(contextPath
								+ "paperless/zj_home.jsp");
					});
			break;
		case "设备管理":
			//设备管理点击事件
			$("#deviceModule").css("background-color", "#FA8383");
			$("#deviceModule").click(
					function() {
						window.location.href = contextPath
								+ "mc/deviceModule.jsp?deviceSiteCode="
								+ _deviceSiteCode;
					});
			break;
		}
		}
		}
	/* 	$("#packManageModule").click(
				function() {
					window.location.href = contextPath
							+ "mc/packManage.jsp?deviceSiteCode="
							+ _deviceSiteCode;
				}); */
		//其他功能点击事件
		$("#otherModule").click(function() {
		});

		//图表右侧添加按钮点击事件
		$("#addBtn").click(function() {
			showScanLostTimeCodeDialog();
		});
		//图表右侧搜索按钮点击事件
		$("#searchBtn").click(
				function() {
					window.location.href = contextPath
							+ "mc/lostTime.jsp?deviceSiteCode="
							+ _deviceSiteCode;
				});
	}

	/**图表*/
	function myChart(data) {
		var myChart = echarts.init(document.getElementById('myChart'));
		option = {
			title : {
				text : '设备运行时间表（单位:分钟）'
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				},
				formatter : function(params) {
					var tar = params[1];
					return tar.name + '<br/>' + tar.seriesName + ' : '
							+ tar.value;
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : {
				type : 'category',
				splitLine : {
					show : false
				},
				data : data.titles
			},
			yAxis : {
				type : 'value',
				min : 0
			},
			series : [ {
				name : '辅助',
				type : 'bar',
				stack : '总量',
				itemStyle : {
					normal : {
						barBorderColor : 'rgba(0,0,0,0)',
						color : 'rgba(0,0,0,0)'
					},
					emphasis : {
						barBorderColor : 'rgba(0,0,0,0)',
						color : 'rgba(0,0,0,0)'
					}
				},
				data : data.assistants
			}, {
				name : '',
				type : 'bar',
				stack : '总量',
				label : {
					normal : {
						show : true,
						position : 'inside'
					}
				},
				data : data.minutes
			} ]
		};
		myChart.setOption(option);
	}
	//显示损时新增扫描弹框
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
	//点击手工输入事件
	function manualinput(){
		$('#scanLostTimeCodeDialog').modal('hide');
		showConfirmDlg("add");
	}
	//显示新增页面
	function showConfirmDlg(e) {
		var date = new Date();
		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
				+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
				+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		$('#startTime').val(value);
		$('#endTime').val(value);
		$("#addLostTime").val("0");
		$('#showConfirmDlg').modal('show');
		$('#submitbtn').attr("onclick","submitclick()");
	}
	//模态框点击返回事件(hide)
	function modelHide() {
		$('#showConfirmDlg').modal('hide');
	}
	//弹出框确认点击事件(对勾)
	function submitclick(e) {
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
			                  sumOfLostTime:$("#addLostTime").val(),   
			                  'deviceSite.id':_deviceSiteId,
			                  reason:$("#lostTimeReason").find("option:selected").text(),
			                  pressLightCode:$("#lostTimeReason").val(),
			                  pressLightCode:$("#lostTimeReason").val()
			               },function(data){
			                 // if(data.success){
			                     $("#alertText").text(data.msg);
			                     $("#alertDialog").modal();
			                     $('#showConfirmDlg').modal('hide');
		                  //}
               })
			
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
									})
					$("#lostTimeType").append(htmlselect);		
					$("#lostTimeType").selectpicker('refresh');
					setaddReason();
				}
			}) 
	}
	
	
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
	 //判断损时代码
		function lostTimeTypeSelect(e) {
			var	lostTimeCode=$("#lostTimeCode").val();
			lostTimeCode = lostTimeCode.replace(/(^\s*)|(\s*$)/g, '');
			if(lostTimeCode==null||lostTimeCode==undefined || lostTimeCode==""){
				/* $("#alertText").text("请输入损时代码！");
				$("#alertDialog").css({"z-index":100000})
				$("#alertDialog").modal(); */
				return false;
			}else{
				$.get("mcPressLightRecord/queryPressLightByCode.do",{
					code:lostTimeCode
				},function(data){
					if(!data){
						$("#alertText").text("损时类别不存在，请重新输入");
						$("#alertDialog").modal();
						return false;
					}
						$('#scanLostTimeCodeDialog').modal('hide');
						
						showConfirmDlg("add");
						
						$("#lostTimeReason").html("<option></option><option value='"+data.code+"'>"+data.reason+"</option>");
						$('#lostTimeReason').selectpicker('val',data.code);
			            $("#lostTimeReason").selectpicker('refresh');
			      //      $("#lostTimeReason").val(data.reason);
						$.get("mcPressLight/getParentTypeBytypeId.do",{
							typeid:data.pressLightType.id
						},function(type){
							if(type.basicCode==null){
								$('#lostTimeType').selectpicker('val',type.code);
			               }else{
			            	    $('#lostTimeType').selectpicker('val',type.basicCode);
			               }
						})
				})
			}
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
	            }
	        })
	}
	 //时间改变事件
	$(document).ready(function(){
		  //原因改变事件
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
		//类型改变事件
		  $("#lostTimeType").change(function() {
		         setaddReason();
		    });
	})
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
						var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showlostTimeDetail(\""
								+ Type.code
								+ "\")' value='"+Type.code+"'>"
								+ Type.name
								+ "</button>");
					$("#lostType").append(button);		
									})
					$("#lostTimeType").append(htmlselect);		
					$("#lostTimeType").selectpicker('refresh');
					setReason();
					setaddReason();
				}
			}) 
	}
	
	//点击类型事件
		function showlostTimeDetail(typeCode) {
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
		//点击手工输入事件
		function manualinput(){
			$('#scanLostTimeCodeDialog').modal('hide');
			showConfirmDlg("add");
			$('#lostTimeCode').val(lostReasonCode);
			$('#lostTimeCode').keydown();
			
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
		//损时确认人扫码框失去焦点事件
		function lostfocus(){
			$("#lostTimeCode").focus();
		}
</script>
<!-- 登录或签退标识 0:登录  1：签退 -->
<input type="hidden" id="loginOrLogout" value="0" />
<div class="main_top">
	<div class="navbar-header" style="margin-top: 5px">
		<img src="mc/assets/css/images/head.png" class="img-head" />
		<!-- <span class="fa fa-sitemap" aria-hidden="true" style="font-size: 30px;float: left;margin:5px;"></span> -->
		<div id="allDeviceSites" style="display: inline;margin-top: 5px;overflow:hidden" ></div>
	</div>
	<div id="navbar" class="container-fluid" style="margin-top: 10px">
		<img src="mc/assets/css/images/login.png" class="img-login"
			style="float: right;" />
			<!-- <span class="fa fa-address-card-o" aria-hidden="true" style="font-size: 30px;float: right;margin-left: 5px;margin-top: 5px"></span> -->
			<label id="showUsernameLabel" >未登录</label>
	</div>
</div>
<div class="main_center">
	<div id="left">
		<div class="main_center_left_top">
			<ul>
				<li><h4>-工单编码:</h4>
					<h4 id="workSheetCode"></h4></li>
				<li><h4>
						-当前日期:
						<script type="text/javascript">
							var date = new Date();
							document.write(date.getFullYear() + "-"
									+ (date.getMonth() + 1) + "-"
									+ date.getDate());
						</script>
					</h4></li>
				<li><h4>
						-当前时间: <span id="showTime"></span>
					</h4></li>
				<li><h4>
						-当前班次:</h4>
						<h4 id="classesName"></h4></li>
				<li><h4>-产品名称:</h4>
					<h4 id="workPieceName"></h4></li>
				<li><h4>-产品编码:</h4>
					<h4 id="workPieceCode"></h4></li>
				<li><h4>-工序名称:</h4>
					<h4 id="processName"></h4></li>
				<li><h4>-工序代码:</h4>
					<h4 id="processCode"></h4></li>
				<li><h4>-设备名称:</h4>
					<h4 id="deviceName"></h4></li>
				<li><h4>-设备代码:</h4>
					<h4 id="deviceCode"></h4></li>
				<li><h4>-生产执行:</h4>
					<h4 id="production"></h4></li>
			</ul>
		</div>
		<div id="myTask" >
			<div style="width: 100%; height: 60%;">
				<div class="main_myTask_top">
					<div style="margin-top: 10px;">
						<span class="fa fa-clock-o fa-2x"></span>
					</div>
					<div style="margin-top: 5px;">开动率</div>
					<div style="margin-top: 5px;">
						<p id="motionRate">100%</p>
					</div>
				</div>
				<div class="main_myTask_top" >
					<div style="margin-top: 10px;">
						<span class="fa fa-line-chart fa-2x"></span>
					</div>
					<div style="margin-top: 5px;">性能率</div>
					<div style="margin-top: 5px;">
						<p id="performanceRate">100%</p>
					</div>
				</div>
				<div class="main_myTask_top" style="margin-right: 0;">
					<div style="margin-top: 10px;">
						<span class="fa fa-check-circle-o fa-2x"></span>
					</div>
					<div style="margin-top: 5px;">质量率</div>
					<div style="margin-top: 5px;">
						<p id="qualityRate">100%</p>
					</div>
				</div>
			</div>
			<div
				id="oeeDiv">
				<p
					style="position: relative; top: 50%; left: 50%; transform: translate(-50%, -50%);font-size:30px;"
					id="oee">88.88%</p>
			</div>
		</div>
	</div>

	<div id="right">
		<div class="main_right_top">
			<div class="main_right_top_line" id="modules_firstLine">
				<div class="main_right_top_line_gap" ></div>
				<div id="login" class="container-fluid mainfunctionButton"
					style="background-color: #4AD662;" onclick="userLogin()"
					data-toggle="modal" data-target="#myModal">
					<h6></h6>
					<span class="fa fa-user-plus" aria-hidden="true"
						></span>
					<h5>人员登录</h5>
				</div>
				 <div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="worksheetModule">
					<h6></h6>
					<span class="fa fa-list-alt" aria-hidden="true"
						></span>
					<h5>工单管理</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="equipmentMappingModule">
					<h6></h6>
					<span class="fa fa-wrench" aria-hidden="true"
						></span>
					<h5>装备关联</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="measuringToolMappingModule">
					<h6></h6>
					<span class="fa fa-balance-scale" aria-hidden="true"
						></span>
					<h5>量具关联</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color:gray;" id="ngRecordModule">
					<h6></h6>
					<span class="fa fa-exclamation-circle" aria-hidden="true"
						style="-webkit-font-smoothing: antialiased;"></span>
					<h5>NG 录入</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="lostTimeModule">
					<h6></h6>
					<span class="fa fa-clock-o" aria-hidden="true"
						></span>
					<h5>损失时间</h5>
				</div> 
			</div>
			<div class="main_right_top_line">
				<div class="main_right_top_line_gap" ></div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="checkrecord">
					<h6></h6>
					<span class="fa fa-calendar-o" aria-hidden="true"
						></span>
					<h5>检验记录</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: gray;" id="paperless">
					<h6></h6>
					<span class="fa fa-archive" aria-hidden="true"
						></span>
					<h5>无纸化系统</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color:gray;" id="deviceModule">
<!-- 				<div class="container-fluid mainfunctionButton"
					style="background-color: #FA8383;" id="deviceModule"> -->
					<h6></h6>
					<span class="fa fa-cogs" aria-hidden="true"
						></span>
					<h5>设备管理</h5>
				</div>
				<div class="container-fluid mainfunctionButton"
					style="background-color: #CCCCCC;" id="otherModule">
					<h6></h6>
					<span class="fa fa-plus-circle" aria-hidden="true"
						></span>
					<h5>其它功能</h5>
				</div>
			</div>
		</div>
		<!-- 设备运行时间图表 -->
		<div class="main_right_bottom">
			
			<div id="myChart"></div>
			
			<div style="width: 15%; height: 100%; float: right; text-align: center;">
				<div class="container-fluid main_myChart_button" id="addBtn"
					style="background-color: #FFCC00;cursor: pointer;">
					<span class="fa fa-plus-circle main_myChart_button_size" ></span>
				</div>
				<div class="container-fluid main_myChart_button" id="searchBtn"
					style="background-color: #FFCC99;cursor: pointer;">
					<span class="fa fa-search main_myChart_button_size" aria-hidden="true"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="end.jsp"%>
<!-- 普通用户登录窗口 -->
<div class="modal fade loadingModal" id="userLoginByEmployeeCodeDialog"
	aria-labelledby="myModalLabel" style="overflow: hidden;height: 400px">
	<div class="modal-dialog" role="document" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="container-fluid" style="text-align: center;">
				<div class="loginimage">
					<span class="fa fa-user-circle" aria-hidden="true"
						style="font-size: 80px"></span>
				</div>
				<!-- <div style="margin-top: 5px;width: 100%">
				<select id="classes" style="width: 400px;" 
									class="selectpicker" data-live-search="true">	
								</select>
				</div> -->
				<input type="text" id="employeeCodeInput" class="form-control"
					style="margin-top: 10px;" autofocus="autofocus"
					placeholder="请扫码用户二维码或刷卡登录" required="required" AUTOCOMPLETE="off" /> 
					<span
					class="fa fa-qrcode" style="font-size: 100px;"></span>
				<div class="modal-footer">
					<a href="javascript:void(0);" data-dismiss="modal"
						onclick="showUsernameAndPasswordLoginDialog()">使用用户名密码登录</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 普通用户签退窗口 -->
<div class="modal fade loadingModal" id="userLogOutByEmployeeCodeDialog"
	aria-labelledby="myModalLabel" style="overflow: hidden;">
	<div class="modal-dialog" role="document" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="container-fluid" style="text-align: center;">
				<div class="loginimage">
					<span class="fa fa-user-circle" aria-hidden="true"
						style="font-size: 80px"></span>
				</div>
				<input type="text" id="employeeCode4Logout" class="form-control"
					style="margin-top: 10px;" autofocus="autofocus"
					placeholder="请扫描用户条码" required="required" AUTOCOMPLETE="off" /> 
					<span
					class="fa fa-qrcode" style="font-size: 100px;"></span>
					<div class="modal-footer">
					<a href="javascript:void(0);" data-dismiss="modal"
						onclick="showUsernameAndPasswordLogoutDialog()">使用用户名密码登出</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 用户名，密码登录框 -->
<div class="modal fade loadingModal"
	id="userLoginByUsernameAndPasswordDialog"
	aria-labelledby="myModalLabel" style="overflow: hidden;">
	<div class="modal-dialog" role="document" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="container-fluid"
				style="text-align: center; padding-right: 15px !important;">
				<div class="loginimage">
					<span class="fa fa-user-circle" aria-hidden="true"
						style="font-size: 60px"></span>
				</div>
				<!-- <div style="margin-top: 5px;width: 100%">
				<select id="classes2" style="width: 100%;" 
									class="selectpicker" data-live-search="true">	
								</select>
				</div> -->
				<input type="text" id="username" class="form-control"
					style="margin-top: 10px;" name="username" placeholder="请输入用户名"
					required="required" AUTOCOMPLETE="off" autofocus="autofocus" /> <input
					type="password" id="password" class="form-control" name="password"
					required="required" AUTOCOMPLETE="off" placeholder="请输入密码"
					style="margin-top: 6px;"> <span class="fa fa-qrcode"
					style="font-size: 80px; cursor: pointer;"
					onclick="showQrLoginDialog()"></span>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" style="width: 100px;"
						onclick="loginByUsernameAndPassword()">登录</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 用户名，密码登录框 -->
<div class="modal fade loadingModal"
	id="userLogoutByUsernameAndPasswordDialog"
	aria-labelledby="myModalLabel" style="overflow: hidden;">
	<div class="modal-dialog" role="document" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="container-fluid"
				style="text-align: center; padding-right: 15px !important;">
				<div class="loginimage">
					<span class="fa fa-user-circle" aria-hidden="true"
						style="font-size: 60px"></span>
				</div>
				<input type="text" id="usernameout" class="form-control"
					style="margin-top: 10px;" name="usernameout" placeholder="请输入用户名"
					required="required" AUTOCOMPLETE="off" autofocus="autofocus" /> <input
					type="password" id="passwordout" class="form-control" name="passwordout"
					required="required" AUTOCOMPLETE="off" placeholder="请输入密码"
					style="margin-top: 6px;"> <span class="fa fa-qrcode"
					style="font-size: 80px; cursor: pointer;"
					onclick="showQrLogoutDialog()"></span>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" style="width: 100px;"
						onclick="logoutByUsernameAndPassword()">登出</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 员工登出，具有未处理的损时提示框 -->
<div class="modal fade loadingModal" id="unhandledLostTimeRecordDialog"
	aria-labelledby="myModalLabel" style="overflow: hidden;">
	<div class="modal-dialog" role="document" style="width: 400px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<p>您有损时未处理!</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" style="width: 100px;"
					onclick="$('#userLogOutByEmployeeCodeDialog').modal('hide');window.location.href='<%=basePath%>'mc/lostTime.jsp?deviceSiteCode=' + _deviceSiteCode;"
					data-dismiss="modal">前去处理</button>
				<button type="button" class="btn btn-default" style="width: 100px;"
					onclick="employeeLogout()" data-dismiss="modal">继续登出</button>
			</div>
		</div>
	</div>
</div>
<!-- 损时扫描条码窗口 -->
<!-- <div class="modal fade" id="scanLostTimeCodeDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="z-index:9999">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: center;">
				<span class="fa fa-qrcode fa-4x"></span>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<label class="col-sm-2 control-label" for="pressLightCode">损时条码</label>
						<div class="col-sm-9">
							<input type="text" name="lostTimeCode" onblur="lostfocus()"
								class="form-control" id="lostTimeCode" autofocus="autofocus" />
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span  style="text-decoration: underline;"
						onclick="manualinput()">手工输入</span>
				</div>
			</div>
		</div>
	</div>
</div> -->

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
					<span style="font-size: 20px;">损时类型</span> <select id="lostTimeType" style="width: 60px; display: inline-block;" 
									class="selectpicker" data-live-search="true">	
								</select><input type="hidden" id="deviceSiteCode" name="deviceSite.code" />
								<input type="hidden" name="id" id="id" /><br>
					<br>
					<!-- <br> <span style="font-size: 20px;">损时原因</span> <input
									id="lostTimeReason" type="text" -->
									<br> <span style="font-size: 20px;">损时原因</span> 
               <select id="lostTimeReason" style="width: 60px; display: inline-block;" 
                           class="selectpicker" data-live-search="true">
                        </select>
               <!-- <input id="lostTimeReason" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control"  />--><img src="mc/assets/css/images/cx.png"
									style="margin-top: 0px; margin-left: 5px;"><br>
					<br>
					<br> <span style="font-size: 20px;">合计时间</span> <input
									id="addLostTime" type="text"
									style="width: 220px; display: inline-block; text-align: right;"
									class="form-control" readonly="readonly" /><br>
					<br>
					<br>
				</form>
				<div
					style="width: 400px; height: 200px; float: right; margin-top: 100px; margin-right: 50px">
				<!-- timepicker 组件-->
					<div style="margin-bottom: 10px;">
						<span style="font-size: 20px; margin-left: 40px;">损时开始</span>
						<span style="font-size: 20px; margin-left: 140px;">损时结束</span>
					</div>
					<div class="col-md-4"
						style="display: -webkit-inline-box; padding-left: 0px">
						<div class="input-group">
							<input type="text" name="startTime" class="form-control"
									id="startTime" style="width: 170px"/>
						</div>

					</div>
					<div class="col-md-4" style="margin-left: 70px">
						<div class="input-group">
							<input type="text" name="endTime" class="form-control"
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
						class="fa fa-check fa-4x" ></span>
				</div>
			</div>
		</div>
	</div>
</div>