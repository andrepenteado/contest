<%=ConfigHelper.get().getString("sistema.descricao")%> - Versão <%=ConfigHelper.get().getString("sistema.versao")%></br>&copy; 2010 André Penteado Soluções

<!-- Fix para body.onload do sitemesh3 -->
<input id="body.onload" type="hidden" value="<sitemesh:write property="body.onload" />"/>
<script type="text/javascript">
  var bodyOnLoad = document.getElementById('body.onload').value;
  if (bodyOnLoad) {
    eval(bodyOnLoad);
  }
</script>