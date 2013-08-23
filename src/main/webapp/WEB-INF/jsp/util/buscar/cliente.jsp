<script type="text/javascript">
function popupPesquisarCliente() {
    $("<iframe id='buscar-cliente' style='border: 0px;' src='${pageContext.request.contextPath}/cadastros/cliente.iniciar.action?popup=1'/>")
    .dialog({
        modal: true,
        title: "Buscar Cliente",
        width: $(window).width()*0.6,
        height: $(window).height()*0.6,
        close: function(event, ui) { $(this).dialog('destroy')},
        open: function (event,ui) {$(this).css("width","95%")}
    });
 }
</script>
<div class="erro_cliente error"></div>
<mtw:inputText name="txt_id_cliente" id="txt_id_cliente" size="5" value="${venda.cliente.id}" />
<script type="text/javascript">
 $("#txt_id_cliente").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13') {
       $.getJSON('${pageContext.request.contextPath}/pesquisar.cliente.action?txt_id_cliente=' + $("#txt_id_cliente").val(), function(actionOutput) {
          if (actionOutput.nomeCliente != undefined) {
             $("#txt_nome_cliente").val(actionOutput.nomeCliente);
             $("div.erro_cliente").text("");
             $("#txt_id_funcionario").focus();
          }
          if (actionOutput.erroBuscarCliente != undefined) {
             $("#txt_nome_cliente").val("");
             $("div.erro_cliente").text(actionOutput.erroBuscarCliente);
          }
       });      
       event.preventDefault();
    }
 });
</script>
<button type="button" class="ui-button ui-state-default ui-corner-all"
       onclick="javascript:popupPesquisarCliente();">
  <span class="ui-icon ui-icon-search"></span>
</button>
<mtw:inputText name="txt_nome_cliente" id="txt_nome_cliente" size="40" readonly="true" value="${venda.cliente.nome}" />