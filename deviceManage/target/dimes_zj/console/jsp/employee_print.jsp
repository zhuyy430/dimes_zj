<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
	function content( label, data){
		var row = '<div class="topjui-row row" >'+
		'<div class="topjui-col-sm12 topjui-col-xs12">'+
			'<label class="topjui-form-label label">' + label +':</label>' +
			'<div class="topjui-input-block data">' + data +
			'</div>'+
		'</div>'
		'</div>';
		return row;
	}
	
		$(function(){
			var ids = window.opener.document.getElementById("ids").value;
			 $.get("employee/printQrByCodes.do",{codes:ids},function(data){
				 var $center = $("#center");
				 var i = 0;
				while(i<data.length){
					var employee1 = data[i++];
					var $row = $("<div class='topjui-row'>");
					if(employee1!=undefined){
						var $col1 = $('<div class="topjui-col-sm4 topjui-col-xs4 col">');
						//列里的div
						var $innerDiv1 = $("<div class='innerDiv'>");
						$col1.append($innerDiv1);
						
						var $innerTop = $("<div class='innerTop'>");
						var $innerBottom = $("<div class='innerBottom'>");
						
						$innerDiv1.append($innerTop);
						$innerDiv1.append($innerBottom);
						
						var $innerBottomLeft = $("<div class='innerBottomLeft'>");
						var $innerBottomRight = $("<div class='innerBottomRight'>");
						
						$innerBottom.append($innerBottomLeft);
						$innerBottom.append($innerBottomRight);
						
						var code = content('工号',employee1.code);
						var name = content('姓名',employee1.name);
						var dept = content('部门',employee1.departmentName);
						$innerBottomLeft.append(code);
						$innerBottomLeft.append(name);
						$innerBottomLeft.append(dept);
						
						$innerBottomRight.append("<img src='<%=basePath%>/"
													+ employee1.qrPath + "' />");

									$row.append($col1);
								}

								$center.append($row);
							}
						});
	});

	function print() {
		$("#center").print();
	}
</script>
<style>
@media print {
	.data {
		border-bottom-style: solid;
		border-bottom-width: 1px;
		width: 3.5cm;
		margin-left: 1.3cm;
		display:table-cell;
		vertical-align: bottom;
		 padding:0px; 
		 height:0.5cm;
		 line-height: 0.5cm;
	}
	.row{
		height:0.5cm;
	}
	.label {
		width: 1cm;
		height:0.5cm;
	}
	img {
		height: 1.8cm;
		width: 1.8cm;
		vertical-align: middle;
		border:solid 1px gray;
	}
	
.innerDiv {
	height: 5cm;
	width: 7cm;
}
	.innerBottomRight {
		height: 2cm;
		float: right;
		width: 2cm;
		display: table-cell;
		vertical-align: bottom;
		text-align: center;
	}
	
	.innerBottomLeft {
	height: 2.2cm;
	float: left;
	width: 4.5cm;
	margin-left: 0px;
	vertical-align: bottom;
	display: table-cell;
	text-align: center;
}
.innerBottom {
	height:2.5cm;
	width:7cm;
	display: table-cell;
	vertical-align:bottom;
	text-align: center;
}
 .innerTop {
	height: 2.5cm;
}
.col {
	text-align: center;
}
}

@media screen {
	img {
		height: 100pt;
		width: 100pt;
		vertical-align: middle;
	/* 	border-right:solid 8px gray;
		border-top:solid 5px gray;
		border-left:solid 5px gray;
		border-bottom:solid 5px gray; */
		border:solid 2px black;
	}
	.label {
		width: 40pt;
	}
	.data {
		border-bottom-style: solid;
		margin-left: 50pt;
	}
	
.innerDiv {
	height: 250pt;
	width: 100%;
	border: solid 1px gray;
}
.innerBottomRight {
	height: 100%;
	float: right;
	width: 40%;
	display: table-cell;
	vertical-align: middle;
	text-align: center;
	line-height: 100pt;
}

.innerBottom {
	height: 50%;
}

.innerTop {
	height: 50%;
}
.innerBottomLeft {
	height: 100%;
	float: left;
	width: 60%;
	margin-left: 0px;
	vertical-align: bottom;
}
.col {
	padding: 10pt;
	text-align: center;
}
}

</style>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'north',title:'',split:true"
		style="height: 50pt;">
		<div>
			<input type='button' value='打印' onclick='javascript:print()' />
			<!-- <input type='button' value='打印' onclick='javascript:window.print()' /> -->
		</div>
	</div>
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<div class=".container" id="center"></div>
	</div>
</div>