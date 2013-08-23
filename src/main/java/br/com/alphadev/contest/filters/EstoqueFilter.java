
package br.com.alphadev.contest.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Empresa;
import br.com.alphadev.contest.entity.Estoque;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.GrupoProduto;
import br.com.alphadev.contest.entity.MovimentoEstoque;
import br.com.alphadev.contest.entity.valueObject.TipoMovimentacao;
import br.com.alphadev.core.BasicFilter;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.Log4jWrapper;

@Stateless
public class EstoqueFilter<T> extends BasicFilter {

    private final Log4jWrapper log = new Log4jWrapper(EstoqueFilter.class, null);

    private Class<T> classeGenerica;

    private ProdutoFilter produtoFilter;

    private Date dataInicial;

    private Date dataFinal;

    private TipoMovimentacao operacaoMovimento;

    private Empresa empresa;

    @Override
    public void setParameters(Object[] params) {
        this.classeGenerica = (Class<T>)params[0];
        this.produtoFilter = ServicesFactory.getInstance(ProdutoFilter.class, params);
    }

    @Override
    public void createFilter() {
        produtoFilter.createFilter();
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        parameters.addAll(produtoFilter.getParameters());
        adicionarFrom();
        adicionarProduto();
        adicionarPeriodoInicialFinal();
        adicionarOperacaoMovimento();
        adicionarEmpresa();
        adicionarOrderBy();
    }

    /**
     * Executar a pesquisa genérica conforme a parametrização
     * feita nos atributos da classe
     * 
     * @return Lista de um determinado objeto (Venda ou Oraçamento ou Nota Fiscal)
     */
    @Override
    public Collection<T> executeFilter() {
        createFilter();
        log.info("Executar filtro " + filterDescription.toString());
        return (Collection<T>)executeQuery(filterDescription.toString(), (Object[][])parameters.toArray(new Object[parameters.size()][2]));
    }

    private void adicionarFrom() {
        if (classeGenerica.isInstance(new MovimentoEstoque())) {
            filterDescription.append("SELECT me FROM br.com.alphadev.contest.entity.MovimentoEstoque me ");
            filterDescription.append("LEFT JOIN me.itemVenda iv ");
            filterDescription.append("LEFT JOIN me.itemCompra ic ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new Estoque())) {
            filterDescription.append("SELECT e FROM br.com.alphadev.contest.entity.Estoque e ");
            filterDescription.append("WHERE 1 = 1 ");
        }
    }

    private void adicionarOrderBy() {
        if (classeGenerica.isInstance(new MovimentoEstoque())) {
            filterDescription.append("ORDER BY me.data ");
        }
        else if (classeGenerica.isInstance(new Estoque())) {
            filterDescription.append("ORDER BY e.produto.descricao ");
        }
    }

    private void adicionarProduto() {
        if (classeGenerica.isInstance(new MovimentoEstoque())) {
            filterDescription.append("AND (");
            filterDescription.append("   iv.produto IN (").append(produtoFilter.getFilter()).append(") ");
            filterDescription.append("   OR ");
            filterDescription.append("   ic.produto IN (").append(produtoFilter.getFilter()).append(") ");
            filterDescription.append(") ");
        }
        else if (classeGenerica.isInstance(new Estoque())) {
            filterDescription.append("AND e.produto IN (").append(produtoFilter.getFilter()).append(") ");
        }
    }

    private void adicionarPeriodoInicialFinal() {
        if (classeGenerica.isInstance(new MovimentoEstoque())) {
            if (dataInicial != null) {
                filterDescription.append("AND me.data >= :pDataInicial ");
                parameters.add(new Object[] { "pDataInicial", dataInicial });
            }
            if (dataFinal != null) {
                filterDescription.append("AND me.data <= :pDataFinal ");
                parameters.add(new Object[] { "pDataFinal", dataFinal });
            }
        }
    }

    private void adicionarOperacaoMovimento() {
        //Excessão dos registros feitos por emissão
        if (classeGenerica.isInstance(new MovimentoEstoque())) {
            if (operacaoMovimento != null) {
                if (operacaoMovimento.name().equals(TipoMovimentacao.EMISSAO)) {
                    filterDescription.append("AND me.operacaoMovimento <> :pOperacao ");
                    parameters.add(new Object[] { "pOperacao", TipoMovimentacao.EMISSAO });
                }
            }
        }
    }

    private void adicionarEmpresa() {
        if (empresa != null) {
            if (classeGenerica.isInstance(new MovimentoEstoque())) {
                filterDescription.append("AND (");
                filterDescription.append("   iv.venda.empresa = :pEmpresa ");
                filterDescription.append("   OR ");
                filterDescription.append("   ic.compra.empresa = :pEmpresa ");
                filterDescription.append(") ");
            }
            else if (classeGenerica.isInstance(new Estoque())) {
                filterDescription.append("AND e.empresa = :pEmpresa ");
            }
            parameters.add(new Object[] { "pEmpresa", empresa });
        }
    }

    public void setDataInicial(Date dataInicial) {
        if (dataInicial != null) {
            GregorianCalendar gcInicial = new GregorianCalendar();
            gcInicial.setTime(dataInicial);
            gcInicial.set(Calendar.HOUR_OF_DAY, 0);
            dataInicial = (Date)gcInicial.getTime();
            this.dataInicial = dataInicial;
        }
    }

    public void setDataFinal(Date dataFinal) {
        if (dataFinal != null) {
            GregorianCalendar gcFinal = new GregorianCalendar();
            gcFinal.setTime(dataFinal);
            gcFinal.set(Calendar.HOUR_OF_DAY, 24);
            dataFinal = (Date)gcFinal.getTime();
            this.dataFinal = dataFinal;
        }
    }

    public void setOperacaoMovimento(TipoMovimentacao operacaoMovimento) {
        this.operacaoMovimento = operacaoMovimento;
    }

    /**
     * @param referencia referencia a ser atribuido
     */
    public void setReferencia(String referencia) {
        this.produtoFilter.setReferencia(referencia);
    }

    /**
     * @param descricao descricao a ser atribuido
     */
    public void setDescricao(String descricao) {
        this.produtoFilter.setDescricao(descricao);
    }

    /**
     * @param grupoProduto grupoProduto a ser atribuido
     */
    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.produtoFilter.setGrupoProduto(grupoProduto);
    }

    /**
     * @param fornecedor fornecedor a ser atribuido
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.produtoFilter.setFornecedor(fornecedor);
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}