
package br.com.alphadev.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade PrazoPagamento
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "prazo_pagamento")
public class PrazoPagamento extends BasicEntity<PrazoPagamento> implements Serializable {

    public PrazoPagamento() {
        super(PrazoPagamento.class);
    }

    public PrazoPagamento(Long id) {
        super(PrazoPagamento.class, id);
    }

    private static final long serialVersionUID = 8651089613969860874L;

    @Id
    @SequenceGenerator(name = "prazo_pagamento_id_seq", sequenceName = "prazo_pagamento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prazo_pagamento_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "parcelas")
    private String parcelas;

    @Column(name = "desconto_juros")
    private Double descontoJuros;

    @Column(name = "observacao")
    private String observacao;

    /**
     * Define o id deste PrazoPagamento.
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste PrazoPagamento para o valor especificado.
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o descricao deste PrazoPagamento.
     * @return o descricao
     */
    public String getDescricao() {
        return this.descricao;
    }

    /**
     * Define o descricao deste PrazoPagamento para o valor especificado.
     * @param descricao o novo descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Define o parcelas deste PrazoPagamento.
     * @return o parcelas
     */
    public String getParcelas() {
        return this.parcelas;
    }

    /**
     * Define o parcelas deste PrazoPagamento para o valor especificado.
     * @param parcelas o novo parcelas
     */
    public void setParcelas(String parcelas) {
        this.parcelas = parcelas;
    }

    /**
     * @param descontoJuros descontoJuros a ser atribuido
     */
    public void setDescontoJuros(Double descontoJuros) {
        this.descontoJuros = descontoJuros;
    }

    /**
     * @return descontoJuros atribuido
     */
    public Double getDescontoJuros() {
        return descontoJuros;
    }

    /**
     * Define o observacao deste PrazoPagamento.
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste PrazoPagamento para o valor especificado.
     * @param observacao o novo observacao
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        final PrazoPagamento other = (PrazoPagamento)obj;
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
        builder.append("PrazoPagamento [");
        if (descricao != null) {
            builder.append("descricao=");
            builder.append(descricao);
        }
        builder.append("]");
        return builder.toString();
    }
}
