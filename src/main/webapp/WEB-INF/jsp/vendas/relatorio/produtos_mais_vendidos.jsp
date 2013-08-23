<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/relatorio.produtosMaisVendidos.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:tabPanelConfig skin="win2k"/>
  <mtw:inputDateConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Exibe os produtos mais vendidos no período">
  <title>Produtos Mais Vendidos</title>
</head>
<body>
  <form name="form_pesquisar" method="post" action="${linkPesquisar}">
  <p align="right">
    <mtw:select name="cbo_vista_prazo" list="vistaPrazo" emptyField="true" style="font-size:8pt;"/>
  </p>
  <center>
  <mtw:tabPanel name="panelProdutosVendidos">
    <mtw:tabPage caption="Por Vendedor" name="pageVendedor">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td>Vendedor</td>
          <td class="label">
            <%@include file="../../util/buscar/funcionario.jsp" %>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
    <mtw:tabPage caption="Por Cliente" name="pageCliente">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td>Cliente</td>
          <td class="label">
            <%@include file="../../util/buscar/cliente.jsp" %>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
    <mtw:tabPage caption="Por Cidade" name="pageCidade">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <%@include file="../../util/buscar/cidade.jsp" %>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
  </mtw:tabPanel>
  <div class="tab-pane"><div class="tab-page">
    <table class="form">
      <tr>
        <td>
          Período de
          <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial" size="10" />
          até
          <mtw:inputDate name="txt_data_final" id="txt_data_final" size="10" />
          &nbsp;&nbsp;&nbsp;&nbsp;
        </td>
      </tr>
    </table>
  </div></div>
  <br><br>
  <c:if test="${fn:length(listaProdutosMaisVendidos) > 0}">
    <mtw:radiobuttons name="lst_tipo_pesquisa" list="pesquisaTotalProdutos" extra="onChange=javascript:document.form_pesquisar.submit();"/>
    <display:table name="listaProdutosMaisVendidos" pagesize="50" export="true" id="produto" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
      <display:column property="[0]" title="Referência"/>
      <display:column property="[1]" title="Produto"/>
      <display:column property="[2]" title="Quantidade" total="true" style="text-align: center;" headerClass="center"/>
      <display:column property="[3]" title="Preço" total="true" style="text-align: right;" headerClass="right"/>
    </display:table>
  </c:if>
  </center>
  </form>
</body>
</html>