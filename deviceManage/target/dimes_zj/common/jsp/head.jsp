<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <link type="text/css" href="console/js/topjui/css/topjui.core.min.css" rel="stylesheet" />
    <link type="text/css" href="console/js/topjui/themes/default/topjui.black.css" rel="stylesheet" id="dynamicTheme"/>
    <!-- FontAwesome字体图标 -->
    <link type="text/css" href="console/js/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
      <link type="text/css" href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet"/>
    <!-- jQuery相关引用 -->
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="common/js/echarts.min.js"></script>
    <script type="text/javascript" src="common/js/echarts-gl.js"></script>
   <!--  <script type="text/javascript" src="console/js/static/plugins/echarts/echarts.min.js"></script> -->
    <script type="text/javascript" src="common/js/echarts.min.js"></script>
    <script type="text/javascript" src="common/js/echarts-gl.js"></script>
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
    <!-- TopJUI框架配置 -->
    <script type="text/javascript" src="console/js/static/public/js/topjui.config.js"></script>
    <!-- TopJUI框架核心 -->
    <script type="text/javascript" src="console/js/topjui/js/topjui.core.min.js"></script>
    <!-- TopJUI中文支持 -->
    <script type="text/javascript" src="console/js/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <!-- layui框架js -->
    <script type="text/javascript" src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="common/js/jquery.table2excel.min.js"></script>
    <script type="text/javascript" src="common/js/common.js"></script>
    <script type="text/javascript" src="common/js/datagrid-detailview.js"></script>
    <!-- 首页js -->
    <script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    </script>