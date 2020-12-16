<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$(function(){
	//查询所有技能等级
	$('#skillLevel').iCombogrid({
				idField:'id',
				textField:'name',
				delay:500,
				mode:'remote',
				url:'processSkillLevel/queryProcessSkillLevelsBySkillIdNoPage.do',
				columns:[[ 
					{field:'id',title:'id',width:60,hidden:true},
					 {field:'code',title:'代码',width:100},
					{field:'name',title:'名称',width:100} 
					]],
					onClickRow : function(index, row){
						$("#skillLevelId").val(row.id);
					},
					queryParams:{
						skillId:$("#skill").iDatagrid("getSelected").skillLevel.skill.id
					}
			});
});

</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="技能信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">技能</label>
							<div class="topjui-input-block">
								<input name="skillName" type="text" readonly="readonly"
									data-toggle="topjui-textbox" data-options="required:true"
									id="empSkill" /> <input type="hidden" name="skillId"
									id="skillId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级</label>
							<div class="topjui-input-block">
								<input type="text" name="skillLevelName"
									data-toggle="topjui-combogrid" data-options="required:true"
									id="skillLevel"> <input type="hidden"
									name="skillLevelId" id="skillLevelId" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>