<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/comissao.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/vendas/comissao.cadastro.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar comissão de vendedores">
  <title>Comissão</title>
  <script language="JavaScript" type="text/javascript">
  function exibir(id) {
      $("<iframe id='exibir-comissao' style='border: 0px;' src='${pageContext.request.contextPath}/vendas/comissao.exibir.action?txt_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Comissão #" + id,
          width: $(window).width()*0.6,
          height: $(window).height()*0.6,
          close: function(event, ui) { $(this).dialog('destroy')},
          open: function (event,ui) {$(this).css("width","95%")}
      });
  }
  function alterar(id) {
      $("#hid_id").val(id);
      $("#form_pesquisar_comissao").attr("action", "${linkCadastro}");
      $("#form_pesquisar_comissao").submit();
  }
  $(function() {
	  $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
      $('#comissaoTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // No.
               { "bVisible": false }, // Pedido
               { "bVisible": false }, // Data
               { "bVisible": true },  // Cliente
               { "bVisible": false }, // Cidade
               { "bVisible": true },  // Emissão
               { "bVisible": false }, // Forma Pagto.
               { "bVisible": false }, // Prazo
               { "bVisible": true },  // Vendedor
               { "bVisible": true },  // Total Venda
               { "bVisible": false }, // Data Pagto.
               { "bVisible": true },  // Comissão
               { "bSearchable": false, "bSortable": false } // Operações
           ]
      });
  });
  </script>
</head>
<body>
  <p align="right"><input type="button" value="Nova Comissão" onClick="window.location.href='${linkCadastro}'" class="button"></p>
  <form name="form_pesquisar_comissao" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id">
  <center>

  <%@include file="../../util/pesquisar/vendas.jsp"%>

  <br>
  <br>
  <c:if test="${fn:length(listaComissao) > 0}">
    <datatables:table data="${listaComissao}" htmlTableId="comissaoTable" dataObjectId="comissao">
      <datatables:column title="No." sortable="true">
        <div align="center">${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.numero : comissao.venda.orcamento != null ? comissao.venda.id : ''}</div>
      </datatables:column>
      <datatables:column title="Pedido" sortable="true">
        <div align="center">${comissao.venda.pedido}</div>
      </datatables:column>
      <datatables:column title="Data" sortable="true">
        <div align="center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${comissao.venda.dataLancamento}"/></div>
      </datatables:column>
      <datatables:column title="Cliente" sortable="true">
        ${comissao.venda.cliente.nome}
      </datatables:column>
      <datatables:column title="Cidade" sortable="true">
        ${comissao.venda.cliente.cidade.nome}
      </datatables:column>
      <datatables:column title="Emissão" sortable="true">
        <div align="center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.emissao : comissao.venda.orcamento != null ? comissao.venda.orcamento.emissao : ''}"/></div>
      </datatables:column>
      <datatables:column title="Forma Pagto." sortable="true">
        <div align="center">${comissao.venda.formaPagamento}</div>
      </datatables:column>
      <datatables:column title="Prazo" sortable="true">
        <div align="center">${comissao.venda.prazoPagamento.descricao}</div>
      </datatables:column>
      <datatables:column title="Vendedor" sortable="true">
        <div align="center">${comissao.venda.funcionario.nome}</div>
      </datatables:column>
      <datatables:column title="Total Venda" sortable="true">
        <div align="right"><fmt:formatNumber pattern="#0.00">${comissao.venda.valorTotal}</fmt:formatNumber></div>
      </datatables:column>
      <datatables:column title="Data Pgto." sortable="true">
        <div align="center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${comissao.dataPagamento}"/></div>
      </datatables:column>
      <datatables:column title="Comissão" sortable="true">
        <div align="right"><fmt:formatNumber pattern="#0.00">${comissao.valor}</fmt:formatNumber></div>
      </datatables:column>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <div align="center">
        <a href="javascript:exibir(${comissao.id});"><img src="images/consultar.gif" title="Consultar" border="0"/></a>
        <a href="javascript:alterar(${comissao.id});"><img src="images/salvar.png" border="0"/></a>
        </div>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>