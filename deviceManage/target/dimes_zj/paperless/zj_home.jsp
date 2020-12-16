<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
    Employee employee = (Employee)session.getAttribute("employee");
    ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<link rel="stylesheet" type="text/css" href="mc/assets/css/icon.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css">
<link rel="stylesheet" type="text/css" href="paperless/css/bootstrap-select.min.css">
<link rel="stylesheet" href="mc/assets/css/bootstrap-timepicker.min.css">
<link rel="stylesheet" href="paperless/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet"
      href="paperless/js/bootstrap-table/bootstrap-table.min.css">


<script src="common/js/bootstrap.min.js"></script>
<script src="paperless/js/bootstrap-treeview.min.js"></script>
<script type="text/javascript" src="paperless/js/devicepreview.js"></script>
<script type="text/javascript" src="paperless/js/deviceCheckPlan.js"></script>
<script type="text/javascript" src="paperless/js/deviceRepair.js"></script>

<script type="text/javascript" src="paperless/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="mc/assets/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://npmcdn.com/flatpickr/dist/l10n/zh.js"></script>
<script type="text/javascript"  src="paperless/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="paperless/js/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript" src="paperless/js/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="paperless/js/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>

<script>
    window.onload=function(){
        //定时器每秒调用一次fnDate()
        setInterval(function(){
            fnDate();
        },1000);
    }

    function fnDate(){
        //时间
        var date = new Date();

        var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
            + (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
            + (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
        $("#time").text(value);
    }
    $(function(){
        var treedata =[];
        $.ajax({
            type : "post",
            url : contextPath + "productionUnit/queryTopProductionUnits.do",
            data : {},
            cache:false,
            dataType : "json",
            success : function(data) {
                $.each(data,function(index, Type) {
                    getData(index,Type,treedata);
                });
                $('#tree').treeview({
                    data: treedata,//节点数据
                    color:"#FFFFFF",
                    backColor:"#1C2437",
                    borderColor:"#1C2437"
                });
            }
        });
        //生产单元赋值
        <%-- var pname="<%=request.getParameter("pname")%>";
        console.log(pname);
        if(pname!=""){
            $("#paperProductionUnitName").text(pname);
        } --%>
        //时间
        var time = new Date();
        $("#time").text(getDateTime(time));
        //搜索工件文档
        searchWorkpieceDocs();
    });
    function openOne() {
        $('#right').css('display','inline');
        $('#productionInfo_right').css('display','none');
        $('#deviceAbout_right').css('display','none');
        $('#collapseOne').collapse('show');
        $('#collapseTwo').collapse('hide');
        $('#collapseThree').collapse('hide');
    }
    function openTwo() {
        <% if (productionLine != null) {
					%>
        $('#right').css('display','none');
        $('#productionInfo_right').css('display','inline');
        $('#deviceAbout_right').css('display','none');
        $('#collapseOne').collapse('hide');
        $('#collapseTwo').collapse('show');
        $('#collapseThree').collapse('hide');
        <%
            }else{
        %>
        $('#collapseOne').collapse('show');
        $('#collapseTwo').collapse('hide');
        $('#collapseThree').collapse('hide');
        alert("请先选择所在生产单元。");
        <%
        }
        %>
    }

    function openThree() {
        <% if (productionLine != null) {
                           %>
        $('#right').css('display','none');
        $('#productionInfo_right').css('display','none');
        $('#deviceAbout_right').css('display','inline');
        $('#collapseOne').collapse('hide');
        $('#collapseTwo').collapse('hide');
        $('#collapseThree').collapse('show');
        <%
            }else{
        %>
        $('#collapseOne').collapse('show');
        $('#collapseTwo').collapse('hide');
        $('#collapseThree').collapse('hide');
        alert("请先选择所在生产单元。");
        <%
        }
        %>
    }

    //退出
    function logout(){
        $.iMessager.confirm('确认对话框', '您想要退出该系统吗？', function(r){
            if (r){
                $.get("paperlessUser/logout.do",function(result){
                    if(result.success){
                        window.location.href="paperless/homePage.jsp";
                    }else{
                        $.iMessager.alert("警告","退出失败，系统内部错误!");
                    }
                });
            }
        });
    }

    //显示
    function showupdateProduction() {
        $("#updateProduction").modal('show');
    }
    //更新生产单元
    function update() {
        var data = $("#tree").treeview('getSelected');
        $.get("paperlessProductionLine/updateConfirmProductionLine.do", {
            productionLineId: data[0].Id
        }, function (result) {
            if (result.success) {
                /*$("#paperProductionUnitName").text(result.productionLine.name);
                $("#paperProductionUnitCode").val(result.productionLine.code);
                $("#paperProductionUnitId").val(result.productionLine.id);
                loadDevices4ProductionUnit();
                $("#deviceCode").val("");
                $("#updateProduction").modal('hide');*/
                location.reload();
            }
        })
    }
    //隐藏
    function updateProductionHide() {
        $("#updateProduction").modal('hide');
    }
    function getData(index,data,tree) {
        if(data==undefined){return}
        if(index<1){
            if(tree.length == 0){   //起始设置
                var d={
                    text: data.name, //节点显示的文本值  string
                    code:data.code,
                    Id:data.id,
                    selectedIcon: "glyphicon glyphicon-ok", //节点被选中时显示的图标       string
                    color: "#FFFFFF", //节点的前景色      string
                    backColor: "#1C2437", //节点的背景色      string
                    href: "", //节点上的超链接
                    selectable: true, //标记节点是否可以选择。false表示节点应该作为扩展标题，不会触发选择事件。  string
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        /*disabled: true,*/ //是否禁用节点
                        expanded: false, //是否展开节点
                        selected: false //是否选中节点
                    },
                    nodes: []
                }
                tree.push(d);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,d);
                    })
                }
            }else{ //下一级对象
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.nodes.push(c);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }
        }else{
            if(tree instanceof Array){
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.push(c)
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }else{
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.nodes.push(c);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }
        }
    }
    //查询在制图号
    function dimesDo(){
        <% if (productionLine != null) {
					%>
        $.get("paperlessWorkpieceDoc/queryDimesDo.do",function(result){
            fillDocTable(result);
        });
        <%
            }else{
        %>
        alert("请先选择所在生产单元。");
        <%
        }
        %>

    }
    //点击确定按钮，查找工件文档信息
    function searchWorkpieceDocs(){
        $.get("paperlessWorkpieceDoc/queryAllWorkpieceDocs.do",{
            code:$("#code").val(),
            codeType:$("#codeType").val(),
            docType:$("#docType").val()
        },function(result){
            fillDocTable(result);
        });
    }
    //填充文档信息表
    function fillDocTable(docs){
        if(docs!=null && docs.length>0){
            //tbody对象
            var $docContent = $("#docContent");
            $docContent.empty();
            for(var i = 0;i<docs.length;i++){
                var doc = docs[i];
                var $tr = $("<tr>");
                var $num = $("<td style='width:50px;'>");
                $num.append(i+1);
                $tr.append($num);

                var $docName = $("<td>");
                $docName.append(doc.name);
                $tr.append($docName);

                var $srcName = $("<td>");
                $srcName.append(doc.srcName);
                $tr.append($srcName);

                var $note = $("<td>");
                $note.append(doc.note);
                $tr.append($note);

                var $contentType = $("<td>");
                $contentType.append(doc.contentType);
                $tr.append($contentType);

                var $uploadDate = $("<td>");
                $uploadDate.append(getDateTime(new Date(doc.uploadDate)));
                $tr.append($uploadDate);

                var $preview = $("<td>");
                var $a = $("<a style='text-decoration:none;cursor:pointer;' href='javascript:void(0)'>");
                $a.append("<span class='fa fa-eye' onClick='preview(\""+doc.url+"\")'> 预览</span>");
                $preview.append($a);
                $tr.append($preview);

                $docContent.append($tr);
            }
        }else{
            var $docContent = $("#docContent");
            $docContent.empty();
            var $tr = $("<tr style='text-align: center ;border: white 1px solid;line-height: 50px;height: 50px;'>");
            var $td = $("<td colspan='7'>");
            $td.append("未搜索到文档");
            $tr.append($td);
            $docContent.append($tr);
        }
    }
    var docUrl = '';
    //文档预览
    function preview(url){
        //显示文档内容的iframe对象
        var $previewDiv = $("#previewDiv");
        $previewDiv.empty();
        var suffix = '';
        //截取后缀
        if(url!=null && $.trim(url)!=''){
            var point = url.lastIndexOf(".");
            suffix = url.substr(point+1);
        }else{
            return false;
        }
        var $docIframe=$("<iframe style='width:100%;height:100%;'>");
        switch(suffix){
            case "pdf":
            case "PDF":{
                $docIframe.attr("src",url);
                $previewDiv.append($docIframe);
                break;
            }
            <%-- case "doc":
            case "docx":
            case "DOC":
            case "DOCX":
            case "xls":
            case "xlsx":
            case "XLS":
            case "XLSX":
            case "ppt":
            case "pptx":
            case "PPT":
            case "PPTX":{
                $docIframe.attr("src",'https://view.officeapps.live.com/op/view.aspx?src=<%=basePath%>'+url);
                $previewDiv.append($docIframe);
                break;
            } --%>
            case "png":
            case "PNG":
            case "jpg":
            case "JPG":
            case "JPEG":
            case "jpeg":
            case "gif":
            case "GIF":
            case "bmp":
            case "BMP":{
                var $img = $("<img style='width: auto;height: auto;max-width: 100%;max-height: 100%;'>");
                $img.attr("src",url);
                $previewDiv.append($img);
                break;
            }
            default:{
                var $p = $("<p style='font-size:20px;color:red;'>");
                $p.append(suffix + "不支持预览!");
                $previewDiv.append($p);
                return false;
            }
        }

        docUrl = url;
    }
    //全屏查看
    function showFullScreen(){
        //显示文档内容的iframe对象
        var $previewDiv = $("#fullScreenDiv");
        $previewDiv.empty();
        var suffix = '';
        //截取后缀
        if(docUrl!=null && $.trim(docUrl)!=''){
            var point = docUrl.lastIndexOf(".");
            suffix = docUrl.substr(point+1);
        }else{
            alert("没有预览文档!");
            return false;
        }
        var $docIframe=$("<iframe style='width:100%;height:100%;'>");
        switch(suffix){
            case "pdf":
            case "PDF":{
                $docIframe.attr("src",docUrl);
                $previewDiv.append($docIframe);
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
            case "BMP":{
                var $img = $("<img style='height:100%;width:100%;'>");
                $img.attr("src",docUrl);
                $previewDiv.append($img);
                break;
            }
        }
        $("#dialog-layer").css("display","block");
    }
</script>
<script type="text/javascript">

    /*删除功能*/
    function delImg(obj) {
        var _this = $(obj);
        var id=_this.siblings(".input").val();
        var photoIds=idList.split(",");
        console.log(id);
        console.log("前："+photoIds);
        photoIds.splice(id,1,"");
        console.log("后："+photoIds);
        imageList.splice(id,1,id)
        var plist="";
        for(var i = 0; i < photoIds.length; i++){
            console.log(photoIds[i]);
            if(i==0){
                plist=photoIds[i];
            }else{
                plist+=","+photoIds[i];
            }
            console.log(plist);
        }
        idList=plist;
        console.log("后："+idList);
        $("#photoIds").val(photoIds)
        _this.parents(".imageDiv").remove();
        $(".addImages").show();
    };
    $(function(){
        <%-- var pname="<%=request.getParameter("pname")%>";
        console.log(pname);
        if(pname!=""){
            $("#paperProductionUnitName").text(pname);
        } --%>
        $('#checkDate').datetimepicker();
        $("#photo").css("border","1px dotted #666666");
        $("#updatePhoto").css("border","1px solid  #666666");
        $("#maintenancePhoto").css("border","1px solid  #666666");
        setlostTimeType();
        var userAgent = navigator.userAgent; //用于判断浏览器类型
        idList=$("#photoIds").val();

        /* //报修单时间初始化
       $("#createDate").datetimepicker({
           format : 'yyyy-mm-dd hh:ii:ss',
           language : 'zh-CN',
           autoclose : true,
           todayBtn:'linked'
       }); */



        $(".file").change(function() {
            //console.log($("#fileInput")[0].files);
            //获取选择图片的对象
            var docObj = $(this)[0];
            var picDiv = $(this).parents(".picDiv");
            //得到所有的图片文件
            var fileList = docObj.files;
            //循环遍历
            for (var i = 0; i < fileList.length; i++) {
                //idList.split(",").length
                /*  if(!idList){
                     imageVal=0;
                 }else{
                     imageVal=idList.split(",").length;
                 } */
                for(var j = 0; j < imageList.length; j++){
                    console.log(imageList[j]);
                    console.log(imageList[j]!=="");
                    if(imageList[j]!==""){
                        console.log(imageList[j]);
                        imageVal=imageList[j]
                        break;
                    }
                }
                //动态添加html元素onclick='deleteImg(\""+ imageVal+ "\")
                var picHtml = "<div class='imageDiv' style='margin-left: 30px'> <img  id='img" + fileList[i].name + "' /><div class='cover'><i class='delbtn' onclick='delImg(this)'>删除</i><input class='input'  type='hidden' value="+imageVal+" /></div></div>";
                console.log(picHtml);

                imageList.splice(imageVal,1,"");
                console.log(imageList);
                console.log(imageVal);
                console.log("name:"+fileList[i].name);
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
                        console.log(data.filePath);
                        if(!idList){
                            idList=data.filePath;
                            $("#photoIds").val(idList)
                        }else{
                            var photoIds=idList.split(",");
                            console.log("before:"+photoIds);
                            photoIds.splice(imageVal,1,data.filePath);
                            console.log("after:"+photoIds);
                            idList=photoIds.join(',');
                            console.log(idList);
                            $("#photoIds").val(idList)
                        }
                    }
                });
                if (fileList && fileList[i]) {
                    //图片属性
                    imgObjPreview.style.display = 'block';
                    imgObjPreview.style.width = '300px';
                    imgObjPreview.style.height = '250px';
                    //imgObjPreview.src = docObj.files[0].getAsDataURL();
                    //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要以下方式
                    if (userAgent.indexOf('MSIE') == -1) {
                        //IE以外浏览器
                        imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]); //获取上传图片文件的物理路径;
                        //console.log(imgObjPreview.src);
                        //var msgHtml = '<input type="file" id="fileInput" multiple/>';
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

        //下拉框未选中显示
        $(".selectpicker").selectpicker({
            noneSelectedText: '请选择' //默认显示内容
        });
    });

    //显示报修新增窗口
    function showAddDeviceRepairOrderDialogDialog() {
        getAllReasonAdd();
        $('#ngDescription').val("");
        $('#picDiv').empty();
        $('div').remove(".imageDiv");
        $(".addImages").show();
        idList="";
        imageList=[0,1,2,3];
        $('#addDeviceRepairOrderDialog').modal('show');
        var date = new Date();
        var value = date.getFullYear() + "-" + ((date.getMonth() + 1)>9?(date.getMonth() + 1):"0"+(date.getMonth() + 1)) + "-"
            + (date.getDate()>9?date.getDate():"0"+date.getDate()) + " " + (date.getHours()>9?date.getHours():"0"+date.getHours()) + ":"
            + (date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes()) + ":" + (date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds());
        $("#createDate").val(value);
        $.ajax({
            type : "post",
            url : contextPath + "deviceSite/queryDeviceSiteByCode.do",
            data : {"deviceSiteCode":"DS_Device1"},
            cache:false,
            dataType : "json",
            success : function(data) {
                if(data){
                    if(data.device){
                        $("#deviceCode").val(data.device.code);
                        $("#deviceName").val(data.device.name);
                        $("#unitType").val(data.device.unitType);
                        $("#deviceType").val(data.device.projectType.name);
                        //根据IP地址查询本机是否有登录用户
                        $.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
                            mcUser) {
                            if (mcUser) {
                                $("#Informant").val(mcUser.employeeName);
                            }
                        });
                        $("#deviceId").val(data.device.id);
                    }
                }
            }
        })
    }
    //添加报修单
    function addDeviceRepair(){
        var deviceSiteCode = $("#deviceSiteCode").val();
        $.ajax({
            type : "post",
            url : contextPath + "mcDeviceRepairOrder/addDeviceRepairOrder.do",
            data : {
                "createDate":$('#createDate').val(),
                "device.id":$('#deviceId').val(),
                "Informant":$('#Informant').val(),
                "pressLight.id":$('#pressLightId').val(),
                "ngDescription":$('#ngDescription').val(),
                "idList":$('#photoIds').val()
            },
            dataType : "json",
            success : function(data) {
                $("#alertText").text(data.msg);
                $("#alertDialog").modal();
                $('#addDeviceRepairOrderDialog').modal('hide');
                if(data.success){
                    $("#showDeviceRepairOrderTable").bootstrapTable("refresh", {
                        url :"mcDeviceRepairOrder/queryDeviceRepairOrderByDeviceCode.do",
                        cache:false,
                        query : {
                            deviceCode :_deviceSiteCode
                        }
                    });
                }
            }
        })
    }

    //获取所有故障原因
    function getAllReasonAdd(){
        $.ajax({
            type : "post",
            url : contextPath + "projectType/queryProjectTypesByType.do?type=breakdownReasonType",
            data : {},
            cache:false,
            dataType : "json",
            success : function(data) {
                //alert(JSON.stringify(data));
                var htmlselect = "<option></option>";
                $.each(data,function(index, Type) {
                    htmlselect += "<option value='"+Type.id+"'>"+Type.name+"</option>";
                })
                console.log(htmlselect);
                $("#pressLight").empty();
                $("#pressLight").append(htmlselect);
                $("#pressLight").selectpicker('refresh');

                $("#updatePressLight").empty();
                $("#updatePressLight").append(htmlselect);
                $("#updatePressLight").selectpicker('refresh');

            }
        })
    }
    //模态框点击返回事件(hide)
    function modelHide() {
        $('#showConfirmDlg').modal('hide');
        $('#showUpdateDlg').modal('hide');
        $('#addDeviceRepairOrderDialog').modal('hide');
        $('#updateDeviceRepairOrderDialog').modal('hide');
        $('#addMaintenanceStaff').modal('hide');
        $('#updateCheckingPlanDialog').modal('hide');
        $('#maintenanceOrderDialog').modal('hide');
        $('#showDoclog').modal('hide');
    }
    //故障弹出框
    function showng(){
        $("#lostReason").empty();
        $("#lostType button").removeClass("btn btn-primary");
        $("#lostType button").addClass("btn btn-default");
        $('#showNgdialog').modal('show');
    }

    //损时类型下拉框
    function setlostTimeType() {
        $.ajax({
            type : "post",
            url : contextPath + "pressLightType/queryFirstLevelType.do?type=PRESSLIGHTTYPE",
            data : {},
            cache:false,
            dataType : "json",
            success : function(data) {
                var htmlselect = "<option></option>";
                $.each(data,function(index, Type) {
                    htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
                    var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='showDetail(\""
                        + Type.code
                        + "\")' value='"+Type.code+"'>"
                        + Type.name
                        + "</button>");
                    var splitButton = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;margin-left:px;height:45px' onclick='splitShowDetail(\""
                        + Type.code
                        + "\")' value='"+Type.code+"'>"
                        + Type.name
                        + "</button>");
                    $("#lostType").append(button);
                })
            }
        })
    }
    //点击类型事件
    function showDetail(typeCode) {
        $("#lostType button").removeClass("btn btn-primary");
        $("#lostType button").addClass("btn btn-default");
        var btns = $("#lostType button");
        for(var i = 0;i<btns.length;i++){
            var btn = $(btns[i]);
            var textOnButton = btn.val();
            if(textOnButton === typeCode){
                btn.removeClass("btn btn-default");
                btn.addClass("btn btn-primary");
            }
        }
        $.ajax({
            type : "post",
            url : contextPath + "mcPressLight/mcGetAllTypeByParentCode.do",
            data : {"pcode":typeCode},
            cache:false,
            dataType : "json",
            success : function(data) {
                $("#lostReason").empty();
                $.each(data,function(index, Type) {
                    var button = $("<button class='btn btn-default' type='button' style='margin-right:15px;margin-top:15px;width:150px;height:45px' onclick='showReasonBut(\""
                        + Type.id
                        + "\")' value ='"
                        + Type.id
                        + "'>"
                        + Type.reason
                        + "</button>");
                    $("#lostReason").append(button);
                })
            }
        })
    }
    function showReasonBut(typeid) {
        lostReasonid = "";
        $("#lostReason button").removeClass("btn btn-primary");
        $("#lostReason button").addClass("btn btn-default");
        var btns = $("#lostReason button");
        for(var i = 0;i<btns.length;i++){
            var btn = $(btns[i]);
            var textOnButton = btn.val();
            console.log(typeid);
            if(textOnButton === typeid){
                btn.removeClass("btn btn-default");
                btn.addClass("btn btn-primary");
                //$("#lostTimeType").selectpicker('val',data.pressLightType.code);
                $("#pressLight").selectpicker('val',typeid);
                $("#pressLightId").val(typeid);
            }
        }
    }

    //确认
    function Confirm(){
        $('#showNgdialog').modal('show');
    }
    //取消
    function Cancel(){
        $('#showNgdialog').modal('hide');
    }
    /*function chooseDevice(){
        console.log($("#deviceCode").val());
        if($("#deviceCode").val()!=null&&$("#deviceCode").val()!=""){
            $('#content').attr('src','paperless/deviceRepair.jsp');
        }else{
            /!* $("#alertText").text("请先选择设备，再点检");
            $("#alertDialog").modal(); *!/
            alert("请先选择设备，再报修");
        }

    }*/
</script>
<style>
    #dialog-layer{
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        overflow: hidden;
        z-index: 1000;
        display:none;
    }
    body {
        margin: 0;
    }
    #outer {
        height: 1080px;
        width: 1920px;
        background-image: url('front/imgs/blue.png');
        padding-top: 40px;
    }
    #loginInfo {
        text-align: center;
        width: 100%;
        height: 40px;
        text-align: right;
        color: #57EEFD;
        margin-right:50px;
        margin-bottom: 0px;
        position: absolute;
        left:-20px;
        top:20px;
        font-size:20px;
        z-index: 999;
    }
    #title {
        text-align: center;
        width: 100%;
        height: 50px;
        line-height: 50px;
        color: #57EEFD;
    }
    #time {
        width: 100%;
        height: 75px;
        text-align: center;
        line-height: 98px;
        color: #57EEFD;
        font-size: 25px;
    }
    #productionUnit{
        height: 50px;
        font-size: 20px;
        color: #57EEFD;
        float: left;
        margin-left: 60px;
        margin-top: -80px;
        font-weight: bold;
    }

    #department{
        height: 50px;
        font-size: 20px;
        color: #57EEFD;
        float: right;
        margin-right: 60px;
        margin-top: -80px;
        font-weight: bold;
    }

    #main{
        height: 860px;
        width: 1800px;
        margin:0 auto;
    }
    #left{
        height:100%;
        width:18%;
        float:left;
        background-color:#1C2437;
    }
    #right{
        height:100%;
        width:81.5%;
        float:right;
        background-color:#18192D;
    }

    .m_right{
        height:100%;
        width:81.5%;
        float:right;
        background-color:#18192D;
    }

    .leftDiv{
        height:60px;
        width:92%;
        margin-top:30px;
        margin-left:auto;
        margin-right:auto;
    }

    td,th{
        border:white 1px solid;
        color:white;
        font-size:14px;
        text-align:center;
        height:50px;
    }

    .tp_button{
        color:white;
        font-size: 25px;
        text-align: center;
        margin: 0 auto;
        background-color:#9966CC;
    }

    .device_button{
        color:white;
        font-size: 25px;
        text-align: center;
        margin: 0 auto;
        background-color:#1F3871;
        float: left;
        margin-right: 20px;
        height: 58px;
        line-height: 58px;
        border:1px solid #666666;
        AutoSize:true;
    }
    .device_button_nomal{
        color:white;
        font-size: 25px;
        text-align: center;
        margin: 0 auto;
        background-color:#1F3871;
        float: left;
        margin-right: 20px;
        height: 58px;
        line-height: 58px;
        border:1px solid #666666;
    }
    .device_button_pressed{
        color:black;
        font-size: 25px;
        text-align: center;
        margin: 0 auto;
        background-color:#666633;
        float: left;
        margin-right: 20px;
        height: 58px;
        line-height: 58px;
    }


    *{margin:0px;padding:0px;}
    .tabbox{}
    .tabbox ul{list-style:none;display:table;margin-top: 4px;float:left;}
    .tabbox ul li{width:160px;height:110px;line-height:100px;padding-left:20px;margin-top:-18px;border:1px solid #666666;cursor:pointer;}
    #ngselete  ul  {margin-top:10px;}
    #ngselete  ul  li{width:240px;height:40px;margin-top:-0px;margin-left:-20px;border:0px solid #666666;cursor:pointer;background-color:#FFFFFF;}
    .tabbox ul li.active{background-color:#1F3871;font-weight:bold;border:1px solid #666666;}
    .tabbox .content{}
    .tabbox .content>div{display:none;}
    .tabbox .content>div.active{display:block;margin-top: 10px}


    .mtabbox{}
    .mtabbox ul{list-style:none;display:table;margin-top: 4px}
    .mtabbox ul li{width:160px;height:110px;line-height:100px;padding-left:20px;margin-top:-18px;border:1px solid #666666;cursor:pointer;}
    .mtabbox ul li.active{background-color:#1F3871;font-weight:bold;border:1px solid #666666;}
    .mtabbox .content{}
    .mtabbox .content>div{display:none;}
    .mtabbox .content>div.active{display:block;margin-top: 10px}



    #showStaffTable tr td, #showNGRecordTable tr td,#showMaintainProjectTable tr td, #showSparepartRecordTable tr td {
        text-align: center;
        color: #FFFFFF;
    }

    .table2  th,td,tr{
        border:#666666 1px solid;
        color:#FFFFFF;
        font-size:14px;
        text-align:center;
        line-height:50px;
        height:50px;
        vertical-align: middle;
        background-color: #1C2437;
    }


    .table1 td,th{
        border:#666666 1px solid;
        color:#FFFFFF;
        font-size:14px;
        text-align:center;
        height:50px;
        vertical-align: middle;
        line-height:50px;
    }


    /* bootstrop表格修改 */
    .table > tbody > tr.success > td {
        background-color: #ff6666;
    }
    .table-hover > tbody > tr > td.success:hover,
    .table-hover > tbody > tr > th.success:hover,
    .table-hover > tbody > tr.success:hover > td,
    .table-hover > tbody > tr:hover > .success,
    .table-hover > tbody > tr.success:hover > th{
        background-color: #ff6633;
    }

    .fixed-table-container tbody .selected td {
        background-color: #ff6666;
    }

    .table > tbody > tr > td,
    .table > tbody > tr > th,
    .table > tfoot > tr > td,
    .table > tfoot > tr > th,
    .table > thead > tr > td,
    .table > thead > tr > th{
        vertical-align:middle;
    }


    .fixed-table-container thead th .th-inner{
        line-height: 50px;
        padding: 0;
    }



</style>
</head>
<body>
<input type="hidden" name="deviceCode" id="deviceCode" />
<div id="outer">
    <div id="loginInfo">
			<span style="margin-right:10px;">
			<%
                if (employee != null) {
            %>
			<%=employee.getCode()%>&nbsp;<%=employee.getName()%>
			<%
                }
            %>
			<%--</span><a href="javascript:void(0);" class="fa fa-sign-out"
                      onClick="logout()"></a>--%>
    </div>
    <div id="title">
        <span style="color:red;font-style: italic;font-size:50px;text-decoration: none;">DIMES
            </span>
        <span style="font-size:35px;margin-left: 20px;">智慧工厂无纸化系统</span>
    </div>
    <div id="time"></div>
    <div id="productionUnit">
        生产单元:<a
            href="javascript:void(0);" onClick="showupdateProduction()">
				<span id="paperProductionUnitName">
					<% if (productionLine != null) {

                    %>
							<%=productionLine.getName()%>
							<%
                            }else{
                            %>
								未选择
							<%
                                }
                            %>
				</span>
    </a>
        <input type="hidden" name="paperProductionUnitCode" id="paperProductionUnitCode"/>
        <input type="hidden" name="paperProductionUnitId" id="paperProductionUnitId"/>
    </div>
    <%--<div id="department">部门:<%
        if(employee!=null){
            if(employee.getPosition()!=null){
                if(employee.getPosition().getDepartment()!=null){
    %>
        <%=employee.getPosition().getDepartment().getName() %>
        <%
                    }
                }
            }
        %></div>--%>
    <div id="main">
        <div id="left">
                <div class="panel-group" id="accordion"><!--大容器-->
                    <div class="panel panel-default"><!--这个表示第一个整块儿的-->
                        <%--<div style="height:90px;background-color:#339999;width:100%;cursor:pointer;
					        color:white;text-align: center;line-height:90px;font-size:20px;" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">--%>
                        <div style="height:90px;background-color:#339999;width:100%;cursor:pointer;
					        color:white;text-align: center;line-height:90px;font-size:20px;" data-toggle="collapse" data-parent="#accordion" onclick="openOne()">
                            <span class="fa fa-file-archive-o" >&nbsp;&nbsp;<span style="font-family: '微软雅黑';" href="#collapseOne">工件文档</span></span>
                        </div>
                        <div class="panel-collapse collapse in"  id="collapseOne"><!--第二部分，折叠部分-->
                            <div class="panel-body" style="background-color:#1C2437;height: 575px;">
                                <div class="leftDiv" style="margin-top: 0px;">
                                    <input type="text" id="code" name="code" style="width:100%;height:100%;font-size:20px;padding-left:3px;
					                color:white;background-color:#49505F;border:#666666 1px solid"/>
                                </div>
                                <div class="leftDiv">
                                    <label style="color: #57EEFD;font-size:16px;margin-left:5px;margin-bottom: 15px;
					display: block;">查询条件</label>
                                    <input id="codeType" data-toggle="topjui-combobox" style="width:266.8px;height: 58px;" name="codeType"
                                           data-options="valueField:'code',textField:'text',data:[{
                                        code:'',
                                        text:'',
                                        selected:true
                                    },{
                                        code:'workpiece',
                                        text:'工件代码'
                                    },{
                                        code:'graphNumber',
                                        text:'工程图号'
                                    }]" style="height:60px;
                                font-size:50px;padding-left:3px;border:#666666 1px solid" />
                                </div>
                                <div class="leftDiv" style="margin-top:60px;">
                                    <label style="color: #57EEFD;font-size:16px;margin-left:5px;margin-bottom: 15px;
					display: block;">文档类别</label>
                                    <input id="docType" data-toggle="topjui-combobox" name="docType" style="width:266.8px;height: 58px;"
                                           data-options="valueField:'id',textField:'name',url:'paperlessWorkpieceDoc/queryAllWorkpieceDocTypes.do'" style="height:60px;
					font-size:50px;padding-left:3px;border:#666666 1px solid">
                                </div>
                                <div class="leftDiv" style="margin-top:100px;background-color:#339999;color:white;
				line-height:60px;font-size:18px;border-radius: 12px;text-align: center;cursor: pointer;" onClick="searchWorkpieceDocs()">
                                    <span style="margin-top: 3px;margin-right:5px;font-size:22px;">确&nbsp;定</span><span class="fa fa-check-square-o"></span>
                                </div>

                                <div class="leftDiv" style="margin-top:20px;background-color:#6633FF;color:white;
				line-height:60px;font-size:18px;border-radius: 12px;text-align: center;cursor: pointer;" onclick="dimesDo()">
                                    <span style="margin-top: 3px;margin-right:5px;font-size:22px;">DiMES在制图号</span>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="panel panel-default"><!--这个表示第一个整块儿的-->
                        <%--<div style="height:90px;background-color:#339999;width:100%;cursor:pointer;
					        color:white;text-align: center;line-height:90px;font-size:20px;" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">--%>
                        <div style="height:90px;background-color:#339999;width:100%;cursor:pointer;
					        color:white;text-align: center;line-height:90px;font-size:20px;" data-toggle="collapse" data-parent="#accordion" onclick="openTwo()">
                            <span class="fa fa-file-archive-o">&nbsp;&nbsp;<span style="font-family: '微软雅黑';" href="#collapseTwo">产线信息</span></span>
                        </div>
                        <div class="panel-collapse collapse" id="collapseTwo"><!--第二部分，折叠部分-->
                            <div class="panel-body" style="background-color:#1C2437;height: 575px;">
                                <div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#0B2D72;margin-top: 10px;cursor:pointer;"
                                     onClick="$('#content_productionLine').attr('src','paperless/productionLineDocumentation.jsp')">图文档资料</div>

                                <%--<div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#0B2D72;margin-top: 30px;">产线报表</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
                                     onClick="$('#content_productionLine').attr('src','paperless/productionReport.jsp')" id="deviceCheckDiv">安全绿十字</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
                                     onClick="chooseDevice()">设备日产量</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
                                     onClick="$('#content_productionLine').attr('src','')" id="deviceCheckDiv">设备OEE趋势</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
                                     onClick="$('#content_productionLine').attr('src','')" id="deviceMaintainDiv">设备停机时间</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#112A5E;cursor:pointer;"
                                     onClick="$('#content_productionLine').attr('src','')" id="deviceMaintainDiv">不合格记录</div>--%>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default"><!--这个表示第一个整块儿的-->
                        <div style="height:90px;background-color:#339999;width:100%;cursor:pointer;
					        color:white;text-align: center;line-height:90px;font-size:20px;" data-toggle="collapse" data-parent="#accordion" onclick="openThree()">
                            <span class="fa fa-file-archive-o">&nbsp;&nbsp;<span style="font-family: '微软雅黑';" href="#collapseThree">设备资料</span></span>
                        </div>
                        <div class="panel-collapse collapse" id="collapseThree"><!--第二部分，折叠部分-->
                            <div class="panel-body" style="background-color:#1C2437;height: 575px;">
                                <div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#484B35;margin-top: 10px;cursor:pointer;"
                                     onClick="$('#content').attr('src','paperless/deviceMessage.jsp')">设备信息</div>
                                <div class="tp_button" style="height: 95px;width: 100%;line-height: 95px;background-color:#484B35;margin-top: 30px;">设备管理</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000"
                                     onClick="$('#content').attr('src','paperless/deviceCheckPlanRecord.jsp')" id="deviceCheckDiv">设备点检记录</div>
                                <%--<div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000"
                                     onClick="chooseDevice()">设备报修</div>--%>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;border-bottom:1px solid #000"
                                     onClick="$('#content').attr('src','paperless/deviceService.jsp')">设备维修记录</div>
                                <div class="tp_button" style="height: 60px;width: 100%;line-height: 60px;background-color:#3A3F35;cursor:pointer;"
                                     onClick="$('#content').attr('src','paperless/deviceMaintain.jsp')" id="deviceMaintainDiv">设备保养记录</div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        <div id="right">
            <div id="top" style="height:39%;width:100%;background-color:#1C2437;margin-bottom:10px;margin-top:0px;
				padding-top:5px;overflow: auto;">
                <table style="width:98%;margin-left:auto;margin-right:auto;">
                    <thead>
                    <tr>
                        <th style="width: 50px;"></th>
                        <th>文件名称</th>
                        <th>源文件名称</th>
                        <th>说明</th>
                        <th>文档类型</th>
                        <th>上传时间</th>
                        <th style="width: 171px;"></th>
                    </tr>
                    </thead>
                    <tbody id="docContent">
                    </tbody>
                </table>
            </div>
            <div id="bottom" style="height:60%;width:100%;background-color:#1C2437;">
                <div style="text-align: right;height:5%;width:100%;line-height:5%;">
						<span style="color:white;margin-top:5px;margin-right:10px;height:100%;cursor:pointer;"
                              class="fa fa-window-maximize" onClick="showFullScreen()"> 全屏查看</span>
                </div>
                <div style="width:100%;height:95%;text-align: center;" id="previewDiv">
                </div>
            </div>
        </div>

        <div class="m_right" id="productionInfo_right" style="display: none;">
            <iframe style="height:100%;width:100%;border-width: 0px;
				background-color:#1C2437;backgroun-color:#1C2437;"
                    id="content_productionLine"  src="paperless/productionLineDocumentation.jsp"></iframe>
        </div>

        <div class="m_right" id="deviceAbout_right" style="display: none;">
            <div style="height: 58px;margin:15px 0 0 20px;" id="devicesDiv">
            </div>
            <iframe style="height:750px;width:1450px;border-width: 0px;
				margin-left:10px;background-color:#1C2437;margin-top:10px;backgroun-color:#1C2437;"
                    id="content"  src="paperless/deviceCheckPlanRecord.jsp"></iframe>
        </div>



    </div>
</div>
<div class="modal fade" id="updateProduction" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog" style="width: 850px;color: white;">
        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
            <div class="modal-header">
                <div
                        style="height:30px; width: 600px; overflow: hidden;">
                    <span style="font-size: 20px;">生产单元</span>
                </div>
            </div>
            <div class="modal-body">
                <div id="tree" style="width: 100%; height: 600px; background-color: #1C2437; padding: 20px;overflow-y:auto;"></div>
            </div>
            <div class="modal-footer" style="margin-top: 20px;">
                <div style="float: right; margin-right: 50px;width: 400px;">
						 <span class='btn btn-default' style="margin-right: 20px;"
                               onclick="update()">确认</span>
                    <span  class='btn btn-default'
                           onclick="updateProductionHide()">取消</span>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 全屏预览 -->
<div id="dialog-layer">
    <div style="font-size:50px;color:#ff0000;z-index:2000;position:fixed;top:0px;left:1870px;cursor:pointer;"
         onClick="$('#dialog-layer').css('display','none')" onmouseenter="$(this).css('font-size','55px')"
         onmouseout="$(this).css('font-size','50px')" >×</div>
    <div style="height:100%;width:100%;" id="fullScreenDiv">

    </div>
</div>


<div class="modal fade" id="deviceCheckDialog" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog" style="width: 1500px;height:900px;">
        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
            <div class="modal-header">
                <h4 class="modal-title" style="color:white;">设备点检</h4>
            </div>
            <div class="modal-body" style="height: 750px">
                <div style="background-color:#1C2437;" >
                    <form id="deviceCheckForm">
                        <div title="设备点检" data-options="iconCls:'fa fa-th'">
                            <div class="topjui-fluid">
                                <div style="height: 20px;"></div>
                                <div class="topjui-row">
                                    <div class="topjui-col-sm4">
                                        <label class="topjui-form-label" style="text-align: center;color:white;">设备代码</label>
                                        <div class="topjui-input-block">
                                            <input type="text" name="check_deviceCode" class="deviceCheckInput"
                                                   id="check_deviceCode" readonly="readonly">
                                            <!-- 点检记录id -->
                                            <input type="hidden" name="id" id="id" />
                                        </div>
                                    </div>
                                    <div class="topjui-col-sm4">
                                        <label class="topjui-form-label" style="text-align: center;color:white;">设备名称</label>
                                        <div class="topjui-input-block">
                                            <input type="text" name="check_deviceName"  class="deviceCheckInput"
                                                   readonly="readonly" id="check_deviceName">
                                        </div>
                                    </div>
                                    <div class="topjui-col-sm4">
                                        <label class="topjui-form-label" style="text-align: center;color:white;">规格型号</label>
                                        <div class="topjui-input-block">
                                            <input type="text" name="check_unitType"  class="deviceCheckInput"
                                                   readonly="readonly" id="check_unitType">
                                        </div>
                                    </div>
                                </div>
                                <div class="topjui-row">
                                    <div class="topjui-col-sm4">
                                        <label class="topjui-form-label" style="text-align: center;color:white;">点检日期</label>
                                        <div class="topjui-input-block">
                                            <input type="text" name="checkDate"  id="checkDate" class="deviceCheckInput" data-date-format="yyyy-mm-dd hh:ii">
                                        </div>
                                    </div>
                                    <div class="topjui-col-sm4">
                                        <label class="topjui-form-label" style="text-align: center;color:white;">点检人员</label>
                                        <div class="topjui-input-block">
                                            <input type="text" name="checkUsername" class="deviceCheckInput"
                                                   readonly="readonly" id="checkUsername">
                                            <input type="hidden" id="checkUsercode" name="checkUsercode" />
                                        </div>
                                    </div>
                                </div>
                                <div class="topjui-row">
                                    <div class="topjui-col-sm5">
                                        <div id="docs" style="border-style: dotted; border-radius: 15px;height:600px;overflow: hidden;
										border-color: #4F5157;">
                                            <div class="fa fa-chevron-left imgDirection" id="showPrevious"></div>
                                            <div style="height:100%;width:90%;float:left;">
                                                <img id="deviceCheckImg" alt="设备点检图片" style="width:98%;height:94%;margin-top:20px;"/>
                                            </div>
                                            <div class="fa fa-chevron-right imgDirection" id="showNext"></div>
                                        </div>
                                    </div>
                                    <div class="topjui-col-sm7">
                                        <div id="items"  style="border-color: #4F5157;border-style: dotted; border-radius: 10px;height:600px;
										margin-left:20px;">
                                            <table id="checkingPlanRecordItemTb" border="1" style="width:98%;margin:5px auto;">
                                                <thead>
                                                <tr>
                                                    <td style="width: 60px"></td>
                                                    <td>项目代码</td>
                                                    <td>点检项目</td>
                                                    <td>标准</td>
                                                    <td>方法</td>
                                                    <td>频次</td>
                                                    <td style="width:60px;">结果</td>
                                                    <td>备注</td>
                                                </tr>
                                                </thead>
                                                <tbody id="checkingPlanRecordItem"></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="topjui-row">
                                    <div class="topjui-col-sm5">
                                        <div style="height:30px;">
                                        </div>
                                    </div>
                                    <div class="topjui-col-sm7">
                                        <div  style="height:30px;color:red;font-size:20px;">
                                            在设备运行和进行设备清洁时有任何异常情况马上报告给主管或维护人员
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="margin-top: 20px;">
                <div style="float: right; margin-right: 50px;width: 400px;">
					 <%--<span class='btn btn-default' style="margin-right: 20px;"
                           onclick="saveCheckPlanRecordItem()" id="saveBtn">保存</span>--%>
                    <span  class='btn btn-default'
                           onclick="$('#deviceCheckDialog').modal('hide')" id="cancelBtn">取消</span>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


<!-- 维修界面 -->
<div class="modal fade" id="updateDeviceRepairOrderDialog" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" >
    <div class="modal-dialog" style="width: 1500px;height:900px;color: white;">
        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
            <div class="modal-header">
                <h4 class="modal-title">编辑设备报修单</h4>
            </div>
            <div class="modal-body" style="height: 750px">
                <form id="pressLightRecordForm" class="form-horizontal">
                    <div class="form-group" style="margin-top: 20px;">
                        <div style="float: left;margin-left: 50px">
                            <div class="labelstyle" style="float: left;">设备代码</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateDeviceCode" id="updateDeviceCode" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                                <input type="hidden" name="orderId" id="orderId" />
                                <input type="hidden" name="updateDeviceId" id="updateDeviceId" />
                                <input type="hidden" name="updateStatus" id="updateStatus" />
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">设备名称</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateDeviceName" id="updateDeviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">规格型号</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateUnitType" id="updateUnitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div style="float: left;margin-left: 50px">
                            <div class="labelstyle" style="float: left;">报修时间</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateCreateDate" class="form-control"
                                       id="updateCreateDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">设备类别</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateDeviceType" class="form-control"
                                       id="updateDeviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">报修人员</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="updateInformant" class="form-control"
                                       id="updateInformant" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
                            </div>
                        </div>
                    </div>
                    <div id="updatePhoto">
                        <div class="tabbox" style="width: 1460px;height: 541px">
                            　　<ul style="color: white;margin-top: 1px;">
                            　　　　<li class="active">报修内容</li>
                            　　　　<li>维修人员</li>
                            　　　　<li>故障原因</li>
                            　　　　<li>维修项目</li>
                            　　　　<li>备件信息</li>
                            　　</ul>

                            <div class="content" style="float: left;width: 1300px;height: 550px;">
                                　　<div class="active">
                                　　　　<div style="margin-left: 20px;">
                                <div class="form-group">
                                    <div style="margin: 20px 0 0 10px;">
                                        <div class="labelstyle" style="float: left;">故障描述</div>
                                        <div style="float: left;margin-left: 10px">
	                                                            <textarea   name="updateNGDescription" id="updateNGDescription" rows="6" cols="150"
                                                                            style="resize:none;background-color: #1C2437;border: 1px #666666 solid;color: white;" ></textarea>
                                        </div>
                                    </div>
                                </div>
                                <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic">
                                    <!-- datagrid表格 -->
                                    <div class="updatePicDiv" id="updatePicDiv" style="margin-top: 50px;float: left;">
                                    </div>
                                </div>
                            </div>
                                　　</div>
                                　　<div style="margin-left: 20px;">
                                <div class="StaffTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showStaffTable' class="table2"></table>

                                    <div onclick="removeRecord('staff')" id="removeMaintenanceStaffView" style="float: right;"
                                         class="container-fluid updateButton" >
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('staff')" id="addMaintenanceStaffView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　<div style="margin-left: 20px;">
                                <div class="NGRecordTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showNGRecordTable' class="table2"></table>


                                    <div onclick="removeRecord('ngRecord')" id="removeNGMaintainView" style="float: right;margin-right: 30px"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('ngRecord')" id="addNGMaintainView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　<div style="margin-left: 20px;">
                                <div class="MaintainProjectTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showMaintainProjectTable' class="table2"></table>


                                    <div onclick="removeRecord('maintainProject')" id="removeMaintainProjectView" style="float: right;margin-right: 30px"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('maintainProject')" id="addMaintainProjectView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　<div style="margin-left: 20px;">
                                <div class="SparepartRecordTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showSparepartRecordTable' class="table2"></table>


                                    <div onclick="removeRecord('sparepartRecord')" id="removeSparepartView" style="float: right;margin-right: 30px"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('sparepartRecord')" id="addSparepartView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　</div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="margin-top: 20px;">
                <div style="float: left; margin-right: 50px;">
	                    <span  class='btn btn-default' id="repairComplete"
                               onclick="showConfirm('repairComplete')">维修完成</span>
                </div>
                <div style="float: right; margin-right: 50px;width: 400px;">
	                    <span  class='btn btn-default' id="updateDeviceRepair"
                               onclick="updateDeviceRepair()">更新</span>
                    <span  class='btn btn-default'
                           onclick="modelHide()">关闭</span>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 维修界面 -->


<!-- 保养界面 -->
<div class="modal fade" id="maintenanceOrderDialog" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog" style="width: 1500px;height:900px;color: white;">
        <div class="modal-content" style="background-color:#1C2437;width:100%;height:100%;">
            <div class="modal-header">
                <h4 class="modal-title">设备保养</h4>
            </div>
            <div class="modal-body" style="height: 750px">
                <form id="pressLightRecordForm" class="form-horizontal">
                    <div class="form-group" style="margin-top: 20px;">
                        <div style="float: left;margin-left: 50px">
                            <div class="labelstyle" style="float: left;">设备代码</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceDeviceCode" id="maintenanceDeviceCode" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                                <input type="hidden" name="maintenanceId" id="maintenanceId" />
                                <input type="hidden" name="updateDeviceId" id="updateDeviceId" />
                                <input type="hidden" name="maintenanceConfirmCode" id="maintenanceConfirmCode" />
                                <input type="hidden" name="maintenanceStatus" id="maintenanceStatus" />
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">设备名称</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceDeviceName" id="maintenanceDeviceName" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">规格型号</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceUnitType" id="maintenanceUnitType" class="form-control" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div style="float: left;margin-left: 50px">
                            <div class="labelstyle" style="float: left;">保养时间</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceCreateDate" class="form-control"
                                       id="maintenanceCreateDate" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;">
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">保养类别</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceDeviceType" class="form-control"
                                       id="maintenanceDeviceType" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
                            </div>
                        </div>
                        <div style="float: left;margin-left: 20px">
                            <div class="labelstyle" style="float: left;">保养人员</div>
                            <div style="float: left;margin-left: 10px">
                                <input type="text" name="maintenanceConfirmName" class="form-control"
                                       id="maintenanceConfirmName" style="width: 230px;height:40px;background-color: #1C2437;border: 1px #666666 solid;color: white;" readonly="readonly">
                            </div>
                        </div>
                    </div>
                    <div id="maintenancePhoto">
                        <div class="mtabbox" style="width: 1460px;height: 541px">
                            　　<ul style="float: left;margin-top: 1px;">
                            　　　　<li class="active">设备分布图</li>
                            　　　　<li>保养人员</li>
                            　　　　<li>保养项目</li>
                            　　　　<li>备件信息</li>
                            　　</ul>

                            <div class="content" style="float: left;width: 1300px;height: 550px;">
                                　　<div class="active">
                                　　　　<div style="margin-left: 20px;">
                                <div title="现场图片" data-options="id:'tab0',iconCls:'fa fa-th'" id="pic" style="margin-top: 20px">
                                    <!-- datagrid表格 -->
                                    <div class="updatePicDiv" id="MaintenancePicDiv" style="margin-top: 20px;float: left;">
                                    </div>
                                </div>
                            </div>
                                　　</div>
                                　　<div style="margin-left: 20px;">
                                <div class="maintenanceStaffTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showMaintenanceStaffTable'></table>

                                    <div onclick="removeRecord('maintenanceStaff')" id="removeStaffMaintenanceView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('maintenanceStaff')" id="addStaffMaintenanceView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　<div style="margin-left: 20px;">
                                <div class="maintenanceProjectTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showMaintenanceProjectTable'></table>
                                </div>

                                <!-- <div onclick="removeRecord('maintenanceProject')" id="removeMaintenanceProjectView" style="float: right;margin-right: -131px"
                                        class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-save" aria-hidden="true"
                                            style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">保存</h4>
                                    </div> -->
                                <!-- <div onclick="addRecord('maintenanceProject')" id="addMaintenanceProjectView" style="float: right;margin-right: -31px"
                                    class="container-fluid updateButton">
                                    <h6></h6>
                                    <span class="fa fa-plus-circle" aria-hidden="true"
                                        style="font-size: 30px; margin-top: -5px"></span>
                                    <h4 style="text-align: center">新增</h4>
                                </div> -->

                            </div>
                                　　<div style="margin-left: 20px;">
                                <div class="maintenanceSparepartRecordTable" style="width: 1200px;margin-top: 0px">
                                    <table id='showMaintenanceSparepartRecordTable'></table>


                                    <div onclick="removeRecord('maintenanceSparepart')" id="removeMaintenanceSparepartView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-times-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">删除</h4>
                                    </div>
                                    <div onclick="addRecord('maintenanceSparepart')" id="addMaintenanceSparepartView" style="float: right;"
                                         class="container-fluid updateButton">
                                        <h6></h6>
                                        <span class="fa fa-plus-circle" aria-hidden="true"
                                              style="font-size: 30px; margin-top: -5px"></span>
                                        <h4 style="text-align: center">新增</h4>
                                    </div>
                                </div>
                            </div>
                                　　</div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="margin-top: 20px;">
                <div style="float: right; margin-right: 50px;width: 400px;">
						<span  class='btn btn-default' id="updateDeviceMaintenance"
                               onclick="updateDeviceMaintenance()">保养完成</span>
                    <span  class='btn btn-default'
                           onclick="modelHide()">关闭</span>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 保养界面 -->