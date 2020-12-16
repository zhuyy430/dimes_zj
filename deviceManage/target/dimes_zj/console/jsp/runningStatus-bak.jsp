<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>"/>
<meta charset="utf-8"/>
 <!-- 浏览器标签图片 -->
    <link rel="shortcut icon" href="console/js/topjui/images/favicon.ico"/>
    <!-- TopJUI框架样式 -->
    <link type="text/css" href="console/js/topjui/css/topjui.core.min.css" rel="stylesheet">
    <link type="text/css" href="console/js/topjui/themes/default/topjui.black.css" rel="stylesheet" id="dynamicTheme"/>
    <!-- FontAwesome字体图标 -->
    <link type="text/css" href="console/js/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
      <link type="text/css" href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet"/>
    <!-- jQuery相关引用 -->
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.min.js"></script>
   <!--  <script type="text/javascript" src="console/js/static/plugins/echarts/echarts.min.js"></script> -->
   
    <script type="text/javascript" src="common/js/echarts.js"></script>
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
    <!-- TopJUI框架配置 -->
    <script type="text/javascript" src="console/js/static/public/js/topjui.config.js"></script>
    <!-- TopJUI框架核心 -->
    <script type="text/javascript" src="console/js/topjui/js/topjui.core.min.js"></script>
    <!-- TopJUI中文支持 -->
    <script type="text/javascript" src="console/js/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <!-- layui框架js -->
    <script type="text/javascript" src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
    <!-- 首页js -->
    <script type="text/javascript" src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
    <script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	$(function(){
    		$.get("deviceSite/queryDeviceSiteRunningStatus.do",function(result){
    			//设置运行数和停机数
    			$("#runningCount").text(result.runningCount );
    			$("#haltCount").text(result.haltCount );
    			$("#warnningCount").text(result.warnningCount);
    			//饼图
    			runningStatus(result);
    		});
    	});
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
    			        data: ['运行', '停机']
    			    },
    			    series : [
    			        {
    			            type: 'pie',
    			            radius : '65%',
    			            center: ['50%', '50%'],
    			            selectedMode: 'single',
    			            data:[
    			                {value:data.runningCount, name: '运行'},
    			                {value:data.haltCount, name: '停机'}
    			            ],
    			            itemStyle: {
    			                emphasis: {
    			                    shadowBlur: 10,
    			                    shadowOffsetX: 0,
    			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
    			                }
    			            }
    			        }
    			    ]
    			};

    		myCharts.setOption(option);
    	}
    </script>
</head>
<body>
	<div id="main" style="width:1920px;height:1080px;background-color:#6495ED;">
		<div style="height:20px;width:100%;"></div>
		<div id="title" style="width:100%;height:5%; border-bottom: solid 1px gray">
				<span style="font-size:3em;margin-left:30px;line-height:90%;font-weight: bold;">嘉兴迪筑集团数字化车间监控系统</span>
		</div>
		<div id="body" style="width:100%;height:90%;padding-left: 20px;">
			<div id="left" style="width:19%;height:100%;float:left;">
				<div style="height:3%;width:95%;margin:0 auto;font-size:18px;">实时数据</div>
				<div style="height:57%;width:95%;margin:0 auto;border:solid 1px gray">
					<div style="height:3%;width: 96%;margin: 0 auto;">运行数</div>
					<div style="height:30%;width: 96%;margin: 0 auto;border-bottom: solid 1px gray;text-align: right;color:blue;">
						<span id="runningCount" style="font-size: 10em;padding-right:30px;width:100%;"></span><span style="font-size:5em;">台</span>
					</div>
					<div style="height:3%;width: 96%;margin: 0 auto;">停机数</div>
					<div style="height:30%;width: 96%;margin: 0 auto;border-bottom: solid 1px gray;text-align: right;color:blue;">
						<span id="haltCount" style="font-size: 10em;padding-right:30px;width:100%;"></span><span style="font-size:5em;">台</span>
					</div>
					<div style="height:3%;width: 96%;margin: 0 auto;">总报警数</div>
					<div style="height:30%;width: 96%;margin: 0 auto;text-align: right;color:yellow;">
						<span id="warnningCount" style="font-size: 10em;padding-right:30px;width:100%;"></span><span style="font-size:5em;">次</span>
					</div>
				</div>
				<div style="height:3%;width:95%;margin:0 auto;font-size:18px;">报警信息</div>
				<div style="height:37%;width:95%;margin:0 auto;border:solid 1px gray">
					
				</div>
			</div>
			<div id="center" style="width:60%;height:100%;float:left">
				<div style="height:3%;width:95%;margin:0 auto;font-size:18px;">车间布局图</div>
				<div style="height:97%;width:95%;margin:0 auto;border:solid 1px gray"></div>
			</div>
			<div id="right" style="width:19%;height:100%;float:left">
				<div style="height:3%;width:95%;margin:0 auto;font-size:18px;">实时占比饼图</div>
				<div style="height:57%;width:95%;margin:0 auto;border:solid 1px gray" id="statusPie"></div>
				<div style="height:3%;width:95%;margin:0 auto;font-size:18px;">状态比例TOP图</div>
				<div style="height:37%;width:95%;margin:0 auto;border:solid 1px gray"></div>
			</div>
		</div>
	</div>
</body>
</html>