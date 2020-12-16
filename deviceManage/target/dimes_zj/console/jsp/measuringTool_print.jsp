<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
	function content( label, data){
		var row = '<div class="topjui-row row">'+
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
			 $.get("measuringTool/printQr.do",{ids:ids},function(data){
				 var $center = $("#center");
				 var i = 0;
				while(i<data.length){
					var $row = $("<div class='topjui-row'>");
					var data1 = data[i++];
					if(data1!=undefined){
						var $col1 = $('<div class="topjui-col-sm4 topjui-col-xs4 col">');
						//列里的div
						var $innerDiv1 = $("<div class='innerDiv'>");
						$col1.append($innerDiv1);
						
						var $innerTop = $("<div class='innerTop'>");
						var $innerBottom = $("<div class='innerBottom'>");
						
						$innerDiv1.append($innerTop);
						$innerDiv1.append($innerBottom);
						
						$innerTop.append("<img src='<%=basePath%>/" + data1.qrPath+"' />");
						
						
						var code = content('量具代码',(data1.code==null?"":data1.code));
						var name = content('量具名称',(data1.equipmentType.name==null?"":data1.equipmentType.name));
						var unitType = content('规格型号',(data1.equipmentType.unitType==null?"":data1.equipmentType.unitType));
						var manufacturer = content('生产厂家',(data1.equipmentType.manufacturer==null?"":data1.equipmentType.manufacturer));
						$innerBottom.append(code);
						$innerBottom.append(name);
						$innerBottom.append(unitType);
						$innerBottom.append(manufacturer);
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
		width:5cm;
		margin-left: 2cm;
		text-align: center;
		display:table-cell;
		vertical-align: bottom;
		height:0.5cm;
	}
	.label {
		width: 1.5cm;
	}
	.row{
		height:0.5cm;
	}
	img {
		height: 1.8cm;
		width: 1.8cm;
		vertical-align: middle;
		margin-top:5px;
	}
	.innerTop {
		height: 40%;
		line-height:2cm;
		text-align: center;
	}
	.innerDiv {
	height: 5cm;
	width: 7cm;
	border: solid 1px gray;
}
.innerBottom {
	height: 60%;
	width:100%;
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
	}
	.label {
		width: 60pt;
	}
	.data {
		border-bottom-style: solid;
		margin-left: 70pt;
	}
	.innerTop {
		height: 50%;
	}
	.innerDiv {
	height: 300pt;
	width: 100%;
	border: solid 1px gray;
	padding:10pt;
}
.innerBottom {
	height: 50%;
	width:100%;
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
		</div>
	</div>
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<div class=".container" id="center"></div>
	</div>
</div>