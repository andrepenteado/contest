<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/financeiro/receber.pesquisar.action</c:set>
<c:set var="linkCadastro"><%=request.getContextPath()%>/financeiro/receber.cadastro.action</c:set>
<c:set var="linkBaixarLote"><%=request.getContextPath()%>/financeiro/receber.baixarLote.action</c:set>
<c:set var="linkGerarBoleto"><%=request.getContextPath()%>/financeiro/receber.gerarBoleto.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o período para pesquisar contas a receber">
  <title>Contas a Receber</title>
  <style type="text/css">
      #checkAll {
          line-height:15px;
          margin-top:-17px;
          margin-bottom:20px;
          background-repeat:no-repeat;
          color:#aaaaaa;
      }
      #checkAll a:link {
          color:#a8a8a8;
          text-decoration:none;   
      }
      #checkAll a:hover {
          color:#cccccc;
          text-decoration:underline;
      }
      #checkAll a:visited {
          color:#a8a8a8;
          text-decoration:none;   
      }
  </style>
  <script language="JavaScript" type="text/javascript">
  function selecionarTodos(modo) {
      var obj;
      var form = document.getElementById('form_pesquisar_receber');
      for (var i = 0; i < form.elements.length; i++) {
          obj = form.elements[i];
          if (obj.type == 'checkbox') {
              if (obj.id == 'chk_id_receber') {
                  if (modo == 1) {
                      obj.checked = 'checked';
                  } else {
                      obj.checked = '';
                  }
              }
          }
      }
  }
  function darBaixa(id) {
      document.form_pesquisar_receber.action = '<%=request.getContextPath()%>/financeiro/receber.darBaixa.action?id_receber=' + id;
      document.form_pesquisar_receber.submit();
  }
  function alterar(id) {
      document.form_pesquisar_receber.action = '${linkCadastro}?txt_id=' + id;
      document.form_pesquisar_receber.submit();
  }
  function baixarLote() {
      document.form_pesquisar_receber.action = '${linkBaixarLote}';
      document.form_pesquisar_receber.submit();
  }
  function gerarBoleto() {
      document.form_pesquisar_receber.action = '${linkGerarBoleto}';
      document.form_pesquisar_receber.submit();
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
  <p align="right"><input type="button" value="Nova Conta" onClick="window.location.href='${linkCadastro}'" class="button"></p>
  <form name="form_pesquisar_receber" id="form_pesquisar_receber" method="post" action="${linkPesquisar}">
  <center>

  <%@include file="../../util/pesquisar/receber.jsp"%>

  <br>
  Colunas a exibir: <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaContas"/>

  <display:table name="listaReceber" pagesize="${naoPaginar eq '1' ? 1000 : 50}" export="true" id="receber" requestURI="${linkPesquisar}" decorator="org.displaytag.decorator.TotalTableDecorator">
    <display:column style="text-align: center; white-space: nowrap;">
      <mtw:input type="checkbox" name="chk_id_receber" id="chk_id_receber" value="${receber.id}"/>
    </display:column>
    <c:if test="${pedido eq '1'}">
      <display:column title="Pedido" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${receber.venda != null}">
          ${receber.venda.pedido}
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${nf eq '1'}">
      <display:column title="NF" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${receber.venda != null && receber.venda.notaFiscal != null}">
          ${receber.venda.notaFiscal.numero}
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${documento eq '1'}">
      <display:column title="Doc." property="numeroDocumento" sortable="true" style="text-align: center;" headerClass="center"/>
    </c:if>
    <display:column title="Descrição" sortable="true">
      ${receber.descricao == null || receber.descricao == '' ? receber.venda.cliente.nome : receber.descricao}
    </display:column>
    <%--<c:if test="${tipo eq '1'}">--%>
      <display:column title="Tipo" sortable="true">
        ${receber.tipoConta == null ? receber.venda.formaPagamento.descricao : receber.tipoConta.descricao}
      </display:column>
    <%--</c:if>--%>
    <c:if test="${emissao eq '1'}">
      <display:column title="Emissão" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${receber.venda != null}">
          <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.venda.emissao}"/>
        </c:if>
      </display:column>
    </c:if>
    <c:if test="${vencimento eq '1'}">
      <display:column title="Vencimento" sortable="true" style="text-align: center;" headerClass="center">
        <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.vencimento}"/>
      </display:column>
    </c:if>
    <c:if test="${totalVenda eq '1'}">
      <display:column title="Venda" sortable="true" style="text-align: center;" headerClass="center">
        <c:if test="${receber.venda != null}">
          <fmt:formatNumber pattern="" value="${receber.venda.valorTotal}" minFractionDigits="2"/>
        </c:if>
      </display:column>
    </c:if>
    <display:column title="N&ordm;" property="parcela" style="text-align: center;"/>
    <display:column title="Parcela" property="valor" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Recebido" property="valorRecebido" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Devido" property="valorDevido" sortable="true" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
    <display:column title="Operações" style="text-align: center;" headerClass="center">
      <a href="javascript:alterar(${receber.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
      <a href="javascript:darBaixa(${receber.id});"><img src="images/dar_baixa.png" title="Dar Baixa" border="0"/></a>
    </display:column>
  </display:table>

  <br>
  <br>

  <table class="form">
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_baixar_lote" type="button" value="Baixar" onClick="javascript:baixarLote();" class="button">
        &nbsp;&nbsp;&nbsp;
        <input name="btn_gerar_boleto" type="button" value="Gerar Boleto" onClick="javascript:gerarBoleto();" class="button">
      </td>
    </tr>
  </table>

  </center>

  <div id="checkAll">
    <a href="javascript:void(0);" onclick="selecionarTodos('1');">Marcar todos</a> / 
    <a href="javascript:void(0);" onclick="selecionarTodos('2');">Desmarcar todos</a>
  </div>

  </form>
</body>
</html>