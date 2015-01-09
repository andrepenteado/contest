<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://github.com/tduchateau/DataTables-taglib" prefix="datatables"%>
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
      $("#txt_id").val(id);
      $("#form_pesquisar_receber").attr("action", "<%=request.getContextPath()%>/financeiro/receber.darBaixa.action");
      $("#form_pesquisar_receber").submit();
  }
  function alterar(id) {
      $("#txt_id").val(id);
      $("#form_pesquisar_receber").attr("action", "${linkCadastro}");
      $("#form_pesquisar_receber").submit();
  }
  function baixarLote() {
      $("#form_pesquisar_receber").attr("action", "${linkBaixarLote}");
      $("#form_pesquisar_receber").submit();
  }
  function gerarBoleto() {
      $("#form_pesquisar_receber").attr("action", "${linkGerarBoleto}");
      $("#form_pesquisar_receber").submit();
  }
  $(function() {
      $('#tabs').tabs();
      <c:if test="${tabName != null}">
        $('#tabs').tabs('select', '#${tabName}');
      </c:if>
      $('#receberTable').dataTable({
         <%@include file="/template/jquery/dataTables/jquery.dataTables.configGrid.js"%>,
         "aoColumns": [
             { "bVisible": true },  // Checkbox
             { "bVisible": false }, // Pedido
             { "bVisible": false }, // NF
             { "bVisible": false }, // Documento
             { "bVisible": true },  // Descrição
             { "bVisible": true },  // Tipo de Pagamento
             { "bVisible": false }, // Emissão
             { "bVisible": true },  // Vencimento
             { "bVisible": false }, // Total Venda
             { "bVisible": false }, // No. Parcela
             { "bVisible": true },  // Valor Parcela
             { "bVisible": false }, // Valor Recebido
             { "bVisible": true },  // Valor Devido
             { "bVisible": false }  // Operações
          ]
      });
  });
  </script>
</head>
<body>
  <p align="right"><input type="button" value="Nova Conta" onClick="window.location.href='${linkCadastro}'" class="button"></p>
  <form name="form_pesquisar_receber" id="form_pesquisar_receber" method="post" action="${linkPesquisar}">
  <mtw:input type="hidden" name="txt_id" id="txt_id"/>
  
  <center>
  <%@include file="../../util/pesquisar/receber.jsp"%>
  <br>

  <c:if test="${listaReceber != null}">
    <datatables:table data="${listaReceber}" htmlTableId="receberTable" dataObjectId="receber">
      <datatables:column title="#">
        <mtw:input type="checkbox" name="chk_id_receber" id="chk_id_receber" value="${receber.id}"/>
      </datatables:column> 
      <datatables:column title="Pedido" sortable="true">
        <c:if test="${receber.venda != null}">
          ${receber.venda.pedido}
        </c:if>
      </datatables:column> 
      <datatables:column title="NF" sortable="true">
        <c:if test="${receber.venda != null && receber.venda.notaFiscal != null}">
          ${receber.venda.notaFiscal.numero}
        </c:if>
      </datatables:column> 
      <datatables:column title="Doc." property="numeroDocumento" sortable="true"/>
      <datatables:column title="Descrição" sortable="true">
        ${receber.descricao == null || receber.descricao == '' ? receber.venda.cliente.nome : receber.descricao}
      </datatables:column>
      <datatables:column title="Tipo" sortable="true">
        ${receber.tipoConta == null ? receber.venda.formaPagamento.descricao : receber.tipoConta.descricao}
      </datatables:column>
      <datatables:column title="Emissão" sortable="true">
        <c:if test="${receber.venda != null}">
          <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.venda.emissao}"/>
        </c:if>
      </datatables:column>
      <datatables:column title="Vencimento" sortable="true">
        <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.vencimento}"/>
      </datatables:column>
      <datatables:column title="Venda" sortable="true">
        <c:if test="${receber.venda != null}">
          <fmt:formatNumber pattern="" value="${receber.venda.valorTotal}" minFractionDigits="2"/>
        </c:if>
      </datatables:column> 
      <datatables:column title="No." property="parcela"/>
      <datatables:column title="Parcela" property="valor" sortable="true"/> 
      <datatables:column title="Recebido" property="valorRecebido" sortable="true"/> 
      <datatables:column title="Devido" property="valorDevido" sortable="true"/> 
      <datatables:column title="Operações">
        <a href="javascript:alterar(${receber.id});"><img src="images/salvar.png" title="Alterar" border="0"/></a>
        <a href="javascript:darBaixa(${receber.id});"><img src="images/dar_baixa.png" title="Dar Baixa" border="0"/></a>
      </datatables:column> 
    </datatables:table> 
  </c:if>

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