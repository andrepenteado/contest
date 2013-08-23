<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="${mensagem}">
  <title>${titulo}</title>
</head>
<body>
<center>
<table cellspacing="0" cellpadding="0" class="form">
  <tr>
    <td>
      <center>
      <input name="btn_voltar" type="button" value="OK" class="button" onClick="window.location.href='${url}';">
      </center>
    </td>
  </tr>
  <tr>
    <td>
      <br>
    </td>
  </tr>
</table>
</center>
</body>
</html>