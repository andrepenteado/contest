
package br.com.alphadev.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade Cidade
 * @author André Penteado
 * @since 3 de Maio de 2007, 11:17
 */
@Entity
@Table(name = "cidade")
public class Cidade extends BasicEntity<Cidade> implements Serializable {

    public Cidade() {
        super(Cidade.class);
    }

    public Cidade(Long id) {
        super(Cidade.class, id);
    }

    private static final long serialVersionUID = -9064974655311851352L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_ibge")
    private String codigoIbge;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais pais;

    /**
     * Define o id deste Cidade.
     * @return o id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Cidade para o valor especificado.
     * @param id o novo id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Cidade.
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Cidade para o valor especificado.
     * @param nome o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * Define o estado deste Cidade.
     * @return o estado
     */
    public Estado getEstado() {
        return this.estado;
    }

    /**
     * Define o estado deste Cidade para o valor especificado.
     * @param estado o novo estado
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Define o pais deste Cidade.
     * @return o pais
     */
    public Pais getPais() {
        if (this.estado != null && this.estado.getPais() != null)
            return this.estado.getPais();
        return this.pais;
    }

    /**
     * Define o pais deste Cidade para o valor especificado.
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
        if (!(object instanceof Cidade)) {
            return false;
        }
        Cidade other = (Cidade)object;
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
        builder.append("Cidade [");
        if (estado != null) {
            builder.append("estado=");
            builder.append(estado);
            builder.append(", ");
        }
        if (nome != null) {
            builder.append("nome=");
            builder.append(nome);
        }
        builder.append("]");
        return builder.toString();
    }
}
