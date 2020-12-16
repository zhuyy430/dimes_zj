<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		 $.get("role/queryMCPowers.do",{
							roleId : $('#departmentDg').iDatagrid('getSelected').id
						},
						function(result) {
							var mcPowersForm = $("#mcPowersForm");
							mcPowersForm.empty();
							if (result.mcPowers && result.mcPowers.length > 0) {
								for (var i = 0; i < result.mcPowers.length; i++) {
									var power = result.mcPowers[i];
									if (result.roleMCPowers
											&& result.roleMCPowers.length > 0) {
										var isExist = false;
										for (j = 0; j < result.roleMCPowers.length; j++) {
											var roleMCPower = result.roleMCPowers[j];
											if (power == roleMCPower.mcpower) {
												isExist = true;
												fillCheckbox(power,true,mcPowersForm);
												break;
											}
										}

										if (!isExist) {
											fillCheckbox(power,false,mcPowersForm);
										}
									} else {
										fillCheckbox(power,false,mcPowersForm);
									}
								}
							}
							//$.parser.parse();
						}); 
	});

	function fillCheckbox(mcpower, checked,mcPowersForm) {
		var $div = $('<div style="margin-bottom:10px;padding-right:200px;">');
		var $input;
		if (checked) {
			// $input = $('<input  data-toggle="topjui-checkbox" checked="true" name="mcpower" value="'+mcpower+'" label="'+mcpower+'">');
			 $input = $('<input type="checkbox" name="mcpower" checked="checked" value="'+mcpower+'">');
		} else {
			//$input = $('<input data-toggle="topjui-checkbox" name="mcpower" value="'+mcpower+'" label="'+mcpower+'">');
			 $input = $('<input type="checkbox" name="mcpower" value="'+mcpower+'">'+mcpower);
		}
		$input.css("margin-right","20px");
		$div.append($input);
		$div.append(mcpower);
		mcPowersForm.append($div);
	}
</script>
<style type="text/css">
input[type=checkbox] {
    margin-right: 10px;
    cursor: pointer;
    width: 15px;
    left:15px;
    height: 15px;
    position: relative;
}
 
input[type=checkbox]:after {
    position: absolute;
    width: 10px;
    height: 15px;
    top: 10;
    content: " ";
    background-color: #fff;
    color: #fff;
    display: inline-block;
    visibility: visible;
    border: 1px solid grey;
    padding: 0 3px;
    border-radius: 3px;
}
 
input[type=checkbox]:checked:after {
    background-color: #0f97e7;
    content: "✓";
    font-size: 12px;
}
 
input[type=checkbox]:disabled:after {
    width: 10px;
    height: 15px;
    top: 10;
    color: #fff;
    display: inline-block;
    visibility: visible;
    border: 1px solid grey;
    padding: 0 3px;
    border-radius: 3px;
    background-color: #E9E7E3;
    content: "✓";
    font-size: 12px;
}
</style>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="mcPowersForm" style="margin-right:20px;"></form>
</div>
	</div>
</div>