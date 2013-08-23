<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action"%>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Entre com seu login e senha para acesso ao sistema">
  <title>Login</title>
</head>
<body onload="javascript:document.formLogin.txt_usuario.focus();">
  <center>
  
  <form name="formLogin" method="post" action="<%=request.getContextPath()%>/login.action">
  <table cellspacing="0" cellpadding="0" class="form">
    <tr>
      <td width="40%" class="label">Usu&aacute;rio</td>
      <td width="50%"><input name="txt_usuario" type="text" size="40">
      </td>
    </tr>
    <tr>
      <td width="40%" class="label">Senha</td>
      <td width="60%"><input name="txt_senha" type="password" size="40">
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao"><input name="btn_entrar" type="submit" value="Entrar" class="button">
      <input name="btn_limpar" type="reset" value="Limpar" class="button">
      </td>
    </tr>
    <tr>
      <td>
        <br>
      </td>
    </tr>
  </table>
  </form>
  </center>
</body>
</html>
