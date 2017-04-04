
package com.github.andrepenteado.contest.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.KGlobal.TipoPesquisaCliente;
import com.github.andrepenteado.contest.KGlobal.TipoPesquisaFornecedor;
import com.github.andrepenteado.contest.KGlobal.TipoPesquisaProduto;
import com.github.andrepenteado.contest.entity.CFOP;
import com.github.andrepenteado.contest.entity.Categoria;
import com.github.andrepenteado.contest.entity.Cidade;
import com.github.andrepenteado.contest.entity.Cliente;
import com.github.andrepenteado.contest.entity.CodigoBarras;
import com.github.andrepenteado.contest.entity.Empresa;
import com.github.andrepenteado.contest.entity.Estado;
import com.github.andrepenteado.contest.entity.Fornecedor;
import com.github.andrepenteado.contest.entity.Funcionario;
import com.github.andrepenteado.contest.entity.GrupoProduto;
import com.github.andrepenteado.contest.entity.PrazoPagamento;
import com.github.andrepenteado.contest.entity.Produto;
import com.github.andrepenteado.contest.entity.TabelaPreco;
import com.github.andrepenteado.contest.entity.TipoConta;
import com.github.andrepenteado.contest.filters.ClienteFilter;
import com.github.andrepenteado.contest.filters.FornecedorFilter;
import com.github.andrepenteado.contest.filters.ProdutoFilter;
import com.github.andrepenteado.core.BasicService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.FunctionsHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 17:33:25
 */
@Stateless
public class CadastroService extends BasicService {

    /******************* CLIENTE **********************/

    public Collection<Cliente> pesquisarCliente(String codigoNome, String telefoneEmail, String ruaCep, Cidade cidade, Funcionario vendedor,
                    TipoPesquisaCliente tipoPesquisaCliente) {
        ClienteFilter filter = ServicesFactory.getInstance(ClienteFilter.class, userLogin);

        if (tipoPesquisaCliente.equals(TipoPesquisaCliente.CODIGO_NOME)) {
            // Por Código (ID)
            try {
                Cliente cliente = new Cliente(Long.parseLong(codigoNome));
                if (cliente.getId() != null
                                && !(userLogin.getCategoriaAtual().getNome().equals(KGlobal.CATEGORIA_VENDEDOR) && cliente.getFuncionario().getId()
                                                .intValue() != userLogin.getFuncionario().getId().intValue())) {
                    ArrayList<Cliente> result = new ArrayList<Cliente>();
                    result.add(cliente);
                    return result;
                }
            }
            catch (Exception ex) {
            }
            // Por Nome
            filter.setNome(codigoNome);
        }
        else if (tipoPesquisaCliente.equals(TipoPesquisaCliente.TELEFONE_EMAIL)) {
            // Por Telefone
            filter.setTelefone(telefoneEmail);
            Collection<Cliente> result = filter.executeFilter();
            if (result != null && !result.isEmpty())
                return result;
            // Por Email
            filter = new ClienteFilter();
            filter.setEmail(telefoneEmail);
        }
        else if (tipoPesquisaCliente.equals(TipoPesquisaCliente.RUA_CEP)) {
            // Por Rua
            filter.setRua(ruaCep);
            Collection<Cliente> result = filter.executeFilter();
            if (result != null && !result.isEmpty())
                return result;
            // Por Cep
            filter = new ClienteFilter();
            filter.setCep(ruaCep);
        }
        else if (tipoPesquisaCliente.equals(TipoPesquisaCliente.CIDADE)) {
            // Por Cidade
            filter.setCidade(cidade);
        }
        else if (tipoPesquisaCliente.equals(TipoPesquisaCliente.VENDEDOR)) {
            // Por Vendedor
            filter.setVendedor(vendedor);
        }

        // Caso seja vendedor, filtrar apenas de sua carteira de clientes
        if (userLogin != null && userLogin.getCategoriaAtual().getNome().equals(KGlobal.CATEGORIA_VENDEDOR))
            filter.setVendedor(userLogin.getFuncionario());

        return filter.executeFilter();
    }

    /******************* FORNECEDOR **********************/

    public Collection<Fornecedor> pesquisarFornecedor(String codigoNome, String telefoneEmail, String ruaCep, Cidade cidade, String cnpj,
                    TipoPesquisaFornecedor tipoPesquisaFornecedor) {
        FornecedorFilter filter = null;
        if (tipoPesquisaFornecedor.equals(TipoPesquisaFornecedor.CODIGO_NOME)) {
            // Por Código (ID)
            try {
                Fornecedor fornecedor = new Fornecedor(Long.parseLong(codigoNome));
                if (fornecedor.getId() != null) {
                    ArrayList<Fornecedor> result = new ArrayList<Fornecedor>();
                    result.add(fornecedor);
                    return result;
                }
            }
            catch (Exception ex) {
            }
            // Por Nome
            filter = new FornecedorFilter();
            filter.setNome(codigoNome);
            return filter.executeFilter();
        }
        else if (tipoPesquisaFornecedor.equals(TipoPesquisaFornecedor.TELEFONE_EMAIL)) {
            // Por Telefone
            filter = new FornecedorFilter();
            filter.setTelefone(telefoneEmail);
            Collection<Fornecedor> result = filter.executeFilter();
            if (result != null && !result.isEmpty())
                return result;
            // Por Email
            filter = new FornecedorFilter();
            filter.setEmail(telefoneEmail);
            return filter.executeFilter();
        }
        else if (tipoPesquisaFornecedor.equals(TipoPesquisaFornecedor.RUA_CEP)) {
            // Por Rua
            filter = new FornecedorFilter();
            filter.setRua(ruaCep);
            Collection<Fornecedor> result = filter.executeFilter();
            if (result != null && !result.isEmpty())
                return result;
            // Por Cep
            filter = new FornecedorFilter();
            filter.setCep(ruaCep);
            return filter.executeFilter();
        }
        else if (tipoPesquisaFornecedor.equals(TipoPesquisaFornecedor.CIDADE)) {
            // Por Cidade
            filter = new FornecedorFilter();
            filter.setCidade(cidade);
            return filter.executeFilter();
        }
        else if (tipoPesquisaFornecedor.equals(TipoPesquisaFornecedor.CNPJ)) {
            filter = new FornecedorFilter();
            filter.setCnpj(cnpj);
            return filter.executeFilter();
        }

        return null;
    }

    /******************* GRUPO DE PRODUTO **********************/

    public List<GrupoProduto> listarGruposProdutos() {
        log.info("Listar grupos de produtos");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT gp ");
        hql.append("FROM com.github.andrepenteado.contest.entity.GrupoProduto gp ");
        hql.append("ORDER BY gp.descricao ");

        return (List<GrupoProduto>)executeQuery(hql.toString(), null);
    }

    /******************* TABELA DE PREÇOS **********************/

    public List<TabelaPreco> listarTabelaPrecos() {
        log.info("Lista tabelas de preços");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT tp ");
        hql.append("FROM com.github.andrepenteado.contest.entity.TabelaPreco tp ");
        hql.append("ORDER BY tp.descricao ");

        return (List<TabelaPreco>)executeQuery(hql.toString(), null);
    }

    /******************* CFOP **********************/

    public List<CFOP> listarCFOP() {
        log.info("Lista CFOP");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c ");
        hql.append("FROM com.github.andrepenteado.contest.entity.CFOP c ");
        hql.append("ORDER BY c.codigo ");

        return (List<CFOP>)executeQuery(hql.toString(), null);
    }

    public CFOP buscarCFOPPorCodigo(String codigo) {
        log.info("buscar CFOP por Código");

        CFOP cfop = (CFOP)findByField(CFOP.class, "codigo", codigo);

        return cfop;
    }

    public List<CFOP> filtrarCFOPPorCodigoDescricao(String codigoDescricao) {
        log.info("Lista CFOP");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c ");
        hql.append("FROM com.github.andrepenteado.contest.entity.CFOP c ");
        hql.append("WHERE c.codigo LIKE :pCodigoDescricao OR UPPER(c.descricao) LIKE UPPER(:pCodigoDescricao) ");
        hql.append("ORDER BY c.codigo ");

        Object[][] params = new Object[][] { { "pCodigoDescricao", "%".concat(codigoDescricao).concat("%") } };

        return (List<CFOP>)executeQuery(hql.toString(), params);
    }

    /******************* TIPO DE CONTA **********************/

    public List<TipoConta> pesquisarTiposContaPorDescricao(String descricao) {
        descricao = FunctionsHelper.toStringNotNull(descricao);
        log.info("Pesquisar tipos de conta por descrição: ".concat(descricao));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT tc ");
        hql.append("FROM com.github.andrepenteado.contest.entity.TipoConta tc ");
        hql.append("WHERE UPPER(tc.descricao) LIKE UPPER(:pDescricao) ");

        Object[][] params = new Object[][] { { "pDescricao", "%".concat(descricao).concat("%") } };

        return (List<TipoConta>)executeQuery(hql.toString(), params);
    }

    /******************* FUNCIONÁRIO **********************/

    public List<Funcionario> pesquisarFuncionarioPorNome(String nome) {
        nome = FunctionsHelper.toStringNotNull(nome);
        log.info("Pesquisar funcionário por nome: ".concat(nome));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT v ");
        hql.append("FROM com.github.andrepenteado.contest.entity.Funcionario v ");
        hql.append("WHERE UPPER(v.nome) LIKE UPPER(:pNome) ");

        Object[][] params = new Object[][] { { "pNome", "%".concat(nome).concat("%") } };

        return (List<Funcionario>)executeQuery(hql.toString(), params);
    }

    /******************* PRAZO DE PAGAMENTO **********************/

    public List<PrazoPagamento> listarPrazosPagamentos() {
        log.info("Listar prazos de pagamentos");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT fp ");
        hql.append("FROM com.github.andrepenteado.contest.entity.PrazoPagamento fp ");
        hql.append("ORDER BY fp.descricao");

        return (List<PrazoPagamento>)executeQuery(hql.toString(), null);
    }

    public PrazoPagamento buscarPrazoPagamentoPorParcelas(String parcelas) {
        log.info("Listar prazos de pagamentos");
        PrazoPagamento prazoPagamento = (PrazoPagamento)findByField(PrazoPagamento.class, "parcelas", parcelas);
        return prazoPagamento;
    }

    /******************* PRODUTO **********************/

    public Produto buscarProdutoPorReferencia(String referencia) {
        referencia = FunctionsHelper.toStringNotNull(referencia);
        log.info("Buscar produto por referência: ".concat(referencia));
        Produto produto = (Produto)findByField(Produto.class, "UPPER(referencia)", referencia.toUpperCase());
        if (produto != null) {
            if (produto.getEstoques() != null)
                produto.getEstoques().size();
            if (produto.getGrupoProduto() == null)
                return null;
        }
        return produto;
    }

    public Produto buscarProdutoPorCodigoExterno(String codigoExterno) {
        codigoExterno = FunctionsHelper.toStringNotNull(codigoExterno);
        log.info("Buscar produto por código externo: ".concat(codigoExterno));
        Produto produto = (Produto)findByField(Produto.class, "UPPER(codigoExterno)", codigoExterno.toUpperCase());
        if (produto != null) {
            if (produto.getEstoques() != null)
                produto.getEstoques().size();
            if (produto.getTabelaPreco() == null)
                return null;
        }
        return produto;
    }

    public Produto buscarProdutoPorCodigoBarras(String numeroCodigoBarras) {
        numeroCodigoBarras = FunctionsHelper.toStringNotNull(numeroCodigoBarras);
        log.info("Buscar produto por código de barras: " + numeroCodigoBarras);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT cb.produto ");
        hql.append("FROM com.github.andrepenteado.contest.entity.CodigoBarras cb ");
        hql.append("WHERE cb.codigoBarras = :pCodigoBarras");

        Object[][] params = new Object[][] { { "pCodigoBarras", numeroCodigoBarras } };
        List<Produto> lista = (List<Produto>)executeQuery(hql.toString(), params);
        if (lista != null && lista.size() > 0)
            return lista.get(0);
        return null;
    }

    public Collection<Produto> pesquisarProduto(String codigoReferenciaDescricao, GrupoProduto grupoProduto, Fornecedor fornecedor,
                    TipoPesquisaProduto tipoPesquisaProduto, boolean ativo) {
        ProdutoFilter filter = null;
        if (tipoPesquisaProduto.equals(TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO)) {
            Set<Produto> result = new HashSet<Produto>();
            // Por Código (ID)
            try {
                Produto produto = new Produto(Long.parseLong(codigoReferenciaDescricao));
                if (produto.getId() != null && (!ativo || (ativo && produto.getGrupoProduto() != null)))
                    result.add(produto);
            }
            catch (Exception ex) {
            }
            // Por Referência
            filter = new ProdutoFilter();
            filter.setAtivo(ativo);
            filter.setReferencia(codigoReferenciaDescricao);
            Collection<Produto> resultReferencia = filter.executeFilter();
            if (resultReferencia != null && !resultReferencia.isEmpty())
                result.addAll(resultReferencia);
            // Por Descrição
            filter = new ProdutoFilter();
            filter.setAtivo(ativo);
            filter.setDescricao(codigoReferenciaDescricao);
            Collection<Produto> resultDescricao = filter.executeFilter();
            if (resultDescricao != null && !resultDescricao.isEmpty())
                result.addAll(resultDescricao);
            return result;
        }
        else if (tipoPesquisaProduto.equals(TipoPesquisaProduto.GRUPO_PRODUTO)) {
            // Por Grupo de Produto
            filter = new ProdutoFilter();
            filter.setAtivo(ativo);
            filter.setGrupoProduto(grupoProduto);
            return filter.executeFilter();
        }
        else if (tipoPesquisaProduto.equals(TipoPesquisaProduto.FORNECEDOR)) {
            // Por Fornecedor
            filter = new ProdutoFilter();
            filter.setAtivo(ativo);
            filter.setFornecedor(fornecedor);
            return filter.executeFilter();
        }
        return null;
    }

    public List<Produto> pesquisarProdutosComFoto() {
        log.info("Pesquisar produtos com foto");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT p ");
        hql.append("FROM com.github.andrepenteado.contest.entity.Produto p ");
        hql.append("WHERE p.foto IS NOT NULL ");
        hql.append("ORDER BY p.descricao");

        return (List<Produto>)executeQuery(hql.toString(), null);
    }

    /******************* CÓDIGO BARRAS **********************/

    public CodigoBarras buscarCodigoBarrasPorNumero(String numeroCodigoBarras) {
        numeroCodigoBarras = FunctionsHelper.toStringNotNull(numeroCodigoBarras);
        log.info("Buscar código de barras por número: ".concat(numeroCodigoBarras));
        return (CodigoBarras)findByField(CodigoBarras.class, "codigoBarras", numeroCodigoBarras);
    }

    public List<CodigoBarras> pesquisarCodigoBarrasPorProduto(long idProduto) {
        log.info("Pesquisar código de barras por produto id: " + idProduto);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT e ");
        hql.append("FROM com.github.andrepenteado.contest.entity.CodigoBarras e ");
        hql.append("WHERE e.produto.id = :pIdProduto ");
        hql.append("ORDER BY e.codigoBarras");

        Object[][] params = new Object[][] { { "pIdProduto", idProduto } };
        return (List<CodigoBarras>)executeQuery(hql.toString(), params);
    }

    /******************* EMPRESA **********************/

    public List<Empresa> listarEmpresas() {
        log.info("Listar empresas");
        return (List<Empresa>)executeQuery("SELECT e FROM com.github.andrepenteado.contest.entity.Empresa e ORDER BY e.razaoSocial", null);
    }

    public Empresa buscarEmpresaPorPaginaInternet(String paginaInternet) {
        log.info("Buscar empresa pela URL: ".concat(paginaInternet));
        return (Empresa)executeFind("SELECT e FROM com.github.andrepenteado.contest.entity.Empresa e WHERE e.paginaInternet = :pURL", new Object[][] { {
                        "pURL", paginaInternet } });
    }

    /******************* CATEGORIA DE ACESSO **********************/

    public List<Categoria> listarCategorias() {
        log.info("Listar categorias de acesso");
        return (List<Categoria>)executeQuery("SELECT c FROM com.github.andrepenteado.contest.entity.Categoria c ORDER BY c.descricao", null);
    }

    /******************* CIDADES **********************/

    public List<Cidade> pesquisarCidadesPorEstado(Estado estado) {
        log.info("Pesquisar cidades pelo estado ".concat(estado.toString()));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c FROM com.github.andrepenteado.contest.entity.Cidade c ");
        hql.append("WHERE c.estado.id = :pIdEstado ");
        hql.append("ORDER BY c.nome");

        Object[][] params = new Object[][] { { "pIdEstado", estado.getId() } };

        return (List<Cidade>)executeQuery(hql.toString(), params);
    }

    public List<Estado> listarEstados() {
        log.info("Listar estados");
        return (List<Estado>)executeQuery("SELECT e FROM com.github.andrepenteado.contest.entity.Estado e ORDER BY e.sigla", null);
    }

}