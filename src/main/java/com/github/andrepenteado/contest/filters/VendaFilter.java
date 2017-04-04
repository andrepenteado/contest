
package com.github.andrepenteado.contest.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import com.github.andrepenteado.contest.entity.Comissao;
import com.github.andrepenteado.contest.entity.NotaFiscal;
import com.github.andrepenteado.contest.entity.Orcamento;
import com.github.andrepenteado.contest.entity.Receber;
import com.github.andrepenteado.contest.entity.Recebido;
import com.github.andrepenteado.contest.entity.Venda;
import com.github.andrepenteado.core.BasicFilter;
import com.github.andrepenteado.util.Log4jWrapper;

@Stateless
public class VendaFilter<T> extends BasicFilter {

    private Log4jWrapper log = new Log4jWrapper(VendaFilter.class, null);

    private Class<T> classeGenerica;

    private long idCliente;

    private Date dataEmissaoInicial;

    private Date dataEmissaoFinal;

    private Date dataVencimentoInicial;

    private Date dataVencimentoFinal;

    private long idVendedor;

    private Integer[] idsTiposConta;

    private long numeroVenda;

    private Boolean cancelada;

    @Override
    public void setParameters(Object[] params) {
        this.classeGenerica = (Class<T>)params[0];
    }

    @Override
    public void createFilter() {
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        adicionarFrom();
        adicionarCliente();
        adicionarVendedor();
        adicionarEmissao();
        adicionarVencimento();
        adicionarTipoConta();
        adicionarNumeroVenda();
        adicionarCancelada();
        adicionarOrderBy();
    }

    /**
     * Executar a pesquisa genérica conforme a parametrização
     * feita nos atributos da classe
     * 
     * @return Lista de um determinado objeto (Venda ou Oraçamento ou Nota Fiscal)
     */
    public Collection<T> executeFilter() {
        createFilter();
        log.info("Executar filtro " + filterDescription.toString());
        return (Collection<T>)executeQuery(filterDescription.toString(), (Object[][])parameters.toArray(new Object[parameters.size()][2]));
    }

    /**
     * Parte FROM da consulta
     */
    private void adicionarFrom() {
        if (classeGenerica.isInstance(new Venda())) {
            filterDescription.append("SELECT v FROM com.github.andrepenteado.contest.entity.Venda v ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new NotaFiscal())) {
            filterDescription.append("SELECT nf FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new Orcamento())) {
            filterDescription.append("SELECT o FROM com.github.andrepenteado.contest.entity.Orcamento o ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new Comissao())) {
            filterDescription.append("SELECT c FROM com.github.andrepenteado.contest.entity.Comissao c ");
            filterDescription.append("WHERE 1 = 1 ");
        }
        else if (classeGenerica.isInstance(new Receber())) {
            filterDescription.append("SELECT r FROM com.github.andrepenteado.contest.entity.Receber r ");
            filterDescription.append("WHERE (r.recebido IS NULL OR r.recebido = FALSE) "); // Campo automático, atualizado por trigger
            /*filterDescription.append("WHERE r.id IN ( "); // Ter recebido parte (deve outra parte)
            filterDescription.append("   SELECT rb1.receber.id ");
            filterDescription.append("   FROM com.github.andrepenteado.contest.entity.Recebido rb1 ");
            filterDescription.append("   GROUP BY rb1.receber.id, rb1.receber.valor ");
            filterDescription.append("   HAVING SUM(rb1.valorPago) < rb1.receber.valor ");
            filterDescription.append(") ");
            filterDescription.append("OR r.id IN ( "); // Não ter pago nada
            filterDescription.append("   SELECT r2.id ");
            filterDescription.append("   FROM com.github.andrepenteado.contest.entity.Receber r2 ");
            filterDescription.append("   WHERE NOT EXISTS ELEMENTS(r2.recebimentos) ");
            filterDescription.append(") ");*/
        }
        else if (classeGenerica.isInstance(new Recebido())) {
            filterDescription.append("SELECT r FROM com.github.andrepenteado.contest.entity.Recebido r WHERE 1 = 1 ");
        }
    }

    /**
     * Parte do filtro por cliente da consulta
     */
    private void adicionarCliente() {
        if (idCliente > 0) {
            if (classeGenerica.isInstance(new Venda())) {
                filterDescription.append("AND v.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
            else if (classeGenerica.isInstance(new NotaFiscal())) {
                filterDescription.append("AND nf.venda.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
            else if (classeGenerica.isInstance(new Orcamento())) {
                filterDescription.append("AND o.venda.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
            else if (classeGenerica.isInstance(new Comissao())) {
                filterDescription.append("AND c.venda.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
            else if (classeGenerica.isInstance(new Receber())) {
                filterDescription.append("AND r.venda.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
            else if (classeGenerica.isInstance(new Recebido())) {
                filterDescription.append("AND r.receber.venda.cliente.id = :pIdCliente ");
                parameters.add(new Object[] { "pIdCliente", idCliente });
            }
        }
    }

    /**
     * Parte do filtro por cliente da consulta
     */
    private void adicionarVendedor() {
        if (idVendedor > 0) {
            if (classeGenerica.isInstance(new Venda())) {
                filterDescription.append("AND v.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
            else if (classeGenerica.isInstance(new NotaFiscal())) {
                filterDescription.append("AND nf.venda.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
            else if (classeGenerica.isInstance(new Orcamento())) {
                filterDescription.append("AND o.venda.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
            else if (classeGenerica.isInstance(new Comissao())) {
                filterDescription.append("AND c.venda.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
            else if (classeGenerica.isInstance(new Receber())) {
                filterDescription.append("AND r.venda.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
            else if (classeGenerica.isInstance(new Recebido())) {
                filterDescription.append("AND r.receber.venda.funcionario.id = :pIdVendedor ");
                parameters.add(new Object[] { "pIdVendedor", idVendedor });
            }
        }
    }

    /**
     * Parte do filtro por emissão da consulta
     */
    private void adicionarEmissao() {
        if (classeGenerica.isInstance(new Venda())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   v.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) OR v.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   v.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) OR v.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoFinal", dataEmissaoFinal });
            }
        }
        else if (classeGenerica.isInstance(new NotaFiscal())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND nf.emissao >= :pEmissaoInicial ");
                parameters.add(new Object[] { "pEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND nf.emissao <= :pEmissaoFinal ");
                parameters.add(new Object[] { "pEmissaoFinal", dataEmissaoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Orcamento())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND o.emissao >= :pEmissaoInicial ");
                parameters.add(new Object[] { "pEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND o.emissao <= :pEmissaoFinal ");
                parameters.add(new Object[] { "pEmissaoFinal", dataEmissaoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Comissao())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   c.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) OR c.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   c.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) OR c.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoFinal", dataEmissaoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Receber())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) OR r.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) OR r.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoFinal", dataEmissaoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Recebido())) {
            if (dataEmissaoInicial != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) OR r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao >= :pDataEmissaoInicial ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoInicial", dataEmissaoInicial });
            }
            if (dataEmissaoFinal != null) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) OR r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.emissao <= :pDataEmissaoFinal ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataEmissaoFinal", dataEmissaoFinal });
            }
        }
    }

    /**
     * Parte do filtro por vencimento da consulta
     */
    private void adicionarVencimento() {
        if (classeGenerica.isInstance(new Venda())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND v.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento >= :pDataVencimentoInicial ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND v.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento <= :pDataVencimentoFinal ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new NotaFiscal())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND nf.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento >= :pDataVencimentoInicial ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND nf.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento <= :pDataVencimentoFinal ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Orcamento())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND o.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento >= :pDataVencimentoInicial ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND o.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento <= :pDataVencimentoFinal ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Comissao())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND c.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento >= :pDataVencimentoInicial ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND c.venda.id IN ( ");
                filterDescription.append("   SELECT r.venda.id FROM com.github.andrepenteado.contest.entity.Receber r ");
                filterDescription.append("   WHERE r.vencimento <= :pDataVencimentoFinal ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Receber())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND r.vencimento >= :pDataVencimentoInicial ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND r.vencimento <= :pDataVencimentoFinal ");
                parameters.add(new Object[] { "pDataVencimentoFinal", dataVencimentoFinal });
            }
        }
        else if (classeGenerica.isInstance(new Recebido())) {
            if (dataVencimentoInicial != null) {
                filterDescription.append("AND r.receber.vencimento >= :pDataVencimentoInicial ");
                parameters.add(new Object[] { "pDataVencimentoInicial", dataVencimentoInicial });
            }
            if (dataVencimentoFinal != null) {
                filterDescription.append("AND r.receber.vencimento <= :pDataVencimentoFinal ");
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
            if (classeGenerica.isInstance(new Receber())) {
                filterDescription.append("AND r.tipoConta.id IN (").append(ids).append(") ");
            }
            else if (classeGenerica.isInstance(new Recebido())) {
                filterDescription.append("AND r.receber.tipoConta.id IN (").append(ids).append(") ");
            }
        }
    }

    /**
     * Parte do filtro por numero da consulta
     */
    private void adicionarNumeroVenda() {
        if (numeroVenda > 0) {
            if (classeGenerica.isInstance(new Venda())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   v.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.numero = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR v.id IN (");
                filterDescription.append("      SELECT o.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.id = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR v.pedido = :pNumeroVenda ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
            }
            else if (classeGenerica.isInstance(new NotaFiscal())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   nf.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.id = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR nf.venda.pedido = :pNumeroVenda ");
                filterDescription.append("   OR nf.numero = :pNumeroVenda ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
            }
            else if (classeGenerica.isInstance(new Orcamento())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   o.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.numero = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR o.venda.pedido = :pNumeroVenda ");
                filterDescription.append("   OR o.id = :pNumeroVenda ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
            }
            else if (classeGenerica.isInstance(new Comissao())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   c.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.numero = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR c.venda.id IN (");
                filterDescription.append("      SELECT o.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.id = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR c.venda.pedido = :pNumeroVenda ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
            }
            else if (classeGenerica.isInstance(new Receber())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.numero = :pNumeroVenda ");
                filterDescription.append("   ) OR ");
                filterDescription.append("   r.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.id = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR r.venda.pedido = :pNumeroVenda ");
                filterDescription.append("   OR r.numeroDocumento = :pNumeroDocumento ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
                parameters.add(new Object[] { "pNumeroDocumento", Long.toString(numeroVenda) });
            }
            else if (classeGenerica.isInstance(new Recebido())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.numero = :pNumeroVenda ");
                filterDescription.append("   ) OR ");
                filterDescription.append("   r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT o.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.Orcamento o ");
                filterDescription.append("      WHERE o.id = :pNumeroVenda ");
                filterDescription.append("   ) ");
                filterDescription.append("   OR r.receber.venda.pedido = :pNumeroVenda ");
                filterDescription.append("   OR r.receber.numeroDocumento = :pNumeroDocumento ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pNumeroVenda", numeroVenda });
                parameters.add(new Object[] { "pNumeroDocumento", Long.toString(numeroVenda) });
            }
        }
    }

    /**
     * Parte do filtro por numero da consulta
     */
    private void adicionarCancelada() {
        if (cancelada != null && cancelada) {
            if (classeGenerica.isInstance(new Venda())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   v.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.cancelada = :pCancelada ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pCancelada", cancelada });
            }
            else if (classeGenerica.isInstance(new NotaFiscal())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   nf.cancelada = :pCancelada ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pCancelada", cancelada });
            }
            else if (classeGenerica.isInstance(new Comissao())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   c.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.cancelada = :pCancelada ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pCancelada", cancelada });
            }
            else if (classeGenerica.isInstance(new Receber())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.cancelada = :pCancelada ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pCancelada", cancelada });
            }
            else if (classeGenerica.isInstance(new Recebido())) {
                filterDescription.append("AND ( ");
                filterDescription.append("   r.receber.venda.id IN ( ");
                filterDescription.append("      SELECT nf.venda.id ");
                filterDescription.append("      FROM com.github.andrepenteado.contest.entity.NotaFiscal nf ");
                filterDescription.append("      WHERE nf.cancelada = :pCancelada ");
                filterDescription.append("   ) ");
                filterDescription.append(") ");
                parameters.add(new Object[] { "pCancelada", cancelada });
            }
        }
    }

    /**
     * Incluir a ordenação da pesquisa
     */
    private void adicionarOrderBy() {
        if (classeGenerica.isInstance(new Venda())) {
            filterDescription.append("ORDER BY v.dataLancamento DESC");
        }
        else if (classeGenerica.isInstance(new NotaFiscal())) {
            filterDescription.append("ORDER BY nf.emissao DESC");
        }
        else if (classeGenerica.isInstance(new Orcamento())) {
            filterDescription.append("ORDER BY o.emissao DESC");
        }
        else if (classeGenerica.isInstance(new Comissao())) {
            filterDescription.append("ORDER BY c.venda.dataLancamento DESC");
        }
        else if (classeGenerica.isInstance(new Receber())) {
            filterDescription.append("ORDER BY r.vencimento DESC");
        }
        else if (classeGenerica.isInstance(new Recebido())) {
            filterDescription.append("ORDER BY r.dataPagamento DESC");
        }
    }

    /**
     * @param idCliente idCliente a ser atribuido
     */
    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @param idVendedor idVendedor a ser atribuido
     */
    public void setIdVendedor(long idVendedor) {
        this.idVendedor = idVendedor;
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
     * @param numeroVenda numeroVenda a ser atribuido
     */
    public void setNumeroVenda(long numeroVenda) {
        this.numeroVenda = numeroVenda;
    }

    /**
     * @param cancelada cancelada a ser atribuido
     */
    public void setCancelada(Boolean cancelada) {
        this.cancelada = cancelada;
    }
}
