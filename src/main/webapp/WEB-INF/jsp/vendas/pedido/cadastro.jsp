<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="br.com.alphadev.contest.KGlobal"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário para realizar a venda">
  <title>Lançamento de Venda</title>
  <mtw:inputMaskConfig />
  <mtw:inputMoneyConfig />
  <script type="text/javascript">
  function incluirItem() {
      document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/vendas/pedido.incluirItem.action';
      document.form_cadastro_pedido.submit();
  }
  function excluirItem(id, descricao) {
      if (confirm('Confirma a exclusão do item ' + descricao)) {
          document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/vendas/pedido.excluirItem.action?txt_indice=' + id;
          document.form_cadastro_pedido.submit();
      }
  }
  </script>
</head>
<body onload="javascript:document.form_cadastro_pedido.${foco == null ? 'txt_id_cliente' : foco}.focus();">
<form name="form_cadastro_pedido" method="post" action="<%=request.getContextPath()%>/vendas/pedido.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${venda.id}" />
  <table class="form">
    <%-- Se não foi emitido e não for o vendedor, alterar tipo --%>
    <c:if test="${venda.emissao == null}">
      <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>" negate="true">
        <tr>
          <td class="label">Tipo</td>
          <td>
            <div class="error"><mtw:error field="rad_tipo_venda"/></div>
            <mtw:radiobuttons name="rad_tipo_venda" id="rad_tipo_venda" list="tipoCompraVenda" defValue="${venda.tipo == null ? 'EMISSAO' : venda.tipo}" />
          </td>
        </tr>
      </mtw:hasAuthorization>
    </c:if>
    <%-- Se foi emitido e não for o vendedor, setar tipo --%>
    <c:if test="${venda.emissao != null}">
      <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>" negate="true">
        <mtw:input type="hidden" name="rad_tipo_venda" value="${venda.tipo}" />
      </mtw:hasAuthorization>
    </c:if>
    <tr>
      <td class="label">Data</td>
      <td>
        <mtw:inputText name="txt_data" id="txt_data" size="10" value="${venda.stringDataLancamento}" readonly="true" />
      </td>
    </tr>
    <tr>
      <td width="25%" class="label">Cliente</td>
      <td width="75%" nowrap="nowrap">
        <%@include file="../../util/buscar/cliente.jsp" %>
      </td>
    </tr>
    <tr>
      <td class="label">Vendedor</td>
      <td nowrap="nowrap">
        <%@include file="../../util/buscar/funcionario.jsp" %>
      </td>
    </tr>
    <tr>
      <td class="label">Prazo de Pagamento</td>
      <td>
        <div class="error"><mtw:error field="cbo_prazo_pagamento"/></div>
        <mtw:select name="cbo_prazo_pagamento" id="cbo_prazo_pagamento" list="listaPrazosPagamentos" defValue="${venda.prazoPagamento.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Forma de Pagamento</td>
      <td>
        <mtw:select name="cbo_forma_pagamento" id="cbo_forma_pagamento" list="formaPagamento" defValue="${venda.formaPagamento}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Número Pedido</td>
      <td>
        <mtw:inputMask name="txt_pedido" id="txt_pedido" size="10" value="${venda.pedido}" maskCustom="9999999999" textAlign="right" />
      </td>
    </tr>
    <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>" negate="true">
      <tr>
        <td class="label">Desconto</td>
        <td>
          <mtw:inputMask name="txt_desconto" id="txt_desconto" size="10" maskCustom="999" textAlign="right" value="${venda.desconto}" /> %
        </td>
      </tr>
    </mtw:hasAuthorization>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" rows="3" cols="60">${venda.observacao}</mtw:textarea></p>
      </td>
    </tr>
  </table>
  <br>
  <h1>Itens da Venda</h1>
  <cite>Incluir os Itens de Produtos da Venda</cite>
  <c:if test="${venda.emissao == null}">
    <table class="form">
      <tr>
        <td width="25%" class="label">Produto</td>
        <td width="75%" nowrap="nowrap">
          <%@include file="../../util/buscar/produto.jsp" %>
        </td>
      </tr>
      <tr>
        <td class="label">Quantidade</td>
        <td>
          <div class="error"><mtw:error field="txt_quantidade"/></div>
          <mtw:inputMask name="txt_quantidade" id="txt_quantidade" size="10" value="${txt_quantidade}" maskCustom="9999999999" textAlign="right" />
        </td>
      </tr>
      <tr>
        <td class="label">Preço</td>
        <td>
          <!-- Mostra o combo com os preços de venda pré-definidos -->
          <c:if test="${comboPrecos != null}">
            <div class="error"><mtw:error field="cbo_lista_preco_produto"/></div>
            <mtw:select name="cbo_lista_preco_produto" list="comboPrecos" emptyField="true"/>
          </c:if>
          <!-- Mostra campos textos para o preço de venda -->
          <c:if test="${comboPrecos == null}">
            <div class="error"><mtw:error field="txt_preco_produto"/></div>
            <mtw:inputMoney name="txt_preco_produto" id="txt_preco_produto" size="20" value="${txt_preco_produto}" dec_point="," textAlign="right" thousands_sep="." />
            <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>" negate="true">
              &nbsp;&nbsp;Comissão (%)
              <mtw:inputMoney name="txt_comissao_produto" id="txt_comissao_produto" size="5" dec_point="," textAlign="right" thousands_sep="." />
            </mtw:hasAuthorization>
          </c:if>
        </td>
      </tr>
      <tr>
        <td colspan="2" align="center">
          <input name="btn_incluir_item" type="button" value="Incluir Item" onclick="javascript:incluirItem();" class="button">
        </td>
      </tr>            
    </table>
  </c:if>
  <center>
  <display:table name="sessionScope.venda.itens" id="item">
    <display:column property="produto.referencia" title="Referência"/>
    <display:column property="produto.descricao" title="Descrição"/>
    <display:column property="quantidade" title="Qtd." style="text-align: center;" headerClass="center" />
    <%-- Vendedor não altera preço e nem visualiza comissão --%>
    <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>">
      <display:column title="Preço" style="text-align: right;" headerClass="right">
        <fmt:formatNumber pattern="#0.00">${item.valorVenda}</fmt:formatNumber>
      </display:column>
    </mtw:hasAuthorization>
    <%-- Vendas e outros perfis alteram preço e comissão --%>
    <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>" negate="true">
      <display:column title="Preço" style="text-align: right;" headerClass="right">
        <mtw:inputMoney name="txt_preco_produto_${item.produto.id}" id="txt_preco_produto_${item.produto.id}" size="10" value="${item.valorVenda}" dec_point="," textAlign="right" thousands_sep="." />
      </display:column>
      <display:column title="Comissão (%)" style="text-align: right;" headerClass="right">
        <mtw:inputMoney name="txt_comissao_produto_${item.produto.id}" id="txt_comissao_produto_${item.produto.id}" size="5" value="${item.comissao}" dec_point="," textAlign="right" thousands_sep="." />
      </display:column>
    </mtw:hasAuthorization>
    <display:column title="Sub-Total" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${item.subTotal}</fmt:formatNumber>
    </display:column>
    <c:if test="${venda.emissao == null}">
      <display:column title="Excluir" style="text-align: center; width: 70px;" headerClass="center">
        <a href="javascript:excluirItem(${item_rowNum},'${item.produto.descricao}')"><img src="images/excluir.gif" border="0" align="middle"/></a>
      </display:column>
    </c:if>
  </display:table>
  <h3>TOTAL: &nbsp;&nbsp;R$ <fmt:formatNumber pattern="#0.00">${venda.valorTotalProduto}</fmt:formatNumber></h3>
  <input name="btn_gravar" type="submit" value="Gravar" class="button" >
  </center>
</form>
</body>
</html>