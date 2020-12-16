<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script>

	function addTd(list,trDom){
		loop1:for(var i=0;i<30;i++){
			if(list!=null&&list.length>0) {
				for (var j = 0; j < list.length; j++) {
					if ((i + 1) == list[j][2]) {
						var content = "<td ";

						if(i<15){
							content += "onclick=\"showRightDialog('"+list[j][4]+"')\"";
						}else{
							content += "onclick=\"showLeftDialog('"+list[j][4]+"')\"";
						}

						if (list[j][3] <= 30) {
							content += "class='tdHave tdLessThan30'";
						} else if (list[j][3] > 30 && list[j][3] <= 365) {
							content += "class='tdHave tdGreaterThan30'";
						} else {
							content += "class='tdHave tdGreaterThan365'";
						}
						content += "><div class='tdDiv'>" + list[j][0] + "</div></td>";
						trDom.append(content);
						continue loop1;
					}
				}
			}
			trDom.append("<td style='background-color: white;'></td>");
		}
	}

	function showLeftDialog(e){
		$('#leftDialog').css("display","block");
		$('#rightDialog').css("display","none");
		layui.use([ 'carousel' ], function() {
			var carousel = layui.carousel;

			//设定各种参数
			var ins3 = carousel.render({
				elem : '#test3',
				full : false,
				interval:30*1000,
				width:'400px',
				height:'630px'
			});
			//轮播切换事件
			carousel.on("change(test1)", function(obj) {

			});
		});
	}

	function showRightDialog(e){
		$('#rightDialog').css("display","block");
		$('#leftDialog').css("display","none");

	}

	$(function() {
		$(document).bind("click",function(e){
			//id为menu的是菜单，id为open的是打开菜单的按钮
			if($(e.target).closest("#leftDialog").length == 0 && $(e.target).closest("#ATable").length == 0 && $(e.target).closest("#BTable").length == 0 && $(e.target).closest("#XTable").length == 0){
				//点击id为menu之外且id不是不是open，则触发
				//close();
				$('#leftDialog').css("display","none");
			}
			if($(e.target).closest("#rightDialog").length == 0 && $(e.target).closest("#ATable").length == 0 && $(e.target).closest("#BTable").length == 0 && $(e.target).closest("#XTable").length == 0){
				//点击id为menu之外且id不是不是open，则触发
				//close();
				$('#rightDialog').css("display","none");
			}
		})


		$.get('kanBan/queryKanbanByPositon.do',{},function(data){
			console.log(data.A1List[1][2]);
			var A1Tr = $('#A1Tr');
			addTd(data.A1List,A1Tr);
			var A2Tr = $('#A2Tr');
			addTd(data.A2List,A2Tr);
			var A3Tr = $('#A3Tr');
			addTd(data.A3List,A3Tr);
			var A4Tr = $('#A4Tr');
			addTd(data.A4List,A4Tr);
			var A5Tr = $('#A5Tr');
			addTd(data.A5List,A5Tr);

			var XTr = $('#XTr');
			addTd(data.XList,XTr);

			var B1Tr = $('#B1Tr');
			addTd(data.B1List,B1Tr);
			var B2Tr = $('#B2Tr');
			addTd(data.B2List,B2Tr);
			var B3Tr = $('#B3Tr');
			addTd(data.B3List,B3Tr);
			var B4Tr = $('#B4Tr');
			addTd(data.B4List,B4Tr);
			var B5Tr = $('#B5Tr');
			addTd(data.B5List,B5Tr);

		});
		var time = new Date();
		$("#time").text(
				time.getFullYear() + "-" + (time.getMonth() + 1) + "-"
				+ time.getDate());

		var $Atr = $('<tr id="positonNum">');
		for(var i=0;i<31;i++){
			if(i==0){
				$Atr.append("<td></td>");
			}else{
				$Atr.append("<td>"+i+"</td>");
			}
		}
		$("#ATable").append($Atr);

	});


</script>

</head>
<body>
	<div id="outer">
		<div id="blank"></div>
		<div id="divTitle">原料仓库存状态</div>
		<div id="time"></div>
		<div id="occupyNum">占用库位:89</div>
		<div id="freeNum">空闲库位:89</div>

		<div id="middle">
			<table style="width: 100%" id="BTable">
				<tr id="B1Tr">
					<td rowspan="5" style="line-height: 100%;text-align: center;font-size: 50px;">B</td>
				</tr>
				<tr id="B2Tr"></tr>
				<tr id="B3Tr"></tr>
				<tr id="B4Tr"></tr>
				<tr id="B5Tr"></tr>
			</table>

			<table style="width: 100%;margin-top: 45px;" id="XTable">
				<tr id="XTr">
					<td style="line-height: 100%;text-align: center;font-size: 50px;">X</td>
				</tr>
			</table>

			<table style="width: 100%;margin-top: 45px;" id="ATable">
				<tr id="A1Tr">
				<td rowspan="5" style="line-height: 100%;text-align: center;font-size: 50px;">A</td>
				</tr>
				<tr id="A2Tr"></tr>
				<tr id="A3Tr"></tr>
				<tr id="A4Tr"></tr>
				<tr id="A5Tr"></tr>
			</table>
		</div>
	</div>

	<div id="leftDialog">
		<div class="layui-carousel" id="test3" lay-filter="test1">
			<div carousel-item="">
				<div style="background-color: #6D96BC;">
					<div style="width: 100%;height: 30px;margin-top: 50px;overflow:hidden;">
						<div class="dialogLabel">箱条码</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">物料名称</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">物料代码</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">规格型号</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">钢厂</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">炉号</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">材料编号</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">数量</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">入库时间</div>
						<div class="dialogValue">10100292</div>
					</div>
					<div style="width: 100%;height: 30px;margin-top: 25px;overflow:hidden;">
						<div class="dialogLabel">供应商</div>
						<div class="dialogValue">10100292</div>
					</div>

				</div>

			</div>
		</div>
	</div>

	<div id="rightDialog">

	</div>
</body>
<style>
	body {
		margin: 0;
	}

	img{
		max-width: 430px; max-height: 150px;
	}

	#leftDialog{
		overflow: hidden;
		position: absolute;
		left: 150px;
		top: 250px;
		z-index: 99;
		width: 400px;
		height: 630px;
		background-color: #6D96BC;
		border-radius: 25px;
		display: none;
	}

	#rightDialog{
		overflow: hidden;
		position: absolute;
		right: 150px;
		top: 250px;
		z-index: 99;
		width: 400px;
		height: 630px;
		background-color: #6D96BC;
		border-radius: 25px;
		display: none;
	}

	.dialogLabel{
		float: left;
		width: 120px;
		background-color: #315282;
		color: white;
		height: 30px;
		line-height: 30px;
		text-align: center;
		border: 1px solid #5B6069;
		margin-left: 32px;
	}

	.dialogValue{
		float: right;
		width: 180px;
		background-color: #315282;
		color: white;
		height: 30px;
		line-height: 30px;
		text-align: center;
		border: 1px solid #5B6069;
		margin-right: 32px;
	}


	td{
		text-align: center;
		height: 65px;
		width: 56px;
		border: 1px solid #819BB9;
		color: white;
		font-size: 2px;
		word-wrap:break-word;
		overflow: hidden;
	}

	.tdHave{
		//color:#4CEFF5;
		color:#FFFFFF;
	}

	.tdNot{
		color:#4CEFF5;
	}

	.tdLessThan30{
		background-color: #000066;
	}

	.tdGreaterThan30{
		background-color: #ff8000;
	}

	.tdGreaterThan365{
		background-color: red;
	}

	.tdDiv{
		width: 71.25px;
		-webkit-transform: scale(0.8);
		-webkit-transform-origin-x: 0;
		-webkit-transform-origin-y: 0;
	}

	table{
		table-layout:fixed;
	}

	#positonNum td{
		text-align: center;
		height: 30px;
		width: 2rem;
		border: 1px solid #819BB9;
		color: white;
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
	#occupyNum{
		position: absolute;
		top: 100px;
		left: 75px;
		color: #FFFFFF;
		font-size: 28px;
		font-weight: bold;
	}
	#freeNum{
		position: absolute;
		top: 100px;
		right: 75px;
		color: #339966;
		font-size: 28px;
		font-weight: bold;
	}
</style>
</html>