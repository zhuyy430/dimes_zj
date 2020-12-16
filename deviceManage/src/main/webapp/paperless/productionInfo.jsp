<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee)session.getAttribute("employee");
	ProductionUnit productionLine=(ProductionUnit)session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<script type="text/javascript" src="paperless/js/devicepreview.js"></script>
<script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	var employee="<%=employee%>"
    	
    		$(function(){
        		<%-- var pname="<%=request.getParameter("pname")%>";
        		console.log(pname);
        		if(pname!=""){
        			$("#paperProductionUnitName").text(pname);
        		} --%>
    		})
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
	width:200px;
	height:150px;
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

#dialog-layer{
  position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    z-index: 1000;
    display:none;
    background-color:  #1C2437;
}

</style>	
</head>
<body>
	<!-- 存储选中的设备编码 -->
	<input type="hidden" name="deviceCode" id="deviceCode" />
	<div id="outer">
		<div id="loginInfo">
			<span style="margin-right: 10px;"> <%
			if (employee != null) {
		%>
			<%=employee.getCode()%>&nbsp;<%=employee.getName()%>
			<%
				}
			%></span><a
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
		if(employee!=null){
			if (employee.getPosition() != null) {
				if (employee.getPosition().getDepartment() != null) {
		%>
			<%=employee.getPosition().getDepartment().getName()%>
			<%
				}
				}
		}
			%>
		</div>

		<div id="middle">
			<div class="m_left">
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#003399;">产线信息</div>
				
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#0B2D72;margin-top: 30px;cursor:pointer;"
				onClick="$('#content').attr('src','paperless/productionLineDocumentation.jsp')">图文档资料</div>
				
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#0B2D72;margin-top: 30px;">产线报表</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
				onClick="$('#content').attr('src','paperless/productionReport.jsp')" id="deviceCheckDiv">安全绿十字</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;" 
				onClick="chooseDevice()">设备日产量</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
				onClick="$('#content').attr('src','')" id="deviceCheckDiv">设备OEE趋势</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
				onClick="$('#content').attr('src','')" id="deviceMaintainDiv">设备停机时间</div>
				<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
				onClick="$('#content').attr('src','')" id="deviceMaintainDiv">不合格记录</div>
			</div>
			<div class="m_right">
				<iframe style="height:808px;width:1420px;border-width: 0px; 
				background-color:#1C2437;backgroun-color:#1C2437;" 
				id="content"  src="paperless/productionReport.jsp"></iframe>
			</div>
		</div>
	</div>
	
	
</body>

	<!-- 全屏预览 -->
<div id="dialog-layer">
	<div style="font-size:50px;color:#EAF0F6;z-index:2000;position:fixed;top:0px;left:1870px;cursor:pointer;" 
	onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
	 onmouseout="$(this).css('font-size','50px')" >×</div>
	<div style="height:100%;width:100%;" id="fullScreenDiv">
		
	</div>
</div>