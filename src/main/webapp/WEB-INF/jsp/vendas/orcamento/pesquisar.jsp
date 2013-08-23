<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/orcamento.pesquisar.action</c:set>
<c:set var="linkEstornar"><%=request.getContextPath()%>/vendas/orcamento.estornar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/vendas/pedido.cadastro.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar orçamentos">
  <title>Orçamentos</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#hid_id").val(id);
      $("#form_pesquisar_orcamentos").attr("action", "${linkCadastro}");
      $("#form_pesquisar_orcamentos").submit();
  }
  function estornar(id) {
      $('<p>Confirma ESTORNO do orçamento ' + id + '?</p>').dialog({
          title: "Estornar Orçamento?",
          resizable: false,
          modal: true,
          buttons: {
              "Estornar": function() {
                  $("#hid_id").val(id);
                  $("#form_pesquisar_orcamentos").attr("action", "${linkEstornar}");
                  $("#form_pesquisar_orcamentos").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function exibir(id) {
      $("<iframe id='exibir-orcamento' style='border: 0px;' src='${pageContext.request.contextPath}/vendas/orcamento.exibir.action?txt_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Orçamento #" + id,
          width: $(window).width()*0.6,
          height: $(window).height()*0.6,
          close: function(event, ui) { $(this).dialog('destroy')},
          open: function (event,ui) {$(this).css("width","95%")}
      });
  }
  $(function() {
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
      $('#orcamentoTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": false }, // Número
               { "bVisible": true },  // Pedido
               { "bVisible": true },  // Cliente
               { "bVisible": false }, // Forma Pagto.
               { "bVisible": false }, // Prazo
               { "bVisible": true },  // Emissão
               { "bVisible": false }, // Vendedor
               { "bVisible": true },  // Total
               { "bSearchable": false, "bSortable": false } // Operações
           ]
      });
  });
  </script>
</head>
<body>
  <form name="form_pesquisar_orcamentos" id="form_pesquisar_orcamentos" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id" id="hid_id">
  <input type="hidden" name="txt_id" id="txt_id">
  <center>

  <%@include file="../../util/pesquisar/vendas.jsp"%>

  <br>
  <c:if test="${fn:length(listaOrcamentos) > 0}">
    <datatables:table data="${listaOrcamentos}" htmlTableId="orcamentoTable" dataObjectId="orcamento">
      <datatables:column title="Número" sortable="true">
        <div align="center">${orcamento.venda.id}</div>
      </datatables:column>
      <datatables:column title="Pedido" sortable="true">
        <div align="center">${orcamento.venda.pedido}</div>
      </datatables:column>
      <datatables:column title="Cliente" sortable="true">
        ${orcamento.venda.cliente == null ? 'CONSUMIDOR' : orcamento.venda.cliente.nome}
      </datatables:column>
      <datatables:column title="Forma Pagto." sortable="true">
        ${orcamento.venda.formaPagamento.descricao}
      </datatables:column>
      <datatables:column title="Prazo" sortable="true">
        ${orcamento.venda.cliente == null ? 'A VISTA' : orcamento.venda.prazoPagamento.descricao}
      </datatables:column>
      <datatables:column title="Emissão" sortable="true">
        <div align="center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${orcamento.emissao}"/></div>
      </datatables:column>
      <datatables:column title="Vendedor" sortable="true">
        ${orcamento.venda.funcionario.nome}
      </datatables:column>
      <datatables:column title="Total" sortable="true">
        <div align="right"><fmt:formatNumber pattern="#0.00">${orcamento.venda.valorTotal}</fmt:formatNumber></div>
      </datatables:column>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <div align="center">
        <a href="javascript:exibir(${orcamento.venda.id});"><img src="images/consultar.gif" title="Consultar" border="0"/></a>
        <%-- <a href="javascript:alterar(${orcamento.venda.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a> --%>
        <a href="javascript:estornar(${orcamento.venda.id});"><img src="images/estorno.png" title="Estornar" border="0"/></a>
        </div>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>