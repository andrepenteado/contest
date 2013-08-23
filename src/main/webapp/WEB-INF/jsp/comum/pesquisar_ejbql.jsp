<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Coloque sua consulta EJB-QL" />
  <title>Página para testes de consultas EJBQL</title>
</head>
<body>
<form name="form_ejbql" method="POST" action="<%=request.getContextPath()%>/public.pesquisarEJBQL.action">
<table cellspacing="0" cellpadding="0" class="msgbox">
  <tr>
    <td>
      <mtw:textarea cols="60" rows="10" name="txt_ejbql"/><br>
      <input type="submit" value="Enviar">
    </td>
  </tr>
</table>
</form>
<pre>
<%
ArrayList<String> result = (ArrayList<String>)request.getAttribute("result");
for (int i = 0; i < result.size(); i++) {
    String str = result.get(i);
    out.print(str);
    out.println("<br>");
} %>
</pre>
</body>
</html>