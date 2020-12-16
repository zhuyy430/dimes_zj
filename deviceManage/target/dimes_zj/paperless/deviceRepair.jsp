<%@page import="com.digitzones.model.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee) session.getAttribute("employee");
	/* if (employee == null) {
		response.sendRedirect(basePath + "paperless/homePage.jsp");
		return;
	} */
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css">
<link rel="stylesheet" type="text/css" href="paperless/css/bootstrap-select.min.css">
<script type="text/javascript" src="paperless/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="../mc/assets/css/bootstrap-timepicker.min.css"> 
<script type="text/javascript"
	src="../mc/assets/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
 <script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	var employee="<%=employee%>"
   		//存储设备编码
   		var deviceCode = '';
    	$(function(){
    		var param = "<%=request.getParameter("param")%>";
    		<%if(employee==null){
    		%>
    		if(param=="dm"){
    		}else{
    			window.location.href="paperless/homePage.jsp";
    		}
    		<%}%>
    		deviceCode = parent.$("#deviceCode").val();
    		var time = new Date();
    		$("#time").text(
    				time.getFullYear() + "-" + (time.getMonth()+1) + "-"
    						+ time.getDate());
    		$("#startDate").flatpickr();
    		$("#endDate").flatpickr();
    		
    	});
    	flatpickr.localize(flatpickr.l10ns.zh);
    	function timestampToTime(timestamp) {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            var Y = date.getFullYear() + '-';
            var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            var D = date.getDate() + ' ';
            var h = date.getHours() + ':';
            var m = date.getMinutes() + ':';
            var s = date.getSeconds();
            return Y+M+D;
        }
    	//退出
    	function logout() {
    		$.iMessager.confirm('确认对话框', '您想要退出该系统吗？', function(r) {
    			if (r) {
    				$.get("paperlessUser/logout.do", function(result) {
    					if (result.success) {
    						window.location.href = "paperless/homePage.jsp";
    					} else {
    						$.iMessager.alert("警告", "退出失败，系统内部错误!");
    					}
    				});
    			}
    		});
    	}
    /*删除功能*/
 	function delImg(obj) {
	        var _this = $(obj);
	        var id=_this.siblings(".input").val();
	        var photoIds=idList.split(",");
	        console.log(id);
	        console.log("前："+photoIds);
	       	photoIds.splice(id,1,"");
	        console.log("后："+photoIds);
	        imageList.splice(id,1,id)
	        var plist="";
	        for(var i = 0; i < photoIds.length; i++){
	        	console.log(photoIds[i]);
	        		if(i==0){
	        			plist=photoIds[i];
            	}else{
            		plist+=","+photoIds[i];
            	}
	        console.log(plist);
	        }
	       idList=plist;
	        console.log("后："+idList);
			$("#photoIds").val(photoIds)
	        _this.parents(".imageDiv").remove();
			$(".addImages").show();
	    };
    $(function(){
 		$("#photo").css("border","1px dotted #00000E");
 		$("#updatePhoto").css("border","1px solid  #00000E");
 		$("#maintenancePhoto").css("border","1px solid  #00000E");
 		$.ajax({
          	type : "get",
	            url : contextPath + "deviceRepairOrder/queryDeviceRepairOrderSerialNumber.do",
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	if(data){
	            			$("#serialNumber").val(data.serialNumber);
	            	}
	            }
	        })
 		/* setlostTimeType(); */
 		var userAgent = navigator.userAgent; //用于判断浏览器类型
 		idList=$("#photoIds").val();
 		
 		/* //报修单时间初始化
		$("#createDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		}); */
		
		showAddDeviceRepairOrderDialogDialog();

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
		        		console.log(imageList[j]);
		        		console.log(imageList[j]!=="");
				if(imageList[j]!==""){
		        		console.log(imageList[j]);
					imageVal=imageList[j]
					break;
				}
			}
 		    //动态添加html元素onclick='deleteImg(\""+ imageVal+ "\")
		        var picHtml = "<div class='imageDiv' style='margin-left: 20px'> <img  id='img" + fileList[i].name + "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="+imageVal+" /></div></div>";
		        console.log(picHtml);
		        
		        imageList.splice(imageVal,1,"");
		        console.log(imageList);
		        console.log(imageVal);
		        console.log("name:"+fileList[i].name);
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
		            	console.log(data.filePath);
		            	if(!idList){
		            		idList=data.filePath;
		            		$("#photoIds").val(idList)
		            	}else{
		            		var photoIds=idList.split(",");
		            		console.log("before:"+photoIds);
		    		       	photoIds.splice(imageVal,1,data.filePath);
		            		console.log("after:"+photoIds);
		            		idList=photoIds.join(',');
		            		console.log(idList);
		            		$("#photoIds").val(idList)
		            	}
		            }
		        });
		        if (fileList && fileList[i]) {
		            //图片属性
		            imgObjPreview.style.display = 'block';
		            imgObjPreview.style.width = '300px';
		            imgObjPreview.style.height = '320px';
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
		
		//下拉框未选中显示
        $(".selectpicker").selectpicker({
            noneSelectedText: '请选择' //默认显示内容  
             });
    });

//显示报修新增窗口
function showAddDeviceRepairOrderDialogDialog() {
	getAllReasonAdd();
 	$('#ngDescription').val("");
 	$('#picDiv').empty();
 	$('div').remove(".imageDiv");
 	$(".addImages").show();
 	idList="";
 	imageList=[0,1,2,3];
 	/* $('#addDeviceRepairOrderDialog').modal('show'); */
 	 var date = new Date();
		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
				+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
				+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
	  $("#createDate").val(value);
	  $.ajax({
          	type : "post",
	            url : contextPath + "device/queryDeviceByCode.do",
	            data : {
	            	"name":"code",
	            	"value":deviceCode         	
	            },
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	if(data){
	            		
	            			$("#deviceCode").val(data.code);
	            			$("#deviceName").val(data.name);
	            			$("#unitType").val(data.unitType);
	            			if(data.projectType!=null){
	            				$("#deviceType").val(data.projectType.name);
	            			}
	            			//根据IP地址查询本机是否有登录用户
	            			/* $.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
	            					mcUser) {
	            				if (mcUser) {
	            					$("#Informant").val(mcUser.employeeName);
	            				}
	            			}); */
	            			$("#Informant").val("<%=((Employee)session.getAttribute("employee")).getName()%>");
	            			$("#deviceId").val(data.id);
	            		
	            	}
	            }
	        })
}
//添加报修单
function addDeviceRepair(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	if($('#pressLightId').val()==""){
		alert("请选择故障类型")
	}else{
	 $.ajax({
			type : "post",
			url : contextPath + "paperlessDeviceRepair/addDeviceRepairOrder.do",
			data : {	
                "createDate":$('#createDate').val(),
                "device.id":$('#deviceId').val(),
                "Informant":$('#Informant').val(),
                "serialNumber":$('#serialNumber').val(),
                "ngreason.id":$('#pressLightId').val(),
                "ngDescription":$('#ngDescription').val(),
                "idList":$('#photoIds').val(),
                "employeeCode":"<%=((Employee)session.getAttribute("employee")).getCode()%>"
			},
			dataType : "json",
			success : function(data) {
				if(data.success){
					alert("添加成功");
					parent.$('#content').attr('src','paperless/deviceService.jsp');
				}else{
					alert(data.msg)
				}
			}
	 })
	}
}

//获取所有故障原因
/* function getAllReasonAdd(){
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
                  htmlselect += "<option value='"+Type.id+"'>"+Type.reason+"</option>";
               })
               console.log(htmlselect);
               $("#pressLight").empty();
               $("#pressLight").append(htmlselect);		
               $("#pressLight").selectpicker('refresh');

               $("#updatePressLight").empty();
               $("#updatePressLight").append(htmlselect);		
               $("#updatePressLight").selectpicker('refresh');
					
            }
        })
} */
function getAllReasonAdd(){
	 $.ajax({
    	type : "post",
           url : contextPath + "projectType/queryProjectTypesByType.do?type=breakdownReasonType",
           data : {},
		   cache:false,
           dataType : "json",
           success : function(data) {
           	//alert(JSON.stringify(data));
               /* var htmlselect = "<option></option>"; */
              $.each(data,function(index, Type) {
                /*  htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>"; */
                 var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
							+ (Type.id)
							+ "\")' value='"+Type.id+"'>"
							+ Type.name
							+ "</button>");
              	$("#lostType").append(button);
              });
            /*   console.log(htmlselect);
              $("#pressLight").empty();
              $("#pressLight").append(htmlselect);		
              $("#pressLight").selectpicker('refresh');

              $("#updatePressLight").empty();
              $("#updatePressLight").append(htmlselect);		
              $("#updatePressLight").selectpicker('refresh'); */
					
           }
       })
}
//模态框点击返回事件(hide)
function modelHide() {
	$('#showConfirmDlg').modal('hide');
	$('#showUpdateDlg').modal('hide');
	/* $('#addDeviceRepairOrderDialog').modal('hide'); */
	$('#updateDeviceRepairOrderDialog').modal('hide');
	$('#addMaintenanceStaff').modal('hide');
	$('#updateCheckingPlanDialog').modal('hide');
	$('#maintenanceOrderDialog').modal('hide');
	$('#showDoclog').modal('hide');
}
//故障弹出框
function showng(){
	$("#lostReason").empty();
	$("#lostType button").removeClass("btn btn-primary");
	$("#lostType button").addClass("btn btn-default");
		$('#showNgdialog').modal('show');
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
		url : contextPath + "deviceProject/queryAllDeviceProjectByProjectTypeId.do",
		data : {"projectTypeId":typeCode},
		cache:false,
		dataType : "json",
		success : function(data) {
			$("#lostReason").empty();
			$.each(data,function(index, Type) {
				var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;height:45px' onclick='showReasonBut(\""
						+ + (Type.id)+"\",\""+(Type.name)
						+ "\")' value='"+Type.id+"'>"
						+ Type.name
						+ "</button>");
			$("#lostReason").append(button);	
			})
		}
	}) 
}
function showReasonBut(id,name) {
	$("#lostReason button").removeClass("btn btn-primary");
	$("#lostReason button").addClass("btn btn-default");
	var btns = $("#lostReason button");
	for(var i = 0;i<btns.length;i++){
		var btn = $(btns[i]);
		var textOnButton = btn.val();
		if(textOnButton === id){
			btn.removeClass("btn btn-default");
			btn.addClass("btn btn-primary");
		}
	}
	$("#lostTypeOn").val(name);
	$("#lostTypeIdOn").val(id);
}

//类型改变事件
$(document).ready(function(){
	//新增报修单故障类型变更
	$("#pressLight").change(function() {
		var id = $("#pressLight option:selected").val();
		console.log(id);
		$("#pressLightId").val(id);
	});
});
//确认
function Confirm(){
	$("#pressLight").val($("#lostTypeOn").val());
	$("#pressLightId").val($("#lostTypeIdOn").val());
	$('#showNgdialog').modal('hide');
}
//取消
function Cancel(){
		$('#showNgdialog').modal('hide');
}
</script>
<style>
body {
	margin: 0;
}
#layoutGraph div{
	height:20px;
	width:20px;
	position:relative;
	cursor:pointer;
	display: inline-block;
}
#outer {
	height: 1080px;
	width: 1920px;
	background-image: url('front/imgs/blue.png');
}

#loginInfo {
	text-align: center;
	width: 100%;
	height: 40px;
	text-align: right;
	color: #57EEFD;
	margin-right: 50px;
	margin-bottom: 0px;
	position: absolute;
	left: -20px;
	top: 20px;
	font-size: 20px;
	z-index: 10000;
}

#blank {
	text-align: center;
	width: 1920px;
	height: 40px;
}

#title {
	text-align: center;
	width: 100%;
	height: 50px;
	line-height: 50px;
	font-size: 50px;
	color: #FF0000;
	overflow: hidden;
}

#time {
	width: 100%;
	height: 90px;
	text-align: center;
	line-height: 98px;
	color: #57EEFD;
	font-size: 25px;
}

#productionUnit{
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: left;
	margin-left: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}

#department{
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: right;
	margin-right: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}


.m_left{
	width: 360px;
	height: 855px;
	float: left;
	background-color: #1C2437;
}

.m_right{
	width: 1420px;
	height: 855px;
	background-color: #1C2437;
	float: right;
	margin-right: 10px;
}

.tp_button{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#9966CC;
}

.device_button{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#666633;
	float: left;
	margin-right: 20px;
	height: 58px;
	line-height: 58px;
}

.tp_button:hover{
	
}


#middle {
	height: 850px;
	width: 94%;
	margin: auto auto;
}

.form-control{
	width: 190px;
	background-color: #1C2437;
	border: 1px #666666 solid;
}


table{
	width:1380px;
	margin:auto auto;
	margin-top: 10px;
	border-collapse: collapse;
}
tr{
	height:40px;
}

table,table tr th, table tr td { border:1px solid white; color: white;text-align: center;}  

#warnning tr td,#topSort tr td{
	text-align: center;
	color:#FFF;
}


#warnning tr td:nth-child(odd){background:#313348;}
#warnning tr td:nth-child(even){background:#2C2E45;}
#topSort tr:nth-child(odd){background:#313348;}
#topSort tr:nth-child(even){background:#2C2E45;}

</style>
<style>
 .imageDiv {
	display:inline-block;
	width:300px;
	height:320px;
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
	width:370px;
	height:240px;
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
.addImages {
	display:inline-block;
	width:300px;
	height:320px;
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
	margin-top:60px;
	text-align:center;
}
.text-detail span {
	font-size:40px;
}
.file {
	position:absolute;
	top:0;
	left:0;
	width:300px;
	height:320px;
	opacity:0;
}

.labelstyle{
	font-size: 15px;
	vertical-align: middle;
	display: table-cell;
	height: 40px;
	line-height: 40px;
}
</style>
</head>
<body style="background-color:#1C2437;">
<!-- 设备报修窗口 -->
<div style="color:white;width: 1375px;height: 700px;border: 1px solid white;margin:auto auto;margin-top: 40px;border-radius: 20px;border: 1px dashed #666666;">
			<div class="modal-body" style="height: 500px;">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">设备代码</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceCode" id="deviceCode" class="form-control" style="width: 230px;height:40px; background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
								<input type="hidden" name="deviceId" id="deviceId" />
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备名称</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceName" id="deviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">单据编号</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="serialNumber" id="serialNumber" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备类别</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceType" class="form-control"
									id="deviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">报修时间</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="createDate" class="form-control"
									id="createDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">规格型号</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="unitType" id="unitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">报修人员</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="Informant" class="form-control"
									id="Informant" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">故障类型</div>
							<div style="float: left;margin-left: 10px;">
								<input type="text" name="Informant" class="form-control"
										id="pressLight" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
								<input type="hidden" name="pressLightId" id="pressLightId" />
								
							</div>
							<div style="float: left;margin-left: 10px">
								<div><span onclick='showng()' class='fa fa-th-list' aria-hidden='true' style='float: left;font-size: 30px;cursor:pointer;'></span>
								</div>
							</div>
						</div>
					</div>
					<div id="photo">
						<div class="form-group">
							<div style="float: left;margin-left: 50px;margin-top: 10px">
								<div class="labelstyle" style="float: left;">故障描述</div>
								<div style="float: left;margin-left: 10px">
									<textarea   name="ngDescription" rows="4" cols="172"
									id="ngDescription" style="resize:none;background-color: #1C2437;border: 1px #666666 solid;color: white;" ></textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div style="float: left;margin-left: 50px">
								<div class="labelstyle" style="float: left;">现场图片</div>
							</div>
						</div>
						<div class="form-group">
							<form method="post" action="relatedDoc/uploadimg.do" id="passForm" enctype="multipart/form-data;charset=utf-8" multipart="">
							    <div id="Pic_pass">
							        <!-- <p style="font-size: 20px;font-weight: bold;margin-left: 50px">请上传照片 </p> -->
							        <!-- <p><span style="color: red">注：每张照片大写不可超过4M，且最多可以传十张</span></p> -->
							        <div class="picDiv" style="margin-left: 30px;">
							            <div class="addImages" id="addImages" style="margin-left: 30px;color: white;background-color:#1C2437; ">
							                
							                <input type="file" class="file" id="fileInput" style="cursor:pointer;" accept="image/png, image/jpeg, image/gif, image/jpg">
							                <div class="text-detail" >
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
				
				<div style="float:right; margin: 20px 30px 0 0;background-color: #A4A419;color: white;width: 120px;height: 40px;text-align: center;line-height: 40px;border-radius: 10px;cursor:pointer;"
						onclick="addDeviceRepair()">保存</div>
			</div>
</div>

<div class="modal fade" id="showNgdialog" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static" style="z-index:10000">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header" style="text-align: left;">
				<!-- <span class="fa fa-qrcode fa-4x"></span> -->
				<h4 class="modal-title">故障类型选择</h4>
			</div>
			<div class="modal-body" style="height: 450px">
				<div class="form-group">
					<div class=".col-xs-12" style="vertical-align: middle;">
						<div style="float: left;background-color: ;width: 150px">
							<div style="margin-top: 40px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">故障类型:</label>
							</div>
							<div style="margin-top: 140px">
								<label class="control-label" for="pressLightCode" style="font-size: 20px;margin-left: 20px">故障原因:</label>
							</div>
						</div>
						<div class="" style="float: left;width: 700px">
							<div id="lostType" style="margin-top: 30px;height: 180px;width: 680px;overflow: auto;" ></div>
							<div id="lostReason" style="margin-top: 5px;height:180px;width: 680px;overflow: auto;" ></div>
						</div>
					</div>
					<input type="hidden" name="pressLightId" id="lostTypeOn" />
					<input type="hidden" name="pressLightId" id="lostTypeIdOn" />
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;">
					<span  class="btn btn-default" style="margin-right: 20px"
						onclick="Confirm()">确认</span>
				</div>
				<div style="float: right; margin-right: 50px;">
					<span  class="btn btn-default" style="margin-right: 20px"
						onclick="Cancel()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>