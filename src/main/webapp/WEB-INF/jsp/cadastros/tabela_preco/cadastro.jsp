<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados da tabela de preço">
  <title>Cadastro de Tabela de Preço</title>
  <mtw:inputMaskConfig />
  <script type="text/javascript">
  function incluirFaixa() {
      document.form_cadastro_tabela_preco.action = '<%=request.getContextPath()%>/cadastros/tabelaPreco.incluirFaixa.action';
      document.form_cadastro_tabela_preco.submit();
  }
  function excluirFaixa(id, descricao) {
      if (confirm('Confirma a exclusão da faixa de preço ' + descricao)) {
          document.form_cadastro_tabela_preco.action = '<%=request.getContextPath()%>/cadastros/tabelaPreco.excluirFaixa.action?txt_indice=' + id;
          document.form_cadastro_tabela_preco.submit();
      }
  }
  </script>
</head>
<body onload="javascript:document.form_cadastro_tabela_preco.txt_descricao.focus();">
<form name="form_cadastro_tabela_preco" method="post" action="<%=request.getContextPath()%>/cadastros/tabelaPreco.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${tabelaPreco.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Descrição</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_descricao"/></div>
        <mtw:inputText name="txt_descricao" id="txt_descricao" size="60" maxlength="100" value="${tabelaPreco.descricao}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="4" cols="60">${tabelaPreco.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
  </table>
  <h1>Faixas de Preço</h1>
  <cite>Incluir as faixas de preços da tabela</cite>
  <table class="form">
    <tr>
      <td width="25%" class="label">Descrição</td>
      <td width="75%">
        <div class="error"><mtw:error field="txt_descricao_faixa"/></div>
        <mtw:inputText name="txt_descricao_faixa" id="txt_descricao_faixa" size="60"/>
      </td>
    </tr>
    <tr>
      <td class="label">Markup à Vista</td>
      <td>
        <div class="error"><mtw:error field="txt_markup_vista"/></div>
        <mtw:inputMoney name="txt_markup_vista" id="txt_markup_vista" size="20" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td class="label">Markup à Prazo</td>
      <td>
        <div class="error"><mtw:error field="txt_markup_prazo"/></div>
        <mtw:inputMoney name="txt_markup_prazo" id="txt_markup_prazo" size="20" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td class="label">Comissão</td>
      <td>
        <div class="error"><mtw:error field="txt_comissao"/></div>
        <mtw:inputMoney name="txt_comissao" id="txt_comissao" size="20" dec_point="," textAlign="right" thousands_sep="." />
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input name="btn_incluir_faixa" type="button" value="Incluir Faixa" onclick="javascript:incluirFaixa();" class="button">
      </td>
    </tr>
  </table>
  <center>
  <display:table name="${tabelaPreco.faixas}" id="faixa">
    <display:column property="descricao" title="Descrição"/>
    <display:column title="Markup à Vista" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${faixa.markupVista}</fmt:formatNumber>
    </display:column>
    <display:column title="Markup à Prazo" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${faixa.markupPrazo}</fmt:formatNumber>
    </display:column>
    <display:column title="Comissão" style="text-align: right;" headerClass="right">
      <fmt:formatNumber pattern="#0.00">${faixa.comissao}</fmt:formatNumber>
    </display:column>
    <display:column title="Excluir" style="text-align: center; width: 70px;" headerClass="center">
      <a href="javascript:excluirFaixa(${faixa_rowNum},'${faixa.descricao}')"><img src="images/excluir.gif" border="0" align="middle"/></a>
    </display:column>
  </display:table>
  <br>
  <input name="btn_gravar" type="submit" value="Gravar" class="button">
  <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/tabelaPreco.pesquisar.action';" class="button">
  </center>
</form>
</body>
</html>