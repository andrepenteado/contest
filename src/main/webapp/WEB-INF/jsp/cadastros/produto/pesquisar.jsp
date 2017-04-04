<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.github.andrepenteado.contest.KGlobal"%>

<c:set var="popup"><%=request.getParameter("popup")%></c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/cadastros/produto.cadastro.action</c:set>
<c:set var="linkExcluir"><%=request.getContextPath()%>/cadastros/produto.excluir.action</c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/produto.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Realizar pesquisa de produtos">
  <c:if test="${popup eq '1'}">
    <meta name="decorator" content="popup">
  </c:if>
  <title>Produtos</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_produtos").attr("action", "${linkCadastro}");
      $("#form_pesquisar_produtos").submit();
  }
  function excluir(id, nome) {
      $('<p>Confirma exclusão do produto ' + nome + '?</p>').dialog({
          title: "Excluir Produto?",
          resizable: false,
          modal: true,
          buttons: {
              "Excluir": function() {
                  $("#txt_id").val(id);
                  $("#form_pesquisar_produtos").attr("action", "${linkExcluir}");
                  $("#form_pesquisar_produtos").submit();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
  }
  function returnPopupProduto(id, referencia, descricao, preco) {
      window.parent.document.forms[0].txt_id_produto.value = id;
      window.parent.document.forms[0].txt_referencia_produto.value = referencia;
      window.parent.document.forms[0].txt_descricao_produto.value = descricao;
      window.parent.document.forms[0].txt_preco_produto.value = preco;
      // Gambi, nao sei porque, precisa executar 2 vezes para sempre fechar o popup
      parent.$("#buscar-produto", window.parent.document).dialog('close');
      parent.$('#buscar-produto', window.parent.document).remove();
      parent.$("#buscar-produto", window.parent.document).dialog('close');
      parent.$('#buscar-produto', window.parent.document).remove();
  }
  $(function() {
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
      $('#produtoTable').dataTable({
          <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
          "aoColumns": [
              { "bVisible": false }, // Código
              { "bVisible": true },  // Referência
              { "bVisible": false }, // Código Externo
              { "bVisible": false }, // NCM
              { "bVisible": true },  // Descrição
              { "bVisible": false }, // Grupo
              { "bVisible": false }, // Tabela de Preços
              { "bVisible": true }, // Custo/Venda Vista
              { "bVisible": false }, // Custo/Venda Prazo
              <c:if test="${produtoVenda == null}">
                { "bVisible": true }, // Custo/Venda Vista
                { "bVisible": false }, // Custo/Venda Prazo
              </c:if>
              <mtw:hasAuthorization groups='<%=KGlobal.CATEGORIA_ADMINISTRATIVO + "," + KGlobal.CATEGORIA_ESTOQUE + "," + KGlobal.CATEGORIA_SUPERUSUARIO%>'>
                { "bVisible": false }, // In
                { "bVisible": false }, // Out
              </mtw:hasAuthorization>
              { "bVisible": true }, // Estoque
              <c:if test="${popup != '1'}">
                { "bSearchable": false, "bSortable": false } // Operações
              </c:if>
          ]
     });
     $('#txt_codigo_referencia_descricao').focus();
  });
  </script>
</head>
<body>
  <form name="form_pesquisar_produtos" id="form_pesquisar_produtos" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="popup" value="${popup}"/>
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>

  <p align="right">
    <c:if test="${popup != '1'}">
      <input type="button" value="Incluir" onClick="window.location.href='${linkCadastro}'" class="button">
      <mtw:input type="hidden" name="hid_ativo" value="0"/>
    </c:if> 
  </p>

  <center>
  
  <%@include file="../../util/pesquisar/produto.jsp" %>
  
  <br>

  <c:if test="${listaProdutos != null}">
    <datatables:table data="${listaProdutos}" htmlTableId="produtoTable" dataObjectId="produto">
      <datatables:column property="id" title="#" sortable="true"/>
      <datatables:column property="referencia" title="Referência" sortable="true"/>
      <datatables:column property="codigoExterno" title="Cód.Ext." sortable="true"/>
      <datatables:column property="ncm" title="NCM" sortable="true"/>
      <c:if test="${popup eq '1'}">
        <datatables:column title="Descrição" sortable="true">
          <c:if test="${produtoVenda == false}">
            <a href="javascript:returnPopupProduto(${produto.id},'${produto.referencia}','${produto.descricao}','${fn:replace(produto.custoPrazo,".",",")}')">${produto.descricao}</a>
          </c:if>
          <c:if test="${produtoVenda == true}">
            <a href="javascript:returnPopupProduto(${produto.id},'${produto.referencia}','${produto.descricao}','${fn:replace(produto.vendaPrazo,".",",")}')">${produto.descricao}</a>
          </c:if>
        </datatables:column>
      </c:if>
      <c:if test="${popup != '1'}">
        <datatables:column title="Descrição" sortable="true">
          <div style="${produto.grupoProduto == null ? 'text-decoration: line-through;' : ''}">${produto.descricao}</div>
        </datatables:column>
      </c:if>
      <datatables:column title="Grupo" sortable="true">${produto.grupoProduto.descricao}</datatables:column>
      <datatables:column title="Tabela" sortable="true">${produto.tabelaPreco.descricao}</datatables:column>
      <c:if test="${produtoVenda == false || produtoVenda == null}">
        <datatables:column title="Custo Vista" sortable="true">
          <fmt:formatNumber pattern="#0.00">${produto.custoVista}</fmt:formatNumber>
        </datatables:column>
        <datatables:column title="Custo Prazo" sortable="true">
          <fmt:formatNumber pattern="#0.00">${produto.custoPrazo}</fmt:formatNumber>
        </datatables:column>
      </c:if>
      <c:if test="${produtoVenda == true || produtoVenda == null}">
        <datatables:column title="Venda Vista" sortable="true">
          <fmt:formatNumber pattern="#0.00">${produto.precoPadraoVista}</fmt:formatNumber>
        </datatables:column>
        <datatables:column title="Venda Prazo" sortable="true">
          <fmt:formatNumber pattern="#0.00">${produto.precoPadraoPrazo}</fmt:formatNumber>
        </datatables:column>
      </c:if>
      <mtw:hasAuthorization groups='<%=KGlobal.CATEGORIA_ADMINISTRATIVO + "," + KGlobal.CATEGORIA_ESTOQUE + "," + KGlobal.CATEGORIA_SUPERUSUARIO%>'>
        <datatables:column property="totalSpecIn" title="In" sortable="true"/>
        <datatables:column property="totalSpecOut" title="Out" sortable="true"/>
      </mtw:hasAuthorization>
      <datatables:column property="quantidadeEstoque" title="Estoque" sortable="true"/>
      <c:if test="${popup != '1'}">
        <datatables:column title="Operações" sortable="false" filterable="false">
          <center>
          <a href="javascript:excluir(${produto.id},'${produto.descricao}')"><img src="images/excluir.gif" border="0"/></a>
          <a href="javascript:alterar(${produto.id})"><img src="images/salvar.png" border="0"/></a>
          </center>
        </datatables:column>
      </c:if>
    </datatables:table>
  </c:if>
  </center>
  </form>
</body>
</html>