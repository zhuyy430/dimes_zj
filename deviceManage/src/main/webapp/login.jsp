<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<link rel="stylesheet"
	href="console/js/static/plugins/bootstrap/css/bootstrap.min.css" />
<style type="text/css">
html, body {
	height: 100%;
}

.box {
	background: url("console/js/topjui/images/loginBg.jpg") no-repeat center
		center;
	background-size: cover;
	margin: 0 auto;
	position: relative;
	width: 100%;
	height: 100%;
}

.login-box {
	width: 100%;
	max-width: 500px;
	height: 400px;
	position: absolute;
	top: 50%;
	margin-top: -200px;
}

@media screen and (min-width: 500px) {
	.login-box {
		left: 50%;
		margin-left: -250px;
	}
}

.form {
	width: 100%;
	max-width: 500px;
	height: 275px;
	margin: 2px auto 0px auto;
}

.login-content {
	border-bottom-left-radius: 8px;
	border-bottom-right-radius: 8px;
	height: 250px;
	width: 100%;
	max-width: 500px;
	background-color: rgba(255, 250, 2550, .6);
	float: left;
}

.input-group {
	margin: 30px 0px 0px 0px !important;
}

.form-control, .input-group {
	height: 40px;
}

.form-actions {
	margin-top: 30px;
}

.form-group {
	margin-bottom: 0px !important;
}

.login-title {
	border-top-left-radius: 8px;
	border-top-right-radius: 8px;
	padding: 20px 10px;
	background-color: rgba(0, 0, 0, .6);
}

.login-title h1 {
	margin-top: 10px !important;
}

.login-title small {
	color: #fff;
}

.link p {
	line-height: 20px;
	margin-top: 30px;
}

.btn-sm {
	padding: 8px 24px !important;
	font-size: 16px !important;
}

.flag {
	position: absolute;
	top: 10px;
	right: 10px;
	color: #fff;
	font-weight: bold;
	font: 14px/normal "microsoft yahei", "Times New Roman", "宋体", Times,
		serif;
}
</style>
<title>嘉兴迪筑工业技术有限公司</title>
</head>
<body>
	<div class="box">
		<div class="login-box">
			<div class="login-title text-center">
				<span class="flag"><i class="fa fa-user"></i> 用户登录</span>
				<h1>
					<small>DIMES系统</small>
				</h1>
			</div>
			<div class="login-content ">
				<div class="form">
					<form id="loginForm" class="form-horizontal" action="user/login.do"
						method="post">
						<%-- <input type="hidden" id="referer" name="referer"
							value="${param.referer}"> --%>
						<div class="form-group">
							<div class="col-xs-10 col-xs-offset-1">

								<div class="input-group">
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-user"></span></span> <input type="text"
										id="username" name="username" class="form-control"
										placeholder="用户名">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-xs-10 col-xs-offset-1">
								<div class="input-group">
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-lock"></span></span> <input
										type="password" id="password" name="password"
										class="form-control" placeholder="密码">
								</div>
							</div>
						</div>
						<span id="tip"
							style="color: red; font-size: 1.2em; width: 100%; margin-left: 100px;"></span>
						<p></p>
						<div class="form-group form-actions">
							<div class="col-xs-12 text-center">
								<sec:authorize access="hasAuthority('ADD_PROCESSRECORD')">
									<button type="button" id="login" class="btn btn-sm btn-success">
										<span class="fa fa-check-circle"></span> 登录
									</button>
								</sec:authorize>
								<button type="button" id="reset" class="btn btn-sm btn-danger">
									<span class="fa fa-close"></span>重置
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<span class="text-danger"><i class="fa fa-warning"></i>
						用户名或密码错误，请重试！</span>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		if (navigator.appName == "Microsoft Internet Explorer"
				&& (navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE6.0"
						|| navigator.appVersion.split(";")[1].replace(/[ ]/g,
								"") == "MSIE7.0" || navigator.appVersion
						.split(";")[1].replace(/[ ]/g, "") == "MSIE8.0")) {
			alert("您的浏览器版本过低，请使用360安全浏览器的极速模式或IE9.0以上版本的浏览器");
		}
	</script>
	<script type="text/javascript">
		$(function() {
			$("#login").on("click", function() {
				if (!$("#username").val()) {
					$("#tip").text("用户名不能为空!");
					return;
				}
				if (!$("#password").val()) {
					$("#tip").text("密码不能为空!");
					return;
				}
				$("#loginForm").submit();
			});
		});
	</script>
</body>
</html>