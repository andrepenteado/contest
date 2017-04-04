
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.andrepenteado.core.BasicEntity;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 15/04/2009 - 18:56:54
 */
@Entity
@Table(name = "comissao")
public class Comissao extends BasicEntity<Comissao> implements Serializable {

    public Comissao() {
        super(Comissao.class);
    }

    public Comissao(Long id) {
        super(Comissao.class, id);
    }

    private static final long serialVersionUID = 7137257763677104519L;

    @Id
    @SequenceGenerator(name = "comissao_id_seq", sequenceName = "comissao_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comissao_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "data_pagamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @Column(name = "observacao")
    private String observacao;

    @OneToOne
    @JoinColumn(name = "id_venda")
    private Venda venda;

    /**
     * @return id atribuido
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id id a ser atribuido
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param descricao descricao a ser atribuido
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return descricao atribuido
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return valor atribuido
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor valor a ser atribuido
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getStringPorcentagem() {
        if (getValor() == null)
            return "0.00";
        String result = FunctionsHelper.numberFormat((getValor().doubleValue() / getVenda().getValorTotal()) * 100, "##0.##");
        if (result == null)
            return "0.00";
        return result;
    }

    /**
     * @return dataPagamento atribuido
     */
    public Date getDataPagamento() {
        return dataPagamento;
    }

    /**
     * @param dataPagamento dataPagamento a ser atribuido
     */
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStringDataPagamento() {
        return FunctionsHelper.dateFormat(getDataPagamento(), "dd/MM/yyyy");
    }

    /**
     * @param observacao observacao a ser atribuido
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return observacao atribuido
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param venda venda a ser atribuido
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    /**
     * @return venda atribuido
     */
    public Venda getVenda() {
        return venda;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comissao other = (Comissao)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Comissao [");
        if (venda != null) {
            builder.append("venda=");
            builder.append(venda);
        }
        builder.append("]");
        return builder.toString();
    }
}
