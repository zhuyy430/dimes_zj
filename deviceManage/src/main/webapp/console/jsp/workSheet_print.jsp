<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/jsp/js/worksheet.js"></script>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
	$(function(){
        var workSheetNo = '<%=request.getParameter("WorkSheetNo")%>';
		if(workSheetNo=='null' || !workSheetNo){
		    alert("没有打印的工单！");
		    return ;
		}

        $.get("workSheet/queryWorkSheetByNo.do",{No:workSheetNo},function(result){
			$("#no").text(workSheetNo);
			$("#workPieceCode").text(result.workPieceCode);
			$("#workPieceName").text(result.workPieceName);
			$("#unitType").text(result.unitType);
			$("#graphNumber").text(result.graphNumber);
			$("#batchNumber").text(result.batchNumber);
			$("#stoveNumber").text(result.stoveNumber);
			$("#productCount").text(result.productCount);
			$("#productionUnitName").text(result.productionUnitName);
			$("#manufactureDate").text(getDate(new Date(result.manufactureDate)));
            $('#workSheetDetail').iDatagrid({
                url:'workSheet/queryWorkSheetDetailByWorkSheetId.do',
                fitColumns:true,
                singleSelect:true,
                pagination:false,
                columns:[[
                    {field:'id',title:'id',hidden:true},
                    /*{field:'processRoute',title:'加工顺序',width:120,align:'center'},*/
                    {field:'processCode',title:'工序代码',width:120,align:'center'},
                    {field:'processName',title:'工序名称',width:120,align:'center'},
                    {field:'deviceCode',title:'设备代码',width:120,align:'center'},
                    {field:'deviceName',title:'设备名称',width:120,align:'center'},
                    {field:'productionCount',title:'计划数量',width:100,align:'center'},
                    {field:'customColumn',title:'',width:100,align:'center'},
                    {field:'customColumn',title:'',width:100,align:'center'}
                ]],
                queryParams:{
                    workSheetId:result.id
                }
            });
		});
	});
	//打印
    function print() {
        $("#layout").print();
    }
</script>
<style type="text/css">
	.labelStyle{
		font-weight: bold;
	}

	@media print {
		#layout{
			width:210mm;
		}
		#north{
			width:210mm;
		}
		#title{
			font-size: 21px;
		}
		#center{
			width:210mm;
		}
		#workSheetDetail{
			width: 210mm;
			font-size: 14px;
		}
	}
</style>
<div data-toggle="topjui-layout" data-options="fit:true" id="layout">
	<div id="north" data-options="region:'north',title:'',fit:false,border:false,bodyCls:'border_right_bottom'" style="height: 350px;width: 100%;">
		<div id="title" style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
			工序流转卡
		</div>
		<div style="float:left;width:150px;height:150px;margin-left: 50px;" id="qrDiv">
			<img />
			<div id="no">

			</div>
		</div>
		<div title="工序流转卡" data-options="iconCls:'fa fa-th'" style="float: right;width:75%;">
			<div class="topjui-fluid">

				<div class="topjui-row">
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">工件代码:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="workPieceCode"></label>
						</div>
					</div>
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">工件名称:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="workPieceName"></label>
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">规格型号:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="unitType"></label>
						</div>
					</div>
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">图号:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="graphNumber"></label>
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">批号:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="batchNumber"></label>
						</div>
					</div>
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">材料编号:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="stoveNumber"></label>
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">数量:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="productCount"></label>
						</div>
					</div>
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">生产单元:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="productionUnitName"></label>
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm6">
						<label class="topjui-form-label labelStyle">生产日期:</label>
						<div class="topjui-input-block">
							<label class="topjui-form-label" id="manufactureDate"></label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'center',fit:false,split:true,border:false" id="center">
		<table id="workSheetDetail"></table>
	</div>
	<div data-options="region:'south',fit:false,split:true,border:false" style="height:50px;">
		<a href="javascript:void(0)"
		   data-toggle="topjui-menubutton"
		   data-options="iconCls:'fa fa-print'" onclick="print()">打印</a>
	</div>
</div>
