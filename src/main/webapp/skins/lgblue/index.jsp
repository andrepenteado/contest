<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="br.com.alphadev.util.ConfigHelper"%>

<html>
<head>
  <%@include file="../../template/head.jsp"%>
  <link rel="stylesheet" type="text/css" href="jquery/ui/redmond/jquery-ui-1.8.20.custom.css" />
  <style type="text/css" media="all">@import "../skins/lgblue/css/style.css";</style>
</head>
<body>
<div class="content">
  <div id="toph"></div>
  <div id="header">
    <div class="rside">
      <div class="citation">
        <h2><%=ConfigHelper.get().getString("sistema.nome")%></h2>
        <h3><%=ConfigHelper.get().getString("sistema.descricao")%></h3>
      </div>
    </div>
    <div class="lside">
      <div class="title">
        <img src="<%=request.getContextPath()%>/favicon.png" />
        <h3><%=ConfigHelper.get().getString("sistema.nome")%></h3>
      </div>
    </div>
  </div>
  <div id="main">
    <div class="center-main">
      <%@include file="../../template/body.jsp"%>
    </div>
    <div class="leftmenu">
      <div class="nav">
        <%@include file="../../template/menu.jsp"%>
      </div>
    </div>
  </div>
  <br />&nbsp;<br />
  <div id="footer">
    <%@include file="../../template/footer.jsp"%>
  </div>
</div>
</body>
</html>
