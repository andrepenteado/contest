<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <meta name="header" content="Entre com os dados no formulário para alterar sua senha de acesso">
    <mtw:inputDateConfig />
    <mtw:inputMaskConfig />
    <title>Alterar Senha</title>
  </head>
  <body>
    <form name="form_alterar_senha" method="post" action="<%=request.getContextPath()%>/comum.alterarSenha.action">
      <table cellspacing="0" cellpadding="0" class="form" width="100%">
        <tr>
          <td width="50%" class="label">Senha Atual</td>
          <td width="50%">
            <div class="error"><mtw:error field="txt_senha_atual"/></div>
            <mtw:input name="txt_senha_atual" type="password"/>
          </td>
        </tr>
        <tr>
          <td class="label">Nova Senha</td>
          <td>
            <div class="error"><mtw:error field="txt_nova_senha"/></div>
            <mtw:input name="txt_nova_senha" type="password"/>
          </td>
        </tr>
        <tr>
          <td class="label">Confirme a Nova Senha</td>
          <td>
            <div class="error"><mtw:error field="txt_confirme_nova_senha"/></div>
            <mtw:input name="txt_confirme_nova_senha" type="password"/>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="btn_entrar" type="submit" value="Alterar" class="button">
		  </td>
        </tr>
      </table>
    </form>
  </body>
</html>
