//管理员登录
function adminLogin()
{
		var url = contextPath + "mcuser/adminLogin.do";
	    var username = $("#adminname").val();
    	if(!username){
    		$("#alertText").text("用户名不能为空!");
    		$("#alertDialog").modal();
    		$("#adminname").focus();
    		return false;
    	}
    	
    	var password = $("#adminpassword").val();
    	if(!password){
    		$("#alertText").text("密码不能为空!");
    		$("#alertDialog").modal();
    		$("#adminpassword").focus();
    		return false;
    	}
	    $.post(url,{username:username,password:password},function(data){
        	if(data.success){
        		window.location.href=contextPath + "mc/setmsg.jsp";
        	}else{
        		$("#alertText").text(data.msg);
        		$("#alertDialog").modal();
        	}
	    });
}

$("#tcTable").bootstrapTable({
	striped: true, //隔行换色
	columns: [{
		field: 'id',
		title: '生产日期'
	}, {
		field: 'name',
		title: '工单号'
	}, {
		field: 'price',
		title: '工件代码'
	}, {
		field: 'price',
		title: '工件名称'
	}, {
		field: 'price',
		title: '客户图号'
	}, {
		field: 'price',
		title: '图号'
	}, {
		field: 'price',
		title: '批号'
	}, {
		field: 'price',
		title: '材料编号'
	}, {
		field: 'price',
		title: '版本号'
	}, {
		field: 'price',
		title: '站点代码'
	}, {
		field: 'price',
		title: '计划数量'
	}, {
		field: 'price',
		title: '完工数量'
	}, {
		field: 'price',
		title: '报工数量'
	}],
	data: [{
		id: 1,
		name: 'Item 1',
		price: ''
	}, {
		id: 2,
		name: 'Item 2',
		price: '$2'
	}]
});

$("#tcTable").on('click-row.bs.table', function(e, row, element) {
	//alert(row.name);
	$('.success').removeClass('success'); //去除之前选中的行的，选中样式
	$(element).addClass('success'); //添加当前选中的 success样式用于区别

	
	//alert(getSelectRows.id);
	//console.log(getSelectRows);
});
