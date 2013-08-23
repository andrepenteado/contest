<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/estoque/relatorio.sintetico.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Relatório sintético do estoque de produtos">
  <title>Relatório Sintético</title>
</head>
<body>
  <center>

  <form name="form_pesquisar_produtos" method="post" action="${linkPesquisar}">

  <%@include file="../../util/pesquisar/produto.jsp" %>

  <div class="tab-pane"><div class="tab-page">
    <table class="form">
      <tr><td>
      Data: <mtw:inputDate name="txt_data" id="txt_data"/>
      </td></tr>
    </table>
  </div></div>

  <br>

  <c:if test="${listaEstoque != null}">
    <display:table name="listaEstoque" export="true" id="movimentoEstoque" requestURI="${linkPesquisar}">
      <display:column property="produto.descricao" title="Descrição" />
      <display:column title="Movimento">
        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${movimentoEstoque.data}"/>
      </display:column>
      <display:column title="Custo à Vista" style="text-align: center;" headerClass="right">
        <fmt:formatNumber pattern="#0.00">${movimentoEstoque.produto.custoVista}</fmt:formatNumber>
      </display:column>
      <display:column title="Custo à Prazo" style="text-align: center;" headerClass="right">
        <fmt:formatNumber pattern="#0.00">${movimentoEstoque.produto.custoPrazo}</fmt:formatNumber>
      </display:column>
      <display:column property="specOut" title="Qtd.Vista" style="text-align: center;" headerClass="center"/>
      <display:column property="specIn" title="Qtd.Prazo" style="text-align: center;" headerClass="center"/>
      <display:column title="Físico" style="text-align: center;" headerClass="center">
        ${movimentoEstoque.specIn + movimentoEstoque.specOut}
      </display:column>
      <display:column title="Final" style="text-align: right;" headerClass="right">
        <fmt:formatNumber pattern="#,##0.00">${movimentoEstoque.specIn * movimentoEstoque.produto.custoPrazo}</fmt:formatNumber>
      </display:column>
      <display:column title="Gerencial" style="text-align: right;" headerClass="right">
        <fmt:formatNumber pattern="#,##0.00">${(movimentoEstoque.specOut * movimentoEstoque.produto.custoVista) + (movimentoEstoque.specIn * movimentoEstoque.produto.custoPrazo)}</fmt:formatNumber>
      </display:column>
    </display:table>
  </c:if>
  
  </form>
  </center>
</body>
</html>