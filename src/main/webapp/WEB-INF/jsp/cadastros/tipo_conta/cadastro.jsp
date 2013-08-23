<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados do tipo de conta">
  <title>Cadastro de Tipo de Conta</title>
  <mtw:inputMaskConfig />
</head>
<body onload="javascript:document.form_cadastro_tipo_conta.txt_descricao.focus();">
<form name="form_cadastro_tipo_conta" method="post" action="<%=request.getContextPath()%>/cadastros/tipoConta.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${tipoConta.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Descrição</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_descricao"/></div>
        <mtw:inputText name="txt_descricao" id="txt_descricao" size="60" maxlength="100" value="${tipoConta.descricao}" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${tipoConta.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/tipoConta.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>