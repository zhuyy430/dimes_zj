<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.digitzones.model.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Employee employee = (Employee) session.getAttribute("employee");
	if (employee == null) {
		response.sendRedirect(basePath + "paperless/homePage.jsp");
		return;
	}
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<style>
body {
	margin: 0;
}

#layoutGraph div {
	height: 20px;
	width: 20px;
	position: relative;
	cursor: pointer;
	display: inline-block;
}
#outer {
	height: 1080px;
	width: 1920px;
	background-image: url('front/imgs/blue.png');
	padding-top: 40px;
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

#productionUnit {
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: left;
	margin-left: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}

#department {
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: right;
	margin-right: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}

.font_style {
	color: white;
	font-size: 15px;
	margin-left: 5px;
}

#topPanel {
	height: 130px;
	width: 94%;
	margin: auto auto;
	margin-top: -50px;
	/* background-color: pink; */
	overflow: hidden;
}

.tp_left {
	height: 130px;
	width: 360px;
	/* margin-bottom: 20px; */
	float: left;
	background-color: #1C2437;
}

.tp_right {
	height: 90px;
	width: 1430px;
	/* background-color: red; */
	float: right;
	margin-top: 40px;
	overflow: hidden;
}

.tp_button {
	float: left;
	width: 220px;
	height: 100%;
	margin-right: 20px;
	color: white;
	line-height: 90px;
	font-size: 25px;
	text-align: center;
	border: 2px solid #666666;
}


#middle {
	height: 800px;
	width: 94%;
	margin: auto auto;
	margin-top: 15px;
}

.title {
	height: 40px;
	width: 100%;
	color: #57EEFD;
	font-size: 25px;
	margin-left: 10px;
}

.top {
	height: 205px;
	width: 100%;
	margin-bottom: 15px;
	background-color: #1C2437;
}

.top_content {
	height: 520px;
	width: 100%;
	border: solid 1px gray;
}

.bottom {
	height: 545px;
	width: 100%;
	background-color: #1C2437;
	overflow: hidden;
}

.bottom_content {
	height: 230px;
	width: 100%;
}
.left_title {
	height: 20px;
	width: 100%;
	color: #8AD5DA;
	margin-left: 20px;
	font-size: 18px;
}

.left_content {
	height: 150px;
	width: 90%;
	color: #1E8FF3;
	text-align: right;
	margin: 0 auto;
	border-bottom: solid 1px gray;
}

.left_content span {
	text-align: right;
	font-size: 5em;
}

.left_content span+span {
	font-size: 3em;
}

table {
	width: 95%;
	margin: auto auto;
}

table thead tr {
	border-bottom: solid 1px gray;
	color: #5D5F6A;
}

tr {
	height: 30px;
}

#warnning tr td, #topSort tr td {
	text-align: center;
	color: #FFF;
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

#warnning tr td:nth-child(odd) {
	background: #313348;
}

#warnning tr td:nth-child(even) {
	background: #2C2E45;
}

#topSort tr:nth-child(odd) {
	background: #313348;
}

#topSort tr:nth-child(even) {
	background: #2C2E45;
}
#right{
	height:100%;
	width:100%;
} 
td,th{
	border:white 1px solid;
	color:white;
	font-size:14px;
	text-align:center;
	height:50px;
}
</style>

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
	
	$(function() {
		
		$.ajax({
			        type: "get",
			        dataType: "json",
			        async: false,  //同步
			        url: "deviceRepairOrder/queryAllDeviceRepairOrder.do",
			        data: {
			       },
			       success: function (data) {
			            var str = "";
						//alert(JSON.stringify(data));
			            $.each(data.rows, function (index, item) {  
			                //循环获取数据 
			var status = ""   ;
			var bgcolor = ""   ;
			if(item.status=='WAITINCOMFIRM'){
				status='等待接单确认'
				bgcolor='#FF9900'
			}else if(item.status=='WAITINGASSIGN'){
				status='等待派单'
				bgcolor='#FF0000'
			}else if(item.status=='MAINTAINING'){
				status='维修中'
				bgcolor='#00CC66'
			}else if(item.status=='WORKSHOPCOMFIRM'){
				status='车间确认'
				bgcolor='#99CCCC'
			}else if(item.status=='MAINTAINCOMPLETE'){
				status='维修完成'
				bgcolor='#949494'
			}else if(item.status=='WAITWORKSHOPCOMFIRM'){
				status='待车间确认'
				bgcolor='99CCCC'
			}
			
			var date = new Date(item.createDate);

			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
					+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
					+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
			
			                str += '<tr bgcolor="'+bgcolor+'">' +   
			                    '<td>' + value + '</td>' +  
			                    '<td>' + item.device.code + '</td>' +  
			                    '<td>' + item.device.name + '</td>' +  
			                    '<td>' + item.device.unitType + '</td>' +  
			                    '<td>' + item.device.projectType.name + '</td>' +  
			                    '<td>' + item.ngDescription + '</td>' +  
			                    '<td>' + item.maintainName + '</td>' +  
			                    '<td>' + status + '</td>' +  
			                    '<td>' + item.device.productionUnit.name + '</td>' +  
			                    '<td>' + item.informant + '</td>' +  
			                    '</tr>';   
			            }); 
			            $("#singles").prepend(str);
			        },
			    });
	});
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
</head>
<body>
<div id="outer">
		<div id="title">
			<a href="paperless/homePage.jsp"
				style="color: red; font-style: italic; font-size: 50px; cursor: pointer; text-decoration: none;">
				<img alt="" src="paperless/img/DFD.jpg"
				style="font-size: 15px; margin-left: -20px;"></img>
			</a> <span style="font-size: 35px; margin-left: 20px;color: #4CEBFF">维修工单任务中心</span>
		</div>
		<div id="time"></div>
		<div id="main" style="background-color: ; width: 94%;height: 90%;margin-left: 3%;margin-top: 0%">
			<!-- 表格 -->
			<div id="right">
				<div id="top" style="height:90%;width:100%;background-color:#1C2437;margin-bottom:10px;margin-top:0px;
				padding-top:5px;overflow: auto;">
					<table id="singles" style="width:98%;margin-left:auto;margin-right:auto;">
						<thead>
							<tr>
								<th>报修时间</th>
								<th>设备代码</th>
								<th>设备名称</th>
								<th>规格型号</th>
								<th>设备类别</th>
								<th>故障描述</th>
								<th>维修人员</th>
								<th>状态</th>
								<th>生产单元</th>
								<th>报修人员</th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
				<!-- <div id="bottom" style="height:60%;width:100%;background-color:#1C2437;">
					<div style="text-align: right;height:10%;width:100%;">
						<span style="color:white;margin-top:5px;margin-right:10px;">全屏查看</span>
					</div>
				</div> -->
			</div>
			<!-- 表格 -->
		</div>
	</div>

</body>
</html>