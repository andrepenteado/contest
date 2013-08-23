<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="br.com.alphadev.contest.KGlobal"%>

<c:set var="popup"><%=request.getParameter("popup")%></c:set>
<c:set var="linkPesquisar"><%=request.getContextPath()%>/cadastros/cfop.pesquisar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Realizar pesquisa de produtos">
  <c:if test="${popup eq '1'}">
    <meta name="decorator" content="popup">
  </c:if>
  <title>CFOP</title>
  <script language="JavaScript" type="text/javascript">
  function returnPopupCFOP(id, codigo, descricao) {
      window.parent.document.forms[0].txt_id_cfop.value = id;
      window.parent.document.forms[0].txt_codigo_cfop.value = codigo;
      window.parent.document.forms[0].txt_descricao_cfop.value = descricao;
      window.parent.closePopup();
  }
  $(function(){
      $('#tabs').tabs();
  });
  </script>
</head>
<body onload="javascript:document.form_pesquisar_cfop.txt_codigo_descricao.focus();">
  <form name="form_pesquisar_cfop" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="popup" value="${popup}"/>

  <center>
  
  <%@include file="../../util/pesquisar/cfop.jsp" %>
  
  <br>
  <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaCFOP"/>
  <c:if test="${listaCFOP != null}">
    <display:table name="listaCFOP" pagesize="${naoPaginar eq '1' ? 1000 : 50}" export="true" id="cfop" requestURI="${linkPesquisar}">
      <display:column property="codigo" title="Código" sortable="true" style="text-align: center; width: 70px;" headerClass="center"/>

      <display:column title="Descrição" sortable="true">
          <a href="javascript:returnPopupCFOP(${cfop.id},'${cfop.codigo}','${cfop.descricao}')">${cfop.descricao}</a>
      </display:column>
    </display:table>
  </c:if>
  </center>
  </form>
</body>
</html>