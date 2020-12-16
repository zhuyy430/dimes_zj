<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script type="text/javascript">
	var tab="untreated";//tab代码(dataid属性值)
	var rids=""; //记录id的字符串
	var id ="";  //table选中记录ID
	var param=""; //判断是修改提交，还是确认验证
	var Devid;   //站点ID
	var pressLightCode; //故障原因CODE
	var endVal="";  //拆分选中数据结束时间
	var startVal=""; //拆分选中数据开始时间
	var sum = 0;  //损时总时长
	var count = 0;//损时数量
	var date = new Date();
	var _deviceSiteCode = ""; //选择的设备编码
	var ordId = ""; //选择数据Id
	var imageVal = 0; //选择数据Id
 	var idList="";    //存储图片路径
 	var imageList=[];
 	
 	 /*删除功能*/
 	function delImg(obj) {
	        var _this = $(obj);
	        var id=_this.siblings(".input").val();
	        var photoIds=idList.split(",");
	       	photoIds.splice(id,1,"");
	        imageList.splice(id,1,id)
	        var plist="";
	        for(var i = 0; i < photoIds.length; i++){
	        		if(i==0){
	        			plist=photoIds[i];
            	}else{
            		plist+=","+photoIds[i];
            	}
	        }
	       idList=plist;
			$("#photoIds").val(photoIds)
	        _this.parents(".imageDiv").remove();
			$(".addImages").show();
	    };
 	
	var dateForm = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+" " + date.getHours() + ":"+ date.getMinutes() + ":" + date.getSeconds();
 	$(function(){
 		$("#photo").css("border","1px dotted #00000E");
 		$("#updatePhoto").css("border","1px solid  #00000E");
 		$("#maintenancePhoto").css("border","1px solid  #00000E");
 		_deviceSiteCode = "<%=request.getParameter("deviceSiteCode")%>";
 		initLostTimeTable(_deviceSiteCode);
 		$("#allDeviceSites").text(_deviceSiteCode);
 		var userAgent = navigator.userAgent; //用于判断浏览器类型
 		idList=$("#photoIds").val();
 		setlostTimeType();
 		$(".file").change(function() {
 			//console.log($("#fileInput")[0].files);
 		    //获取选择图片的对象
 		    var docObj = $(this)[0];
 		    var picDiv = $(this).parents(".picDiv");
 		    //得到所有的图片文件
 		    var fileList = docObj.files;
 		    //循环遍历
 		    for (var i = 0; i < fileList.length; i++) {
	 		    //idList.split(",").length
	 		   /*  if(!idList){
	 		    	imageVal=0;
	 		    }else{
	 		    	imageVal=idList.split(",").length;
	 		    } */
				for(var j = 0; j < imageList.length; j++){
					if(imageList[j]!==""){
						imageVal=imageList[j]
						break;
					}
				}
	 		    //动态添加html元素onclick='deleteImg(\""+ imageVal+ "\")
 		        var picHtml = "<div class='imageDiv' style='margin-left: 30px'> <img  id='img" + fileList[i].name + "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="+imageVal+" /></div></div>";
 		        
 		        imageList.splice(imageVal,1,"");
 		        picDiv.prepend(picHtml);
 		        //获取图片imgi的对象
 		        var imgObjPreview = document.getElementById("img" + fileList[i].name);
 		        
 		        var formData = new FormData();
 		        formData.append('file',fileList[i]);
 		        
 		        var url ="relatedDoc/uploadimg.do";            
 		        $.ajax({
 		            type:'POST',
 		            url:url,
 		            data:formData,
 		            contentType:false,
 		            processData:false,
 		            dataType:'json',
 		            mimeType:"multipart/form-data",
 		            success:function(data){
 		            	if(!idList){
 		            		idList=data.filePath;
 		            		$("#photoIds").val(idList)
 		            	}else{
 		            		var photoIds=idList.split(",");
 		    		       	photoIds.splice(imageVal,1,data.filePath);
 		            		idList=photoIds.join(',');
 		            		$("#photoIds").val(idList)
 		            	}
 		            }
 		        });
 		        if (fileList && fileList[i]) {
 		            //图片属性
 		            imgObjPreview.style.display = 'block';
 		            imgObjPreview.style.width = '180px';
 		            imgObjPreview.style.height = '180px';
 		            //imgObjPreview.src = docObj.files[0].getAsDataURL();
 		            //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要以下方式
 		            if (userAgent.indexOf('MSIE') == -1) {
 		                //IE以外浏览器
 		                imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]); //获取上传图片文件的物理路径;
 		                //console.log(imgObjPreview.src);
 		                //var msgHtml = '<input type="file" id="fileInput" multiple/>';
 		            } else {
 		                //IE浏览器
 		                if (docObj.value.indexOf(",") != -1) {
 		                    var srcArr = docObj.value.split(",");
 		                    imgObjPreview.src = srcArr[i];
 		                } else {
 		                    imgObjPreview.src = docObj.value;
 		                }
 		            }
 		        }
 		       for(var j = 0; j < imageList.length; j++){
					if(imageList[j]!==""){
		 		        	$(".addImages").show();
						break;
					}
		 		    $(".addImages").hide();
				}
 		    }
 		})
 		
 		
 		$("#deviceSiteCode").val("<%=request.getParameter("deviceSiteCode")%>");
 		$("#lostTimeDeviceSiteCode").text("<%=request.getParameter("deviceSiteCode")%>");
 		$('#startTime').val(new Date());
 		$('#endTime').val(new Date());
 		$("#alertDialog").css("z-index","10001")
 		
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
		//报修单时间初始化
		$("#createDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		});
		//报修单时间初始化
		$("#StaffcreateDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		});
		//报修单时间初始化
		$("#SparepartcreateDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		});
		//点检时间初始化
		$("#checkCreateDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		});
		//保养时间初始化
		$("#maintenanceCreateDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		});
 		$("#endTime").datetimebox({
 			required: true,
 			    parser: function(date){ return new Date(Date.parse(date.replace(/-/g,"/")));}
		});
 		$("#endTime").datetimebox('setValue', dateForm);



        //左右按钮点击事件
        $("#showPrevious").click(function(){
            if(index<=0){
               alert("已是第一张!");
            }else{
                var $previewDiv = $("#deviceCheckImg");
                var url="";
                if(docs!=null && docs.length>0){
                    //默认显示第0张图片
                    url=docs[--index].url;
                }
                $previewDiv.empty();
                var suffix = '';

                //截取后缀
                if(url!=null && $.trim(url)!=''){
                    var point = url.lastIndexOf(".");
                    suffix = url.substr(point+1);
                }else{
                    return false;
                }
                var $docIframe=$("<iframe style='width:100%;height:100%;'>");
                switch(suffix){
                    case "pdf":
                    case "PDF":{
                        $docIframe.attr("src",url);
                        $previewDiv.append($docIframe);
                        break;
                    }
                    <%-- case "doc":
                    case "docx":
                    case "DOC":
                    case "DOCX":
                    case "xls":
                    case "xlsx":
                    case "XLS":
                    case "XLSX":
                    case "ppt":
                    case "pptx":
                    case "PPT":
                    case "PPTX":{
                        $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                        $previewDiv.append($docIframe);
                        break;
                    } --%>
                    case "png":
                    case "PNG":
                    case "jpg":
                    case "JPG":
                    case "JPEG":
                    case "jpeg":
                    case "gif":
                    case "GIF":
                    case "bmp":
                    case "BMP":{
                        var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                        $img.attr("src",url);
                        $previewDiv.append($img);
                        break;
                    }
                    default:{
                        var $p = $("<p style='font-size:20px;color:red;'>");
                        $p.append(suffix + "不支持预览!");
                        $previewDiv.append($p);
                        return false;
                    }
                }
                //$("#deviceCheckImg").attr("src",_docs[--index].url);
            }

        });
        //左右按钮点击事件
        $("#showNext").click(function(){
            if(index>=docs.length-1){
                alert("已是最后一张!");
                return ;
            }else{
                var $previewDiv = $("#deviceCheckImg");
                var url="";
                if(docs!=null && docs.length>0){
                    //默认显示第0张图片
                    url=docs[++index].url;
                }
                $previewDiv.empty();
                var suffix = '';

                //截取后缀
                if(url!=null && $.trim(url)!=''){
                    var point = url.lastIndexOf(".");
                    suffix = url.substr(point+1);
                }else{
                    return false;
                }
                var $docIframe=$("<iframe style='width:100%;height:100%;'>");
                switch(suffix){
                    case "pdf":
                    case "PDF":{
                        $docIframe.attr("src",url);
                        $previewDiv.append($docIframe);
                        break;
                    }
                    <%-- case "doc":
                    case "docx":
                    case "DOC":
                    case "DOCX":
                    case "xls":
                    case "xlsx":
                    case "XLS":
                    case "XLSX":
                    case "ppt":
                    case "pptx":
                    case "PPT":
                    case "PPTX":{
                        $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                        $previewDiv.append($docIframe);
                        break;
                    } --%>
                    case "png":
                    case "PNG":
                    case "jpg":
                    case "JPG":
                    case "JPEG":
                    case "jpeg":
                    case "gif":
                    case "GIF":
                    case "bmp":
                    case "BMP":{
                        var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                        $img.attr("src",url);
                        $previewDiv.append($img);
                        break;
                    }
                    default:{
                        var $p = $("<p style='font-size:20px;color:red;'>");
                        $p.append(suffix + "不支持预览!");
                        $previewDiv.append($p);
                        return false;
                    }
                }
                //$("#deviceCheckImg").attr("src",_docs[++index].url);
            }
        });
	})
 	 
	
	//点击tab事件
	function lostTimeTabClick(e) {
		var u;
		id="";
		tab = e.getAttribute("dataid");
		if(tab=="SPOTINSPECTION"){  //点检
			$(".CheckingPlanTable").show();
			$(".DeviceRepairOrderTable").hide();
			$(".MaintenancePlanRecordsTable").hide();
			$(".DocTable").hide();
			$("#title").text("近期点检情况");
			$(".mc_device_spotinspection").show();
			$(".mc_device_maintenanceitem").hide();
			$(".mc_device_maintain").hide();
			$(".mc_device_doc").hide();
			$("#showCheckingPlanTable").bootstrapTable("refresh", {
				url :"mcCheckingPlanRecord/queryAllCheckingPlanRecordByDeviceSiteCode.do",
				cache:false,	
				query : {
					deviceSiteCode :_deviceSiteCode
				},
			});
		}else if(tab=="MAINTENANCEITEM"){  //报修、维修
			$(".CheckingPlanTable").hide();
			$(".MaintenancePlanRecordsTable").hide();
			$(".DeviceRepairOrderTable").show();
			$(".DocTable").hide();
			$("#title").text("近期报修/维修记录");
			$(".mc_device_maintenanceitem").show();
			$(".mc_device_spotinspection").hide();
			$(".mc_device_maintain").hide();
			$(".mc_device_doc").hide();
			$("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
				url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
				cache:false,
				query : {
					deviceCode :_deviceSiteCode
				}
			});
		}else if(tab=="MAINTAIN"){  //保养
			$(".CheckingPlanTable").hide();
			$(".MaintenancePlanRecordsTable").show();
			$(".DeviceRepairOrderTable").hide();
			$(".DocTable").hide();
			$("#title").text("近期保养计划状况");
			$(".mc_device_maintain").show();
			$(".mc_device_maintenanceitem").hide();
			$(".mc_device_spotinspection").hide();
			$(".mc_device_doc").hide();
			$("#showMaintenancePlanRecordsTable").bootstrapTable("refresh", {
				url :"mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
				cache:false,
				query : {
					deviceSiteCode :_deviceSiteCode
				}
			});
		}else if(tab=="DOC"){  //文档
			$(".CheckingPlanTable").hide();
			$(".MaintenancePlanRecordsTable").hide();
			$(".DeviceRepairOrderTable").hide();
			$(".DocTable").show();
			$("#title").text("相关文档");
			$(".mc_device_doc").show();
			$(".mc_device_spotinspection").hide();
			$(".mc_device_maintenanceitem").hide();
			$(".mc_device_maintain").hide();
			$("#showDocTable").bootstrapTable("refresh", {
				url :"relatedDoc/queryRelatedDocumentByDeviceId.do",
				cache:false,
				query : {
					deviceCode :_deviceSiteCode
				}
			});
		}
	};
	//tab选中样式
	$(function() {
		$("#myTab li:first").addClass("active");
	});
	
	
	//初始化损时列表table
	var beginData = "";
	var endData = "";
	var sum = 0;
	var count = 0;
	   function initLostTimeTable(deviceCode) {
		id="";
		$("#title").text("近期点检情况");
		$(".mc_device_spotinspection").show();
		$(".DeviceRepairOrderTable").hide();
		$(".MaintenancePlanRecordsTable").hide();
		$(".DocTable").hide();
		var deviceSiteCode = $("#deviceSiteCode").val();
		$("#showCheckingPlanTable").bootstrapTable({
			url :"mcCheckingPlanRecord/queryAllCheckingPlanRecordByDeviceSiteCode.do",
			cache:false,
			height : longTableHeight,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
					deviceSiteCode :deviceCode
				};
				return temp;
			},
			columns : [{ checkbox: true },{
				field : 'id',
				title : 'Id'
			}, 
			{
				field : 'checkingDate',
				align : 'center',
				title : '计划点检日期',
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
				field : 'className',
				align : 'center',
				title : '班次'
			}, {
				field : 'checkedDate',
				align : 'center',
				title : '实际点检时间',
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
				return "";
				}
			}, {
				field : 'status',
				align : 'center',
				title : '点检状态'
			}, {
				field : 'employeeName',
				align : 'center',
				title : '点检人'
			}]
		});
		$("#showDeviceRepairOrderTable").bootstrapTable({
			url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
			cache:false,
			height : longTableHeight,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
					deviceCode :deviceCode
				};
				return temp;
			},
			columns : [{ checkbox: true },{
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
						return date.getFullYear() + '-'
								+ ((date.getMonth() + 1)>9? (date.getMonth() + 1):("0"+(date.getMonth() + 1)))+'-'
								+ (date.getDate()>9? date.getDate():("0"+date.getDate())) + " "
								+ (date.getHours()>9? date.getHours():("0"+date.getHours())) + ":"
								+ (date.getMinutes()>9? date.getMinutes():("0"+date.getMinutes())) + ":"
								+ (date.getSeconds()>9? date.getSeconds():("0"+date.getSeconds()));
					}
				}
			}, {
				field : 'completeDate',
				align : 'center',
				title : '维修完成时间',
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
					return "";
				}
			}, {
				field : 'status',
				align : 'center',
				title : '状态',
				formatter : function(value, row, index) {
					if (value=="WAITINGASSIGN") {
						return "等待派单";
					}else if(value=="WAITINCOMFIRM"){
						return "等待接单确认"; 
					}else if(value=="MAINTAINING"){
						return "维修中";
					}else if(value=="WORKSHOPCOMFIRM"){
						return "车间确认";
					}else if(value=="MAINTAINCOMPLETE"){
						return "维修完成";
					}else if(value=="WAITWORKSHOPCOMFIRM"){
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
			}]
		});
		$("#showMaintenancePlanRecordsTable").bootstrapTable({
			url :"mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
			cache:false,
			height : longTableHeight,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
					deviceSiteCode :deviceCode
				};
				return temp;
			},
			columns : [{ checkbox: true },{
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
			}, {
				field : 'maintenanceDate',
				align : 'center',
				title : '计划日期',
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
					return "";
				}
			}, {
				field : 'maintenancedDate',
				align : 'center',
				title : '保养时间',
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
			},{
				field : 'employeeName',
				align : 'center',
				title : '责任人',
				formatter : function(value, row, index) {
					if (value) {
						return value;
					}
					return "";
				}
			},{
				field : 'confirmName',
				align : 'center',
				title : '确认人',
				formatter : function(value, row, index) {
					if (value) {
						return value;
					}
					return "";
				}
			}]
		});
		$("#showDocTable").bootstrapTable({
			url :"relatedDoc/queryRelatedDocumentByDeviceId.do",
			cache:false,
			height : longTableHeight,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
					deviceCode :deviceCode
				};
				return temp;
			},
			columns : [{ checkbox: true },{
				field : 'id',
				title : 'Id'
			}, 
			{
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
			}]
		});
		//报修单维修人员信息
		$("#showStaffTable").bootstrapTable({
			url :"maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
			cache:false,
			height : 270,
			width: 900,
			//idField : 'id',
			 singleSelect : true,
	        clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
						deviceRepairOrderId:ordId
				};
				return temp;
			},
			responseHandler:function(data){
				return data.rows;
			},
			columns : [{ checkbox: true },{
				field : 'id',
				title : 'Id'
			}, 
			{
				field : 'code',
				align : 'center',
				title : '员工代码',
			}, {
				field : 'name',
				align : 'center',
				title : '员工名称'
			}, {
				field : 'receiveType',
				align : 'center',
				title : '接单类型',
				formatter : function(value, row, index) {
					if (value=="SYSTEMGASSIGN") {
						return "系统派单";
					}else if(value=="ARTIFICIALGASSIGN"){
						return "人工派单";
					}else if(value=="ASSIST"){
						return "协助";
					}else{
						return "";
					}
				}
			}, {
				field : 'assignTime',
				align : 'center',
				title : '派单时间',
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
					return "";
				}
			}, {
				field : 'receiveTime',
				align : 'center',
				title : '接收时间',
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
					return "";
				}
			}, {
				field : 'completeTime',
				align : 'center',
				title : '完成时间',
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
					return "";
				}
			}]
		});
		//报修单故障原因列表
		$("#showNGRecordTable").bootstrapTable({
			url :"ngMaintainRecord/queryNGMaintainRecordVO.do",
			cache:false,
			height : 270,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
						deviceRepairOrderId:ordId
				};
				return temp;
			},
			responseHandler:function(data){
				return data.rows;
			},
			columns : [{ checkbox: true },{
				field : 'id',
				title : 'Id'
			}, 
			{
				field : 'pressLightTypeCode',
				align : 'center',
				title : '故障类别代码',
				formatter : function(value, row, index) {
					if (row.projectType) {
						if(row.projectType.parent){
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
						if(row.projectType.parent){
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
			}]
		});
		//报修单维修项目列表
		$("#showMaintainProjectTable").bootstrapTable({
			url :"maintainProject/queryMaintainProject.do",
			cache:false,
			height : 270,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
						deviceRepairOrderId:ordId
				};
				return temp;
			},
			responseHandler:function(data){
				return data.rows;
			},
			columns : [{ checkbox: true },{
				field : 'id',
				title : 'Id'
			}, 
			{
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
			}]
		});
		
		//备件列表
		$("#showSparepartRecordTable").bootstrapTable({
			url :"sparepartRecord/querySparepartRecord.do",
			cache:false,
			height : 270,
			//idField : 'id',
			 singleSelect : true,
            clickToSelect : true,
			striped : true, //隔行换色
			queryParams : function(params) {
				var temp = {
						deviceRepairOrderId:ordId
				};
				return temp;
			},
			responseHandler:function(data){
				return data.rows;
			},
			columns : [{ checkbox: true },{
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
			} */, {
				field : 'typeName',
				align : 'center',
				title : '备件类别',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						if(row.sparepart.projectType){
							return row.sparepart.projectType.name;
						}
					}
					return "";
				}
			}, {
				field : 'sparepartCode',
				align : 'center',
				title : '备件代码',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.code;
					}
					return "";
				}
			}, {
				field : 'sparepartName',
				align : 'center',
				title : '备件名称',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.name;
					}
					return "";
				}
			}, {
				field : 'unitType',
				align : 'center',
				title : '规格型号',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.unitType;
					}
					return "";
				}
			}, {
				field : 'batchNumber',
				align : 'center',
				title : '批号',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.batchNumber;
					}
					return "";
				}
			}, {
				field : 'graphNumber',
				align : 'center',
				title : '图号',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.graphNumber;
					}
					return "";
				}
			}, {
				field : 'measurementUnit',
				align : 'center',
				title : '计量单位',
				formatter : function(value, row, index) {
					if (row.sparepart) {
						return row.sparepart.measurementUnit;
					}
					return "";
				}
			}, {
				field : 'quantity',
				align : 'center',
				title : '数量'
			}, {
				field : 'note',
				align : 'center',
				title : '备注'
			}, {
				field : 'createDate',
				align : 'center',
				title : '耗用时间',
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
					return "";
				}
			}]
		});
		
		$('#showCheckingPlanTable').bootstrapTable('hideColumn', 'id');
		$("#showCheckingPlanTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showDeviceRepairOrderTable').bootstrapTable('hideColumn', 'id');
		$("#showDeviceRepairOrderTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showMaintenancePlanRecordsTable').bootstrapTable('hideColumn', 'id');
		$("#showMaintenancePlanRecordsTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showDocTable').bootstrapTable('hideColumn', 'id');
		$("#showDocTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showStaffTable').bootstrapTable('hideColumn', 'id');
		$("#showStaffTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showNGRecordTable').bootstrapTable('hideColumn', 'id');
		$("#showNGRecordTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showMaintainProjectTable').bootstrapTable('hideColumn', 'id');
		$("#showMaintainProjectTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
		$('#showSparepartRecordTable').bootstrapTable('hideColumn', 'id');
		$("#showSparepartRecordTable").on('click-row.bs.table',
				function(e, row, element) { //splitLostTime confirmLostTime
					id = row.id;
					$('.success').removeClass('success'); //去除之前选中的行的，选中样式
					$(element).addClass('success'); //添加当前选中的 success样式用于区别
				});
	}  
	//类型改变事件
	$(document).ready(function(){
		//新增报修单故障类型变更
		$("#pressLight").change(function() {
			var id = $("#pressLight option:selected").val();
			$("#pressLightId").val(id);
		});
		//维修报修单故障类型变更
		$("#updatePressLight").change(function() {
			var id = $("#updatePressLight option:selected").val();
			$("#updatePressLightId").val(id);
		});
		//维修人员变更
		$("#StaffName").change(function() {
			var code = $("#StaffName option:selected").val();
			$("#StaffCode").val(code);
		});
		//维修人员变更
		$("#maintenanceInformantName").change(function() {
			var code = $("#maintenanceInformantName option:selected").val();
			$("#maintenanceInformantCode").val(code);
		});
		//故障类型变更
		$("#NGMaintainName").change(function() {
			var code = $("#NGMaintainName option:selected").val();
			$.ajax({
				type : "post",
				url : contextPath + "deviceProject/queryAllDeviceProjectByProjectTypeId.do",
				data : {projectTypeId:code},
				cache:false,
				dataType : "json",
				success : function(data) {
					
					   var htmlselect = "<option></option>";
		               $.each(data,function(index, Type) {
		                  htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
		               })
		               $("#deviceproject").empty();
		               $("#deviceproject").append(htmlselect);		
		               $("#deviceproject").selectpicker('refresh');
					$("#NGMaintainCode").val(data.code);
				}
			}) 
		});

		//故障原因变更
		$("#deviceproject").change(function() {
			var id = $("#deviceproject option:selected").val();
			if(!id){
				$("#NGMaintainId").val("");	
				return;
			}
			$.ajax({
				type : "post",
				url : contextPath + "deviceProject/queryDeviceProject.do",
				data : {id:id},
				cache:false,
				dataType : "json",
				success : function(data) {
					
					$("NGMaintainName").val(data.projectType.id); 
					$("#NGMaintainName").selectpicker('val',data.projectType.id);
					$("#NGMaintainId").val(data.id);
				}
			}) 
		});
		//维修项目变更
		$("#MaintainProjectName").change(function() {
			var id = $("#MaintainProjectName option:selected").val();
			if(!id){
				$("#MaintainProjectCode").val("");
				$("#MaintainProjectTypeCode").val("");
				$("#MaintainProjectTypeName").val("");
				$("#MaintainProjectTypeId").val("");
				return;
			}
			$.ajax({
				type : "post",
				url : contextPath + "deviceProject/queryDeviceProjectById.do",
				data : {id:id},
				cache:false,
				dataType : "json",
				success : function(data) {
					$("#MaintainProjectCode").val(data.code);
					$("#MaintainProjectTypeCode").val(data.deviceTypeCode);
					$("#MaintainProjectTypeName").val(data.deviceTypeName);
					$("#MaintainProjectTypeId").val(data.deviceTypeId);
				}
			}) 
		});
		//备件信息变更
		$("#SparepartName").change(function() {
			var code = $("#SparepartName option:selected").val();
			if(!code){
				$("#SparepartCode").val("");
				$("#SparepartId").val("");	
				return;
			}
			$.ajax({
				type : "post",
				url : contextPath + "sparepart/querySparepartsByCode.do",
				data : {SparepartsCode:code},
				cache:false,
				dataType : "json",
				success : function(data) {
					$("#SparepartCode").val(data.code);
					$("#SparepartId").val(data.id);
				}
			}) 
		});
		//备件信息变更
		$("#MaintenanceSparepartName").change(function() {
			var code = $("#MaintenanceSparepartName option:selected").val();
			if(!code){
				$("#MaintenanceSparepartCode").val("");
				$("#MaintenanceSparepartId").val("");	
				return;
			}
			$.ajax({
				type : "post",
				url : contextPath + "sparepart/querySparepartsByCode.do",
				data : {SparepartsCode:code},
				cache:false,
				dataType : "json",
				success : function(data) {
					$("#MaintenanceSparepartCode").val(data.code);
					$("#MaintenanceSparepartId").val(data.id);
				}
			}) 
		});
	});
	
	//模态框点击返回事件(hide)
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
	//拆分模态框点击返回事件(hide)
	function splitModelHide() {
		$('#splitShowConfirmDlg').modal('hide');
		$('#splitShowUpdateDlg').modal('hide');
		
	}
	
	//设备报修功能按钮
	function addLostTime(e){
		param =e;
		if(e=="repairAdd"){//新增
				 showAddDeviceRepairOrderDialogDialog();
		}else if(e=="repairUpdate"){//修改
				 var alldata=$("#showDeviceRepairOrderTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}/* else if(alldata[0].status!="MAINTAINING"){
			        $("#alertText").text("请选择维修中或待车间确认的数据");
					$("#alertDialog").modal();
					return false;
				} */
				$("#addSparepartView").show();
				$("#addMaintainProjectView").show();
				$("#addNGMaintainView").show();
				$("#addMaintenanceStaffView").show();
				$("#removeSparepartView").show();
				$("#removeMaintainProjectView").show();
				$("#removeNGMaintainView").show();
				$("#removeMaintenanceStaffView").show();
				showUpdateDeviceRepairOrderDialogDialog(alldata[0].id);
		}else if(e=="repairView"){//查看
				var alldata=$("#showDeviceRepairOrderTable").bootstrapTable('getSelections');
			    var sum = alldata.length;
			    param="";
			    if(sum<1){//id==''
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
		}else if(e=="repairConfirm"){//确认
			var alldata=$("#showDeviceRepairOrderTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			}
			 if(alldata[0].status=="WAITWORKSHOPCOMFIRM"){
			 	$.ajax({
		          	type : "post",
			            url : contextPath + "mcDeviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=MAINTAINCOMPLETE",
			            data : {"id":alldata[0].id},
						cache:false,
			            dataType : "json",
			            success : function(data) {
			            	//刷新维修记录表
		                     $("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
		 						url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
		 						cache:false,
		 						query : {
		 							deviceCode :_deviceSiteCode
		 						}
		 					});
			            	$("#alertText").text(data.message);
		                     $("#alertDialog").modal();
			            }
			        })
			 }else{
				 $("#alertText").text("该记录不是可确认记录!");
                 $("#alertDialog").modal();
			 }
		}else if(e=="repairComplete"){//维修完成
			 if($("#updateStatus").val()=="MAINTAINING"){
			 	$.ajax({
		          	type : "post",
			            url : contextPath + "mcDeviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=WAITWORKSHOPCOMFIRM",
			            data : {"id":$("#orderId").val()},
						cache:false,
			            dataType : "json",
			            success : function(data) {
			            	//刷新维修记录表
		                     $("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
		 						url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
		 						cache:false,
		 						query : {
		 							deviceCode :_deviceSiteCode
		 						}
		 					});
			            	$("#alertText").text(data.message);
		                     $("#alertDialog").modal();
			            }
			        })
			 }else{
				 $("#alertText").text("该记录不是可完成记录!");
                $("#alertDialog").modal();
                $('#updateDeviceRepairOrderDialog').modal('show');
			 }
		}else if(e=="deviceCheck"){//点检更新
			var alldata=$("#showCheckingPlanTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			}
			 queryDeviceCheck(alldata[0].id,e);
			$('#checksave').show();
			$('#updateCheckingPlanDialog').modal('show');
		}else if(e=="CheckView"){//查看点检信息
			var alldata=$("#showCheckingPlanTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			}
			 queryDeviceCheck(alldata[0].id,e);
			$('#checksave').hide();
			$('#updateCheckingPlanDialog').modal('show');
		}else if(e=="Maintenance"){ //保养
			 var alldata=$("#showMaintenancePlanRecordsTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			} 
			$("#addMaintenanceSparepartView").show();
		    //$("#addMaintenanceProjectView").show();
		    $("#addStaffMaintenanceView").show();
			$("#removeMaintenanceSparepartView").show();
		    //$("#removeMaintenanceProjectView").show();
		    $("#removeStaffMaintenanceView").show();
		    setMaintenancePlanRecordsTable(alldata[0].id);
		    
		    $("#showMaintenanceStaffTable").bootstrapTable("refresh", {
				url :"mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
			//刷新保养项目表
			$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
				url :"mcMaintenanceItem/queryMaintenanceItemById.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
			//刷新备件信息表
			$("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
				url :"maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
		    
			$('#maintenanceOrderDialog').modal('show');
		}else if(e=="MaintenanceConfirm"){ //确认
			var alldata=$("#showMaintenancePlanRecordsTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			}
			 if(!alldata[0].confirmDate&&alldata[0].maintenancedDate){
			 	$.ajax({
		          	type : "post",
			            url : contextPath + "mcMaintenancePlanRecord/confirm.do",
			            data : {"id":alldata[0].id},
						cache:false,
			            dataType : "json",
			            success : function(data) {
			            	//刷新维修人员表
		                     $("#showMaintenancePlanRecordsTable").bootstrapTable("refresh", {
		 						url :"mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
		 						cache:false,
		 						query : {
		 							deviceSiteCode :_deviceSiteCode
		 						}
		 					});
			            	$("#alertText").text(data.message);
		                     $("#alertDialog").modal();
			            }
			        })
			 }else{
				 $("#alertText").text("该记录已确认或未保养!");
                $("#alertDialog").modal();
			 }
		}else if(e=="MaintenanceView"){ //查看
			$("#addMaintenanceSparepartView").hide();
		    //$("#addMaintenanceProjectView").hide();
		    $("#addStaffMaintenanceView").hide();
			$("#removeMaintenanceSparepartView").hide();
		    //$("#removeMaintenanceProjectView").hide();
		    $("#removeStaffMaintenanceView").hide();
		    
		    var alldata=$("#showMaintenancePlanRecordsTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			} 
		    setMaintenancePlanRecordsTable(alldata[0].id);
		    
		    $("#showMaintenanceStaffTable").bootstrapTable("refresh", {
				url :"mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
			//刷新保养项目表
			$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
				url :"mcMaintenanceItem/queryMaintenanceItemById.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
			//刷新备件信息表
			$("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
				url :"maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
				cache:false,
				query : {
					recordId:ordId
				}
			});
			$('#maintenanceOrderDialog').modal('show');
		}else if(e=="DocView"){//文档查看
			var alldata=$("#showDocTable").bootstrapTable('getSelections');
			 var sum = alldata.length;
			 param="";
			 if(sum<1){//id==''
		        $("#alertText").text("请选择数据");
				$("#alertDialog").modal();
				return false;
			}
			/* $("#DocImg").attr("src",alldata[0].url);
			$('#showDoclog').modal('show');*/


            //显示文档内容的iframe对象
            var $previewDiv = $("#fullScreenDiv");
            $previewDiv.empty();
            var suffix = '';
            //截取后缀
            var url=alldata[0].url
            if(url!=null && $.trim(url)!=''){
                var point = url.lastIndexOf(".");
                suffix = url.substr(point+1);
            }else{
                alert("没有预览文档!");
                return false;
            }
            var $docIframe=$("<iframe style='width:100%;height:100%;'>");
            switch(suffix){
                case "pdf":
                case "PDF":{
                    $docIframe.attr("src",url);
                    $previewDiv.append($docIframe);
                    break;
                }
                case "png":
                case "PNG":
                case "jpg":
                case "JPG":
                case "JPEG":
                case "jpeg":
                case "gif":
                case "GIF":
                case "bmp":
                case "BMP":{
                    var $img = $("<img style='height:100%;width:100%;'>");
                    $img.attr("src",url);
                    $previewDiv.append($img);
                    break;
                }
            }
            $("#dialog-layer").css("display","block");

		}
	}
	//获取所有故障原因
	function getAllReasonAdd(){
		 $.ajax({
         	type : "post",
	            url : contextPath + "deviceProject/queryAllDeviceProjectByType.do?type=BREAKDOWNREASON",
	            data : {},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	//alert(JSON.stringify(data));
	                var htmlselect = "<option></option>";
	               $.each(data,function(index, Type) {
	                  htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
	               })
	               $("#pressLight").empty();
	               $("#pressLight").append(htmlselect);		
	               $("#pressLight").selectpicker('refresh');

	               $("#updatePressLight").empty();
	               $("#updatePressLight").append(htmlselect);		
	               $("#updatePressLight").selectpicker('refresh');
						
	            }
	        })
	}
	
	//显示报修新增窗口
	function showAddDeviceRepairOrderDialogDialog() {
		getAllReasonAdd();
	 	$('#ngDescription').val("");
	 	$('#picDiv').empty();
	 	$('div').remove(".imageDiv");
	 	$(".addImages").show();
	 	idList="";
	 	imageList=[0,1,2,3];
	 	$('#addDeviceRepairOrderDialog').modal('show');
	 	 var date = new Date();
			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
					+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
					+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
		  $("#createDate").val(value);
		  $.ajax({
	          	type : "post",
		            url : contextPath + "deviceSite/queryDeviceSiteByCode.do",
		            data : {"deviceSiteCode":_deviceSiteCode},
					cache:false,
		            dataType : "json",
		            success : function(data) {
		            	if(data){
		            		if(data.device){
		            			$("#deviceCode").val(data.device.code);
		            			$("#deviceName").val(data.device.name);
		            			$("#unitType").val(data.device.unitType);
		            			if(data.device.projectType)
			            			$("#deviceType").val(data.device.projectType.name);
		            			//根据IP地址查询本机是否有登录用户
		            			$.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
		            					mcUser) {
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
	//查询需要修改的报修单信息
	function showUpdateDeviceRepairOrderDialogDialog(orderId) {
		getAllReasonAdd();
		getPic(orderId);
		ordId=orderId;
	 	$('#picDiv').empty();
	 	$('#updateDeviceRepairOrderDialog').modal('show');
		  $.ajax({
	          	type : "post",
		            url : contextPath + "mcDeviceRepairOrder/queryDeviceRepairOrderById.do",
		            data : {"id":orderId},
					cache:false,
		            dataType : "json",
		            success : function(data) {
		            	if(data){
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
		            			$("#updatePressLight").selectpicker('val',data.ngid);
		            			$("#updatePressLightId").val(data.ngid);
		            			//刷新维修人员表
		            			$("#showStaffTable").bootstrapTable("refresh", {
		            				url :"maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
		            				cache:false,
		            				query : {
		            					deviceRepairOrderId:orderId
		            				}
		            			});
		            	}
		            }
		        })
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
	
//新增设备报修
/* function addDeviceRepair(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	 $.ajax({
			type : "post",
			url : contextPath + "mcDeviceRepairOrder/addDeviceRepairOrder.do",
			data : {	
                "createDate":$('#createDate').val(),
                "device.id":$('#deviceId').val(),
                "Informant":$('#Informant').val(),
                "projectType.id":$('#pressLightId').val(),
                "ngDescription":$('#ngDescription').val(),
                "idList":$('#photoIds').val()
			},
			dataType : "json",
			success : function(data) {
					$("#alertText").text(data.msg);
                    $("#alertDialog").modal();
                    $('#addDeviceRepairOrderDialog').modal('hide');
				if(data.success){
					$("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
						url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
						cache:false,
						query : {
							deviceCode :_deviceSiteCode
						}
					});
				}
			}
	 })
} */

//更新设备报修
function updateDeviceRepair(){
	$('#updateDeviceRepairOrderDialog').modal('hide');
	 $.ajax({
			type : "post",
			url : contextPath + "mcDeviceRepairOrder/updateDeviceRepairOrder.do",
			data : {	
				"createDate":$('#updateCreateDate').val(),				
                "id":$('#orderId').val(),
                "device.id":$('#updateDeviceId').val(),
                "ngreason.id":$('#updatePressLightId').val(),
                "ngDescription":$('#updateNGDescription').val(),
			},
			dataType : "json",
			success : function(data) {
					$("#alertText").text(data.msg);
                    $("#alertDialog").modal();
                    $('#addDeviceRepairOrderDialog').modal('hide');
				if(data.success){
					$("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
						url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
						cache:false,
						query : {
							deviceCode :_deviceSiteCode
						}
					});
				}
			}
	 })
}
//修改设备报修
function addDeviceRepair(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	 $.ajax({
			type : "post",
			url : contextPath + "mcDeviceRepairOrder/addDeviceRepairOrder.do",
			data : {	
                "createDate":$('#createDate').val(),
                "device.id":$('#deviceId').val(),
                "Informant":$('#Informant').val(),
                "ngreason.id":$('#pressLightId').val(),
                "ngDescription":$('#ngDescription').val(),
                "idList":$('#photoIds').val()
			},
			dataType : "json",
			success : function(data) {
					$("#alertText").text(data.msg);
                    $("#alertDialog").modal();
                    $('#addDeviceRepairOrderDialog').modal('hide');
				if(data.success){
					$("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
						url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
						cache:false,
						query : {
							deviceCode :_deviceSiteCode
						}
					});
				}
			}
	 })
}

function getPic(id){
	 var url ="deviceRepairOrder/queryDeviceRepairOrderById.do?id="+id;            
    $.ajax({
        type:'POST',
        url:url,
        contentType:false,
        processData:false,
        dataType:'json',
        mimeType:"multipart/form-data",
        success:function(data){
       	 var picDiv = $("#updatePicDiv");
       	 picDiv.empty();
        	if(data.picName){
        		var picName = data.picName
        		  for (var i = 0; i < picName.length; i++) {
        		var picHtml = "<div class='imageDiv' style='margin-top: -0px'> <img  id='img" + i + "' src='"+contextPath+picName[i]+"' style='height: 180px;width: 180px;float: left;' /> </div>";
        		 picDiv.append(picHtml);
        		  }
        	}
        }
    });
}
    var index = 0;
$(function (){
		　　$(".tabbox li").click(function ()
		　　{
		　　　　//获取点击的元素给其添加样式，讲其兄弟元素的样式移除
		　　　　$(this).addClass("active").siblings().removeClass("active");
		　　　　$(this).addClass("active").siblings().css("border","1px solid #00000E")
				//移除边框属性
		　　　　//$(this).css("border","initial");
		　　　　$(this).css("border","0px solid #00000E");
		　　　　//获取选中元素的下标
		　　　　var index = $(this).index();
		　　　　$(this).parent().siblings().children().eq(index).addClass("active")
		　　　　.siblings().removeClass("active");
				if(index==1){
					//刷新维修人员表
					$("#showStaffTable").bootstrapTable("refresh", {
						url :"maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
						cache:false,
						query : {
							deviceRepairOrderId:ordId
						}
					});
				}else if(index==2){
					//刷新维修人员表
					$("#showNGRecordTable").bootstrapTable("refresh", {
						url :"ngMaintainRecord/queryNGMaintainRecordVO.do",
						cache:false,
						query : {
							deviceRepairOrderId:ordId
						}
					});
				}else if(index==3){
					//刷新维修项目表
					$("#showMaintainProjectTable").bootstrapTable("refresh", {
						url :"maintainProject/queryMaintainProject.do",
						cache:false,
						query : {
							deviceRepairOrderId:ordId
						}
					});
				}else if(index==4){
					//刷新备件信息表
					$("#showSparepartRecordTable").bootstrapTable("refresh", {
						url :"sparepartRecord/querySparepartRecord.do",
						cache:false,
						query : {
							deviceRepairOrderId:ordId
						}
					});
				}
		　　});
		　　$(".mtabbox li").click(function ()
		　　{
		　　　　//获取点击的元素给其添加样式，讲其兄弟元素的样式移除
		　　　　$(this).addClass("active").siblings().removeClass("active");
		　　　　$(this).addClass("active").siblings().css("border","1px solid #00000E")
				//移除边框属性
		　　　　$(this).css("border","0px solid #00000E")
		　　　　//获取选中元素的下标
		　　　　var index = $(this).index();
		　　　　$(this).parent().siblings().children().eq(index).addClass("active")
		　　　　.siblings().removeClass("active");
				if(index==1){
					//刷新保养人员表
					$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
						url :"mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
						cache:false,
						query : {
							recordId:ordId
						}
					});
				}else if(index==2){
					//刷新保养项目表
					$("#showMaintenanceProjectTable").bootstrapTable("refresh", {
						url :"mcMaintenanceItem/queryMaintenanceItemById.do",
						cache:false,
						query : {
							recordId:ordId
						}
					});
				}else if(index==3){
					//刷新备件信息表
					$("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
						url :"maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
						cache:false,
						query : {
							recordId:ordId
						}
					});
				}
		　　});
		});
		
		function addRecord(parameter){
			if(parameter=="staff"){//显示新增维护人员
				$("#StaffName").empty();
				$("#StaffName").selectpicker('refresh');
				SetStaffName();
				$("#StaffCode").val("");
				var date = new Date();
					var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
							+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
							+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
				$("#StaffcreateDate").val(value);
				 
				$('#updateDeviceRepairOrderDialog').modal('hide');
				$('#addMaintenanceStaff').modal('show');
				
			}else if(parameter=="ngRecord"){//显示新增故障原因
				$("#NGMaintainName").empty();
				$("#NGMaintainName").selectpicker('refresh');
				$("#deviceproject").empty();
			     $("#deviceproject").selectpicker('refresh');
				SetNGMaintainName();
				Setdeviceproject();
				 $("#NGMaintainCode").val("");
				 $("#NGMaintainNote").val("");
				 
				$('#updateDeviceRepairOrderDialog').modal('hide');
				$('#addNGMaintain').modal('show');
				
			}else if(parameter=="maintainProject"){//显示新增维护项目
				$("#MaintainProjectName").empty();
				SetMaintainProject();
				$("#MaintainProjectName").selectpicker('refresh');
				 $("#MaintainProjectCode").val("");
				 $("#MaintainProjectTypeCode").val("");
				 $("#MaintainProjectTypeName").val("");
				 $("#MaintainProjectNote").val("");
				 $("#MaintainProjectProcessingMethod").val("");
				 $("#MaintainProjectRemark").val("");
				 
				$('#updateDeviceRepairOrderDialog').modal('hide');
				$('#addMaintainProject').modal('show');
				
			}else if(parameter=="sparepartRecord"){//显示新增备件
				$("#SparepartName").empty();
				SetSparepartName();
				$('#updateDeviceRepairOrderDialog').modal('hide');
				 var date = new Date();
					var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
							+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
							+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
				  $("#SparepartcreateDate").val(value);
				  $("#SparepartCode").val("");
				  $("#SparepartId").val("");
				  $("#SparepartNum").val("1");
				  $("#SparepartNote").val("");
				  $("#SparepartName").selectpicker('refresh');
				$('#addSparepart').modal('show');
				
			}else if(parameter=="maintenanceStaff"){//保养人员
				$("#maintenanceInformantName").empty();
				$("#StaffName").selectpicker('refresh');
				SetStaffName();
				$("#maintenanceInformantCode").val("");
				var date = new Date();
					var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
							+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
							+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
				$("#StaffcreateDate").val(value);
				 
				$('#maintenanceOrderDialog').modal('hide');
				$('#addMaintenance').modal('show');
			}else if(parameter=="maintenanceSparepart"){//保养备件
				$("#MaintenanceSparepartName").empty();
				SetMaintenanceSparepartName();
				$('#maintenanceOrderDialog').modal('hide');
				  $("#MaintenanceSparepartCode").val("");
				  $("#MaintenanceSparepartId").val("");
				  $("#MaintenanceSparepartNum").val("1");
				  $("#MaintenanceSparepartName").selectpicker('refresh');
				$('#addMaintenanceSparepart').modal('show');
			}
			else{
				return "";
			}
		}
		//删除记录
		function removeRecord(parameter){
			if(parameter=="staff"){//删除维护人员记录
				
				 var alldata=$("#showStaffTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				 }
				 
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcMaintenanceStaffRecord/deleteMaintenanceStaffRecord.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                     $("#showStaffTable").bootstrapTable("refresh", {
										url :"mcMaintenanceStaffRecord/queryMaintenanceStaffRecord.do",
										cache:false,
										query : {
											deviceRepairOrderId:ordId
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else if(parameter=="ngRecord"){//删除故障原因记录
				var alldata=$("#showNGRecordTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
			        if(window.confirm('你确定要删除吗？')){
			        	$.ajax({
				          	type : "post",
					            url : contextPath + "mcNGMaintainRecord/deleteNGMaintainRecord.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                    $("#showNGRecordTable").bootstrapTable("refresh", {
										url :"ngMaintainRecord/queryNGMaintainRecordVO.do",
										cache:false,
										query : {
											deviceRepairOrderId:ordId
										}
									});
				                    alert(data.message);
					            }
					        })
		             }else{
		                 return false;
		             }
				 	
			}else if(parameter=="maintainProject"){//删除维护项目记录
				var alldata=$("#showMaintainProjectTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
				 
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcMaintainProject/deleteMaintainProjectById.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                    $("#showMaintainProjectTable").bootstrapTable("refresh", {
										url :"mcMaintainProject/queryMaintainProject.do",
										cache:false,
										query : {
											deviceRepairOrderId:ordId
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else if(parameter=="sparepartRecord"){//删除备件记录
				var alldata=$("#showSparepartRecordTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcSparepartRecord/deleteSparepartRecord.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                    $("#showSparepartRecordTable").bootstrapTable("refresh", {
										url :"mcSparepartRecord/querySparepartRecord.do",
										cache:false,
										query : {
											deviceRepairOrderId:ordId
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else if(parameter=="maintenanceStaff"){//删除保养人员记录
				var alldata=$("#showMaintenanceStaffTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcMaintenanceUser/deleteMaintenanceUser.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                   $("#showMaintenanceStaffTable").bootstrapTable("refresh", {
										url :"mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
										cache:false,
										query : {
											recordId:ordId
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else if(parameter=="maintenanceProject"){//删除保养备件记录
				var alldata=$("#showMaintenanceProjectTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcMaintenanceItem/deleteMaintenanceItem.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                   $("#showMaintenanceProjectTable").bootstrapTable("refresh", {
										url :"mcMaintenanceItem/queryMaintenanceItemById.do",
										cache:false,
										query : {
											recordId:ordId
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else if(parameter=="maintenanceSparepart"){//删除保养备件记录
				var alldata=$("#showMaintenanceSparepartRecordTable").bootstrapTable('getSelections');
				 var sum = alldata.length;
				 param="";
				 if(sum<1){//id==''
			        $("#alertText").text("请选择数据");
					$("#alertDialog").modal();
					return false;
				}
				 if(window.confirm('你确定要删除吗？')){
					 $.ajax({
				          	type : "post",
					            url : contextPath + "mcMaintenanceSparepart/deleteMaintenanceSparepart.do",
					            data : {"id":alldata[0].id},
								cache:false,
					            dataType : "json",
					            success : function(data) {
					            	//刷新记录表
				                   $("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
										url :"mcMaintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
										cache:false,
										query : {
											recordId:id
										}
									});
					            	$("#alertText").text(data.message);
				                     $("#alertDialog").modal();
					            }
					        })
	             }else{
	                 return false;
	             }
				 	
			}else{
				return "";
			}
		}
		
		//员工下拉框
		function SetStaffName() {
			 $.ajax({
					type : "post",
					url : contextPath + "employee/queryAllEmployees.do",
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
						})
						$("#StaffName").append(htmlselect);
						$("#StaffName").selectpicker('refresh');
						$("#maintenanceInformantName").append(htmlselect);
						$("#maintenanceInformantName").selectpicker('refresh');
					}
				}) 
		}
		//故障原因下拉框
		function SetNGMaintainName() {
			 $.ajax({
					type : "post",
					url : contextPath + "projectType/queryProjectTypesByType.do?type=breakdownReasonType",
					data : {},
					cache:false,
					dataType : "json",
					success : function(data) {
					 	var htmlselect = "<option></option>";
						$.each(data,function(index, Type) {
							htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
							var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
									+ Type.code
									+ "\")' value='"+Type.id+"'>"
									+ Type.name
									+ "</button>");
						})
						$("#NGMaintainName").append(htmlselect);
						$("#NGMaintainName").selectpicker('refresh');
					}
				}) 
		}
		
		function Setdeviceproject() {
			$.ajax({
				type : "post",
				url : contextPath + "deviceProject/queryAllDeviceProjectByType.do?type=BREAKDOWNREASON",
				data : {},
				cache:false,
				dataType : "json",
				success : function(data) {
					var htmlselect = "<option></option>";
					$.each(data,function(index, Type) {
						htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
						var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
								+ Type.code
								+ "\")' value='"+Type.id+"'>"
								+ Type.name
								+ "</button>");
					})
					$("#deviceproject").append(htmlselect);
					$("#deviceproject").selectpicker('refresh');
				}
			}) 
		}
		//维修项目下拉框
		function SetMaintainProject() {
			 $.ajax({
					type : "post",
					url : contextPath + "deviceProject/queryAllDeviceProjectByType.do?type=MAINTENANCEITEM",
					data : {},
					cache:false,
					dataType : "json",
					success : function(data) {
					 	var htmlselect = "<option></option>";
						$.each(data,function(index, Type) {
							htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
							var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
									+ Type.id
									+ "\")' value='"+Type.code+"'>"
									+ Type.name
									+ "</button>");
						})
						$("#MaintainProjectName").append(htmlselect);
						$("#MaintainProjectName").selectpicker('refresh');
					}
				}) 
		}
		//维修项目下拉框
		function SetSparepartName() {
			 $.ajax({
					type : "post",
					url : contextPath + "sparepart/querySparepartsByDeviceRepairId.do",
					data : {DeviceRepairId: ordId},
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
						})
						$("#SparepartName").append(htmlselect);
						$("#SparepartName").selectpicker('refresh');
					}
				}) 
		}
		//保养备件下拉框
		function SetMaintenanceSparepartName() {
			 $.ajax({
					type : "post",
					url : contextPath + "sparepart/querySparepartsByMaintenanceId.do",
					data : {MaintenanceId: ordId},
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
						})
						
						$("#MaintenanceSparepartName").append(htmlselect);
						$("#MaintenanceSparepartName").selectpicker('refresh');
					}
				}) 
		}
		//新增维修人员
		function addMaintenanceStaff() {
			if(!$('#StaffCode').val()){
				$("#alertText").text("请选择维修人员！");
                $("#alertDialog").modal();
                return;
			}
			 $.ajax({
					type : "post",
					url : contextPath + "mcMaintenanceStaffRecord/addMaintenanceStaffRecord.do",
					data : {
						"deviceRepair.id":ordId,
						name:$("#StaffName option:selected").text(),
	          			code:$('#StaffCode').val(),
	          			assignTime:$('#StaffcreateDate').val(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						 $("#alertText").text(data.msg);
	                     $("#alertDialog").modal();
						$('#addMaintenanceStaff').modal('hide');
						$('#updateDeviceRepairOrderDialog').modal('show');
						$("#showStaffTable").bootstrapTable("refresh", {
							url :"maintenanceStaffRecord/queryMaintenanceStaffRecord.do",
							cache:false,
							query : {
								deviceRepairOrderId:ordId
							}
						});
					}
				}) 
		}
		//新增保养人员
		function addMaintenance() {
			if(!$('#maintenanceInformantCode').val()){
				$("#alertText").text("请选择保养人员！");
                $("#alertDialog").modal();
                return;
			}
			 $.ajax({
					type : "post",
					url : contextPath + "mcMaintenanceUser/addMaintenanceUser.do",
					data : {
						"maintenancePlanRecord.id":ordId,
						name:$("#maintenanceInformantName option:selected").text(),
	          			code:$('#maintenanceInformantCode').val(),
	          			orderType:$("#orderType option:selected").text(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						if(!data.success){
							$("#alertText").text("添加失败");
		                     $("#alertDialog").modal();
		                     return;
						}
						$("#alertText").text("添加成功");
	                    $("#alertDialog").modal();
						$('#addMaintenance').modal('hide');
						$('#maintenanceOrderDialog').modal('show');
						$("#showMaintenanceStaffTable").bootstrapTable("refresh", {
							url :"mcMaintenanceUser/queryMaintenanceUserByRecordId.do",
							cache:false,
							query : {
								recordId:ordId
							}
						});
					}
				}) 
		}
		//新增故障原因
		function addNGMaintain() {
			if(!$('#NGMaintainId').val()){
				alert("请选择故障原因！");
				return;
			}
			 $.ajax({
					type : "post",
					url : contextPath + "mcNGMaintainRecord/addMaintainProject.do",
					data : {
						"deviceRepair.id":ordId,
						"deviceProject.id":$('#NGMaintainId').val(),
	          			note:$('#NGMaintainNote').val(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						alert(data.msg);
						$('#addNGMaintain').modal('hide');
						$('#updateDeviceRepairOrderDialog').modal('show');
						$("#showNGRecordTable").bootstrapTable("refresh", {
							url :"ngMaintainRecord/queryNGMaintainRecordVO.do",
							cache:false,
							query : {
								deviceRepairOrderId:ordId
							}
						});
					}
				}) 
		}

		//新增维修项目
		function addMaintainProject() {
			if(!$('#MaintainProjectTypeId').val()){
				$("#alertText").text("请选择维修项目！");
                $("#alertDialog").modal();
                return;
			}
			 $.ajax({
					type : "post",
					url : contextPath + "mcMaintainProject/addMaintainProject.do",
					data : {
						"deviceRepair.id":ordId,
						code:$('#MaintainProjectCode').val(),
						name:$("#MaintainProjectName option:selected").text(),
						"type.id":$('#MaintainProjectTypeId').val(),
	          			note:$('#MaintainProjectNote').val(),
	          			processingMethod:$('#MaintainProjectProcessingMethod').val(),
	          			remark:$('#MaintainProjectRemark').val(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						 $("#alertText").text(data.msg);
	                     $("#alertDialog").modal();
						$('#addMaintainProject').modal('hide');
						$('#updateDeviceRepairOrderDialog').modal('show');
						$("#showMaintainProjectTable").bootstrapTable("refresh", {
							url :"maintainProject/queryMaintainProject.do",
							cache:false,
							query : {
								deviceRepairOrderId:ordId
							}
						});
					}
				}) 
		}
		//新增备件信息
		function addSparepart() {
			if(!$('#SparepartId').val()){
				$("#alertText").text("请选择备件！");
                $("#alertDialog").modal();
                return;
			}
			 $.ajax({
					type : "post",
					url : contextPath + "mcSparepartRecord/addSparepartRecord.do",
					data : {
						"deviceRepair.id":ordId,
						"sparepart.id":$('#SparepartId').val(),
						quantity:$('#SparepartNum').val(),
	          			note:$('#SparepartNote').val(),
	          			createDate:$('#SparepartcreateDate').val(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						 $("#alertText").text(data.msg);
	                     $("#alertDialog").modal();
						$('#addSparepart').modal('hide');
						$('#updateDeviceRepairOrderDialog').modal('show');
						$("#showSparepartRecordTable").bootstrapTable("refresh", {
							url :"sparepartRecord/querySparepartRecord.do",
							cache:false,
							query : {
								deviceRepairOrderId:ordId
							}
						});
					}
				}) 
		}
		//保养备件新增
		function addMaintenanceSparepart() {
			if(!$('#MaintenanceSparepartId').val()){
				$("#alertText").text("请选择备件！");
                $("#alertDialog").modal();
                return;
			} 
			$.ajax({
					type : "post",
					url : contextPath + "mcMaintenanceSparepart/addMaintenanceSpareparts.do",
					data : {
						maintenancePlanRecordId:ordId,
						sparepartIds:$('#MaintenanceSparepartId').val(),
						count:$('#MaintenanceSparepartNum').val(),
					},
					cache:false,
					dataType : "json",
					success : function(data) {
						if(!data.success){
						 $("#alertText").text("添加失败");
	                     $("#alertDialog").modal();
	                     return ;
						}
						 $("#alertText").text("添加成功");
	                     $("#alertDialog").modal();
						$('#addMaintenanceSparepart').modal('hide');
						$('#maintenanceOrderDialog').modal('show');
						$("#showMaintenanceSparepartRecordTable").bootstrapTable("refresh", {
							url :"maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
							cache:false,
							query : {
								recordId:id
							}
						});
					}
				}) 
		}
		
		//修改报修单子表格返回事件(hide)
		function RepairOrderHide() {
			$('#addMaintenanceStaff').modal('hide');
			$('#addNGMaintain').modal('hide');
			$('#addMaintainProject').modal('hide');
			$('#addSparepart').modal('hide');
			$('#updateDeviceRepairOrderDialog').modal('show');
		}
		//修改报修单子表格返回事件(hide)
		function maintenanceOrderHide() {
			$('#addMaintenance').modal('hide');
			$('#addNGMaintain').modal('hide');
			$('#addMaintainProject').modal('hide');
			$('#addSparepart').modal('hide');
			$('#addMaintenanceSparepart').modal('hide');
			$('#maintenanceOrderDialog').modal('show');
		}
        var docs="";
		//查询点检详细信息
		function queryDeviceCheck(id,type) {
			$.get("mcCheckingPlanRecord/queryCheckingPlanRecordById.do",{id:id},function(result){
				//设备点检记录对象
				var record = result.record;
				//点检人对象
			 	var checkUsername = result.checkUsername;
				var checkUsercode = result.checkUsercode;
				$("#checkInformant").val(checkUsername);
				$("#checkInformantCode").val(checkUsercode);
				//关联文档集合
				docs = result.docs;
				//点检项目标准记录
				var projectRecords = result.projectRecords;
				setCheckingPlanRecord(record);
				setDocs(docs);
                if(type=="deviceCheck"){//点检
                    setProjectRecords(projectRecords);
				}
                if(type=="CheckView"){//查看点检信息
                    queryProjectRecords(projectRecords);
				}

			});
		}

    //全屏查看
    function showFullScreen(){
        //显示文档内容的iframe对象
        var $previewDiv = $("#fullScreenDiv");
        $previewDiv.empty();
        var suffix = '';
        //截取后缀
        var url=docs[index].url
        if(url!=null && $.trim(url)!=''){
            var point = url.lastIndexOf(".");
            suffix = url.substr(point+1);
        }else{
            alert("没有预览文档!");
            return false;
        }
        var $docIframe=$("<iframe style='width:100%;height:100%;'>");
        switch(suffix){
            case "pdf":
            case "PDF":{
                $docIframe.attr("src",url);
                $previewDiv.append($docIframe);
                break;
            }
            case "png":
            case "PNG":
            case "jpg":
            case "JPG":
            case "JPEG":
            case "jpeg":
            case "gif":
            case "GIF":
            case "bmp":
            case "BMP":{
                var $img = $("<img style='height:100%;width:100%;'>");
                $img.attr("src",url);
                $previewDiv.append($img);
                break;
            }
        }
        $("#dialog-layer").css("display","block");
    }
		
		//向页面填充点检记录信息
		function setCheckingPlanRecord(record){
			$("#checkId").val(record.id);
			$("#checkDeviceCode").val(record.deviceCode);
			$("#checkDeviceName").val(record.deviceName);
			$("#checkUnitType").val(record.unitType);
			var date = new Date();
			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
					+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
					+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
		  $("#checkCreateDate").val(value);
		}
		
		//填充文档信息(设备点检图片)
		function setDocs(docs){
		    index=0;
			/*if(docs && docs.length>0){
				var docsDiv = $("#docs");
				docsDiv.empty();
				for(var i = 0;i<docs.length;i++){
					if(i%2==0){
						var leftDiv = $("<div class='leftDiv' style='height:160px;width:46%;float:left; margin:5px;border-radius: 10px;'>");
						var leftImg = $("<img  src='" + docs[i].url + "' style='width:100%;height:100%;border-radius: 10px;'/>");
						leftDiv.append(leftImg);
						docsDiv.append(leftDiv);
					}else{
						var rightDiv = $("<div class='rightDiv' style='height:160px;width:46%;float:right;margin:5px;border-radius: 10px;'>");
						var rightImg = $("<img  src='" + docs[i].url + "' style='width:100%;height:100%;border-radius: 10px;'/>");
						rightDiv.append(rightImg);
						docsDiv.append(rightDiv);
					}
				}
			}*/
            var $previewDiv = $("#deviceCheckImg");
            var url="";
            if(docs!=null && docs.length>0){
                //默认显示第0张图片
                url=docs[index].url;
            }
            $previewDiv.empty();
            var suffix = '';

            //截取后缀
            if(url!=null && $.trim(url)!=''){
                var point = url.lastIndexOf(".");
                suffix = url.substr(point+1);
            }else{
                return false;
            }
            var $docIframe=$("<iframe style='width:100%;height:100%;'>");
            switch(suffix){
                case "pdf":
                case "PDF":{
                    $docIframe.attr("src",url);
                    $previewDiv.append($docIframe);
                    break;
                }
                <%-- case "doc":
                case "docx":
                case "DOC":
                case "DOCX":
                case "xls":
                case "xlsx":
                case "XLS":
                case "XLSX":
                case "ppt":
                case "pptx":
                case "PPT":
                case "PPTX":{
                    $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                    $previewDiv.append($docIframe);
                    break;
                } --%>
                case "png":
                case "PNG":
                case "jpg":
                case "JPG":
                case "JPEG":
                case "jpeg":
                case "gif":
                case "GIF":
                case "bmp":
                case "BMP":{
                    var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                    $img.attr("src",url);
                    $previewDiv.append($img);
                    break;
                }
                default:{
                    var $p = $("<p style='font-size:20px;color:red;'>");
                    $p.append(suffix + "不支持预览!");
                    $previewDiv.append($p);
                    return false;
                }
            }
		}





		//点检项目标准信息
		function setProjectRecords(projectRecords){
			var tbody = $("#checkingPlanRecordItem");
			tbody.empty();
			if(projectRecords && projectRecords.length>0){
				for(var i = 0;i<projectRecords.length;i++){
					var record = projectRecords[i];
					var tr = $("<tr>");
					//点检项目名称和id列
					var nameAndId = $("<td>"); 
					nameAndId.append("<input type='hidden' class='ids' value='" + record.id + "' />");
					nameAndId.append(record.name);
					
					tr.append(nameAndId);
					//标准列
					var standard = $("<td style='width:100px;' align='center'>");
					standard.append(record.standard);
					tr.append(standard);
					//方法列
					var method = $("<td style='width:100px;' align='center'>");
					method.append(record.method);
					tr.append(method);
					
					// 频次列
					var frequency = $("<td style='width:100px;' align='center'>");
					frequency.append(record.frequency);
					tr.append(frequency);
					
					//点检限值列
					var value =$("<td style='width:100px;'>");
					var recordvalue="0";
					if(!record.checkValue){
						recordvalue=record.checkValue;
					}
					value.append("<input type='number' class='checkValue' value='" + parseInt(record.checkValue) + "' style='width:100px;' onblur='showResult(this,"+i+")'/> ");
					tr.append(value);
					tbody.append(tr);
					//结果列
					var result = $("<td align='center'>");
					var combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;text-align=center'><option selected='selected'>OK</option><option>NG</option></select>");
					if(record.result){
						if(record.result=='NG'){
							combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;text-align=center' ><option>OK</option><option selected='selected'>NG</option></select>");;
						}
					}
					result.append(combobox);
					tr.append(result);
					//备注列
		            var note =$("<td align='center'>");
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

    //显示点检项目标准信息
    function queryProjectRecords(projectRecords){
        var tbody = $("#checkingPlanRecordItem");
        tbody.empty();
        if(projectRecords && projectRecords.length>0){
            for(var i = 0;i<projectRecords.length;i++){
                var record = projectRecords[i];
                var tr = $("<tr>");
                //点检项目名称和id列
                var nameAndId = $("<td>");
                nameAndId.append("<input type='hidden' class='ids' value='" + record.id + "' />");
                nameAndId.append(record.name);

                tr.append(nameAndId);
                //标准列
                var standard = $("<td style='width:100px;' align='center'>");
                standard.append(record.standard);
                tr.append(standard);
                //方法列
                var method = $("<td style='width:100px;' align='center'>");
                method.append(record.method);
                tr.append(method);

                // 频次列
                var frequency = $("<td style='width:100px;' align='center'>");
                frequency.append(record.frequency);
                tr.append(frequency);

                //点检限值列
                var value =$("<td style='width:100px;'>");
                var recordvalue="0";
                if(!record.checkValue){
                    recordvalue=record.checkValue;
                }
                value.append("<input type='number' class='checkValue' value='" + parseInt(record.checkValue) + "' style='width:100px;' onblur='showResult(this,"+i+")'/> ");
                tr.append(value);
                //tbody.append(tr);
                //结果列
                var result = $("<td align='center'>");

                result.append(record.result);
                tr.append(result);
                //备注列
                var note =$("<td align='center'>");
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


		//更新点检信息
		function updateDeviceCheck(){
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
	    	
	    	
	    	//获取点检备注结果数组
	          var noteObj=$(".noteValue");
	          var notes = "";
	          if(noteObj && noteObj.length>0){
	             for(var i = 0;i<noteObj.length;i++){
	                notes+=$(noteObj[i]).val()+" @";
	             }
	             notes =notes.substring(0,notes.length-2);
	          }
	          
	          var checkValueObj=$(".checkValue");
	      	var checkValue = "";
	      	if(checkValueObj && checkValueObj.length>0){
	      		for(var i = 0;i<checkValueObj.length;i++){
	      			checkValue+=$(checkValueObj[i]).val()+" ,";
	      		}
	      		checkValue =checkValue.substring(0,checkValue.length-1);
	      	} 
	      	
	    	//发送异步请求
	    	$.get("mcCheckingPlanRecord/deviceCheck.do",{
	    		id:$("#checkId").val(),
	    		/* deviceCode:$("#deviceCode").iTextbox("getValue"),
	    		deviceName:$("#deviceName").iTextbox("getValue"),
	    		unitType:$("#unitType").iTextbox("getValue"), */
	    		checkedDate:$("#checkCreateDate").val(),
	    		employeeName:$("#checkInformant").val(),
	    		employeeCode:$("#checkInformantCode").val(),
	    		picPath:urls,
	    		itemIds:ids,
	    		results:results,
	    		notes:notes,
	    		checkValue:checkValue
	    	},function(result){
	    		 $("#alertText").text("点检完成");
                 $("#alertDialog").modal();
				$('#updateCheckingPlanDialog').modal('hide');
				$("#showCheckingPlanTable").bootstrapTable("refresh", {
					url :"mcCheckingPlanRecord/queryAllCheckingPlanRecordByDeviceSiteCode.do",
					cache:false,	
					query : {
						deviceSiteCode :_deviceSiteCode
					},
				});
	    	});
		}
		
		//执行保养计划
		function updateDeviceMaintenance(){
			var status=$("#maintenanceStatus").val();
			if(status!="保养中"){
				 $("#alertText").text("该记录不是可完成记录!");
                 $("#alertDialog").modal();
                 return false;
			}
	    	//发送异步请求
	    	$.get("mcMaintenancePlanRecord/updateMaintenancePlanRecord.do",{
	    		id:$("#maintenanceId").val(),
	    		maintenancedDate:$("#maintenanceCreateDate").val(),
	    		confirmName:$("#maintenanceConfirmName").val(),
	    		confirmCode:$("#maintenanceConfirmCode").val(),
	    	},function(result){
	    		 $("#alertText").text("保养完成");
                 $("#alertDialog").modal();
				$('#maintenanceOrderDialog').modal('hide');
				$("#showMaintenancePlanRecordsTable").bootstrapTable("refresh", {
					url :"mcMaintenancePlanRecord/queryAllMaintenancePlanRecordsByDeviceSiteCode.do",
					cache:false,
					query : {
						deviceSiteCode :_deviceSiteCode
					}
				});
	    	});
		}
		//查询保养信息
		function setMaintenancePlanRecordsTable(id){
			ordId=id;
			$.get("mcMaintenancePlanRecord/queryMaintenancePlanRecordById.do",{id:id},function(result){
				//设备保养记录对象
				var device = result.device;
				$("#maintenanceDeviceCode").val(device.code);
				$("#maintenanceDeviceName").val(device.name);
				$("#maintenanceUnitType").val(device.unitType);
				
				$("#maintenanceId").val(result.id);
				$("#maintenanceDeviceType").val(result.maintenanceType);
				$("#maintenanceConfirmName").val(result.employeeName);
				$("#maintenanceConfirmCode").val(result.employeeCode);
				$("#maintenanceStatus").val(result.status);
				var date = new Date();
				var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
						+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
						+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
				$("#maintenanceCreateDate").val(value);
				
				showMaintenanceImages(result);
			});
			
			//保养人员信息
			$("#showMaintenanceStaffTable").bootstrapTable({
				url :"mcMintenanceUser/queryMaintenanceUserByRecordId.do",
				cache:false,
				height : 270,
				width: 900,
				//idField : 'id',
				 singleSelect : true,
		        clickToSelect : true,
				striped : true, //隔行换色
				queryParams : function(params) {
					var temp = {
							recordId:id
					};
					return temp;
				},
				/* responseHandler:function(data){
					return data.rows;
				}, */
				columns : [{ checkbox: true },{
					field : 'id',
					title : 'Id'
				}, 
				{
					field : 'code',
					align : 'center',
					title : '员工代码',
				}, {
					field : 'name',
					align : 'center',
					title : '员工名称'
				}, {
					field : 'orderType',
					align : 'center',
					title : '接单类型',
					/* formatter : function(value, row, index) {
						console.log(value);
						if (value=="SYSTEMGASSIGN") {
							return "系统派单";
						}else if(value=="ARTIFICIALGASSIGN"){
							return "人工派单";
						}else if(value=="ASSIST"){
							return "协助";
						}else{
							return "";
						}
					} */
				}, {
					field : 'dispatchDate',
					align : 'center',
					title : '派单时间',
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
						return "";
					}
				}, {
					field : 'receiptDate',
					align : 'center',
					title : '接收时间',
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
						return "";
					}
				}, {
					field : 'completeDate',
					align : 'center',
					title : '完成时间',
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
						return "";
					}
				}]
			});
			//保养项目列表
			$("#showMaintenanceProjectTable").bootstrapTable({
				url :"mcMaintenanceItem/queryMaintenanceItemById.do",
				cache:false,
				height : 270,
				//idField : 'id',
				 singleSelect : true,
	            clickToSelect : true,
				striped : true, //隔行换色
				queryParams : function(params) {
					var temp = {
							recordId:id
					};
					return temp;
				},
				/* responseHandler:function(data){
					return data.rows;
				}, */
				columns : [/* { checkbox: true }, */{
					field : 'id',
					title : 'Id'
				}, 
				{
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
					/* formatter : function(value, row, index) {
						if (value) {
							return strHtml = "<select class='ss'><option value='Item 1' selected='selected'>OK</option><option value='Item 2'>NG</option></select>";
						}
						return strHtml =     "<select class='ss'><option value='Item 1' >OK</option><option value='Item 2' selected='selected'>NG</option></select>";
					} */
					formatter:function(value,row,index){
						var div = $('<div>');
						var $result = $('<input type=checkbox name=result />');
						div.append($result);
						$result.attr('id',row.id);
						$result.attr('confirmDate',row.confirmDate);
						if(value){
							$result.attr('checked','checked');
						}else{
							$result.removeAttr('checked');
						}
						
						if(row.confirmDate){
							$result.attr('disabled',true);
						}
						return div.html();
					}
				}],
				 onLoadSuccess:function(){
                    	$('input[type=checkbox]').click(function(){
                    		var confirmDate = $(this).attr('confirmDate');
                    		var isChecked = $(this)[0].checked;
                    		var maintenanceItemId = $(this).attr('id');
                    		//没有确认，可以随便修改结果
                    		if(!confirmDate){
                    			if(isChecked){
                            			$.get('mcMaintenanceItem/setMaintenanceDate.do',{id:maintenanceItemId},function(){
                            				//$('#spotCheck').iDatagrid('reload');
                            			});
                            		}else{
                            			$.get('mcMaintenanceItem/setMaintenanceDate2Null.do',{id:maintenanceItemId},function(){
                            				//$('#spotCheck').iDatagrid('reload');
                            			});
                            		}
                            		return ;
                    		}
                    	});
                    }
			});
			
			//备件列表
			$("#showMaintenanceSparepartRecordTable").bootstrapTable({
				url :"maintenanceSparepart/queryMaintenanceSparepartByRecordId.do",
				cache:false,
				height : 270,
				//idField : 'id',
				 singleSelect : true,
	            clickToSelect : true,
				striped : true, //隔行换色
				queryParams : function(params) {
					var temp = {
							recordId:id
					};
					return temp;
				},
				/* responseHandler:function(data){
					return data.rows;
				}, */
				columns : [{ checkbox: true },{
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
				} */, {
					field : 'typeName',
					align : 'center',
					title : '备件类别',
					formatter : function(value, row, index) {
						if (row.projectType) {
								return row.projectType.name;
						}
						return "";
					}
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
					field : 'batchNumber',
					align : 'center',
					title : '批号',
				}, {
					field : 'graphNumber',
					align : 'center',
					title : '图号',
				}, {
					field : 'measurementUnit',
					align : 'center',
					title : '计量单位',
				}, {
					field : 'count',
					align : 'center',
					title : '数量'
				}, {
					field : 'note',
					align : 'center',
					title : '备注'
				}, {
					field : 'useDate',
					align : 'center',
					title : '耗用时间',
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
						return "";
					}
				}]
			});
			
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
			$('#showMaintenanceSparepartRecordTable').bootstrapTable('hideColumn', 'id');
			$("#showMaintenanceSparepartRecordTable").on('click-row.bs.table',
					function(e, row, element) { //splitLostTime confirmLostTime
						id = row.id;
						$('.success').removeClass('success'); //去除之前选中的行的，选中样式
						$(element).addClass('success'); //添加当前选中的 success样式用于区别
					});
		}
		
		//显示设备分布图
		function showMaintenanceImages(row){
			$.get("mcMaintenancePlanRecord/queryMaintenanceImages.do",{
				deviceId:row.device.id,
				docTypeCode:row.maintenanceTypeCode
			},function(result){
				fillTab0(result);
			});
		}
		//填充tab0，tab0为显示设备分布图的div
		function fillTab0(raletedDocuments){
			var tab0 = $("#MaintenancePicDiv");
			tab0.empty();
			if(raletedDocuments && raletedDocuments.length>0){
				for(var i = 0;i<raletedDocuments.length;i++){
					var $img = $("<img style='height:200px;width:200px;margin:5px;'>");
					$img.attr("src",raletedDocuments[i].url);
					$img.attr("alt",raletedDocuments[i].name);
					tab0.append($img);
				}
			}
		}
		//确认弹出框
		function showConfirm(p){
			if(p=="repairConfirm"){
				$('#showRepairConfirm').modal('show');
			}else if(p=="MaintenanceConfirm"){
				$('#showMaintenanceConfirm').modal('show');
			}else if(p=="repairComplete"){
				$('#updateDeviceRepairOrderDialog').modal('hide');
				$('#showRepairComplete').modal('show');
			}else if(p=="repairUpdate"){
				$('#updateDeviceRepairOrderDialog').modal('show');
			}
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
						})
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
			for(var i = 0;i<btns.length;i++){
				var btn = $(btns[i]);
				var textOnButton = btn.val();
				if(textOnButton === typeid){
					btn.removeClass("btn btn-default");
					btn.addClass("btn btn-primary");
					//$("#lostTimeType").selectpicker('val',data.pressLightType.code);
    			$("#pressLight").selectpicker('val',typeid);
    			$("#pressLightId").val(typeid);
				}
			}
		}
		
		//故障弹出框
		function showng(){
			$("#lostReason").empty();
			$("#lostType button").removeClass("btn btn-primary");
			$("#lostType button").addClass("btn btn-default");
				$('#showNgdialog').modal('show');
		}
		//确认
		function Confirm(){
				$('#showNgdialog').modal('show');
		}
		//取消
		function Cancel(){
				$('#showNgdialog').modal('hide');
		}
		/*  function deleteImg(id) {
		        var photoIds=idList.split(",");
		        console.log(id);
		        console.log("前："+photoIds);
		       	photoIds.splice(id,1,"");
		        console.log("后："+photoIds);
		        
		        var plist="";
		        for(var i = 0; i < photoIds.length; i++){
		        		if(!plist){
		        			plist=photoIds[i];
		            	}else{
		            		plist+=","+photoIds[i];
		            	}
		        }
		       idList=plist;
				$("#photoIds").val(photoIds)
		        _this.parents(".imageDiv").remove();
				$(".addImages").show();
		    }; */
</script>

<style type="text/css">
#dialog-layer{
	position: fixed;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	overflow: hidden;
	z-index: 2000;
	display:none;
}

.updateButton{
	width:80px;
	height:80px;
	padding-top:5px;
	border:2px solid darkgray;
	border-radius:16px;
	margin-left:20px;
	float: right;
	margin-top:8px;
	display:inline-block;
	cursor:pointer;
	text-align:center;
}
fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver;border-radius:8px}

legend{padding:.5em;border:0;width:auto;margin-bottom:-10px}

.imageDiv {
	display:inline-block;
	width:180px;
	height:180px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px
}
.cover {
	position:absolute;
	z-index:1;
	top:0;
	left:0;
	width:180px;
	height:180px;
	background-color:rgba(0,0,0,.3);
	display:none;
	line-height:125px;
	text-align:center;
	cursor:pointer;
}
.cover .delbtn {
	color:red;
	font-size:20px;
}
.imageDiv:hover .cover {
	display:block;
}


.imgDirection{
	height:100%;width:5%;float:left;cursor:pointer;color:#80848E;
	font-size:30px;
	text-align:center;
	margin-top:150px;
}


.addImages {
	display:inline-block;
	width:180px;
	height:180px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px;
}
.text-detail {
	margin-top:40px;
	text-align:center;
}
.text-detail span {
	font-size:40px;
}
.file {
	position:absolute;
	top:0;
	left:0;
	width:180px;
	height:180px;
	opacity:0;
}
.input {
}

*{margin:0px;padding:0px;}
.tabbox{}
.tabbox ul{list-style:none;display:table;}
.tabbox ul li{width:100px;height:84px;line-height:70px;padding-left:20px;margin-top:-20px;border:1px solid #00000E;cursor:pointer;background-color:#999999;}
#ngselete  ul  {margin-top:10px;}
#ngselete  ul  li{width:240px;height:40px;margin-top:-0px;margin-left:-20px;border:0px solid #00000E;cursor:pointer;background-color:#FFFFFF;}
.tabbox ul li.active{background-color:#FFFFFF;font-weight:bold;border:0px solid #00000E;}
.tabbox .content{}
.tabbox .content>div{display:none;}
.tabbox .content>div.active{display:block;margin-top: -30px}
.mtabbox{}
.mtabbox ul{list-style:none;display:table;}
.mtabbox ul li{width:100px;height:74px;line-height:70px;padding-left:20px;margin-top:-20px;border:1px solid #00000E;cursor:pointer;background-color:#999999;}
.mtabbox ul li.active{background-color:#FFFFFF;font-weight:bold;border:0px solid #00000E;}
.mtabbox .content{}
.mtabbox .content>div{display:none;}
.mtabbox .content>div.active{display:block;margin-top: -30px}
</style>


<div class="tyPanelSize" >
	<div class="tytitle" style="height: 7%; text-align: center">
		<button class='btn btn-success' id="lostTimeDeviceSiteCode" disabled="disabled" style="float:left;margin: 1%;background-color: "></button>
		<div class="tytitle_t">
			<!-- <img style="float: left; margin-top: 7px;"
				src="mc/assets/css/images/tb.png"> -->
				<span class="fa fa-user-circle fa-2x" aria-hidden="true" style="float: left; margin-top: 10px;margin-right: 10px"></span>
				<span style="float: left; margin-top: 17px;">设备管理</span>
		</div>
		
	</div>
	<div class="mc_workmanage_center"
		style="height: 73%;">

		<div style="text-align: center">

			<!-- Nav tabs -->
			<ul id="myTab" class="nav nav-tabs" role="tablist"
				style="overflow: hidden; display: inline-block;">
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="SPOTINSPECTION"><a href='#home' aria-controls="untreated"
					role='tab' data-toggle='tab'>设备点检</a></li>
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="MAINTENANCEITEM"><a href='#home' aria-controls="untreated"
					role='tab' data-toggle='tab'>设备报修/维修</a></li>
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="MAINTAIN"><a href='#home'
					aria-controls="untreated" role='tab' data-toggle='tab'>设备保养</a></li>
				<li role='presentation' onclick='lostTimeTabClick(this)'
					dataid="DOC"><a href='#home'
					aria-controls="untreated" role='tab' data-toggle='tab'>文档资料</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content"
				style="border-top: 1px solid darkgray; margin-top: -6px;">
				<div role="tabpanel" class="tab-pane active" id="home"
					style="width: 96%; margin-left: 2%;">
					<div style='width: 100%; float: right; margin-top: 0px;'>
						<div>
							<span style="float: left; margin-top: 17px; font-size: 20px;font-weight: bold;"  id="title"></span>
						</div>
						<div class="CheckingPlanTable">
							<table id='showCheckingPlanTable'></table>
						</div>
						<div class="DeviceRepairOrderTable">
							<table id='showDeviceRepairOrderTable'></table>
						</div>
						<div class="MaintenancePlanRecordsTable">
							<table id='showMaintenancePlanRecordsTable'></table>
						</div>
						<div class="DocTable">
							<table id='showDocTable'></table>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- 点检 -->
	<div class="mc_device_spotinspection"
		style="height: 20%;margin-left: 2%; margin-right: 2%;overflow:hidden;display:none;">
		<div>
			<div  onclick="addLostTime('CheckView')"
			 class="container-fluid functionButton"
				id="confirmLostTime">
				<h6></h6>
				<span class="fa fa-search-plus" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">查看</h4><!-- onclick="updateLostTime('comfirmdlg')" -->
			</div>
			<div onclick="addLostTime('deviceCheck')" id="addLight" style="float: right;margin-right: 0;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">点检</h4>
			</div>
		</div>
	</div>
	<!-- 报修/维修 -->
	<div class="mc_device_maintenanceitem"
		style="height: 20%;margin-left: 2%; margin-right: 2%;overflow:hidden;display:none;">
		<div>
			<div 
			 	onclick="addLostTime('repairView')" class="container-fluid functionButton"
				id="View" >
				<h6></h6>
				<span class="fa fa-search-plus" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">查看</h4><!-- onclick="updateLostTime('comfirmdlg')" -->
			</div>
			<div 
			 	onclick="showConfirm('repairConfirm')" class="container-fluid functionButton"
				id="repairConfirm" >
				<h6></h6>
				<span class="fa fa-check-square-o" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">确认</h4><!-- onclick="showSplitDialog()" -->
			</div>


			<div onclick="addLostTime('repairAdd')" id="repairAdd" style="float: right;margin-right: 0;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-plus-circle" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">新增</h4>
			</div>
			
			<div onclick="addLostTime('repairUpdate')" id="repairUpdate" style="float: right;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">维修</h4>
			</div>
		</div>
	</div>
	<!-- 保养 -->
	<div class="mc_device_maintain"
		style="height: 20%;margin-left: 2%; margin-right: 2%;overflow:hidden;display:none;">
		<div>
			<div onclick="addLostTime('MaintenanceView')"
			 class="container-fluid functionButton"
				id="confirmLostTime">
				<h6></h6>
				<span class="fa fa-search-plus" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">查看</h4><!-- onclick="updateLostTime('comfirmdlg')" -->
			</div>
			<div onclick="showConfirm('MaintenanceConfirm')"
			 class="container-fluid functionButton"
				id="splitLostTime">
				<h6></h6>
				<span class="fa fa-check-square-o" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">确认</h4><!-- onclick="showSplitDialog()" -->
			</div>


			<div onclick="addLostTime('Maintenance')" id="addLight" style="float: right;margin-right: 0;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-wrench" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">保养</h4>
			</div>
		</div>
	</div>
	<!-- 相关文档 -->
	<div class="mc_device_doc"
		style="height: 20%;margin-left: 2%; margin-right: 2%;overflow:hidden;display:none;">
		<div>
			<div onclick="addLostTime('DocView')" id="addLight" style="float: right;margin-right: 0;"
				class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-search-plus" aria-hidden="true"
					style=" margin: 0 auto"></span>
				<h4 style="text-align: center">查看</h4>
			</div>
		</div>
	</div>
</div>

<!-- 报修单新增维修人员 -->
<div class="modal fade" id="addMaintenanceStaff" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增维修人员</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">员工姓名</span> <select id="StaffName" name="StaffName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><br>
					<br>
					<br>
					<br> <span style="font-size: 20px;">员工代码</span> <input
									id="StaffCode" name="StaffCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /><br>
					<br>
					<br>
					<br>
							<span style="font-size: 20px;">派单时间</span><input type="text" name="StaffcreateDate"
									id="StaffcreateDate" style="width: 220px; display: inline-block; text-align: left;"
									class="form-control"><br>
					<br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addMaintenanceStaff()">新增</span>
					<span  class='btn btn-default'
						onclick="RepairOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 新增保养人员 -->
<div class="modal fade" id="addMaintenance" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增保养人员</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">员工姓名</span> <select id="maintenanceInformantName" name="maintenanceInformantName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><br>
					<br>
					<br>
					<br> <span style="font-size: 20px;">员工代码</span> <input
									id="maintenanceInformantCode" name="maintenanceInformantCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /><br>
					<br>
					<br>
					<br>
							<span style="font-size: 20px;">派单类型</span><select class="selectpicker" name="orderType" id="orderType">
									<option value="人工派单">人工派单</option>
									<option value="协助">协助</option>
								</select><br>
					<br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addMaintenance()">新增</span>
					<span  class='btn btn-default'
						onclick="maintenanceOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 报修单新增故障原因 -->
<div class="modal fade" id="addNGMaintain" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增故障原因</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">故障类型</span> <select id="NGMaintainName" name="NGMaintainName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><br>
					<br>
					<br>
					<br> <span style="font-size: 20px;">故障代码</span> <!-- <input
									id="NGMaintainCode" name="NGMaintainCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /> -->
									<select
								id="deviceproject" name="deviceproject"
								style="width: 230px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block;"
								equipID="" class="selectpicker" data-live-search="true">
							</select>
							
									<input id="NGMaintainId" name="NGMaintainId" type="hidden"
									class="form-control" /><br>
					<br>
					<br>
					<br>
							<span style="font-size: 20px;">说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明</span><textarea   name="NGMaintainNote" rows="4" cols="50"
									data-toggle="topjui-textarea" data-options="required:true" id="NGMaintainNote" style="resize:none" ></textarea><br>
					<br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addNGMaintain()">新增</span>
					<span  class='btn btn-default'
						onclick="RepairOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 报修单新增维修项目 -->
<div class="modal fade" id="addMaintainProject" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增维修项目</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 485px; background-color: white; padding: 20px">
			<div >
				<form
					style="width: 380px; margin-top: -10px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">项目名称</span> <select id="MaintainProjectName" name="MaintainProjectName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select>
								<input id="MaintainProjectTypeId" name="MaintainProjectTypeId" type="hidden"
									class="form-control" /><br>
					<br> <span style="font-size: 20px;">项目代码</span> <input
									id="MaintainProjectCode" name="MaintainProjectCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /><br>
					<br> <span style="font-size: 20px;">类别代码</span> <input
									id="MaintainProjectTypeCode" name="MaintainProjectTypeCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /><br>
					<br> <span style="font-size: 20px;">类别名称</span> <input
									id="MaintainProjectTypeName" name="MaintainProjectTypeName" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" /><br>
					<br>
							<span style="font-size: 20px;">说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明</span><textarea   name="MaintainProjectNote" rows="3" cols="50"
									data-toggle="topjui-textarea" data-options="required:true" id="MaintainProjectNote" style="resize:none" ></textarea><br>
							<span style="font-size: 20px;">方&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;法</span><textarea   name="MaintainProjectProcessingMethod" rows="3" cols="50"
									data-toggle="topjui-textarea" data-options="required:true" id="MaintainProjectProcessingMethod" style="resize:none" ></textarea><br>
							<span style="font-size: 20px;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</span><textarea   name="MaintainProjectRemark" rows="3" cols="50"
									data-toggle="topjui-textarea" data-options="required:true" id="MaintainProjectRemark" style="resize:none" ></textarea><br>
					<br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addMaintainProject()">新增</span>
					<span  class='btn btn-default'
						onclick="RepairOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 报修单新增备件信息 -->
<div class="modal fade" id="addSparepart" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增备件信息</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 10px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">耗用时间</span><input type="text" name="SparepartcreateDate"
									id="SparepartcreateDate" style="width: 220px; display: inline-block; text-align: left;"
									class="form-control"><br>
					<br>
					<span style="font-size: 20px;">备件名称</span> <select id="SparepartName" name="SparepartName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><br>
					<br>
					<br> <span style="font-size: 20px;">备件代码</span> <input
									id="SparepartCode" name="SparepartCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" />
									
									<input	id="SparepartId" name="SparepartId" type="hidden" class="form-control"/>
									<br>
					<br>
					<br> <span style="font-size: 20px;">数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量</span> <input
									id="SparepartNum" name="SparepartNum" type="number" value="1"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" /><br>
					<br>
							<span style="font-size: 20px;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</span><textarea   name="SparepartNote" rows="3" cols="50"
									data-toggle="topjui-textarea" data-options="required:true" id="SparepartNote" style="resize:none" ></textarea><br>
					<br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addSparepart()">新增</span>
					<span  class='btn btn-default'
						onclick="RepairOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 保养记录新增备件信息 -->
<div class="modal fade" id="addMaintenanceSparepart" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<div
				style="height:40px; width: 600px; overflow: hidden;">
				<span style="font-size: 20px;">新增备件信息</span>
			</div>
			</div>
	<div class="modal-body">
		<div
			style="width: 500px; height: 385px; background-color: white; padding: 20px">
			
			<div >
				<form
					style="width: 380px; margin-top: 10px; margin-left: 20px; float: left;">
					<span style="font-size: 20px;">备件名称</span> <select id="MaintenanceSparepartName" name="MaintenanceSparepartName" style="width: 60px; display: inline-block;" equipID=""
									class="selectpicker" data-live-search="true">
								</select><br>
					<br>
					<br> <span style="font-size: 20px;">备件代码</span> <input
									id="MaintenanceSparepartCode" name="MaintenanceSparepartCode" type="text"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" readonly="readonly" />
									
									<input	id="MaintenanceSparepartId" name="MaintenanceSparepartId" type="hidden" class="form-control"/>
									<br>
					<br>
					<br> <span style="font-size: 20px;">数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量</span> <input
									id="MaintenanceSparepartNum" name="MaintenanceSparepartNum" type="number" value="1"
									style="width: 220px; display: inline-block; text-align: left;"
									class="form-control" /><br>
				</form>			
			</div>
		</div>
	</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addMaintenanceSparepart()">新增</span>
					<span  class='btn btn-default'
						onclick="maintenanceOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 维修确认框 -->
<div class="modal fade" id="showRepairConfirm">
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
				<p>您确认要确认吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="addLostTime('repairConfirm')" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 维修完成框 -->
<div class="modal fade" id="showRepairComplete">
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
				<p>您确认完成吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="showConfirm('repairUpdate')">取消</button>
				<a onclick="addLostTime('repairComplete')" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 保养确认框 -->
<div class="modal fade" id="showMaintenanceConfirm">
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
				<p>您确认要确认吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="addLostTime('MaintenanceConfirm')" class="btn btn-success"
					data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>

<!-- 设备报修窗口 -->
<div class="modal fade" id="addDeviceRepairOrderDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">添加设备报修单</h4>
			</div>
			<div class="modal-body" style="height: 500px">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">设备代码</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceCode" id="deviceCode" class="form-control" style="width: 170px" readonly="readonly"/>
								<input type="hidden" name="deviceId" id="deviceId" />
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">设备名称</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceName" id="deviceName" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">规格型号</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="unitType" id="unitType" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">报修时间</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="createDate" class="form-control"
									id="createDate" style="width: 170px">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">设备类别</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceType" class="form-control"
									id="deviceType" style="width: 170px" readonly="readonly">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">报修人员</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="Informant" class="form-control"
									id="Informant" style="width: 170px" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">故障类型</label>
							<div style="float: left;margin-left: 10px">
								<select class="selectpicker" name="pressLight"
									id="pressLight" style="width: 170px">
								</select>
								<input type="hidden" name="pressLightId" id="pressLightId" />
								
							</div>
							<!-- <div style="float: left;margin-left: 10px">
								<div><span onclick='showng()' class='fa fa-th-list' aria-hidden='true' style='float: left;font-size: 30px;'></span>
								</div>
							</div> -->
						</div>
					</div>
					<div id="photo">
						<div class="form-group">
							<div style="float: left;margin-left: 50px;margin-top: 10px">
								<label style="float: left;">故障描述</label>
								<div style="float: left;margin-left: 10px">
									<textarea   name="ngDescription" rows="4" cols="88"
									data-toggle="topjui-textarea" data-options="required:true" id="ngDescription" style="resize:none" ></textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
						<form method="post" action="relatedDoc/uploadimg.do" id="passForm" enctype="multipart/form-data;charset=utf-8" multipart="">
							    <div id="Pic_pass">
							        <!-- <p style="font-size: 20px;font-weight: bold;margin-left: 50px">请上传照片 </p> -->
							        <!-- <p><span style="color: red">注：每张照片大写不可超过4M，且最多可以传十张</span></p> -->
							        <div class="picDiv">
							            <div class="addImages" id="addImages" style="margin-left: 30px">
							                
							                <input type="file" class="file" id="fileInput" accept="image/png, image/jpeg, image/gif, image/jpg">
							                <div class="text-detail">
							                    <span>+</span>
							                    <p>点击上传图片</p>
							                </div>
							                <input id="photoIds" hidden="true"/>
							            </div>
							        </div>
							    </div>
							</form>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="addDeviceRepair()">保存</span>
					<span  class='btn btn-default'
						onclick="modelHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 维修界面 -->
<div class="modal fade" id="updateDeviceRepairOrderDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 1000px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">编辑设备报修单</h4>
			</div>
			<div class="modal-body" style="height: 550px">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">设备代码</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateDeviceCode" id="updateDeviceCode" class="form-control" style="width: 170px" readonly="readonly"/>
								<input type="hidden" name="orderId" id="orderId" />
								<input type="hidden" name="updateDeviceId" id="updateDeviceId" />
								<input type="hidden" name="updateStatus" id="updateStatus" />
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">设备名称</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateDeviceName" id="updateDeviceName" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">规格型号</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateUnitType" id="updateUnitType" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">报修时间</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateCreateDate" class="form-control"
									id="updateCreateDate" style="width: 170px">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">设备类别</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateDeviceType" class="form-control"
									id="updateDeviceType" style="width: 170px" readonly="readonly">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">报修人员</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="updateInformant" class="form-control"
									id="updateInformant" style="width: 170px" readonly="readonly">
							</div>
						</div>
					</div>
					<div id="updatePhoto">
						<div class="tabbox" style="width: 960px;height: 421px">
						　　<ul style="float: left;">
						　　　　<li class="active">报修内容</li>
						　　　　<li>维修人员</li>
						　　　　<li>故障原因</li>
						　　　　<li>维修项目</li>
						　　　　<li>备件信息</li>
						　　</ul>
						
						<div class="content" style="float: left;width: 850px;height: 300px;">
						　　<div class="active">
						　　　　<div style="margin-left: 20px;">
									<div class="form-group">
													<div style="float: left;margin-left: 10px">
														<label style="float: left;">故障类型</label>
														<div id="ngselete" style="float: left;margin-left: 10px">
															<select class="selectpicker" name="updatePressLight"
																id="updatePressLight" style="width: 170px">
															</select>
															<input type="hidden" name="updatePressLightId" id="updatePressLightId" />
															
														</div>
													</div>
													<div style="float: left;margin: 10px">
														<label style="float: left;">故障描述</label>
														<div style="float: left;margin-left: 10px">
															<textarea   name="updateNGDescription" rows="4" cols="88"
															data-toggle="topjui-textarea" data-options="required:true" id="updateNGDescription" style="resize:none" ></textarea>
														</div>
													</div>
												</div>
												 <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic">
													<!-- datagrid表格 -->
													<div class="updatePicDiv" id="updatePicDiv" style="margin-top: -20px">
						      					  </div>
												</div>
								</div>
						　　</div>
						　　<div style="margin-left: 20px;">
								<div class="StaffTable" style="width: 800px;margin-top: 20px">
									<table id='showStaffTable'></table>
									
									<div onclick="removeRecord('staff')" id="removeMaintenanceStaffView" style="float: right;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('staff')" id="addMaintenanceStaffView" style="float: right;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
								</div>
							</div>
						　　<div style="margin-left: 20px;">
								<div class="NGRecordTable" style="width: 800px;margin-top: 20px">
									<table id='showNGRecordTable'></table>
								</div>
								
								<div onclick="removeRecord('ngRecord')" id="removeNGMaintainView" style="float: right;margin-right: 30px;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('ngRecord')" id="addNGMaintainView" style="float: right;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
									
							</div>
						　　<div style="margin-left: 20px;">
								<div class="MaintainProjectTable" style="width: 800px;margin-top: 20px">
									<table id='showMaintainProjectTable'></table>
								</div>
								
								<div onclick="removeRecord('maintainProject')" id="removeMaintainProjectView" style="float: right;margin-right: 30px;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('maintainProject')" id="addMaintainProjectView" style="float: right;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
								
							</div>
						　　<div style="margin-left: 20px;">
								<div class="SparepartRecordTable" style="width: 800px;margin-top: 20px">
									<table id='showSparepartRecordTable'></table>
								</div>
								
								<div onclick="removeRecord('sparepartRecord')" id="removeSparepartView" style="float: right;margin-right: 30px;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('sparepartRecord')" id="addSparepartView" style="float: right;margin-top: 20px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
								
							</div>
						　　</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					<span  class='btn btn-default'
						onclick="showConfirm('repairComplete')">完成</span>
					<span  class='btn btn-default'
						onclick="updateDeviceRepair()">更新</span>
					<span  class='btn btn-default'
						onclick="modelHide()">退出</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 点检 界面-->
<div class="modal fade" id="updateCheckingPlanDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 1400px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">设备点检</h4>
			</div>
			<div class="modal-body" style="height: 500px">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">设备代码</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkDeviceCode" id="checkDeviceCode" class="form-control" style="width: 170px" readonly="readonly"/>
								<input type="hidden" name="checkId" id="checkId" />
								<input type="hidden" name="checkInformantCode" id="checkInformantCode" />
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">设备名称</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkDeviceName" id="checkDeviceName" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">规格型号</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkUnitType" id="checkUnitType" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">点检时间</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkCreateDate" class="form-control"
									id="checkCreateDate" style="width: 170px">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">点检人员</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkInformant" class="form-control"
									id="checkInformant" style="width: 170px" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="" style="width: 400px;float: left;">
							<div id="docs" style="border-style: dotted; border-radius: 15px;height:350px;overflow: hidden;
										border-color: #4F5157;position: relative">
								<span style="cursor:pointer;position: absolute; top: 10px;right:0;"
									  class="fa fa-window-maximize" onClick="showFullScreen()"></span>
								<div class="fa fa-chevron-left imgDirection" id="showPrevious"></div>
								<div style="height:100%;width:90%;float:left;" id="deviceCheckImg">
									<%--<img  alt="设备点检图片" style="width:98%;height:94%;margin-top:20px;"/>--%>
								</div>
								<div class="fa fa-chevron-right imgDirection" id="showNext"></div>

							</div>
						</div>
						<div class="" style="width: 800px;float: left;">
							<div id="items"  style="border-style: dotted; border-radius: 10px;height:350px;
							margin-left:20px;">
								<table id="checkingPlanRecordItemTb" border="1" style="width:98%;margin:5px auto;">
									<thead>
										<tr>
											<td align="center">点检项目</td>
											<td align="center">标准</td>
											<td align="center">方法</td>
											<td align="center">频次</td>
											<td align="center">点检值</td>
											<td align="center">结果</td>
											<td align="center">备注</td>
										</tr>
									</thead>
									<tbody id="checkingPlanRecordItem"></tbody>
								</table>
							</div>
						</div>
					</div>
					<div>
								<div  style="height:30px;color:red;font-size:20px;width: 900px">
								在设备运行和进行设备清洁时有任何异常情况马上报告给主管或维护人员 
							</div>
					</div>
				</form>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					<span  class='btn btn-default' id="checksave"
						onclick="updateDeviceCheck()">完成</span>
					<span  class='btn btn-default'
						onclick="modelHide()">退出</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 保养界面 -->
<div class="modal fade" id="maintenanceOrderDialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 1000px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">设备保养</h4>
			</div>
			<div class="modal-body" style="height: 500px">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">设备代码</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceDeviceCode" id="maintenanceDeviceCode" class="form-control" style="width: 170px" readonly="readonly"/>
								<input type="hidden" name="maintenanceId" id="maintenanceId" />
								<input type="hidden" name="updateDeviceId" id="updateDeviceId" />
								<input type="hidden" name="maintenanceConfirmCode" id="maintenanceConfirmCode" />
								<input type="hidden" name="maintenanceStatus" id="maintenanceStatus" />
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">设备名称</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceDeviceName" id="maintenanceDeviceName" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<label style="float: left;">规格型号</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceUnitType" id="maintenanceUnitType" class="form-control" style="width: 170px" readonly="readonly"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<label style="float: left;">保养时间</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceCreateDate" class="form-control"
									id="maintenanceCreateDate" style="width: 170px">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">保养类别</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceDeviceType" class="form-control"
									id="maintenanceDeviceType" style="width: 170px" readonly="readonly">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<label style="float: left;">保养人员</label>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="maintenanceConfirmName" class="form-control"
									id="maintenanceConfirmName" style="width: 170px" readonly="readonly">
							</div>
						</div>
					</div>
					<div id="maintenancePhoto">
						<div class="mtabbox" style="width: 790px;height: 371px">
						　　<ul style="float: left;">
						　　　　<li class="active">设备分布图</li>
						　　　　<li>保养人员</li>
						　　　　<li>保养项目</li>
						　　　　<li>备件信息</li>
						　　</ul>
						
						<div class="content" style="float: left;width: 689px;height: 300px;">
						　　<div class="active">
						　　　　<div style="margin-left: 20px;">
												 <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic" style="margin-top: 20px">
													<!-- datagrid表格 -->
													<div class="updatePicDiv" id="MaintenancePicDiv" style="margin-top: -20px;width: 300px;height: 300px;">
						      					  </div>
												</div>
								</div>
						　　</div>
						　　<div style="margin-left: 20px;">
								<div class="maintenanceStaffTable" style="width: 800px;margin-top: 20px">
									<table id='showMaintenanceStaffTable'></table>
									
									<div onclick="removeRecord('maintenanceStaff')" id="removeStaffMaintenanceView" style="float: right;"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('maintenanceStaff')" id="addStaffMaintenanceView" style="float: right;"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
								</div>
							</div>
						　　<div style="margin-left: 20px;">
								<div class="maintenanceProjectTable" style="width: 800px;margin-top: 20px">
									<table id='showMaintenanceProjectTable'></table>
								</div>
								
								<!-- <div onclick="removeRecord('maintenanceProject')" id="removeMaintenanceProjectView" style="float: right;margin-right: -131px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-save" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">保存</h4>
									</div> -->
									<!-- <div onclick="addRecord('maintenanceProject')" id="addMaintenanceProjectView" style="float: right;margin-right: -31px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div> -->
								
							</div>
						　　<div style="margin-left: 20px;">
								<div class="maintenanceSparepartRecordTable" style="width: 800px;margin-top: 20px">
									<table id='showMaintenanceSparepartRecordTable'></table>
								</div>
								
								<div onclick="removeRecord('maintenanceSparepart')" id="removeMaintenanceSparepartView" style="float: right;margin-right: -131px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-times-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">删除</h4>
									</div>
									<div onclick="addRecord('maintenanceSparepart')" id="addMaintenanceSparepartView" style="float: right;margin-right: -31px"
										class="container-fluid updateButton">
										<h6></h6>
										<span class="fa fa-plus-circle" aria-hidden="true"
											style="font-size: 30px; margin-top: -5px"></span>
										<h4 style="text-align: center">新增</h4>
									</div>
								
							</div>
						　　</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					<span  class='btn btn-default'
						onclick="updateDeviceMaintenance()">完成</span>
					<span  class='btn btn-default'
						onclick="modelHide()">退出</span>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="showNgdialog" role="dialog"
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
							<div style="margin-top: 60px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">故障类型:</label>
							</div>
							<div style="margin-top: 80px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">故障原因:</label>
							</div>
						</div>
						<div class="col-sm-9" style="float: left;width: 700px">
							<div id="lostType" style="display: inline;float:left;margin-top: 30px" ></div>
							<div id="lostReason" style="display: inline;float:left;margin-top: 5px;" ></div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span  class="btn btn-default" style="margin-right: 20px"
						onclick="Cancel()">确认</span>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="showDoclog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="z-index:10000">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-body" style="height: 450px">
				<img id="DocImg" alt="" src="" style="width: 100%;height: 100%">
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 30px;">
					<span  class="btn btn-default" style="margin-right: 20px"
						onclick="modelHide()">退出</span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 全屏预览 -->
<div id="dialog-layer">
	<div style="font-size:50px;color:#A60000;z-index:3000;position:fixed;top:30px;right:20px;cursor:pointer;"
		 onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
		 onmouseout="$(this).css('font-size','50px')" >×</div>
	<div style="height:100%;width:100%;" id="fullScreenDiv">

	</div>
</div>
<%@ include file="end.jsp"%>
