<%@page import="com.github.andrepenteado.contest.KGlobal"%>

<c:set var="pesquisarNumero"><%=KGlobal.TipoPesquisaVenda.NUMERO%></c:set>
<c:set var="pesquisarCliente"><%=KGlobal.TipoPesquisaVenda.CLIENTE%></c:set>
<c:set var="pesquisarEmissao"><%=KGlobal.TipoPesquisaVenda.EMISSAO%></c:set>
<c:set var="pesquisarVencimento"><%=KGlobal.TipoPesquisaVenda.VENCIMENTO%></c:set>

<%@include file="../../comum/processando.jsp" %>

<div id="tabs">
  <ul>
    <li><a href="#${pesquisarNumero}">Buscar por N�mero</a></li>
    <li><a href="#${pesquisarCliente}">Filtrar por Cliente</a></li>
    <li><a href="#${pesquisarEmissao}">Filtrar por Emiss�o</a></li>
    <li><a href="#${pesquisarVencimento}">Filtrar por Vencimento</a></li>
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
	      <input name="${pesquisarNumero}" type="submit" value="Buscar" class="button">
	    </td>
	  </tr>
    </table>
  </div>
  <div id="${pesquisarCliente}">
    <table class="form">
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td class="label">Cliente:</td>
        <td><%@include file="../buscar/cliente.jsp"%></td>
      </tr>
	  <tr><td colspan="2">&nbsp;</td></tr>
	  <tr>
	    <td class="barrabotao" colspan="2">
	      <input name="${pesquisarCliente}" type="submit" value="Pesquisar" class="button">
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
	      <input name="${pesquisarEmissao}" type="submit" value="Pesquisar" class="button">
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
	      <input name="${pesquisarVencimento}" type="submit" value="Pesquisar" class="button">
	    </td>
	  </tr>
    </table>
  </div>
</div>