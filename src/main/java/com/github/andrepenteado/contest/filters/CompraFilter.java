
package com.github.andrepenteado.contest.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import com.github.andrepenteado.contest.entity.Compra;
import com.github.andrepenteado.contest.entity.Pagar;
import com.github.andrepenteado.contest.entity.Pago;
import com.github.andrepenteado.core.BasicFilter;
import com.github.andrepenteado.util.Log4jWrapper;

@Stateless
public class CompraFilter<T> extends BasicFilter {

    private Log4jWrapper log = new Log4jWrapper(CompraFilter.class, null);

    private Class<T> classeGenerica;

    private long idFornecedor;

    private Date dataEmissaoInicial;

    private Date dataEmissaoFinal;

    private Date dataVencimentoInicial;

    private Date dataVencimentoFinal;

    private Integer[] idsTiposConta;

    private long numeroCompra;

    @Override
    public void setParameters(Object[] params) {
        this.classeGenerica = (Class<T>)params[0];
    }

    @Override
    public void createFilter() {
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        adicionarFrom();
        adicionarFornecedor();
        adicionarEmissao();
        adicionarVencimento();
        adicionarTipoConta();
        adicionarNumeroCompra();
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

    /**
     * Parte FROM da consulta
     */
    private void adicionarFrom() {
        if (classeGenerica.isInstance(new Compra())) {
            filterDescription.append("SELECT c FROM com.github.andrepenteado.contest.entity.Compra c ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new Pagar())) {
            filterDescription.append("SELECT p FROM com.github.andrepenteado.contest.entity.Pagar p ");
            filterDescription.append("WHERE (p.pago IS NULL OR p.pago = FALSE) ");  // Campo automático, atualizado por trigger
            /*filterDescription.append("WHERE p.id IN ( "); // Ter pago parte (deve outra parte)
            filterDescription.append("   SELECT pg1.pagar.id ");
            filterDescription.append("   FROM com.github.andrepenteado.contest.entity.Pago pg1 ");
            filterDescription.append("   GROUP BY pg1.pagar.id, pg1.pagar.valor ");
            filterDescription.append("   HAVING SUM(pg1.valorPago) < pg1.pagar.valor ");
            filterDescription.append(") ");
            filterDescription.append("OR p.id IN ( "); // Não ter pago nada
            filterDescription.append("   SELECT p2.id ");
            filterDescription.append("   FROM com.github.andrepenteado.contest.entity.Pagar p2 ");
            filterDescription.append("   WHERE NOT EXISTS ELEMENTS(p2.pagamentos) ");
            filterDescription.append(") ");*/
        }
        else if (classeGenerica.isInstance(new Pago())) {
            filterDescription.append("SELECT g FROM com.github.andrepenteado.contest.entity.Pago g ");
            filterDescription.append("WHERE 1 = 1 ");
        }
    }

    /**
     * Parte do filtro por fornecedor da consulta
     */
    private void adicionarFornecedor() {
        if (idFornecedor > 0) {
            if (classeGenerica.isInstance(new Compra())) {
                filterDescription.append("AND c.fornecedor.id = :pIdFornecedor ");
                parameters.add(new Object[] { "pIdFornecedor", idFornecedor });
            }
            else if (classeGenerica.isInstance(new Pagar())) {
                filterDescription.append("AND p.compra.fornecedor.id = :pIdFornecedor ");
                parameters.add(new Object[] { "pIdFornecedor", idFornecedor });
            }
            else if (classeGenerica.isInstance(new Pago())) {
                filterDescription.append("AND g.pagar.compra.fornecedor.id = :pIdFornecedor ");
                parameters.add(new Object[] { "pIdFornecedor", idFornecedor });
            }
        }
    }

    /**
     * Parte do filtro por emissão da consulta
     */
    private void adicionarEmissao() {
        if (classeGenerica.isInstance(new Compra())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND c.emissao >= :pEmissaoInicial ");
                parameters.add(new Object[] { "pEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND c.emissao <= :pEmissaoFinal ");
                parameters.add(new Object[] { "pEmissaoFinal", dataEmissaoFinal });
            }
        }
        if (classeGenerica.isInstance(new Pagar())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND p.compra.emissao >= :pEmissaoInicial ");
                parameters.add(new Object[] { "pEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND p.compra.emissao <= :pEmissaoFinal ");
                parameters.add(new Object[] { "pEmissaoFinal", dataEmissaoFinal });
            }
        }
        if (classeGenerica.isInstance(new Pago())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND g.pagar.compra.emissao >= :pEmissaoInicial ");
                parameters.add(new Object[] { "pEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND g.pagar.compra.emissao <= :pEmissaoFinal ");
                parameters.add(new Object[] { "pEmissaoFinal", dataEmissaoFinal });
            }
        }
    }

    /**
     * Parte do filtro por vencimento da consulta
     */
    private void adicionarVencimento() {
        if (classeGenerica.isInstance(new Compra())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND c.id IN ( ");
                filterDescription.append("   SELECT p.compra.id FROM com.github.andrepenteado.contest.entity.Pagar p ");
                filterDescription.append("   WHERE p.vencimento >= :pDataVencimentoInicial ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND c.id IN ( ");
                filterDescription.append("   SELECT p.compra.id FROM com.github.andrepenteado.contest.entity.Pagar p ");
                filterDescription.append("   WHERE p.vencimento <= :pDataVencimentoFinal ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Pagar())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND p.vencimento >= :pDataVencimentoInicial ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND p.vencimento <= :pDataVencimentoFinal ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Pago())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND g.pagar.vencimento >= :pDataVencimentoInicial ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND g.pagar.vencimento <= :pDataVencimentoFinal ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
    }

    /**
     * Parte do filtro por tipos de conta da consulta
     */
    private void adicionarTipoConta() {
        if (idsTiposConta != null && idsTiposConta.length > 0) {
            String ids = StringUtils.join(idsTiposConta, ",");
            if (classeGenerica.isInstance(new Pagar())) {
                filterDescription.append("AND p.tipoConta.id IN (").append(ids).append(") ");
            }
            else if (classeGenerica.isInstance(new Pago())) {
                filterDescription.append("AND g.pagar.tipoConta.id IN (").append(ids).append(") ");
            }
        }
    }

    /**
     * Parte do filtro por numero da consulta
     */
    private void adicionarNumeroCompra() {
        if (numeroCompra > 0) {
            if (classeGenerica.isInstance(new Compra())) {
                filterDescription.append("AND c.notaFiscal = :pNumeroCompra ");
                parameters.add(new Object[] { "pNumeroCompra", numeroCompra });
            }
            else if (classeGenerica.isInstance(new Pagar())) {
                filterDescription.append("AND p.compra.notaFiscal = :pNumeroCompra ");
                parameters.add(new Object[] { "pNumeroCompra", numeroCompra });
            }
            else if (classeGenerica.isInstance(new Pago())) {
                filterDescription.append("AND g.pagar.compra.notaFiscal = :pNumeroCompra ");
                parameters.add(new Object[] { "pNumeroCompra", numeroCompra });
            }
        }
    }

    /**
     * Incluir a ordenação da pesquisa
     */
    private void adicionarOrderBy() {
        if (classeGenerica.isInstance(new Compra())) {
            filterDescription.append("ORDER BY c.emissao DESC");
        }
        else if (classeGenerica.isInstance(new Pagar())) {
            filterDescription.append("ORDER BY p.vencimento DESC");
        }
        else if (classeGenerica.isInstance(new Pago())) {
            filterDescription.append("ORDER BY g.dataPagamento");
        }
    }

    /**
     * @param idFornecedor idFornecedor a ser atribuido
     */
    public void setIdFornecedor(long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    /**
     * @param dataEmissaoInicial dataEmissaoInicial a ser atribuido
     */
    public void setDataEmissaoInicial(Date dataEmissaoInicial) {
        if (dataEmissaoInicial != null) {
            GregorianCalendar gcInicial = new GregorianCalendar();
            gcInicial.setTime(dataEmissaoInicial);
            gcInicial.set(Calendar.HOUR_OF_DAY, 0);
            dataEmissaoInicial = gcInicial.getTime();
            this.dataEmissaoInicial = dataEmissaoInicial;
        }
    }

    /**
     * @param dataEmissaoFinal dataEmissaoFinal a ser atribuido
     */
    public void setDataEmissaoFinal(Date dataEmissaoFinal) {
        if (dataEmissaoFinal != null) {
            GregorianCalendar gcFinal = new GregorianCalendar();
            gcFinal.setTime(dataEmissaoFinal);
            gcFinal.set(Calendar.HOUR_OF_DAY, 24);
            dataEmissaoFinal = gcFinal.getTime();
            this.dataEmissaoFinal = dataEmissaoFinal;
        }
    }

    /**
     * @param dataVencimentoInicial dataVencimentoInicial a ser atribuido
     */
    public void setDataVencimentoInicial(Date dataVencimentoInicial) {
        if (dataVencimentoInicial != null) {
            GregorianCalendar gcInicial = new GregorianCalendar();
            gcInicial.setTime(dataVencimentoInicial);
            gcInicial.set(Calendar.HOUR_OF_DAY, 0);
            dataVencimentoInicial = gcInicial.getTime();
            this.dataVencimentoInicial = dataVencimentoInicial;
        }
    }

    /**
     * @param dataVencimentoFinal dataVencimentoFinal a ser atribuido
     */
    public void setDataVencimentoFinal(Date dataVencimentoFinal) {
        if (dataVencimentoFinal != null) {
            GregorianCalendar gcFinal = new GregorianCalendar();
            gcFinal.setTime(dataVencimentoFinal);
            gcFinal.set(Calendar.HOUR_OF_DAY, 24);
            dataVencimentoFinal = gcFinal.getTime();
            this.dataVencimentoFinal = dataVencimentoFinal;
        }
    }

    /**
     * @param idsTiposConta idsTiposConta a ser atribuido
     */
    public void setIdsTiposConta(int[] idsTiposConta) {
        this.idsTiposConta = new Integer[idsTiposConta.length];
        for (int i = 0; i < idsTiposConta.length; i++) {
            int id = idsTiposConta[i];
            this.idsTiposConta[i] = new Integer(id);
        }
    }

    /**
     * @param numeroCompra numeroCompra a ser atribuido
     */
    public void setNumeroCompra(long numeroCompra) {
        this.numeroCompra = numeroCompra;
    }
}
