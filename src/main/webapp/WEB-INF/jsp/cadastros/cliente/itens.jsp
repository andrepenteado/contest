<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/cliente.itens.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="decorator" content="popup">
  <title>Pesquisar Itens Vendidos Por Cliente</title>
</head>
<body>
  <center>
  <display:table name="listaItens" pagesize="50" export="false" id="item" requestURI="${linkPesquisar}">
    <display:column title="Data" style="text-align: center;" headerClass="center">
      <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${item.venda.dataLancamento}"/>
    </display:column>
    <display:column property="produto.descricao" title="Produto"/>
    <display:column property="venda.formaPagamento" title="Forma Pgto." style="text-align: center;" headerClass="center"/>
    <display:column property="quantidade" title="Qtd." style="text-align: center;" headerClass="center"/>
    <display:column title="Preço" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${item.valorVenda}</fmt:formatNumber>
    </display:column>
    <display:column title="Sub-Total" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${item.valorVenda * item.quantidade}</fmt:formatNumber>
    </display:column>
  </display:table>
  </center>
</body>
</html>