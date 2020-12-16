<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="安环等级" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级级别</label>
							<div class="topjui-input-block">
								<input type="text" name="weight" data-toggle="topjui-combobox"
									data-options="valueField:'value',textField:'text',data:[
									{value:1,text:'一级',selected:true},
									{value:2,text:'二级'},
									{value:3,text:'三级'},
									{value:4,text:'四级'},
									{value:5,text:'五级'},
									{value:6,text:'六级'},
									{value:7,text:'七级'},
									{value:8,text:'八级'},
									{value:9,text:'九级'},
									{value:10,text:'十级'}
									]" id="weight">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">颜色</label>
							<div class="topjui-input-block">
								<input type="color" name="color" data-toggle="topjui-textbox"
									id="color">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="note" data-toggle="topjui-textarea"
									id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div style="color:red;text-align:center;">注：等级级别越高，代表故障越严重!</div>
	</div>
</div>