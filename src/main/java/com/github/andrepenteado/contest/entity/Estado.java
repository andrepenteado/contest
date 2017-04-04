
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade Estado
 * @author André Penteado
 * @since 3 de Maio de 2007, 11:17
 */
@Entity
@Table(name = "estado")
public class Estado extends BasicEntity<Estado> implements Serializable {

    public Estado() {
        super(Estado.class);
    }

    public Estado(Long id) {
        super(Estado.class, id);
    }

    private static final long serialVersionUID = -6730719503656934127L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sigla", nullable = false)
    private String sigla;

    @Column(name = "codigo_ibge")
    private String codigoIbge;

    @JoinColumn(name = "id_pais", referencedColumnName = "id")
    @ManyToOne
    private Pais pais;

    /**
     * Define o id deste Estado.
     * @return o id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Estado para o valor especificado.
     * @param id o novo id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Estado.
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Estado para o valor especificado.
     * @param nome o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o sigla deste Estado.
     * @return o sigla
     */
    public String getSigla() {
        return this.sigla;
    }

    /**
     * Define o sigla deste Estado para o valor especificado.
     * @param sigla o novo sigla
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * @param codigoIbge codigoIbge a ser atribuido
     */
    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    /**
     * @return codigoIbge atribuido
     */
    public String getCodigoIbge() {
        return codigoIbge;
    }

    /**
     * Define o pais deste Estado.
     * @return o pais
     */
    public Pais getPais() {
        return this.pais;
    }

    /**
     * Define o pais deste Estado para o valor especificado.
     * @param pais o novo pais
     */
    public void setPais(Pais pais) {
        this.pais = pais;
    }

    /**
     * Retorna um valor de código hash para o objeto.  Esta implementação computa
     * um valor de código hash baseado nos campos id deste objeto.
     * @return um valor de código hash para este objeto.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Determina se outro objeto é igual a este ContatoEntidade.  O resultado é
     * <code>true</code> se e somente se o argumento não for nulo e for um objeto ContatoEntidade o qual
     * tem o mesmo valor para o campo id como este objeto.
     * @param object o objeto de referência com o qual comparar
     * @return <code>true</code> se este objeto é o mesmo como o argumento;
     * <code>false</code> caso contrário.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado)object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Estado [");
        if (sigla != null) {
            builder.append("sigla=");
            builder.append(sigla);
        }
        builder.append("]");
        return builder.toString();
    }

}
