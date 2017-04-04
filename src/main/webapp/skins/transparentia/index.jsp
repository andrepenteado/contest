<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.github.andrepenteado.util.ConfigHelper"%>

<html>
<head>
  <%@include file="../../template/head.jsp"%>
  <link rel="stylesheet" type="text/css" href="jquery/ui/redmond/jquery-ui-1.8.20.custom.css" />
  <link rel="stylesheet" type="text/css" href="../skins/transparentia/css/default.css" media="screen, projection"/>
</head>
<body>
<div class="container">
  <div class="main">
    <div class="header">
      <div class="title">
        <img src="<%=request.getContextPath()%>/favicon.png" />
        <h1><%=ConfigHelper.get().getString("sistema.nome")%></h1>
      </div>
    </div>
    <div class="content">
      <div class="item">
        <%@include file="../../template/body.jsp"%>
      </div>
    </div>
    <div class="sidenav">
      <%@include file="../../template/menu.jsp"%>
    </div>
    <div class="clearer"><span></span></div>
  </div>
  <div class="footer">
    <%@include file="../../template/footer.jsp"%>
  </div>
</div>
</body>
</html>
