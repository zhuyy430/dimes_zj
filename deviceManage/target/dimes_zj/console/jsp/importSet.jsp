<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript">
$(function(){
	$("#basic").combobox('textbox').css("font-size", "18px");
})
function downloadFile(){
	 var name = $("#basic").val();
	 console.log(name)
	 if(name==""){
		 alert("请选择基础资料")
	 }else{
	 	window.location.href="relatedDoc/downloadTemplates.do?name="+name;
	 }
}
	 
	 function uploadFile(){
		 var name = $("#basic").val();
		 var url="";
		 if(!document.getElementById("file1").files[0]){
			 alert("请选择文件");
			 return ;
		 }
		 if(name==""){
			 alert("请选择基础资料");
			 return ;
		 }else if(name=="设备模版"){
			 url="device/uploadTemplate.do";
		 }else if(name=="工件工序参数信息模版"){
			 url="device/uploadWorkpieceProcessParameters.do";
		 }
		 $("#upload").attr("disabled","disabled");
             var formData = new FormData();
             formData.append("file", document.getElementById("file1").files[0]);   
             $.ajax({
                 url: url,
                 type: "POST",
                 data: formData,
                 contentType: false,
                 processData: false,
                 success: function (data) {
                     $("#upload").removeAttr("disabled");
                     if (data.success) {
                         alert("导入成功！");
                     }else{
                         alert(data.msg);
                     }
                 }
             });
}
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<div class="topjui-row" style="margin-top: 50px;margin-left: 20%">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="font-size:20px;margin-top: 5px">基础资料</label>
							<div class="topjui-input-block" style="margin-left: 5%">
								<input id="basic" style="width: 30%;" data-toggle="topjui-combobox" data-options="
									valueField: 'label',
									textField: 'value',
									data: [{
									    label: '设备模版',
									    value: '设备信息'
									},{label:'工件工序参数信息模版',value:'工件工序参数信息'}]">
<sec:authorize access="hasAuthority('BASICDATAEXPORT')">
							<input type="button" style="margin-left: 20px;width:100px;height:40px;" id="download" value="导出模版" onclick="downloadFile()"/>
</sec:authorize>
							</div>
							<div style="margin-top: 50px"></div>
							<label class="topjui-form-label" style="font-size:20px;">选择文件</label>
							<input type="file" id="file1" style="width: 30%;font-size:20px;"/>
<sec:authorize access="hasAuthority('BASICDATAIMPORT')">
        					<input type="button" id="upload" value="导入" style="width:100px;height:40px;" onclick="uploadFile()"/>
</sec:authorize>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>