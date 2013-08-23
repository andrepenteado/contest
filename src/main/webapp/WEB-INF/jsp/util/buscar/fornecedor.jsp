<script language="JavaScript" type="text/javascript">
function popupPesquisarFornecedor() {
    $("<iframe id='buscar-fornecedor' style='border: 0px;' src='${pageContext.request.contextPath}/cadastros/fornecedor.iniciar.action?popup=1'/>")
    .dialog({
        modal: true,
        title: "Buscar Fornecedor",
        width: $(window).width()*0.6,
        height: $(window).height()*0.6,
        close: function(event, ui) { $(this).dialog('destroy')},
        open: function (event,ui) {$(this).css("width","95%")}
    });
}
</script>

<div class="erro_fornecedor error"></div>
<mtw:inputText name="txt_id_fornecedor" id="txt_id_fornecedor" size="8" onkeypress="javascript:buscarFornecedor(event);" value="${compra.fornecedor.id}" />
<script type="text/javascript">
 $("#txt_id_fornecedor").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13') {
       $.getJSON('${pageContext.request.contextPath}/pesquisar.fornecedor.action?txt_id_fornecedor=' + $("#txt_id_fornecedor").val(), function(actionOutput) {
          if (actionOutput.nomeFornecedor != undefined) {
             $("#txt_nome_fornecedor").val(actionOutput.nomeFornecedor);
             $("div.erro_fornecedor").text("");
          }
          if (actionOutput.erroBuscarFornecedor != undefined) {
             $("#txt_nome_fornecedor").val("");
             $("div.erro_fornecedor").text(actionOutput.erroBuscarFornecedor);
          }
       });      
       event.preventDefault();
    }
 });
</script>
<button type="button" class="ui-button ui-state-default ui-corner-all" onclick="javascript:popupPesquisarFornecedor();">
 <span class="ui-icon ui-icon-search"></span>
</button>
<mtw:inputText name="txt_nome_fornecedor" id="txt_nome_fornecedor" size="40" readonly="true" value="${compra.fornecedor.nome}" />