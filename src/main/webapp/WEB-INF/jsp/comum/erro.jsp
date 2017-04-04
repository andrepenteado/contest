<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page isErrorPage="true"%>
<%@page import="org.apache.log4j.Logger"%>
<%
Logger log = Logger.getLogger("com.github.andrepenteado.web.contest.JSP");
log.fatal("JSP ERROR", exception);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.github.andrepenteado.util.ConfigHelper"%><html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="<%=ConfigHelper.get().getString("error.descriptionGeneral")%>">
  <title><%=ConfigHelper.get().getString("error.general")%></title>
</head>
<body>
<table cellspacing="0" cellpadding="0" class="form">
  <tr>
    <td>
      <p align="left">Obrigado. </p>
      <p align="left">Equipe <%=ConfigHelper.get().getString("sistema.nome")%>. </p>
    </td>
  </tr>
</table>
</body>
</html>