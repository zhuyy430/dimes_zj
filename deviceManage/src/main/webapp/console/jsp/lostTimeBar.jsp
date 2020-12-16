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
<base href="<%=basePath%>" />
<meta charset="utf-8" />
<!-- 浏览器标签图片 -->
<link rel="shortcut icon" href="console/js/topjui/images/favicon.ico" />
<!-- TopJUI框架样式 -->
<link type="text/css" href="console/js/topjui/css/topjui.core.min.css"
	rel="stylesheet">
<link type="text/css"
	href="console/js/topjui/themes/default/topjui.black.css"
	rel="stylesheet" id="dynamicTheme" />
<!-- FontAwesome字体图标 -->
<link type="text/css"
	href="console/js/static/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link type="text/css"
	href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet" />
<!-- jQuery相关引用 -->
<script type="text/javascript"
	src="console/js/static/plugins/jquery/jquery.min.js"></script>
<!--  <script type="text/javascript" src="console/js/static/plugins/echarts/echarts.min.js"></script> -->

<script type="text/javascript" src="common/js/echarts.js"></script>
<script type="text/javascript"
	src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
<!-- TopJUI框架配置 -->
<script type="text/javascript"
	src="console/js/static/public/js/topjui.config.js"></script>
<!-- TopJUI框架核心 -->
<script type="text/javascript"
	src="console/js/topjui/js/topjui.core.min.js"></script>
<!-- TopJUI中文支持 -->
<script type="text/javascript"
	src="console/js/topjui/js/locale/topjui.lang.zh_CN.js"></script>
<!-- layui框架js -->
<script type="text/javascript"
	src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
<!-- 首页js -->
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
<script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	//点击产线，查询该产线的oee
    	function requestLostTime(productionUnitId){
			$.get("lostTimeRecord/queryProductionUnitRunningTimes.do",{productionUnitId:productionUnitId},function(result){
				lostTimeBarGraph(result);
    		});
    	}
    	//损时分析
    	function lostTimeBarGraph(data){
    		option = {
    				title : [{
    					text : '设备运行时间表（单位:分钟）'
    				},{
    					text:"设备数 : " + data.deviceSiteCount,
    					right:50
    				}],
    				tooltip : {
    					trigger : 'axis',
    					axisPointer : { // 坐标轴指示器，坐标轴触发有效
    						type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
    					},
    					formatter : function(params) {
    						var tar = params[1];
    						return tar.name + '<br/>' + tar.seriesName + ' : '
    								+ tar.value;
    					}
    				},
    				grid : {
    					left : '3%',
    					right : '4%',
    					bottom : '3%',
    					containLabel : true
    				},
    				xAxis : {
    					type : 'category',
    					splitLine : {
    						show : false
    					},
    					data : data.titles
    				},
    				yAxis : {
    					type : 'value',
    					min : 0
    				},
    				series : [ {
    					name : '辅助',
    					type : 'bar',
    					stack : '总量',
    					itemStyle : {
    						normal : {
    							barBorderColor : 'rgba(0,0,0,0)',
    							color : 'rgba(0,0,0,0)'
    						},
    						emphasis : {
    							barBorderColor : 'rgba(0,0,0,0)',
    							color : 'rgba(0,0,0,0)'
    						}
    					},
    					data : data.assistants
    				}, {
    					name : '',
    					type : 'bar',
    					stack : '总量',
    					label : {
    						normal : {
    							show : true,
    							position : 'inside'
    						}
    					},
    					data : data.minutes
    				} ]
    			};
    		// 基于准备好的dom，初始化echarts实例
    		var myChart = echarts.init(document.getElementById('lostTimeBarGraphBody'));
    		// 使用刚指定的配置项和数据显示图表。
    		myChart.setOption(option); 
    	}
    </script>
</head>
<body>
	<div style="width: 50%;margin:auto auto;">
		<input id="cc1" data-toggle="topjui-combobox"
			data-options="
        valueField: 'id',
        textField: 'name',
        url: 'productionUnit/queryAllProductionUnits.do',
        onLoadSuccess: function () { //加载完成后,val[0]写死设置选中第一项
                var val = $(this).combobox('getData');
                for (var item in val[0]) {
                    if (item == 'id') {
                        $(this).combobox('select', val[0][item]);
                    }
                }
            },
        onSelect: function(rec){
        	requestLostTime(rec.id);
        }">
	</div>
	<div id="lostTimeBarGraphBody" style="width: 1600px; height: 800px;"></div>
</body>
</html>