<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<style type="text/css">
.imageDiv {
	display:inline-block;
	width:370px;
	height:240px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px
}
.cover {
	position:absolute;
	z-index:1;
	top:0;
	left:0;
	width:370px;
	height:240px;
	background-color:rgba(0,0,0,.3);
	display:none;
	line-height:125px;
	text-align:center;
	cursor:pointer;
}
.cover .delbtn {
	color:red;
	font-size:20px;
}
.imageDiv:hover .cover {
	display:block;
}
.addImages {
	display:inline-block;
	width:370px;
	height:240px;
	-webkit-box-sizing:border-box;
	-moz-box-sizing:border-box;
	box-sizing:border-box;
	border:1px dashed darkgray;
	background:#f8f8f8;
	position:relative;
	overflow:hidden;
	margin:10px;
}
.text-detail {
	margin-top:80px;
	text-align:center;
}
.text-detail span {
	font-size:40px;
}
.file {
	position:absolute;
	top:0;
	left:0;
	width:370px;
	height:240px;
	opacity:0;
}
.input {
}

</style>
<script>
var imageVal = 0; //选择数据Id
var idList="";    //存储图片路径
var imageList=[0,1,2,3];

/*删除功能*/
	function delImg(obj) {
        var _this = $(obj);
        var id=_this.siblings(".input").val();
        var photoIds=idList.split(",");
       	photoIds.splice(id,1,"");
        imageList.splice(id,1,id)
        var plist="";
        for(var i = 0; i < photoIds.length; i++){
        		if(i==0){
        			plist=photoIds[i];
        	}else{
        		plist+=","+photoIds[i];
        	}
        }
       idList=plist;
		$("#ids").val(photoIds)
        _this.parents(".imageDiv").remove();
		$(".addImages").show();
    };

$(function() {

var userAgent = navigator.userAgent; //用于判断浏览器类型
idList=$("#ids").val();

		$(".file").change(function() {
		    //获取选择图片的对象
		    var docObj = $(this)[0];
		    var picDiv = $(this).parents(".picDiv");
		    //得到所有的图片文件
		    var fileList = docObj.files;
		    //循环遍历
		    for (var i = 0; i < fileList.length; i++) {
			for(var j = 0; j < imageList.length; j++){
				if(imageList[j]!==""){
					imageVal=imageList[j]
					break;
				}
			}
 		    //动态添加html元素onclick='deleteImg(\""+ imageVal+ "\")
		        var picHtml = "<div class='imageDiv' style='margin-left: 15px'> <img  id='img" + fileList[i].name + "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="+imageVal+" /></div></div>";
		        
		        imageList.splice(imageVal,1,"");
		        picDiv.prepend(picHtml);
		        //获取图片imgi的对象
		        var imgObjPreview = document.getElementById("img" + fileList[i].name);
		        
		        var formData = new FormData();
		        formData.append('file',fileList[i]);
		        var url ="relatedDoc/uploadimg.do";            
		        $.ajax({
		            type:'POST',
		            url:url,
		            data:formData,
		            contentType:false,
		            processData:false,
		            dataType:'json',
		            mimeType:"multipart/form-data",
		            success:function(data){
		            	if(!idList){
		            		idList=data.filePath;
		            		$("#ids").val(idList)
		            	}else{
		            		var photoIds=idList.split(",");
		    		       	photoIds.splice(imageVal,1,data.filePath);
		            		idList=photoIds.join(',');
		            		$("#ids").val(idList)
		            	}
		            }
		        });
		        if (fileList && fileList[i]) {
		            //图片属性
		            imgObjPreview.style.display = 'block';
		            imgObjPreview.style.width = '370px';
		            imgObjPreview.style.height = '240px';
		            //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要以下方式
		            if (userAgent.indexOf('MSIE') == -1) {
		                //IE以外浏览器
		                imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]); //获取上传图片文件的物理路径;
		            } else {
		                //IE浏览器
		                if (docObj.value.indexOf(",") != -1) {
		                    var srcArr = docObj.value.split(",");
		                    imgObjPreview.src = srcArr[i];
		                } else {
		                    imgObjPreview.src = docObj.value;
		                }
		            }
		        }
		       for(var j = 0; j < imageList.length; j++){
				if(imageList[j]!==""){
	 		        	$(".addImages").show();
					break;
				}
	 		    $(".addImages").hide();
			}
		    }
		})
})


$('#ofsoNo').iTextbox({
	onChange : function(newValue, oldValue) {
		if($("#ofsoNo").val()!=""){
			$.get(contextPath + "deviceRepairOrder/queryofsoNoBySerialNumber.do",{
				serialNumber:$("#ofsoNo").val()
			},function(data){
				if(data.success){
					$("#deviceCode").textbox("setValue", data.deviceRepair.device.code);
					$("#deviceName").textbox("setValue", data.deviceRepair.device.name);
					$("#unitType").textbox("setValue", data.deviceRepair.device.unitType);
					$("#deviceId").val(data.deviceRepair.device.id);
					if(data.deviceRepair.device.projectType){
						$("#projectType").textbox("setValue",data.deviceRepair.device.projectType.name);
						$("#projectTypeId").val(data.deviceRepair.device.projectType.id);
					}else{
						$("#projectType").textbox("setValue",'');
						$("#projectTypeId").val('');
					}
					if(data.deviceRepair.device.productionUnit){
						$("#productionUnitName").textbox("setValue",data.deviceRepair.device.productionUnit.name);
						$("#productionUnitId").val(data.deviceRepair.device.productionUnit.id);
					}
					$("#installPosition").textbox("setValue", data.deviceRepair.device.installPosition);
					$("#pressLight").textbox("setValue", data.deviceRepair.ngreason.name);
					$("#pressLightId").val(data.deviceRepair.ngreason.id);
				}else{
					alert(data.msg);
				}
			});
		}
		
	}
})
	$(function() {
		//初始化生产时间
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		var dateStr = year + "-" + (month>9?month:("0"+month)) + "-" + (day>9?day:("0"+day)) + " " + 
		(hours>9?hours:("0"+hours)) + ":" + (minutes>9?minutes:("0"+minutes)) + ":" + (seconds>9?seconds:("0"+seconds));
		$("#createDate").val(dateStr);
				$("#pressLight").iTextbox({
				    width:200,
				    buttonIcon:'fa fa-search',
				    onClickButton:function(){
				    	$('#showPressLightsDialog').dialog("open");
				    }
				});
				$('#showPressLightsDialog').dialog({
				    title: '故障原因',
				    width: 700,
				    height: 600,
				    closed: true,
				    cache: false,
				    href: 'console/jsp/deviceManagement/deviceRepairOrder_showPressLight.jsp',
				    modal: true,
				    buttons:[{
				        text:'保存',
				        handler:function(){
				         	var pressLight = $('#pressLightTable').iDatagrid('getSelected');
				         	confirmPressLight(pressLight);
				        }
				    },{
				        text:'关闭',
				        handler:function(){
				        	$('#showPressLightsDialog').dialog("close");
				        }
				    }]
				});
		//设备列表
		$('#deviceCode').iCombogrid(
				{
					idField : 'code',
					textField : 'name',
					delay : 500,
					mode : 'remote',
					url : 'device/queryAllDevices.do?module=deviceManage',
					columns : [ [ {
						field : 'id',
						title : 'id',
						width : 60,
						hidden : true
					}, {
						field : 'code',
						title : '代码',
						width : 100
					}, {
						field : 'name',
						title : '设备名称',
						width : 100
					} ] ],
					onClickRow : function(index, row) {
						$("#deviceName").textbox("setValue", row.name);
						$("#unitType").textbox("setValue", row.unitType);
						$("#deviceId").val(row.id);
						if(row.projectType){
							$("#projectType").textbox("setValue",row.projectType.name);
							$("#projectTypeId").val(row.projectType.id);
						}else{
							$("#projectType").textbox("setValue",'');
							$("#projectTypeId").val('');
						}
						if(row.productionUnit){
							$("#productionUnitName").textbox("setValue",row.productionUnit.name);
							$("#productionUnitId").val(row.productionUnit.id);
						}
						$("#installPosition").textbox("setValue", row.installPosition);
					}
				});
		//流水号
		$.get(contextPath + "deviceRepairOrder/queryDeviceRepairOrderSerialNumber.do",{
		},function(data){
			$("#serialNumber").textbox("setValue", data.serialNumber);
		});
	});
	//故障原因确定
	function confirmPressLight(pressLight){
		if(!pressLight){
     		$('#showPressLightsDialog').dialog("close");
     		return ;
     	}
   		$('#pressLight').iTextbox('setValue',pressLight.code);
   		$('#pressLight').iTextbox('setText',pressLight.name);
   		$('#pressLightId').val(pressLight.id);
		$('#showPressLightsDialog').dialog("close");
	}
	function querydevice(id){
		$('#deviceCode').iCombogrid(
				{
					idField : 'code',
					textField : 'name',
					delay : 500,
					mode : 'remote',
					url : 'device/queryAllDevicesByProductionUnitId.do?module=deviceManage&productionUnitId='+id,
					columns : [ [ {
						field : 'id',
						title : 'id',
						width : 60,
						hidden : true
					}, {
						field : 'code',
						title : '代码',
						width : 100
					}, {
						field : 'name',
						title : '设备名称',
						width : 100
					} ] ],
					onClickRow : function(index, row) {
						$("#deviceName").textbox("setValue", row.name);
						$("#unitType").textbox("setValue", row.unitType);
						$("#deviceId").val(row.id);
						$("#productionUnitId").val(id);
						if(row.projectType){
							$("#projectType").textbox("setValue",row.projectType.name);
							$("#projectTypeId").val(row.projectType.id);
						}
						$("#installPosition").textbox("setValue", row.installPosition);
					}
				});
	}
</script>
<div id="showPressLightsDialog">
	</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post">
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div
						style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;">
						设备报修单</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">单据编号</label>
							<div class="topjui-input-block">
								<input type="text" name="serialNumber" data-toggle="topjui-textbox"
									data-options="required:true" id="serialNumber" readonly="readonly">
							</div>
						</div>
						
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报修时间</label>
							<div class="topjui-input-block">
								<input type="text" name="createDate" style="width: 100%;"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true"
									id="createDate">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报修人员</label>
							<div class="topjui-input-block">
								<input type="text" name="Informant"
									data-toggle="topjui-textbox" data-options="required:true"
									id="Informant" value='<sec:authentication property="principal.username"/>' readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
									<input data-toggle="topjui-combotreegrid" data-options="width:'100%',required:true,
							        panelWidth:500,
							        url:'',
							        idField:'id',
							        treeField:'name',
							        url:'productionUnit/queryTopProductionUnits.do',
							        columns:[[
							            {field:'id',title:'id',hidden:true},
							            {field:'code',title:'编码',width:200},
							            {field:'name',title:'单元名称',width:200}
							        ]],
							        onSelect: function(rec){
            querydevice(rec.id);
             $('#projectTypeId').val(rec.id);
        }"  name="productionUnitName" id="productionUnitName">
								<input type="hidden" name="productionUnitId"
									id="productionUnitId"> 
								<input type="hidden" name="productionUnitCode"
									id="productionUnitCode"> 
							</div>
					</div>
					</div>
					<div class="topjui-row">
					
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceCode"
									data-toggle="topjui-textbox" data-options="required:true,"
									id="deviceCode"/>
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceName" data-toggle="topjui-textbox"
									data-options="required:false" id="deviceName" readonly="readonly">
									<input type="text" hidden="hidden" name="deviceId"
									id="deviceId">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType"
									data-toggle="topjui-textbox" data-options="required:false"
									id="unitType" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备类别</label>
							<div class="topjui-input-block">
								<input type="text" name="projectType"
									data-toggle="topjui-textbox" data-options="required:false"
									id="projectType" readonly="readonly">
								<input type="text" hidden="hidden" name="projectTypeId"
									id="projectTypeId">
							</div>
						</div>
					</div>
						<div class="topjui-row">
							<div class="topjui-col-sm3">
								<label class="topjui-form-label">安装位置</label>
								<div class="topjui-input-block">
									<input type="text" name="installPosition"
										data-toggle="topjui-textbox" data-options="required:false"
										id="installPosition" readonly="readonly">
								</div>
							</div>
							<div class="topjui-col-sm3">
								<label class="topjui-form-label">故障原因</label>
								<div class="topjui-input-block">
									<input type="text" name="pressLight"
										data-toggle="topjui-textbox" data-options="required:true"
										id="pressLight" >
										<input type="text" hidden="hidden" name="pressLightId"
									id="pressLightId">
								</div>
							</div>
							<div class="topjui-col-sm3">
								<label class="topjui-form-label">原带病维修单</label>
								<div class="topjui-input-block">
									<input type="text" name="ofsoNo"
										data-toggle="topjui-textbox" data-options="required:false"
										id="ofsoNo" onChange="ofsoNoChange()" >
								</div>
							</div>
						</div>
					<div class="topjui-col-sm12">
							<label class="topjui-form-label">故障描述</label>
							<div class="topjui-input-block">
									<input type="text" name="ngDescription"
								data-toggle="topjui-textarea" data-options="required:true" id="ngDescription">
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>


	<div data-options="region:'south',fit:false,split:true,border:false"
		style="height: 50%">
		<div data-toggle="topjui-tabs"
			data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
			<div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'">
			<form method="post" action="relatedDoc/uploadimg.do" id="passForm" enctype="multipart/form-data;charset=utf-8" multipart="">
    
    <div id="Pic_pass">
        <!-- <p><span style="color: red">注：每张照片大写不可超过4M，且最多可以传十张</span></p> -->
        <div class="picDiv" style="margin-top: 20px">
            <div class="addImages">
                
                <input type="file" class="file" id="fileInput" accept="image/png, image/jpeg, image/gif, image/jpg">
                <div class="text-detail">
                    <span>+</span>
                    <p>点击上传</p>
                </div>
                <input id="ids" hidden="true"/>
            </div>
        </div>
    </div>
</form>
			</div>
		</div>
	</div>
</div>
<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>
<!-- 工具按钮 -->
<div id="workSheetDetail-toolbar" class="topjui-toolbar"
	data-options="grid:{
           type:'datagrid',
           id:'workSheetDetail'
       }">
	<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		data-options="method:'doAjax',
       extend: '#workSheetDetail-toolbar',
       iconCls:'fa fa-trash',
       url:'workSheet/deleteWorkSheetDetailInMemory.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'workSheetDetail',param:'id:id'}">删除</a>
</div>