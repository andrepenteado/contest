<%@page import="br.com.alphadev.contest.KGlobal"%>

<c:set var="pesquisarCodigoReferenciaDescricao"><%=KGlobal.TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO%></c:set>
<c:set var="pesquisarGrupoProduto"><%=KGlobal.TipoPesquisaProduto.GRUPO_PRODUTO%></c:set>
<c:set var="pesquisarFornecedor"><%=KGlobal.TipoPesquisaProduto.FORNECEDOR%></c:set>

<%@include file="../../comum/processando.jsp" %>

<div id="tabs">
  <ul>
    <li><a href="#${pesquisarCodigoReferenciaDescricao}">Produto</a></li>
    <li><a href="#${pesquisarGrupoProduto}">Grupo de Produto</a></li>
    <li><a href="#${pesquisarFornecedor}">Fornecedor</a></li>
  </ul>
  <div id="${pesquisarCodigoReferenciaDescricao}">
    <table class="form">
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td>
          <div class="error"><mtw:error field="txt_codigo_referencia_descricao"/></div>
          <center>
          Produto <mtw:inputText name="txt_codigo_referencia_descricao" id="txt_codigo_referencia_descricao" size="40" onkeyup="isUpper(this);" />
          </center>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="barrabotao">
          <input name="${pesquisarCodigoReferenciaDescricao}" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarGrupoProduto}">
    <table class="form">
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td>
          <div class="error"><mtw:error field="cbo_grupo_produto"/></div>
          <center>
          Grupo de Produto: <mtw:select list="listaGruposProdutos" name="cbo_grupo_produto" id="cbo_grupo_produto" />
          </center>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="barrabotao">
          <input name="${pesquisarGrupoProduto}" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarFornecedor}">
    <table class="form">
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="label">Fornecedor</td>
        <td>
          <%@include file="../../util/buscar/fornecedor.jsp" %>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="${pesquisarFornecedor}" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
</div>