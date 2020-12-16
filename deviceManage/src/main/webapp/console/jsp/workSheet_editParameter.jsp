<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$(function(){
	
	var row = $('#workSheetDetail').iEdatagrid("getSelected");
		// 可编辑工单 详情
	 	$('#workSheetDetailParameter').iEdatagrid({
		    url: 'workSheet/queryParameterRecordsByWorkSheetDetailId.do',
		    queryParams:{
		    	workSheetDetailId:row.id
		    },
		    updateUrl: 'workSheet/updateWorkSheetDetailParameterRecord.do',
		    onError:function(index,row){
		    	if(row.jqXHR.status==200){
		    		$('#workSheetDetail').iEdatagrid('reload');
		    	}
		    }
		}); 
		 if(row.parameterSource=='固定值'){
			 $('#workSheetDetailParameter').iEdatagrid("disableEditing");
		}
});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table title="" id="workSheetDetailParameter">
			<thead>
				<tr>
					<th 
						data-options="field:'id',title:'id',checkbox:false,hidden:true,width:200,align:'center'"></th>
					<th 
						data-options="field:'parameterCode',title:'参数代码',checkbox:false,width:100,align:'center'"></th>
					<th 
						data-options="field:'parameterName',title:'参数名称',align:'center'"></th>
					<th class="editable" editor="{type:'numberbox',options:{min:0,precision:4}}"
						data-options="field:'upLine',title:'控制线UL',width:100,align:'center'"></th>
					<th class="editable" editor="{type:'numberbox',options:{min:0,precision:4}}"
						data-options="field:'lowLine',title:'控制线LL',width:100,align:'center'"></th>
					<th class="editable" editor="{type:'numberbox',options:{min:0,precision:4}}"
						data-options="field:'standardValue',title:'标准值',width:100,align:'center'"></th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<!-- 工具按钮 -->
	<div id="workSheetDetailParameter-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'workSheetDetailParameter'
       }">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#workSheetDetailParameter-toolbar',
       iconCls:'fa fa-trash'" onclick="$('#workSheetDetailParameter').iEdatagrid('cancelRow')">取消编辑</a>
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#workSheetDetailParameter-toolbar',
       iconCls:'fa fa-plus'" onclick="$('#workSheetDetailParameter').iEdatagrid('saveRow');">保存</a>
	</div>
