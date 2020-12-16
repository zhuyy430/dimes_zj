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
<link type="text/css"
	href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css"
	href="console/jsp/deviceManagement/js/jquery-easyui-1.5.5.2/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css"
	href="console/jsp/deviceManagement/js/jquery-easyui-1.5.5.2/themes/icon.css">
<link type="text/css"
	href="console/jsp/deviceManagement/css/statistics.css" rel="stylesheet" />
<script type="text/javascript"
	src="console/jsp/deviceManagement/js/jquery-easyui-1.5.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="console/jsp/deviceManagement/js/jquery-easyui-1.5.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="console/jsp/deviceManagement/js/jquery-easyui-1.5.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="common/js/echarts.min.js"></script>
<script type="text/javascript" src="common/js/echarts-gl.js"></script>
<script type="text/javascript"
	src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
<script type="text/javascript"
	src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript"
	src="console/jsp/deviceManagement/js/jquery.pivotgrid.js"></script>
<script type="text/javascript" src="common/js/common.js"></script>
<script type="text/javascript"
	src="console/jsp/deviceManagement/js/checkOverview.js"></script>
</head>
<body>
	<div id="toolbar" style="padding: 10px;" class="layui-inline">
		<form id="queryForm" style="display: inline;">
			<div style="margin-top: 10px;">
				<label style="font-size: 14px; margin-left: 10px;">日期范围 </label>
				<div class="layui-inline">
					<input id="from" name="from" type="text" class="layui-input"
						style="height: 30px;">
				</div>
				<label style="font-size: 14px;">至</label>
				<div class="layui-inline">
					<input id="to" name="to" type="text" class="layui-input"
						style="height: 30px;">
				</div>
				<a id="query" href="javascript:void(0);" class="layui-btn" style="height:35px;width:150px;"
			onclick="queryData()">查询</a> <a onclick="exportData()"
			href="javascript:void(0);" class="layui-btn" style="height:35px;width:150px;">导出</a>
			</div>
		</form>
	</div>
	<div id="statusCountDiv"
		style="height: 500px; width: 100%; margin-top: 20px;"></div>
	<div style="width: 99.8%; overflow: auto; height: 500px;">
		<table id="pg"></table>
	</div>
</body>
</html>