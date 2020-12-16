<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="common/jsp/head.jsp"%>
<script type="text/javascript" src="front/js/main.js"></script>
<style>
body {
	padding: 0px;
	margin: 0px;
}
.chartDiv {
	height: 370px;
	width: 490px;
	margin-bottom: 5px;
	border: 1px solid #0459A2;
}

.directLeft {
	float: left;
}

.directRight {
	float: right;
}

.chartDiv>:first-child {
	height: 50px;
	width: 100%;
}
/*高度可通过父容器高度计算获取*/
.chartBodyDiv {
	height: 340px;
	width: 100%;
}
</style>
</head>
<body>
	<!-- 最外层容器 -->
	<div
		style="padding: 10px; height: 1720px; width: 1000px; margin: 0 auto;">
		<!-- 看板标题 -->
		<div
			style="position: relative; height: 150px; width: 100%; margin-bottom: 5px; background-image: url('front/imgs/logo.JPG'); background-repeat: no-repeat; background-size: 100% 100%;">
			<!-- <img alt="logo" id="logo" style="height: 100%; width: 100%;"
				src="front/imgs/logo.JPG"> -->
			<div
				style="position: absolute; bottom: 10px; right: 30px; font-size: 3em; color: white;">
				生产单元:<span id="productionUnitName"></span>
			</div>
		</div>
		<!-- OEE Graph -->
		<div class="chartDiv directLeft" id="oeeGraphTitle">
			<div id="oeeGraphHeader">
				<img id="oeeTitle" style="height: 100%; width: 100%;"
					src="front/imgs/oeeTitle.png">
			</div>
			<div id="oeeGraph" class="chartBodyDiv">
				<div id="oeeGraphBody" style="height: 310px; width: 100%;"></div>
			</div>
		</div>
		<!-- Output Monitor 产量 -->
		<div class="chartDiv directRight" id="outputMonitor">
			<div id="outputMonitorHeader">
				<img id="outputTitle" style="height: 100%; width: 100%;"
					src="front/imgs/outputTitle.png">
			</div>
			<div id="outputMonitorBody" class="chartBodyDiv">
				<div id="outputGraphBody" style="width: 100%; height: 280px;"></div>
			</div>
		</div>
		<!-- 不合格产品 -->
		<div class="chartDiv directLeft" id="lowQuality">
			<div id="lowQualityHeader">
				<img id="lowQualityTitle" style="height: 100%; width: 100%;"
					src="front/imgs/lowQualityTitle.png">
			</div>
			<div id="lowQualityBody" class="chartBodyDiv">
				<div id="ngRecordGraphBody" style="width: 100%; height: 300px;"></div>
			</div>
		</div>
		<!-- 人员技能 -->
	<%--	<div class="chartDiv directRight" id="operatorSkill">
			<div id="operatorSkillHeader">
				<img id="empTitle" style="height: 100%; width: 100%;"
					src="front/imgs/empTitle.png">
			</div>
			<div id="operatorSkillBody" class="chartBodyDiv">
				<div id="employeeSkillGraphBody" style="width: 100%; height: 310px;"></div>
			</div>
		</div>--%>
		<!-- 安全 -->
		<div class="chartDiv directRight" id="safetyCross">
			<div id="safetyCrossHeader">
				<img id="safetyCrossTitle" style="height: 100%; width: 100%;"
					src="front/imgs/safetyCross.png">
			</div>
			<div id="safetyCrossBody" class="chartBodyDiv"
				style="text-align: center;">
				<div id="main" style="width: 100%; height: 300px;">
					<table id="mainTable"
						style="width: 95%; height: 95%; margin: auto auto;"></table>
					<!-- <div id="gradeDiv" style="text-align:center;margin:5px auto;"></div> -->
				</div>
				<div id="gradeDiv"
					style="margin: 0 auto; text-align: center; text-size: 20px; height: 15px; line-height: 20px;"></div>
			</div>
		</div>
		<!-- 防错布局 -->
		<div class="chartDiv directLeft" id="errorProof">
			<div id="errorProofHeader">
				<img id="errorProofTitle" style="height: 100%; width: 100%;"
					src="front/imgs/lostTimeTitle.png">
			</div>
			<div id="errorProofBody" class="chartBodyDiv">
				<div id="lostTimeReasonGraphBody"
					style="width: 100%; height: 300px;"></div>
			</div>
		</div>
		<!-- newDIV1 -->
		<div class="chartDiv directRight" id="newDiv1">
			<div id="newDiv1Header" style="border-bottom: 1px solid #000">
				<img id="newDiv1Title" style="height: 100%; width: 100%;"
					src="front/imgs/noPic2.jpg">
			</div>
			<div id="newDiv1Body" class="chartBodyDiv"
				style="text-align: center;">
				<div id="newDiv1main" style="width: 100%; height: 300px;">
					<img id="newDiv1img" style="height: 100%; width: 100%;"
						src="front/imgs/noPic.jpg">
				</div>
			</div>
		</div>
		<!-- newDIV2 -->
		<%--<div class="chartDiv directRight" id="newDiv2">
			<div id="newDiv2Header" style="border-bottom: 1px solid #000">
				<img id="newDiv2Title" style="height: 100%; width: 100%;"
					src="front/imgs/noPic2.jpg">
			</div>
			<div id="newDiv2Body" class="chartBodyDiv"
				style="text-align: center;">
				<div id="newDiv2main" style="width: 100%; height: 300px;">
					<img id="newDiv2img" style="height: 100%; width: 100%;"
						src="front/imgs/noPic.jpg">
				</div>
			</div>
		</div>--%>
		<div id="classesDiv" style="display:none;"></div>
	</div>
</body>
</html>
