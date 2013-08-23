<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/estoque/movimento.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/estoque/movimento.cadastro.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar Movimentação de Estoque">
  <title>Movimentação de Estoque</title>
  <script language="JavaScript" type="text/javascript">  
  function exibir(id, dtInicail, dtFinal) {
      pCtrl.search('<%=request.getContextPath()%>/estoque/movimento.pesquisarDetalhado.action?txt_id=' + id, 600, 400, new Array());
  }  
  </script>
</head>
<body>
  <p align="right">
    <input type="button" value="Lançar Estoque" onClick="window.location.href='${linkCadastro}'" class="button">
  </p>

  <form name="form_pesquisar_movimentacao_estoque" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id">
  <center>

  <%@include file="../../util/pesquisar/produto.jsp" %>

  <div class="tab-pane"><div class="tab-page">
    <table class="form">
      <tr><td>
      Data Inicial: <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial"/>
      &nbsp;&nbsp;&nbsp;
      Data Final: <mtw:inputDate name="txt_data_final" id="txt_data_final"/>
      </td></tr>
    </table>
  </div></div>

  <display:table name="listaMovimento" pagesize="50" export="true" id="movimentoEstoque" requestURI="${linkPesquisar}">
    <display:column title="Produto">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        ${movimentoEstoque.itemCompra.produto.descricao}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        ${movimentoEstoque.itemVenda.produto.descricao}
      </c:if>
    </display:column>
    <display:column title="Descrição">
      <c:choose>
        <c:when test="${movimentoEstoque.itemCompra != null && movimentoEstoque.itemCompra.compra != null}">
          ${movimentoEstoque.itemCompra.compra.fornecedor.nome}
        </c:when>
        <c:when test="${movimentoEstoque.itemVenda != null && movimentoEstoque.itemVenda.venda != null}">
          ${movimentoEstoque.itemVenda.venda.cliente.nome}
        </c:when>
        <c:otherwise>
          ${movimentoEstoque.tipoMovimentacao.descricao}
        </c:otherwise>
      </c:choose>
    </display:column>
    <display:column title="<center>Data</center>" style="text-align: center;">
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${movimentoEstoque.data}"/>
    </display:column>
    <display:column title="<center>Movimento</center>" style="text-align: center;">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        <c:if test="${movimentoEstoque.tipoMovimentacao != 'ESTORNO'}">+</c:if>
        <c:if test="${movimentoEstoque.tipoMovimentacao == 'ESTORNO'}">-</c:if>
        ${movimentoEstoque.itemCompra.quantidade}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        <c:if test="${movimentoEstoque.tipoMovimentacao != 'ESTORNO'}">-</c:if>
        <c:if test="${movimentoEstoque.tipoMovimentacao == 'ESTORNO'}">+</c:if>
        ${movimentoEstoque.itemVenda.quantidade}
      </c:if>
    </display:column>    
    <display:column property="tipoMovimentacao" title="<center>Tipo</center>" style="text-align: center;"/>
    <display:column title="<center>E/S</center>" style="text-align: center;">
      <c:if test="${movimentoEstoque.itemCompra != null}">Entrada</c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">Saída</c:if>
    </display:column>
    <display:column property="vistaPrazo" title="<center>E/S</center>" style="text-align: center;"/>
    <display:column title="<center>Estoque</center>" style="text-align: center;">
        ${movimentoEstoque.specIn + movimentoEstoque.specOut}
    </display:column>
    <%--
    <display:column title="<center>Detalhes</center>" style="text-align: center;" headerClass="center">
      <a href="javascript:exibir(${movimentoEstoque.id});"><img src="images/excluir.gif" border="0"/></a>
    </display:column>
    --%>
  </display:table>
  </center>
  </form>
</body>
</html>