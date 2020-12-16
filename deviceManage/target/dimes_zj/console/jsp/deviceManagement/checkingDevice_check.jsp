<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
<script type="text/javascript">
var _docs ;
var index = 0;
$(function(){
	$.get("checkingPlanRecord/queryCheckingPlanRecordById.do",{id:<%=request.getParameter("id")%>},function(result){
		//设备点检记录对象
		var record = result.record;
		//点检人对象
	 	var checkUsername = result.checkUsername;
		var checkUsercode = result.checkUsercode;
		$("#checkUsername").iTextbox("setValue",checkUsername);
		$("#checkUsercode").val(checkUsercode);
		//关联文档集合
		var docs = result.docs;
		//点检项目标准记录
		var projectRecords = result.projectRecords;
		setCheckingPlanRecord(record);
		_docs = docs;
		setDocs(docs);
		setProjectRecords(projectRecords); 
	});
});
//向页面填充点检记录信息
function setCheckingPlanRecord(record){
	$("#id").val(record.id);
	$("#deviceCode").iTextbox("setValue",record.deviceCode);
	$("#deviceName").iTextbox("setValue",record.deviceName);
	$("#unitType").iTextbox("setValue",record.unitType);
	if(!record.checkedDate){
		$("#checkDate").iDatetimebox("setValue",getDateTime(new Date()));
	}else{
		$("#checkDate").iDatetimebox("setValue",getDateTime(new Date(record.checkedDate)));
	}
}
//填充文档信息(设备点检图片)
function setDocs(docs){
	 //文档索引
    var index = 0;
    //显示图片的img对象
     var $previewDiv = $("#deviceCheckImg");
    var url="";
    if(docs!=null && docs.length>0){
        //默认显示第0张图片
        url=docs[index].url;
    }
    $previewDiv.empty();
    var suffix = '';

    //截取后缀
    if(url!=null && $.trim(url)!=''){
        var point = url.lastIndexOf(".");
        suffix = url.substr(point+1);
    }else{
        return false;
    }
    var $docIframe=$("<iframe style='width:100%;height:100%;'>");
    switch(suffix){
        case "pdf":
        case "PDF":{
            $docIframe.attr("src",url);
            $previewDiv.append($docIframe);
            break;
        }
        <%-- case "doc":
        case "docx":
        case "DOC":
        case "DOCX":
        case "xls":
        case "xlsx":
        case "XLS":
        case "XLSX":
        case "ppt":
        case "pptx":
        case "PPT":
        case "PPTX":{
            $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
            $previewDiv.append($docIframe);
            break;
        } --%>
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
            var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
            $img.attr("src",url);
            $previewDiv.append($img);
            break;
        }
        default:{
            var $p = $("<p style='font-size:20px;color:red;'>");
            $p.append(suffix + "不支持预览!");
            $previewDiv.append($p);
            return false;
        }
    }

    //docUrl = url;
}
//根据点检项目的id查找该项目条目对应的文件
function queryFiles(checkingPlanRecordItemId){
	$.get("checkingPlanRecord/queryCheckingPlanRecordItemFilesByItemId.do",{itemId:checkingPlanRecordItemId},function (result) {
		if(result&&result.length>0){
		    var imgsDiv = $("#imgsDiv");
		    imgsDiv.empty();
		    for(var i = 0;i<result.length;i++){
		        var img = result[i];
				var $img = $("<img style='height:120px;margin-left:10px;cursor:pointer;' onclick='window.open(this.src);'/>");
				$img.attr("src",img.url);
				imgsDiv.append($img);
			}
		}
    });
}
var projectRecordsList;
//显示点检项目标准信息
function setProjectRecords(projectRecords){
	projectRecordsList = projectRecords;
	var tbody = $("#checkingPlanRecordItem");
	tbody.empty();
	if(projectRecords && projectRecords.length>0){
		for(var i = 0;i<projectRecords.length;i++){
			var record = projectRecords[i];
			var tr = $("<tr style='cursor:pointer;' onclick='queryFiles(+"+record.id+")'>");
			//序号
			var serNo = $("<td>");
			serNo.append(i+1);
			tr.append(serNo);
			//项目代码
			var code = $("<td>");
			code.append(record.code);
			tr.append(code);
			//点检项目名称和id列
			var nameAndId = $("<td style='width:100px;'>"); 
			nameAndId.append("<input type='hidden' class='ids' value='" + record.id + "' />");
			nameAndId.append(record.name);
			
			tr.append(nameAndId);
			//标准列
			var standard = $("<td style='width:100px;'>");
			standard.append(record.standard);
			tr.append(standard);
			//方法列
			var method = $("<td style='width:100px;'>");
			method.append(record.method);
			tr.append(method);
			
			// 频次列
			var frequency = $("<td style='width:100px;'>");
			frequency.append(record.frequency);
			tr.append(frequency);
			//上限值列
			var standard = $("<td style='width:80px;'>");
			standard.append(record.upperLimit);
			tr.append(standard);
			//下限值列
			var standard = $("<td style='width:80px;'>");
			standard.append(record.lowerLimit);
			tr.append(standard);
			//点检限值列
			var value =$("<td style='width:100px;'>");
			var recordvalue="0";
			if(!record.checkValue){
				recordvalue=record.checkValue;
			}
			value.append("<input type='number' class='checkValue' value='" + parseInt(record.checkValue) + "' style='width:100px;' onblur='showResult(this,"+i+")'/> ");
			tr.append(value);
			tbody.append(tr);
			//结果列
			var result = $("<td>");
			var combobox = $("<select class='resultValue' id='"+i+"' style='width:100px;margin:3px auto;'><option selected='selected'>OK</option><option>NG</option></select>");
			if(record.result){
				if(record.result=='NG'){
					combobox = $("<select class='resultValue' id='"+i+"' style='width:100px;margin:3px auto;'><option>OK</option><option selected='selected'>NG</option></select>");;
				}
			}
			result.append(combobox);
			tr.append(result);
			
			//备注列
			var note =$("<td>");
			var recordNote=" ";
			if(record.note!=null){
				recordNote=record.note;
			}
			note.append("<input type='text' class='noteValue' value='" + recordNote + "' />");
			tr.append(note);
			tbody.append(tr);
		}
	}
}


function showResult(obj,id){
	//id即为数组索引
	var projectRecord = projectRecordsList[id];
	//点检值
	var value = parseInt(obj.value);
	if(projectRecord.lowerLimit && projectRecord.upperLimit){
		if(value>=projectRecord.lowerLimit && value<=projectRecord.upperLimit){
			$('#'+id).val("OK");
		}else{
			$('#'+id).val("NG");
		}
	}
	return false;
}
function save(param){
	//获取所有图片的url
	var imgs = $("#docs img");
	//存储图片url
	var urls = "";
	if(imgs && imgs.length>0){
		for(var i = 0;i<imgs.length;i++){
			urls+=$(imgs[i]).attr("src") + ",";
		}
		urls = urls.substring(0,urls.length-1);
	}
	//获取点检项的id数组，以逗号间隔
	var idsObj = $(".ids");
	var ids = "";
	if(idsObj && idsObj.length>0){
		for(var i = 0;i<idsObj.length;i++){
			ids+=$(idsObj[i]).attr("value")+",";
		}
		ids = ids.substring(0,ids.length-1);
	}
	//获取点检项的结果数组
	var resultObj = $(".resultValue");
	var results = "";
	//是否存在NG记录
	var tf=false;
	
	if(resultObj && resultObj.length>0){
		for(var i = 0;i<resultObj.length;i++){
			results += $(resultObj[i]).val() + ",";
			if($(resultObj[i]).val()=="NG"){
				tf=true;
			}
		}
		results = results.substring(0,results.length-1);
	}
	
	var noteObj=$(".noteValue");
	var notes = "";
	if(noteObj && noteObj.length>0){
		for(var i = 0;i<noteObj.length;i++){
			notes+=$(noteObj[i]).val()+" @";
		}
		notes =notes.substring(0,notes.length-1);
	} 
	var checkValueObj=$(".checkValue");
	var checkValue = "";
	if(checkValueObj && checkValueObj.length>0){
		for(var i = 0;i<checkValueObj.length;i++){
			checkValue+=$(checkValueObj[i]).val()+" ,";
		}
		checkValue =checkValue.substring(0,checkValue.length-1);
	} 
	if(param=='finish'){
		//发送异步请求
		if(tf){
			if(window.confirm('有点检NG项，是否创建设备报修单?')){
	            //alert("确定");
				$.get("checkingPlanRecord/deviceCheck.do",{
					id:$("#id").val(),
					checkedDate:$("#checkDate").iDatetimebox("getValue"),
					employeeName:$("#checkUsername").iTextbox("getValue"),
					employeeCode:$("#checkUsercode").val(),
					picPath:urls,
					itemIds:ids,
					results:results,
					checkValue:checkValue,
					notes:notes
				});
				var deviceCode=$("#deviceCode").val();
				var params = {title:"新增/编辑",href:"console/jsp/deviceManagement/checkToAddDeviceRepairOrder_add.jsp?code="+deviceCode}; 
				addParentTab(params);
	         }else{
	            alert("修复完成后才可以提交点检完成");
	            return false;
	        }
		}else{
			$.get("checkingPlanRecord/deviceCheck.do",{
				id:$("#id").val(),
				checkedDate:$("#checkDate").iDatetimebox("getValue"),
				employeeName:$("#checkUsername").iTextbox("getValue"),
				employeeCode:$("#checkUsercode").val(),
				picPath:urls,
				itemIds:ids,
				results:results,
				checkValue:checkValue,
				notes:notes
			});
		}
		
	}else if(param=='save'){
		//发送异步请求
		$.get("checkingPlanRecord/deviceCheck.do",{
			id:$("#id").val(),
			employeeName:$("#checkUsername").iTextbox("getValue"),
			employeeCode:$("#checkUsercode").val(),
			picPath:urls,
			itemIds:ids,
			results:results,
			checkValue:checkValue,
			notes:notes
		},function(result){
			alert("已保存");
		});
	}
}

//全屏查看
function showFullScreen(){
    //显示文档内容的iframe对象
    var $previewDiv = $("#fullScreenDiv");
    $previewDiv.empty();
    var suffix = '';
    //截取后缀
	var url=_docs[index].url
    if(url!=null && $.trim(url)!=''){
        var point = url.lastIndexOf(".");
        suffix = url.substr(point+1);
    }else{
        alert("没有预览文档!");
        return false;
    }
    var $docIframe=$("<iframe style='width:100%;height:100%;'>");
    switch(suffix){
        case "pdf":
        case "PDF":{
            $docIframe.attr("src",url);
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
            $img.attr("src",url);
            $previewDiv.append($img);
            break;
        }
    }
    $("#dialog-layer").css("display","block");
}
</script>
<style>
#checkingPlanRecordItemTb tr td{
	text-align:center;
	font-size:14px;
}
label {
	text-align: center;
}
input[type='radio'] {
	margin-left: 5px;
	margin-top: 5px;
}
[for] {
	font-size: 14px;
	margin-left: 10px;
}
.cycleType {
	margin-top: 10px;
}
/**显示点检图片方向键样式*/
.imgDirection{
	height:100%;width:5%;float:left;cursor:pointer;color:#80848E;
	font-size:30px;
	text-align:center;
	margin-top:300px;
}
#dialog-layer{
	position: fixed;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	overflow: hidden;
	z-index: 1000;
	display:none;
}
</style>
<script>
	$(function(){
		 //左右按钮点击事件
	    $("#showPrevious").click(function(){
	        if(index<=0){
	            $.iMessager.alert("提示","已是第一张!");
	        }else{
                var $previewDiv = $("#deviceCheckImg");
                var url="";
                if(_docs!=null && _docs.length>0){
                    //默认显示第0张图片
                    url=_docs[--index].url;
                }
                $previewDiv.empty();
                var suffix = '';

                //截取后缀
                if(url!=null && $.trim(url)!=''){
                    var point = url.lastIndexOf(".");
                    suffix = url.substr(point+1);
                }else{
                    return false;
                }
                var $docIframe=$("<iframe style='width:100%;height:100%;'>");
                switch(suffix){
                    case "pdf":
                    case "PDF":{
                        $docIframe.attr("src",url);
                        $previewDiv.append($docIframe);
                        break;
                    }
                    <%-- case "doc":
                    case "docx":
                    case "DOC":
                    case "DOCX":
                    case "xls":
                    case "xlsx":
                    case "XLS":
                    case "XLSX":
                    case "ppt":
                    case "pptx":
                    case "PPT":
                    case "PPTX":{
                        $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                        $previewDiv.append($docIframe);
                        break;
                    } --%>
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
                        var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                        $img.attr("src",url);
                        $previewDiv.append($img);
                        break;
                    }
                    default:{
                        var $p = $("<p style='font-size:20px;color:red;'>");
                        $p.append(suffix + "不支持预览!");
                        $previewDiv.append($p);
                        return false;
                    }
                }
	            //$("#deviceCheckImg").attr("src",_docs[--index].url);
	        }

	    });
	    //左右按钮点击事件
	    $("#showNext").click(function(){
	        if(index>=_docs.length-1){
	            $.iMessager.alert("提示","已是最后一张!");
	            return ;
	        }else{
                var $previewDiv = $("#deviceCheckImg");
                var url="";
                if(_docs!=null && _docs.length>0){
                    //默认显示第0张图片
                    url=_docs[++index].url;
                }
                $previewDiv.empty();
                var suffix = '';

                //截取后缀
                if(url!=null && $.trim(url)!=''){
                    var point = url.lastIndexOf(".");
                    suffix = url.substr(point+1);
                }else{
                    return false;
                }
                var $docIframe=$("<iframe style='width:100%;height:100%;'>");
                switch(suffix){
                    case "pdf":
                    case "PDF":{
                        $docIframe.attr("src",url);
                        $previewDiv.append($docIframe);
                        break;
                    }
                    <%-- case "doc":
                    case "docx":
                    case "DOC":
                    case "DOCX":
                    case "xls":
                    case "xlsx":
                    case "XLS":
                    case "XLSX":
                    case "ppt":
                    case "pptx":
                    case "PPT":
                    case "PPTX":{
                        $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                        $previewDiv.append($docIframe);
                        break;
                    } --%>
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
                        var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                        $img.attr("src",url);
                        $previewDiv.append($img);
                        break;
                    }
                    default:{
                        var $p = $("<p style='font-size:20px;color:red;'>");
                        $p.append(suffix + "不支持预览!");
                        $previewDiv.append($p);
                        return false;
                    }
                }
	            //$("#deviceCheckImg").attr("src",_docs[++index].url);
	        }
	    });
	    
	    $('#deviceRepairOrderAddDialog').dialog({
		    title: '新增/编辑',
		    width: 1600,
		    height: 800,
		    closed: true,
		    cache: false,
		    href: 'console/jsp/deviceManagement/deviceRepairOrder_add.jsp?id='+"",
		    modal: true,
		    buttons:[{
		        text:'保存',
		        handler:function(){
		        	var code = $('#code').val();
                    $.get('deviceRepairOrder/addDeviceRepairOrder.do',{
                    serialNumber:$('#serialNumber').val(),
                    createDate:$('#createDate').val(),
                    'device.id':$('#deviceId').val(),
                    'projectType.id':$('#deviceTypeId').val(),
                    Informant:$('#Informant').val(),
                    'productionUnit.id':$('#productionUnitId').val(),
                    'ngreason.id':$('#pressLightId').val(),
                    ngDescription:$('#ngDescription').val(),
                    idList:$('#ids').val(),
                    originalFailSafeOperationNo:$('#ofsoNo').val()
                    },function(data){
                        if(data.success){
                            $('#deviceRepairOrderAddDialog').iDialog('close');
                        }else{
                            alert(data.msg);
                        }
                    });
		        }
		        
		    },{
		        text:'关闭',
		        handler:function(){
		        	$('#deviceRepairOrderAddDialog').dialog("close");
		        }
		    }]
		});
	});
</script>
<div id="deviceRepairOrderAddDialog"></div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="deviceCheckForm">
			<div title="设备点检" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="text-align: center;">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceCode" data-toggle="topjui-textbox"
									data-options="required:false" id="deviceCode" readonly="readonly">
									<!-- 点检记录id -->
									<input type="hidden" name="id" id="id" />
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="text-align: center;">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceName" data-toggle="topjui-textbox"
									data-options="required:false" readonly="readonly" id="deviceName">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="text-align: center;">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
									data-options="required:false" readonly="readonly" id="unitType">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="text-align: center;">点检日期</label>
							<div class="topjui-input-block">
								<input type="text" name="checkDate" data-toggle="topjui-datetimebox"
									data-options="required:false" id="checkDate">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="text-align: center;">点检人员</label>
							<div class="topjui-input-block">
								<input type="text" name="checkUsername" data-toggle="topjui-textbox"
									data-options="required:false" readonly="readonly" id="checkUsername">
								<input type="hidden" id="checkUsercode" name="checkUsercode" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm5">
							<div id="docs" style="border-style: dotted; border-radius: 15px;height:500px;overflow: hidden;
										border-color: #4F5157;">
											<span style="cursor:pointer;float: right;display: inline-block;margin-right: 10px;"
												  class="fa fa-window-maximize" onClick="showFullScreen()"></span>
											<div class="fa fa-chevron-left imgDirection" id="showPrevious"></div>
											<div style="height:100%;width:90%;float:left;" id="deviceCheckImg">
												<%--<img  alt="设备点检图片" style="width:98%;height:94%;margin-top:20px;"/>--%>
											</div>
											<div class="fa fa-chevron-right imgDirection" id="showNext"></div>

							</div>
						</div>
						<div class="topjui-col-sm7">
							<div id="items"  style="border-style: dotted; border-radius: 10px;height:500px;
							margin-left:20px;overflow: auto;">
								<table id="checkingPlanRecordItemTb" border="1" style="width:98%;margin:5px auto;">
									<thead>
										<tr>
											<td>序号</td>
											<td  style='width:80px;'>项目代码</td>
											<td>点检项目</td>
											<td>标准</td>
											<td>方法</td>
											<td>频次</td>
											<td>上限值</td>
											<td>下限值</td>
											<td>点检值</td>
											<td>结果</td>
											<td>备注</td>
										</tr>
									</thead>
									<tbody id="checkingPlanRecordItem"></tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm5">
							<div style="height:30px;">
							</div>
						</div>
						<div class="topjui-col-sm7">
							<div  style="height:30px;color:red;font-size:20px;">
								在设备运行和进行设备清洁时有任何异常情况马上报告给主管或维护人员 
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<div  style="height:120px;border-style: dotted; border-radius: 15px;border-color: #4F5157;" id="imgsDiv">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="float:right;margin-right:100px;">
				<button class="layui-btn" onclick="save('save')">保&nbsp;&nbsp;&nbsp;&nbsp;存</button>
				<button class="layui-btn" onclick="save('finish')">点检完成</button>
			</div>
		</form>
	</div>
</div>

<!-- 全屏预览 -->
<div id="dialog-layer">
	<div style="font-size:50px;color:#A60000;z-index:2000;position:fixed;top:30px;right:20px;cursor:pointer;"
		 onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
		 onmouseout="$(this).css('font-size','50px')" >×</div>
	<div style="height:100%;width:100%;" id="fullScreenDiv">

	</div>
</div>
