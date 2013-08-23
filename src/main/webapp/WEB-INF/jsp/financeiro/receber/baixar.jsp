<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Dar baixa total ou parcialmente em contas a receber">
  <title>Baixar Contas a Receber</title>
  <mtw:inputMoneyConfig />
  <mtw:inputMaskConfig />
  <mtw:inputDateConfig />
</head>
<body onload="javascript:document.form_baixar_recebida.txt_data_recebimento.focus();">
<form name="form_baixar_recebida" method="post" action="<%=request.getContextPath()%>/financeiro/receber.baixar.action">
  <mtw:input type="hidden" name="hid_id" value="${receber.id}" />
  <table class="form" width="90%">
    <tr>
      <c:if test="${receber.venda != null}">
        <td width="30%" class="label">Cliente</td>
        <td width="70%">
          <input type="text" name="txt_nome_cliente" id="txt_nome_cliente" size="50" value="${receber.venda.cliente.nome}" disabled="disabled" />
        </td>
      </c:if>
      <c:if test="${receber.venda == null}">
        <td width="30%" class="label">Descrição</td>
        <td width="70%" >
          <div class="error"><mtw:error field="txt_descricao"/></div>
          <input name="txt_descricao" id="txt_descricao" size="50" maxlength="100" value="${receber.descricao}" disabled="disabled"/>
        </td>
      </c:if>
    </tr>
    <tr>
      <td class="label">Tipo</td>
      <td>
        <input name="txt_tipo_conta" id="txt_tipo_conta" value="${receber.tipoConta.descricao}" disabled="disabled" />
      </td>
    </tr>
    <tr>
      <td class="label">Vencimento</td>
      <td>
        <input type="text" name="txt_vencimento" id="txt_vencimento" size="10" value="${receber.stringVencimento}" disabled="disabled"/>
      </td>
    </tr>
    <tr>
      <td class="label">Valores: Recebido</td>
      <td>
        <input type="text" name="txt_recebido" id="txt_recebido" size="10" value="${receber.valorRecebido}" disabled="disabled" /> 
        Devido: <input type="text" name="txt_valor_devido" id="txt_valor_devido" size="10" value="${receber.valorDevido}" disabled="disabled" />
      </td>
    </tr>
    <tr>
      <td class="label">Data de Recebimento</td>
      <td>
        <div class="error"><mtw:error field="txt_data_recebimento"/></div>
        <mtw:inputDate name="txt_data_recebimento" id="txt_data_recebimento" size="10" />
      </td>
    </tr>
    <tr>
      <td class="label">Valor Recebido</td>
      <td>
        <div class="error"><mtw:error field="txt_valor_recebido"/></div>
        <mtw:inputMoney name="txt_valor_recebido" id="txt_valor_recebido" size="20" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td class="label">Forma de Pagamento</td>
      <td>
        <mtw:select name="cbo_forma_pagamento" id="cbo_forma_pagamento" list="formaPagamento" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Número do Documento</td>
      <td>
        <input name="txt_numero_documento" id="txt_numero_documento" size="50" maxlength="100" value="${receber.numeroDocumento}"/>
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
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/financeiro/receber.pesquisar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>