<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Registro de Visitantes">
  <title>Visitantes</title>
  <mtw:inputDateConfig />
  <script language="JavaScript" type="text/javascript">
  var gCtx = null;
  var gCanvas = null;
  var imageData = null;
  var ii = 0;
  var jj = 0;
  var c = 0;
  function fotografiaOnLoad() {
      initCanvas(320, 240);
  }
  function initCanvas(ww, hh) {
      gCanvas = document.getElementById("canvas");
      var w = ww;
      var h = hh;
      gCanvas.style.width = w + "px";
      gCanvas.style.height = h + "px";
      gCanvas.width = w;
      gCanvas.height = h;
      gCtx = gCanvas.getContext("2d");
      //myimage = new Image();
      //myimage.src = '/admin/common.buscarArquivo.action?id=&hash=';
      gCtx.clearRect(0, 0, w, h);
      //gCtx.drawImage(myimage, 0, 0, 320, 240);
      imageData = gCtx.getImageData(0, 0, 320, 240);
  }
  function passLine(stringPixels) {
      var coll = stringPixels.split("-");
      for ( var i = 0; i < 320; i++) {
          var intVal = parseInt(coll[i]);
          r = (intVal >> 16) & 0xff;
          g = (intVal >> 8) & 0xff;
          b = (intVal) & 0xff;
          imageData.data[c + 0] = r;
          imageData.data[c + 1] = g;
          imageData.data[c + 2] = b;
          imageData.data[c + 3] = 255;
          c += 4;
      }
      if (c >= 320 * 240 * 4) {
          c = 0;
          gCtx.putImageData(imageData, 0, 0);
      }
  }
  function captureToCanvas() {
      flash = document.getElementById("embedflash");
      flash.ccCapture();
      gCanvas = document.getElementById("canvas");
      document.form_visitante.foto_canvas_base64.value = gCanvas.toDataURL();
  }
  </script>
</head>
<body onload="javascript:document.form_visitante.txt_nome.focus(); fotografiaOnLoad();">
<form name="form_visitante" method="post" action="<%=request.getContextPath()%>/recepcao/visitante.gravar.action">
  <table class="form">
    <tr>
      <td width="30%" class="label">Nome</td>
      <td width="70%">
        <div class="error"><mtw:error field="txt_nome"/></div>
        <mtw:inputText name="txt_nome" id="txt_nome" size="60" maxlength="100" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td class="label">N&ordm; RG</td>
      <td>
        <div class="error"><mtw:error field="txt_rg"/></div>
        <mtw:inputText name="txt_rg" id="txt_rg" size="60" maxlength="100" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td class="label">Emissão RG</td>
      <td>
        <div class="error"><mtw:error field="txt_emissao_rg"/></div>
        <mtw:inputDate name="txt_emissao_rg" id="txt_emissao_rg" size="10" />
      </td>
    </tr>
    <tr>
      <td class="label">Orgão Emissor RG</td>
      <td>
        <div class="error"><mtw:error field="cbo_orgao_rg"/></div>
        <mtw:select name="cbo_orgao_rg" id="cbo_orgao_rg" list="estados" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <p align="center"><mtw:textarea name="txt_observacao" id="txt_observacao" rows="6" cols="60">${grupoProduto.observacao}</mtw:textarea></p>
      </td>
    </tr>

    <input type="hidden" name="foto_canvas_base64" id="foto_canvas_base64" />
    <tr>
      <th colspan="2">Fotografia</th>
    </tr>
    <tr>
      <td align="center">
        <object id="iembedflash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0"
                width="320" height="240">
          <param name="movie" value="/contest/skins/transparentia/images/camcanvas.swf" />
          <param name="quality" value="high" />
          <param name="allowScriptAccess" value="always" />
          <embed allowScriptAccess="always" id="embedflash" 
                 src="/contest/skins/transparentia/images/camcanvas.swf" quality="high" width="320" height="240"
                 type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"
                 mayscript="true" />
        </object>
        <br>
        <a href="javascript:captureToCanvas()"><img src="/contest/skins/transparentia/images/photo.png"/>Fotografar</a>
      </td>
       <td align="center">
        <canvas style="border:1px solid blue" id="canvas" width="320" height="240"></canvas>
      </td>
    </tr>

  </table>
  <br>
  <h1>Entrada</h1>
  <cite>Incluir o registro de entrada do visitante</cite>

  <table class="form">
    <tr>
      <td class="label">Meio de Transporte</td>
      <td>
        <div class="error"><mtw:error field="cbo_transporte"/></div>
        <mtw:select name="cbo_transporte" id="cbo_transporte" list="transporte" emptyField="true" />
      </td>
    </tr>
    <tr>
      <td class="label">Identificação do Transporte</td>
      <td>
        <div class="error"><mtw:error field="txt_identificao_transporte"/></div>
        <mtw:inputText name="txt_identificao_transporte" id="txt_identificao_transporte" size="60" maxlength="100" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr>
      <td class="label">Data Entrada</td>
      <td>
        <div class="error"><mtw:error field="txt_data_entrada"/></div>
        <mtw:inputDate name="txt_data_entrada" id="txt_data_entrada" size="10" />
      </td>
    </tr>
    <tr>
      <td class="label">Hora Entrada</td>
      <td>
        <div class="error"><mtw:error field="txt_hora_entrada"/></div>
        <mtw:inputText name="txt_hora_entrada" id="txt_hora_entrada" size="30" onkeyup="isUpper(this);" />
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="2" class="barrabotao">
        <input name="btn_gravar" type="submit" value="Gravar" class="button">
        <input name="btn_voltar" type="button" value="Voltar" onClick="window.location.href='<%=request.getContextPath()%>/cadastros/grupoProduto.iniciar.action';" class="button">
      </td>
    </tr>
  </table>
</form>
</body>
</html>