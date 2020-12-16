<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee) session.getAttribute("employee");
	if (employee == null) {
		response.sendRedirect(basePath + "paperless/homePage.jsp");
		return;
	}
	ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css">

<link rel="stylesheet" type="text/css" href="paperless/css/bootstrap-select.min.css">

<link rel="stylesheet" href="mc/assets/css/bootstrap-timepicker.min.css"> 
<script type="text/javascript" src="paperless/js/bootstrap-select.min.js"></script>  
<script type="text/javascript"
	src="mc/assets/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
<script src="common/js/bootstrap.min.js"></script>
<script type="text/javascript" src="paperless/js/deviceCheckPlan.js"></script>
<script type="text/javascript" src="paperless/js/deviceRepair.js"></script>
<script type="text/javascript" src="paperless/js/devicepreview.js"></script>

<link rel="stylesheet" href="paperless/css/bootstrap-datetimepicker.min.css"> 
<script type="text/javascript"
    src="paperless/js/bootstrap-datetimepicker.min.js"></script>
    
<script type="text/javascript"
    src="paperless/js/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript"
    src="paperless/js/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script
    src="paperless/js/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>
<link rel="stylesheet"
    href="paperless/js/bootstrap-table/bootstrap-table.min.css">
<script type="text/javascript">
window.onload=function(){
	//定时器每秒调用一次fnDate()
	setInterval(function(){
	fnDate();
	},1000);
	}
	
	function fnDate(){
		//时间
		var date = new Date();

		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
				+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
				+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		$("#time").text(value);
	}
    	var contextPath = "<%=basePath%>";
    	var employee="<%=employee%>"
    	$(function(){
    		var time = new Date();
    		$("#time").text(getDateTime(time));
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
    </script>
    
    <script type="text/javascript">
    
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
    	<%-- var pname="<%=request.getParameter("pname")%>";
		console.log(pname);
		if(pname!=""){
			$("#paperProductionUnitName").text(pname);
		} --%>
    	$('#checkDate').datetimepicker();
 		$("#photo").css("border","1px dotted #666666");
 		$("#updatePhoto").css("border","1px solid  #666666");
 		$("#maintenancePhoto").css("border","1px solid  #666666");
 		setlostTimeType();
 		var userAgent = navigator.userAgent; //用于判断浏览器类型
 		idList=$("#photoIds").val();
 		
 		/* //报修单时间初始化
		$("#createDate").datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN',
			autoclose : true,
			todayBtn:'linked'
		}); */
		
		

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
		        var picHtml = "<div class='imageDiv' style='margin-left: 30px'> <img  id='img" + fileList[i].name + "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="+imageVal+" /></div></div>";
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
		            imgObjPreview.style.height = '250px';
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
 	$('#addDeviceRepairOrderDialog').modal('show');
 	 var date = new Date();
		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
				+ (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
				+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
	  $("#createDate").val(value);
	  $.ajax({
          	type : "post",
	            url : contextPath + "deviceSite/queryDeviceSiteByCode.do",
	            data : {"deviceSiteCode":"DS_Device1"},
				cache:false,
	            dataType : "json",
	            success : function(data) {
	            	if(data){
	            		if(data.device){
	            			$("#deviceCode").val(data.device.code);
	            			$("#deviceName").val(data.device.name);
	            			$("#unitType").val(data.device.unitType);
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
//添加报修单
function addDeviceRepair(){
	var deviceSiteCode = $("#deviceSiteCode").val();
	 $.ajax({
			type : "post",
			url : contextPath + "mcDeviceRepairOrder/addDeviceRepairOrder.do",
			data : {	
                "createDate":$('#createDate').val(),
                "device.id":$('#deviceId').val(),
                "Informant":$('#Informant').val(),
                "pressLight.id":$('#pressLightId').val(),
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

//获取所有故障原因
function getAllReasonAdd(){
	 $.ajax({
     	type : "post",
            url : contextPath + "projectType/queryProjectTypesByType.do?type=breakdownReasonType",
            data : {},
			cache:false,
            dataType : "json",
            success : function(data) {
            	//alert(JSON.stringify(data));
                var htmlselect = "<option></option>";
               $.each(data,function(index, Type) {
                  htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
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
}
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
		console.log(typeid);
		if(textOnButton === typeid){
			btn.removeClass("btn btn-default");
			btn.addClass("btn btn-primary");
			//$("#lostTimeType").selectpicker('val',data.pressLightType.code);
		$("#pressLight").selectpicker('val',typeid);
		$("#pressLightId").val(typeid);
		}
	}
}

//确认
function Confirm(){
		$('#showNgdialog').modal('show');
}
//取消
function Cancel(){
		$('#showNgdialog').modal('hide');
}
function chooseDevice(){
		console.log($("#deviceCode").val());
		if($("#deviceCode").val()!=null&&$("#deviceCode").val()!=""){
			$('#content').attr('src','paperless/deviceRepair.jsp');
		}else{
			/* $("#alertText").text("请先选择设备，再点检");
            $("#alertDialog").modal(); */
            alert("请先选择设备，再报修");
		}
		
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
	z-index: 999;
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
	overflow-y: auto;
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
	background-color:#1F3871;
	float: left;
	margin-right: 20px;
	height: 58px;
	line-height: 58px;
	border:1px solid #666666;
	AutoSize:true;
}
.device_button_nomal{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#1F3871;
	float: left;
	margin-right: 20px;
	height: 58px;
	line-height: 58px;
	border:1px solid #666666;
}
.device_button_pressed{
	color:black;
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




table{
	width:1380px;
	margin:auto auto;
	border-collapse: collapse;
}
tr{
	height:40px;
}

#items{
	overflow-y:auto;
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
	height:250px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
}
.cover {
	position:absolute;
	z-index:1;
	top:0;
	left:0;
	width:160px;
	height:130px;
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
	width:160px;
	height:130px;
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
	width:160px;
	height:130px;
	opacity:0;
}

.deviceCheckInput{
	width:300px;
	font-size:14px;
	background-color:#1C2437;
	border:1px solid #666666;
	color:white;
	padding:2px;
	height:30px;
}

#checkingPlanRecordItemTb td{
	boder:1px solid #666666;
}

.optionStyle{
	background-color:#1C2437;
}

/**显示点检图片方向键样式*/
.imgDirection{
	height:100%;width:5%;float:left;cursor:pointer;color:#80848E;
	font-size:30px;
	text-align:center;
	margin-top:300px;
}

</style>	
<style>
/* 维修保养 */
#showStaffTable tr td, #showNGRecordTable tr td,#showMaintainProjectTable tr td, #showSparepartRecordTable tr td {
    text-align: center;
    color: #FFFFFF;
}

.table2  th,td,tr{
    border:#666666 1px solid;
    color:#FFFFFF;
    font-size:14px;
    text-align:center;
    line-height:50px;
    height:50px;
    vertical-align: middle;
    background-color: #1C2437;
} 
 
 
.table1 td,th{
    border:#666666 1px solid;
    color:#FFFFFF;
    font-size:14px;
    text-align:center;
    height:50px;
    vertical-align: middle;
    line-height:50px;
}
	
.labelstyle{
	font-size: 15px;
	vertical-align: middle;
	display: table-cell;
	height: 40px;
	line-height: 40px;
}
	
	
	
*{margin:0px;padding:0px;}
.tabbox{}
.tabbox ul{list-style:none;display:table;margin-top: 4px;float:left;}
.tabbox ul li{width:160px;height:110px;line-height:100px;padding-left:20px;margin-top:-18px;border:1px solid #666666;cursor:pointer;}
#ngselete  ul  {margin-top:10px;}
#ngselete  ul  li{width:240px;height:40px;margin-top:-0px;margin-left:-20px;border:0px solid #666666;cursor:pointer;background-color:#FFFFFF;}
.tabbox ul li.active{background-color:#1F3871;font-weight:bold;border:1px solid #666666;}
.tabbox .content{}
.tabbox .content>div{display:none;}
.tabbox .content>div.active{display:block;margin-top: 10px}
 

.mtabbox{}
.mtabbox ul{list-style:none;display:table;margin-top: 4px}
.mtabbox ul li{width:160px;height:110px;line-height:100px;padding-left:20px;margin-top:-18px;border:1px solid #666666;cursor:pointer;}
.mtabbox ul li.active{background-color:#1F3871;font-weight:bold;border:1px solid #666666;}
.mtabbox .content{}
.mtabbox .content>div{display:none;}
.mtabbox .content>div.active{display:block;margin-top: 10px}



.functionButton{
    width:110px;
    height:110px;
    padding-top:5px;
    border:2px solid darkgray;
    border-radius:16px;
    margin-right:20px;
    display:inline-block;
    cursor:pointer;
    text-align:center;
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


#dialog-layer{
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    z-index: 1000;
    display:none;
    background-color:  #1C2437;
}


/* bootstrop表格修改 */
.table > tbody > tr.success > td {
  background-color: #ff6666;
}
.table-hover > tbody > tr > td.success:hover,
.table-hover > tbody > tr > th.success:hover,
.table-hover > tbody > tr.success:hover > td,
.table-hover > tbody > tr:hover > .success,
.table-hover > tbody > tr.success:hover > th{
  background-color: #ff6633;
}

.fixed-table-container tbody .selected td {
    background-color: #ff6666;
}

.table > tbody > tr > td,
.table > tbody > tr > th, 
.table > tfoot > tr > td, 
.table > tfoot > tr > th, 
.table > thead > tr > td, 
.table > thead > tr > th{
	vertical-align:middle;
}


.fixed-table-container thead th .th-inner{
 line-height: 50px;
 padding: 0;
}
</style>
</head>
<body>
	<!-- 存储选中的设备编码 -->
	<input type="hidden" name="deviceCode" id="deviceCode" />
	<div id="outer">
		<div id="loginInfo">
			<span style="margin-right: 10px;"> <%=employee.getCode()%>&nbsp;<%=employee.getName()%></span><a
				href="javascript:void(0);" class="fa fa-sign-out" onClick="logout()"></a>
		</div>
		<div id="blank"></div>
		<div id="title">
		<a href="paperless/homePage.jsp"
				style="color: red; font-style: italic; font-size: 50px; cursor: pointer; text-decoration: none;">DIMES
				<sub class="fa fa-mail-forward"
				style="font-size: 15px; margin-left: -20px;"></sub>
			</a> <span style="font-size: 35px; margin-left: 20px;color:#57EEFD;">智慧工厂无纸化系统</span>
		</div>
		<div id="time"></div>
		<div id="productionUnit">生产单元:
		<span id="paperProductionUnitName">
			<% if (productionLine != null) {
							
					%>
							<%=productionLine.getName()%>
							<%
								}else{
							%>
								未选择
							<%
							}
							%>
		</span>
		</div>
		<div id="department">部门:
		<%
			if (employee.getPosition() != null) {
				if (employee.getPosition().getDepartment() != null) {
		%>
			<%=employee.getPosition().getDepartment().getName()%>
			<%
				}
				}
			%>
		</div>

		<div id="middle">
			<div class="m_left">
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#666633;">设备相关</div>
				
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#484B35;margin-top: 30px;cursor:pointer;"
				onClick="$('#content').attr('src','paperless/deviceMessage.jsp')">设备信息</div>
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#484B35;margin-top: 30px;">设备管理</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000"
				onClick="$('#content').attr('src','paperless/deviceCheckPlanRecord.jsp')" id="deviceCheckDiv">设备点检</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000" 
				onClick="chooseDevice()">设备报修</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000"
				onClick="$('#content').attr('src','paperless/deviceService.jsp')">设备维修</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;"
				onClick="$('#content').attr('src','paperless/deviceMaintain.jsp')" id="deviceMaintainDiv">设备保养</div>
			</div>
			<div class="m_right">
				<div style="height: 58px;margin:15px 0 0 20px;" id="devicesDiv">
				</div>	
				<iframe style="height:750px;width:1400px;border-width: 0px; 
				margin-left:10px;background-color:#1C2437;margin-top:10px;backgroun-color:#1C2437;" 
				id="content"  src="paperless/deviceCheckPlanRecord.jsp"></iframe>
			</div>
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


<div class="modal fade" id="deviceCheckDialog" role="dialog"  
	aria-labelledby="myModalLabel" aria-hidden="true"
	data-backdrop="static">
	<div class="modal-dialog" style="width: 1500px;height:900px;">
		<div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
			<div class="modal-header">
				<h4 class="modal-title" style="color:white;">设备点检</h4>
			</div>
			<div class="modal-body" style="height: 750px">
				<div style="background-color:#1C2437;" >
					<form id="deviceCheckForm">
						<div title="设备点检" data-options="iconCls:'fa fa-th'">
							<div class="topjui-fluid">
								<div style="height: 20px;"></div>
								<div class="topjui-row">
									<div class="topjui-col-sm4">
										<label class="topjui-form-label" style="text-align: center;color:white;">设备代码</label>
										<div class="topjui-input-block">
											<input type="text" name="check_deviceCode" class="deviceCheckInput"
												 id="check_deviceCode" readonly="readonly">
												<!-- 点检记录id -->
												<input type="hidden" name="id" id="id" />
										</div>
									</div>
									<div class="topjui-col-sm4">
										<label class="topjui-form-label" style="text-align: center;color:white;">设备名称</label>
										<div class="topjui-input-block">
											<input type="text" name="check_deviceName"  class="deviceCheckInput"
												 readonly="readonly" id="check_deviceName">
										</div>
									</div>
									<div class="topjui-col-sm4">
										<label class="topjui-form-label" style="text-align: center;color:white;">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="check_unitType"  class="deviceCheckInput"
												readonly="readonly" id="check_unitType">
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm4">
										<label class="topjui-form-label" style="text-align: center;color:white;">点检日期</label>
										<div class="topjui-input-block">
											<input type="text" name="checkDate"  id="checkDate" class="deviceCheckInput" data-date-format="yyyy-mm-dd hh:ii">
										</div>
									</div>
									<div class="topjui-col-sm4">
										<label class="topjui-form-label" style="text-align: center;color:white;">点检人员</label>
										<div class="topjui-input-block">
											<input type="text" name="checkUsername" class="deviceCheckInput"
											 readonly="readonly" id="checkUsername">
											<input type="hidden" id="checkUsercode" name="checkUsercode" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm5">
										<div id="docs" style="border-style: dotted; border-radius: 15px;height:600px;overflow: hidden;
										border-color: #4F5157;">
											<div class="fa fa-chevron-left imgDirection" id="showPrevious"></div>
											<div style="height:100%;width:90%;float:left;">
												<img id="deviceCheckImg" alt="设备点检图片" style="width:98%;height:94%;margin-top:20px;"/>
											</div>
											<div class="fa fa-chevron-right imgDirection" id="showNext"></div>
										</div>
									</div>
									<div class="topjui-col-sm7">
										<div id="items"  style="border-color: #4F5157;border-style: dotted; border-radius: 10px;height:600px;
										margin-left:20px;">
											<table id="checkingPlanRecordItemTb" border="1" style="width:98%;margin:5px auto;">
												<thead>
													<tr>
														<td style="width: 60px"></td>
														<td>项目代码</td>
														<td>点检项目</td>
														<td>标准</td>
														<td>方法</td>
														<td>频次</td>
														<td style="width:60px;">结果</td>
														<td>备注</td>
													</tr>
												</thead>
												<tbody id="checkingPlanRecordItem"></tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm5">
										<div style="height:30px;">
										</div>
									</div>
									<div class="topjui-col-sm7">
										<div  style="height:30px;color:red;font-size:20px;">
											在设备运行和进行设备清洁时有任何异常情况马上报告给主管或维护人员 
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px;width: 400px;">
					 <span class='btn btn-default' style="margin-right: 20px;"
						onclick="saveCheckPlanRecordItem()" id="saveBtn">保存</span>
					<span  class='btn btn-default'
						onclick="$('#deviceCheckDialog').modal('hide')" id="cancelBtn">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
<!-- 维修弹窗开始 -->
	<!-- 维修界面 -->
	<div class="modal fade" id="updateDeviceRepairOrderDialog" role="dialog"
	    aria-labelledby="myModalLabel" aria-hidden="true"
	    data-backdrop="static" >
	    <div class="modal-dialog" style="width: 1500px;height:900px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
	            <div class="modal-header">
	                <h4 class="modal-title">编辑设备报修单</h4>
	            </div>
	            <div class="modal-body" style="height: 750px">
	                <form id="pressLightRecordForm" class="form-horizontal">
	                    <div class="form-group" style="margin-top: 20px;">
	                        <div style="float: left;margin-left: 50px">
	                            <div class="labelstyle" style="float: left;">设备代码</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateDeviceCode" id="updateDeviceCode" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
	                                <input type="hidden" name="orderId" id="orderId" />
	                                <input type="hidden" name="updateDeviceId" id="updateDeviceId" />
	                                <input type="hidden" name="updateStatus" id="updateStatus" />
	                            </div>
	                        </div>
	                         <div style="float: left;margin-left: 20px">
	                            <div class="labelstyle" style="float: left;">设备名称</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateDeviceName" id="updateDeviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
	                            </div>
	                        </div>
	                         <div style="float: left;margin-left: 20px">
	                            <div class="labelstyle" style="float: left;">规格型号</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateUnitType" id="updateUnitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="form-group">
	                        <div style="float: left;margin-left: 50px">
	                           <div class="labelstyle" style="float: left;">报修时间</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateCreateDate" class="form-control"
	                                    id="updateCreateDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
	                            </div>
	                        </div>
	                        <div style="float: left;margin-left: 20px">
	                            <div class="labelstyle" style="float: left;">设备类别</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateDeviceType" class="form-control"
	                                    id="updateDeviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
	                            </div>
	                        </div>
	                        <div style="float: left;margin-left: 20px">
	                            <div class="labelstyle" style="float: left;">报修人员</div>
	                            <div style="float: left;margin-left: 10px">
	                                <input type="text" name="updateInformant" class="form-control"
	                                    id="updateInformant" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
	                            </div>
	                        </div>
	                    </div>
	                    <div id="updatePhoto">
	          <div class="tabbox" style="width: 1460px;height: 541px">
	                        　　<ul style="color: white;margin-top: 1px;">
	                        　　　　<li class="active">报修内容</li>
	                        　　　　<li>维修人员</li>
	                        　　　　<li>故障原因</li>
	                        　　　　<li>维修项目</li>
	                        　　　　<li>备件信息</li>
	                        　　</ul>
	                        
	                        <div class="content" style="float: left;width: 1300px;height: 550px;margin-top: -50px">
	                        　　<div class="active">
	                        　　　　<div style="margin-left: 20px;">
	                                    <div class="form-group">
	                                                    <div style="margin: 20px 0 0 10px;">
	                                                        <div class="labelstyle" style="float: left;">故障描述</div>
	                                                        <div style="float: left;margin-left: 10px">
	                                                            <textarea   name="updateNGDescription" id="updateNGDescription" rows="6" cols="150"
	                                                             style="resize:none;background-color: #1C2437;border: 1px #666666 solid;color: white;" ></textarea>
	                                                        </div>
	                                                    </div>
	                                                </div>
	                                                 <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic">
	                                                    <!-- datagrid表格 -->
	                                                    <div class="updatePicDiv" id="updatePicDiv" style="margin-top: 50px;float: left;">
	                                                    </div>
	                                                </div>
	                                </div>
	                        　　</div>
	                        　　<div style="margin-left: 20px;">
	                                <div class="StaffTable" style="width: 1200px;margin-top: 0px">
	                                    <table id='showStaffTable' class="table2"></table>
	                                    
	                                    <div onclick="removeRecord('staff')" id="removeMaintenanceStaffView" style="float: right;"
	                                        class="container-fluid updateButton" >
	                                        <h6></h6>
	                                        <span class="fa fa-times-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">删除</h4>
	                                    </div>
	                                    <div onclick="addRecord('staff')" id="addMaintenanceStaffView" style="float: right;"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-plus-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">新增</h4>
	                                    </div>
	                                </div>
	                            </div>
	                        　　<div style="margin-left: 20px;">
	                                <div class="NGRecordTable" style="width: 1200px;margin-top: 0px">
	                                    <table id='showNGRecordTable' class="table2"></table>
	                                
	                                
	                                <div onclick="removeRecord('ngRecord')" id="removeNGMaintainView" style="float: right;margin-right: 30px"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-times-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">删除</h4>
	                                    </div>
	                                    <div onclick="addRecord('ngRecord')" id="addNGMaintainView" style="float: right;"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-plus-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">新增</h4>
	                                    </div>
	                                 </div> 
	                            </div>
	                        　　<div style="margin-left: 20px;">
	                                <div class="MaintainProjectTable" style="width: 1200px;margin-top: 0px">
	                                    <table id='showMaintainProjectTable' class="table2"></table>
	                                
	                                
	                                <div onclick="removeRecord('maintainProject')" id="removeMaintainProjectView" style="float: right;margin-right: 30px"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-times-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">删除</h4>
	                                    </div>
	                                    <div onclick="addRecord('maintainProject')" id="addMaintainProjectView" style="float: right;"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-plus-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">新增</h4>
	                                    </div>
	                                </div>
	                            </div>
	                        　　<div style="margin-left: 20px;">
	                                <div class="SparepartRecordTable" style="width: 1200px;margin-top: 0px">
	                                    <table id='showSparepartRecordTable' class="table2"></table>
	                               
	                                
	                                <div onclick="removeRecord('sparepartRecord')" id="removeSparepartView" style="float: right;margin-right: 30px"
	                                        class="container-fluid updateButton">
	                                        <h6></h6>
	                                        <span class="fa fa-times-circle" aria-hidden="true"
	                                            style="font-size: 30px; margin-top: -5px"></span>
	                                        <h4 style="text-align: center">删除</h4>
	                                    </div>
	                                    <div onclick="addRecord('sparepartRecord')" id="addSparepartView" style="float: right;"
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
	                    </div>
	                </form>
	            </div>
	            <div class="modal-footer" style="margin-top: 20px;">
	            <div style="float: left; margin-right: 50px;">
	                    <span  class='btn btn-default' id="repairComplete"
	                        onclick="showConfirm('repairComplete')">维修完成</span>
	            </div>
	                <div style="float: right; margin-right: 50px;width: 400px;">
	                    <span  class='btn btn-default' id="updateDeviceRepair"
	                        onclick="updateDeviceRepair()">更新</span>
	                    <span  class='btn btn-default'
	                        onclick="modelHide()">关闭</span>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
	
	
	
	<!-- 报修单新增维修人员 -->
	<div class="modal fade" id="addMaintenanceStaff" role="dialog"
	    aria-labelledby="myModalLabel" aria-hidden="true"
	    data-backdrop="static">
	    <div class="modal-dialog" style="width: 600px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
	            <div class="modal-header">
	                <div
	                style="height:40px; width: 600px; overflow: hidden;">
	                <span style="font-size: 20px;">新增维修人员</span>
	            </div>
	            </div>
	    <div class="modal-body">
	        <div
	            style="width: 500px; height: 385px; background-color: #1C2437; padding: 20px">
	            
	            <div >
	                <form
	                    style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
	                    <span style="font-size: 20px;">员工姓名</span> <select id="StaffName" name="StaffName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block;" equipID=""
	                                    class="selectpicker" data-live-search="true">
	                                </select><br>
	                    <br>
	                    <br>
	                    <br> <span style="font-size: 20px;">员工代码</span> <input
	                                    id="StaffCode" name="StaffCode" type="text"
	                                    style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
	                                    class="form-control" readonly="readonly" /><br>
	                    <br>
	                    <br>
	                    <br>
	                            <span style="font-size: 20px;">派单时间</span><input type="text" name="StaffcreateDate"
	                                    id="StaffcreateDate" style="margin-left:10px; width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
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
	
	
	<!-- 报修单新增故障原因 -->
	<div class="modal fade" id="addNGMaintain" role="dialog"
	    aria-labelledby="myModalLabel" aria-hidden="true"
	    data-backdrop="static">
	    <div class="modal-dialog" style="width: 600px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
	            <div class="modal-header">
	                <div
	                style="height:40px; width: 600px; overflow: hidden;">
	                <span style="font-size: 20px;">新增故障原因</span>
	            </div>
	            </div>
	    <div class="modal-body">
	        <div
	            style="width: 500px; height: 385px; background-color: #1C2437; padding: 20px">
	            
	            <div >
	                <form
	                    style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
	                    <span style="font-size: 20px;">故障类型</span> <select id="NGMaintainName" name="NGMaintainName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block;" equipID=""
	                                    class="selectpicker" data-live-search="true">
	                                </select><br>
	                    <br>
	                    <br>
	                    <br> <span style="font-size: 20px;">故障代码</span> <!-- <input
	                                    id="NGMaintainCode" name="NGMaintainCode" type="text"
	                                    style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
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
	                            <span style="background-color: #1C2437;border: 1px #666666 solid;color: white;font-size: 20px;">说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明</span><textarea   name="NGMaintainNote" rows="4" cols="48"
	                                     data-options="required:true" id="NGMaintainNote" style="resize:none;background-color: #1C2437" ></textarea><br>
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
	<div class="modal-dialog" style="width: 600px; color: white;">
		<div class="modal-content"
			style="background-color: #1C2437; width: 100%; height: 100%;">
			<div class="modal-header">
				<div style="height: 40px; width: 600px; overflow: hidden;">
					<span style="font-size: 20px;">新增维修项目</span>
				</div>
			</div>
			<div class="modal-body">
				<div
					style="width: 500px; height: 485px; background-color: #1C2437; padding: 20px">
					<div>
						<form
							style="width: 380px; margin-top: -10px; margin-left: 20px; float: left;">
							<span style="font-size: 20px;">类别名称</span> <!-- <input
								id="MaintainProjectTypeName" name="MaintainProjectTypeName"
								type="text"
								style="width: 230px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block; text-align: left;"
								class="form-control" readonly="readonly" /> --><select
								id="MaintainProjectTypeName" name="MaintainProjectTypeName"
								style="width: 230px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block;"
								equipID="" class="selectpicker" data-live-search="true"></select><br> <br>
							<span style="font-size: 20px;">类别代码</span> <input
								id="MaintainProjectTypeCode" name="MaintainProjectTypeCode"
								type="text"
								style="width: 220px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block; text-align: left;"
								class="form-control" readonly="readonly" /><br> <br>
							<span style="font-size: 20px;">项目名称</span> <select
								id="MaintainProjectName" name="MaintainProjectName"
								style="width: 230px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block;"
								equipID="" class="selectpicker" data-live-search="true">
							</select> <input id="MaintainProjectTypeId" name="MaintainProjectTypeId"
								type="hidden" class="form-control" /><br> <br> <span
								style="font-size: 20px;">项目代码</span> <input
								id="MaintainProjectCode" name="MaintainProjectCode" type="text"
								style="width: 220px; height: 40px; background-color: #1C2437; border: 1px #666666 solid; color: white; display: inline-block; text-align: left;"
								class="form-control" readonly="readonly" /><br> <br>
							<span style="font-size: 20px;">说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明</span>
							<textarea name="MaintainProjectNote" rows="3" cols="48"
								data-options="required:true"
								id="MaintainProjectNote"
								style="resize: none; background-color: #1C2437; border: 1px #666666 solid; color: white;"></textarea>
							<br> <span style="font-size: 20px;">方&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;法</span>
							<textarea name="MaintainProjectProcessingMethod" rows="3"
								cols="48" 
								data-options="required:true"
								id="MaintainProjectProcessingMethod"
								style="resize: none; background-color: #1C2437; border: 1px #666666 solid; color: white;"></textarea>
							<br> <span style="font-size: 20px;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</span>
							<textarea name="MaintainProjectRemark" rows="3" cols="48"
								 data-options="required:true"
								id="MaintainProjectRemark"
								style="resize: none; background-color: #1C2437; border: 1px #666666 solid; color: white;"></textarea>
							<br> <br>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;">
				<div style="float: right; margin-right: 50px; width: 400px;">
					<span class='btn btn-default' style="margin-right: 20px;"
						onclick="addMaintainProject()">新增</span> <span
						class='btn btn-default' onclick="RepairOrderHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>
	<!-- 报修单新增备件信息 -->
	<div class="modal fade" id="addSparepart" role="dialog"
	    aria-labelledby="myModalLabel" aria-hidden="true"
	    data-backdrop="static">
	   <div class="modal-dialog" style="width: 600px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
	            <div class="modal-header">
	                <div
	                style="height:40px; width: 600px; overflow: hidden;">
	                <span style="font-size: 20px;">新增备件信息</span>
	            </div>
	            </div>
	    <div class="modal-body">
	        <div
	            style="width: 500px; height: 385px; background-color: #1C2437; padding: 20px">
	            
	            <div >
	                <form
	                    style="width: 380px; margin-top: 10px; margin-left: 20px; float: left;">
	                    <span style="font-size: 20px;">耗用时间</span><input type="text" name="SparepartcreateDate"
	                                    id="SparepartcreateDate" style="margin-left:4px;width: 220px; display: inline-block; text-align: left;margin-left: 4px"
	                                    class="form-control"><br>
	                    <br>
	                    <span style="font-size: 20px;">备件名称</span> <select id="SparepartName" name="SparepartName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block;" equipID=""
	                                    class="selectpicker" data-live-search="true">
	                                </select><br>
	                    <br>
	                    <br> <span style="font-size: 20px;">备件代码</span> <input
	                                    id="SparepartCode" name="SparepartCode" type="text"
	                                    style="margin-left:0px;width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
	                                    class="form-control" readonly="readonly" />
	                                    
	                                    <input    id="SparepartId" name="SparepartId" type="hidden" class="form-control"/>
	                                    <br>
	                    <br>
	                    <br> <span style="font-size: 20px;">数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量</span> <input
	                                    id="SparepartNum" name="SparepartNum" type="number" value="1"
	                                    style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
	                                    class="form-control" /><br>
	                    <br>
	                            <span style="font-size: 20px;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</span><textarea   name="SparepartNote" rows="3" cols="48"
	                                     data-options="required:true" id="SparepartNote" style="resize:none;background-color: #1C2437;border: 1px #666666 solid;color: white;" ></textarea><br>
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

<!-- 维修弹窗结束 -->



<!-- 保养弹窗开始 -->
	<!-- 保养界面 -->
	<div class="modal fade" id="maintenanceOrderDialog" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog" style="width: 1500px;height:900px;color: white;">
			<div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
				<div class="modal-header">
					<h4 class="modal-title">设备保养</h4>
				</div>
				<div class="modal-body" style="height: 750px">
					<form id="pressLightRecordForm" class="form-horizontal">
						<div class="form-group" style="margin-top: 20px;">
							<div style="float: left;margin-left: 50px">
								<div class="labelstyle" style="float: left;">设备代码</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceDeviceCode" id="maintenanceDeviceCode" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
									<input type="hidden" name="maintenanceId" id="maintenanceId" />
									<input type="hidden" name="updateDeviceId" id="updateDeviceId" />
									<input type="hidden" name="maintenanceConfirmCode" id="maintenanceConfirmCode" />
									<input type="hidden" name="maintenanceStatus" id="maintenanceStatus" />
								</div>
							</div>
							 <div style="float: left;margin-left: 20px">
								<div class="labelstyle" style="float: left;">设备名称</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceDeviceName" id="maintenanceDeviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
								</div>
							</div>
							 <div style="float: left;margin-left: 20px">
								<div class="labelstyle" style="float: left;">规格型号</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceUnitType" id="maintenanceUnitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div style="float: left;margin-left: 50px">
								<div class="labelstyle" style="float: left;">保养时间</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceCreateDate" class="form-control"
										id="maintenanceCreateDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
								</div>
							</div>
							<div style="float: left;margin-left: 20px">
								<div class="labelstyle" style="float: left;">保养类别</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceDeviceType" class="form-control"
										id="maintenanceDeviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
								</div>
							</div>
							<div style="float: left;margin-left: 20px">
								<div class="labelstyle" style="float: left;">保养人员</div>
								<div style="float: left;margin-left: 10px">
									<input type="text" name="maintenanceConfirmName" class="form-control"
										id="maintenanceConfirmName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
								</div>
							</div>
						</div>
						<div id="maintenancePhoto">
							<div class="mtabbox" style="width: 1460px;height: 541px">
							　　<ul style="float: left;margin-top: 1px;">
							　　　　<li class="active">设备分布图</li>
							　　　　<li>保养人员</li>
							　　　　<li>保养项目</li>
							　　　　<li>备件信息</li>
							　　</ul>
							
							<div class="content" style="float: left;width: 1300px;height: 550px;">
							　　<div class="active">
							　　　　<div style="margin-left: 20px;">
													 <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic" style="margin-top: 20px">
														<!-- datagrid表格 -->
														<div class="updatePicDiv" id="MaintenancePicDiv" style="margin-top: 20px;float: left;">
							      					  </div>
													</div>
									</div>
							　　</div>
							　　<div style="margin-left: 20px;">
									<div class="maintenanceStaffTable" style="width: 1200px;margin-top: 0px">
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
									<div class="maintenanceProjectTable" style="width: 1200px;margin-top: 0px">
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
									<div class="maintenanceSparepartRecordTable" style="width: 1200px;margin-top: 0px">
										<table id='showMaintenanceSparepartRecordTable'></table>
									
									
									<div onclick="removeRecord('maintenanceSparepart')" id="removeMaintenanceSparepartView" style="float: right;"
											class="container-fluid updateButton">
											<h6></h6>
											<span class="fa fa-times-circle" aria-hidden="true"
												style="font-size: 30px; margin-top: -5px"></span>
											<h4 style="text-align: center">删除</h4>
										</div>
										<div onclick="addRecord('maintenanceSparepart')" id="addMaintenanceSparepartView" style="float: right;"
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
						</div>
					</form>
				</div>
				<div class="modal-footer" style="margin-top: 20px;">
					<div style="float: right; margin-right: 50px;width: 400px;">
						<span  class='btn btn-default' id="updateDeviceMaintenance"
							onclick="updateDeviceMaintenance()">保养完成</span>
						<span  class='btn btn-default'
							onclick="modelHide()">关闭</span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 新增保养人员 -->
	<div class="modal fade" id="addMaintenance" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog" style="width: 600px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
				<div class="modal-header">
					<div
					style="height:40px; width: 600px; overflow: hidden;">
					<span style="font-size: 20px;">新增保养人员</span>
				</div>
				</div>
		<div class="modal-body">
			<div
				style="width: 500px; height: 385px; background-color: #1C2437; padding: 20px">
				
				<div >
					<form
						style="width: 380px; margin-top: 40px; margin-left: 20px; float: left;">
						<span style="font-size: 20px;">员工姓名</span> <select id="maintenanceInformantName" name="maintenanceInformantName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block;" equipID=""
										class="selectpicker" data-live-search="true">
									</select><br>
						<br>
						<br>
						<br> <span style="font-size: 20px;">员工代码</span> <input
										id="maintenanceInformantCode" name="maintenanceInformantCode" type="text"
										style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white; display: inline-block; text-align: left;"
										class="form-control" readonly="readonly" /><br>
						<br>
						<br>
						<br>
								<span style="font-size: 20px;">派单类型</span><select class="selectpicker" name="orderType" id="orderType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
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
	
	
	<!-- 保养记录新增备件信息 -->
	<div class="modal fade" id="addMaintenanceSparepart" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static">
		<div class="modal-dialog" style="width: 800px;color: white;">
	        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
				<div class="modal-header">
					<div
					style="height:40px; width: 700px; overflow: hidden;">
					<span style="font-size: 20px;">新增备件信息</span>
				</div>
				</div>
		<div class="modal-body">
				<div
					style="width: 770px; height: 485px; background-color: #1C2437; padding: 20px">
					<div style="width:100%; margin-top: -10px">
				备件编码:<input id="sparepartCode" 
					name="sparepartCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;background-color: #1C2437;">
				备件名称:<input id="sparepartName" 
					name="sparepartName" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;background-color: #1C2437;">
				规格型号:<input id="sparepartUnitType" 
					name="sparepartUnitType" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;background-color: #1C2437;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" style="background-color: white;"
				onclick="reloadSparepart()">搜索</a>
			</div>
					<div style="margin-top: 10px">
						<table id="MaintenanceSparepartTab">
						</table>
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
	
	<!-- 走动管理图片预览 -->
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
	<div style="font-size:50px;color:#EAF0F6;z-index:2000;position:fixed;top:0px;left:1870px;cursor:pointer;" 
	onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
	 onmouseout="$(this).css('font-size','50px')" >×</div>
	<div style="height:100%;width:100%;" id="fullScreenDiv">
		
	</div>
</div>