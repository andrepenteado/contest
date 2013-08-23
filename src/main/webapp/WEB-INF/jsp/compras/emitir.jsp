<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Entre com os dados no formulário para emitir nota fiscal de compra">
  <meta name="decorator" content="popup">
  <title>Emitir Nota Fiscal de Compra</title>
  <mtw:inputMaskConfig />
  <mtw:inputDateConfig />
  <script language="JavaScript" type="text/javascript">
    function emitir() {
        document.form_nf.submit();
        pCtrl.returnValue(new Array());
    }
  </script>
</head>
<body onload="javascript:document.form_nf.txt_nota_fiscal.focus();">
<form name="form_nf" method="post" action="<%=request.getContextPath()%>/compras/pedido.notaFiscal.action">
  <mtw:input type="hidden" name="hid_id" />
  <center>
  <table class="form">
    <tr>
      <td class="label">Nota Fiscal</td>
      <td>
        <mtw:inputMask name="txt_nota_fiscal" id="txt_nota_fiscal" size="10" maskCustom="999999999" textAlign="right" />
      </td>
    </tr>
    <tr>
      <td class="label">Emissão</td>
      <td>
        <div class="error"><mtw:error field="txt_emissao"/></div>
        <mtw:inputDate name="txt_emissao" id="txt_emissao" size="10" value="${compra.stringEmissao}" />
      </td>
    </tr>
    <tr>
      <td class="label">ICMS</td>
      <td>
        <mtw:inputMask name="txt_icms" id="txt_icms" size="10" maskCustom="999" textAlign="right" /> %
      </td>
    </tr>
    <tr>
      <td class="label">IPI</td>
      <td>
        <mtw:inputMask name="txt_ipi" id="txt_ipi" size="10" maskCustom="999" textAlign="right" /> %
      </td>
    </tr>
    <tr>
      <td class="label">Frete</td>
      <td>
        <mtw:select name="cbo_frete" id="cbo_frete" list="frete" emptyField="false" />
      </td>
    </tr>
    <tr>
      <td width="30%" class="label">Transportador</td>
      <td width="70%">
        <mtw:inputText name="txt_transportador" id="txt_transportador" size="40" maxlength="100" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td class="label">CFOP</td>
      <td>
        <div class="error"><mtw:error field="cbo_cfop"/></div>
        <mtw:select name="cbo_cfop" id="cbo_cfop" list="cfop" emptyField="false" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <center><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60"/></center>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="button" value="Gravar" class="button" onClick="javascript:this.disabled=true; emitir();">
      </td>
    </tr>
  </table>
  </center>
</form>
</body>
</html>