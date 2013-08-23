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
  <meta name="header" content="Preencha o formulário para incluir a compra">
  <title>Entrada de Produtos</title>
  <mtw:inputMaskConfig />
  <mtw:inputMoneyConfig />
  <mtw:inputDateConfig />
  <script type="text/javascript">
  function incluirItem() {
      document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/compras/pedido.incluirItem.action';
      document.form_cadastro_pedido.submit();
  }
  function excluirItem(id, descricao) {
      if (confirm('Confirma a exclusão do item ' + descricao)) {
          document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/compras/pedido.excluirItem.action?txt_indice=' + id;
          document.form_cadastro_pedido.submit();
      }
  }
  function emitir() {
      document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/compras/pedido.emitir.action';
      document.form_cadastro_pedido.submit();
  }  
  function alterar() {
      document.form_cadastro_pedido.action = '<%=request.getContextPath()%>/compras/pedido.alterar.action';
      document.form_cadastro_pedido.submit();
  }
  </script>
</head>
<body onload="javascript:document.form_cadastro_pedido.${foco}.focus();">
<form name="form_cadastro_pedido" method="post" action="<%=request.getContextPath()%>/compras/pedido.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${compra.id}" />
  <table class="form">
    <tr>
      <td width="25%" class="label">Pedido</td>
      <td width="75%">
        <mtw:inputText name="txt_pedido" id="txt_pedido" size="10" value="${compra.pedido}" />
      </td>
    </tr>
    <tr>
      <td class="label">Data</td>
      <td>
        <mtw:inputDate name="txt_data" id="txt_data" size="10" value="${compra.stringDataPedido}" />
      </td>
    </tr>
    <tr>
      <td class="label">Previsão Entrega</td>
      <td>
        <mtw:inputDate name="txt_previsao_entrega" id="txt_previsao_entrega" size="10" value="${compra.stringPrevisaoEntrega}" />
      </td>
    </tr>
    <tr>
      <td class="label">Nota Fiscal</td>
      <td>
        <mtw:inputMask name="txt_nota_fiscal" id="txt_nota_fiscal" size="10" maskCustom="999999999" textAlign="right" value="${compra.notaFiscal}"/>
      </td>
    </tr>
    <tr>
      <td class="label">Emissão</td>
      <td>
        <div class="error"><mtw:error field="txt_emissao"/></div>
        <mtw:inputDate name="txt_emissao" id="txt_emissao" size="10" value="${compra.stringEmissao}" />
      </td>
    </tr>
    <tr>
      <td class="label">Fornecedor</td>
      <td>
        <%@include file="../util/buscar/fornecedor.jsp" %>
      </td>
    </tr>
    <tr>
      <td class="label">Prazo de Pagamento</td>
      <td>
        <div class="error"><mtw:error field="cbo_prazo_pagamento"/></div>
       	<mtw:select name="cbo_prazo_pagamento" id="cbo_prazo_pagamento" list="listaPrazosPagamentos" defValue="${compra.prazoPagamento.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Frete</td>
      <td>
        <mtw:select name="cbo_frete" id="cbo_frete" list="frete" emptyField="true" defValue="${compra.frete}"/>
      </td>
    </tr>
    <tr>
      <td class="label">Transportador</td>
      <td>
        <mtw:inputText name="txt_transportador" id="txt_transportador" size="40" maxlength="100" onkeyup="isUpper(this);" value="${compra.transportador}"/>
      </td>
    </tr>
  </table>
  <br>
  <h1>Itens da Compra</h1>
  <cite>Incluir os Itens de Produtos da Compra</cite>
  
  <c:if test="${!compra.emitido}">
  
	  <table class="form">
	    <tr>
	      <td width="25%" class="label">Produto</td>
	      <td width="75%">
	        <%@include file="../util/buscar/produto.jsp" %>
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
	      <td class="label">CFOP</td>
	      <td>
	        <%@include file="../util/buscar/cfop.jsp" %>
	      </td>
	    </tr>
	
	    <tr>
	      <td class="label">ICMS</td>
	      <td>
	        <mtw:inputMoney name="txt_icms" id="txt_icms" size="15" value="${txt_icms}" dec_point="," textAlign="right" thousands_sep="."/> %
	      </td>
	    </tr>
	    <tr>
	      <td class="label">IPI</td>
	      <td>
	        <mtw:inputMoney name="txt_ipi" id="txt_ipi" size="15" value="${txt_ipi}" dec_point="," textAlign="right" thousands_sep="."/> %
	      </td>
	    </tr>
	
	    <tr>
	      <td class="label">Preço</td>
	      <td>
	        <div class="error"><mtw:error field="txt_preco_produto"/></div>
	        <mtw:inputMoney name="txt_preco_produto" id="txt_preco_produto" size="20" value="${txt_preco_produto}" dec_point="," textAlign="right" thousands_sep="." />
	        &nbsp;&nbsp;&nbsp;&nbsp;
	        <input name="btn_incluir_item" type="button" value="Incluir Item" onclick="javascript:incluirItem();" class="button">
	      </td>
	    </tr>
	  </table>
  </c:if>
  
  <center>
  <display:table name="sessionScope.compra.itens" id="item">
    <display:column property="produto.id" title="ID" style="text-align: center;" headerClass="center"/>
    <display:column property="produto.referencia" title="Referência"/>
    <display:column property="produto.descricao" title="Descrição"/>
    <display:column property="quantidade" title="Qtd." style="text-align: center;" headerClass="center" />
    <display:column property="cfop.codigo" title="CFOP" style="text-align: center;" headerClass="center" />
    <display:column title="Preço" style="text-align: right;" headerClass="right">
      <mtw:inputMoney name="txt_preco_produto_${item.produto.id}" id="txt_preco_produto_${item.produto.id}" size="10" value="${item.valorCompra}" dec_point="," textAlign="right" thousands_sep="." />
    </display:column>
    <display:column title="Impostos" style="text-align: right;" headerClass="right">
      <c:forEach items="${item.impostos}" var="impostoCompra">
		${impostoCompra.imposto.descricao}: ${impostoCompra.aliquota}%<br>      
      </c:forEach>
    </display:column>
    <display:column title="Sub-Total" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${item.subTotal}</fmt:formatNumber>
    </display:column>
    <c:if test="${!compra.emitido}">
	    <display:column title="Excluir" style="text-align: center; width: 70px;" headerClass="center">
	      <a href="javascript:excluirItem(${item_rowNum},'${item.produto.descricao}')"><img src="images/excluir.gif" border="0" align="middle"/></a>
	    </display:column>
	</c:if>
  </display:table>
  <h3>TOTAL: &nbsp;&nbsp;R$ <fmt:formatNumber pattern="#0.00">${compra.totalProduto}</fmt:formatNumber></h3>

  <c:if test="${!compra.emitido}">
	  <input name="btn_gravar" type="submit" value="Gravar" class="button">
	  <input name="btn_emitir" type="button" value="Emitir" class="button" onclick="javascript:emitir();">
	  <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/compras/pedido.iniciar.action';" class="button">
  </c:if>
  <c:if test="${compra.emitido}">
	  <input name="btn_alterar" type="button" value="Gravar" class="button" onclick="javascript:alterar();">
   	  <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/compras/pedido.pesquisar.action';" class="button">	  
  </c:if>
  </center>
</form>
</body>
</html>