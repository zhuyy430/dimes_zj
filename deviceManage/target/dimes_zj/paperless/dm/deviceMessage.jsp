<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee) session.getAttribute("employee");
	ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css">

<link rel="stylesheet" href="../mc/assets/css/bootstrap-timepicker.min.css"> 
<link rel="stylesheet" type="text/css" href="paperless/css/bootstrap-select.min.css">
<script type="text/javascript" src="paperless/js/bootstrap-select.min.js"></script>
<script type="text/javascript"
	src="../mc/assets/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
 <script type="text/javascript">
    	var contextPath = "<%=basePath%>";
    	var employee="<%=employee%>"
   		//存储设备编码
   		var deviceCode = '';
    	$(function(){
    		deviceCode = parent.$("#deviceCode").val();
    		var time = new Date();
    		$("#time").text(getDateTime(time));
    		$("#startDate").flatpickr();
    		$("#endDate").flatpickr();
    		loadDoc();
    		loadDeviceMessage();
    	});
    	flatpickr.localize(flatpickr.l10ns.zh);
    	function timestampToTime(timestamp) {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            var Y = date.getFullYear() + '-';
            var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            var D = date.getDate() + ' ';
            var h = date.getHours() + ':';
            var m = date.getMinutes() + ':';
            var s = date.getSeconds();
            return Y+M+D+h+m+s;
        }
    	//退出
    	function logout() {
    		$.iMessager.confirm('确认对话框', '您想要退出该系统吗？', function(r) {
    			if (r) {
    				$.get("paperlessUser/logout.do", function(result) {
    					if (result.success) {
    						window.location.href = "paperless/homePage.jsp";
    					} else {
    						$.iMessager.alert("警告", "退出失败，系统内部错误!");
    					}
    				});
    			}
    		});
    	}
    	
    	function loadDeviceMessage(){
    		$.get("paperlessDevice/queryDeviceByCode.do",{
    			name:"code",
    			value:deviceCode
    		},function(result){
    			$("#deviceCode").val(result.code);
    			$("#deviceName").val(result.name);
    			$("#deviceUnitType").val(result.unitType);
    			if(result.projectType!=null){
    				$("#deviceType").val(result.projectType.name);
    			}else{
    				$("#deviceType").val("");
    			}
    			$("#outFactoryCode").val(result.outFactoryCode);
    			$("#manufacturer").val(result.manufacturer);
    			$("#trader").val(result.trader);
    			$("#status").val(result.status);
    			if(result.outFactoryDate!=null){
    				$("#outFactoryDate").val(timestampToTime(result.outFactoryDate));
    			}else{
    				$("#outFactoryDate").val("");
    			}
    			if(result.inFactoryDate!=null){
    				$("#inFactoryDate").val(timestampToTime(result.inFactoryDate));
    			}else{
    				$("#inFactoryDate").val("");
    			}
    			if(result.installDate!=null){
    				$("#installDate").val(timestampToTime(result.installDate));
    			}else{
    				$("#installDate").val("");
    			}
    			if(result.checkDate!=null){
    				$("#checkDate").val(timestampToTime(result.checkDate));
    			}else{
    				$("#checkDate").val("");
    			}
    			
    			$("#assetNumber").val(result.assetNumber);
    			$("#weight").val(result.weight);
    			$("#shapeSize").val(result.shapeSize);
    			$("#installPosition").val(result.installPosition);
    			$("#power").val(result.power);
    			$("#actualPower").val(result.actualPower);
    			$("#note").val(result.note);
    		});
    		
    	}
    	
    	//显示文档
    	function loadDoc(){
    		$.get("paperlessRelatedDoc/queryDocsByRelatedIdAndType.do",{
    			deviceCode:deviceCode,
    			moduleCode:"device"
    		},function(data){
    			for(var i=0;i<data.length;i++){
					var uploadDate=timestampToTime(data[i].uploadDate);
					var status="";
					var pic="";
					
					/* if(data[i].picName.length>0){
						pic="预览";
						picName=data[i].picName;
					} */
					var html="<tr><th>"+(i+1)+"</th><th>"+data[i].relatedDocumentType.name+"</th><th>"+data[i].name+"</th><th>"+data[i].srcName+"</th><th>"+data[i].note+"</th><th>"+uploadDate+"</th><th style='cursor:pointer;' onclick='preview(\""+data[i].url+"\")'>预览</th></tr>";
					
					$("#taskTabel").append(html);
				}
    		})
    	}
    	
    	//文档预览
    	function preview(url){
    		parent.showFullScreen(url);
    	}	
		
		

</script>
<style>
body {
	margin: 0;
}
#layoutGraph div{
	height:20px;
	width:20px;
	position:relative;
	cursor:pointer;
	display: inline-block;
}
#outer {
	height: 1080px;
	width: 1920px;
	background-image: url('front/imgs/blue.png');
}

#loginInfo {
	text-align: center;
	width: 100%;
	height: 40px;
	text-align: right;
	color: #57EEFD;
	margin-right: 50px;
	margin-bottom: 0px;
	position: absolute;
	left: -20px;
	top: 20px;
	font-size: 20px;
	z-index: 10000;
}

#blank {
	text-align: center;
	width: 1920px;
	height: 40px;
}

#title {
	text-align: center;
	width: 100%;
	height: 50px;
	line-height: 50px;
	font-size: 50px;
	color: #FF0000;
	overflow: hidden;
}

#time {
	width: 100%;
	height: 90px;
	text-align: center;
	line-height: 98px;
	color: #57EEFD;
	font-size: 25px;
}

#productionUnit{
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: left;
	margin-left: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}

#department{
	height: 50px;
	font-size: 26px;
	color: #57EEFD;
	float: right;
	margin-right: 60px;
	margin-top: -95px;
	font-family: SimHei  ;
}


.m_left{
	width: 360px;
	height: 855px;
	float: left;
	background-color: #1C2437;
}

.m_right{
	width: 1420px;
	height: 855px;
	background-color: #1C2437;
	float: right;
	margin-right: 10px;
}

.tp_button{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#9966CC;
}

.device_button{
	color:white;
	font-size: 25px;
	text-align: center;
	margin: 0 auto;
	background-color:#666633;
	float: left;
	margin-right: 20px;
	height: 58px;
	line-height: 58px;
}

.tp_button:hover{
	
}


#middle {
	height: 850px;
	width: 94%;
	margin: auto auto;
}

.form-control{
	width: 190px;
	background-color: #1C2437;
	border: 1px #666666 solid;
}


table{
	width:1380px;
	margin:auto auto;
	margin-top: 10px;
	border-collapse: collapse;
}
tr{
	height:40px;
}

table,table tr th, table tr td { border:1px solid white; color: white;text-align: center;}  

td, th {
	border: white 1px solid;
	color: white;
	font-size: 14px;
	text-align: center;
	height: 50px;
}

#warnning tr td,#topSort tr td{
	text-align: center;
	color:#FFF;
}


#warnning tr td:nth-child(odd){background:#313348;}
#warnning tr td:nth-child(even){background:#2C2E45;}
#topSort tr:nth-child(odd){background:#313348;}
#topSort tr:nth-child(even){background:#2C2E45;}

.labelstyle{
	font-size: 15px;
	vertical-align: middle;
	display: table-cell;
	height: 40px;
	line-height: 40px;
}
</style>

</head>
<body style="background-color:#1C2437;">
<!-- 设备信息窗口 -->
<div style="color:white;width: 1375px;height: 305px;border: 1px solid white;margin:auto auto;border-radius: 20px;border: 1px dashed #666666;">
			<div class="modal-body" style="height: 305px;margin-top: 7px;">
				<form id="pressLightRecordForm" class="form-horizontal">
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">设备代码</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceCode" id="deviceCode" class="form-control" style="width: 230px;height:40px; background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备名称</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceName" id="deviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">规则型号</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceUnitType" id="deviceUnitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备类别</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="deviceType" class="form-control"
									id="deviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">出产编号</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="outFactoryCode" class="form-control"
									id="outFactoryCode" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">生产厂家</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="manufacturer" id="manufacturer" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">经销商&nbsp;&nbsp;&nbsp;</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="trader" class="form-control"
									id="trader" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备状态</div>
							<div style="float: left;margin-left: 10px;">
								<input type="text" name="status" id="status" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>

						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">出厂日期</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="outFactoryDate" id="outFactoryDate" class="form-control" style="width: 230px;height:40px; background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">到厂日期</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="inFactoryDate" id="inFactoryDate" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">安装日期</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="installDate" id="installDate" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">验收日期</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="checkDate" class="form-control"
									id="checkDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">资产编号</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="assetNumber" id="assetNumber" class="form-control" style="width: 230px;height:40px; background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">设备重量</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="weight" id="weight" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">外形尺寸</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="shapeSize" id="shapeSize" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						<div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">安装位置</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="installPosition" class="form-control"
									id="installPosition" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="float: left;margin-left: 50px">
							<div class="labelstyle" style="float: left;">单台功率</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="power" id="power" class="form-control" style="width: 230px;height:40px; background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">实际功率</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="actualPower" id="actualPower" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						 <div style="float: left;margin-left: 20px">
							<div class="labelstyle" style="float: left;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</div>
							<div style="float: left;margin-left: 10px">
								<input type="text" name="note" id="note" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
							</div>
						</div>
						
					</div>
					
				</form>
				
			</div>
</div>
<div style="overflow:auto;height: 440px;font-size: 20px;margin:auto auto;">
		<table style="color:white;width: 1375px;border: 1px solid white;margin:auto auto;margin-top: 20px;">
			<thead>
				<tr>
					<th style='width:50px;'></th>
					<th style='width:250px;'>文档类别</th>
					<th>文件名称</th>
					<th style='width:250px;'>源文件名称</th>
					<th>说明</th>
					<th style='width:250px;'>上传时间</th>
					<th style='width:100px;'></th>
				</tr>
			</thead>
			<tbody id="taskTabel">
			</tbody>
		</table>
</div>
</body>
</html>