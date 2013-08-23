<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Cadastro de Comissão de Vendedor">
  <title>Comissão</title>
  <mtw:inputMoneyConfig />
  <mtw:inputMaskConfig />
  <mtw:inputDateConfig />
</head>
<body onload="javascript:document.form_comissao.txt_descricao.focus();">
<form name="form_contas_receber" method="post" action="<%=request.getContextPath()%>/vendas/comissao.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${comissao.id}" />
  <table class="form">
    <tr>
      <c:if test="${comissao.venda != null}">
        <td width="30%" class="label">Cliente</td>
        <td width="70%">
          <input type="text" name="txt_nome_cliente" id="txt_nome_cliente" size="50" value="${comissao.venda.cliente.nome}" disabled="disabled" />
        </td>
      </c:if>
      <c:if test="${comissao.venda == null}">
        <td width="30%" class="label">Descrição</td>
        <td width="70%" >
          <div class="error"><mtw:error field="txt_descricao"/></div>
          <mtw:inputText name="txt_descricao" id="txt_descricao" size="50" maxlength="100" value="${comissao.descricao}" />
        </td>
      </c:if>
    </tr>
    <tr>
      <td class="label">Vendedor</td>
      <td>
        <div class="error"><mtw:error field="cbo_vendedor"/></div>
        <mtw:select name="cbo_vendedor" id="cbo_vendedor" list="listaVendedores" defValue="${comissao.venda.funcionario.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Data Pagamento</td>
      <td>
        <mtw:inputDate name="txt_data_pagamento" id="txt_data_pagamento" size="10" value="${comissao.stringDataPagamento}" />
      </td>
    </tr>
    <tr>
      <td class="label">Valor</td>
      <td>
        <div class="error"><mtw:error field="txt_valor"/></div>
        <mtw:inputMoney name="txt_valor" id="txt_valor" size="20" value="${comissao.valor}" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${receber.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/vendas/comissao.pesquisar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>