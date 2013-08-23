<%@page import="br.com.alphadev.contest.KGlobal"%>
<%@page import="org.mentawai.action.BaseLoginAction"%>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw"%>

<%
if (BaseLoginAction.isLogged(session)) {
%>
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_ADMINISTRATIVO%>">
     <%@include file="../WEB-INF/menu/perfil_administrativo.jsp"%>
   </mtw:hasAuthorization>
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_COMPRAS%>">
     <%@include file="../WEB-INF/menu/perfil_compras.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_FINANCEIRO%>">
     <%@include file="../WEB-INF/menu/perfil_financeiro.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDAS%>">
     <%@include file="../WEB-INF/menu/perfil_vendas.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>">
     <%@include file="../WEB-INF/menu/perfil_vendedor.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_ESTOQUE%>">
     <%@include file="../WEB-INF/menu/perfil_estoque.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_RECEPCAO%>">
     <%@include file="../WEB-INF/menu/perfil_recepcao.jsp"%>
   </mtw:hasAuthorization> 
   <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO%>">
     <%@include file="../WEB-INF/menu/perfil_superusuario.jsp"%>
   </mtw:hasAuthorization> 
   <h1>Usuário</h1>
   <ul>
     <li><a href="<%=request.getContextPath()%>/comum.abrirAlterarSenha.action">Alterar Senha</a></li>
     <li><a href="<%=request.getContextPath()%>/comum.sair.action">Sair</a></li>
   </ul> <%
} 
else {  %>
   <%@include file="../WEB-INF/menu/perfil_publico.jsp"%> <%
} %>