<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<link type="text/css" href="console/js/topjui/css/topjui.timeaxis.css" rel="stylesheet">
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
function downloadFile(){
	 var dg = $("#documentData").iDatagrid('getSelected');
	 if(!dg){
		 $.messager.alert("提示","请选择要下载的文件!");
		 return ;
	 }
	 var id = dg.id;
	 window.location.href="maintenanceRelatedDocument/download.do?id=" + id;
}

function showFile(){
	var dg = $("#documentData").iDatagrid('getSelected');
	 if(!dg){
		 $.messager.alert("提示","请选择要查看的文件!");
		 return ;
	 }
	 var id = dg.id;
	 showFullScreen(dg.url);
}
function confirmNG(){
	var did=$('#pressLightTable').iDatagrid('getSelected');
	console.log(did);
    if(did==null){
        alert('请选择添加的数据')
        return ;
    }
         $.get('ngMaintainRecord/addMaintainProject.do',{
                'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                'deviceProject.id':$('#pressLightTable').iDatagrid('getSelected').id,
                note:$('#note').val()
         },function(data){
                if(data.success){
                    $('#spotCheck').iDatagrid('reload');
                    $('#spotCheckAddDialog').iDialog('close');
          		}else{
                    alert(data.msg);
                 }
          });
}
//时间轴
function timeAxis(id){
	 $.get("deviceRepairOrder/queryWorkflow.do",{businessKey:id},function(data){
		var $div = $("<div  class='topjui-timeaxis-container' style='height:100%;width:100%;' data-toggle='topjui-timeaxis'>");
		 var list = JSON.stringify(data.data);
		//去除key中的单引号
		list = list.replace(/"(\w+)":/g, "$1:");
		var reg = new RegExp("\"","g");//g,表示全部替换。
		var l = list.replace(reg,"'");
		 $div.attr("data-options","title:'设备维修',list:" + l);
		 
		 var $wrapper=$("<div class='wrapper'>");
		 $div.append($wrapper);
		 var $light = "<div class='light'><i></i></div>";
		 $wrapper.append($light);
		 
		 var $topjui_timeaxis_main = $("<div class='topjui-timeaxis-main'>");
		 $wrapper.append($topjui_timeaxis_main);
		 
		 var $title = $("<h1 class='title'>");
		 $topjui_timeaxis_main.append($title);
		 $title.text("设备维修");
		 for(var i = 0;i<data.data.length;i++){
			 var $year = $("<div class='year year" + i +"'>");
			 $topjui_timeaxis_main.append($year);
			 
			 var $yearValue = $("<h2>");
			 $year.append($yearValue);
			 
			 $yearValue.append("<a>" + data.data[i].year + "<i></i></a>");
			 
			 var $list = $("<div class='list' style='height:116px;'>");
			 $year.append($list);
			 for(var j = 0;j<data.data[i].list.length;j++){
				 var obj = data.data[i].list[j];
				 var $ul = $("<ul class='ul"+j+"' style='position:absolute;'>");
				 $list.append($ul);
				 
				 var $li = $("<li class='cls'>");
				 $ul.append($li);
				 
				 var $date = $("<p class='date'>"+obj.date+"</p>");
				 $li.append($date);
				 var $intro = $("<p class='intro'>" + obj.intro + "</p>");
				 $li.append($intro);
				 var $version = $("<p class='version'>&nbsp;" +''  + "</p>");
				 $li.append($version);
				 var $more = $("<div class='more more00'>")
				 $li.append($more);
				 $more.append("<p>"+obj.more[0]?obj.more:'' + "</p>");
			 }
		 }
		var tab5 = $("#tab5"); 
		tab5.html($div);
	}); 
}

 function getPic(id){
	 var url ="deviceRepairOrder/queryDeviceRepairOrderById.do?id="+id;            
     $.ajax({
         type:'POST',
         url:url,
         contentType:false,
         processData:false,
         dataType:'json',
         mimeType:"multipart/form-data",
         success:function(data){
        	 var picDiv = $("#picDiv");
        	 picDiv.empty();
         	if(data.picName){
         		var picName = data.picName
         		  for (var i = 0; i < picName.length; i++) {
         		var picHtml = "<div class='imageDiv' > <img  id='img" + i + "' src='"+contextPath+picName[i]+"' style='height: 240px;width: 370px;float: left;margin: 10px;' /> </div>";
         		 picDiv.append(picHtml);
         		  }
         	}
         }
     });
 }
 
//全屏查看
function showFullScreen(url){
		var docUrl=url;
		//显示文档内容的iframe对象
		var $previewDiv = $("#fullScreenDiv");
		$previewDiv.empty();
		var suffix = '';
		//截取后缀
		if(docUrl!=null && $.trim(docUrl)!=''){
			 var point = docUrl.lastIndexOf("."); 
			 suffix = docUrl.substr(point+1);
		}else{
			alert("没有预览文档!");
			return false;
		}
		var $docIframe=$("<iframe style='width:100%;height:100%;'>");
		switch(suffix){
		case "pdf":
		case "PDF":{
			$docIframe.attr("src",docUrl);
			$previewDiv.append($docIframe);
			break;
		}
		case "png":
		case "PNG":
		case "jpg":
		case "JPG":
		case "JPEG":
		case "jpeg":
		case "gif":
		case "GIF":
		case "bmp":
		case "BMP":{
			var $img = $("<img style='height:100%;width:100%;'>");
			$img.attr("src",docUrl);
			$previewDiv.append($img);
			break;
		}
		}
		$("#dialog-layer").css("display","block");
}
	
 var relatedId="";
 function getrelatedId(rid){
	 relatedId=rid;
 }
 
//搜索保养记录
	function reloadRecord(){
		$("#deviceDg").iDatagrid('load',{
			condition:$("#condition").iTextbox('getValue'),
			employeeName:$("#employeeName").iTextbox('getValue'),
			maintainType:$("#maintainType").iCombobox('getValue'),
			maintainStatus:$("#maintainStatus").iCombobox('getValue'),
			search_from:$("#search_from").val(),
			search_to:$("#search_to").val()
		});
	}
	//重置
	function resetSearch(){
		$("#condition").iTextbox('setValue','');
		$("#employeeName").iTextbox('setValue','');
		$("#maintainType").iCombobox('setValue','');
		$("#maintainStatus").iCombobox('setValue','');
		$("#search_from").iTextbox('setValue','');
		$("#search_to").iTextbox('setValue','');
		reloadRecord();
	}
 
</script>
</head>
<body>
<input type="hidden" id="ids" />
    <div data-toggle="topjui-layout" data-options="fit:true">
        <div
            data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
            <div data-toggle="topjui-layout" data-options="fit:true">
                <div
                    data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                    style="height: 60%;">
                    <!-- datagrid表格 -->
                    <table data-toggle="topjui-datagrid"
                        data-options="id:'deviceDg',
                       url:'deviceRepairOrder/queryDeviceRepairOrder.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       childTab: [{id:'southTabs'}],
                        onSelect:function(index,row){
                                       getPic(row.id);
                                       timeAxis(row.id);
                                       getrelatedId(row.device.id);
                               },
                               onLoadSuccess:function(){
                                       $('#spotCheck').iDatagrid('reload');
                                       $('#lubrication').iDatagrid('reload');
                                       $('#spareparts').iDatagrid('reload');
                                       $('#maintain').iDatagrid('reload');
                                       $('#documentData').iDatagrid('reload');
                               }">
                        <thead>
                           <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                <th data-options="field:'serialNumber',title:'单据编号',width:'150px',align:'center',sortable:false"></th>
                                <th
                                    data-options="field:'createDate',title:'报修时间',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                }"></th>
                                
                                <th data-options="field:'deviceCode',title:'设备代码',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.code;
	                                    }
                                    }"></th>
                                <th data-options="field:'deviceName',title:'设备名称',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.name;
	                                    }
                                    }"></th>
                                <th
                                    data-options="field:'unitType',width:'150px',align:'center',title:'规格型号',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.unitType;
	                                    }
                                    }"></th>
                                <th data-options="field:'projectTypeName',title:'设备类别',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	if(row.device.projectType){
	                                    		return row.device.projectType.name
	                                    	}
	                                    }
                                    }"></th>
                               
                                <th data-options="field:'batchNumber',title:'故障类型',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.ngreason) {
	                                    	return row.ngreason.name;
	                                    }else{return ''}
                                    }"></th>
								<th data-options="field:'ngDescription',title:'故障描述',width:'200px',align:'center',sortable:false"></th>
								
								 <th data-options="field:'maintainName',width:'150px',align:'center',title:'维修人员',sortable:false"></th>
								 
								  <th data-options="field:'status',width:'150px',align:'center',title:'维修状态',sortable:false,formatter:function(value,row,index){
	                                    if (row.status=='WAITINGASSIGN'){//等待派单
									            return '等待派单'; 
									        }else if(row.status=='WAITINCOMFIRM'){//等待接单确认
									            return '等待接单确认';
									        }else if(row.status=='MAINTAINING'){//维修中
									            return '维修中';
									        }else if(row.status=='WORKSHOPCOMFIRM'){//车间确认
									            return '车间确认'; 
									        }else if(row.status=='MAINTAINCOMPLETE'){//维修完成
									            return '维修完成'; 
									        }else if(row.status=='WAITWORKSHOPCOMFIRM'){//待车间确认
									            return '待车间确认'; 
									        }else if(row.status=='FAIL_SAFEOPERATION'){//待车间确认
									            return '带病运行'; 
									        }else{
									            return ''; 
									        }
                                    }"></th>
								
								 <th
                                    data-options="field:'productCount',title:'安装位置',width:'150px',align:'center',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	return row.device.installPosition;
	                                    }
                                    }"></th>
                                    
                                 <th data-options="field:'informant',width:'150px',align:'center',title:'报修人员',sortable:false"></th>
                                 
                                <th data-options="field:'productionUnit',width:'150px',align:'center',title:'生产单元',sortable:false,formatter:function(value,row,index){
	                                    if (row.device) {
	                                    	if(row.device.productionUnit){
	                                    		return row.device.productionUnit.name
	                                    	}
	                                    }
                                    }"></th>
                                 <th data-options="field:'confirmName',width:'150px',align:'center',title:'确认人员',sortable:false"></th>
                                 
                                  <th
                                    data-options="field:'completeDate',title:'确认时间',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                }"></th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div data-options="region:'south',fit:false,split:true,border:false"
                    style="height: 40%">
                    <div data-toggle="topjui-tabs"
                        data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     singleSelect:true,
                     parentGrid:{
                         type:'datagrid',
                         id:'deviceDg',
                         param:'deviceRepairOrderId:id'
                     }">
                       <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic">
							<!-- datagrid表格 -->
							<div class="picDiv" id="picDiv">
      					  </div>
						</div>
						<div title="维修人员" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'spareparts',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                               parentGrid:{
		                         type:'datagrid',
		                         id:'deviceDg',
		                         param:'deviceRepairOrderId:id'
		                     },
						       url:'maintenanceStaffRecord/queryMaintenanceStaffRecord.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',width:'80px',hidden:true"></th>
										<th
											data-options="field:'code',title:'员工代码',width:'180px',align:'center',sortable:false"></th>
										<th
											data-options="field:'name',title:'员工姓名',width:'180px',align:'center',sortable:false"></th>
										<th
											data-options="field:'receiveType',title:'接单类型',width:'180px',align:'center',sortable:false,formatter:function(value,row,index){
			                        	if(value=='SYSTEMGASSIGN'){
			                        		return '系统派单';
			                        	}else if(value=='ARTIFICIALGASSIGN'){
			                        		return '人工派单';
			                        	}else if(value=='ROBLIST'){
			                        		return '抢单';
			                        	}else if(value=='REWORK'){
			                        		return '返修';
			                        	}else{
			                        		return '协助';
			                        	}
			                        	}"></th>
										<th
											data-options="field:'assignTime',title:'派单时间',width:'180px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                }"></th>
										<th
											data-options="field:'receiveTime',title:'接单时间',width:'180px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                }"></th>
										<th
											data-options="field:'completeTime',title:'完成时间',width:'180px',align:'center', formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
                                        return dateStr;
                                    }else{
                                        return '';
                                    }
                                }"></th>
                                <th
											data-options="field:'maintenanceTime',title:'维修用时(分)',width:'180px',align:'center',sortable:false"></th>
											<th
											data-options="field:'occupyTime',title:'占用工时(分)',width:'180px',align:'center',sortable:false"></th>
									</tr>
								</thead>
							</table>
						</div>
                        <div title="故障原因" data-options="id:'tab1',iconCls:'fa fa-th'">
                            <!-- datagrid表格 -->
                            <table data-toggle="topjui-datagrid"
                                data-options="id:'spotCheck',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                               parentGrid:{
		                         type:'datagrid',
		                         id:'deviceDg',
		                         param:'deviceRepairOrderId:id'
		                     },
                               url:'ngMaintainRecord/queryNGMaintainRecordVO.do'">
                                <thead>
                                    <tr>
                                        <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                        <th data-options="field:'pressLightTypeCode',title:'故障类型代码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.projectType){
					                        		if(row.projectType.parent){
					                        		return row.projectType.parent.code; 
				                        		}else{
				                        			return '';
				                        		}
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                        <th data-options="field:'pressLightTypeName',title:'故障类别',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.projectType){
					                        		if(row.projectType.parent){
					                        		return row.projectType.parent.name;
				                        		}else{
				                        			return '';
				                        		}
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                        <th data-options="field:'pressLightCode',title:'类型代码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.projectType){
				                        		return row.projectType.code; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                        <th data-options="field:'pressLightName',title:'故障类型',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.projectType){
				                        		return row.projectType.name; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                        <th data-options="field:'note',title:'说明',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.note){
				                        		return row.note; 
				                        	}else{
				                        		return row.projectType.note;
				                        	}
			                        	}"></th>
                                        <th data-options="field:'processingMethod',title:'处理方法',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.processingMethod){
				                        		return row.processingMethod; 
				                        	}else{
				                        		return row.projectType.method;
				                        	}
			                        	}"></th>
                                        <th data-options="field:'remark',title:'备注',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.remark){
				                        		return row.remark; 
				                        	}else{
				                        		return row.projectType.remark;
				                        	}
			                        	}"></th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                        <div title="维修项目" data-options="id:'tab3',iconCls:'fa fa-th'">
                            <table data-toggle="topjui-datagrid"
                                    data-options="id:'lubrication',
                                   initCreate: false,
                                   fitColumns:true,
                                   singleSelect:true,
                                   parentGrid:{
		                         type:'datagrid',
		                         id:'deviceDg',
		                         param:'deviceRepairOrderId:id'
		                     },
                                   url:'maintainProject/queryMaintainProject.do'">
                                    <thead>
                                        <tr>
                                            <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                            <th data-options="field:'typeCode',title:'类别代码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.type){
				                        		return row.type.code; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'typeName',title:'类别名称',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.type){
				                        		return row.type.name; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'code',title:'项目代码',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'name',title:'项目名称',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'note',title:'说明',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'processingMethod',title:'方法',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'remark',title:'备注',width:50,sortable:false,width:'200px',align:'center'"></th>
                                        </tr>
                                    </thead>
                            </table>
                        </div>
                        <div title="备件信息" data-options="id:'tab4',iconCls:'fa fa-th'">
                            <table data-toggle="topjui-datagrid"
                                    data-options="id:'maintain',
                                   initCreate: false,
                                   fitColumns:true,
                                   singleSelect:true,
                                   parentGrid:{
		                         type:'datagrid',
		                         id:'deviceDg',
		                         param:'deviceRepairOrderId:id'
		                     },
                                   url:'sparepartRecord/querySparepartRecord.do'">
                                    <thead>
                                        <tr>
                                            <th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                                            <th data-options="field:'typeCode',title:'类别代码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
					                        	if(row.sparepart.projectType){
					                        		return row.sparepart.projectType.code; 
					                        	}else{
					                        		return '';
					                        	}
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'typeName',title:'备件类别',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
					                        	if(row.sparepart.projectType){
					                        		return row.sparepart.projectType.name; 
					                        	}else{
					                        		return '';
					                        	}
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'sparepartCode',title:'备件代码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.code; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'sparepartName',title:'备件名称',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.name; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'unitType',title:'规格型号',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.unitType; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
			                        	<th data-options="field:'quantity',title:'数量',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'note',title:'备注',width:50,sortable:false,width:'200px',align:'center'"></th>
                                            <th data-options="field:'mnemonicCode',title:'助记码',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.mnemonicCode; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'batchNumber',title:'批号',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.batchNumber; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'graphNumber',title:'图号',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.graphNumber; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'manufacturer',title:'厂商',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.manufacturer; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'measurementUnit',title:'计量单位',width:50,sortable:false,width:'200px',align:'center',formatter:function(value,row,index){
				                        	if(row.sparepart){
				                        		return row.sparepart.measurementUnit; 
				                        	}else{
				                        		return '';
				                        	}
			                        	}"></th>
                                            <th data-options="field:'createDate',title:'耗用日期',width:50,sortable:false,width:'200px',align:'center', formatter:function(value,row,index){
	                                    if (value) {
	                                        var date = new Date(value);
	                                        var month = date.getMonth()+1;
	                                        var monthStr = ((month>=10)?month:('0' + month));
	                                        
	                                        var day = date.getDate();
	                                        var dayStr = ((day>=10)?day:('0'+day));
	                                        
	                                        var hour = date.getHours();
	                                        var hourStr = ((hour>=10)?hour:('0' + hour));
	                                        
	                                        var minute = date.getMinutes();
	                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
	                                        
	                                        var second = date.getSeconds();
	                                        var secondStr = ((second>=10)?second:('0' +second));
	                                        
	                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr+ ' ' +hourStr+':'+minuteStr+':'+secondStr ;
	                                        return dateStr;
	                                    }else{
	                                        return '';
	                                    }
                                    }"></th>
                                        </tr>
                                    </thead>
                            </table>
                        </div>
                        <div title="流程记录" data-options="iconCls:'fa fa-th'" id="tab5">
                    	</div>
                    	<div title="相关文档" data-options="id:'tab6',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'maintenanceRelatedDocument/queryMaintenanceRelatedDocumentByRecordId.do'">
								<thead>
									<tr>
										<th
											data-options="field:'id',title:'id',checkbox:false,hidden:true,width:80"></th>
										<th
											data-options="field:'contentType',title:'文件类型',width:120,align:'center'"></th>
										<th
											data-options="field:'name',title:'文件名称',width:120,align:'center'"></th>
										<th
											data-options="field:'srcName',title:'源文件名称',width:120,align:'center'"></th>
										<th
											data-options="field:'fileSize',title:'大小(KB)',width:120,align:'center',formatter:function(value,row,index){
												if(value){
													return (value/1024).toFixed(2);
												}else{
													return '';
												}
											}"></th>
										<th
											data-options="field:'url',title:'存储路径',width:120,align:'center'"></th>
										<th
											data-options="field:'uploadDate',title:'上传时间',width:120,align:'center',formatter:function(value,row,index){
											if(value){
												return getDateTime(new Date(value));
											}else{
												return '';
											}
										}"></th>
										<th
											data-options="field:'note',title:'说明',width:120,align:'center'"></th>
									</tr>
								</thead>
							</table>
						</div>
                </div> 
            </div>
        </div>
    </div>
</div>
 
    <!-- 设备维修记录表格工具栏开始 -->
	<div id="deviceDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'deviceDg'
       }">
<sec:authorize access="hasAuthority('COMPLETED_MAINTENANCESTAFFRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-square-o',
       url:'deviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=WAITWORKSHOPCOMFIRM',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'}">维修完成</a>
</sec:authorize>
<sec:authorize access="hasAuthority('CONFIRM_MAINTENANCESTAFFRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-check',
       url:'deviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=MAINTAINCOMPLETE',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'}">车间确认</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REWORK_MAINTENANCESTAFFRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-reply',
       url:'deviceRepairOrder/updateDeviceRepairForConfirmAndReword.do?status=REWORK',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'deviceDg',param:'id:id'}">返修</a>
</sec:authorize>
<sec:authorize access="hasAuthority('CHANGEORDER_MAINTENANCESTAFFRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#deviceDg-toolbar',
       iconCls:'fa fa-exchange',
       parentGrid:{
       	type:'datagrid',
       	id:'deviceDg',
       	param:'parentDeviceId:id'
       },
       dialog:{
           id:'deviceRepairTransferDialog',
           width: 900,
           height: 400,
           href:'console/jsp/deviceManagement/deviceRepairOrder_transfer.jsp',
           url:'deviceRepairOrder/queryDeviceRepairOrderById.do?id={id}',
           buttons:[
           	{text:'保存',handler:function(){
           			 var status = ($('#deviceDg').iDatagrid('getSelected').status);
	                   if(status=='WAITINGASSIGN'||status=='WAITWORKSHOPCOMFIRM'||status=='MAINTAINCOMPLETE'){
	                      alert('该记录不可转单，请重新选择！')
	           				$('#deviceRepairTransferDialog').iDialog('close');
	                      return;
	                   }
           			$.get('TransferRecordController/addTransferRecordRecord.do',{
          			'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
          			reason:$('#reason').val(),
          			transferCode:$('#maintainCode').val(),
          			transferName:$('#maintainName').val()
           			},function(data){
           				if(data.success){
	           				$('#deviceRepairTransferDialog').iDialog('close');
           					alert(data.msg);
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceRepairTransferDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">转单</a>
</sec:authorize>
       <div style="margin-right:20px;"> 过滤条件:<input type="text" id="condition" name="condition"
      	data-toggle="topjui-textbox" data-options="prompt:'设备代码、名称、规格型号',width:200"/> 
      	<label style="text-align: center;">报修时间:</label>
			<input type="text" name="search_from" data-toggle="topjui-datebox"
				id="search_from" style="width:150px;">
			TO <input type="text" name="search_to" data-toggle="topjui-datebox"
				id="search_to" style="width:150px;">
      	维修人员:<input id="employeeName" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="employeeName"
					data-options="width:200,valueField:'name',textField:'name',url:'maintenanceStaff/queryMaintenanceStaffAll.do'">
      	维修状态:<input id="maintainStatus" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="maintainStatus"
					data-options="width:200,valueField:'value',textField:'text',
					data:[
					{text:'待派单',value:'WAITINGASSIGN'},
					{text:'待接单',value:'WAITINCOMFIRM'},
					{text:'维修中',value:'MAINTAINING'},
					{text:'待车间确认',value:'WAITWORKSHOPCOMFIRM'},
					{text:'维修完成',value:'MAINTAINCOMPLETE'},
					{text:'带病运行',value:'FAIL_SAFEOPERATION'}
					]">
		故障类型:<input id="maintainType" name="maintainType" 
      	data-toggle="topjui-combobox" data-options="width:200,valueField:'code',textField:'name',url:'deviceProject/queryDevicesProjectByType.do?type=BREAKDOWNREASON'"/>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadRecord()">搜索</a>
      	<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-ban'" 
				onclick="resetSearch()">重置</a>
      	</div>
	</div>
	<!-- 设备维修记录表格工具栏结束 -->
	<!-- 维修人员表格工具栏开始 -->
	<div id="spareparts-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'spareparts'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFFRECORD_MAINTENANCESTAFF')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#spareparts-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'deviceDg',
       	param:'parentDeviceId:id'
       },
       dialog:{
           id:'parameterAddDialog',
           width: 600,
           height: 400,
           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			$.get('maintenanceStaffRecord/addMaintenanceStaffRecord.do',{
          			'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
          			name:$('#name').val(),
          			code:$('#code').val(),
          			assignTime:$('#date').val(),
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#spareparts').iDatagrid('reload');
	           				$('#deviceDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFFRECORD_MAINTENANCESTAFF')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#spareparts-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                	id:'parameterEditDialog',
                	    width: 600,
                		height: 400,
		           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_edit.jsp',
		           url:'maintenanceStaffRecord/queryMaintainProjectById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			var deviceRepairIid = $('#deviceDg').iDatagrid('getSelected');
	           		if(deviceRepairIid==null){
	           			alert('请选中报修单!');
	           			return ;
	           		}
           			$.get('maintenanceStaffRecord/updateMaintenanceStaffRecord.do',{
          			id:$('#spareparts').iDatagrid('getSelected').id,
           			'deviceRepair.id':deviceRepairIid.id,
          			name:$('#name').val(),
          			code:$('#code').val(),
          			assignTime:$('#date').val(),
          			occupyTime:$('#occupyTime').val(),
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#spareparts').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ACCEPT_MAINTENANCESTAFFRECORD_MAINTENANCESTAFF')">
            <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#spareparts-toolbar',
       iconCls:'fa fa-chain',
       url:'deviceRepairOrder/updateDeviceRepairOrderStatusById.do?status=MAINTAINING',
       grid: {uncheckedMsg:'请先勾选要操作的数据',param:'id:id'},
       reload:[
        {type:'datagrid',id:'deviceDg'}
    	]">接单</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFFRECORD_MAINTENANCESTAFF')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#spareparts-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceStaffRecord/deleteMaintenanceStaffRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'id:id'}">删除</a>
</sec:authorize>
	</div>
	<!-- 维修人员表格工具栏结束 -->
    <!-- 故障原因表格工具栏开始 -->
    <div id="spotCheck-toolbar" class="topjui-toolbar"
        data-options="grid:{
           type:'datagrid',
           id:'spotCheck'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFFRECORD_NGMAINTAIN')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#spotCheck-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'spotCheckAddDialog',
           width: 600,
           height: 500,
           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_NGMaintainRecord_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
               var did=$('#pressLightTable').iDatagrid('getSelected');
               if(did==null){
	               alert('请选择添加的数据')
	               return ;
               }
                    $.get('ngMaintainRecord/addMaintainProject.do',{
                           'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                           'deviceProject.id':$('#pressLightTable').iDatagrid('getSelected').id,
                           note:$('#note').val()
                    },function(data){
                           if(data.success){
                               $('#spotCheck').iDatagrid('reload');
                               $('#spotCheckAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#spotCheckAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFFRECORD_NGMAINTAIN')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method: 'openDialog',
            extend: '#spotCheck-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'spotCheckEditDialog',
                width: 600,
                   height: 450,
                href:'console/jsp/deviceManagement/MaintenanceStaffRecord_NGMaintainRecord_edit.jsp',
                url:'ngMaintainRecord/queryNGMaintainRecordById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
                      $.get('ngMaintainRecord/updateNGMaintainRecord.do',{
                      		id:$('#spotCheck').iDatagrid('getSelected').id,
                           'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                           'deviceProject.id':$('#id').val(),
                           note:$('#note').val(),
                           processingMethod:$('#processingMethod').val(),
                           remark:$('#remark').val()
                    },function(data){
                           if(data.success){
                               $('#spotCheckEditDialog').iDialog('close');
                               $('#spotCheck').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#spotCheckEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFFRECORD_NGMAINTAIN')">
         <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#spotCheck-toolbar',
       iconCls:'fa fa-trash',
       url:'ngMaintainRecord/deleteNGMaintainRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'spotCheck',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
    <!-- 故障原因表格工具栏结束 -->
    <!-- 维修项目表格工具栏开始 -->
    <div id="lubrication-toolbar" class="topjui-toolbar"
        data-options="grid:{
           type:'datagrid',
           id:'lubrication'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFFRECORD_MAINTAINPROJECT')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#lubrication-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'lubricationAddDialog',
           width: 600,
           height: 600,
           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_MaintainProject_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
                    $.get('maintainProject/addMaintainProject.do',{
                           'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                           code:$('#projectCode').val(),
                           name:$('#projectName').val(),
                           'type.id':$('#typeId').val(),
                           note:$('#note').val(),
                           processingMethod:$('#processingMethod').val(),
                           remark:$('#remark').val()
                    },function(data){
                           if(data.success){
                               $('#lubrication').iDatagrid('reload');
                               $('#lubricationAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#lubricationAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFFRECORD_MAINTAINPROJECT')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method: 'openDialog',
            extend: '#lubrication-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'lubricationEditDialog',
                width: 600,
                 height: 600,
                href:'console/jsp/deviceManagement/MaintenanceStaffRecord_MaintainProject_edit.jsp',
                url:'maintainProject/queryMaintainProjectById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
                       $.get('maintainProject/updateMaintainProject.do',{
                       		id:$('#lubrication').iDatagrid('getSelected').id,
                           'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                           code:$('#projectCode').val(),
                           name:$('#projectName').val(),
                           'type.id':$('#typeId').val(),
                           note:$('#note').val(),
                           processingMethod:$('#processingMethod').val(),
                           remark:$('#remark').val()
                       },function(data){
                           if(data.success){
                               $('#lubricationEditDialog').iDialog('close');
                               $('#lubrication').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#lubricationEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFFRECORD_MAINTAINPROJECT')">
         <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#lubrication-toolbar',
       iconCls:'fa fa-trash',
       url:'maintainProject/deleteMaintainProjectById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'lubrication',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
    <!-- 维修项目表格工具栏结束 -->
    <!-- 备件信息表格工具栏开始 -->
    <div id="maintain-toolbar" class="topjui-toolbar"
        data-options="grid:{
           type:'datagrid',
           id:'maintain'
       }">
<sec:authorize access="hasAuthority('ADD_MAINTENANCESTAFFRECORD_SPAREPART')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#maintain-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'parentDeviceId:id'
       },
       dialog:{
           id:'maintainAddDialog',
           width: 800,
           height: 800,
           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_SparepartRecord_add.jsp',
           buttons:[
               {text:'保存',handler:function(){
	               var ids = $('#sparepartTab').iDatagrid('getSelections');
	           		var idsArray = new Array();
	           		if(ids.length<=0){
	           			alert('请选择要添加的数据!');
	           			return ;
	           		}else{
	           			for(var i = 0;i < ids.length;i++){
	           				idsArray.push(ids[i].id);
	           			}
	           		}
                    $.get('sparepartRecord/addSparepartRecord.do',{
                           deviceRepairId:$('#deviceDg').iDatagrid('getSelected').id,
                           sparepartIds:JSON.stringify(idsArray)
                    },function(data){
                           if(data.success){
                               $('#maintain').iDatagrid('reload');
                               $('#maintainAddDialog').iDialog('close');
                     		}else{
                               alert(data.msg);
                            }
                     });
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#maintainAddDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MAINTENANCESTAFFRECORD_SPAREPART')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method: 'openDialog',
            extend: '#maintain-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
                id:'maintainEditDialog',
                width: 600,
                height: 400,
                href:'console/jsp/deviceManagement/MaintenanceStaffRecord_SparepartRecord_edit.jsp',
                url:'sparepartRecord/queryaddSparepartRecordById.do?id={id}',
                buttons:[
               {text:'保存',handler:function(){
		               var quantity = $('#quantity').val();
		           			if(quantity<=0){
		           				alert('备件数量不得少于1');
		           				return ;
		           			}
                       $.get('sparepartRecord/updateSparepartRecord.do',{
                           id:$('#maintain').iDatagrid('getSelected').id,
                           'deviceRepair.id':$('#deviceDg').iDatagrid('getSelected').id,
                           'sparepart.id':$('#sparepartId').val(),
                           quantity:quantity,
                           note:$('#note').val(),
                           createDate:$('#date').val()
                       },function(data){
                           if(data.success){
                               $('#maintainEditDialog').iDialog('close');
                               $('#maintain').iDatagrid('reload');
                           }else{
                               alert(data.msg);
                           }
                       });
               },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#maintainEditDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFFRECORD_SPAREPART')">
         <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#maintain-toolbar',
       iconCls:'fa fa-trash',
       url:'sparepartRecord/deleteSparepartRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'maintain',param:'id:id'}">删除</a>
</sec:authorize>
    </div>
    <!-- 备件信息表格工具栏结束 -->
   <!-- 流程记录表格工具栏开始 -->
	<!-- 流程记录表格工具栏结束 -->
	<!-- 相关文档表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
<sec:authorize access="hasAuthority('UPLOAD_MAINTENANCESTAFFRECORD_DOC')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="method:'openDialog',
       extend: '#maintain-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'deviceDg',
           param:'relatedId:id'
       },
       dialog:{
           id:'uploadDocDialog',
           title:'上传',
           width: 600,
           height: 400,
           href:'console/jsp/deviceManagement/MaintenanceStaffRecord_doc_upload.jsp',
           buttons:[
               {text:'上传',handler:function(){
               var file = $('#file').val();
               if(!file){
               	$.iMessager.alert('提示','请选择要上传的文件!');
					  return false;
               }
               
               	 $.ajaxFileUpload({
                url: 'maintenanceRelatedDocument/upload.do', 
                type: 'post',
                data: {
                		name:$('#name').iTextbox('getValue'), 
                		note:$('#note').iTextbox('getValue'),
                		'maintenanceStaffRecord.id':$('#deviceDg').iDatagrid('getSelected').id
                	  },
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'file', //文件上传域的ID
                dataType: 'text', //返回值类型 一般设置为json
                success: function (data, status) {
                var obj = JSON.parse(data);
                	if(!obj.success){
                		 $.iMessager.alert('提示',obj.message);
                		 return false;
                	}
                	 $('#uploadDocDialog').iDialog('close');
					 $('#documentData').iDatagrid('reload');
                },
                error:function(error){
                	alert(error);
                }
            }
        );
                },iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
               {text:'关闭',handler:function(){
                   $('#uploadDocDialog').iDialog('close');
               },iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">上传</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MAINTENANCESTAFFRECORD_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#documentData-toolbar',
       iconCls:'fa fa-trash',
       url:'maintenanceRelatedDocument/deleteMaintenanceRelatedDocument.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'documentData',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DOWNLOAD_MAINTENANCESTAFFRECORD_DOC')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_MAINTENANCESTAFFRECORD_DOC')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls:'fa fa-search'" onclick="showFile()">查看</a>
</sec:authorize>
	</div>
	<div id="dialog-layer">
		<div style="font-size:50px;color:red;z-index:2000;position:fixed;top:0px;right:100px;cursor:pointer;" 
		onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
		 onmouseout="$(this).css('font-size','50px')" >×</div>
		<div id="fullScreenDiv">
		
		</div>
	</div>
</body>
</html>
<style>
#dialog-layer{
  position: fixed;
  height:100%;
  width:100%;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  z-index: 1000;
  display:none;
}

#fullScreenDiv{
  margin:auto auto;
  height:80%;
  width:80%;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}
</style>