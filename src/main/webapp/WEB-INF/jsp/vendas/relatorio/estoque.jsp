<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="linkEmissao"><%=request.getContextPath()%>/vendas/relatorio.estoque.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:tabPanelConfig skin="win2k"/>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Relatório com dados do estoque de produtos">
  <title>Estoque de Produtos</title>
</head>
<body>
  <center>
  <mtw:tabPanel name="panelEstoque">
    <mtw:tabPage caption="Por Grupo de Produto" name="pageGrupoProduto">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td>
            <center>
            Grupo de Produto: <mtw:select list="listaGruposProduto" name="cbo_grupo_produto" id="cbo_grupo_produto" emptyField="true" />
            </center>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="barrabotao">
            <input name="${pesquisarCodigoReferenciaDescricao}" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
    <mtw:tabPage caption="Por Fornecedor" name="pageFornecedor">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="label">Fornecedor</td>
          <td>
            <center>
            <%@include file="../../util/buscar/fornecedor.jsp" %>
            </center>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="barrabotao" colspan="2">
            <input name="${pesquisarCodigoReferenciaDescricao}" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
  </mtw:tabPanel>
  <br>
  <c:if test="${listaEstoque != null}">
    <display:table name="listaEstoque" export="true" id="estoque" requestURI="${linkEmissao}">
      <display:column property="produto.descricao" title="Descrição" />
      <display:column title="Custo à Vista" style="text-align: center;" headerClass="right">
        <fmt:formatNumber pattern="#0.00">${estoque.produto.custoVista}</fmt:formatNumber>
      </display:column>
      <display:column title="Custo à Prazo" style="text-align: center;" headerClass="right">
        <fmt:formatNumber pattern="#0.00">${estoque.produto.custoPrazo}</fmt:formatNumber>
      </display:column>
      <display:column property="specOut" title="Qtd.Vista" style="text-align: center;" headerClass="center"/>
      <display:column property="specIn" title="Qtd.Prazo" style="text-align: center;" headerClass="center"/>
      <display:column title="Físico" style="text-align: center;" headerClass="center">
        ${estoque.specIn + estoque.specOut}
      </display:column>
      <display:column title="Final" style="text-align: right;" headerClass="right">
        <fmt:formatNumber pattern="#,##0.00">${estoque.specIn * estoque.produto.custoPrazo}</fmt:formatNumber>
      </display:column>
      <display:column title="Gerencial" style="text-align: right;" headerClass="right">
        <fmt:formatNumber pattern="#,##0.00">${(estoque.specOut > 0 ? estoque.specOut * estoque.produto.custoVista : 0) + (estoque.specIn * estoque.produto.custoPrazo)}</fmt:formatNumber>
      </display:column>
    </display:table>
  </c:if>
  </center>
</body>
</html>