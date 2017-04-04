
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.andrepenteado.contest.entity.valueObject.FormaPagamento;
import com.github.andrepenteado.core.BasicEntity;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * Classe de entidade Recebido
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "recebido")
public class Recebido extends BasicEntity<Recebido> implements Serializable {

    public Recebido() {
        super(Recebido.class);
    }

    public Recebido(Long id) {
        super(Recebido.class, id);
    }

    private static final long serialVersionUID = 4809819365110774747L;

    @Id
    @SequenceGenerator(name = "recebido_id_seq", sequenceName = "recebido_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recebido_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_pagamento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "valor_pago", nullable = false)
    private Double valorPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "id_receber", nullable = false)
    private Receber receber;

    @ManyToOne
    @JoinColumn(name = "id_caixa")
    private Caixa caixa;

    /**
     * @param id id a ser atribuido
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return id atribuido
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Define o vencimento deste Receber.
     * @return o vencimento
     */
    public Date getDataPagamento() {
        return this.dataPagamento;
    }

    public String getStringDataPagamento() {
        return FunctionsHelper.dateFormat(getDataPagamento(), "dd/MM/yyyy");
    }

    /**
     * Define o vencimento deste Receber para o valor especificado.
     * @param vencimento o novo vencimento
     */
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    /**
     * Define o valor deste Receber.
     * @return o valor
     */
    public Double getValorPago() {
        return this.valorPago;
    }

    /**
     * Define o valor deste Receber para o valor especificado.
     * @param valor o novo valor
     */
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    /**
     * @return observacao atribuido
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao observacao a ser atribuido
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * Define o venda deste Receber.
     * @return o venda
     */
    public Receber getReceber() {
        return this.receber;
    }

    /**
     * Define o venda deste Receber para o valor especificado.
     * @param venda o novo venda
     */
    public void setReceber(Receber receber) {
        this.receber = receber;
    }

    /**
     * @param parcela parcela a ser atribuido
     */
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    /**
     * @return parcela atribuido
     */
    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public Caixa getCaixa() {
        return this.caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Recebido other = (Recebido)obj;
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
        builder.append("Recebido [");
        if (dataPagamento != null) {
            builder.append("dataPagamento=");
            builder.append(dataPagamento);
            builder.append(", ");
        }
        if (receber != null) {
            builder.append("receber=");
            builder.append(receber);
        }
        builder.append("]");
        return builder.toString();
    }

}