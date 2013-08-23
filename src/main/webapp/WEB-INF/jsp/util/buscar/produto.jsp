<script type="text/javascript">
function popupPesquisarProduto() {
    $("<iframe id='buscar-produto' style='border: 0px;' src='${pageContext.request.contextPath}/cadastros/produto.iniciar.action?popup=1'/>")
    .dialog({
        modal: true,
        title: "Buscar Produto",
        width: $(window).width()*0.6,
        height: $(window).height()*0.6,
        close: function(event, ui) { $(this).dialog('destroy')},
        open: function (event,ui) {$(this).css("width","95%")}
    });
 }
function buscarProduto(event) {
    if (isEnter(event)) {
        document.forms[0].action = '${pageContext.request.contextPath}/pesquisar.produto.action?target=${pageContext.request.requestURI}';
        document.forms[0].submit();
    }
}
</script>
<mtw:input type="hidden" id="txt_id_produto" name="txt_id_produto"/>
<div class="error"><mtw:error field="txt_descricao_produto"/></div>
<mtw:inputText name="txt_referencia_produto" id="txt_referencia_produto" size="10" onkeypress="javascript:buscarProduto(event);" onkeyup="isUpper(this);" />
<button type="button" class="ui-button ui-state-default ui-corner-all"
       onclick="javascript:popupPesquisarProduto();">
  <span class="ui-icon ui-icon-search"></span>
</button>
<mtw:inputText name="txt_descricao_produto" id="txt_descricao_produto" readonly="true" size="40" />