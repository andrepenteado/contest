<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Cadastro de Contas a Receber">
  <title>Contas a Receber</title>
  <mtw:inputMoneyConfig />
  <mtw:inputMaskConfig />
  <mtw:inputDateConfig />
</head>
<body onload="javascript:document.form_contas_receber.txt_descricao.focus();">
<form name="form_contas_receber" method="post" action="<%=request.getContextPath()%>/financeiro/receber.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${receber.id}" />
  <table class="form">
    <tr>
      <c:if test="${receber.venda != null}">
        <td width="30%" class="label">Fornecedor</td>
        <td width="70%">
          <input type="text" name="txt_nome_cliente" id="txt_nome_cliente" size="50" value="${receber.venda.cliente.nome}" disabled="disabled" />
        </td>
      </c:if>
      <c:if test="${receber.venda == null}">
        <td width="30%" class="label">Descrição</td>
        <td width="70%" >
          <div class="error"><mtw:error field="txt_descricao"/></div>
          <mtw:inputText name="txt_descricao" id="txt_descricao" size="50" maxlength="100" value="${receber.descricao}" />
        </td>
      </c:if>
    </tr>
    <tr>
      <td class="label">Tipo</td>
      <td>
        <mtw:select name="cbo_tipo_conta" id="cbo_tipo_conta" list="tipoConta" defValue="${receber.tipoConta.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Vencimento</td>
      <td>
        <mtw:inputDate name="txt_vencimento" id="txt_vencimento" size="10" value="${receber.stringVencimento}" />
      </td>
    </tr>
    <tr>
      <td class="label">Valor</td>
      <td>
        <div class="error"><mtw:error field="txt_valor"/></div>
        <mtw:inputMoney name="txt_valor" id="txt_valor" size="20" value="${receber.valor}" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td class="label">Número do Documento</td>
      <td>
        <mtw:inputText name="txt_numero_documento" id="txt_numero_documento" size="50" maxlength="100" value="${receber.numeroDocumento}"/>
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