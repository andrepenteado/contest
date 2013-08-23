<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados da forma de pagamento">
  <title>Cadastro de Prazo de Pagamento</title>
  <mtw:inputMaskConfig />
  <mtw:inputMoneyConfig />
</head>
<body onload="javascript:document.form_cadastro_prazo_pagamento.txt_descricao.focus();">
<form name="form_cadastro_prazo_pagamento" method="post" action="<%=request.getContextPath()%>/cadastros/prazoPagamento.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${prazoPagamento.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Descrição</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_descricao"/></div>
        <mtw:inputText name="txt_descricao" id="txt_descricao" size="60" maxlength="100" value="${prazoPagamento.descricao}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Parcelas</td>
      <td width="70%">
        <mtw:inputText name="txt_parcelas" id="txt_parcelas" size="30" maxlength="100" value="${prazoPagamento.parcelas}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Desconto / Juros</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_desconto_juros"/></div>
        <mtw:inputText name="txt_desconto_juros" id="txt_desconto_juros" size="20" value="${prazoPagamento.descontoJuros}" />
        &nbsp;&nbsp;Desconto: -10 / Juros: 10
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${prazoPagamento.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/prazoPagamento.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>