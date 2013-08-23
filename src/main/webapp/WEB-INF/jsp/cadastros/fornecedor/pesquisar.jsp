<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="br.com.alphadev.contest.KGlobal"%>

<c:set var="popup"><%=request.getParameter("popup")%></c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/fornecedor.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/fornecedor.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/fornecedor.pesquisar.action</c:set>

<c:set var="pesquisarCodigoNome"><%=KGlobal.TipoPesquisaFornecedor.CODIGO_NOME%></c:set>
<c:set var="pesquisarTelefoneEmail"><%=KGlobal.TipoPesquisaFornecedor.TELEFONE_EMAIL%></c:set>
<c:set var="pesquisarRuaCep"><%=KGlobal.TipoPesquisaFornecedor.RUA_CEP%></c:set>
<c:set var="pesquisarCidade"><%=KGlobal.TipoPesquisaFornecedor.CIDADE%></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Realizar pesquisa de fornecedores">
  <c:if test="${popup eq '1'}">
    <meta name="decorator" content="popup">
  </c:if>
  <title>Fornecedores</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
	  $("#txt_id").val(id);
      $("#form_pesquisar_fornecedores").attr("action", "${linkCadastro}");
      $("#form_pesquisar_fornecedores").submit();
  }
  function excluir(id, nome) {
      $('<p>Confirma exclusão do fornecedor ' + nome + '?</p>').dialog({
    	  title: "Excluir Fornecedor?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_fornecedores").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_fornecedores").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function returnPopupFornecedor(id, nome) {
      parent.$("#txt_id_fornecedor", window.parent.document).val(id);
      parent.$("#txt_nome_fornecedor", window.parent.document).val(nome);
      parent.$("div.erro_fornecedor", window.parent.document).text("");
      // Gambi, nao sei porque, precisa executar 2 vezes para sempre fechar o popup
      parent.$("#buscar-fornecedor", window.parent.document).dialog('close');
      parent.$('#buscar-fornecedor', window.parent.document).remove();
      parent.$("#buscar-fornecedor", window.parent.document).dialog('close');
      parent.$('#buscar-fornecedor', window.parent.document).remove();
  }
  $(function(){
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
      $('#fornecedorTable').dataTable({
           <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
           "aoColumns": [
                         { "bVisible": true },  // Código
                         { "bVisible": true },  // Nome
                         { "bVisible": false }, // Endereço
                         { "bVisible": false }, // Bairro
                         { "bVisible": false }, // CEP
                         { "bVisible": true },  // Cidade
                         { "bVisible": false }, // Telefone
                         { "bVisible": false }, // E-Mail
                         <c:if test="${popup != '1'}">
                           { "bSearchable": false, "bSortable": false } // Operações
                         </c:if>
                     ]
      });
      $('#txt_codigo_nome').focus();
  });
  </script>
</head>
<body>
  <p align="right">
    <c:if test="${popup != '1'}">
      <input type="button" value="Incluir" onClick="window.location.href='${linkCadastro}'" class="button">
    </c:if> 
  </p>

  <form name="form_pesquisar_fornecedores" id="form_pesquisar_fornecedores" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="popup" value="${popup}"/>
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>

  <%@include file="../../comum/processando.jsp" %>

  <center>
  <div id="tabs">
    <ul>
      <li><a href="#${pesquisarCodigoNome}">Por Código ou Nome</a></li>
      <li><a href="#${pesquisarTelefoneEmail}"">Por Telefone ou Email</a></li>
      <li><a href="#${pesquisarRuaCep}">Por Rua ou CEP</a></li>
      <li><a href="#${pesquisarCidade}">Por Cidade</a></li>
    </ul>
    <div id="${pesquisarCodigoNome}">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="label">
            <center>
            Código ou Nome: <mtw:inputText name="txt_codigo_nome" size="40" maxlength="100" onkeyup="isUpper(this);" />
            </center>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="${pesquisarCodigoNome}" type="submit" value="Pesquisar" class="button" >
          </td>
        </tr>
      </table>
    </div>
    <div id="${pesquisarTelefoneEmail}"">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="label">
            <center>
            Telefone ou Email: <mtw:inputText name="txt_telefone_email" size="40" maxlength="100" />
            </center>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="${pesquisarTelefoneEmail}" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </div>
    <div id="${pesquisarRuaCep}">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="label">
            <center>
            Rua ou CEP: <mtw:inputText name="txt_rua_cep" size="40" maxlength="100" onkeyup="isUpper(this);" />
            </center>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="${pesquisarRuaCep}" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </div>
    <div id="${pesquisarCidade}">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <%@include file="../../util/buscar/cidade.jsp" %>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="${pesquisarCidade}" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </div>
  </div>
  <br>
  <c:if test="${listaFornecedores != null}">
    <datatables:table data="${listaFornecedores}" htmlTableId="fornecedorTable" dataObjectId="fornecedor">
      <datatables:column property="id" title="#" sortable="true"/>
      <c:if test="${popup eq '1'}">
        <datatables:column title="Nome" sortable="true">
          <a href="javascript:returnPopupFornecedor('${fornecedor.id}','${fornecedor.nome}')">${fornecedor.nome}</a>
        </datatables:column>
      </c:if>
      <c:if test="${popup != '1'}">
        <datatables:column property="nome" title="Nome" sortable="true"/>
      </c:if>
      <datatables:column title="Endereço" sortable="true">${fornecedor.rua} ${fornecedor.numero} ${fornecedor.complemento}</datatables:column>
      <datatables:column property="bairro" title="Bairro" sortable="true"/>
      <datatables:column property="cep" title="CEP" sortable="true"/>
      <datatables:column title="Cidade" sortable="true">${fornecedor.cidade.nome}-${fornecedor.cidade.estado.sigla}</datatables:column>
      <datatables:column property="telefone" title="Telefone" sortable="true"/>
      <datatables:column property="email" title="E-mail" sortable="true"/>
      <c:if test="${popup != '1'}">
        <datatables:column title="Operações" sortable="false" filterable="false">
          <center>
          <a href="javascript:excluir(${fornecedor.id},'${fornecedor.nome}')"><img src="images/excluir.gif" border="0"/></a>
          <a href="javascript:alterar(${fornecedor.id})"><img src="images/salvar.png" border="0"/></a>
          </center>
        </datatables:column>
      </c:if>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>