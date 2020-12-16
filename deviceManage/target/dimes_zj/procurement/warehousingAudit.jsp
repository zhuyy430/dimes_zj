<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="procurement/js/warehousingApplicationForm.js"></script>
<script type="text/javascript" src="common/js/ajaxfileupload.js"></script>
<script type="text/javascript">
	//搜索采购订单记录
	function reloadRecord(){

		var str = $("input:checkbox[name='status']:checked").map(function(index,elem) {
		                return $(elem).val();
		        }).get().join("','");
		$("#warehousingApplicationFormDetailDg").iDatagrid("reload",{
				checkStatus:"'"+str+"'",
		        vendor:$("#vendor").val(),
		        requestNo:$("#requestNo").val(),
		        purchasingOrderNo:$("#purchasingOrderNo").val(),
		});
	}
	function downloadFile() {
		var dg = $("#documentData").iDatagrid('getSelected');
		if (!dg) {
			$.messager.alert("提示", "请选择要下载的文件!");
			return;
		}
		var id = dg.id;
		window.location.href = "applicationRelatedDoc/download.do?id=" + id;
	}
	//预览
	function preview() {
		var $doc = $("#documentData").iDatagrid("getSelected");
		if (!$doc) {
			$.iMessager.alert('提示','请选择预览的文档!');
			return false;
		}
		var url = $doc.url;
		//显示文档内容的iframe对象
		var $previewDiv = $("#fullScreenDiv");
		$previewDiv.empty();
		var suffix = '';
		//截取后缀
		if (url != null && $.trim(url) != '') {
			var point = url.lastIndexOf(".");
			suffix = url.substr(point + 1);
		} else {
			return false;
		}
		var $docIframe = $("<iframe style='width:100%;height:100%;'>");
		switch (suffix) {
			case "pdf":
			case "PDF": {
				/* $docIframe.attr("src", url);
				$previewDiv.append($docIframe); */
				window.open(url);
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
			case "BMP": {
				window.open(url);			
				break;
			}
			default: {
				$.iMessager.alert('提示','选择文件不支持预览!');
			}
		}
	}
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                        data-options="id:'warehousingApplicationFormDetailDg',
                       url:'warehousingApplicationFormDetail/queryWarehousingApplicationFormDetail.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       childTab: [{id:'southTabs'}],
                       onLoadSuccess:function(){
                       		$('#southTabs').iTabs('select',0);
                       }">
                    <thead>
                    <tr>
                    	<th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
                        <th data-options="field:'checkStatus',title:'检验状态',width:'180px',align:'center'"></th>
                        <th data-options="field:'purchasingNo',title:'采购单号',width:'180px',align:'center'"></th>
                        <th data-options="field:'formNo',title:'申请单号',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.warehousingApplicationForm){
                                return row.warehousingApplicationForm.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'vendorName',title:'供应商名称',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.warehousingApplicationForm){
                                return row.warehousingApplicationForm.vendorName;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'180px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'180px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'180px',align:'center'"></th>
                        <th data-options="field:'amount',title:'数量',width:'180px',align:'center'"></th>
                        <th data-options="field:'unit',title:'单位',width:'180px',align:'center'"></th>
                        <th data-options="field:'amountOfBoxes',title:'箱数',width:'180px',align:'center'"></th>
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
                         id:'warehousingApplicationFormDetailDg',
                         param:'id:id'
                     }">
						<div title="文档资料" data-options="id:'tab1',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'documentData',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
                       			fitColumns:true,
						       url:'applicationRelatedDoc/queryDocsByRelatedId.do'">
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
<div id="warehousingApplicationFormDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'warehousingApplicationFormDetailDg'
       }">
<sec:authorize access="hasAuthority('QUERY_WAREHOUSINGAUDIT')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadRecord()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PASS_WAREHOUSINGAUDIT')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="method:'doAjax',
       extend: '#warehousingApplicationFormDetailDg-toolbar',
       iconCls:'fa fa-check-circle-o',
       url:'warehousingApplicationFormDetail/checkWarehousingApplicationFormDetail.do?status=检验通过',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'warehousingApplicationFormDetailDg',param:'id:id'}" id="audit">检验通过</a>
</sec:authorize>
<sec:authorize access="hasAuthority('NOTPASS_WAREHOUSINGAUDIT')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="method:'doAjax',
       extend: '#warehousingApplicationFormDetailDg-toolbar',
       iconCls:'fa fa-times-circle-o',
       url:'warehousingApplicationFormDetail/checkWarehousingApplicationFormDetail.do?status=检验未通过',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'warehousingApplicationFormDetailDg',param:'id:id'}" id="unaudit">检验未通过</a>
</sec:authorize>
	<form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
    <input type="checkbox" name="status" value="未检验" style=""><label>未检验</label>
    <input type="checkbox" name="status" value="检验通过"><label>检验通过</label>
    <input type="checkbox" name="status" value="检验未通过"><label>检验未通过</label>
        供应商:<input id="vendor" data-toggle="topjui-textbox" style="width:12%">
        申请单号:<input id="requestNo" data-toggle="topjui-textbox" style="width:12%">
        采购单号:<input id="purchasingOrderNo" data-toggle="topjui-textbox" style="width:12%">
    </form>
    
    <!-- 文档资料表格工具栏开始 -->
	<div id="documentData-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'documentData'
       }">
<sec:authorize access="hasAuthority('UPLOAD_DOC_WAREHOUSINGAUDIT')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#documentData-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
           type:'datagrid',
           id:'warehousingApplicationFormDetailDg',
           param:'id:id'
       },
       dialog:{
           id:'uploadDocDialog',
           title:'上传',
           width: 600,
           height: 400,
           href:'procurement/device_doc_upload.jsp',
           buttons:[
               {text:'上传',handler:function(){
               var file = $('#file').val();
               if(!file){
               	$.iMessager.alert('提示','请选择要上传的文件!');
					  return false;
               }
               	 $.ajaxFileUpload({
                url: 'applicationRelatedDoc/upload.do', 
                type: 'post',
                data: {
                		name:$('#name').iTextbox('getValue'), 
                		note:$('#note').iTextbox('getValue'),
                		relatedId:$('#warehousingApplicationFormDetailDg').iDatagrid('getSelected').id
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
<sec:authorize access="hasAuthority('DEL_DOC_WAREHOUSINGAUDIT')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#documentData-toolbar',
       iconCls:'fa fa-trash',
       url:'applicationRelatedDoc/deleteRelatedDocument.do',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'documentData',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DOWNLOAD_DOC_WAREHOUSINGAUDIT')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-download'" onclick="downloadFile()">下载</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PREVIEW_DOC_WAREHOUSINGAUDIT')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			onclick="preview()" data-options="iconCls:'fa fa-eye'">预览</a>
</sec:authorize>
			
	</div>
</div>
<div id="showVendorsDialog"></div>
</body>
</html>
