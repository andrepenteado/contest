<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkDetalhar"><%=request.getContextPath()%>/financeiro/relatorio.detalharTipoConta.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Exibe contas pelo tipo no período detalhadamente">
  <title>Detalhes de Contas por Tipo no Período</title>
</head>
<body>
  <center>
  <c:if test="${fn:length(lista) > 0}">
    <display:table name="lista" pagesize="150" export="true" id="objeto" requestURI="${linkDetalhar}" decorator="org.displaytag.decorator.TotalTableDecorator">
    </display:table>
  </c:if>
  </center>
</body>
</html>