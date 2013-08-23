<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/estoque/relatorio.previsao.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:tabPanelConfig skin="win2k"/>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Situação e Previsão de Estoque">
  <title>Previsão de Estoque</title>
</head>
<body>
  <form name="form_pesquisar_tcp" method="post" action="${linkPesquisar}">
  <center>
  <mtw:tabPanel name="panelPrevisãoEstoque">
    <mtw:tabPage caption="Previsão por Produto" name="pageProduto">
      <table class="form">
        <tr>
          <td width="25%" class="label">Produto</td>
          <td width="75%">
            <%@include file="../../util/buscar/produto.jsp" %>
          </td>
        </tr>
        <tr>
          <td width="100" class="label">Período de </td>
          <td width="400">
            <mtw:inputDate name="txt_data_inicial" id="txt_data_inicial" size="10" />
            Até 
            <mtw:inputDate name="txt_data_final" id="txt_data_final" size="10" />
          </td>
          <td width="100" class="label"> </td>
          <td>
            
          </td>
        </tr>
        <tr>
          <td class="label">Dias de Segurança </td>
          <td>
            <mtw:inputText name="txt_dias_seguranca" id="txt_dias_seguranca" size="40" onkeyup="isUpper(this);" />
          </td>
        </tr>        
        <tr>
          <td class="label">Periodicidade de Compra</td>
          <td>
            <div class="error"><mtw:error field="cbo_periodicidade"/></div>
            <mtw:select list="periodicidadeEstatistica" name="cbo_periodicidade" id="cbo_periodicidade" emptyField="true" />
          </td>
        </tr>
        <tr>
          <td class="label">Tempo de Entrega do pedido </td>          
        <td>
             <div class="error"><mtw:error field="rad_pagto"/></div>
             <mtw:input type="radio" name="rad_entrega" id="rad_entrega" value="V" /> Imediato &nbsp;
             <mtw:input type="radio" name="rad_entrega" id="rad_entrega" value="P" /> Demora  &nbsp;
             <mtw:inputText name="txt_dias_demora" id="txt_dias_demora" size="3" onkeyup="isUpper(this);" /> Dias         
        </td>
        </tr>
        <tr><td>&nbsp;</td></tr>        
        <tr>
          <td colspan="4" class="barrabotao">
            <input name="btn_pesquisar" type="submit" value="Pesquisar" class="button">
          </td>
        </tr>
      </table>
    </mtw:tabPage>
    <mtw:tabPage caption="Por Fornecedor" name="pageFornecedor">
    </mtw:tabPage>
    <mtw:tabPage caption="Curva ABC" name="pageCurva">
    </mtw:tabPage>
  </mtw:tabPanel>
  <display:table name="lista" pagesize="50" export="false" id="previsaoEstoque" requestURI="${linkPesquisar}">
    <display:column title="Produto">
      ${objeto[0]}      
    </display:column>
     <display:column title="Qtde. Vista">
            
    </display:column>
    <display:column title="Qtde. Prazo">
            
    </display:column>
    <display:column title="Fisico">
            
    </display:column>
    <display:column title="Minimo">
            
    </display:column>
    <display:column title="Duração Estoque">
            
    </display:column>
    <display:column title="Sugestão Compra">
            
    </display:column>
    <display:column title="<center>Detalhes</center>" style="text-align: center;" headerClass="center">
      <a href="javascript:exibir(${movimentoEstoque.id});"><img src="images/imprimir.png" border="0"/></a>
    </display:column>
    
  </display:table>
  
  </center>
  </form>
</body>
</html>