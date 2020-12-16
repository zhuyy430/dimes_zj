<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title></title>
    <style type="text/css">
        .layui-input{
            height:30px;
        }
        .list{
            width: 250px;height: 100%;float: left;margin-right: 15px;

        }
        .opacity{
            filter:alpha(opacity=15);
            -moz-opacity:0.15;
            opacity:0.15;
            z-index: 2;
			
			
        }
		.module{
			width: 100%;position:relative;margin-top: 10px;
		}
		
		.moduleOpacity{
			height: 100%;width: 100%;background-color: #FFFFFF
		}
		.moduleText{
			width: 100%;z-index: 20;position: absolute;bottom: 0;line-height: 48px;color:#FEFFFF;font-size: 16px;
		}
    </style>
</head>
<body class="layui-layout-body" >
<div style='width: 100%;height: 85px;background-color: #0F0E3D'>
    <p style="line-height: 65px;color: #FFFFFF;font-size: 34px;width: 400px;height:65px;display: inline-block;margin-left: 100px;border-left: 2px solid #808096;padding-left: 40px;margin-top: 10px;">设备数据可视化</p>
</div>

<div style="width: 100%;height: 995px;background-color: #0F0E3D;overflow:auto;" id="main" >

</div>
</body>

<script type="text/javascript">
	$(function(){
        console.log(contextPath + "device/deviceDetailVisualization.do");
		$.get(contextPath + "device/deviceDetailVisualization.do",function(result){
			$.each(result,function(index, data) {

				var html="<div class=\"list\"><div style=\"height:80px;width: 100%;position:relative;\"><div class=\"opacity moduleOpacity\"></div><div style=\"height: 12px;width: 100%;background-color: #ECC74E;z-index: 20;position: absolute;top: 0\"></div><div style=\"width: 100%;z-index: 10;position: absolute;top: 30px\"><span style=\"color:#FEFFFF;font-size: 25px;margin-left: 20px;\">"+data[index].name+"</span></div></div>"+

                    "<div style=\"height:350px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><img src=\"front/images/17.png\" style=\"width:100%;height: 80%;top: 0;z-index: 20;position: absolute;\"><div style=\"height: 6px;width: 100%;background-color: #ECC74E;z-index: 20;position: absolute;bottom: 45px;\"></div><div class=\"moduleText\" style=\"height: 48px;\"><span style=\"margin-left: 20px;\">工件编号</span><span style=\"float: right;margin-right: 20px;\">"+data[index].name+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">加工状态</span><span style=\"float: right;margin-right: 20px;\">"+data[index].v0+"</span></div></div>"+

					"<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">急停</span><span style=\"float: right;margin-right: 20px;\">"+data.v1+"</span></div></div>"+

					"<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">进给速度</span><span style=\"float: right;margin-right: 20px;\">"+data.v2+"</span></div></div>"+

					"<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">进给倍率</span><span style=\"float: right;margin-right: 20px;\">"+data.v3+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">主轴转速</span><span style=\"float: right;margin-right: 20px;\">"+data.v4+"</span></div></div>"+

					"<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">主轴倍率</span><span style=\"float: right;margin-right: 20px;\">"+data.v5+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">有效轴数</span><span style=\"float: right;margin-right: 20px;\">"+data.v6+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">主轴负载</span><span style=\"float: right;margin-right: 20px;\">"+data.v7+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">工作模式</span><span style=\"float: right;margin-right: 20px;\">"+data.v8+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">切削时间</span><span style=\"float: right;margin-right: 20px;\">"+data.v9+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">上电时间</span><span style=\"float: right;margin-right: 20px;\">"+data.v10+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">运行时间</span><span style=\"float: right;margin-right: 20px;\">"+data.v11+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">单次加工数</span><span style=\"float: right;margin-right: 20px;\">"+data.v12+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">累计加工数</span><span style=\"float: right;margin-right: 20px;\">"+data.v13+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">当前程序加工时间</span><span style=\"float: right;margin-right: 20px;\">"+data.v14+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">报警状态</span><span style=\"float: right;margin-right: 20px;\">"+data.v15+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">报警号</span><span style=\"float: right;margin-right: 20px;\">"+data.v16+"</span></div></div>"+

                    "<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">报警信息</span><span style=\"float: right;margin-right: 20px;\">"+data.v17+"</span></div></div>"+

					"<div style=\"height:48px;\" class=\"module\"><div class=\"opacity moduleOpacity\"></div><div class=\"moduleText\"><span style=\"margin-left: 20px;\">报警时长</span><span style=\"float: right;margin-right: 20px;\">"+data.v18+"</span></div></div></div>"
					$("#main").append(html);
			})
		})
	})
</script>

</html>