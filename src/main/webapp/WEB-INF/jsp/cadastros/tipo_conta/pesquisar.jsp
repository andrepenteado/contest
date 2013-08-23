<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/tipoConta.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/tipoConta.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/tipoConta.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Realizar pesquisa dos tipos de conta por descrição">
  <title>Tipos de Conta</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_tipos_conta").attr("action", "${linkCadastro}");
      $("#form_pesquisar_tipos_conta").submit();
  }
  function excluir(id, descricao) {
      $('<p>Confirma exclusão do tipo de conta ' + descricao + '?</p>').dialog({
          title: "Excluir Tipo de Conta?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_tipos_conta").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_tipos_conta").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  $(function() {
      $('#tipoContaTable').dataTable({
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
  <form name="form_pesquisar_tipos_conta" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>
  <center>
  <c:if test="${listaTiposConta != null}">
    <datatables:table data="${listaTiposConta}" htmlTableId="tipoContaTable" dataObjectId="tipoConta">
      <datatables:column property="id" title="#"/>
      <datatables:column property="descricao" title="Descrição"/>
      <datatables:column title="Operações">
        <a href="javascript:excluir(${tipoConta.id},'${tipoConta.descricao}')"><img src="images/excluir.gif" border="0"/></a>
        <a href="javascript:alterar(${grupoProduto.id})"><img src="images/salvar.png" border="0"/></a>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>