<%@page import="com.github.andrepenteado.contest.KGlobal"%>

<script type="text/javascript">
function popupPesquisarFuncionario() {
   $("<iframe id='buscar-funcionario' style='border: 0px;' src='${pageContext.request.contextPath}/cadastros/funcionario.iniciar.action?popup=1'/>")
   .dialog({
       modal: true,
       title: "Buscar Funcionário",
       width: $(window).width()*0.6,
       height: $(window).height()*0.6,
       close: function(event, ui) { $(this).dialog('destroy')},
       open: function (event,ui) {$(this).css("width","95%")}
   });
}
</script>

<mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_SUPERUSUARIO.concat(",").concat(KGlobal.CATEGORIA_VENDAS).concat(",").concat(KGlobal.CATEGORIA_ADMINISTRATIVO)%>">
 <div class="erro_funcionario error"></div>
 <mtw:inputText name="txt_id_funcionario" id="txt_id_funcionario" size="5" value="${venda.funcionario.id}" />
 <script type="text/javascript">
   $("#txt_id_funcionario").keypress(function(event) {
      var keycode = (event.keyCode ? event.keyCode : event.which);
      if(keycode == '13') {
         $.getJSON('${pageContext.request.contextPath}/pesquisar.funcionario.action?txt_id_funcionario=' + $("#txt_id_funcionario").val(), function(actionOutput) {
        	if (actionOutput.nomeFuncionario != undefined) {
        	   $("#txt_nome_funcionario").val(actionOutput.nomeFuncionario);
        	   $("div.erro_funcionario").text("");
        	   $("#cbo_prazo_pagamento").focus();
        	}
        	if (actionOutput.erroBuscarFuncionario != undefined) {
        	   $("#txt_nome_funcionario").val("");
        	   $("div.erro_funcionario").text(actionOutput.erroBuscarFuncionario);
        	}
         }); 	  
         event.preventDefault();
      }
   });
 </script>
 <button type="button" class="ui-button ui-state-default ui-corner-all" onclick="javascript:popupPesquisarFuncionario();">
   <span class="ui-icon ui-icon-search"></span>
 </button>
 <mtw:inputText name="txt_nome_funcionario" id="txt_nome_funcionario" value="${venda.funcionario.nome}" readonly="true" size="40" />
</mtw:hasAuthorization>
<mtw:hasAuthorization groups="<%=KGlobal.CATEGORIA_VENDEDOR%>">
  <div class="error"><mtw:error field="txt_id_funcionario"/><mtw:error field="txt_nome_funcionario"/></div>
  <mtw:input type="hidden" name="txt_id_funcionario" id="txt_id_funcionario" value="${user.funcionario.id}"/>
  <mtw:inputText name="txt_nome_funcionario" id="txt_nome_funcionario" size="40" value="${user.funcionario.nome}" readonly="true" />
</mtw:hasAuthorization>