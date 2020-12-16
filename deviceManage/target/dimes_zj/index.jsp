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
    <link rel="shortcut icon" href="console/js/topjui/images/favicon.ico"/>
    <title>DIMES</title>
    <link type="text/css" href="console/js/topjui/css/topjui.core.min.css" rel="stylesheet">
    <link type="text/css" href="console/js/topjui/themes/default/topjui.black.css" rel="stylesheet" id="dynamicTheme"/>
    <link type="text/css" href="console/js/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
      <link type="text/css" href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet"/>
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="common/js/echarts.js"></script>
    <script type="text/javascript" src="common/js/echarts-gl.js"></script>
    <script type="text/javascript" src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="console/js/static/public/js/topjui.config.js"></script>
    <script type="text/javascript" src="console/js/topjui/js/topjui.core.min.js"></script>
    <script type="text/javascript" src="console/js/topjui/js/locale/topjui.lang.zh_CN.js"></script>
    <script type="text/javascript" src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
   <script src="console/js/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="console/js/static/plugins/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript">
    	$(function(){
    		$("#cancelBtn").click(function(){
    			$('#loginForm')[0].reset(); 
    		});
    		
    	});
    </script>
    <style>
        input[type=text], input[type=password] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            opacity: 0.8;
        }
        .cancelbtn {
            width: auto;
            padding: 10px 18px;
            background-color: #f44336;
        }
        .imgcontainer {
            text-align: center;
            margin: 24px 0 12px 0;
            position: relative;
        }
        img.avatar {
            width: 40%;
            border-radius: 50%;
        }
        .container {
            padding: 16px;
        }
        span.psw {
            float: right;
            padding-top: 16px;
        }
        .modal {
            display: block; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0,0,0); /* Fallback color */
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            padding-top: 60px;
        }
        .modal-content {
            background-color: #fefefe;
            margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
            border: 1px solid #888;
            width: 500px; /* Could be more or less, depending on screen size */
        }
        .close {
            position: absolute;
            right: 25px;
            top: 0;
            color: #000;
            font-size: 35px;
            font-weight: bold;
        }
        .close:hover,
        .close:focus {
            color: red;
            cursor: pointer;
        }
        .animate {
            -webkit-animation: animatezoom 0.6s;
            animation: animatezoom 0.6s
        }
        @-webkit-keyframes animatezoom {
            from {-webkit-transform: scale(0)}
            to {-webkit-transform: scale(1)}
        }
        @keyframes animatezoom {
            from {transform: scale(0)}
            to {transform: scale(1)}
        }
        @media screen and (max-width: 300px) {
            span.psw {
                display: block;
                float: none;
            }
            .cancelbtn {
                width: 100%;
            }
        }
    </style>
    <script type="text/javascript">
        // 获取模型
        var modal = document.getElementById('id01');
        // 鼠标点击模型外区域关闭登录框
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    </script>
</head>
<body>
<div class="container-fluid" style="">
    <div class="row-fluid" style="width: 100%;height: 65px;">
        <div class="span12">
            <div class="row-fluid">
                <div class="span6" style="float: left;margin-left: 100px;margin-top: 5px;">
                </div>
                 <div class="span6" style="float: right;margin-right: 100px;margin-top: 15px">
                    <button class="layui-btn" onclick="document.getElementById('id01').style.display='block'" style="width:150px;">登录</button>
                </div>
                  <div class="span6" style="float: right;margin-right: 20px;margin-top: 15px">
                    <button class="layui-btn" onclick="document.location.href='mc/main.jsp'" style="width:150px;">MC端</button>
                </div>
                <div class="span6" style="float: right;margin-right: 20px;margin-top: 15px">
                    <button class="layui-btn" onclick="document.location.href='board.jsp'" style="width:150px;">产线</button>
                </div>
                <div class="span6" style="float: right;margin-right: 20px;margin-top: 15px">
                    <button class="layui-btn" onclick="document.location.href='carousel.jsp'" style="width:150px;">车间看板</button>
                </div>
            </div>
        </div>
    </div>
    <div style="width: 100%;height: 460px;background-color: #999999;text-align:center;padding-top: 30px;">
        <h1 style="font-size: 66px;font-family:宋体;color: #ffffff; ">启迪远航 &nbsp;创筑未来</h1>
        <h1 style="font-size: 66px;font-family:Arial;color: #ffffff; ">Move products forward</h1>
        <p style="font-size: 20px; font-weight: 400;  font-family: Arial; color: rgb(255, 255, 255);">DigitZone is the easiest way for production team<br /> to track their workpieces and get results.</p>
    </div>
    <div style="width: 100%;background-color: #ffffff;text-align:center">
        <div style="width: 100%;height:30px"></div>
        <i class="fa fa-heart" style="font-size:  76px;"></i>
        <p style="font-size: 40px; font-weight: 400;  font-family: Arial; color: #000000;">Great manufacturing<br />
         execution get great results with<br /> DigitZone</span></p>
    </div>
    <div id="id01" class="modal">
        <form class="modal-content animate" id="loginForm" method="post" action="login">
            <div class="imgcontainer">
                <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
                <img src="front/imgs/img_avatar.png" alt="Avatar" class="avatar">
            </div>
            <div class="container" style="width: 498px;">
                <label><b>Username</b></label>
                <input type="text" placeholder="请输入用户名" id="username" name="username" required autofocus="autofocus" />
                <label><b>Password</b></label>
                <input type="password" placeholder="请输入密码"  id="password" name="password" required>
                <button type="submit"  style="width:49%;">登录</button>
                <button type="button" id="cancelBtn" style="width:49%;background-color:#F4A460;">重置</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>