<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib prefix="datatables" uri="http://github.com/tduchateau/DataTables-taglib" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Dados detalhados da venda">
  <meta name="decorator" content="popup">
  <title>Dados detalhados da venda</title>
  <script language="JavaScript" type="text/javascript">
  $(function() {
      $('#tabs').tabs();
      $('#itensVendaTable').dataTable({
          <%@include file="/template/jquery/dataTables/jquery.dataTables.configList.js"%>
      });
  });
  </script>
</head>
<body>
  <div id="tabs">
    <ul>
      <li><a href="#tabVenda">Venda</a></li>
      <li><a href="#tabCliente">Cliente</a></li>
      <li><a href="#tabTotais">Totais</a></li>
      <li><a href="#tabComissao">Comissão</a></li>
      <li><a href="#tabVencimentos">Vencimentos</a></li>
      <li><a href="#tabObservacoes">Observações</a></li>
    </ul>
    <div id="tabVenda">
      <table class="form" width="70%">
        <tr>
          <td class="label"><b>Número:</b></td>
          <td>
            ${venda.notaFiscal != null ? venda.notaFiscal.numero : venda.orcamento != null ? venda.orcamento.id : 'Não Emitida'}
            ${venda.notaFiscal != null ? '/ Prazo' : venda.orcamento != null ? '/ Vista'  : ''}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Emissão:</b></td>
          <td>
            ${venda.notaFiscal != null ? venda.notaFiscal.stringEmissao : venda.orcamento != null ? venda.orcamento.stringEmissao : 'Não Emitida'}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Prazo de Pagamento:</b></td>
          <td>
            ${venda.prazoPagamento.descricao}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Número Pedido:</b></td>
          <td>
            ${venda.pedido}
          </td>
        </tr>
      </table>
    </div>
    <div id="tabCliente">
      <table class="form" width="80%">
        <tr>
          <td class="label"><b>Nome:</b></td>
          <td>
            ${venda.cliente.id} - ${venda.cliente.nome}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Endereço:</b></td>
          <td>
            ${venda.cliente.rua}, ${venda.cliente.numero}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Cidade:</b></td>
          <td>
            ${venda.cliente.cidade.nome}/${venda.cliente.cidade.estado.sigla}
          </td>
        </tr>
      </table>
    </div>
    <div id="tabTotais">
      <table class="form" width="70%">
        <tr>
          <td class="label"><b>Total Produtos:</b></td>
          <td>
            R$ <fmt:formatNumber pattern="#0.00">${venda.valorTotalProduto}</fmt:formatNumber>
          </td>
        </tr>
        <tr>
          <td class="label"><b>Total Desconto:</b></td>
          <td>
            <fmt:formatNumber pattern="#0.00">${venda.desconto}</fmt:formatNumber> %
          </td>
        </tr>
        <tr>
          <td class="label"><b>TOTAL VENDA:</b></td>
          <td>
            R$ <fmt:formatNumber pattern="#0.00">${venda.valorTotal}</fmt:formatNumber>
          </td>
        </tr>
      </table>
    </div>
    <div id="tabComissao">
      <table class="form" width="70%">
        <tr>
          <td class="label"><b>Vendedor:</b></td>
          <td>
            ${venda.funcionario.nome}
          </td>
        </tr>
        <tr>
          <td class="label"><b>Comissão:</b></td>
          <td>
            R$ <fmt:formatNumber pattern="#0.00">${venda.comissao.valor}</fmt:formatNumber>
          </td>
        </tr>
      </table>
    </div>
    <div id="tabVencimentos">
      <center>
      <br>
      <display:table name="venda.recebimentos" id="receber">
        <display:column property="parcela" title="Parcela" style="text-align: center;" headerClass="center"/>
        <display:column title="Vencimento">
          <fmt:formatDate pattern="dd/MM/yyyy" value="${receber.vencimento}"/>
        </display:column>
        <display:column title="Valor" style="text-align: right;" headerClass="right">
          <fmt:formatNumber pattern="#0.00">${receber.valor}</fmt:formatNumber>
        </display:column>
        <display:column title="Recebido?">
          ${receber.recebido ? 'Sim' : 'Não'}
        </display:column>
      </display:table>
      </center>
    </div>
    <div id="tabObservacoes">
      <table class="form" width="70%">
        <tr>
          <td>
            ${venda.observacao}
          </td>
        </tr>
        <tr>
          <td>
            ${venda.notaFiscal != null ? venda.notaFiscal.observacao : venda.orcamento != null ? venda.orcamento.observacao : ''}
          </td>
        </tr>
      </table>
    </div>
  </div>
  <center>
  <br>
  <datatables:table data="${venda.itens}" htmlTableId="itensVendaTable" dataObjectId="item">
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
    <datatables:column title="Preço">
      <div align="right"><fmt:formatNumber pattern="#0.00">${item.valorVenda}</fmt:formatNumber></div>
    </datatables:column>
    <datatables:column title="Comissão (%)">
      <div align="right"><fmt:formatNumber pattern="#0.00">${item.comissao}</fmt:formatNumber></div>
    </datatables:column>
    <datatables:column title="Sub-Total">
      <div align="right"><fmt:formatNumber pattern="#0.00">${item.subTotal}</fmt:formatNumber></div>
    </datatables:column>
  </datatables:table>
  </center>
</body>
</html>