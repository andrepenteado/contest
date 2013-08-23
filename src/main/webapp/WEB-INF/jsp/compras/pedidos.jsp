<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/compras/pedido.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/compras/pedido.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/compras/pedido.excluir.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pedidos de compras não emitidos">
  <title>Compra</title>
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
  <%-- function emitir(id) {
      var ret = new Array();
      ret.push(new Array('URL_TO_REDIRECT', '<%=request.getContextPath()%>/compras/pedido.iniciar.action'));
      pCtrl.search('<%=request.getContextPath()%>/compras/pedido.emitir.action?hid_id=' + id, 600, 450, ret);
  } --%>
  $(function(){
      $('#pedidoCompraTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configList.js"%>
              ,"aoColumns": [
                  { "bVisible": true },  // ID
                  { "bVisible": true },  // Fornecedor
                  { "bVisible": true },  // Data
                  { "bVisible": true },  // Total
                  { "bSearchable": false, "bSortable": false } // Operações
              ],
              "sDom": '<"H"iCf>rt<"F"i>'
      });
  });
  </script>
</head>
<body>
  <form name="form_pesquisar_pedidos" id="form_pesquisar_pedidos" action="${linkPesquisar}">
  <input type="hidden" name="hid_id" id="hid_id">

  <p align="right">
    <input type="button" value="Nova Compra" onClick="window.location.href='${linkCadastro}'" class="button">
  </p>
  
  <center>
  <datatables:table data="${listaPedidos}" htmlTableId="pedidoCompraTable" dataObjectId="compra">
    <datatables:column property="id" title="ID"/>
    <datatables:column title="Fornecedor">
      ${compra.fornecedor.nome}
    </datatables:column>
    <datatables:column title="Data">
      <center>
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${compra.dataPedido}"/>
      </center>
    </datatables:column>
    <datatables:column title="Total">
      <fmt:formatNumber pattern="#0.00">${compra.totalProduto}</fmt:formatNumber>
    </datatables:column>
    <datatables:column title="Operações">
      <center>
      <a href="javascript:alterar(${compra.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
      <a href="javascript:excluir(${compra.id});"><img src="images/excluir.gif" title="Excluir" border="0"/></a>
      </center>
    </datatables:column>
  </datatables:table>
  </form>
  </center>
</body>
</html>