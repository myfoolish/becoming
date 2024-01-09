<%--
  Created by IntelliJ IDEA.
  User: liuxiaowei
  Date: 2020/9/28
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>xxx系统登录页</title>
    <style>
        div{
            width:300px;
            height:100px;
            position: absolute;
            top:50%;
            left:50%;
            margin-top:-50px;
            margin-left:-150px;
        }
    </style>
</head>
<body>
    <div>
        <%-- 和login-processing-url保持一致 --%>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <table>
                <tr>
                    <td>用户名</td>
                    <td>
                        <input type="text" name="username" />
                    </td>
                </tr>
                <tr>
                    <td>密码</td>
                    <td>
                        <input type="password" name="password" />
                        <input type="submit" value="登陆" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
