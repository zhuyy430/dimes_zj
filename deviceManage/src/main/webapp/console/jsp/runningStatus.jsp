<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<link
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
	<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 <script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	//绘制设备运行状态
    	function drawRunningStatus(deviceSites){
    		if(deviceSites&&deviceSites.length>0){
    			var divs = $("#layoutGraph div");
    			for(var i = 0;i<deviceSites.length;i++){
    				var deviceSite = deviceSites[i];
    				if(i<divs.length){
    					$(divs[i]).attr("id",deviceSite.code);	
    				}
    				switch(deviceSite.status){
    				case "0":{
    					$("#" + deviceSite.code).css("background-color","#0F0");
    					break;
    				}
    				case "1":{
    					$("#" + deviceSite.code).css("background-color","yellow");
    					break;
    				}
    				case "2":{
    					$("#" + deviceSite.code).css("background-color","#F00");
    					break;
    				}
    				}
    			}
    		}
    	}
    	$(function(){
    		 //鼠标滑过弹出框
    		  $("#layoutGraph div").mouseover(function(){
    			  $.get(contextPath + "deviceSite/queryDeviceSiteByCode.do",{deviceSiteCode:$(this).attr("id")},function(data){
    				 if(data){
    					 var options = {
    							 content:function(){
    								 var result = $("<div style='z-index:999;width:200px;height:100px;background-color:#F9F9F9;'>");
    									result.append("<p>站点代码：" + data.code + "</p>");
    									result.append("<p>站点名称:" + data.name + "</p>");
    									result.append("<p>设备代码:" + data.device.code + "</p>");
    									result.append("<p>设备名称:" + data.device.name);
    								return  result;
    							 },
    							 html:true,
    							 trigger:'hover',
    							 placement:'right'
    					 };
	    				 $("#" + data.code).popover(options);
    				 }
    			 }); 
    		 }); 
    		 //设备运行状态
    		$.get("deviceSite/queryDeviceSiteRunningStatus.do",function(result){
    			drawRunningStatus(result.deviceSites);
    			//设置运行数和停机数
    			$("#runningCount").text(result.runningCount );
    			$("#haltCount").text(result.haltCount );
    			$("#warnningCount").text(result.warnningCount);
    			//饼图
    			runningStatus(result);
    			//报警信息
    			warnning(result.records);
    		});
    		
    		$.get("oee/queryOperationRateTop.do",function(data){
    			topSort(data);
    		});
    		
    		var time = new Date();
    		$("#time").text(
    				time.getFullYear() + "-" + (time.getMonth()+1) + "-"
    						+ time.getDate());
    	});
    	//运行率TOP
    	function topSort(data){
    		var $tbody = $("#topSort");
    		if(data.placing){
    			for(var i = 0;i<data.placing.length;i++){
    				var placing = data.placing[i];
    				var deviceSiteName = data.deviceSiteNames[i];
    				var $row = $("<tr>");
    				var $td = $("<td>");
    				
    				$td.append(placing +"  " +deviceSiteName);
    				$row.append($td);
    				$tbody.append($row);
    			}
    		}
    	}
    	//报警信息 
    	function warnning(data){
    		var $tbody = $("#warnning");
    		if(data){
    			for(var i = 0;i<data.length;i++){
    				var pressLightRecord = data[i];
    				var $row = $("<tr>");
    				var $td0 = $("<td>");
    				var $td1 = $("<td>");
    				var $td2 = $("<td>");
    				
    				$td0.append(pressLightRecord.deviceSite.name);
    				$td1.append(pressLightRecord.halt?"是":"否");
    				var time = pressLightRecord.pressLightTime;
    				var timeObj = new Date(time);
    				$td2.append(timeObj.getHours() + ":" + timeObj.getMinutes());
    				
    				
    				$row.append($td0);
    				$row.append($td1);
    				$row.append($td2);
    				
    				$tbody.append($row);
    			}
    		}
    	}
    	//饼图 
    	function runningStatus(data){
    		var myCharts = echarts.init(document.getElementById("statusPie"));
    		option = {
    			    tooltip : {
    			        trigger: 'item',
    			        formatter: " {c} ({d}%)"
    			    },
    			    legend: {
    			        bottom: 10,
    			        left: 'center',
    			        data: ['运行', '停机'],
    			        textStyle:{
    			        	color:'#FFF'
    			        }
    			    },
    			    series : [
    			        {
    			            type: 'pie',
    			            radius : '65%',
    			            center: ['50%', '50%'],
    			            selectedMode: 'single',
    			            data:[
    			                {value:data.runningCount, name: '运行',label:{color:'#FFF'}},
    			                {value:data.haltCount, name: '停机',label:{color:'#FFF'}}
    			            ],
    			            itemStyle: {
    			                emphasis: {
    			                    shadowBlur: 10,
    			                    shadowOffsetX: 0,
    			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
    			                }
    			            },
    			            color:[
    			            	'#0F0','#F00'
    			        ]
    			        }
    			    ]
    			};

    		myCharts.setOption(option);
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
	color: #57EEFD;
}

#time {
	width: 100%;
	height: 90px;
	text-align: center;
	line-height: 98px;
	color: #57EEFD;
	font-size: 25px;
}

#middle {
	height: 850px;
	width: 90%;
	margin: auto auto;
}

#innerMiddle {
	height: 850px;
	width: 700px;
	background-color: #1C2437;
	float: left;
}

.sides {
	height: 100%;
	width: 500px;
}

.left {
	float: left;
	margin-right: 14px;
}

.right {
	float: right;
}

.title {
	height: 40px;
	width: 100%;
	color: #57EEFD;
	font-size: 25px;
	margin-left: 10px;
}

.top {
	height: 560px;
	width: 100%;
	margin-bottom: 20px;
	background-color: #1C2437;
	
}
.top_content {
	height: 520px;
	width: 100%;
	border: solid 1px gray;
}

.bottom {
	height: 270px;
	width: 100%;
	background-color: #1C2437;
}
.bottom_content {
	height: 230px;
	width: 100%;
}

.left_title{
	height:20px;
	width:100%;
	color:#8AD5DA;
	margin-left:20px;
	font-size:18px;
}
.left_content{
	height:150px;
	width:90%;
	color:#1E8FF3;
	text-align: right;
	margin:0 auto;
	border-bottom: solid 1px gray;
}
.left_content span{
	text-align: right;
	font-size:5em;
}
.left_content span+span{
	font-size:3em;
}


table{
	width:95%;
	margin:auto auto;
}

table thead tr{
	border-bottom: solid 1px gray;
	color:#5D5F6A;
}
tr{
	height:30px;
}

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
		<div id="blank"></div>
		<div id="title">设备运行状态</div>
		<div id="time"></div>

		<div id="middle">
			<div class="sides left">
				<div class="top">
					<div class="title">实时数据</div>
					<div
						class="top_content">
						<div class="left_title">运行数</div>
						<div class="left_content">
							<span id="runningCount"></span>
							<span>台</span>
						</div>
						<div class="left_title">停机数</div>
						<div class="left_content">
							<span id="haltCount"></span>
							<span>台</span>
						</div>
						<div class="left_title">总报警数</div>
						<div class="left_content" style="color:#EFFE0C;">
							<span id="warnningCount"></span>
							<span>次</span>
						</div>
					</div>
				</div>
				<div class="bottom">
					<div class="title">报警信息</div>
					<div class="bottom_content">
						<table>
							<thead>
								<tr>
									<td>站点名称</td>
									<td>是否停机</td>
									<td>时间</td>
								</tr>
							</thead>
							<tbody id="warnning"></tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="innerMiddle">
				<div class="title">车间布局图</div>
				<div id="layoutGraph" style="width:700px;height:810px;overflow:hidden;background-image:url('front/imgs/productionUnit.png');background-repeat:no-repeat; background-size:100% 100%; ">
					<div  style="left: 200px;top: 45px;"></div>
					<div  style="left: 450px;top: 45px;vertical-align: top;"></div>
					<div  style="left: 120px;top: 180px;"></div>
					<div  style="left: 100px;top: 400px;"></div>
					<div  style="left: 70px;top: 630px;"></div>
					<div  style="left: 430px;top: 180px;vertical-align: top;"></div>
					<div style="left: 400px;top: 400px;vertical-align: top;"></div>
					<div  style="left: 380px;top: 630px;vertical-align: top;"></div>
				</div>
			</div>
			<div class="sides right">
				<div  class="top">
					<div class="title">TOP占比-饼图</div>
					<div class="top_content" id="statusPie"></div>
				</div>
				<div class="bottom">
					<div class="title">占比信息</div>
					<div class="bottom_content">
						<table>
							<thead>
								<tr>
									<td style="text-align: center;">运行率TOP</td>
								</tr>
							</thead>
							<tbody id="topSort"></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>