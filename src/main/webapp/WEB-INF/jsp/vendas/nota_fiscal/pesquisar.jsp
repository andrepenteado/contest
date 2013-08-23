<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/vendas/pedido.cadastro.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/notaFiscal.pesquisar.action</c:set>
<c:set var="linkEstornar"><%=request.getContextPath()%>/vendas/notaFiscal.estornar.action</c:set>
<c:set var="linkCancelar"><%=request.getContextPath()%>/vendas/notaFiscal.cancelar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar notas fiscais emitidas">
  <title>Notas Fiscais</title>
  <script type="text/javascript">
  function alterar(id) {
      $("#hid_id").val(id);
      $("#form_pesquisar_nf").attr("action", "${linkCadastro}");
      $("#form_pesquisar_nf").submit();
  }
  function estornar(id, nf) {
      $('<p>Confirma ESTORNO da nota fiscal ' + nf + '?</p>').dialog({
          title: "Estornar Nota Fiscal?",
          resizable: false,
          modal: true,
          buttons: {
              "Estornar": function() {
                  $("#hid_id").val(id);
                  $("#form_pesquisar_nf").attr("action", "${linkEstornar}");
                  $("#form_pesquisar_nf").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function cancelar(id, nf) {
      $('<p>Confirma CANCELAMENTO da nota fiscal ' + nf + '?</p>').dialog({
          title: "Cancelar Nota Fiscal?",
          resizable: false,
          modal: true,
          buttons: {
              "Estornar": function() {
                  $("#hid_id").val(id);
                  $("#form_pesquisar_nf").attr("action", "${linkCancelar}");
                  $("#form_pesquisar_nf").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function exibir(id) {
      $("<iframe id='exibir-nf' style='border: 0px;' src='${pageContext.request.contextPath}/vendas/notaFiscal.exibir.action?txt_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Nota Fiscal #" + id,
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
      $('#notaFiscalTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // Número
               { "bVisible": false }, // Pedido
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
  <form name="form_pesquisar_nf" id="form_pesquisar_nf" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id" id="hid_id">
  <center>

  <%@include file="../../util/pesquisar/vendas.jsp"%>

  <br>
  <c:if test="${fn:length(listaNotaFiscal) > 0}">
    <datatables:table data="${listaNotaFiscal}" htmlTableId="notaFiscalTable" dataObjectId="nf">
      <datatables:column title="NF" sortable="true">
        <div align="center">
        ${nf.numero}
        </div>
      </datatables:column>
      <datatables:column title="Pedido" sortable="true">
        <div align="center">
        ${nf.venda.pedido}
        </div>
      </datatables:column>
      <datatables:column title="Cliente" sortable="true">
        ${nf.cancelada ? "<s>" : ""}
        ${nf.venda.cliente.nome}
        ${nf.cancelada ? "</s>" : ""}
      </datatables:column>
      <datatables:column title="Forma Pagto." sortable="true">
        ${nf.cancelada ? "<s>" : ""}
        ${nf.venda.formaPagamento.descricao}
        ${nf.cancelada ? "</s>" : ""}
      </datatables:column>
      <datatables:column title="Prazo" sortable="true">
        ${nf.cancelada ? "<s>" : ""}
        ${nf.venda.prazoPagamento.descricao}
        ${nf.cancelada ? "</s>" : ""}
      </datatables:column>
      <datatables:column title="Emissão" sortable="true">
        <div align="center">
        ${nf.cancelada ? "<s>" : ""}
        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${nf.emissao}"/>
        ${nf.cancelada ? "</s>" : ""}
        </div>
      </datatables:column>
      <datatables:column title="Vendedor" sortable="true">
        ${nf.venda.funcionario.nome}
      </datatables:column>
      <datatables:column title="Total" sortable="true">
        <div align="right">
        ${nf.cancelada ? "<s>" : ""}
        <fmt:formatNumber pattern="#0.00">${nf.venda.valorTotal}</fmt:formatNumber>
        ${nf.cancelada ? "</s>" : ""}
        </div>
      </datatables:column>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <div align="center">
        <a href="javascript:exibir(${nf.venda.id});"><img src="images/consultar.gif" title="Consultar" border="0"/></a>
        <a href="javascript:alterar(${nf.venda.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
        <a href="javascript:estornar(${nf.venda.id},${nf.numero});"><img src="images/estorno.png" title="Estornar" border="0"/></a>
        <a href="javascript:cancelar(${nf.venda.id},${nf.numero});"><img src="images/excluir.gif" title="Cancelar" border="0"/></a>
        </div>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>