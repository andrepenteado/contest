<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="decorator" content="popup">
  <title>Catalogo de Produtos</title>
</head>
<body>
  <center>
  <table>
    <tr>
      <td colspan="3"><hr></td>
    </tr>
    <c:forEach items="${listaProdutos}" var="produto">
    <tr>
      <td>
        <img src="<%=request.getContextPath()%>/public.visualizarFoto.action?txt_id=${produto.id}" alt="foto" width="250" height="200" />
      </td>
      <td>
        &nbsp;&nbsp;&nbsp;&nbsp;
      </td>
      <td valign="center">
        <b>Descrição:</b> ${produto.descricao}<br><br>
        <b>Capacidade:</b> ${produto.capacidade} ml<br><br>
        <b>Largura:</b> <fmt:formatNumber pattern="#0.###">${produto.largura}</fmt:formatNumber> cm<br><br>
        <b>Altura:</b> <fmt:formatNumber pattern="#0.###">${produto.altura}</fmt:formatNumber> cm<br><br>
        <b>Comprimento:</b> <fmt:formatNumber pattern="#0.###">${produto.comprimento}</fmt:formatNumber> cm<br><br>
        <b>Diâmetro:</b> <fmt:formatNumber pattern="#0.###">${produto.diametro}</fmt:formatNumber> cm
      </td>
    </tr>
    <c:if test="${produto.observacao != null}">
      <tr>
        <td colspan="3"><center><h6>${produto.observacao}</h6></center></td>
      </tr>
    </c:if>
    <tr>
      <td colspan="3"><hr></td>
    </tr>
    </c:forEach>
  </table>
  </center>
</body>
</html>