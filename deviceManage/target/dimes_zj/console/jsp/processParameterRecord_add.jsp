<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function(){
		var date = new Date();
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
        
        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
        				' ' + hourStr + ':' + minuteStr + ':' + secondStr; 
        
        $("#currentDate").val(dateStr);
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="生产单元信息" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">参数名称</label>
						<div class="topjui-input-block">
								<input id="parameterName" data-toggle="topjui-combobox" name="parameterName" data-options="valueField:'parameterName',textField:'parameterName',
								url:'processParameterRecord/queryParametersByWorkpieceCodeAndProcessCode.do?workPieceCode={workPieceCode}&processCode={processCode}',
								onSelect:function(data){
									$('#parameterCode').iTextbox('setValue',data.parameterCode);
									$('#upLine').iTextbox('setValue',data.upLine);
									$('#lowLine').iTextbox('setValue',data.lowLine);
									$('#standardValue').iTextbox('setValue',data.standardValue);
								}">
								<input type="hidden" name="workPieceCode" value="">
								<input type="hidden" name="processCode" value="">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">参数代码</label>
						<div class="topjui-input-block">
							<input type="text" name="parameterCode" data-toggle="topjui-textbox"
								data-options="required:false" id="parameterCode" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">控制线UL</label>
						<div class="topjui-input-block">
							<input type="text" name="upLine" data-toggle="topjui-textbox"
								data-options="required:false" id="upLine" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">控制线LL</label>
						<div class="topjui-input-block">
							<input type="text" name="lowLine" data-toggle="topjui-textbox"
								data-options="required:false" id="lowLine" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">标准值</label>
						<div class="topjui-input-block">
							<input type="text" name="standardValue" data-toggle="topjui-textbox"
								data-options="required:false" id="standardValue" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">时间</label>
						<div class="topjui-input-block">
							<input type="text" name="currentDate" data-toggle="topjui-datetimebox"
								data-options="required:false,showSeconds:true" id="currentDate">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">参数值</label>
						<div class="topjui-input-block">
							<input type="text" name="parameterValue" data-toggle="topjui-numberbox"
								data-options="required:true,precision:2,value:0" id="parameterValue" >
						</div>
					</div>
				</div>
				<!-- <div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">状态</label>
						<div class="topjui-input-block">
							<input type="text" name="status" data-toggle="topjui-textbox"
								data-options="required:false" id="status">
						</div>
					</div>
				</div> -->
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">状态/故障代码</label>
						<div class="topjui-input-block">
							<input type="text" name="statusCode" data-toggle="topjui-textbox"
								data-options="required:false" id="statusCode">
						</div>
					</div>
				</div>
			</div>
		</div>
		
		</form>
	</div>
</div>