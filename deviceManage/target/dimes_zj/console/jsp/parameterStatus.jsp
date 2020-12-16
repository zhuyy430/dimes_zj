<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function() {
		$
				.get(
						"oee/queryDeviceSite4ParameterStatusShow.do",
						function(result) {

							if (result.error) {
								alert(result.error);
								return;
							}

							var $main = $("#parameterStatus");
							//$main.append("<div id='title'>")

							for (var i = 0; i < result.deviceSites.length; i++) {
								//	for(var i = 0;i<5;i++){
								$titleDiv = $("<div class='title'>");
								$titleDiv.append(result.deviceSites[i].name);
								$contentDiv = $("<div class='content'>");

								$main.append($titleDiv);
								$main.append($contentDiv);

								$picDiv = $("<div class='pic'>");

								$oeeDiv = $("<div class='oee'>");
								$rtyDiv = $("<div class='rty' style='padding:auto auto;'>");

								$rtyText = $("<div style='height:30% ;width:100%;font-size:0.5em;color:white;'>");
								$rtyText.text("良品率");
								$rtyDiv.append($rtyText);

								$rtyValue = $("<div style='height:70% ;width:100%;font-size:0.8em;color:#32B9D4;'>");
								$rtyDiv.append($rtyValue);

								$parameterDiv = $("<div class='parameter'>");

								var $table = $("<table style='width:100%;'>");
								var $tr = $("<tr style='backgroun-color:#F00'>");
								$table.append($tr);

								var $parameterNameTd = $("<th>参数名</th>");
								var $parameterValueTd = $("<th>参数值</th>");
								$tr.append($parameterNameTd);
								$tr.append($parameterValueTd);
								//循环显示参数信息
								for (var j = 0; j < result.parameters[i].length; j++) {
									var $contentTr = $("<tr>");
									var $contentNameTd = $("<td>");
									var $contentValueTd = $("<td>");

									$contentTr.append($contentNameTd);
									$contentTr.append($contentValueTd);

									$table.append($contentTr);
									$contentNameTd
											.append(result.parameters[i][j].parameter.name);
									$contentValueTd
											.append(result.parameters[i][j].trueValue);
								}

								$parameterDiv.append($table);

								$contentDiv.append($picDiv);
								$contentDiv.append($oeeDiv);
								$contentDiv.append($rtyDiv);
								$contentDiv.append($parameterDiv);
								$rtyValue
										.append((result.rtys[i] ? result.rtys[i]
												: 0)
												+ "%");
								oee($oeeDiv.get(0),
										result.oees[i] ? result.oees[i] : 0);
								$picDiv
										.append("<img src='"
												+ result.deviceSites[i].device.photoName
												+ "' />");
							}
							//$("#title").text("嘉兴迪筑智能工业技术有限公司");
						});

		var time = new Date();
		$("#time").text(
				time.getFullYear() + "-" + (time.getMonth() + 1) + "-"
						+ time.getDate());
	});

	//生产单元
	function oee(divObj, value, title) {
		var app = echarts.init(divObj);
		option = {
			title : {
				show : true,
				text : title,
				left : 'center'
			},
			tooltip : {
				formatter : "{a} <br/>{b} : {c}%"
			},
			toolbox : {
				feature : {
					restore : {},
					saveAsImage : {}
				}
			},
			series : [ {
				name : title,
				type : 'gauge',
				radius : '100%',
				detail : {
					formatter : '{value}%',
					fontSize:14
				},
				data : [ {
					value : value,
					name : ''
				} ],
				axisLine : {
					lineStyle : {
						width : 10
					// 这个是修改宽度的属性
					}
				},
				splitLine : {
					length : 10,
					lineStyle : {
						width : 2,// 这个是修改宽度的属性
					}
				},
			} ]
		};
		app.setOption(option);
	}
</script>
<style>
body {
	margin: 0;
}

img{
	max-width: 430px; max-height: 150px;
}

#outer {
	height: 1080px;
	width: 1920px;
	background-image: url('front/imgs/blue.png');
}

#blank {
	text-align: center;
	width: 1920px;
	height: 40px;
}

#divTitle {
	text-align: center;
	width: 100%;
	height: 50px;
	line-height: 50px;
	font-size: 50px;
	color: #57EEFD;
}

#time {
	width: 100%;
	height: 90px;
	text-align: center;
	line-height: 98px;
	color: #57EEFD;
	font-size: 25px;
}

#middle {
	height: 850px;
	width: 92%;
	margin: auto auto;
	background-color: #1C2437;
}

#parameterStatus {
	height: 810px;
	width: 100%;
	overflow:auto;
}

.divTitle {
	height: 40px;
	width: 100%;
	color: #57EEFD;
	font-size: 25px;
	margin-left: 10px;
	margin-top: 5px;
	background-color: #1C2437;
	border-bottom: 1px solid #55E6F1;
}

table tr td, table tr th {
	text-align: center;
	font-size: 1.3em;
	color: white;
}

/* table:first-child {
	background-color: #10325F;
}
 */
#title {
	width: 99.5%;
	height: 40px;
	line-height: 40px;
	color: white;
	background-color: #0E4B83;
	padding-left: 10px;
	font-size: 1.5em;
}

.title {
	width: 1723px;
	height: 30px;
	line-height: 30px;
	color: white;
	background-color:#1C2437;
	padding-left: 5px;
}

.content {
	width: 100%;
	height: 180px;
	background-color: #1C2437;
}
/* .content div{
	margin-right:1px;
} */
.pic {
	float: left;
	height: 100%;
	width: 25%;
	text-align: center;
}

.oee {
	float: left;
	height: 100%;
	width: 25%;
}

.rty {
	float: left;
	height: 100%;
	width: 25%;
	font-size: 5em;
	text-align: center;
	padding: auto auto;
	color: white;
	font-family: "宋体";
}

.parameter {
	float: left;
	height: 100%;
	width: 25%;
}
</style>
</head>
<body>
	<div id="outer">
		<div id="blank"></div>
		<div id="divTitle">设备运行状态</div>
		<div id="time"></div>

		<div id="middle">
			<div class="divTitle">参数状态</div>
			<div id="parameterStatus"></div>
		</div>
	</div>
</body>
</html>