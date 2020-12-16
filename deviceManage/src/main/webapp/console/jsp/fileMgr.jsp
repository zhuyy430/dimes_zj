<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<style type="text/css">
.myButton {
	box-shadow: 0px 0px 0px 2px #9fb4f2;
	background:linear-gradient(to bottom, #7892c2 5%, #476e9e 100%);
	background-color:#7892c2;
	border-radius:10px;
	border:1px solid #4e6096;
	display:inline-block;
	cursor:pointer;
	color:#ffffff;
	font-family:Arial;
	font-size:19px;
	padding:12px 37px;
	text-decoration:none;
	text-shadow:0px 1px 0px #283966;
	margin-left:45%;
	margin-top:100px
}
.myButton:hover {
	background:linear-gradient(to bottom, #476e9e 5%, #7892c2 100%);
	background-color:#476e9e;
}
.myButton:active {
	position:relative;
	top:1px;
}
</style>
<script>
        window.addEventListener('message', function(e) {
            alert(JSON.stringify(e.data));
            console.log('获取子级B页面返回值:');
            console.log(e.data);
        })
        function toFile(){
            window.open("http://localhost:8080/filemgr/?param1=dimesAdmin&param2=dimes-Admin-");
        }
    </script>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <!--<iframe src="http://localhost:8080/filemgr/" frameborder="0" width="100%" height="100%" scrolling=""></iframe>-->
            <button  class="myButton" onclick="toFile()">文档管控</button>
		</div>
	</div>
	</div>
</body>
</html>


