<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados do fornecedor">
  <title>Cadastro de Fornecedor</title>
  <mtw:inputMaskConfig />
  <script type="text/javascript">
  function popupPesquisarComprasFornecedor(id) {
      pCtrl.search('<%=request.getContextPath()%>/cadastros/fornecedor.compras.action?txt_id=' + id, 600, 400, new Array());
  }
  function popupPesquisarProdutosFornecedor(id) {
      pCtrl.search('<%=request.getContextPath()%>/cadastros/fornecedor.itens.action?txt_id=' + id, 600, 400, new Array());
  }
  </script>
</head>
<body onload="javascript:document.form_cadastro_fornecedor.txt_nome.focus();">
<form name="form_cadastro_fornecedor" method="post" action="<%=request.getContextPath()%>/cadastros/fornecedor.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${fornecedor.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Nome</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_nome"/></div>
        <mtw:inputText name="txt_nome" id="txt_nome" size="60" maxlength="100" value="${fornecedor.nome}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Rua</td>
      <td width="70%">
        <mtw:inputText name="txt_rua" id="txt_rua" size="60" maxlength="100" value="${fornecedor.rua}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Número</td>
      <td width="70%">
        <mtw:inputText name="txt_numero" id="txt_numero" size="30" maxlength="50" value="${fornecedor.numero}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Complemento</td>
      <td width="70%">
        <mtw:inputText name="txt_complemento" id="txt_complemento" size="60" maxlength="100" value="${fornecedor.complemento}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Bairro</td>
      <td width="70%">
        <mtw:inputText name="txt_bairro" id="txt_bairro" size="30" maxlength="50" value="${fornecedor.bairro}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">CEP</td>
      <td width="70%">
        <mtw:inputMask name="txt_cep" id="txt_cep" maskDefined="CEP" value="${fornecedor.cep}" />
      </td>
    </tr>

    <c:set var="idEstado">${fornecedor.cidade.estado.id}</c:set>
    <c:set var="idCidade">${fornecedor.cidade.id}</c:set>
    <%@include file="../../util/buscar/cidade.jsp" %>

    <tr>
      <td width="30%" class="label">Telefone</td>
      <td width="70%">
        <mtw:inputMask name="txt_telefone" id="txt_telefone" maskDefined="FONE" value="${fornecedor.telefone}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Fax</td>
      <td width="70%">
        <mtw:inputMask name="txt_fax" id="txt_fax" maskDefined="FONE" value="${fornecedor.fax}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">E-mail</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_email"/></div>
        <mtw:inputText name="txt_email" id="txt_email" size="60" maxlength="100" value="${fornecedor.email}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Página Internet</td>
      <td width="70%">
        <mtw:inputText name="txt_pagina_internet" id="txt_pagina_internet" size="60" maxlength="100" value="${fornecedor.paginaInternet}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Contato</td>
      <td width="70%">
        <mtw:inputText name="txt_contato" id="txt_contato" size="60" maxlength="100" value="${fornecedor.contato}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">CNPJ ou CPF</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_cnpj"/></div>
        <mtw:inputMask name="txt_cnpj" id="txt_cnpj" maskDefined="CNPJ" value="${fornecedor.cnpj}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Insc.Estadual ou RG</td>
      <td width="70%">
        <mtw:inputText name="txt_ie" id="txt_ie" size="30" maxlength="50" value="${fornecedor.inscricaoEstadual}" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" rows="6" cols="60">${fornecedor.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <c:if test="${fornecedor.id > 0}">
          <input name="btn_pesquisar_vendas" type="button" value="Vendas" onClick="javascript:popupPesquisarComprasFornecedor(${fornecedor.id});" class="button">&nbsp;
          <input name="btn_pesquisar_produtos" type="button" value="Produtos" onClick="javascript:popupPesquisarProdutosFornecedor(${fornecedor.id});" class="button">&nbsp;
        </c:if>
        <input name="btn_gravar" type="submit" value="Gravar" class="button">&nbsp;
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/fornecedor.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>