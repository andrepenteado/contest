<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/compras/pedido.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/compras/pedido.cadastro.action</c:set>
<c:set var="linkEstornar"><%=request.getContextPath()%>/compras/pedido.estornar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar as compras efetuadas">
  <title>Compra</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#hid_id").val(id);
      $("#form_pesquisar_compra").attr("action", "${linkCadastro}");
      $("#form_pesquisar_compra").submit();
  }
  function estornar(id) {
      $('<p>Confirma ESTORNO da compra ' + id + '?</p>').dialog({
          title: "Estornar Compra?",
          resizable: false,
          modal: true,
          buttons: {
              "Estornar": function() {
                  $("#hid_id").val(id);
                  $("#form_pesquisar_compra").attr("action", "${linkEstornar}");
                  $("#form_pesquisar_compra").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function exibir(id) {
      $("<iframe id='exibir-compra' style='border: 0px;' src='${pageContext.request.contextPath}/compras/pedido.exibir.action?txt_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Compra #" + id,
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
      $('#compraTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // Número
               { "bVisible": true },  // Fornecedor
               { "bVisible": true },  // Forma Pagto.
               { "bVisible": true },  // Emissão
               { "bVisible": true },  // Total
               { "bSearchable": false, "bSortable": false } // Operações
           ]
      });
  });
  </script>
</head>
<body>
  <form name="form_pesquisar_compra" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id">
  <center>

  <%@include file="../util/pesquisar/compras.jsp" %>

  <br>
  <c:if test="${fn:length(listaCompra) > 0}">
    <datatables:table data="${listaCompra}" htmlTableId="compraTable" dataObjectId="compra">
      <datatables:column property="id" title="ID" sortable="true"/>
      <datatables:column title="Fornecedor" sortable="true">
        ${compra.fornecedor.nome}
      </datatables:column>
      <datatables:column title="Forma Pgto." sortable="true">
        <div align="center">${compra.prazoPagamento.descricao}</div>
      </datatables:column>
      <datatables:column title="Emissão" sortable="true">
        <div align="center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${compra.emissao}"/></div>
      </datatables:column>
      <datatables:column title="Total" sortable="true">
        <div align="right"><fmt:formatNumber pattern="#0.00">${compra.totalProduto}</fmt:formatNumber></div>
      </datatables:column>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <div align="center">
        <a href="javascript:exibir(${compra.id});"><img src="images/consultar.gif" title="Consultar" border="0"/></a>
        <a href="javascript:alterar(${compra.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
        <a href="javascript:estornar(${compra.id});"><img src="images/estorno.png" title="Estornar" border="0"/></a>
        </div>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>