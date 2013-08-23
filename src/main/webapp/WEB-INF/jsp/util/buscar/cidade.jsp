<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
function preencherComboCidades() {
    $("#cbo_cidade").html('<option value=""> - </option>');
    $.getJSON('${pageContext.request.contextPath}/pesquisar.cidades.action?id_estado=' + $("#cbo_estado").val(), function(actionOutput) {
        $.each(actionOutput.cidades, function(i, cidade) {
            $("#cbo_cidade").append('<option value=\'' + cidade.id + '\'>' + cidade.nome + '</option>');
        });
        <c:if test="${idCidade > 0}">
            $("#cbo_cidade").val("${idCidade}");
        </c:if>
    });
}
</script>

<tr>
  <td width="30%" class="label">Estado</td>
  <td width="70%">
    <mtw:select name="cbo_estado" id="cbo_estado" list="estados" emptyField="true" extra="onchange=preencherComboCidades();" />
  </td>
</tr>
<tr>
  <td width="30%" class="label">Cidade</td>
  <td width="70%">
    <select id="cbo_cidade" name="cbo_cidade"/>
  </td>
</tr>

<script type="text/javascript">
<c:if test="${idEstado > 0}">
    $("#cbo_estado").val("${idEstado}");
    preencherComboCidades();
</c:if>
</script>