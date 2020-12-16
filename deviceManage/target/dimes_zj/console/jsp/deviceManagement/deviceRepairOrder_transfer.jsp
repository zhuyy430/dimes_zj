<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post">
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div
						style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;">
						设备报修单转单</div>
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
									data-toggle="topjui-textbox"
									data-options="required:true,showSeconds:true,formatter:function(value,row,index){
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
                                    }"
									id="createDate" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报修人员</label>
							<div class="topjui-input-block">
								<input type="text" name="informant"
									data-toggle="topjui-textbox" data-options="required:true"
									id="informant"  readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
									<input data-toggle="topjui-textbox" data-options="required:true"  name="productionUnitName" id="productionUnitName" readonly="readonly">
							</div>
					</div>
					</div>
					<div class="topjui-row">
					
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceCode"
									data-toggle="topjui-textbox" data-options="required:true,"
									id="deviceCode" readonly="readonly"/>
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceName" data-toggle="topjui-textbox"
									data-options="required:false" id="deviceName" readonly="readonly">
									<input type="text" hidden="hidden" name="deviceId"
									id="deviceId" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">维修人员</label>
							<div class="topjui-input-block">
								<input type="text" name="maintainName"
									data-toggle="topjui-textbox" data-options="required:false"
									id="maintainName" readonly="readonly">
								<input type="hidden" name="maintainCode"
									id="maintainCode">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">设备类别</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceTypeName"
									data-toggle="topjui-textbox" data-options="required:false"
									id="deviceTypeName" readonly="readonly">
								<input type="text" hidden="hidden" name="deviceTypeId"
									id="deviceTypeId">
							</div>
						</div>
					</div>
					<div class="topjui-col-sm12">
							<label class="topjui-form-label">转单原因</label>
							<div class="topjui-input-block">
								<select data-toggle="topjui-combobox" name="reason"
								 data-options="required:false" id="reason">
									 <option>换班交接</option>
									 <option>新任务派遣</option>
									 <option>能力不足</option>
								</select>
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>
</div>
<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>

<!-- 工具按钮 -->