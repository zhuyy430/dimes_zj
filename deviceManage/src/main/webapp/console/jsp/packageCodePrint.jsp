<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/deliveryPlan.js"></script>
<style type="text/css">
.button {
  -webkit-border-radius: 5;
  -moz-border-radius: 5;
  border-radius: 5px;
  text-shadow: 0px -1px 0px #fcf9f8;
  -webkit-box-shadow: inset 0px 1px 3px 0px #91b8b3;
  -moz-box-shadow: inset 0px 1px 3px 0px #91b8b3;
  box-shadow: inset 0px 1px 3px 0px #91b8b3;
  font-family: 微软雅黑;
  color: #070707;
  font-size: 2px;
  padding: 12px 30px 12px 30px;
  border: solid #566963 1px;
  text-decoration: none;
}

.button:hover {
  color: #070707;
  background: #f6f7f7;
  text-decoration: none;
}
</style>
<script type="text/javascript">
	var inventoryCode ="";//物料代码
	var issave = false;
	var printdata = "";
    var deliveryPlanDetailId = '<%=request.getParameter("deliveryPlanDetailId")%>';
    $(function(){
    	resetmsg();
    	showInspectionRecordDetail();
    });
    //生成条码信息
    function generatePackageCode(){
    	var batchNumber = $("#batchNumber").val();
    	var batchNumberOfSended = $("#batchNumberOfSended").val();
    	var codeStart = $("#codeStart").val();
    	var codeEnd = $("#codeEnd").val();
    	var boxNumStart = $("#boxNumStart").val();
    	var boxAmount = $("#boxAmount").val();
    	$.get("packageCode/generatePackageCode.do", {
    		batchNumber:batchNumber,
            batchNumberOfSended:batchNumberOfSended,
            furnaceNumber:batchNumberOfSended,
    		codeStart:codeStart,
    		codeEnd:codeEnd,
    		boxNumStart:boxNumStart,
    		boxAmount:boxAmount,
    		inventoryCode:inventoryCode,
    		deliveryPlanDetailId:deliveryPlanDetailId
		}, function(data) {
			/* $('#deliveryPlanDetailDg').iDatagrid('reloadFooter',
			    data
			); */
			$('#packageTba').iEdatagrid('reload');
		});
    }
    
    //重置条码信息
    function resetmsg(){
    	$("#deliveryPlanDetailId").val(deliveryPlanDetailId);
		$.get("deliveryPlanDetail/queryById.do", {
			Id : deliveryPlanDetailId
		}, function(data) {
			inventoryCode = data.inventoryCode;
			$("#batchNumber").textbox('setValue',  data.batchNumber);
			$("#batchNumberOfSended").textbox('setValue',  data.batchNumberOfSended);
		});

		$.get("packageCode/queryPackageCodeCode.do", {
		}, function(data) {
			$("#codeStart").textbox('setValue',  data);
			$("#codeEnd").textbox('setValue',  data);
			$("#boxNumStart").textbox('setValue',  1);
			$("#boxAmount").textbox('setValue',  1);
			$('#packageTba').iEdatagrid('reload');
		});
    }

    //删除
    function deletemsg(){
			var obj=$('#packageTba').iEdatagrid('getSelected');
			if(!obj){
				$.iMessager.alert('提示','请选中要删除的数据');
   				return false;
			}
			if(window.confirm('你确定要删除吗？')){
				$.get("packageCode/deletePackageCode.do", {
					code:obj.code
				}, function(data) {
					$('#packageTba').iEdatagrid('reload');
				});
			}
    }
    //保存
    function savemsg(){
			$.get("packageCode/addPackageCode.do", {
			}, function(data) {
				issave = true;
				for (var i = 0; i < data.data.length; i++) {
					if(!printdata){
						printdata=data.data[i].id;
					}
					else
						printdata+=","+data.data[i].id;
	        	 }
				
				$.iMessager.alert('提示',data.msg);
				document.getElementById("reset").setAttribute("disabled", true);
				document.getElementById("reset").setAttribute("style", "background: #C0C0C0;");
		    	document.getElementById("create").setAttribute("disabled", true);
		    	document.getElementById("create").setAttribute("style", "background: #C0C0C0;");
			});
    	}
    //打印
    function openWindow(){
    	if(!issave){
    		$.iMessager.alert('提示',"保存后才能打印");
    		return false
    	}
        var checkedArray = $("#packageTba").iEdatagrid("getRows");
        if(checkedArray.length>0){
            var newWin = window.open("console/jsp/packageCode_print.jsp?ids="+printdata);
        }else{
        	$.iMessager.alert('提示',"没有打印内容 !");
            return false;
        }
    }
    function showInspectionRecordDetail() {
        $('#packageTba').iEdatagrid({
            pagination:false,
            autoSave:true,
            url: 'packageCode/queryPackageCode.do',
            updateUrl:'packageCode/updatePackageCodeSession.do'
        });
    }
</script>
</head>
<div  style="width: 100%;">
    <a href="javascript:void(0)" data-toggle="topjui-menubutton"
		data-options="extend: '#deliveryPlanDetailDg-toolbar',
       iconCls: 'fa fa-plus'"
		onclick="savemsg()">保存</a> <a href="javascript:void(0)"
		data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-trash'"
		onclick="deletemsg()">删除</a> <a href="javascript:void(0)"
		data-toggle="topjui-menubutton"
		data-options="extend: '#deliveryPlanDetailDg-toolbar',
       iconCls: 'fa fa-eye'"
		onclick="openWindow()">打印</a>
<div style="width: 100%;">
    <form id="searchForm" method="post">
		<div title="" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height: 10px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm4">
						<label class="topjui-form-label">开始条码</label>
						<div class="topjui-input-block">
							<input id="codeStart" data-toggle="topjui-textbox" readonly="readonly">
						</div>
					</div>
					<div class="topjui-col-sm4	">
						<label class="topjui-form-label">结束条码</label>
						<div class="topjui-input-block">
							<input id="codeEnd" data-toggle="topjui-textbox">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm4">
						<label class="topjui-form-label">开始箱号</label>
						<div class="topjui-input-block">
							<input id="boxNumStart" data-toggle="topjui-textbox">
						</div>
					</div>
					<div class="topjui-col-sm4">
						<label class="topjui-form-label">每箱数量</label>
						<div class="topjui-input-block">
							<input id="boxAmount" data-toggle="topjui-textbox">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm4">
						<label class="topjui-form-label">批号</label>
						<div class="topjui-input-block">
							<input id="batchNumber" data-toggle="topjui-textbox">
						</div>
					</div>
					<div class="topjui-col-sm4">
						<label class="topjui-form-label">材料编号</label>
						<div class="topjui-input-block">
							<input id="batchNumberOfSended" data-toggle="topjui-textbox">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm8">
						<div class="topjui-input-block" style="float: right;">
							<input id="reset" class="button" type="button" value="重置" onclick="resetmsg()" style="background: #f2f6f8;"> <input class="button" id="create"
								type="button" value="生成" onclick="generatePackageCode()" style="background: #f2f6f8;">
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
<div style="height:505px;width:96%;margin:0 auto;" title="箱材料信息">
    <div style="float: left;width: 96%;height:500px;">
        <table id='packageTba'>
					<thead>
						<tr>
							<th field='id' width='250px' hidden="true" align='center'></th>
							<th field='code' width='250px'  align='center'>条码</th>
							<th field='inventoryCode' width='250px' align='center'>物料代码</th>
							<th field='batchNumber' width='250px' align='center'>批号</th>
							<th field='furnaceNumber' width='250px' align='center'>材料编号</th>
							<th field='boxamount' width='250px' align='center' editor="text">每箱数量</th>
							<th field='boxnum' width='250px' align='center'>箱号</th>
						</tr>
					</thead>
				</table>
    </div>
</div>
</body>
</html>
