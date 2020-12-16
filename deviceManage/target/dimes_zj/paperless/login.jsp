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

<link rel="stylesheet"
	href="console/js/static/plugins/bootstrap/css/bootstrap.min.css" />
<style type="text/css">
html, body {
	height: 100%;
}

.box {
	/* background: url("console/js/topjui/images/loginBg.jpg") no-repeat center
		center; */
	background-size: cover;
	margin: 0 auto;
	position: relative;
	width: 100%;
	height: 100%;
	background-color: #0058AC;
}

.login-box {
	width: 100%;
	max-width: 450px;
	height: 400px;
	position: absolute;
	top: 50%;
	margin-top: -200px;
	background-color: #E9EEF2;
	float: left;
	margin-left: 200px;
	border-bottom-right-radius: 12px;
	border-top-right-radius: 12px;
}
.login-pic {
	width: 100%;
	max-width: 700px;
	height: 400px;
	position: absolute;
	top: 50%;
	margin-top: -200px;
	background-color: red;
	float: left;
	margin-left: 200px;
}

@media screen and (min-width: 500px) {
	.login-box {
		left: 90%;
		margin-left: -600px;
	}
	.login-pic {
		left: 60%;
		margin-left: -724px;
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
	background-color: #E9EEF2;
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
	border-top-left-radius: 12px;
	border-top-right-radius: 12px;
	padding: 20px 10px;
	background-color: #E9EEF2;
}

.login-title h1 {
	margin-top: 10px !important;
}

.login-title small {
	color: #0064C8;
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
<body onkeydown="keyLogin()">
	<div class="box">
		<span class="col-xs-12 text-center" style="color: white;float:left;font-size: 50px;margin-top: 70px">DIMES智慧工厂无纸化系统服务平台</span>
	<div  class="login-pic">
	<!-- <span style="color: white;float:left;font-size: 50px;margin-top: 150px">图片</span> -->
	<img alt="" src="paperless/img/paper.png">
	</div>
		<div class="login-box">
			<div class="login-title text-center">
				<h1>
					<small>用户登录</small>
				</h1>
			</div>
			<div class="login-content " >
				<div class="form">
					<form id="loginForm" class="form-horizontal" action="paperlessUser/login.do"
						method="post" enctype="application/json">
						<%-- <input type="hidden" id="referer" name="referer"
							value="${param.referer}"> --%>
						<div class="form-group">
							<div class="col-xs-10 col-xs-offset-1">

								<div class="input-group">
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-user"></span></span> <input type="text"
										id="username" name="username" class="form-control" required autofocus="autofocus"
										placeholder="请输入用户名">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-xs-10 col-xs-offset-1">
								<div class="input-group">
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-lock"></span></span> <input
										type="password" id="password" name="password"
										class="form-control" placeholder="请输入密码">
								</div>
							</div>
						</div>
						<span id="tip"
							style="color: red; font-size: 1.2em; width: 100%; margin-left: 100px;"></span>
						<p></p>
						<div class="form-group form-actions">
							<div class="col-xs-12 text-center">
									<button type="button" id="login" class="btn btn-sm btn-success" style="background-color: #0058AC;width: 82%">
										<!-- <span class="fa fa-check-circle" ></span> --> 登录</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div style="float: left;margin-top: 580px;width: 100%;">
			<span  style="color: white;font-size: 20px;display: block;text-align: center;">建议浏览器:Google Chrome、IE9以上、Firefox v22</span>
			<span  style="color: white;font-size: 20px;display: block;text-align: center;">技术厂商:嘉兴市迪筑工业技术有限公司</span>
			<span  style="color: white;font-size: 20px;display:block;text-align: center;">联系电话:13355835239</span> 
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
			//$("#loginForm").submit();
			$.ajax({
	            url:"paperlessUser/login.do",
	            type:"POST",
	            data:JSON.stringify($('form').serializeObject()),
	            contentType:"application/json",  //缺失会出现URL编码，无法转成json对象
	            success:function(data){
	            	if(data.success){
	            		window.location.href="paperless/homePage.jsp";
	            	}else{
	            		$("#tip").text(data.msg);
	            	}
	            }
	        });
		});
	});
	function keyLogin(){ 
	    if (event.keyCode==13)  //回车键的键值为13 
	       document.getElementById("login").click(); //调用登录按钮的登录事件 
	    } 
	</script>
</body>
</html>