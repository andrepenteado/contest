<%@page import="com.github.andrepenteado.contest.KGlobal"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/vendas/pedido.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/vendas/pedido.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/pedido.pesquisar.action</c:set>
<c:set var="linkVerAvisos"><%=request.getContextPath()%>/vendas/pedido.verAvisos.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar pedidos a serem processados">
  <title>Lançamento de Pedido</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#hid_id").val(id);
      $("#form_pesquisar_pedidos").attr("action", "${linkCadastro}");
      $("#form_pesquisar_pedidos").submit();
  }
  function excluir(id) {
      $('<p>Confirma exclusão do pedido ' + id + '?</p>').dialog({
          title: "Excluir Pedido?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#hid_id").val(id);
                  $("#form_pesquisar_pedidos").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_pedidos").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function verAvisos(id) {
      $.getJSON('${linkVerAvisos}?hid_id=' + id, function(actionOutput) {
    	  $('<p>' + actionOutput.avisos + '</p>').dialog({
    		  title: "Avisos do Pedido #" + id,
              modal: true,
    	  })
      });
  }
  function imprimir(id) {
      $("<iframe id='imprimir-pedido' style='border: 0px;' src='${pageContext.request.contextPath}/vendas/orcamento.emitir.action?hid_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Imprimir Pedido #" + id,
          width: $(window).width()*0.6,
          height: $(window).height()*0.6,
          close: function(event, ui) { $("#form_pesquisar_pedidos").submit(); $(this).dialog('destroy'); },
          open: function (event,ui) { $(this).css("width","95%") }
      });
  }
  function emitir(id) {
      $("<iframe id='emitir-pedido' style='border: 0px;' src='${pageContext.request.contextPath}/vendas/notaFiscal.emitir.action?hid_id=" + id + "'/>")
      .dialog({
          modal: true,
          title: "Emitir NF do Pedido #" + id,
          width: $(window).width()*0.6,
          height: $(window).height()*0.6,
          close: function(event, ui) { $("#form_pesquisar_pedidos").submit(); $(this).dialog('destroy'); },
          open: function (event,ui) { $(this).css("width","95%") }
      });
  }
  function avisos(id) {
	  $("#avisos").dialog({
		  title: "Avisos do Pedido #" + id,
		  modal: true
	  });
  }
  $(function(){
      $('#pedidoVendaTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configList.js"%>
              ,"aoColumns": [
                  { "bVisible": true },  // ID
                  { "bVisible": false }, // Pedido
                  { "bVisible": true },  // Data
                  { "bVisible": true },  // Cliente
                  { "bVisible": false }, // Cidade
                  { "bVisible": false }, // Forma de Pagamento
                  { "bVisible": false }, // Prazo
                  { "bVisible": false }, // Vendedor
                  { "bVisible": true },  // Total
                  { "bSearchable": false, "bSortable": false } // Operações
              ],
              "sDom": '<"H"iCf>rt<"F"i>'
      });
  });
  </script>
</head>
<body>
  <center>
  <form name="form_pesquisar_pedidos" id="form_pesquisar_pedidos" action="${linkPesquisar}">
  <input type="hidden" name="hid_id" id="hid_id">

  <p align="right">
    <input type="button" value="Novo Pedido" onClick="window.location.href='${linkCadastro}'" class="button">
  </p>

  <datatables:table data="${listaPedidos}" htmlTableId="pedidoVendaTable" dataObjectId="venda">
    <datatables:column property="id" title="Número" sortable="true"/>
    <datatables:column property="pedido" title="Pedido" sortable="true"/>
    <datatables:column title="Data" sortable="true">
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${venda.dataLancamento}"/>
    </datatables:column>
    <datatables:column title="Cliente" sortable="true">
      ${venda.cliente.nome}
    </datatables:column>
    <datatables:column title="Cidade" sortable="true">
      ${venda.cliente.cidade.nome}
    </datatables:column>
    <datatables:column property="formaPagamento" title="Forma Pagto." sortable="true"/>
    <datatables:column title="Prazo" sortable="true">
      ${venda.prazoPagamento.descricao}
    </datatables:column>
    <datatables:column title="Vendedor" sortable="true">
      ${venda.funcionario.nome}
    </datatables:column>
    <datatables:column title="Total" sortable="true">
      <fmt:formatNumber pattern="#0.00">${venda.valorTotal}</fmt:formatNumber>
    </datatables:column>
    <datatables:column title="Operações" sortable="false" filterable="false">
      <center>
      <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO.concat(",").concat(KGlobal.CATEGORIA_VENDAS)%>">
        <a href="javascript:imprimir(${venda.id});"><img src="images/imprimir.png" title="Imprimir" border="0"/></a>
        <a href="javascript:emitir(${venda.id});"><img src="images/nf.png" title="Emitir" border="0"/></a>
      </mtw:hasAuthorization>
      <a href="javascript:alterar(${venda.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
      <a href="javascript:excluir(${venda.id});"><img src="images/excluir.gif" title="Excluir" border="0"/></a>
      <a href="javascript:verAvisos(${venda.id});"><img src="images/star.png" title="Ver Avisos" border="0"/></a>
      </center>
    </datatables:column>
  </datatables:table>
  </form>
  </center>
</body>
</html>