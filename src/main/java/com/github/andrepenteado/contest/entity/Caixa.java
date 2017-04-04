
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.andrepenteado.core.BasicEntity;

/**
 * @author André Penteado
 * @since 28/04/2005 (19:57:32)
 */
@Entity
@Table(name = "caixa")
public class Caixa extends BasicEntity<Caixa> implements Serializable {

    public Caixa() {
        super(Caixa.class);
    }

    public Caixa(Long id) {
        super(Caixa.class, id);
    }

    private static final long serialVersionUID = 5961138523856572902L;

    @Id
    @SequenceGenerator(name = "caixa_id_seq", sequenceName = "caixa_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caixa_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_abertura")
    @Temporal(TemporalType.DATE)
    private Date dataAbertura;

    @Column(name = "hora_abertura")
    private String horaAbertura;

    @Column(name = "valor_abertura")
    private Double valorAbertura;

    @Column(name = "data_fechamento")
    @Temporal(TemporalType.DATE)
    private Date dataFechamento;

    @Column(name = "hora_fechamento")
    private String horaFechamento;

    @Column(name = "valor_fechamento")
    private Double valorFechamento;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    /**
     * @return Retorna o dataAbertura.
     */
    public Date getDataAbertura() {
        return dataAbertura;
    }

    /**
     * @return Retorna o dataFechamento.
     */
    public Date getDataFechamento() {
        return dataFechamento;
    }

    /**
     * @return Retorna o horaAbertura.
     */
    public String getHoraAbertura() {
        return horaAbertura;
    }

    /**
     * @return Retorna o horaFechamento.
     */
    public String getHoraFechamento() {
        return horaFechamento;
    }

    /**
     * @return Retorna o id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return Retorna o valorAbertura.
     */
    public Double getValorAbertura() {
        return valorAbertura;
    }

    /**
     * @return Retorna o valorFechamento.
     */
    public Double getValorFechamento() {
        return valorFechamento;
    }

    /**
     * @param dataAbertura O dataAbertura a ser definido.
     */
    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * @param dataFechamento O dataFechamento a ser definido.
     */
    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    /**
     * @param horaAbertura O horaAbertura a ser definido.
     */
    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    /**
     * @param horaFechamento O horaFechamento a ser definido.
     */
    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    /**
     * @param id O id a ser definido.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param valorAbertura O valorAbertura a ser definido.
     */
    public void setValorAbertura(Double valorAbertura) {
        this.valorAbertura = valorAbertura;
    }

    /**
     * @param valorFechamento O valorFechamento a ser definido.
     */
    public void setValorFechamento(Double valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return empresa atribuido
     */
    public Funcionario getFuncionario() {
        return funcionario;
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
        final Caixa other = (Caixa)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Caixa [");
        if (dataAbertura != null) {
            builder.append("dataAbertura=");
            builder.append(dataAbertura);
            builder.append(", ");
        }
        if (funcionario != null) {
            builder.append("funcionario=");
            builder.append(funcionario);
        }
        builder.append("]");
        return builder.toString();
    }
}
