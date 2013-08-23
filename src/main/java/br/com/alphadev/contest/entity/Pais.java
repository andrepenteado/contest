
package br.com.alphadev.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade Pais
 * @author André Penteado
 * @since 3 de Maio de 2007, 11:17
 */
@Entity
@Table(name = "pais")
public class Pais extends BasicEntity<Pais> implements Serializable {

    public Pais() {
        super(Pais.class);
    }

    public Pais(Long id) {
        super(Pais.class, id);
    }

    private static final long serialVersionUID = 5028860121451315871L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sigla")
    private String sigla;

    /**
     * Define o id deste Pais.
     * @return o id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Pais para o valor especificado.
     * @param id o novo id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Pais.
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Pais para o valor especificado.
     * @param nome o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o sigla deste Pais.
     * @return o sigla
     */
    public String getSigla() {
        return this.sigla;
    }

    /**
     * Define o sigla deste Pais para o valor especificado.
     * @param sigla o novo sigla
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
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
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais)object;
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
        builder.append("Pais [");
        if (nome != null) {
            builder.append("nome=");
            builder.append(nome);
        }
        builder.append("]");
        return builder.toString();
    }
}
