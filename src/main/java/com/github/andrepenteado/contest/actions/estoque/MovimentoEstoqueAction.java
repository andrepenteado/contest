
package com.github.andrepenteado.contest.actions.estoque;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.KGlobal.EntradaSaida;
import com.github.andrepenteado.contest.KGlobal.TipoPesquisaProduto;
import com.github.andrepenteado.contest.entity.Fornecedor;
import com.github.andrepenteado.contest.entity.GrupoProduto;
import com.github.andrepenteado.contest.entity.ItemCompra;
import com.github.andrepenteado.contest.entity.ItemVenda;
import com.github.andrepenteado.contest.entity.MovimentoEstoque;
import com.github.andrepenteado.contest.entity.Produto;
import com.github.andrepenteado.contest.entity.valueObject.TipoMovimentacao;
import com.github.andrepenteado.contest.entity.valueObject.VistaPrazo;
import com.github.andrepenteado.contest.filters.EstoqueFilter;
import com.github.andrepenteado.contest.services.CadastroService;
import com.github.andrepenteado.contest.services.EstoqueService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.FunctionsHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Daniel Buso
 * @since 25/01/2009 - 11:12:52
 */
@ActionClass(prefix = "/estoque/movimento")
public class MovimentoEstoqueAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(MovimentoEstoqueAction.class, null);
    private EstoqueService estoqueService = null;
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            estoqueService = (EstoqueService)ServicesFactory.getInstance(EstoqueService.class, new Object[] { userLogin });
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(EstoqueService.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(EstoqueService.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ESTOQUE) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if ("gravar".equals(innerAction)) {
            /*validator.add("txt_data", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Data"));*/
            validator.add("txt_descricao_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Produto"));
            validator.add("txt_quantidade", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Quantidade"));
            validator.add("txt_preco_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Preço"));
            validator.add("cbo_tipo_movimentacao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Tipo Movimentação"));
            validator.add("opt_vista_prazo", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Vista/Prazo"));
            validator.add("opt_tipo_movimento", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Entrada/Saida"));
            validator.add("txt_observacao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Observação"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/movimento/pesquisar.jsp"))
    public String pesquisar() {
        try {
            Date dataInicial = input.getDate("txt_data_inicial", "dd/MM/yyyy");
            Date dataFinal = input.getDate("txt_data_final", "dd/MM/yyyy");
            Long idFornecedor = input.getLong("txt_id_fornecedor");
            String descricao = input.getString("txt_codigo_referencia_descricao");
            long idGrupo = input.getLong("cbo_grupo_produto");

            if (dataInicial == null) {
                dataInicial = new Date();
                output.setValue("txt_data_inicial", FunctionsHelper.dateFormat(dataInicial, "dd/MM/yyyy"));
            }
            if (dataFinal == null) {
                dataFinal = new Date();
                output.setValue("txt_data_final", FunctionsHelper.dateFormat(dataFinal, "dd/MM/yyyy"));
            }

            Collection<MovimentoEstoque> listaMovimentos = new ArrayList<MovimentoEstoque>();
            EstoqueFilter<MovimentoEstoque> filter = ServicesFactory.getInstance(EstoqueFilter.class, MovimentoEstoque.class);

            filter.setDataInicial(dataInicial);
            filter.setDataFinal(dataFinal);

            if (input.getString(TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO.name()) != null
                            && input.getString(TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO.name()).length() > 0) {
                if (descricao == null || "".equals(descricao)) {
                    addError("txt_codigo_referencia_descricao", ConfigHelper.getProperty("error.required", "Produto"));
                    return ERROR;
                }
                filter.setReferencia(descricao);
                listaMovimentos = filter.executeFilter();
                if (listaMovimentos.isEmpty()) {
                    filter = ServicesFactory.getInstance(EstoqueFilter.class, MovimentoEstoque.class);
                    filter.setDataInicial(dataInicial);
                    filter.setDataFinal(dataFinal);
                    filter.setDescricao(descricao);
                    listaMovimentos = filter.executeFilter();
                }
            }
            else if (input.getString(TipoPesquisaProduto.FORNECEDOR.name()) != null
                            && input.getString(TipoPesquisaProduto.FORNECEDOR.name()).length() > 0) {
                filter.setFornecedor(new Fornecedor(idFornecedor));
                listaMovimentos = filter.executeFilter();
            }
            else if (input.getString(TipoPesquisaProduto.GRUPO_PRODUTO.name()) != null
                            && input.getString(TipoPesquisaProduto.GRUPO_PRODUTO.name()).length() > 0) {
                filter.setGrupoProduto(new GrupoProduto(idGrupo));
                listaMovimentos = filter.executeFilter();
            }

            output.setValue("listaMovimento", listaMovimentos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/movimento/pesquisar_detalhado.jsp"))
    public String pesquisarDetalhado() {
        try {
            //long idProduto = input.getLong("txt_id");
            Date dataInicial = input.getDate("txt_dt_Inicial");
            Date dataFinal = input.getDate("txt_dt_Final");

            Collection<MovimentoEstoque> listaMovimentos = new ArrayList<MovimentoEstoque>();
            EstoqueFilter<MovimentoEstoque> filter = ServicesFactory.getInstance(EstoqueFilter.class, MovimentoEstoque.class);

            //filterDescription.setProduto(idProduto);
            filter.setDataInicial((java.sql.Date)dataInicial);
            filter.setDataFinal((java.sql.Date)dataFinal);

            listaMovimentos = filter.executeFilter();

            output.setValue("listaMovimentoDestalhado", listaMovimentos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/movimento/cadastro.jsp"))
    public String cadastro() {
        try {
            output.setValue("movimentoEstoque", new MovimentoEstoque(input.getLong("txt_id")));
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/estoque/movimento/cadastro.jsp") })
    public String gravar() {
        try {
            EntradaSaida tipoMovimento = input.getString("opt_tipo_movimento") != null ? EntradaSaida.valueOf(input.getString("opt_tipo_movimento"))
                            : null;
            Produto produto = cadastroServices.buscarProdutoPorReferencia(input.getString("txt_referencia_produto"));
            int quantidade = input.getInt("txt_quantidade");
            Double preco = new Double(input.getString("txt_preco_produto").replace(".", "").replace(",", "."));

            MovimentoEstoque movimentoEstoque = new MovimentoEstoque(input.getLong("hid_id"));
            movimentoEstoque.setData(new Date()); //input.getDate("txt_data", "dd/MM/yyyy"));
            movimentoEstoque.setTipoMovimentacao(input.getString("cbo_tipo_movimentacao") != null ? TipoMovimentacao.valueOf(input
                            .getString("cbo_tipo_movimentacao")) : null);
            movimentoEstoque.setVistaPrazo(input.getString("opt_vista_prazo") != null ? VistaPrazo.valueOf(input.getString("opt_vista_prazo")) : null);
            movimentoEstoque.setObservacao(input.getString("txt_observacao"));

            if (tipoMovimento.equals(EntradaSaida.ENTRADA)) {
                if (movimentoEstoque.getItemCompra() == null)
                    movimentoEstoque.setItemCompra(new ItemCompra());
                movimentoEstoque.setItemVenda(null);
                movimentoEstoque.getItemCompra().setProduto(produto);
                movimentoEstoque.getItemCompra().setQuantidade(quantidade);
                movimentoEstoque.getItemCompra().setValorCompra(preco);
            }
            else if (tipoMovimento.equals(EntradaSaida.SAIDA)) {
                if (movimentoEstoque.getItemVenda() == null)
                    movimentoEstoque.setItemVenda(new ItemVenda());
                movimentoEstoque.setItemCompra(null);
                movimentoEstoque.getItemVenda().setProduto(produto);
                movimentoEstoque.getItemVenda().setQuantidade(quantidade);
                movimentoEstoque.getItemVenda().setValorVenda(preco);
            }

            estoqueService.gravarMovimentoEstoque(movimentoEstoque);

            output.setValue("titulo", "Lançamento de Estoque");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Estoque"));
            output.setValue("url", input.getProperty("contextPath") + "/estoque/movimento.pesquisar.action");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}
