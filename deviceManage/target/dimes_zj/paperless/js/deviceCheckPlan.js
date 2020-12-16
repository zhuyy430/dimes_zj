var _docs ;
var index = 0;
$(function(){
    //加载当前产线下的设备
    loadDevices4ProductionUnit();
    //loadDevices4ProductionUnitId();
    //左右按钮点击事件
    $("#showPrevious").click(function(){
        if(index<=0){
            $.iMessager.alert("提示","已是第一张!");
        }else{
            $("#deviceCheckImg").attr("src",_docs[--index].url);
        }
    });
    //左右按钮点击事件
    $("#showNext").click(function(){
        if(index>=_docs.length-1){
            $.iMessager.alert("提示","已是最后一张!");
            return ;
        }else{
            $("#deviceCheckImg").attr("src",_docs[++index].url);
        }
    });
});
//保存点检条码
function saveCheckPlanRecordItem(){
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
    if(resultObj && resultObj.length>0){
        for(var i = 0;i<resultObj.length;i++){
            results += $(resultObj[i]).val() + ",";
        }
        results = results.substring(0,results.length-1);
    }
    //获取点检备注结果数组
    var noteObj=$(".noteValue");
    var notes = "";
    if(noteObj && noteObj.length>0){
        for(var i = 0;i<noteObj.length;i++){
            notes+=$(noteObj[i]).val()+",@";
        }
        notes =notes.substring(0,notes.length-2);
    }
    //发送异步请求
    $.get("paperlessCheckPlan/deviceCheck.do",{
        id:$("#id").val(),
        checkedDate:$("#checkDate").val(),
        employeeName:$("#checkUsername").val(),
        employeeCode:$("#checkUsercode").val(),
        picPath:urls,
        itemIds:ids,
        results:results,
        notes:notes
    },function(result){
        $.iMessager.alert("提示","已点检");
        $('#deviceCheckDialog').modal('hide');
        //模拟当前选中的设备点击事件
        if(_pressedDevice!=null){
            _pressedDevice.click();
        }else{
            //模拟设备点检菜单点击事件
            $("#deviceCheckDiv").click();
        }
    });
}
//向页面填充点检记录信息
function setCheckingPlanRecord(record){
    $("#id").val(record.id);
    $("#check_deviceCode").val(record.deviceCode);
    $("#check_deviceName").val(record.deviceName);
    $("#check_unitType").val(record.unitType);
    if(!record.checkedDate){
    	$("#checkDate").val(getDateTime(new Date()));
    }else{
    	$("#checkDate").val(getDateTime(new Date(record.checkedDate)));
    }
}
 
//填充文档信息(设备点检图片)
function setDocs(docs){
    //文档索引
    var index = 0;
    //显示图片的img对象
     var img = $("#deviceCheckImg");
    if(docs!=null && docs.length>0){
        //默认显示第0张图片
        img.attr("src",docs[index].url);
    }
}
///显示点检项目标准信息
//flag 0:查看  1：点检
function setProjectRecords(projectRecords,flag){
    var tbody = $("#checkingPlanRecordItem");
    tbody.empty();
    if(projectRecords && projectRecords.length>0){
        for(var i = 0;i<projectRecords.length;i++){
            var record = projectRecords[i];
            var tr = $("<tr>");
            
            var $num=$("<td>");
            $num.append(i+1);
            tr.append($num);
           
            //项目代码
			var code = $("<td style='width:100px;'>");
			code.append(record.code);
			tr.append(code);
            
            //点检项目名称和id列
            var nameAndId = $("<td>"); 
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
            
            //结果列
            var result = $("<td>");
            var combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;background-color:#1C2437;'><option selected='selected' class='optionStyle'>OK</option><option class='optionStyle'>NG</option></select>");
            if(record.result){
                if(record.result=='NG'){
                    combobox = $("<select class='resultValue' style='width:100px;margin:3px auto;background-color:#1C2437;'><option class='optionStyle'>OK</option><option selected='selected' class='optionStyle'>NG</option></select>");;
                }
            }
            if(flag==1){
                result.append(combobox);
            }else{
                result.append(record.result);
            }
            tr.append(result);
            
            //备注列
            var note =$("<td style='width:100px;'>");
            var recordNote=" ";
            if(record.note!=null){
                recordNote=record.note;
            }
            note.append("<input type='text' class='noteValue' value='" + recordNote + "' style='background-color: #1C2437;color:white;border:0;' />");
            tr.append(note);
            
            tbody.append(tr);
        }
    }
}
//加载生产单元下的所有设备
function loadDevices4ProductionUnit(){
    $.get("paperlessCheckPlan/loadDevices4ProductionUnit.do?module=deviceManage",function(result){
        var $devicesDiv = $("#devicesDiv");
        var $paperlessdevicesDiv = $("#paperlessdevicesDiv");
        $devicesDiv.empty();
        $paperlessdevicesDiv.empty();
        if(result!=null && result.length>0){
            for(var i = 0;i<result.length;i++){
                var device = result[i];
                var $deviceDiv = $('<div class="device_button" style="margin-bottom:10px;cursor:pointer;" value="'+device.code+'" onClick="changeColor(this)">');
                $deviceDiv.append(device.name);
                
                $devicesDiv.append($deviceDiv);
                $paperlessdevicesDiv.append($deviceDiv);
            }
        }
    });
}
/*//加载生产单元下的所有设备
function loadDevices4ProductionUnitId(){
    $.get("paperlessCheckPlan/loadDevices4ProductionUnitId.do?module=deviceManage&productionUnitId="+$("#paperProductionUnitId").val(),function(result){
        var $devicesDiv = $("#paperlessdevicesDiv");
        $devicesDiv.empty();
        if(result!=null && result.length>0){
            for(var i = 0;i<result.length;i++){
                var device = result[i];
                var $deviceDiv = $('<div class="device_button" style="margin-bottom:10px;cursor:pointer;" value="'+device.code+'" onClick="changeColor(this)">');
                $deviceDiv.append(device.name);
                
                $devicesDiv.append($deviceDiv);
            }
        }
    });
}*/
/**
 * 显示点检详情
 * @param id
 * @param flag 0:查看 1：点检
 * @returns
 */
function showCheckingDetail(id,flag){
    $.get("paperlessCheckPlan/queryCheckingPlanRecordById.do",{id:id},function(result){
        //设备点检记录对象
        var record = result.record;
        //点检人对象
         var checkUsername = result.checkUsername;
        var checkUsercode = result.checkUsercode;
        $("#checkUsername").val(checkUsername);
        $("#checkUsercode").val(checkUsercode);
        //关联文档集合
        var docs = result.docs;
        //点检项目标准记录
        var projectRecords = result.projectRecords;
        setCheckingPlanRecord(record);
        _docs = docs;
        setDocs(docs);
        setProjectRecords(projectRecords,flag); 
    });
    if(flag==0){
        $("#saveBtn").css("display",'none');
        $("#cancelBtn").text("关闭");
    }else{
        $("#saveBtn").css("display",'inline');
        $("#cancelBtn").text("取消");
    }
    $("#deviceCheckDialog").modal("show");
}
 
 
 
//保存被按的按钮对象
var _pressedDevice = null;
//点击按钮改变按钮颜色，并且保存设备编码
function changeColor(obj){
    $(".device_button").removeClass('device_button_pressed');
    $(".device_button").addClass('device_button_nomal');
    
    var pressedDevice = $(obj);
    _pressedDevice = pressedDevice;
    pressedDevice.removeClass('device_button_normal');
    pressedDevice.addClass('device_button_pressed');
    //保存设备编码
    $("#deviceCode").val(obj.getAttribute("value"));
    //刷新iframe
    var $iframe = $("#content");
    var src = $iframe.attr("src");
    switch(src){
    //点检记录页面
    case "paperless/deviceCheckPlanRecord.jsp":{
        $iframe.attr("src","paperless/deviceCheckPlanRecord.jsp");
        break;
    }
    case "paperless/deviceRepair.jsp":{
        $iframe.attr("src","paperless/deviceRepair.jsp");
        break;
    }
    case "paperless/deviceMessage.jsp":{
        $iframe.attr("src","paperless/deviceMessage.jsp");
        break;
    }
    case "paperless/deviceService.jsp":{
        $iframe.attr("src","paperless/deviceService.jsp");
        break;
    }
    case "paperless/deviceMaintain.jsp":{
        $iframe.attr("src","paperless/deviceMaintain.jsp");
        break;
    }
    case "paperless/deviceCheckPlanRecord.jsp?param=dm":{
		$iframe.attr("src","paperless/deviceCheckPlanRecord.jsp?param=dm");
		break;
	}
	case "paperless/deviceRepair.jsp?param=dm":{
		$iframe.attr("src","paperless/deviceRepair.jsp?param=dm");
		break;
	}
	case "paperless/deviceMessage.jsp?param=dm":{
		$iframe.attr("src","paperless/deviceMessage.jsp?param=dm");
		break;
	}
	case "paperless/deviceService.jsp?param=dm":{
		$iframe.attr("src","paperless/deviceService.jsp?param=dm");
		break;
	}
	case "paperless/deviceMaintain.jsp?param=dm":{
		$iframe.attr("src","paperless/deviceMaintain.jsp?param=dm");
		break;
	}
    }
}