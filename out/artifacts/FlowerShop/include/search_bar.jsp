<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="childNav">
	<div class="wrap">
		<ul class="clearfix">
			<li class="first"><a href="javascript:searchHot('花花公子')">花花公子</a></li>
			<li><a href="javascript:searchHot('南极人')">南极人</a></li>
			<li><a href="javascript:searchHot('李宁')">李宁</a></li>
			<li><a href="javascript:searchHot('阿迪达斯')">阿迪达斯</a></li>
			<li><a href="javascript:searchHot('啄木鸟')">啄木鸟</a></li>
			<li><a href="javascript:searchHot('特步')">特步</a></li>
			<li><a href="javascript:searchHot('染牌')">染牌</a></li>
			<li><a href="javascript:searchHot('鸿星尔克')">鸿星尔克</a></li>
			<li><a href="javascript:searchHot('连衣裙')">连衣裙</a></li>
			<li><a href="javascript:searchHot('短裙')">短裙</a></li>
			<li class="last"><input type="text" id="selectname" value="${search_words }" /><a href="javascript:selectname()" >&nbsp;&nbsp;搜索</a></li>
		</ul>
	</div>
</div>