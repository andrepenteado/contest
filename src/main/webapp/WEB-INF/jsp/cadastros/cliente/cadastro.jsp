<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados do cliente">
  <title>Cadastro de Cliente</title>
  <mtw:inputMaskConfig />
  <script type="text/javascript">
  function popupPesquisarVendasCliente(id) {
      pCtrl.search('<%=request.getContextPath()%>/cadastros/cliente.vendas.action?txt_id=' + id, 600, 400, new Array());
  }
  function popupPesquisarProdutosCliente(id) {
      pCtrl.search('<%=request.getContextPath()%>/cadastros/cliente.itens.action?txt_id=' + id, 600, 400, new Array());
  }
  </script>
</head>
<body onload="javascript:document.form_cadastro_cliente.txt_nome.focus();">

<div align="right">
  Desde: <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${cliente.dataCadastro}"/> 
</div>

<form name="form_cadastro_cliente" method="post" action="<%=request.getContextPath()%>/cadastros/cliente.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${cliente.id}" />
  <table class="form">
    <tr>
      <td width="20%" class="label">Nome</td>
      <td width="80%">
        <div class="error"><mtw:error field="txt_nome"/></div>
        <mtw:inputText name="txt_nome" id="txt_nome" size="60" maxlength="100" value="${cliente.nome}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td class="label">Rua</td>
      <td>
        <mtw:inputText name="txt_rua" id="txt_rua" size="60" maxlength="100" value="${cliente.rua}" />
      </td>
    </tr>
    <tr>
      <td class="label">Número</td>
      <td>
        <mtw:inputText name="txt_numero" id="txt_numero" size="30" maxlength="50" value="${cliente.numero}" />
      </td>
    </tr>
    <tr>
      <td class="label">Complemento</td>
      <td>
        <mtw:inputText name="txt_complemento" id="txt_complemento" size="60" maxlength="100" value="${cliente.complemento}" />
      </td>
    </tr>
    <tr>
      <td class="label">Bairro</td>
      <td>
        <mtw:inputText name="txt_bairro" id="txt_bairro" size="30" maxlength="50" value="${cliente.bairro}" />
      </td>
    </tr>
    <tr>
      <td class="label">CEP</td>
      <td>
        <mtw:inputMask name="txt_cep" id="txt_cep" maskDefined="CEP" value="${cliente.cep}" />
      </td>
    </tr>

    <c:set var="idEstado">${cliente.cidade.estado.id}</c:set>
    <c:set var="idCidade">${cliente.cidade.id}</c:set>
    <%@include file="../../util/buscar/cidade.jsp" %>

    <tr>
      <td class="label">Telefone</td>
      <td>
        <mtw:inputMask name="txt_telefone" id="txt_telefone" maskDefined="FONE" value="${cliente.telefone}" />
      </td>
    </tr>
    <tr>
      <td class="label">Fax</td>
      <td>
        <mtw:inputMask name="txt_fax" id="txt_fax" maskDefined="FONE" value="${cliente.fax}" />
      </td>
    </tr>
    <tr>
      <td class="label">E-mail</td>
      <td>
        <div class="error"><mtw:error field="txt_email"/></div>
        <mtw:inputText name="txt_email" id="txt_email" size="60" maxlength="100" value="${cliente.email}" />
      </td>
    </tr>
    <tr>
      <td class="label">Página Internet</td>
      <td>
        <mtw:inputText name="txt_pagina_internet" id="txt_pagina_internet" size="60" maxlength="100" value="${cliente.paginaInternet}" />
      </td>
    </tr>
    <tr>
      <td class="label">Contato</td>
      <td>
        <mtw:inputText name="txt_contato" id="txt_contato" size="60" maxlength="100" value="${cliente.contato}" />
      </td>
    </tr>
    <tr>
      <td class="label">CNPJ ou CPF</td>
      <td>
        <mtw:inputText name="txt_cnpj" id="txt_cnpj" size="30" value="${cliente.cnpj}" />
      </td>
    </tr>
    <tr>
      <td class="label">Insc.Estadual ou RG</td>
      <td>
        <mtw:inputText name="txt_ie" id="txt_ie" size="30" maxlength="50" value="${cliente.inscricaoEstadual}" />
      </td>
    </tr>
    <tr>
      <td class="label">Vendedor</td>
      <td>
        <mtw:select name="cbo_vendedor" id="cbo_vendedor" list="listaVendedores" defValue="${cliente.funcionario.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" rows="6" cols="60">${cliente.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <center>
        <c:if test="${cliente.id > 0}">
          <input name="btn_pesquisar_vendas" type="button" value="Vendas" onClick="javascript:popupPesquisarVendasCliente(${cliente.id});" class="button">&nbsp;
          <input name="btn_pesquisar_produtos" type="button" value="Produtos" onClick="javascript:popupPesquisarProdutosCliente(${cliente.id});" class="button">&nbsp;
        </c:if>
        <input name="btn_gravar" type="submit" value="Gravar" class="button">&nbsp;
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/cliente.iniciar.action';" class="button">
        </center>
      </td>
    </tr>
  </table>
</form>
</body>
</html>