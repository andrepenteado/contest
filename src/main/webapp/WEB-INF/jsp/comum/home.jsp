<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.mentawai.action.BaseLoginAction"%>
<%@page import="br.com.alphadev.util.ConfigHelper"%>
<%@page import="br.com.alphadev.contest.entity.Funcionario"%>
<%@page import="br.com.alphadev.util.UsuarioLogadoWrapper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.alphadev.contest.entity.Categoria"%>

<c:set var="linkDestino"><%=request.getContextPath()%>/comum.carregarModulos.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
  <meta name="header" content="Bem-Vindo ao sistema <%=ConfigHelper.get().getString("sistema.nome")%>">
  <title>Ol&aacute; ${user.funcionario.nome}!</title>
</head>
<body> 
  <center> <%
  if (!BaseLoginAction.isLogged(session)) { %>
    <table>
      <tr>
        <th><h1>Ol&aacute; Visitante!</h1></th>
      </tr>
      <tr>
        <td>
          <h3>Por favor, efetue <a href="<%=ConfigHelper.get().getString("sso.loginPage")%>" title="Login">login</a>.</h3>
        </td>
      </tr>
    </table> <%
  } 
  else { %>
    <c:if test="${user.categoriaAtual == null}">
      <table style="width: 80%; margin-left: 9%; margin-right: 9%;">
        <tr>
          <td>
            <h3>
            <center>Selecione seu perfil de acesso abaixo:</center>
            </h3>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>
            <center>
            <table style="border: 0px; border-collapse: collapse;">
              <tr> <%
                Funcionario usuario = ((UsuarioLogadoWrapper)session.getAttribute("user")).getFuncionario();
                ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>(usuario.getCategorias());
                int colunas = 3;
                int count = 0;
                for (Categoria uc : listaCategorias) {  %>
                    <td style="border: 0px; padding: 5px; text-align: center;" width="250px">
                      <center>
                      <a href="${linkDestino}?id_categoria=<%=uc.getId().intValue()%>">
                        <img src="images/logo_categoria.gif" border="0" /><br />
                        <b><%=uc.getDescricao()%></b>
                      </a>
                      </center>
                    </td> <%
                    count++;
                    if (count == colunas) {
                        count = 0; %>
                        </tr><tr> <%
                    }
                } %>
              </tr>
            </table>
            </center>
          </td>
        </tr>
      </table>
    </c:if>
    <c:if test="${user.categoriaAtual != null}">
      <table style="width: 80%; margin-left: 9%; margin-right: 9%;">
        <tr>
          <td>
            <h3>
            <center>Funcionário ${user.funcionario.empresa.razaoSocial}</center>
            </h3>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>
            <h3>
            <center>Você está conectado: ${user.categoriaAtual.descricao}</center>
            </h3>
          </td>
        </tr>
      </table>
    </c:if>
  <% } %>
  </center>
</body>
</html>