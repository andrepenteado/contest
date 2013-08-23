<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/pagar.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/financeiro/pagar.cadastro.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o período para pesquisar contas a pagar">
  <title>Contas a Pagar</title>
  <script language="JavaScript" type="text/javascript">
  function darBaixa(id) {
      document.form_pesquisar_pagar.action = '<%=request.getContextPath()%>/financeiro/pagar.darBaixa.action?id_pagar=' + id;
      document.form_pesquisar_pagar.submit();
  }
  function alterar(id) {
      document.form_pesquisar_pagar.action = '${linkCadastro}?txt_id=' + id;
      document.form_pesquisar_pagar.submit();
  }
  $(function() {
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
  });
  </script>
</head>
<body>
  <p align="right"><input type="button" value="Nova Conta" onClick="window.location.href='${linkCadastro}'" class="button"></p>
  <form name="form_pesquisar_pagar" method="post" action="${linkPesquisar}">
  <center>

  <%@include file="../../util/pesquisar/pagar.jsp" %>

  <display:table name="listaPagar" pagesize="${naoPaginar eq '1' ? 1000 : 50}" export="true" id="pagar" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
    <display:column title="Descrição" sortable="true">
      ${pagar.descricao == null || pagar.descricao == '' ? pagar.compra.fornecedor.nome : pagar.descricao}
    </display:column>    <display:column title="Tipo">
      ${pagar.tipoConta == null ? 'Compra' : pagar.tipoConta.descricao}
    </display:column>
    <display:column title="Vencimento" sortable="true" style="text-align: center;" headerClass="center">      <fmt:formatDate pattern="dd/MM/yyyy" value="${pagar.vencimento}"/>    </display:column>    <display:column title="Total" property="valor" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Pago" property="valorPago" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Devido" property="valorDevido" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Alterar" style="text-align: center;" headerClass="center">
      <a href="javascript:alterar(${pagar.id});"><img src="images/salvar.png" border="0"/></a>
    </display:column>
    <display:column title="Dar Baixa" style="text-align: center;" headerClass="center">
      <a href="javascript:darBaixa(${pagar.id});"><img src="images/dar_baixa.png" border="0"/></a>
    </display:column>
  </display:table>
  </center>
  </form>
</body>
</html>