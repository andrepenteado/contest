<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário com os dados do funcionário">
  <title>Cadastro de Funcionário</title>
  <mtw:inputMaskConfig />
</head>
<body onload="javascript:document.form_cadastro_funcionario.txt_nome.focus();">
<form name="form_cadastro_funcionario" method="post" action="<%=request.getContextPath()%>/cadastros/funcionario.gravar.action">
  <mtw:input type="hidden" name="hid_id" value="${funcionario.id}" />
  <table class="form">
    <tr>
      <td width="30%" class="label">Nome</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_nome"/></div>
        <mtw:inputText name="txt_nome" id="txt_nome" size="60" maxlength="100" value="${funcionario.nome}" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Telefone</td>
      <td width="70%">
        <mtw:inputMask name="txt_telefone" id="txt_telefone" maskDefined="FONE" value="${funcionario.telefone}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Comissão</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_comissao"/></div>
        <mtw:inputMask name="txt_comissao" id="txt_comissao" maskCustom="9**9" size="5" maxlength="30" value="${funcionario.comissao}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Identificação de Acesso</td>
      <td width="70%">
        <mtw:inputText name="txt_identificacao" id="txt_identificacao" size="40" maxlength="60" value="${funcionario.identificacao}" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Senha</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_senha"/></div>
        <mtw:input type="password" name="txt_senha" id="txt_senha" size="40" maxlength="60" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Confirme Senha</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_confirme_senha"/></div>
        <mtw:input type="password" name="txt_confirme_senha" id="txt_confirme_senha" size="40" maxlength="60" />
      </td>
    </tr>
    <tr>
      <td class="label">Categorias de Acesso</td>
      <td>
        <mtw:checkboxes list="categorias" name="txt_categorias" id="txt_categorias" useBR="true"/>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${funcionario.observacao}</mtw:textarea></p>
      </td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/funcionario.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>