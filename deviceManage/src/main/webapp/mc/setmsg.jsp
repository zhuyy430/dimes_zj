<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script type="text/javascript">
	$.ajaxSetup({cache:false});
	//弹出添加设备站点窗口
	function showAddDeviceSitesDialog(){
		$('#showDeviceSitesListTable').bootstrapTable('refresh');
		$("#addDeviceSitesDlg").modal();
		$.ajax({
        	type : "post",
            url : contextPath + "productionUnit/queryAllLeafProductionUnits.do",
            data : {},
			cache:false,
            dataType : "json",
            success : function(data) {
            	if(jQuery.isEmptyObject(data)){
					getAllReasonAdd();
            	}else{
                var htmlselect = "<option></option>";
	               $.each(data,function(index, Type) {
	                  htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
	               })
	               $("#productionUnitName").empty();
	               $("#productionUnitName").append(htmlselect);		
	               $("#productionUnitName").selectpicker('refresh');
            	}
            }
		})
	}
	//弹出添加用户的窗口
	function showAddUsersDialog(){
		$('#showUsersListTable').bootstrapTable('refresh');
		$("#addUsersDlg").modal();
	}
	//删除站点或用户
	function deleteData(){
		var type=$("#type").val();
		//删除站点
		if("deviceSite"==type){
			var selections =  $('#deviceSiteListTable').bootstrapTable("getSelections");
			var idsArray = new Array();
			for(var i = 0;i<selections.length;i++){
				var obj = selections[i];
				idsArray.push(obj.id);
			}
			$.get(contextPath + "mcdeviceSite/deleteMCDeviceSites.do",{mcdsids:JSON.stringify(idsArray)},function(result){
				//刷新站点列表
				$("#deviceSiteListTable").bootstrapTable("refresh");
			});
		}
		//删除用户
		if("user"==type){
			var selections =  $('#userListTable').bootstrapTable("getSelections");
			var idsArray = new Array();
			for(var i = 0;i<selections.length;i++){
				var obj = selections[i];
				idsArray.push(obj.id);
			}
			$.get(contextPath + "mcuser/deleteMCUsers.do",{ids:JSON.stringify(idsArray)},function(result){
				//刷新站点列表
				$("#userListTable").bootstrapTable("refresh");
			});
		}
	}
	
	//设备站点条件查询
	function reloadDevice(){
		var deviceName = $("#deviceName").val();
		var deviceCode = $("#deviceCode").val();
		var productionUnitCode = $("#productionUnitName option:selected").val()
		
		var opt = {
        url: "mcdeviceSite/getAllDeviceSite.do",
        silent: true,
        query:{
        	deviceName:deviceName,
        	deviceCode:deviceCode,
        	productionUnitCode:productionUnitCode
        }
    };
			$("#showDeviceSitesListTable").bootstrapTable("refresh",opt);
	}

	$(function() {
        $.get(contextPath + "mcWarehouse/queryMCWarehouse.do",function(result){
            if(result){
                $("#cWhCode").val(result.cWhCode);
            }
        });
	//NG仓库失去焦点事件
    	$("#cWhCode").blur(function(){
    	    var cwhCode = $("#cWhCode").val();
            $.get(contextPath + "mcWarehouse/addOrUpdateMCWarehouse.do",{cwhCode:cwhCode},function(result){
                if(!result.success){
                    $("#alertText").text(result.msg);
                    $("#alertDialog").modal();
                }
            });
    	})
		$(".selectpicker").selectpicker({
            noneSelectedText: '请选择' //默认显示内容  
        });
		//添加设备站点按钮点击事件
		$("#addDeviceSitesBtn").click(function(){
			//获取所有被选中的数据
			var selections =  $('#showDeviceSitesListTable').bootstrapTable("getSelections");
			if(selections.length<=0){
				return false;
			}
			//向后台传递参数的数组
			var objArr = new Array();
			for(var i = 0;i<selections.length;i++){
				var obj = selections[i];
				objArr.push(obj);
			}
			
			$.get(contextPath + "mcdeviceSite/addMCDeviceSites.do",{objs:JSON.stringify(objArr)},function(result){
				//刷新站点列表
				$("#deviceSiteListTable").bootstrapTable("refresh");
			});
		});
		//添加用户按钮点击事件
		$("#addUsersBtn").click(function(){
			//获取所有被选中的数据
			var selections =  $('#showUsersListTable').bootstrapTable("getSelections");
			if(selections.length<=0){
				return false;
			}
			//向后台传递参数的数组
			var objArr = new Array();
			for(var i = 0;i<selections.length;i++){
				var obj = selections[i];
				objArr.push(obj);
			}
			
			$.get(contextPath + "mcuser/addMCUsers.do",{objs:JSON.stringify(objArr)},function(result){
				//刷新站点列表
				$("#userListTable").bootstrapTable("refresh");
			});
		});
		//删除设备站点按钮点击事件
		$("#deleteDeviceSitesBtn").click(function(){
			var selections =  $('#deviceSiteListTable').bootstrapTable("getSelections");
			if(selections.length<=0){
				$("#alertText").text("请选择要删除的数据!");
        		$("#alertDialog").modal();
				return false;
			}
			$("#type").val("deviceSite");
			//显示删除确认框
			 $("#deleteConfirmDlg").modal();
		});
		//删除用户按钮点击事件
		$("#deleteUsersBtn").click(function(){
			var selections =  $('#userListTable').bootstrapTable("getSelections");
			if(selections.length<=0){
				$("#alertText").text("请选择要删除的数据!");
        		$("#alertDialog").modal();
				return false;
			}
			$("#type").val("user");
			//显示删除确认框
			 $("#deleteConfirmDlg").modal();
		});
		
		
		//显示当前主机上的设备站点
		$("#deviceSiteListTable").bootstrapTable({
			idField : 'id',
			url : "mcdeviceSite/getAllMCDeviceSite.do",
			cache: false,
			striped : true, //隔行换色
			clickToSelect : true,
			checkboxHeader : true,
			toolbarAlign:'left',
			height:400,
			columns : [ {
				checkbox : true,
				field : '',
				title : ''
			}, {
				field : 'deviceCode',
				title : '设备代码'
			}, {
				field : 'deviceName',
				title : '设备名称'
			}, {
				field : 'deviceSiteCode',
				title : '设备站点代码'
			}, {
				field : 'deviceSiteName',
				title : '设备站点名称'
			} ],
			toolbar : '#deviceSiteListToolbar'
		});
		$("#userListTable").bootstrapTable({
			idField : 'id',
			url : "mcuser/getAllMCUser.do",
			cache: false,
			striped : false, //隔行换色
			clickToSelect : true,
			checkboxHeader : true,
			height:380,
			columns : [ {
				checkbox : true,
				field : '',
				title : ''
			}, {
				field : 'username',
				title : '用户账号'
			}, {
				field : 'employeeCode',
				title : '员工代码'
			}, {
				field : 'employeeName',
				title : '员工姓名'
			}],
			toolbar : '#userListToolbar'
		});
	});
</script>
<div class="container-fluid" style="width: 100%; height: 967px;">
	<div class="container-fluid"
		style="margin-top: 1%; border-bottom: 2px solid #5B5B5B;">
		<label style="font-size: 25px; margin-left: 10%">关联设备</label>
		<button class="btn btn-default" style="float: right; margin-right: 1%"
			onclick="window.location.href=contextPath + 'mc/main.jsp'">
			<span class="fa fa-mail-reply "></span>
		</button>
	</div>

	<div style="height: 420px; width: 80%;" class="container-fluid">
		<table id="deviceSiteListTable"></table>
	</div>
    <div class="container-fluid"
    		style="margin-top: 1%;">
        <div style="float:right;margin-top: -10px;margin-right:73%">
            <input type="text" name="cWhCode" class="form-control" id="cWhCode">
        </div>
        <label style="font-size: 25px; margin-left: 10%;margin-top: -30px">NG仓库</label>
    </div>
	<div class="container-fluid"
		style="margin-top: 1%; border-bottom: 2px solid #5B5B5B;">
		<label style="font-size: 25px; margin-left: 10%;margin-top: -30px">权限设置</label>
	</div>

	<div style="width: 80%; height: 400px;" class="container-fluid">
		<table id="userListTable"></table>
	</div>
</div>
<%@ include file="end.jsp"%>
<!-- 关联设备设备站点列表工具栏 -->
<div id="deviceSiteListToolbar">
	<button onclick="showAddDeviceSitesDialog()" type="button" class="btn btn-default">
		<span class="fa fa-plus" aria-hidden="true"></span>新增
	</button>
	<button id="deleteDeviceSitesBtn" type="button" class="btn btn-default">
		<span class="fa fa-remove" aria-hidden="true"></span>删除
	</button>
</div>
<!-- 权限设置用户列表工具栏 -->
<div id="userListToolbar">
	<button onclick="showAddUsersDialog()" type="button" class="btn btn-default">
		<span class="fa fa-plus" aria-hidden="true"></span>新增
	</button>
	<button id="deleteUsersBtn" type="button" class="btn btn-default">
		<span class="fa fa-remove" aria-hidden="true"></span>删除
	</button>
</div>
<!-- 新增设备站点弹出框 -->
<div class="modal fade" tabindex="-1" role="dialog"
	id="addDeviceSitesDlg">
	<div class="modal-dialog" role="document" style="width: 800px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">设备站点</h4>
			</div>
			<div class="modal-body" style="height: 520px">
				<div>
    </div>
    设备名称:<input id="deviceName"  style="margin:8px;width: 120px"
					name="deviceName">
    设备代码:<input id="deviceCode"  style="margin:8px;width: 120px"
					name="deviceCode">
    生产单元:<select id="productionUnitName" style="margin:8px;width: 150px; display: inline-block;" equipID=""
                           class="selectpicker" data-live-search="true" >
                        </select>
  <input type="button" onclick="reloadDevice()" value="搜索" style="margin-left: 8px">
		<table id="showDeviceSitesListTable"
           data-toggle="table"
           data-url="mcdeviceSite/getAllDeviceSite.do"
           data-pagination="true"
           data-height="415">
        <thead>
       	<tr>
			<th data-field="" data-checkbox="true"></th>
			<th data-field="deviceSiteCode" data-align="center">站点代码</th>
			<th data-field="deviceSiteName" data-align="center">站点名称</th>
			<th data-field="deviceCode" data-align="center">设备代码</th>
			<th data-field="deviceName" data-align="center">设备名称</th>
		</tr>
        </thead>
    </table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" id="addDeviceSitesBtn">确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
<!-- 新增用户弹出框 -->
<div class="modal fade" tabindex="-1" role="dialog"
	id="addUsersDlg">
	<div class="modal-dialog" role="document" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">用户</h4>
			</div>
			<div class="modal-body" style="height: 450px">
				<table data-toggle="table" data-url="mcuser/getAllUser.do" id="showUsersListTable"  data-cache=false	data-height="400">
					<thead>
						<tr>
							<th data-field="" data-checkbox="true"></th>
							<th data-field="username" data-align="center">用户账号</th>
							<th data-field="employeeCode" data-align="center">员工代码</th>
							<th data-field="employeeName" data-align="center">员工姓名</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="modal-footer" style="margin-top: 30px">
				<button type="button" class="btn btn-default" data-dismiss="modal" id="addUsersBtn">确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
<!-- 删除确认框 -->
 <div class="modal fade" id="deleteConfirmDlg">  
  <div class="modal-dialog">  
    <div class="modal-content message_align">  
      <div class="modal-header">  
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>  
        <h4 class="modal-title">提示信息</h4>  
      </div>  
      <div class="modal-body">  
        <p>您确认要删除吗？</p>  
      </div>  
      <div class="modal-footer">  
         <input type="hidden" id="type"/>  
         <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>  
         <a  onclick="deleteData()" class="btn btn-success" data-dismiss="modal">确定</a>  
      </div>  
    </div>  
  </div> 
</div>