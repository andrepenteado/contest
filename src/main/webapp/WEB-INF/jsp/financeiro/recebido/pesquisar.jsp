<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/recebido.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/financeiro/recebido.cadastro.action</c:set>
<c:set var="linkEstorno"><%=request.getContextPath()%>/financeiro/recebido.estornar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o período para pesquisar recebimentos">
  <title>Recebimentos Efetuados</title>
  <script language="JavaScript" type="text/javascript">
  function alterar(id) {
      document.form_pesquisar_recebido.action = '${linkCadastro}?txt_id=' + id;
      document.form_pesquisar_recebido.submit();
  }
  function estornar(id, valorPago) {
      if (confirm('Confirma estornar o recebimento de valor ' + valorPago)) {
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
  <form name="form_pesquisar_recebido" method="post" action="${linkPesquisar}">
  <center>

  <%@include file="../../util/pesquisar/receber.jsp"%>

  <br>
  Colunas a exibir: <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaContas"/>

  <display:table name="listaRecebido" pagesize="${naoPaginar eq '1' ? 1000 : 50}" export="true" id="recebido" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
    <c:if test="${pedido eq '1'}">
      <display:column title="Pedido" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${recebido.receber.venda != null}">
          ${recebido.receber.venda.pedido}
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${nf eq '1'}">
      <display:column title="NF" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${recebido.receber.venda != null && recebido.receber.venda.notaFiscal != null}">
          ${recebido.receber.venda.notaFiscal.numero}
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${documento eq '1'}">
      <display:column title="Doc." property="receber.numeroDocumento" sortable="true" style="text-align: center;" headerClass="center"/>
    </c:if>
    <display:column title="Descrição" sortable="true">
      ${recebido.receber.descricao == null || recebido.receber.descricao == '' ? recebido.receber.venda.cliente.nome : recebido.receber.descricao}
    </display:column>
    <c:if test="${tipo eq '1'}">
      <display:column title="Tipo" sortable="true">
        ${recebido.receber.tipoConta == null ? 'Venda' : recebido.receber.tipoConta.descricao}
      </display:column>
    </c:if>
    <c:if test="${emissao eq '1'}">
      <display:column title="Emissão" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${recebido.receber.venda != null}">
          <fmt:formatDate pattern="dd/MM/yyyy" value="${recebido.receber.venda.emissao}"/>
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${vencimento eq '1'}">
      <display:column title="Vencimento" sortable="true" style="text-align: center;" headerClass="center">
        <fmt:formatDate pattern="dd/MM/yyyy" value="${recebido.receber.vencimento}"/>
      </display:column>
    </c:if>
    <c:if test="${pagamento eq '1'}">
      <display:column title="Pagamento" sortable="true" style="text-align: center;" headerClass="center">        <fmt:formatDate pattern="dd/MM/yyyy" value="${recebido.dataPagamento}"/>      </display:column>
    </c:if>
    <c:if test="${totalVenda eq '1'}">
      <display:column title="Venda" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${recebido.receber.venda != null}">
          <fmt:formatNumber pattern="" value="${recebido.receber.venda.valorTotal}" minFractionDigits="2"/>
        </c:if>
      </display:column>
    </c:if>
    <display:column title="N&ordm;" property="receber.parcela" style="text-align: center;"/>
    <display:column title="Parcela" property="receber.valor" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Recebido" property="valorPago" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Alterar" style="text-align: center;" headerClass="center">
      <a href="javascript:alterar(${recebido.id});"><img src="images/salvar.png" border="0"/></a>
    </display:column>
    <display:column title="Estornar" style="text-align: center;" headerClass="center">
      <a href="javascript:estornar(${recebido.id},${recebido.valorPago});"><img src="images/estorno.png" border="0"/></a>
    </display:column>
  </display:table>
  </center>
  </form>
</body>
</html>