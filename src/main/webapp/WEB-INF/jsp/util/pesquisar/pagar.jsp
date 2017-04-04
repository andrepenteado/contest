<%@page import="com.github.andrepenteado.contest.KGlobal"%>

<c:set var="pesquisarNumero"><%=KGlobal.TipoPesquisaPagar.NUMERO%></c:set>
<c:set var="pesquisarFornecedor"><%=KGlobal.TipoPesquisaPagar.FORNECEDOR%></c:set>
<c:set var="pesquisarEmissao"><%=KGlobal.TipoPesquisaPagar.EMISSAO%></c:set>
<c:set var="pesquisarVencimento"><%=KGlobal.TipoPesquisaPagar.VENCIMENTO%></c:set>
<c:set var="pesquisarTipoConta"><%=KGlobal.TipoPesquisaPagar.TIPO_CONTA%></c:set>

<%@include file="../../comum/processando.jsp" %>

<div id="tabs">
  <ul>
    <li><a href="#${pesquisarNumero}">Buscar por N�mero</a></li>
    <li><a href="#${pesquisarFornecedor}">Filtrar por Fornecedor</a></li>
    <li><a href="#${pesquisarEmissao}">Filtrar por Emiss�o</a></li>
    <li><a href="#${pesquisarVencimento}">Filtrar por Vencimento</a></li>
    <li><a href="#${pesquisarTipoConta}">Filtrar por Tipo de Conta</a></li>
  </ul>
  <div id="${pesquisarNumero}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td align="center">
          N�mero <mtw:inputMask name="txt_numero" id="txt_numero" maskCustom="99999999999999999999" />
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="btn_numero" type="submit" value="Buscar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarFornecedor}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="label">Fornecedor:</td>
        <td><%@include file="../buscar/fornecedor.jsp"%></td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="btn_fornecedor" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarEmissao}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td align="center">Data de Emiss�o: In�cio <mtw:inputDate name="txt_emissao_inicial" id="txt_emissao_inicial" size="10" />&nbsp;&nbsp;
          Fim <mtw:inputDate name="txt_emissao_final" id="txt_emissao_final" size="10" />
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="btn_emissao" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarVencimento}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td align="center">Data de Vencimento: In�cio <mtw:inputDate name="txt_vencimento_inicial" id="txt_vencimento_inicial" size="10" />&nbsp;&nbsp;
          Fim <mtw:inputDate name="txt_vencimento_final" id="txt_vencimento_final" size="10" />
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="btn_vencimento" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
  <div id="${pesquisarTipoConta}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td align="center">
          <mtw:checkboxes list="tipoConta" name="chk_tipos_conta" id="chk_tipos_conta" useBR="false"/>
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="barrabotao" colspan="2">
          <input name="btn_tipo_conta" type="submit" value="Pesquisar" class="button">
        </td>
      </tr>
    </table>
  </div>
</div>