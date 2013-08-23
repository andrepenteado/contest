<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
  <%@include file="../../template/head.jsp"%>
  <link rel="stylesheet" type="text/css" href="jquery/ui/-ui-1.8.20.custom.css" />
  <link rel="stylesheet" type="text/css" media="screen,projection" href="../skins/multiflex2/css/style_screen.css" />
</head>
<body>
  <div class="page-container-2">
    <!-- header--->
    <!-- Sitename -->
    <div class="site-name">
      <p class="title"><%=ConfigHelper.get().getString("sistema.nome")%></p>
      <p class="subtitle"><%=ConfigHelper.get().getString("sistema.descricao")%></p>
    </div>
    <!-- Site slogan -->
    <div class="site-slogan-container">
      <div class="site-slogan">
        <p class="title"><%=ConfigHelper.get().getString("sistema.nome")%></p>
        <p class="subtitle"></p>
        <p class="text"><%=ConfigHelper.get().getString("sistema.descricao")%></p>
      </div>
    </div>
    <!-- header-banner -->
    <div>
      <img class="img-header" src="../skins/multiflex2/img/header.jpg" alt="" />
    </div>
    <!-- Navigation Level 2 -->
    <!-- <div class="nav2">
      <ul>
        <li><a href="http://www.free-css.com/">Home</a></li>
        <li><a href="layout2.html" class="selected">Layout 2</a></li>
        <li><a href="layout3.html">Layout 3</a></li>
        <li><a href="options_basic.html">Basic Options</a></li>
        <li><a href="options_extra.html">Extra Options</a></li>
      </ul>
    </div> -->
    <!-- NAVIGATION -->
    <!-- Navigation Level 3 -->
    <div class="nav3">
      <p><%@include file="../../template/menu.jsp"%></p>
    </div>
    <!-- 	CONTENT -->
    <div class="content2">
      <%@include file="../../template/body.jsp"%>
      <br><br>
    </div>
    <!-- FOOTER -->
    <div class="footer">
      <p><%@include file="../../template/footer.jsp"%></p>
    </div>
  </div>
</body>
</html>
