<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/vendas/comissao.pendente.action</c:set>
<c:set var="linkPagamento"><%=request.getContextPath()%>/vendas/comissao.pagamento.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pagamento de comissões dos vendedores">
  <title>Comissão de Vendedores</title>
  <mtw:inputMoneyConfig />
  <script type="text/javascript">
  function calcularTotal() {
      var obj;
      var total = 0;
      var form = document.getElementById('form_comissao');
      for (var i = 0; i < form.elements.length; i++) {
    	  obj = form.elements[i];
          if (obj.type == 'checkbox') {
              if (obj.id == 'chk_id_comissao') {
            	  if (obj.checked == true) {
            		  var strValor = document.getElementById('txt_valor_' + obj.value);
            		  var valor = parseFloat(strValor.value.replace('.','').replace(',','.'));
            		  total += valor;
            	  }
              }
          }
      }
      document.getElementById('span_total').innerHTML = 'R$ ' + total.toFixed(2);
  }
  function realizarPagamento() {
      $("#form_comissao").attr("action", "${linkPagamento}");
      $("#form_comissao").submit();
  }
  function marcarDesmarcarTodos() {
      var obj;
      var form = document.getElementById('form_comissao');
      var modo = 1;
      if (form.chk_todos.checked == false)
    	  modo = 2;
      for (var i = 0; i < form.elements.length; i++) {
          obj = form.elements[i];
          if (obj.type == 'checkbox') {
              if (obj.id == 'chk_id_comissao') {
                  if (modo == 1) {
                      obj.checked = 'checked';
                  } else {
                      obj.checked = '';
                  }
              }
          }
      }
      calcularTotal();
  }
  $(function() {
      $('#tabs').tabs();
  });
  </script>
</head>
<body onload="javascript:document.form_comissao.txt_id_funcionario.focus();">
  <form name="form_comissao" id="form_comissao" method="post" action="${linkPesquisar}">
  <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO.concat(",").concat(KGlobal.CATEGORIA_ADMINISTRATIVO)%>">
    <p align="right">
      <mtw:select name="cbo_vista_prazo" list="vistaPrazo" emptyField="true" style="font-size:8pt;"/>
    </p>
  </mtw:hasAuthorization>
  <center>
  <div id="tabs">
    <ul>
      <li><a href="#tabVendedor">Vendedor</a></li>
    </ul>
    <div id="tabVendedor">
      <table class="form">
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td class="label">Vendedor</td>
          <td>
            <%@include file="../../util/buscar/funcionario.jsp" %>
            &nbsp;&nbsp;
            <input name="btn_pesquisar" type="submit" value="Ver Pendentes" class="button">
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
      </table>
    </div>
  </div>
  <br>
  Colunas a exibir: <mtw:checkboxes name="chk_colunas_pesquisa" list="colunasPesquisaVendas"/>
  <c:if test="${comissoesPendentes != null}">
    <display:table name="${comissoesPendentes}" export="true" id="comissao" decorator="org.displaytag.decorator.TotalTableDecorator" requestURI="${linkPesquisar}">
      <c:if test="${numero eq '1'}">
        <display:column title="N&deg;" sortable="true" style="text-align: center;" headerClass="center">
          ${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.numero : comissao.venda.orcamento != null ? comissao.venda.id : ''}
        </display:column>
      </c:if>
      <c:if test="${pedido eq '1'}">
        <display:column property="venda.pedido" title="Pedido" sortable="true" style="text-align: center;" headerClass="center"/>
      </c:if>
      <display:column title="Emissão" style="text-align: center;" sortable="true" headerClass="center">
        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${comissao.venda.notaFiscal != null ? comissao.venda.notaFiscal.emissao : comissao.venda.orcamento != null ? comissao.venda.orcamento.emissao : ''}"/>
      </display:column>
      <display:column property="venda.cliente.nome" sortable="true" title="Cliente"/>
      <c:if test="${cidade eq '1'}">
        <display:column property="venda.cliente.cidade.nome" sortable="true" title="Cliente"/>
      </c:if>
      <c:if test="${formaPagamento eq '1'}">
        <display:column property="venda.formaPagamento" title="Forma Pagto." sortable="true" style="text-align: center;" headerClass="center"/>
      </c:if>
      <c:if test="${prazo eq '1'}">
        <display:column property="venda.prazoPagamento.descricao" title="Prazo" sortable="true" style="text-align: center;" headerClass="center"/>
      </c:if>
      <display:column property = "venda.valorTotal" title="Total" format="{0,number,0.00}" style="text-align: right;" headerClass="right" total="true"/>
      <display:column property="valor" title="Comissão" format="{0,number,0.00}" style="text-align: center;" headerClass="center" total="true"/>
      <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO.concat(",").concat(KGlobal.CATEGORIA_ADMINISTRATIVO)%>">
        <display:column title="Valor <input type='checkbox' name='chk_todos' onclick='marcarDesmarcarTodos()'>" style="text-align: center; white-space: nowrap;">
          <mtw:inputMoney name="txt_valor_${comissao.id}" value="${comissao.valor}" size="10" dec_point="," textAlign="right" thousands_sep="." onblur="calcularTotal()" />
          <input type="checkbox" name="chk_id_comissao" id="chk_id_comissao" value="${comissao.id}" onclick="calcularTotal()"/>&nbsp;
        </display:column>
      </mtw:hasAuthorization>
    </display:table>
  </c:if>
  <br>
  <table class="form">
    <tr>
      <td colspan="2" class="label">
        <center>
          <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO.concat(",").concat(KGlobal.CATEGORIA_ADMINISTRATIVO)%>">
            <input name="btn_pagamento" type="button" value="Pagar Comissão" onClick="javascript:realizarPagamento();" class="button">
          </mtw:hasAuthorization>
          &nbsp;&nbsp;
          <b><font style="font-size: 14px;">Valor Total: </font><font color="blue" style="font-size: 14px;"><span id="span_total">R$ 0.00</span></font></b>
        </center>
      </td>
    </tr>
  </table>
  </center>
  </form>
</body>
</html>