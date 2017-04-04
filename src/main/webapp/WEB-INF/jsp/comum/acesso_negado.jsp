<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@page import="com.github.andrepenteado.util.ConfigHelper" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="<%=ConfigHelper.get().getString("error.accessDenied")%>">
  <title>Privilégios Insuficientes</title>
</head>
<body>
<table cellspacing="0" cellpadding="0" class="form" width="100%">
  <tr>
    <td>
      <center><br><br>
      <b><%=ConfigHelper.get().getString("error.accessDenied")%></b>
      <br><br><br>
      <a href= "<%=request.getContextPath()%>/comum.home.action">Clique aqui</a> para fazer seu login
      </center>
    </td>
  </tr>
</table>
</body>
</html>