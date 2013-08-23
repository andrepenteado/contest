<script type="text/javascript">
function buscarCfop(event) {
    if (isEnter(event)) {
        document.forms[0].action = '${pageContext.request.contextPath}/pesquisar.cfop.action?target=${pageContext.request.requestURI}';
        document.forms[0].submit();
    }
}
function popupPesquisarCfop() {
    $("<iframe id='buscar-cfop' style='border: 0px;' src='${pageContext.request.contextPath}/cadastros/cfop.iniciar.action?popup=1'/>")
    .dialog({
        modal: true,
        title: "Buscar CFOP",
        width: $(window).width()*0.6,
        height: $(window).height()*0.6,
        close: function(event, ui) { $(this).dialog('destroy')},
        open: function (event,ui) {$(this).css("width","95%")}
    });
 }
 function closePopup() {
     $('#buscar-cfop').dialog('close');
     return false;
 }
</script>
<mtw:input type="hidden" id="txt_id_cfop" name="txt_id_cfop"/>
<div class="error"><mtw:error field="txt_descricao_cfop"/></div>
<mtw:inputText name="txt_codigo_cfop" id="txt_codigo_cfop" size="8" onkeypress="javascript:buscarCfop(event);" />
<a href="javascript:popupPesquisarCfop();"><img src="images/pesquisar.jpg" border="0" align="middle"></a>
<mtw:inputText name="txt_descricao_cfop" id="txt_descricao_cfop" readonly="true" size="40" />