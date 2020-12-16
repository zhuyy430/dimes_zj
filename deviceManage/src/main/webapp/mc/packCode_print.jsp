<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
	<%-- function content( label, data){
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
			 $.get("device/printQr.do",{ids:ids},function(data){
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
						
						
						
						var code = content('设备代码',(data1.code==null?" ":data1.code));
						var name = content('设备名称',(data1.name==null?" ":data1.name));
						var unitType = content('规格型号',(data1.unitType==null?" ":data1.unitType));
						var installDate = content('安装日期',(data1.installDate==null?" ":data1.installDate));
						$innerBottom.append(code);
						$innerBottom.append(name);
						$innerBottom.append(unitType);
						$innerBottom.append(installDate);
						
						$row.append($col1);
					}
								$center.append($row);
							}
						});
	}); --%>
	
	$(function(){
		$.get("mcPackingBox/print.do",{},function(data){
			var $center = $("#printArea");
			var i = 0;
			while(i<data.length){
				var $barcode = $("<div class='barcode'>");
				var $innerTop = $("<div class='innerTop'>");
				var $innerBottom = $("<div class='innerBottom'>");
				var data1 = data[i++];
				var key1;
				var value;
				for(var key in data1){
					key1=key;
					value=data1[key];
				}
				console.log(key1);
				console.log(value);
				$innerTop.append("<img src='<%=basePath%>/" +value+"' />");
				$innerBottom.append(key1);
				$barcode.append($innerTop);
				$barcode.append($innerBottom);
				$center.append($barcode);
			} 
		})
	})
	

	function print() {
		$("#printArea").print();
	}
</script>
<style>
@media print {
	.barcode{
		width:70mm;
		height:50mm;
		vertical-align: middle;
	}
	img {
		width: 78mm;
		height: 22mm;
	}
	.innerTop {
		height: 50%;
		width: 100%;
		vertical-align:middle;
		text-align: center;
		overflow: hidden;
	}
	.innerDiv {
		
	}
	.innerBottom {
		height: 35%;
		width:100%;
		text-align: center;
		font-size: 7mm;
	}
}

@media screen {
	.barcode{
		width:100pt;
		height:75pt;
		vertical-align: middle;
	}
	img {
		width: 100pt;
		height: 40pt;
		vertical-align: middle;
	}
	.innerTop {
		margin-top:15%;
		height: 50%;
		width: 100%;
		vertical-align:middle;
		text-align: center;
		overflow: hidden;
	}
	.innerDiv {
		
	}
	.innerBottom {
		height: 35%;
		width:100%;
		text-align: center;
		font-size: 5pt;
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
		<div id="printArea" style="margin-bottom: 70pt;" style="height:60mm;width:80px;"></div>
	</div>
</div>