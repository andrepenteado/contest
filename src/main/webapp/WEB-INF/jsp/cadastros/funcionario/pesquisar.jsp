<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>

<c:set var="popup"><%=request.getParameter("popup")%></c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/funcionario.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/funcionario.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/funcionario.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Realizar pesquisa de funcionários por nome">
  <c:if test="${popup eq '1'}">
    <meta name="decorator" content="popup">
  </c:if>
  <title>Funcionários</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_funcionarios").attr("action", "${linkCadastro}");
      $("#form_pesquisar_funcionarios").submit();
  }
  function excluir(id, nome) {
      $('<p>Confirma exclusão do funcionário ' + nome + '?</p>').dialog({
          title: "Excluir Funcionário?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_funcionarios").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_funcionarios").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function returnPopupFuncionario(id, nome) {
	  parent.$("#txt_id_funcionario", window.parent.document).val(id);
	  parent.$("#txt_nome_funcionario", window.parent.document).val(nome);
	  parent.$("div.erro_funcionario", window.parent.document).text("");
      // Gambi, nao sei porque, precisa executar 2 vezes para sempre fechar o popup
      parent.$("#buscar-funcionario", window.parent.document).dialog('close');
      parent.$('#buscar-funcionario', window.parent.document).remove();
      parent.$("#buscar-funcionario", window.parent.document).dialog('close');
      parent.$('#buscar-funcionario', window.parent.document).remove();
  }
  $(function() {
      $('#tabs').tabs();
      $('#funcionarioTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>
           <c:if test="${popup != '1'}">
              ,"aoColumns": [
                  { "bVisible": true }, // Código
                  { "bVisible": true }, // Nome
                  { "bVisible": true }, // Identificação
                  { "bSearchable": false, "bSortable": false } // Operações
              ]
           </c:if>
      });
      $('#txt_funcionario').focus();
  });
  </script>
</head>
<body>
  <p align="right">
    <c:if test="${popup != '1'}">
      <input type="button" value="Incluir" onClick="window.location.href='${linkCadastro}'" class="button">
    </c:if> 
  </p>

  <form name="form_pesquisar_funcionarios" id="form_pesquisar_funcionarios" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="popup" value="${popup}"/>
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>

  <%@include file="../../comum/processando.jsp" %>

  <center>
  <div id="tabs">
    <ul>
      <li><a href="#tabNome">Por Nome</a></li>
    </ul>
    <div id="tabNome">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
    	  <td colspan="2" class="label">
            <center>Nome <mtw:inputText name="txt_funcionario" size="40" maxlength="100" onkeyup="isUpper(this);" /></center>
    	  </td>
    	</tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </div>
  </div>
  <br>
  <c:if test="${listaFuncionarios != null}">
    <datatables:table data="${listaFuncionarios}" htmlTableId="funcionarioTable" dataObjectId="funcionario">
      <datatables:column property="id" title="#" sortable="true"/>
      <c:choose>
        <c:when test="${popup eq '1'}">
          <datatables:column title="Nome" sortable="true">
            <a href="javascript:returnPopupFuncionario('${funcionario.id}','${funcionario.nome}')">${funcionario.nome}</a>
          </datatables:column>
        </c:when>
        <c:otherwise>
          <datatables:column property="nome" title="Nome" sortable="true"/>
          <datatables:column property="identificacao" title="Identificação" sortable="true"/>
          <datatables:column title="Operações" sortable="false" filterable="false">
            <center>
            <a href="javascript:excluir(${funcionario.id},'${funcionario.nome}')"><img src="images/excluir.gif" border="0"/></a>
            <a href="javascript:alterar('${funcionario.id}')"><img src="images/salvar.png" border="0"/></a>
            </center>
          </datatables:column>
        </c:otherwise>
      </c:choose>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>