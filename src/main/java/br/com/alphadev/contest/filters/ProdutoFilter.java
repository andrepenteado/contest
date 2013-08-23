
package br.com.alphadev.contest.filters;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.GrupoProduto;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.core.BasicFilter;
import br.com.alphadev.util.Log4jWrapper;

@Stateless
public class ProdutoFilter extends BasicFilter {

    private Log4jWrapper log = new Log4jWrapper(ProdutoFilter.class, null);

    private String referencia;

    private String descricao;

    private GrupoProduto grupoProduto;

    private Fornecedor fornecedor;

    private boolean ativo;

    @Override
    public void createFilter() {
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        filterDescription.append("SELECT p FROM br.com.alphadev.contest.entity.Produto p ");
        if (isAtivo())
            filterDescription.append("WHERE p.grupoProduto IS NOT NULL ");
        else
            filterDescription.append("WHERE 1 = 1 ");
        adicionarReferencia();
        adicionarDescricao();
        adicionarGrupoProduto();
        adicionarFornecedor();
        filterDescription.append("ORDER BY p.descricao");
    }

    @Override
    public Collection<Produto> executeFilter() {
        createFilter();
        log.info("Executar filtro " + filterDescription.toString());
        return (Collection<Produto>)executeQuery(filterDescription.toString(), (Object[][])parameters.toArray(new Object[parameters.size()][2]));
    }

    private void adicionarReferencia() {
        if (referencia != null) {
            filterDescription.append("AND UPPER(p.referencia) LIKE UPPER(:pReferencia) ");
            parameters.add(new Object[] { "pReferencia", "%".concat(referencia).concat("%") });
        }
    }

    private void adicionarDescricao() {
        if (descricao != null) {
            filterDescription.append("AND UPPER(p.descricao) LIKE UPPER(:pDescricao) ");
            parameters.add(new Object[] { "pDescricao", "%".concat(descricao).concat("%") });
        }
    }

    private void adicionarGrupoProduto() {
        if (grupoProduto != null) {
            filterDescription.append("AND p.grupoProduto = :pGrupoProduto ");
            parameters.add(new Object[] { "pGrupoProduto", grupoProduto });
        }
    }

    private void adicionarFornecedor() {
        if (fornecedor != null) {
            filterDescription.append("AND p IN ( ");
            filterDescription.append("   SELECT ic.produto FROM br.com.alphadev.contest.entity.ItemCompra ic ");
            filterDescription.append("   WHERE ic.compra.fornecedor = :pFornecedor ");
            filterDescription.append(") ");
            parameters.add(new Object[] { "pFornecedor", fornecedor });
        }
    }

    /**
     * @param referencia referencia a ser atribuido
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @param descricao descricao a ser atribuido
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @param grupoProduto grupoProduto a ser atribuido
     */
    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    /**
     * @param fornecedor fornecedor a ser atribuido
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @param ativo ativo a ser atribuido
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * @return ativo atribuido
     */
    public boolean isAtivo() {
        return ativo;
    }
}
