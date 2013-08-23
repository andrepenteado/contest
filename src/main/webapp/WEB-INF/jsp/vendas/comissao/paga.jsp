<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/comissao.paga.action</c:set>
<c:set var="linkDetalhes"><%=request.getContextPath()%>/vendas/comissao.detalharPagas.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Escolha o vendedor para exibir as comissões pagas ao funcionário">
  <title>Comissão de Funcionário</title>
  <mtw:inputMoneyConfig />
  <script type="text/javascript">
  function detalhar(dataPagamento) {
      $("#hid_data_pagamento").val(dataPagamento);
      $("#form_comissao_paga").attr("action", "${linkDetalhes}");
      $("#form_comissao_paga").submit();
  }
  $(function() {
      $('#tabs').tabs();
  });
  </script>
</head>
<body onload="javascript:document.form_comissao_paga.txt_id_funcionario.focus();">
  <center>
  <form name="form_comissao_paga" id="form_comissao_paga" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="hid_data_pagamento" id="hid_data_pagamento"/>
  <div id="tabs">
    <ul>
      <li><a href="#tabVendedor">Vendedor</a></li>
    </ul>
    <div id="tabVendedor">
	  <table class="form">
	    <tr>
	      <td class="label">Vendedor</td>
	      <td>
	        <%@include file="../../util/buscar/funcionario.jsp" %>
	        &nbsp;&nbsp;&nbsp;
	        <input name="btn_pesquisar" type="submit" value="Ver Pagamentos" class="button">
	      </td>
          </tr>
        <tr><td>&nbsp;</td></tr>
	  </table>
	</div>
  </div>
  <br>
  <c:if test="${listaComissoesPagas != null}">
    <display:table name="${listaComissoesPagas}" export="false" id="objeto" decorator="org.displaytag.decorator.TotalTableDecorator">
      <display:column title="No." style="text-align: center;" headerClass="center">${objeto_rowNum}</display:column>
      <display:column title="Data Pagamento" style="text-align: center;" headerClass="center">
        <a href="javascript:detalhar('<fmt:formatDate pattern="dd/MM/yyyy" value="${objeto[0]}"/>');">
        <fmt:formatDate pattern="dd/MM/yyyy" value="${objeto[0]}"/>
        </a>
      </display:column>
      <display:column title="Total Comissão" property="[1]" format="{0,number,0.00}" style="text-align: right;" headerClass="center" total="true"/>
    </display:table>
  </c:if>
  </form>
  </center>
</body>
</html>