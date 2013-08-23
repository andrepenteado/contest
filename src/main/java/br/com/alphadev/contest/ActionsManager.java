
package br.com.alphadev.contest;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.renderer.JsonRenderer;
import org.mentawai.authorization.AuthorizationManager;
import org.mentawai.authorization.Group;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.Context;
import org.mentawai.core.Forward;
import org.mentawai.core.Redirect;
import org.mentawai.core.StreamConsequence;
import org.mentawai.list.ListManager;
import org.mentawai.list.SimpleListData;

import br.com.alphadev.contest.KGlobal.EntradaSaida;
import br.com.alphadev.contest.actions.BuscaPadraoAction;
import br.com.alphadev.contest.actions.ComumAction;
import br.com.alphadev.contest.actions.LoginAction;
import br.com.alphadev.contest.actions.PublicAction;
import br.com.alphadev.contest.entity.Categoria;
import br.com.alphadev.contest.entity.Estado;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.GrupoProduto;
import br.com.alphadev.contest.entity.PrazoPagamento;
import br.com.alphadev.contest.entity.TabelaPreco;
import br.com.alphadev.contest.entity.TipoConta;
import br.com.alphadev.contest.entity.valueObject.FormaPagamento;
import br.com.alphadev.contest.entity.valueObject.Frete;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.contest.entity.valueObject.TipoMovimentacao;
import br.com.alphadev.contest.entity.valueObject.TipoProduto;
import br.com.alphadev.contest.entity.valueObject.Unidade;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.web.AbstractActionsManager;

/**
 * @author Andre Penteado
 * @since 11/08/2007 - 16:00:13
 */
public class ActionsManager extends AbstractActionsManager {

    private final Logger log = Logger.getLogger(ActionsManager.class);

    @Override
    public void init(Context application) {
        super.init(application);
        try {
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_SUPERUSUARIO));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_ADMINISTRATIVO));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_VENDAS));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_COMPRAS));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_FINANCEIRO));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_VENDEDOR));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_ESTOQUE));
            AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_RECEPCAO));
        }
        catch (Exception ex) {
            log.error("NÃO FOI POSSÍVEL CARREGAR CATEGORIAS DE ACESSO", ex);
        }
    }

    @Override
    public void loadActions() {
        super.loadActions();

        ActionConfig ac;

        log.debug("Carregando ações de login");
        ac = new ActionConfig("/login", LoginAction.class);
        ac.addConsequence(LoginAction.SUCCESS, new Redirect("/comum.carregarModulos.action"));
        ac.addConsequence(LoginAction.ERROR, new Forward("/comum/login.jsp"));
        ac.addConsequence(LoginAction.SUCCESS, "abrir", new Redirect("/comum.home.action"));
        ac.addConsequence(LoginAction.ERROR, "abrir", new Forward("/comum/login.jsp"));
        addActionConfig(ac);

        log.debug("Carregando ações comuns");
        ac = new ActionConfig("/comum", ComumAction.class);
        ac.addConsequence(ComumAction.SUCCESS, "abrirAlterarSenha", new Forward("/comum/alterar_senha.jsp"));
        ac.addConsequence(ComumAction.SUCCESS, "alterarSenha", new Forward("/comum/mensagem.jsp"));
        ac.addConsequence(ComumAction.ERROR, "alterarSenha", new Forward("/comum/alterar_senha.jsp"));
        ac.addConsequence(ComumAction.SUCCESS, "home", new Forward("/comum/home.jsp"));
        ac.addConsequence(ComumAction.SUCCESS, "carregarModulos", new Redirect("/comum.home.action"));
        ac.addConsequence(ComumAction.SUCCESS, "erro", new Forward(getPaginaErro()));
        ac.addConsequence(ComumAction.SUCCESS, "acessoNegado", new Forward(getPaginaAcessoNegado()));
        ac.addConsequence(ComumAction.SUCCESS, "sair", new Redirect(getPaginaLogin()));
        addActionConfig(ac);

        log.debug("Carregando ações públicas");
        ac = new ActionConfig("/public", PublicAction.class);
        ac.addConsequence(PublicAction.SUCCESS, "catalogoProdutos", new Forward("/cadastros/produto/catalogo.jsp"));
        ac.addConsequence(PublicAction.SUCCESS, "pesquisarEJBQL", new Forward("/comum/pesquisar_ejbql.jsp"));
        ac.addConsequence(PublicAction.SUCCESS, "visualizarFoto", new StreamConsequence("image/jpeg"));
        addActionConfig(ac);

        log.debug("Carregando ações de pesquisas");
        ac = new ActionConfig("/pesquisar", BuscaPadraoAction.class);
        ac.addConsequence(SUCCESS, "funcionario", new AjaxConsequence(new JsonRenderer()));
        ac.addConsequence(SUCCESS, "cidades", new AjaxConsequence(new JsonRenderer()));
        ac.addConsequence(SUCCESS, "cliente", new AjaxConsequence(new JsonRenderer()));
        ac.addConsequence(SUCCESS, "fornecedor", new AjaxConsequence(new JsonRenderer()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.CADASTRO_PEDIDO.getTarget(), "produto",
                        new Forward(BuscaPadraoAction.FORWARD.CADASTRO_PEDIDO.getTarget()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget(), "produto",
                        new Forward(BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.CADASTRO_ESTOQUE.getTarget(), "produto",
                        new Forward(BuscaPadraoAction.FORWARD.CADASTRO_ESTOQUE.getTarget()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.PESQUISAR_COMPRA.getTarget(), "fornecedor", new Forward(
                        BuscaPadraoAction.FORWARD.PESQUISAR_COMPRA.getTarget()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget(), "fornecedor", new Forward(
                        BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget()));
        ac.addConsequence(BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget(), "cfop",
                        new Forward(BuscaPadraoAction.FORWARD.LANCAMENTO_COMPRA.getTarget()));
        addActionConfig(ac);
    }

    @Override
    public void loadLists() throws IOException {
        super.loadLists();

        try {
            log.debug("Carregando lista de formas de pagamento");
            SimpleListData listaFormasPagamentos = new SimpleListData("formaPagamento");
            for (FormaPagamento tc : FormaPagamento.values())
                listaFormasPagamentos.add(tc.name(), tc.getDescricao());
            ListManager.addList(listaFormasPagamentos);
        }
        catch (Exception ex) {
            log.debug("Lista de formas de pagamento não carregadas");
        }

        try {
            log.debug("Carregando lista de tipos de produto");
            SimpleListData listaTiposProduto = new SimpleListData("tipoProduto");
            for (TipoProduto tp : TipoProduto.values())
                listaTiposProduto.add(tp.name(), tp.getDescricao());
            ListManager.addList(listaTiposProduto);
        }
        catch (Exception ex) {
            log.debug("Lista de tipos de produto não carregada");
        }

        try {
            log.debug("Carregando lista de unidades");
            SimpleListData listaUnidade = new SimpleListData("unidade");
            for (Unidade u : Unidade.values())
                listaUnidade.add(u.name(), u.getDescricao());
            ListManager.addList(listaUnidade);
        }
        catch (Exception ex) {
            log.debug("Lista de unidades não carregada");
        }

        try {
            log.debug("Carregando lista de fretes");
            SimpleListData listaFrete = new SimpleListData("frete");
            for (Frete f : Frete.values())
                listaFrete.add(f.name(), f.getDescricao());
            ListManager.addList(listaFrete);
        }
        catch (Exception ex) {
            log.debug("Lista de fretes não carregada");
        }

        try {
            log.debug("Carregando lista de operações de movimento de produto");
            SimpleListData listaTipoMovimentacao = new SimpleListData("tipoMovimentacao");
            for (TipoMovimentacao om : TipoMovimentacao.values())
                listaTipoMovimentacao.add(om.name(), om.getDescricao());
            ListManager.addList(listaTipoMovimentacao);
        }
        catch (Exception ex) {
            log.debug("Lista de operações de movimento de produto não carregada");
        }

        try {
            log.debug("Carregando lista de tipos de compra / venda");
            SimpleListData listaTipoCompraVenda = new SimpleListData("tipoCompraVenda");
            for (TipoCompraVenda tcv : TipoCompraVenda.values())
                listaTipoCompraVenda.add(tcv.name(), tcv.getDescricao());
            ListManager.addList(listaTipoCompraVenda);
        }
        catch (Exception ex) {
            log.debug("Lista de tipos de compra / venda não carregada");
        }

        try {
            log.debug("Carregando lista vista / prazo");
            SimpleListData listaVistaPrazo = new SimpleListData("vistaPrazo");
            for (VistaPrazo to : VistaPrazo.values())
                listaVistaPrazo.add(to.name(), to.getDescricao());
            ListManager.addList(listaVistaPrazo);
        }
        catch (Exception ex) {
            log.debug("Lista de tipos de operações de movimento de produto não carregada");
        }

        try {
            log.debug("Carregando lista de tipos de movimento de produto");
            SimpleListData listaTipoMovimento = new SimpleListData("tipoMovimento");
            for (EntradaSaida tm : EntradaSaida.values())
                listaTipoMovimento.add(tm.name(), tm.getDescricao());
            ListManager.addList(listaTipoMovimento);
        }
        catch (Exception ex) {
            log.debug("Lista de tipos de movimento de produto não carregada");
        }

        try {
            log.debug("Carregando lista de tipos de conta");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaTipoConta = new SimpleListData("tipoConta");
            for (TipoConta categoria : cs.pesquisarTiposContaPorDescricao(""))
                listaTipoConta.add(categoria.getId().intValue(), categoria.getDescricao());
            ListManager.addList(listaTipoConta);
        }
        catch (Exception ex) {
            log.debug("Lista de tipos de conta não carregada");
        }

        try {
            log.debug("Carregando lista de categorias de acesso");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaCategoria = new SimpleListData("categorias");
            for (Categoria categoria : cs.listarCategorias())
                listaCategoria.add(categoria.getId().intValue(), categoria.getDescricao());
            ListManager.addList(listaCategoria);
        }
        catch (Exception ex) {
            log.debug("Lista de categorias de acesso não carregada");
        }

        try {
            log.debug("Carregando lista de estados");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaEstados = new SimpleListData("estados");
            for (Estado estado : cs.listarEstados())
                listaEstados.add(estado.getId().intValue(), estado.getNome());
            ListManager.addList(listaEstados);
        }
        catch (Exception ex) {
            log.debug("Lista de estados não carregada");
        }

        try {
            log.debug("Carregando lista de anos de venda");
            VendaService vs = (VendaService)ServicesFactory.getInstance(VendaService.class, null);
            SimpleListData listaAno = new SimpleListData("anoVenda");
            int anoInicio = vs.buscarAnoInicioVendas();
            for (int ano = GregorianCalendar.getInstance().get(Calendar.YEAR); ano >= anoInicio; ano--)
                listaAno.add(ano, Integer.toString(ano));
            ListManager.addList(listaAno);
        }
        catch (Exception ex) {
            log.debug("Lista de anos de venda não carregada");
        }

        try {
            log.debug("Carregando lista de grupos de produtos");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaGruposProdutos = new SimpleListData("listaGruposProdutos");
            for (GrupoProduto grupoProduto : cs.listarGruposProdutos())
                listaGruposProdutos.add(grupoProduto.getId().intValue(), grupoProduto.getDescricao());
            ListManager.addList(listaGruposProdutos);
        }
        catch (Exception ex) {
            log.debug("Lista de grupos de produtos não carregada");
        }

        try {
            log.debug("Carregando lista de tabelas de preços");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaTabelaPrecos = new SimpleListData("listaTabelasPrecos");
            for (TabelaPreco tabelaPreco : cs.listarTabelaPrecos())
                listaTabelaPrecos.add(tabelaPreco.getId().intValue(), tabelaPreco.getDescricao());
            ListManager.addList(listaTabelaPrecos);
        }
        catch (Exception ex) {
            log.debug("Lista de tabelas de preços não carregada");
        }

        try {
            log.debug("Carregando lista de vendedores");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaVendedores = new SimpleListData("listaVendedores");
            for (Funcionario vendedor : cs.pesquisarFuncionarioPorNome(""))
                listaVendedores.add(vendedor.getId().intValue(), vendedor.getNome());
            ListManager.addList(listaVendedores);
        }
        catch (Exception ex) {
            log.debug("Lista de vendedores não carregada");
        }

        try {
            log.debug("Carregando lista de prazos de pagamentos");
            CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            SimpleListData listaPrazosPagamentos = new SimpleListData("listaPrazosPagamentos");
            for (PrazoPagamento prazoPagamento : cs.listarPrazosPagamentos())
                listaPrazosPagamentos.add(prazoPagamento.getId().intValue(), prazoPagamento.getDescricao());
            ListManager.addList(listaPrazosPagamentos);
        }
        catch (Exception ex) {
            log.debug("Lista de prazos de pagamentos não carregada");
        }
    }

    @Override
    public String getPaginaAcessoNegado() {
        return "/comum/acesso_negado.jsp";
    }

    @Override
    public String getPaginaErro() {
        return "/comum/erro.jsp";
    }

    @Override
    public String getPaginaLogin() {
        return "/login.abrir.action";
    }
}
