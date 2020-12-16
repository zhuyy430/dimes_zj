<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
							<div style="margin-top: 50px"></div>
							<input type="file" id="file1" style="width: 80%;font-size:20px;"/>
        					<!-- <input type="button" id="upload" value="导入" style="width:100px;height:40px;" onclick="uploadFile()"/> -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>