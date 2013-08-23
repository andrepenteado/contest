<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/grupoProduto.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/grupoProduto.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/grupoProduto.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Listar grupos de produtos">
  <title>Grupos de Produtos</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_grupos_produto").attr("action", "${linkCadastro}");
      $("#form_pesquisar_grupos_produto").submit();
  }
  function excluir(id, descricao) {
      $('<p>Confirma exclusão do grupo de produto ' + descricao + '?</p>').dialog({
          title: "Excluir Grupo de Produto?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_grupos_produto").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_grupos_produto").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  $(function() {
      $('#grupoProdutoTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // Código
               { "bVisible": true },  // Descrição
               { "bVisible": true },  // Superior
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
  <form name="form_pesquisar_grupos_produto" id="form_pesquisar_grupos_produto" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>
  <center>
  <c:if test="${listaGruposProdutos != null}">
    <datatables:table data="${listaGruposProdutos}" htmlTableId="grupoProdutoTable" dataObjectId="grupoProduto">
      <datatables:column property="id" title="#" sortable="true"/>
      <datatables:column property="descricao" title="Descrição" sortable="true"/>
      <datatables:column title="Superior" sortable="true">
        ${grupoProduto.grupoProdutoSuperior.descricao}
      </datatables:column>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <center>
        <a href="javascript:excluir(${grupoProduto.id},'${grupoProduto.descricao}')"><img src="images/excluir.gif" border="0"/></a>
        <a href="javascript:alterar(${grupoProduto.id})"><img src="images/salvar.png" border="0"/></a>
        </center>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>