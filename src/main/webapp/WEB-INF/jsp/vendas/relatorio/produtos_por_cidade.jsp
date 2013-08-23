<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/relatorio.produtosPorCidade.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Exibe os produtos vendidos no período por cidade">
  <title>Produtos Vendidos Por Cidade</title>
</head>
<body>
  <form name="form_pesquisar" method="post" action="${linkPesquisar}">
  <center>
  <table class="form">
    <tr>
      <td width="40%" class="label">Data Inicial </td>
      <td width="60%">
        <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial" size="15" />
      </td>
    </tr>
    <tr>
      <td class="label">Data Final </td>
      <td>
        <mtw:inputDate name="txt_data_final" id="txt_data_final" size="15" />
      </td>
    </tr>
    <tr>
      <td class="label">Cidade </td>
      <td>
        <mtw:inputText name="txt_cidade" id="txt_cidade" size="30" />
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
  <br><br>
  <c:if test="${fn:length(listaProdutosCidade) > 0}">
    <mtw:radiobuttons name="lst_tipo_pesquisa" list="pesquisaTotalProdutos" extra="onChange=javascript:document.form_pesquisar.submit();"/>
    <display:table name="listaProdutosCidade" pagesize="50" export="true" id="produto" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
      <display:column property="[0]" title="Referência"/>
      <display:column property="[1]" title="Produto"/>
      <display:column property="[2]" title="Cidade"/>
      <display:column property="[3]" title="Quantidade" total="true" style="text-align: center;" headerClass="center"/>
      <display:column property="[4]" title="Preço" total="true" style="text-align: right;" headerClass="right"/>
    </display:table>
  </c:if>
  </center>
  </form>
</body>
</html>