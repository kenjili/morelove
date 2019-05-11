<%@ page contentType="text/html; utf-8" pageEncoding="utf-8" %>
<html>
<body>
<h1>微爱接口测试</h1>


<h2>登录接口测试</h2>
<form action="/user/login" method="post">
    <input name="username" value="就业"><br>
    <input name="password" value="wjy123456" type="password"><br>
    <input type="submit" value="登录">
</form>

<br>

<h2>退出登录</h2>
<a href="/user/logout">退出</a>


<br>
<br>

<h2>上传相册</h2>
<form action="/itinerary/public" method="post" enctype="multipart/form-data">
    <input name="title" type="text" value="标题"><br>
    <input name="details" type="text" value="详情"><br>
    <input name="albumName" type="text" value="相册名称"><br>
    <input name="address" type="text" value="位置信息"><br>
    <input name="photos" type="file" multiple="multiple"><br>
    <input type="submit" value="提交" name="submit"><br>
</form>

<br>
<br>

<h2>发表时光</h2>
<form action="/lovetime/public" method="post" enctype="multipart/form-data">
    <input name="isPublic" type="text" value="false"><br>
    <input name="context" type="text" value="内容"><br>
    <input name="images" type="file" multiple="multiple"><br>
    <input type="submit" value="提交" name="submit"><br>
</form>

<br>
<br>

</body>
</html>
