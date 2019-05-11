<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 引用jstl核心标签库 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--是否禁用el表达式-->
<%@ page isELIgnored="false" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>微爱</title>
<style type="text/css">
body {margin: 0px; padding:0px; font-family:"微软雅黑", Arial, "Trebuchet MS", Verdana, Georgia,Baskerville,Palatino,Times; font-size:16px;}
div{margin-left:auto; margin-right:auto;}
a {text-decoration: none; color: #1064A0;}
a:hover {color: #0078D2;}
img { border:none; }
h1,h2,h3,h4 {
	margin:0;
	font-weight:normal; 
	font-family: "微软雅黑", Arial, "Trebuchet MS", Helvetica, Verdana ; 
}
h1{font-size:44px; color:#0188DE; padding:20px 0px 10px 0px;}
h2{color:#0188DE; font-size:16px; padding:10px 0px 40px 0px;}

#page{width:910px; padding:20px 20px 40px 20px; margin-top:80px;}
.button{width:180px; height:28px; margin-left:0px; margin-top:10px; background:#009CFF; border-bottom:4px solid #0188DE; text-align:center;}
.button a{width:180px; height:28px; display:block; font-size:14px; color:#fff; }
.button a:hover{ background:#5BBFFF;}
</style>

</head>
<body>

<div id="page" style="border-style:dashed;border-color:#e4e4e4;line-height:30px;">
	<h1>${title}</h1>
	<font color="#666666">${message}</font><br /><br />
</div>

</body>
</html>
