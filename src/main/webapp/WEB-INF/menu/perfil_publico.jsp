<%@page import="br.com.alphadev.util.ConfigHelper"%>
<h1>Menu Principal</h1>
<ul>
  <li><a href="<%=request.getContextPath()%>/login.abrir.action">Login</a></li>
  <li><a href="<%=ConfigHelper.get().getString("sistema.paginaEmpresa")%>">Empresa</a></li>
  <li><a href="http://www.alphadev.com.br">Desenvolvimento</a></li>
</ul>