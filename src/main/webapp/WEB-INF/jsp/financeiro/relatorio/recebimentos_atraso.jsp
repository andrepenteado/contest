<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/relatorio.recebimentosAtraso.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Lista as contas a receber em atraso por ano">
  <title>Recebimentos em Atraso</title>
</head>
<body>
  <center>
  <form name="form_pesquisar_atrasos" method="post" action="${linkPesquisar}">
     Ano: <mtw:select name="lst_ano" list="anoVenda" emptyField="true" defValue="${ano}" extra="onchange=javascript:document.form_pesquisar_atrasos.submit();"/>
  </form>
  <br>
  <display:table name="listaReceber" pagesize="150" export="true" id="receber" decorator="org.displaytag.decorator.TotalTableDecorator" requestURI="${linkPesquisar}">
    <display:column title="ID" property="id"/>
    <display:column title="Descrição">
      ${receber.descricao == null || receber.descricao == '' ? receber.venda.cliente.nome : receber.descricao}
    </display:column>
    <display:column title="Tipo">
      ${receber.tipoConta == null ? 'Venda' : receber.tipoConta.descricao}
    </display:column>
    <display:column title="Vencimento" style="text-align: center;" headerClass="center">
      <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.vencimento}"/>
    </display:column>
    <display:column title="Total" property="valor" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Recebido" property="valorRecebido" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Devido" property="valorDevido" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
  </display:table>
  </center>
  </form>
</body>
</html>