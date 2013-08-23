
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
 * @author André Penteado
 * @since 03/05/2011 - 11:25:00
 */
@Entity
@Table(name = "cfop")
public class CFOP extends BasicEntity<CFOP> implements Serializable {

    private static final long serialVersionUID = 3455562940983241751L;

    @Id
    @SequenceGenerator(name = "cfop_id_seq", sequenceName = "cfop_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfop_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "aplicacao")
    private String aplicacao;

    @Column(name = "substituicao_tributaria")
    private Boolean substituicaoTributaria;

    public CFOP() {
        super(CFOP.class);
    }

    public CFOP(Long id) {
        super(CFOP.class, id);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the aplicacao
     */
    public String getAplicacao() {
        return aplicacao;
    }

    /**
     * @param aplicacao the aplicacao to set
     */
    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    /**
     * @param substituicaoTributaria the substituicaoTributaria to set
     */
    public void setSubstituicaoTributaria(boolean substituicaoTributaria) {
        this.substituicaoTributaria = substituicaoTributaria;
    }

    /**
     * @return the substituicaoTributaria
     */
    public boolean isSubstituicaoTributaria() {
        return substituicaoTributaria;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
        CFOP other = (CFOP)obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        }
        else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CFOP [codigo=" + codigo + "]";
    }
}
