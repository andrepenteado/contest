<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkProcessar"><%=request.getContextPath()%>/financeiro/retorno.processar.action</c:set>
<c:set var="linkNovoProcessamento"><%=request.getContextPath()%>/financeiro/retorno.iniciar.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <mtw:tabPanelConfig skin="win2k"/>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Processar retorno bancário de contas a receber">
  <title>Processar Retorno Bancário</title>
  <script language="JavaScript" type="text/javascript">
  function exibir(id) {
      pCtrl.search('<%=request.getContextPath()%>/compras/pedido.exibir.action?txt_id=' + id, 600, 500, new Array());
  }  
  function inserirNovoCampo() {
 	  var contador = document.getElementById("txt_contador_campo").value;		
		
      var quebra = document.createElement("br");
      var inputFile = document.createElement("input");
      inputFile.setAttribute("type", "file");
      inputFile.setAttribute("size", "70%");
      inputFile.setAttribute("name", "txt_arquivo_" + contador);
      inputFile.setAttribute("id", "txt_arquivo_" + contador);
      inputFile.setAttribute("onchange","javascript:inserirNovoCampo();");

      var inserir = document.getElementById("id_inserir_dinamico");
      inserir.insertBefore(quebra, inserir.firstChild);
      inserir.insertBefore(inputFile, inserir.firstChild);
      
      document.getElementById("txt_contador_campo").value = parseInt(contador) + 1;
  }
  </script>
</head>
<body>
	<c:if test="${listaCompra == null}">
	  <form name="form_processar" method="post" action="${linkProcessar}" enctype="multipart/form-data">
	  	<input type="hidden" id="txt_contador_campo" name="txt_contador_campo" value="${contadorCampo}">
		<center>
	    <table class="form">
	      <tr><td colspan="2">&nbsp;</td></tr>
	      <tbody id="campoFile">
		      <tr>
		        <td>
                  <b>Arquivos:</b>
                  <div id="id_inserir_dinamico">
                    <input type="file" size="70%" name="txt_arquivo_1" id="txt_arquivo_1" onchange="javascript:inserirNovoCampo();"/>
                  </div>
		        </td>
		      </tr>
	      </tbody>
	      <tr><td colspan="2">&nbsp;</td></tr>
	      <tr>
	        <td class="barrabotao" colspan="2">
	          <input name="btn_processar" type="submit" value="Processar" class="button">
	        </td>
	      </tr>
	    </table>
		</center>
	  </form>
	</c:if>
  	
  	<c:if test="${listaCompra != null}">
	    <form name="form_nova_importacao" method="post" action="${linkNovoProcessamento}">
			<center>
		    <table class="form">
		      <tr>
		        <td class="barrabotao">
		          <input name="btn_novo" type="submit" value="Novo Processamento" class="button" >
		        </td>
		      </tr>
		    </table>
			</center>
		</form>
		  	
    	<center><h3>Resultado: Processamento realizado com sucesso.</h3></center>
		<display:table name="listaCompra" pagesize="50" export="false" id="compra" >
			<display:column property="notaFiscal" title="NF" style="text-align: center;" headerClass="center"/>
		    <display:column title="Fornecedor">
		    	<a href="javascript:exibir(${compra.id});">${compra.fornecedor.nome}</a>
		    </display:column>
		    <display:column title="Emissão" style="text-align: center;" headerClass="center">
		      	<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${compra.emissao}"/>
		    </display:column>
		    <display:column title="Total" style="text-align: right;" headerClass="right">
		      	<fmt:formatNumber pattern="#0.00">${compra.totalProduto}</fmt:formatNumber>
		    </display:column>
		    <display:column title="Consultar" style="text-align: center;" headerClass="center">
		      	<a href="javascript:exibir(${compra.id});"><img src="images/pesquisar.jpg" border="0"/></a>
		    </display:column>
		</display:table>
  	</c:if>
  
	<c:if test="${mapErros != null}">
		<br>
    	<center><h3 style="color:red">Resultado: Problemas com o processamento.</h3></center>

		<div class="error" >
		  	<c:forEach items="${mapErrosKeys}" var="nota">
		  		<center>
				<table width="60%" style="margin:15px;">
					<tr>
						<td align="center">${nota}</td>
					</tr> 	
			  		<c:forEach items="${mapErros[nota]}" var="erro">
			  			<tr>
			  				<td align="center">${erro}</td>
			  			</tr>	
					</c:forEach>			
				</table>
				</center>		
		  	</c:forEach>
		</div>
 	  	
	</c:if>
	
</body>
</html>