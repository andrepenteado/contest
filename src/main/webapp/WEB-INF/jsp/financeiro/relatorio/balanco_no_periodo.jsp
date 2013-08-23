<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/relatorio.tipoContaPorPeriodo.action</c:set>
<c:set var="linkDetalhar"><%=request.getContextPath()%>/financeiro/relatorio.detalharTipoConta.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Exibe contas pelo tipo no período">
  <title>Contas por Tipo no Período</title>
  <script language="JavaScript" type="text/javascript">
  $(function() {
      $('#tabs').tabs();
  });
  </script>
</head>
<body onload="javascript:document.form_pesquisar_tcp.txt_data_inicial.focus();">
  <form name="form_pesquisar_tcp" method="post" action="${linkPesquisar}">
  <center>
  <div id="tabs">
    <ul>
      <li><a href="balancoPeriodo">Balanço no Período</a></li>
    </ul>
    <div id="balancoPeriodo">
      <table class="form">
        <tr>
          <td width="100" class="label">Data Inicial </td>
          <td width="200">
            <div class="error"><mtw:error field="txt_data_inicial"/></div>
            <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial" size="15" />
          </td>
          <td width="100" class="label">Data Final </td>
          <td>
            <div class="error"><mtw:error field="txt_data_final"/></div>
            <mtw:inputDate name="txt_data_final" id="txt_data_final" size="15" />
          </td>
        </tr>
        <tr>
          <td class="label">Periodicidade </td>
          <td>
            <div class="error"><mtw:error field="cbo_periodicidade"/></div>
            <mtw:select list="periodicidadeEstatistica" name="cbo_periodicidade" id="cbo_periodicidade" emptyField="true" />
          </td>
          <td class="label">Situação </td>
          <td>
            <div class="error"><mtw:error field="rad_situacao"/></div>
            <mtw:select list="situacaoConta" name="chk_situacao" id="chk_situacao" emptyField="true" />
          </td>
        </tr>
        <tr>
          <td class="label">Tipo de Conta </td>
          <td colspan="3">
            <div class="error"><mtw:error field="chk_tipo_conta"/></div>
            <mtw:input type="checkbox" name="chk_tipo_conta" id="chk_tipo_conta" value="-10" />Movimento Produto&nbsp;
            <mtw:checkboxes list="tipoConta" name="chk_tipo_conta" id="chk_tipo_conta" useBR="false" />
          </td>
        </tr>
        <tr>
          <td class="label">Gerar Gráfico? </td>
          <td colspan="3">
            <mtw:input type="checkbox" name="chk_grafico" id="chk_grafico" value="true" />
          </td>
        </tr>
        <tr>
          <td colspan="4" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </div>
  </div>
  <c:if test="${jpegBase64 != null}">
    <br><br>
    <center>
    <img src="data:image/jpg;base64,${jpegBase64}">
    </center>
  </c:if>
  <br><br>
  <c:if test="${fn:length(lista) > 0}">
    <display:table name="lista" pagesize="150" export="true" id="objeto" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
      <display:column title="Tipo de Conta" style="text-align: center;" headerClass="center">
         ${objeto[3] == null ? 'Movimento Produto' : objeto[2]}
      </display:column>
      <display:column property="[1]" title="Periodo" style="text-align: center;" headerClass="center" />
      <display:column property="[4]" title="Valor" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
      <display:column title="Detalhar" style="text-align: center;" headerClass="center">
        <a href="${linkDetalhar}?id_tipo_conta=${objeto[3] == null ? '-10' : objeto[2]}&${objeto[0]}"><img src="images/search.gif" border="0"/></a>
      </display:column>
    </display:table>
  </c:if>
  </center>
  </form>
</body>
</html>