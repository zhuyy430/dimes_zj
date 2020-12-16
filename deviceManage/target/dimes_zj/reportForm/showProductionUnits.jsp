<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function(){
	    $("#productionUnitTable").iTreegrid({
            url:'productionUnit/queryTopProductionUnits.do',
            idField:'id',
            treeField:'name',
			singleSelect:true,
			onDblClickRow :function(rowIndex,rowData){
				confirmProductionUnits();
 				},
            columns:[[
                {title:'生产单元编码',field:'code',width:'30%'},
                {field:'name',title:'生产单元名称',width:'70%',align:'left'}
            ]]
		});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'center',title:'',split:true,border:false,width:'100%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
		<table id="productionUnitTable" style="width:100%;height:400px">
		</table>
	</div>
</div>
