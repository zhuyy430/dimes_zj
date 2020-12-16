<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
			var id = $("#productionUnitDg").iTreegrid("getSelected").id;
		$(function(){
			$('#employeeTable').iDatagrid({
                idField:'id',
                pagination:true,
                pageSize:15,
                pageList:[10,15,20],
                pagePosition:'bottom',
                fitColumns:true,
			    url:'person/queryPersonNotInByProductionUnitId.do?productionUnitId='+id,
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'员工编码'},
			        {field:'name',title:'员工名称',width:100},
			        {field:'cDept_Num',title:'部门编号',width:100},
			        {field:'tel',title:'手机号',width:100},
			        {field:'email',title:'邮箱',width:100}
			    ]]
			});
		});
		
		//搜索
		 function reloadEmployee(){
		     $('#employeeTable').iDatagrid("load",{
		    	 code:$("#code").val(),
		    	 productionUnitId:id,
		    	 name:$("#name").val(),
		    	 dept:$("#dept").val()
		     });
		 }
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px; margin-top: 10px;">
		员工编码:<input id="code"
					name="code" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		员工名称:<input id="name"
					name="name" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
       	部门编号:<input id="dept"
					name="dept" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadEmployee()">查询</a>
		   </div>

	</div>
	<div style="padding-bottom:40px;background:#eee;"
		 data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeeTable"></table>
	</div>
</div>