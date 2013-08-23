<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page errorPage="/comum.erro.action" %>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.mentaframework.org/tags-mtw/" prefix="mtw" %>

<c:set var="linkPesquisar"><%=request.getContextPath()%>/estoque/estoque.pesquisarDetalhado.action</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <mtw:inputDateConfig />
  <mtw:inputMaskConfig />
  <mtw:tabPanelConfig skin="win2k"/>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <meta name="header" content="Pesquisar Movimentação Detalhada de Estoque">
  <title>Movimentação Detalhada de Estoque</title>
  <script language="JavaScript" type="text/javascript">  
  function exibir(id) {
      pCtrl.search('<%=request.getContextPath()%>/estoque/estoque.exibir.action?txt_id=' + id, 600, 400, new Array());
  }  
  </script>
</head>
<body>
  <form name="form_pesquisar_movimentacao_detalhada_estoque" method="post" action="${linkPesquisar}">
  <input type="hidden" name="hid_id">
  <center>

  <br>
  <display:table name="listaMovimentoDetalhado" pagesize="50" export="false" id="movimentoDetalhado" requestURI="${linkPesquisar}">
    <display:column property="data" title="Data" style="text-align: center;" headerClass="center"/>
    <display:column title="Produto">
      <a href="javascript:exibir(${movimentoEstoque.data});">${movimentoEstoque.itemVenda.produto.descricao}</a>
      <a href="javascript:exibir(${movimentoEstoque.data});">${movimentoEstoque.itemCompra.produto.descricao}</a>
    </display:column>
    <display:column title="Descrição" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemVenda == null}">
        ${movimentoEstoque.itemCompra.compra.fornecedor.nome}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        ${movimentoEstoque.itemVenda.venda.cliente.nome}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        ${movimentoEstoque.itemVenda.operacaoMovimento.descricao}
      </c:if>
    </display:column>
    <display:column title="Tipo" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        ENTRADA
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        SAÍDA
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        OUTROS
      </c:if>
    </display:column>
    <display:column title="Movimentação" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.isSpecIn == true}">
        IN
      </c:if>
      <c:if test="${movimentoEstoque.isSpecIn == false}">
        OUT
      </c:if>
    </display:column>
    <display:column title="Qtde" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        ${movimentoEstoque.itemCompra.quantidade}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        ${movimentoEstoque.itemVenda.quantidade}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        N/C
      </c:if>
    </display:column>
    <display:column title="Val. Unit." style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        ${movimentoEstoque.itemCompra.valorCompra}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        ${movimentoEstoque.itemVenda.valorVenda}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        N/C
      </c:if>
    </display:column>
    <display:column title="Val. Total" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        ${movimentoEstoque.itemCompra.quantidade}*${movimentoEstoque.itemCompra.valorCompra}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        ${movimentoEstoque.itemVenda.quantidade}*${movimentoEstoque.itemVenda.valorVenda}
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        N/C
      </c:if>
    </display:column>
    <display:column title="Pedido" style="text-align: center;" headerClass="center">
      <c:if test="${movimentoEstoque.itemCompra != null}">
        <a href="javascript:exibir(${movimentoEstoque.itemCompra.Compra.id});"><img src="images/excluir.gif" border="0"/></a>        
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda != null}">
        <a href="javascript:exibir(${movimentoEstoque.itemVenda.Venda.id});"><img src="images/excluir.gif" border="0"/></a>        
      </c:if>
      <c:if test="${movimentoEstoque.itemVenda == null && movimentoEstoque.itemCompra == null}">
        SEM PEDIDO
      </c:if>
    </display:column>    
  </display:table>
  </center>
  </form>
</body>
</html>