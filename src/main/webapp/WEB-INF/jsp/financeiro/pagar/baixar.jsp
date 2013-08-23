<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Dar baixa total ou parcialmente em contas a pagar">
  <title>Baixar Contas a Pagar</title>
  <mtw:inputMoneyConfig />
  <mtw:inputMaskConfig />
  <mtw:inputDateConfig />
</head>
<body onload="javascript:document.form_baixar_paga.txt_data_pagamento.focus();">
<form name="form_baixar_paga" method="post" action="<%=request.getContextPath()%>/financeiro/pagar.baixar.action">
  <mtw:input type="hidden" name="hid_id" value="${pagar.id}" />
  <table class="form" width="90%">
    <tr>
      <c:if test="${pagar.compra != null}">
        <td width="30%" class="label">Fornecedor</td>
        <td width="70%">
          <input type="text" name="txt_nome_fornecedor" id="txt_nome_fornecedor" size="50" value="${pagar.compra.fornecedor.nome}" disabled="disabled" />
        </td>
      </c:if>
      <c:if test="${pagar.compra == null}">
        <td width="30%" class="label">Descrição</td>
        <td width="70%" >
          <input name="txt_descricao" id="txt_descricao" size="50" maxlength="100" value="${pagar.descricao}" disabled="disabled" />
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
        <input type="text" name="txt_vencimento" id="txt_vencimento" size="10" value="${pagar.stringVencimento}" disabled="disabled"/>
      </td>
    </tr>
    <tr>
      <td class="label">Valores: Pago</td>
      <td>
        <input type="text" name="txt_pago" id="txt_pago" size="10" value="${pagar.valorPago}" disabled="disabled" /> 
        Devido: <input type="text" name="txt_valor_devido" id="txt_valor_devido" size="10" value="${pagar.valorDevido}" disabled="disabled" />
      </td>
    </tr>
    <tr>
      <td class="label">Data de Pagamento</td>
      <td>
        <div class="error"><mtw:error field="txt_data_pagamento"/></div>
        <mtw:inputDate name="txt_data_pagamento" id="txt_data_pagamento" size="10" />
      </td>
    </tr>
    <tr>
      <td class="label">Valor Pago</td>
      <td>
        <div class="error"><mtw:error field="txt_valor_pago"/></div>
        <mtw:inputMoney name="txt_valor_pago" id="txt_valor_pago" size="20" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td class="label">Forma de Pagamento</td>
      <td>
        <mtw:select name="cbo_forma_pagamento" id="cbo_forma_pagamento" list="formaPagamento" emptyField="true" />
      </td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/financeiro/pagar.pesquisar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>