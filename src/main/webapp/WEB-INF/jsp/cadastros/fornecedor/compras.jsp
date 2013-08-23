<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/fornecedor.vendas.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="decorator" content="popup">
  <title>Pesquisar Vendas Por Cliente</title>
</head>
<body>
  <center>
  <display:table name="listaCompras" pagesize="50" export="false" id="venda" requestURI="${linkPesquisar}">
    <display:column property="id" title="ID" style="text-align: center;" headerClass="center"/>
    <display:column property="prazoPagamento.descricao" title="Prazo Pgto." style="text-align: center;" headerClass="center"/>
    <display:column title="Data" style="text-align: center;" headerClass="center">
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${compra.emissao}"/>
    </display:column>
    <display:column title="Total" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${compra.total}</fmt:formatNumber>
    </display:column>
  </display:table>
  </center>
</body>
</html>