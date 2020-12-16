<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
var numberList=[];

$(function(){
	$('#processesTable').iDatagrid({
		idField:'id',
	    url:'processes/queryOtherProcesses.do',
	    columns:[[
	        {field:'id',title:'id',checkbox:true},
	        {field:'code',title:'代码',width:100},
	        {field:'name',title:'名称',width:100},
	        {field:'processTypeName',title:'工序类别',width:100,formatter:function(value,row,index){
	        	if(row.processType){
	        		return row.processType.name;
	        	}else{
	        		return '';
	        	}
	        }},
	        {field:'note',title:'备注',width:100},
	        {field:'number',title:'顺序',width:100}
	    ]],
	    queryParams:{
	    	InventoryCode:$('#departmentDg').iDatagrid("getSelected").cInvCode
	    },
	    onLoadSuccess:function(data){
	    	/* for(var i=0;i<data.rows.length;i++){ */
	    		 for (var j=0;j<numberList.length;j++){
	    			/* if(data.rows[i].id==numberList[j].id){
	    				console.log(i);
	    				checkRow(i);
		    		}  */  
		    		var index=-1;
		    			index=$('#processesTable').iDatagrid('getRowIndex',numberList[j]);
		    		if(index!=-1){
		    			$('#processesTable').iDatagrid('checkRow',index);
		    			$('#processesTable').iDatagrid('updateRow',{
	    		    	    index: index,
	    		    	    row: {
	    		    	        number:(j+1)
	    		    	    }
	    		    	});
		    		}
	    		} 
	    		 console.log(); 
	    	/* } */ 
	    },
	    onCheck: function(index, row) {
	    	console.log(numberList);
	    	var isornot=true;
	    	for (var i=0;i<numberList.length;i++){
	    		if (numberList[i]==row.id){
	    			isornot=false;
	    		}
	    	}
	    	if(isornot){
	    		numberList.push(row.id);
		    	$('#processesTable').iDatagrid('updateRow',{
		    	    index: index,
		    	    row: {
		    	        id: row.id,
		    	        code: row.code,
		    	        name:row.name,
		    	        processTypeName:row.processTypeName,
		    	        note:row.note,
		    	        number:numberList.length
		    	    }
		    	});
	    	}
	    	
	    },
	    onUncheck:function(index, row) { 
	    	for (var i=0;i<numberList.length;i++){
	    		if (numberList[i]==row.id){
	    			numberList.splice(i,1);
	    			$('#processesTable').iDatagrid('updateRow',{
    		    	    index: index,
    		    	    row: {
    		    	        number:''
    		    	    }
    		    	}); 
	    		}
	    	}
	    	var checklist=$('#processesTable').iDatagrid('getChecked');
	    	for(var i=0;i<checklist.length;i++){
	    		for(var j=0;j<numberList.length;j++){
	    			if(numberList[j]==checklist[i].id){
	    				var index=$('#processesTable').iDatagrid('getRowIndex',numberList[j]);
	    				if(index!=-1){
	    					$('#processesTable').iDatagrid('updateRow',{
		    		    	    index: index,
		    		    	    row: {
		    		    	        number:(j+1)
		    		    	    }
		    		    	});
	    				}
	    			}
	    		}
	    		
	    	}
	    }
	});
}); 
		
		
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="processesTable"></table>
	</div>
</div>