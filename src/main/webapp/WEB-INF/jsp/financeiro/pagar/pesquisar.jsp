<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://github.com/tduchateau/DataTables-taglib" prefix="datatables"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/pagar.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/financeiro/pagar.cadastro.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o período para pesquisar contas a pagar">
  <title>Contas a Pagar</title>
  <script language="JavaScript" type="text/javascript">
  function darBaixa(id) {
     $("#txt_id").val(id);
     $("#form_pesquisar_pagar").attr("action", "<%=request.getContextPath()%>/financeiro/pagar.darBaixa.action");
     $("#form_pesquisar_pagar").submit();
  }
  function alterar(id) {
     $("#txt_id").val(id);
     $("#form_pesquisar_pagar").attr("action", "${linkCadastro}");
     $("#form_pesquisar_pagar").submit();
  }
  $(function() {
     $('#tabs').tabs();
     <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
     </c:if>
     $('#pagarTable').dataTable({
        <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
        "aoColumns": [
           { "bVisible": true }, // Checkbox
           { "bVisible": true }, // Pedido
           { "bVisible": true }, // NF
           { "bVisible": true }, // Documento
           { "bVisible": true }, // Descrição
           { "bVisible": true }, // Tipo de Pagamento
           { "bVisible": true }  // Emissão
         ]
     });
  });
  </script>
</head>
<body>
  <p align="right"><input type="button" value="Nova Conta" onClick="window.location.href='${linkCadastro}'" class="button"></p>
  <form name="form_pesquisar_pagar" id="form_pesquisar_pagar" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>

  <center>
  <%@include file="../../util/pesquisar/pagar.jsp" %>
  <br>

  <c:if test="${listaPagar != null}">
    <datatables:table data="${listaPagar}" htmlTableId="pagarTable" dataObjectId="pagar">
      <datatables:column title="Descrição" sortable="true">
        ${pagar.descricao == null || pagar.descricao == '' ? pagar.compra.fornecedor.nome : pagar.descricao}
      </datatables:column>      <datatables:column title="Tipo">
        ${pagar.tipoConta == null ? 'Compra' : pagar.tipoConta.descricao}
      </datatables:column>
      <datatables:column title="Vencimento" sortable="true">        <fmt:formatDate pattern="dd/MM/yyyy" value="${pagar.vencimento}"/>      </datatables:column>      <datatables:column title="Total" property="valor" sortable="true"/>
      <datatables:column title="Pago" property="valorPago" sortable="true"/>
      <datatables:column title="Devido" property="valorDevido" sortable="true"/>
      <datatables:column title="Operações">
        <a href="javascript:alterar(${pagar.id});"><img src="images/salvar.png" border="0"/></a>
        <a href="javascript:darBaixa(${pagar.id});"><img src="images/dar_baixa.png" border="0"/></a>
      </datatables:column>
    </datatables:table>
  </c:if>

  </center>
  </form>
</body>
</html>