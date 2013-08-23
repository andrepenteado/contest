<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Formulário de Lançamentos Diversos">
  <title>Movimentação de Produtos</title>
  <mtw:inputMaskConfig />
  <mtw:inputMoneyConfig />
  <mtw:inputDateConfig />
</head>
<body onload="javascript:document.form_cadastro_lancamento_estoque.${foco}.focus();">
<form name="form_cadastro_pedido" method="post" action="<%=request.getContextPath()%>/estoque/movimento.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${movimentoEstoque.id}" />
  <table class="form">
    <%--
    <tr>
      <td width="25%" class="label">Data</td>
      <td width="75%">
        <div class="error"><mtw:error field="txt_data"/></div>
        <mtw:inputDate name="txt_data" id="txt_data" size="10" value="${movimentoEstoque.data}" />
      </td>
    </tr>
    --%>
    <tr>
      <td class="label">Produto</td>
      <td>
        <%@include file="../../util/buscar/produto.jsp" %>
      </td>
    </tr>
    <tr>
      <td class="label">Quantidade</td>
      <td>
        <div class="error"><mtw:error field="txt_quantidade"/></div>
        <mtw:inputMask name="txt_quantidade" id="txt_quantidade" size="5" value="${movimentoEstoque.quantidade}" maskCustom="99999" textAlign="right" />
      </td>
    </tr>
    <tr>
      <td class="label">Preço</td>
      <td>
        <div class="error"><mtw:error field="txt_preco_produto"/></div>
        <mtw:inputMoney name="txt_preco_produto" id="txt_preco_produto" size="20" value="${movimentoEstoque.valor}" dec_point="," textAlign="right" thousands_sep="." />
        &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
    <tr>
      <td class="label">Tipo de Movimentação</td>
      <td>
        <div class="error"><mtw:error field="cbo_tipo_movimentacao"/></div>
        <mtw:select name="cbo_tipo_movimentacao" id="cbo_tipo_movimentacao" list="tipoMovimentacao" defValue="${movimentoEstoque.tipoMovimentacao}" />
      </td>
    </tr>
    <tr>
      <td class="label"></td>
      <td>
        <div class="error"><mtw:error field="opt_vista_prazo"/></div>
        <mtw:radiobuttons name="opt_vista_prazo" id="opt_vista_prazo" list="vistaPrazo" defValue="${movimentoEstoque.vistaPrazo}" />
      </td>
    </tr>    
    <tr>
      <td class="label"></td>
      <td>
        <div class="error"><mtw:error field="opt_tipo_movimento"/></div>
        <mtw:radiobuttons name="opt_tipo_movimento" id="opt_tipo_movimento" list="tipoMovimento" defValue="${movimentoEstoque.tipoMovimento}" />
      </td>
    </tr>    
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="2">
        <div class="error"><mtw:error field="txt_observacao"/></div>
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${movimentoEstoque.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr><td><br></td></tr>
    <tr>
      <td colspan="2" class="barrabotao">
      <center>
        <input name="btn_pesquisar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/estoque/movimento.pesquisar.action';" class="button">
        </center>
      </td>      
    </tr>    
  </table>
  <br>
</form>
</body>
</html>