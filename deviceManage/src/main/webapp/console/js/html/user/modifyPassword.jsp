<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .editTable .label {
        min-width: 80px;
        width: 80px;
    }
</style>
<table class="editTable">
    <tr>
        <td class="label">用户名</td>
        <td><input type="text" id="username" name="username" value='${session.SPRING_SECURITY_CONTEXT.authentication.principal.username}'></td>
    </tr>
    <tr>
        <td class="label">原始密码</td>
        <td><input type="password" id="password" name="password" ></td>
    </tr>
    <tr>
        <td class="label">新密码</td>
        <td><input type="text" id="newpassword" name="newpassword">
        </td>
    </tr>
    <tr>
        <td class="label">重复新密码</td>
        <td><input type="text" id="password2" name="password2"></td>
    </tr>
</table>

<script type="text/javascript">
$.extend($.fn.validatebox.defaults.rules, {
    notEquals: {
		validator: function(value, param){
			return value==param[0];
		},
		message: '新密码不能同原始密码相同!'
    }
});
    $(function () {
        $('#username').iTextbox({
            width: 200,
            required: true
        });
        $('#password').iPasswordbox({
            width: 200,
            required: true
        });
        $('#newpassword').iPasswordbox({
            width: 200,
            required: true
        });
        $('#password2').iPasswordbox({
            width: 200,
            required: true,
            validType: "equals['#newpassword']"
        });
    });
</script>