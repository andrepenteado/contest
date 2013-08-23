<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Dados detalhados da compra">
  <meta name="decorator" content="popup">
  <title>Dados detalhados da compra</title>
  <script language="JavaScript" type="text/javascript">
  $(function() {
      $('#tabs').tabs();
      $('#itensCompraTable').dataTable({
          <%@include file="/template/jquery/dataTables/jquery.dataTables.configList.js"%>
      });
  });
  </script>
</head>
<body>
  <div id="tabs">
    <ul>
      <li><a href="#tabCompra">Compra</a></li>
      <li><a href="#tabFornecedor">Fornecedor</a></li>
      <li><a href="#tabTotais">Totais</a></li>
      <li><a href="#tabVencimentos">Vencimentos</a></li>
      <li><a href="#tabObservacoes">Observações</a></li>
    </ul>
    <div id="tabCompra">
      <table class="form" width="70%">
        <tr>
          <td class="label"><b>Número:</b></td>
          <td>
            ${compra.notaFiscal == null && compra.emissao == null ? 'Não Emitida' : compra.notaFiscal }
            ${compra.notaFiscal == null && compra.emissao != null ? '/ Vista' : '/ Prazo' }
          </td>
        </tr>
        <tr>
          <td class="label"><b>Emissão:</b></td>
          <td>
            ${compra.emissao != null ? compra.stringEmissao : 'Não Emitida'}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Prazo de Pagamento:</b></td>
          <td>
            ${compra.prazoPagamento.descricao}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Número Pedido:</b></td>
          <td>
            ${compra.pedido}
          </td>
        </tr>
      </table>
    </div>
    <div id="tabFornecedor">
      <table class="form" width="80%">
        <tr>
          <td class="label"><b>Nome:</b></td>
          <td>
            ${compra.fornecedor.id} - ${compra.fornecedor.nome}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Endereço:</b></td>
          <td>
            ${compra.fornecedor.rua}, ${compra.fornecedor.numero}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Cidade:</b></td>
          <td>
            ${compra.fornecedor.cidade.nome}/${compra.fornecedor.cidade.estado.sigla}
          </td>
        </tr>
      </table>
    </div>
    <div id="tabTotais">
      <table class="form" width="70%">
        <tr>
          <td class="label"><b>TOTAL VENDA:</b></td>
          <td>
            R$ <fmt:formatNumber pattern="#0.00">${compra.valorTotal}</fmt:formatNumber>
          </td>
        </tr>
      </table>
    </div>
    <div id="tabVencimentos">
      <center>
      <br>
      <display:table name="compra.pagamentos" id="pagar">
        <display:column property="parcela" title="Parcela" style="text-align: center;" headerClass="center"/>
        <display:column title="Vencimento">
          <fmt:formatDate pattern="dd/MM/yyyy" value="${pagar.vencimento}"/>
        </display:column>
        <display:column title="Valor" style="text-align: right;" headerClass="right">
          <fmt:formatNumber pattern="#0.00">${pagar.valor}</fmt:formatNumber>
        </display:column>
        <display:column title="Pago?">
          ${pagar.pago ? 'Sim' : 'Não'}
        </display:column>
      </display:table>
      </center>
    </div>
    <div id="tabObservacoes">
      <table class="form" width="70%">
        <tr>
          <td>
            ${compra.observacao}
          </td>
        </tr>
      </table>
    </div>
  </div>
  <center>
  <br>
  <datatables:table data="${compra.itens}" htmlTableId="itensCompraTable" dataObjectId="item">
    <datatables:column title="ID">
      <div align="center">${item.produto.id}</div>
    </datatables:column>
    <datatables:column title="Referência">
      ${item.produto.referencia}
    </datatables:column>
    <datatables:column title="Descrição">
      ${item.produto.descricao}
    </datatables:column>
    <datatables:column property="quantidade" title="Qtd."/>
    <datatables:column title="CFOP">
      <div align="center">${item.cfop.codigo}</div>
    </datatables:column>
    <datatables:column title="Preço">
      <div align="right"><fmt:formatNumber pattern="#0.00">${item.valorCompra}</fmt:formatNumber></div>
    </datatables:column>
    <datatables:column title="Impostos">
      <div align="right">
      <c:forEach items="${item.impostos}" var="impostoCompra">
        ${impostoCompra.imposto.descricao}:${impostoCompra.aliquota}%<br>      
      </c:forEach>
      </div>
    </datatables:column>
    <datatables:column title="Sub-Total">
      <div align="right"><fmt:formatNumber pattern="#0.00">${item.subTotal}</fmt:formatNumber></div>
    </datatables:column>
  </datatables:table>
  </center>
</body>
</html>