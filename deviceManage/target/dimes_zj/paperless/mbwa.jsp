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
    	var index=0;
    	var imageArray=[];
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
    		
    		$("#showPrevious").click(function(){
    			index--;
    			if(index<=0){
    				index=0;
    				$.iMessager.alert("提示","已是第一张!");
    			}else{
    				$("#mbwaImg").attr("src",imageArray[index]);
    			}
    		});
    		//左右按钮点击事件
    		$("#showNext").click(function(){
    			index++;
    			if(index>=imageArray.length-1){
    				$.iMessager.alert("提示","已是最后一张!");
    				index=imageArray.length-1;
    				return ;
    			}else{
    				$("#mbwaImg").attr("src",imageArray[index]);
    			}
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
    	
    	flatpickr.localize(flatpickr.l10ns.zh);
    	var picName=[];
    	function selectz() {
    		$("#taskTabel").html("");
			$.get(contextPath + "paperlessTask/queryTaskByCondition.do",{
				startDate:$("#startDate").val(),
				endDate:$("#endDate").val(),
				status:$("#status").val(),
				content:$("#content").val()
			},function(data){
				for(var i=0;i<data.length;i++){
					var createtime=timestampToTime(data[i].createtime);
					var status="";
					var pic="";
					if(data[i].status=="CREATE"){
						status="未完成";
					}else if(data[i].status=="COMPLETE"){
						status="已完成";
					}
					
					if(data[i].picName.length>0){
						pic="预览";
						picName=data[i].picName;
					}
					var html="<tr><th>"+(i+1)+"</th><th>走动管理</th><th>"+data[i].createUserName+"</th><th>"+createtime+"</th><th>"+data[i].manageType+"</th><th>"+data[i].description+"</th><th>"+data[i].userName+"</th><th>"+status+"</th><th onclick='showimage(picName)'>"+pic+"</th></tr>";
					$("#taskTabel").append(html);
				}
				
			});
		}
    	function showimage(e){
    		console.log(e[index]);
    		imageArray=e;
    		$("#dialog-layer").css("display","block");
    		$("#mbwaImg").attr("src",e[index]);
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


#dialog-layer{
  position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    z-index: 1000;
    display:none;
    background-color: #1C2437;
}

/**显示点检图片方向键样式*/
.imgDirection{
	height:100%;width:5%;float:left;cursor:pointer;color:#80848E;
	font-size:30px;
	text-align:center;
	margin-top:500px;
}

</style>
</head>
<body>
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
			<div style="width: 360px;height: 855px;float: left;background-color: #1C2437;">
				<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;">走动管理</div>
				<input class="mb_btn" id="content" style="margin-top: 30px;" >
				<p class="p_style">开始日期</p>
				<input class="mb_btn" id="startDate" style="margin-top: 5px;" >
				 
				<p class="p_style">结束日期</p>
				<input class="mb_btn" id="endDate" style="margin-top: 5px;" >
				<p class="p_style">状态</p>
				<select id="status" class="mb_btn" style="margin-top: 5px;" >
					<option value="CREATE">未完成</option>
					<option value="COMPLETE">已完成</option>
				</select>
				<div class="tp_button" style="width:340px;height:65px;border-radius: 20px;line-height: 65px;margin-top: 30px;" onclick="selectz()">确定
					<span class="fa fa-level-down  fa-rotate-90" aria-hidden="true"style="font-size: 25px; margin: 0 auto"></span>
				</div>
			</div>
			<div style="width: 1420px;height: 855px;background-color: #1C2437;float: right;margin-right: 10px;">
				<div style="overflow:auto;height: 835px;font-size: 20px;">
					<table>
						<thead>
							<tr>
								<th width="50"></th>
								<th>来源</th>
								<th>提出人</th>
								<th>日期</th>
								<th>管理事项</th>
								<th>内容描述</th>
								<th>责任人</th>
								<th>完成状态</th>
								<th>图片</th>
							</tr>
						</thead>
							
						<tbody id="taskTabel">
							
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

<div id="dialog-layer">
		<div style="font-size:50px;color:#EAF0F6;z-index:2000;position:fixed;top:0px;left:1870px;cursor:pointer;" 
		onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
		 onmouseout="$(this).css('font-size','50px')" >×</div>
		
			<div id="docs" style="border-radius: 15px;height:100%;overflow: hidden;
				border-color: #4F5157;">
					<div class="fa fa-chevron-left imgDirection" id="showPrevious"></div>
					<div style="height:100%;width:90%;float:left;">
						<img id="mbwaImg" alt="走动管理图片" style="width:100%;height:94%;margin-top:20px;"/>
					</div>
					<div class="fa fa-chevron-right imgDirection" id="showNext"></div>
			</div>
		
	</div>