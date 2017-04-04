
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

/**
 * @author Andre Penteado
 * @since 18/09/2007 - 23:31:39
 */
@Entity
@Table(name = "configuracao")
public class Configuracao extends BasicEntity<Configuracao> implements Serializable {

    public Configuracao() {
        super(Configuracao.class);
    }

    public Configuracao(Long id) {
        super(Configuracao.class, id);
    }

    private static final long serialVersionUID = -4070642834050663561L;

    @Id
    @Column(name = "id_empresa")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_empresa", insertable = false, updatable = false)
    private Empresa empresa;

    @Column(name = "situacao_nfe")
    private Integer situacaoNfe;

    @Column(name = "versao_emissor_nfe")
    private String versaoEmissorNfe;

    @Column(name = "versao_leiaute_nfe")
    private String versaoLeiauteNfe;

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
     * @return empresa atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return situacaoNfe atribuido
     */
    public Integer getSituacaoNfe() {
        return situacaoNfe;
    }

    /**
     * @param situacaoNfe situacaoNfe a ser atribuido
     */
    public void setSituacaoNfe(Integer situacaoNfe) {
        this.situacaoNfe = situacaoNfe;
    }

    /**
     * @return versaoEmissorNfe atribuido
     */
    public String getVersaoEmissorNfe() {
        return versaoEmissorNfe;
    }

    /**
     * @param versaoEmissorNfe versaoEmissorNfe a ser atribuido
     */
    public void setVersaoEmissorNfe(String versaoEmissorNfe) {
        this.versaoEmissorNfe = versaoEmissorNfe;
    }

    /**
     * @param versaoLeiauteNfe versaoLeiauteNfe a ser atribuido
     */
    public void setVersaoLeiauteNfe(String versaoLeiauteNfe) {
        this.versaoLeiauteNfe = versaoLeiauteNfe;
    }

    /**
     * @return versaoLeiauteNfe atribuido
     */
    public String getVersaoLeiauteNfe() {
        return versaoLeiauteNfe;
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
        Configuracao other = (Configuracao)obj;
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
        builder.append("Configuracao [empresa=");
        builder.append(empresa);
        builder.append("]");
        return builder.toString();
    }
}
