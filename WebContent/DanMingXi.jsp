
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>千衣千寻-商城 - 首页</title>
    <link type="text/css" rel="stylesheet" href="css/style.css" />
    <script type="text/javascript" src="scripts/function.js"></script>

    <script language="javascript">

        function ScrollImgLeft(){
            var speed=20
            var scroll_begin = document.getElementById("scroll_begin");
            var scroll_end = document.getElementById("scroll_end");
            var scroll_div = document.getElementById("scroll_div");
            scroll_end.innerHTML=scroll_begin.innerHTML
            function Marquee(){
                if(scroll_end.offsetWidth-scroll_div.scrollLeft<=0)
                    scroll_div.scrollLeft-=scroll_begin.offsetWidth
                else
                    scroll_div.scrollLeft++
            }
            var MyMar=setInterval(Marquee,speed)
            scroll_div.onmouseover=function() {clearInterval(MyMar)}
            scroll_div.onmouseout=function() {MyMar=setInterval(Marquee,speed)}
        }
        function selectname(){
            var name = document.getElementById("selectname").value;
            location.href='selectProductList?name='+name;
        }
        function searchHot(name){
            location.href='selectProductList?name='+name;
        }
    </script>


</head>
<body>
<div id="header" class="wrap">
    <div id="logo"><img src="images/logo.gif" /></div>
    <div class="help"><c:if test="${name!=null}"><a href="selectdd?dd=${name.EU_USER_ID }">个人订单</a></c:if><c:if test="${name!=null}"><a href="${pageContext.request.contextPath}/manage/userupdate?msg=zsq&id=${name.EU_USER_ID }">当前用户${name.EU_USER_ID }</a></c:if><a href="ShopSelect" class="shopping">购物车</a><c:if test="${name==null}"><a href="login.jsp">登录</a>|<a href="register.jsp">注册</a></c:if><c:if test="${name!=null}"><a href="zx">退出</a></c:if><a href="SelallServlet">留言</a><c:if test="${name.EU_STATUS==2}"><a href="manage/index.jsp" >去后台</a></c:if></div>
    <div class="navbar">
        <ul class="clearfix">
            <c:choose>
                <c:when test="${empty selected_fid}">
                    <li class="current" ><a href="indexSelect">首页</a></li>
                </c:when>
                <c:otherwise>
                    <li ><a href="indexSelect">首页</a></li>
                </c:otherwise>
            </c:choose>
            <c:forEach var="f" items="${flist}">
                <c:choose>
                    <c:when test="${selected_fid == f.EPC_ID}">
                        <li class="current" ><a href="selectProductList?fid=${f.EPC_ID }">${f.EPC_NAME }</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="selectProductList?fid=${f.EPC_ID }">${f.EPC_NAME }</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
    </div>
</div>
<%@ include file="/include/search_bar.jsp" %>
<div id="main" class="wrap">
    <div class="lefter">
        <div class="box">
            <h2>商品分类</h2>
            <dl>
                <dt><a href="selectProductList">全部商品</a></dt>
                <c:forEach var="f" items="${flist}">
                    <dt><a href="selectProductList?fid=${f.EPC_ID }">${f.EPC_NAME }</a></dt>
                    <c:forEach var="c" items="${clist}">
                        <c:if test="${f.EPC_ID==c.EPC_PARENT_ID}">
                            <c:if test="${p.EPC_CHILD_ID!=c.EPC_ID}">
                                <dd><a href="selectProductList?cid=${c.EPC_ID }">${c.EPC_NAME }</a></dd>
                            </c:if>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </dl>
        </div>
        <div class="spacer"></div>
        <div class="last-view">
            <h2>最近浏览</h2>
            <dl class="clearfix">
                <c:forEach var="lastp" items="${lastlylist}">
                    <dt><a href="selectProductView?id=${lastp.EP_ID }"><img height="40" src="images/product/${lastp.EP_FILE_NAME }" /></a></dt>
                    <dd><a href="selectProductView?id=${lastp.EP_ID }">${lastp.EP_NAME }</a></dd>
                </c:forEach>
            </dl>
        </div>
    </div>


    <div class="main">
        <div class="price-off">
            <h2>订单明细</h2>
            <table class="pure-table pure-table-bordered" border="1px">
                <thead>
                <tr>
                    <th>用户ID</th>
                    <th>用户名</th>
                    <th>收货地址</th>
                    <th>订单时间</th>
                    <th>订单总价</th>
                    <th>订单状态</th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.order}" var="f">
                    <tr>
                        <td>${f.EO_USER_ID}</td>
                        <td>${f.EO_USER_NAME }</td>
                        <td>${f.EO_USER_ADDRESS }</td>
                        <td>${f.EO_CREATE_TIME }</td>
                        <td>${f.EO_COST}</td>
                        <th>${f.orderStatusStr}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>

    </div>
</div>
<div id="footer">
    千衣千寻
</div>
</body>
</html>


