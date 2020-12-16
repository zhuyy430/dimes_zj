<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee)session.getAttribute("employee");
	ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<script>
	$(function(){
		//时间
		var time = new Date();
		$("#time").text(getDateTime(time));
		//搜索工件文档
		searchWorkpieceDocs();
	});
	//退出
	function logout(){
		$.iMessager.confirm('确认对话框', '您想要退出该系统吗？', function(r){
		    if (r){
		    	$.get("paperlessUser/logout.do",function(result){
		    		if(result.success){
		    			window.location.href="paperless/homePage.jsp";
		    		}else{
		    			$.iMessager.alert("警告","退出失败，系统内部错误!");
		    		}
		    	});
		    }
		});
	}
	//点击确定按钮，查找工件文档信息
	function searchWorkpieceDocs(){
		$.get("paperlessProductionLine/queryProductionLineDocs.do",{
			productionLineId:<%=((ProductionUnit) session.getAttribute("productionLine"))==null?"":((ProductionUnit) session.getAttribute("productionLine")).getId() %>
		},function(result){
			fillDocTable(result);
		});
	}
	//填充文档信息表
	function fillDocTable(docs){
		if(docs!=null && docs.length>0){
			//tbody对象
			var $docContent = $("#docContent");
			$docContent.empty();
			for(var i = 0;i<docs.length;i++){
				var doc = docs[i];
				var $tr = $("<tr>");
				var $num = $("<td style='width:50px;'>");
				$num.append(i+1);
				$tr.append($num);
				
				var $docName = $("<td>");
				$docName.append(doc.name);
				$tr.append($docName);
				
				var $srcName = $("<td>");
				$srcName.append(doc.srcName);
				$tr.append($srcName);
				
				var $note = $("<td>");
				$note.append(doc.note);
				$tr.append($note);
				
				var $contentType = $("<td>");
				$contentType.append(doc.contentType);
				$tr.append($contentType);
				
				var $uploadDate = $("<td>");
				$uploadDate.append(getDateTime(new Date(doc.uploadDate)));
				$tr.append($uploadDate);
				
				var $preview = $("<td>");
				var $a = $("<a style='text-decoration:none;cursor:pointer;' href='javascript:void(0)'>");
				$a.append("<span class='fa fa-eye' onClick='preview(\""+doc.url+"\")'> 预览</span>");
				$preview.append($a);
				$tr.append($preview);
				
				$docContent.append($tr);
			}
		}else{
            var $docContent = $("#docContent");
            $docContent.empty();
            var $tr = $("<tr style='text-align: center ;border: white 1px solid;line-height: 50px;height: 50px;'>");
            var $td = $("<td colspan='7'>");
            $td.append("未搜索到文档");
            $tr.append($td);
            $docContent.append($tr);
		}
	}
	var docUrl = '';
	//文档预览
	function preview(url){
		//显示文档内容的iframe对象
		var $previewDiv = $("#previewDiv");
		$previewDiv.empty();
		var suffix = '';
		//截取后缀
		if(url!=null && $.trim(url)!=''){
			 var point = url.lastIndexOf("."); 
			 suffix = url.substr(point+1);
		}else{
			return false;
		}
		var $docIframe=$("<iframe style='width:100%;height:100%;'>");
		switch(suffix){
		case "pdf":
		case "PDF":{
			$docIframe.attr("src",url);
			$previewDiv.append($docIframe);
			break;
		}
		<%-- case "doc":
		case "docx":
		case "DOC":
		case "DOCX":
		case "xls":
		case "xlsx":
		case "XLS":
		case "XLSX":
		case "ppt":
		case "pptx":
		case "PPT":
		case "PPTX":{
			$docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
			$previewDiv.append($docIframe);
			break;
		} --%>
		case "png":
		case "PNG":
		case "jpg":
		case "JPG":
		case "JPEG":
		case "jpeg":
		case "gif":
		case "GIF":
		case "bmp":
		case "BMP":{
			var $img = $("<img>");
			$img.attr("src",url);
			$previewDiv.append($img);
			break;
		}
		default:{
			var $p = $("<p style='font-size:20px;color:red;'>");
			$p.append(suffix + "不支持预览!");
			$previewDiv.append($p);
			return false;
		}
		}
		
		docUrl = url;
	}
	//全屏查看
	function showFullScreen(){
		parent.showFullScreen(docUrl);
	}
</script>
<style>
#dialog-layer{
  position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    z-index: 1000;
    display:none;
}
body {
	margin: 0;
}
#outer {
	height: 1080px;
	width: 1920px;
	background-image: url('front/imgs/blue.png');
	padding-top: 40px;
}
#loginInfo {
	text-align: center;
	width: 100%;
	height: 40px;
	text-align: right;
	color: #57EEFD;
	margin-right:50px;
	margin-bottom: 0px;
	position: absolute;
	left:-20px;
	top:20px;
	font-size:20px;
	z-index: 999;
}
#title {
	text-align: center;
	width: 100%;
	height: 50px;
	line-height: 50px;
	color: #57EEFD;
}
#time {
	width: 100%;
	height: 75px;
	text-align: center;
	line-height: 98px;
	color: #57EEFD;
	font-size: 25px;
}
#productionUnit{
	height: 50px;
	font-size: 20px;
	color: #57EEFD;
	float: left;
	margin-left: 60px;
	margin-top: -80px;
	font-weight: bold;
}

#department{
	height: 50px;
	font-size: 20px;
	color: #57EEFD;
	float: right;
	margin-right: 60px;
	margin-top: -80px;
	font-weight: bold;
}

#main{
	height: 860px;
	width: 1800px;
	margin:0 auto;
}
 #left{
	height:100%;
	width:18%;
	float:left;
	background-color:#1C2437;
}
#right{
	height:100%;
	width:81.5%;
	float:right;
	background-color:#18192D;
} 

.leftDiv{
	height:60px;
	width:92%;
	margin-top:30px;
	margin-left:auto;
	margin-right:auto;
}

td,th{
	border:white 1px solid;
	color:white;
	font-size:14px;
	text-align:center;
	height:50px;
}
</style>
</head>
<body style="background-color: #18192D;">	
			<div style="width: 1415px;height: 805px;">
				<div id="top" style="height:39%;width:100%;background-color:#1C2437;margin-bottom:10px;margin-top:0px;
				padding-top:5px;overflow: auto;">
					<table style="width:98%;margin-left:auto;margin-right:auto;">
						<thead>
							<tr>
								<th style="width: 50px;"></th>
								<th>文件名称</th>
								<th>源文件名称</th>
								<th>说明</th>
								<th>文档类型</th>
								<th>上传时间</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody id="docContent">
						</tbody>
					</table>
				</div>
				<div id="bottom" style="height:60%;width:100%;background-color:#1C2437;overflow: auto;">
					<div style="text-align: right;height:5%;width:100%;line-height:5%;">
						<span style="color:white;margin-top:5px;margin-right:10px;height:100%;cursor:pointer;"
						class="fa fa-window-maximize" onClick="showFullScreen()"> 全屏查看</span>
					</div>
					<div style="width:100%;height:95%;text-align: center;" id="previewDiv">
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 全屏预览 -->
	<div id="dialog-layer">
		<div style="font-size:50px;color:#EAF0F6;z-index:2000;position:fixed;top:0px;left:1870px;cursor:pointer;" 
		onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
		 onmouseout="$(this).css('font-size','50px')" >×</div>
		<div style="height:100%;width:100%;" id="fullScreenDiv">
		
		</div>
	</div>
</body>
</html>