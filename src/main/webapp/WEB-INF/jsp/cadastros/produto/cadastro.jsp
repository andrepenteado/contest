<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="br.com.alphadev.contest.KGlobal"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados do produto">
  <title>Cadastro de Produto</title>
  <mtw:inputMaskConfig />
  <mtw:inputMoneyConfig />
</head>
<body onload="javascript:document.form_cadastro_produto.txt_referencia.focus();">
<form name="form_cadastro_produto" method="post" action="<%=request.getContextPath()%>/cadastros/produto.gravar.action" enctype="multipart/form-data">
  <mtw:input type="hidden" name="hid_id" value="${produto.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Referência</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_referencia"/></div>
        <mtw:inputText name="txt_referencia" id="txt_referencia" size="30" maxlength="100" value="${produto.referencia}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">NCM</td>
      <td width="70%">
        <mtw:inputText name="txt_ncm" id="txt_ncm" size="30" maxlength="100" value="${produto.ncm}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Código Externo</td>
      <td width="70%">
        <mtw:inputText name="txt_codigo_externo" id="txt_codigo_externo" size="30" maxlength="100" value="${produto.codigoExterno}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Descrição</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_descricao"/></div>
        <mtw:inputText name="txt_descricao" id="txt_descricao" size="60" maxlength="100" value="${produto.descricao}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <%--
    <tr>
      <td width="30%" class="label" style="padding-top: 6px;">Foto</td>
      <td width="70%">
        <input type="file" name="txt_foto" id="txt_foto" size="45"/>
        <br />
        <img src="<%=request.getContextPath()%>/public.visualizarFoto.action?txt_id=${produto.id}" alt="foto" width="250" height="200" />
      </td>
    </tr>
    --%>
    <tr>
      <td width="30%" class="label">Unidade</td>
      <td width="70%">
        <mtw:radiobuttons name="lst_unidade" id="lst_unidade" list="unidade" defValue="${produto.unidade}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Medida</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_medida"/></div>
        <mtw:inputText name="txt_medida" id="txt_medida" size="10" value="${produto.medida}"/>
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Tipo</td>
      <td width="70%">
        <mtw:radiobuttons name="lst_tipo" id="lst_tipo" list="tipoProduto" defValue="${produto.tipo}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Grupo</td>
      <td width="70%">
        <mtw:select name="cbo_grupo" id="cbo_grupo" list="listaGruposProdutos" defValue="${produto.grupoProduto.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Tabela de Preços</td>
      <td width="70%">
        <mtw:select name="cbo_tabela_preco" id="cbo_tabela_preco" list="listaTabelasPrecos" defValue="${produto.tabelaPreco.id}" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Venda à Vista</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_venda_vista"/></div>
        <mtw:inputMoney name="txt_venda_vista" id="txt_venda_vista" size="30" value="${produto.vendaVista}" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Venda à Prazo</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_venda_prazo"/></div>
        <mtw:inputMoney name="txt_venda_prazo" id="txt_venda_prazo" size="30" value="${produto.vendaPrazo}" dec_point="," textAlign="right" thousands_sep="." />
      </td>
    </tr>
    <mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_ADMINISTRATIVO + "," + KGlobal.CATEGORIA_SUPERUSUARIO%>">
      <tr>
        <td width="30%" class="label">Custo à Vista</td>
        <td width="70%">
          <mtw:inputText name="txt_custo_vista" id="txt_custo_vista" size="30" value="${produto.custoVista}" readonly="true" />
        </td>
      </tr>
      <tr>
        <td width="30%" class="label">Custo à Prazo</td>
        <td width="70%">
          <mtw:inputText name="txt_custo_prazo" id="txt_custo_prazo" size="30" value="${produto.custoPrazo}" readonly="true" />
        </td>
      </tr>
      <c:forEach items="${produto.estoques}" var="estoque">
        <tr>
          <td width="30%" class="label">Spec.Out</td>
          <td width="70%">
            <mtw:inputMask name="txt_spec_out_${estoque.empresa.id}" id="txt_spec_out_${estoque.empresa.id}" maskCustom="99999" size="10" value="${estoque.specOut}" />
            - ${estoque.empresa.nomeFantasia}
          </td>
        </tr>
        <tr>
          <td width="30%" class="label">Spec.In</td>
          <td width="70%">
            <mtw:inputMask name="txt_spec_in_${estoque.empresa.id}" id="txt_spec_in_${estoque.empresa.id}" maskCustom="99999" size="10" value="${estoque.specIn}" />
            - ${estoque.empresa.nomeFantasia}
          </td>
        </tr>
      </c:forEach>
    </mtw:hasAuthorization>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${produto.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/produto.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>