<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/pago.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/financeiro/pago.cadastro.action</c:set>
<c:set var="linkEstorno"><%=request.getContextPath()%>/financeiro/pago.estornar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o período para pesquisar pagamentos">
  <title>Pagamentos Efetuados</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      document.form_pesquisar_pago.action = '${linkCadastro}?txt_id=' + id;
      document.form_pesquisar_pago.submit();
  }
  function estornar(id, valorPago) {
      if (confirm('Confirma estornar o pagamento de valor ' + valorPago)) {
          document.form_pesquisar_recebido.action = '${linkEstorno}?txt_id=' + id;
          document.form_pesquisar_recebido.submit();
      }
  }
  $(function() {
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
  });
  </script>
</head>
<body>
  <form name="form_pesquisar_pago" method="post" action="${linkPesquisar}">
  <center>

  <%@include file="../../util/pesquisar/pagar.jsp" %>

  <br>
  Colunas a exibir: <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaContas"/>

  <display:table name="listaPago" pagesize="${naoPaginar eq '1' ? 1000 : 50}" export="true" id="pago" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
    <display:column title="Descrição" sortable="true">
      ${pago.pagar.descricao == null || pago.pagar.descricao == '' ? pago.pagar.compra.fornecedor.nome : pago.pagar.descricao}
    </display:column>    <display:column title="Tipo" sortable="true">
      ${pago.pagar.tipoConta == null ? 'Compra' : pago.pagar.tipoConta.descricao}
    </display:column>
    <display:column title="Pagamento" sortable="true" style="text-align: center;" headerClass="center">      <fmt:formatDate pattern="dd/MM/yyyy" value="${pago.dataPagamento}"/>    </display:column>    <display:column title="Total" property="pagar.valor" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Pago" property="valorPago" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Alterar" style="text-align: center;" headerClass="center">
      <a href="javascript:alterar(${pago.id});"><img src="images/salvar.png" border="0"/></a>
    </display:column>
    <display:column title="Estornar" style="text-align: center;" headerClass="center">
      <a href="javascript:estornar(${pago.id},${pago.valorPago});"><img src="images/estorno.png" border="0"/></a>
    </display:column>
  </display:table>
  </center>
  </form>
</body>
</html>