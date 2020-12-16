<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link type="text/css"
	href="console/js/static/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<link rel="stylesheet"
	href="mc/assets/js/bootstrap-table/bootstrap-table.min.css">
<link href="mc/assets/js/bootstrap3-editable/css/bootstrap-editable.css"
	rel="stylesheet">

<link rel="stylesheet" href="mc/assets/css/bootstrap-select.min.css">
<link rel="stylesheet" href="mc/assets/css/bootstrap-timepicker.min.css"> 
<link rel="stylesheet"
	href="mc/assets/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" type="text/css" href="mc/assets/css/easyui.css">
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">
<!-- <script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js" type="text/javascript"></script> -->
<script type="text/javascript" src="console/js/static/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="common/js/echarts.js"></script>
<script type="text/javascript" src="common/js/echarts-gl.js"></script>
<!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
<!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
<!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="common/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="mc/assets/js/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript"
	src="mc/assets/js/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript"
	src="mc/assets/js/bootstrap-select.js"></script>
<script type="text/javascript"
	src="mc/assets/js/bootstrap-datetimepicker.min.js"></script>  
	<script type="text/javascript"
	src="mc/assets/js/bootstrap-timepicker.min.js"></script> 
<script type="text/javascript"
	src="mc/assets/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript" src="mc/assets/js/main.js"></script>
<script
	src="mc/assets/js/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
<script
	src="mc/assets/js/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>
<script type="text/javascript" src="mc/assets/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="mc/assets/js/easyui-lang-zh_CN.js"></script>
<!-- <link rel="stylesheet" href="mc/assets/css/style1920-1080.css">
<link rel="stylesheet" href="mc/assets/css/style1024-768.css">  -->
<link rel="stylesheet" type="text/css" href="mc/assets/css/style1024-768.css" media="screen and (width:1024px)"/>
<link rel="stylesheet" type="text/css" href="mc/assets/css/style1920-1080.css" media="screen and (width:1920px)"/> 


<script type="text/javascript">
var longTableHeight;	//pressLight  packManage  ngEntry  lostTime deviceModule
var shortTableHeight;	//measuringMapping employeeMapping work


    	var contextPath = "<%=basePath%>";
    	$(function(){
    		if (screen.width == 1920){
    	    	longTableHeight=540;
    	     	shortTableHeight=500;
    	    }else  if (screen.width == 1024){
    	    	longTableHeight=370;
    	    	shortTableHeight=330;
    	   }else{
    		   longTableHeight=540;
   	     	shortTableHeight=500;
    	   }
    		
    		
    		if($("span").hasClass("fa")){
    			$("span").css("-webkit-font-smoothing", "antialiased");
    		}
    		//实现对字符码的截获，keypress中屏蔽了这些功能按键
    		 document.onkeypress = banBackSpace;
    		 //对功能按键的获取
    		 document.onkeydown = banBackSpace;
    	});
    	$.ajaxSetup({cache:false});
    	
    	function banBackSpace(e){
    		 var ev = e || window.event;
    		 //各种浏览器下获取事件对象
    		 var obj = ev.relatedTarget || ev.srcElement || ev.target ||ev.currentTarget;
    		 //按下Backspace键
    		 if(ev.keyCode == 8||ev.keyCode == 116||ev.keyCode ==112||(event.ctrlKey)&&(event.keyCode==78)||(event.shiftKey)&&(event.keyCode==121)||(window.event.altKey)&&(window.event.keyCode==115)
    				 ||(event.ctrlKey && event.keyCode==82)||(window.event.altKey)&&((window.event.keyCode==37)||(window.event.keyCode==39))){
    		 var tagName = obj.nodeName //标签名称
    		 //如果标签不是input或者textarea则阻止Backspace
    		 if(tagName!='INPUT' && tagName!='TEXTAREA'){
    		  return stopIt(ev);
    		 }
    		 var tagType = obj.type.toUpperCase();//标签类型
    		 //input标签除了下面几种类型，全部阻止Backspace
    		 if(tagName=='INPUT' && (tagType!='TEXT' && tagType!='TEXTAREA' && tagType!='PASSWORD')){
    		  return stopIt(ev);
    		 }
    		 //input或者textarea输入框如果不可编辑则阻止Backspace
    		 if((tagName=='INPUT' || tagName=='TEXTAREA') && (obj.readOnly==true || obj.disabled ==true)){
    		  return stopIt(ev);
    		 }
    		 }
    		}
    		function stopIt(ev){
    		 if(ev.preventDefault ){
    		 //preventDefault()方法阻止元素发生默认的行为
    		 ev.preventDefault();
    		 }
    		 if(ev.returnValue){
    		 //IE浏览器下用window.event.returnValue = false;实现阻止元素发生默认的行为
    		 ev.returnValue = false;
    		 }
    		 return false;
    		}
</script>
<style>

</style>
</head>
<body><!-- style="margin-left: 20%;margin-right: 21%" -->
	<div class="container-fluid container-top">
		<!-- <span class="glyphicon glyphicon-align-justify" aria-hidden="true"
			style="font-size: 35px" onclick="openLogin()" data-toggle="modal" data-target="#myModal"></span> -->
		<span class="fa fa-bars container-top-bars" aria-hidden="true"
			id="sysSet" data-toggle="modal" data-target="#adminLoginDialog"></span>
	</div>
	<!-- 系统管理员登录dialog -->
	<div class="modal fade loadingModal" id="adminLoginDialog"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document" style="width: 400px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>

				<div class="container-fluid" style="text-align: center;">
					<form class="form-signin" id="adminLoginForm">
						<div class="loginimage">
							<span class="fa fa-user-circle" aria-hidden="true"
								style="font-size: 80px"></span>
						</div>
						<input type="text" id="adminname" class="form-control"
							style="margin-top: 10px;" name="username" placeholder="输入管理员账号"
							required autofocus> <input type="password"
							id="adminpassword" class="form-control" name="password"
							placeholder="输入管理员密码" required style="margin-top: 6px;">
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								style="width: 100px;" onclick="adminLogin()">登录</button>

						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 警告框 -->
	<div class="modal fade loadingModal" role="dialog"
		style="z-index: 1111;" id="alertDialog" data-backdrop="static">
		<div class="modal-dialog" role="document" style="width: 300px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="alertTitle">警告</h4>
				</div>
				<div class="modal-body">
					<p id="alertText"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>