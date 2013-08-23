<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/tabelaPreco.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/tabelaPreco.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/tabelaPreco.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Listar tabelas de preços">
  <title>Tabelas de Preços</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_tabelas_preco").attr("action", "${linkCadastro}");
      $("#form_pesquisar_tabelas_preco").submit();
  }
  function excluir(id, descricao) {
      $('<p>Confirma exclusão da tabela de preço ' + descricao + '?</p>').dialog({
          title: "Excluir Tabela de Preço?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_tabelas_preco").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_tabelas_preco").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  $(function() {
      $('#tabelaPrecoTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // Código
               { "bVisible": true },  // Descrição
               { "bSearchable": false, "bSortable": false } // Operações
           ]
      });
  });
  </script>
</head>
<body>
  <p align="right">
    <input type="button" value="Incluir" onClick="window.location.href='${linkCadastro}'" class="button">
  </p>
  <form name="form_pesquisar_tabelas_preco" id="form_pesquisar_tabelas_preco" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>
  <center>
  <c:if test="${listaTabelaPrecos != null}">
    <datatables:table data="${listaTabelaPrecos}" htmlTableId="tabelaPrecoTable" dataObjectId="tabelaPreco">
      <datatables:column property="id" title="#" sortable="true"/>
      <datatables:column property="descricao" title="Descrição" sortable="true"/>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <center>
        <a href="javascript:excluir(${tabelaPreco.id},'${tabelaPreco.descricao}')"><img src="images/excluir.gif" border="0"/></a>
        <a href="javascript:alterar(${grupoProduto.id})"><img src="images/salvar.png" border="0"/></a>
        </center>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>