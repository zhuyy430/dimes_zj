<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<style type="text/css">
.ant-btn {
    line-height: 1.499;
    position: relative;
    display: inline-block;
    font-weight: 400;
    white-space: nowrap;
    text-align: center;
    background-image: none;
    border: 1px solid transparent;
    -webkit-box-shadow: 0 2px 0 rgba(0,0,0,0.015);
    box-shadow: 0 2px 0 rgba(0,0,0,0.015);
    cursor: pointer;
    -webkit-transition: all .3s cubic-bezier(.645, .045, .355, 1);
    transition: all .3s cubic-bezier(.645, .045, .355, 1);
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    height: 50px;
    width:20%;
    padding: 0 15px;
    font-size: 14px;
    border-radius: 4px;
    color: rgba(0,0,0,0.65);
    background-color: #fff;
    border-color: #d9d9d9;
}
</style>

<div class="tyPanelSize">
	<div class="tytitle" style="height: 7%; text-align: center">
		<div style="overflow: hidden; display: inline-block">
			<!-- <img style="float: left; margin-top: 7px;"
				src="mc/assets/css/images/tb.png"> -->
				<span class="fa fa-user-circle fa-2x" aria-hidden="true" style="float: left; margin-top: 10px;margin-right: 10px"></span>
				<span style="float: left; margin-top: 17px;">量具关联</span>
		</div>
	</div>
	<div class="mc_employee_center"
		style="height: 68%;">

		<div style="text-align: center">

			<!-- Nav tabs -->
			<ul id="myTab" class="nav nav-tabs" role="tablist"
				style="overflow: hidden; display: inline-block;">
			</ul>

			<!-- Tab panes -->
			<div class="tab-content"
				style="border-top: 1px solid darkgray; margin-top: -6px;">
				<div role="tabpanel" class="tab-pane active" id="home">
					<div class="eq_center_left" >
						<div><img id="showpicture" src=""
							style=" margin-top: 30px;border: 0px" />
							</div>
						<p id="showeqname"
							style="font-weight: bold; text-align: left; margin-left: 30px;">量具名称:</p>
						<p id="showeqid"
							style="font-weight: bold; text-align: left; margin-left: 30px;">量具编号:</p>

					</div>
					<!-- <div style="width: 650px;float: right;margin-top: 20px;">
								<div><img src="mc/assets/css/images/cx.png" style="float: left;margin-top: 7px;margin-left: 5px;"><input type="text" style="float: left;margin:10px;" /></div>
								<table id="myTable" ></table>
							</div> -->

				</div>
			</div>

		</div>
	</div>
	<div class="mc_employee_bottom" style="height: 25%; overflow: hidden">
		<div style="margin-top: 10px;">
			<!--修改-->
				<div id="" style="float: left; margin-left: 20px;"
				 class="container-fluid functionButton" onclick="getAllEquipments('update')">
				<h6></h6>
				<span class="fa fa-wrench fa-4x" aria-hidden="true"></span>
				<h4 style="text-align: center">修改</h4>
				</div>
			<!--解除-->
				<div id="" style="float: left; margin-left: 10px;"
				 class="container-fluid functionButton" onclick="deleteTool()">
				 <h6></h6>
				<span class="fa fa-times-circle fa-4x" aria-hidden="true"></span>
				<h4 style="text-align: center">解除</h4>
				</div>
			<!--关联-->
				<div id="" style="float: left; margin-left: 10px;"
				 class="container-fluid functionButton" onclick="showmodel('add')">
				<h6></h6>
				<span class="fa fa-chain fa-4x" aria-hidden="true"></span>
				<h4 style="text-align: center">关联</h4>
				</div>
			<!--替换-->
				<div id="" style="float: right; margin-right: 20px;"
				 class="container-fluid functionButton" onclick="getAllEquipments('replace')">
				<h6></h6>
				<span class="fa fa-exchange fa-4x" aria-hidden="true"></span>
				<h4 style="text-align: center">替换</h4>
				</div>
		</div>
		
	</div>
</div>

<!--model-->

		<!-- 模态框（Modal） -->
		<!--修改-->
		<div class="modal fade" id="xgModal"  role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
			<div class="modal-dialog" style="width: 600px;margin-top: 300px">
				<div class="modal-content">
					<div class="modal-body" style="height: 70px;background-color: #D3F3F3">
						<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
						</button>
							<h2 class="modal-title"
								style="border-bottom: 0px solid darkgray; font-size: 25px; padding: 5px;">量具修改</h2>
						</div>
						<div style="padding-left: 100px;height: 430px">
							<div style="margin-top: 20px;">
									<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">量具代码 </span><input
									id="xgequipmentcode" type="text" equipID=""
									style="width: 280px; display: inline-block;"
									class="form-control" readOnly="true" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">名称 </span><input
									id="xgequipmentname" type="text"
									style="width: 280px; display: inline-block;"
									class="form-control" readOnly="true" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">规格型号 </span><input
									id="xgunitType" type="text"
									style="width: 280px; display: inline-block; "
									class="form-control" value="" readonly="readonly"/>
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">辅助人 </span>
										<input type="text" id="xgequipmenthelp" code=""
									style="width: 280px; display: inline-block; "
									class="form-control" />
										<input type="hidden" id="xgequipmenthelpid"
									class="form-control" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">使用频次 </span><input
									id="xgusageRate" type="text"
									style="width: 280px; display: inline-block; "
									class="form-control" value="1" />
							</div>
							<div style="margin-top: 40px;">
									<input	onclick="modelHide()" value="取消" type="button" class="ant-btn" style="margin-left: 8%;float: left;font-weight:bold">
									<input type="button"
									style="margin-left: 8%;background-color: #85E0A3;font-weight:bold" value="确定"  onclick="mappingedit()" class="ant-btn">
							</div>
							
							</div>
							<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
						</div>
					</div>
					<div class="modal-footer">
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
		<!--解除-->
		<!--解除确认框-->
		<div class="modal fade" id="deleteConfirmDlg">
			<div class="modal-dialog">
				<div class="modal-content message_align">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">×</span>
						</button>
						<h4 class="modal-title">提示信息</h4>
					</div>
					<div class="modal-body">
						<p>您确认要解除吗？</p>
					</div>
					<div class="modal-footer">
						<input type="hidden" id="type" />
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<a onclick="deleteData()" class="btn btn-success"
							data-dismiss="modal">确定</a>
					</div>
				</div>
			</div>
		</div>
		<!--关联-->
		<div class="modal fade" id="glModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
			<div class="modal-dialog" style="width: 600px;margin-top: 300px">
				<div class="modal-content">
					<div class="modal-body" style="height: 70px;background-color: #D3F3F3">
						<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
						</button>
							<h2 class="modal-title"
								style="border-bottom: 0px solid darkgray; font-size: 25px; padding: 5px;">量具关联</h2>
						</div>
						<div style="padding-left: 100px;height: 430px">
							<div style="margin-top: 20px;">
									<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">量具代码 </span><input
									id="equipmentcode" type="text" equipID=""
									style="width: 280px; display: inline-block;"
									class="form-control" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">名称 </span><input
									id="equipmentname" type="text"
									style="width: 280px; display: inline-block;"
									class="form-control" readOnly="true" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">规格型号 </span><input
									id="unitType" type="text"
									style="width: 280px; display: inline-block; "
									class="form-control" value=""  readonly="readonly"/>
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">辅助人 </span>
									<input type="text" id="equipmenthelp" code=""
									style="width: 280px; display: inline-block; "
									class="form-control" />
									<input type="hidden" id="equipmenthelpid" code=""
									class="form-control" />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;text-align:right;">使用频次 </span><input
									id="usageRate" type="text"
									style="width: 280px; display: inline-block; "
									class="form-control" value="1" />
							</div>
							<div style="margin-top: 40px;">
									<input	onclick="modelHide()" value="取消" type="button" class="ant-btn" style="margin-left: 8%;float: left;font-weight:bold">
									<input data-toggle="modal" onclick="equipmentSearch('','submit')" type="button"
									style="margin-left: 8%;background-color: #85E0A3;font-weight:bold" value="确定" class="ant-btn">
							</div>
							</div>
							<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
		<!--关联确认框-->
		<div class="modal fade" id="mappingConfirmDlg">
			<div class="modal-dialog">
				<div class="modal-content message_align">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">×</span>
						</button>
						<h4 class="modal-title">提示信息</h4>
					</div>
					<div class="modal-body">
						<p>您确认要关联吗？</p>
					</div>
					<div class="modal-footer">
						<input type="hidden" id="type" />
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<a onclick="mappingcommit()" class="btn btn-success"
							data-dismiss="modal">确定</a>
					</div>
				</div>
			</div>
		</div>
		<!--替换-->
		<div class="modal fade" id="thModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
			<div class="modal-dialog" style="width: 780px;margin-top: 300px">
				<!-- <div class="modal-content">

					<div class="modal-body" style="height:360px; ">
						<h2 class="modal-title"
							style="border-bottom: 2px solid darkgray; text-align: center;">替换</h2> -->
				<div class="modal-content">
					<div class="modal-body" style="height: 70px;background-color: #D3F3F3">
						<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
						</button>
							<h2 class="modal-title"
								style="border-bottom: 0px solid darkgray; font-size: 25px; padding: 5px;">量具替换</h2>
						<div
							style="float: left; width: 330px; border-right: 1px solid darkgray; margin-top: 20px; padding: 5px; padding-left: 50px;float: left;margin-left: 30px">
							<div style="margin-top: 10px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">量具代码 </span><input
									id="oldId" type="text" equipID=""
									style="width: 180px; display: inline-block;text-align:right; "
									class="form-control" readonly />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">名称 </span><input
									id="oldName" type="text"
									style="width: 180px; display: inline-block;text-align:right; "
									class="form-control" readonly />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">规格型号 </span><input
									id="oldUnitType" type="text"
									style="width: 180px; display: inline-block;text-align:right; "
									class="form-control" readonly />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">辅助人 </span><input
									id="oldUserHelp" type="text"
									style="width: 180px; display: inline-block;text-align:right; "
									class="form-control" readonly />
							</div>
							<div style="margin-top: 20px;margin-bottom: 10px">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">使用频次 </span><input
									id="oldusageRate" type="text"
									style="width: 180px; display: inline-block;text-align:right; "
									class="form-control" readonly />
							</div>
						</div>
						<div style="float: left;width: 10px;margin-top:20px;height:280px;">
							<!-- <span class="fa fa-arrow-left" aria-hidden="true" style="font-size: 50px;margin-left: 15px"></span> -->
						</div>
						<div
							style="float: right; width: 330px; border-left: 1px solid darkgray; margin-top: 20px;  padding: 5px; padding-left: 10px;float: left;margin-right: 30px">
							<div style="margin-top: 10px;">
									<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">量具代码 </span><input
									id="newId" type="text" equipID=""
									style="width: 180px; display: inline-block;text-align:right;"
									class="form-control"  />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">名称 </span><input
									id="newName" type="text"
									style="width: 180px; display: inline-block;text-align:right;"
									class="form-control" readonly />
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">规格型号 </span><input
									id="newUnitType" type="text"
									style="width: 180px; display: inline-block;text-align:right;"
									class="form-control" readonly="readonly"/>
							</div>
							<div style="margin-top: 20px;">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">辅助人 </span>
									<input type="text" id="newUserHelp" code=""
									style="width: 180px; display: inline-block;text-align:right;"
									class="form-control" />
									<input type="hidden" id="newUserHelpid" code=""
									class="form-control" />
							</div>
							<div style="margin-top: 20px;margin-bottom: 10px">
								<span style="width: 60px; display: inline-block;padding-right:3px;font-weight:bold;">使用频次 </span><input
									id="newusageRate" type="text"
									style="width: 180px; display: inline-block;text-align:right;"
									value="1" class="form-control" />
							</div>
						</div>
					</div>
					<div class="modal-footer" style="border: 0px solid darkgray;">
						<div style="float: right; width: 100%;">
									<input onclick="mappingreplace()" type="button"
									style="margin-right: 17%;background-color: #85E0A3;width: 10%;float: right;font-weight:bold" value="确定" class="ant-btn">
							<input	onclick="modelHide()" value="取消" type="button" class="ant-btn" style="font-weight:bold;margin-right: 4%;float: right;width: 10%">
						</div>

					</div>
					<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
						</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
		<!--model-->
<script type="text/javascript">
var action;
var helpaction;
var userexist=false;
$(function(){
	//量具二维码扫描框回车事件
	$("#Equipment").keydown(function(q) {
		if (q.keyCode == 13) {		
			
			if($("#Equipment").val()==null||$("#Equipment").val()==""){
				$("#Equipment").val("");
				$("#alertText").text("请输入量具代码");
				$("#alertDialog").modal();
			}else{
			$.ajax({
				url : "equipment/queryAllEquipments.do",
				data : {q:$("#Equipment").val()},
				cache:false,
				dataType : "json",
				success : function(data) {
					//var equipment= data.get(0);
					if(data[0]!=null){
						if(action=="add"){
							$('#scanEquipmentcodeDialog').modal('hide');
							showmodel("add");
							$("#equipmentcode").val($("#Equipment").val()).change();
							}else if(action=="replace"){
								$('#scanEquipmentcodeDialog').modal('hide');
								
								$("#newId").val($(this).val()).change();
								showmodel("replace");
							}
					}else{
						$("#Equipment").val("");
						$("#alertText").text("没有此种量具");
						$("#alertDialog").modal();
					}
				}
			});
			}
		
		}
	});	
	//量具二维码扫描框改变事件
	 $("#Equipment").change(function(q) {
		 if($("#Equipment").val()==null||$("#Equipment").val()==""){
				$("#Equipment").val("");
				$("#alertText").text("请输入量具代码");
				$("#alertDialog").modal();
			}else{
			$.ajax({
				url : "equipment/queryAllEquipments.do",
				data : {q:$("#Equipment").val()},
				cache:false,
				dataType : "json",
				success : function(data) {
					//var equipment= data.get(0);
					if(data[0]!=null){
						if(action=="add"){
							$('#scanEquipmentcodeDialog').modal('hide');
							showmodel("add");
							$("#equipmentcode").val($("#Equipment").val()).change();
							}else if(action=="replace"){
								$('#scanEquipmentcodeDialog').modal('hide');
								
								$("#newId").val($("#Equipment").val()).change();
								showmodel("replace");
							}
					}else{
						$("#Equipment").val("");
						$("#alertText").text("没有此种量具");
						$("#alertDialog").modal();
					}
				}
			});
			}
	});	 
	//辅助人二维码扫描框回车事件
	$("#UserCode").keydown(function(event) {
		if (event.keyCode == 13) {
			if ($("#UserCode").val()==null||$("#UserCode").val()=="") {
				$("#UserCode").val("");
				$("#alertText").text("请输入员工代码！");
				$("#alertDialog").modal();
			} else {
				$
				.ajax({
					url : contextPath
							+ "employee/queryEmployeeByCode.do",
					type : 'post',
					data : {
						 "code" : $("#UserCode").val()
					},
					cache:false,
					success : function(data) {
						// alert(JSON.stringify(data));
						if(!data){
							$("#UserCode").val("");
							$("#alertText").text("该员工信息不存在，请重新输入！");
							$("#alertDialog").modal();
						}else{
								if(helpaction=="add"){
									helperselect("add");
									}else if(helpaction=="replace"){
										helperselect("replace");
									}else if(helpaction=="update"){
										helperselect("update");
									}
						}
					}
				})
		}
			}
	});
	//辅助人二维码扫描框改变事件
	$("#UserCode").change(function(event) {
		if ($("#UserCode").val()==null||$("#UserCode").val()=="") {
			$("#UserCode").val("");
			$("#alertText").text("请输入员工代码！");
			$("#alertDialog").modal();
		} else {
			$
			.ajax({
				url : contextPath
						+ "employee/queryEmployeeByCode.do",
				type : 'post',
				data : {
					 "code" : $("#UserCode").val()
				},
				cache:false,
				success : function(data) {
					// alert(JSON.stringify(data));
					if(!data){
						$("#UserCode").val("");
						$("#alertText").text("该员工信息不存在，请重新输入！");
						$("#alertDialog").modal();
					}else{
							if(helpaction=="add"){
								helperselect("add");
								}else if(helpaction=="replace"){
									helperselect("replace");
								}else if(helpaction=="update"){
									helperselect("update");
								}
					}
				}
			})
	}
	}); 

});
//onblur="lostfocus()" class="form-control" id="Equipment"
	function mappingConfirm(){
		/* if(!userexist){
			return false;
		} */
		$('#mappingConfirmDlg').modal('show');	
	}
	function lostfocus(){
		$('#Equipment').focus();	
		$('#UserCode').focus();	
	}
	//弹出(关联/替换)框
	function showmodel(e) {
		
		if(e=='add'){
			
			$('#equipmentcode').val("");
			$('#equipmentname').val("");
			$('#unitType').val("");
			$('#equipmenthelp').val("");
			$('#usageRate').val("1");
			
			$('#glModal').modal('show');
			//弹出框显示后触发事件
			$('#glModal').on('shown.bs.modal', function(e) {
				$('#equipmentcode').focus();
			});
		}else if(e=="replace"){
			$('#thModal').modal('show');
			//弹出框显示后触发事件
			$('#thModal').on('shown.bs.modal', function(e) {
				$('#newId').focus();
			});
		}else if(e=="update"){
			
			
			$('#xgModal').modal('show');
			//弹出框显示后触发事件
			$('#xgModal').on('shown.bs.modal', function(e) {
				$('#xgequipmenthelp').focus();
			});
		}
	}
	
	
	//量具代码文本框改变事件

		$("#equipmentcode").change(function() {
		var	codetext=$("#equipmentcode").val();
		if(codetext==null||codetext==""){
			codetext="+++_+_+_+__++";
		}
		equipmentSearch(codetext,'');
		})
		$("#newId").change(function() {
		var	codetext=$("#newId").val();
		if(codetext==null||codetext==""){
			codetext="+++_+_+_+__++";
		}
		equipmentSearch(codetext,'');
		})
	
		//量具代码文本框回车事件
		$("#equipmentcode").keydown(function(event) {
				if (event.keyCode == 13) {
					var	codetext=$("#equipmentcode").val();
					if(codetext==null||codetext==""){
						codetext="+++_+_+_+__++";
					}
					equipmentSearch(codetext,'');
				}
			});
		
		
		
		$("#newId").keydown(function(event) {
				if (event.keyCode == 13) {
					var	codetext=$("#newId").val();
					if(codetext==null||codetext==""){
						codetext="+++_+_+_+__++";
					}
					equipmentSearch(codetext,'');
				}
			});
	
	//辅助人代码文本框改变事件

		$("#xgequipmenthelp").change(function() {
			$("#UserCode").val("");
		helperselect("kupdate");
		})
		
		$("#equipmenthelp").change(function() {
			$("#UserCode").val("");
		helperselect("kadd");
		})		
		$("#newUserHelp").change(function() {
			$("#UserCode").val("");
		helperselect("kreplace");
		})		
		
		//辅助人代码文本框回车事件

		$("#xgequipmenthelp").keydown(function(event) {
				if (event.keyCode == 13) {
					$("#UserCode").val("");
					helperselect("kupdate");
				}
			});
		
		
		
		
		$("#equipmenthelp").keydown(function(event) {
				if (event.keyCode == 13) {
					$("#UserCode").val("");
					helperselect("kadd");
				}
			});
		
		
		$("#newUserHelp").keydown(function(event) {
				if (event.keyCode == 13) {
					$("#UserCode").val("");
					helperselect("kreplace");
				}
			});
		
	//模态框点击返回事件(hide)
	function modelHide() {
		$('#glModal').modal('hide');
		$('#xgModal').modal('hide');
		$('#thModal').modal('hide');
	}
	
	//点击tab事件
	var Code;
	var Devid;
	var id;
	function choosetab(e) {
		var u;
		Code = e.getAttribute("dataid");
		id = u;
		getDevid();
		$("#myTable").bootstrapTable("refresh", {
			url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
			cache:false,
			query : {
				searchText : "",
				deviceSiteCode : Code
			}
		});
	};
	//通过站点代码得到站点id
	function getDevid() {
		$.get("mcequipment/findDeviceSiteId.do", {
			deviceSiteCode : Code
		}, function(data) {
			Devid = data.id;
		});
		$("#usageRate").val("1");
	
		$('#equipmentcode').val("");
		$('#equipmentname').val("");
		
		$("#UserCode").val("");
		$("#equipmenthelp").val("");
	}

	//搜索点击事件
	function toolsearch() {
		var text = $("#search").val();
		$("#myTable").bootstrapTable("refresh", {
			url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
			cache:false,
			query : {
				searchText : text,
				deviceSiteCode : Code
			}
		});
	};
	
	
	
	var operationType = "add";
	/**弹出新增或修改ng记录框
	type:
		add:新增
		update:修改
	 */

	
	//设置员工信息:回车或扫码
 	function helperselect(e) {
 		var userCode;
 		userexist = false;
 		//var userName;
		if(e=='update'){
			userCode = $("#UserCode").val();
		}else if(e=='add'){
			userCode = $("#UserCode").val();		
		}else if(e=='replace'){
			userCode = $("#UserCode").val();		
		}else if(e=='kupdate'){
			userCode = $("#xgequipmenthelp").val();
		}else if(e=='kadd'){
			/* var username=$("#equipmenthelp").val()
			var stuta = (username=="undefined" || username == null || username == "");
			console.log(stuta) */
			userCode = $("#equipmenthelp").val();
		}else if(e=='kreplace'){
			userCode = $("#newUserHelp").val();
		}

		if (!userCode) {
			$("#alertText").text("请输入员工代码！");
			$("#alertDialog").modal();
		} else {
			$
			.ajax({
				url : contextPath
						+ "employee/queryEmployeeByCode.do",
				type : 'post',
				data : {
					 "code" : userCode
				},
				cache:false,
				success : function(data) {
					// alert(JSON.stringify(data));
					if(!data){
						$("#alertText").text("该员工信息不存在，请重新输入！");
						$("#alertDialog").modal();
					}else
						userexist = true;
			
						$('#scanHelpercodeDialog').modal('hide');
					if(e=='update'){
						$("#xgequipmenthelp").val(data.name);
						$("#xgequipmenthelpid").val(data.code);
						$('#xgModal').modal('show');
					}else if(e=='add'){
						$("#equipmenthelp").val(data.name);
						$("#equipmenthelp").attr("code",data.code);
						$('#glModal').modal('show');
					}else if(e=="replace"){
						$("#newUserHelp").val(data.name);
						$("#newUserHelp").attr("code",data.code);
						$('#thModal').modal('show');
					}else if(e=='kupdate'){
						$("#xgequipmenthelp").val(data.name);
						$("#xgequipmenthelpid").val(data.code);
						$('#xgModal').modal('show');
					}else if(e=='kadd'){
						$("#equipmenthelp").val(data.name);
						$("#equipmenthelp").attr("code",data.code);
						$('#glModal').modal('show');
					}else if(e=="kreplace"){
						$("#newUserHelp").val(data.name);
						$("#newUserHelp").attr("code",data.code);
						$('#thModal').modal('show');
					}
				 
			}
		})
	} 
	}
	
	//通过量具code查找量具名称
	function equipmentSearch(e,refer) {
		if(!e){
			e=$("#equipmentcode").val();
		}
			$
			.ajax({
				url : contextPath
						+ "mcMeasuringToolMappingRecord/mcQueryMeasuringToolsByCode.do",
				type : 'post',
				data : {
					"q" : e
				},
				cache:false,
				success : function(data) {
					if(data){
						$("#equipmentcode").val(data.code);
						$("#equipmentname").val(data.equipmentType.name);
						$("#unitType").val(data.equipmentType.unitType);
						$("#newName").val(data.equipmentType.name);
						$("#newId").val(data.code);
						$("#newUnitType").val(data.equipmentType.unitType);
						$("#equipmentcode").attr("equipID",data.id);
						$("#newId").attr("equipID",data.id);
						if(refer){
							$('#mappingConfirmDlg').modal('show');
						}
						}else{
							$("#alertText").text("没有此种装备");
							$("#alertDialog").modal();
						}
				}
			})
	}
	
	
	

	//关联(辅助人下拉框)
	function userSearch(e) {
		$.ajax({
			url : "user/queryNotCurrentUsers.do",
			data : {},
			dataType : "json",
			cache:false,
			success : function(data) {
				var selectvalue = "";
				$.each(data, function(index, user) {
					selectvalue += "<option value='"+user.id+"'>"
							+ user.username + "</option>";
				})
				$("#equipmenthelp").append(selectvalue);
				$("#xgequipmenthelp").append(selectvalue);
				$("#newUserHelp").append(selectvalue);
				$(".selectpicker").selectpicker('refresh');
			}
		});
	}

	//量具关联修改
	function mappingedit() {
		/* if(!userexist){
			return false;
		} */
		var equipmentId = $("#xgequipmentcode").attr("equipID");//量具代码
		var userHelp = $("#xgequipmenthelpid").val(); //辅助人ID
	//	var userHelpname = $("#xgequipmenthelp").val(); //辅助人name
		var equipmentname = $("#xgequipmentname").val(); //量具名称
		var fre = $("#xgusageRate").val(); //使用频次
		$.post(contextPath
				+ "mcMeasuringToolMappingRecord/mcUpdateMeasuringToolMappingRecord.do", {
			//mappingDate:$('#mappingDate').val(),
			'equipment.id' : equipmentId,
			'deviceSite.code' : Code,
			helperId : userHelp,
		//	helperName : userHelpname,
			'equipment.name' : equipmentname,
			usageRate : fre,
			id : id
		}, function(data) {
			//alert(JSON.stringify(data));
			if (data.success) {
				$("#alertText").text("修改成功");
				$("#alertDialog").modal();
				$('#xgModal').modal('hide');
				$("#myTable").bootstrapTable("refresh", {
					url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
					cache:false,
					query : {
						searchText : "",
						deviceSiteCode : Code
					}
				});
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
				$('#xgModal').modal('hide');
			}
		});

	}
	//获取选中量具关联信息
	function getAllEquipments(e) {
		var tf = (typeof (id) == "undefined");
		if (tf) {
			$("#alertText").text("请选择一条数据!");
			$("#alertDialog").modal();
			return false;
		} else {
			getDevid();
			$.ajax({
						url : contextPath
								+ "equipmentMappingRecord/queryEquipmentMappingRecordById.do",
						type : 'post',
						data : {
							"id" : id
						},
						cache:false,
						success : function(data) {
							if (e == "update") {
								/* var options=document.getElementById('equipmentid');
								alert(options) */
								// $("#equipmentid").val(data.equipment.id);
								$("#xgequipmentcode").attr("equipID",data.equipment.id);
								$("#xgequipmentcode").val(data.equipment.code);
								$("#xgunitType").val(data.equipment.equipmentType.unitType);
								$("#xgequipmentname").val(data.equipment.equipmentType.name);
								//$("#xgequipmenthelpid").val(data.helperId);
							    //$("#xgequipmenthelp").val(data.helperName);
							    $("#xgequipmenthelpid").val('');
								$("#xgequipmenthelp").val('');
								if(data.helperId!=null&&data.helperId!=''){
									$
									.ajax({
										url : contextPath
												+ "employee/queryEmployeeByCode.do",
										type : 'post',
										data : {
											"code" : data.helperId
										},
										cache:false,
										success : function(data) {
											$("#xgequipmenthelp").val(data.name);
											$("#xgequipmenthelpid").val(data.code);
										}
									});
								}
								$('#xgModal').modal('show');
							} else {
								$("#oldId").val(data.equipment.code);
								$("#oldName").val(data.equipment.equipmentType.name);
								$("#oldUnitType").val(data.equipment.equipmentType.unitType);
								//$("#oldUserHelp").val(data.helperName);
								$("#oldusageRate").val(data.usageRate);
								if(data.helperId){
									
									$.ajax({
										url : contextPath
												+ "employee/queryEmployeeByCode.do",
										type : 'post',
										data : {
											"code" : data.helperId
										},
										cache:false,
										success : function(data) {
											$("#oldUserHelp").val(data.name);
										}
									});
								}
								$("#newId").val("");
								$("#newUserHelp").val("");
								$("#newName").val("");
								$("#newusageRate").val("1");
								showmodel('replace');
							}
						}
					});
		}
	}
	//替换提交
	function mappingreplace() {
		/* var username=$("#newUserHelp").val()
		var stuta = (username=="undefined" || username == null || username == "");
		if(!userexist&&stuta){
			return false;
		} */
		
		var newId = $('#newId').val();
		var replaceid = $('#newId').attr("equipID");
		if (!newId) {
			$("#alertText").text("输入替换的量具代码!");
			$("#alertDialog").modal();
			return false;
		}
		//判断装备是否存在
		$.ajax({
			url : contextPath
			+ "mcMeasuringToolMappingRecord/mcQueryMeasuringToolsByCode.do",
	type : 'post',
	data : {
		"q" : newId
	},
			cache:false,
			dataType : "json",
			success : function(data) {
				//var equipment= data.get(0);
				if(data==null){
					return false;
				}else{
					replaceid = data.id;
					var newUserHelp = $('#newUserHelp').val();
					var newName = $('#newName').val();
					var newusageRate = $('#newusageRate').val();
					var unbindsuccess;
					
					//量具解除
					$
					.get(
							"mcMeasuringToolMappingRecord/mcReplaceMeasuringToolMappingRecord.do",
							{
								//mappingDate:$('#mappingDate').val(),
								id : id,
								'equipment.id' : replaceid,
								'deviceSite.code' : Code,
								helperName : $("#newUserHelp").val(),
								helperId : $("#newUserHelp").attr("code"),
								usageRate : $('#newusageRate').val()
							}, function(data) {
								if (data.success) {
									$("#alertText").text("替换成功");
									$("#alertDialog").modal();
									$('#thModal').modal('hide');
									$("#myTable").bootstrapTable("refresh", {
										url : contextPath
										+ "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
										cache:false,
										query : {
											searchText : "",
											deviceSiteCode : Code
										}
									});
								} else {
									$("#alertText").text(data.msg);
									$("#alertDialog").modal();
									$('#thModal').modal('hide');
								}
							});
				}
			}
		});
		
	}
	//关联提交
	function mappingcommit() {
		
		var equipmentid = $("#equipmentcode").attr("equipID");
		var equipmenthelpname = $("#equipmenthelp").val();
		var equipmenthelpid = $('#equipmenthelp').attr("code");
		var usageRate = $('#usageRate').val()
		$.get("mcMeasuringToolMappingRecord/mcAddMeasuringToolMappingRecord.do", {
			//mappingDate:$('#mappingDate').val(),
			'equipment.id' : equipmentid,
			'deviceSite.id' : Devid,
		//	helperName : equipmenthelpname,
			helperId : equipmenthelpid,
			helperName : $("#addequipmenthelp").val(),
			usageRate : usageRate
		}, function(data) {
			if (data.success) {
				$("#alertText").text("成功关联");
				$("#alertDialog").modal();
				$("#myTable").bootstrapTable("refresh", {
					url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
					cache:false,
					query : {
						searchText : "",
						deviceSiteCode : Code
					}
				});
				$('#glModal').modal('hide');
				//$('#departmentAddDialog').iDialog('close');
				//$('#departmentDg').iDatagrid('reload');
			} else {
				$("#alertText").text(data.msg);
				$("#alertDialog").modal();
			}
		});

		/* var helperName=$("#equipmenthelp").find("option:selected").text();
		var	helperId=$('#equipmenthelp').val();
		alert(helperName+"==="+helperId);  */
	}

	//量具关联解除
	function deleteData() {
		$.ajax({
			type : "post",
			url : contextPath
					+ "mcMeasuringToolMappingRecord/mcUnbindMeasuringToolMappingRecord.do",
			data : {
				"id" : id
			},
			cache:false,
			dataType : "json",
			success : function(data) {
				if (data.success) {
					$("#myTable").bootstrapTable("refresh", {
						url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
						cache:false,
						query : {
							searchText : "",
							deviceSiteCode : Code
						}
					});

					$("#alertText").text(data.message);
					$("#alertDialog").modal();
				}
			}
		});
	}
	//量具解除条件确认
	function deleteTool() {
		var tf = (typeof (id) == "undefined");
		if (!tf) {
			//显示删除确认框
			$("#deleteConfirmDlg").modal();
		} else {
			$("#alertText").text("请选择要解除的数据!");
			$("#alertDialog").modal();
			return false;
		}
	};
	//获取设备站点代码
	$(function() {
		$
				.ajax({
					type : "post",
					url : "mcdeviceSite/getAllMCDeviceSite.do",
					data : {},
					cache:false,
					dataType : "json",
					success : function(data) {
						//  alert(data);
						var htmltop = "";
						$.each(
										data,
										function(index, MCDeviceSite) {
											if(index==0){
												/* Devid = MCDeviceSite.id; */
												Code = MCDeviceSite.deviceSiteCode;
												getDevid();
											}
											//   html+="<option value='" + MCDeviceSite.deptNo + "'>" + dept.deptName + "</option>";
											htmltop += "<li role='presentation' onclick='choosetab(this)' dataid='"
													+ MCDeviceSite.deviceSiteCode
													+ "'><a href='#home' aria-controls='"+MCDeviceSite.deviceSiteCode+"' role='tab' data-toggle='tab'>"
													+ MCDeviceSite.deviceSiteName
													+ "</a></li>";
										})
						$("#myTab").append(htmltop);
						$('#myTab a:first').tab('show');
						var htmlbody = "";
						htmlbody += "<div class='eq_center_right'><div><span onclick='toolsearch()' class='fa fa-search' aria-hidden='true' style='float: left;margin-top: 7px;font-size: 30px;'></span><input id='search' type='text' style='float: left;margin:10px;' /></div><table id='myTable' style='overflow: hidden;text-overflow: ellipsis;white-space: nowrap;'></table></div>";

						$("#home").append(htmlbody);
						$("#myTable")
								.bootstrapTable(
										{
											url : "mcMeasuringToolMappingRecord/mcQueryMeasuringToolMappingRecordByDeviceSiteCode.do",
											cache:false,
											height : shortTableHeight,											
											singleSelect : true,
											striped : true, //隔行换色
											queryParams : function(params) {
												//这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
												var temp = {
													deviceSiteCode : Code,
													searchText : ""
												};
												return temp;
											},
											columns : [
													{
														align: 'center',
														field : 'id',
														title : 'ID'
													},
													{
														align: 'center',
														field : 'equipment.id',
														title : 'eqid'
													},
													{
														align: 'center' ,
														field : 'equipment.code',
														title : '量具代码'
													},
													{
														align: 'center',
														field : 'equipment.equipmentType.name',
														title : '名称'
													},
													{
														align: 'center',
														field : 'equipment.equipmentType.unitType',
														title : '规格型号'
													},
													{
														align: 'center',
														field : 'equipment.equipmentType.measurementType',
														title : '计量类型'
													},
													{
														align: 'center',
														field : 'equipment.cumulation',
														title : '计量累积'
													},
													{
														align: 'center',
														field : 'equipment.equipmentType.measurementObjective',
														title : '计量目标'
													},
													{
														align: 'center',
														field : 'equipment.measurementDifference',
														title : '计量差异'
													},{
														align: 'center',
														field : 'usageRate',
														title : '使用频次'
													},{
														align: 'center',
														field : 'mappingDate',
														title : '关联时间',
														formatter : function(value, row, index) {
															if (value) {
																var date = new Date(value);
																return date.getFullYear() + '-'
																		+ ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + '-'
																		+ (date.getDate()>9?date.getDate():"0"+date.getDate())+'  '
																		+ (date.getHours()>9?date.getHours():"0"+date.getHours())+':'
																		+ (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes())+':'
																		+ (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
															}
														}
													} ]
										});
						//隐藏列为Id的列
						$('#myTable').bootstrapTable('hideColumn', 'id');
						$('#myTable').bootstrapTable('hideColumn',
								'equipment.id');
						//选中行
						$("#myTable").on(
								'click-row.bs.table',
								function(e, row, element) {
									id = row.id;
									$('.success').removeClass('success'); //去除之前选中的行的，选中样式
									$(element).addClass('success'); //添加当前选中的 success样式用于区别	
									$("#showpicture").attr("src",row.equipment.picName);
									$("#showeqname").html(
											"选中量具:<span>" + row.equipment.equipmentType.name
													+ "</span>");
									$("#showeqid").html(
											"量具编号:<span>" + row.equipment.code
													+ "</span>");

								});
						/*  window.location.reload(); */
					},
					error : function() {
						$("#alertText").text("出错");
						$("#alertDialog").modal();
					}

				});
		
	})

	//tab的方法
	$(function() {
		$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
			// 获取已激活的标签页的名称
			activeTab = $(e.target).text();
			$("#alertText").text(activeTab);
			$("#alertDialog").modal();
			// 获取前一个激活的标签页的名称
			var previousTab = $(e.relatedTarget).text();
			//alert(previousTab);
			$(".active-tab span").html(activeTab);
			$(".previous-tab span").html(previousTab);
		});
	});

	//tab选中样式
	$(function() {
		$("#myTab li:first").addClass("active");
	})
</script>


<%@ include file="end.jsp"%>