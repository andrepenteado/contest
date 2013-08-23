<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/prazoPagamento.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/prazoPagamento.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/prazoPagamento.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Listar de prazos de pagamentos">
  <title>Prazos de Pagamentos</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_prazos_pagamentos").attr("action", "${linkCadastro}");
      $("#form_pesquisar_prazos_pagamentos").submit();
  }
  function excluir(id, descricao) {
      $('<p>Confirma exclusão do prazo de pagamento ' + descricao + '?</p>').dialog({
          title: "Excluir Prazo Pagamento?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_prazos_pagamentos").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_prazos_pagamentos").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  $(function() {
      $('#prazoPagamentoTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
               { "bVisible": true },  // Código
               { "bVisible": true },  // Descrição
               { "bVisible": true },  // Parcelas
               { "bVisible": true },  // Desconto / Juros
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
  <form name="form_pesquisar_prazos_pagamentos" id="form_pesquisar_prazos_pagamentos" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>
  <center>
  <c:if test="${listaPrazosPagamentos != null}">
    <datatables:table data="${listaPrazosPagamentos}" htmlTableId="prazoPagamentoTable" dataObjectId="prazoPagamento">
      <datatables:column property="id" title="#" sortable="true"/>
      <datatables:column property="descricao" title="Descrição" sortable="true"/>
      <datatables:column property="parcelas" title="Parcelas" sortable="true"/>
      <datatables:column property="descontoJuros" title="Desconto / Juros" sortable="true"/>
      <datatables:column title="Operações" sortable="false" filterable="false">
        <center>
        <a href="javascript:excluir(${prazoPagamento.id},'${prazoPagamento.descricao}')"><img src="images/excluir.gif" border="0"/></a>
        <a href="javascript:alterar(${prazoPagamento.id})"><img src="images/salvar.png" border="0"/></a>
        </center>
      </datatables:column>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>