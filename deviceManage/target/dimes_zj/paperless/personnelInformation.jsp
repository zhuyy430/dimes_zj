<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee) session.getAttribute("employee");
	ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css">
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
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
    	$(function(){
    		<%-- var pname="<%=request.getParameter("pname")%>";
    		console.log(pname);
    		if(pname!=""){
    			$("#paperProductionUnitName").text(pname);
    		} --%>
    		
    		var time = new Date();
    		$("#time").text(getDateTime(time));
    		$("#startDate").flatpickr();
    		$("#endDate").flatpickr();
    		employeesByProductionUnitId();
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
    	
    	flatpickr.localize(flatpickr.l10ns.zh);
    	var picName=[];
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
    	
    	function employeesByProductionUnitId(){
    		$.get("employee/queryAllEmployeesByProductionUnitId.do",{
    			productionUnitId:"<%=productionLine==null?"":productionLine.getId()%>"
    			},function(result) {
    				var html=""
   					for(var i=0;i<result.length;i++){
       					
       					var position="";
       					var photo="";
       					if(result[i].positionName!=null){
       						position=result[i].positionName
       					}
       					if(result[i].photo!=null){
       						photo=result[i].photo
       					}
       					html+="<div class='employee'>"+
   			    					"<img src='"+photo+"' style='width: 140px;height:100%;display: inline-block;float: left;'>"+
   			    					"<div style='float: right; width: 205px; height: 100%;'>"+
   			    						"<p class='font_style' style='margin-top: 20px;'>姓名:"+result[i].name+"</p>"+
   			    						"<p class='font_style' style='margin-top: 25px;'>工号:"+result[i].code+"</p>"+
   			    						"<p class='font_style' style='margin-top: 25px;'>岗位:"+position+"</p>"+
   			    						"<p class='font_style' style='margin-top: 25px;'>入职日期:"+timestampToTime(result[i].inDate)+"</p>"+
   			    					"</div>"+
   			    			  "</div>"
       					
       				}
    				
    				$("#employees").append(html);
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


.tp_button{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#9966CC;
}

.tp_button:hover{
	
}

.mb_btn{
	margin: 0 auto;
	width:340px;
	height:65px;
	display:block;
	font-size: 25px;
	background-color: #49505F;
	border: 1px solid #666666;
	color: white;
}

.p_style{
	margin: 0 auto;
	width:340px;
	height:40px;
	margin-top: 30px;
	background-color: #1C2437;
	color: #57EEFD;
	font-size: 22px;
	line-height: 40px;
}

#middle {
	height: 850px;
	width: 94%;
	margin: auto auto;
}

.font_style {
	color: white;
	font-size: 15px;
	margin-left: 5px;
}

.employee{
	float: left;
	width: 365px;
	height: 215px;
	margin-right: 90px;
	margin-top:20px;
	border: 2px solid #4CEBFF;
	padding: 5px;
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
</head>
<body>
	<div id="outer">
		<div id="loginInfo">
			<span style="margin-right: 10px;"><%
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
			<div style="width: 360px;height: 855px;float: left;background-color: #1C2437;">
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color: #CC9966">人员信息</div>
				
			</div>
			<div style="width: 1420px;height: 855px;background-color: #1C2437;float: right;margin-right: 10px;">
				<div style="overflow:auto;height: 835px;font-size: 20px;">
						<div id="employees" style="margin: auto auto;width: 1380px;height:815px;overflow: hidden;overflow-y:auto;">
							
						</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

