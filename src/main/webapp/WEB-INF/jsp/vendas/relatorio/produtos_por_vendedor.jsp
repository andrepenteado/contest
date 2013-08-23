<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/relatorio.produtosPorVendedor.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Exibe os produtos vendidos no período por vendedor">
  <title>Produtos Vendidos Por Vendedor</title>
</head>
<body>
  <form name="form_pesquisar_pvv" method="post" action="${linkPesquisar}">
  <center>
  <table class="form">
    <tr>
      <td width="45%" class="label">Data Inicial </td>
      <td width="55%">
        <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial" size="15" />
      </td>
    </tr>
    <tr>
      <td width="45%" class="label">Data Final </td>
      <td width="55%">
        <mtw:inputDate name="txt_data_final" id="txt_data_final" size="15" />
      </td>
    </tr>
    <tr>
      <td width="45%" class="label">Funcionário </td>
      <td width="55%">
        <mtw:select list="listaFuncionarios" name="cbo_funcionario" id="cbo_funcionario" emptyField="true" />
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
  <c:if test="${fn:length(listaProdutosVendedor) > 0}">
    <mtw:radiobuttons name="lst_tipo_pesquisa" list="pesquisaTotalProdutos" extra="onChange=javascript:document.form_pesquisar_pvv.submit();"/>
    <display:table name="listaProdutosVendedor" pagesize="50" export="true" id="comissao" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
      <display:column property="[0]" title="Vendedor"/>
      <display:column property="[1]" title="Produto"/>
      <display:column property="[2]" title="Quantidade" style="text-align: center;" headerClass="center" total="true"/>
      <display:column property="[3]" title="Sub-Total" style="text-align: right;" headerClass="right" total="true"/>    </display:table>
  </c:if>
  </center>
  </form>
</body>
</html>