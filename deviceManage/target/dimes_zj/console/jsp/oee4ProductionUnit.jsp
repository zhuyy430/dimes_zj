<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
	function requestOee(productionUnitId) {
		$.get("oee/queryOee4ProductionUnit.do", {
			productionUnitId : productionUnitId
		}, function(result) {
			oeeGraph(result);
		});
	}
	//显示oee信息
	function oeeGraph(result) {
		var series = new Array();
		var colorArray = new Array();
		colorArray.push('#8FBC8F');
		colorArray.push('#F00');
		colorArray.push('#00F');
		for (var i = 0; i < result.classes.length; i++) {
			var jsonObj = {
				name : result.classes[i].name,
				type : 'bar',
				itemStyle : {
					normal : {
						color : colorArray[i],
						label : {
							show : false,
							position : 'top',
							formatter : '{c}'
						}
					}
				},
				//data:[50, 75, 100, 150, 200, 250, 150, 100, 95, 160, 50, 45]
				data : result.oeeList[i]
			}

			series[i] = jsonObj;
		}
		series.push({
			name : '平均值',
			type : 'line',
			itemStyle : {
				normal : {
					color : '#888'
				}
			},
			data : result.avgs
		});

		series.push({
			name : '目标',
			type : 'line',
			itemStyle : {
				normal : {
					color : '#008000',
				}
			},
			data : result.goalOeeList
		});
		var jsonStr = JSON.stringify(series);
		var option = {
			title : {
				show : true,
				text : 'oee'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : result.classes
			},
			xAxis : [ {
				type : 'category',
				data : result.days
			} ],
			yAxis : [ {
				type : 'value',
				name : 'oee值/百分比',
				min : 0,
				max : 100,
				interval : 10,
				axisLabel : {
					formatter : '{value}% '
				}
			} ],
			series : series
		};

		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('oeeGraphBody'));
		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}
</script>
</head>
<body>
	<div style="width: 50%; margin: auto auto;">
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
        	requestOee(rec.id);
        }">
	</div>
	<div id="oeeGraphBody" style="width: 1800px; height: 800px;"></div>
</body>
</html>