var tab = "untreated";// tab代码(dataid属性值)
var rids = ""; // 记录id的字符串
var id = ""; // table选中记录ID
var param = ""; // 判断是修改提交，还是确认验证
var Devid; // 站点ID
var pressLightCode; // 故障原因CODE
var endVal = ""; // 拆分选中数据结束时间
var startVal = ""; // 拆分选中数据开始时间
var sum = 0; // 损时总时长
var count = 0;// 损时数量
var date = new Date();
var _deviceSiteCode = ""; // 选择的设备编码
var ordId = ""; // 选择数据Id
var imageVal = 0; // 选择数据Id
var idList = ""; // 存储图片路径
var imageList = [];

/* 删除功能 */
function delImg(obj) {
	var _this = $(obj);
	var id = _this.siblings(".input").val();
	var photoIds = idList.split(",");
	photoIds.splice(id, 1, "");
	imageList.splice(id, 1, id)
	var plist = "";
	for (var i = 0; i < photoIds.length; i++) {
		if (i == 0) {
			plist = photoIds[i];
		} else {
			plist += "," + photoIds[i];
		}
	}
	idList = plist;
	$("#photoIds").val(photoIds)
	_this.parents(".imageDiv").remove();
	$(".addImages").show();
};

var dateForm = date.getFullYear() + '-' + (date.getMonth() + 1) + '-'
		+ date.getDate() + " " + date.getHours() + ":" + date.getMinutes()
		+ ":" + date.getSeconds();
$(function() {
	$("#photo").css("border", "1px dotted #00000E");
	$("#updatePhoto").css("border", "1px solid  #00000E");
	$("#maintenancePhoto").css("border", "1px solid  #00000E");
	var userAgent = navigator.userAgent; // 用于判断浏览器类型
	idList = $("#photoIds").val();
	setlostTimeType();
	$(".file")
			.change(
					function() {
						// 获取选择图片的对象
						var docObj = $(this)[0];
						var picDiv = $(this).parents(".picDiv");
						// 得到所有的图片文件
						var fileList = docObj.files;
						// 循环遍历
						for (var i = 0; i < fileList.length; i++) {
							for (var j = 0; j < imageList.length; j++) {
								if (imageList[j] !== "") {
									imageVal = imageList[j]
									break;
								}
							}
							// 动态添加html元素onclick='deleteImg(\""+ imageVal+ "\")
							var picHtml = "<div class='imageDiv' style='margin-left: 30px'> <img  id='img"
									+ fileList[i].name
									+ "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="
									+ imageVal + " /></div></div>";
							imageList.splice(imageVal, 1, "");
							picDiv.prepend(picHtml);
							// 获取图片imgi的对象
							var imgObjPreview = document.getElementById("img"
									+ fileList[i].name);

							var formData = new FormData();
							formData.append('file', fileList[i]);

							var url = "relatedDoc/uploadimg.do";
							$.ajax({
								type : 'POST',
								url : url,
								data : formData,
								contentType : false,
								processData : false,
								dataType : 'json',
								mimeType : "multipart/form-data",
								success : function(data) {
									if (!idList) {
										idList = data.filePath;
										$("#photoIds").val(idList)
									} else {
										var photoIds = idList.split(",");
										photoIds.splice(imageVal, 1,
												data.filePath);
										idList = photoIds.join(',');
										$("#photoIds").val(idList)
									}
								}
							});
							if (fileList && fileList[i]) {
								// 图片属性
								imgObjPreview.style.display = 'block';
								imgObjPreview.style.width = '180px';
								imgObjPreview.style.height = '180px';
								// imgObjPreview.src =
								// docObj.files[0].getAsDataURL();
								// 火狐7以上版本不能用上面的getAsDataURL()方式获取，需要以下方式
								if (userAgent.indexOf('MSIE') == -1) {
									// IE以外浏览器
									imgObjPreview.src = window.URL
											.createObjectURL(docObj.files[i]); // 获取上传图片文件的物理路径;
								} else {
									// IE浏览器
									if (docObj.value.indexOf(",") != -1) {
										var srcArr = docObj.value.split(",");
										imgObjPreview.src = srcArr[i];
									} else {
										imgObjPreview.src = docObj.value;
									}
								}
							}
							for (var j = 0; j < imageList.length; j++) {
								if (imageList[j] !== "") {
									$(".addImages").show();
									break;
								}
								$(".addImages").hide();
							}
						}
					})

	$('#startTime').val(new Date());
	$('#endTime').val(new Date());
	$("#alertDialog").css("z-index", "10001")

	// 获取站点的ID
	$.get("mcequipment/findDeviceSiteId.do", {
		deviceSiteCode : $("#deviceSiteCode").val()
	}, function(data) {
		Devid = data.id;
	});
	// 查询所有本地设备站点
	$
			.get(
					contextPath + "mcdeviceSite/getAllMCDeviceSite.do",
					function(data) {
						if (data && data.length > 0) {
							// 显示所有设备站点的div
							var allDeviceSites = $("#allDeviceSites");
							for (var i = 0; i < data.length; i++) {
								var mcDeviceSite = data[i];
								var $button = $("<button class='btn btn-default' type='button' style='margin-right:5px;' onclick='showDetailDevice(\""
										+ mcDeviceSite.deviceSiteCode
										+ "\")'>"
										+ mcDeviceSite.deviceSiteCode
										+ "</button>");
								allDeviceSites.append($button);
								if (i == 0) {
									initLostTimeTable(mcDeviceSite.deviceSiteCode);
								}
							}
						}
						// 点击第一个按钮
						$("#allDeviceSites button:first").click();
					});

	// 本地设备站点按钮点击事件
	showDetailDevice = function(deviceSiteCode) {
		_deviceSiteCode = deviceSiteCode;
		$("#allDeviceSites button").removeClass("btn btn-primary");
		$("#allDeviceSites button").addClass("btn btn-default");
		var btns = $("#allDeviceSites button");
		for (var i = 0; i < btns.length; i++) {
			var btn = $(btns[i]);
			var textOnButton = btn.text();
			if (textOnButton === _deviceSiteCode) {
				btn.removeClass("btn btn-default");
				btn.addClass("btn btn-primary");
			}
		}

		if (!_deviceSiteCode) {
			return;
		}
	}

	// 下拉框未选中显示
	$(".selectpicker").selectpicker({
		noneSelectedText : '请选择' // 默认显示内容
	});
	$(".flowSuggestion").selectpicker({

		noneSelectedText : '请选择',

		width : '100%'

	});

});

$(function() {
	// 报修单时间初始化
	$("#createDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii:ss',
		language : 'zh-CN',
		autoclose : true,
		todayBtn : 'linked'
	});
	// 报修单时间初始化
	$("#StaffcreateDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii:ss',
		language : 'zh-CN',
		autoclose : true,
		todayBtn : 'linked'
	});
	// 报修单时间初始化
	$("#SparepartcreateDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii:ss',
		language : 'zh-CN',
		autoclose : true,
		todayBtn : 'linked'
	});
	// 保养时间初始化
	$("#maintenanceCreateDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii:ss',
		language : 'zh-CN',
		autoclose : true,
		todayBtn : 'linked'
	});
	$("#endTime").datetimebox({
		required : true,
		parser : function(date) {
			return new Date(Date.parse(date.replace(/-/g, "/")));
		}
	});
	$("#endTime").datetimebox('setValue', dateForm);
})

// 点击tab事件
function lostTimeTabClick(e) {
	var u;
	id = "";
	tab = e.getAttribute("dataid");
	if (tab == "MAINTENANCEITEM") { // 报修、维修
		$(".CheckingPlanTable").hide();
		$(".MaintenancePlanRecordsTable").hide();
		$(".DeviceRepairOrderTable").show();
		$(".DocTable").hide();
		$(".mc_device_maintenanceitem").show();
		$(".mc_device_spotinspection").hide();
		$(".mc_device_maintain").hide();
		$(".mc_device_doc").hide();
		$("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
			url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
			cache : false,
			query : {
				deviceCode : _deviceSiteCode
			}
		});
	} else if (tab == "MAINTAIN") { // 保养
		$(".CheckingPlanTable").hide();
		$(".MaintenancePlanRecordsTable").show();
		$(".DeviceRepairOrderTable").hide();
		$(".DocTable").hide();
		$(".mc_device_maintain").show();
		$(".mc_device_maintenanceitem").hide();
		$(".mc_device_spotinspection").hide();
		$(".mc_device_doc").hide();
		$("#showMaintenancePlanRecordsTable")
				.bootstrapTable(
						"refresh",
						{
							url : "mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
							cache : false,
							query : {
								deviceSiteCode : _deviceSiteCode
							}
						});
	} else if (tab == "DOC") { // 文档
		$(".CheckingPlanTable").hide();
		$(".MaintenancePlanRecordsTable").hide();
		$(".DeviceRepairOrderTable").hide();
		$(".DocTable").show();
		$(".mc_device_doc").show();
		$(".mc_device_spotinspection").hide();
		$(".mc_device_maintenanceitem").hide();
		$(".mc_device_maintain").hide();
		$("#showDocTable").bootstrapTable("refresh", {
			url : "relatedDoc/queryRelatedDocumentByDeviceId.do",
			cache : false,
			query : {
				deviceCode : _deviceSiteCode
			}
		});
	}
};
// tab选中样式
$(function() {
	$("#myTab li:first").addClass("active");
});

// 初始化损时列表table
var beginData = "";
var endData = "";
var sum = 0;
var count = 0;
function initLostTimeTable(deviceCode) {
	id = "";
	$(".mc_device_spotinspection").show();
	$(".DeviceRepairOrderTable").hide();
	$(".MaintenancePlanRecordsTable").hide();
	$(".DocTable").hide();
	var deviceSiteCode = $("#deviceSiteCode").val();

	$("#showDeviceRepairOrderTable")
			.bootstrapTable(
					{
						url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
						cache : false,
						height : 370,
						// idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, // 隔行换色
						queryParams : function(params) {
							var temp = {
								deviceCode : deviceCode
							};
							return temp;
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								},
								{
									field : 'createDate',
									align : 'center',
									title : '报修时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
									}
								},
								{
									field : 'completeDate',
									align : 'center',
									title : '维修完成时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'status',
									align : 'center',
									title : '状态',
									formatter : function(value, row, index) {
										if (value == "WAITINGASSIGN") {
											return "等待派单";
										} else if (value == "WAITINCOMFIRM") {
											return "等待接单确认";
										} else if (value == "MAINTAINING") {
											return "维修中";
										} else if (value == "WORKSHOPCOMFIRM") {
											return "车间确认";
										} else if (value == "MAINTAINCOMPLETE") {
											return "维修完成";
										} else if (value == "WAITWORKSHOPCOMFIRM") {
											return "待车间确认";
										}
										return "";
									}
								}, {
									field : 'pressLight',
									align : 'center',
									title : '故障类型',
									formatter : function(value, row, index) {
										if (row.pressLight) {
											return row.pressLight.reason;
										}
										return "";
									}
								}, {
									field : 'maintainName',
									align : 'center',
									title : '责任人',
									formatter : function(value, row, index) {
										if (value) {
											return value;
										}
										return "";
									}
								} ]
					});
	$("#showMaintenancePlanRecordsTable")
			.bootstrapTable(
					{
						url : "mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
						cache : false,
						height : 370,
						// idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, // 隔行换色
						queryParams : function(params) {
							var temp = {
								deviceSiteCode : deviceCode
							};
							return temp;
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								},
								{
									field : 'maintenanceType',
									align : 'center',
									title : '保养类别',
									formatter : function(value, row, index) {
										if (value) {
											return value;
										}
										return "";
									}
								},
								{
									field : 'maintenanceDate',
									align : 'center',
									title : '计划日期',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'maintenancedDate',
									align : 'center',
									title : '保养时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								}, {
									field : 'status',
									align : 'center',
									title : '状态',
									formatter : function(value, row, index) {
										if (value) {
											return value;
										}
										return "";
									}
								}, {
									field : 'employeeName',
									align : 'center',
									title : '责任人',
									formatter : function(value, row, index) {
										if (value) {
											return value;
										}
										return "";
									}
								}, {
									field : 'confirmName',
									align : 'center',
									title : '确认人',
									formatter : function(value, row, index) {
										if (value) {
											return value;
										}
										return "";
									}
								} ]
					});
	$("#showDocTable").bootstrapTable({
		url : "relatedDoc/queryRelatedDocumentByDeviceId.do",
		cache : false,
		height : 370,
		// idField : 'id',
		singleSelect : true,
		clickToSelect : true,
		striped : true, // 隔行换色
		queryParams : function(params) {
			var temp = {
				deviceCode : deviceCode
			};
			return temp;
		},
		columns : [ {
			checkbox : true
		}, {
			field : 'id',
			title : 'Id'
		}, {
			field : 'relatedDocumentType',
			align : 'center',
			title : '文件类别',
			formatter : function(value, row, index) {
				if (row.relatedDocumentType) {
					return row.relatedDocumentType.name;
				}
				return "";
			}
		}, {
			field : 'name',
			align : 'center',
			title : '文件名称',
		}, {
			field : 'note',
			align : 'center',
			title : '说明',
			formatter : function(value, row, index) {
				if (value) {
					return value;
				}
				return "";
			}
		} ]
	});
	// 报修单维修人员信息
	$("#showStaffTable")
			.bootstrapTable(
					{
						url : "maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
						cache : false,
						height : 370,
						width : 900,
						// idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, // 隔行换色
						queryParams : function(params) {
							var temp = {
								deviceRepairOrderId : ordId
							};
							return temp;
						},
						responseHandler : function(data) {
							return data.rows;
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								},
								{
									field : 'code',
									align : 'center',
									title : '员工代码',
								},
								{
									field : 'name',
									align : 'center',
									title : '员工名称'
								},
								{
									field : 'receiveType',
									align : 'center',
									title : '接单类型',
									formatter : function(value, row, index) {
										if (value == "SYSTEMGASSIGN") {
											return "系统派单";
										} else if (value == "ARTIFICIALGASSIGN") {
											return "人工派单";
										} else if (value == "ASSIST") {
											return "协助";
										} else {
											return "";
										}
									}
								},
								{
									field : 'assignTime',
									align : 'center',
									title : '派单时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'receiveTime',
									align : 'center',
									title : '接收时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'completeTime',
									align : 'center',
									title : '完成时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								} ]
					});
	// 报修单故障原因列表
	$("#showNGRecordTable").bootstrapTable({
		url : "ngMaintainRecord/queryNGMaintainRecordVO.do",
		cache : false,
		height : 370,
		// idField : 'id',
		singleSelect : true,
		clickToSelect : true,
		striped : true, // 隔行换色
		queryParams : function(params) {
			var temp = {
				deviceRepairOrderId : ordId
			};
			return temp;
		},
		responseHandler : function(data) {
			return data.rows;
		},
		columns : [ {
			checkbox : true
		}, {
			field : 'id',
			title : 'Id'
		}, {
			field : 'pressLightTypeCode',
			align : 'center',
			title : '故障类别代码',
			formatter : function(value, row, index) {
				if (row.projectType) {
					if (row.projectType.parent) {
						return row.projectType.parent.code;
					}
				}
				return "";
			}
		}, {
			field : 'pressLightTypeName',
			align : 'center',
			title : '故障类别',
			formatter : function(value, row, index) {
				if (row.projectType) {
					if (row.projectType.parent) {
						return row.projectType.parent.name;
					}
				}
				return "";
			}
		}, {
			field : 'pressLightCode',
			align : 'center',
			title : '类型代码',
			formatter : function(value, row, index) {
				if (row.projectType) {
					return row.projectType.code;
				}
				return "";
			}
		}, {
			field : 'pressLightName',
			align : 'center',
			title : '故障类型',
			formatter : function(value, row, index) {
				if (row.projectType) {
					return row.projectType.name;
				}
				return "";
			}
		}, {
			field : 'note',
			align : 'center',
			title : '说明',
			formatter : function(value, row, index) {
				if (row.note) {
					return row.note;
				}
				return "";
			}
		}, {
			field : 'processingMethod',
			align : 'center',
			title : '处理方法',
			formatter : function(value, row, index) {
				if (row.processingMethod) {
					return row.processingMethod;
				}
				return "";
			}
		}, {
			field : 'remark',
			align : 'center',
			title : '备注',
			formatter : function(value, row, index) {
				if (row.remark) {
					return row.remark;
				}
				return "";
			}
		} ]
	});
	// 报修单维修项目列表
	$("#showMaintainProjectTable").bootstrapTable({
		url : "maintainProject/queryMaintainProject.do",
		cache : false,
		height : 370,
		// idField : 'id',
		singleSelect : true,
		clickToSelect : true,
		striped : true, // 隔行换色
		queryParams : function(params) {
			var temp = {
				deviceRepairOrderId : ordId
			};
			return temp;
		},
		responseHandler : function(data) {
			return data.rows;
		},
		columns : [ {
			checkbox : true
		}, {
			field : 'id',
			title : 'Id'
		}, {
			field : 'typeCode',
			align : 'center',
			title : '类别代码',
			formatter : function(value, row, index) {
				if (row.type) {
					return row.type.code;
				}
				return "";
			}
		}, {
			field : 'typeName',
			align : 'center',
			title : '类别名称',
			formatter : function(value, row, index) {
				if (row.type) {
					return row.type.name;
				}
				return "";
			}
		}, {
			field : 'code',
			align : 'center',
			title : '项目代码'
		}, {
			field : 'name',
			align : 'center',
			title : '项目名称'
		}, {
			field : 'note',
			align : 'center',
			title : '说明'
		}, {
			field : 'processingMethod',
			align : 'center',
			title : '处理方法'
		}, {
			field : 'remark',
			align : 'center',
			title : '备注'
		} ]
	});

	// 备件列表
	$("#showSparepartRecordTable")
			.bootstrapTable(
					{
						url : "sparepartRecord/querySparepartRecord.do",
						cache : false,
						height : 370,
						// idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, // 隔行换色
						queryParams : function(params) {
							var temp = {
								deviceRepairOrderId : ordId
							};
							return temp;
						},
						responseHandler : function(data) {
							return data.rows;
						},
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								}/*
									 * ,{ field : 'typeCode', align : 'center',
									 * title : '类别代码', formatter :
									 * function(value, row, index) { if
									 * (row.sparepart) {
									 * if(row.sparepart.projectType){ return
									 * row.sparepart.projectType.code; } }
									 * return ""; } }
									 */,
								{
									field : 'typeName',
									align : 'center',
									title : '备件类别',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											if (row.sparepart.projectType) {
												return row.sparepart.projectType.name;
											}
										}
										return "";
									}
								},
								{
									field : 'sparepartCode',
									align : 'center',
									title : '备件代码',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.code;
										}
										return "";
									}
								},
								{
									field : 'sparepartName',
									align : 'center',
									title : '备件名称',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.name;
										}
										return "";
									}
								},
								{
									field : 'unitType',
									align : 'center',
									title : '规格型号',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.unitType;
										}
										return "";
									}
								},
								{
									field : 'batchNumber',
									align : 'center',
									title : '批号',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.batchNumber;
										}
										return "";
									}
								},
								{
									field : 'graphNumber',
									align : 'center',
									title : '图号',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.graphNumber;
										}
										return "";
									}
								},
								{
									field : 'measurementUnit',
									align : 'center',
									title : '计量单位',
									formatter : function(value, row, index) {
										if (row.sparepart) {
											return row.sparepart.measurementUnit;
										}
										return "";
									}
								},
								{
									field : 'quantity',
									align : 'center',
									title : '数量'
								},
								{
									field : 'note',
									align : 'center',
									title : '备注'
								},
								{
									field : 'createDate',
									align : 'center',
									title : '耗用时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								} ]
					});

	$('#showCheckingPlanTable').bootstrapTable('hideColumn', 'id');
	$("#showCheckingPlanTable").on('click-row.bs.table',
			function(e, row, element) { // splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
				$(element).addClass('success'); // 添加当前选中的 success样式用于区别
			});
	$('#showDeviceRepairOrderTable').bootstrapTable('hideColumn', 'id');
	$("#showDeviceRepairOrderTable").on('click-row.bs.table',
			function(e, row, element) { // splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
				$(element).addClass('success'); // 添加当前选中的 success样式用于区别
			});
	$('#showMaintenancePlanRecordsTable').bootstrapTable('hideColumn', 'id');
	$("#showMaintenancePlanRecordsTable").on('click-row.bs.table',
			function(e, row, element) { // splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
				$(element).addClass('success'); // 添加当前选中的 success样式用于区别
			});
	$('#showDocTable').bootstrapTable('hideColumn', 'id');
	$("#showDocTable").on('click-row.bs.table', function(e, row, element) { // splitLostTime
																			// confirmLostTime
		id = row.id;
		$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
		$(element).addClass('success'); // 添加当前选中的 success样式用于区别
	});
	$('#showStaffTable').bootstrapTable('hideColumn', 'id');
	$("#showStaffTable").on('click-row.bs.table', function(e, row, element) { // splitLostTime
																				// confirmLostTime
		id = row.id;
		$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
		$(element).addClass('success'); // 添加当前选中的 success样式用于区别
	});
	$('#showNGRecordTable').bootstrapTable('hideColumn', 'id');
	$("#showNGRecordTable").on('click-row.bs.table', function(e, row, element) { // splitLostTime
																					// confirmLostTime
		id = row.id;
		$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
		$(element).addClass('success'); // 添加当前选中的 success样式用于区别
	});
	$('#showMaintainProjectTable').bootstrapTable('hideColumn', 'id');
	$("#showMaintainProjectTable").on('click-row.bs.table',
			function(e, row, element) { // splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
				$(element).addClass('success'); // 添加当前选中的 success样式用于区别
			});
	$('#showSparepartRecordTable').bootstrapTable('hideColumn', 'id');
	$("#showSparepartRecordTable").on('click-row.bs.table',
			function(e, row, element) { // splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); // 去除之前选中的行的，选中样式
				$(element).addClass('success'); // 添加当前选中的 success样式用于区别
			});
}
// 类型改变事件
$(document)
		.ready(
				function() {
					// 新增报修单故障类型变更
					$("#pressLight").change(function() {
						var id = $("#pressLight option:selected").val();
						$("#pressLightId").val(id);
					});
					// 维修报修单故障类型变更
					$("#updatePressLight").change(function() {
						var id = $("#updatePressLight option:selected").val();
						$("#updatePressLightId").val(id);
					});
					// 维修人员变更
					$("#StaffName").change(function() {
						var code = $("#StaffName option:selected").val();
						$("#StaffCode").val(code);
					});
					// 维修人员变更
					$("#maintenanceInformantName")
							.change(
									function() {
										var code = $(
												"#maintenanceInformantName option:selected")
												.val();
										$("#maintenanceInformantCode")
												.val(code);
									});
					// 故障类型变更
					$("#NGMaintainName")
							.change(
									function() {
										var code = $(
												"#NGMaintainName option:selected")
												.val();
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "deviceProject/queryAllDeviceProjectByProjectTypeId.do",
													data : {
														projectTypeId : code
													},
													cache : false,
													dataType : "json",
													success : function(data) {

														var htmlselect = "<option></option>";
														$
																.each(
																		data,
																		function(
																				index,
																				Type) {
																			htmlselect += "<option value='"
																					+ Type.id
																					+ "'>"
																					+ Type.name
																					+ "</option>";
																		})
														$("#deviceproject")
																.empty();
														$("#deviceproject")
																.append(
																		htmlselect);
														$("#deviceproject")
																.selectpicker(
																		'refresh');
														$("#NGMaintainCode")
																.val(data.code);
													}
												})
									});

					// 故障原因变更
					$("#deviceproject")
							.change(
									function() {
										var id = $(
												"#deviceproject option:selected")
												.val();
										if (!id) {
											$("#NGMaintainId").val("");
											return;
										}
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "deviceProject/queryDeviceProject.do",
													data : {
														id : id
													},
													cache : false,
													dataType : "json",
													success : function(data) {

														$("NGMaintainName")
																.val(
																		data.projectType.id);
														$("#NGMaintainName")
																.selectpicker(
																		'val',
																		data.projectType.id);
														$("#NGMaintainId").val(
																data.id);
													}
												})
									});
					// 维修项目变更
					$("#MaintainProjectName")
							.change(
									function() {
										var id = $(
												"#MaintainProjectName option:selected")
												.val();
										if (!id) {
											$("#MaintainProjectCode").val("");
											$("#MaintainProjectTypeId").val("");
											return;
										}
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "deviceProject/queryDeviceProjectById.do",
													data : {
														id : id
													},
													cache : false,
													dataType : "json",
													success : function(data) {
														$(
																"#MaintainProjectCode")
																.val(data.code);
														$(
																"#MaintainProjectTypeId")
																.val(
																		data.deviceTypeId);
													}
												})
									});
					$("#MaintainProjectTypeName")
							.change(
									function() {
										var id = $(
												"#MaintainProjectTypeName option:selected")
												.val();
										if (!id) {
											$("#MaintainProjectCode").val("");
											$("#MaintainProjectTypeId").val("");
											return;
										}
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "deviceProject/queryDevicesProjectByProjectTypeIdNotPage.do?type=MAINTENANCEITEM",
													data : {
														projectTypeId : id
													},
													cache : false,
													dataType : "json",
													success : function(data) {
														$(
																"#MaintainProjectName")
																.empty();
														$(
																"#MaintainProjectTypeCode")
																.val(
																		data[0].deviceTypeCode);
														var htmlselect = "<option></option>";
														$
																.each(
																		data,
																		function(
																				index,
																				Type) {
																			htmlselect += "<option value='"
																					+ Type.id
																					+ "'>"
																					+ Type.name
																					+ "</option>";
																			var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
																					+ Type.id
																					+ "\")' value='"
																					+ Type.code
																					+ "'>"
																					+ Type.name
																					+ "</button>");
																		})
														$(
																"#MaintainProjectName")
																.append(
																		htmlselect);
														$(
																"#MaintainProjectName")
																.selectpicker(
																		'refresh');
													}
												})
									});
					// 备件信息变更
					$("#SparepartName")
							.change(
									function() {
										var code = $(
												"#SparepartName option:selected")
												.val();
										if (!code) {
											$("#SparepartCode").val("");
											$("#SparepartId").val("");
											return;
										}
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "sparepart/querySparepartsByCode.do",
													data : {
														SparepartsCode : code
													},
													cache : false,
													dataType : "json",
													success : function(data) {
														$("#SparepartCode")
																.val(data.code);
														$("#SparepartId").val(
																data.id);
													}
												})
									});
					// 备件信息变更
					$("#MaintenanceSparepartName")
							.change(
									function() {
										var code = $(
												"#MaintenanceSparepartName option:selected")
												.val();
										if (!code) {
											$("#MaintenanceSparepartCode").val(
													"");
											$("#MaintenanceSparepartId")
													.val("");
											return;
										}
										$
												.ajax({
													type : "post",
													url : contextPath
															+ "sparepart/querySparepartsByCode.do",
													data : {
														SparepartsCode : code
													},
													cache : false,
													dataType : "json",
													success : function(data) {
														$(
																"#MaintenanceSparepartCode")
																.val(data.code);
														$(
																"#MaintenanceSparepartId")
																.val(data.id);
													}
												})
									});
				});

// 模态框点击返回事件(hide)
function modelHide() {
	$('#showConfirmDlg').modal('hide');
	$('#showUpdateDlg').modal('hide');
	$('#addDeviceRepairOrderDialog').modal('hide');
	$('#updateDeviceRepairOrderDialog').modal('hide');
	$('#addMaintenanceStaff').modal('hide');
	$('#updateCheckingPlanDialog').modal('hide');
	$('#maintenanceOrderDialog').modal('hide');
	$('#showDoclog').modal('hide');
}
// 拆分模态框点击返回事件(hide)
function splitModelHide() {
	$('#splitShowConfirmDlg').modal('hide');
	$('#splitShowUpdateDlg').modal('hide');

}

// 设备报修功能按钮
function addLostTime(e) {
	param = e;
	if (e == "repairAdd") {// 新增
		showAddDeviceRepairOrderDialogDialog();
	} else if (e == "repairUpdate") {// 修改
		var alldata = $("#showDeviceRepairOrderTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}/*
			 * else if(alldata[0].status!="MAINTAINING"){
			 * $("#alertText").text("请选择维修中或待车间确认的数据");
			 * $("#alertDialog").modal(); return false; }
			 */
		$("#addSparepartView").show();
		$("#addMaintainProjectView").show();
		$("#addNGMaintainView").show();
		$("#addMaintenanceStaffView").show();
		$("#removeSparepartView").show();
		$("#removeMaintainProjectView").show();
		$("#removeNGMaintainView").show();
		$("#removeMaintenanceStaffView").show();
		showUpdateDeviceRepairOrderDialogDialog(alldata[0].id);
	} else if (e == "repairView") {// 查看
		var alldata = $("#showDeviceRepairOrderTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		$("#addSparepartView").hide();
		$("#addMaintainProjectView").hide();
		$("#addNGMaintainView").hide();
		$("#addMaintenanceStaffView").hide();
		$("#removeSparepartView").hide();
		$("#removeMaintainProjectView").hide();
		$("#removeNGMaintainView").hide();
		$("#removeMaintenanceStaffView").hide();
		showUpdateDeviceRepairOrderDialogDialog(alldata[0].id);
	} else if (e == "repairConfirm") {// 确认
		var alldata = $("#showDeviceRepairOrderTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (alldata[0].status == "WAITWORKSHOPCOMFIRM") {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcDeviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=MAINTAINCOMPLETE",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新维修记录表
							$("#showDeviceRepairOrderTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
												cache : false,
												query : {
													deviceCode : _deviceSiteCode
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			$("#alertText").text("该记录不是可确认记录!");
			$("#alertDialog").modal();
		}
	} else if (e == "repairComplete") {// 维修完成
		if ($("#updateStatus").val() == "MAINTAINING") {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "paperlessDeviceRepair/updateDeviceRepairOrderStatusById.do",
						data : {
							"id" : $("#orderId").val(),
							"status" : "WAITWORKSHOPCOMFIRM"
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新维修记录表
							$('#content').attr('src',
									'paperless/dm/deviceService.jsp');
							alert(data.message);
						}
					})
		} else {
			$("#alertText").text("该记录不是可完成记录!");
			$("#alertDialog").modal();
			$('#updateDeviceRepairOrderDialog').modal('show');
		}
	} else if (e == "Maintenance") { // 保养
		var alldata = $("#showMaintenancePlanRecordsTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		$("#addMaintenanceSparepartView").show();
		// $("#addMaintenanceProjectView").show();
		$("#addStaffMaintenanceView").show();
		$("#removeMaintenanceSparepartView").show();
		// $("#removeMaintenanceProjectView").show();
		$("#removeStaffMaintenanceView").show();
		setMaintenancePlanRecordsTable(alldata[0].id);

		$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
			url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
			cache : false,
			query : {
				recordId : ordId
			}
		});
		// 刷新保养项目表
		$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
			url : "mcMaintenanceItem/queryMaintenanceItemById.do",
			cache : false,
			query : {
				recordId : ordId
			}
		});
		// 刷新备件信息表
		$("#showMaintenanceSparepartRecordTable")
				.bootstrapTable(
						"refresh",
						{
							url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
							cache : false,
							query : {
								recordId : ordId
							}
						});

		$('#maintenanceOrderDialog').modal('show');
	} else if (e == "MaintenanceConfirm") { // 确认
		var alldata = $("#showMaintenancePlanRecordsTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (!alldata[0].confirmDate && alldata[0].maintenancedDate) {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcMaintenancePlanRecord/confirm.do",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新维修人员表
							$("#showMaintenancePlanRecordsTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
												cache : false,
												query : {
													deviceSiteCode : _deviceSiteCode
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			$("#alertText").text("该记录已确认或未保养!");
			$("#alertDialog").modal();
		}
	} else if (e == "MaintenanceView") { // 查看
		$("#addMaintenanceSparepartView").hide();
		// $("#addMaintenanceProjectView").hide();
		$("#addStaffMaintenanceView").hide();
		$("#removeMaintenanceSparepartView").hide();
		// $("#removeMaintenanceProjectView").hide();
		$("#removeStaffMaintenanceView").hide();

		var alldata = $("#showMaintenancePlanRecordsTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		setMaintenancePlanRecordsTable(alldata[0].id);

		$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
			url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
			cache : false,
			query : {
				recordId : ordId
			}
		});
		// 刷新保养项目表
		$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
			url : "mcMaintenanceItem/queryMaintenanceItemById.do",
			cache : false,
			query : {
				recordId : ordId
			}
		});
		// 刷新备件信息表
		$("#showMaintenanceSparepartRecordTable")
				.bootstrapTable(
						"refresh",
						{
							url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
							cache : false,
							query : {
								recordId : ordId
							}
						});
		$('#maintenanceOrderDialog').modal('show');
	} else if (e == "DocView") {// 文档查看
		var alldata = $("#showDocTable").bootstrapTable('getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		$("#DocImg").attr("src", alldata[0].url);
		$('#showDoclog').modal('show');
	}
}
// 显示报修新增窗口
function showAddDeviceRepairOrderDialogDialog() {
	getAllReasonAdd();
	$('#ngDescription').val("");
	$('#picDiv').empty();
	$('div').remove(".imageDiv");
	$(".addImages").show();
	idList = "";
	imageList = [ 0, 1, 2, 3 ];
	$('#addDeviceRepairOrderDialog').modal('show');
	var date = new Date();
	var value = date.getFullYear()
			+ "-"
			+ ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1))
			+ "-"
			+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
			+ " "
			+ (date.getHours() > 9 ? date.getHours() : "0" + date.getHours())
			+ ":"
			+ (date.getMinutes() > 9 ? date.getMinutes() : "0"
					+ date.getMinutes())
			+ ":"
			+ (date.getSeconds() > 9 ? date.getSeconds() : "0"
					+ date.getSeconds());
	$("#createDate").val(value);
	$.ajax({
		type : "post",
		url : contextPath + "deviceSite/queryDeviceSiteByCode.do",
		data : {
			"deviceSiteCode" : _deviceSiteCode
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			if (data) {
				if (data.device) {
					$("#deviceCode").val(data.device.code);
					$("#deviceName").val(data.device.name);
					$("#unitType").val(data.device.unitType);
					$("#deviceType").val(data.device.projectType.name);
					// 根据IP地址查询本机是否有登录用户
					$.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do",
							function(mcUser) {
								if (mcUser) {
									$("#Informant").val(mcUser.employeeName);
								}
							});
					$("#deviceId").val(data.device.id);
				}
			}
		}
	})
}
// 查询需要修改的报修单信息
function showUpdateDeviceRepairOrderDialogDialog(orderId) {
	getAllReasonAdd();
	getPic(orderId);
	ordId = orderId;
	$('#picDiv').empty();
	$('#updateDeviceRepairOrderDialog').modal('show');
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "mcDeviceRepairOrder/queryDeviceRepairOrderById.do",
				data : {
					"id" : orderId
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					if (data) {
						$("#updateNGDescription").val(data.ngDescription);
						$("#orderId").val(data.id);
						$("#updateDeviceCode").val(data.deviceCode);
						$("#updateDeviceName").val(data.deviceName);
						$("#updateCreateDate").val(data.createDate);
						$("#updateUnitType").val(data.deviceUnitType);
						$("#updateDeviceType").val(data.deviceTypeName);
						$("#updateInformant").val(data.informant);
						$("#updateDeviceId").val(data.deviceId);
						$("#updateStatus").val(data.status);
						$("#updatePressLight").selectpicker('val', data.ngid);
						$("#updatePressLightId").val(data.ngid);
						// 刷新维修人员表
						$("#showStaffTable")
								.bootstrapTable(
										"refresh",
										{
											url : "maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
											cache : false,
											query : {
												deviceRepairOrderId : orderId
											}
										});
					}
				}
			})
}

// 点击手工输入事件
function manualinput() {
	$('#scanLostTimeCodeDialog').modal('hide');
	showConfirmDlg("add");
	$('#lostTimeCode').val(lostReasonCode);
	$('#lostTimeCode').keydown();

}
// 损时原因点击确认事件
function confirminput() {
	if (lostReasonCode) {
		$('#lostTimeCode').val(lostReasonCode);
		$('#lostTimeCode').change();
	} else {
		$("#alertText").text("请选择损时原因！");
		$("#alertDialog").modal();
	}
	lostReasonCode = "";
}
// 损时原因点击确认事件
function splitConfirminput() {
	if (lostReasonCode) {
		$('#splitLostTimeCode').val(lostReasonCode);
		$('#splitLostTimeCode').change();
	} else {
		$("#alertText").text("请选择损时原因！");
		$("#alertDialog").modal();
	}
	lostReasonCode = "";
}
// 点击手工输入事件
function splitManualinput() {
	$('#splitScanLostTimeCodeDialog').modal('hide');
	showConfirmDlg("split");
	$('#splitLostTimeCode').val(lostReasonCode);
	$('#splitLostTimeCode').keydown();

}
// 修改二维码扫描框改变事件
$('#pressLightCode').change(function() {
	$('#updateRcodeDialog').modal('hide');
	$("#pressLightCode").val($(this).val()).change();// key值
});

// 新增设备报修
function addDeviceRepair() {
	var deviceSiteCode = $("#deviceSiteCode").val();
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "mcDeviceRepairOrder/addDeviceRepairOrder.do",
				data : {
					"createDate" : $('#createDate').val(),
					"device.id" : $('#deviceId').val(),
					"Informant" : $('#Informant').val(),
					"ngreason.id" : $('#pressLightId').val(),
					"ngDescription" : $('#ngDescription').val(),
					"idList" : $('#photoIds').val()
				},
				dataType : "json",
				success : function(data) {
					$("#alertText").text(data.msg);
					$("#alertDialog").modal();
					$('#addDeviceRepairOrderDialog').modal('hide');
					if (data.success) {
						$("#showDeviceRepairOrderTable")
								.bootstrapTable(
										"refresh",
										{
											url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
											cache : false,
											query : {
												deviceCode : _deviceSiteCode
											}
										});
					}
				}
			})
}

// 更新设备报修
function updateDeviceRepair() {
	$('#updateDeviceRepairOrderDialog').modal('hide');
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "mcDeviceRepairOrder/updateDeviceRepairOrder.do",
				data : {
					"createDate" : $('#updateCreateDate').val(),
					"id" : $('#orderId').val(),
					"device.id" : $('#updateDeviceId').val(),
					"ngreason.id" : $('#updatePressLightId').val(),
					"ngDescription" : $('#updateNGDescription').val(),
				},
				dataType : "json",
				success : function(data) {
					$("#alertText").text(data.msg);
					$("#alertDialog").modal();
					$('#addDeviceRepairOrderDialog').modal('hide');
					if (data.success) {
						$("#showDeviceRepairOrderTable")
								.bootstrapTable(
										"refresh",
										{
											url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
											cache : false,
											query : {
												deviceCode : _deviceSiteCode
											}
										});
					}
				}
			})
}
// 修改设备报修
function addDeviceRepair() {
	var deviceSiteCode = $("#deviceSiteCode").val();
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "mcDeviceRepairOrder/addDeviceRepairOrder.do",
				data : {
					"createDate" : $('#createDate').val(),
					"device.id" : $('#deviceId').val(),
					"Informant" : $('#Informant').val(),
					"pressLight.id" : $('#pressLightId').val(),
					"ngDescription" : $('#ngDescription').val(),
					"idList" : $('#photoIds').val()
				},
				dataType : "json",
				success : function(data) {
					$("#alertText").text(data.msg);
					$("#alertDialog").modal();
					$('#addDeviceRepairOrderDialog').modal('hide');
					if (data.success) {
						$("#showDeviceRepairOrderTable")
								.bootstrapTable(
										"refresh",
										{
											url : "mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
											cache : false,
											query : {
												deviceCode : _deviceSiteCode
											}
										});
					}
				}
			})
}

function getPic(id) {
	var url = "deviceRepairOrder/queryDeviceRepairOrderById.do?id=" + id;
	$
			.ajax({
				type : 'POST',
				url : url,
				contentType : false,
				processData : false,
				dataType : 'json',
				mimeType : "multipart/form-data",
				success : function(data) {
					var picDiv = $("#updatePicDiv");
					picDiv.empty();
					if (data.picName) {
						var picName = data.picName
						for (var i = 0; i < picName.length; i++) {
							var picHtml = "<div class='imageDiv' style='margin-top: -0px'> <img  id='img"
									+ i
									+ "' src='"
									+ contextPath
									+ picName[i]
									+ "' style='height: 250px;width: 300px;float: left;' /> </div>";
							picDiv.append(picHtml);
						}
					}
				}
			});
}

$(function() {
	$(".tabbox li")
			.click(
					function() {
						// 获取点击的元素给其添加样式，讲其兄弟元素的样式移除
						$(this).addClass("active").siblings().removeClass(
								"active");
						$(this).addClass("active").siblings().css("border",
								"1px solid #666666");
						// 移除边框属性
						$(this).css("border", "0px solid #666666");
						// 获取选中元素的下标
						var index = $(this).index();
						$(this).parent().siblings().children().eq(index)
								.addClass("active").siblings().removeClass(
										"active");
						if (index == 1) {
							// 刷新维修人员表
							$("#showStaffTable")
									.bootstrapTable(
											"refresh",
											{
												url : "maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
												cache : false,
												query : {
													deviceRepairOrderId : ordId
												}
											});
						} else if (index == 2) {
							// 刷新维修人员表
							$("#showNGRecordTable")
									.bootstrapTable(
											"refresh",
											{
												url : "ngMaintainRecord/queryNGMaintainRecordVO.do",
												cache : false,
												query : {
													deviceRepairOrderId : ordId
												}
											});
						} else if (index == 3) {
							// 刷新维修项目表
							$("#showMaintainProjectTable")
									.bootstrapTable(
											"refresh",
											{
												url : "maintainProject/queryMaintainProject.do",
												cache : false,
												query : {
													deviceRepairOrderId : ordId
												}
											});
						} else if (index == 4) {
							// 刷新备件信息表
							$("#showSparepartRecordTable")
									.bootstrapTable(
											"refresh",
											{
												url : "sparepartRecord/querySparepartRecord.do",
												cache : false,
												query : {
													deviceRepairOrderId : ordId
												}
											});
						}
					});
	$(".mtabbox li")
			.click(
					function() {
						// 获取点击的元素给其添加样式，讲其兄弟元素的样式移除
						$(this).addClass("active").siblings().removeClass(
								"active");
						$(this).addClass("active").siblings().css("border",
								"1px solid #666666")
						// 移除边框属性
						$(this).css("border", "1px solid #666666")
						// 获取选中元素的下标
						var index = $(this).index();
						$(this).parent().siblings().children().eq(index)
								.addClass("active").siblings().removeClass(
										"active");
						if (index == 1) {
							// 刷新保养人员表
							$("#showMaintenanceStaffTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
						} else if (index == 2) {
							// 刷新保养项目表
							$("#showMaintenanceProjectTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceItem/queryMaintenanceItemById.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
						} else if (index == 3) {
							// 刷新备件信息表
							$("#showMaintenanceSparepartRecordTable")
									.bootstrapTable(
											"refresh",
											{
												url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
						}
					});
});

function addRecord(parameter) {
	if (parameter == "staff") {// 显示新增维护人员
		$("#StaffName").empty();
		$("#StaffName").selectpicker('refresh');
		SetStaffName();
		$("#StaffCode").val("");
		var date = new Date();
		var value = date.getFullYear()
				+ "-"
				+ ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
						+ (date.getMonth() + 1))
				+ "-"
				+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
				+ " "
				+ (date.getHours() > 9 ? date.getHours() : "0"
						+ date.getHours())
				+ ":"
				+ (date.getMinutes() > 9 ? date.getMinutes() : "0"
						+ date.getMinutes())
				+ ":"
				+ (date.getSeconds() > 9 ? date.getSeconds() : "0"
						+ date.getSeconds());
		$("#StaffcreateDate").val(value);

		/* $('#updateDeviceRepairOrderDialog').modal('hide'); */
		$('#addMaintenanceStaff').modal('show');

	} else if (parameter == "ngRecord") {// 显示新增故障原因
		$("#NGMaintainName").empty();
		$("#NGMaintainName").selectpicker('refresh');
		$("#deviceproject").empty();
		$("#deviceproject").selectpicker('refresh');
		SetNGMaintainName();
		Setdeviceproject();
		$("#NGMaintainCode").val("");
		$("#NGMaintainNote").val("");

		/* $('#updateDeviceRepairOrderDialog').modal('hide'); */
		$('#addNGMaintain').modal('show');

	} else if (parameter == "maintainProject") {// 显示新增维护项目
		$("#MaintainProjectName").empty();
		$("#MaintainProjectTypeName").empty();
		SetMaintainProjectType();
		$("#MaintainProjectName").selectpicker('refresh');
		$("#MaintainProjectTypeName").selectpicker('refresh');
		$("#MaintainProjectTypeCode").val("");
		$("#MaintainProjectCode").val("");
		$("#MaintainProjectNote").val("");
		$("#MaintainProjectProcessingMethod").val("");
		$("#MaintainProjectRemark").val("");

		/* $('#updateDeviceRepairOrderDialog').modal('hide'); */
		$('#addMaintainProject').modal('show');

	} else if (parameter == "sparepartRecord") {// 显示新增备件
		$("#SparepartName").empty();
		SetSparepartName();
		/* $('#updateDeviceRepairOrderDialog').modal('hide'); */
		var date = new Date();
		var value = date.getFullYear()
				+ "-"
				+ ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
						+ (date.getMonth() + 1))
				+ "-"
				+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
				+ " "
				+ (date.getHours() > 9 ? date.getHours() : "0"
						+ date.getHours())
				+ ":"
				+ (date.getMinutes() > 9 ? date.getMinutes() : "0"
						+ date.getMinutes())
				+ ":"
				+ (date.getSeconds() > 9 ? date.getSeconds() : "0"
						+ date.getSeconds());
		$("#SparepartcreateDate").val(value);
		$("#SparepartCode").val("");
		$("#SparepartId").val("");
		$("#SparepartNum").val("1");
		$("#SparepartNote").val("");
		$("#SparepartName").selectpicker('refresh');
		$('#addSparepart').modal('show');

	} else if (parameter == "maintenanceStaff") {// 保养人员
		$("#maintenanceInformantName").empty();
		$("#StaffName").selectpicker('refresh');
		SetStaffName();
		$("#maintenanceInformantCode").val("");
		var date = new Date();
		var value = date.getFullYear()
				+ "-"
				+ ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
						+ (date.getMonth() + 1))
				+ "-"
				+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
				+ " "
				+ (date.getHours() > 9 ? date.getHours() : "0"
						+ date.getHours())
				+ ":"
				+ (date.getMinutes() > 9 ? date.getMinutes() : "0"
						+ date.getMinutes())
				+ ":"
				+ (date.getSeconds() > 9 ? date.getSeconds() : "0"
						+ date.getSeconds());
		$("#StaffcreateDate").val(value);

		/* $('#maintenanceOrderDialog').modal('hide'); */
		$('#addMaintenance').modal('show');
	} else if (parameter == "maintenanceSparepart") {// 保养备件
		// 刷新备件信息表
		$("#MaintenanceSparepartTab").bootstrapTable("refresh", {
			url : "paperLessSparepart/queryOtherSpareparts.do",
			cache : false,
			query : {
				maintenancePlanRecordId : ordId
			}
		});
		$('#addMaintenanceSparepart').modal('show');
	} else {
		return "";
	}
}

function reloadSparepart() {
	// 刷新备件信息表
	$("#MaintenanceSparepartTab").bootstrapTable("refresh", {
		url : "paperLessSparepart/queryOtherSpareparts.do",
		cache : false,
		query : {
			maintenancePlanRecordId : ordId,
			sparepartCode : $("#sparepartCode").val(),
			sparepartName : $("#sparepartName").val(),
			sparepartUnitType : $("#sparepartUnitType").val()
		}
	});
}
// 删除记录
function removeRecord(parameter) {
	if (parameter == "staff") {// 删除维护人员记录

		var alldata = $("#showStaffTable").bootstrapTable('getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}

		if (window.confirm('你确定要删除吗？')) {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcMaintenanceStaffRecord/deleteMaintenanceStaffRecord.do",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新记录表
							$("#showStaffTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceStaffRecord/queryMaintenanceStaffRecord.do",
												cache : false,
												query : {
													deviceRepairOrderId : ordId
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			return false;
		}

	} else if (parameter == "ngRecord") {// 删除故障原因记录
		var alldata = $("#showNGRecordTable").bootstrapTable('getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (window.confirm('你确定要删除吗？')) {
			$.ajax({
				type : "post",
				url : contextPath
						+ "mcNGMaintainRecord/deleteNGMaintainRecord.do",
				data : {
					"id" : alldata[0].id
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					// 刷新记录表
					$("#showNGRecordTable").bootstrapTable("refresh", {
						url : "NGMaintainRecord/queryNGMaintainRecordVO.do",
						cache : false,
						query : {
							deviceRepairOrderId : ordId
						}
					});
					alert(data.message);
				}
			})
		} else {
			return false;
		}

	} else if (parameter == "maintainProject") {// 删除维护项目记录
		var alldata = $("#showMaintainProjectTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}

		if (window.confirm('你确定要删除吗？')) {
			$.ajax({
				type : "post",
				url : contextPath
						+ "mcMaintainProject/deleteMaintainProjectById.do",
				data : {
					"id" : alldata[0].id
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					// 刷新记录表
					$("#showMaintainProjectTable").bootstrapTable("refresh", {
						url : "mcMaintainProject/queryMaintainProject.do",
						cache : false,
						query : {
							deviceRepairOrderId : ordId
						}
					});
					$("#alertText").text(data.message);
					$("#alertDialog").modal();
				}
			})
		} else {
			return false;
		}

	} else if (parameter == "sparepartRecord") {// 删除备件记录
		var alldata = $("#showSparepartRecordTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (window.confirm('你确定要删除吗？')) {
			$.ajax({
				type : "post",
				url : contextPath
						+ "mcSparepartRecord/deleteSparepartRecord.do",
				data : {
					"id" : alldata[0].id
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					// 刷新记录表
					$("#showSparepartRecordTable").bootstrapTable("refresh", {
						url : "mcSparepartRecord/querySparepartRecord.do",
						cache : false,
						query : {
							deviceRepairOrderId : ordId
						}
					});
					$("#alertText").text(data.message);
					$("#alertDialog").modal();
				}
			})
		} else {
			return false;
		}

	} else if (parameter == "maintenanceStaff") {// 删除保养人员记录
		var alldata = $("#showMaintenanceStaffTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (window.confirm('你确定要删除吗？')) {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcMaintenanceUser/deleteMaintenanceUser.do",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新记录表
							$("#showMaintenanceStaffTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			return false;
		}

	} else if (parameter == "maintenanceProject") {// 删除保养备件记录
		var alldata = $("#showMaintenanceProjectTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (window.confirm('你确定要删除吗？')) {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcMaintenanceItem/deleteMaintenanceItem.do",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新记录表
							$("#showMaintenanceProjectTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceItem/queryMaintenanceItemById.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			return false;
		}

	} else if (parameter == "maintenanceSparepart") {// 删除保养备件记录
		var alldata = $("#showMaintenanceSparepartRecordTable").bootstrapTable(
				'getSelections');
		var sum = alldata.length;
		param = "";
		if (sum < 1) {// id==''
			$("#alertText").text("请选择数据");
			$("#alertDialog").modal();
			return false;
		}
		if (window.confirm('你确定要删除吗？')) {
			$
					.ajax({
						type : "post",
						url : contextPath
								+ "mcMaintenanceSparepart/deleteMaintenanceSparepart.do",
						data : {
							"id" : alldata[0].id
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							// 刷新记录表
							$("#showMaintenanceSparepartRecordTable")
									.bootstrapTable(
											"refresh",
											{
												url : "mcMaintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
												cache : false,
												query : {
													recordId : ordId
												}
											});
							$("#alertText").text(data.message);
							$("#alertDialog").modal();
						}
					})
		} else {
			return false;
		}

	} else {
		return "";
	}
}

// 员工下拉框
function SetStaffName() {
	$
			.ajax({
				type : "post",
				url : contextPath + "employee/queryAllEmployees.do",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.code + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#StaffName").append(htmlselect);
					$("#StaffName").selectpicker('refresh');
					$("#maintenanceInformantName").append(htmlselect);
					$("#maintenanceInformantName").selectpicker('refresh');
				}
			})
}
// 故障原因下拉框
function SetNGMaintainName() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "projectType/queryProjectTypesByType.do?type=breakdownReasonType",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.id + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.id
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#NGMaintainName").append(htmlselect);
					$("#NGMaintainName").selectpicker('refresh');
				}
			})
}
function Setdeviceproject() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "deviceProject/queryAllDeviceProjectByType.do?type=BREAKDOWNREASON",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.id + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.id
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#deviceproject").append(htmlselect);
					$("#deviceproject").selectpicker('refresh');
				}
			})
}
// 维修项目下拉框
function SetMaintainProject() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "deviceProject/queryAllDeviceProjectByType.do?type=MAINTENANCEITEM",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.id + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.id
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#MaintainProjectName").append(htmlselect);
					$("#MaintainProjectName").selectpicker('refresh');
				}
			})
}
// 维修项目类别下拉框
function SetMaintainProjectType() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "projectType/queryTopOneProjectTypes.do?rootType=ROOTMAINTENANCEITEMTYPE",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.id + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.id
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#MaintainProjectTypeName").append(htmlselect);
					$("#MaintainProjectTypeName").selectpicker('refresh');
				}
			})
}
// 维修项目下拉框
function SetSparepartName() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "sparepart/querySparepartsByDeviceRepairId.do",
				data : {
					DeviceRepairId : ordId
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.code + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
									})
					$("#SparepartName").append(htmlselect);
					$("#SparepartName").selectpicker('refresh');
				}
			})
}
// 保养备件下拉框
function SetMaintenanceSparepartName() {
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "sparepart/querySparepartsByMaintenanceId.do",
				data : {
					MaintenanceId : ordId
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.code + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
									})

					$("#MaintenanceSparepartName").append(htmlselect);
					$("#MaintenanceSparepartName").selectpicker('refresh');
				}
			})
}
// 新增维修人员
function addMaintenanceStaff() {
	if (!$('#StaffCode').val()) {
		$("#alertText").text("请选择维修人员！");
		$("#alertDialog").modal();
		return;
	}
	$.ajax({
		type : "post",
		url : contextPath
				+ "mcMaintenanceStaffRecord/addMaintenanceStaffRecord.do",
		data : {
			"deviceRepair.id" : ordId,
			name : $("#StaffName option:selected").text(),
			code : $('#StaffCode').val(),
			assignTime : $('#StaffcreateDate').val(),
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			$("#alertText").text(data.msg);
			$("#alertDialog").modal();
			$('#addMaintenanceStaff').modal('hide');
			$('#updateDeviceRepairOrderDialog').modal('show');
			$("#showStaffTable").bootstrapTable("refresh", {
				url : "maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
				cache : false,
				query : {
					deviceRepairOrderId : ordId
				}
			});
		}
	})
}
// 新增保养人员
function addMaintenance() {
	if (!$('#maintenanceInformantCode').val()) {
		$("#alertText").text("请选择保养人员！");
		$("#alertDialog").modal();
		return;
	}
	$.ajax({
		type : "post",
		url : contextPath + "mcMaintenanceUser/addMaintenanceUser.do",
		data : {
			"maintenancePlanRecord.id" : ordId,
			name : $("#maintenanceInformantName option:selected").text(),
			code : $('#maintenanceInformantCode').val(),
			orderType : $("#orderType option:selected").text(),
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			if (!data.success) {
				$("#alertText").text("添加失败");
				$("#alertDialog").modal();
				return;
			}
			$("#alertText").text("添加成功");
			$("#alertDialog").modal();
			$('#addMaintenance').modal('hide');
			$('#maintenanceOrderDialog').modal('show');
			$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
				url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
				cache : false,
				query : {
					recordId : ordId
				}
			});
		}
	})
}
// 新增故障原因
function addNGMaintain() {
	if (!$('#NGMaintainId').val()) {
		alert("请选择故障原因！");
		return;
	}
	$.ajax({
		type : "post",
		url : contextPath + "mcNGMaintainRecord/addMaintainProject.do",
		data : {
			"deviceRepair.id" : ordId,
			"deviceProject.id" : $('#NGMaintainId').val(),
			note : $('#NGMaintainNote').val(),
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			alert(data.msg);
			$('#addNGMaintain').modal('hide');
			$('#updateDeviceRepairOrderDialog').modal('show');
			$("#showNGRecordTable").bootstrapTable("refresh", {
				url : "ngMaintainRecord/queryNGMaintainRecordVO.do",
				cache : false,
				query : {
					deviceRepairOrderId : ordId
				}
			});
		}
	})
}

// 新增维修项目
function addMaintainProject() {
	if (!$('#MaintainProjectTypeId').val()) {
		$("#alertText").text("请选择维修项目！");
		$("#alertDialog").modal();
		return;
	}
	$.ajax({
		type : "post",
		url : contextPath + "mcMaintainProject/addMaintainProject.do",
		data : {
			"deviceRepair.id" : ordId,
			code : $('#MaintainProjectCode').val(),
			name : $("#MaintainProjectName option:selected").text(),
			"type.id" : $('#MaintainProjectTypeId').val(),
			note : $('#MaintainProjectNote').val(),
			processingMethod : $('#MaintainProjectProcessingMethod').val(),
			remark : $('#MaintainProjectRemark').val(),
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			$("#alertText").text(data.msg);
			$("#alertDialog").modal();
			$('#addMaintainProject').modal('hide');
			$('#updateDeviceRepairOrderDialog').modal('show');
			$("#showMaintainProjectTable").bootstrapTable("refresh", {
				url : "maintainProject/queryMaintainProject.do",
				cache : false,
				query : {
					deviceRepairOrderId : ordId
				}
			});
		}
	})
}
// 新增备件信息
function addSparepart() {
	if (!$('#SparepartId').val()) {
		$("#alertText").text("请选择备件！");
		$("#alertDialog").modal();
		return;
	}
	$.ajax({
		type : "post",
		url : contextPath + "mcSparepartRecord/addSparepartRecord.do",
		data : {
			"deviceRepair.id" : ordId,
			"sparepart.id" : $('#SparepartId').val(),
			quantity : $('#SparepartNum').val(),
			note : $('#SparepartNote').val(),
			createDate : $('#SparepartcreateDate').val(),
		},
		cache : false,
		dataType : "json",
		success : function(data) {
			$("#alertText").text(data.msg);
			$("#alertDialog").modal();
			$('#addSparepart').modal('hide');
			$('#updateDeviceRepairOrderDialog').modal('show');
			$("#showSparepartRecordTable").bootstrapTable("refresh", {
				url : "sparepartRecord/querySparepartRecord.do",
				cache : false,
				query : {
					deviceRepairOrderId : ordId
				}
			});
		}
	})
}
// 保养备件新增
function addMaintenanceSparepart() {
	var ids = $('#MaintenanceSparepartTab').bootstrapTable('getSelections');
	if (!ids) {
		alert("请选择备件！");
		return;
	}
	var arrids = new Array();
	for (var i = 0; i < ids.length; i++) {
		arrids.push(ids[i].id)
	}
	$
			.ajax({
				type : "post",
				url : contextPath
						+ "maintenanceSparepart/addMaintenanceSpareparts.do",
				data : {
					maintenancePlanRecordId : ordId,
					sparepartIds : arrids.join(','),
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					if (!data.success) {
						$("#alertText").text("添加失败");
						$("#alertDialog").modal();
						return;
					}
					$("#alertText").text("添加成功");
					$("#alertDialog").modal();
					$('#addMaintenanceSparepart').modal('hide');
					$('#maintenanceOrderDialog').modal('show');
					$("#showMaintenanceSparepartRecordTable")
							.bootstrapTable(
									"refresh",
									{
										url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
										cache : false,
										query : {
											recordId : ordId
										}
									});
				}
			})
}

// 修改报修单子表格返回事件(hide)
function RepairOrderHide() {
	$('#addMaintenanceStaff').modal('hide');
	$('#addNGMaintain').modal('hide');
	$('#addMaintainProject').modal('hide');
	$('#addSparepart').modal('hide');
	$('#updateDeviceRepairOrderDialog').modal('show');
}
// 修改报修单子表格返回事件(hide)
function maintenanceOrderHide() {
	$('#addMaintenance').modal('hide');
	$('#addNGMaintain').modal('hide');
	$('#addMaintainProject').modal('hide');
	$('#addSparepart').modal('hide');
	$('#addMaintenanceSparepart').modal('hide');
	$('#maintenanceOrderDialog').modal('show');
}

// 执行保养计划
function updateDeviceMaintenance() {
	var status = $("#maintenanceStatus").val();
	if (status != "保养中") {
		$("#alertText").text("该记录不是可完成记录!");
		$("#alertDialog").modal();
		return false;
	}
	// 发送异步请求
	$
			.get(
					"paperlessMaintenancePlan/updateMaintenancePlanRecord.do",
					{
						id : $("#maintenanceId").val(),
						maintenancedDate : $("#maintenanceCreateDate").val(),
						confirmName : $("#maintenanceConfirmName").val(),
						confirmCode : $("#maintenanceConfirmCode").val(),
					},
					function(result) {
						alert("保养完成");
						$('#maintenanceOrderDialog').modal('hide');
						//刷新
						var $iframe = $("#content");
						var src = $iframe.attr("src");
						$iframe.attr("src","paperless/dm/deviceMaintain.jsp");
						$("#showMaintenancePlanRecordsTable")
								.bootstrapTable(
										"refresh",
										{
											url : "mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
											cache : false,
											query : {
												deviceSiteCode : _deviceSiteCode
											}
										});
					});
}
// 查询保养信息
function setMaintenancePlanRecordsTable(id) {
	ordId = id;
	$.get("paperlessMaintenancePlan/queryMaintenancePlanRecordById.do", {
		id : id
	}, function(result) {
		// 设备保养记录对象
		var device = result.device;
		$("#maintenanceDeviceCode").val(device.code);
		$("#maintenanceDeviceName").val(device.name);
		$("#maintenanceUnitType").val(device.unitType);

		$("#maintenanceId").val(result.id);
		$("#maintenanceDeviceType").val(result.maintenanceType);
		$("#maintenanceConfirmName").val(result.employeeName);
		$("#maintenanceConfirmCode").val(result.employeeCode);
		$("#maintenanceStatus").val(result.status);
		var date;
		var value
		if (!result.maintenancedDate) {
			date = new Date();
			value = date.getFullYear()
			+ "-"
			+ ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1))
			+ "-"
			+ (date.getDate() > 9 ? date.getDate() : "0" + date.getDate())
			+ " "
			+ (date.getHours() > 9 ? date.getHours() : "0"
					+ date.getHours())
			+ ":"
			+ (date.getMinutes() > 9 ? date.getMinutes() : "0"
					+ date.getMinutes())
			+ ":"
			+ (date.getSeconds() > 9 ? date.getSeconds() : "0"
					+ date.getSeconds());
		} else {
			value = result.maintenancedDate;
			
		}
		$("#maintenanceCreateDate").val(value);

		showMaintenanceImages(result);
	});

	// 保养人员信息
	$("#showMaintenanceStaffTable")
			.bootstrapTable(
					{
						url : "mcMintenanceUser/queryMaintenanceUserByRecordId.do",
						cache : false,
						height : 370,
						width : 900,
						// idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, // 隔行换色
						queryParams : function(params) {
							var temp = {
								recordId : id
							};
							return temp;
						},
						/*
						 * responseHandler:function(data){ return data.rows; },
						 */
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								},
								{
									field : 'code',
									align : 'center',
									title : '员工代码',
								},
								{
									field : 'name',
									align : 'center',
									title : '员工名称'
								},
								{
									field : 'orderType',
									align : 'center',
									title : '接单类型',
								},
								{
									field : 'dispatchDate',
									align : 'center',
									title : '派单时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'receiptDate',
									align : 'center',
									title : '接收时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								},
								{
									field : 'completeDate',
									align : 'center',
									title : '完成时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								} ]
					});
	//保养项目列表
	$("#showMaintenanceProjectTable").bootstrapTable({
		url : "mcMaintenanceItem/queryMaintenanceItemById.do",
		cache : false,
		//idField : 'id',
		singleSelect : true,
		clickToSelect : true,
		striped : true, //隔行换色
		queryParams : function(params) {
			var temp = {
				recordId : id
			};
			return temp;
		},
		columns : [ 
		{
			field : 'id',
			title : 'Id'
		}, {
			title : '',
			field : '',
			align : 'center',
			width:10,
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : 'code',
			align : 'center',
			title : '项目代码',
		}, {
			field : 'name',
			align : 'center',
			title : '保养项目',
		}, {
			field : 'standard',
			align : 'center',
			title : '标准'
		}, {
			field : 'method',
			align : 'center',
			title : '方法'
		}, {
			field : 'frequency',
			align : 'center',
			title : '频次'
		}, {
			field : 'maintenanceDate',
			align : 'center',
			title : '结果',
			 formatter : function(value, row, index) {
						if (value) {
							return strHtml = "<select class='ss'><option value='Item 1' selected='selected'>OK</option><option value='Item 2'>NG</option></select>";
						}
						return strHtml =     "<select class='ss'><option value='Item 1' >OK</option><option value='Item 2' selected='selected'>NG</option></select>";
					} ,
			formatter : function(value, row, index) {
				var div = $('<div>');
				var $result = $('<input type="checkbox" name="result" style="zoom:200%;"/>');
				div.append($result);
				$result.attr('id', row.id);
				$result.attr('confirmDate', row.confirmDate);
				if (value) {
					$result.attr('checked', 'checked');
				} else {
					$result.removeAttr('checked');
				}

				if (row.confirmDate) {
					$result.attr('disabled', true);
				}
				return div.html();
			}
		} ],
		onLoadSuccess : function() {
			$('input[type=checkbox]').click(function() {
				var confirmDate = $(this).attr('confirmDate');
				var isChecked = $(this)[0].checked;
				var maintenanceItemId = $(this).attr('id');
				//没有确认，可以随便修改结果
				if (!confirmDate) {
					if (isChecked) {
						$.get('mcMaintenanceItem/setMaintenanceDate.do', {
							id : maintenanceItemId
						}, function() {
							//$('#spotCheck').iDatagrid('reload');
						});
					} else {
						$.get('mcMaintenanceItem/setMaintenanceDate2Null.do', {
							id : maintenanceItemId
						}, function() {
							//$('#spotCheck').iDatagrid('reload');
						});
					}
					return;
				}
			});
		}
	});
	
	//备件列表
	$("#showMaintenanceSparepartRecordTable")
			.bootstrapTable(
					{
						url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
						cache : false,
						height : 370,
						//idField : 'id',
						singleSelect : true,
						clickToSelect : true,
						striped : true, //隔行换色
						queryParams : function(params) {
							var temp = {
								recordId : id
							};
							return temp;
						},
						/* responseHandler:function(data){
									return data.rows;
								}, */
						columns : [
								{
									checkbox : true
								},
								{
									field : 'id',
									title : 'Id'
								}/* ,{
													field : 'typeCode',
													align : 'center',
													title : '类别代码',
													formatter : function(value, row, index) {
														if (row.sparepart) {
															if(row.sparepart.projectType){
																return row.sparepart.projectType.code;
															}
														}
														return "";
													}
												} */,
								{
									field : 'typeName',
									align : 'center',
									title : '备件类别',
									formatter : function(value, row, index) {
										if (row.projectType) {
											return row.projectType.name;
										}
										return "";
									}
								},
								{
									field : 'code',
									align : 'center',
									title : '备件代码',
								},
								{
									field : 'name',
									align : 'center',
									title : '备件名称',
								},
								{
									field : 'unitType',
									align : 'center',
									title : '规格型号',
								},
								{
									field : 'batchNumber',
									align : 'center',
									title : '批号',
								},
								{
									field : 'graphNumber',
									align : 'center',
									title : '图号',
								},
								{
									field : 'measurementUnit',
									align : 'center',
									title : '计量单位',
								},
								{
									field : 'count',
									align : 'center',
									title : '数量'
								},
								{
									field : 'note',
									align : 'center',
									title : '备注'
								},
								{
									field : 'useDate',
									align : 'center',
									title : '耗用时间',
									formatter : function(value, row, index) {
										if (value) {
											var date = new Date(value);
											return date.getFullYear()
													+ '-'
													+ ((date.getMonth() + 1) > 9 ? (date
															.getMonth() + 1)
															: ("0" + (date
																	.getMonth() + 1)))
													+ '-'
													+ (date.getDate() > 9 ? date
															.getDate()
															: ("0" + date
																	.getDate()))
													+ " "
													+ (date.getHours() > 9 ? date
															.getHours()
															: ("0" + date
																	.getHours()))
													+ ":"
													+ (date.getMinutes() > 9 ? date
															.getMinutes()
															: ("0" + date
																	.getMinutes()))
													+ ":"
													+ (date.getSeconds() > 9 ? date
															.getSeconds()
															: ("0" + date
																	.getSeconds()));
										}
										return "";
									}
								} ]
					});

	//未添加备件列表
	$("#MaintenanceSparepartTab").bootstrapTable({
		url : "paperLessSparepart/queryOtherSpareparts.do",
		cache : false,
		height : 370,
		//idField : 'id',
		singleSelect : false,
		clickToSelect : true,
		striped : true, //隔行换色
		onClickRow : function(row, $element) {
			/*$('.success').removeClass('success');
			$($element).addClass('success');*/
		},
		queryParams : function(params) {
			var temp = {
				maintenancePlanRecordId : id
			};
			return temp;
		},
		/* responseHandler:function(data){
					return data.rows;
				}, */
		columns : [ {
			checkbox : true
		}, {
			field : 'id',
			title : 'Id'
		}, {
			field : 'code',
			align : 'center',
			title : '备件代码',
		}, {
			field : 'name',
			align : 'center',
			title : '备件名称',
		}, {
			field : 'unitType',
			align : 'center',
			title : '规格型号',
		}, {
			field : 'measurementUnit',
			align : 'center',
			title : '计量单位',
		}, {
			field : 'note',
			align : 'center',
			title : '备注',
			width : 80
		} ]
	});

	$('#MaintenanceSparepartTab').bootstrapTable('hideColumn', 'id');
	/*$("#MaintenanceSparepartTab").on('click-row.bs.table',
			function(e, row, element) { 
		id = row.id;
		$('.success').removeClass('success'); //去除之前选中的行的，选中样式
		$(element).addClass('success'); //添加当前选中的 success样式用于区别
	});*/
	$('#showMaintenanceStaffTable').bootstrapTable('hideColumn', 'id');
	$("#showMaintenanceStaffTable").on('click-row.bs.table',
			function(e, row, element) { //splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); //去除之前选中的行的，选中样式
				$(element).addClass('success'); //添加当前选中的 success样式用于区别
			});
	$('#showMaintenanceProjectTable').bootstrapTable('hideColumn', 'id');
	$("#showMaintenanceProjectTable").on('click-row.bs.table',
			function(e, row, element) { //splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); //去除之前选中的行的，选中样式
				$(element).addClass('success'); //添加当前选中的 success样式用于区别
			});
	$('#showMaintenanceSparepartRecordTable')
			.bootstrapTable('hideColumn', 'id');
	$("#showMaintenanceSparepartRecordTable").on('click-row.bs.table',
			function(e, row, element) { //splitLostTime confirmLostTime
				id = row.id;
				$('.success').removeClass('success'); //去除之前选中的行的，选中样式
				$(element).addClass('success'); //添加当前选中的 success样式用于区别
			});
}

//显示设备分布图
function showMaintenanceImages(row) {
	$.get("mcMaintenancePlanRecord/queryMaintenanceImages.do", {
		deviceId : row.device.id,
		docTypeCode : row.maintenanceTypeCode
	}, function(result) {
		fillTab0(result);
	});
}
//填充tab0，tab0为显示设备分布图的div
function fillTab0(raletedDocuments) {
	var tab0 = $("#MaintenancePicDiv");
	tab0.empty();
	if (raletedDocuments && raletedDocuments.length > 0) {
		for (var i = 0; i < raletedDocuments.length; i++) {
			var $img = $("<img style='height:200px;width:200px;margin:5px;'>");
			$img.attr("src", raletedDocuments[i].url);
			$img.attr("alt", raletedDocuments[i].name);
			tab0.append($img);
		}
	}
}
//确认弹出框
function showConfirm(p) {
	if (p == "repairConfirm") {
		$('#showRepairConfirm').modal('show');
	} else if (p == "MaintenanceConfirm") {
		$('#showMaintenanceConfirm').modal('show');
	} else if (p == "repairComplete") {
		$('#updateDeviceRepairOrderDialog').modal('hide');
		$('#showRepairComplete').modal('show');
	} else if (p == "repairUpdate") {
		$('#updateDeviceRepairOrderDialog').modal('show');
	}
}
//损时类型下拉框
function setlostTimeType() {
	$
			.ajax({
				type : "post",
				url : contextPath + "pressLightType/queryFirstLevelType.do?type=PRESSLIGHTTYPE",
				data : {},
				cache : false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$
							.each(
									data,
									function(index, Type) {
										htmlselect += "<option value='"
												+ Type.code + "'>" + Type.name
												+ "</option>";
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
										var splitButton = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='splitShowDetail(\""
												+ Type.code
												+ "\")' value='"
												+ Type.code
												+ "'>"
												+ Type.name
												+ "</button>");
										$("#lostType").append(button);
									})
				}
			})
}
//点击类型事件
function showDetail(typeCode) {
	$("#lostType button").removeClass("btn btn-primary");
	$("#lostType button").addClass("btn btn-default");
	var btns = $("#lostType button");
	for (var i = 0; i < btns.length; i++) {
		var btn = $(btns[i]);
		var textOnButton = btn.val();
		if (textOnButton === typeCode) {
			btn.removeClass("btn btn-default");
			btn.addClass("btn btn-primary");
		}
	}
	$
			.ajax({
				type : "post",
				url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
				data : {
					"pcode" : typeCode
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					$("#lostReason").empty();
					$
							.each(
									data,
									function(index, Type) {
										var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;height:45px' onclick='showReasonBut(\""
												+ Type.id
												+ "\")' value ='"
												+ Type.id
												+ "'>"
												+ Type.reason
												+ "</button>");
										$("#lostReason").append(button);
									})
				}
			})
}
function showReasonBut(typeid) {
	lostReasonid = "";
	$("#lostReason button").removeClass("btn btn-primary");
	$("#lostReason button").addClass("btn btn-default");
	var btns = $("#lostReason button");
	for (var i = 0; i < btns.length; i++) {
		var btn = $(btns[i]);
		var textOnButton = btn.val();
		if (textOnButton === typeid) {
			btn.removeClass("btn btn-default");
			btn.addClass("btn btn-primary");
			//$("#lostTimeType").selectpicker('val',data.pressLightType.code);
			$("#pressLight").selectpicker('val', typeid);
			$("#pressLightId").val(typeid);
		}
	}
}

//故障弹出框
function showng() {
	$("#lostReason").empty();
	$("#lostType button").removeClass("btn btn-primary");
	$("#lostType button").addClass("btn btn-default");
	$('#showNgdialog').modal('show');
}
//确认
function Confirm() {
	$('#showNgdialog').modal('show');
}
//取消
function Cancel() {
	$('#showNgdialog').modal('hide');
}
//显示保养界面
function showM(id, flag) {
	_deviceSiteCode = id;
	if (flag == "保养") {
		$("#addMaintenanceSparepartView").show();
		$("#addStaffMaintenanceView").show();
		$("#removeMaintenanceSparepartView").show();
		$("#removeStaffMaintenanceView").show();
		document.getElementById("updateDeviceMaintenance").style.display = ""; 
	} else if (flag == "查看") {
		$("#addMaintenanceSparepartView").hide();
		$("#addStaffMaintenanceView").hide();
		$("#removeMaintenanceSparepartView").hide();
		$("#removeStaffMaintenanceView").hide();
		document.getElementById("updateDeviceMaintenance").style.display = "none"; 
	}
	setMaintenancePlanRecordsTable(id);

	$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
		url : "mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
		cache : false,
		query : {
			recordId : id
		}
	});
	//刷新保养项目表
	$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
		url : "mcMaintenanceItem/queryMaintenanceItemById.do",
		cache : false,
		query : {
			recordId : id
		}
	});
	//刷新备件信息表
	$("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
		url : "maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
		cache : false,
		query : {
			recordId : id
		}
	});

	$('#maintenanceOrderDialog').modal('show');
}
//显示维修界面
function showS(id, clickStatus, code) {
	_deviceSiteCode = id;
	initLostTimeTable();
	if (clickStatus == "维修") {
		$("#addMaintenanceStaffView").show();
		$("#removeMaintenanceStaffView").show();
		$("#removeNGMaintainView").show();
		$("#addNGMaintainView").show();
		$("#removeMaintainProjectView").show();
		$("#addMaintainProjectView").show();
		$("#removeSparepartView").show();
		$("#addSparepartView").show();
		document.getElementById("repairComplete").style.display = ""; 
		document.getElementById("updateDeviceRepair").style.display = ""; 
		showUpdateDeviceRepairOrderDialogDialog(id);
	} else if (clickStatus == "接单") {
		$.get("paperlessDeviceRepair/updateDeviceRepairOrderStatusById.do", {
			id : id,
			status : "MAINTAINING"
		}, function(result) {
			if (result.statusCode == '200') {
				parent.$('#content').attr('src',
						'paperless/dm/deviceService.jsp');
				$.iMessager.alert(result.title, result.message);
			} else {
				$.iMessager.alert("警告", "操作失败!");
			}
		});
	} else if (clickStatus == "查看") {
		$("#addMaintenanceStaffView").hide();
		$("#removeMaintenanceStaffView").hide();
		$("#removeNGMaintainView").hide();
		$("#addNGMaintainView").hide();
		$("#removeMaintainProjectView").hide();
		$("#addMaintainProjectView").hide();
		$("#removeSparepartView").hide();
		$("#addSparepartView").hide();
		document.getElementById("repairComplete").style.display = "none"; 
		document.getElementById("updateDeviceRepair").style.display = "none"; 
		showUpdateDeviceRepairOrderDialogDialog(id);
	} else if (clickStatus == "派单") {
		alert("派单");
	} else if (clickStatus == "确认") {
		$.get("paperlessDeviceRepair/updateDeviceRepairOrderStatusById.do", {
			id : id,
			status : "MAINTAINCOMPLETE"
		}, function(result) {
			if (result.statusCode == '200') {
				parent.$('#content').attr('src',
						'paperless/dm/deviceService.jsp');
				$.iMessager.alert(result.title, result.message);
			} else {
				$.iMessager.alert("警告", "操作失败!");
			}
		});
	}

}