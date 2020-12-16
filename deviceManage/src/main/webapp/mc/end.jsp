 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style type="text/css">

</style>
 <div class="container-fluid end" style="">
    <div class="end_bar">
	    <span class="fa fa-home" aria-hidden="true" style="font-size: 45px;width:80px;cursor:pointer;color: white;" onclick="home()"></span>
	    <span class="fa fa-arrows-alt" id="fullscreen" aria-hidden="true" style="font-size: 45px;color: white;width:80px;cursor:pointer;
	    margin-left: 150px"></span>
    </div>
</div>

<script>
//回到首页
function home(){
	window.location.href=contextPath + "mc/main.jsp";
}
	 //控制全屏
    function enterfullscreen() {//进入全屏
        var docElm = document.documentElement;
//W3C
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
        }
//FireFox
        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
        }
//Chrome等
        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
        }
//IE11
        else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    }
    function exitfullscreen() { //退出全屏
        if (document.exitFullscreen) {
            document.exitFullscreen();
        }
        else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        }
        else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        }
        else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }

    var a=0;
    $('#fullscreen').on('click',function () {
        a++;
        a%2==1?enterfullscreen():exitfullscreen();
    })
</script>

</body>
</html>