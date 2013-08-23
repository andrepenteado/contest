<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="linkVoltar"><%=request.getContextPath()%>/vendas/comissao.paga.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/comissao.detalharPagas.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Comissões pagas em determinada data">
  <title>Comissão de Vendedor</title>
</head>
<body>
  <form name="form_comissao" id="form_comissao" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_data_pagamento" value="${hid_data_pagamento}">
  <mtw:input type="hidden" name="txt_id_funcionario" value="${txt_id_funcionario}"/>
  <center>
  Colunas a exibir: <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaVendas"/>
  <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
  <br>
  <br>
  <display:table name="${listaComissao}" export="false" id="comissao" decorator="org.displaytag.decorator.TotalTableDecorator" requestURI="${linkPesquisar}">
    <c:if test="${numero eq '1'}">
      <display:column title="N&deg;" sortable="true" style="text-align: center;" headerClass="center">
        ${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.numero : comissao.venda.orcamento != null ? comissao.venda.id : ''}
      </display:column>
    </c:if>
    <c:if test="${pedido eq '1'}">
      <display:column property="venda.pedido" sortable="true" title="Pedido" style="text-align: center;" headerClass="center"/>
    </c:if>
    <display:column title="Emissão" style="text-align: center;" sortable="true" headerClass="center">
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.emissao : comissao.venda.orcamento != null ? comissao.venda.orcamento.emissao : ''}"/>
    </display:column>
    <display:column property="venda.cliente.nome" title="Cliente" sortable="true"/>
    <c:if test="${cidade eq '1'}">
      <display:column property="venda.cliente.cidade.nome" title="Cliente" sortable="true"/>
    </c:if>
    <c:if test="${formaPagamento eq '1'}">
      <display:column property="venda.formaPagamento" title="Forma Pagto." sortable="true" style="text-align: center;" headerClass="center"/>
    </c:if>
    <c:if test="${prazo eq '1'}">
      <display:column property="venda.prazoPagamento.descricao" title="Prazo" sortable="true" style="text-align: center;" headerClass="center"/>
    </c:if>
    <display:column property = "venda.valorTotal" sortable="true" title="Total" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column property="stringPorcentagem" sortable="true" title="%" style="text-align: right;" headerClass="right"/>
    <display:column property="valor" title="Comissão" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="center" total="true"/>
  </display:table>
  <input name="btn_pesquisar" type="button" value="Voltar" onClick="javascript:window.location.href='${linkVoltar}';" class="button">
  </center>
  </form>
</body>
</html>