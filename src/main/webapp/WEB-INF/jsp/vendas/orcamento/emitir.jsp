<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Preencha o formulário para imprimir o orçamento">
  <meta name="decorator" content="popup">
  <mtw:inputMaskConfig />
  <title>Orçamento de Venda</title>
  <script language="JavaScript" type="text/javascript">
  function emitir() {
      $("#form_orcamento").submit();
      $("<p align='center'><br>Aguarde o download do arquivo para fechar a janela</p>").dialog({
    	  modal: true,
    	  resizable: false,
          title: "Download ...",
          open: function(event, ui) { $(".ui-dialog-titlebar").hide(); }
      });
  }
  $(function() {
	  $("#txt_transportador").focus();
  });
  </script>
</head>
<body>
<form name="form_orcamento" id="form_orcamento" method="post" action="<%=request.getContextPath()%>/vendas/orcamento.gravar.action">
  <mtw:input type="hidden" name="hid_id" />
  <center>
  <table class="form">
    <tr>
      <td width="30%" class="label">Transportador</td>
      <td width="70%">
        <mtw:inputText name="txt_transportador" id="txt_transportador" size="40" maxlength="100" onkeyup="isUpper(this);" />
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