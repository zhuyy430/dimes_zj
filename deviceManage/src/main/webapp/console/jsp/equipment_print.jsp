<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
	var ids = '<%=request.getParameter("equipmentIds")%>';
	//var ids = window.opener.document.getElementById("ids").value;
	function content( label, data){
		var row = /*'<div class="topjui-row row">'+*/
				'<div class="topjui-col-sm12 topjui-col-xs12">'+
				'<label class="topjui-form-label label" style="text-align: center;">' + label +':</label>' +
				'<div class="topjui-input-block data">' + data +
				'</div>'+
				'</div>'
				/*'</div>'*/;
		return row;
	}

	function content1( label, data){
		var row = /*'<div class="topjui-row row">'+*/
				'<div class="toplabel">'+
				'<label class="topjui-form-label label" style="text-align: center;">' + label +'</label>' +
				'<div class="topjui-input-block data1">' + data +
				'</div>'+
				'</div>'
				/*'</div>'*/;
		return row;
	}



	$(function(){
		$.get("equipment/printQr.do",{ids:ids},function(data){
			var $center = $("#center");
			var i = 0;
			while(i<data.length){
				var $row = $("<div class='topjui-row row-bottom'>");
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



					$innerTop.append("<img style='float:left;' src='<%=basePath%>/" + data1.qrPath+"' />");




					var boxBarType = content1(''," ");
					var formNo = content1(''," ");
					var warehousingDate = content1(''," ");


					var no = content('装备序号',(data1.code==null?" ":data1.code));
					var Name = content('装备名称',(data1.equipmentType==null?" ":(data1.equipmentType.name==null)?" ":data1.equipmentType.name));
					var unitType = content('装备规格',(data1.equipmentType==null?" ":(data1.equipmentType.unitType==null)?" ":data1.equipmentType.unitType));
					var measurementObjective = content('计量目标',(data1.equipmentType==null?" ":(data1.equipmentType.measurementObjective==null)?" ":data1.equipmentType.measurementObjective));
					var inWarehouseDate=content('入库时间',(data1.inWarehouseDate==null?" ":data1.inWarehouseDate));


					$innerBottom.append(no);
					$innerBottom.append(Name);
					$innerBottom.append(unitType);
					$innerBottom.append(measurementObjective);
					$innerBottom.append(inWarehouseDate);

					/*var furnaceNumber = content('材料编号',(data1.furnaceNumber==null?" ":data1.furnaceNumber));
					var amount = content('数量',(data1.amountOfPerBox));
					var boxNum = content('班次',(data1.classCode==null?" ":data1.classCode)+"/"+(data1.className==null?" ":data1.className));
					var employeeName = content('员工',(data1.employeeName));
					var productionLine = content('产线',(data1.productionLine));*/

					//$innerTop.append(boxBarType);
					//$innerTop.append(formNo);
					$innerTop.append("<div class='companyTop'>江苏南洋中京科技有限公司</div><div class='companyBottom'>模具标识卡</div>");


					/*$innerBottom.append(barcode);
					$innerTop.append(boxBarType);
					$innerTop.append(formNo);
					$innerTop.append(warehousingDate);
					$innerBottom.append(inventoryName);
					$innerBottom.append(inventoryCode);
					$innerBottom.append(specificationType);
					$innerBottom.append(batchNumber);
					$innerBottom.append(furnaceNumber);
					$innerBottom.append(amount);
					$innerBottom.append(boxNum);
					$innerBottom.append(employeeName);
					$innerBottom.append(productionLine);*/


					$row.append($col1);
				}
				$center.append($row);
			}
		});
	});

	function print() {
		$("#center").print();
		$.get("equipment/addEquipmentPrint.do",{ids:ids},function(data){

		})
	}
</script>
<style>
	@media print {
		.data {
			border-bottom-style: solid;
			border-bottom-width: 1px;
			width:6cm;
			margin-left: 0.5cm;
			display: table-cell;
			vertical-align: bottom;
			height:0.5cm;
			font-size:6px;
		}
		.data1{
			/*border-bottom-style: solid;
			border-bottom-width: 1px;*/
			width:5cm;
			margin-left: 0.5cm;
			display: table-cell;
			vertical-align: bottom;
			height:0.5cm;
			font-size:6px;
		}

		.row-bottom{
			margin-bottom: 0;
		}

		.label {
			width: 1.5cm;
			font-size:8pt;
			margin-left: 0.5cm;
		}
		.row{
			height:0.1cm;
		}
		img {
			height: 3cm;
			width: 3cm;
			vertical-align: middle;
			margin-top:-0.4cm;
			margin-left: 0.2cm;
		}
		.innerTop {
			height: 40%;
			line-height:2cm;
			text-align: center;
			overflow: hidden;
			padding-top: 0.6cm;
		}
		.innerDiv {
			height: 7.5cm;
			width: 10cm;
			/*  border: solid 1px gray;*/
			overflow: hidden;
		}
		.innerBottom {
			padding-top: -1cm;
			height: 60%;
			width:100%;
		}
		.col {
			text-align: center;
		}
		.toplabel{
			float: right;
			margin-right: 0.2cm;
			width: 6.6cm;
		}

		.companyTop{
			margin-top: 0.2cm;
			font-size: 17px;
		}
		.companyBottom{
			margin-top: 0.6cm;
			font-size: 17px;
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
		.data1 {
			/*border-bottom-style: solid;*/
			margin-left: 70pt;
		}
		.innerTop {
			height: 50%;
			overflow: hidden;
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
		.toplabel{
			float: right;
			width: 70%;
		}

		.companyTop{
			margin-top: 20px;
			font-size: 20px;
		}
		.companyBottom{
			margin-top: 40px;
			font-size: 20px;
		}
	}


</style>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'north',title:'',split:true"
		 style="height: 50pt;">
		<div>
			<input type='button' value='打印' onclick='javascript:print()' />
			<input type='button' value='关闭' onclick='window.close()' />
		</div>
	</div>
	<div
			data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<div class=".container" id="center"></div>
	</div>
</div>